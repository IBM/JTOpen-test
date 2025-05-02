///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementWarnings.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementWarnings.java
//
// Classes:      JDStatementWarnings
//
////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;

import test.JDStatementTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.TestDriver;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Testcase JDStatementWarnings. This tests the following methods of the JDBC
 * Statement class:
 * 
 * <ul>
 * <li>clearWarnings()
 * <li>getWarning()
 * </ul>
 **/
public class JDStatementWarnings extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDStatementWarnings";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDStatementTest.main(newArgs); 
   }

  // Private data.
  private Connection connection_;
  private static String table_ = JDStatementTest.COLLECTION + ".JDSE5";
  private static String table_2 = JDStatementTest.COLLECTION + ".JDSW11";

  /**
   * Constructor.
   **/
  public JDStatementWarnings(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDStatementWarnings", namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
    Statement s = connection_.createStatement();
    table_ = JDStatementTest.COLLECTION + ".JDSE5";
    table_2 = JDStatementTest.COLLECTION + ".JDSW11";

    initTable(s, table_, " (NAME VARCHAR(10), ID INT, SCORE INT)");
    s.executeUpdate(
        "INSERT INTO " + table_ + " (NAME, ID) VALUES ('cnock', 1)");
    s.executeUpdate(
        "INSERT INTO " + table_ + " (NAME, ID) VALUES ('murch', 2)");
    s.executeUpdate(
        "INSERT INTO " + table_ + " (NAME, ID) VALUES ('joshvt', 3)");
    s.executeUpdate(
        "INSERT INTO " + table_ + " (NAME, ID) VALUES ('robb', -1)");

    initTable(s, table_2, "(DATE1 DATE)");
    s.executeUpdate("INSERT INTO " + table_2 + " VALUES('2008-02-29')");

    
 
    
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    Statement s = connection_.createStatement();
    s.executeUpdate("DROP TABLE " + table_);
    s.executeUpdate("DROP TABLE " + table_2);
    s.close();
    connection_.close();
  }

  /**
   * Set a warning for the native driver
   */
  private void setNativeWarning(Statement s) {
    // Use reflection to set a Warning
    // Reflection must be used to permit the toolbox to compile
    try {
      SQLWarning warning = new SQLWarning("This is a warning");

      Class<?> db2Statement = Class.forName("com.ibm.db2.jdbc.app.DB2Statement");
      Class<?>[] args = new Class[1];
      args[0] = Class.forName("java.sql.SQLWarning");
      java.lang.reflect.Method method = db2Statement.getMethod("addWarning",
          args);
      Object[] parms = new Object[1];
      parms[0] = warning;
      method.invoke(s, parms);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Forces a single warning to be posted to the statement.
   * 
   * @param s
   *          The statement.
   * @exception Exception
   *              If an exception occurs.
   **/
  public void forceWarning(Statement s) throws Exception {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
      setNativeWarning(s);
    } else {
      // This should force the warning "option value changed".
      // This is assuming that the statement has result set
      // concurrency of updatable. The warning should be issued
      // because this query does not then have a FOR UPDATE
      // clause.
      s.executeQuery("SELECT * FROM QIWS.QCUSTCDT");
    }
  }

  /**
   * clearWarnings() - Has no effect when there are no warnings.
   **/
  public void Var001() {
    if (checkJdbc20()) {
      if ((isToolboxDriver() && getRelease() >= JDTestDriver.RELEASE_V7R1M0)
          || (getDriver() == JDTestDriver.DRIVER_NATIVE
              && getRelease() >= JDTestDriver.RELEASE_V7R1M0)) {
        try {
          Statement s = connection_.createStatement();
          s.executeUpdate("DELETE FROM " + table_ + " WHERE NAME='cnock'");

          s.clearWarnings();
          SQLWarning w1 = s.getWarnings();
          s.clearWarnings();
          SQLWarning w2 = s.getWarnings();
          s.close();
          assertCondition((w1 == null) && (w2 == null));

        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      } else {
        try {
          Statement s = connection_.createStatement(
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          s.clearWarnings();
          SQLWarning w1 = s.getWarnings();
          s.clearWarnings();
          SQLWarning w2 = s.getWarnings();
          s.close();
          assertCondition((w1 == null) && (w2 == null));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * clearWarnings() - Clears warnings after 1 has been posted.
   **/
  public void Var002() {
    if (checkJdbc20()) {
      if ((isToolboxDriver() && getRelease() >= JDTestDriver.RELEASE_V7R1M0)
          || (getDriver() == JDTestDriver.DRIVER_NATIVE
              && getRelease() >= JDTestDriver.RELEASE_V7R1M0)) {
        try {
          Statement s = connection_.createStatement();
          s.executeUpdate("DELETE FROM " + table_ + " WHERE NAME='Susan'");
          SQLWarning w1 = s.getWarnings();
          s.clearWarnings();
          SQLWarning w2 = s.getWarnings();
          s.close();
          assertCondition((w1 != null) && (w2 == null));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      } else {
        try {
          Statement s = connection_.createStatement(
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          forceWarning(s);
          SQLWarning w1 = s.getWarnings();
          s.clearWarnings();
          SQLWarning w2 = s.getWarnings();
          s.close();
          assertCondition((w1 != null) && (w2 == null));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * clearWarnings() - Clears warnings when a statement is closed.
   **/
  public void Var003() {
    if (checkJdbc20()) {
      if ((isToolboxDriver() && getRelease() >= JDTestDriver.RELEASE_V7R1M0)
          || (getDriver() == JDTestDriver.DRIVER_NATIVE
              && getRelease() >= JDTestDriver.RELEASE_V7R1M0)) {
        try {
          Statement s = connection_.createStatement();
          s.executeUpdate("DELETE FROM " + table_ + " WHERE NAME='Susan'");
          s.close();
          SQLWarning w1 = s.getWarnings();
          s.clearWarnings();
          SQLWarning w2 = s.getWarnings();
          assertCondition((w1 != null) && (w2 == null));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      } else {
        try {
          Statement s = connection_.createStatement(
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          forceWarning(s);
          s.close();
          SQLWarning w1 = s.getWarnings();
          s.clearWarnings();
          SQLWarning w2 = s.getWarnings();
          assertCondition((w1 != null) && (w2 == null));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getWarning() - Returns null if no warnings have been reported.
   **/
  public void Var004() {
    if (checkJdbc20()) {
      if ((isToolboxDriver() && getRelease() >= JDTestDriver.RELEASE_V7R1M0)
          || (getDriver() == JDTestDriver.DRIVER_NATIVE
              && getRelease() >= JDTestDriver.RELEASE_V7R1M0)) {
        try {
          Statement s = connection_.createStatement();
          s.executeUpdate("DELETE FROM " + table_ + " WHERE NAME='murch'");
          s.clearWarnings();
          SQLWarning w = s.getWarnings();
          s.close();
          assertCondition(w == null);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      } else {
        try {
          Statement s = connection_.createStatement(
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          s.clearWarnings();
          SQLWarning w = s.getWarnings();
          s.close();
          assertCondition(w == null);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getWarning() - Returns the first warning when 1 warning has been reported.
   **/
  public void Var005() {
    if (checkJdbc20()) {
      if ((isToolboxDriver() && getRelease() >= JDTestDriver.RELEASE_V7R1M0)
          || (getDriver() == JDTestDriver.DRIVER_NATIVE
              && getRelease() >= JDTestDriver.RELEASE_V7R1M0)) {
        try {
          Statement s = connection_.createStatement();
          s.clearWarnings();
          s.executeUpdate("DELETE FROM " + table_ + " WHERE NAME='Susan'");
          SQLWarning w1 = s.getWarnings();
          SQLWarning w2 = w1.getNextWarning();
          s.close();
          assertCondition((w1 != null) && (w2 == null));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      } else {
        try {
          Statement s = connection_.createStatement(
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          s.clearWarnings();
          forceWarning(s);
          SQLWarning w1 = s.getWarnings();
          SQLWarning w2 = w1.getNextWarning();
          s.close();
          assertCondition((w1 != null) && (w2 == null));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getWarning() - Returns the first warning when 2 warnings have been
   * reported.
   **/
  public void Var006() {
    if (checkJdbc20()) {
      if ((isToolboxDriver() && getRelease() >= JDTestDriver.RELEASE_V7R1M0)
          || (getDriver() == JDTestDriver.DRIVER_NATIVE
              && getRelease() >= JDTestDriver.RELEASE_V7R1M0)) {
        try {
          PreparedStatement s = connection_.prepareStatement(
              "SELECT * FROM QIWS.QCUSTCDT WHERE CUSNUM = ?",
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          s.clearWarnings();
          // CUSTNUM is only 6 digits long, so we should get a data truncation
          // warning
          // if we try to use a number that is 7 digits long
          // This is an error since we lose significant information
          try {
            s.setInt(1, 1234567);
            s.setInt(1, 4567893);
            SQLWarning w1 = s.getWarnings();
            SQLWarning w2 = w1.getNextWarning();
            SQLWarning w3 = w2.getNextWarning();
            s.close();
            if (getRelease() >= JDTestDriver.RELEASE_V7R1M0
                && (isToolboxDriver()
                    || getDriver() == JDTestDriver.DRIVER_NATIVE))
              failed("did not throw truncation exception for v5r5");
            else
              assertCondition((w1 != null) && (w2 != null) && (w3 == null));
          } catch (DataTruncation e) {
            if (getRelease() >= JDTestDriver.RELEASE_V7R1M0
                && (isToolboxDriver()
                    || getDriver() == JDTestDriver.DRIVER_NATIVE))
              succeeded();
            else
              failed(e, "Unexpected Exception");
          }

        } catch (Exception e) {
          assertSqlException(e, -99999, "07006", "Data type mismatch",
              "Mismatch instead of truncation in latest toolbox ");
        }
      } else {

        /*
         * There is currently no way to force more than one warning on a
         * Statement. The problem lies in the fact that the execute() methods
         * clear warnings.
         * 
         * try { Statement s = connection_.createStatement
         * (ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
         * forceWarning (s); forceWarning (s); SQLWarning w1 = s.getWarnings ();
         * SQLWarning w2 = w1.getNextWarning (); SQLWarning w3 =
         * w2.getNextWarning (); s.close (); assertCondition ((w1 != null) &&
         * (w2 != null) && (w3 == null)); } catch(Exception e) { failed(e,
         * "Unexpected Exception"); }
         */
        assertCondition(true);
      }
    }
  }

  /**
   * getWarning() - Returns the first warning even after the statement is
   * closed.
   **/
  public void Var007() {
    if ((isToolboxDriver() && getRelease() >= JDTestDriver.RELEASE_V7R1M0)
        || (getDriver() == JDTestDriver.DRIVER_NATIVE
            && getRelease() >= JDTestDriver.RELEASE_V7R1M0)) {
      try {
        Statement s = connection_.createStatement();
        s.executeUpdate("DELETE FROM " + table_ + " WHERE NAME='Susan'");
        s.close();
        SQLWarning w = s.getWarnings();
        assertCondition(w != null);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    } else {
      if (checkJdbc20()) {
        try {
          Statement s = connection_.createStatement(
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          forceWarning(s);
          s.close();
          SQLWarning w = s.getWarnings();
          assertCondition(w != null);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getWarnings() -- Make sure a data conversion warning is returned in V5R3
   * (this probably works for toolbox earlier then this
   */

  public void Var008() {
    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT 8/0 from QSYS2.QSQPTABL");
        SQLWarning w = s.getWarnings();
        s.close();

        String message;
        if (w != null) {
          message = w.getMessage();
        } else {
          message = "NO WARNING FOUND";
        }
        if (message == null)
          message = "NO MESSAGE IN WARNING for rs=" + rs;
        message = message.toUpperCase();
        assertCondition(
            message.indexOf("DATA CONVERSION OR DATA MAPPING ERROR") >= 0,
            "message should be 'DATA CONVERSION OR DATA MAPPING ERROR', but is '"
                + message + "' --  new V5R3 testcase, added by native driver");
      } catch (Exception e) {
        failed(e,
            "Unexpected Exception -- new V5R3 testcase, added by native driver");
      }
    } else {
      notApplicable();
    }
  }

  /**
   * getWarnings() -- Make sure a data conversion warning is returned.
   */

  public void Var009() {
    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
      String tablename = JDStatementTest.COLLECTION + ".JDSTMWRNTS";
      try {
        Statement s = connection_.createStatement();

        try {
          s.executeUpdate("drop table " + tablename);
        } catch (Exception e) {
           String message = e.toString(); 
           if (message.indexOf("not found") < 0) {
               if (message.indexOf("Not authorized")>=0) { 
                 Connection pwrConnection = testDriver_.getConnection(baseURL_, pwrSysUserID_, pwrSysEncryptedPassword_);
                 Statement pwrStatement = pwrConnection.createStatement(); 
                 pwrStatement.executeUpdate("drop table " + tablename);
                 pwrStatement.close(); 
                 pwrConnection.close(); 
               } else { 
                 throw e; 
               }        
           } /* else OK */ 
        }

        s.executeUpdate("create table " + tablename + "(cstts timestamp)");
        s.executeUpdate("insert into " + tablename
            + " values('1991-06-05-12.52.01.482752')");

        ResultSet rs = s.executeQuery(
            "SELECT CSTTS - 1990 YEARS - 5 MONTHS - 4 DAYS - 12 HOURS - 52 MINUTES - 20 SECONDS FROM "
                + tablename);

        SQLWarning w = s.getWarnings();
        s.close();

        String message;
        if (w != null) {
          message = w.getMessage();
        } else {
          message = "NO WARNING FOUND for rs=" + rs;
        }
        if (message == null)
          message = "NO MESSAGE IN WARNING";
        message = message.toUpperCase();

        // @C2 if(isToolboxDriver()) //@C1A

        assertCondition(
            message.indexOf(
                "RESULT OF DATE OR TIMESTAMP EXPRESSION NOT VALID") >= 0,
            "message should be 'RESULT OF DATE OR TIMESTAMP EXPRESSION NOT VALID',"
                + "but is '" + message); // @C1A
        /*
         * @C2 else //@C1A assertCondition
         * (message.indexOf("DATA CONVERSION OR DATA MAPPING ERROR") >=0,
         * "message should be 'DATA CONVERSION OR DATA MAPPING ERROR', but is '"
         * +message+"' --  new V5R3 testcase, added by native driver");
         */
      } catch (Exception e) {
        failed(e,
            "Unexpected Exception -- new V5R3 testcase, added by native driver");
      }
    } else {
      notApplicable();
    }
  }

  /**
   * getWarnings() -- Make sure a data truncation warning is returned in V5R3
   * This tests the new attribute that needs to be set in CLI to receive data
   * trucation warnings.
   */

  public void Var010() {
    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0
        && getDriver() == JDTestDriver.DRIVER_NATIVE) // host server doesn't
                                                      // have a way to get
                                                      // truncation warnings
    {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery(
            "SELECT CAST('HELLO' AS CHAR(3)) FROM QSYS2.QSQPTABL");

        SQLWarning w = s.getWarnings();
        s.close();

        String state;
        if (w != null) {
          state = w.getSQLState();
        } else {
          state = "NO WARNING FOUND for rs=" + rs;
        }
        if (state == null)
          state = "NO SQL STATE IN WARNING";

        assertCondition(state.equals("01004"), "SQL state should be '01004',"
            + "but is '" + state + "' added by native driver 3/22/2004");
      } catch (Exception e) {
        failed(e,
            "Unexpected Exception -- new V5R3 testcase, added by native driver 3/22/2004");
      }
    } else {
      notApplicable();
    }
  }

  public void Var011()

  {

    // this is where the warning test for SQL state 01506 goes
    // warning for an illegal date being created using addition and subtraction
    // of years/months to date type
    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0
        && getDriver() == JDTestDriver.DRIVER_NATIVE) {
      try {
        String select = "select date1 + 1 year from " + table_2
            + " where date1 + 1 year > '2008-01-01'";
        Statement stmt = connection_.createStatement();
        ResultSet rs = stmt.executeQuery(select);
        SQLWarning warn = stmt.getWarnings();
        String state;

        if (warn != null) {
          state = warn.getSQLState();
        } else {
          state = "NO WARNING FOUND for rs=" + rs;
        }
        if (state == null) {
          state = "NO SQL STATE IN WARNING";
        }
        assertCondition(state.equals("01506"), "SQL state should be '01506',"
            + "but is '" + state + "' added 6/6/2008");

      } catch (Exception e) {

        failed(e, "Unexpected Exception -- added 6/6/2008");

      }

    } else {
      notApplicable();
    }

  }

  public void Var012()

  {

    // this is where the warning test for SQL state 01506 goes
    // warning for an illegal date being created using addition and subtraction
    // of years/months to date type
    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0
        && getDriver() == JDTestDriver.DRIVER_NATIVE) {
      try {
        String select = "select date1 + 1 year from " + table_2
            + " where date1 + 1 year > '2008-01-01'";
        Connection connection = testDriver_.getConnection(
            baseURL_ + ";blocking enabled=false", userId_, encryptedPassword_);

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(select);
        SQLWarning warn = stmt.getWarnings();
        String state;

        if (warn != null) {
          state = warn.getSQLState();
        } else {
          state = "NO WARNING FOUND for rs=" + rs;
        }
        if (state == null) {
          state = "NO SQL STATE IN WARNING";
        }
        connection.close();
        assertCondition(state.equals("01506"), "SQL state should be '01506',"
            + "but is '" + state + "' added 6/6/2008");

      } catch (Exception e) {

        failed(e, "Unexpected Exception -- added 6/6/2008");

      }

    } else {
      notApplicable();
    }

  }

  public void Var013() {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    sb.append("New long message test added 01/16/2020\n");
    /* Test a long warning message */
    /* Fixed in toolbox January 2020 */
    try {
      String warningSql = "BEGIN SIGNAL SQLSTATE '01JWE' SET MESSAGE_TEXT = '1         2         3         4         5         6         7         8         9         100       1         2         3         4         5         6         7         8         9         '; END";
      Statement stmt = connection_.createStatement();
      stmt.execute(warningSql);
      SQLWarning warn = stmt.getWarnings();
      String state;

      if (warn != null) {
        state = warn.getSQLState();
        if ("01JWE".equals(state)) {
        } else {
          passed = false;
          sb.append("State is " + state + " sb 01JWE\n");
        }
        String message = warn.getMessage();
        String expected = "[SQL0438] 1         2         3         4         5         6         7         8         9         100       1         2         3         4         5         6         7         8         9         ";
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          expected = "Message 1         2         3         4         5         6         7         8         9         100       1         2         3         4         5         6         7         8         9 returned from SIGNAL, RESIGNAL, or RAISE_ERROR.";
        }

        if (expected.equals(message)) {
          // Passed
        } else {
          passed = false;
          sb.append("Got Message '" + message + "'\n");
          sb.append("         sb '" + expected + "'\n");
        }
      } else {
        passed = false;
        sb.append("Warning not returned\n ");
      }
      assertCondition(passed, sb.toString());
    } catch (Exception e) {

      failed(e, "Unexpected Exception " + sb.toString());

    }

  }

  public void Var014() {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    sb.append("New long message test added 01/16/2020\n");
    /* Test a long warning message */
    /* Fixed in toolbox January 2020 */
    try {
      
      String createSql = "create or replace procedure " + collection_
          + ".throwit() language sql BEGIN SIGNAL SQLSTATE '01JWE' SET MESSAGE_TEXT = '1         2         3         4         5         6         7         8         9         100       1         2         3         4         5         6         7         8         9         '; END";

      String warningSql = "call " + collection_ + ".throwit()";
      String dropSql = "drop procedure " + collection_ + ".throwit()";
      Statement stmt = connection_.createStatement();
      boolean retry = true; 
      while (retry) {
        retry = false;

        try {
          stmt.execute(dropSql);
        } catch (Exception e) {
          String message = e.toString();
          if (message.indexOf("not found") >= 0) {
            // Ignore if it doesn't exist
          } else if (message.indexOf("Not authorized")>=0) { 
            AS400 pwrSys = TestDriver.getPwrSys();
            CommandCall cmd = new CommandCall (pwrSys);
            cmd.run ("QSYS/GRTOBJAUT OBJ(" + collection_ + "/THROWIT) OBJTYPE(*PGM) USER(*PUBLIC) AUT(*ALL)");

          } else {
            System.out.println("Unable to drop procedure using " + dropSql);
            e.printStackTrace();
          }
        }
      }
      stmt.execute(createSql);
      stmt.execute(warningSql);
      SQLWarning warn = stmt.getWarnings();
      String state;

      if (warn != null) {
        state = warn.getSQLState();
        if ("01JWE".equals(state)) {
        } else {
          passed = false;
          sb.append("FAILED: State is " + state + " sb 01JWE\n");
        }
        String message = warn.getMessage();
        String expected = "[SQL0438] 1         2         3         4         5         6         7         8         9         100       1         2         3         4         5         6         7         8         9         ";

        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          expected = "Message 1         2         3         4         5         6         7         8         9         100       1         2         3         4         5         6         7         8         9 returned from SIGNAL, RESIGNAL, or RAISE_ERROR.";
        }
        if (expected.equals(message)) {
          // Passed
        } else {
          passed = false;
          sb.append("FAILED: Got Message '" + message + "'\n");
          sb.append("                 sb '" + expected + "'\n");
        }
      } else {
        passed = false;
        sb.append("FAILED: Warning not returned\n ");
      }
      stmt.execute(dropSql); 
      stmt.close(); 
      assertCondition(passed, sb.toString());
    } catch (Exception e) {

      failed(e, "Unexpected Exception " + sb.toString());

    }

  }

  /**
   * getWarning() - Returns the correct warnings for DELETE FROM table
   **/
  public void Var015() {
    if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX
        && getRelease() >= JDTestDriver.RELEASE_V7R3M0)) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      try {
        Statement s = connection_.createStatement();
        String table = collection_ + ".DELETEME";
        s.executeUpdate("CREATE OR REPLACE TABLE " + table + " (C1 INT)");
        s.executeUpdate("DELETE FROM " + table);
        SQLWarning w1 = s.getWarnings();
        if (w1 == null) {
          passed = false;
          sb.append("First warning is null");
        } else {
          String w1ExpectedState = "02000";
          int w1ExpectedCode = 100;
          String w1ExpectedMessage = "Row not found for DELETE";
          if (!w1ExpectedState.equals(w1.getSQLState())) {
            passed = false;
            sb.append("\nw1ExpectedState sb " + w1ExpectedState + " but was "
                + w1.getSQLState());
          }
          if (w1ExpectedCode != w1.getErrorCode()) {
            passed = false;
            sb.append("\nw1ExpectedCode sb " + w1ExpectedCode + " but was "
                + w1.getErrorCode());
          }
          String w1Message = w1.getMessage();
          if (w1Message == null || (w1Message.indexOf(w1ExpectedMessage) < 0)) {
            passed = false;
            sb.append("\nw1ExpectedMessage sb " + w1ExpectedMessage
                + " but was " + w1.getMessage());
          }
          SQLWarning w2 = w1.getNextWarning();
          // If we have a second warning, make sure it is valid.
          if (w2 != null) {
            String w2ExpectedState = "01504";
            int w2ExpectedCode = 00;
            String w2ExpectedMessage = "Text not available for message";
            if (!w2ExpectedState.equals(w2.getSQLState())) {
              passed = false;
              sb.append("\nw2ExpectedState sb " + w2ExpectedState + " but was "
                  + w2.getSQLState());
            }
            if (w2ExpectedCode != w2.getErrorCode()) {
              passed = false;
              sb.append("\nw2ExpectedCode sb " + w2ExpectedCode + " but was "
                  + w2.getErrorCode());
            }
            String w2Message = w2.getMessage();
            if (w2Message == null
                || (w2Message.indexOf(w2ExpectedMessage) < 0)) {
              passed = false;
              sb.append("\nw2ExpectedMessage sb " + w2ExpectedMessage
                  + " but was " + w2.getMessage());
            }
          }
        }
        s.executeUpdate("DROP TABLE " + table);
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    } else {
	notApplicable("TOOLBOX V7R3 and later version"); 
    }
  }

  /**
   * getWarning() - Returns the warning when result set is empty. See issue 60347.
   **/
  public void Var016() {
    if ((getDriver() == JDTestDriver.DRIVER_TOOLBOX
        && getRelease() >= JDTestDriver.RELEASE_V7R3M0)) {
      StringBuffer sb = new StringBuffer();
      sb.append("\nNew toolbox tests for multiple warnings -- added 2021-02-24"); 
      boolean passed = true; 
      Connection pwrConnection = null;  
      try {
        // Create another connection with SECOFR authority and set up a job description.
        sb.append("\nGetting pwrConnection with "+pwrSysUserID_); 
        pwrConnection =  testDriver_.getConnection(baseURL_, pwrSysUserID_, pwrSysEncryptedPassword_);
        Statement pwrStatement = pwrConnection.createStatement(); 

        String sql = "CALL QSYS2.QCMDEXC('DLTJOBD JOBD("+collection_+"/JDSTWJOBD)')"; 
        
        
        sb.append("\nExecuting PWR "+sql); 
        try { 
        pwrStatement.executeUpdate(sql); 
        } catch (SQLException e) { 
          // ignore errors from the drop 
        }
        sql = "CALL QSYS2.QCMDEXC('CRTJOBD JOBD("+collection_+"/JDSTWJOBD)')"; 
        sb.append("\nExecuting PWR "+sql); 
        pwrStatement.executeUpdate(sql); 
        sql = "CALL QSYS2.QCMDEXC('RVKOBJAUT OBJ("+collection_+"/JDSTWJOBD) OBJTYPE(*JOBD) USER(*PUBLIC) AUT(*ALL)')"; 
        sb.append("\nExecuting PWR "+sql); 
        pwrStatement.executeUpdate(sql); 
        pwrConnection.commit(); 

       
        
        
        
        Statement s = connection_.createStatement();
        sql = "VALUES CURRENT USER"; 
        sb.append("\nExecuting "+sql);
        ResultSet rs = s.executeQuery(sql); 
        rs.next(); 
        sb.append("\nCurrent user is "+rs.getString(1)); 

        // Run a query that fails
        try { 
            s.executeQuery("FAIL_QUERY"); 
        } catch (Exception e) { 
          
        }
	int retryCount = 5;
	while (retryCount > 0) { 
	    sql = "SELECT * FROM TABLE(QSYS2.JOB_DESCRIPTION_INFO('JDSTWJOBD', '"+collection_+"'))";
	    sb.append("\nExecuting "+sql);
	    rs = s.executeQuery(sql);
	    boolean rowFound = rs.next();
      SQLWarning w1 = s.getWarnings();

          if (w1 == null) {
            sb.append("\nError:  First warning is null. Row found is "+rowFound);
            retryCount--;
            Thread.sleep(500);
            ;
            if (retryCount == 0) {
              passed = false;
            }
          } else {
            retryCount = 0; 
		String w1ExpectedState = "01548";
		int w1ExpectedCode = 100;
		String w1ExpectedMessage = "Procedure or user-defined function JOB_DESCRIPTION_INFO in QSYS2 returned a warning SQLSTATE";
		if (!w1ExpectedState.equals(w1.getSQLState())) {
		    passed = false;
		    sb.append("\nError: w1ExpectedState sb " + w1ExpectedState + " but was "
			      + w1.getSQLState());
		} else {
		  sb.append("\nGot Expected state"); 
		}
		if (w1ExpectedCode != w1.getErrorCode()) {
		    passed = false;
		    sb.append("\nError: w1ExpectedCode sb " + w1ExpectedCode + " but was "
			      + w1.getErrorCode());
		} else {
		  sb.append("\nGot expected error code"); 
		}
		String w1Message = w1.getMessage();
		if (w1Message == null || (w1Message.indexOf(w1ExpectedMessage) < 0)) {
		    passed = false;
		    sb.append("\nError: w1ExpectedMessage sb " + w1ExpectedMessage
			      + " but was " + w1.getMessage());
		} else {
		  sb.append("\nGot expected message"); 
		}
	    }
	}
        
        /// Run the following query -- make sure we get multiple warnings.
        int statementWarningCount= 0;
        int resultSetWarningCount = 0;
        int rowCount = 0; 
        sql = "select * from qsys2.job_description_info"; 
        
        sb.append("\nExecuting "+sql);
        rs = s.executeQuery(sql);
        SQLWarning w11 = s.getWarnings();
        if (w11 != null) {
          statementWarningCount++; 
          s.clearWarnings();
        }
        while (rs.next()) {
          rowCount++; 
          w11 = s.getWarnings();
          if (w11 != null) {
            statementWarningCount++; 
            s.clearWarnings();
          }
          w11 = rs.getWarnings(); 
          if (w11 != null) {
            resultSetWarningCount++; 
            rs.clearWarnings();
          }
        
        }
        
        if ((statementWarningCount + resultSetWarningCount) == 0) {
          passed = false; 
          sb.append("\nError:  No warnings occurred from query: "+sql);
        } else {
          sb.append("\nstatementWarningCount="+statementWarningCount); 
          sb.append("\nresultSetWarningCount="+resultSetWarningCount);
          sb.append("\n             rowCount="+rowCount  ); 
        }
          
        
        
        s.close(); 
        if (passed) { 
          sql = "CALL QSYS2.QCMDEXC('DLTJOBD JOBD("+collection_+"/JDSTWJOBD)')"; 
          sb.append("\nExecuting "+sql); 
          pwrStatement.executeUpdate(sql); 
        }
        pwrStatement.close(); 
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (pwrConnection != null) { 
          try {
            pwrConnection.close();
          } catch (SQLException e) {
          } 
        }
      }
    } else {
	notApplicable("TOOLBOX V7R3 and later version"); 
    }

  }

  boolean checkWarning(SQLWarning w1, String w1ExpectedState,
      int w1ExpectedCode, String w1ExpectedMessage, StringBuffer sb) {
    boolean passed = true;
    if (!w1ExpectedState.equals(w1.getSQLState())) {
      passed = false;
      sb.append("\nFAILED: w1ExpectedState sb " + w1ExpectedState + " but was "
          + w1.getSQLState());
    }
    if (w1ExpectedCode != w1.getErrorCode()) {
      passed = false;
      sb.append("\nFAILED: w1ExpectedCode sb " + w1ExpectedCode + " but was "
          + w1.getErrorCode());
    }
    String w1Message = w1.getMessage();
    if (w1Message == null || (w1Message.indexOf(w1ExpectedMessage) < 0)) {
      passed = false;
      sb.append("\nFAILED: w1ExpectedMessage sb " + w1ExpectedMessage + " but was "
          + w1.getMessage());
    }
    return passed; 
  }          
 
  /**
   * getWarning() - Should return a truncation warning from a set statement 
   **/
  public void Var017() {
    StringBuffer sb = new StringBuffer();
    String sql;
    boolean passed = true;
    try {
      Statement s = connection_.createStatement();
      sql = "CREATE OR REPLACE VARIABLE " + collection_ + ".CHAR1 CHAR(1)";
      sb.append("\nRunning:" + sql);
      s.execute(sql);
      sql = "SET " + collection_ + ".CHAR1 = 'AB'";

      sb.append("\nRunning execute:" + sql);
      s.execute(sql);
      SQLWarning w1 = s.getWarnings();
      if (w1 == null) {
        passed = false;
        sb.append("\nFAILED: Warning is null");
      } else {
        if (!checkWarning(w1, "01004", 0, "", sb))
          passed = false;
        SQLWarning w2 = w1.getNextWarning();
        if (w2 != null) {
          passed = false;
          sb.append("\nFAILED:Second warning was not null but " + w2);
        }

      }

      sb.append("\nRunning executeUpdate:" + sql);
      s.executeUpdate(sql);
      w1 = s.getWarnings();
      if (w1 == null) {
        passed = false;
        sb.append("\nFAILED: Warning is null");
      } else {
        if (!checkWarning(w1, "01004", 0, "", sb))
          passed = false;
        SQLWarning w2 = w1.getNextWarning();
        if (w2 != null) {
          passed = false;
          sb.append("\nFAILED: Second warning was not null but " + w2);
        }

      }

      sb.append("\nPreparing: "+sql); 
      PreparedStatement ps = connection_.prepareStatement(sql); 
      sb.append("\nRunning execute"); 
      ps.execute(); 
      w1 = ps.getWarnings();
         if (w1 == null) {
        passed = false;
        sb.append("\nFAILED: Warning is null");
      } else {
        if (!checkWarning(w1, "01004", 0, "", sb))
          passed = false;
        SQLWarning w2 = w1.getNextWarning();
        if (w2 != null) {
          passed = false;
          sb.append("\nFAILED: Second warning was not null but " + w2);
        }

      }   
      
      sb.append("\nRunning executeUpdate"); 
      ps.executeUpdate(); 
      w1 = ps.getWarnings();
         if (w1 == null) {
        passed = false;
        sb.append("\nFAILED: Warning is null");
      } else {
        if (!checkWarning(w1, "01004", 0, "", sb))
          passed = false;
        SQLWarning w2 = w1.getNextWarning();
        if (w2 != null) {
          passed = false;
          sb.append("\nFAILED: Second warning was not null but " + w2);
        }

      }   
      ps.close(); 
      
      sql = "select * from SYSIBM.SYSDUMMY1 WHERE IBMREQD  = CAST('YES' AS CHAR(1))";
      sb.append("\nRunning execute:" + sql);
      s.execute(sql);
      w1 = s.getWarnings();
      if (w1 == null) {
        passed = false;
        sb.append("\nFAILED: Warning is null");
      } else {
        if (!checkWarning(w1, "01004", 0, "", sb))
          passed = false;
        SQLWarning w2 = w1.getNextWarning();
        if (w2 != null) {
          passed = false;
          sb.append("\nFAILED:Second warning was not null but " + w2);
        }

      }
      
      sb.append("\nRunning executeQuery:" + sql);
      s.executeQuery(sql);
      w1 = s.getWarnings();
      if (w1 == null) {
        passed = false;
        sb.append("\nFAILED: Warning is null");
      } else {
        if (!checkWarning(w1, "01004", 0, "", sb))
          passed = false;
        SQLWarning w2 = w1.getNextWarning();
        if (w2 != null) {
          passed = false;
          sb.append("\nFAILED:Second warning was not null but " + w2);
        }

      }
      
      sb.append("\nPreparing: "+sql); 
      ps = connection_.prepareStatement(sql); 
      sb.append("\nRunning execute"); 
      ps.execute(); 
      w1 = ps.getWarnings();
         if (w1 == null) {
        passed = false;
        sb.append("\nFAILED: Warning is null");
      } else {
        if (!checkWarning(w1, "01004", 0, "", sb))
          passed = false;
        SQLWarning w2 = w1.getNextWarning();
        if (w2 != null) {
          passed = false;
          sb.append("\nFAILED: Second warning was not null but " + w2);
        }

      }   
      
      sb.append("\nRunning executeQuery"); 
      ps.executeQuery(); 
      w1 = ps.getWarnings();
         if (w1 == null) {
        passed = false;
        sb.append("\nFAILED: Warning is null");
      } else {
        if (!checkWarning(w1, "01004", 0, "", sb))
          passed = false;
        SQLWarning w2 = w1.getNextWarning();
        if (w2 != null) {
          passed = false;
          sb.append("\nFAILED: Second warning was not null but " + w2);
        }

      }   
      ps.close(); 
      
      
      sql = "DROP VARIABLE " + collection_ + ".CHAR1";
      sb.append("\nRunning:" + sql);
      s.execute(sql);
      s.close();

      assertCondition(passed, sb);
    } catch (Exception e) {
      failed(e, sb);
    }

  }
  
}
