///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SysvalTestDriver.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

/**
 The SysvalTestDriver class provides a test driver to execute testcases.
 <p>This test driver tests the following testcases:
 <ul>
 <li>SysvalCtorTestcase
 <li>SysvalGetSetTestcase
 <li>SysvalBeanInfoTestcase
 <li>SysvalSerialization
 <li>SysvalExtendedTestcase
 <li>SysvalReleaseTestcase
 <li>SysvalListCtorTestcase
 <li>SysvalListGetSetTestcase
 <li>SysvalListBeanInfoTestcase
 <li>SysvalListSerialization
 <li>SysvalP9907639
 <li>SysvalP9928495
 </ul>
 **/
public class SysvalTestDriver extends TestDriver
{
    /**
     Main for running standalone application tests.
     **/
    public static void main(String args[])
    {
        try
        {
            TestDriver.runApplication(new SysvalTestDriver(args));
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
    public SysvalTestDriver() throws Exception
    {
        super();
    }

    /**
     This constructor is used for applications.
     @param  args  The array of command line arguments.
     @exception  Exception  Incorrect arguments will cause an exception.
     **/
    public SysvalTestDriver(String[] args) throws Exception
    {
        super(args);
    }

    /**
     Creates Testcase objects for all the testcases in this component.
     **/
    public void createTestcases()
    {
        // SecPortMapperTestcase needs to be first to insure proper initial value testing.
        Testcase[] testcases =
        {
            new SysvalGroupCtorTestcase(),
            new SysvalGroupTestcase(),
            new SysvalGroupBeanInfoTestcase(),
            new SysvalCtorTestcase(),
            new SysvalGetSetTestcase(),
            new SysvalBeanInfoTestcase(),
            new SysvalSerialization(),
            new SysvalExtendedTestcase(),
            new SysvalReleaseTestcase(),
            new SysvalListCtorTestcase(),
            new SysvalListGetSetTestcase(),
            new SysvalListBeanInfoTestcase(),
            new SysvalListSerialization(),
            new SysvalP9907639(),
            new SysvalP9928495()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
            testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_);
            addTestcase(testcases[i]);
        }
    }
}
