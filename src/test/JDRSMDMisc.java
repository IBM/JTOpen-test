///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDMisc.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDRSMDMisc.java
//
// Classes:      JDRSMDMisc
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSMDMisc.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>toString()
</ul>
**/
public class JDRSMDMisc
extends JDTestcase {

    private static String TABLE               = JDRSMDTest.COLLECTION
                                                      + ".JDRSMDM";



    // Private data.
    private Statement statement_    = null;
    private ResultSet rs_           = null;
    private ResultSetMetaData rsmd_ = null;




/**
Constructor.
**/
    public JDRSMDMisc (AS400 systemObject,
                       Hashtable namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
    {
        super (systemObject, "JDRSMDMisc",
               namesAndVars, runMode, fileOutputStream,
               password);
    }




/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
        TABLE               = JDRSMDTest.COLLECTION + ".JDRSMDM";
        
        // SQL400 - driver neutral...
        String url = baseURL_
                     // String url = "jdbc:as400://" + systemObject_.getSystemName()
                     
                     ;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

        // Get a result set from a query.
        statement_ = connection_.createStatement ();
        initTable(statement_,  TABLE, " (COL1 INTEGER)");
        rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE);
        rsmd_ = rs_.getMetaData ();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        rs_.close ();

        cleanupTable(statement_ , TABLE);
        statement_.close ();
        connection_.close ();
        
    }

    
/**
toString() - Test toString when the cursor name has not been set.
**/
    public void Var001()
    {
        try {
            String s = rsmd_.toString ();
            assertCondition (s.length () > 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
toString() - Test toString when the cursor name has been set.

SQL400 - Perhaps this is a problem but we will need to deal with but 
         the Toolbox frees all their resources on the close of the
         result set and free them on the close of the statement.
         
         This means that we need to add the line to close the
         statement for cleanup to work correctly.
**/
    public void Var002()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setCursorName ("TEST1");
            ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE);
            ResultSetMetaData rsmd = rs.getMetaData ();
            String cursorName = rsmd.toString ();
            rs.close ();
            if (getDriver() == JDTestDriver.DRIVER_NATIVE)  // @B1A
                s.close();
            assertCondition (cursorName.equals ("TEST1"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
toString() - Test toString when the result set is closed.

SQL400 - Perhaps this is a problem but we will need to deal with but 
         the Toolbox frees all their resources on the close of the
         result set and free them on the close of the statement.
         
         This means that we need to add the line to close the
         statement for cleanup to work correctly.
**/
    public void Var003()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setCursorName ("TEST2");
            ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            if (getDriver() == JDTestDriver.DRIVER_NATIVE) // @B1A
                s.close();
            assertCondition (rsmd.toString ().equals ("TEST2"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
toString() - Test toString when the meta data comes from a prepared statement.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM " + TABLE);
                ps.setCursorName ("TEST777");
                ResultSetMetaData rsmd = ps.getMetaData ();
                String cursorName = rsmd.toString ();
                ps.close ();
                assertCondition (cursorName.equals ("TEST777"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


}



