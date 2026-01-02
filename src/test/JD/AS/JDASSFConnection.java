///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASSFConnection.java
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
package test.JD.AS;


import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.sql.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnection;

import test.*;
import test.socketProxy.*;

/**
 * Testcase JDASSFStatement Test for the Statement object covers for Seamless
 * failover
 **/
public class JDASSFConnection extends JDASTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDASSFConnection";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDASTest.main(newArgs); 
   }
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
  Vector<String> cleanupSql1 = new Vector<String>(); 

  /**
   * Constructor.
   **/
  public JDASSFConnection(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, "JDASSFConnection", namesAndVars, runMode,
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

        output_.println("DONE WITH SETUP");
      }

    } catch (Exception e) {
      output_.println("Setup error.");
      output_.println("Last sql statement was the following");
      output_.println(sql);
      e.printStackTrace(output_);
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
        Enumeration<String> e = cleanupSql1.elements(); 
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

  

  public interface ConnectionMethodTest {
     String getTestDescription(); 
     void doMethod(AS400JDBCConnection con, StringBuffer sb, int i, boolean[] exceptionInTransaction) throws SQLException;
    
  }

  public void testOperation(ConnectionMethodTest testMethod) {

    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      AS400JDBCConnection connection = null; 
      boolean passed = true;
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
        connection = (AS400JDBCConnection) testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        connection.setAutoCommit(false);
        boolean[] exceptionInTransaction = new boolean[1]; 
        // Working path
        sb.append("Calling method 1\n"); 
        testMethod.doMethod(connection, sb, 1,exceptionInTransaction); 
        connection.commit();
       
       
        // Seamless path
        socketProxy.endActiveConnections();
        sb.append("Calling method 2\n"); 
        testMethod.doMethod(connection, sb, 2,exceptionInTransaction); 
        connection.commit();
        

        // Within transaction path
        sb.append("Calling method 3\n"); 
        testMethod.doMethod(connection, sb, 3,exceptionInTransaction); 
        try {
          sb.append("Calling  method 4\n"); 
          testMethod.doMethod(connection, sb, 4,exceptionInTransaction);
          if (exceptionInTransaction[0]) {
             passed = false;
             sb.append("FAILED: Did not throw exception within transaction\n");
          }
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
        
        // Unable to retry path 
        
        
        
        connection.commit();
        connection.close(); 
        
        socketProxy.endService();

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      } finally {
        try {
          connection.close();
        } catch (SQLException e) {
        }
      }
    } else {
      
    }
  
    
  }

  public class CommitMethodTest implements ConnectionMethodTest{
    public String getTestDescription() { return "commit() test"; }
    public void doMethod(AS400JDBCConnection con, StringBuffer sb, int i, boolean[] exceptionInTransaction) throws SQLException {
      con.commit(); 
      exceptionInTransaction[0] = false; 
    }
   
 }

  
  /**
   *  Test commit
   **/
  public void Var001() {
    testOperation(new CommitMethodTest());
   }

  
  

  public class CreateStatementMethodTest implements ConnectionMethodTest{
    public String getTestDescription() { return "createStatement() test"; }
    public void doMethod(AS400JDBCConnection con, StringBuffer sb, int i, boolean[] exceptionInTransaction) throws SQLException {
      con.createStatement(); 
      exceptionInTransaction[0] = false; 
    }
   
 }

  /**
   * Test createStatement()
   */

  public void Var002() {
    testOperation(new CreateStatementMethodTest());
   }

  
  public class CreateStatementTypeConcurrencyMethodTest implements ConnectionMethodTest{
    public String getTestDescription() { return "createStatement(resultSetType, resultSetConcurrency ) test"; }
    public void doMethod(AS400JDBCConnection con, StringBuffer sb, int i, boolean[] exceptionInTransaction) throws SQLException {
      con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY); 
      exceptionInTransaction[0] = false; 
    }
   
 }

  /**
   * Test createStatement()
   */

  public void Var003() {
    testOperation(new CreateStatementTypeConcurrencyMethodTest());
   }

  
  

}
