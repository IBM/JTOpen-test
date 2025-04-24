///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDIsNullable.java
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
// File Name:    JDRSMDIsNullable.java
//
// Classes:      JDRSMDIsNullable
//
////////////////////////////////////////////////////////////////////////

package test.JD.RSMD;

import java.io.FileOutputStream;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSMDTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;



/**
Testcase JDRSMDIsNullable.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>isNullable()
</ul>
**/
public class JDRSMDIsNullable
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMDIsNullable";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSMDTest.main(newArgs); 
   }



    private static final String PACKAGE             = "JDRSMDIN";
    private static final String PACKAGE_CACHE_NO    = "extended dynamic=false";
    private static String PACKAGE_CACHE_YES   = "extended dynamic=true;package="
                                                      + PACKAGE + ";package library="
                                                      + JDRSMDTest.COLLECTION
                                                      + ";package cache=true";
    private static String TABLE               = JDRSMDTest.COLLECTION
                                                      + ".JDRSMDIN";



    // Private data.
    private String properties_      = "";
    private Statement statement_    = null;
    private ResultSet rs_           = null;
    private ResultSetMetaData rsmd_ = null;




/**
Constructor.
**/
    public JDRSMDIsNullable (AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
    {
        super (systemObject, "JDRSMDIsNullable",
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
        PACKAGE_CACHE_YES   = "extended dynamic=true;package="
          + PACKAGE + ";package library="
          + JDRSMDTest.COLLECTION
          + ";package cache=true";
        TABLE               = JDRSMDTest.COLLECTION  + ".JDRSMDIN";
        
        // SQL400 - driver neutral...
        String url = baseURL_
                     // String url = "jdbc:as400://" + systemObject_.getSystemName()
                     
                     ;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        statement_ = connection_.createStatement ();
        initTable(statement_,  TABLE,  " (YES INTEGER, NO INTEGER NOT NULL)");

        if (isToolboxDriver()) {
            JDSetupPackage.prime (systemObject_, PACKAGE,
                                  JDRSMDTest.COLLECTION, "SELECT * FROM " + TABLE);
        }
        else {
            JDSetupPackage.prime (systemObject_, encryptedPassword_, PACKAGE,
                                  JDRSMDTest.COLLECTION, "SELECT * FROM " + TABLE, 
                                  "", getDriver());
        }

        reconnect (PACKAGE_CACHE_NO);
    }



/**
Reconnects with different properties, if needed.

@exception Exception If an exception occurs.
**/
    private void reconnect (String properties)
    throws Exception
    {
        if (! properties_.equals (properties)) {
            properties_ = properties;
            if (connection_ != null) {
                if (rs_ != null)
                    rs_.close ();
                statement_.close ();
                connection_.close ();
            }

            // SQL400 - driver neutral...
            String url = baseURL_
                         // String url = "jdbc:as400://" + systemObject_.getSystemName()
                         
                         ;
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
            statement_ = connection_.createStatement ();
            rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE);
            rsmd_ = rs_.getMetaData ();
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        rs_.close ();

        cleanupTable(statement_,  TABLE);

        statement_.close ();
        connection_.close ();
    }



/**
isNullable() - Check column -1.  Should throw an exception.
(Package cache = false)
**/
    public void Var001()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.isNullable (-1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isNullable() - Check column 0.  Should throw an exception.
(Package cache = false)
**/
    public void Var002()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.isNullable (0);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isNullable() - Check a column greater than the max.
Should throw an exception. (Package cache = false)
**/
    public void Var003()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            rsmd_.isNullable (3);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isNullable() - Check a column that is nullable.
(Package cache = false)
**/
    public void Var004()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.isNullable (1);
            assertCondition (s == ResultSetMetaData.columnNullable);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isNullable() - Check a column that is not nullable.
(Package cache = false)
**/
    public void Var005()
    {
        try {
            reconnect (PACKAGE_CACHE_NO);
            int s = rsmd_.isNullable (2);
            assertCondition (s == ResultSetMetaData.columnNoNulls);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isNullable() - Check column -1.  Should throw an exception.
(Package cache = true)
**/
    public void Var006()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.isNullable (-1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isNullable() - Check column 0.  Should throw an exception.
(Package cache = true)
**/
    public void Var007()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.isNullable (0);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isNullable() - Check a column greater than the max.
Should throw an exception. (Package cache = true)
**/
    public void Var008()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            rsmd_.isNullable (3);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isNullable() - Check a column that is nullable.
(Package cache = true)
**/
    public void Var009()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.isNullable (1);
            assertCondition (s == ResultSetMetaData.columnNullable);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isNullable() - Check a column with a multiple character name.
(Package cache = true)
**/
    public void Var010()
    {
        try {
            reconnect (PACKAGE_CACHE_YES);
            int s = rsmd_.isNullable (2);
            assertCondition (s == ResultSetMetaData.columnNoNulls);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isNullable() - Check when the result set is closed.

SQL400 - Perhaps this is a problem but we will need to deal with but 
         the Toolbox frees all their resources on the close of the
         result set and free them on the close of the statement.
         
         This means that we need to add the line to close the
         statement for cleanup to work correctly.
**/
    public void Var011()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT YES FROM " + TABLE);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            int v = rsmd.isNullable (1);
            if (getDriver() == JDTestDriver.DRIVER_NATIVE) // @B1A
                s.close();
            assertCondition (v == ResultSetMetaData.columnNullable);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isNullable() - Check when the meta data is from a prepared statement.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("SELECT YES FROM " + TABLE);
                ResultSetMetaData rsmd = ps.getMetaData ();
                int s = rsmd.isNullable (1);
                ps.close ();
                assertCondition (s == ResultSetMetaData.columnNullable);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isNullable() - Check when the result set is from a database meta data.
**/
    public void Var013()
    {
        try {
            DatabaseMetaData dmd = connection_.getMetaData ();
            ResultSet rs = dmd.getSchemas ();
            ResultSetMetaData rsmd = rs.getMetaData ();
            int s = rsmd.isNullable (1);
            rs.close ();
            assertCondition (s == ResultSetMetaData.columnNoNulls);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}
