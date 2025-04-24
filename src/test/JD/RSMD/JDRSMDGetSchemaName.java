///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDGetSchemaName.java
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
// File Name:    JDRSMDGetSchemaName.java
//
// Classes:      JDRSMDGetSchemaName
//
////////////////////////////////////////////////////////////////////////

package test.JD.RSMD;

import com.ibm.as400.access.AS400;

import test.JDRSMDTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDRSMDGetSchemaName.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>getSchemaName()
</ul>
**/
public class JDRSMDGetSchemaName
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMDGetSchemaName";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSMDTest.main(newArgs); 
   }

    // Private data.
    private ResultSet rs_            = null;
    private ResultSetMetaData rsmd_  = null;
    private Statement statement_     = null;


    // Private data.
    private Connection connection2_  = null;
    private ResultSet rs2_           = null;
    private ResultSetMetaData rsmd2_ = null;
    private Statement statement2_    = null;


/**
Constructor.
**/
    public JDRSMDGetSchemaName (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSMDGetSchemaName",
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

        //@C1A
        String url2 = baseURL_
            
            + ";extended metadata=true";
        connection2_ = testDriver_.getConnection (url2,systemObject_.getUserId(), encryptedPassword_);
        statement2_ = connection2_.createStatement ();
        rs2_ = statement2_.executeQuery ("SELECT * FROM "
            + JDRSMDTest.RSMDTEST_GET);
        rsmd2_ = rs2_.getMetaData ();

    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        rs_.close ();
        rs2_.close ();          //@C1A
        statement2_.close ();   //@C1A
        statement_.close ();
        connection_.close ();
        connection2_.close();   //@C1A
    }


    protected void cleanupConnections ()
        throws Exception
    {
        connection_.close ();
        connection2_.close();   //@C1A
    }

/**
getSchemaName() - Check column -1.  Should throw an exception.
**/
    public void Var001()
    {
        try {
            String s = rsmd_.getSchemaName (-1);
            failed ("Didn't throw SQLException  got "+s+"");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getSchemaName() - Check column 0.  Should throw an exception.
**/
    public void Var002()
    {
        try {
            String s = rsmd_.getSchemaName (0);
            failed ("Didn't throw SQLException got "+s+"");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getSchemaName() - Check a column greater than the max.
Should throw an exception.
**/
    public void Var003()
    {
        try {
            String s = rsmd_.getSchemaName (35);
            failed ("Didn't throw SQLException got "+s+"");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getSchemaName() - Check a valid column.
**/
    public void Var004()
    {
        try {
            String s = rsmd_.getSchemaName (1);
            assertCondition (s.equals (""), "Got '"+s+"' expected ''");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getSchemaName() - Check when the result set is closed.
**/
    public void Var005()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            String v = rsmd.getSchemaName (1);
            assertCondition (v.equals (""), "Got '"+v+"' expected ''");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSchemaName() - Check when the meta data is from a prepared statement.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps;
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    ps = connection_.prepareStatement ("SELECT * FROM "
						       + JDRSMDTest.RSMDTEST_GET); 

		} else { 
		    ps = connection_.prepareStatement ("SELECT * FROM "
						       + JDRSMDTest.RSMDTEST_GET, ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_READ_ONLY);
		}
                ResultSetMetaData rsmd = ps.getMetaData ();
                String v = rsmd.getSchemaName (1);
                ps.close ();
                assertCondition (v.equals (""), "Got '"+v+"' sb ''");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



//@C1A  Add testcases to test "extended metadata" property.


/**
getSchemaName() - Check column -1.  Should throw an exception.
**/
    public void Var007()
    {
        try {
            String s = rsmd2_.getSchemaName (-1);
            failed ("Didn't throw SQLException got "+s+"");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getSchemaName() - Check column 0.  Should throw an exception.
**/
    public void Var008()
    {
        try {
            String s = rsmd2_.getSchemaName (0);
            failed ("Didn't throw SQLException got "+s+"");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getSchemaName() - Check a column greater than the max.
Should throw an exception.
**/
    public void Var009()
    {
        try {
            String s = rsmd2_.getSchemaName (35);
            failed ("Didn't throw SQLException got "+s+"");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getSchemaName() - Check a valid column.
**/
    public void Var010()
    {
        try {
            String s = rsmd2_.getSchemaName (1);
            assertCondition (s.equals (JDRSMDTest.COLLECTION), "schemaName="+s+" sb "+JDRSMDTest.COLLECTION);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getSchemaName() - Check when the result set is closed.
**/
    public void Var011()
    {
        try {
            Statement s = connection2_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            String v = rsmd.getSchemaName (1);
            assertCondition (v.equals (JDRSMDTest.COLLECTION), "schemaName="+v+" sb "+JDRSMDTest.COLLECTION);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSchemaName() - Check when the meta data is from a prepared statement.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps;
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    ps = connection2_.prepareStatement ("SELECT * FROM "
							+ JDRSMDTest.RSMDTEST_GET); 
		} else { 
		    ps = connection2_.prepareStatement ("SELECT * FROM "
							+ JDRSMDTest.RSMDTEST_GET, ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
		}
                ResultSetMetaData rsmd = ps.getMetaData ();
                String v = rsmd.getSchemaName (1);
                ps.close ();
                assertCondition (v.equals (JDRSMDTest.COLLECTION), "schemaName="+v+" sb "+JDRSMDTest.COLLECTION);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getSchemaName() - Make sure we get old information if "extended metadata=false".
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
            try {
                Connection connection3 = testDriver_.getConnection(baseURL_
                                                                  + ";extended metadata=false",systemObject_.getUserId(),encryptedPassword_); 
                PreparedStatement ps;
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    ps = connection3.prepareStatement ("SELECT * FROM "
						       + JDRSMDTest.RSMDTEST_GET); 
		} else { 
		    ps = connection3.prepareStatement ("SELECT * FROM "
						       + JDRSMDTest.RSMDTEST_GET, ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_READ_ONLY);
		}
                ResultSetMetaData rsmd = ps.getMetaData ();
                String v = rsmd.getSchemaName (1);
                ps.close ();
                assertCondition (v.equals (""), "Got '"+v+"' sb ''");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




}



