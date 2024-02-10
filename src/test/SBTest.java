///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SBTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Vector;

import test.misc.SBTestcase;

/**
Test driver for the Subsystem component.  
**/
public class SBTest
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
        runApplication(new SBTest(args));
    }


/**
Constructs an object for applets.
 @exception  Exception  If an exception occurs.
**/
    public SBTest()
        throws Exception
    {
        super();
    }


/**
Constructs an object for testing applications.

@param      args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public SBTest(String[] args)
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

        // Test the Subsystem classes.

        addTestcase(new SBTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_,  pwrSys_, TestDriverStatic.brief_));
    }

}


