///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SSLTest.java
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

import test.SSL.SSLChangePasswordTestcase;
import test.SSL.SSLChgPwdDialogTestcase;
import test.SSL.SSLConnectTestcase;
import test.SSL.SSLCtorTestcase;
import test.SSL.SSLEventTestcase;
import test.SSL.SSLPortMapperTestcase;
import test.SSL.SSLPropertyTestcase;
import test.SSL.SSLSerializeTestcase;
import test.SSL.SSLSignonInfoTestcase;
import test.SSL.SSLSignonTestcase;
import test.SSL.SSLValidatePasswordTestcase;
import test.SSL.SSLVrmTestcase;

/**
 Test driver for the SecureAS400 and associated classes.  Refer to TestDriver for calling syntax.
 **/
public class SSLTest extends TestDriver
{
    /**
     Main for running standalone application tests.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new SSLTest(args));
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
    public SSLTest() throws Exception
    {
        super();
    }

    /**
     This ctor used for applications.
     @param  args  The array of command line arguments.
     @exception  Exception  Incorrect arguments will cause an exception.
     **/
    public SSLTest(String[] args) throws Exception
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
    	
        // SSLPortMapperTestcase needs to be first to insure proper initial value testing.
        Testcase[] testcases =
        {
            new SSLChangePasswordTestcase(),
            new SSLChgPwdDialogTestcase(),
            new SSLConnectTestcase(),
            new SSLCtorTestcase(),
            new SSLEventTestcase(),
            new SSLPortMapperTestcase(),
            new SSLPropertyTestcase(),
            new SSLSerializeTestcase(),
            new SSLSignonTestcase(),
            new SSLSignonInfoTestcase(),
            new SSLValidatePasswordTestcase(),
            new SSLVrmTestcase(),
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_);
            addTestcase(testcases[i]);
        }
    }
}
