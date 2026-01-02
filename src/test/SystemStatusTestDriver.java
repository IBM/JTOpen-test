///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SystemStatusTestDriver.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import test.SystemStatus.SystemPoolBeanInfoUnattendedTestcase;
import test.SystemStatus.SystemPoolUnattendedTestcase;
import test.SystemStatus.SystemStatusBeanInfoUnattendedTestcase;
import test.SystemStatus.SystemStatusUnattendedTestcase;

/**
 The SystemStatusTestDriver class provides the test driver for the testcase files.
 <p>The test driver contains ten testcase files :
 <ul>
 <li>SystemStatusDialogAttendedTestcase
 <li>VSystemStatusUnattendedTestcase
 <li>VSystemStatusPaneUnattendedTestcase
 <li>VSystemPoolUnattendedTestcase
 <li>VSystemStatusBeanInfoUnattendedTestcase
 <li>VSystemPoolBeanInfoUnattendedTestcase
 <li>SystemStatusUnattendedTestcase
 <li>SystemPoolUnattendedTestcase
 <li>SystemStatusBeanInfoUnattendedTestcase
 <li>SystemPoolBeanInfoUnattendedTestcase
 </ul>
 **/
public class SystemStatusTestDriver extends TestDriver
{
    /**
     Main for running standalone application tests.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new SystemStatusTestDriver(args));
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
    public SystemStatusTestDriver() throws Exception
    {
        super();
    }

    /**
     This ctor used for applications.
     @param  args  the array of command line arguments.
     @exception  Exception  Incorrect arguments will cause an exception
     **/
    public SystemStatusTestDriver(String[] args) throws Exception
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
            new SystemPoolUnattendedTestcase(),
            new SystemPoolBeanInfoUnattendedTestcase(),
            new SystemStatusUnattendedTestcase(),
            new SystemStatusBeanInfoUnattendedTestcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_, pwrSysUserID_, pwrSysPassword_);
            testcases[i].setProxy5(proxy5_); 
            addTestcase(testcases[i]);
        }
    }
}
