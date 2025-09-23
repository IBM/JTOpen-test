///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionStress.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionStress.java
//
// Classes:      JDConnectionStress
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Hashtable; import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDHandleStress;
import test.JDTestcase;



/**
Testcase JDConnectionStress.  This tests handle stuff for the
native JDBC driver.
**/
public class JDConnectionStress
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionStress";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }

    public static int RUN_SECONDS=60; 

/**
Constructor.
**/
    public JDConnectionStress (AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password,
                             String pwrUID,     //@H2A
                             String pwrPwd) {   //@H2A
        super (systemObject, "JDConnectionStress",
               namesAndVars, runMode, fileOutputStream,
               password);

    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
    }



    public void runTest(int threads,
			int connectionsPerThread,
			int statementsPerConnection,
			int percentStatementOps,
			int percentAllocOps,
			int runSeconds,
			int growTime,
			int shrinkTime) {
	if (checkNative()) {
	    long cliFixLevel =getCliFixLevel();
	    System.out.println("cliFixLevel is "+cliFixLevel);


	    PrintStream writer = null;
	    File outputFile = null;
 	    try {
		String filename = "/tmp/JDConnectionStress."+System.currentTimeMillis(); 
		System.out.println("Running with output to "+filename); 
		 outputFile = new File (filename);

		writer = new PrintStream(new FileOutputStream(outputFile));

		JDHandleStress.run(writer,
				   threads,
				   connectionsPerThread,
				   statementsPerConnection,
				   percentStatementOps,
				   percentAllocOps,
				   runSeconds,
				   growTime,
				   shrinkTime);
		writer.flush();
		writer.close();
		outputFile.delete();
		System.gc();
		assertCondition(true);
	    } catch (Exception e) {
		if (writer != null) {
		    writer.flush();
		    writer.close();
		}
		failed(e,"Test failed, please examine "+outputFile);
		System.gc();
	    }
	}

    }

/**
Run a test for 1 minute that does not free handles.
The testcase should prevent this from exhausing the
handle limit.

Starts with about 155,000 handles
10 * 10 * 310 * 5 =
*/
    public void Var001() {
	runTest(
		10, /* threads */
		10, /* connectionsPerThread */
		310, /* statementsPerConnection */
		100, /* percentStatementOps */
		90,  /* percentAllocOps */
		RUN_SECONDS, /* runSeconds */
		300, /* growTime */
		1    /* shrinkTime */
		);
    }



    /* This is the test used during development  */
    /* This does not free / allocated connections */
    /* 2 minutes */ 
    public void Var002() {
	runTest(
		20, /* threads */
		10, /* connectionsPerThread */
		150, /* statementsPerConnection */
		100, /* percentStatementOps */
		90,  /* percentAllocOps */
		RUN_SECONDS, /* runSeconds */
		45, /* growTime */
		5    /* shrinkTime */
		);
    }


    /* Add some connection allocation and free into the fix */
    /* 2 minutes */ 
    public void Var003() {
	runTest(
		20, /* threads */
		10, /* connectionsPerThread */
		150, /* statementsPerConnection */
		95, /* percentStatementOps */
		80,  /* percentAllocOps */
		RUN_SECONDS, /* runSeconds */
		45, /* growTime */
		10    /* shrinkTime */
		);
    }

    /* Testing only connection allocation and free */
    /* This detects a race condition found in CPS 8USQRX */
    /* 2 minutes */ 
    

    public void Var004() {
	runTest(
		10, /* threads */
		10, /* connectionsPerThread */
		10, /* statementsPerConnection */
		0, /* percentStatementOps */
		99,  /* percentAllocOps */
		RUN_SECONDS, /* runSeconds */
		1, /* growTime */
		2    /* shrinkTime */
		);
    }


    /* 1 minute */ 
    public void Var005() {
	runTest(
		10, /* threads */
		10, /* connectionsPerThread */
		10, /* statementsPerConnection */
		0, /* percentStatementOps */
		99,  /* percentAllocOps */
		RUN_SECONDS, /* runSeconds */
		1, /* growTime */
		2    /* shrinkTime */
		);
    }


}



