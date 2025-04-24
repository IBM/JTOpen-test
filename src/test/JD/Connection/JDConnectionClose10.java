///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionClose10.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionClose10.java
//
// Classes:      JDConnectionClose10
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDJobName;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Hashtable; import java.util.Vector;




/**
Testcase JDConnectionClose10.  his tests the following methods
of the JDBC Connection class:

<ul>
<li>close()</li>
<li>isClosed()</li>
</ul>
**/
public class JDConnectionClose10
extends JDConnectionClose {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionClose10";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }






/**
Constructor.
**/
    public JDConnectionClose10 (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              String pwrSysUserID, 
                              String pwrSysPassword)
    {
        super (systemObject, "JDConnectionClose10",
               namesAndVars, runMode, fileOutputStream,
               password,
               pwrSysUserID,
               pwrSysPassword);
    }



    public void Var001 () { notApplicable(); }
    public void Var002 () { notApplicable(); }
    public void Var003 () { notApplicable(); }
    public void Var004 () { notApplicable(); }
    public void Var005 () { notApplicable(); }
    public void Var006 () { notApplicable(); }
    public void Var007 () { notApplicable(); }
    public void Var008 () { notApplicable(); }
    public void Var009 () { notApplicable(); }


/**
Testing closing of connection and use of statements.  See CPS 8D9VP3 and 84PLLH.
Prior to a fix, the garbage collector could be in the process of closing connections while
the main connection is close.  This can lead to all sorts of weird errors.
The solution is to update the closing of the connection to force the finalizers to run.
This testcase should detect this problem.
**/
    public void Var010 ()
    {
	boolean passed = true;

	int CONNECTIONS_TO_TEST = 300;
	int STATEMENTS_PER_CONNECTION = 300;
	if (isToolboxDriver()) {
	    CONNECTIONS_TO_TEST = 100;
	    STATEMENTS_PER_CONNECTION = 100;
	}

	STATEMENTS_PER_CONNECTION = overrideFromProperty("test.JDConnectionClose.STATEMENTS_PER_CONNECTION", STATEMENTS_PER_CONNECTION); 
  CONNECTIONS_TO_TEST = overrideFromProperty("test.JDConnectionClose.CONNECTIONS_TO_TEST", CONNECTIONS_TO_TEST); 

	StringBuffer sb = new StringBuffer();
	try {
	    long lastTime = System.currentTimeMillis() + 120000;
	    long nextMessage = System.currentTimeMillis() + 15000;
	    for (int i =  0; i < CONNECTIONS_TO_TEST  && System.currentTimeMillis() < lastTime; i++) {
	        if (System.currentTimeMillis() > nextMessage) {
	          long timeleft = ( lastTime - System.currentTimeMillis()) / 1000;
	          String message = "JDConnectionClose.Var010 loop="+i+"/"+CONNECTIONS_TO_TEST+
                  " or timeLeft = "+timeleft+"s";
	          JDJobName.sendProgramMessage(message );
	          System.out.println(message);
	          nextMessage = System.currentTimeMillis() + 15000;
	        }
		sb.append("Create connection loop "+i+"\n");
		// System.out.println("Create connection loop "+i+"\n");

		Connection conn = testDriver_.getConnection (baseURL_+";errors=full", userId_, encryptedPassword_);
		for (int j = 0; j < STATEMENTS_PER_CONNECTION; j++) {
		    PreparedStatement ps = conn.prepareStatement("Select * from sysibm.sysdummy1");
		    ps.executeQuery();
		}
		sb.append("Starting gc\n");
		System.gc();
		sb.append("Closing connection\n");
		conn.close();
	    }


	    // We pass if we complete without any exceptions.
	    assertCondition (passed);
	}
	catch (Throwable e) {
	    failed(e, "Unexpected Exception\n "+sb.toString());
	}

    }



}



