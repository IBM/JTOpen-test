///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASSFPreparedStatement.java
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


import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;
import java.sql.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnection;
import com.ibm.as400.access.AS400JDBCPreparedStatement;
import com.ibm.as400.access.AS400JDBCStatement;

import test.socketProxy.SocketProxyPair;

/**
 * Testcase JDASSFPreparedStatement Test for the Statement object covers for
 * Seamless failover
 **/
public class JDASSFPreparedStatement extends JDASTestcase {
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
  public JDASSFPreparedStatement(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, "JDASSFPreparedStatement", namesAndVars, runMode,
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
          PreparedStatement stmt = connection
              .prepareStatement("select * from sysibm.sysdummy1");
          ResultSet rs = stmt.executeQuery();
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
          sb.append("Ending Active Connections1\n"); 
          socketProxy.endActiveConnections();

          activeByteCount = socketProxy.getActiveByteCount();
          sb.append("active byte count after ending connections is "
              + activeByteCount + "\n");
          if (activeByteCount > 0) {
            passed = false;
            sb.append("active byte count is " + activeByteCount + "\n");
          }

          sb.append("Running query again should work without exception.  ");
          rs = stmt.executeQuery();
          while (rs.next()) {
            rs.getString(1);
          }
          rs.close();

          sb.append("Running query again -- should work ");
          rs = stmt.executeQuery();
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

        AS400JDBCPreparedStatement stmt = (AS400JDBCPreparedStatement) connection
            .prepareStatement("Select * from sysibm.sysdummy1");

        stmt.setCursorName("MYCURSOR4216");

        ResultSet rs = stmt.executeQuery();
        
        stmt.clearWarnings();
        stmt.getFetchDirection();
        stmt.getFetchSize();
        stmt.getMaxFieldSize();
        stmt.execute();
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

        stmt.close();

        stmt = (AS400JDBCPreparedStatement) connection
            .prepareStatement("CALL QSYS2.QCMDEXC('DSPLIBL')");
        stmt.execute();
        rs = stmt.getResultSet();
        if (rs != null) {
          sb.append("Should get null for rs\n");
          passed = false;

        }

        stmt.closeOnCompletion();
        stmt.isCloseOnCompletion();

        connection.close();
        socketProxy.endService();
        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  /* Test inherited methods that are not usable. */
  public void Var003() {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    sb.append("Test inherited methods that are not usable\n");
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

        AS400JDBCPreparedStatement stmt = (AS400JDBCPreparedStatement) connection
            .prepareStatement("Select * from sysibm.sysdummy1");

        try {
          sb.append("Calling stmt.addBatch\n");
          stmt.addBatch("INSERT INTO X VALUES(1)");
        } catch (Exception e) {
          String expected = "Function sequence error";
          if (e.toString().indexOf(expected) < 0) {
            passed = false;
            sb.append("FAILED:  Did not find exception '" + expected + "'\n");
            printStackTraceToStringBuffer(e, sb);
          }
        }

        try {
          sb.append("Calling stmt.execute\n");
          stmt.execute("INSERT INTO X VALUES(1)");
        } catch (Exception e) {
          String expected = "Function sequence error";
          if (e.toString().indexOf(expected) < 0) {
            passed = false;
            sb.append("FAILED:  Did not find exception '" + expected + "'\n");
            printStackTraceToStringBuffer(e, sb);
          }
        }

        try {
          sb.append("Calling stmt.execute(RETURN_GENERATED_KEYS)\n");
          stmt.execute("INSERT INTO X VALUES(1)",
              Statement.RETURN_GENERATED_KEYS);
        } catch (Exception e) {
          String expected = "Function sequence error";
          if (e.toString().indexOf(expected) < 0) {
            passed = false;
            sb.append("FAILED:  Did not find exception '" + expected + "'\n");
            printStackTraceToStringBuffer(e, sb);
          }
        }

        try {
          sb.append("Calling stmt.execute(columnIndexes)\n");
          int[] columnIndexes = { 1 };
          stmt.execute("INSERT INTO X VALUES(1)", columnIndexes);
        } catch (Exception e) {
          String expected = "Function sequence error";
          if (e.toString().indexOf(expected) < 0) {
            passed = false;
            sb.append("FAILED:  Did not find exception '" + expected + "'\n");
            printStackTraceToStringBuffer(e, sb);
          }
        }

        try {
          sb.append("Calling stmt.execute(columnNames)\n");
          String[] columnNames = { "C1" };
          stmt.execute("INSERT INTO X VALUES(1)", columnNames);
        } catch (Exception e) {
          String expected = "Function sequence error";
          if (e.toString().indexOf(expected) < 0) {
            passed = false;
            sb.append("FAILED:  Did not find exception '" + expected + "'\n");
            printStackTraceToStringBuffer(e, sb);
          }
        }

        try {
          sb.append("Calling stmt.executeUpdate\n");
          stmt.executeUpdate("INSERT INTO X VALUES(1)");
        } catch (Exception e) {
          String expected = "Function sequence error";
          if (e.toString().indexOf(expected) < 0) {
            passed = false;
            sb.append("FAILED:  Did not find exception '" + expected + "'\n");
            printStackTraceToStringBuffer(e, sb);
          }
        }

        try {
          sb.append("Calling stmt.executeUpdate(RETURN_GENERATED_KEYS)\n");
          stmt.executeUpdate("INSERT INTO X VALUES(1)",
              Statement.RETURN_GENERATED_KEYS);
        } catch (Exception e) {
          String expected = "Function sequence error";
          if (e.toString().indexOf(expected) < 0) {
            passed = false;
            sb.append("FAILED:  Did not find exception '" + expected + "'\n");
            printStackTraceToStringBuffer(e, sb);
          }
        }

        try {
          sb.append("Calling stmt.executeUpdate(columnIndexes)\n");
          int[] columnIndexes = { 1 };
          stmt.executeUpdate("INSERT INTO X VALUES(1)", columnIndexes);
        } catch (Exception e) {
          String expected = "Function sequence error";
          if (e.toString().indexOf(expected) < 0) {
            passed = false;
            sb.append("FAILED:  Did not find exception '" + expected + "'\n");
            printStackTraceToStringBuffer(e, sb);
          }
        }

        try {
          sb.append("Calling stmt.executeUpdate(columnNames)\n");
          String[] columnNames = { "C1" };
          stmt.executeUpdate("INSERT INTO X VALUES(1)", columnNames);
        } catch (Exception e) {
          String expected = "Function sequence error";
          if (e.toString().indexOf(expected) < 0) {
            passed = false;
            sb.append("FAILED:  Did not find exception '" + expected + "'\n");
            printStackTraceToStringBuffer(e, sb);
          }
        }

        try {
          sb.append("Calling stmt.executeLargeUpdate\n");
          stmt.executeLargeUpdate("INSERT INTO X VALUES(1)");
        } catch (Exception e) {
          String expected = "Function sequence error";
          if (e.toString().indexOf(expected) < 0) {
            passed = false;
            sb.append("FAILED:  Did not find exception '" + expected + "'\n");
            printStackTraceToStringBuffer(e, sb);
          }
        }

        try {
          sb.append("Calling stmt.executeLargeUpdate(RETURN_GENERATED_KEYS)\n");
          stmt.executeLargeUpdate("INSERT INTO X VALUES(1)",
              Statement.RETURN_GENERATED_KEYS);
        } catch (Exception e) {
          String expected = "Function sequence error";
          if (e.toString().indexOf(expected) < 0) {
            passed = false;
            sb.append("FAILED:  Did not find exception '" + expected + "'\n");
            printStackTraceToStringBuffer(e, sb);
          }
        }

        try {
          sb.append("Calling stmt.executeLargeUpdate(columnIndexes)\n");
          int[] columnIndexes = { 1 };
          stmt.executeLargeUpdate("INSERT INTO X VALUES(1)", columnIndexes);
        } catch (Exception e) {
          String expected = "Function sequence error";
          if (e.toString().indexOf(expected) < 0) {
            passed = false;
            sb.append("FAILED:  Did not find exception '" + expected + "'\n");
            printStackTraceToStringBuffer(e, sb);
          }
        }

        try {
          sb.append("Calling stmt.executeLargeUpdate(columnNames)\n");
          String[] columnNames = { "C1" };
          stmt.executeLargeUpdate("INSERT INTO X VALUES(1)", columnNames);
        } catch (Exception e) {
          String expected = "Function sequence error";
          if (e.toString().indexOf(expected) < 0) {
            passed = false;
            sb.append("FAILED:  Did not find exception '" + expected + "'\n");
            printStackTraceToStringBuffer(e, sb);
          }
        }

        try {
          sb.append("Calling stmt.executeQuery(sql)\n");
          stmt.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
        } catch (Exception e) {
          String expected = "Function sequence error";
          if (e.toString().indexOf(expected) < 0) {
            passed = false;
            sb.append("FAILED:  Did not find exception '" + expected + "'\n");
            printStackTraceToStringBuffer(e, sb);
          }
        }

        connection.close();
        socketProxy.endService();
        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  
  
  /* Test set methods */
  public void Var004() {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    sb.append("Test set methods\n");
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

        AS400JDBCPreparedStatement pstmt = (AS400JDBCPreparedStatement) connection
            .prepareStatement("Select cast(? AS VARCHAR(80)) from sysibm.sysdummy1");
        
        pstmt.setNull(1,Types.VARCHAR);
        pstmt.setBoolean(1,true); 
        pstmt.setByte(1, (byte) 1); 
        pstmt.setShort(1, (short) 1); 
        pstmt.setInt(1, 1); 
        pstmt.setLong(1, 1); 
        pstmt.setFloat(1, 1); 
        pstmt.setDouble(1, 1); 
        pstmt.setBigDecimal(1,  new BigDecimal(1));
        pstmt.setString(1,  "hi"); 
        pstmt.setDate(1, new java.sql.Date(123456)); 
        pstmt.setTime(1, new java.sql.Time(1)); 
        pstmt.setTimestamp(1, new java.sql.Timestamp(123456));
        byte[] charBytes = { 0x31,0x32,0x33,0x34 }; 
        InputStream inputStream = new ByteArrayInputStream(charBytes);
        pstmt.setAsciiStream(1, inputStream, 4); 
        byte[] unicodeCharBytes = { 0x00,0x31,0x00,0x32,0x00,0x33,0x00,0x34 }; 
        InputStream unicodeInputStream = new ByteArrayInputStream(unicodeCharBytes);
        pstmt.setUnicodeStream(1, unicodeInputStream,  8);
        pstmt.setObject(1, "HI"); 
        pstmt.setObject(1, "HI", Types.VARCHAR); 
        pstmt.setObject(1, "HI", Types.VARCHAR,3); 


        pstmt.close(); 

        pstmt = (AS400JDBCPreparedStatement) connection
            .prepareStatement("Select cast(? AS VARBINARY(80)) from sysibm.sysdummy1");
        byte[] bytes = { 0, 1, 2, 3}; 
        
        pstmt.setBytes(1, bytes);
        inputStream = new ByteArrayInputStream(bytes);
        pstmt.setBinaryStream(1, inputStream, 4);
        pstmt.clearParameters(); 
        connection.close();
        socketProxy.endService();
        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
     
    }
  }

  /* Test boolean executeQuery() */
  public void Var005() {

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
        AS400JDBCPreparedStatement pstmt = (AS400JDBCPreparedStatement) connection
            .prepareStatement("SELECT * FROM SYSIBM.SYSDUMMY1");
        ResultSet rs = null; 
        // Working path
        rs = pstmt.executeQuery();
        connection.commit();
        // Seamless path
        socketProxy.endActiveConnections();
        rs = pstmt.executeQuery();
        connection.commit();

        // Within transaction path
        rs = pstmt.executeQuery();
        socketProxy.endActiveConnections();
        try {
          rs = pstmt.executeQuery();
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
        rs = pstmt.executeQuery();
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

        sb.append("Ending active connections2 ");
        socketProxy.endActiveConnections();

        
        try {
          rs = pstmt.executeQuery();
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


  /* Test boolean executeUpdate() */
  public void Var006() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      boolean passed = true;
      String sql = null; 
      String table = collection+".JDASSFPSMT06";
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean executeUpdate()\n");
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
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        rs = stmt.getGeneratedKeys(); 
        
        
        // Working path
        sql = "insert into "+table+" VALUES(?)";
        sb.append("Preparing "+sql+"\n"); 
        AS400JDBCPreparedStatement pstmt = (AS400JDBCPreparedStatement) connection
            .prepareStatement(sql );
        
        sb.append("Executing with 1\n"); 
        pstmt.setInt(1, 1); 
        pstmt.executeUpdate();
        connection.commit();

        // Seamless path
        socketProxy.endActiveConnections();
        sb.append("Executing with 2\n"); 
        pstmt.setInt(1, 2); 
        pstmt.executeUpdate();
        connection.commit();

        // Within transaction path
        sb.append("Executing with 3\n"); 
        pstmt.setInt(1, 3); 
        pstmt.executeUpdate();
        socketProxy.endActiveConnections();
        try {
          sb.append("Executing with 4\n"); 
          pstmt.setInt(1, 4); 
          pstmt.executeUpdate();
          passed = false;
          sb.append("FAILED: Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("FAILED: Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        sb.append("Setting up unable to retry path\n"); 
        pstmt.setInt(1, 5); 
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        sb.append("Executing with 5\n"); 
        pstmt.executeUpdate();
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

        sb.append("Ending active connections3 ");
        socketProxy.endActiveConnections();

        
        try {
          sb.append("Testing unable to retry path\n"); 
          sb.append("Executing with previously set 5\n"); 
          pstmt.executeUpdate();
          passed = false;
          sb.append("FAILED: Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("FAILED: Did not find '" + expected + "' in '" + eString + "'");
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
    
    
  /* Test boolean execute() */
  public void Var007() {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      boolean passed = true;
      String sql = null; 
      String table = collection+".JDASSFPSMT07";
      StringBuffer sb = new StringBuffer();
      sb.append("Test boolean execute()\n");
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
        stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
        rs = stmt.getGeneratedKeys(); 
        connection.commit();

        
        // Working path
        sql = "insert into "+table+" VALUES(?)";
        AS400JDBCPreparedStatement pstmt = (AS400JDBCPreparedStatement) connection
            .prepareStatement(sql );
        
        sb.append("Executing with 1\n"); 
        pstmt.setInt(1, 1); 
        pstmt.execute();
        connection.commit();

        // Seamless path
        socketProxy.endActiveConnections();
        sb.append("Executing with 2\n"); 
        pstmt.setInt(1, 2); 
        pstmt.execute();
        connection.commit();

        // Within transaction path
        sb.append("Executing with 3\n"); 
        pstmt.setInt(1, 3); 
        pstmt.execute();
        socketProxy.endActiveConnections();
        try {
          sb.append("Executing with 4\n"); 
          pstmt.setInt(1, 4); 
          pstmt.execute();
          passed = false;
          sb.append("FAILED: Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("FAILED: Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }

        connection.commit();

        // Unable to retry path
        sb.append("Setting up unable to retry path\n"); 
        pstmt.setInt(1, 5); 
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        sb.append("Executing with 5\n"); 
        pstmt.execute();
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

        sb.append("Ending active connections4 ");
        socketProxy.endActiveConnections();

        
        try {
          sb.append("Testing unable to retry path\n"); 
          sb.append("Executing with previously set 5\n"); 
          pstmt.execute();
          passed = false;
          sb.append("FAILED: Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("FAILED: Did not find '" + expected + "' in '" + eString + "'");
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
    
  public interface SetMethodTest {
    String getTableName(); 
    String getTestDescription(); 
    String getTableDefinition();
    void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException;
    /* call the get method on the result set to check the result.  Return false if incorrect */ 
    boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException; 
  }

  /* Test setInt() */
  public void runSetMethodTest(SetMethodTest testMethod ) {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      Connection connection = null; 
      Connection checkConnection = null; 
      boolean passed = true;
      String sql = null; 
      String table = collection+"."+testMethod.getTableName();
      StringBuffer sb = new StringBuffer();
      sb.append("Test "+testMethod.getTestDescription()+"\n");
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
        connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        sb.append("Connecting to " + url + "\n");
        checkConnection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);
        Statement checkStmt = checkConnection.createStatement(); 
        ResultSet checkRS = null; 
        sb.append("Autocommit is false\n"); 
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
        sql="CREATE TABLE "+table+ testMethod.getTableDefinition();
        sb.append("Executing "+sql+"\n"); 
        stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        
        rs = stmt.getGeneratedKeys(); 
        
        // Working path
        sql = "insert into "+table+" VALUES(?)";
        sb.append("Preparing "+sql+"\n"); 
        AS400JDBCPreparedStatement pstmt = (AS400JDBCPreparedStatement) connection
            .prepareStatement(sql );
        sb.append("Calling set method 1\n"); 
        testMethod.doSetMethod(pstmt, sb, 1); 
        sb.append("...executeUpdate\n"); 
        pstmt.executeUpdate();
        sb.append("..commit\n"); 
        connection.commit();
	sb.append("Running SELECT * FROM "+table+"\n"); 
        checkRS = checkStmt.executeQuery("SELECT * FROM "+table);
        checkRS.next(); 
        sb.append("..checking\n"); 
        passed = passed && testMethod.checkResult(checkRS, sb, 1);
        sb.append("..cleanup\n"); 
        checkStmt.execute("DELETE FROM "+table); 
        
        // Seamless path
        sb.append("Ending Active Connections5\n"); 
        socketProxy.endActiveConnections();
        sb.append("Calling set method 2\n"); 
        testMethod.doSetMethod(pstmt, sb, 2);
        sb.append("..executeUpdate\n"); 
        pstmt.executeUpdate();
        connection.commit();
        
        checkRS = checkStmt.executeQuery("SELECT * FROM "+table);
        checkRS.next(); 
        sb.append("..checking\n"); 
        passed = passed && testMethod.checkResult(checkRS, sb, 2);
        checkStmt.execute("DELETE FROM "+table); 

        // Within transaction path
        sb.append("Calling set method 3\n"); 
        testMethod.doSetMethod(pstmt, sb, 3); 
        sb.append("..executeUpdate\n"); 
        pstmt.executeUpdate();
        sb.append("Ending Active Connections6\n"); 
        socketProxy.endActiveConnections();
        try { 
        stmt.executeQuery("Select * from sysibm.sysdummy1");
        } catch (Exception e) {
          // Ignore failure. 
        }
        connection.commit(); 
        stmt.executeQuery("Select * from sysibm.sysdummy1");
        sb.append("Ending Active Connections7\n"); 
        socketProxy.endActiveConnections();
        try {
          sb.append("Calling set method 4\n"); 
          testMethod.doSetMethod(pstmt, sb, 4); 
          passed = false;
          sb.append("FAILED: Did not throw exception within transaction\n");
        } catch (Exception e) {
          String expected = "connection failed but has been re-established";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("FAILED: Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          } else {
            sb.append("Append:  Passed throwing exception within transaction\n"); 
          }
        }
        connection.commit();
        checkStmt.execute("DELETE FROM "+table); 

        sb.append("Ending Active Connections8\n"); 
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();

        // Get connection in working state
        // Seamless path after previous failure 
        sb.append("Calling set method 5\n"); 
        testMethod.doSetMethod(pstmt, sb, 5); 
        pstmt.execute(); 
        connection.commit();
        checkRS = checkStmt.executeQuery("SELECT * FROM "+table);
        checkRS.next(); 
        sb.append("..checking\n"); 
        passed = passed && testMethod.checkResult(checkRS, sb, 5);
        checkStmt.execute("DELETE FROM "+table); 

        sb.append("Ending Active Connections9\n"); 
        socketProxy.endActiveConnections();
        connection.commit(); 
        
        // Seamless path after previous failure 
        sb.append("Ending Active Connections10\n"); 
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        sb.append("Calling set method 6\n"); 
        testMethod.doSetMethod(pstmt, sb, 6); 
        sb.append("..executeUpdate\n"); 
        pstmt.executeUpdate();
        connection.commit(); 
        sb.append("Ending Active Connections11\n"); 
        socketProxy.endActiveConnections();
        connection.commit();
	sb.append("Running query: SELECT * FROM "+table+"\n"); 
        checkRS = checkStmt.executeQuery("SELECT * FROM "+table);
        checkRS.next(); 
        sb.append("..checking\n"); 
        passed = passed && testMethod.checkResult(checkRS, sb, 6);
        checkStmt.execute("DELETE FROM "+table); 
        
        
        // Unable to retry path
        sb.append("Setting up unable to retry path\n"); 
        sb.append("Ending Active Connections12\n"); 
        socketProxy.endActiveConnections();
        stmt.executeQuery("select * from sysibm.sysdummy1"); 
        connection.commit(); 
        sb.append("Ending Active Connections13\n"); 
        socketProxy.endActiveConnections();
        socketProxy.resetCounts();
        sb.append("Calling set method 7\n"); 
        testMethod.doSetMethod(pstmt, sb, 7); 
        // long toClientByteCount = failSocketProxyPair.getToClientByteCount();
        long toServerByteCount = socketProxy.getToServerByteCount();
        sb.append("toServerByteCount was "+toServerByteCount+"\n"); 
        connection.commit();


        sb.append("Ending active connections14 ");
        socketProxy.endActiveConnections();
        stmt.executeQuery("select * from sysibm.sysdummy1"); 
        connection.commit(); 
        sb.append("Ending Active Connections15\n"); 
        socketProxy.endActiveConnections();

        // Now set the limit so that we get a failure on the connection that
        // will continually repeat itself.
        socketProxy.resetCounts();
        sb.append("Setting endAfterByteCount(toServerByteCount="+(toServerByteCount - 10)+"\n"); 
        socketProxy.endAfterByteCount(toServerByteCount - 10, Long.MAX_VALUE);
        
        try {
          sb.append("Testing unable to retry path\n"); 
          sb.append("Calling set method 8\n"); 
          testMethod.doSetMethod(pstmt, sb, 8); 
          passed = false;
          sb.append("FAILED: Did not throw exception creating unusable proxies\n");
        } catch (Exception e) {
          String expected = "Communication link failure";
          String eString = e.toString();
          if (eString.indexOf(expected) < 0) {
            sb.append("FAILED: Did not find '" + expected + "' in '" + eString + "'");
            passed = false;
          }
        }
        socketProxy.endService();

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      } finally {
        try {
          connection.close();
        } catch (SQLException e) {
        }
        try {
          checkConnection.close();
        } catch (SQLException e) {
        } 
      }
    } else {
     
    }
  }

  public class SetIntMethodTest implements SetMethodTest {
    
    public String getTableName() { return "JDASSFPSMT08";}
    public String getTestDescription() { return "Test setInt()";} 
    public String getTableDefinition() { return "(C1 INT)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setInt(1, i); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      int answer = rs.getInt(1); 
      if (answer != i) {
        sb.append("Error checking result: Got "+answer+" sb "+i+"\n"); 
        return false; 
      }
      return true; 
    }
  }

  
  /* Test setInt() */
  public void Var008() {
    runSetMethodTest(new SetIntMethodTest()); 
  }
    
  public class SetBooleanMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT09";}
    public String getTestDescription() { return "Test setBoolean()";} 
    public String getTableDefinition() { return "(C1 VARCHAR(20))";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setBoolean(1, (i % 2) == 0); 
    }
      public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
        boolean answer = rs.getBoolean(1); 
        boolean expected = (i % 2) == 0; 
        if (answer != expected ) {
          sb.append("Error checking result: Got "+answer+" sb "+expected+"\n"); 
          return false; 
        }
        return true; 
      }
  }
    
  /* Test setBoolean() */
  public void Var009() {
    runSetMethodTest(new SetBooleanMethodTest()); 
  }
    
  public class SetByteMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT10";}
    public String getTestDescription() { return "Test setByte()";} 
    public String getTableDefinition() { return "(C1 INT)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setByte(1, (byte) i ); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      int answer = rs.getInt(1); 
      if (answer != i) {
        sb.append("Error checking result: Got "+answer+" sb "+i+"\n"); 
        return false; 
      }
      return true; 
    }
  }

  /* Test setByte() */
  public void Var010() {
    runSetMethodTest(new SetByteMethodTest()); 
  }

  public class SetShortMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT11";}
    public String getTestDescription() { return "Test setShort()";} 
    public String getTableDefinition() { return "(C1 SMALLINT)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setShort(1, (short) i ); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      int answer = rs.getInt(1); 
      if (answer != i) {
        sb.append("Error checking result: Got "+answer+" sb "+i+"\n"); 
        return false; 
      }
      return true; 
    }
  }

  
  /* Test setShort() */
  public void Var011() {
    runSetMethodTest(new SetByteMethodTest()); 
  }
  
  public class SetLongMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT12";}
    public String getTestDescription() { return "Test setLong()";} 
    public String getTableDefinition() { return "(C1 BIGINT)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setLong(1,  i ); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      int answer = rs.getInt(1); 
      if (answer != i) {
        sb.append("Error checking result: Got "+answer+" sb "+i+"\n"); 
        return false; 
      }
      return true; 
    }
  }
    

  /* Test setLong() */
  public void Var012() {
    runSetMethodTest(new SetLongMethodTest()); 
  }
  
  public class SetFloatMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT13";}
    public String getTestDescription() { return "Test setFloat()";} 
    public String getTableDefinition() { return "(C1 DOUBLE)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setFloat(1,  i ); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      int answer = rs.getInt(1); 
      if (answer != i) {
        sb.append("Error checking result: Got "+answer+" sb "+i+"\n"); 
        return false; 
      }
      return true; 
    }
  }
    
  /* Test setFloat() */
  public void Var013() {
    runSetMethodTest(new SetFloatMethodTest()); 
  }

  public class SetDoubleMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT14";}
    public String getTestDescription() { return "Test setDouble()";} 
    public String getTableDefinition() { return "(C1 DOUBLE)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setDouble(1,  i ); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      int answer = rs.getInt(1); 
      if (answer != i) {
        sb.append("Error checking result: Got "+answer+" sb "+i+"\n"); 
        return false; 
      }
      return true; 
    }
  }

  /* Test setDouble() */
  public void Var014() {
    runSetMethodTest(new SetDoubleMethodTest()); 
  }

  public class SetBigDecimalMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT15";}
    public String getTestDescription() { return "Test setBigDecimal()";} 
    public String getTableDefinition() { return "(C1 DECIMAL(10,2))";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setBigDecimal(1,  new BigDecimal(i) ); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      int answer = rs.getInt(1); 
      if (answer != i) {
        sb.append("Error checking result: Got "+answer+" sb "+i+"\n"); 
        return false; 
      }
      return true; 
    }
  }

  /* Test setBigDecimal() */
  public void Var015() {
    runSetMethodTest(new SetBigDecimalMethodTest()); 
  }

  public class SetStringMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT16";}
    public String getTestDescription() { return "Test setString()";} 
    public String getTableDefinition() { return "(C1 VARCHAR(80))";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setString(1,  "String"+i ); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      String answer = rs.getString(1); 
      String expected = "String"+i; 
      if (! expected.equals(answer)) {
        sb.append("Error checking result: Got "+answer+" sb "+expected+"\n"); 
        return false; 
      }
      return true; 
    }
  }

