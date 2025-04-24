///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASDefaultRetry.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////
//
//  File Name: JDASDefaultRetry.java
//  Classes:   JDASDefaultRetry
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
  Testcase JDASDefaultRetry
  
 **/
public class JDASDefaultRetry extends JDASTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDASDefaultRetry";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDASTest.main(newArgs); 
   }

    
    
		private String systemName;

		/**
      Constructor.  This is called from the AS400JDBCEnableCALTest constructor.
     **/
    public JDASDefaultRetry(AS400 systemObject,
        Hashtable<String,Vector<String>> namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password, String pwrSysUserID, String pwrSysPassword)
 {
		super(systemObject, "JDASDefaultRetry", namesAndVars, runMode,
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


	/**
	 * Test the defaultRetry.  This test will take 10 minutes.
	 **/
	public void Var001() {
		boolean passed = true;
		StringBuffer sb = new StringBuffer();
		sb.append("Test the default retry behavior\n"); 
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
				"clientRerouteAlternateServerName=localhost;clientRerouteAlternatePortNumber="+localPort2;
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				sb.append(ts.toString()+":Connecting to " + url + "\n");
				long startTime = System.currentTimeMillis();
				try { 
				    Connection connection = testDriver_.getConnection(url,systemObject_.getUserId(), encryptedPassword_);
				    passed = false;
				    sb.append("Able to get connection "+connection+" after "+(System.currentTimeMillis() - startTime) +" ms\n"); 
				} catch (Exception e) {
				    long stopTime = System.currentTimeMillis();
				    long testTime = stopTime - startTime;
				    int proxy1FailedConnectCount = socketProxy1.getFailedConnectCount(); 
            int proxy2FailedConnectCount = socketProxy2.getFailedConnectCount(); 
				    if ((testTime < 600000) || (proxy1FailedConnectCount < 7) || (proxy2FailedConnectCount < 6)) {
					     passed = false;
					     sb.append("Exception occurred after "+testTime+" ms. Should be < 600000\n"); 
               sb.append("Proxy1 failedConnectCount: "+ proxy1FailedConnectCount+"\n");
               sb.append("Proxy2 failedConnectCount: "+socketProxy2.getFailedConnectCount()+"\n");
				    } else {
				       System.out.println("Test took "+testTime+" ms");
				       System.out.println("Proxy1 failedConnectCount: "+socketProxy1.getFailedConnectCount());
               System.out.println("Proxy2 failedConnectCount: "+socketProxy2.getFailedConnectCount());
				      
				    }

				}	
					
				socketProxy1.endService(); 
				socketProxy2.endService();
				if (!passed) {
				    sb.append("socketProxy1 output ------------------------\n");
				    sb.append(socketProxy1.getOutput());
				    sb.append("\n"); 
				    sb.append("socketProxy2 output ------------------------\n");
				    sb.append(socketProxy2.getOutput());
				    sb.append("\n"); 

				}
				assertCondition(passed, sb);
			} catch (Exception e) {
				failed(e, "Unexpected Exception\n" + sb.toString());
			}
		} else {
			
		}
	}
	
	


	
	
}
