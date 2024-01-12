///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpaceTest.java
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

import test.UserSpace.UserSpaceBeans;
import test.UserSpace.UserSpaceChgAttrTestcase;
import test.UserSpace.UserSpaceCrtDltTestcase;
import test.UserSpace.UserSpacePgmCallTestcase;
import test.UserSpace.UserSpaceReadTestcase;
import test.UserSpace.UserSpaceWriteTestcase;

/**
 Test driver for the User Space component.
 <p>If needed objects created during test can be deleted manually by using the following commands:
 <ul>
 <li>DeLTLIB USTEST
 <li>DeLTLIB USAUTHLIB
 <li>DeLTLIB USTESTAUTH
 <li>DLTAUTL USAUTHLIST
 <li>DLTUSRPRF USTEST
 </ul>
 **/
public class UserSpaceTest extends TestDriver
{

    public static String COLLECTION               = "USTEST"; 


    /**
     Main for running standalone application tests.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new UserSpaceTest(args));
        }
        catch (Exception e)
        {
            System.out.println("Program terminated abnormally.");
            e.printStackTrace();
        }
    }

    /**
     This ctor used for applets.
     @exception  Exception  Initialization errors may cause an exception.
     **/
    public UserSpaceTest() throws Exception
    {
        super();
    }

    /**
     This ctor used for applications.
     @param  args  The array of command line arguments.
     @exception  Exception  Incorrect arguments will cause an exception
     **/
    public UserSpaceTest(String[] args) throws Exception
    {
        super(args);
    }

    /**
     Performs cleanup needed after running testcases.
     @exception  Exception  If an exception occurs.
     **/
    public void cleanup() throws Exception
    {
        deleteObjects();
        pwrSys_.disconnectService(AS400.COMMAND);
    }

    /**
     Performs setup needed before running testcases.
     @exception  Exception  If an exception occurs.
     **/
    public void setup() throws Exception
    {


	if (testLib_ != null) { // @E1A
	    COLLECTION = testLib_;
	}

        deleteObjects();
        cmdRun("RUNSQL SQL('CREATE COLLECTION "+COLLECTION+"') ");
    }

    // Cleanup some of the objects created during the test.
    // @exception  Exception  If an exception occurs.
    private void deleteObjects() throws Exception
    {
        // The collection is not the COLLECTION used by all tests.  Do not delete it. 
	      // deleteLibrary(""+COLLECTION+"");
        // cmdRun("DLTUSRPRF USRPRF("+COLLECTION+") OWNOBJOPT(*DLT)", "CPF2204");
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
    	
    	
        Testcase[] testcases =
        {
            new UserSpaceCrtDltTestcase(),
            new UserSpaceReadTestcase(),
            new UserSpaceWriteTestcase(),
            new UserSpaceChgAttrTestcase(),
            new UserSpaceBeans(),
            new UserSpacePgmCallTestcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_, pwrSysUserID_, pwrSysPassword_);
            addTestcase(testcases[i]);
        }
    }
}
