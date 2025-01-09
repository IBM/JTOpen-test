///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMoveToCurrentRow.java
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
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestcase;



/**
Testcase JDRSMoveToCurrentRow.  This tests the following
methods of the JDBC ResultSet class:

<ul>
<li>moveToCurrentRow()
</ul>
**/
public class JDRSMoveToCurrentRow
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMoveToCurrentRow";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private Statement           statement_;
    private Statement           statement2_;



/**
Constructor.
**/
    public JDRSMoveToCurrentRow (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSMoveToCurrentRow",
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
    
            // This statement is forward only.
            statement_ = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
    
            // This statement is used for variations that
            // need to test with a scrollable cursor.
            statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
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
            connection_.close ();
        }
    }



/**
moveToCurrentRow() - Should throw an exception on a closed result set.
**/
    public void Var001 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.next ();
            rs.close ();
            rs.moveToCurrentRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
moveToCurrentRow() - Should throw an exception on a cancelled statement.
**/
    public void Var002 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.next ();
            statement2_.cancel ();
            rs.moveToCurrentRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
moveToCurrentRow() - Should throw an exception when the result set
is not scrollable.
**/
    public void Var003 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " WHERE ID = 1");
            rs.next ();
            rs.moveToCurrentRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
moveToCurrentRow() - Should have no effect after a beforeFirst().
**/
    public void Var004 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.beforeFirst ();
            rs.moveToCurrentRow ();
            boolean success = rs.isBeforeFirst ();
            rs.close ();
            assertCondition (success == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
moveToCurrentRow() - Should have no effect after a first().
**/
    public void Var005 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.first ();
            rs.moveToCurrentRow ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
moveToCurrentRow() - Should have no effect after an absolute().
**/
    public void Var006 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.absolute (50);
            rs.moveToCurrentRow ();
            int id = rs.getInt ("ID");
            rs.close ();
            assertCondition (id == 50);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
moveToCurrentRow() - Should have no effect after a relative().
**/
    public void Var007 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.first ();
            rs.relative (75);
            rs.moveToCurrentRow ();
            int id = rs.getInt ("ID");
            rs.close ();
            assertCondition (id == 76);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
moveToCurrentRow() - Should have no effect after a previous().
**/
    public void Var008 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.absolute (75);
            rs.previous ();
            rs.moveToCurrentRow ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == 74);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
moveToCurrentRow() - Should have no effect after a last().
**/
    public void Var009 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.last ();
            rs.moveToCurrentRow ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == 99);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
moveToCurrentRow() - Should have no effect after an afterLast().
**/
    public void Var010 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS);
            rs.afterLast ();
            rs.moveToCurrentRow ();
            boolean success = rs.isAfterLast ();
            rs.close ();
            assertCondition (success == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
moveToCurrentRow() - Should return to the current row after a 
moveToInsertRow(), when the current row is positioned using beforeFirst().
**/
    public void Var011 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.beforeFirst ();
            rs.moveToInsertRow ();
            rs.moveToCurrentRow ();
            boolean success = rs.isBeforeFirst ();
            rs.close ();
            assertCondition (success == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
moveToCurrentRow() - Should return to the current row after a 
moveToInsertRow(), when the current row is positioned using first().
**/
    public void Var012 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.first ();
            rs.moveToInsertRow ();
            rs.moveToCurrentRow ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
moveToCurrentRow() - Should return to the current row after a 
moveToInsertRow(), when the current row is positioned using absolute().
**/
    public void Var013 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.absolute (-38);
            rs.moveToInsertRow ();
            rs.moveToCurrentRow ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == 62);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
moveToCurrentRow() - Should return to the current row after a 
moveToInsertRow(), when the current row is positioned using relative().
**/
    public void Var014 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.next ();
            rs.relative (38);
            rs.moveToInsertRow ();
            rs.moveToCurrentRow ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == 39);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
moveToCurrentRow() - Should return to the current row after a 
moveToInsertRow(), when the current row is positioned using next().
**/
    public void Var015 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.next ();
            rs.moveToInsertRow ();
            rs.moveToCurrentRow ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
moveToCurrentRow() - Should return to the current row after a 
moveToInsertRow(), when the current row is positioned using previous().
**/
    public void Var016 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.absolute (45);
            rs.previous ();
            rs.moveToInsertRow ();
            rs.moveToCurrentRow ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == 44);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
moveToCurrentRow() - Should return to the current row after a 
moveToInsertRow(), when the current row is positioned using last().
**/
    public void Var017 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.last ();
            rs.moveToInsertRow ();
            rs.moveToCurrentRow ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == 99);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
moveToCurrentRow() - Should return to the current row after a 
moveToInsertRow(), when the current row is positioned using afterLast().
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.afterLast ();
            rs.moveToInsertRow ();
            rs.moveToCurrentRow ();
            boolean success = rs.isAfterLast ();
            rs.close ();
            assertCondition (success == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
moveToCurrentRow() - Should have no effect after a moveToInsertRow(),
then moveToCurrentRow() (i.e., a superfluous moveToCurrentRow()).
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
        try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.absolute (77);
            rs.moveToInsertRow ();
            rs.moveToCurrentRow ();
            rs.moveToCurrentRow ();
            int id1 = rs.getInt ("ID");
            rs.close ();
            assertCondition (id1 == 77);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
moveToCurrentRow() - Should clear any warnings.
**/
    @SuppressWarnings("deprecation")
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery ("SELECT C_KEY,C_CHAR_50 FROM " + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FLOAT");

            // Force a warning (data truncation).
            rs.getBigDecimal("C_CHAR_50", 0);

            SQLWarning before = rs.getWarnings ();
            rs.moveToCurrentRow ();
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



// TEST NOTE: It would be nice to verify that moveToCurrentRow() implicity
//            closes a previously retrieved InputStream.  However
//            it is not obvious how to check that an InputStream
//            has been closed.


}



