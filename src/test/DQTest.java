///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DQTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.IOException;
import java.util.StringTokenizer;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Job;

import test.DQ.DQAttributesTestcase;
import test.DQ.DQBeansTestcase;
import test.DQ.DQClearTestcase;
import test.DQ.DQConnectTestcase;
import test.DQ.DQCreateTestcase;
import test.DQ.DQCtorTestcase;
import test.DQ.DQDeleteTestcase;
import test.DQ.DQKeyedPeekTestcase;
import test.DQ.DQKeyedReadTestcase;
import test.DQ.DQPeekTestcase;
import test.DQ.DQReadTestcase;
import test.DQ.DQRefreshAttributesTestcase;
import test.DQ.DQWriteTestcase;

/**
 Test driver for the DQ (data queue) component.
 <p>For security tests to be run an AS400 userid/password with *SECADM authority must be passed on the -pwrSys parameter (ie -pwrSys uid,pwd).
 <p>To run to a server that does not support 64K data queue entries, specify "NoBigDQ" as the first argument of the -misc parameter.
 <p>See TestDriver for remaining calling syntax.
 @see TestDriver
 **/
public class DQTest extends TestDriver
{
    //static final String SERVERNAME_NATIVE = "QJVACMDSRV";
    public static final String SERVERNAME_NATIVE =  "QP0ZSPWP  "; // name of server job when running natively, and the test was invoked via a shell script called on the Qshell command line
    public static final String SERVERNAME_NATIVE1 = "QJVAEXEC"; 
    public static boolean allowBigDQ = true;

    /**
     Main for running standalone application tests.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new DQTest(args));
        }
        catch (Exception e)
        {
            System.out.println("Program terminated abnormally.");
            e.printStackTrace();
        }

        // Needed to make the virtual machine quit.
            System.exit(0);
    }

    /**
     This ctor used for applets.
     @exception  Exception  Initialization errors may cause an exception.
     **/
    public DQTest() throws Exception
    {
        super();
    }

    /**
     This ctor used for applications.
     @param  args  the array of command line arguments
     @exception  Exception  Incorrect arguments will cause an exception
     **/
    public DQTest(String[] args) throws Exception
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
            new DQConnectTestcase(),
            new DQAttributesTestcase(),
            new DQClearTestcase(),
            new DQCreateTestcase(),
            new DQCtorTestcase(),
            new DQDeleteTestcase(),
            new DQKeyedPeekTestcase(),
            new DQKeyedReadTestcase(),
            new DQPeekTestcase(),
            new DQReadTestcase(),
            new DQRefreshAttributesTestcase(),
            new DQWriteTestcase(),
            new DQBeansTestcase()
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
        if (misc_ != null)
        {
            StringTokenizer miscTokenizer = new StringTokenizer(misc_, ",");
            int numberOfTokens = miscTokenizer.countTokens();
            if (numberOfTokens != 1)
            {
                out_.println("Wrong number of -misc parameters: " + numberOfTokens);
            }
            for (int i = 0; i < numberOfTokens; ++i)
            {
                String tok = miscTokenizer.nextToken();
                if (tok.toUpperCase().startsWith("NOBIGDQ"))
                {
                    allowBigDQ = false;
                }
                else
                {
                    out_.println("Unrecognized -misc argument: " + tok);
                }
            }
        }

        if (!cmdRun("CRTLIB DQSECTEST"))
        {
            out_.println("Setup failed.");
        }
        if (!cmdRun("CRTLIB DQTEST"))
        {
            out_.println("Setup failed.");
	} else {
	    out_.println("Library DQTEST created");
	}
	if (!cmdRun("GRTOBJAUT OBJ(DQTEST) OBJTYPE(*LIB) USER("+userId_+") AUT(*ALL)")) {
	    out_.println("CRTOBJAUT failed");
	} 

    }

    /**
     Performs cleanup needed after running testcases.
     @exception  Exception  If an exception occurs.
     **/
    public void cleanup() throws Exception
    {
        // Shut down servers.
        systemObject_.disconnectService(AS400.DATAQUEUE);
        CommandCall c= new CommandCall(pwrSys_); 
	deleteLibrary(c,"DQSECTEST");
	deleteLibrary(c, "DQTEST"); 
        if (pwrSys_ != null) pwrSys_.disconnectService(AS400.COMMAND);
        super.cleanup();
    }
}
