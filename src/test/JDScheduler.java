package test;

import java.io.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.net.InetAddress;

public class JDScheduler {

  public final static int SCHEDULE_COLUMN_COUNT = 4;
  public final static int MAX_ACTION_LENGTH = 60;
  public final static int MAX_OUTPUTFILE_LENGTH = 60;
  public final static String SCHEDULE_DEFINITION = "(PRIORITY INTEGER, ADDED_TS TIMESTAMP, INITIALS VARCHAR(10), ACTION VARCHAR("
      + MAX_ACTION_LENGTH + "))";
  // Fix: ALTER TABLE JDTESTINFO.SCHED1 ALTER COLUMN ACTION SET DATA TYPE
  // VARCHAR(60)
  public final static String SCHEDULE_ORDERING = " ORDER BY PRIORITY,ADDED_TS";
  public final static int RUN_COLUMN_COUNT = 5;
  public final static String RUN_DEFINITION = "(PRIORITY INTEGER, " + "ADDED_TS TIMESTAMP, " + "INITIALS VARCHAR(10),"
      + " ACTION VARCHAR(" + MAX_ACTION_LENGTH + "), " + "STARTED_TS TIMESTAMP, " + "OUTPUTFILE VARCHAR("
      + MAX_OUTPUTFILE_LENGTH + "))";
  // Fix: ALTER TABLE JDTESTINFO.SCRUN1 ALTER COLUMN ACTION SET DATA TYPE
  // VARCHAR(60)
  public static final int PRIORITY_RERUNFAILED_TC = 10;
  public static final int PRIORITY_DIFFERENCE_RERUNFAILED = 10;
  public static final int PRIORITY_REGRESSION_TC = 20;

  public final static int STAT_COLUMN_COUNT = 6;
  public final static String STAT_DEFINITION = "(INITIALS VARCHAR(10), ACTION VARCHAR(" + MAX_ACTION_LENGTH
      + "), COUNT INT, AVERAGE_SECONDS DOUBLE, RECENT_AVERAGE_SECONDS DOUBLE, LAST_SECONDS DOUBLE, LAST_TS TIMESTAMP)";
  // Fix: ALTER TABLE JDTESTINFO.SCSTA1 ALTER COLUMN ACTION SET DATA TYPE
  // VARCHAR(60)

  public static String COLLECTION = "JDTESTINFO";

  public static Hashtable threadConnectionHashtable = new Hashtable();
  // Default is to connect to local host.
  // Can be changed via -DjdbcUrl
  public static String jdbcURL = "jdbc:db2:*LOCAL";
  // Default is to connect with current id
  // These are read from the ini/.netrc file
  // But can be overriden using -Duserid or -Dpassword
  public static String system = "localhost";
  public static String userid = null;
  public static String password = null;

  // TestUserid and password read from the ini/.netrcfile
  public static String testUserid = null;
  public static String testPassword = null;

  public static String adminUserid = null;
  public static String adminPassword = null;

  public static String defaultId = "1";

  static {
    try {

      // Always load the ini files
      Properties iniProperties = new Properties();
      InputStream fileInputStream = JDRunit.loadResource("ini/netrc.ini", null);
      iniProperties.load(fileInputStream);
      fileInputStream.close();

      fileInputStream = JDRunit.loadResource("ini/systems.ini", null);
      iniProperties.load(fileInputStream);
      fileInputStream.close();

      // Set defaults for non-400 platform
      if (!JTOpenTestEnvironment.isOS400) {

        String localHost = JDHostName.getHostName().toLowerCase();
        int dotIndex = localHost.indexOf(".");
        if (dotIndex >= 0) {
          localHost = localHost.substring(0, dotIndex);
        }
        defaultId = getConfigProperty(iniProperties, "SCHEDULERID_" + localHost);
        if (defaultId == null) {
          defaultId = "1";
          throw new Exception("ID for SCHEDULERID_" + localHost + " not found in ini/systems.ini");
        }
        system = getConfigProperty(iniProperties, "SCHEDULERDB_" + localHost);
        if (system == null) {
          throw new Exception("DB for SCHEDULERDB_" + localHost + " not found in ini/systems.ini");
        }

        jdbcURL = "jdbc:as400:" + system;
        userid = getConfigProperty(iniProperties, "USERID");
        password = getConfigProperty(iniProperties, userid + ".password");
        if (password == null) {
          password = getConfigProperty(iniProperties, "PASSWORD");
          char[] encryptedPassword = PasswordVault.getEncryptedPassword(password);
          password = PasswordVault.decryptPasswordLeak(encryptedPassword);
        }
      }
      adminUserid = getConfigProperty(iniProperties, "USERID");
      adminPassword = getConfigProperty(iniProperties, adminUserid + ".password");
      if (adminPassword == null)
        adminPassword = getConfigProperty(iniProperties, "PASSWORD");

      testUserid = getConfigProperty(iniProperties, "TESTUSERID");
      testPassword = getConfigProperty(iniProperties, testUserid + ".password");
      if (testPassword == null)
        testPassword = getConfigProperty(iniProperties, "TESTPASSWORD");

      String newValue;
      newValue = System.getProperty("jdbcURL");
      if (newValue != null)
        jdbcURL = newValue;

      newValue = System.getProperty("userid");
      if (newValue != null)
        userid = newValue;

      newValue = System.getProperty("password");
      if (newValue != null)
        password = newValue;

    } catch (Exception e) {
      System.out.println("WARNING:  Setup Exception");
      e.printStackTrace(System.out);
    }
  }

