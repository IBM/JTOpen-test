///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASSeamlessFailover1.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////
//
//  File Name: AS400JDBCSeamlessFailover.java
//  Classes:   AS400JDBCSeamlessFailover
//
////////////////////////////////////////////////////////////////////////////
// 
////////////////////////////////////////////////////////////////////////////
package test.JD.AS;


import java.io.FileOutputStream;
import java.util.Hashtable; import java.util.Vector;
import java.sql.*;

import com.ibm.as400.access.AS400;

import test.JDASTest;
import test.SocketProxy;

/**
 * Testcase JDASSeamlessFailover
 **/
public class JDASSeamlessFailover1 extends JDASSeamlessFailover {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDASSeamlessFailover1";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDASTest.main(newArgs); 
   }

  /**
   * Constructor.
   **/
  public JDASSeamlessFailover1(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, "JDASSeamlessFailover1", namesAndVars, runMode,
        fileOutputStream, password, pwrSysUserID, pwrSysPassword);
    // originalPrintWriter_ = Trace.getPrintWriter();
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

  /**
   * Test to make sure that we connect to the second system when the first
   * system is not usable.
   **/
  public void Var002() {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    sb.append("Test a connect with enableClientAffinitiesList\n");
    sb.append("Make we connect to the second system and then connect to the first\n");
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      try {
        SocketProxy socketProxy1 = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort1 = socketProxy1.getPortNumber();
        Thread proxyThread1 = new Thread(socketProxy1);
        proxyThread1.start();
        socketProxy1.enable(false);

        SocketProxy socketProxy2 = SocketProxy.getSocketProxy(7011, systemName,
            JDASTest.AS_DATABASE);
        int localPort2 = socketProxy2.getPortNumber();
        Thread proxyThread2 = new Thread(socketProxy2);
        proxyThread2.start();

        String url = "jdbc:as400:localhost:"
            + localPort1
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1;"
            + "retryIntervalForClientReroute=1;"
            + "clientRerouteAlternateServerName=localhost;clientRerouteAlternatePortNumber="
            + localPort2;
        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        sb.append("Running query 1\n");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from sysibm.sysdummy1");
        while (rs.next()) {
          rs.getString(1);
        }
        sb.append("Done running query\n");

        long activeByteCount1 = socketProxy1.getActiveByteCount();
        sb.append("active byte count 1 is " + activeByteCount1 + "\n");
        long activeByteCount2 = socketProxy2.getActiveByteCount();
        sb.append("active byte count 2 is " + activeByteCount2 + "\n");

        if (activeByteCount2 == 0) {
          passed = false;
          sb.append("FAILED.  active byte count 2 was zero");
        }
        socketProxy1.enable(true);
        socketProxy2.endActiveConnections();

        activeByteCount2 = socketProxy2.getActiveByteCount();
        sb.append("active byte count after ending connections is "
            + activeByteCount2 + "\n");
        if (activeByteCount2 > 0) {
          passed = false;
          sb.append("active byte count is " + activeByteCount2 + "\n");
        }

        sb.append("Running query again should not get exception\n ");
        rs = stmt.executeQuery("select * from sysibm.sysdummy1");
        while (rs.next()) {
          rs.getString(1);
        }

        sb.append("Running query again -- should work \n");
        rs = stmt.executeQuery("select * from sysibm.sysdummy1");
        while (rs.next()) {
          rs.getString(1);
        }

        // Check that the query ran on the first socket connection.
        activeByteCount1 = socketProxy1.getActiveByteCount();
        sb.append("active byte count 1 is " + activeByteCount1 + "\n");
        activeByteCount2 = socketProxy2.getActiveByteCount();
        sb.append("active byte count 2 is " + activeByteCount2 + "\n");

        if (activeByteCount1 == 0) {
          passed = false;
          sb.append("FAILED.  active byte count 1 was zero");
        }

        socketProxy1.endService();
        socketProxy2.endService();
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
      
    }
  }

  /**
   * Test to make sure that we connect to the last system system when the first
   * system is not usable. In this case, we use a primary port that does not
   * exist. Also specify the properties so that the default port is used.
   **/
  public void Var003() {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    sb.append("Test a connect with enableClientAffinitiesList\n");
    sb.append("Make we connect to the second system and then connect to the first\n");
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      try {
        SocketProxy socketProxy1 = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort1 = socketProxy1.getPortNumber();
        Thread proxyThread1 = new Thread(socketProxy1);
        proxyThread1.start();
        socketProxy1.enable(false);

        SocketProxy socketProxy2 = SocketProxy.getSocketProxy(7011, systemName,
            JDASTest.AS_DATABASE);
        int localPort2 = socketProxy2.getPortNumber();
        Thread proxyThread2 = new Thread(socketProxy2);
        proxyThread2.start();

        String url = "jdbc:as400:localhost:7;enableClientAffinitiesList=1;enableSeamlessFailover=1;"
            + "retryIntervalForClientReroute=1;"
            + "clientRerouteAlternateServerName=localhost,localhost,"
            + systemName
            + ";clientRerouteAlternatePortNumber="
            + localPort1
            + "," + localPort2;
        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        sb.append("Running query 1\n");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from sysibm.sysdummy1");
        while (rs.next()) {
          rs.getString(1);
        }
        sb.append("Done running query\n");

        long activeByteCount1 = socketProxy1.getActiveByteCount();
        sb.append("active byte count 1 is " + activeByteCount1 + "\n");
        long activeByteCount2 = socketProxy2.getActiveByteCount();
        sb.append("active byte count 2 is " + activeByteCount2 + "\n");

        if (activeByteCount2 == 0) {
          passed = false;
          sb.append("FAILED.  active byte count 2 was zero");
        }
        socketProxy1.enable(true);
        socketProxy2.endActiveConnections();

        activeByteCount2 = socketProxy2.getActiveByteCount();
        sb.append("active byte count after ending connections is "
            + activeByteCount2 + "\n");
        if (activeByteCount2 > 0) {
          passed = false;
          sb.append("active byte count is " + activeByteCount2 + "\n");
        }

        sb.append("Running query again ");
        rs = stmt.executeQuery("select * from sysibm.sysdummy1");
        while (rs.next()) {
          rs.getString(1);
        }

        sb.append("Running query again -- should work ");
        rs = stmt.executeQuery("select * from sysibm.sysdummy1");
        while (rs.next()) {
          rs.getString(1);
        }

        // Check that the query ran on the first socket connection.
        activeByteCount1 = socketProxy1.getActiveByteCount();
        sb.append("active byte count 1 is " + activeByteCount1 + "\n");
        activeByteCount2 = socketProxy2.getActiveByteCount();
        sb.append("active byte count 2 is " + activeByteCount2 + "\n");

        if (activeByteCount1 == 0) {
          passed = false;
          sb.append("FAILED.  active byte count 1 was zero");
        }

        socketProxy1.endService();
        socketProxy2.endService();
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
      
    }
  }

  /**
   * Test to make sure that we connect to the last system system when the first
   * system is not usable. In this case, we use a primary server name that does
   * not exist. Also specify the properties so that the default port is used.
   **/
  public void Var004() {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    sb.append("Test a connect with enableClientAffinitiesList\n");
    sb.append("Make we connect to the second system and then connect to the first\n");
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      try {
        SocketProxy socketProxy1 = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int localPort1 = socketProxy1.getPortNumber();
        Thread proxyThread1 = new Thread(socketProxy1);
        proxyThread1.start();
        socketProxy1.enable(false);

        SocketProxy socketProxy2 = SocketProxy.getSocketProxy(7011, systemName,
            JDASTest.AS_DATABASE);
        int localPort2 = socketProxy2.getPortNumber();
        Thread proxyThread2 = new Thread(socketProxy2);
        proxyThread2.start();

        String url = "jdbc:as400:serverdoesnotexist.org;enableClientAffinitiesList=1;enableSeamlessFailover=1;"
            + "retryIntervalForClientReroute=1;"
            + "clientRerouteAlternateServerName=localhost,localhost,"
            + systemName
            + ";clientRerouteAlternatePortNumber="
            + localPort1
            + "," + localPort2;
        sb.append("Connecting to " + url + "\n");
        Connection connection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);

        sb.append("Running query 1\n");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from sysibm.sysdummy1");
        while (rs.next()) {
          rs.getString(1);
        }
        sb.append("Done running query\n");

        long activeByteCount1 = socketProxy1.getActiveByteCount();
        sb.append("active byte count 1 is " + activeByteCount1 + "\n");
        long activeByteCount2 = socketProxy2.getActiveByteCount();
        sb.append("active byte count 2 is " + activeByteCount2 + "\n");

        if (activeByteCount2 == 0) {
          passed = false;
          sb.append("FAILED.  active byte count 2 was zero");
        }
        socketProxy1.enable(true);
        socketProxy2.endActiveConnections();

        activeByteCount2 = socketProxy2.getActiveByteCount();
        sb.append("active byte count after ending connections is "
            + activeByteCount2 + "\n");
        if (activeByteCount2 > 0) {
          passed = false;
          sb.append("active byte count is " + activeByteCount2 + "\n");
        }

        sb.append("Running query again ");
        rs = stmt.executeQuery("select * from sysibm.sysdummy1");
        while (rs.next()) {
          rs.getString(1);
        }

        sb.append("Running query again -- should work ");
        rs = stmt.executeQuery("select * from sysibm.sysdummy1");
        while (rs.next()) {
          rs.getString(1);
        }

        // Check that the query ran on the first socket connection.
        activeByteCount1 = socketProxy1.getActiveByteCount();
        sb.append("active byte count 1 is " + activeByteCount1 + "\n");
        activeByteCount2 = socketProxy2.getActiveByteCount();
        sb.append("active byte count 2 is " + activeByteCount2 + "\n");

        if (activeByteCount1 == 0) {
          passed = false;
          sb.append("FAILED.  active byte count 1 was zero");
        }

        socketProxy1.endService();
        socketProxy2.endService();
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
      
    }
  }

  public void Var005() {
    notApplicable();
  }

  public void Var006() {
    notApplicable();
  }

  /* These are the same test as in JDASClientReroute */
  /* We run them again to make sure they work with seamless */

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with int parameters.
   **/
  public void Var007() {
    testPSTypeParametersSwitch(psIntTransactions, psIntParms, JAVA_INT);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with small parameters.
   **/
  public void Var008() {
    testPSTypeParametersSwitch(psSmallintTransactions, psSmallintParms,
        JAVA_SHORT);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with bigint parameters.
   **/
  public void Var009() {
    testPSTypeParametersSwitch(psBigintTransactions, psBigintParms, JAVA_LONG);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with real parameters.
   **/
  public void Var010() {
    testPSTypeParametersSwitch(psRealTransactions, psRealParms, JAVA_FLOAT);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with float parameters.
   **/
  public void Var011() {
    testPSTypeParametersSwitch(psFloatTransactions, psFloatParms, JAVA_DOUBLE);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Double parameters.
   **/
  public void Var012() {
    testPSTypeParametersSwitch(psDoubleTransactions, psDoubleParms, JAVA_DOUBLE);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Decimal parameters.
   **/
  public void Var013() {
    testPSTypeParametersSwitch(psDecimalTransactions, psDecimalParms,
        JAVA_BIGDECIMAL);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Numeric parameters.
   **/
  public void Var014() {
    testPSTypeParametersSwitch(psNumericTransactions, psNumericParms,
        JAVA_BIGDECIMAL);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Decfloat parameters.
   **/
  public void Var015() {
    testPSTypeParametersSwitch(psDecfloatTransactions, psDecfloatParms,
        JAVA_BIGDECIMAL);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Char parameters.
   **/
  public void Var016() {
    testPSTypeParametersSwitch(psCharTransactions, psCharParms, JAVA_STRING);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Varchar parameters.
   **/
  public void Var017() {
    testPSTypeParametersSwitch(psVarcharTransactions, psVarcharParms,
        JAVA_STRING);
  }
  
  
  
  
  
}
