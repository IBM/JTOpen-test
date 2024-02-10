///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionClose11.java
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
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDJobName;

import java.io.FileOutputStream;
import java.util.Hashtable;




/**
Testcase JDConnectionClose11.  his tests the following methods
of the JDBC Connection class:

<ul>
<li>close()</li>
<li>isClosed()</li>
</ul>
**/
public class JDConnectionClose11
extends JDConnectionClose {






/**
Constructor.
**/
    public JDConnectionClose11 (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              String pwrSysUserID, 
                              String pwrSysPassword)
    {
        super (systemObject, "JDConnectionClose11",
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

In this case we don't explicitly close the connection, but allow the gc to close it.
**/
    public void Var011 ()
    {
	boolean passed = true;
	// int CONNECTIONS_PER_GC = 10;
	int CONNECTIONS_TO_TEST = 300;
	int STATEMENTS_PER_CONNECTION = 300;
	if (isToolboxDriver()) {
	    CONNECTIONS_TO_TEST = 100;
	    STATEMENTS_PER_CONNECTION = 100;
	}

	STATEMENTS_PER_CONNECTION = overrideFromProperty("test.JDConnectionClose.STATEMENTS_PER_CONNECTION", STATEMENTS_PER_CONNECTION); 
  CONNECTIONS_TO_TEST = overrideFromProperty("test.JDConnectionClose.CONNECTIONS_TO_TEST", CONNECTIONS_TO_TEST); 

	StringBuffer sb = new StringBuffer();
	// Run for a maximum of 5 minutes = 300 seconds
	long lastTime = System.currentTimeMillis() + 300000;
        long nextMessage = System.currentTimeMillis() + 15000;
	try {

	    for (int i =  0;
		 i < CONNECTIONS_TO_TEST && System.currentTimeMillis() < lastTime;
		 i++) {

	          if (System.currentTimeMillis() > nextMessage) {
	            long timeleft = ( lastTime - System.currentTimeMillis()) / 1000;
	            String message = "JDConnectionClose.Var011 loop="+i+"/"+CONNECTIONS_TO_TEST+
	            " or timeLeft = "+timeleft+"s";
	            JDJobName.sendProgramMessage(message );
	            System.out.println(message);
	            nextMessage = System.currentTimeMillis() + 15000;
	          }


		leakConnection(sb, i, STATEMENTS_PER_CONNECTION);
	            // if (i % CONNECTIONS_PER_GC == 0) {
	            //    sb.append("Starting gc\n");
	            //    System.gc();
	            // }
	    }


	    // We pass if we complete without any exceptions.
	    assertCondition (passed);
	}
	catch (Throwable e) {
	    failed(e, "Unexpected Exception\n "+sb.toString());
	}

    }
 
}



