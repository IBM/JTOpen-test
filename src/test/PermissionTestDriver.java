///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PermissionTestDriver.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import test.TestDriver;
import test.Permission.PermissionDLOUnattendedTestcase;
import test.Permission.PermissionQSYSUnattendedTestcase;
import test.Permission.PermissionRootUnattendedTestcase;
import test.Permission.PermissionUnattendedTestcase;

import com.ibm.as400.access.*;

/**
The PermissionTestDriver class provides test driver for the testcase files.
<p>The test driver runs the following testcase files:
<ul>
<li>PermissionDLOUnattendedTestcase
<li>PermissionQSYSUnattendedTestcase
<li>PermissionRootUnattendedTestcase
<li>PermissionUnattendedTestcase
</ul>

NOTE:
To run the ATTENDED testcases for the Permission component, use PermissionAttendedTestDriver.
**/
public class PermissionTestDriver extends  TestDriver
{
      public static AS400 PwrSys = null;
      private CommandCall cmd= null;
/**
Main for running standalone application tests.
**/
  public static void main(String args[])
  {
    try {
      PermissionTestDriver example = new
PermissionTestDriver(args);
      example.init();
      example.start();
      example.stop();
      example.destroy();
    }
    catch (Exception e)
    {
      System.out.println("Program terminated abnormally.");
      e.printStackTrace();
    }

    
  }



/**
This ctor used for applets.
@exception Exception Initialization errors may cause an exception.
**/
  public PermissionTestDriver()
       throws Exception
  {
    super();
  }



/**
This ctor used for applications.
@param args the array of command line arguments
@exception Exception Incorrect arguments will cause an exception
**/
  public PermissionTestDriver(String[] args)
       throws Exception
  {
    super(args);

  }



/**
Creates Testcase objects for all the testcases in this component.
**/
  public void createTestcases()
  {
	  
	   if(TestDriverStatic.pause_)
	   { 
  		  	try 
  		  	{						
  		  		systemObject_.connectService(AS400.SIGNON);
  			}
  	     	catch (AS400SecurityException e) 
  	     	{
  	     		// TODO Auto-generated catch block
  				e.printStackTrace();
  			} 
  	     	catch (IOException e) 
  	     	{
  				// TODO Auto-generated catch block
  	     	    e.printStackTrace();
  			}
  				 	 	   
  	     	try
  	     	{
  	     	    Job[] jobs = systemObject_.getJobs(AS400.SIGNON);
  	     	    System.out.println("Host Server job(s): ");

  	     	    	for(int i = 0 ; i< jobs.length; i++)
  	     	    	{   	    	
  	     	    		System.out.println(jobs[i]);
  	     	    	}    	    
  	     	 }
  	     	 catch(Exception exc){}
  	     	    
  	     	 try 
  	     	 {
  	     	    	System.out.println ("Toolbox is paused. Press ENTER to continue.");
  	     	    	System.in.read ();
  	     	 } 
  	     	 catch (Exception exc) {};   	   
	   } 
	   
     	if (!(pwrSys_.getUserId()).equals(""))
	    {     
     	         char[] pwrSysCharPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
     		 PwrSys = new AS400(systemObject_.getSystemName(), pwrSysUserID_, pwrSysCharPassword);  
     	         Arrays.fill(pwrSysCharPassword, ' ');
	    }
     	
     	else
     	{
     	  	System.out.println("Warning: -pwrSys option not specified. Some " +
	        "variations may fail.");
	  
     	}
	
	    //Changed misc_ to pwrSys_ so all test drivers are consistent.         
	   
	 /*
   // Setup the power user if provided.
        if (misc_ != null)
        {
          StringTokenizer miscTok = new StringTokenizer(misc_, ",");
          String uid = miscTok.nextToken();
          String pwd = miscTok.nextToken();

          PwrSys = new AS400(systemObject_.getSystemName(), uid, pwd);


        }
        else
        {
          System.out.println("-misc option not specified. Some variations may fail.");
        }
*/
	   
	   
    // Instantiate all testcases to be run.
    boolean allTestcases = (namesAndVars_.size() == 0);

    // $$$ TO DO $$$

    addTestcase(new PermissionDLOUnattendedTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_, getSystemName()));

    addTestcase(new PermissionQSYSUnattendedTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_));

    addTestcase(new PermissionRootUnattendedTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_));

    addTestcase(new PermissionUnattendedTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_, getSystemName()));    // @A2c


     // Put out error message for each invalid testcase name.
    for (Enumeration e = namesAndVars_.keys(); e.hasMoreElements();)
    {
      System.out.println("Testcase " + e.nextElement() + " not found.");
    }
  }
}


