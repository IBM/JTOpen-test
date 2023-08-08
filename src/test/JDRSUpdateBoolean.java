///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateBoolean.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSUpdateBoolean.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateBoolean()
</ul>
**/
public class JDRSUpdateBoolean
extends JDTestcase
{



    // Private data.
    private static final String key_            = "JDRSUpdateBoolean";
    private static String select_         = "SELECT * FROM "
                                                    + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;



/**
Constructor.
**/
    public JDRSUpdateBoolean (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSUpdateBoolean",
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
        select_         = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;

        if (isJdbc20 ()) {
            // SQL400 - driver neutral...
            String url = baseURL_
            // String url = "jdbc:as400://" + systemObject_.getSystemName()
                
                
                + ";data truncation=true";
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
            connection_.setAutoCommit(false); // @C1A
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
    
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
                + " (C_KEY) VALUES ('DUMMY_ROW')");
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
                + " (C_KEY) VALUES ('" + key_ + "')");
    
            rs_ = statement_.executeQuery (select_ + " FOR UPDATE");
            System.out.println("Table is "+JDRSTest.RSTEST_UPDATE);
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
            connection_.commit(); // @C1A
            connection_.close ();
        }
    }



/**
updateBoolean() - Should throw exception when the result set is
closed.
**/
    public void Var001()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
            rs.next ();
            rs.close ();
            rs.updateBoolean ("C_SMALLINT", true);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBoolean() - Should throw exception when the result set is
