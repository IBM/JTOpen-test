///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSWasNull.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.RS;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;



/**
Testcase JDRSWasNull.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>wasNull()
</ul>
**/
public class JDRSWasNull
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSWasNull";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private Statement           statement_;
    private Statement           statement0_;
    private Statement           statement1_;
    private ResultSet           rs_;



/**
Constructor.
**/
    public JDRSWasNull (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSWasNull",
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
        String url = baseURL_;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(),encryptedPassword_, "JDRSWasNull");
	try { 
	    connection_.setAutoCommit(false); // @E1A
	} catch (Exception e) {
	    output_.println("Warning:  Unable to setAutoCommit(false)");
	    e.printStackTrace();  
	} 
        statement0_ = connection_.createStatement ();
        statement1_ = connection_.createStatement ();

        if (isJdbc20 ()) {
	    if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		statement_ = connection_.createStatement (); 
	    } else { 
		statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							  ResultSet.CONCUR_UPDATABLE);
	    }
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_GET
                + " (C_KEY) VALUES ('DUMMY_ROW')");
            rs_ = statement_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET + " FOR UPDATE");
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        if (isJdbc20 ()) {
            rs_.close ();
            statement_.close ();
        }
        statement1_.close ();
        statement0_.close ();
        connection_.commit(); // @E1A
        connection_.close ();
    }



/**
wasNull() - Should throw exception when the result set is
closed.
**/
    public void Var001()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.close ();
            rs.wasNull ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
wasNull() - Should return false when cursor is not pointing
to a row.
**/
    public void Var002()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            boolean v = rs.wasNull ();
            rs.close ();
            s.close ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Should return false when a column has not been read.
**/
    public void Var003()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            boolean v = rs.wasNull ();
            rs.close ();
            s.close ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from an invalid column.  Check that wasNull is not
cleared by this when the previous read was not NULL.
**/
    public void Var004()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            rs.getInt ("C_INTEGER");
            try {
                rs.getInt ("BOGUS");
            }
            catch (SQLException e) {
                // Ignore.
            }
            assertCondition (rs.wasNull () == false);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from an invalid column.  Check that wasNull is not
cleared by this when the previous read was NULL.
**/
    public void Var005()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            rs.getInt ("C_INTEGER");
            try {
                rs.getInt ("BOGUS");
            }
            catch (SQLException e) {
                // Ignore.
            }
            assertCondition (rs.wasNull () == true);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get a value with an invalid get function.  Check that wasNull
still works even though no value was retrieved, when the value
is not NULL.
**/
    public void Var006()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            try {
                rs.getTimestamp ("C_INTEGER");
            }
            catch (SQLException e) {
                // Ignore.
            }
            assertCondition (rs.wasNull () == false);
        }
        catch(Exception e) {    
            failed (e, "Unexpected Exception");
        }
    }


/**
wasNull() - Get a value with an invalid get function.  Check that wasNull
still works even though no value was retrieved, when the value
is NULL.
**/
    public void Var007()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            try {
                rs.getTimestamp ("C_INTEGER");
            }
            catch (SQLException e) {
                // Ignore.
            }
            assertCondition (rs.wasNull () == true, "wasNull returned false when not value returned");
        }
        catch(Exception e) {    
            failed (e, "Unexpected Exception");
        }
    }


