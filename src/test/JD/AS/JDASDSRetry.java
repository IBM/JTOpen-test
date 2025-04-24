///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASDSRetry.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////
//
//  File Name: JDASRetry.java
//  Classes:   JDASRetry
//
////////////////////////////////////////////////////////////////////////////
// 
////////////////////////////////////////////////////////////////////////////
package test.JD.AS;


import java.io.FileOutputStream;
import java.util.Hashtable; import java.util.Vector;
import java.sql.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;

import test.JDASTest;
import test.SocketProxy;

/**
  Testcase JDASRetry
  
 **/
public class JDASDSRetry extends JDASTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDASDSRetry";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDASTest.main(newArgs); 
   }

    public static final int MAXIMUM_CONNECTION_OVERHEAD = 100; 
    public static final int MINIMUM_CONNECTION_OVERHEAD = 0; 
    
		private String systemName;

		/**
      Constructor.  This is called from the AS400JDBCEnableCALTest constructor.
     **/
    public JDASDSRetry(AS400 systemObject,
        Hashtable<String,Vector<String>> namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password, String pwrSysUserID, String pwrSysPassword)
 {
		super(systemObject, "JDASDSRetry", namesAndVars, runMode,
		    fileOutputStream, password, pwrSysUserID, pwrSysPassword);
		// originalPrintWriter_ = Trace.getPrintWriter();
	}


  	public void setup() {
  	}


  		/**
       @exception  Exception  If an exception occurs.
       **/
      public void cleanup() throws Exception
      {  
      }

      public void testRetry(int maxRetries, int retryInterval) {
        StringBuffer sb = new StringBuffer();
        String testString = "Test the retry behavior maxRetries = "+maxRetries+" retryInterval="+retryInterval; 
        sb.append(testString+"\n"); 
        
        String maxRetriesProperty=""; 
        String retryIntervalProperty=""; 
        if (maxRetries >= 0) {
          maxRetriesProperty = ";maxRetriesForClientReroute="+maxRetries; 
        }
        if (retryInterval >= 0) {
          retryIntervalProperty = ";retryIntervalForClientReroute="+retryInterval; 
        }
        
        // Determine the default settings for test
        if (maxRetries < 0 && retryInterval >= 0) {
          maxRetries = 3; 
        } else if (retryInterval < 0 && maxRetries > 0) {
          retryInterval = 0; 
        } else if (maxRetries < 0 && retryInterval < 0 ) {
          failed("invalid values for maxRetries and retryInterval"); 
          return; 
        }
        
        long expectedMinimumTestMillis = maxRetries * (1000 * retryInterval + MINIMUM_CONNECTION_OVERHEAD); 
        long expectedMaximumTestMillis = MAXIMUM_CONNECTION_OVERHEAD + maxRetries * (1000 * retryInterval + MAXIMUM_CONNECTION_OVERHEAD); 
        
        boolean passed = true;
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
            socketProxy2.enable(false); 

            
            String url = "jdbc:as400:localhost:"+localPort1+";enableClientAffinitiesList=1;"+
            "clientRerouteAlternateServerName=localhost;clientRerouteAlternatePortNumber="+localPort2+
            maxRetriesProperty+retryIntervalProperty;
            
            
            sb.append("Connecting to " + url + " via ds \n");
            long startTime = System.currentTimeMillis();
            try { 
              AS400JDBCDataSource ds = new AS400JDBCDataSource("localhost",systemObject_.getUserId(), encryptedPassword_);
              ds.setPortNumber(localPort1);
              ds.setEnableClientAffinitiesList(1);
              ds.setClientRerouteAlternateServerName("localhost");
              ds.setClientRerouteAlternatePortNumber(""+localPort2);
              ds.setMaxRetriesForClientReroute(maxRetries);
              ds.setRetryIntervalForClientReroute(retryInterval);
              
              Connection connection = ds.getConnection(); 
                passed = false;
                sb.append("Able to get connection "+connection+" after "+(System.currentTimeMillis() - startTime) +" ms\n"); 
            } catch (Exception e) {
                long stopTime = System.currentTimeMillis();
                long testTime = stopTime - startTime;
                int proxy1FailedConnectCount = socketProxy1.getFailedConnectCount(); 
                int proxy2FailedConnectCount = socketProxy2.getFailedConnectCount();
                if ((testTime < expectedMinimumTestMillis) || 
                    (testTime > expectedMaximumTestMillis) || 
                    (proxy1FailedConnectCount != maxRetries + 1 ) || 
                    (proxy2FailedConnectCount != maxRetries )) {
                   passed = false;
                   sb.append("Exception occurred after "+testTime+" ms. Should be between "+
                       expectedMinimumTestMillis +" and "+expectedMaximumTestMillis+"\n"); 
                   sb.append("Proxy1 failedConnectCount: "+ proxy1FailedConnectCount+" sb "+(maxRetries+1)+"\n");
                   sb.append("Proxy2 failedConnectCount: "+socketProxy2.getFailedConnectCount()+" sb "+maxRetries+"\n");
                } else {
                   System.out.println(testString); 
                   System.out.println("Test took "+testTime+" ms between "+
                       expectedMinimumTestMillis +" and "+expectedMaximumTestMillis);
                   System.out.println("Proxy1 failedConnectCount: "+socketProxy1.getFailedConnectCount());
                   System.out.println("Proxy2 failedConnectCount: "+socketProxy2.getFailedConnectCount());
                  
                }

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
	 * Test 10 maxRetries for client reroute (with no setting for interval (default 0))
	 **/
	public void Var001() {
	  testRetry(10, -1); 
	}
	
	/* Test 1 second retry interval with default (3) retries */ 
  public void Var002() {
    testRetry(-1, 1); 
  }
	

  /* Test 0 retries, 1 second retry interval */ 
  public void Var003() {
    testRetry(0, 1); 
  }
  
  /* Test 100 retries, 0 second retry interval */ 
  public void Var004() {
    testRetry(100, 0); 
  }

  /* Test 1 retries, 5 second retry interval */ 
  public void Var005() {
    testRetry(1, 5); 
  }

	
	
}
