///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SchedulerManager.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.servlet;

import java.net.InetAddress;

import java.io.*;
import java.util.*;
import java.sql.*;
import com.ibm.as400.access.*;

import test.JDGroupTestInfo;
import test.JDScheduler;
import test.JDTestcase;

/**
 * Provides support for managing the JDScheduler
 */
public class SchedulerManager extends BaseHandler {

  public static String jdbcURL = "jdbc:db2:*LOCAL";
  // Default is to connect with current id
  // Can be changed via -Duserid=
  // Can be changed via -Dpassword=
  public static String userid = null;
  // We do not worry about protecting passwords in memory in this application 
  public static String password =  null;
  public static String defaultId = "1";
  public static String localHost = "";
  public static String system = "";
  static {
    try {
      localHost = InetAddress.getLocalHost().getHostName().toLowerCase();

      int dotIndex = localHost.indexOf(".");
      if (dotIndex >= 0) {
        localHost = localHost.substring(0, dotIndex);
      }
    } catch (Exception e) {
      localHost = "UNKNOWN";
    }

    // Set defaults for non-400 platform
    String osName = System.getProperty("os.name");
    if (osName.indexOf("400") < 0) {
      try {

        defaultId = JDScheduler.getDefaultId(); 
        system   =  JDScheduler.getDefaultSystem(); 

        jdbcURL = "jdbc:as400:" + system;
        userid = JDScheduler.getAdminUserid(); 
        System.out.println("userid is "+userid); 
        password = JDScheduler.getAdminPassword(); 
      } catch (Exception e) {
        System.out.println("WARNING:  Exception on startup");
        e.printStackTrace(System.out);
      }
    } else {
      system = localHost;
    }

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

  }


  public static Connection connection = null;

