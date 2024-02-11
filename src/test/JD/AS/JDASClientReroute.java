///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASClientReroute.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////
//
//  File Name: AS400JDBCEnableCALTestcase.java
//  Classes:   AS400JDBCEnableCALTestcase
//
////////////////////////////////////////////////////////////////////////////
// 
////////////////////////////////////////////////////////////////////////////
package test.JD.AS;


import java.io.FileOutputStream;
import java.util.Hashtable;
import java.sql.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;

import test.JDASTest;
import test.PasswordVault;
import test.SocketProxy;

/**
  Testcase JDASClientReroute
  
 **/
public class JDASClientReroute extends JDASTestcase
{

    
    
		String systemName;

		/**
      Constructor.  This is called from the AS400JDBCEnableCALTest constructor.
     **/
    public JDASClientReroute(AS400 systemObject,
        Hashtable namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password, String pwrSysUserID, String pwrSysPassword)
 {
		super(systemObject, "JDASClientReroute", namesAndVars, runMode,
		    fileOutputStream, password, pwrSysUserID, pwrSysPassword);
		// originalPrintWriter_ = Trace.getPrintWriter();
	}

    
    public JDASClientReroute(AS400 systemObject,
        String testname, 
        Hashtable namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password, String pwrSysUserID, String pwrSysPassword)
 {
    super(systemObject, testname, namesAndVars, runMode,
        fileOutputStream, password, pwrSysUserID, pwrSysPassword);
    // originalPrintWriter_ = Trace.getPrintWriter();
  }


  	public void setup() {
  		String sql=""; 
  		try {
  			systemName = systemObject_.getSystemName(); 
  			
  			fixupSql(setupSql); 
  			fixupSql(statementSql); 
  			fixupSql(callableStatementSql); 
  			fixupSql(cleanupSql); 
  			
  			if (isToolboxFixDate(TOOLBOX_FIX_DATE)) {
  				url_ = "jdbc:as400://" + systemObject_.getSystemName() + ";user="
  				    + systemObject_.getUserId() ;

  				Connection connection = testDriver_.getConnection(url_,
  				    systemObject_.getUserId(), encryptedPassword_);

  				Statement s = connection.createStatement();
  				for (int i = 0; i < setupSql.length; i++) {
  					sql = setupSql[i]; 
      				s.execute(sql);
  				}
  				connection.close();

  			}

  		} catch (Exception e) {
  			System.out.println("Setup error.");
  			System.out.println("Last sql statement was the following"); 
  			System.out.println(sql); 
  			e.printStackTrace(System.out);
  		}
  	}


