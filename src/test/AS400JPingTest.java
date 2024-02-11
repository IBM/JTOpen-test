///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JPingTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import test.MiscAH.AS400JPingTestcase;

/**
 Test driver for the Form Input components.
 The testcases will be attempted in unattended mode.
 No input parms are required when calling this test driver.  They will be ignored if specified.
 See TestDriver for remaining calling syntax.
 @see TestDriver
 **/
public class AS400JPingTest extends TestDriver
{
    /**
     Main for running standalone application tests.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new AS400JPingTest(args));
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
    public AS400JPingTest() throws Exception
    {
        super();
    }

    /**
     This ctor used for applications.
     @param  args  the array of command line arguments.
     @exception  Exception  Incorrect arguments will cause an exception.
     **/
    public AS400JPingTest(String[] args) throws Exception
    {
        super(args);
    }

    /**
     Creates Testcase objects for all the testcases in this component.
     **/
    public void createTestcases()
    {
        Testcase[] testcases =
        {
            new AS400JPingTestcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_,  pwrSysUserID_, pwrSysPassword_);
            addTestcase(testcases[i]);
        }
    }
}