  public static void checkConnection(StringBuffer sb) throws Exception {

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

      if (userid == null) {
        connection = DriverManager.getConnection(jdbcURL);
      } else {
        connection = DriverManager.getConnection(jdbcURL, userid, password);
      }

    }
    try {
	    Statement stmt = connection.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1"); 
	    rs.close(); 
	    stmt.close(); 
    } catch (SQLException sqlex) {
      sb.append("<hr><pre>");
      sb.append("Warning:  Exception caught verifying statement\n");
      JDTestcase.printStackTraceToStringBuffer(sqlex, sb);
      sb.append("\nCreating new connection\n"); 
      sb.append("</pre><hr>");
      if (userid == null) {
        connection = DriverManager.getConnection(jdbcURL);
      } else {
        connection = DriverManager.getConnection(jdbcURL, userid, password);
      }
    } 

  }

  public static AS400 as400 = null;

  public static String getLocalSystemStatus() {
    String osName = System.getProperty("os.name");
    if (osName.indexOf("400") >= 0) {
      try {
        if (as400 == null) {
          as400 = new AS400("localhost");
        }
        SystemStatus systemStatus = new SystemStatus(as400);
        return "ASP used = " + systemStatus.getPercentSystemASPUsed();
      } catch (Exception e) {
        return "Status caught exception " + e;
      }
    } else if (osName.indexOf("Windows") >= 0) {
      return "os.name=" + osName;
    } else if (osName.indexOf("Linux") >= 0) {
      return "os.name=" + osName;
    } else {
      return "os.name=" + osName + " not supported";
    }
  }

  public static Connection getConnection(StringBuffer sb) throws Exception {
    checkConnection(sb);
    return connection; 
  }

  public static Statement getStatement(StringBuffer sb) throws Exception {
    checkConnection(sb);

    Statement stmt;
    try {
      stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();

      if (userid == null) {
        connection = DriverManager.getConnection(jdbcURL);
      } else {
        connection = DriverManager.getConnection(jdbcURL, userid, password);
      }

      stmt = connection.createStatement();

    }
    return stmt;
  }

  public static PreparedStatement prepareStatement(String sql, StringBuffer sb)
      throws Exception {
    checkConnection(sb);
    PreparedStatement ps;
    try {
      ps = connection.prepareStatement(sql);
    } catch (SQLException e) {
      e.printStackTrace();

      if (userid == null) {
        connection = DriverManager.getConnection(jdbcURL);
      } else {
        connection = DriverManager.getConnection(jdbcURL, userid, password);
      }

      ps = connection.prepareStatement(sql);

    }
    return ps;
  }

  /* execute request */
  public static void execute(PrintWriter printWriter, Map<String, String[]> parameterMap) throws Throwable {

    String shortSystem = system;
    int dotIndex = shortSystem.indexOf(".");
    if (dotIndex > 0) {
      shortSystem = shortSystem.substring(0, dotIndex);
    }

    StringBuffer sb = new StringBuffer();
    sb.append("Content-Type: text/html\r\n\r\n");
    sb.append("<html><head><TITLE>SM-" + localHost + "</TITLE></head>");
    java.util.Date date = new java.util.Date();
    sb.append("<body><h2>SchedulerManager - " + localHost + " DB=" + shortSystem
        + " on " + date + "</h2>\n");

    sb.append("Local System Status " + getLocalSystemStatus() + "<br>");

    StringBuffer sb2 = new StringBuffer();
    checkConnection(sb);
    int groupTestJobCount = JDGroupTestInfo
        .getRunningGroupTestJobCount(connection, sb2);
    sb.append("groupTestJobcount=" + groupTestJobCount + "<br>");

    try {
      // Code to process future ags.
      String refresh = getArg("AUTOREFRESH", parameterMap);
      String view = getArg("VIEW", parameterMap);

      StringBuffer keyPrefix = new StringBuffer();

      if (view == null) {

        /* Show the current schedule jobs */

        if (refresh != null) {
          int refreshTime = Integer.parseInt(refresh);
          if (refreshTime == 0) {
            refreshTime = 60000;
          }
          if (refreshTime < 5000) {
            refreshTime = 5000;
          }
          sb.append("<script type=\"text/javascript\">\n");
          sb.append("setTimeout(function(){window.location.reload(true);},"
              + refreshTime + ");\n");
          sb.append("</script>\n");
          keyPrefix.append("REFRESH=" + refreshTime);
        }
        keyPrefix.append(
            "<input type=\"button\" value=\"Reload Page\" onClick=\"window.location.reload()\">\n");
        if (refresh == null) {
          keyPrefix.append(
              "<input type=\"button\" value=\"Auto refresh\" onClick=\"window.location.href='SchedulerManager.acgi?AUTOREFRESH=60000'\">\n");
        }
        keyPrefix.append(
            "<input type=\"button\" value=\"Home\" onClick=\"window.location.href='/'\">\n");

        Statement stmt = getStatement(sb);

        displayInitialsStatus(sb, stmt, defaultId, keyPrefix.toString());

        sb.append("<h2>Running items (JDTESTINFO.SCRUN" + defaultId
            + ")</h2>\n<hr>\n");
        sb.append("<table border=\"1\">\n");
        sb.append("<th>PRIORITY</th>\n");
        sb.append("<th>ADDED_TS</th>\n");
        sb.append("<th>INITIALS</th>\n");
        sb.append("<th>ACTION</th>\n");
        sb.append("<th>STARTED_TS</th>\n");
        sb.append("<th>RUN_SECONDS</th>\n");
        sb.append("<th>AVG_RUN_SECONDS</th>\n");
        sb.append("<th>OUTPUTFILE</th>\n");

        ResultSet rs = stmt
            .executeQuery("select  A.PRIORITY, A.ADDED_TS, A.INITIALS, "
                + " A.ACTION,A.STARTED_TS, "
                + "TIMESTAMPDIFF(2, CAST((CURRENT TIMESTAMP - A.STARTED_TS) AS CHAR(22))) AS RUN_SECONDS, "
                + " CAST( B.RECENT_AVERAGE_SECONDS AS INTEGER) AS AVG_RUN_SECONDS, A.OUTPUTFILE  "
                + " from JDTESTINFO.SCRUN" + defaultId
                + " A LEFT OUTER JOIN JDTESTINFO.SCSTA1 B "
                + " ON  A.INITIALS=B.INITIALS and A.ACTION=B.ACTION");

        while (rs.next()) {
          sb.append("<tr>");
          for (int i = 1; i <= 7; i++) {
            sb.append("<td>" + rs.getString(i) + "</td>");
          }
          String outfile = rs.getString(8);
          sb.append(
              "<td><a href=\"http:" + outfile + "\">" + outfile + "</a></td>");
          sb.append("</tr>\n");
        }
        rs.close();
        sb.append("</table>");

        sb.append("<h2>Scheduled items (JDTESTINFO.SCHED" + defaultId
            + ")</h2>\n<hr>\n");
        sb.append("<table border=\"1\">\n");
        sb.append("<th>PRIORITY</th>\n");
        sb.append("<th>ADDED_TS</th>\n");
        sb.append("<th>INITIALS</th>\n");
        sb.append("<th>ACTION</th>\n");
        sb.append("<th>AVG_RUN_SECONDS</th>\n");

        rs = stmt.executeQuery("select A.*, CAST( B.RECENT_AVERAGE_SECONDS AS INTEGER) AS AVG_RUN_SECONDS from JDTESTINFO.SCHED" + defaultId
            + " A LEFT OUTER JOIN JDTESTINFO.SCSTA1 B "
            + "  ON A.INITIALS = B.INITIALS and A.ACTION=B.ACTION "
            + "  ORDER BY PRIORITY,ADDED_TS");
        while (rs.next()) {
          sb.append("<tr>");
          for (int i = 1; i <= 4; i++) {
            sb.append("<td>" + rs.getString(i) + "</td>");
          }
          sb.append("</tr>\n");
        }
        rs.close();
        sb.append("</table>");

        stmt.close();
        // Don't close the connection, we are reusing it
      }

    } catch (Exception e) {
      sb.append("<h2>Exception occurred in SchedulerManagerEngine</h2>");
      sb.append("<pre>");
      printStackTraceToStringBuffer(e, sb);
      sb.append("</pre>");

    }
    sb.append("<HR><HR></body></html>");


    printWriter.println(sb.toString());
    printWriter.flush();

  }

  public static void printStackTraceToStringBuffer(Exception e,
      StringBuffer sb) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    e.printStackTrace(printWriter);
    String exception = stringWriter.toString();
    sb.append(exception);

  }

  public static Vector<String> runQuery(Statement stmt, String query) {
    Vector<String> returnVector = new Vector<String> ();
    try {
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        String s = rs.getString(1);
        if (s != null) {
          returnVector.addElement(s);
        } else {
          returnVector.addElement("null for " + query);
        }
      }

    } catch (Exception e) {

      returnVector.addElement("EXCEPTION");
    }
    return returnVector;
  }

  public static void displayInitialsStatus(StringBuffer sb, Statement stmt,
      String defaultId, String keyPrefix) {

    // Get List all testcases
    Vector<String> currentlyStalledInitials = runQuery(stmt,
        "SELECT DISTINCT INITIALS from JDTESTINFO.SCRUN" + defaultId
            + " WHERE STARTED_TS < CURRENT TIMESTAMP - 1 HOUR");

    Vector<String> currentlyRunningInitials = runQuery(stmt,
        "SELECT DISTINCT INITIALS from JDTESTINFO.SCRUN" + defaultId);
    Vector<String> currentlyScheduledInitials = runQuery(stmt,
        "SELECT DISTINCT INITIALS from JDTESTINFO.SCHED" + defaultId);
    Vector<String> allRecentInitials = runQuery(stmt,
        "SELECT DISTINCT INITIALS from JDTESTINFO.SCSTA" + defaultId
            + " WHERE LAST_TS > ( CURRENT TIMEstamp - 7 days ) ");
    Vector<String> allRunInitials = runQuery(stmt,
        "SELECT DISTINCT INITIALS from JDTESTINFO.SCSTA" + defaultId);

    // Sort all the vectors
    Collections.sort(currentlyRunningInitials);
    Collections.sort(currentlyScheduledInitials);
    Collections.sort(allRecentInitials);
    Collections.sort(allRunInitials);

    Hashtable<String, String> displayedInitials = new Hashtable<String,String>();

    // Create a table of the good initials
    sb.append("<table>\n<tr>");
    sb.append("<td>" + keyPrefix + "</td>");
    sb.append("<td bgcolor=\"pink\">STALLED</td>");
    sb.append("<td bgcolor=\"yellow\">RUNNING</td>");
    sb.append("<td bgcolor=\"cyan\">SCHEDULED</td>");
    sb.append("<td bgcolor=\"lawngreen\">RUN WITHIN LAST WEEK</td>");
    sb.append("<td bgcolor=\"red\">NO RECENT RUN</td>");
    sb.append("</tr></table>\n");

    sb.append("<table>\n<tr>");

    int printCount = 0;
    Enumeration<String> e1 = currentlyStalledInitials.elements();
    while (e1.hasMoreElements()) {
      String initials = (String) e1.nextElement();
      if (displayedInitials.get(initials) == null) {
        sb.append("<td bgcolor=\"pink\">");
        sb.append(initials);
        sb.append("</td>");
        displayedInitials.put(initials, initials);
        printCount++;
      }
    }
    if (printCount > 0) {
      sb.append("</tr>\n<tr>");
      printCount = 0;
    }

    e1 = currentlyRunningInitials.elements();
    while (e1.hasMoreElements()) {
      String initials = (String) e1.nextElement();
      if (displayedInitials.get(initials) == null) {
        sb.append("<td bgcolor=\"yellow\">");
        sb.append(initials);
        sb.append("</td>");
        displayedInitials.put(initials, initials);
        printCount++;
      }
    }

    if (printCount > 0) {
      sb.append("</tr>\n<tr>");
      printCount = 0;
    }

    e1 = currentlyScheduledInitials.elements();
    while (e1.hasMoreElements()) {
      String initials = (String) e1.nextElement();
      if (displayedInitials.get(initials) == null) {
        sb.append("<td bgcolor=\"cyan\">");
        sb.append(initials);
        sb.append("</td>");
        displayedInitials.put(initials, initials);
        printCount++;
        if (printCount >= 20) {
          sb.append("</tr>\n<tr>");
          printCount = 0;
        }

      }
    }

    if (printCount > 0) {
      sb.append("</tr>\n<tr>");
      printCount = 0;
    }

    e1 = allRecentInitials.elements();
    while (e1.hasMoreElements()) {
      String initials = (String) e1.nextElement();
      if (displayedInitials.get(initials) == null) {
        sb.append("<td bgcolor=\"lawngreen\">");
        sb.append(initials);
        sb.append("</td>");
        displayedInitials.put(initials, initials);
        printCount++;
        if (printCount >= 20) {
          sb.append("</tr>\n<tr>");
          printCount = 0;
        }

      }
    }

    sb.append("</tr></table>\n");

    // Create a table of the bad initials

    sb.append("<table><tr>\n");

    e1 = allRunInitials.elements();
    while (e1.hasMoreElements()) {
      String initials = (String) e1.nextElement();
      if (displayedInitials.get(initials) == null) {
        sb.append("<td bgcolor=\"red\">");
        sb.append(initials);
        sb.append("</td>");
        displayedInitials.put(initials, initials);
        printCount++;
        if (printCount >= 20) {
          sb.append("</tr>\n<tr>");
          printCount = 0;
        }

      }
    }

    sb.append("</tr></table>\n");

  }

  public static void submitProcess(String initials, String action,
      String priority, StringBuffer sb) {

    if (action.equals("REGRESSIONPLUS")) {
      submitProcess(initials, "REGRESSION", priority, sb);
      submitProcess(initials, "REPORT", priority + 1, sb);
      submitProcess(initials, "RERUNFAILED", priority + 2, sb);
      submitProcess(initials, "REPORT", priority + 3, sb);
      submitProcess(initials, "EMAIL", priority + 4, sb);
      return;
    }

    if (action.equals("RERUNFAILEDPLUS")) {
      submitProcess(initials, "REPORT", priority, sb);
      submitProcess(initials, "RERUNFAILED", priority + 1, sb);
      submitProcess(initials, "REPORT", priority + 2, sb);
      submitProcess(initials, "EMAIL", priority + 3, sb);
      return;
    }

    try {
      checkConnection(sb);
      String scheduleTable = "JDTESTINFO.SCHED" + defaultId;
      PreparedStatement ps = prepareStatement(
          "INSERT INTO " + scheduleTable + " VALUES(?,CURRENT TIMESTAMP,?,?)",sb);
      int intPriority = 1;
      try {
        intPriority = Integer.parseInt(priority);
      } catch (Exception e) {
      }
      if (intPriority < 1)
        intPriority = 1;
      ps.setInt(1, intPriority);
      ps.setString(2, initials);
      ps.setString(3, action);
      ps.executeUpdate();
      ps.close();
      sb.append(
          "Added " + intPriority + " " + initials + " " + action + "<br>\n");
    } catch (Exception e) {
      sb.append("Error:  exception caught " + e.toString() + "<br>\n");
      /* Print the stack trace to the exception buffer */
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      e.printStackTrace(printWriter);
      String exception = stringWriter.toString();
      appendWithReplacement(sb, exception, "\n", "<br>\n");

    }

  }

  public static void appendWithReplacement(StringBuffer sb, String s,
      String from, String to) {
    int fromLength = from.length();
    int startIndex = 0;
    int replaceIndex = 0;
    replaceIndex = s.indexOf(from, startIndex);
    while (replaceIndex > 0) {
      sb.append(from.substring(startIndex, replaceIndex));
      sb.append(to);
      startIndex = replaceIndex + fromLength;
      replaceIndex = s.indexOf(from, startIndex);
    }

    if (startIndex < fromLength) {
      sb.append(from.substring(startIndex));
    }

  }

}
