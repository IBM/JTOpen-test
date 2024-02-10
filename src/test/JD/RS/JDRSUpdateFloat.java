///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateFloat.java
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
import test.JVMInfo;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSUpdateFloat.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateFloat()
</ul>
**/
public class JDRSUpdateFloat
extends JDTestcase
{



    // Private data.
    private static final String key_            = "JDRSUpdateFloat";
    private static String select_         = "SELECT * FROM "
                                                    + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;



/**
Constructor.
**/
    public JDRSUpdateFloat (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSUpdateFloat",
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
	    try { 
		String url = baseURL_
		  
		  
		  + ";data truncation=true";
		connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
		connection_.setAutoCommit(false); // @C1A
		statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							  ResultSet.CONCUR_UPDATABLE);
		//
		// On 5/10/2007 changed this to be TYPE_SCROLL_SENSITIVE instead of
                //              TYPE_SCROLL_INSENSITIVE.  Otherwise, changes are not
                //              being seen on V5R5.
		// 
		statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							   ResultSet.CONCUR_READ_ONLY);

		statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
					  + " (C_KEY) VALUES ('DUMMY_ROW')");
		statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
					  + " (C_KEY) VALUES ('" + key_ + "')");

		select_         = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;

		rs_ = statement_.executeQuery (select_ + " FOR UPDATE");

	    } catch (Exception e) {
		System.out.println("-----------------Error on setup -------------------------");
		e.printStackTrace();
		System.out.println("---------------------------------------------------------");
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
            rs_.close ();
            statement_.close ();
            connection_.commit(); // @C1A
            connection_.close ();
        }
    }



/**
updateFloat() - Should throw exception when the result set is
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
            rs.updateFloat ("C_FLOAT", 5);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateFloat() - Should throw exception when the result set is
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
            rs.updateFloat ("C_FLOAT", 5);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateFloat() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, null);
            rs_.updateFloat ("C_FLOAT", 6);
            rs_.updateRow(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateFloat() - Should throw an exception when the column
is an invalid index.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat (100, 43);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateFloat() - Should throw an exception when the column
is 0.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat (0, -232);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateFloat() - Should throw an exception when the column
is -1.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat (-1, 76);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateFloat() - Should work when the column index is valid.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat (5, 545);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_FLOAT");
            rs2.close ();
            assertCondition (v == 545);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Should throw an exception when the column
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
            rs_.updateFloat (null, 32);
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
      }
    }


/**
updateFloat() - Should throw an exception when the column
name is an empty string.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("", 93);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateFloat() - Should throw an exception when the column
name is invalid.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("INVALID", 101);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateFloat() - Should work when the column name is valid.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_FLOAT", -599);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_FLOAT");
            rs2.close ();
            assertCondition (v == -599);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Should be reflected by get, even if update has
not yet been issued (i.e. update is still pending).
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_FLOAT", 11111);
            assertCondition (rs_.getFloat ("C_FLOAT") == 11111);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateFloat() - Should be reflected by get, after update has
been issued, but cursor has not been repositioned.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_FLOAT", 11111);
            rs_.updateRow ();
            assertCondition (rs_.getFloat ("C_FLOAT") == 11111);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateFloat() - Should be reflected by get, after update has
been issued and cursor has been repositioned.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_FLOAT", 11111);
            rs_.updateRow ();
            rs_.beforeFirst ();
            JDRSTest.position (rs_, key_);
            float v = rs_.getFloat ("C_FLOAT");
            assertCondition (v == 11111);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateFloat() - Should work when the current row is the insert
row.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateString ("C_KEY", "JDRSUpdateFloat 1");
            rs_.updateFloat ("C_FLOAT", 98765);
            rs_.insertRow ();
            JDRSTest.position (rs_, "JDRSUpdateFloat 1");
            assertCondition (rs_.getFloat ("C_FLOAT") == 98765);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Should be reflected by get on insert row, even if
insert has not yet been issued (i.e. insert is still pending).
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateFloat ("C_FLOAT", 222);
            assertCondition (rs_.getFloat ("C_FLOAT") == 222);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

           

/**
updateFloat() - Should throw an exception on a deleted row.
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "DUMMY_ROW");
            rs_.deleteRow ();
            rs_.updateFloat ("C_FLOAT", 2892);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


/**
updateFloat() - Update a SMALLINT, when the value does not
have a decimal part.
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_SMALLINT", 876);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            short v = rs2.getShort ("C_SMALLINT");
            rs2.close ();
            assertCondition (v == 876);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Update a SMALLINT, when the value has a decimal part.  This
is expected to work.
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);

            rs_.updateFloat ("C_SMALLINT", 87.443f);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            short v = rs2.getShort ("C_SMALLINT");
            rs2.close ();
            assertCondition (v == 87);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Update a SMALLINT, when the value is too large.  This will throw
a data truncation exception.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_SMALLINT");
            rs_.updateFloat ("C_SMALLINT", 874985443.5f);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          if (e instanceof DataTruncation) {
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 8)
                && (dt.getTransferSize() == 2));
            
          } else {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }

        }
        }
    }



/**
updateFloat() - Update a INTEGER, when the value does not have a
decimal part.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_INTEGER", -128374);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            int v = rs2.getInt ("C_INTEGER");
            rs2.close ();
            assertCondition (v == -128374);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Update a INTEGER, when the value does have a
decimal part.  This works.
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_INTEGER", -128.374f);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            int v = rs2.getInt ("C_INTEGER");
            rs2.close ();
            assertCondition (v == -128);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Update a INTEGER, when the value is too large.  This
causes a data truncation exception.
**/
    public void Var023 ()
    {
      if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
          JDRSTest.position (rs_, key_);
          expectedColumn = rs_.findColumn ("C_INTEGER");
          rs_.updateFloat ("C_INTEGER", -12823237439287374.8f);
          rs_.updateRow ();
          failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          if (e instanceof DataTruncation) {
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 8)
                && (dt.getTransferSize() == 4));
          } else {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
          
        }
      }
    }



