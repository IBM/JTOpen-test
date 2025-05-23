///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDSTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDSTest.java
//
// Classes:      JDDSTest
//
////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;

import test.JD.DS.JDDSGetConnection;
import test.JD.DS.JDDSGetReference;
import test.JD.DS.JDDSMisc;
import test.JD.DS.JDDSProperties;



/**
Test driver for the JDBC Driver class.
**/
public class JDDSTest
      extends JDTestDriver {



/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.
@exception  Exception  If an exception occurs.
**/
   public static void main (String args[])
   throws Exception
   {
      runApplication (new JDDSTest (args));
   }



/**
Constructs an object for applets.
@exception  Exception  If an exception occurs.
**/
   public JDDSTest ()
   throws Exception
   {
      super();
   }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.
@exception  Exception  If an exception occurs.
**/
   public JDDSTest (String[] args)
   throws Exception
   {
      super (args);
   }

/**
Performs setup needed before running testcases.

@exception Exception If an exception occurs.
**/
   public void setup ()
   throws Exception
   {
      super.setup();
   }


/**
Creates the testcases.
**/
   public void createTestcases2 ()
   {
	   
	   if(TestDriverStatic.pause_)
	   	 { 
		  	try 
		  	{						
		  		systemObject_.connectService(AS400.DATABASE);
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
	     	    Job[] jobs = systemObject_.getJobs(AS400.DATABASE);
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
	   
      addTestcase (new JDDSGetConnection (systemObject_,
                                          namesAndVars_, runMode_, fileOutputStream_, 
                                          password_));

      addTestcase (new JDDSGetReference (systemObject_,
                                         namesAndVars_, runMode_, fileOutputStream_, 
                                         password_));

      addTestcase (new JDDSProperties (systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_, 
                                       password_, pwrSysUserID_, pwrSysPassword_) );

      addTestcase (new JDDSMisc (systemObject_,
                                 namesAndVars_, runMode_, fileOutputStream_, 
                                 password_));
   }

}


