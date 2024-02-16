///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JobDescTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
//
// File Name:    JobDescTest.java
//
// Classes:      JobDescTest
//
////////////////////////////////////////////////////////////////////////
package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Vector;

/**
Test driver for the Job Description component.  
**/
public class JobDescTest
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
        runApplication(new JobDescTest(args));
    }


/**
Constructs an object for applets.
 @exception  Exception  If an exception occurs.
**/
    public JobDescTest()
        throws Exception
    {
        super();
    }


/**
Constructs an object for testing applications.

@param      args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public JobDescTest(String[] args)
        throws Exception
    {
        super(args);
    }


/**
Creates the testcases.
**/
    public void createTestcases ()
    {
        boolean allTestcases = (namesAndVars_.size() == 0);

        // Test the Job Description classes.

        addTestcase(new JobDescTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_,  pwrSys_, TestDriverStatic.brief_));
    }

}