/**
updateFloat() - Update a REAL.
**/
    public void Var024 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_REAL", -1.3043f);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_REAL");
            rs2.close ();
            assertCondition (v == -1.3043f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Update a FLOAT.
**/
    public void Var025 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_FLOAT", 9845.02221f);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_FLOAT");
            rs2.close ();
            assertCondition (v == 9845.02221f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Update a DOUBLE.
**/
    public void Var026 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_DOUBLE", -19845.19284f);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            double v = rs2.getDouble ("C_DOUBLE");
            rs2.close ();
            assertCondition (v == -19845.19284f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Update a DECIMAL.
**/
    public void Var027 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_DECIMAL_105", 533.11f);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
            rs2.close ();
            assertCondition (v.floatValue() == 533.11f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Update a DECIMAL, when the value has a fraction 
that gets truncated.  This is OK.
**/
    public void Var028 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_DECIMAL_40", 5331.11f);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_40");
            rs2.close ();
            DataTruncation dt = (DataTruncation) rs_.getWarnings ();
            assertCondition ((v.intValue() == 5331) && (dt == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Update a DECIMAL, when the value is too big.  This causes
a data truncation exception.
**/
    public void Var029 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_DECIMAL_40", 53311.1f);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
	    if (e instanceof DataTruncation) { 
            assertCondition(e instanceof DataTruncation);
            /*
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 5)
                && (dt.getTransferSize() == 4));
                */
	    } else {
		assertSqlException(e, -99999, "07006", "Data type mismatch",
				   "Mismatch instead of truncation in latest toolbox");

	    }

        }
        }
    }



/**
updateFloat() - Update a NUMERIC.
**/
    public void Var030 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_NUMERIC_105", -9933.1112f);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
            rs2.close ();
            assertCondition (v.floatValue() == -9933.1112f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Update a NUMERIC where only the fraction truncates.
This is OK.
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_NUMERIC_40", -9933.1112f);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_40");
            rs2.close ();
            assertCondition (v.intValue() == -9933);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Update a NUMERIC, when the value is too big.  This 
causes a data truncation exception.
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_NUMERIC_40");
            rs_.updateFloat ("C_NUMERIC_40", -33111.5f);
            rs_.updateRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
	    if (e instanceof DataTruncation) { 
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 6)
                && (dt.getTransferSize() == 4));
	    } else {
		assertSqlException(e, -99999, "07006", "Data type mismatch",
				   "Mismatch instead of truncation in latest toolbox");
	    }

        }
        }

    }



