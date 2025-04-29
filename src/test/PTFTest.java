///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PTFTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import test.misc.PTFGroupTestcase;

/**
Test driver for the SaveFile* classes.  
**/
public class PTFTest
extends TestDriver
{

/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public static void main(String args[])
        throws Exception
    {
        runApplication(new PTFTest(args));
    }


/**
Constructs an object for applets.
 @exception  Exception  If an exception occurs.
**/
    public PTFTest()
        throws Exception
    {
        super();
    }


/**
Constructs an object for testing applications.

@param      args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public PTFTest(String[] args)
        throws Exception
    {
        super(args);
    }


/**
Creates the testcases.
**/
    public void createTestcases ()
    {
        // boolean allTestcases = (namesAndVars_.size() == 0);

        // Test the SaveFile classes.

        addTestcase(new PTFGroupTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, pwrSys_, TestDriverStatic.brief_, isLocal_, isNative_));
    }

}