not updatable.
**/
    public void Var002()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_UPDATE);
            rs.next ();
            rs.updateBoolean ("C_SMALLINT", false);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBoolean() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, null);
            rs_.updateBoolean ("C_SMALLINT", true);
            rs_.updateRow(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBoolean() - Should throw an exception when the column
is an invalid index.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean (100, false);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBoolean() - Should throw an exception when the column
is 0.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean (0, true);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBoolean() - Should throw an exception when the column
is -1.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean (-1, false);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBoolean() - Should work when the column index is valid.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean (2, true);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            boolean v = rs2.getBoolean ("C_SMALLINT");
            rs2.close ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Should throw an exception when the column
name is null.
**/
    public void Var008()
    {
      if (checkJdbc20 ()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC throws null pointer exception when column name is null "); 
        } else { 
          
          try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean (null, false);
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
      }
    }


/**
updateBoolean() - Should throw an exception when the column
name is an empty string.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("", true);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBoolean() - Should throw an exception when the column
name is invalid.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("INVALID", false);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBoolean() - Should work when the column name is valid.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_SMALLINT", false);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            boolean v = rs2.getBoolean ("C_SMALLINT");
            rs2.close ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Should be reflected by get, even if update has
not yet been issued (i.e. update is still pending), when the value
set is true.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_SMALLINT", true);
            assertCondition (rs_.getBoolean ("C_SMALLINT") == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateBoolean() - Should be reflected by get, even if update has
not yet been issued (i.e. update is still pending), when the value
set is false.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_SMALLINT", false);
            assertCondition (rs_.getBoolean ("C_SMALLINT") == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateBoolean() - Should be reflected by get, after update has
been issued, but cursor has not been repositioned, when the 
value set is true.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_SMALLINT", true);
            rs_.updateRow ();
            assertCondition (rs_.getBoolean ("C_SMALLINT") == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateBoolean() - Should be reflected by get, after update has
been issued, but cursor has not been repositioned, when the 
value set is false.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_SMALLINT", false);
            rs_.updateRow ();
            assertCondition (rs_.getBoolean ("C_SMALLINT") == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateBoolean() - Should be reflected by get, after update has
been issued and cursor has been repositioned, when the value is
true.
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_SMALLINT", true);
            rs_.updateRow ();
            rs_.beforeFirst ();
            JDRSTest.position (rs_, key_);
            boolean v = rs_.getBoolean ("C_SMALLINT");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Should be reflected by get, after update has
been issued and cursor has been repositioned, when the value is
false.
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_SMALLINT", false);
            rs_.updateRow ();
            rs_.beforeFirst ();
            JDRSTest.position (rs_, key_);
            boolean v = rs_.getBoolean ("C_SMALLINT");
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Should work when the current row is the insert
row, when the value set is true.
**/
    public void Var018()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateString ("C_KEY", "JDRSUpdateBoolean 1");
            rs_.updateBoolean ("C_SMALLINT", true);
            rs_.insertRow ();
            JDRSTest.position (rs_, "JDRSUpdateBoolean 1");
            assertCondition (rs_.getBoolean ("C_SMALLINT") == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Should work when the current row is the insert
row, when the value set is false.
**/
    public void Var019()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateString ("C_KEY", "JDRSUpdateBoolean 2");
            rs_.updateBoolean ("C_SMALLINT", false);
            rs_.insertRow ();
            JDRSTest.position (rs_, "JDRSUpdateBoolean 2");
            assertCondition (rs_.getBoolean ("C_SMALLINT") == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Should be reflected by get on insert row, even if
insert has not yet been issued (i.e. insert is still pending), when
the value set is true.
**/
    public void Var020()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateBoolean ("C_SMALLINT", true);
            assertCondition (rs_.getBoolean ("C_SMALLINT") == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

           

/**
updateBoolean() - Should be reflected by get on insert row, even if
insert has not yet been issued (i.e. insert is still pending), when
the value set is false.
**/
    public void Var021()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateBoolean ("C_SMALLINT", false);
            assertCondition (rs_.getBoolean ("C_SMALLINT") == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

           

/**
updateBoolean() - Should throw an exception on a deleted row.
**/
    public void Var022()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "DUMMY_ROW");
            rs_.deleteRow ();
            rs_.updateBoolean ("C_SMALLINT", false);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


/**
updateBoolean() - Update a SMALLINT with the value true.
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_SMALLINT", true);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            short v = rs2.getShort ("C_SMALLINT");
            rs2.close ();
            assertCondition (v == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a SMALLINT with the value false.
**/
    public void Var024 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_SMALLINT", false);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            short v = rs2.getShort ("C_SMALLINT");
            rs2.close ();
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a INTEGER, with the value true.
**/
    public void Var025 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_INTEGER", true);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            int v = rs2.getInt ("C_INTEGER");
            rs2.close ();
            assertCondition (v == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a INTEGER, with the value false.
**/
    public void Var026 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_INTEGER", false);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            int v = rs2.getInt ("C_INTEGER");
            rs2.close ();
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a REAL, with the value true.
**/
    public void Var027 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_REAL", true);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_REAL");
            rs2.close ();
            assertCondition (v == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a REAL, with the value false.
**/
    public void Var028 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_REAL", false);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_REAL");
            rs2.close ();
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a FLOAT, with the value true.
**/
    public void Var029 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_FLOAT", true);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_FLOAT");
            rs2.close ();
            assertCondition (v == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a FLOAT, with the value false.
**/
    public void Var030 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_FLOAT", false);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_FLOAT");
            rs2.close ();
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a DOUBLE, with the value true.
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_DOUBLE", true);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            double v = rs2.getDouble ("C_DOUBLE");
            rs2.close ();
            assertCondition (v == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a DOUBLE, with the value false.
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_DOUBLE", false);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            double v = rs2.getDouble ("C_DOUBLE");
            rs2.close ();
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a DECIMAL, with the value true.
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_DECIMAL_105", true);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
            rs2.close ();
            assertCondition (v.intValue() == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a DECIMAL, with the value false.
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_DECIMAL_105", false);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
            rs2.close ();
            assertCondition (v.intValue() == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a NUMERIC, with the value true.
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_NUMERIC_105", true);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
            rs2.close ();
            assertCondition (v.intValue() == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a NUMERIC, with the value false.
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_NUMERIC_105", true);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
            rs2.close ();
            assertCondition (v.intValue() == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a CHAR, with the value true.
**/
    public void Var037 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_CHAR_50", true);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_CHAR_50");
            rs2.close ();
            assertCondition (v.equals ("1                                                 "));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a CHAR, with the value false.
**/
    public void Var038 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_CHAR_50", false);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_CHAR_50");
            rs2.close ();
            assertCondition (v.equals ("0                                                 "));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a VARCHAR, with the value true.
**/
    public void Var039 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_VARCHAR_50", true);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_VARCHAR_50");
            rs2.close ();
            assertCondition (v.equals ("1"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a VARCHAR, with the value false.
**/
    public void Var040 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_VARCHAR_50", false);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_VARCHAR_50");
            rs2.close ();
            assertCondition (v.equals ("0"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBoolean() - Update a BINARY.
**/
    public void Var041 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_BINARY_20", true);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBoolean() - Update a VARBINARY.
**/
    public void Var042 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_VARBINARY_20", false);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBoolean() - Update a CLOB.
**/
    public void Var043 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBoolean ("C_CLOB", true);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateBoolean() - Update a DBCLOB.
**/
    public void Var044 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBoolean ("C_DBCLOB", false);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateBoolean() - Update a BLOB.
**/
    public void Var045 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBoolean ("C_BLOB", true);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateBoolean() - Update a DATE.
**/
    public void Var046 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_DATE", false);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBoolean() - Update a TIME.
**/
    public void Var047 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_TIME", true);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBoolean() - Update a TIMESTAMP.
**/
    public void Var048 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_TIMESTAMP", false);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBoolean() - Update a DATALINK.
**/
    public void Var049 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            // We do not test updating datalinks, since it is not 
            // possible to open a updatable cursor/result set with
            // a datalink column.
            notApplicable("DATALINK update not supported.");
            /*
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBoolean ("C_DATALINK", true);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
            */
        }
        }
    }



/**
updateBoolean() - Update a DISTINCT.
**/
    public void Var050 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBoolean ("C_DISTINCT", true);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_DISTINCT");
                rs2.close ();
                assertCondition (v.equals ("1        "));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }



/**
updateBoolean() - Update a BIGINT, with the value true.
**/
    public void Var051()
    {
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_BIGINT", true);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            long v = rs2.getLong ("C_BIGINT");
            rs2.close ();
            assertCondition (v == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
        }
    }



/**
updateBoolean() - Update a BIGINT, with the value false.
**/
    public void Var052()
    {
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_BIGINT", false);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            long v = rs2.getLong ("C_BIGINT");
            rs2.close ();
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
        }
    }

    /**
    updateBoolean() - Update a DFP16, with the value true.
    **/
        public void Var053 ()
        {
          if (checkDecFloatSupport ()) {
            try {
              Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                  ResultSet.CONCUR_UPDATABLE);
              ResultSet rs = s.executeQuery ("SELECT * FROM "
                  + JDRSTest.RSTEST_DFP16+" FOR UPDATE ");
              rs.next(); 
              rs.updateBoolean (1, true);
              rs.updateRow ();
              ResultSet rs2 = statement2_.executeQuery ("SELECT * FROM "
                  + JDRSTest.RSTEST_DFP16);
              rs2.next(); 
              BigDecimal v = rs2.getBigDecimal (1);
              rs2.close ();
              assertCondition (v.intValue() == 1);
            }
            catch (Exception e) {
              failed (e, "Unexpected Exception");
            }
          }
        }



    /**
    updateBoolean() - Update a DFP16, with the value false.
    **/
        public void Var054 ()
        {
          if (checkDecFloatSupport ()) {
            try {
              Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                  ResultSet.CONCUR_UPDATABLE);
              ResultSet rs = s.executeQuery ("SELECT * FROM "
                  + JDRSTest.RSTEST_DFP16+" FOR UPDATE ");
              rs.next(); 
              rs.updateBoolean (1, false);
              rs.updateRow ();
              ResultSet rs2 = statement2_.executeQuery ("SELECT * FROM "
                  + JDRSTest.RSTEST_DFP16);
              rs2.next();
              BigDecimal v = rs2.getBigDecimal (1);
              rs2.close ();
              assertCondition (v.intValue() == 0);
            }
            catch (Exception e) {
              failed (e, "Unexpected Exception");
            }
          }
        }
        

        /**
        updateBoolean() - Update a DFP34, with the value true.
        **/
            public void Var055 ()
            {
              if (checkDecFloatSupport ()) {
                try {
                  Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                      ResultSet.CONCUR_UPDATABLE);
                  ResultSet rs = s.executeQuery ("SELECT * FROM "
                      + JDRSTest.RSTEST_DFP34+" FOR UPDATE ");
                  rs.next(); 
                  rs.updateBoolean (1, true);
                  rs.updateRow ();
                  ResultSet rs2 = statement2_.executeQuery ("SELECT * FROM "
                      + JDRSTest.RSTEST_DFP34);
                  rs2.next(); 
                  BigDecimal v = rs2.getBigDecimal (1);
                  rs2.close ();
                  assertCondition (v.intValue() == 1);
                }
                catch (Exception e) {
                  failed (e, "Unexpected Exception");
                }
              }
            }



        /**
        updateBoolean() - Update a DFP34, with the value false.
        **/
            public void Var056 ()
            {
              if (checkDecFloatSupport ()) {
                try {
                  Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                      ResultSet.CONCUR_UPDATABLE);
                  ResultSet rs = s.executeQuery ("SELECT * FROM "
                      + JDRSTest.RSTEST_DFP34+" FOR UPDATE ");
                  rs.next(); 
                  rs.updateBoolean (1, false);
                  rs.updateRow ();
                  ResultSet rs2 = statement2_.executeQuery ("SELECT * FROM "
                      + JDRSTest.RSTEST_DFP34);
                  rs2.next(); 
                  BigDecimal v = rs2.getBigDecimal (1);
                  rs2.close ();
                  assertCondition (v.intValue() == 0);
                }
                catch (Exception e) {
                  failed (e, "Unexpected Exception");
                }
              }
            }
            

            
            /**
updateBoolean() - Update a BOOLEAN, with the value false.
**/
    public void Var057()
    {
        if (checkBooleanSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_BOOLEAN", false);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            boolean v = rs2.getBoolean ("C_BOOLEAN");
            rs2.close ();
            assertCondition (v == false, " got "+v+" sb false");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

            /**
updateBoolean() - Update a BOOLEAN, with the value true.
**/
    public void Var058()
    {
        if (checkBooleanSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBoolean ("C_BOOLEAN", true);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            boolean v = rs2.getBoolean ("C_BOOLEAN");
            rs2.close ();
            assertCondition (v == true, " got "+v+" sb true");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

        
        
        
}



