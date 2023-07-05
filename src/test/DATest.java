///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DATest.java
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
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Job;

/**
 Test driver for the DA (data area) component.  For security tests to be run in unattended mode, a AS400 userid/password with *SECADM authority must be passed on the -misc parameter (ie -misc uid,pwd).  If not specified, these tests will not be attempted in unattended mode.  If not specified and running attended, a sign-on will be displayed.  To exclude all testcase variations which drop connection, specify "NoDropper" as the third argument of the -misc parameter.  To run with native optimization, specify "Native" as the third argument of the -misc parameter.  See TestDriver for remaining calling syntax.
 **/
public class DATest extends TestDriver
{
    /**
     Main for running standalone application tests.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new DATest(args));
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
    public DATest() throws Exception
    {
        super();
    }

    /**
     This ctor used for applications.
     @param  args  The array of command line arguments.
     @exception  Exception  Incorrect arguments will cause an exception.
     **/
    public DATest(String[] args) throws Exception
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
       		    systemObject_.connectService(AS400.DATAQUEUE);
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
    			Job[] jobs = systemObject_.getJobs(AS400.DATAQUEUE);
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
            new DACtorTestcase(),
            new DACreateTestcase(),
            new DAReadTestcase(),
            new DAWriteTestcase(),
            new DAClearTestcase(),
            new DADeleteTestcase(),
            new DATypeTestcase(),
            new DAGetSetTestcase(),
            new DABeansTestcase(),
            new DASerializeTestcase(),
            new DAP9934437Testcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_);
            addTestcase(testcases[i]);
        }
    }

    /**
     Performs setup needed before running testcases.
     @exception  Exception  If an exception occurs.
     **/
    public void setup() throws Exception
    {
        CommandCall cmd = new CommandCall(systemObject_);

        try
        {
            deleteLibrary(cmd, "DATEST");
        }
        catch (Exception e)
        {
        }

        try
        {
            if (cmd.run("CRTLIB DATEST") == false)
            {
                System.out.println("Setup failed " + cmd.getMessageList()[0].getID() + " " + cmd.getMessageList()[0].getText());
            }
        }
        catch (Exception e)
        {
            System.out.println("Setup failed " + e);
            e.printStackTrace();
        }
    }

    /**
     Performs cleanup needed after running testcases.
     @exception  Exception  If an exception occurs.
     **/
    public void cleanup() throws Exception
    {
        CommandCall cmd = new CommandCall(systemObject_);
        try
        {
	    if (deleteLibrary(cmd, "DATEST") != null)
            {
                System.out.println("Cleanup failed " + cmd.getMessageList()[0].getID() + " " + cmd.getMessageList()[0].getText());
            }
        }
        catch (Exception e)
        {
            System.out.println("Cleanup failed " + e);
            e.printStackTrace();
        }
        systemObject_.disconnectAllServices();
        super.destroy();
    }
}
