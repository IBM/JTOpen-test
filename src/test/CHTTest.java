 //////////////////////////////////////////////////////////////////////
 //
 ///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CHTTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.CommandCall;

import test.MiscAH.CHTEntryTestcase;
import test.MiscAH.CHTTestcase;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import java.util.StringTokenizer;
import java.util.Enumeration;

/**
Test driver for the ClusteredHashTable class.
NOTE TO TESTER:
In order to run these tests successfully, you must install the "PowerHA" product
on your test system.  (For V6R1: 5761-HAS.  For V7R1: 5770-HAS.)

**/
public class CHTTest
extends TestDriver
{

    // Constants.
    public static final String chtSvrName_ = "CHTJTTEST";
    public static AS400 pwrSys_ = null;

    private static final String clusterName = "CHTCLUSTER";

/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public static void main (String args[])
        throws Exception
    {
        runApplication (new CHTTest (args));
        System.exit(0);
    }



/**
Constructs an object for applets.
 @exception  Exception  If an exception occurs.
**/
    public CHTTest ()
        throws Exception
    {
        super();
    }


/**
Constructs an object for testing applications.

@param      args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public CHTTest (String[] args)
        throws Exception
    {
        super (args);
    }

/**
Does a STRCHTSVR command.
 @exception  Exception  If an exception occurs.
**/
    public static void strChtSvr()
      throws Exception
    {
	String STRCHTSVR_CMD = "STRCHTSVR SERVER(" + chtSvrName_ + ")";
	CommandCall cmd = new CommandCall(pwrSys_);
        // Create the clustered hash table
	if (cmd.run(STRCHTSVR_CMD) != true)
	{
            // Note that there was an error.
	    System.out.println("STRCHTSVR command failed!");
	}
        // Show the messages (returned whether or not there was an error.)
	AS400Message[] messagelist = cmd.getMessageList();
	for (int i = 0; i < messagelist.length; ++i)
	{
            // Show each message.
	    System.out.println(messagelist[i].getText());
	}
    }


/**
Does an ENDCHTSVR command.
 @exception  Exception  If an exception occurs.
**/
    public static void endChtSvr()
      throws Exception
    {
	String ENDCHTSVR_CMD = "ENDCHTSVR SERVER(" + chtSvrName_ + ")";
	CommandCall cmd = new CommandCall(pwrSys_);
        // Create the clustered hash table
	if (cmd.run(ENDCHTSVR_CMD) != true)
	{
            // Note that there was an error.
	    System.out.println("ENDCHTSVR command failed!");
	}
        // Show the messages (returned whether or not there was an error.)
	AS400Message[] messagelist = cmd.getMessageList();
	for (int i = 0; i < messagelist.length; ++i)
	{
            // Show each message.
	    System.out.println(messagelist[i].getText());
	}
    }


/**
Performs setup needed before running testcases.
 @exception  Exception  If an exception occurs.
**/
    public void setup ()
        throws Exception
    {
	String ipAddress = null;

	// initialize the IP address
	if (misc_ != null)
	{
	    StringTokenizer tokenizer = new StringTokenizer (misc_, ",");
	    ipAddress = tokenizer.nextToken ();
	    String uid = tokenizer.nextToken ();
	    String pwd = tokenizer.nextToken ();
	    pwrSys_ = new AS400 (systemObject_.getSystemName(), uid, pwd);
	}

     String sysName = getSystemName();
     if (sysName != null && sysName.equalsIgnoreCase("localhost")) {
       try {
         sysName = java.net.InetAddress.getLocalHost().getHostName();
         int dotIndex = sysName.indexOf('.');
         if (dotIndex != -1) {
           sysName = sysName.substring(0, dotIndex); // get unqualified name
         }
       } catch (Exception e) { System.out.println(e.getMessage()); }
     }
	String CRTCLU_CMD = "CRTCLU CLUSTER(" + clusterName + ") NODE((" + sysName + " ('" + ipAddress + "')))";
	String STRCHTSVR_CMD = "STRCHTSVR SERVER(" + chtSvrName_ + ")";

        // Create a cluster
	pwrSys_.setCcsid(37);
	CommandCall cmd = new CommandCall(pwrSys_);
	if (cmd.run(CRTCLU_CMD) != true)
	{
            // Note that there was an error.
	    System.out.println("CRTCLU command failed!");
	}
        // Show the messages (returned whether or not there was an error.)
	AS400Message[] messagelist = cmd.getMessageList();
	for (int i = 0; i < messagelist.length; ++i)
	{
            // Show each message.
	    System.out.println(messagelist[i].getText());
	}

        // Create the clustered hash table
	if (cmd.run(STRCHTSVR_CMD) != true)
	{
            // Note that there was an error.
	    System.out.println("STRCHTSVR command failed!");
	}
        // Show the messages (returned whether or not there was an error.)
	messagelist = cmd.getMessageList();
	for (int i = 0; i < messagelist.length; ++i)
	{
            // Show each message.
	    System.out.println(messagelist[i].getText());
	}
    }


/**
Performs cleanup needed after running testcases.
 @exception  Exception  If an exception occurs.
**/
    public void cleanup ()
        throws Exception
    {
	String ENDCHTSVR_CMD = "ENDCHTSVR SERVER(" + chtSvrName_ + ")";
	String DLTCLU_CMD = "DLTCLU CLUSTER(" + clusterName + ")";

        // End the clustered hash table
	CommandCall cmd = new CommandCall(pwrSys_);
	if (cmd.run(ENDCHTSVR_CMD) != true)
	{
            // Note that there was an error.
	    System.out.println("ENDCHTSVR command failed!");
	}
        // Show the messages (returned whether or not there was an error.)
	AS400Message[] messagelist = cmd.getMessageList();
	for (int i = 0; i < messagelist.length; ++i)
	{
            // Show each message.
	    System.out.println(messagelist[i].getText());
	}

        // Delete the cluster
	if (cmd.run(DLTCLU_CMD) != true)
	{
            // Note that there was an error.
	    System.out.println("DLTCLU command failed!");
	}
        // Show the messages (returned whether or not there was an error.)
	messagelist = cmd.getMessageList();
	for (int i = 0; i < messagelist.length; ++i)
	{
            // Show each message.
	    System.out.println(messagelist[i].getText());
	}
    }

/**
Creates the testcases.
**/
    public void createTestcases ()
    {
      addTestcase (new CHTTestcase (systemObject_,
				    namesAndVars_, runMode_,
				    fileOutputStream_));

      addTestcase (new CHTEntryTestcase (systemObject_,
				         namesAndVars_, runMode_,
				         fileOutputStream_));


      // Reports invalid testcase names.
      for (Enumeration e = namesAndVars_.keys (); e.hasMoreElements (); ) {
        String name = (String)e.nextElement ();
        System.out.println ("Testcase " + name + " not found.");
      }
    }

}




