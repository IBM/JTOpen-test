///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDIsReadOnly.java
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
// File Name:    JDRSMDIsReadOnly.java
//
// Classes:      JDRSMDIsReadOnly
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSMDIsReadOnly.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>isReadOnly()
</ul>
**/
public class JDRSMDIsReadOnly
extends JDTestcase {

    private static String TABLE               = JDRSMDTest.COLLECTION
                                                + ".JDRSMDRO";



    // Private data.
    private Connection connection2_  = null;      //@C1A
    private Statement statement_     = null;
    private Statement statement2_    = null;      //@C1A
    private ResultSet rs_            = null;
    private ResultSet rs2_           = null;      //@C1A
    private ResultSetMetaData rsmd_  = null;
    private ResultSetMetaData rsmd2_ = null;      //@C1A



/**
Constructor.
**/
    public JDRSMDIsReadOnly (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
    {
        super (systemObject, "JDRSMDIsReadOnly",
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
        TABLE               = JDRSMDTest.COLLECTION + ".JDRSMDRO";

        // SQL400 - driver neutral...
        String url = baseURL_
                     // String url = "jdbc:as400://" + systemObject_.getSystemName()
                     
                     ;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

        // Get a read only result set from a query.
        statement_ = connection_.createStatement ();

        initTable(statement_,  TABLE,  " (COL1 INTEGER)");
        rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE);
        rsmd_ = rs_.getMetaData ();

        String url2 = baseURL_                                //@C1A
                      
                      + ";extended metadata=true";            //@C1A
        connection2_ = testDriver_.getConnection (url2,systemObject_.getUserId(), encryptedPassword_);      //@C1A
        statement2_ = connection_.createStatement ();         //@C1A   
        rs2_ = statement2_.executeQuery ("SELECT * FROM " + TABLE);   //@C1A
        rsmd2_ = rs2_.getMetaData ();                         //@C1A
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
	if (rs_ != null) { 
	    rs_.close ();
	}
	if (rs2_ != null) { 
	    rs2_.close();           //@C1A
	}
	if (statement2_ != null) { 
	    statement2_.close();    //@C1A
	}
	// Close the connection here.  If anything failed, it may  
        // still have the cursor open, will cause the drop of the table to fail @D1M
	if (connection2_ != null) { 
	    connection2_.close ();  //@C1A@D1M
	}
	if (statement_ != null) { 
	    cleanupTable(statement_,  TABLE);

	    statement_.close ();
	}
	if (connection_ != null) { 
	    connection_.close ();
	}

    }


  protected void cleanupConnections() throws Exception {
    // Close the connection here. If anything failed, it may
    // still have the cursor open, will cause the drop of the table to fail @D1M
    if (connection2_ != null) {
      connection2_.close(); // @C1A@D1M
    }
    if (connection_ != null) {
      connection_.close();
    }

  }

/**
isReadOnly() - Check column -1.  Should throw an exception.
**/
    public void Var001()
    {
        try {
            rsmd_.isReadOnly (-1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isReadOnly() - Check column 0.  Should throw an exception.
**/
    public void Var002()
    {
        try {
            rsmd_.isReadOnly (0);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isReadOnly() - Check a column greater than the max.
Should throw an exception.
**/
    public void Var003()
    {
        try {
            rsmd_.isReadOnly (3);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isReadOnly() - Check a column with an updatable query result set.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            try {
                // Get an updatable result set from a query.
                Statement statement;
		statement = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                                   ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = statement.executeQuery ("SELECT * FROM " + TABLE + " FOR UPDATE");
                ResultSetMetaData rsmd = rs.getMetaData ();
                boolean s = rsmd.isReadOnly (1);
                rs.close ();
                statement.close ();
                assertCondition (s == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isReadOnly() - Check a column with a read only query result set.
**/
    public void Var005()
    {
        try {
            boolean s = rsmd_.isReadOnly (1);
            assertCondition (s == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isReadOnly() - Check a column with a catalog result set.
**/
    public void Var006()
    {
        try {
            // Get a catalog result set.
            DatabaseMetaData dmd = connection_.getMetaData ();
            ResultSet rs = dmd.getTableTypes ();
            ResultSetMetaData rsmd = rs.getMetaData ();
            boolean s = rsmd.isReadOnly (1);
            rs.close ();            
            assertCondition (s == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isReadOnly() - Check when the result set is closed.

SQL400 - Perhaps this is a problem but we will need to deal with but 
         the Toolbox frees all their resources on the close of the
         result set and free them on the close of the statement.
         
         This means that we need to add the line to close the
         statement for cleanup to work correctly.
**/
    public void Var007()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            boolean v = rsmd.isReadOnly (1);
            if (getDriver() == JDTestDriver.DRIVER_NATIVE) // @B1A
                s.close();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isReadOnly() - Check when the meta data comes from a prepared statement.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection_.prepareStatement ("SELECT * FROM " + TABLE);
                ResultSetMetaData rsmd = ps.getMetaData ();
                boolean v = rsmd.isReadOnly (1);
                ps.close ();
                assertCondition (v == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


//@C1A New testcases to test "extended metadata" property.


/**
isReadOnly() - Check column -1.  Should throw an exception.
**/
    public void Var009()
    {
        try {
            rsmd2_.isReadOnly (-1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isReadOnly() - Check column 0.  Should throw an exception.
**/
    public void Var010()
    {
        try {
            rsmd2_.isReadOnly (0);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isReadOnly() - Check a column greater than the max.
Should throw an exception.
**/
    public void Var011()
    {
        try {
            rsmd2_.isReadOnly (3);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isReadOnly() - Check a column with an updatable query result set.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            try {
                // Get an updatable result set from a query.
                Statement statement = connection2_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                                    ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = statement.executeQuery ("SELECT * FROM " + TABLE + " FOR UPDATE");
                ResultSetMetaData rsmd = rs.getMetaData ();
                boolean s = rsmd.isReadOnly (1);
                rs.close ();
                statement.close ();
                assertCondition (s == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isReadOnly() - Check a column with a read only query result set.
**/
    public void Var013()
    {
        try {
            boolean s = rsmd2_.isReadOnly (1);
            assertCondition (s == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isReadOnly() - Check a column with a catalog result set.
**/
    public void Var014()
    {
        try {
            // Get a catalog result set.
            DatabaseMetaData dmd = connection2_.getMetaData ();
            ResultSet rs = dmd.getTableTypes ();
            ResultSetMetaData rsmd = rs.getMetaData ();
            boolean s = rsmd.isReadOnly (1);
            rs.close ();            
            assertCondition (s == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isReadOnly() - Check when the result set is closed.

SQL400 - Perhaps this is a problem but we will need to deal with but 
         the Toolbox frees all their resources on the close of the
         result set and free them on the close of the statement.
         
         This means that we need to add the line to close the
         statement for cleanup to work correctly.
**/
    public void Var015()
    {
        try {
            Statement s = connection2_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            boolean v = rsmd.isReadOnly (1);
            if (getDriver() == JDTestDriver.DRIVER_NATIVE) // @B1A
                s.close();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isReadOnly() - Check when the meta data comes from a prepared statement.
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps = connection2_.prepareStatement ("SELECT * FROM " + TABLE);
                ResultSetMetaData rsmd = ps.getMetaData ();
                boolean v = rsmd.isReadOnly (1);
                ps.close ();
                assertCondition (v == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
isReadOnly() - Test new support for variation 4.  Check a column with an updatable query result set. Make sure we get an updatable
cursor w/o "FOR UPDATE" as of v5r2/PTF on v5r1.
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
            try {
                // Get an updatable result set from a query.
                Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                                   ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = statement.executeQuery ("SELECT * FROM " + TABLE);
                ResultSetMetaData rsmd = rs.getMetaData ();
                boolean s = rsmd.isReadOnly (1);
                rs.close ();
                statement.close ();
                assertCondition (s == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
isReadOnly() - Make sure same results when "extended metadata=false".
**/
    public void Var018()
    {
        if (checkJdbc20 ()) {
            try {
                Connection connection3 = testDriver_.getConnection(baseURL_
                                          
                                          + ";extended metadata=false",systemObject_.getUserId(),encryptedPassword_);

                // Get an updatable result set from a query.
                Statement statement = connection3.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                                   ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = statement.executeQuery ("SELECT * FROM " + TABLE);
                ResultSetMetaData rsmd = rs.getMetaData ();
                boolean s = rsmd.isReadOnly (1);
                rs.close ();
                statement.close ();
                assertCondition (s == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



}
