///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  HLTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

/**
 Test driver for the HistoryLog object and associated classes.  Refer to TestDriver for calling syntax.
 **/
public class HLTest extends TestDriver
{
    /**
     Main for running standalone application tests.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new HLTest(args));
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
    public HLTest() throws Exception
    {
        super();
    }

    /**
     This ctor used for applications.
     @param  args  The array of command line arguments.
     @exception  Exception  Incorrect arguments will cause an exception.
     **/
    public HLTest(String[] args) throws Exception
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
            new HLTestcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_);
            addTestcase(testcases[i]);
        }
    }
}
