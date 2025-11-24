///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCPDSConnectionCreateStatement.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCPDSConnectionCreateStatement.java
//
// Classes:      JDCPDSConnectionCreateStatement
//
////////////////////////////////////////////////////////////////////////

package test.JD.CPDS;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import javax.sql.DataSource;

import com.ibm.as400.access.AS400;

import test.JDCPDSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

/**
 * Testcase JDCPDSConnectionCreateStatement. This tests the following methods of
 * the JDBC Connection class:
 * 
 * <ul>
 * <li>createStatement()
 * <li>prepareStatement()
 * <li>prepareCall()
 * </ul>
 **/
public class JDCPDSConnectionCreateStatement extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCPDSConnectionCreateStatement";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCPDSTest.main(newArgs); 
   }

  //
  // Changed 3/3/05 to use connection object since we always compile
  // the testcases using JDK 1.4
  //
  // Using the implemented type DB2ConnectionHandle instead of plain
  // java.sql.Connection for the connection_ object, since the
  // createStatement(int,int)
  // method is not defined in the interface util JDBC 2.0 (9/99). The connection
  // is of type DB2ConnectionHandle returned by the pooled connection.
  // This cannot be cast to DB2Connection

  // Private data.
  private Connection closedConnection_;
  private static String table_ = JDCPDSTest.COLLECTION + ".JDCCS";
  private static String keysTable_ = JDCPDSTest.COLLECTION + ".JDGENERATEDKEYS";
  private DataSource db2ConnectionPoolDataSource;

  /**
   * Constructor.
   **/
  public JDCPDSConnectionCreateStatement(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream,

      String password) {
    super(systemObject, "JDCPDSConnectionCreateStatement", namesAndVars, runMode, fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void setup() throws Exception {
    table_ = JDCPDSTest.COLLECTION + ".JDCCS";
    if (isJdbc20StdExt()) {
      String clearPassword_ = PasswordVault.decryptPasswordLeak(encryptedPassword_);

      db2ConnectionPoolDataSource = (DataSource) JDReflectionUtil
          .createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
      JDReflectionUtil.callMethod_V(db2ConnectionPoolDataSource, "setDatabaseName", system_);
      JDReflectionUtil.callMethod_V(db2ConnectionPoolDataSource, "setUser", userId_);
      JDReflectionUtil.callMethod_V(db2ConnectionPoolDataSource, "setPassword", clearPassword_);
      connection_ = db2ConnectionPoolDataSource.getConnection();
      connection_.clearWarnings();

      Statement s = connection_.createStatement();
      try {
        s.executeUpdate("DROP TABLE " + table_);
      } catch (Exception e) {
      }
      s.executeUpdate("CREATE TABLE " + table_ + " (COL1 VARCHAR(15))");
      s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES ('Initial')");

      //
      // Setup for keys testing
      //
      {
        keysTable_ = JDCPDSTest.COLLECTION + ".JDGENERATEDKEYS";
        try {
          s.executeUpdate("DROP TABLE " + keysTable_);
        } catch (Exception e) {
        }
        s.executeUpdate("CREATE TABLE " + keysTable_ + " (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY)");
        s.execute("INSERT INTO " + keysTable_ + " (NAME) VALUES ('first')", Statement.RETURN_GENERATED_KEYS);
      }

      s.close();
      JDSetupProcedure.create(systemObject_, connection_, JDSetupProcedure.STP_CSPARMS, supportedFeatures_,
          collection_);

      closedConnection_ = db2ConnectionPoolDataSource.getConnection();
      closedConnection_.close();
    }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    if (isJdbc20StdExt()) {
      Statement s = (Statement) connection_.createStatement();
      s.executeUpdate("DROP TABLE " + table_);
      s.executeUpdate("DROP TABLE " + keysTable_);
      s.close();

      connection_.close();
      connection_ = null; 

    }
  }

  /**
   * Checks whether a result set is scrollable.
   **/
  private boolean checkScrollable(ResultSet rs) {
    try {
      rs.last();
      rs.previous();
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether a result set is updatable.
   **/
  private boolean checkUpdatable(ResultSet rs) {

    try {
      rs.next();
      rs.updateString(1, "Hello");
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  /**
   * Allocates statements until the connection maximum has been reached, and
   * returns the number of statements allocated.
   * 
   * @param c The connection.
   * @return The number of statements.
   **/
  private int fillConnection(Connection c) {
    int numberOfStatements = 0;

    try {
      // Allow for the possibility that the driver
      // incorrectly allows more than the upper
      // limit.
      for (int i = 0; i < 600; ++i) {

        // System.out.println("connection number " + i);
        // if (i == 26)
        // {
        // i = i;
        // }

        // Allocate a different kind of statement
        // each time, just for fun.
        switch (i % 3) {
        case 0:
          c.createStatement();
          break;
        case 1:
          c.prepareStatement("SELECT * FROM QSYS2.SYSTABLES");
          break;
        case 2:
          c.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)");
          break;
        }

        ++numberOfStatements;
      }
    } catch (SQLException e) {
      // Ignore. This will happen when the connection is full.
    }

    return numberOfStatements;
  }

  /**
   * createStatement() with 0 parameters - Create a statement in a connection.
   * Verify the result set type and concurrency in the statement.
   **/
  public void Var001() {
    if (checkJdbc20StdExt()) {
      try {
        Statement s = (Statement) connection_.createStatement();
        int resultSetType = s.getResultSetType();
        int resultSetConcurrency = s.getResultSetConcurrency();
        s.close();
        assertCondition(
            (resultSetType == ResultSet.TYPE_FORWARD_ONLY) && (resultSetConcurrency == ResultSet.CONCUR_READ_ONLY));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * createStatement() - Create a statement in a closed connection. Should throw
   * an exception. Initially thrw a NullPointerException. Now, it correctly throws
   * SQLException
   **/

  public void Var002() {
    if (checkJdbc20StdExt()) {
      try {
        Statement s = closedConnection_.createStatement();
        failed("Did not throw exception. but got " + s);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * createStatement() with 2 parameters - Create a statement in a connection.
   * Verify the result set type and concurrency in the statement.
   **/
  public void Var003() {
    if (checkJdbc20StdExt()) {
      try {
        boolean dummy = connection_.getAutoCommit();
        Statement s = (Statement) connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE);
        int resultSetType = s.getResultSetType();
        int resultSetConcurrency = s.getResultSetConcurrency();
        SQLWarning w = connection_.getWarnings();
        s.close();
        assertCondition((resultSetType == ResultSet.TYPE_SCROLL_SENSITIVE)
            && (resultSetConcurrency == ResultSet.CONCUR_UPDATABLE) && (w == null), "dummy=" + dummy);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /*
   * 
   * /** createStatement() with 2 parameters - Create a statement with a bogus
   * result set type.
   **/
  public void Var004() {
    if (checkJdbc20StdExt()) {
      try {
        Statement s = (Statement) connection_.createStatement(4378, ResultSet.CONCUR_UPDATABLE);
        failed("Did not throw exception for " + s);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * createStatement() with 2 parameters - Create a statement with a bogus result
   * set concurrency.
   **/
  public void Var005() {
    if (checkJdbc20StdExt()) {
      try {
        Statement s = (Statement) connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 1027);
        failed("Did not throw exception but got " + s);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * createStatement() with 2 parameters - Create a statement in a closed
   * connection. Should throw an exception.
   **/
  public void Var006() {
    if (checkJdbc20StdExt()) {
      try {
        Statement s = closedConnection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        failed("Did not throw exception but got " + s);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException"); // see comments in var002
      }
    }
  }

  /**
   * createStatement() with 2 parameters - Create a statement as forward-only and
   * read-only.
   **/
  public void Var007() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        Statement s = (Statement) connection_.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery("SELECT * FROM " + table_);
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w == null) && (type == ResultSet.TYPE_FORWARD_ONLY)
            && (concurrency == ResultSet.CONCUR_READ_ONLY) && (updatable == false) && (scrollable == false));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * createStatement() with 2 parameters - Create a statement as forward-only and
   * updatable.
   **/
  public void Var008() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        Statement s = (Statement) connection_.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w == null) && (type == ResultSet.TYPE_FORWARD_ONLY)
            && (concurrency == ResultSet.CONCUR_UPDATABLE) && (updatable == true) && (scrollable == false));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * createStatement() with 2 parameters - Create a statement as scroll
   * insensitive and read-only.
   **/
  public void Var009() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        Statement s = (Statement) connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery("SELECT * FROM " + table_);
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w == null) && (type == ResultSet.TYPE_SCROLL_INSENSITIVE)
            && (concurrency == ResultSet.CONCUR_READ_ONLY) && (updatable == false) && (scrollable == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * createStatement() with 2 parameters - Create a statement as scroll
   * insensitive and updatable.
   **/
  public void Var010() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        Statement s = (Statement) connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_UPDATABLE);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w != null) && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
            && (concurrency == ResultSet.CONCUR_UPDATABLE) && (updatable == true) && (scrollable == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * createStatement() with 2 parameters - Create a statement as scroll sensitive
   * and read-only.
   **/
  public void Var011() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        Statement s = (Statement) connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery("SELECT * FROM " + table_);
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) // @C1
        {
          boolean condition = ((w == null) && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
              && (concurrency == ResultSet.CONCUR_READ_ONLY) && (updatable == false) && (scrollable == true));
          if (!condition) {
            System.out.println("w = " + w);
            System.out.println("updatable = " + updatable);
            System.out.println("scrollable = " + scrollable);
          }
          assertCondition(condition);
        } else {
          assertCondition((w != null) && (type == ResultSet.TYPE_SCROLL_INSENSITIVE)
              && (concurrency == ResultSet.CONCUR_READ_ONLY) && (updatable == false) && (scrollable == true));
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * createStatement() with 2 parameters - Create a statement as scroll sensitive
   * and updatable.
   **/
  public void Var012() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        Statement s = (Statement) connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery("SELECT * FROM " + table_ + " FOR UPDATE");
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w == null) && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
            && (concurrency == ResultSet.CONCUR_UPDATABLE) && (updatable == true) && (scrollable == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareStatement() - Create a prepared statement in a connection. Verify the
   * result set type and concurrency in the statement.
   **/
  public void Var013() {
    if (checkJdbc20StdExt()) {
      try {
        PreparedStatement ps = (PreparedStatement) connection_.prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
        int resultSetType = ps.getResultSetType();
        int resultSetConcurrency = ps.getResultSetConcurrency();
        ps.close();
        assertCondition(
            (resultSetType == ResultSet.TYPE_FORWARD_ONLY) && (resultSetConcurrency == ResultSet.CONCUR_READ_ONLY));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareStatement() - Pass null for SQL statement. Should throw an exception.
   **/
  public void Var014() {
    if (checkJdbc20StdExt()) {
      try {
        PreparedStatement ps = (PreparedStatement) connection_.prepareStatement(null);
        failed("Did not throw exception but got " + ps);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareStatement() - Pass an empty string for SQL statement. Should throw an
   * exception.
   **/
  public void Var015() {
    if (checkJdbc20StdExt()) {
      try {
        PreparedStatement ps = (PreparedStatement) connection_.prepareStatement("");
        failed("Did not throw exception but got " + ps);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareStatement() - Pass a blank string for SQL statement. Should throw an
   * exception.
   **/
  public void Var016() {
    if (checkJdbc20StdExt()) {
      try {
        PreparedStatement ps = (PreparedStatement) connection_.prepareStatement(" ");
        failed("Did not throw exception but got " + ps);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareStatement() - Pass an bogus string for SQL statement. Should throw an
   * exception.
   **/
  public void Var017() {
    if (checkJdbc20StdExt()) {
      try {
        PreparedStatement ps = (PreparedStatement) connection_.prepareStatement("CLIF");
        failed("Did not throw exception but got " + ps);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareStatement() - Create a prepared statement in a closed connection.
   * Should throw an exception.
   **/
  public void Var018() {
    if (checkJdbc20StdExt()) {
      try {
        PreparedStatement ps = closedConnection_.prepareStatement("SELECT * FROM QIWS.QCUSTCDT");
        failed("Did not throw exception but got " + ps);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException"); // see comments in var002
      }
    }
  }

  /**
   * prepareStatement() with 3 parameters - Pass null for SQL statement. Should
   * throw an exception.
   **/
  public void Var019() {
    if (checkJdbc20StdExt()) {
      try {
        PreparedStatement ps = (PreparedStatement) connection_.prepareStatement(null, ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_UPDATABLE);
        failed("Did not throw exception but got " + ps);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareStatement() with 3 parameters - Pass an empty string for SQL
   * statement. Should throw an exception.
   **/
  public void Var020() {
    if (checkJdbc20StdExt()) {
      try {
        PreparedStatement ps = (PreparedStatement) connection_.prepareStatement("", ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
        failed("Did not throw exception but got " + ps);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareStatement() with 3 parameters - Pass a blank string for SQL statement.
   * Should throw an exception.
   **/
  public void Var021() {
    if (checkJdbc20StdExt()) {
      try {
        PreparedStatement ps = (PreparedStatement) connection_.prepareStatement(" ", ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
        failed("Did not throw exception but got " + ps);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareStatement() with 3 parameters - Pass a bogus string for SQL statement.
   * Should throw an exception.
   **/
  public void Var022() {
    if (checkJdbc20StdExt()) {
      try {
        PreparedStatement ps = (PreparedStatement) connection_.prepareStatement("CLIF",
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        failed("Did not throw exception but got " + ps);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareStatement() with 3 parameters - Pass an bad value for result set type.
   * Should throw an exception.
   **/
  public void Var023() {
    if (checkJdbc20StdExt()) {
      try {
        PreparedStatement ps = (PreparedStatement) connection_.prepareStatement("SELECT * FROM QIWS.QCUSTCDT", -574,
            ResultSet.CONCUR_READ_ONLY);
        failed("Did not throw exception but got " + ps);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareStatement() with 3 parameters - Pass an bad value for result set
   * concurrency. Should throw an exception.
   **/
  public void Var024() {
    if (checkJdbc20StdExt()) {
      try {
        PreparedStatement ps = (PreparedStatement) connection_.prepareStatement("SELECT * FROM QIWS.QCUSTCDT",
            ResultSet.TYPE_SCROLL_INSENSITIVE, -754);
        failed("Did not throw exception but got " + ps);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareStatement() with 3 parameters - Create a prepared statement in a
   * closed connection. Should throw an exception.
   **/
  public void Var025() {
    if (checkJdbc20StdExt()) {
      try {
        PreparedStatement ps = closedConnection_.prepareStatement("SELECT * FROM QIWS.QCUSTCDT",
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        failed("Did not throw exception but got " + ps);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException"); // see comments in var002
      }
    }
  }

  /**
   * prepareStatement() with 2 parameters - Prepare a read only query as
   * forward-only and read-only.
   **/
  public void Var026() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        PreparedStatement s = (PreparedStatement) connection_.prepareStatement("SELECT * FROM " + table_,
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery();
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w == null) && (type == ResultSet.TYPE_FORWARD_ONLY)
            && (concurrency == ResultSet.CONCUR_READ_ONLY) && (updatable == false) && (scrollable == false));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareStatement() with 2 parameters - Prepare a updateable query as
   * forward-only and read-only.
   **/
  public void Var027() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        PreparedStatement s = (PreparedStatement) connection_.prepareStatement(
            "SELECT * FROM " + table_ + " FOR UPDATE", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery();
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w == null) && (type == ResultSet.TYPE_FORWARD_ONLY)
            && (concurrency == ResultSet.CONCUR_READ_ONLY) && (updatable == false) && (scrollable == false));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareStatement() with 2 parameters - Prepare a read-only statement as
   * forward-only and updatable.
   **/
  public void Var028() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        PreparedStatement s = (PreparedStatement) connection_.prepareStatement("SELECT * FROM " + table_,
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery();
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) // @C2
        {
          assertCondition((w == null) && (type == ResultSet.TYPE_FORWARD_ONLY)
              && (concurrency == ResultSet.CONCUR_UPDATABLE) && (updatable == true) && (scrollable == false));
        } else {
          assertCondition((w == null) && (type == ResultSet.TYPE_FORWARD_ONLY)
              && (concurrency == ResultSet.CONCUR_READ_ONLY) && (updatable == false) && (scrollable == false));
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareStatement() with 2 parameters - Prepare an updateable statement as
   * forward-only and updatable.
   **/
  public void Var029() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        PreparedStatement s = (PreparedStatement) connection_.prepareStatement(
            "SELECT * FROM " + table_ + " FOR UPDATE", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery();
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w == null) && (type == ResultSet.TYPE_FORWARD_ONLY)
            && (concurrency == ResultSet.CONCUR_UPDATABLE) && (updatable == true) && (scrollable == false));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareStatement() with 2 parameters - Prepare a statement as scroll
   * insensitive and read-only.
   **/
  public void Var030() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        PreparedStatement s = (PreparedStatement) connection_.prepareStatement("SELECT * FROM " + table_,
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery();
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w == null) && (type == ResultSet.TYPE_SCROLL_INSENSITIVE)
            && (concurrency == ResultSet.CONCUR_READ_ONLY) && (updatable == false) && (scrollable == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareStatement() with 2 parameters - Prepare a statement as scroll
   * insensitive and updatable.
   **/
  public void Var031() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        PreparedStatement s = (PreparedStatement) connection_.prepareStatement(
            "SELECT * FROM " + table_ + " FOR UPDATE", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery();
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w != null) && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
            && (concurrency == ResultSet.CONCUR_UPDATABLE) && (updatable == true) && (scrollable == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareStatement() with 2 parameters - Prepare a statement as scroll
   * sensitive and read-only.
   **/
  public void Var032() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        PreparedStatement s = (PreparedStatement) connection_.prepareStatement("SELECT * FROM " + table_,
            ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery();
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) // @C1
        {
          boolean condition = ((w == null) && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
              && (concurrency == ResultSet.CONCUR_READ_ONLY) && (updatable == false) && (scrollable == true));
          if (!condition) {
            System.out.println("w = " + w);
            System.out.println("updatable = " + updatable);
            System.out.println("scrollable = " + scrollable);
          }
          assertCondition(condition);
        } else {
          assertCondition((w != null) && (type == ResultSet.TYPE_SCROLL_INSENSITIVE)
              && (concurrency == ResultSet.CONCUR_READ_ONLY) && (updatable == false) && (scrollable == true));
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareStatement() with 2 parameters - Prepare a statement as scroll
   * sensitive and updatable.
   **/
  public void Var033() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        PreparedStatement s = (PreparedStatement) connection_.prepareStatement(
            "SELECT * FROM " + table_ + " FOR UPDATE", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery();
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w == null) && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
            && (concurrency == ResultSet.CONCUR_UPDATABLE) && (updatable == true) && (scrollable == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareCall() - Create a callable statement in a connection. Verify the
   * result set type and concurrency in the statement.
   **/
  public void Var034() {
    if (checkJdbc20StdExt()) {
      try {
        CallableStatement cs = (CallableStatement) connection_
            .prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)");
        int resultSetType = cs.getResultSetType();
        int resultSetConcurrency = cs.getResultSetConcurrency();
        cs.close();
        assertCondition(
            (resultSetType == ResultSet.TYPE_FORWARD_ONLY) && (resultSetConcurrency == ResultSet.CONCUR_READ_ONLY));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareCall() - Pass null. Should throw an exception.
   **/
  public void Var035() {
    if (checkJdbc20StdExt()) {
      try {
        CallableStatement cs = (CallableStatement) connection_.prepareCall(null);
        failed("Did not throw exception but got " + cs);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareCall() - Pass an empty string. Should throw an exception.
   **/
  public void Var036() {
    if (checkJdbc20StdExt()) {
      try {
        CallableStatement cs = (CallableStatement) connection_.prepareCall("");
        failed("Did not throw exception but got " + cs);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareCall() - Pass a blank string. Should throw an exception.
   **/
  public void Var037() {
    if (checkJdbc20StdExt()) {
      try {
        CallableStatement cs = (CallableStatement) connection_.prepareCall(" ");
        failed("Did not throw exception but got " + cs);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareCall() - Pass a bogus string. Should throw an exception.
   **/
  public void Var038() {
    if (checkJdbc20StdExt()) {
      try {
        CallableStatement cs = (CallableStatement) connection_.prepareCall("CLIF");
        failed("Did not throw exception but got " + cs);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareCall() - Create a callable statement in a closed connection. Should
   * throw an exception.
   **/
  public void Var039() {
    if (checkJdbc20StdExt()) {
      try {
        CallableStatement cs = closedConnection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)");
        failed("Did not throw exception but got " + cs);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException"); // see comments in var002
      }
    }
  }

  /**
   * prepareCall() with 3 parameters - Create a callable statement in a
   * connection.
   **/
  public void Var040() {
    if (checkJdbc20StdExt()) {
      try {
        CallableStatement cs = (CallableStatement) connection_.prepareCall(
            "CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)", ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE);
        boolean success = (cs.getResultSetType() == ResultSet.TYPE_SCROLL_SENSITIVE)
            && (cs.getResultSetConcurrency() == ResultSet.CONCUR_UPDATABLE);
        cs.close();
        assertCondition(success);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareCall() with 3 parameters - Pass null for SQL statement. Should throw
   * an exception.
   **/
  public void Var041() {
    if (checkJdbc20StdExt()) {
      try {
        CallableStatement cs = (CallableStatement) connection_.prepareCall(null, ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_UPDATABLE);
        failed("Did not throw exception but got " + cs);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareCall() with 3 parameters - Pass an empty string for SQL statement.
   * Should throw an exception.
   **/
  public void Var042() {
    if (checkJdbc20StdExt()) {
      try {
        CallableStatement cs = (CallableStatement) connection_.prepareCall("", ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_UPDATABLE);
        failed("Did not throw exception but got " + cs);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareCall() with 3 parameters - Pass a blank string for SQL statement.
   * Should throw an exception.
   **/
  public void Var043() {
    if (checkJdbc20StdExt()) {
      try {
        CallableStatement cs = (CallableStatement) connection_.prepareCall(" ", ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
        failed("Did not throw exception but got " + cs);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareCall() with 3 parameters - Pass a bogus string for SQL statement.
   * Should throw an exception.
   **/
  public void Var044() {
    if (checkJdbc20StdExt()) {
      try {
        CallableStatement cs = (CallableStatement) connection_.prepareCall("CLIF", ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
        failed("Did not throw exception but got " + cs);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareCall() with 3 parameters - Pass an bad value for result set type.
   * Should throw an exception.
   **/
  public void Var045() {
    if (checkJdbc20StdExt()) {
      try {
        CallableStatement cs = (CallableStatement) connection_
            .prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)", -574, ResultSet.CONCUR_UPDATABLE);
        failed("Did not throw exception but got " + cs);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareCall() with 3 parameters - Pass an bad value for result set
   * concurrency. Should throw an exception.
   **/
  public void Var046() {
    if (checkJdbc20StdExt()) {
      try {
        CallableStatement cs = (CallableStatement) connection_
            .prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)", ResultSet.TYPE_SCROLL_INSENSITIVE, -754);
        failed("Did not throw exception but got " + cs);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * prepareCall() with 3 parameters - Create a callable statement in a closed
   * connection. Should throw an exception.
   **/
  public void Var047() {
    if (checkJdbc20StdExt()) {
      try {
        CallableStatement cs = closedConnection_.prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + " (?,?,?)",
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        failed("Did not throw exception but got " + cs);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException"); // see comments in var002
      }
    }
  }

  /**
   * prepareCall() with 2 parameters - Prepare a statement as forward-only and
   * read-only.
   **/
  public void Var048() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        PreparedStatement s = (PreparedStatement) connection_.prepareCall("SELECT * FROM " + table_,
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery();
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w == null) && (type == ResultSet.TYPE_FORWARD_ONLY)
            && (concurrency == ResultSet.CONCUR_READ_ONLY) && (updatable == false) && (scrollable == false));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareCall() with 2 parameters - Prepare a statement as forward-only and
   * updatable.
   **/
  public void Var049() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        PreparedStatement s = (PreparedStatement) connection_.prepareCall("SELECT * FROM " + table_ + " FOR UPDATE",
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery();
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w == null) && (type == ResultSet.TYPE_FORWARD_ONLY)
            && (concurrency == ResultSet.CONCUR_UPDATABLE) && (updatable == true) && (scrollable == false));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareCall() with 2 parameters - Prepare a statement as scroll insensitive
   * and read-only.
   **/
  public void Var050() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        PreparedStatement s = (PreparedStatement) connection_.prepareCall("SELECT * FROM " + table_,
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery();
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w == null) && (type == ResultSet.TYPE_SCROLL_INSENSITIVE)
            && (concurrency == ResultSet.CONCUR_READ_ONLY) && (updatable == false) && (scrollable == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareCall() with 2 parameters - Prepare a statement as scroll insensitive
   * and updatable.
   **/
  public void Var051() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        PreparedStatement s = (PreparedStatement) connection_.prepareCall("SELECT * FROM " + table_ + " FOR UPDATE",
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery();
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w != null) && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
            && (concurrency == ResultSet.CONCUR_UPDATABLE) && (updatable == true) && (scrollable == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareCall() with 2 parameters - Prepare a statement as scroll sensitive and
   * read-only.
   **/
  public void Var052() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        PreparedStatement s = (PreparedStatement) connection_.prepareCall("SELECT * FROM " + table_,
            ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery();
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) // @C1
        {
          assertCondition((w == null) && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
              && (concurrency == ResultSet.CONCUR_READ_ONLY) && (updatable == false) && (scrollable == true));
        } else {
          assertCondition((w != null) && (type == ResultSet.TYPE_SCROLL_INSENSITIVE)
              && (concurrency == ResultSet.CONCUR_READ_ONLY) && (updatable == false) && (scrollable == true));
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareCall() with 2 parameters - Prepare a statement as scroll sensitive and
   * updatable.
   **/
  public void Var053() {
    if (checkJdbc20StdExt()) {
      try {
        connection_.clearWarnings();
        PreparedStatement s = (PreparedStatement) connection_.prepareCall("SELECT * FROM " + table_ + " FOR UPDATE",
            ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        SQLWarning w = connection_.getWarnings();
        ResultSet rs = (ResultSet) s.executeQuery();
        int type = rs.getType();
        int concurrency = rs.getConcurrency();
        boolean updatable = checkUpdatable(rs);
        boolean scrollable = checkScrollable(rs);
        rs.close();
        s.close();
        assertCondition((w == null) && (type == ResultSet.TYPE_SCROLL_SENSITIVE)
            && (concurrency == ResultSet.CONCUR_UPDATABLE) && (updatable == true) && (scrollable == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * createStatement()/prepareStatement()/prepareCall() - Verify that we can
   * create exactly 255 statements.
   * 
   * SQL400 - The Native JDBC driver handle limit keeps changing. V4R4 - 500 :
   * V4R5 - 40,000 : V5R1 - 80,000 Just test that we can get over 5000
   **/
  public void Var054() {
    if (checkJdbc20StdExt()) {
      try {
        Connection c = db2ConnectionPoolDataSource.getConnection();
        int count = fillConnection(c);
        c.close();

        assertCondition(count > 500); // anything larger than 500 is success...

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * createStatement()/prepareStatement()/prepareCall() - Verify that statement
   * handles get reused.
   * 
   * SQL400 - The Native JDBC driver handle limit keeps changing. V4R4 - 500 :
   * V4R5 - 40,000 : V5R1 - 80,000 Just test that we can get over 5000
   **/
  public void Var055() {
    if (checkJdbc20StdExt()) {
      try {

        Connection c = db2ConnectionPoolDataSource.getConnection();

        // force a garbage collection to increase the likelyhood
        // we'll reuse the same handle nubmer
        System.gc();
        Thread.sleep(5000);
        System.gc();

        // Create a driver specific statement object.
        Statement s = c.createStatement();

        // Get it's CLI handle
        int beforeHandle = JDReflectionUtil.callMethod_I(s, "getStatementHandle");

        // Close it.
        s.close();

        // When we create a new one, it should use the same handle over.
        s = c.createStatement();
        int afterHandle = JDReflectionUtil.callMethod_I(s, "getStatementHandle");

        c.close();

        assertCondition(beforeHandle == afterHandle,
            "Statement Handle Not Reused.  before=" + beforeHandle + " after=" + afterHandle);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepareStatement (for autogenerated keys) -- make sure this works.
   **/

  public void Var056() {
    try {

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO " + keysTable_ + " (NAME) VALUES ('Holly')",
          Statement.RETURN_GENERATED_KEYS);
      ps.execute();
      ResultSet rs = ps.getGeneratedKeys();
      boolean nextWorked = false;
      boolean nextWorkedAgain = false;
      String key = "";
      if (rs != null) {
        nextWorked = rs.next();
        key = rs.getString(1);
        nextWorkedAgain = rs.next();
      }
      assertCondition(rs != null && key.length() == 1 && nextWorked && !nextWorkedAgain,
          " rs = " + rs + " should not be null;" + " key.length = " + key.length() + " sb 1;" + " nextWorked = "
              + nextWorked + " sb true;" + " nexWorkedAgain=" + nextWorkedAgain + "sb false "
              + " new variation added by native driver 12/03/03");
      ps.close();
    } catch (Exception e) {
      failed(e, "Unexpected Exception -- new variation added by native driver.  10/29/03");
    }

  }

}