/**
updateFloat() - Update a CHAR.
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_CHAR_50", 112.33f);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_CHAR_50");
            rs2.close ();
            assertCondition (v.equals ("112.33                                            "));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Update a CHAR, when the value is too big.
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_CHAR_1");
            rs_.updateFloat ("C_CHAR_1", 1.211f);
            rs_.updateRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 5)
                && (dt.getTransferSize() == 1));

        }
        }
    }



/**
updateFloat() - Update a VARCHAR.
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_VARCHAR_50", -112.2f);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_VARCHAR_50");
            rs2.close ();
            assertCondition (v.equals ("-112.2"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateFloat() - Update a BINARY.
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_BINARY_20", 1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateFloat() - Update a VARBINARY.
**/
    public void Var037 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_VARBINARY_20", 2);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateFloat() - Update a CLOB.
**/
    public void Var038 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateFloat ("C_CLOB", -3);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateFloat() - Update a DBCLOB.
**/
    public void Var039 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateFloat ("C_DBCLOB", 0);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }




/**
updateFloat() - Update a BLOB.
**/
    public void Var040 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateFloat ("C_BLOB", 99);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateFloat() - Update a DATE.
**/
    public void Var041 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_DATE", 6);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateFloat() - Update a TIME.
**/
    public void Var042 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_TIME", 22);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateFloat() - Update a TIMESTAMP.
**/
    public void Var043 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_TIMESTAMP", 987);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateFloat() - Update a DATALINK.
**/
    public void Var044 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateFloat ("C_DATALINK", 111);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateFloat() - Update a DISTINCT.
**/
    public void Var045 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateFloat ("C_DISTINCT", -11.55f);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_DISTINCT");
                rs2.close ();
                assertCondition (v.equals ("-11.55   "));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }



/**
updateFloat() - Update a BIGINT, when the value does not have a
decimal part.
**/
    public void Var046()
    {
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_BIGINT", 428372);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            long v = rs2.getLong("C_BIGINT");
            rs2.close ();
            assertCondition (v == 428372);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
        }
    }



/**
updateFloat() - Update a BIGINT, when the value does have a
decimal part.
**/
    public void Var047()
    {
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_BIGINT", 7828.33112f);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            long v = rs2.getLong("C_BIGINT");
            rs2.close ();
            assertCondition (v == 7828);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
        }
    }



