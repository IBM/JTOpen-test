///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDGetTableName.java
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
// File Name:    JDRSMDGetTableName.java
//
// Classes:      JDRSMDGetTableName
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
Testcase JDRSMDGetTableName.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>getTableName()
</ul>
**/
public class JDRSMDGetTableName
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMDGetTableName";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSMDTest.main(newArgs); 
   }

    // Private data.
    private Connection connection2_ = null;
    private ResultSet rs_           = null;
    private ResultSet rs2_	    = null;
    private ResultSetMetaData rsmd_ = null;
    private ResultSetMetaData rsmd2_= null;
    private Statement statement_    = null;
    private Statement statement2_   = null;

/**
Constructor.
**/
    public JDRSMDGetTableName (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSMDGetTableName",
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

	/* extended metadata connection */
	// SQL400 - driver neutral...
        String url2 = baseURL_
        // String url = "jdbc:as400://" + systemObject_.getSystemName()
           
	    + ";extended metadata=true";
        connection2_ = testDriver_.getConnection (url2,systemObject_.getUserId(), encryptedPassword_);
        statement2_ = connection2_.createStatement ();
        rs2_ = statement2_.executeQuery ("SELECT * FROM "
            + JDRSMDTest.RSMDTEST_GET);
        rsmd2_ = rs2_.getMetaData();
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

	rs2_.close();
	statement2_.close();
	connection2_.close();
    }

    protected void cleanupConnections ()
        throws Exception
    {
        connection_.close ();

  connection2_.close();
    }



