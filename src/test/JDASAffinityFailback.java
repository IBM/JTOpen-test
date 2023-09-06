///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASAffinityFailback.java
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
package test;


import java.io.FileOutputStream;
import java.util.Hashtable;
import java.sql.*;

import com.ibm.as400.access.AS400;

/**
 * Testcase JDASAffinityFailback
 **/
public class JDASAffinityFailback extends JDASTestcase {

  private static final String SERVER_IP_ADDRESS_QUERY = "select SERVER_IP_ADDRESS from qsys2.tcpip_info";
  private static final int AFFINITY_FAILBACK_INTERVAL = 5;

  String systemName;

  Connection transactionalConnection;
  String collection;

 
  private String secondarySystem;

  private SocketProxy primarySocketProxy;

  private Thread primaryProxyThread;

  private SocketProxy secondarySocketProxy;

  private Thread secondaryProxyThread;

  private String primaryIpAddress;

  /**
   * Constructor.
   **/
  public JDASAffinityFailback(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, "JDASAffinityFailback", namesAndVars, runMode,
        fileOutputStream, password, pwrSysUserID, pwrSysPassword);
    // originalPrintWriter_ = Trace.getPrintWriter();
  }

  public JDASAffinityFailback(AS400 systemObject, String testname, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, testname, namesAndVars, runMode,
        fileOutputStream, password, pwrSysUserID, pwrSysPassword);
    // originalPrintWriter_ = Trace.getPrintWriter();
  }

  
  
  public void setup() {
    String sql = "";
    try {
      secondarySystem = System.getProperty("jta.secondary.system"); 
      
      systemName = systemObject_.getSystemName();

  
      if (isToolboxFixDate(TOOLBOX_FIX_DATE )) {
  
        String url;

        primarySocketProxy = SocketProxy.getSocketProxy(7001, systemName,
            JDASTest.AS_DATABASE);
        int primaryPort = primarySocketProxy.getPortNumber();
        primaryProxyThread = new Thread(primarySocketProxy);
        primaryProxyThread.start();

        secondarySocketProxy = SocketProxy.getSocketProxy(7001, secondarySystem,
            JDASTest.AS_DATABASE);
        int secondaryPort = secondarySocketProxy.getPortNumber();
        secondaryProxyThread = new Thread(secondarySocketProxy);
        secondaryProxyThread.start();

        
       

        url = "jdbc:as400:localhost:"
            + primaryPort
            + ";enableClientAffinitiesList=1;enableSeamlessFailover=1;"
            + "affinityFailbackInterval="+AFFINITY_FAILBACK_INTERVAL+";" 
            + "retryIntervalForClientReroute=1;"
            +" maxRetriesForClientReroute=3;"
            + "clientRerouteAlternateServerName=localhost;clientRerouteAlternatePortNumber="
            + secondaryPort;

        transactionalConnection = testDriver_.getConnection(url,
            systemObject_.getUserId(), encryptedPassword_);
        transactionalConnection.setAutoCommit(false);
        transactionalConnection
            .setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);


        Statement stmt = transactionalConnection.createStatement(); 
        ResultSet rs = stmt.executeQuery(SERVER_IP_ADDRESS_QUERY); 
        rs.next(); 
        primaryIpAddress = rs.getString(1); 
        rs.close(); 
        stmt.close(); 
        transactionalConnection.commit(); 
        
        collection = JDASTest.COLLECTION;

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
        url_ = "jdbc:as400://" + systemObject_.getSystemName() + ";user="
            + systemObject_.getUserId() ;

        Connection connection = testDriver_.getConnection(url_,
            systemObject_.getUserId(), encryptedPassword_);

        Statement s = connection.createStatement();
        for (int i = 0; i < cleanupSql.length; i++) {
          try {
            s.execute(cleanupSql[i]);
          } catch (Exception e) {
            e.printStackTrace(System.out);
          }
        }
        connection.close();

        transactionalConnection.close();
        
        primarySocketProxy.endService(); 
        secondarySocketProxy.endService(); 
        
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  /**
   * 
   **/
  
  public void Var001() {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    sb.append(" Testcase to verify simple switch back to primary "); 
    sb.append("\n"); 
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      try {
        Statement stmt = transactionalConnection.createStatement();
        
        sb.append("Verifying that we on on primary\n"); 
        ResultSet rs = stmt.executeQuery(SERVER_IP_ADDRESS_QUERY);
        rs.next(); 
        String currentIpAddress = rs.getString(1);
	System.out.println("Current address(primary)="+currentIpAddress); 
        if (!currentIpAddress.equals(primaryIpAddress)) {
          passed = false; 
          sb.append("CurrentIpAddress is "+currentIpAddress+"\n"); 
          sb.append("                 sb "+primaryIpAddress+"\n"); 
        }
        rs.close(); 
        transactionalConnection.commit(); 
        
        sb.append("Switching to secondary\n"); 
        primarySocketProxy.endActiveConnections();
        primarySocketProxy.enable(false); 
        
        rs = stmt.executeQuery(SERVER_IP_ADDRESS_QUERY);
        rs.next(); 
        currentIpAddress = rs.getString(1); 
	System.out.println("Current address(secondary)="+currentIpAddress); 
        if (currentIpAddress.equals(primaryIpAddress)) {
          passed = false; 
          sb.append("Still on primary "+currentIpAddress+"\n"); 
          sb.append("     primary is  "+primaryIpAddress+"\n"); 
        }
        rs.close(); 
        transactionalConnection.commit(); 
        
        primarySocketProxy.enable(true);

        Thread.sleep( 2000);
	rs = stmt.executeQuery("SELECT * FROM SYSIBM.SYSDUMMY1");
	rs.close();
	sb.append("Calling commit to make sure it does not switch\n"); 
	transactionalConnection.commit();

	sb.append("Making sure still on secondary");

        rs = stmt.executeQuery(SERVER_IP_ADDRESS_QUERY);
        rs.next(); 
        currentIpAddress = rs.getString(1); 
	System.out.println("Current address(secondary)="+currentIpAddress); 
        if (currentIpAddress.equals(primaryIpAddress)) {
          passed = false; 
          sb.append("Still on primary "+currentIpAddress+"\n"); 
          sb.append("     primary is  "+primaryIpAddress+"\n"); 
        }
        rs.close(); 
        transactionalConnection.commit(); 


; 
        Thread.sleep((AFFINITY_FAILBACK_INTERVAL * 1000)-2000);
        sb.append("Calling commit to switch back\n"); 
        transactionalConnection.commit(); 
        
        
        sb.append("Verifying that we on on primary\n"); 
        rs = stmt.executeQuery(SERVER_IP_ADDRESS_QUERY);
        rs.next(); 
        currentIpAddress = rs.getString(1); 
	System.out.println("Current address(primary)="+currentIpAddress); 
        if (!currentIpAddress.equals(primaryIpAddress)) {
          passed = false; 
          sb.append("CurrentIpAddress is "+currentIpAddress+"\n"); 
          sb.append("                 sb "+primaryIpAddress+"\n"); 
        }
        rs.close(); 
        transactionalConnection.commit(); 
        
        
        
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    }
  }


  public void Var002() {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    sb.append(" Testcase to verify that connection continues to operate if primary still unavailable "); 
    sb.append("\n"); 
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
      try {
        Statement stmt = transactionalConnection.createStatement();
        
        sb.append("Verifying that we on on primary\n"); 
        ResultSet rs = stmt.executeQuery(SERVER_IP_ADDRESS_QUERY);
        rs.next(); 
        String currentIpAddress = rs.getString(1); 
        if (!currentIpAddress.equals(primaryIpAddress)) {
          passed = false; 
          sb.append("CurrentIpAddress is "+currentIpAddress+"\n"); 
          sb.append("                 sb "+primaryIpAddress+"\n"); 
        }
        rs.close(); 
        transactionalConnection.commit(); 
        
        sb.append("Switching to secondary\n"); 
        primarySocketProxy.endActiveConnections();
        primarySocketProxy.enable(false); 
        
        rs = stmt.executeQuery(SERVER_IP_ADDRESS_QUERY);
        rs.next(); 
        currentIpAddress = rs.getString(1); 
        if (currentIpAddress.equals(primaryIpAddress)) {
          passed = false; 
          sb.append("Still on primary "+currentIpAddress+"\n"); 
          sb.append("     primary is  "+primaryIpAddress+"\n"); 
        }
        rs.close(); 
        transactionalConnection.commit(); 
        
        for (int i = 0; i < 10; i++) {
          Thread.sleep(AFFINITY_FAILBACK_INTERVAL * 1000);
          sb.append("Calling commit to trigger switch back\n");
          long beforeSwitchTime = System.currentTimeMillis();
          transactionalConnection.commit();
          long afterSwitchTime = System.currentTimeMillis();
          long commitTime = afterSwitchTime - beforeSwitchTime;
          if (commitTime > 1000) {
            passed = false;
            sb.append("**** commit took " + commitTime + " ms\n");
          }

          rs = stmt.executeQuery(SERVER_IP_ADDRESS_QUERY);
          rs.next();
          currentIpAddress = rs.getString(1);
          if (currentIpAddress.equals(primaryIpAddress)) {
            passed = false;
            sb.append("Still on primary " + currentIpAddress + "\n");
            sb.append("     primary is  " + primaryIpAddress + "\n");
          }
          rs.close();
          transactionalConnection.commit();
        }
        

        
        
        
        
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception\n" + sb.toString());
      }
    } else {
    }
  }


  
  
}
