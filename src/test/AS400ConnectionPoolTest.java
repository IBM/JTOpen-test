///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400ConnectionPoolTest.java
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

import test.MiscAH.AS400ConnectionPoolBeanInfoTestcase;
import test.MiscAH.AS400ConnectionPoolSerializationTestcase;
import test.MiscAH.AS400ConnectionPoolTestcase;

/**
The AS400ConnectionPoolTest class tests the AS400ConnectionPool class.
**/
public class AS400ConnectionPoolTest
extends TestDriver
{

  // Determine if testcase is running on AS/400.
  static boolean onAS400_1 = JTOpenTestEnvironment.isOS400; 
  



/**
Run the test as an application.

@param  args        The command line arguments.
@exception Exception Incorrect arguments will cause an exception
**/
    public static void main (String args[])
        throws Exception
    {

	// Make sure that the GUI is off, otherwise
	// testcases will null passwords will cause a prompt.
	System.setProperty("com.ibm.as400.access.AS400.guiAvailable", "false");

        runApplication (new AS400ConnectionPoolTest (args));
        System.exit(0);
    }



/**
Constructs an object for applets.
@exception Exception Initialization errors may cause an exception
**/
    public AS400ConnectionPoolTest ()
        throws Exception
    {
        super();
    }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.
@exception Exception Incorrect arguments will cause an exception
**/
    public AS400ConnectionPoolTest (String[] args)
        throws Exception
    {
        super (args);
    }



/**
Creates the testcases.
**/
    public void createTestcases ()
    {

	// Make sure that the GUI is off, otherwise
	// testcases will null passwords will cause a prompt.
	System.setProperty("com.ibm.as400.access.AS400.guiAvailable", "false");

    	if(TestDriverStatic.pause_)
    	{ 
       	   try 
       	    {						
       		    systemObject_.connectService(AS400.SIGNON);
			}
       	    catch (AS400SecurityException e) 
       	    {
				e.printStackTrace();
			} 
       	    catch (IOException e) 
       	    {
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
    		catch(Exception exc){ exc.printStackTrace(); }
       	    try 
       	    {
       	    	System.out.println ("Toolbox is paused. Press ENTER to continue.");
       	    	System.in.read ();
       	    } 
       	    catch (Exception exc) {};   	   
    	} 
       	
        Testcase[] testcases =
        {
            new AS400ConnectionPoolTestcase(),
            new AS400ConnectionPoolBeanInfoTestcase(),
            new AS400ConnectionPoolSerializationTestcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_1, namesAndVars_, runMode_, fileOutputStream_,  pwrSysUserID_, pwrSysPassword_);
            testcases[i].setProxy5(proxy5_); 
            addTestcase(testcases[i]);
        }
    }
}

