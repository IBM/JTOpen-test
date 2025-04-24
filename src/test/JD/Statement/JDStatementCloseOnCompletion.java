///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementCloseOnCompletion.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementCloseOnCompletion.java
//
// Classes:      JDStatementCloseOnCompletion
//
////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDStatementTest;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;

/**
 * Testcase JDStatementCloseOnCompletion. This tests the following method of the
 * JDBC Statement class:
 * 
 * <ul>
 * <li>close()</li>
 * </ul>
 **/
public class JDStatementCloseOnCompletion extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDStatementCloseOnCompletion";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDStatementTest.main(newArgs); 
   }

  // Private data.
  private Connection connection_;

  private String CALLINSERT = "SET BY SETUP"; 
  private String CALLQUERY = "SET BY SETUP"; 
  private String CALLQUERY3 = "SET BY SETUP"; 
  String callInsertProcedure = null; 
  String callQueryProcedure = null; 
  String callQuery3Procedure = null; 
  
  /**
   * Constructor.
   **/
  public JDStatementCloseOnCompletion(AS400 systemObject,
      Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password) {
    super(systemObject, "JDStatementCloseOnCompletion", namesAndVars, runMode,
        fileOutputStream, password);
  }

  void executeDrop(Statement stmt, String sql) {
    try {
      stmt.executeUpdate(sql); 
    } catch (Exception e) {
      String expectedException = "NOT FOUND"; 
      String message = e.toString().toUpperCase(); 
      if (message.indexOf(expectedException)< 0) {
        System.out.println("WARNING:  Unexpected exception when deleting table.");
        System.out.flush(); 
        e.printStackTrace();
        System.err.flush(); 
      }
    }
  }
  
  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    // Initialize private data.
    connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
    
    Statement stmt = connection_.createStatement();
    
    callQueryProcedure = JDStatementTest.COLLECTION+".SCOCQRY"; 
    String sql; 
    
    sql = "Drop PROCEDURE "+callQueryProcedure; 
    executeDrop(stmt,sql); 
    
    sql = "CREATE PROCEDURE "+callQueryProcedure+" () dynamic result set 1 "+
              "LANGUAGE SQL BEGIN DECLARE C1 CURSOR FOR SELECT * FROM SYSIBM.SYSDUMMY1;" +
              " open  c1; set result sets cursor c1; end "; 
    stmt.executeUpdate(sql); 
    
    CALLQUERY = "CALL "+callQueryProcedure+"()"; 

    callInsertProcedure = JDStatementTest.COLLECTION+".SCOCINS";
    sql = "Drop PROCEDURE "+callInsertProcedure; 
    executeDrop(stmt,sql); 

    sql = "CREATE PROCEDURE "+callInsertProcedure+" (TABLENAME VARCHAR(80), VALUE INTEGER ) "+
    "LANGUAGE SQL BEGIN "+
    " DECLARE S1 VARCHAR(1024);" +
    " SET S1 = 'INSERT INTO ' || TABLENAME || ' (C1) VALUES (?) ';  "+
    " PREPARE PS1 FROM S1;" +
    " EXECUTE PS1 USING VALUE; "+
    " end "; 

    System.out.println("creating procedure using sql "+sql); 
    stmt.executeUpdate(sql);
    
    CALLINSERT =  "CALL "+callInsertProcedure+"(?,?)";

    
    callQuery3Procedure = JDStatementTest.COLLECTION+".SCOCQRY3"; 
    
    sql = "Drop PROCEDURE "+callQuery3Procedure; 
    executeDrop(stmt,sql); 
    
    sql = "CREATE PROCEDURE "+callQuery3Procedure+" () dynamic result sets 3 "+
              "LANGUAGE SQL BEGIN " +
              "DECLARE C1 CURSOR FOR SELECT '1' FROM SYSIBM.SYSDUMMY1; " +
              "DECLARE C2 CURSOR FOR SELECT '2' FROM SYSIBM.SYSDUMMY1; " +
              "DECLARE C3 CURSOR FOR SELECT '3' FROM SYSIBM.SYSDUMMY1; " +
              " open  c1; open c2; open c3;  set result sets cursor c1, cursor c2, cursor c3; " +
              " end "; 
    stmt.executeUpdate(sql); 
    
    CALLQUERY3 = "CALL "+callQuery3Procedure+"()"; 

    
    
    
    
    stmt.close(); 
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    Statement stmt = connection_.createStatement(); 
    String sql; 

    sql = "Drop PROCEDURE "+callQueryProcedure; 
    executeDrop(stmt,sql); 

    sql = "Drop PROCEDURE "+callInsertProcedure; 
    executeDrop(stmt,sql); 

    sql = "Drop PROCEDURE "+callQuery3Procedure; 
    executeDrop(stmt,sql); 

    stmt.close(); 
    connection_.close();
  }

  /**
   * Indicates if a statement is closed.
   **/
  boolean isStatementClosed(Statement s, StringBuffer sb) {
    // Try to run a query. An exception will be
    // thrown if the statement is closed.
    try {
      s.executeQuery("SELECT * FROM QSYS2.SYSPROCS");
      return false;
    } catch (SQLException e) {
      return checkClosedException(e, sb);
    }
  }

  
  boolean isStatementClosed(PreparedStatement ps, StringBuffer sb) throws SQLException {
    // Try execute. An exception will be
    // thrown if the statement is closed.
    sb.append("Calling isStatementClosed(PreparedStatement)\n"); 
    try {
      ps.execute();
      return false;
    } catch (SQLException e) {

	return checkClosedException(e, sb);
    }

  }

  /*
   * should not be called when processing multiple result sets because this will
   * call execute to see if the statement is really closed. 
   */

  boolean isStatementClosed(CallableStatement cs, StringBuffer sb) throws SQLException {
    // Try execute. An exception will be
    // thrown if the statement is closed.
    sb.append("Calling isStatementClosed(CallableStatement)\n"); 
    try {
      cs.getResultSetType();
      return false;
    } catch (SQLException e) {
	return checkClosedException(e, sb);
    }

  }

  
  
  
  /**
   * isCloseOnCompletion() - For Statement object, check default value.
   **/
  public void Var001() {
    try {
      Statement s = connection_.createStatement();
      boolean value = JDReflectionUtil.callMethod_B(s, "isCloseOnCompletion");
      assertCondition(value == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isCloseOnCompletion() - For statement object, check set value.
   **/
  public void Var002() {
    try {
      Statement s = connection_.createStatement();
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      boolean value = JDReflectionUtil.callMethod_B(s, "isCloseOnCompletion");
      assertCondition(value == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isCloseOnCompletion() - For statement object, call on closed statement.
   * Should throw exception.
   **/
  public void Var003() {
    try {
      Statement s = connection_.createStatement();
      s.close(); 
      boolean value = JDReflectionUtil.callMethod_B(s, "isCloseOnCompletion");
      assertCondition(false, "Did not throw exception on closed statement but got "+value);
    } catch (Exception e) {
     	assertClosedException(e, "");

    }
  }

  /**
   * closeOnCompletion() -- For statement object, verify that throws exception
   * for closed statement
   **/
  public void Var004() {
    try {
      Statement s = connection_.createStatement();
      s.close();
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      assertCondition(false, "Did not throw exception on closed statement");
    } catch (Exception e) {
	assertClosedException(e, "");
    }
  }

  /**
   * closeOnCompletion() -- For statement object, verify that you are able to
   * execute multiple non-query statements
   **/
  public void Var005() {
    String info = " -- verify that you are able to execute multiple non-query statements";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    try {
      Statement s = connection_.createStatement();
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      String table = JDStatementTest.COLLECTION + ".JDSCOC005";
      sql = "Drop table " + table;
      try {
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("Caught expected exception " + e.toString() + "\n");
      }

      sql = "Create table " + table + " (C1 INTEGER) ";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "delete from " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "drop table " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(true);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }

  /**
   * closeOnCompletion() -- For statement object, verify that close on
   * completion works after running the query.
   **/
  public void Var006() {
    String info = " -- verify that close on completion works after running the query";
    String table = JDStatementTest.COLLECTION + ".JDSCOC006";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    boolean passed = true;
    try {
      Statement s = connection_.createStatement();
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      sql = "Drop table " + table;
      try {
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("Caught expected exception " + e.toString() + "\n");
      }

      sql = "Create table " + table + " (C1 INTEGER) ";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "delete from " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "select * from  " + table;
      sb.append("Executing " + sql + "\n");
      ResultSet rs = s.executeQuery(sql);
      rs.close();

      if (!isStatementClosed(s, sb)) {
        passed = false;
        sb.append("Statement was not closed after result set was closed\n");
      }

      s = connection_.createStatement();

      sql = "drop table " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(passed,sb);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }

  /**
   * closeOnCompletion() -- For statement object, verify that close on
   * completion works fetching only the first row of the result set
   **/

  public void Var007() {
    String info = " -- verify that close on completion works after fetching only the first row of the result set";
    String table = JDStatementTest.COLLECTION + ".JDSCOC007";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    boolean passed = true;
    try {
      Statement s = connection_.createStatement();
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      sql = "Drop table " + table;
      try {
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("Caught expected exception " + e.toString() + "\n");
      }

      sql = "Create table " + table + " (C1 INTEGER) ";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(8)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(9)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "delete from " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "select * from  " + table;
      sb.append("Executing " + sql + "\n");
      ResultSet rs = s.executeQuery(sql);
      rs.next();
      rs.close();

      if (!isStatementClosed(s,sb)) {
        passed = false;
        sb.append("Statement was not closed after result set was closed");
      }

      s = connection_.createStatement();

      sql = "drop table " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(passed,sb);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }

  /**
   * closeOnCompletion() -- For statement object, verify that close on
   * completion works fetching all rows of the result set
   **/

  public void Var008() {
    String info = " -- verify that close on completion works after fetching only all rows of the result set";
    String table = JDStatementTest.COLLECTION + ".JDSCOC008";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    boolean passed = true;
    try {
      Statement s = connection_.createStatement();
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      sql = "Drop table " + table;
      try {
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("Caught expected exception " + e.toString() + "\n");
      }

      sql = "Create table " + table + " (C1 INTEGER) ";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(8)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(9)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "delete from " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "select * from  " + table;
      sb.append("Executing " + sql + "\n");
      ResultSet rs = s.executeQuery(sql);
      while (rs.next())
        ;
      rs.close();

      if (!isStatementClosed(s,sb)) {
        passed = false;
        sb.append("Statement was not closed after result set was closed");
      }

      s = connection_.createStatement();

      sql = "drop table " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(passed,sb);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }

  /**
   * closeOnCompletion() -- For statement object, verify that close on
   * completion works fetching all rows of the result set and moreResults called
   **/

  public void Var009() {
    String info = " -- verify that close on completion works after fetching only all rows of the result set and moreResults called";
    String table = JDStatementTest.COLLECTION + ".JDSCOC009";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    boolean passed = true;
    try {
      Statement s = connection_.createStatement();
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      sql = "Drop table " + table;
      try {
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("Caught expected exception " + e.toString() + "\n");
      }

      sql = "Create table " + table + " (C1 INTEGER) ";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(8)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(9)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "delete from " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "select * from  " + table;
      sb.append("Executing " + sql + "\n");
      ResultSet rs = s.executeQuery(sql);
      while (rs.next())
        ;

      sb.append("Calling more results");
      s.getMoreResults();
      try {
        rs.close();
      } catch (Exception e) {
        sb.append("Tolerated cxception on rs.close() " + e.toString() + "\n");
      }

      if (!isStatementClosed(s,sb)) {
        passed = false;
        sb
            .append("Statement was not closed after result set was closed and getMoreResultsCalled ");
      }

      s = connection_.createStatement();

      sql = "drop table " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(passed,sb);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }

  // ----------------------------------------------------------------
  // Repeat tests for Prepared statements
  // ----------------------------------------------------------------

  /**
   * isCloseOnCompletion() - For PreparedStatement object, check default value.
   **/
  public void Var010() {
    try {
      PreparedStatement s = connection_
          .prepareStatement("select * from sysibm.sysdummy1");
      boolean value = JDReflectionUtil.callMethod_B(s, "isCloseOnCompletion");
      assertCondition(value == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isCloseOnCompletion() - For PreparedStatement object, check set value.
   **/
  public void Var011() {
    try {
      Statement s = connection_
          .prepareStatement("select * from sysibm.sysdummy1");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      boolean value = JDReflectionUtil.callMethod_B(s, "isCloseOnCompletion");
      assertCondition(value == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isCloseOnCompletion() - For PreparedStatement object, call on closed
   * statement. Should throw exception.
   **/
  public void Var012() {
    try {
      Statement s = connection_
          .prepareStatement("Select * from sysibm.sysdummy1");
      boolean value = JDReflectionUtil.callMethod_B(s, "isCloseOnCompletion");
      assertCondition(value == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * closeOnCompletion() -- For PreparedStatement object, verify that throws
   * exception for closed statement
   **/
  public void Var013() {
    try {
      Statement s = connection_
          .prepareStatement("select * from sysibm.sysdummy1");
      s.close();
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      assertCondition(false, "Did not throw exception on closed statement");
    } catch (Exception e) {
	assertClosedException(e, "");
    }
  }

  /**
   * closeOnCompletion() -- For PreparedStatement object, verify that you are
   * able to execute multiple non-query statements
   **/
  public void Var014() {
    String info = " -- verify that you are able to execute multiple non-query statements";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    try {
      Statement s = connection_.createStatement();
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      String table = JDStatementTest.COLLECTION + ".JDSCOC014";
      sql = "Drop table " + table;
      try {
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("Caught expected exception " + e.toString() + "\n");
      }

      sql = "Create table " + table + " (C1 INTEGER) ";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(?)";
      PreparedStatement ps = connection_.prepareStatement(sql);
      JDReflectionUtil.callMethod_V(ps, "closeOnCompletion");

      ps.setString(1, "1");
      sb.append("Executing " + sql + " 1\n");
      ps.execute();
      ps.setString(1, "2");
      sb.append("Executing " + sql + " 2\n");
      ps.execute();
      ps.setString(1, "3");
      sb.append("Executing " + sql + " 3\n");
      ps.execute();

      sql = "drop table " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(true);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }

  /**
   * closeOnCompletion() -- For PreparedStatement object, verify that close on
   * completion works after running the query.
   **/
  public void Var015() {
    String info = " -- verify that close on completion works after running the query";
    String table = JDStatementTest.COLLECTION + ".JDSCOC015";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    boolean passed = true;
    try {
      Statement s = connection_.createStatement();
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      sql = "Drop table " + table;
      try {
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("Caught expected exception " + e.toString() + "\n");
      }

      sql = "Create table " + table + " (C1 INTEGER) ";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "delete from " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "select * from  " + table;

      sb.append("Preparing  " + sql + "\n");
      PreparedStatement ps = connection_.prepareStatement(sql);
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(ps, "closeOnCompletion");
      sb.append("Calling running query\n");
      ResultSet rs = ps.executeQuery();
      sb.append("Closing RS\n");
      rs.close();

      sb.append("Closing checking statement\n");
      if (!isStatementClosed(ps,sb)) {
        passed = false;
        sb.append("Statement was not closed after result set was closed\n");
      }

      s = connection_.createStatement();

      sql = "drop table " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(passed,sb);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }

  /**
   * closeOnCompletion() -- For PreparedStatement object, verify that close on
   * completion works fetching only the first row of the result set
   **/

  public void Var016() {
    String info = " -- verify that close on completion works after fetching only the first row of the result set";
    String table = JDStatementTest.COLLECTION + ".JDSCOC016";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    boolean passed = true;
    try {
      Statement s = connection_.createStatement();
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      sql = "Drop table " + table;
      try {
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("Caught expected exception " + e.toString() + "\n");
      }

      sql = "Create table " + table + " (C1 INTEGER) ";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(8)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(9)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "delete from " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "select * from  " + table;
      sb.append("Preparing  " + sql + "\n");
      PreparedStatement ps = connection_.prepareStatement(sql);
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(ps, "closeOnCompletion");

      ResultSet rs = ps.executeQuery();
      rs.next();
      rs.close();

      if (!isStatementClosed(ps,sb)) {
        passed = false;
        sb.append("Statement was not closed after result set was closed");
      }

      s = connection_.createStatement();

      sql = "drop table " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(passed,sb);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }

  /**
   * closeOnCompletion() -- For PreparedStatement object, verify that close on
   * completion works fetching all rows of the result set
   **/

  public void Var017() {
    String info = " -- verify that close on completion works after fetching only all rows of the result set";
    String table = JDStatementTest.COLLECTION + ".JDSCOC017";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    boolean passed = true;
    try {
      Statement s = connection_.createStatement();
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      sql = "Drop table " + table;
      try {
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("Caught expected exception " + e.toString() + "\n");
      }

      sql = "Create table " + table + " (C1 INTEGER) ";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(8)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(9)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "delete from " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "select * from  " + table;
      sb.append("Preparing " + sql + "\n");
      PreparedStatement ps = connection_.prepareStatement(sql);
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(ps, "closeOnCompletion");

      ResultSet rs = ps.executeQuery();
      while (rs.next())
        ;
      rs.close();

      if (!isStatementClosed(ps,sb)) {
        passed = false;
        sb.append("Statement was not closed after result set was closed");
      }

      s = connection_.createStatement();

      sql = "drop table " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(passed,sb);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }

  /**
   * closeOnCompletion() -- For PreparedStatement object, verify that close on
   * completion works fetching all rows of the result set and moreResults called
   **/

  public void Var018() {
    String info = " -- verify that close on completion works after fetching only all rows of the result set and moreResults called";
    String table = JDStatementTest.COLLECTION + ".JDSCOC018";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    boolean passed = true;
    try {
      Statement s = connection_.createStatement();
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      sql = "Drop table " + table;
      try {
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("Caught expected exception " + e.toString() + "\n");
      }

      sql = "Create table " + table + " (C1 INTEGER) ";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(8)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(9)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "delete from " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "select * from  " + table;
      sb.append("Preparing " + sql + "\n");
      PreparedStatement ps = connection_.prepareStatement(sql);
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(ps, "closeOnCompletion");

      ResultSet rs = ps.executeQuery();
      while (rs.next())
        ;

      sb.append("Calling more results");
      ps.getMoreResults();
      try {
        rs.close();
      } catch (Exception e) {
        sb.append("Tolerated cxception on rs.close() " + e.toString() + "\n");
      }

      if (!isStatementClosed(ps,sb)) {
        passed = false;
        sb
            .append("Statement was not closed after result set was closed and getMoreResultsCalled ");
      }

      s = connection_.createStatement();

      sql = "drop table " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(passed,sb);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }


                                           
                
                
                
  /**
   * closeOnCompletion() -- For prepared object, verify that close on completion
   * works fetching all rows of the result set autogenerated keys used
   **/
  public void Var019() {
      if (checkJdbc40()) { 
	  String info = " -- verify that close on completion works after fetching only all rows of the result set and autogenerated keys used";
	  String table = JDStatementTest.COLLECTION + ".JDSCOC019";
	  StringBuffer sb = new StringBuffer();
	  String sql = "";
	  boolean passed = true;
	  try {
	      Statement s = connection_.createStatement();
	      sb.append("Calling closeOnCompletion\n");
	      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
	      sql = "Drop table " + table;
	      try {
		  sb.append("Executing " + sql + "\n");
		  s.executeUpdate(sql);
	      } catch (Exception e) {
		  sb.append("Caught expected exception " + e.toString() + "\n");
	      }

	      sql = "Create table " + table + " (C1 INTEGER,  GENID INT GENERATED ALWAYS AS IDENTITY) ";

	      sb.append("Executing " + sql + "\n");
	      s.executeUpdate(sql);

	      sql = "insert into " + table + " (C1) values(?)";
	      sb.append("Executing " + sql + "\n");
	      PreparedStatement ps = connection_.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS );

	      for (int i = 1; i < 5; i++) {
		  sb.append("setting "+i+" / 5 \n");
		  ps.setInt(1, i);
		  ps.executeUpdate();
		  ResultSet rs = ps.getGeneratedKeys();
		  rs.next();
		  sb.append("Generated key was "+rs.getString(1)); 
		  rs.close();
	      }


	      if (isStatementClosed(ps, sb)) {
		  passed = false;
		  sb.append("Statement should not be closed \n");
	      }

	      s = connection_.createStatement();

	      sql = "drop table " + table;
	      sb.append("Executing " + sql + "\n");
	      s.executeUpdate(sql);

	      assertCondition(passed,sb);
	  } catch (Exception e) {
	      failed(e, "Unexpected exception -- " + sb.toString() + info);
	  }
      }
  }


  // Repeat tests for callable statements

  
  
  
  
  /**
   * isCloseOnCompletion() - For PreparedStatement object, check default value.
   **/
  public void Var020() {
    try {
      PreparedStatement s = connection_
          .prepareStatement("select * from sysibm.sysdummy1");
      boolean value = JDReflectionUtil.callMethod_B(s, "isCloseOnCompletion");
      assertCondition(value == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isCloseOnCompletion() - For PreparedStatement object, check set value.
   **/
  public void Var021() {
    try {
      Statement s = connection_
          .prepareCall(CALLQUERY);
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      boolean value = JDReflectionUtil.callMethod_B(s, "isCloseOnCompletion");
      assertCondition(value == true);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * isCloseOnCompletion() - For PreparedStatement object, call on closed
   * statement. Should throw exception.
   **/
  public void Var022() {
    try {
      Statement s = connection_
          .prepareCall(CALLQUERY);
      boolean value = JDReflectionUtil.callMethod_B(s, "isCloseOnCompletion");
      assertCondition(value == false);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * closeOnCompletion() -- For PreparedStatement object, verify that throws
   * exception for closed statement
   **/
  public void Var023() {
    try {
      Statement s = connection_
          .prepareCall(CALLQUERY);
      s.close();
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      assertCondition(false, "Did not throw exception on closed statement");
    } catch (Exception e) {
	assertClosedException(e, "");
    }
  }

  /**
   * closeOnCompletion() -- For PreparedStatement object, verify that you are
   * able to execute multiple non-query statements
   **/
  public void Var024() {
    String info = " -- verify that you are able to execute multiple non-query statements";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    try {
      Statement s = connection_.createStatement();
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      String table = JDStatementTest.COLLECTION + ".JDSCOC014";
      sql = "Drop table " + table;
      try {
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("Caught expected exception " + e.toString() + "\n");
      }

      sql = "Create table " + table + " (C1 INTEGER) ";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = CALLINSERT;
      sb.append("Preparing " + sql + "\n");
      PreparedStatement ps = connection_.prepareCall(sql);
      JDReflectionUtil.callMethod_V(ps, "closeOnCompletion");

      ps.setString(1, table);
      ps.setString(2, "1");
      sb.append("Executing " + sql + " 1\n");
      ps.execute();
      ps.setString(2, "2");
      sb.append("Executing " + sql + " 2\n");
      ps.execute();
      ps.setString(2, "3");
      sb.append("Executing " + sql + " 3\n");
      ps.execute();

      sql = "drop table " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(true);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }

  /**
   * closeOnCompletion() -- For PreparedStatement object, verify that close on
   * completion works after running the query.
   **/
  public void Var025() {
    String info = " -- verify that close on completion works after running the query";
    String table = JDStatementTest.COLLECTION + ".JDSCOC015";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    boolean passed = true;
    try {
      Statement s = connection_.createStatement();
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      sql = "Drop table " + table;
      try {
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("Caught expected exception " + e.toString() + "\n");
      }

      sql = "Create table " + table + " (C1 INTEGER) ";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "delete from " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = CALLQUERY;

      sb.append("Preparing  " + sql + "\n");
      PreparedStatement ps = connection_.prepareCall(sql);
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(ps, "closeOnCompletion");
      sb.append("Calling running query\n");
      ResultSet rs = ps.executeQuery();
      sb.append("Closing RS\n");
      rs.close();

      sb.append("Closing checking statement\n");
      if (!isStatementClosed(ps,sb)) {
        passed = false;
        sb.append("Statement was not closed after result set was closed\n");
      }

      s = connection_.createStatement();

      sql = "drop table " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(passed,sb);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }

  /**
   * closeOnCompletion() -- For PreparedStatement object, verify that close on
   * completion works fetching only the first row of the result set
   **/

  public void Var026() {
    String info = " -- verify that close on completion works after fetching only the first row of the result set";
    String table = JDStatementTest.COLLECTION + ".JDSCOC016";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    boolean passed = true;
    try {
      Statement s = connection_.createStatement();
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      sql = "Drop table " + table;
      try {
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("Caught expected exception " + e.toString() + "\n");
      }

      sql = "Create table " + table + " (C1 INTEGER) ";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(8)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(9)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "delete from " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "select * from  " + table;
      sb.append("Preparing  " + sql + "\n");
      PreparedStatement ps = connection_.prepareCall(sql);
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(ps, "closeOnCompletion");

      ResultSet rs = ps.executeQuery();
      rs.next();
      rs.close();

      if (!isStatementClosed(ps,sb)) {
        passed = false;
        sb.append("Statement was not closed after result set was closed");
      }

      s = connection_.createStatement();

      sql = "drop table " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(passed,sb);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }

  /**
   * closeOnCompletion() -- For PreparedStatement object, verify that close on
   * completion works fetching all rows of the result set
   **/

  public void Var027() {
    String info = " -- verify that close on completion works after fetching only all rows of the result set";
    String table = JDStatementTest.COLLECTION + ".JDSCOC017";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    boolean passed = true;
    try {
      Statement s = connection_.createStatement();
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      sql = "Drop table " + table;
      try {
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("Caught expected exception " + e.toString() + "\n");
      }

      sql = "Create table " + table + " (C1 INTEGER) ";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(8)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(9)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "delete from " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "select * from  " + table;
      sb.append("Preparing " + sql + "\n");
      PreparedStatement ps = connection_.prepareCall(sql);
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(ps, "closeOnCompletion");

      ResultSet rs = ps.executeQuery();
      while (rs.next())
        ;
      rs.close();

      if (!isStatementClosed(ps,sb)) {
        passed = false;
        sb.append("Statement was not closed after result set was closed");
      }

      s = connection_.createStatement();

      sql = "drop table " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(passed,sb);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }

  /**
   * closeOnCompletion() -- For PreparedStatement object, verify that close on
   * completion works fetching all rows of the result set and moreResults called
   **/

  public void Var028() {
    String info = " -- verify that close on completion works after fetching only all rows of the result set and moreResults called";
    String table = JDStatementTest.COLLECTION + ".JDSCOC018";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    boolean passed = true;
    try {
      Statement s = connection_.createStatement();
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(s, "closeOnCompletion");
      sql = "Drop table " + table;
      try {
        sb.append("Executing " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("Caught expected exception " + e.toString() + "\n");
      }

      sql = "Create table " + table + " (C1 INTEGER) ";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(8)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);
      sql = "insert into " + table + " values(9)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "delete from " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "insert into " + table + " values(7)";
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      sql = "select * from  " + table;
      sb.append("Preparing " + sql + "\n");
      PreparedStatement ps = connection_.prepareCall(sql);
      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(ps, "closeOnCompletion");

      ResultSet rs = ps.executeQuery();
      while (rs.next())
        ;

      sb.append("Calling more results");
      ps.getMoreResults();
      try {
        rs.close();
      } catch (Exception e) {
        sb.append("Tolerated cxception on rs.close() " + e.toString() + "\n");
      }

      if (!isStatementClosed(ps,sb)) {
        passed = false;
        sb
            .append("Statement was not closed after result set was closed and getMoreResultsCalled ");
      }

      s = connection_.createStatement();

      sql = "drop table " + table;
      sb.append("Executing " + sql + "\n");
      s.executeUpdate(sql);

      assertCondition(passed,sb);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }

  
  // Test when multiple results sets. Should only be closed when all dependent
  // results sets have been closed.

  public void Var029() {
    String info = " -- verify that close on completion works when fetching multiple result sets";
    StringBuffer sb = new StringBuffer();
    String sql = "";
    boolean passed = true;
    try {

      sql = CALLQUERY3; 
      sb.append("Preparing "+sql+"\n"); 
      CallableStatement cs = connection_.prepareCall(sql);

      sb.append("Calling closeOnCompletion\n");
      JDReflectionUtil.callMethod_V(cs, "closeOnCompletion");

      boolean resultSetsAvailable = cs.execute();
      if ( !resultSetsAvailable) {
        sb.append("execute reports no results sets avaiable\n"); 
        passed = false; 
      }
      ResultSet rs = cs.getResultSet();
      while (rs.next()) {
	  sb.append("C1 has "+rs.getString(1)+"\n"); 
      }
      rs.close(); 
      sb.append("Calling more results to get 2nd rs \n");
      boolean more = cs.getMoreResults();
      if (more) { 
        if (isStatementClosed(cs,sb)) {
          sb.append("Error:  Statement is closed after moving to second rs\n"); 
          passed = false; 
        } else {
          rs = cs.getResultSet(); 
	  while (rs.next()) {
	      sb.append("C2 has "+rs.getString(1)+"\n"); 
	  }
	  rs.close(); 
          sb.append("Calling more results to get third rs\n ");
          more = cs.getMoreResults();
          if (more) { 
            if (isStatementClosed(cs, sb)) {
              sb
                  .append("Error:  Statement is closed after moving to third rs\n");
              passed = false;
	    } else {
		rs = cs.getResultSet();
		while (rs.next()){
		    sb.append("C3 has "+rs.getString(1)+"\n"); 
		}
		rs.close();

		// This should have closed the statement 
		if (!isStatementClosed(cs, sb)) {
		    sb.append("Statement should have been closed when closing the last result set\n");
		    passed = false; 
		} else {
		    sb.append("Statement closed as expected\n"); 
		}

	    }            
          } else {
            sb.append("Error:  Third RS not available\n"); 
            passed = false; 
          }
          
        }
      } else {
        sb.append("Error:  Second RS not available\n"); 
        passed = false; 
      }


      assertCondition(passed,sb);
    } catch (Exception e) {
      failed(e, "Unexpected exception -- " + sb.toString() + info);
    }
  }

  
  
  
}