/**
getTableName() - Check column -1.  Should throw an exception.
**/
    public void Var001()
    {
        try {
            String s = rsmd_.getTableName (-1);
            failed ("Didn't throw SQLException got "+s);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getTableName() - Check column 0.  Should throw an exception.
**/
    public void Var002()
    {
        try {
            String s = rsmd_.getTableName (0);
            failed ("Didn't throw SQLException got "+s);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getTableName() - Check a column greater than the max.
Should throw an exception.
**/
    public void Var003()
    {
        try {
            String s = rsmd_.getTableName (35);
            failed ("Didn't throw SQLException got "+s);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getTableName() - Check a valid column.
**/
    public void Var004()
    {
        try {
            String s = rsmd_.getTableName (1);
	    String expected="";
	    if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		expected = "RSMD_GET"; 
	    } 
            assertCondition (s.equals (expected), "Got "+s+" expected '"+expected+"'");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTableName() - Check when the result set is closed.
**/
    public void Var005()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            String v = rsmd.getTableName (1);
	    String expected="";
	    if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		expected = "RSMD_GET"; 
	    } 

            assertCondition (v.equals (expected), "Got "+v+" expected '"+expected+"'");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getTableName() - Check when the meta data is from a prepared statement.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM "
                    + JDRSMDTest.RSMDTEST_GET);
                ResultSetMetaData rsmd = ps.getMetaData ();
                String v = rsmd.getTableName (1);
                ps.close ();
		String expected = "";
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    expected = "RSMD_GET"; 
		} 

                assertCondition (v.equals (expected), "got '"+v+"' expected '"+expected+"'");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/* WILSONJO extended metadata*/

/**
getTableName() - Check column -1.  Should throw an exception.
**/
    public void Var007()
    {

        try {
            String s = rsmd2_.getTableName (-1);
            failed ("Didn't throw SQLException got "+s+"-- added by native 02/03/04 for extended metadata");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", " added by native 02/03/04 for extended metadata");
        }
    }



/**
getTableName() - Check column 0.  Should throw an exception.
**/
    public void Var008()
    {
        try {
            String s = rsmd2_.getTableName (0);
            failed ("Didn't throw SQLException -- got "+s+" added by native 02/03/04 for extended metadata");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", " added by native 02/03/04 for extended metadata");
        }
    }



/**
getTableName() - Check a column greater than the max.
Should throw an exception.
**/
    public void Var009()
    {
        try {
            String s = rsmd2_.getTableName (35);
            failed ("Didn't throw SQLException  got "+s+" -- added by native 02/03/04 for extended metadata");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", " added by native 02/03/04 for extended metadata");
        }
    }



/**
getTableName() - Check a valid column.
**/
    public void Var010()
    {
        try {
            String s = rsmd2_.getTableName (1);
            assertCondition (s.equals ("RSMD_GET"), "Expected: \"RSMD_GET\" Received: \"" + s + "\"  -- added by native 02/03/04 for extended metadata");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception -- added by native 02/03/04 for extended metadata");
        }
    }



/**
getTableName() - Check when the result set is closed.
**/
    public void Var011()
    {
        try {
            Statement s = connection2_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSMDTest.RSMDTEST_GET);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            String v = rsmd.getTableName (1);
            assertCondition (v.equals ("RSMD_GET"), "Expected: \"RSMD_GET\" Received: \"" + v + "\" -- added by native 02/03/04 for extended metadata");
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception -- added by native 02/03/04 for extended metadata");
        }
    }



/**
getTableName() - Check when the meta data is from a prepared statement.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection2_.prepareStatement ("SELECT * FROM "
                    + JDRSMDTest.RSMDTEST_GET);
                ResultSetMetaData rsmd = ps.getMetaData ();
                String v = rsmd.getTableName (1);
                ps.close ();
                assertCondition (v.equals ("RSMD_GET"), "Expected: \"RSMD_GET\" Received: \"" + v + "\" -- added by native 02/03/04 for extended metadata");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception -- added by native 02/03/04 for extended metadata");
            }
        }
    }

/**
getTableName() - Check that we get the old information if extended metadata = false.
**/
    public void Var013()
    {
	 if (checkJdbc20 ()) {
            try {
		String url = baseURL_
		  
		  
		  + ";extended metadata=false";

		Connection conn = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
		PreparedStatement ps = conn.prepareStatement ("SELECT * FROM " + JDRSMDTest.RSMDTEST_GET);
                ResultSetMetaData rsmd = ps.getMetaData ();
                String v = rsmd.getTableName (1);

		ps.close ();
		conn.close();
		String expected = "";
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    expected = "RSMD_GET"; 
		} 
		assertCondition (v.equals (expected), "Expected: \""+expected+"\" Received: \"" + v + "\" -- added by native 02/03/04 for extended metadata");
		
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception -- added by native 02/03/04 for extended metadata");
            }
        }
    }

/**
getTableName() - Check a valid column after a union has occurred.
**/
    public void Var014()
    {
	if (checkJdbc20 ()) {
	    try {
		Statement s = connection2_.createStatement ();
		ResultSet rs = s.executeQuery ("SELECT C_KEY FROM "
					       + JDRSMDTest.RSMDTEST_GET + " UNION " + "SELECT C_KEY FROM " + JDRSMDTest.RSMDTEST_GET2);
		ResultSetMetaData rsmd = rs.getMetaData ();
		rs.close ();
		String v = rsmd.getTableName (1);
                // No table name is available for the union
		assertCondition ("".equals (v), "Expected: \"RSMD_GET\" Received: \"" + v + "\"  -- added by native 02/05/04 for extended metadata");
	    }
	    catch(Exception e) {
		failed (e, "Unexpected Exception -- added by native 02/05/04 for extended metadata");
	    }
	}
    }

/**
getTableName() - Check a valid column after a union has occurred (Dont' close the cursor yet)..
**/
    public void Var015()
    {
	if (checkJdbc20 ()) {
	    try {
		Statement s = connection2_.createStatement ();
		ResultSet rs = s.executeQuery ("SELECT C_KEY FROM "
					       + JDRSMDTest.RSMDTEST_GET + " UNION " + "SELECT C_KEY FROM " + JDRSMDTest.RSMDTEST_GET2);
		ResultSetMetaData rsmd = rs.getMetaData ();
		String v = rsmd.getTableName (1);
                // No table name is available for the union
		rs.close ();
		assertCondition ("".equals (v), "Expected: \"RSMD_GET\" Received: \"" + v + "\"  -- added by native 10/26/05 for extended metadata");
	    }
	    catch(Exception e) {
		failed (e, "Unexpected Exception -- added by native 10/26/05 for extended metadata");
	    }
	}
    }
    

}