/**
updateFloat() - Update a BIGINT, when the value is too large.  This 
causes a data truncation error.
**/
    public void Var048()
    {
        if (checkJdbc20 ()) {
            if (checkBigintSupport()) {
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateFloat ("C_BIGINT", 3128127243983947239745236.374f);
                    failed ("Didn't throw SQLException");
                } catch (Exception e) {
		    boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
		    if (success) { 
			assertCondition(true); 
		    } else { 
			failed(e, "Expected data type mismatch -- updated 7/1/2014 to match other data types"); 
		    }


                }
            }
        }
    }

    public void Var049() { notApplicable(); } 
    public void Var050() { notApplicable(); } 

    
    public void dfpTest(String table, float value, String expected) {
      if (checkDecFloatSupport()) {
        try {
          Statement s = connection_.createStatement(
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s
          .executeQuery("SELECT * FROM " + table + " FOR UPDATE ");
          rs.next();
          rs.updateFloat(1, value);
          rs.updateRow();
          
          ResultSet rs2 = statement2_.executeQuery("SELECT * FROM " + table);
          rs2.next();
          String v = rs2.getString(1);
          rs2.close();
          s.close();
          try {
           connection_.commit(); 
          } catch (Exception e) {} 
          assertCondition(v.equals(expected),
			  "\nGot  " + v +
			  "\nfrom "+ value +
			  "\n sb  " + expected);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }

    public void dfpRoundTest(String roundingMode, String table, float value, String expected) {
      if (checkDecFloatSupport()) {
        try {
          String url = baseURL_
          
          
          + ";data truncation=true" 
          + ";decfloat rounding mode="+roundingMode;
          Connection connection = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
          
          Statement s = connection.createStatement(
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s
          .executeQuery("SELECT * FROM " + table + " FOR UPDATE ");
          rs.next();
          rs.updateFloat(1, value);
          rs.updateRow();
          
          ResultSet rs2 = statement2_.executeQuery("SELECT * FROM " + table);
          rs2.next();
          String v = rs2.getString(1);
          rs2.close();
          s.close();
          connection.close(); 
          assertCondition(v.equals(expected), "Got " + v + " sb " + expected +" from "+value);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
    
    /**
     * updateFloat -- set a DFP16 value (normal)
     */
    public void Var051 () { dfpTest(JDRSTest.RSTEST_DFP16, (float) 4533.43, "4533.43"); }
  
    /**
     * updateFloat -- set a DFP16 value (Nan)
     */    
    public void Var052 () { dfpTest(JDRSTest.RSTEST_DFP16, Float.NaN, "NaN"); }

    
    /**
     * updateFloat.-- set a DFP16 value (NEGATIVE_INFINITY)
     */    
    public void Var053 () { dfpTest(JDRSTest.RSTEST_DFP16, Float.NEGATIVE_INFINITY, "-Infinity"); }

    /**
     * updateFloat -- set a DFP16 value (POSITIVE_INFINITY)
     */    
    public void Var054 () { dfpTest(JDRSTest.RSTEST_DFP16, Float.POSITIVE_INFINITY, "Infinity"); }

    /**
     * updateFloat -- set a DFP16 value (MAX_VALUE)
     */    
    
    public void Var055 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTest(JDRSTest.RSTEST_DFP16, Float.MAX_VALUE, "340282350000000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, Float.MAX_VALUE, "3.4028235E+38");
	}
    }

    /**
     * updateFloat -- set a DFP16 value (MIN_VALUE)
     */    
    public void Var056 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTest(JDRSTest.RSTEST_DFP16, Float.MIN_VALUE, "0.0000000000000000000000000000000000000000000014");
	} else {
	    dfpTest(JDRSTest.RSTEST_DFP16, Float.MIN_VALUE, "1.4E-45"); 
	} 
    }

    /** 
     *  updateFloat -- set a DFP16 positive with rounding mode "round half even"
     */
    public void Var057 () {
	dfpRoundTest("round half even", JDRSTest.RSTEST_DFP16, 
        1.2345678901234545f, "1.2345679");
    }
    
    public void Var058 () { dfpRoundTest("round half even", JDRSTest.RSTEST_DFP16, 
        1.2345678901234545f, "1.2345679"); }

    /** 
     *  updateFloat -- set a DFP16 negative with rounding mode "round half even"  
     */
    public void Var059 () { dfpRoundTest("round half even", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234555f, "-1.2345679"); }
    public void Var060 () { dfpRoundTest("round half even", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234545f, "-1.2345679"); }


    /** 
     *  updateFloat -- set a DFP16 positive with rounding mode "round half up"   
     */
    public void Var061 () { dfpRoundTest("round half up", JDRSTest.RSTEST_DFP16, 
        1.2345678901234555f, "1.2345679"); }
    public void Var062 () { dfpRoundTest("round half up", JDRSTest.RSTEST_DFP16, 
        1.2345678901234545f, "1.2345679"); }

    /** 
     *  updateFloat -- set a DFP16 negative with rounding mode "round half up"   
     */
    public void Var063 () { dfpRoundTest("round half up", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234555f, "-1.2345679"); }
    public void Var064 () { dfpRoundTest("round half up", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234545f, "-1.2345679"); }


    /** 
     *  updateFloat -- set a DFP16 positive with rounding mode "round down"   
     */
    public void Var065 () { dfpRoundTest("round down", JDRSTest.RSTEST_DFP16, 
        1.2345678901234569f, "1.2345679"); }
    public void Var066 () { dfpRoundTest("round down", JDRSTest.RSTEST_DFP16, 
        1.2345678901234561f, "1.2345679"); }

    
    /** 
     *  updateFloat -- set a DFP16 negative with rounding mode "round down"   
     */
    public void Var067 () { dfpRoundTest("round down", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234569f, "-1.2345679"); }
    public void Var068 () { dfpRoundTest("round down", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234561f, "-1.2345679"); }
  
    
    /** 
     *  updateFloat -- set a DFP16 positive with rounding mode "round ceiling"   
     */
    public void Var069 () { dfpRoundTest("round ceiling", JDRSTest.RSTEST_DFP16, 
        1.2345678901234555f, "1.2345679"); }
    public void Var070 () { dfpRoundTest("round ceiling", JDRSTest.RSTEST_DFP16, 
        1.2345678901234545f, "1.2345679"); }

    
    /** 
     *  updateFloat -- set a DFP16 negative with rounding mode "round ceiling"   
     */
    public void Var071 () { dfpRoundTest("round ceiling", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234555f, "-1.2345679"); }
    public void Var072 () { dfpRoundTest("round ceiling", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234545f, "-1.2345679"); }

    /** 
     *  updateFloat -- set a DFP16 positive with rounding mode "round floor"   
     */
    public void Var073 () { dfpRoundTest("round floor", JDRSTest.RSTEST_DFP16, 
        1.2345678901234555f, "1.2345679"); }
    public void Var074 () { dfpRoundTest("round floor", JDRSTest.RSTEST_DFP16, 
        1.2345678901234545f, "1.2345679"); }
    

    /** 
     *  updateFloat -- set a DFP16 negative with rounding mode "round floor"   
     */
    public void Var075 () { dfpRoundTest("round floor", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234555f, "-1.2345679"); }
    public void Var076 () { dfpRoundTest("round floor", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234545f, "-1.2345679"); }
    

    /** 
     *  updateFloat -- set a DFP16 positive with rounding mode "round half down"   
     */
    public void Var077 () { dfpRoundTest("round half down", JDRSTest.RSTEST_DFP16, 
        1.2345678901234555f, "1.2345679"); }
    public void Var078 () { dfpRoundTest("round half down", JDRSTest.RSTEST_DFP16, 
        1.2345678901234545f, "1.2345679"); }

    
    
    /** 
     *  updateFloat -- set a DFP16 negative with rounding mode "round half down"   
     */
    public void Var079 () { dfpRoundTest("round half down", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234555f, "-1.2345679"); }
    public void Var080 () { dfpRoundTest("round half down", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234545f, "-1.2345679"); }

    
    /** 
     *  updateFloat -- set a DFP16 positive with rounding mode "round up"   
     */
    public void Var081 () { dfpRoundTest("round up", JDRSTest.RSTEST_DFP16, 
        1.2345678901234555f, "1.2345679"); }
    public void Var082 () { dfpRoundTest("round up", JDRSTest.RSTEST_DFP16, 
        1.2345678901234545f, "1.2345679"); }

    
    /** 
     *  updateFloat -- set a DFP16 negative with rounding mode "round up"   
     */
    public void Var083 () { dfpRoundTest("round up", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234555f, "-1.2345679"); }
    public void Var084 () { dfpRoundTest("round up", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234545f, "-1.2345679"); }

    
    /**
     * updateFloat -- set a DFP34 value (normal)
     */
    public void Var085 () { dfpTest(JDRSTest.RSTEST_DFP34, 4533.43f, "4533.43"); }
  
    /**
     * updateFloat -- set a DFP34 value (Nan)
     */    
    public void Var086 () { dfpTest(JDRSTest.RSTEST_DFP34, Float.NaN, "NaN"); }

    
    /**
     * updateFloat -- set a DFP34 value (NEGATIVE_INFINITY)
     */    
    public void Var087 () { dfpTest(JDRSTest.RSTEST_DFP34, Float.NEGATIVE_INFINITY, "-Infinity"); }

    /**
     * updateFloat -- set a DFP34 value (POSITIVE_INFINITY)
     */    
    public void Var088 () { dfpTest(JDRSTest.RSTEST_DFP34, Float.POSITIVE_INFINITY, "Infinity"); }

    /**
     * updateFloat -- set a DFP34 value (MAX_VALUE)
     */    
    public void Var089 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTest(JDRSTest.RSTEST_DFP34, Float.MAX_VALUE, "340282350000000000000000000000000000000");
	} else {
	    dfpTest(JDRSTest.RSTEST_DFP34, Float.MAX_VALUE, "3.4028235E+38");
	} 
    }

    /**
     * updateFloat -- set a DFP34 value (MIN_VALUE)
     */    
    public void Var090 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) && getJDK() <= JVMInfo.JDK_142) {
	    dfpTest(JDRSTest.RSTEST_DFP34, Float.MIN_VALUE, "0.0000000000000000000000000000000000000000000014");
	} else {
	    dfpTest(JDRSTest.RSTEST_DFP34, Float.MIN_VALUE, "1.4E-45");
	}
    }

    /** 
     *  updateFloat -- set a DFP34 positive with rounding mode "round half even"  
     */
    public void Var091 () { dfpRoundTest("round half even", JDRSTest.RSTEST_DFP34, 
        1.2345678901234555f, "1.2345679"); }
    
    public void Var092 () { dfpRoundTest("round half even", JDRSTest.RSTEST_DFP34, 
        1.2345678901234545f, "1.2345679"); }

    /** 
     *  updateFloat -- set a DFP34 negative with rounding mode "round half even"  
     */
    public void Var093 () { dfpRoundTest("round half even", JDRSTest.RSTEST_DFP34, 
        -1.2345678901234555f, "-1.2345679"); }
    public void Var094 () { dfpRoundTest("round half even", JDRSTest.RSTEST_DFP34, 
        -1.2345678901234545f, "-1.2345679"); }


    /** 
     *  updateFloat -- set a DFP34 positive with rounding mode "round half up"   
     */
    public void Var095 () { dfpRoundTest("round half up", JDRSTest.RSTEST_DFP34, 
        1.2345678901234555f, "1.2345679"); }
    public void Var096 () { dfpRoundTest("round half up", JDRSTest.RSTEST_DFP34, 
        1.2345678901234545f, "1.2345679"); }

    /** 
     *  updateFloat -- set a DFP34 negative with rounding mode "round half up"   
     */
    public void Var097 () { dfpRoundTest("round half up", JDRSTest.RSTEST_DFP34, 
        -1.2345678901234555f, "-1.2345679"); }
    public void Var098 () { dfpRoundTest("round half up", JDRSTest.RSTEST_DFP34, 
        -1.2345678901234545f, "-1.2345679"); }


    /** 
     *  updateFloat -- set a DFP34 positive with rounding mode "round down"   
     */
    public void Var099 () { dfpRoundTest("round down", JDRSTest.RSTEST_DFP34, 
        1.2345678901234569f, "1.2345679"); }
    public void Var100 () { dfpRoundTest("round down", JDRSTest.RSTEST_DFP34, 
        1.2345678901234561f, "1.2345679"); }

    
    /** 
     *  updateFloat -- set a DFP34 negative with rounding mode "round down"   
     */
    public void Var101 () { dfpRoundTest("round down", JDRSTest.RSTEST_DFP34, 
        -1.2345678901234569f, "-1.2345679"); }
    public void Var102 () { dfpRoundTest("round down", JDRSTest.RSTEST_DFP34, 
        -1.2345678901234561f, "-1.2345679"); }
  
    
    /** 
     *  updateFloat -- set a DFP34 positive with rounding mode "round ceiling"   
     */
    public void Var103 () { dfpRoundTest("round ceiling", JDRSTest.RSTEST_DFP34, 
        1.2345678901234555f, "1.2345679"); }
    public void Var104 () { dfpRoundTest("round ceiling", JDRSTest.RSTEST_DFP34, 
        1.2345678901234545f, "1.2345679"); }

    
    /** 
     *  updateFloat -- set a DFP34 negative with rounding mode "round ceiling"   
     */
    public void Var105 () { dfpRoundTest("round ceiling", JDRSTest.RSTEST_DFP34, 
        -1.2345678901234555f, "-1.2345679"); }
    public void Var106 () { dfpRoundTest("round ceiling", JDRSTest.RSTEST_DFP34, 
        -1.2345678901234545f, "-1.2345679"); }

    /** 
     *  updateFloat -- set a DFP34 positive with rounding mode "round floor"   
     */
    public void Var107 () { dfpRoundTest("round floor", JDRSTest.RSTEST_DFP34, 
        1.2345678901234555f, "1.2345679"); }
    public void Var108 () { dfpRoundTest("round floor", JDRSTest.RSTEST_DFP34, 
        1.2345678901234545f, "1.2345679"); }
    

    /** 
     *  updateFloat -- set a DFP34 negative with rounding mode "round floor"   
     */
    public void Var109 () { dfpRoundTest("round floor", JDRSTest.RSTEST_DFP34, 
        -1.2345678901234555f, "-1.2345679"); }
    public void Var110 () { dfpRoundTest("round floor", JDRSTest.RSTEST_DFP34, 
        -1.2345678901234545f, "-1.2345679"); }
    

    /** 
     *  updateFloat -- set a DFP34 positive with rounding mode "round half down"   
     */
    public void Var111 () { dfpRoundTest("round half down", JDRSTest.RSTEST_DFP34, 
        1.2345678901234555f, "1.2345679"); }
    public void Var112 () { dfpRoundTest("round half down", JDRSTest.RSTEST_DFP34, 
        1.2345678901234545f, "1.2345679"); }

    
    
    /** 
     *  updateFloat -- set a DFP34 negative with rounding mode "round half down"   
     */
    public void Var113 () { dfpRoundTest("round half down", JDRSTest.RSTEST_DFP34, 
        -1.2345678901234555f, "-1.2345679"); }
    public void Var114 () { dfpRoundTest("round half down", JDRSTest.RSTEST_DFP34, 
        -1.2345678901234545f, "-1.2345679"); }

    
    /** 
     *  updateFloat -- set a DFP34 positive with rounding mode "round up"   
     */
    public void Var115 () { dfpRoundTest("round up", JDRSTest.RSTEST_DFP34, 
        1.2345678901234555f, "1.2345679"); }
    public void Var116 () { dfpRoundTest("round up", JDRSTest.RSTEST_DFP34, 
        1.2345678901234545f, "1.2345679"); }

    
    /** 
     *  updateFloat -- set a DFP34 negative with rounding mode "round up"   
     */
    public void Var117 () { dfpRoundTest("round up", JDRSTest.RSTEST_DFP34, 
        -1.2345678901234555f, "-1.2345679"); }
    public void Var118 () { dfpRoundTest("round up", JDRSTest.RSTEST_DFP34, 
        -1.2345678901234545f, "-1.2345679"); }

/**
updateFloat() - Update a BOOLEAN
**/
    public void Var119()
    {
        if (checkJdbc20 ()) {
        if (checkBooleanSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_BOOLEAN", 428372);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            boolean v = rs2.getBoolean("C_BOOLEAN");
            rs2.close ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
        }
    }

    /**
updateFloat() - Update a BOOLEAN
**/
    public void Var120()
    {
        if (checkJdbc20 ()) {
        if (checkBooleanSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateFloat ("C_BOOLEAN", 0);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            boolean v = rs2.getBoolean("C_BOOLEAN");
            rs2.close ();
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
        }
    }

    
    
}



