///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSNext.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSNext.  This tests the following
methods of the JDBC ResultSet class:

<ul>
<li>next()
</ul>
**/
public class JDRSNext
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSNext";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private DatabaseMetaData    dmd_;
    private Statement           statement_;
    private Statement           statement0_;
    private Statement           statement2_;
    private Statement           statement3_;
    private Statement           statement4_;



/**
Constructor.
**/
    public JDRSNext (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSNext",
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
	if (connection_ != null) connection_.close();
        connection_ = testDriver_.getConnection (baseURL_ + ";data truncation=true", userId_, encryptedPassword_);
        dmd_ = connection_.getMetaData ();
        statement0_ = connection_.createStatement ();
             
        // This statement is used for variations that
        // need to test with the max rows set.
        statement4_ = connection_.createStatement ();
	try { 
	    statement4_.setMaxRows (50);
	} catch (Exception e) {
	    System.out.println("Warning:  Exception calling setMaxRows(50) "+e.toString()); 
	} 

        if (isJdbc20 ()) {
    
            // This statement is forward only.
            statement_ = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
    
            // This statement is used for variations that
            // need to test with the max rows set.
	    try { 
		statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
							   ResultSet.CONCUR_READ_ONLY);
	    } catch (Exception e) {
		statement2_ = connection_.createStatement(); 
	    }
	    try { 
		statement2_.setMaxRows (50);
	    } catch (Exception e) {
	    }
            // This statement is used for variations that
            // need to test with a scrollable cursor.
	    try {
	
		statement3_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							   ResultSet.CONCUR_UPDATABLE);
	    } catch (Exception e) {
		statement3_ = connection_.createStatement (); 
		System.out.println("Warning:  Exception creating scrollSensistive/Updatatable cursor: "+e.toString()); 
	    } 

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
            statement_.close ();
            statement2_.close ();
            statement3_.close ();
        }
        statement0_.close ();
        statement4_.close ();
        connection_.close ();
    }



/**
next() - Should return false on an empty result set.
**/
    public void Var001 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " WHERE ID = -1");
            boolean success = rs.next ();
            rs.close ();
            assertCondition (success == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
next() - Should throw an exception on a closed result set.
**/
    public void Var002 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.close ();
            rs.next ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
next() - Should throw an exception on a cancelled statement.
**/
    public void Var003 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            statement0_.cancel ();
            rs.next ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
next() - Should work on a 1 row result set.
**/
    public void Var004 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " WHERE ID = 1");
            boolean success1 = rs.next ();
            int id1 = rs.getInt ("ID");
            boolean success2 = rs.next ();
            rs.close ();
            assertCondition ((success1 == true) && (success2 == false)
                    && (id1 == 1));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
next() - Should work on a large result set.
**/
    public void Var005 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            boolean success = true;
            int count = 0;
            while (rs.next ()) {
                ++count;
                if (rs.getInt (1) != count)
                    success = false;
            }
            rs.close ();
            assertCondition ((success == true) && (count == 99));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
next() - Should work on a "simple" result set.
**/
    public void Var006 ()
    {
        try {
            ResultSet rs = dmd_.getTableTypes ();
            boolean success = true;
            int count = 0;
            while (rs.next ()) {
                ++count;
                if (rs.getString ("TABLE_TYPE") == null)
                    success = false;
            }
            rs.close ();
	    /* Changed the comparision -- @D2C */ 
	    int expectedCount = 3;
	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		// add 1 for MQT
		expectedCount++; 
	    }
	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		// add 1 for ALIAS
		expectedCount++; 
	    }

	    assertCondition ((success == true) && (count == expectedCount), "Changed 08/03/05 by native driver, success="+success+" count = "+count+" sb "+expectedCount );

        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
next() - Should work on a "mapped" result set.
**/
    public void Var007 ()
    {
	String message = ""; 
        try {
            ResultSet rs = dmd_.getColumns (null, "QIWS",
                "QCUSTCDT", "%");
            boolean success = true;
            int count = 0;
            while (rs.next ()) {
                ++count;
		if (! rs.getString ("TABLE_NAME").startsWith ("QCUSTCDT")) {
		    message += "rs.getString(TABLE_NAME)="+rs.getString ("TABLE_NAME")+" should begin with QCUSTCDT"; 
		    success = false;
		}
            }
            rs.close ();
            assertCondition ((success == true) && (count == 11), "Error calling getColumns for  QIWS/QCUSTCDT/% columns  count="+count+" sb 11 "+message);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
next() - Should return true after a beforeFirst().
**/
    public void Var008 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.beforeFirst ();
            boolean success = rs.next ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id == 1));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
