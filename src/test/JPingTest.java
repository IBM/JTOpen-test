///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JPingTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
//
// File Name:  JPingTest.java
//
// Class Name:  JPingTest
//
////////////////////////////////////////////////////////////////////////

package test;

/**
 Test driver for the Form Input components.
 The testcases will be attempted in unattended mode.
 No input parms are required when calling this test driver.  They will be ignored if specified.
 See TestDriver for remaining calling syntax.
 @see TestDriver
 **/
public class JPingTest extends TestDriver
{
    /**
     Main for running standalone application tests.
     **/
    public static void main(String args[])
    {

        try
        {
            TestDriver.runApplication(new JPingTest(args));
        }
        catch (Throwable e)
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
    public JPingTest() throws Exception
    {
        super();
    }

    /**
     This ctor used for applications.
     @param  args  the array of command line arguments.
     @exception  Exception  Incorrect arguments will cause an exception.
     **/
    public JPingTest(String[] args) throws Exception
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
            new JPingTestcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_,  pwrSysUserID_, pwrSysPassword_);
            testcases[i].setProxy5(proxy5_); 
            addTestcase(testcases[i]);
        }
    }
}
