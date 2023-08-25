///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSLTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;

import java.io.IOException;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.Job;


/**
Test driver for the JDBC Statement Listener class.
**/
public class JDSLTest
extends JDTestDriver
{

/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.

@exception Exception If an exception occurs.
**/
    public static void main (String args[])
        throws Exception
    {
        runApplication (new JDSLTest (args));
    }



/**
Constructs an object for applets.

@exception Exception If an exception occurs.
**/
    public JDSLTest ()
        throws Exception
    {
        super();
    }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.

@exception Exception If an exception occurs.
**/
    public JDSLTest (String[] args)
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
        super.setup(); // @D1A
    }



/**
Performs setup needed after running testcases.

@exception Exception If an exception occurs.
**/
    public void cleanup ()
        throws Exception
    {
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
    	 
    	 
        addTestcase (new JDStatementListener (systemObject_,
            namesAndVars_, runMode_, fileOutputStream_, 
            password_));

    }


}


