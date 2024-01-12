///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTTest.java
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

import test.DT.DTArrayTestcase;
import test.DT.DTBin1Testcase;
import test.DT.DTBin2Testcase;
import test.DT.DTBin4Testcase;
import test.DT.DTBin8Testcase;
import test.DT.DTByteArrayTestcase;
import test.DT.DTDateTimeTestcase;
import test.DT.DTFloat4Testcase;
import test.DT.DTFloat8Testcase;
import test.DT.DTInterfaceTestcase;
import test.DT.DTPackedDoubleTestcase;
import test.DT.DTPackedTestcase;
import test.DT.DTStructureTestcase;
import test.DT.DTTextTestcase;
import test.DT.DTUnsignedBin1Testcase;
import test.DT.DTUnsignedBin2Testcase;
import test.DT.DTUnsignedBin4Testcase;
import test.DT.DTUnsignedBin8Testcase;
import test.DT.DTZonedDoubleTestcase;
import test.DT.DTZonedTestcase;

/**
 Test driver for the Data Types component.
 @see TestDriver
 **/
public class DTTest extends TestDriver
{
    /**
     Main for running standalone application tests.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new DTTest(args));
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
    public DTTest() throws Exception
    {
        super();
    }

    /**
     This ctor used for applications.
     @param  args  the array of command line arguments
     @exception  Exception  Incorrect arguments will cause an exception
     **/
    public DTTest(String[] args) throws Exception
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
            new DTArrayTestcase(),
            new DTBin1Testcase(),
            new DTBin2Testcase(),
            new DTBin4Testcase(),
            new DTBin8Testcase(),
            new DTByteArrayTestcase(),
            new DTFloat4Testcase(),
            new DTFloat8Testcase(),
            new DTPackedTestcase(),
            new DTPackedDoubleTestcase(),
            new DTStructureTestcase(),
            new DTTextTestcase(),
            new DTUnsignedBin1Testcase(),
            new DTUnsignedBin2Testcase(),
            new DTUnsignedBin4Testcase(),
            new DTUnsignedBin8Testcase(),
            new DTZonedTestcase(),
            new DTZonedDoubleTestcase(),
            new DTInterfaceTestcase(),
            new DTDateTimeTestcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_);
            addTestcase(testcases[i]);
        }
    }
}
