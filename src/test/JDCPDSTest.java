///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCPDSTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCPDSTest.java
//
// Classes:      JDCPDSTest
//
////////////////////////////////////////////////////////////////////////
//
//
// Release     Date        Userid    Comments
//
//
////////////////////////////////////////////////////////////////////////

package test;

import java.sql.*;


/**
Test driver for the JDBC ConnectionPoolDataSource class.
**/
public class JDCPDSTest
extends JDTestDriver {

    public static  String COLLECTION     = "JDCPDSTEST";


/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.
@exception  Exception  If an exception occurs.
**/
    public static void main (String args[])
    throws Exception
    {
        runApplication (new JDCPDSTest (args));
    }



/**
Constructs an object for applets.
@exception  Exception  If an exception occurs.
**/
    public JDCPDSTest ()
    throws Exception
    {
        super();
    }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.
@exception  Exception  If an exception occurs.
**/
    public JDCPDSTest (String[] args)
    throws Exception
    {
        super (args);
    }

/**
Performs setup needed before running testcases.

@exception Exception If an exception occurs.
**/
    public void setup ()
      throws Exception
    {
        super.setup();
        if(testLib_ != null)
            COLLECTION = testLib_;
    }

/**
dropCollections - - this does not run automatically - - it is called by JDCleanup.
**/
    public static void dropCollections(Connection c)
    {
        dropCollection(c, COLLECTION);
    }


/**
Creates the testcases.
**/
    public void createTestcases2 ()
    {

        addTestcase (new JDCPDSConnectionClose (systemObject_,
                                                namesAndVars_, runMode_, fileOutputStream_, 
                                                password_));

        addTestcase (new JDCPDSConnectionCommit (systemObject_,
                                                 namesAndVars_, runMode_, fileOutputStream_, 
                                                 password_));

        addTestcase (new JDCPDSConnectionCreateStatement (systemObject_,
                                                          namesAndVars_, runMode_, fileOutputStream_, 
                                                          password_));

        addTestcase (new JDCPDSConnectionFormat (systemObject_,
                                                 namesAndVars_, runMode_, fileOutputStream_, 
                                                 password_));

        addTestcase (new JDCPDSConnectionLibraries (systemObject_,
                                                    namesAndVars_, runMode_, fileOutputStream_, 
                                                    password_));

        addTestcase (new JDCPDSConnectionMisc (systemObject_,
                                               namesAndVars_, runMode_, fileOutputStream_, 
                                               password_));

        addTestcase (new JDCPDSConnectionNaming (systemObject_,
                                                 namesAndVars_, runMode_, fileOutputStream_, 
                                                 password_));

        addTestcase (new JDCPDSConnectionNativeSQL (systemObject_,
                                                    namesAndVars_, runMode_, fileOutputStream_, 
                                                    password_));

        addTestcase (new JDCPDSConnectionReadOnly (systemObject_,
                                                   namesAndVars_, runMode_, fileOutputStream_, 
                                                   password_));

        addTestcase (new JDCPDSConnectionSort (systemObject_,
                                               namesAndVars_, runMode_, fileOutputStream_, 
                                               password_));

        addTestcase (new JDCPDSConnectionTransactionIsolation (systemObject_,
                                                               namesAndVars_, runMode_, fileOutputStream_, 
                                                               password_));

        addTestcase (new JDCPDSConnectionWarnings (systemObject_,
                                                   namesAndVars_, runMode_, fileOutputStream_, 
                                                   password_));

        addTestcase (new JDCPDSGetConnection (systemObject_,
                                              namesAndVars_, runMode_, fileOutputStream_, 
                                              password_));

        addTestcase (new JDCPDSGetReference (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));

        addTestcase (new JDCPDSMisc (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_));

        addTestcase (new JDCPDSProperties (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));
    }

}


