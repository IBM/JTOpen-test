///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSCPDSTest.java
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
// File Name:    JDSCPDSTest.java
//
// Classes:      JDSCPDSTest
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

import test.JD.SCPDS.JDSCPDSConnectionClose;
import test.JD.SCPDS.JDSCPDSConnectionCommit;
import test.JD.SCPDS.JDSCPDSConnectionCreateStatement;
import test.JD.SCPDS.JDSCPDSConnectionFormat;
import test.JD.SCPDS.JDSCPDSConnectionLibraries;
import test.JD.SCPDS.JDSCPDSConnectionMisc;
import test.JD.SCPDS.JDSCPDSConnectionNaming;
import test.JD.SCPDS.JDSCPDSConnectionNativeSQL;
import test.JD.SCPDS.JDSCPDSConnectionReadOnly;
import test.JD.SCPDS.JDSCPDSConnectionSort;
import test.JD.SCPDS.JDSCPDSConnectionTransactionIsolation;
import test.JD.SCPDS.JDSCPDSConnectionWarnings;
import test.JD.SCPDS.JDSCPDSGetConnection;
import test.JD.SCPDS.JDSCPDSGetReference;
import test.JD.SCPDS.JDSCPDSMisc;
import test.JD.SCPDS.JDSCPDSProperties;


/**
Test driver for the JDBC ConnectionPoolDataSource class.
**/
public class JDSCPDSTest
extends JDTestDriver {

    public static  String COLLECTION     = "JDSCPDSTEST";


/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.
@exception  Exception  If an exception occurs.
**/
    public static void main (String args[])
    throws Exception
    {
        runApplication (new JDSCPDSTest (args));
    }



/**
Constructs an object for applets.
@exception  Exception  If an exception occurs.
**/
    public JDSCPDSTest ()
    throws Exception
    {
        super();
    }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.
@exception  Exception  If an exception occurs.
**/
    public JDSCPDSTest (String[] args)
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

        addTestcase (new JDSCPDSConnectionClose (systemObject_,
                                                namesAndVars_, runMode_, fileOutputStream_, 
                                                password_));

        addTestcase (new JDSCPDSConnectionCommit (systemObject_,
                                                 namesAndVars_, runMode_, fileOutputStream_, 
                                                 password_));

        addTestcase (new JDSCPDSConnectionCreateStatement (systemObject_,
                                                          namesAndVars_, runMode_, fileOutputStream_, 
                                                          password_));

        addTestcase (new JDSCPDSConnectionFormat (systemObject_,
                                                 namesAndVars_, runMode_, fileOutputStream_, 
                                                 password_));

        addTestcase (new JDSCPDSConnectionLibraries (systemObject_,
                                                    namesAndVars_, runMode_, fileOutputStream_, 
                                                    password_));

        addTestcase (new JDSCPDSConnectionMisc (systemObject_,
                                               namesAndVars_, runMode_, fileOutputStream_, 
                                               password_));

        addTestcase (new JDSCPDSConnectionNaming (systemObject_,
                                                 namesAndVars_, runMode_, fileOutputStream_, 
                                                 password_));

        addTestcase (new JDSCPDSConnectionNativeSQL (systemObject_,
                                                    namesAndVars_, runMode_, fileOutputStream_, 
                                                    password_));

        addTestcase (new JDSCPDSConnectionReadOnly (systemObject_,
                                                   namesAndVars_, runMode_, fileOutputStream_, 
                                                   password_));

        addTestcase (new JDSCPDSConnectionSort (systemObject_,
                                               namesAndVars_, runMode_, fileOutputStream_, 
                                               password_));

        addTestcase (new JDSCPDSConnectionTransactionIsolation (systemObject_,
                                                               namesAndVars_, runMode_, fileOutputStream_, 
                                                               password_));

        addTestcase (new JDSCPDSConnectionWarnings (systemObject_,
                                                   namesAndVars_, runMode_, fileOutputStream_, 
                                                   password_));

        addTestcase (new JDSCPDSGetConnection (systemObject_,
                                              namesAndVars_, runMode_, fileOutputStream_, 
                                              password_));

        addTestcase (new JDSCPDSGetReference (systemObject_,
                                             namesAndVars_, runMode_, fileOutputStream_, 
                                             password_));

        addTestcase (new JDSCPDSMisc (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_, 
                                     password_));

        addTestcase (new JDSCPDSProperties (systemObject_,
                                           namesAndVars_, runMode_, fileOutputStream_, 
                                           password_));
    }

}