/**
wasNull() - Should work when an update is pending, and the
column is not null.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateString ("C_VARCHAR_50", "Ninja");
            rs_.getString ("C_VARCHAR_50");
            boolean v = rs_.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
wasNull() - Should work when an update is pending, and the
column is null.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateNull ("C_VARCHAR_50");
            rs_.getString ("C_VARCHAR_50");
            boolean v = rs_.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
wasNull() - Should work when an update has been done,
when the column is not null.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateString ("C_VARCHAR_50", "Money");
            rs_.updateRow ();
            rs_.getString ("C_VARCHAR_50");
            boolean v = rs_.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
wasNull() - Should work when an update has been done,
when the column is null.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateNull ("C_VARCHAR_50");
            rs_.updateRow ();
            rs_.getString ("C_VARCHAR_50");
            boolean v = rs_.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
wasNull() - Should work when the current row is the insert
row, when an insert is pending, when the column is not null.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateString ("C_VARCHAR_50", "Slipped");
            rs_.getString ("C_VARCHAR_50");
            boolean v = rs_.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
wasNull() - Should work when the current row is the insert
row, when an insert is pending, when the column is null.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateNull ("C_VARCHAR_50");
            rs_.getString ("C_VARCHAR_50");
            boolean v = rs_.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
wasNull() - Should work when the current row is the insert
row, when an insert has been done, when the column is not null.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateString ("C_VARCHAR_50", "Koala");
            rs_.insertRow ();
            rs_.getString ("C_VARCHAR_50");
            boolean v = rs_.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
wasNull() - Should work when the current row is the insert
row, when an insert has been done, when the column is null.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateNull ("C_VARCHAR_50");
            rs_.insertRow ();
            rs_.getString ("C_VARCHAR_50");
            boolean v = rs_.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
wasNull() - Should return false on a deleted row.
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "DUMMY_ROW");
            rs_.deleteRow ();
            boolean v = rs_.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


/**
wasNull() - Get from a SMALLINT, when the column is not null.
**/
    public void Var017 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            rs.getShort ("C_SMALLINT");
            boolean v = rs.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a SMALLINT, when the column is null.
**/
    public void Var018 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            rs.getShort ("C_SMALLINT");
            boolean v = rs.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a INTEGER, when the column is not null.
**/
    public void Var019 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            rs.getInt ("C_INTEGER");
            boolean v = rs.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a INTEGER, when the column is null.
**/
    public void Var020 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            rs.getInt ("C_INTEGER");
            boolean v = rs.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a REAL, when the column is not null.
**/
    public void Var021 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            rs.getFloat ("C_REAL");
            boolean v = rs.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a REAL, when the column is null.
**/
    public void Var022 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            rs.getFloat ("C_REAL");
            boolean v = rs.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a FLOAT, when the column is not null.
**/
    public void Var023 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            rs.getFloat ("C_FLOAT");
            boolean v = rs.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a FLOAT, when the column is null.
**/
    public void Var024 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            rs.getFloat ("C_FLOAT");
            boolean v = rs.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a DOUBLE, when the column is not null.
**/
    public void Var025 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            rs.getDouble ("C_DOUBLE");
            boolean v = rs.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a DOUBLE, when the column is null.
**/
    public void Var026 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            rs.getDouble ("C_DOUBLE");
            boolean v = rs.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a DECIMAL, when the column is not null.
**/
    public void Var027 ()
    {
    	assertCondition(true);
    	/* removed deprecated test 10/24/2011 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            rs.getBigDecimal ("C_DECIMAL_50", 0);
            boolean v = rs.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */ 
    }



