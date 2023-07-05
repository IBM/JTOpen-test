///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ProdLicTest.java
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
 The ProdLicTest class tests the ProdLicTest class.
 **/
public class ProdLicTest extends TestDriver
{
    /**
     Run the test as an application.
     @param  args  The command line arguments.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new ProdLicTest(args));
        }
        catch (Exception e)
        {
            System.out.println("Program terminated abnormally.");
            e.printStackTrace();
        }

            System.exit(0);
    }

    /**
     Constructs an object for applets.
     @exception  Exception  Initialization errors may cause an exception.
     **/
    public ProdLicTest() throws Exception
    {
        super();
    }

    /**
     Constructs an object for testing applications.
     @param  args  The command line arguments.
     @exception  Exception  Incorrect arguments will cause an exception
     **/
    public ProdLicTest(String[] args) throws Exception
    {
        super(args);
    }

    /**
     Creates the testcases.
     **/
    public void createTestcases()
    {
    	
    	if(TestDriverStatic.pause_)
    	{ 
    		  	try 
    		  	{						
    		  		systemObject_.connectService(AS400.CENTRAL);
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
    	     	    Job[] jobs = systemObject_.getJobs(AS400.CENTRAL);
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
  	   

        Testcase[] testcases =
        {
            new ProdLicTestcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_, pwrSysUserID_, pwrSysPassword_);
            addTestcase(testcases[i]);
        }
    }
}
