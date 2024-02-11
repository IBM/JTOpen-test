///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASDSClientReroute1.java
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
import test.SocketProxy;

/**
  Testcase JDASClientReroute
  
 **/
public class JDASDSClientReroute1 extends JDASClientReroute
{

    
    
		/**
      Constructor.  This is called from the AS400JDBCEnableCALTest constructor.
     **/
    public JDASDSClientReroute1(AS400 systemObject,
        Hashtable namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password, String pwrSysUserID, String pwrSysPassword)
 {
		super(systemObject, "JDASDSClientReroute1", namesAndVars, runMode,
		    fileOutputStream, password, pwrSysUserID, pwrSysPassword);
		// originalPrintWriter_ = Trace.getPrintWriter();
	}


	/**
	 * Test to make sure that we can recover to the same system. 
	 **/
	public void Var001() {
		boolean passed = true;
		StringBuffer sb = new StringBuffer();
		sb.append("Test a simple connect with enableClientAffinitiesList\n");
		sb.append("Make sure the connection is re-established after it drops\n");
		if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
			try {
				SocketProxy socketProxy = SocketProxy.getSocketProxy(7001, systemName, JDASTest.AS_DATABASE); 
				int localPort = socketProxy.getPortNumber(); 
				Thread proxyThread = new Thread(socketProxy); 
				proxyThread.start(); 
				
				// String url = "jdbc:as400:localhost:"+localPort+";enableClientAffinitiesList=1;retryIntervalForClientReroute=10"; 

				sb.append("Connecting to datasouce\n");
				AS400JDBCDataSource ds = new AS400JDBCDataSource("localhost", systemObject_.getUserId(), encryptedPassword_); 
				ds.setPortNumber(localPort);
				ds.setEnableClientAffinitiesList(1);
				ds.setRetryIntervalForClientReroute(10);; 
				
				Connection connection = ds.getConnection();

				for (int i = 0; i < 20; i++) {
					sb.append("Running query 1\n");
					Statement stmt = connection.createStatement();
					ResultSet rs = stmt.executeQuery("select * from sysibm.sysdummy1");
					while (rs.next()) {
						rs.getString(1);
					}
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

					sb.append("Running query again ");
					try {
						rs = stmt.executeQuery("select * from sysibm.sysdummy1");
						while (rs.next()) {
							rs.getString(1);
						}
						passed = false;
						sb.append("Did not get excepion");
					} catch (SQLException sqlex) {
						int sqlCode = sqlex.getErrorCode();
						if (sqlCode != -4498) {
							passed = false;
							sb.append("Did not get error code of -4498\n");
							printStackTraceToStringBuffer(sqlex, sb);

						}
					}

					sb.append("Running query again -- should work ");
					rs = stmt.executeQuery("select * from sysibm.sysdummy1");
					while (rs.next()) {
						rs.getString(1);
					}
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
				SocketProxy socketProxy1 = SocketProxy.getSocketProxy(7001, systemName, JDASTest.AS_DATABASE); 
				int localPort1 = socketProxy1.getPortNumber(); 
				Thread proxyThread1 = new Thread(socketProxy1); 
				proxyThread1.start(); 
				socketProxy1.enable(false); 
				
				SocketProxy socketProxy2 = SocketProxy.getSocketProxy(7011, systemName, JDASTest.AS_DATABASE); 
				int localPort2 = socketProxy2.getPortNumber(); 
				Thread proxyThread2 = new Thread(socketProxy2); 
				proxyThread2.start(); 

				
        sb.append("Connecting to datasource -- enableClientAffinitiesList=1;retryIntervalForClientReroute=10;"
            + "clientRerouteAlternateServerName=localhost;clientRerouteAlternatePortNumber="+localPort2+"\n");
        AS400JDBCDataSource ds = new AS400JDBCDataSource("localhost", systemObject_.getUserId(), encryptedPassword_); 
        ds.setEnableClientAffinitiesList(1);
        ds.setPortNumber(localPort1);
        ds.setRetryIntervalForClientReroute(10);
        ds.setClientRerouteAlternateServerName("localhost");
        ds.setClientRerouteAlternatePortNumber(""+localPort2);
				
				Connection connection = ds.getConnection();
				
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
					try {
						rs = stmt.executeQuery("select * from sysibm.sysdummy1");
						while (rs.next()) {
							rs.getString(1);
						}
						passed = false;
						sb.append("Did not get excepion");
					} catch (SQLException sqlex) {
						int sqlCode = sqlex.getErrorCode();
						if (sqlCode != -4498) {
							passed = false;
							sb.append("Did not get error code of -4498\n");
							printStackTraceToStringBuffer(sqlex, sb);

						}
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
	 * system is not usable.   In this case, we use a primary port that does not exist. 
	 * Also specify the properties so that the default port is used. 
	 **/
	public void Var003() {
		boolean passed = true;
		StringBuffer sb = new StringBuffer();
		sb.append("Test a connect with enableClientAffinitiesList\n");
		sb.append("Make we connect to the second system and then connect to the first\n");
		if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
			try {
				SocketProxy socketProxy1 = SocketProxy.getSocketProxy(7001, systemName, JDASTest.AS_DATABASE); 
				int localPort1 = socketProxy1.getPortNumber(); 
				Thread proxyThread1 = new Thread(socketProxy1); 
				proxyThread1.start(); 
				socketProxy1.enable(false); 
				
				SocketProxy socketProxy2 = SocketProxy.getSocketProxy(7011, systemName, JDASTest.AS_DATABASE); 
				int localPort2 = socketProxy2.getPortNumber(); 
				Thread proxyThread2 = new Thread(socketProxy2); 
				proxyThread2.start(); 

				
				String urlX = "jdbc:as400:localhost:7;enableClientAffinitiesList=1;retryIntervalForClientReroute=10;"+
				    "clientRerouteAlternateServerName=localhost,localhost,"+systemName+";" +
						"clientRerouteAlternatePortNumber="+localPort1+","+localPort2;
				
				
        sb.append("Connecting to datasouce wih former URL "+urlX+"\n");
        AS400JDBCDataSource ds = new AS400JDBCDataSource("localhost", systemObject_.getUserId(), encryptedPassword_); 
        ds.setPortNumber(7);
        ds.setEnableClientAffinitiesList(1);
        ds.setRetryIntervalForClientReroute(10);; 
        ds.setClientRerouteAlternateServerName("localhost,localHost,"+systemName);
        ds.setClientRerouteAlternatePortNumber(localPort1+","+localPort2);
        Connection connection = ds.getConnection();

        
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
					try {
						rs = stmt.executeQuery("select * from sysibm.sysdummy1");
						while (rs.next()) {
							rs.getString(1);
						}
						passed = false;
						sb.append("Did not get excepion");
					} catch (SQLException sqlex) {
						int sqlCode = sqlex.getErrorCode();
						if (sqlCode != -4498) {
							passed = false;
							sb.append("Did not get error code of -4498\n");
							printStackTraceToStringBuffer(sqlex, sb);

						}
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
	 * system is not usable.   In this case, we use a primary server name that does not exist. 
	 * Also specify the properties so that the default port is used. 
	 **/
	public void Var004() {
		boolean passed = true;
		StringBuffer sb = new StringBuffer();
		sb.append("Test a connect with enableClientAffinitiesList\n");
		sb.append("Make we connect to the second system and then connect to the first\n");
		if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
			try {
				SocketProxy socketProxy1 = SocketProxy.getSocketProxy(7001, systemName, JDASTest.AS_DATABASE); 
				int localPort1 = socketProxy1.getPortNumber(); 
				Thread proxyThread1 = new Thread(socketProxy1); 
				proxyThread1.start(); 
				socketProxy1.enable(false); 
				
				SocketProxy socketProxy2 = SocketProxy.getSocketProxy(7011, systemName, JDASTest.AS_DATABASE); 
				int localPort2 = socketProxy2.getPortNumber(); 
				Thread proxyThread2 = new Thread(socketProxy2); 
				proxyThread2.start(); 

				
				String urlX = "jdbc:as400:serverdoesnotexist.org;enableClientAffinitiesList=1;retryIntervalForClientReroute=10;"+
				"clientRerouteAlternateServerName=localhost,localhost,"+systemName+";clientRerouteAlternatePortNumber="+localPort1+","+localPort2;
				sb.append("Connecting to datasouce wih former URL "+urlX+"\n");
        AS400JDBCDataSource ds = new AS400JDBCDataSource("serverdoesnotexist.org", systemObject_.getUserId(), encryptedPassword_); 
        
        ds.setEnableClientAffinitiesList(1);
        ds.setRetryIntervalForClientReroute(10);; 
        ds.setClientRerouteAlternateServerName("localhost,localHost,"+systemName);
        ds.setClientRerouteAlternatePortNumber(localPort1+","+localPort2);
        Connection connection = ds.getConnection();


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
					try {
						rs = stmt.executeQuery("select * from sysibm.sysdummy1");
						while (rs.next()) {
							rs.getString(1);
						}
						passed = false;
						sb.append("Did not get excepion");
					} catch (SQLException sqlex) {
						int sqlCode = sqlex.getErrorCode();
						if (sqlCode != -4498) {
							passed = false;
							sb.append("Did not get error code of -4498\n");
							printStackTraceToStringBuffer(sqlex, sb);

						}
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
	
	public void Var005() { notApplicable();} 
	public void Var006() { notApplicable();} 

	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with int parameters. 
	 **/
	public void Var007() {
		testDSPSTypeParametersSwitch(psIntTransactions, psIntParms, JAVA_INT,30); 
	}

	
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with small parameters. 
	 **/
	public void Var008() {
		testDSPSTypeParametersSwitch(psSmallintTransactions, psSmallintParms, JAVA_SHORT,30); 
	}


	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with bigint parameters. 
	 **/
	public void Var009() {
		testDSPSTypeParametersSwitch(psBigintTransactions, psBigintParms, JAVA_LONG,30); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with real parameters. 
	 **/
	public void Var010() {
		testDSPSTypeParametersSwitch(psRealTransactions, psRealParms, JAVA_FLOAT,30); 
	}

	
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with float parameters. 
	 **/
	public void Var011() {
		testDSPSTypeParametersSwitch(psFloatTransactions, psFloatParms, JAVA_DOUBLE,30); 
	}


	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Double parameters. 
	 **/
	public void Var012() {
		testDSPSTypeParametersSwitch(psDoubleTransactions, psDoubleParms, JAVA_DOUBLE,30); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Decimal parameters. 
	 **/
	public void Var013() {
		testDSPSTypeParametersSwitch(psDecimalTransactions, psDecimalParms, JAVA_BIGDECIMAL,30); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Numeric parameters. 
	 **/
	public void Var014() {
		testDSPSTypeParametersSwitch(psNumericTransactions, psNumericParms, JAVA_BIGDECIMAL,30); 
	}
	
	
	


	
	
}