  public static String[] usageInfo = { "java JDSchedule <ID> SERVER ",
      "java JDSchedule <ID> ADD <priority> <initials> <action>  -- Adds a new entry if it does not exist",
      "java JDSchedule <ID> REMOVE <initials> <action> ", "java JDSchedule <ID> LIST",
      "java JDSchedule <ID> STATS [<initials>] [<action>] ",

      "", "Priorities are ", "PRIORITY 0 -- Run ASAP -- Reserved for runtime usage",
      "PRIORITY 5 -- Run JOB  -- Used to preempt other jobs", "PRIORITY 10 -- RERUNFAILED submitted jobs",
      "PRIORITY 20 -- REGRESSION submitted jobs", "PRIORITY 30 -- REGRESSION submission",
      "PRIORITY 30 -- RERUNFAILED submission", "PRIORITY 30 -- REPORT      submission",
      "PRIORITY 30 -- EMAIL submission", "MUST RUN testcases from ini/mustrun.ini run with 1 less priority", };

  public static void usage() {
    for (int i = 0; i < usageInfo.length; i++) {
      System.out.println(usageInfo[i]);
    }
  }

  private static String getConfigProperty(Properties iniProperties, String property) {
    String propertyValue;
    propertyValue = java.lang.System.getProperty(property);
    if (propertyValue == null) {
      propertyValue = System.getenv(property);
      if (propertyValue == null) {
        propertyValue = (String) iniProperties.getProperty(property);
      }
    }
    return propertyValue;
  }

  public static void main(String args[]) {
    go(args);
  }

