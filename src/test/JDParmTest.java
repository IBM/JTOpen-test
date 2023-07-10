///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmTest.java
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
// File Name:    JDParmTest.java
//
// Classes:      JDParmTest
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test;

import java.sql.Connection;
import java.sql.Statement;



/**
Test driver for the JDBC PreparedStatement class.
**/
public class JDParmTest
extends JDTestDriver {


    /**
   * 
   */
  private static final long serialVersionUID = 1L;

    // Constants.
    public static String COLLECTION     = "JDTESTPA";

    public static String PATEST_SET     = COLLECTION + ".PATEST_SET";



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
        runApplication (new JDParmTest (args));
    }



/**
Constructs an object for applets.

@exception Exception If an exception occurs.
**/
    public JDParmTest ()
    throws Exception
    {
        super();
    }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.

@exception Exception If an exception occurs.
**/
    public JDParmTest (String[] args)
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


	if (testLib_ != null) { 
	    COLLECTION = testLib_;
	}

        connection_ = getConnection (getBaseURL (),
                                     systemObject_.getUserId (), encryptedPassword_);


        statement_ = connection_.createStatement ();

        // Setup PATEST_SET table.
        // Create a table that uses the largest row size the database will
        // allow me to use.
//        statement_.executeUpdate("create table cujosql.strings " + 
//                        "(col1 char(1), col2 char(20), col3 char (32744))");

	//
        // Make sure the collection exists
        //
	JDSetupCollection.create(connection_, COLLECTION, false);


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

            // Testing setString to CHAR FOR BIT DATA fields.
            // working
            addTestcase (new JDParmStringBinary (systemObject_,
                                                 namesAndVars_, runMode_, fileOutputStream_, 
                                                 password_));

//        // Testing setString to BLOB fields (char translation of blobs is not supported).
//        addTestcase (new JDParmStringBlob (systemObject_,
//                                            namesAndVars_, runMode_, fileOutputStream_, 
//                                            password_));

            // Testing setString to CHAR fields.
            // working
            addTestcase (new JDParmStringChar (systemObject_,
                                               namesAndVars_, runMode_, fileOutputStream_, 
                                               password_));

            // Testing setString to CHAR (with embedded nulls) fields.
            // working
            addTestcase (new JDParmStringCharHex (systemObject_,
                                                  namesAndVars_, runMode_, fileOutputStream_, 
                                                  password_));

            // Testing setString to CLOB fields.
            // working
            addTestcase (new JDParmStringClob (systemObject_,
                                               namesAndVars_, runMode_, fileOutputStream_, 
                                               password_));

            // Testing setString to DBCLOB fields.
            // working
            addTestcase (new JDParmStringDbclob (systemObject_,
                                                 namesAndVars_, runMode_, fileOutputStream_, 
                                                 password_));

            // Testing setString to DECIMAL fields.
            // working
            // todo - get the index in for data truncation
            addTestcase (new JDParmStringDecimal (systemObject_,
                                                  namesAndVars_, runMode_, fileOutputStream_, 
                                                  password_));

            // Testing setString to GRAPHIC fields.
            // working
            addTestcase (new JDParmStringGraphic (systemObject_,
                                                  namesAndVars_, runMode_, fileOutputStream_, 
                                                  password_));

            // Testing setString to CHAR (mixed CCSID) fields.
            // working
            addTestcase (new JDParmStringMixed (systemObject_,
                                                namesAndVars_, runMode_, fileOutputStream_, 
                                                password_));

            // Testing setString to CHAR (mixed CCSID - DBCS chars used) fields.
            // working - note:  we can't get the right 'needed' length
            addTestcase (new JDParmStringMixed2 (systemObject_,
                                                 namesAndVars_, runMode_, fileOutputStream_, 
                                                 password_));

            // Testing setString to NUMERIC fields.
            // todo - get the index in for data truncation
            addTestcase (new JDParmStringNumeric (systemObject_,
                                                  namesAndVars_, runMode_, fileOutputStream_, 
                                                  password_));

            // Testing setString to VARCHAR FOR BIT DATA fields.
            // working
            addTestcase (new JDParmStringVarbinary (systemObject_,
                                                    namesAndVars_, runMode_, fileOutputStream_, 
                                                    password_));

            // Testing setString to VARCHAR fields.
            // working
            addTestcase (new JDParmStringVarchar (systemObject_,
                                                  namesAndVars_, runMode_, fileOutputStream_, 
                                                  password_));

            // Testing setString to VARCHAR (with embedded nulls) fields.
            // working
            addTestcase (new JDParmStringVarcharHex (systemObject_,
                                                     namesAndVars_, runMode_, fileOutputStream_, 
                                                     password_));

            // Testing setString to VARGRAPHIC fields.
            // working
            addTestcase (new JDParmStringVargraphic (systemObject_,
                                                     namesAndVars_, runMode_, fileOutputStream_, 
                                                     password_));

            // Testing setString to VARCHAR (mixed CCSID) fields.
            // working
            addTestcase (new JDParmStringVarmixed (systemObject_,
                                                   namesAndVars_, runMode_, fileOutputStream_, 
                                                   password_));

            // Testing setString to VARCHAR (mixed CCSID - DBCS chars used) fields.
            // working - note:  we can't get the right 'needed' length
            addTestcase (new JDParmStringVarmixed2 (systemObject_,
                                                    namesAndVars_, runMode_, fileOutputStream_, 
                                                    password_));

            // Testing setString to GRAPHIC CCSID(13488) fields.
            // working
            addTestcase (new JDParmStringWchar (systemObject_,
                                                namesAndVars_, runMode_, fileOutputStream_, 
                                                password_));

            // Testing setString to VARGRAPHIC CCSID(13488) fields.
            // working
            addTestcase (new JDParmStringWvarchar (systemObject_,
                                                   namesAndVars_, runMode_, fileOutputStream_, 
                                                   password_));

            addTestcase (new JDParmStringLarge (systemObject_,
                                                   namesAndVars_, runMode_, fileOutputStream_, 
                                                   password_));

    }
}



