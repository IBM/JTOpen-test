///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDIsDefinitelyWritable.java
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
// File Name:    JDRSMDIsDefinitelyWritable.java
//
// Classes:      JDRSMDIsDefinitelyWritable
//
////////////////////////////////////////////////////////////////////////

package test.JD.RSMD;

import com.ibm.as400.access.AS400;

import test.JDRSMDTest;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSMDIsDefinitelyWritable.  This tests the following method
of the JDBC ResultSetMetaData class:

<ul>
<li>isDefinitelyWritable()
</ul>
**/
public class JDRSMDIsDefinitelyWritable
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMDIsDefinitelyWritable";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSMDTest.main(newArgs); 
   }

    private static String TABLE               = JDRSMDTest.COLLECTION
                                                    + ".JDRSMDDW";



    // Private data.
    private Statement statement_    = null;
    private ResultSet rs_           = null;
    private ResultSetMetaData rsmd_ = null;




/**
Constructor.
**/
    public JDRSMDIsDefinitelyWritable (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSMDIsDefinitelyWritable",
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
        TABLE               = JDRSMDTest.COLLECTION  + ".JDRSMDDW";

        // SQL400 - driver neutral...
        String url = baseURL_
        // String url = "jdbc:as400://" + systemObject_.getSystemName()
            
            ;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

        // Get a read only result set from a query.
        statement_ = connection_.createStatement ();
        initTable(statement_,  TABLE,  " (COL1 INTEGER)");
        connection_.commit(); // for xa
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

        cleanupTable(statement_, TABLE);
        connection_.commit(); // for xa

        statement_.close ();
        connection_.close ();
    }



/**
isDefinitelyWritable() - Check column -1.  Should throw an exception.
**/
    public void Var001()
    {
        try {
            rsmd_.isDefinitelyWritable (-1);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isDefinitelyWritable() - Check column 0.  Should throw an exception.
**/
    public void Var002()
    {
        try {
            rsmd_.isDefinitelyWritable (0);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isDefinitelyWritable() - Check a column greater than the max.
Should throw an exception.
**/
    public void Var003()
    {
        try {
            rsmd_.isDefinitelyWritable (3);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
isDefinitelyWritable() - Check a column with an updatable query result set.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            try {
                // Get an updatable result set from a query.
                Statement statement;
		try { 
		    statement = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							     ResultSet.CONCUR_UPDATABLE);
		} catch (Exception e) {
		    statement = connection_.createStatement (); 
		} 
                ResultSet rs = statement.executeQuery ("SELECT * FROM " + TABLE + " FOR UPDATE");
                ResultSetMetaData rsmd = rs.getMetaData ();
                boolean s = rsmd.isDefinitelyWritable (1);
                rs.close ();
                statement.close ();
                assertCondition (s == false, "got "+s+" sb false for UPDATE select");
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isDefinitelyWritable() - Check a column with a read only query result set.
**/
    public void Var005()
    {
        try {
            boolean s = rsmd_.isDefinitelyWritable (1);
            assertCondition (s == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isDefinitelyWritable() - Check a column with a catalog result set.
**/
    public void Var006()
    {
        try {
            // Get a catalog result set.
            DatabaseMetaData dmd = connection_.getMetaData ();
            ResultSet rs = dmd.getTableTypes ();
            ResultSetMetaData rsmd = rs.getMetaData ();
            boolean s = rsmd.isDefinitelyWritable (1);
            rs.close ();
            assertCondition (s == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isDefinitelyWritable() - Check when the result set is closed.
**/
    public void Var007()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " + TABLE);
            ResultSetMetaData rsmd = rs.getMetaData ();
            rs.close ();
            boolean v = rsmd.isDefinitelyWritable (1);
            s.close ();
            assertCondition (v == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
isDefinitelyWritable() - Check when the meta data comes from a prepared statement.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
            try {
                PreparedStatement ps;
		try { 
		    ps = connection_.prepareStatement ("SELECT * FROM " + TABLE,
						       ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException ex) {
		    ps = connection_.prepareStatement ("SELECT * FROM " + TABLE); 
		} 
                ResultSetMetaData rsmd = ps.getMetaData ();
                boolean v = rsmd.isDefinitelyWritable (1);
                ps.close ();
                assertCondition (v == false);
            }
            catch(Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



}



