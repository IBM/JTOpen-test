///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASSFStatement.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////
//
//  File Name: AS400JDBCSFStatement.java
//  Classes:   AS400JDBCSFStatement
//
////////////////////////////////////////////////////////////////////////////
// 
////////////////////////////////////////////////////////////////////////////
package test;


import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.sql.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCStatement;

/**
 * Testcase JDASSFStatement Test for the Statement object covers for Seamless
 * failover
 **/
public class JDASSFStatement extends JDASTestcase {
  static final int RUN_SECONDS = 20;

  private String systemName;

  Connection transactionalConnection;
  Connection autocommitConnection;
  Connection failConnection;
  SocketProxyPair transactionalSocketProxyPair;
  SocketProxyPair autocommitSocketProxyPair;
  SocketProxyPair failSocketProxyPair;
  String collection;

  private String table;
  Vector cleanupSql = new Vector(); 

  /**
   * Constructor.
   **/
  public JDASSFStatement(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, "JDASSFStatement", namesAndVars, runMode,
        fileOutputStream, password, pwrSysUserID, pwrSysPassword);
    // originalPrintWriter_ = Trace.getPrintWriter();
  }

  public void setup() {
    String sql = "";
    try {
      systemName = systemObject_.getSystemName();

      if (isToolboxFixDate(TOOLBOX_FIX_DATE)) {
        url_ = "jdbc:as400://" + systemObject_.getSystemName() + ";user="
            + systemObject_.getUserId() ;

        String url;

        transactionalSocketProxyPair = SocketProxyPair.getSocketProxyPair(8001,
            8010, systemName, JDASTest.AS_DATABASE);

        url = "jdbc:as400:localhost:"
            + transactionalSocketProxyPair.getPortNumber1()
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1;"
            + "retryIntervalForClientReroute=1;"
            + "clientRerouteAlternateServerName=localhost;clientRerouteAlternatePortNumber="
            + transactionalSocketProxyPair.getPortNumber2();

        transactionalConnection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);
        transactionalConnection.setAutoCommit(false);
        transactionalConnection
            .setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        autocommitSocketProxyPair = SocketProxyPair.getSocketProxyPair(8021,
            8030, systemName, JDASTest.AS_DATABASE);

        url = "jdbc:as400:localhost:"
            + autocommitSocketProxyPair.getPortNumber1()
            + ";date format=jis;time format=jis;"
            + "enableClientAffinitiesList=1;enableSeamlessFailover=1;"
            + "retryIntervalForClientReroute=1;"
            + "clientRerouteAlternateServerName=localhost;clientRerouteAlternatePortNumber="
            + autocommitSocketProxyPair.getPortNumber2();

        autocommitConnection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);
        autocommitConnection.setAutoCommit(true);

        failSocketProxyPair = SocketProxyPair.getSocketProxyPair(8021, 8030,
            systemName, JDASTest.AS_DATABASE);

        url = "jdbc:as400:localhost:"
            + failSocketProxyPair.getPortNumber1()
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1;"
            + "retryIntervalForClientReroute=1;"
            + "clientRerouteAlternateServerName=localhost;clientRerouteAlternatePortNumber="
            + failSocketProxyPair.getPortNumber2();

        failConnection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        failConnection.setAutoCommit(true);

        collection = JDASTest.COLLECTION;
        table = collection + ".JDASEMLSS";
        Statement stmt = transactionalConnection.createStatement();
        try {
          stmt.executeUpdate("DROP TABLE " + table);
        } catch (Exception e) {

        }
        transactionalConnection.commit();

        stmt.executeUpdate("CREATE TABLE " + table
            + "(C1 INT, GENID INT GENERATED ALWAYS AS IDENTITY)");
        transactionalConnection.commit();

        System.out.println("DONE WITH SETUP");
      }

    } catch (Exception e) {
      System.out.println("Setup error.");
      System.out.println("Last sql statement was the following");
      System.out.println(sql);
      e.printStackTrace(System.out);
    }
  }

  /**
   * @exception Exception
   *              If an exception occurs.
   **/
  public void cleanup() throws Exception {
    try {
      if (isToolboxFixDate(TOOLBOX_FIX_DATE)) {

        Statement stmt = transactionalConnection.createStatement();
        Enumeration e = cleanupSql.elements(); 
        while (e.hasMoreElements()) {
          try { 
             String sql = (String) e.nextElement(); 
             stmt.execute(sql);
             transactionalConnection.commit(); 
          } catch (Exception ex) {
             // Ignore any cleanup exceptions 
          }
        }
        
        transactionalConnection.close();
        autocommitConnection.close();
        failConnection.close();
        transactionalSocketProxyPair.endService();
        autocommitSocketProxyPair.endService();
        failSocketProxyPair.endService();

      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  /**
   * Test to make sure that we can recover to the same system without an
   * exception
   **/
  public void Var001() {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    sb.append("Test a simple connect with enableClientAffinitiesList\n");
    sb.append("Make sure the connection is re-established after it drops\n");
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        for (int i = 0; i < 20; i++) {
          sb.append("Running query 1\n");
          Statement stmt = connection.createStatement();
          ResultSet rs = stmt.executeQuery("select * from sysibm.sysdummy1");
          while (rs.next()) {
            rs.getString(1);
          }
          rs.close();
          sb.append("Done running query\n");
          long activeByteCount = socketProxy.getActiveByteCount();
          sb.append("active byte count is " + activeByteCount + "\n");

          if (activeByteCount == 0) {
            passed = false;
            sb.append("FAILED.  active byte count was zero");
          }
          socketProxy.endActiveConnections();

          activeByteCount = socketProxy.getActiveByteCount();
          sb.append("active byte count after ending connections is "
              + activeByteCount + "\n");
          if (activeByteCount > 0) {
            passed = false;
            sb.append("active byte count is " + activeByteCount + "\n");
          }

          sb.append("Running query again should work without exception.  ");
          rs = stmt.executeQuery("select * from sysibm.sysdummy1");
          while (rs.next()) {
            rs.getString(1);
          }
          rs.close();

          sb.append("Running query again -- should work ");
          rs = stmt.executeQuery("select * from sysibm.sysdummy1");
          while (rs.next()) {
            rs.getString(1);
          }
          rs.close();
        }
        socketProxy.endService();
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  /* Test methods that don't need special processing */
  public void Var002() {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    sb.append("Test a simple connect with enableClientAffinitiesList\n");
    sb.append("Make sure the connection is re-established after it drops\n");
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();

        stmt.setCursorName("MYCURSOR4216");
        String sql = "Select * from sysibm.sysdummy1";
	sb.append("Running "+sql+"\n"); 
        ResultSet rs = stmt.executeQuery(sql);
        stmt.clearWarnings();
        stmt.getFetchDirection();
        stmt.getFetchSize();
        stmt.getMaxFieldSize();
	sql = "Select * from sysibm.sysdummy1";
	sb.append("Running "+sql+"\n"); 
        stmt.execute(sql);
        rs = stmt.getResultSet();
        Connection c1 = stmt.getConnection();
        c1.clearWarnings();
        stmt.getMaxRows();

        stmt.getPositionOfSyntaxError();
        stmt.getQueryTimeout();

        stmt.getResultSetConcurrency();
        stmt.getResultSetHoldability();
        stmt.getResultSetType();
        stmt.getUpdateCount();
        stmt.getWarnings();
        stmt.isClosed();
        stmt.setEscapeProcessing(false);
        stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
        stmt.setFetchSize(100);
        stmt.setMaxFieldSize(900);
        stmt.setMaxRows(900);
        stmt.setQueryTimeout(1000000);
        stmt.toString();
        stmt.setPoolable(false);
        stmt.isPoolable();
        stmt.getLargeUpdateCount();
        stmt.setLargeMaxRows(1000000);
        stmt.getLargeMaxRows();

        rs.close(); 
        stmt.closeOnCompletion();
        stmt.isCloseOnCompletion();
	sql = "CREATE TABLE QTEMP.JDASSFSTMT02(C1 INT)"; 
	sb.append("Running "+sql+"\n"); 
        stmt.execute(sql);
        rs = stmt.getResultSet();
        if (rs != null) {
          sb.append("Should get null for rs\n");
          passed = false;

        }

        stmt.addBatch("CREATE TABLE QTEMP.JDASSFSTMT02A(C1 INT)");
        stmt.addBatch("CREATE TABLE QTEMP.JDASSFSTMT02B(C1 INT)");
        stmt.addBatch("CREATE TABLE QTEMP.JDASSFSTMT02C(C1 INT)");
        stmt.clearBatch(); 
        
        connection.close();
        socketProxy.endService();
        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  /* Test boolean execute(java.lang.String) */
  public void Var003() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      boolean passed = true;
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean execute(java.lang.String)\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        // Working path
        stmt.execute("CREATE TABLE QTEMP.JDASSFSTMT03A(C1 INT)");
        connection.commit();
        // Seamless path
        socketProxy.endActiveConnections();
        stmt.execute("CREATE TABLE QTEMP.JDASSFSTMT03B(C1 INT)");
        connection.commit();

        // Within transaction path
        stmt.execute("CREATE TABLE QTEMP.JDASSFSTMT03C(C1 INT)");
        socketProxy.endActiveConnections();
        try {
          stmt.execute("CREATE TABLE QTEMP.JDASSFSTMT03D(C1 INT)");
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        stmt.execute("CREATE TABLE QTEMP.JDASSFSTMT03E(C1 INT)");
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();

        
        try {
          stmt.execute("CREATE TABLE QTEMP.JDASSFSTMT03F(C1 INT)");
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  /* Test boolean execute(java.lang.String, autoGeneratedKeys) */
  public void Var004() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      boolean passed = true;
      String sql = null; 
      String table = collection+".JDASSFSTMT04";
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean execute(java.lang.String, autoGeneratedKeys)\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        ResultSet rs = null; 
        // Setup
        try {
           sql = "DROP TABLE  "+table;
           cleanupSql.add(sql); 
           sb.append("Executing "+sql+"\n"); 
           stmt.execute(sql); 
        } catch (Exception e) { 
           sb.append("Ignored exception caught on "+sql); 
           printStackTraceToStringBuffer(e, sb);
        }
        sql="CREATE TABLE "+table+"(C1 INT)";
        sb.append("Executing "+sql+"\n"); 
        stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
        rs = stmt.getGeneratedKeys(); 
        
        
        // Working path
        sql = "insert into "+table+" VALUES(1)";
        sb.append("Executing "+sql+"\n"); 
        stmt.execute(sql,Statement.RETURN_GENERATED_KEYS);
        rs = stmt.getGeneratedKeys(); 
        connection.commit();

        // Seamless path
        socketProxy.endActiveConnections();
        sql = "insert into "+table+" VALUES(2)";
        sb.append("Executing "+sql+"\n"); 
        stmt.execute(sql,Statement.RETURN_GENERATED_KEYS);
        connection.commit();

        // Within transaction path
        sql = "insert into "+table+" VALUES(3)";
        sb.append("Executing "+sql+"\n"); 
        stmt.execute(sql,Statement.RETURN_GENERATED_KEYS);
        socketProxy.endActiveConnections();
        try {
          sql = "insert into "+table+" VALUES(4)";
          sb.append("Executing "+sql+"\n"); 
          stmt.execute(sql,Statement.RETURN_GENERATED_KEYS);
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        sql = "insert into "+table+" VALUES(5)";
        sb.append("Executing "+sql+"\n"); 
        stmt.execute(sql,Statement.RETURN_GENERATED_KEYS);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();

        
        try {
          sql = "insert into "+table+" VALUES(6)";
          sb.append("Executing "+sql+"\n"); 
          stmt.execute(sql,Statement.RETURN_GENERATED_KEYS);
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();
        

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  /* Test boolean execute(java.lang.String, autoGeneratedKeyIndexes) */
  public void Var005() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      int [] keys = {1}; 
      boolean passed = true;
      String sql = null; 
      String table = collection+".JDASSFSTMT05";
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean execute(java.lang.String, autoGeneratedIndexes)\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        
        // Setup
        try {
           sql = "DROP TABLE  "+table;
           cleanupSql.add(sql); 
           sb.append("Executing "+sql+"\n"); 
           stmt.execute(sql); 
        } catch (Exception e) { 
           sb.append("Ignored exception caught on "+sql); 
           printStackTraceToStringBuffer(e, sb);
        }
        sql="CREATE TABLE "+table+"(C1 INT)";
        stmt.execute(sql); 
        
        
        // Working path
        sql = "insert into "+table+" VALUES(1)";
        sb.append("Executing "+sql+"\n"); 
        stmt.execute(sql,keys);
        connection.commit();

        // Seamless path
        socketProxy.endActiveConnections();
        sql = "insert into "+table+" VALUES(2)";
        sb.append("Executing "+sql+"\n"); 
        stmt.execute(sql,keys);
        connection.commit();

        // Within transaction path
        sql = "insert into "+table+" VALUES(3)";
        sb.append("Executing "+sql+"\n"); 
        stmt.execute(sql,keys);
        socketProxy.endActiveConnections();
        try {
          sql = "insert into "+table+" VALUES(4)";
          sb.append("Executing "+sql+"\n"); 
          stmt.execute(sql,keys);
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        sql = "insert into "+table+" VALUES(5)";
        sb.append("Executing "+sql+"\n"); 
        stmt.execute(sql,keys);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();

        
        try {
          sql = "insert into "+table+" VALUES(6)";
          sb.append("Executing "+sql+"\n"); 
          stmt.execute(sql,keys);
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();
        

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  
  /* Test boolean execute(java.lang.String, autoGeneratedKeyNames) */
  public void Var006() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      String [] keys = {"C1"}; 
      boolean passed = true;
      String sql = null; 
      String table = collection+".JDASSFSTMT06";
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean execute(java.lang.String, autoGeneratedKeyNames)\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        
        // Setup
        try {
           sql = "DROP TABLE  "+table;
           cleanupSql.add(sql); 
           sb.append("Executing "+sql+"\n"); 
           stmt.execute(sql); 
        } catch (Exception e) { 
           sb.append("Ignored exception caught on "+sql); 
           printStackTraceToStringBuffer(e, sb);
        }
        sql="CREATE TABLE "+table+"(C1 INT)";
        stmt.execute(sql); 
        
        
        // Working path
        sql = "insert into "+table+" VALUES(1)";
        sb.append("Executing "+sql+"\n"); 
        stmt.execute(sql,keys);
        connection.commit();

        // Seamless path
        socketProxy.endActiveConnections();
        sql = "insert into "+table+" VALUES(2)";
        sb.append("Executing "+sql+"\n"); 
        stmt.execute(sql,keys);
        connection.commit();

        // Within transaction path
        sql = "insert into "+table+" VALUES(3)";
        sb.append("Executing "+sql+"\n"); 
        stmt.execute(sql,keys);
        socketProxy.endActiveConnections();
        try {
          sql = "insert into "+table+" VALUES(4)";
          sb.append("Executing "+sql+"\n"); 
          stmt.execute(sql,keys);
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        sql = "insert into "+table+" VALUES(5)";
        sb.append("Executing "+sql+"\n"); 
        stmt.execute(sql,keys);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();

        
        try {
          sql = "insert into "+table+" VALUES(6)";
          sb.append("Executing "+sql+"\n"); 
          stmt.execute(sql,keys);
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();
        

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  

  /* Test boolean executeUpdate(java.lang.String) */
  public void Var007() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      boolean passed = true;
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean executeUpdate(java.lang.String)\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        // Working path
        stmt.executeUpdate("CREATE TABLE QTEMP.JDASSFSTMT07A(C1 INT)");
        connection.commit();
        // Seamless path
        socketProxy.endActiveConnections();
        stmt.executeUpdate("CREATE TABLE QTEMP.JDASSFSTMT07B(C1 INT)");
        connection.commit();

        // Within transaction path
        stmt.executeUpdate("CREATE TABLE QTEMP.JDASSFSTMT07C(C1 INT)");
        socketProxy.endActiveConnections();
        try {
          stmt.executeUpdate("CREATE TABLE QTEMP.JDASSFSTMT07D(C1 INT)");
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        stmt.executeUpdate("CREATE TABLE QTEMP.JDASSFSTMT07E(C1 INT)");
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();

        
        try {
          stmt.executeUpdate("CREATE TABLE QTEMP.JDASSFSTMT07F(C1 INT)");
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  /* Test boolean executeUpdate(java.lang.String, autoGeneratedKeys) */
  public void Var008() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      boolean passed = true;
      String sql = null; 
      String table = collection+".JDASSFSTMT08";
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean executeUpdate(java.lang.String, autoGeneratedKeys)\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        ResultSet rs = null; 
        // Setup
        try {
           sql = "DROP TABLE  "+table;
           cleanupSql.add(sql); 
           sb.append("Executing "+sql+"\n"); 
           stmt.executeUpdate(sql); 
        } catch (Exception e) { 
           sb.append("Ignored exception caught on "+sql); 
           printStackTraceToStringBuffer(e, sb);
        }
        sql="CREATE TABLE "+table+"(C1 INT)";
        stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        rs = stmt.getGeneratedKeys(); 
        
        
        // Working path
        sql = "insert into "+table+" VALUES(1)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        rs = stmt.getGeneratedKeys(); 
        connection.commit();

        // Seamless path
        socketProxy.endActiveConnections();
        sql = "insert into "+table+" VALUES(2)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        connection.commit();

        // Within transaction path
        sql = "insert into "+table+" VALUES(3)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        socketProxy.endActiveConnections();
        try {
          sql = "insert into "+table+" VALUES(4)";
          sb.append("Executing "+sql+"\n"); 
          stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        sql = "insert into "+table+" VALUES(5)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();

        
        try {
          sql = "insert into "+table+" VALUES(6)";
          sb.append("Executing "+sql+"\n"); 
          stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();
        

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  /* Test boolean executeUpdate(java.lang.String, autoGeneratedKeyIndexes) */
  public void Var009() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      int [] keys = {1}; 
      boolean passed = true;
      String sql = null; 
      String table = collection+".JDASSFSTMT09";
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean executeUpdate(java.lang.String, autoGeneratedIndexes)\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        
        // Setup
        try {
           sql = "DROP TABLE  "+table;
           cleanupSql.add(sql); 
           sb.append("Executing "+sql+"\n"); 
           stmt.executeUpdate(sql); 
        } catch (Exception e) { 
           sb.append("Ignored exception caught on "+sql); 
           printStackTraceToStringBuffer(e, sb);
        }
        sql="CREATE TABLE "+table+"(C1 INT)";
        stmt.executeUpdate(sql); 
        
        
        // Working path
        sql = "insert into "+table+" VALUES(1)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql,keys);
        connection.commit();

        // Seamless path
        socketProxy.endActiveConnections();
        sql = "insert into "+table+" VALUES(2)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql,keys);
        connection.commit();

        // Within transaction path
        sql = "insert into "+table+" VALUES(3)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql,keys);
        socketProxy.endActiveConnections();
        try {
          sql = "insert into "+table+" VALUES(4)";
          sb.append("Executing "+sql+"\n"); 
          stmt.executeUpdate(sql,keys);
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        sql = "insert into "+table+" VALUES(5)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql,keys);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();

        
        try {
          sql = "insert into "+table+" VALUES(6)";
          sb.append("Executing "+sql+"\n"); 
          stmt.executeUpdate(sql,keys);
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();
        

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  
  /* Test boolean executeUpdate(java.lang.String, autoGeneratedKeyNames) */
  public void Var010() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      String [] keys = {"C1"}; 
      boolean passed = true;
      String sql = null; 
      String table = collection+".JDASSFSTMT10";
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean executeUpdate(java.lang.String, autoGeneratedKeyNames)\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        
        // Setup
        try {
           sql = "DROP TABLE  "+table;
           cleanupSql.add(sql); 
           sb.append("Executing "+sql+"\n"); 
           stmt.executeUpdate(sql); 
        } catch (Exception e) { 
           sb.append("Ignored exception caught on "+sql); 
           printStackTraceToStringBuffer(e, sb);
        }
        sql="CREATE TABLE "+table+"(C1 INT)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql); 
        
        
        // Working path
        sql = "insert into "+table+" VALUES(1)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql,keys);
        connection.commit();

        // Seamless path
        socketProxy.endActiveConnections();
        sql = "insert into "+table+" VALUES(2)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql,keys);
        connection.commit();

        // Within transaction path
        sql = "insert into "+table+" VALUES(3)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql,keys);
        socketProxy.endActiveConnections();
        try {
          sql = "insert into "+table+" VALUES(4)";
          sb.append("Executing "+sql+"\n"); 
          stmt.executeUpdate(sql,keys);
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        sql = "insert into "+table+" VALUES(5)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql,keys);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();

        
        try {
          sql = "insert into "+table+" VALUES(6)";
          sb.append("Executing "+sql+"\n"); 
          stmt.executeUpdate(sql,keys);
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();
        

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }


  /* Test boolean executeLargeUpdate(java.lang.String) */
  public void Var011() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      boolean passed = true;
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean executeLargeUpdate(java.lang.String)\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        // Working path
        stmt.executeLargeUpdate("CREATE TABLE QTEMP.JDASSFSTMT11A(C1 INT)");
        connection.commit();
        // Seamless path
        socketProxy.endActiveConnections();
        stmt.executeLargeUpdate("CREATE TABLE QTEMP.JDASSFSTMT11B(C1 INT)");
        connection.commit();

        // Within transaction path
        stmt.executeLargeUpdate("CREATE TABLE QTEMP.JDASSFSTMT11C(C1 INT)");
        socketProxy.endActiveConnections();
        try {
          stmt.executeLargeUpdate("CREATE TABLE QTEMP.JDASSFSTMT11D(C1 INT)");
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        stmt.executeLargeUpdate("CREATE TABLE QTEMP.JDASSFSTMT11E(C1 INT)");
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();

        
        try {
          stmt.executeLargeUpdate("CREATE TABLE QTEMP.JDASSFSTMT11F(C1 INT)");
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  /* Test boolean executeLargeUpdate(java.lang.String, autoGeneratedKeys) */
  public void Var012() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      boolean passed = true;
      String sql = null; 
      String table = collection+".JDASSFSTMT12";
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean executeLargeUpdate(java.lang.String, autoGeneratedKeys)\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        ResultSet rs = null; 
        // Setup
        try {
           sql = "DROP TABLE  "+table;
           cleanupSql.add(sql); 
           sb.append("Executing "+sql+"\n"); 
           stmt.executeLargeUpdate(sql); 
        } catch (Exception e) { 
           sb.append("Ignored exception caught on "+sql); 
           printStackTraceToStringBuffer(e, sb);
        }
        sql="CREATE TABLE "+table+"(C1 INT)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeLargeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        rs = stmt.getGeneratedKeys(); 
        
        
        // Working path
        sql = "insert into "+table+" VALUES(1)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeLargeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        rs = stmt.getGeneratedKeys(); 
        connection.commit();

        // Seamless path
        socketProxy.endActiveConnections();
        sql = "insert into "+table+" VALUES(2)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeLargeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        connection.commit();

        // Within transaction path
        sql = "insert into "+table+" VALUES(3)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeLargeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        socketProxy.endActiveConnections();
        try {
          sql = "insert into "+table+" VALUES(4)";
          sb.append("Executing "+sql+"\n"); 
          stmt.executeLargeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        sql = "insert into "+table+" VALUES(5)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeLargeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();

        
        try {
          sql = "insert into "+table+" VALUES(6)";
          sb.append("Executing "+sql+"\n"); 
          stmt.executeLargeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();
        

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  /* Test boolean executeLargeUpdate(java.lang.String, autoGeneratedKeyIndexes) */
  public void Var013() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      int [] keys = {1}; 
      boolean passed = true;
      String sql = null; 
      String table = collection+".JDASSFSTMT13";
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean executeLargeUpdate(java.lang.String, autoGeneratedIndexes)\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        
        // Setup
        try {
           sql = "DROP TABLE  "+table;
           cleanupSql.add(sql); 
           sb.append("Executing "+sql+"\n"); 
           stmt.executeLargeUpdate(sql); 
        } catch (Exception e) { 
           sb.append("Ignored exception caught on "+sql); 
           printStackTraceToStringBuffer(e, sb);
        }
        sql="CREATE TABLE "+table+"(C1 INT)";
        stmt.executeLargeUpdate(sql); 
        
        
        // Working path
        sql = "insert into "+table+" VALUES(1)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeLargeUpdate(sql,keys);
        connection.commit();

        // Seamless path
        socketProxy.endActiveConnections();
        sql = "insert into "+table+" VALUES(2)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeLargeUpdate(sql,keys);
        connection.commit();

        // Within transaction path
        sql = "insert into "+table+" VALUES(3)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeLargeUpdate(sql,keys);
        socketProxy.endActiveConnections();
        try {
          sql = "insert into "+table+" VALUES(4)";
          sb.append("Executing "+sql+"\n"); 
          stmt.executeLargeUpdate(sql,keys);
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        sql = "insert into "+table+" VALUES(5)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeLargeUpdate(sql,keys);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();

        
        try {
          sql = "insert into "+table+" VALUES(6)";
          sb.append("Executing "+sql+"\n"); 
          stmt.executeLargeUpdate(sql,keys);
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();
        

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  
  /* Test boolean executeLargeUpdate(java.lang.String, autoGeneratedKeyNames) */
  public void Var014() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      String [] keys = {"C1"}; 
      boolean passed = true;
      String sql = null; 
      String table = collection+".JDASSFSTMT14";
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean executeLargeUpdate(java.lang.String, autoGeneratedKeyNames)\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        
        // Setup
        try {
           sql = "DROP TABLE  "+table;
           cleanupSql.add(sql); 
           sb.append("Executing "+sql+"\n"); 
           stmt.executeLargeUpdate(sql); 
        } catch (Exception e) { 
           sb.append("Ignored exception caught on "+sql); 
           printStackTraceToStringBuffer(e, sb);
        }
        sql="CREATE TABLE "+table+"(C1 INT)";
        stmt.executeLargeUpdate(sql); 
        
        
        // Working path
        sql = "insert into "+table+" VALUES(1)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeLargeUpdate(sql,keys);
        connection.commit();

        // Seamless path
        socketProxy.endActiveConnections();
        sql = "insert into "+table+" VALUES(2)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeLargeUpdate(sql,keys);
        connection.commit();

        // Within transaction path
        sql = "insert into "+table+" VALUES(3)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeLargeUpdate(sql,keys);
        socketProxy.endActiveConnections();
        try {
          sql = "insert into "+table+" VALUES(4)";
          sb.append("Executing "+sql+"\n"); 
          stmt.executeLargeUpdate(sql,keys);
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        sql = "insert into "+table+" VALUES(5)";
        sb.append("Executing "+sql+"\n"); 
        stmt.executeLargeUpdate(sql,keys);
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();

        
        try {
          sql = "insert into "+table+" VALUES(6)";
          sb.append("Executing "+sql+"\n"); 
          stmt.executeLargeUpdate(sql,keys);
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();
        

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  
  /* Test boolean executeQuery(java.lang.String) */
  public void Var015() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      boolean passed = true;
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean executeQuery(java.lang.String)\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        ResultSet rs = null; 
        // Working path
        rs = stmt.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
        connection.commit();
        // Seamless path
        socketProxy.endActiveConnections();
        rs = stmt.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
        connection.commit();

        // Within transaction path
        rs = stmt.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
        socketProxy.endActiveConnections();
        try {
          rs = stmt.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        rs = stmt.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();

        
        try {
          rs = stmt.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  /* Test getMoreResults() */
  public void Var016() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      boolean passed = true;
      StringBuffer sb = new StringBuffer();
      sb.append("Test getMoreResults()\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        ResultSet rs = null;
        String sql = null ; 
        String procedure =  collection+".JDASSFSTMT16";
        // Setup 
        try {
          sql = "DROP PROCEDURE  "+procedure;
          cleanupSql.add(sql); 
          sb.append("Executing "+sql+"\n"); 
          stmt.executeLargeUpdate(sql); 
       } catch (Exception e) { 
          sb.append("Ignored exception caught on "+sql); 
          printStackTraceToStringBuffer(e, sb);
       }
       sql="CREATE PROCEDURE "+procedure+"() " +
       		"LANGUAGE SQL DYNAMIC RESULT SETS 3 " +
       		"BEGIN " +
       		"DECLARE C1 CURSOR FOR SELECT '1' FROM SYSIBM.SYSDUMMY1; " +
       		"DECLARE C2 CURSOR FOR SELECT '2' FROM SYSIBM.SYSDUMMY1; " +
       		"DECLARE C3 CURSOR FOR SELECT '3' FROM SYSIBM.SYSDUMMY1; " +
       		"OPEN C1; OPEN C2; OPEN C3;  " +
       		"SET RESULT SETS CURSOR C1, CURSOR C2, CURSOR C3; " +
       		"END ";
       stmt.execute(sql); 
       connection.commit(); 

        
        // Working path
        sql = "CALL  "+procedure+"()";
        sb.append("Executing sql "+sql+"\n"); 
        stmt.execute(sql);
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from first result set\n"); 
        stmt.getMoreResults();
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from second result set\n"); 
        stmt.getMoreResults();
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from third result set\n"); 
        
        connection.commit();
        
        // Seamless path
        sb.append("---------- Testing seamless path --------------\n"); 
        sql = "CALL  "+procedure+"()";
        sb.append("Executing sql "+sql+"\n"); 
        stmt.execute(sql);
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from first result set\n"); 
        
        socketProxy.endActiveConnections();
        try { 
        stmt.getMoreResults();
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from second result set\n"); 
        stmt.getMoreResults();
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from third result set\n"); 
        sb.append("Did not throw exception for getMoreResults\n");
        } catch (Exception e) {
          sb.append("Got Exception\n"); 
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
          
        }
        connection.commit();

        // Within transaction path
        sb.append("------------- testing within transaction path -----------------\n"); 
        rs = stmt.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
        stmt.execute("CALL  "+procedure+"()");
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from first result set\n"); 
        
        socketProxy.endActiveConnections();
        try {
          stmt.getMoreResults();
          rs = stmt.getResultSet();
          rs.next();
          sb.append("Got "+rs.getString(1)+" from second result set\n"); 
          stmt.getMoreResults();
          rs = stmt.getResultSet();
          rs.next();
          sb.append("Got "+rs.getString(1)+" from third result set\n"); 
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();

        stmt.execute("CALL  "+procedure+"()");
        rs = stmt.getResultSet();
        rs.next(); 
        sb.append("Got "+rs.getString(1)+" from first result set\n"); 
        rs.close();
        stmt.getMoreResults();
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from second result set\n"); 
        rs.close(); 
        stmt.getMoreResults();
        rs = stmt.getResultSet();
        rs.next(); 
        sb.append("Got "+rs.getString(1)+" from third result set\n"); 
        rs.close();

        
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();

        
        try {

          stmt.execute("CALL  "+procedure+"()");
          rs = stmt.getResultSet();
          rs.next(); 
          sb.append("Got "+rs.getString(1)+" from first result set\n"); 
          rs.close();
          stmt.getMoreResults();
          rs = stmt.getResultSet();
          rs.next();
          sb.append("Got "+rs.getString(1)+" from second result set\n"); 
          rs.close(); 
          stmt.getMoreResults();
          rs = stmt.getResultSet();
          rs.next(); 
          sb.append("Got "+rs.getString(1)+" from third result set\n"); 
          rs.close();
          
          
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  /* Test getMoreResults(int) */
  public void Var017() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      boolean passed = true;
      StringBuffer sb = new StringBuffer();
      sb.append("Test getMoreResults(int)\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        ResultSet rs = null;
        String sql = null ; 
        String procedure =  collection+".JDASSFSTMT16";
        // Setup 
        try {
          sql = "DROP PROCEDURE  "+procedure;
          cleanupSql.add(sql); 
          sb.append("Executing "+sql+"\n"); 
          stmt.executeLargeUpdate(sql); 
       } catch (Exception e) { 
          sb.append("Ignored exception caught on "+sql); 
          printStackTraceToStringBuffer(e, sb);
       }
       sql="CREATE PROCEDURE "+procedure+"() " +
          "LANGUAGE SQL DYNAMIC RESULT SETS 3 " +
          "BEGIN " +
          "DECLARE C1 CURSOR FOR SELECT '1' FROM SYSIBM.SYSDUMMY1; " +
          "DECLARE C2 CURSOR FOR SELECT '2' FROM SYSIBM.SYSDUMMY1; " +
          "DECLARE C3 CURSOR FOR SELECT '3' FROM SYSIBM.SYSDUMMY1; " +
          "OPEN C1; OPEN C2; OPEN C3;  " +
          "SET RESULT SETS CURSOR C1, CURSOR C2, CURSOR C3; " +
          "END ";
       stmt.execute(sql); 
       connection.commit(); 

        
        // Working path
        sql = "CALL  "+procedure+"()";
        sb.append("Executing sql "+sql+"\n"); 
        stmt.execute(sql);
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from first result set\n"); 
        stmt.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from second result set\n"); 
        stmt.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from third result set\n"); 
        
        connection.commit();
        
        // Seamless path
        sb.append("---------- Testing seamless path --------------\n"); 
        sql = "CALL  "+procedure+"()";
        sb.append("Executing sql "+sql+"\n"); 
        stmt.execute(sql);
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from first result set\n"); 
        
        socketProxy.endActiveConnections();
        try { 
        stmt.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from second result set\n"); 
        stmt.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from third result set\n"); 
        sb.append("Did not throw exception for getMoreResults\n");
        } catch (Exception e) {
          sb.append("Got Exception\n"); 
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
          
        }
        connection.commit();

        // Within transaction path
        sb.append("------------- testing within transaction path -----------------\n"); 
        rs = stmt.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
        stmt.execute("CALL  "+procedure+"()");
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from first result set\n"); 
        
        socketProxy.endActiveConnections();
        try {
          stmt.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
          rs = stmt.getResultSet();
          rs.next();
          sb.append("Got "+rs.getString(1)+" from second result set\n"); 
          stmt.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
          rs = stmt.getResultSet();
          rs.next();
          sb.append("Got "+rs.getString(1)+" from third result set\n"); 
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();

        stmt.execute("CALL  "+procedure+"()");
        rs = stmt.getResultSet();
        rs.next(); 
        sb.append("Got "+rs.getString(1)+" from first result set\n"); 
        rs.close();
        stmt.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
        rs = stmt.getResultSet();
        rs.next();
        sb.append("Got "+rs.getString(1)+" from second result set\n"); 
        rs.close(); 
        stmt.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
        rs = stmt.getResultSet();
        rs.next(); 
        sb.append("Got "+rs.getString(1)+" from third result set\n"); 
        rs.close();

        
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 100)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 100,
            Long.MAX_VALUE);

        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();

        
        try {

          stmt.execute("CALL  "+procedure+"()");
          rs = stmt.getResultSet();
          rs.next(); 
          sb.append("Got "+rs.getString(1)+" from first result set\n"); 
          rs.close();
          stmt.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
          rs = stmt.getResultSet();
          rs.next();
          sb.append("Got "+rs.getString(1)+" from second result set\n"); 
          rs.close(); 
          stmt.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
          rs = stmt.getResultSet();
          rs.next(); 
          sb.append("Got "+rs.getString(1)+" from third result set\n"); 
          rs.close();
          
          
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  
  /* Test boolean executeBatch() */
  public void Var018() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      boolean passed = true;
      String sql = null; 
      String table = collection+".JDASSFSTMT18";
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean executeBatch()\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        AS400JDBCStatement stmt2 = (AS400JDBCStatement) connection
            .createStatement();
        
        // Setup
        try {
           sql = "DROP TABLE  "+table;
           cleanupSql.add(sql); 
           sb.append("Executing "+sql+"\n"); 
           stmt.executeUpdate(sql); 
        } catch (Exception e) { 
           sb.append("Ignored exception caught on "+sql); 
           printStackTraceToStringBuffer(e, sb);
        }
        sql="CREATE TABLE "+table+"(C1 INT)";
        stmt.executeUpdate(sql); 
        
        
        // Working path
        sql = "insert into "+table+" VALUES(11)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql); 
        sql = "insert into "+table+" VALUES(12)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql); 
        sql = "insert into "+table+" VALUES(13)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql);
        sb.append("ExecuteBatch"); 
        stmt.executeBatch();
        connection.commit();

        // Seamless path -- still should fail. 
        
        sql = "insert into "+table+" VALUES(11)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql); 
        sql = "insert into "+table+" VALUES(12)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql); 
        sql = "insert into "+table+" VALUES(13)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql);
        socketProxy.endActiveConnections();
        try { 
            sb.append("ExecuteBatch"); 
            stmt.executeBatch();
            sb.append("ERROR:   executeBatch did not fail\n"); 
            passed = false; 
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("ERROR: Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }
        
        connection.commit();

        // Within transaction path
        sql = "insert into "+table+" VALUES(3)";
        sb.append("Executing "+sql+"\n"); 
        stmt2.executeUpdate(sql);
        stmt.clearBatch(); 
        sql = "insert into "+table+" VALUES(11)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql); 
        sql = "insert into "+table+" VALUES(12)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql); 
        sql = "insert into "+table+" VALUES(13)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql);
        
        socketProxy.endActiveConnections();
        try {
          sb.append("ExecuteBatch"); 
          stmt.executeBatch();
          sb.append("ERROR:  Did not throw exception within transaction\n");
          passed = false; 
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        
        socketProxy.endService();

        connection.close();
        

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  /* Test boolean executeLargeBatch() */
  public void Var019() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      boolean passed = true;
      String sql = null; 
      String table = collection+".JDASSFSTMT18";
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean executeLargeBatch()\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        AS400JDBCStatement stmt2 = (AS400JDBCStatement) connection
            .createStatement();
        
        // Setup
        try {
           sql = "DROP TABLE  "+table;
           cleanupSql.add(sql); 
           sb.append("Executing "+sql+"\n"); 
           stmt.executeUpdate(sql); 
        } catch (Exception e) { 
           sb.append("Ignored exception caught on "+sql); 
           printStackTraceToStringBuffer(e, sb);
        }
        sql="CREATE TABLE "+table+"(C1 INT)";
        stmt.executeUpdate(sql); 
        
        
        // Working path
        sql = "insert into "+table+" VALUES(11)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql); 
        sql = "insert into "+table+" VALUES(12)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql); 
        sql = "insert into "+table+" VALUES(13)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql);
        sb.append("executeLargeBatch"); 
        stmt.executeLargeBatch();
        connection.commit();

        // Seamless path -- still should fail. 
        
        sql = "insert into "+table+" VALUES(11)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql); 
        sql = "insert into "+table+" VALUES(12)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql); 
        sql = "insert into "+table+" VALUES(13)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql);
        socketProxy.endActiveConnections();
        try { 
            sb.append("executeLargeBatch"); 
            stmt.executeLargeBatch();
            sb.append("ERROR:   executeLargeBatch did not fail\n"); 
            passed = false; 
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("ERROR: Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }
        
        connection.commit();

        // Within transaction path
        sql = "insert into "+table+" VALUES(3)";
        sb.append("Executing "+sql+"\n"); 
        stmt2.executeUpdate(sql);
        stmt.clearBatch(); 
        sql = "insert into "+table+" VALUES(11)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql); 
        sql = "insert into "+table+" VALUES(12)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql); 
        sql = "insert into "+table+" VALUES(13)";
        sb.append("Adding to batch "+sql+"\n");
        stmt.addBatch(sql);
        
        socketProxy.endActiveConnections();
        try {
          sb.append("executeLargeBatch"); 
          stmt.executeLargeBatch();
          sb.append("ERROR:  Did not throw exception within transaction\n");
          passed = false; 
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        
        socketProxy.endService();

        connection.close();
        

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }


  
  /* Test boolean close() */
  public void Var020() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      boolean passed = true;
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean close()\n");
      try {
        SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort = socketProxy.getPortNumber();
        Thread proxyThread = new Thread(socketProxy);
        proxyThread.start();

        String url = "jdbc:as400:localhost:" + localPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1"
            + ";retryIntervalForClientReroute=1";

        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        AS400JDBCStatement stmt = (AS400JDBCStatement) connection
            .createStatement();
        ResultSet rs = null; 
        // Working path
        rs = stmt.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
        stmt.close(); 
        connection.commit();
        // Seamless path
        stmt = (AS400JDBCStatement) connection .createStatement();
        socketProxy.endActiveConnections();
        stmt.close(); 
        connection.commit();

        // Within transaction path
        stmt = (AS400JDBCStatement) connection .createStatement();
        rs = stmt.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
        socketProxy.endActiveConnections();
        try {
          stmt.close(); 
          passed = false;
          sb.append("Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        stmt = (AS400JDBCStatement) connection .createStatement();
        stmt.close(); 
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 1)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 1,
            Long.MAX_VALUE);
        
        sb.append("Ending active connections ");
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();

        stmt = (AS400JDBCStatement) connection .createStatement();
        
        try {
          stmt.close(); 
          passed = false;
          sb.append("Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        
        socketProxy.endService();

        connection.close();

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }



  
  /* Methods left to test */
  /*
   * 
   * -- Need to test the method 3 ways. -- Working -- Fails and retry works --
   * stmt.cancel();
   * JDReflectionUtil.callMethod_O(stmt,"unwrap",Class.forName("java.sql.Statement" )) ; 
   * JDReflectionUtil.callMethod_B(stmt,"isWrapperFor",Class.forName("java.sql.Statement")) ;
   * 
   * 
   
   */

}
