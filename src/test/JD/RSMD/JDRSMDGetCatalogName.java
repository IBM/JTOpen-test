///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDGetCatalogName.java
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
// File Name:    JDRSMDGetCatalogName.java
//
// Classes:      JDRSMDGetCatalogName
//
////////////////////////////////////////////////////////////////////////

package test.JD.RSMD;

import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSMDTest;
import test.JDTestcase;



/**
Testcase JDRSMDGetCatalogName.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>getCatalogName()
</ul>
**/
public class JDRSMDGetCatalogName
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMDGetCatalogName";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSMDTest.main(newArgs); 
   }



    // Private data.
    private ResultSet rs_           = null;
    private ResultSetMetaData rsmd_ = null;
    private Statement statement_    = null;



/**
Constructor.
**/
    public JDRSMDGetCatalogName (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSMDGetCatalogName",
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
        // SQL400 - driver neutral...
        String url = baseURL_
        // String url = "jdbc:as400://" + systemObject_.getSystemName()
            
             ;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        statement_ = connection_.createStatement ();
        rs_ = statement_.executeQuery ("SELECT * FROM "
            + JDRSMDTest.RSMDTEST_GET);
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
        statement_.close ();
        connection_.close ();
    }



/**
getCatalogName() - Check column -1.  Should throw an exception.
**/
    public void Var001()
    {
        try {
            String s = rsmd_.getCatalogName (-1);
            failed ("Didn't throw SQLException"+s);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getCatalogName() - Check column 0.  Should throw an exception.
**/
    public void Var002()
    {
        try {
            String s = rsmd_.getCatalogName (0);
            failed ("Didn't throw SQLException"+s);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getCatalogName() - Check a column greater than the max.
Should throw an exception.
**/
    public void Var003()
    {
        try {
            String s = rsmd_.getCatalogName (35);
            failed ("Didn't throw SQLException"+s);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getCatalogName() - Check a valid column.
**/
    public void Var004()
    {
        try {
            String s = rsmd_.getCatalogName (1);
            assertCondition (s.equals (connection_.getCatalog()));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCatalogName() - Check when the result set is closed.
**/
    public void Var005()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            String v = rsmd.getCatalogName (1);
            s.close ();
            assertCondition (v.equals (connection_.getCatalog()));
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getCatalogName() - Check when the meta data is from a prepared statement.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM "
                    + JDRSMDTest.RSMDTEST_GET);
                ResultSetMetaData rsmd = ps.getMetaData ();
                String s = rsmd.getCatalogName (1);
                assertCondition (s.equals (connection_.getCatalog()));
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



}



