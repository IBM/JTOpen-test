///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDGetColumnCount.java
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
// File Name:    JDRSMDGetColumnCount.java
//
// Classes:      JDRSMDGetColumnCount
//
////////////////////////////////////////////////////////////////////////

package test.JD.RSMD;

import com.ibm.as400.access.AS400;

import test.JDRSMDTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSMDGetColumnCount.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>getColumnCount()
</ul>
**/
public class JDRSMDGetColumnCount
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMDGetColumnCount";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSMDTest.main(newArgs); 
   }



    // Private data.
    private Statement statement_    = null;



/**
Constructor.
**/
    public JDRSMDGetColumnCount (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSMDGetColumnCount",
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
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        statement_.close ();
        connection_.close ();
    }



/**
getColumnCount() - Check a result set with 1 column.
**/
    public void Var001()
    {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT C_REAL FROM "
                + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            int columnCount = rsmd.getColumnCount ();
            rs.close ();
            assertCondition (columnCount == 1);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnCount() - Check a result set with 10 columns.
**/
    public void Var002()
    {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT C_KEY, "
                + "C_SMALLINT, C_INTEGER, C_REAL, C_FLOAT, C_DOUBLE, "
                + "C_DECIMAL_50, C_DECIMAL_105, C_NUMERIC_50, "
                + "C_NUMERIC_105 FROM "
                + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            int columnCount = rsmd.getColumnCount ();
            rs.close ();
            assertCondition (columnCount == 10);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnCount() - Check when the result set is closed.
**/
    public void Var003()
    {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT C_KEY, C_SMALLINT FROM "
                + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            int columnCount = rsmd.getColumnCount ();
            assertCondition (columnCount == 2);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getColumnCount() - Check when the meta data comes from a prepared statement.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps;
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    ps = connection_.prepareStatement ("SELECT C_KEY, C_SMALLINT FROM "
						       + JDRSMDTest.RSMDTEST_GET);
		} else { 
		    ps = connection_.prepareStatement ("SELECT C_KEY, C_SMALLINT FROM "
						       + JDRSMDTest.RSMDTEST_GET, ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_READ_ONLY);
		}
                ResultSetMetaData rsmd = ps.getMetaData ();
                int columnCount = rsmd.getColumnCount ();
                ps.close ();
                assertCondition (columnCount == 2);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



}