/**
wasNull() - Get from a DECIMAL, when the column is null.
**/
    public void Var028 ()
    {
        try {
        	assertCondition(true);
        	/* removed deprecated test 10/24/2011 
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            rs.getBigDecimal ("C_DECIMAL_50", 0);
            boolean v = rs.wasNull ();
            assertCondition (v == true);
            */ 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a NUMERIC, when the column is not null.
**/
    public void Var029 ()
    {
        try {
        	assertCondition(true);
        	/* removed deprecated test 10/24/2011 
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            rs.getBigDecimal ("C_NUMERIC_105", 0);
            boolean v = rs.wasNull ();
            assertCondition (v == false);
            */ 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a NUMERIC, when the column is null.
**/
    public void Var030 ()
    {
        try {
        	assertCondition(true);
        	/* removed deprecated test 10/24/2011 
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            rs.getBigDecimal ("C_NUMERIC_105", 0);
            boolean v = rs.wasNull ();
            assertCondition (v == true);
            */ 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a CHAR, when the column is not null.
**/
    public void Var031 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            rs.getString ("C_CHAR_50");
            boolean v = rs.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a CHAR, when the column is null.
**/
    public void Var032 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_NULL");
            rs.getString ("C_CHAR_50");
            boolean v = rs.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a VARCHAR, when the column is not null.
**/
    public void Var033 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            rs.getString ("C_VARCHAR_50");
            boolean v = rs.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a VARCHAR, when the column is null.
**/
    public void Var034 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_NULL");
            rs.getString ("C_VARCHAR_50");
            boolean v = rs.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a CLOB, when the column is not null.
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                rs.getClob ("C_CLOB");
                boolean v = rs.wasNull ();
                assertCondition (v == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }



/**
wasNull() - Get from a CLOB, when the column is null.
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_NULL");
                rs.getClob ("C_CLOB");
                boolean v = rs.wasNull ();
                assertCondition (v == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }



/**
wasNull() - Get from a DBCLOB, when the column is not null.
**/
    public void Var037 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                rs.getClob ("C_DBCLOB");
                boolean v = rs.wasNull ();
                assertCondition (v == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }



/**
wasNull() - Get from a DBCLOB, when the column is null.
**/
    public void Var038 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_NULL");
                rs.getClob ("C_DBCLOB");
                boolean v = rs.wasNull ();
                assertCondition (v == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }



/**
wasNull() - Get from a BINARY, when the column is not null.
**/
    public void Var039 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            rs.getBytes ("C_BINARY_20");
            boolean v = rs.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a BINARY, when the column is null.
**/
    public void Var040 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NULL");
            rs.getBytes ("C_BINARY_20");
            boolean v = rs.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a VARBINARY, when the column is not null.
**/
    public void Var041 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            rs.getBytes ("C_VARBINARY_20");
            boolean v = rs.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a VARBINARY, when the column is null.
**/
    public void Var042 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NULL");
            rs.getBytes ("C_VARBINARY_20");
            boolean v = rs.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a BLOB, when the column is not null.
**/
    public void Var043 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                rs.getBlob ("C_BLOB");
                boolean v = rs.wasNull ();
                assertCondition (v == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }



/**
wasNull() - Get from a BLOB, when the column is null.
**/
    public void Var044 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_NULL");
                rs.getBlob ("C_BLOB");
                boolean v = rs.wasNull ();
                assertCondition (v == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }



/**
wasNull() - Get from a DATE, when the column is not null.
**/
    public void Var045 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            rs.getDate ("C_DATE");
            boolean v = rs.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a DATE, when the column is null.
**/
    public void Var046 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_NULL");
            rs.getDate ("C_DATE");
            boolean v = rs.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a TIME, when the column is not null.
**/
    public void Var047 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            rs.getTime ("C_TIME");
            boolean v = rs.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a TIME, when the column is null.
**/
    public void Var048 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_NULL");
            rs.getTime ("C_TIME");
            boolean v = rs.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a TIMESTAMP, when the column is not null.
**/
    public void Var049 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            rs.getTimestamp ("C_TIMESTAMP");
            boolean v = rs.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a TIMESTAMP, when the column is null.
**/
    public void Var050 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_NULL");
            rs.getTimestamp ("C_TIMESTAMP");
            boolean v = rs.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
wasNull() - Get from a DATALINK, when the column is not null.
**/
    public void Var051 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement1_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GETDL);
                JDRSTest.position0 (rs, "LOB_FULL");
                rs.getObject ("C_DATALINK");
                boolean v = rs.wasNull ();
                rs.close();
                assertCondition (v == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
wasNull() - Get from a DATALINK, when the column is null.
**/
    public void Var052 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement1_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GETDL);
                JDRSTest.position0 (rs, "LOB_NULL");
                rs.getObject ("C_DATALINK");
                boolean v = rs.wasNull ();
                rs.close();
                assertCondition (v == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
wasNull() - Get from a DISTINCT, when the column is not null.
**/
    public void Var053 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                rs.getObject ("C_DISTINCT");
                boolean v = rs.wasNull ();
                assertCondition (v == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
wasNull() - Get from a DISTINCT, when the column is null.
**/
    public void Var054 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_NULL");
                rs.getObject ("C_DISTINCT");
                boolean v = rs.wasNull ();
                assertCondition (v == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
wasNull() - Get from a BIGINT, when the column is not null.
**/
    public void Var055()
    {
        if (checkBigintSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            rs.getLong ("C_BIGINT");
            boolean v = rs.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
wasNull() - Get from a BIGINT, when the column is null.
**/
    public void Var056 ()
    {
        if (checkBigintSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            rs.getLong ("C_BIGINT");
            boolean v = rs.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

    
    /**
wasNull() - Get from a BOOLEAN, when the column is not null.
**/
    public void Var057()
    {
        if (checkBigintSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            rs.getBoolean ("C_BOOLEAN");
            boolean v = rs.wasNull ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
wasNull() - Get from a BIGINT, when the column is null.
**/
    public void Var058 ()
    {
        if (checkBigintSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            rs.getLong ("C_BOOLEAN");
            boolean v = rs.wasNull ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


    public void checkNull(String query) {
      try {
        ResultSet rs = statement0_.executeQuery(query);
        rs.next();
        rs.getString(1);
        boolean v = rs.wasNull();
        assertCondition(v == true, "For query '" + query + "' wasNull returned false");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }

    }
   
    /**
     * wasNull() - Get from a TIMESTAMP, when the column had a data mapping error.
     **/
    public void Var059() {
      checkNull("select TO_DATE (CHAR(9000000 +19000000), 'YYYYMMDD') from sysibm.sysdummy1");
    }

    /**
     * wasNull() - Get from a INTEGER, when the column had a data mapping error.
     **/
    public void Var060() {
      checkNull("select INTEGER(1) / INTEGER(0) from sysibm.sysdummy1");
    }

    /**
     * wasNull() - Get from a CHAR, where truncation occurred.
     **/
    public void Var061() {
      try {
        String query = "select CAST('ABC' AS CHAR(2)) from sysibm.sysdummy1";
        ResultSet rs = statement0_.executeQuery(query);
        rs.next();
        rs.getString(1);
        boolean v = rs.wasNull();
        assertCondition(v == false, "For query '" + query + "' wasNull returned true");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

    /**
     * wasNull() - Get from various types,  when the column had a data mapping error.
     **/
    public void Var062() {   checkNull("select INTEGER('A') from sysibm.sysdummy1");   }
    public void Var063() {   checkNull("select SMALLINT('A') from sysibm.sysdummy1");   }
    public void Var064() {   checkNull("select BIGINT('A') from sysibm.sysdummy1");   }
    public void Var065() {   checkNull("select DECIMAL('A') from sysibm.sysdummy1");   }
    public void Var066() {   checkNull("select cast('A' as numeric(10,2)) from sysibm.sysdummy1");   }
    public void Var067() {   checkNull("select FLOAT('A') from sysibm.sysdummy1");   }
    public void Var068() {   checkNull("select DOUBLE('A') from sysibm.sysdummy1");   }
    public void Var069() {   checkNull("select DECFLOAT('A') from sysibm.sysdummy1");   }
    public void Var070() {   checkNull("select DATE('AA/AA/AA') from sysibm.sysdummy1");   }
    public void Var071() {   checkNull("select TIME('AA/AA/AA') from sysibm.sysdummy1");   }
    public void Var072() {   checkNull("select TIMESTAMP('AAAA/AA/AA') from sysibm.sysdummy1");   }


}



