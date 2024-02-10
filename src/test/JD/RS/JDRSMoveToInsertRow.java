///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMoveToInsertRow.java
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
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSMoveToInsertRow.  This tests the following
methods of the JDBC ResultSet class:

<ul>
<li>moveToInsertRow()
</ul>
**/
public class JDRSMoveToInsertRow
extends JDTestcase
{



    // Private data.
    private DatabaseMetaData    dmd_;
    private Statement           statement_;
    private Statement           statement2_;
    private Statement           statement3_;
    private int                 counter_        = 509;



/**
Constructor.
**/
    public JDRSMoveToInsertRow (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSMoveToInsertRow",
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
        if (isJdbc20 ()) {
            connection_ = testDriver_.getConnection (baseURL_ + ";data truncation=true", userId_, encryptedPassword_);
    
            // We will rollback any changes we make for the benefit
            // of other testcases depending on this table containing
            // the initial set of rows.
            connection_.setAutoCommit (false);
            connection_.setTransactionIsolation (Connection.TRANSACTION_READ_UNCOMMITTED);
    
            dmd_ = connection_.getMetaData ();
    
            // This statement is forward only.
            statement_ = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_UPDATABLE);
    
            // This statement is read only.
            statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
    
            // This statement is used for variations that
            // need to test with a scrollable cursor.
            statement3_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
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
    
            connection_.rollback ();
            connection_.close ();
        }
    }



/**
moveToInsertRow() - Should throw an exception on a closed result set.
**/
    public void Var001 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.next ();
            rs.close ();
            rs.moveToInsertRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
moveToInsertRow() - Should throw an exception on a cancelled statement.
**/
    public void Var002 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.next ();
            statement3_.cancel ();
            rs.moveToInsertRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
moveToInsertRow() - Should throw an exception when the result set
is not scrollable.
**/
    public void Var003 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " WHERE ID = 1 FOR UPDATE");
            rs.next ();
            rs.moveToInsertRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
moveToInsertRow() - Should throw an exception when the result set
is not updatable.
**/
    public void Var004 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " WHERE ID = 1");
            rs.next ();
            rs.moveToInsertRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
moveToInsertRow() - Should work after a beforeFirst().
**/
    public void Var005 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.beforeFirst ();
            rs.moveToInsertRow ();           
            rs.updateInt (1, ++counter_);
            rs.insertRow ();
            rs.last ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == counter_);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
moveToInsertRow() - Should work after a first().
**/
    public void Var006 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.first ();
            rs.moveToInsertRow ();
            rs.updateInt (1, ++counter_);
            rs.insertRow ();
            rs.last ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == counter_);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
moveToInsertRow() - Should work after an absolute().
**/
    public void Var007 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.absolute (-87);
            rs.moveToInsertRow ();
            rs.updateInt (1, ++counter_);
            rs.insertRow ();
            rs.last ();
            int id = rs.getInt ("ID");
            rs.close ();
            assertCondition (id == counter_);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
moveToInsertRow() - Should work after a relative().
**/
    public void Var008 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.first ();
            rs.relative (75);
            rs.moveToInsertRow ();
            rs.updateInt (1, ++counter_);
            rs.insertRow ();
            rs.last ();
            int id = rs.getInt ("ID");
            rs.close ();
            assertCondition (id == counter_);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
moveToInsertRow() - Should work after a previous().
**/
    public void Var009 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.absolute (75);
            rs.previous ();
            rs.moveToInsertRow ();
            rs.updateInt (1, ++counter_);
            rs.insertRow ();
            rs.last ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == counter_);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
moveToInsertRow() - Should work after a last().
**/
    public void Var010 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.last ();
            rs.moveToInsertRow ();
            rs.updateInt (1, ++counter_);
            rs.insertRow ();
            rs.last ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == counter_);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
moveToInsertRow() - Should have no effect after an afterLast().
**/
    public void Var011 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.afterLast ();
            rs.moveToInsertRow ();
            rs.updateInt (1, ++counter_);
            rs.insertRow ();
            rs.last ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == counter_);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


/**
moveToInsertRow() - Should return back to the insert row a moveToInsertRow(),
then moveToCurrentRow().
**/
    public void Var012 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.absolute (77);
            rs.moveToInsertRow ();
            rs.moveToCurrentRow ();
            rs.moveToInsertRow ();
            rs.updateInt (1, ++counter_);
            rs.insertRow ();
            rs.last ();
            int id1 = rs.getInt ("ID");
            rs.close ();
            assertCondition (id1 == counter_);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
moveToInsertRow() - Should clear any warnings.
**/
    public void Var013 ()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery ("SELECT C_KEY,C_CHAR_50 FROM " + JDRSTest.RSTEST_GET + " FOR UPDATE");
            JDRSTest.position0 (rs, "CHAR_FLOAT");

            // Force a warning (data truncation).
            rs.getBigDecimal("C_CHAR_50", 0);

            SQLWarning before = rs.getWarnings ();
            rs.moveToInsertRow ();
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



// TEST NOTE: It would be nice to verify that moveToInsertRow() implicity
//            closes a previously retrieved InputStream.  However
//            it is not obvious how to check that an InputStream
//            has been closed.


}