  		/**
       @exception  Exception  If an exception occurs.
       **/
      public void cleanup() throws Exception
      {  
          try
          {
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

      			}
          }
          catch (Throwable e) { e.printStackTrace(); }
      }


	public void testPSTypeParametersSwitch(String[][] psTransactions, String[][][][] psParms, int parmType, int runSeconds) {
	  if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
		SocketProxy socketProxy1 = SocketProxy.getSocketProxy(7011, systemName, JDASTest.AS_DATABASE); 
		int localPort1 = socketProxy1.getPortNumber(); 
		Thread proxyThread1 = new Thread(socketProxy1); 
		proxyThread1.start(); 
		socketProxy1.enable(true); 
		
		SocketProxy socketProxy2 = SocketProxy.getSocketProxy(7021, systemName, JDASTest.AS_DATABASE); 
		int localPort2 = socketProxy2.getPortNumber(); 
		Thread proxyThread2 = new Thread(socketProxy2); 
		proxyThread2.start(); 
		socketProxy2.enable(false); 

		SocketProxy socketProxy3 = SocketProxy.getSocketProxy(7031, systemName, JDASTest.AS_DATABASE); 
		int localPort3 = socketProxy3.getPortNumber(); 
		Thread proxyThread3 = new Thread(socketProxy3); 
		proxyThread3.start(); 
		socketProxy3.enable(false); 

		SocketProxy socketProxy4 = SocketProxy.getSocketProxy(7031, systemName, JDASTest.AS_DATABASE); 
		int localPort4 = socketProxy4.getPortNumber(); 
		Thread proxyThread4 = new Thread(socketProxy4); 
		proxyThread4.start(); 
		socketProxy4.enable(false); 

		SocketProxy[] proxies = new SocketProxy[4]; 
		
		proxies[0] = socketProxy1; 
		proxies[1] = socketProxy2; 
		proxies[2] = socketProxy3; 
		proxies[3] = socketProxy4;
		StringBuffer sb; 
		SwitchKillThread switchKillThread;
		String url; 
		try { 
		Connection killerConnection = testDriver_.getConnection(url_,
		    systemObject_.getUserId(), encryptedPassword_);

		sb = new StringBuffer(); 
		switchKillThread = new SwitchKillThread(killerConnection, 
				"", MINIMUM_TRANSACTION_MILLISECONDS, sb, proxies);
		
		
		url = "jdbc:as400:localhost:7;enableClientAffinitiesList=1;retryIntervalForClientReroute=10;"+
				"clientRerouteAlternateServerName=localhost,localhost,localhost,localhost;" +
					"clientRerouteAlternatePortNumber="+localPort1+","+localPort2+","+localPort3+","+localPort4;

		} catch (Exception e) { 
			failed(e); 
			return; 
		}
		testPSTypeParameters(psTransactions, psParms, parmType, url, switchKillThread, runSeconds, sb);
		// Cleanup  
		for (int i = 0; i < proxies.length; i++) {
			proxies[i].endService(); 
		}
	  }  
	}

  public void testDSPSTypeParametersSwitch(String[][] psTransactions, String[][][][] psParms, int parmType, int runSeconds) {
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
    SocketProxy socketProxy1 = SocketProxy.getSocketProxy(7011, systemName, JDASTest.AS_DATABASE); 
    int localPort1 = socketProxy1.getPortNumber(); 
    Thread proxyThread1 = new Thread(socketProxy1); 
    proxyThread1.start(); 
    socketProxy1.enable(true); 
    
    SocketProxy socketProxy2 = SocketProxy.getSocketProxy(7021, systemName, JDASTest.AS_DATABASE); 
    int localPort2 = socketProxy2.getPortNumber(); 
    Thread proxyThread2 = new Thread(socketProxy2); 
    proxyThread2.start(); 
    socketProxy2.enable(false); 

    SocketProxy socketProxy3 = SocketProxy.getSocketProxy(7031, systemName, JDASTest.AS_DATABASE); 
    int localPort3 = socketProxy3.getPortNumber(); 
    Thread proxyThread3 = new Thread(socketProxy3); 
    proxyThread3.start(); 
    socketProxy3.enable(false); 

    SocketProxy socketProxy4 = SocketProxy.getSocketProxy(7031, systemName, JDASTest.AS_DATABASE); 
    int localPort4 = socketProxy4.getPortNumber(); 
    Thread proxyThread4 = new Thread(socketProxy4); 
    proxyThread4.start(); 
    socketProxy4.enable(false); 

    SocketProxy[] proxies = new SocketProxy[4]; 
    
    proxies[0] = socketProxy1; 
    proxies[1] = socketProxy2; 
    proxies[2] = socketProxy3; 
    proxies[3] = socketProxy4;
    StringBuffer sb; 
    SwitchKillThread switchKillThread;
    Connection killerConnection = null; 
    Connection connection; 
    try { 
      char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
      AS400JDBCDataSource killerDS = new AS400JDBCDataSource(system_, systemObject_.getUserId(), passwordChars);
      killerConnection  = killerDS.getConnection(); 

    sb = new StringBuffer(); 
    switchKillThread = new SwitchKillThread(killerConnection, 
        "", MINIMUM_TRANSACTION_MILLISECONDS, sb, proxies);
    
    
    PasswordVault.clearPassword(passwordChars);
    AS400JDBCDataSource ds = new AS400JDBCDataSource("localhost", systemObject_.getUserId(), passwordChars);
    
    ds.setPortNumber(7);
    ds.setEnableClientAffinitiesList(1);
    ds.setRetryIntervalForClientReroute(10);
    ds.setClientRerouteAlternateServerName("localhost,localhost,localhost,localhost");
    ds.setClientRerouteAlternatePortNumber(localPort1+","+localPort2+","+localPort3+","+localPort4);
    connection = ds.getConnection(); 
    } catch (Exception e) { 
      failed(e); 
      return; 
    }
    testPSTypeParameters(psTransactions, psParms, parmType, connection, killerConnection, switchKillThread, runSeconds, sb);
    // Cleanup  
    for (int i = 0; i < proxies.length; i++) {
      proxies[i].endService(); 
    }
    }  
  }

	
  
	public void testCSTypeParametersSwitch( String[][] csTransactions, String[][][][] csParms, int parmType, int runSeconds) {
	  if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
		SocketProxy socketProxy1 = SocketProxy.getSocketProxy(7011, systemName, JDASTest.AS_DATABASE); 
		int localPort1 = socketProxy1.getPortNumber(); 
		Thread proxyThread1 = new Thread(socketProxy1); 
		proxyThread1.start(); 
		socketProxy1.enable(true); 
		
		SocketProxy socketProxy2 = SocketProxy.getSocketProxy(7021, systemName, JDASTest.AS_DATABASE); 
		int localPort2 = socketProxy2.getPortNumber(); 
		Thread proxyThread2 = new Thread(socketProxy2); 
		proxyThread2.start(); 
		socketProxy2.enable(false); 

		SocketProxy socketProxy3 = SocketProxy.getSocketProxy(7031, systemName, JDASTest.AS_DATABASE); 
		int localPort3 = socketProxy3.getPortNumber(); 
		Thread proxyThread3 = new Thread(socketProxy3); 
		proxyThread3.start(); 
		socketProxy3.enable(false); 

		SocketProxy socketProxy4 = SocketProxy.getSocketProxy(7031, systemName, JDASTest.AS_DATABASE); 
		int localPort4 = socketProxy4.getPortNumber(); 
		Thread proxyThread4 = new Thread(socketProxy4); 
		proxyThread4.start(); 
		socketProxy4.enable(false); 

		SocketProxy[] proxies = new SocketProxy[4]; 
		
		proxies[0] = socketProxy1; 
		proxies[1] = socketProxy2; 
		proxies[2] = socketProxy3; 
		proxies[3] = socketProxy4;
		StringBuffer sb; 
		SwitchKillThread switchKillThread;
    Connection killerConnection;
    Connection connection; 
		String url; 
		try { 
		killerConnection = testDriver_.getConnection(url_,
		    systemObject_.getUserId(), encryptedPassword_);

		sb = new StringBuffer(); 
		switchKillThread = new SwitchKillThread(killerConnection, 
				"", MINIMUM_TRANSACTION_MILLISECONDS, sb, proxies);
		
		
		url = "jdbc:as400:localhost:7;enableClientAffinitiesList=1;retryIntervalForClientReroute=10;"+
				"clientRerouteAlternateServerName=localhost,localhost,localhost,localhost;" +
					"clientRerouteAlternatePortNumber="+localPort1+","+localPort2+","+localPort3+","+localPort4;

    sb.append("Connecting to " + url + "\n");
     connection = testDriver_.getConnection(url,
        systemObject_.getUserId(), encryptedPassword_);


		} catch (Exception e) { 
			failed(e); 
			return; 
		}
		testCSTypeParameters(csTransactions, csParms, parmType, connection, killerConnection, switchKillThread, runSeconds,  sb);
		// Cleanup  
		for (int i = 0; i < proxies.length; i++) {
			proxies[i].endService(); 
		}

	}
	}
  public void testDSCSTypeParametersSwitch( String[][] csTransactions, String[][][][] csParms, int parmType, int runSeconds) {
    if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
    SocketProxy socketProxy1 = SocketProxy.getSocketProxy(7011, systemName, JDASTest.AS_DATABASE); 
    int localPort1 = socketProxy1.getPortNumber(); 
    Thread proxyThread1 = new Thread(socketProxy1); 
    proxyThread1.start(); 
    socketProxy1.enable(true); 
    
    SocketProxy socketProxy2 = SocketProxy.getSocketProxy(7021, systemName, JDASTest.AS_DATABASE); 
    int localPort2 = socketProxy2.getPortNumber(); 
    Thread proxyThread2 = new Thread(socketProxy2); 
    proxyThread2.start(); 
    socketProxy2.enable(false); 

    SocketProxy socketProxy3 = SocketProxy.getSocketProxy(7031, systemName, JDASTest.AS_DATABASE); 
    int localPort3 = socketProxy3.getPortNumber(); 
    Thread proxyThread3 = new Thread(socketProxy3); 
    proxyThread3.start(); 
    socketProxy3.enable(false); 

    SocketProxy socketProxy4 = SocketProxy.getSocketProxy(7031, systemName, JDASTest.AS_DATABASE); 
    int localPort4 = socketProxy4.getPortNumber(); 
    Thread proxyThread4 = new Thread(socketProxy4); 
    proxyThread4.start(); 
    socketProxy4.enable(false); 

    SocketProxy[] proxies = new SocketProxy[4]; 
    
    proxies[0] = socketProxy1; 
    proxies[1] = socketProxy2; 
    proxies[2] = socketProxy3; 
    proxies[3] = socketProxy4;
    StringBuffer sb; 
    SwitchKillThread switchKillThread;
    Connection killerConnection;
    Connection connection; 
    String url; 
    try { 
      char[] passwordChars = PasswordVault.decryptPassword(encryptedPassword_);
      AS400JDBCDataSource killerDs = new AS400JDBCDataSource(system_, 
        systemObject_.getUserId(), passwordChars);
      killerConnection = killerDs.getConnection(); 

    sb = new StringBuffer(); 
    switchKillThread = new SwitchKillThread(killerConnection, 
        "", MINIMUM_TRANSACTION_MILLISECONDS, sb, proxies);
    
    
    url = "jdbc:as400:localhost:7;enableClientAffinitiesList=1;retryIntervalForClientReroute=10;"+
        "clientRerouteAlternateServerName=localhost,localhost,localhost,localhost;" +
          "clientRerouteAlternatePortNumber="+localPort1+","+localPort2+","+localPort3+","+localPort4;

    sb.append("Connecting to DS with " + url + " information\n");
    
    AS400JDBCDataSource ds = new AS400JDBCDataSource("localhost", 
        systemObject_.getUserId(), passwordChars);
    PasswordVault.clearPassword(passwordChars);
    ds.setPortNumber(7);
    ds.setEnableClientAffinitiesList(1);
    ds.setRetryIntervalForClientReroute(10);
    ds.setClientRerouteAlternateServerName("localhost,localhost,localhost,localhost");
    ds.setClientRerouteAlternatePortNumber(localPort1+","+localPort2+","+localPort3+","+localPort4 );
  
        
     connection = ds.getConnection(); 
     

    } catch (Exception e) { 
      failed(e); 
      return; 
    }
    testCSTypeParameters(csTransactions, csParms, parmType, connection, killerConnection, switchKillThread, runSeconds,  sb);
    // Cleanup  
    for (int i = 0; i < proxies.length; i++) {
      proxies[i].endService(); 
    }

  }
  }
	
	
	public void Var001() { notApplicable("See JDASClientReroute1"); } 
  public void Var002() { notApplicable("See JDASClientReroute1"); } 
  public void Var003() { notApplicable("See JDASClientReroute1"); } 
  public void Var004() { notApplicable("See JDASClientReroute1"); } 
  public void Var005() { notApplicable("See JDASClientReroute1"); } 
  public void Var006() { notApplicable("See JDASClientReroute1"); } 
  public void Var007() { notApplicable("See JDASClientReroute1"); } 
  public void Var008() { notApplicable("See JDASClientReroute1"); } 
  public void Var009() { notApplicable("See JDASClientReroute1"); } 
  public void Var010() { notApplicable("See JDASClientReroute1"); } 
  public void Var011() { notApplicable("See JDASClientReroute1"); } 
  public void Var012() { notApplicable("See JDASClientReroute1"); } 
  public void Var013() { notApplicable("See JDASClientReroute1"); } 
  public void Var014() { notApplicable("See JDASClientReroute1"); } 
	
  public void Var015() { notApplicable("See JDASClientReroute2"); } 
  public void Var016() { notApplicable("See JDASClientReroute2"); } 
  public void Var017() { notApplicable("See JDASClientReroute2"); } 
  public void Var018() { notApplicable("See JDASClientReroute2"); } 
  public void Var019() { notApplicable("See JDASClientReroute2"); } 
  public void Var020() { notApplicable("See JDASClientReroute2"); } 
  public void Var021() { notApplicable("See JDASClientReroute2"); } 


  public void Var022() { notApplicable("See JDASClientReroute3"); } 
  public void Var023() { notApplicable("See JDASClientReroute3"); } 
  public void Var024() { notApplicable("See JDASClientReroute3"); } 
  public void Var025() { notApplicable("See JDASClientReroute3"); } 
  public void Var026() { notApplicable("See JDASClientReroute3"); } 
  public void Var027() { notApplicable("See JDASClientReroute3"); } 
  public void Var028() { notApplicable("See JDASClientReroute3"); } 
  public void Var029() { notApplicable("See JDASClientReroute3"); } 
  public void Var030() { notApplicable("See JDASClientReroute3"); } 

  
  public void Var031() { notApplicable("See JDASClientReroute4"); } 
  public void Var032() { notApplicable("See JDASClientReroute4"); } 
  public void Var033() { notApplicable("See JDASClientReroute4"); } 
  public void Var034() { notApplicable("See JDASClientReroute4"); } 
  public void Var035() { notApplicable("See JDASClientReroute4"); } 
  public void Var036() { notApplicable("See JDASClientReroute4"); } 
  public void Var037() { notApplicable("See JDASClientReroute4"); } 

  public void Var038() { notApplicable("See JDASClientReroute5"); } 
  public void Var039() { notApplicable("See JDASClientReroute5"); } 
  public void Var040() { notApplicable("See JDASClientReroute5"); } 
  public void Var041() { notApplicable("See JDASClientReroute5"); } 
  public void Var042() { notApplicable("See JDASClientReroute5"); } 
  public void Var043() { notApplicable("See JDASClientReroute5"); } 

  public void Var044() { notApplicable("See JDASClientReroute6"); } 
  public void Var045() { notApplicable("See JDASClientReroute6"); } 
  public void Var046() { notApplicable("See JDASClientReroute6"); } 
  public void Var047() { notApplicable("See JDASClientReroute6"); } 
  public void Var048() { notApplicable("See JDASClientReroute6"); } 
  public void Var049() { notApplicable("See JDASClientReroute6"); } 

  public void Var050() { notApplicable("See JDASClientReroute7"); } 
  public void Var051() { notApplicable("See JDASClientReroute7"); } 
  public void Var052() { notApplicable("See JDASClientReroute7"); } 

}