next() - Should return true after a first().
**/
    public void Var009 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.first ();
            boolean success = rs.next ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id == 2));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
next() - Should return true after an absolute().
**/
    public void Var010 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.absolute (50);
            boolean success = rs.next ();
            int id = rs.getInt ("ID");
            rs.close ();
            assertCondition ((success == true) && (id == 51));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
next() - Should return true after a relative().
**/
    public void Var011 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.first ();
            rs.relative (75);
            boolean success = rs.next ();
            int id = rs.getInt ("ID");
            rs.close ();
            assertCondition ((success == true) && (id == 77));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
next() - Should return true after a previous().
**/
    public void Var012 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.absolute (75);
            rs.previous ();
            boolean success = rs.next ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id == 75));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
next() - Should return false after a last().
**/
    public void Var013 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.last ();
            boolean success = rs.next ();
            rs.close ();
            assertCondition (success == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
next() - Should return false after an afterLast().
**/
    public void Var014 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.afterLast ();
            boolean success = rs.next ();
            rs.close ();
            assertCondition (success == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
next() - Should return true after a moveToInsertRow().
**/
    public void Var015 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.next ();
            rs.moveToInsertRow ();
            boolean success = rs.next ();
            int id1 = rs.getInt ("ID");
            rs.close ();
            assertCondition ((success == true) && (id1 == 2));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
next() - Should return true after a moveToInsertRow(),
then moveToCurrentRow().
**/
    public void Var016 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.next ();
            rs.moveToInsertRow ();
            rs.moveToCurrentRow ();
            boolean success = rs.next ();
            int id1 = rs.getInt ("ID");
            rs.close ();
            assertCondition ((success == true) && (id1 == 2));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
next() - Should clear any warnings.
**/
    public void Var017 ()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s;
	    try { 
	    s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
	    } catch(Exception e) {
		s = connection_.createStatement(); 
	    }
            ResultSet rs = s.executeQuery ("SELECT C_KEY,C_CHAR_50 FROM " + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FLOAT");

            // Force a warning (data truncation).
            rs.getBigDecimal("C_CHAR_50", 0);

            SQLWarning before = rs.getWarnings ();
            rs.next ();
            SQLWarning after = rs.getWarnings ();
            rs.close ();
            s.close ();
            assertCondition ((before != null) && (after == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
next() - Set max rows and fetch the rows using only next().
Max rows should be honored.
**/
    public void Var018 ()
    {
        try {
            ResultSet rs = statement4_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            boolean success = true;
            int count = 0;
            while (rs.next ())
                ++count;
            rs.close ();
            assertCondition ((success == true) && (count == 50));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
next() - Set max rows and fetch the rows using
absolute(+) and next().  This is the case where we can
always keep track of the row number.  Max rows should be honored.
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            int count = 0;
            rs.absolute (45);
            while (rs.next ())
                ++count;
            rs.close ();
            assertCondition (count == 5);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
next() - Set max rows and fetch the rows using
absolute(-) and next().  This is the case where we lose
track of the row number.  Max rows should not be honored.

Max rows should always be honored and the native JDBC driver
will do so.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) 
        {
        try 
        {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            boolean success = true;
            int count = 0;
            rs.absolute (-55);
            while (rs.next ())
                ++count;
            rs.close ();

            assertCondition ((success == true) && (count == 50));
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
next() - Update the rows using next().
**/
    public void Var021 ()
    {
        try {
            // Update each value using next().
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE OF VALUE");
            PreparedStatement ps = connection_.prepareStatement ("UPDATE "
                + JDRSTest.RSTEST_POS + " SET VALUE = 'NEXT' WHERE CURRENT OF "
                + rs.getCursorName ());
            while (rs.next ())
                ps.execute ();
            rs.close ();
            ps.close ();

            // Go through the result set again as a check.
            ResultSet rs2 = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            boolean success = true;
            int count = 0;
            while (rs2.next ()) {
                ++count;
                if (! rs2.getString ("VALUE").equals ("NEXT"))
                    success = false;
            }
            rs2.close ();

            assertCondition ((success == true) && (count == 99));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



// TEST NOTE: It would be nice to verify that next() implicity
//            closes a previously retrieved InputStream.  However
//            it is not obvious how to check that an InputStream
//            has been closed.


}



