///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDBUTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDBUTest.java
//
// Classes:      JDBUTest
//
////////////////////////////////////////////////////////////////////////

package test;

import java.sql.Connection;
import java.sql.Statement;

import test.JD.JDSetupCollection;
import test.JD.BU.JDBUPSArray;
import test.JD.BU.JDBUPSFunctional;
import test.JD.BU.JDBUStatementArray;



/**
Test driver for the JDBC PreparedStatement class.
**/
public class JDBUTest
extends JDTestDriver {



	// Constants.
    public static String COLLECTION     = "JDTESTBU";

    public static String BUTEST       = COLLECTION + ".BUTEST";
    public static String BUTESTDATA   = COLLECTION + ".BUTESTDATA";
    public static String BUTESTLOB    = COLLECTION + ".BUTESTLOB";


    // Private data.
    private Connection  connection_;
    private Statement   statement_;



/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.

@exception Exception If an exception occurs.
**/
    public static void main (String args[])
    throws Exception
    {
        runApplication (new JDBUTest (args));
    }



/**
Constructs an object for applets.

@exception Exception If an exception occurs.
**/
    public JDBUTest ()
    throws Exception
    {
        super();
    }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.

@exception Exception If an exception occurs.
**/
    public JDBUTest (String[] args)
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

        connection_ = getConnection (getBaseURL (),
                                     systemObject_.getUserId (), encryptedPassword_);


        statement_ = connection_.createStatement ();

        if(testLib_ != null) { 
          COLLECTION = testLib_;
          BUTEST     = COLLECTION + ".BUTEST";
          BUTESTDATA = COLLECTION + ".BUTESTDATA";
          BUTESTLOB  = COLLECTION + ".BUTESTLOB";
        }
        JDSetupCollection.create (systemObject_, 
                                  connection_, COLLECTION);

        connection_.commit(); // for xa
    }



/**
Performs setup needed after running testcases.

@exception Exception If an exception occurs.
**/
    public void cleanup ()
    throws Exception
    {
        statement_.close ();
        connection_.commit(); // for xa
        connection_.close ();
    }


/**
Cleanup - - this does not run automatically - - it is called by JDCleanup.
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

            // Testing related to the array created for 
            //   PreparedStatement batch updates
            addTestcase (new JDBUPSArray (systemObject_,
                                          namesAndVars_, runMode_, fileOutputStream_, 
                                          password_));

            // Testing related to the array created for 
            //   Statement batch updates
            addTestcase (new JDBUStatementArray (systemObject_,
                                          namesAndVars_, runMode_, fileOutputStream_, 
                                          password_));

            // Testing related strange batch update conditions
            addTestcase (new JDBUPSFunctional (systemObject_,
                                          namesAndVars_, runMode_, fileOutputStream_, 
                                          password_));

    }
}



