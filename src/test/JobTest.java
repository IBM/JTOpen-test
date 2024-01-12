///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JobTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//
//
//
///////////////////////////////////////////////////////////////////////////////
//
// File Name:  JobTest.java
//
// Class Name:  JobTest
//
///////////////////////////////////////////////////////////////////////////////
//
//
//
//
///////////////////////////////////////////////////////////////////////////////

package test;

import test.Job.DateTimeConverterTestcase;
import test.Job.JobListBeans;
import test.Job.JobListTestcase;
import test.Job.JobLogBeans;
import test.Job.JobLogTestcase;
import test.Job.JobUnattendedTestcase;

/**
 The JobInfoTestDriver class provides a test driver to execute testcases.  Please create a user profile whose name is "TSTJOBUSR1" and signon to the iSeries server with the user name before executing the test driver.
 <p>This test driver tests the following testcase:
 <ul>
 <li>DateTimeConverterTestcase
 <li>JobUnattendedTestcase
 <li>JobListBeans
 <li>JobListTestcase
 <li>JobLogBeans
 <li>JobLogTestcase
 </ul>
 **/
public class JobTest extends TestDriver
{
    /**
     Main for running standalone application tests.
     @param  args  The command line arguments.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new JobTest(args));
        }
        catch (Exception e)
        {
            System.out.println("Program terminated abnormally.");
            e.printStackTrace();
        }

            System.exit(0);
    }

    /**
     This constructor is used for applets.
     @exception  Exception  Initialization errors may cause an exception.
     **/
    public JobTest() throws Exception
    {
        super();
    }

    /**
     This constructor is used for applications.
     @param  args  The command line arguments.
     @exception  Exception  Incorrect arguments will cause an exception.
     **/
    public JobTest(String[] args) throws Exception
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
            new DateTimeConverterTestcase(),
            new JobUnattendedTestcase(),
            // JobList testcases.
            new JobListBeans(),
            new JobListTestcase(),
            // JobLog testcases.
            new JobLogBeans(),
            new JobLogTestcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_,   pwrSysUserID_, pwrSysPassword_);
            addTestcase(testcases[i]);
        }
    }
}