  /* Test setString() */
  public void Var016() {
    runSetMethodTest(new SetStringMethodTest()); 
  }

  public class SetBytesMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT17";}
    public String getTestDescription() { return "Test setBytes()";} 
    public String getTableDefinition() { return "(C1 VARBINARY(80))";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      byte[] bytes = new byte[1]; 
      bytes[0] = (byte) i; 
      pstmt.setBytes(1,  bytes ); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      byte[] answer = rs.getBytes(1);
      if (answer == null) { 
        sb.append("Error checking result: Got null \n"); 
        return false; 
      }
      if (answer.length != 1) {
        sb.append("Error checking result: Length of array is "+answer.length+" sb 1\n"); 
        return false; 
      }
      if (answer[0] != (byte) i) { 
        sb.append("Error checking results: answer[0]="+answer[0]+" sb "+i+"\n");
        return false; 
      }
      return true; 
    }
  }
  
  /* Test setBytes() */
  public void Var017() {
    runSetMethodTest(new SetBytesMethodTest()); 
  }

  public class SetDateMethodTest implements SetMethodTest {
    java.sql.Date date; 
    public String getTableName() { return "JDASSFPSMT18";}
    public String getTestDescription() { return "Test setDate()";} 
    public String getTableDefinition() { return "(C1 DATE)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      date = new java.sql.Date(3600000*24*i + System.currentTimeMillis()); 
      pstmt.setDate(1,  date ); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      Date answer = rs.getDate(1); 
      if (answer == null) { 
        sb.append("Error checking result: Got null \n"); 
        return false; 
      }
      if (answer.equals(date)) {
        sb.append("Error checking result: Got "+answer+" sb "+date +"\n"); 
        return false; 
      }
      return true; 
    }
  }
  
  /* Test setDate() */
  public void Var018() {
    runSetMethodTest(new SetDateMethodTest()); 
  }

  public class SetTimeMethodTest implements SetMethodTest {
    java.sql.Time time  = null; 
    public String getTableName() { return "JDASSFPSMT19";}
    public String getTestDescription() { return "Test setTime()";} 
    public String getTableDefinition() { return "(C1 Time)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      time = new java.sql.Time(36000*i); 
      pstmt.setTime(1,  time ); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      Time answer = rs.getTime(1); 
      if (answer == null) { 
        sb.append("Error checking result: Got null \n"); 
        return false; 
      }
      if (answer.equals(time)) {
        sb.append("Error checking result: Got "+answer+" sb "+time +"\n"); 
        return false; 
      }
      return true; 
    }
  }

  
  /* Test setTime() */
  public void Var019() {
    runSetMethodTest(new SetTimeMethodTest()); 
  }

  public class SetTimestampMethodTest implements SetMethodTest {
    java.sql.Timestamp ts = null; 
    public String getTableName() { return "JDASSFPSMT20";}
    public String getTestDescription() { return "Test setTimestamp()";} 
    public String getTableDefinition() { return "(C1 Timestamp)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      ts = new java.sql.Timestamp(3600000*24*i + System.currentTimeMillis()); 
      pstmt.setTimestamp(1,  ts ); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      Timestamp answer = rs.getTimestamp(1); 
      if (answer == null) { 
        sb.append("Error checking result: Got null \n"); 
        return false; 
      }
      if (!answer.equals(ts)) {
        sb.append("Error checking result: Got "+answer+" sb "+ts +"\n"); 
        return false; 
      }
      return true; 
    }
  }

  /* Test setTimestamp() */
  public void Var020() {
    runSetMethodTest(new SetTimestampMethodTest());
  }

  public class ClearParametersMethodTest implements SetMethodTest {
    public String getTableName() {      return "JDASSFPSMT21";  }
    public String getTestDescription() {      return "Test clearParameters()";    }
    public String getTableDefinition() {      return "(C1 INT)";    }
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb,
        int i) throws SQLException {
      switch (i) {
      case 4:
      case 7:
      case 8:
        pstmt.clearParameters();
        break;
      default:
        pstmt.clearParameters();
        pstmt.setInt(1, i);
        break;
      }
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      switch (i) { 
      case 4:
      case 7:
      case 8:
        return true;
      default:
          int answer = rs.getInt(1); 
          if (answer != i) {
            sb.append("Error checking result: Got "+answer+" sb "+i+"\n"); 
            return false; 
          }
          return true; 
      }
    }
  }

  /* Test clearParameters() */
  public void Var021() {
    runSetMethodTest(new ClearParametersMethodTest());
  }

  public class SetDateCalMethodTest extends SetDateMethodTest {
    public String getTableName() { return "JDASSFPSMT22";}
    public String getTestDescription() { return "Test setDate(cal)";} 
    public String getTableDefinition() { return "(C1 DATE)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      date = new java.sql.Date(3600000*24*i + System.currentTimeMillis()); 
      pstmt.setDate(1,  date, new GregorianCalendar() ); 
    }
  }

  /* Test setDate(cal) */
  public void Var022() {
    runSetMethodTest(new SetDateCalMethodTest()); 
  }

  public class SetTimeCalMethodTest extends SetTimeMethodTest {
    public String getTableName() { return "JDASSFPSMT23";}
    public String getTestDescription() { return "Test setTime(cal)";} 
    public String getTableDefinition() { return "(C1 Time)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      time = new java.sql.Time(36000*i); 
      pstmt.setTime(1,  time, new GregorianCalendar() ); 
    }
  }

  
  /* Test setTime(cal) */
  public void Var023() {
    runSetMethodTest(new SetTimeCalMethodTest()); 
  }

  public class SetTimestampCalMethodTest extends SetTimestampMethodTest {
    public String getTableName() { return "JDASSFPSMT24";}
    public String getTestDescription() { return "Test setTimestamp(cal)";} 
    public String getTableDefinition() { return "(C1 Timestamp)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      ts = new java.sql.Timestamp(3600000*24*i + System.currentTimeMillis()); 
      pstmt.setTimestamp(1,  ts, new GregorianCalendar() ); 
    }
  }

  /* Test setTimestamp() */
  public void Var024() {
    runSetMethodTest(new SetTimestampCalMethodTest());
  }

  /* Test setNull() */

  public class SetNullMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT25";}
    public String getTestDescription() { return "Test setNull()";} 
    public String getTableDefinition() { return "(C1 VARCHAR(80))";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setNull(1, Types.VARCHAR); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      String result = rs.getString(1); 
      if (result != null) {
        sb.append("Error checking results: expected null but got "+result); 
        return false; 
      }
      return true;
    }
  }


  public void Var025() {
    runSetMethodTest(new SetNullMethodTest());
  }
        
  public class SetNullStringMethodTest extends SetNullMethodTest {
    public String getTableName() { return "JDASSFPSMT26";}
    public String getTestDescription() { return "Test setNull(String)";} 
    public String getTableDefinition() { return "(C1 VARCHAR(80))";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setNull(1, Types.VARCHAR, "VARCHAR"); 
        }
  }


  /* Test setNull() */
  public void Var026() {
    runSetMethodTest(new SetNullStringMethodTest());
  }
   
  public class SetAsciiStreamLengthMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT27";}
    public String getTestDescription() { return "Test setAsciiStream(length)";} 
    public String getTableDefinition() { return "(C1 VARCHAR(80))";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      byte[] bytesArray = { 0x31, 0x32, 0x33, 0x34, 0x35 };
      ByteArrayInputStream asciiStream = new ByteArrayInputStream(bytesArray); 
      pstmt.setAsciiStream(1, asciiStream, bytesArray.length); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      String answer = rs.getString(1); 
      if (!"12345".equals(answer)) { 
        sb.append("Error checking results:  got '"+answer+"' sb '12345'\n");
        return false;
      }
      return true; 
    }
  }

  /* Test setAsciiStream() */
  public void Var027() {
    runSetMethodTest(new SetAsciiStreamLengthMethodTest());
  }

  public class SetUnicodeStreamMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT28";}
    public String getTestDescription() { return "Test setUnicodeStream()";} 
    public String getTableDefinition() { return "(C1 VARCHAR(80))";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      byte[] bytesArray = { 0x00, 0x31, 0x00, 0x32, 0x00, 0x33, 0x00, 0x34, 0x00, 0x35 };
      ByteArrayInputStream unicodeStream = new ByteArrayInputStream(bytesArray); 
      pstmt.setUnicodeStream(1, unicodeStream, bytesArray.length); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      String answer = rs.getString(1); 
      if (!"12345".equals(answer)) { 
        sb.append("Error checking results:  got '"+answer+"' sb '12345'\n");
        return false;
      }
      return true; 
    }
    
  }

  /* Test setUnicodeStream() */
  public void Var028() {
    runSetMethodTest(new SetUnicodeStreamMethodTest());
  }

  public class SetBinaryStreamLengthMethodTest implements SetMethodTest {
    byte[] bytesArray = { 0x31, 0x32, 0x33, 0x34, 0x35 };
    public String getTableName() { return "JDASSFPSMT29";}
    public String getTestDescription() { return "Test setBinaryStream(length)";} 
    public String getTableDefinition() { return "(C1 VARBINARY(80))";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      
      ByteArrayInputStream binaryStream = new ByteArrayInputStream(bytesArray); 
      pstmt.setBinaryStream(1, binaryStream, bytesArray.length); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      boolean passed = true; 
      byte[] answer = rs.getBytes(1); 
      if (answer == null) { 
        sb.append("Error checking result:  got null\n"); 
        return false; 
      }
      if (answer.length != bytesArray.length) { 
        sb.append("Error check results: legnth is "+answer.length+" sb "+bytesArray.length+"\n");
        return false; 
      }
      for (int j = 0; j < bytesArray.length; j++) { 
        if (answer[j] != bytesArray[j]) { 
          sb.append("Error check results: answer["+j+"] is "+answer[j]+" sb "+bytesArray[j]+"\n");
          passed = false; 
        }
      }
      return passed; 
    }
    
  }

  /* Test setBinaryStream() */
  public void Var029() {
    runSetMethodTest(new SetBinaryStreamLengthMethodTest());
  }
  
  public class SetObjectMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT30";}
    public String getTestDescription() { return "Test setObject()";} 
    public String getTableDefinition() { return "(C1 VARCHAR(80))";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setObject(1,  "String"+i ); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      String answer = rs.getString(1); 
      String expected = "String"+i; 
      if (! expected.equals(answer)) {
        sb.append("Error checking result: Got "+answer+" sb "+expected+"\n"); 
        return false; 
      }
      return true; 
    }
  }

  /* Test setObject() */ 
  public void Var030() {
    runSetMethodTest(new SetObjectMethodTest());
  }

  
  public class SetObjectTypeMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT31";}
    public String getTestDescription() { return "Test setObject(type)";} 
    public String getTableDefinition() { return "(C1 VARCHAR(80))";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setObject(1,  "String"+i, Types.VARCHAR ); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      String answer = rs.getString(1); 
      String expected = "String"+i; 
      if (! expected.equals(answer)) {
        sb.append("Error checking result: Got "+answer+" sb "+expected+"\n"); 
        return false; 
      }
      return true; 
    }
  }

  /* Test setObject(type) */ 
  public void Var031() {
    runSetMethodTest(new SetObjectTypeMethodTest());
  }

  public class SetObjectTypeScaleMethodTest implements SetMethodTest {
    
    public String getTableName() { return "JDASSFPSMT32";}
    public String getTestDescription() { return "Test setObject(type)";} 
    public String getTableDefinition() { return "(C1 DECIMAL(10,2))";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setObject(1,  new BigDecimal(i), Types.DECIMAL, 2 ); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      BigDecimal answer = rs.getBigDecimal(1); 
      BigDecimal expected = new BigDecimal(i);  
      expected = expected.setScale(2); 
      if (! expected.equals(answer)) {
        sb.append("Error checking result: Got "+answer+" sb "+expected+"\n"); 
        return false; 
      }
      return true; 
    }
  }

  /* Test setObject(type scale) */ 
  public void Var032() {
    runSetMethodTest(new SetObjectTypeScaleMethodTest());
  }

  /* Test setCharacterStream() */
  
  public class SetCharacterStreamLengthMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT33";}
    public String getTestDescription() { return "Test setCharacterStream(length)";} 
    public String getTableDefinition() { return "(C1 VARCHAR(80))";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      StringReader reader = new StringReader("12345"); 
      pstmt.setCharacterStream(1, reader, 5); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      String answer = rs.getString(1); 
      if (!"12345".equals(answer)) { 
        sb.append("Error checking results:  got '"+answer+"' sb '12345'\n");
        return false;
      }
      return true; 
    }
    
  }

  public void Var033() {
    runSetMethodTest(new SetCharacterStreamLengthMethodTest());
  }

  /* setRef not tested */ 


  /* Test setBlob() */

  public class SetBlobMethodTest implements SetMethodTest {
    Blob blob = null; 
    byte[] blobBytes = null; 
    public String getTableName() { return "JDASSFPSMT34";}
    public String getTestDescription() { return "Test setBlob()";} 
    public String getTableDefinition() { return "(C1 BLOB)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      blobBytes = new byte[5]; 
      for (int j = 0; j < blobBytes.length; j++) {
        blobBytes[j] = (byte)( i + j); 
      }
      blob = ((AS400JDBCConnection)pstmt.getConnection()).createBlob();
      blob.setBytes(1,blobBytes); 
      pstmt.setBlob(1, blob); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      boolean passed = true; 
      Blob answer = rs.getBlob(1);
      int answerLength = (int) answer.length();
      if (answer == null) {
        sb.append("Error checking results:  got null\n"); 
        return false; 
      }
      byte[] answerBytes = answer.getBytes(1, answerLength);
      if (answerBytes.length != blobBytes.length) { 
        sb.append("Error checking results:  answerBytes.length = "+answerBytes.length+" sb "+blobBytes.length+"\n"); 
        return false; 
      }
      for (int j = 0; j < blobBytes.length; j++) { 
        if (answerBytes[j] != blobBytes[j]) { 
          sb.append("Error check results:  answerBytes["+j+"] = "+answerBytes[j]+" sb "+blobBytes[j]+"\n");
          passed = false; 
        }
      }
      return passed; 
    }
    
  }

  public void Var034() {
    runSetMethodTest(new SetBlobMethodTest());
  }
  
  public class SetClobMethodTest implements SetMethodTest {
    Clob clob = null; 
    String clobString = null; 
    public String getTableName() { return "JDASSFPSMT35";}
    public String getTestDescription() { return "Test setClob()";} 
    public String getTableDefinition() { return "(C1 CLOB)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      if (clob == null) {
          clob = ((AS400JDBCConnection)pstmt.getConnection()).createClob();
          clobString ="12345";
          clob.setString(1, clobString );  
      }
      pstmt.setClob(1, clob); 
    }
    
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      boolean passed = true; 
      Clob answer = rs.getClob(1);
      if (answer == null) {
        sb.append("Error checking results:  got null\n"); 
        return false; 
      }
      int answerLength = (int) answer.length();
      String answerString = answer.getSubString(1, answerLength);
      if (!answerString.equals(clobString)) { 
        sb.append("Error checking results:  answerString = "+answerString+" sb "+clobString+"\n"); 
        return false; 
      }
      return passed; 
    }
    
  }

  /* Test setClob() */
  public void Var035() {
    runSetMethodTest(new SetClobMethodTest());
  }
  
  /* Test setURL() */

  public class SetURLMethodTest implements SetMethodTest {
    URL url = null; 
    public String getTableName() { return "JDASSFPSMT36";}
    public String getTestDescription() { return "Test setURL()";} 
    public String getTableDefinition() { return "(C1 VARCHAR(80))";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      try {
        url = new URL("http://www.ibm.com/index"+i+".html");
      } catch (Exception e) {
        SQLException sqlex = new SQLException(e.toString()); 
        sqlex.initCause(e);
        throw sqlex; 
      }
      pstmt.setURL(1, url); 
    }
    
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      boolean passed = true; 
      URL answer = rs.getURL(1);
      if (answer == null) {
        sb.append("Error checking results:  got null\n"); 
        return false; 
      }
      if (!answer.equals(url)) { 
        sb.append("Error checking results:  answer = "+answer+" sb "+url+"\n"); 
        return false; 
      }
      return passed; 
    }
    
  }

  public void Var036() {
    runSetMethodTest(new SetURLMethodTest());
  }

  public class SetClobReaderMethodTest extends SetClobMethodTest {
    public String getTableName() { return "JDASSFPSMT37";}
    public String getTestDescription() { return "Test setClob(reader)";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      clobString = "12345"; 
      pstmt.setClob(1, new StringReader(clobString)); 
    }
  }

  /* Test setClob() */
  public void Var037() {
    runSetMethodTest(new SetClobReaderMethodTest());
  }

  public class SetClobReaderLengthMethodTest extends SetClobMethodTest {
    public String getTableName() { return "JDASSFPSMT38";}
    public String getTestDescription() { return "Test setClob(reader length)";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      clobString = "12345"; 
      pstmt.setClob(1, new StringReader(clobString), clobString.length()); 
    }
  }

  /* Test setClob() */
  public void Var038() {
    runSetMethodTest(new SetClobReaderLengthMethodTest());
  }

  public class SetNClobReaderMethodTest extends SetClobMethodTest {
    public String getTableName() { return "JDASSFPSMT39";}
    public String getTestDescription() { return "Test setNClob(reader)";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      clobString = "12345"; 
      pstmt.setNClob(1, new StringReader(clobString)); 
    }
  }

  /* Test setClob() */
  public void Var039() {
    runSetMethodTest(new SetNClobReaderMethodTest());
  }
  
  
  public class SetAsciiStreamMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT40";}
    public String getTestDescription() { return "Test setAsciiStream()";} 
    public String getTableDefinition() { return "(C1 CLOB)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      byte[] bytesArray = { 0x31, 0x32, 0x33, 0x34, 0x35 };
      ByteArrayInputStream asciiStream = new ByteArrayInputStream(bytesArray); 
      pstmt.setAsciiStream(1, asciiStream); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      String answer = rs.getString(1); 
      if (!"12345".equals(answer)) { 
        sb.append("Error checking results:  got '"+answer+"' sb '12345'\n");
        return false;
      }
      return true; 
    }
  }

  /* Test setAsciiStream() */
  public void Var040() {
    runSetMethodTest(new SetAsciiStreamMethodTest());
  }

  public class SetAsciiStreamLongLengthMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT41";}
    public String getTestDescription() { return "Test setAsciiStream(long length)";} 
    public String getTableDefinition() { return "(C1 CLOB)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      byte[] bytesArray = { 0x31, 0x32, 0x33, 0x34, 0x35 };
      ByteArrayInputStream asciiStream = new ByteArrayInputStream(bytesArray); 
      pstmt.setAsciiStream(1, asciiStream, (long) bytesArray.length); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      String answer = rs.getString(1); 
      if (!"12345".equals(answer)) { 
        sb.append("Error checking results:  got '"+answer+"' sb '12345'\n");
        return false;
      }
      return true; 
    }
  }

  /* Test setAsciiStream() */
  public void Var041() {
    runSetMethodTest(new SetAsciiStreamLongLengthMethodTest());
  }

  public class SetBinaryStreamMethodTest extends SetBinaryStreamLengthMethodTest {
    public String getTableName() { return "JDASSFPSMT42";}
    public String getTestDescription() { return "Test setBinaryStream()";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      
      ByteArrayInputStream binaryStream = new ByteArrayInputStream(bytesArray); 
      pstmt.setBinaryStream(1, binaryStream); 
    }
  }

  /* Test setBinaryStream() */
  public void Var042() {
    runSetMethodTest(new SetBinaryStreamMethodTest());
  }

  public class SetBinaryStreamLongLengthMethodTest extends SetBinaryStreamLengthMethodTest {
    public String getTableName() { return "JDASSFPSMT43";}
    public String getTestDescription() { return "Test setBinaryStream(long length)";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      
      ByteArrayInputStream binaryStream = new ByteArrayInputStream(bytesArray); 
      pstmt.setBinaryStream(1, binaryStream, (long) bytesArray.length); 
    }
  }

  /* Test setBinaryStream() */
  public void Var043() {
    runSetMethodTest(new SetBinaryStreamLongLengthMethodTest());
  }

  
  public class SetBlobInputStreamMethodTest extends SetBlobMethodTest {
    public String getTableName() { return "JDASSFPSMT44";}
    public String getTestDescription() { return "Test setBlob(inputStream)";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      blobBytes = new byte[5]; 
      for (int j = 0; j < blobBytes.length; j++) {
        blobBytes[j] = (byte)( i + j); 
      }
      pstmt.setBlob(1, new ByteArrayInputStream(blobBytes)); 
    }
  }

  /* Test setBlob(inputStream) */
  public void Var044() {
    runSetMethodTest(new SetBlobInputStreamMethodTest());
  }

  public class SetBlobInputStreamLengthMethodTest extends SetBlobMethodTest {
    public String getTableName() { return "JDASSFPSMT45";}
    public String getTestDescription() { return "Test setBlob(inputStream length)";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      blobBytes = new byte[5]; 
      for (int j = 0; j < blobBytes.length; j++) {
        blobBytes[j] = (byte)( i + j); 
      }
      pstmt.setBlob(1, new ByteArrayInputStream(blobBytes), blobBytes.length); 
    }
  }

  /* Test setBlob(inputStream) */
  public void Var045() {
    runSetMethodTest(new SetBlobInputStreamMethodTest());
  }


  public class SetCharacterStreamMethodTest extends SetCharacterStreamLengthMethodTest {
    public String getTableName() { return "JDASSFPSMT46";}
    public String getTestDescription() { return "Test setCharacterStream()";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      StringReader reader = new StringReader("12345"); 
      pstmt.setCharacterStream(1, reader); 
    }
    
  }

  /* Test setCharacterStream() */
  public void Var046() {
    runSetMethodTest(new SetCharacterStreamMethodTest());
  }

  public class SetCharacterStreamLongLengthMethodTest extends SetCharacterStreamLengthMethodTest {
    public String getTableName() { return "JDASSFPSMT47";}
    public String getTestDescription() { return "Test setCharacterStream(long length)";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      StringReader reader = new StringReader("12345"); 
      pstmt.setCharacterStream(1, reader, (long) 5); 
    }
    
  }

  /* Test setCharacterStream() */
  public void Var047() {
    runSetMethodTest(new SetCharacterStreamLongLengthMethodTest());
  }
  

  public class SetNCharacterStreamMethodTest extends SetCharacterStreamLengthMethodTest {
    public String getTableName() { return "JDASSFPSMT48";}
    public String getTestDescription() { return "Test setNCharacterStream()";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      StringReader reader = new StringReader("12345"); 
      pstmt.setNCharacterStream(1, reader); 
    }
    
  }

  /* Test setCharacterStream() */
  public void Var048() {
    runSetMethodTest(new SetNCharacterStreamMethodTest());
  }

  public class SetNCharacterStreamLongLengthMethodTest extends SetCharacterStreamLengthMethodTest {
    public String getTableName() { return "JDASSFPSMT49";}
    public String getTestDescription() { return "Test setNCharacterStream(long length)";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      StringReader reader = new StringReader("12345"); 
      pstmt.setNCharacterStream(1, reader, (long) 5); 
    }
    
  }

  /* Test setCharacterStream() */
  public void Var049() {
    runSetMethodTest(new SetCharacterStreamLongLengthMethodTest());
  }
  
  public class SetNClobReaderLengthMethodTest extends SetClobMethodTest {
    public String getTableName() { return "JDASSFPSMT50";}
    public String getTestDescription() { return "Test setNClob(reader length)";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      clobString = "12345"; 
      pstmt.setNClob(1, new StringReader(clobString), clobString.length()); 
    }
  }

  /* Test setClob() */
  public void Var050() {
    runSetMethodTest(new SetNClobReaderLengthMethodTest());
  }


  public class SetNStringMethodTest extends SetStringMethodTest {
    public String getTableName() { return "JDASSFPSMT51";}
    public String getTestDescription() { return "Test setNString()";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setNString(1,  "String"+i ); 
    }
  }

  /* Test setString() */
  public void Var051() {
    runSetMethodTest(new SetNStringMethodTest()); 
  }


  public class SetDB2DefaultMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT52";}
    public String getTestDescription() { return "Test setDB2Default()";} 
    public String getTableDefinition() { return "(C1 VARCHAR(80) default '12345')";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setDB2Default(1); 
    }
    public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
      String answer = rs.getString(1); 
      String expected = "12345"; 
      if (! expected.equals(answer)) {
        sb.append("Error checking result: Got "+answer+" sb "+expected+"\n"); 
        return false; 
      }
      return true; 
    }
  }

  /* Test setString() */
  public void Var052() {
    runSetMethodTest(new SetDB2DefaultMethodTest()); 
  }


  public class SetDBDefaultMethodTest extends SetDB2DefaultMethodTest {
    public String getTableName() { return "JDASSFPSMT53";}
    public String getTestDescription() { return "Test setDBDefault()";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setDBDefault(1); 
    }
  }
  
  /* Test setString() */
  public void Var053() {
    runSetMethodTest(new SetDBDefaultMethodTest()); 
  }


  public class SetDB2UnassignedMethodTest extends SetDB2DefaultMethodTest {
    public String getTableName() { return "JDASSFPSMT54";}
    public String getTestDescription() { return "Test setDB2Unassigned()";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setDB2Unassigned(1); 
    }
  }
  
  /* Test setString() */
  public void Var054() {
    runSetMethodTest(new SetDB2UnassignedMethodTest()); 
  }

  public class SetDBUnassignedMethodTest extends SetDB2DefaultMethodTest {
    public String getTableName() { return "JDASSFPSMT55";}
    public String getTestDescription() { return "Test setDBUnassigned()";} 
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setDBUnassigned(1); 
    }
  }
  
  /* Test setString() */
  public void Var055() {
    runSetMethodTest(new SetDBUnassignedMethodTest()); 
  }

  
  public class GetDB2ParameterNameMethodTest extends ClearParametersMethodTest {
    public String getTableName() {      return "JDASSFPSMT56";  }
    public String getTestDescription() {      return "Test getDB2ParameterName()";    }
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb,
        int i) throws SQLException {
      switch (i) {
      case 4:
      case 7:
      case 8:
        pstmt.getDB2ParameterName(1);
        break;
      default:
        pstmt.getDB2ParameterName(1);
        pstmt.setInt(1, i);
        break;
      }
    }
  }

  /* Test getDB2ParameterName() */
  public void Var056() {
    runSetMethodTest(new GetDB2ParameterNameMethodTest());
  }

  public class GetParameterMetadataMethodTest extends ClearParametersMethodTest {
    public String getTableName() {      return "JDASSFPSMT57";  }
    public String getTestDescription() {      return "Test getParameterMetadata()";    }
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb,
        int i) throws SQLException {
      switch (i) {
      case 4:
      case 7:
      case 8:
        pstmt.getParameterMetaData();
        break;
      default:
        pstmt.getParameterMetaData();
        pstmt.setInt(1, i);
        break;
      }
    }
  }

  /* Test getDB2ParameterName() */
  public void Var057() {
    runSetMethodTest(new GetDB2ParameterNameMethodTest());
  }


  
  public class SetBooleanBooleanMethodTest implements SetMethodTest {
    public String getTableName() { return "JDASSFPSMT58";}
    public String getTestDescription() { return "Test setBoolean() on Boolean";} 
    public String getTableDefinition() { return "(C1 BOOLEAN)";}
    public void doSetMethod(AS400JDBCPreparedStatement pstmt, StringBuffer sb, int i) throws SQLException {
      pstmt.setBoolean(1, (i % 2) == 0); 
    }
      public boolean checkResult(ResultSet rs, StringBuffer sb, int i) throws SQLException {
        boolean answer = rs.getBoolean(1); 
        boolean expected = (i % 2) == 0; 
        if (answer != expected ) {
          sb.append("Error checking result: Got "+answer+" sb "+expected+"\n"); 
          return false; 
        }
        return true; 
      }
  }
    
  /* Test setBoolean() on Boolean*/
  public void Var058() {
    if (checkBooleanSupport()) { 
      runSetMethodTest(new SetBooleanBooleanMethodTest());
    }
  }
  
  
  /* Methods left to test */
  /*

     
  
JDBC42     public  void setObject(int parameterIndex,
        Object x,
        SQLType  
        targetSqlType,
        int scaleOrLength)

        
JDBC42            public void setObject(int parameterIndex,
        Object x,
        SQLType  
        targetSqlType)
 throws SQLException {

  public void setNClob(int parameterIndex, NClob clob) throws SQLException {
  public void setSQLXML(int parameterIndex, SQLXML xml) throws SQLException {
  public void setRowId(int parameterIndex, RowId x) throws SQLException
                  
  public ParameterMetaData getParameterMetaData() throws SQLException {
           
  setArray(int parameterIndex, Array x) 
   
   
   
   
   */

}
