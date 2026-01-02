///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PgmTest.java
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
import com.ibm.as400.access.SystemProperties;

import test.Pgm.PgmBeans;
import test.Pgm.PgmConstructor;
import test.Pgm.PgmOnThreadTestcase;
import test.Pgm.PgmParmTestcase;
import test.Pgm.PgmRLETestcase;
import test.Pgm.PgmRunTestcase;

/**
 Test driver for the Example component.
 **/
public class PgmTest extends TestDriver
{
    static boolean assumeProgramsThreadSafe_ = false;

    /**
     Performs setup needed before running testcases.
     @exception  Exception  If an exception occurs.
     **/
    public void setup() throws Exception
    {
        // Determine if testcase is running on i5/OS.
        boolean onI5OS = JTOpenTestEnvironment.isOS400; 

        if (onI5OS != onAS400_)
        {
            out_.println("Detected operating system  does not match arguments passed.");
        }

        if (onI5OS)
        {
            String s = SystemProperties.getProperty(SystemProperties.PROGRAMCALL_THREADSAFE);
            assumeProgramsThreadSafe_ = (s != null && s.equals("true")) ? true : false;
        }
    }

    /**
     Main for running standalone application tests.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new PgmTest(args));
        }
        catch (Exception e)
        {
            System.out.println("Program terminated abnormally.");
            e.printStackTrace();
        }

            System.exit(0);
    }

    /**
     This ctor used for applets.
     @exception  Exception  Initialization errors may cause an exception.
     **/
    public PgmTest() throws Exception
    {
        super();
    }

    /**
     This ctor used for applications.
     @param args the array of command line arguments.
     @exception  Exception  Incorrect arguments will cause an exception.
     **/
    public PgmTest(String[] args) throws Exception
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
   		  		systemObject_.connectService(AS400.COMMAND);
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
   	     	    Job[] jobs = systemObject_.getJobs(AS400.COMMAND);
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
            new PgmConstructor(),
            new PgmParmTestcase(),
            new PgmRunTestcase(),
            new PgmOnThreadTestcase(),
            new PgmBeans(),
            new PgmRLETestcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_, pwrSysUserID_, pwrSysPassword_);
            testcases[i].setProxy5(proxy5_); 
            addTestcase(testcases[i]);
        }
    }
}
