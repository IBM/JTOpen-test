///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementExceptions.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementExceptions.java
//
// Classes:      JDStatementExceptions
//
////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDStatementTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Date;
import java.sql.*;
import java.util.Random;

/**
 * Testcase JDStatementExceptions. Tests, in JDBC 4.0, the use of the following
 * exceptions.
 * 
 * <ul>
 * <li>0A -- SQLFeatureNotSupportedException
 * <li>08 -- SQLTransientConnectionException
 * <li>08 -- SQLNonTransientConnectionException
 * <li>22 -- SQLDataException
 * <li>23 -- SQLIntegrityConstraintVoilationException
 * <li>40 -- SQLTransactionRollbackException
 * <li>42 -- SQLSyntaxErrorException
 * <li>NA -- SQLInvalidAuthorizationException
 * <li>NA -- SQLTimeoutException
 * </ul>
 **/
public class JDStatementExceptions extends JDTestcase {
  public static final String added = "-- added by native driver for JDBC 4.0 07/11/2006";

  // Private data.
  private Connection connection_, connectionPwr_;
  private Statement stmt_;

  /**
   * Constructor.
   **/
  public JDStatementExceptions(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, "JDStatementExceptions", namesAndVars, runMode,
        fileOutputStream, password);
    pwrSysUserID_ = pwrSysUserID;
    pwrSysEncryptedPassword_ = PasswordVault.getEncryptedPassword(pwrSysPassword);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
    // Added prefetch=false for variation 14
    connectionPwr_ = testDriver_.getConnection(baseURL_ + ";prefetch=false",
        pwrSysUserID_, pwrSysEncryptedPassword_);
    stmt_ = connection_.createStatement();

  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    stmt_.close();
    connection_.close();
  }

  protected void checkException(Exception ex, String checkName,
      String message) {
    String checkedNames = "";
    Class exClass = ex.getClass();
    String name = exClass.getName();
    boolean found = false;
    while ((!name.equals("java.lang.Object")) && (!found)) {
      int index = name.indexOf(checkName);
      if (index >= 0) {
        found = true;
      } else {
        checkedNames += name + " ";
        exClass = exClass.getSuperclass();
        name = exClass.getName();
      }
    }
    if (found) {
      assertCondition(true);
    } else {
      // Print the stack trace in the failing case
      ex.printStackTrace();
      if (ex instanceof SQLException) {
        failed(ex + " not instance of " + checkName + " found : " + checkedNames
            + " SQLstate=" + ((SQLException) ex).getSQLState() + " SQLcode="
            + ((SQLException) ex).getErrorCode());

      } else {
        failed(
            ex + " not instance of " + checkName + " found : " + checkedNames);
      }
    }

  }

  /**
   * check for OA SQLFeatureNotSupportedException
   **/
  public void Var001() {
    if (checkJdbc40()) {
      try {
        Object[] attributes = new Object[5];
        JDReflectionUtil.callMethod_O(connection_, "createStruct", "typeName",
            attributes);
        failed("Didn't throw exception " + added);
      } catch (Exception ex) {
        checkException(ex, ".SQLFeatureNotSupportedException", added);
      }

    }
  }

  /**
   * check for 0A SQLFeatureNotSupportedException SQL0752
   **/
  public void Var002() {
    if (checkJdbc40()) {
      notApplicable("TODO:  find a way to get the DB to issue a SQL0752");
    }
  }

  /**
   * check for 08 -- SQLTransientConnectionException
   */
  public void Var003() {
    if (checkJdbc40()) {

      notApplicable(
          "TODO:  find a way to get the DB to issue a 08xxx -- transient -- Currently not possible with native driver");
    }
  }

  class KillThread extends Thread {
    Connection c = null;
    StringBuffer sb = null;

    public KillThread(Connection c, StringBuffer sb) {
      this.c = c;
      this.sb = sb;
    }

    public void run() {
      try {
        Statement stmt = c.createStatement();
        String sql = "call QSYS.QCMDEXC('"
            + "ENDJOB JOB(*) OPTION(*IMMED)                                                                '"
            + ", 0000000080.00000) ";
        sb.append("Calling " + sql + "\n");
        stmt.executeQuery(sql);
        sb.append("Execute done\n");
        Thread.sleep(1);
      } catch (Exception e) {
        // e.printStackTrace();
        sb.append("Exception occured " + e.toString());
      }
    }

  }

  /**
   * check for 08 -- SQLNonTransientConnectionException -- Check the case where
   * the server has been killed
   **/
  public void Var004() {
    if (checkJdbc40()) {
      if (isToolboxDriver() && getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
        notApplicable("command does not work on v5r3");
        return;
      }
      int subDriver = testDriver_.getSubDriver();
      if (subDriver == JDTestDriver.SUBDRIVER_JTOPENCA
          || subDriver == JDTestDriver.SUBDRIVER_JTOPENSF) {
        notApplicable("client recovers for client affinities");
        return;
      }

      Connection c1 = null;
      try {
        c1 = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
        Statement s1 = c1.createStatement();

        //
        // Start a thread to kill the connection
        //
        StringBuffer stringBuffer = new StringBuffer();
        KillThread killThread = new KillThread(c1, stringBuffer);
        killThread.start();

        //
        // Wait for the thread to finish killing
        //
        killThread.join();

        ResultSet rs = s1.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
        rs.next();

        failed("Didn't throw exception " + added + " but got rs " + rs
            + " kill thread says\"" + stringBuffer.toString() + "\"");
      } catch (Exception ex) {
        checkException(ex, ".SQLNonTransientConnectionException", added);
      } finally {
        if (c1 != null) {
          try {
            c1.close();
          } catch (Exception e) {
            // do nothing
          }
        }
      }

    }
  }

  /**
   * check for 08 -- SQLNonTransientConnectionException -- Check the case a
   * connection call cannot be made.
   **/
  public void Var005() {
    if (checkJdbc40()) {
      Connection c1 = null;
      try {
        c1 = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
        Statement s1 = c1.createStatement();

        String sql;

        sql = "call QSYS.QCMDEXC      ('RMVRDBDIRE RDB(JDSTMTEXCNOEXIST)                '"
            + ", 0000000040.00000) ";

        try {
          s1.executeUpdate(sql);

        } catch (Exception e) {
          // Ignore
        }
        // Add a fake RDB entry on the system
        sql = "call QSYS.QCMDEXC( 'ADDRDBDIRE RDB(JDSTMTEXCNOEXIST) RMTLOCNAME(''192.168.2.3'' *IP)             '"
            + ", 0000000070.00000) ";
        s1.executeUpdate(sql);
        // Create a procedure to connect to the fake RDB entry
        String procedureName = JDStatementTest.COLLECTION + ".JDSTMTEX05";
        try {
          s1.executeUpdate("DROP PROCEDURE " + procedureName);
        } catch (Exception e) {

        }
        s1.executeUpdate("create procedure " + procedureName + "()"
            + " language sql begin connect to JDSTMTEXCNOEXIST; end");

        System.out.println(
            "Attempting to connect to JDSTMTEXCNOEXIST -- this will take a while to timeout (about 180 seconds)");

        s1.executeUpdate("call " + procedureName + "()");

        failed("Didn't throw exception " + added);
      } catch (Exception ex) {
        checkException(ex, ".SQLNonTransientConnectionException", added);
      } finally {
        if (c1 != null) {
          try {
            c1.close();
          } catch (Exception e) {
            // do nothing
          }
        }
      }

    }
  }

  /**
   * check for 22 -- SQLDataException
   **/
  public void Var006() {
    if (checkJdbc40()) {
      String table = JDStatementTest.COLLECTION + ".JDSTMTEX06";
      try {
        initTable(stmt_, table, " (c1 int)");
        stmt_.executeUpdate("insert into " + table + " values( 1/0 )");
        failed("Didn't throw exception " + added);
      } catch (Exception ex) {
        checkException(ex, ".SQLDataException", added);
      } finally {
        try {
          stmt_.executeUpdate("Drop table " + table);
        } catch (Exception e) {

        }
      }

    }
  }

  /**
   * check for23 -- SQLIntegrityConstraintViolationException -- SQL0603
   **/

  public void Var007() {
    if (checkJdbc40()) {
      String table = JDStatementTest.COLLECTION + ".JDSTMTEX07";
      try {
        initTable(stmt_, table, " (c1 int)");
        stmt_.executeUpdate("insert into " + table + " values( 3 )");
        stmt_.executeUpdate("insert into " + table + " values( 3 )");
        stmt_.executeUpdate("CREATE UNIQUE INDEX " + JDStatementTest.COLLECTION
            + ".JDSTMTEI06 ON " + table + " (C1)");
        failed("Didn't throw exception " + added);
      } catch (Exception ex) {
        // tb sometimes SQLSyntaxErrorException is returned
        if (isToolboxDriver())
          checkException(ex, ".SQLException", added);
        else
          checkException(ex, ".SQLIntegrityConstraintViolationException",
              added);
      } finally {
        try {
          stmt_.executeUpdate("Drop table " + table);
        } catch (Exception e) {

        }
      }

    }
  }

  /**
   * check for 40 -- SQLTransactionRollbackException SQL0913 -- Row in use
   * 
   * This no longer returns 40502, but returns 57003
   **/
  public void Var008() {
    if (checkJdbc40()) {
      Connection c1 = null;
      Connection c2 = null;
      try {

        c1 = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
        c1.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        c1.setAutoCommit(false);
        Statement s1 = c1.createStatement();
        String table = JDStatementTest.COLLECTION + ".JDSTMTEX08";

        initTable(s1, table, " (c1 int)");
        s1.executeUpdate("insert into  " + table + " values(3)");

        c2 = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
        c1.setAutoCommit(false);
        c2.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        Statement s2 = c2.createStatement();
        s2.executeUpdate("insert into  " + table + " values(3)");
        ResultSet rs = s2.executeQuery("SELECT * FROM " + table);

        failed("Didn't throw exception " + added + " but got rs " + rs);
      } catch (Exception ex) {
        if (isToolboxDriver())
          checkException(ex, ".SQLException", added);
        else
          checkException(ex, ".SQLException", added);
      } finally {
        if (c1 != null) {
          try {
            c1.rollback();
            c1.close();
          } catch (Exception e) {
            // do nothing
          }
        }
        if (c2 != null) {
          try {
            c2.rollback();
            c2.close();
          } catch (Exception e) {
            // do nothing
          }
        }
      }

    }
  }

  /**
   * check for 42 -- SQLSyntaxErrorException
   **/
  public void Var009() {
    if (checkJdbc40()) {
      try {
        stmt_.executeUpdate("select XXXX");
        failed("Didn't throw exception " + added);
      } catch (Exception ex) {
        checkException(ex, ".SQLSyntaxErrorException", added);
      }
    }
  }

  /**
   * check for 42 -- SQLSyntaxErrorException -- detected by driver
   **/
  public void Var010() {
    if (checkJdbc40()) {
      try {
        stmt_.executeUpdate(null);
        failed("Didn't throw exception " + added);
      } catch (Exception ex) {
        checkException(ex, ".SQLSyntaxErrorException", added);
      }
    }
  }

  /**
   * check for NA -- SQLInvalidAuthorizationException
   **/
  public void Var011() {

       if (proxy_ != null && (!proxy_.equals(""))) {
            notApplicable("Specified exception not returned by proxy driver");
            return;
        }

    if (checkJdbc40()) {
      try {
        String prompt = "";
        if (isToolboxDriver()) {
          prompt = ";prompt=false";
        }
        Connection c = DriverManager.getConnection(baseURL_ + prompt, "FRED",
            "FRED");
        stmt_.executeUpdate(null);
        failed("Didn't throw exception for" + c + added);
      } catch (Exception ex) {
        if (isToolboxDriver()) {
          String proxy = System
              .getProperty("com.ibm.as400.access.AS400.proxyServer");
          if (proxy == null)
            checkException(ex, ".SQLNonTransientConnectionException", added);
          else
            checkException(ex, ".SQLException", added); // proxy server throws
                                                        // this
        } else
          checkException(ex, ".SQLInvalidAuthorizationSpecException", added);
      }
    }
  }

  /**
   * check for NA -- SQLInvalidAuthorizationException
   **/
  public void Var012() {
    if (checkJdbc40()) {
      try {
        String prompt = "";
        if (isToolboxDriver()) {
          prompt = ";prompt=false";
        }
        Connection c = DriverManager.getConnection(baseURL_ + prompt, "QSECOFR",
            "FRED");
        failed("Didn't throw exception for " + c + added);
      } catch (Exception ex) {
        if (isToolboxDriver()) {
          String proxy = System
              .getProperty("com.ibm.as400.access.AS400.proxyServer");
          if (proxy == null)
            checkException(ex, ".SQLNonTransientConnectionException", added);
          else
            checkException(ex, ".SQLException", added); // proxy server throws
                                                        // this
        } else
          checkException(ex, ".SQLInvalidAuthorizationSpecException", added);
      }
    }
  }

  /**
   * check for 28 -- SQLInvalidAuthorizationException SQL0567
   **/
  public void Var013() {
    if (checkJdbc40()) {
      try {
        stmt_.executeUpdate("set session authorization QSYS");
        failed("Didn't throw exception " + added);
      } catch (Exception ex) {
        checkException(ex, ".SQLInvalidAuthorizationSpecException", added);
      }
    }
  }

  /**
   * check for NA -- SQLTimeoutException
   **/

  public void Var014() {

    if (checkJdbc40()) {
      Statement s = null;
      String query = "UNSET";
      try {
        Random random = new Random();
        boolean retry = true;
        int columnCount = 6;
        long start = 0;
        long finish = 0;
        while (retry) {
          retry = false;
          query = "SELECT * FROM ";
          String orderbyClause = " ORDER BY ";
          boolean firstOrderBy = true;
          for (int i = 0; i < columnCount; i++) {
            if (firstOrderBy) {
              firstOrderBy = false;
            } else {
              orderbyClause += ",";
            }
            int whichTable = random.nextInt(5);
            switch (whichTable) {
            case 0:
              query += "QSYS2.SYSTABLES AS A" + i + ",";
              orderbyClause += "A" + i + ".TABLE_NAME";
              break;
            case 1:
              query += "QSYS2.SYSTABLES AS B" + i + ",";
              orderbyClause += "B" + i + ".TABLE_NAME";
              break;
            case 2:
              query += "QSYS2.SYSTABLES AS C" + i + ",";
              orderbyClause += "C" + i + ".TABLE_NAME";
              break;
            case 3:
              query += "QSYS2.SYSVIEWS AS D" + i + ",";
              orderbyClause += "D" + i + ".TABLE_NAME";
              break;
            case 4:
            default:
              query += "QSYS2.SYSCOLUMNS AS E" + i + ",";
              orderbyClause += "E" + i + ".TABLE_NAME";
            }
          }

          query += " QSYS2.SYSINDEXES AS F" + random.nextInt(1000); /* @F1C */
          // 33923 - v5r5 optimizer is smarter...
          if (getRelease() >= JDTestDriver.RELEASE_V7R1M0)
            query += orderbyClause + " optimize for all rows ";
          s = connectionPwr_.createStatement();
          //
          // The timeout must be low.. The timeout ends after the
          // execute..This means that the prefetch may cause the
          // query to look like it is hanging (but prefetch is happening)
          //
          start = System.currentTimeMillis();
          s.setQueryTimeout(1);
          s.executeQuery(query);
          finish = System.currentTimeMillis();
          if ((finish - start) < 4000) {
            retry = true;
            columnCount++;
          }
        }
        failed("Didn't throw timeout exception for query '" + query + "' time="
            + (finish - start) + " ms " + added);
      } catch (Exception ex) {
        if (isToolboxDriver())
          checkException(ex, ".SQLException", added + " Query=" + query);
        else
          checkException(ex, ".SQLTimeout", added + " Query=" + query);
      } finally {
        if (s != null) {
          try {
            s.close();
          } catch (Exception e) {
            // ignore
          }
        }
      }
    }
  }

  /**
   * Check for exceptions from FETCH from a stored procedure call. Fix for CPS
   * 9XYKBW and defect SE62364
   */

  public void Var015() {
    Connection c1 = null;
    Connection c2 = null;
    StringBuffer sb = new StringBuffer(
        " -- added 7/16/2015 for CPS 9XYKBW / SE62364 (JTOpen 8.5 no lock exceptions) \n");
    boolean passed = true;
    String sql = null;

    try {

      c1 = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
      c1.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
      c1.setAutoCommit(false);
      Statement s1 = c1.createStatement();
      String tablename = JDStatementTest.COLLECTION + ".LOCKME";
      try {
        sql = "DROP TABLE " + tablename;
        sb.append(new Date() + "Executing: " + sql + "\n");
        s1.executeUpdate(sql);
      } catch (Exception e) {
        sb.append(new Date() + "..Warning exception: " + e.toString());
      }
      sql = "CREATE TABLE " + tablename + "(C1 INT)";
      sb.append(new Date() + "Executing: " + sql + "\n");
      s1.executeUpdate(sql);
      c1.commit();
      for (int i = 1; i <= 5; i++) {
        sql = "INSERT INTO " + tablename + " VALUES(" + i + ")";
        sb.append(new Date() + "Executing: " + sql + "\n");
        s1.executeUpdate(sql);
      }

      String procname = JDStatementTest.COLLECTION + ".LOCKPROC";
      try {
        sql = "DROP PROCEDURE " + procname;
        sb.append(new Date() + "Executing: " + sql + "\n");
        s1.executeUpdate(sql);
      } catch (Exception e) {
        sb.append(new Date() + "..Warning exception: " + e.toString());
      }

      sql = "CREATE PROCEDURE " + procname
          + "() LANGUAGE SQL BEGIN DECLARE ANSWER  CHAR(5); DECLARE CUR1 CURSOR  FOR SELECT C1 FROM "
          + tablename
          + " FOR UPDATE OF C1; OPEN CUR1; FETCH FROM CUR1 INTO ANSWER; END";
      sb.append(new Date() + "Executing: " + sql + "\n");
      s1.executeUpdate(sql);

      c1.commit();

      sql = "SELECT C1 FROM " + tablename + " FOR UPDATE OF C1";
      sb.append(new Date() + "Executing: " + sql + "\n");
      ResultSet rs = s1.executeQuery(sql);
      rs.next();
      rs.next();
      // This should leave a lock on the object and cause the
      // fetch to fail.

      // Open a new connection and call the procedure
      // This should result in an exception

      c2 = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
      c2.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
      {
        Statement s2 = c2.createStatement();
        // Change the default wait time to 5 seconds
        sql = "CALL QSYS.QCMDEXC('CHGJOB DFTWAIT(5)                 ',000000020.00000)";
        sb.append(new Date() + "Executing  " + sql + "\n");
        s2.executeUpdate(sql);
        s2.close();
      }
      try {
        sql = "CALL " + procname + "()";
        sb.append(new Date() + "Preparing  " + sql + "\n");
        CallableStatement cs2 = c2.prepareCall(sql);
        sb.append(new Date() + "Executing  " + sql + "\n");
        cs2.execute();

        sb.append(new Date() + "FAILED:  Exception should have been thrown\n");
        passed = false;

      } catch (Exception e) {
        String expectedMessage = "IN USE";
        String errorMessage = e.toString().toUpperCase();
        e.printStackTrace(System.out);

        if (errorMessage.indexOf(expectedMessage) < 0) {
          sb.append(new Date() + "ERROR: ErrorMessage:" + errorMessage
              + " did not have " + expectedMessage);
          passed = false;

        }
        try {
          Statement s2 = c2.createStatement();

          sb.append(new Date() + "Executing  " + sql + "\n");
          s2.execute(sql);

          sb.append(
              new Date() + "FAILED:  Exception should have been thrown\n");
          passed = false;

        } catch (Exception e2) {
          expectedMessage = "IN USE";
          errorMessage = e2.toString().toUpperCase();
          e2.printStackTrace(System.out);

          if (errorMessage.indexOf(expectedMessage) < 0) {
            sb.append(new Date() + "ERROR: ErrorMessage:" + errorMessage
                + " did not have " + expectedMessage);
            passed = false;

          }

        }

      }

      // Cleanup
      sql = "DROP PROCEDURE " + procname;
      sb.append(new Date() + "Executing: " + sql + "\n");
      s1.executeUpdate(sql);

      sql = "DROP TABLE " + tablename;
      sb.append(new Date() + "Executing: " + sql + "\n");
      s1.executeUpdate(sql);

      c1.close();
      c1 = null;
      c2.close();
      c2 = null;
      assertCondition(passed, sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + sb.toString());
    } finally {
      if (c1 != null) {
        try {
          c1.close();
          c1 = null;
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if (c2 != null) {
        try {
          c2.close();
          c2 = null;
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void Var016() {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    sb.append("New long message test added 01/16/2020\n");
    /* Test a long warning message */
    /* Fixed in toolbox January 2020 */
    try {
      String warningSql = "BEGIN SIGNAL SQLSTATE '38T00' SET MESSAGE_TEXT = '1         2         3         4         5         6         7         8         9         100       1         2         3         4         5         6         7         8         9         '; END";
      try {
        Statement stmt = connection_.createStatement();
        stmt.execute(warningSql);
        passed = false;
        sb.append("Did not throw exception");
      } catch (SQLException sqlex) {

        String state;

        state = sqlex.getSQLState();
        if ("38T00".equals(state)) {
        } else {
          passed = false;
          sb.append("State is " + state + " sb 38T00\n");
        }
        String message = sqlex.getMessage();
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

      }
      assertCondition(passed, sb.toString());
    } catch (Exception e) {

      failed(e, "Unexpected Exception " + sb.toString());

    }

  }

  public void Var017() {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    sb.append("New long message test added 01/16/2020\n");
    /* Test a long warning message */
    /* Fixed in toolbox January 2020 */
    try {
      String createSql = "create or replace procedure "+collection_+".throwit() language sql BEGIN SIGNAL SQLSTATE '38T00' SET MESSAGE_TEXT = '1         2         3         4         5         6         7         8         9         100       1         2         3         4         5         6         7         8         9         '; END";
      
      String warningSql = "call "+collection_+".throwit()";
      try {
        Statement stmt = connection_.createStatement();
        stmt.execute(createSql); 
        stmt.execute(warningSql);
        passed = false;
        sb.append("Did not throw exception");
      } catch (SQLException sqlex) {

        String state;

        state = sqlex.getSQLState();
        if ("38T00".equals(state)) {
        } else {
          passed = false;
          sb.append("State is " + state + " sb 38T00\n");
        }
        String message = sqlex.getMessage();
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

      }
      assertCondition(passed, sb.toString());
    } catch (Exception e) {

      failed(e, "Unexpected Exception " + sb.toString());

    }

  }


  
  public void Var018() {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    sb.append("New long message test added 01/16/2020\n");
    /* Test a long warning message */
    /* Fixed in toolbox January 2020 */
    try {
      String errorSql = "call QSYS2.QCMDEXC('DEL A234567891234567892123456789312345678941234567895123456789612345678971234567898')";
      try {
        Statement stmt = connection_.createStatement();
        stmt.execute(errorSql);
        passed = false;
        sb.append("Did not throw exception");
      } catch (SQLException sqlex) {

        String state;

        state = sqlex.getSQLState();
        if ("38501".equals(state)) {
        } else {
          passed = false;
          sb.append("State is " + state + " sb 38501\n");
        }
        String message = sqlex.getMessage();
        String expected = "[CPFA0A9] Object not found.  Object is A234567891234567892123456789312345678941234567895123456789612345678971234567898.";
	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	    expected = "Object not found.  Object is A234567891234567892123456789312345678941234567895123456789612345678971234567898."; 
	}

        if (expected.equals(message)) {
          // Passed
        } else {
          passed = false;
          sb.append("Got Message '" + message + "'\n");
          sb.append("         sb '" + expected + "'\n");
        }

      }
      assertCondition(passed, sb.toString());
    } catch (Exception e) {

      failed(e, "Unexpected Exception " + sb.toString());

    }

  }

  
  
}