  public static void go(String args[]) {
    try {
      if (args.length < 2) {
        usage();
      } else {
        String id = args[0];
        if (Integer.parseInt(id) == 0) {
          id = defaultId;
        }
        String operation = args[1].toUpperCase();
        if (operation.equals("ADD")) {
          int priority = Integer.parseInt(args[2]);
          String initials = args[3];

          for (int i = 4; i < args.length; i++) {
            String action = args[i];
            add(System.out, id, priority, initials, action);
          }
        } else if (operation.equals("REMOVE")) {
          String initials = args[2];
          String action = null;
          if (args.length > 3) {
            action = args[3];
          }
          remove(System.out, id, initials, action);
        } else if (operation.equals("LIST")) {
          list(System.out, id);
        } else if (operation.equals("STATS")) {
          String initials = "%";
          if (args.length > 2) {
            initials = args[2];
          }

          String action = "%";
          if (args.length > 3) {
            action = args[3];
          }
          stats(System.out, id, initials, action);
        } else {
          System.out.println(" DID NOT understand id=" + id);
          usage();
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
      usage();
    }
  }

  public static void stats(PrintStream out, String id, String initials, String action) {
    try {
      boolean setInitialsParameter = false;
      boolean setActionParameter = false;

      verifyTables(id);
      String statTable = getStatisticsTableName(id);
      String query = "SELECT * FROM " + statTable + " WHERE ";
      if (initials.equals("%")) {
        query += "INITIALS LIKE '%' ";
      } else {
        query += "INITIALS = ? ";
        setInitialsParameter = true;
      }

      if (action.equals("%")) {
        query += " AND ACTION LIKE '%'";
      } else {
        query += " AND ACTION = ?";
        setActionParameter = true;
      }

      Connection c = getCurrentThreadJdbcConnection();
      PreparedStatement ps = c.prepareStatement(query);
      if (setInitialsParameter) {
        ps.setString(1, initials);
        if (setActionParameter) {
          ps.setString(2, action);
        }
      } else {
        if (setActionParameter) {
          ps.setString(1, action);
        }
      }

      ResultSet rs = ps.executeQuery();
      ResultSetMetaData rsmd = rs.getMetaData();
      for (int i = 0; i < STAT_COLUMN_COUNT; i++) {
        if (i > 0)
          out.print(",");
        out.print(rsmd.getColumnName(i + 1));
      }
      out.println();

      while (rs.next()) {
        for (int i = 0; i < STAT_COLUMN_COUNT; i++) {
          if (i > 0)
            out.print(",");
          out.print(rs.getString(i + 1));
        }
        out.println();
      }
      rs.close();

    } catch (Exception e) {
      out.println("Error getting stats");
      e.printStackTrace(out);
    }

  }

  public static void list(PrintStream out, String id) {
    try {

      String scheduleTable = getScheduleTableName(id);
      String runTable = getScheduleRunName(id);

      Connection c = getCurrentThreadJdbcConnection();
      verifyTables(id);
      Statement s = c.createStatement();
      ResultSet rs;
      rs = s.executeQuery("Select current timestamp from sysibm.sysdummy1");
      rs.next();
      out.println("----------------------------------------------------");
      out.println("LIST as of " + rs.getString(1));
      out.println("----------------------------------------------------");

      out.println("----------------------------------------------------");
      out.println("RUNNING ITEMS (" + runTable + ")");
      out.println("----------------------------------------------------");
      rs = s.executeQuery("select  A.PRIORITY,A.ADDED_TS,A.INITIALS,A.ACTION,A.STARTED_TS, "
          + " TIMESTAMPDIFF(2,CAST((CURRENT TIMESTAMP - A.STARTED_TS) AS CHAR(22)))  AS RUN_SECONDS, "
          + " CAST( B.RECENT_AVERAGE_SECONDS AS INTEGER) AS AVG_RUN_SECONDS " + " from " + runTable
          + " A LEFT OUTER JOIN JDTESTINFO.SCSTA1 B ON  A.INITIALS=B.INITIALS and A.ACTION=B.ACTION ");
      out.println(
          "PRI,                  ADDED_TS,  INIT, ACTION,                 STARTED_TS, RUN_SECONDS, AVG_RUN_SECONDS");
      while (rs.next()) {
        for (int i = 0; i < RUN_COLUMN_COUNT + 2; i++) {
          if (i > 0)
            out.print(", ");
          out.print(rs.getString(i + 1));
        }
        out.println();
      }

      out.println("----------------------------------------------------");
      out.println("SCHEDULED ITEMS (" + scheduleTable + ")");
      out.println("----------------------------------------------------");
      rs = s.executeQuery("select * from " + scheduleTable + SCHEDULE_ORDERING);
      while (rs.next()) {
        for (int i = 0; i < SCHEDULE_COLUMN_COUNT; i++) {
          if (i > 0)
            out.print(", ");
          out.print(rs.getString(i + 1));
        }
        out.println();
      }
      out.println("----------------------------------------------------");
      out.println("END");
      out.println("----------------------------------------------------");

    } catch (Exception e) {
      out.println("Error listing information");
      e.printStackTrace(out);
    }
  }

  protected static String getScheduleTableName(String id) {
    return COLLECTION + ".SCHED" + id;
  }

  protected static String getScheduleRunName(String id) {
    return COLLECTION + ".SCRUN" + id;
  }

  protected static String getStatisticsTableName(String id) {
    return COLLECTION + ".SCSTA" + id;
  }

  public static void remove(PrintStream out, String id, String initials, String action) {
    try {
      verifyTables(id);
      String scheduleTable = getScheduleTableName(id);
      Connection c = getCurrentThreadJdbcConnection();
      String condition = "=";
      if (initials.indexOf("%") >= 0) {
        condition = " like ";
      }
      String sql = "";
      PreparedStatement update;
      if (action == null) {
        sql = "DELETE from " + scheduleTable + " where INITIALS" + condition + "? ";
        update = c.prepareStatement(sql);
      } else {
        sql = "DELETE from " + scheduleTable + " where INITIALS" + condition + "? and ACTION=?";
        update = c.prepareStatement(sql);
        update.setString(2, action);

      }
      update.setString(1, initials);
      update.executeUpdate();
      int updateCount = update.getUpdateCount();
      out.println("Rows updated = " + updateCount);
      if (updateCount == 0)
        out.println(" .. sql was " + sql + " initials=" + initials + " action=" + action);
      update.close();

    } catch (Exception e) {
      out.println("Error in add ");
      e.printStackTrace(out);
    }
  }

  public static void add(PrintStream out, String id, int priority, String initials, String action) {
    try {

      // Handle the PLUS versions
      if (action.equals("REGRESSIONPLUS")) {
        add(out, id, priority, initials, "REGRESSION");
        add(out, id, priority, initials, "REPORT");
        add(out, id, priority, initials, "RERUNFAILED");
        add(out, id, priority, initials, "REPORT");
        add(out, id, priority, initials, "EMAIL");
        return;
      }

      if (action.equals("RERUNFAILEDPLUS")) {
        add(out, id, priority, initials, "RERUNFAILED");
        add(out, id, priority, initials, "REPORT");
        add(out, id, priority, initials, "EMAIL");
        return;
      }

      //
      // adjust the priority if needed
      //
      // System.out.println("Checkin isMustrun "+initials);
      if (JDReport.isMustRun(initials)) {
        if (priority > 0) {
          priority--;
        }
      }

      verifyTables(id);
      String scheduleTable = getScheduleTableName(id);
      Connection c = getCurrentThreadJdbcConnection();
      if (action.equals("REPORT") || (action.indexOf("java") == 0)) {
        // we allow duplicate report actions
        // we allow duplicate java actions (i.e. BSOAuthenticate / JTACleanupTx)
      } else {

        PreparedStatement query = c
            .prepareStatement("SELECT * from " + scheduleTable + " where PRIORITY=? AND INITIALS=? and ACTION=?");
        query.setInt(1, priority);
        query.setString(2, initials);
        query.setString(3, action);
        ResultSet rs = query.executeQuery();
        if (rs.next()) {
          query.close();
          out.println("Error:  Entry for " + priority + " " + initials + " " + action + " already exists in the queue");
          out.flush();
          return;
        }
        query.close();
      }

      PreparedStatement ps = c.prepareStatement("INSERT INTO " + scheduleTable + " VALUES(?,CURRENT TIMESTAMP,?,?)");
      ps.setInt(1, priority);
      ps.setString(2, initials);
      if (action.length() > MAX_ACTION_LENGTH) {
        action = action.substring(0, MAX_ACTION_LENGTH);
      }
      ps.setString(3, action);
      ps.executeUpdate();
      ps.close();
      out.println("Added " + priority + " " + initials + " " + action);
    } catch (Exception e) {
      out.println("Error in adding  " + id + "," + priority + "," + initials + "," + action);
      e.printStackTrace(out);
    }

  }

  /**
   * Implements a thread based pooled connection
   */

  public static Connection getCurrentThreadJdbcConnection() throws Exception {
    Thread currentThread = Thread.currentThread();
    Connection connection = (Connection) threadConnectionHashtable.get(currentThread);

    if (connection != null) {
      // Make sure the connection is good
      try {
        Statement s = connection.createStatement();
        s.executeQuery("select * from sysibm.sysdummy1");
        s.close();
      } catch (Exception e) {
        System.out.println("Warning " + currentThread + ": Connection not usable");
        e.printStackTrace(System.out);
        connection = null;
      }
    }

    if (connection == null) {
      if (jdbcURL.indexOf("db2") > 0) {
        Class.forName("com.ibm.db2.jdbc.app.DB2Driver");
      } else if (jdbcURL.indexOf("as400") > 0) {
        Class.forName("com.ibm.as400.access.AS400JDBCDriver");
      } else if (jdbcURL.indexOf("jtopenlite") > 0) {
        Class.forName("com.ibm.jtopenlite.database.jdbc.JDBCDriver");
      } else {
        throw new Exception("Unrecognized URL " + jdbcURL);
      }
      System.out.println("Thread " + currentThread + ": Requesting new connection at " + jdbcURL + " using " + userid
          + "," + password);
      if (userid == null) {
        connection = DriverManager.getConnection(jdbcURL + ";prompt = false");
      } else {
        connection = DriverManager.getConnection(jdbcURL + ";prompt = false", userid, password);
      }
      threadConnectionHashtable.put(currentThread, connection);
    }
    return connection;

  }

  public static void verifyTables(String id) throws Exception {

    Connection c = getCurrentThreadJdbcConnection();
    Statement s = c.createStatement();
    String scheduleTable = getScheduleTableName(id);
    String runTable = getScheduleRunName(id);
    String statTable = getStatisticsTableName(id);

    try {
      ResultSet rs = s.executeQuery("select * from " + scheduleTable + " fetch first 2 rows only");
      rs.close();

    } catch (SQLException e) {

      String message = e.toString().toUpperCase();
      if ((message.indexOf("NOT FOUND") >= 0)) {
        s.executeUpdate("CREATE TABLE " + scheduleTable + " " + SCHEDULE_DEFINITION);
      } else {

        System.out.println("Warning:  Unexpected exception ");
        e.printStackTrace();
      }

    }
    String sql = "NOT SET";
    try {
      sql = "select * from " + runTable + " fetch first 2 rows only";
      ResultSet rs = s.executeQuery(sql);
      rs.close();

      /* See if table has been upgraded */
      try {
        sql = "select STARTED_TS from " + runTable + " fetch first 2 rows only";
        rs = s.executeQuery(sql);
        rs.close();
      } catch (SQLException e) {
        String message = e.toString().toUpperCase();
        if ((message.indexOf("NOT FOUND") >= 0) || (message.indexOf("NOT IN") >= 0)) {
          /* Assume run table needs to be upgraded */
          sql = "ALTER TABLE " + runTable + " ADD COLUMN STARTED_TS TIMESTAMP";
          s.executeUpdate(sql);
          sql = "UPDATE " + runTable + " SET STARTED_TS = CURRENT TIMESTAMP";
          s.executeUpdate(sql);
        } else {
          System.out.println("Unexpected error sql=" + sql);
          e.printStackTrace();
        }
      }

      /* See if table has been upgraded */
      try {
        sql = "select OUTPUTFILE from " + runTable + " fetch first 2 rows only";
        rs = s.executeQuery(sql);
        rs.close();
      } catch (SQLException e) {
        String message = e.toString().toUpperCase();
        if ((message.indexOf("NOT FOUND") >= 0) || (message.indexOf("NOT IN") >= 0)) {
          /* Assume run table needs to be upgraded */
          sql = "ALTER TABLE " + runTable + " ADD COLUMN OUTPUTFILE  VARCHAR(" + MAX_OUTPUTFILE_LENGTH + ")";
          s.executeUpdate(sql);
        } else {
          System.out.println("Unexpected error sql=" + sql);
          e.printStackTrace();
        }
      }

    } catch (SQLException e) {
      String message = e.toString().toUpperCase();
      if ((message.indexOf("NOT FOUND") >= 0)) {
        sql = "CREATE TABLE " + runTable + " " + RUN_DEFINITION;
        s.executeUpdate(sql);
      } else {

        System.out.println("Warning:  Unexpected exception for sql=" + sql);
        e.printStackTrace();
      }
    }

    try {
      /* see if table exists */
      ResultSet rs = s.executeQuery("select * from " + statTable + " fetch first 2 rows only");
      rs.close();
      try {
        /* see if table needs to be altered */
        rs = s.executeQuery("select LAST_TS from " + statTable + " fetch first 2 rows only");
        rs.close();

      } catch (Exception e) {
        s.executeUpdate("ALTER TABLE " + statTable + "  ADD COLUMN LAST_TS TIMESTAMP   ");
      }

    } catch (SQLException e) {
      s.executeUpdate("CREATE TABLE " + statTable + " " + STAT_DEFINITION);
    }

    // Check to see if alter need to be done.
    checkActionLength(s, scheduleTable);
    checkActionLength(s, runTable);
    checkActionLength(s, statTable);

    s.close();
  }

  static void checkActionLength(Statement s, String table) {
    try {
      ResultSet rs = s.executeQuery("Select ACTION FROM " + table);
      ResultSetMetaData rsmd = rs.getMetaData();

      int columnSize = rsmd.getColumnDisplaySize(1);
      rs.close();
      if (columnSize < MAX_ACTION_LENGTH) {

        String sql = "ALTER TABLE " + table + " ALTER COLUMN ACTION SET DATA TYPE VARCHAR(" + MAX_ACTION_LENGTH + ")";
        s.executeUpdate(sql);
      }

    } catch (Exception e) {
      System.out.println("Warning:  Unexpected exception ");
      e.printStackTrace(System.out);

    }
  }
}
