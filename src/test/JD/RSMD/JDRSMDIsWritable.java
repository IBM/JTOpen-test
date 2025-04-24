///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDIsWritable.java
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
// File Name:    JDRSMDIsWritable.java
//
// Classes:      JDRSMDIsWritable
//
////////////////////////////////////////////////////////////////////////

package test.JD.RSMD;

import com.ibm.as400.access.AS400;

import test.JDRSMDTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDRSMDIsWritable.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>isWritable()
</ul>
**/
public class JDRSMDIsWritable
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMDIsWritable";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSMDTest.main(newArgs); 
   }

    private static String TABLE               = JDRSMDTest.COLLECTION
                                                    + ".JDRSMIS";



    // Private data.
    private Statement statement_     = null;
    private ResultSet rs_            = null;
    private ResultSetMetaData rsmd_  = null;

    private Connection connection2_  = null;   //@C1A
    private Statement statement2_    = null;   //@C1A
    private ResultSet rs2_           = null;   //@C1A
    private ResultSetMetaData rsmd2_ = null;   //@C1A





/**
Constructor.
**/
    public JDRSMDIsWritable (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSMDIsWritable",
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
        TABLE               = JDRSMDTest.COLLECTION  + ".JDRSMIS";

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

        //@C1A Add to test extended metadata property.
        String url2 = baseURL_
            + ";extended metadata=true";
        connection2_ = testDriver_.getConnection (url2,systemObject_.getUserId(), encryptedPassword_);

        // Get a read only result set from a query.
        statement2_ = connection2_.createStatement ();        
        rs2_ = statement2_.executeQuery ("SELECT * FROM " + TABLE);
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
        rs2_.close ();            //@C1A
        statement2_.close ();     //@C1A
        cleanupTable(statement_, TABLE);
        statement_.close ();
        connection_.close ();
        connection2_.close ();    //@C1A
    }


    protected void cleanupConnections ()
        throws Exception
    {
        connection_.close ();
        connection2_.close ();    //@C1A
    }


/**
isWritable() - Check column -1.  Should throw an exception.
**/
    public void Var001()
    {
        try {
            rsmd_.isWritable (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isWritable() - Check column 0.  Should throw an exception.
**/
    public void Var002()
    {
        try {
            rsmd_.isWritable (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isWritable() - Check a column greater than the max.
Should throw an exception.
**/
    public void Var003()
    {
        try {
            rsmd_.isWritable (3);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isWritable() - Check a column with an updatable query result set.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            try {        
                // Get an updatable result set from a query.
                Statement statement = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = statement.executeQuery ("SELECT * FROM " + TABLE + " FOR UPDATE");
                ResultSetMetaData rsmd = rs.getMetaData ();
                boolean s = rsmd.isWritable (1);
                rs.close ();
                statement.close ();
                assertCondition (s == true);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isWritable() - Check a column with a read only query result set.
**/
    public void Var005()
    {
        try {
            boolean s = rsmd_.isWritable (1);
            assertCondition (s == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isWritable() - Check a column with a catalog result set.
**/
    public void Var006()
    {
        try {        
            // Get a catalog result set.
            DatabaseMetaData dmd = connection_.getMetaData ();
            ResultSet rs = dmd.getTableTypes ();
            ResultSetMetaData rsmd = rs.getMetaData ();
            boolean s = rsmd.isWritable (1);
            rs.close ();
            assertCondition (s == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isWritable() - Check when the result set is closed.
**/
    public void Var007()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            boolean v = rsmd.isWritable (1);
            s.close ();
            assertCondition (v == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isWritable() - Check when the meta data comes from a prepared statement.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps;
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    ps = connection_.prepareStatement ("SELECT * FROM " + TABLE); 
		} else { 
		    ps = connection_.prepareStatement ("SELECT * FROM " + TABLE,
						       ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_READ_ONLY);
		}
                ResultSetMetaData rsmd = ps.getMetaData ();
                boolean v = rsmd.isWritable (1);
                ps.close ();
                assertCondition (v == false);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

    
//@C1A Test new "extended metadata" property.


/**
isWritable() - Check column -1.  Should throw an exception.
**/
    public void Var009()
    {
        try {
            rsmd2_.isWritable (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isWritable() - Check column 0.  Should throw an exception.
**/
    public void Var010()
    {
        try {
            rsmd2_.isWritable (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isWritable() - Check a column greater than the max.
Should throw an exception.
**/
    public void Var011()
    {
        try {
            rsmd2_.isWritable (3);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isWritable() - Check a column with an updatable query result set.
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
                boolean s = rsmd.isWritable (1);
                rs.close ();
                statement.close ();
                assertCondition (s == true);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isWritable() - Check a column with a read only query result set.
**/
    public void Var013()
    {
        try {
            boolean s = rsmd2_.isWritable (1);
            assertCondition (s == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isWritable() - Check a column with a catalog result set.
**/
    public void Var014()
    {
        try {        
            // Get a catalog result set.
            DatabaseMetaData dmd = connection2_.getMetaData ();
            ResultSet rs = dmd.getTableTypes ();
            ResultSetMetaData rsmd = rs.getMetaData ();
            boolean s = rsmd.isWritable (1);
            rs.close ();
            assertCondition (s == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isWritable() - Check when the result set is closed.
**/
    public void Var015()
    {
        try {
            Statement s = connection2_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            boolean v = rsmd.isWritable (1);
            s.close ();
            assertCondition (v == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isWritable() - Check when the meta data comes from a prepared statement.
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps;
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    ps = connection_.prepareStatement ("SELECT * FROM " + TABLE); 
		} else { 
		
		    ps = connection2_.prepareStatement ("SELECT * FROM " + TABLE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
		}
                ResultSetMetaData rsmd = ps.getMetaData ();
                boolean v = rsmd.isWritable (1);
                ps.close ();
                assertCondition (v == false);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
isWritable() - Check that we still get an updateable cursor without FOR UPDATE as of v5r2
servers, or v5r1 servers with the PTF.
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
            try {        
                // Get an updatable result set from a query.
                Statement statement = connection2_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = statement.executeQuery ("SELECT * FROM " + TABLE);
                ResultSetMetaData rsmd = rs.getMetaData ();
                boolean s = rsmd.isWritable (1);
                rs.close ();
                statement.close ();
                assertCondition (s == true);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


    /**
isWritable() - Check that we don't get new data if "extended metadata=false".
**/
    public void Var018()
    {
        if (checkJdbc20 ()) {
            try {
                Connection connection3 = testDriver_.getConnection(baseURL_
                                                                   + ";extended metadata=false"); 
                Statement statement = connection3.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = statement.executeQuery ("SELECT * FROM " + TABLE);
                ResultSetMetaData rsmd = rs.getMetaData ();
                boolean s = rsmd.isWritable (1);
                rs.close ();
                statement.close ();
                assertCondition (s == true);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }






}



