///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateInt.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;



/**
Testcase JDRSUpdateInt.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateInt()
</ul>
**/
public class JDRSUpdateInt
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSUpdateInt";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static final String key_            = "JDRSUpdateInt";
    private static String select_         = "SELECT * FROM "
                                                    + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;



/**
Constructor.
**/
    public JDRSUpdateInt (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSUpdateInt",
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
updateInt() - Should throw exception when the result set is
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
            rs.updateInt ("C_INTEGER", 5);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateInt() - Should throw exception when the result set is
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
            rs.updateInt ("C_INTEGER", 5);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateInt() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, null);
            rs_.updateInt ("C_INTEGER", 6);
            rs_.updateRow(); 
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateInt() - Should throw an exception when the column
is an invalid index.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt (100, 43);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateInt() - Should throw an exception when the column
is 0.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt (0, -232);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateInt() - Should throw an exception when the column
is -1.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt (-1, 76);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateInt() - Should work when the column index is valid.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt (3, 545);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            int v = rs2.getInt ("C_INTEGER");
            rs2.close ();
            assertCondition (v == 545);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateInt() - Should throw an exception when the column
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
            rs_.updateInt (null, 32);
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
      }
    }


/**
updateInt() - Should throw an exception when the column
name is an empty string.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("", 93);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateInt() - Should throw an exception when the column
name is invalid.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("INVALID", 101);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateInt() - Should work when the column name is valid.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_INTEGER", -599);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            int v = rs2.getInt ("C_INTEGER");
            rs2.close ();
            assertCondition (v == -599);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateInt() - Should be reflected by get, even if update has
not yet been issued (i.e. update is still pending).
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_INTEGER", 11111);
            assertCondition (rs_.getInt ("C_INTEGER") == 11111);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateInt() - Should be reflected by get, after update has
been issued, but cursor has not been repositioned.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_INTEGER", 11111);
            rs_.updateRow ();
            assertCondition (rs_.getInt ("C_INTEGER") == 11111);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateInt() - Should be reflected by get, after update has
been issued and cursor has been repositioned.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_INTEGER", 11111);
            rs_.updateRow ();
            rs_.beforeFirst ();
            JDRSTest.position (rs_, key_);
            int v = rs_.getInt ("C_INTEGER");
            assertCondition (v == 11111);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateInt() - Should work when the current row is the insert
row.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateString ("C_KEY", "JDRSUpdateInt 1");
            rs_.updateInt ("C_INTEGER", 98765);
            rs_.insertRow ();
            JDRSTest.position (rs_, "JDRSUpdateInt 1");
            assertCondition (rs_.getInt ("C_INTEGER") == 98765);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateInt() - Should be reflected by get on insert row, even if
insert has not yet been issued (i.e. insert is still pending).
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateInt ("C_INTEGER", 222);
            assertCondition (rs_.getInt ("C_INTEGER") == 222);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

           

/**
updateInt() - Should throw an exception on a deleted row.
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "DUMMY_ROW");
            rs_.deleteRow ();
            rs_.updateInt ("C_INTEGER", 2892);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


/**
updateInt() - Update a SMALLINT.
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_SMALLINT", 876);
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
updateInt() - Update a SMALLINT, when the integer is too big.
**/
    public void Var019 ()
    {
      if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
          JDRSTest.position (rs_, key_);
          expectedColumn = rs_.findColumn ("C_SMALLINT");
          rs_.updateInt ("C_SMALLINT", 876123);
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
                && (dt.getTransferSize() == 2));
          } else {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
          
          
        }
      }
    }



/**
updateInt() - Update a INTEGER.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_INTEGER", -128374);
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
updateInt() - Update a REAL.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_REAL", -1);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_REAL");
            rs2.close ();
            assertCondition (v == -1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateInt() - Update a FLOAT.
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_FLOAT", 9845);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_FLOAT");
            rs2.close ();
            assertCondition (v == 9845);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateInt() - Update a DOUBLE.
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_DOUBLE", -19845);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            double v = rs2.getDouble ("C_DOUBLE");
            rs2.close ();
            assertCondition (v == -19845);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateInt() - Update a DECIMAL.
**/
    public void Var024 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_DECIMAL_105", 533);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
            rs2.close ();
            assertCondition (v.intValue() == 533);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateInt() - Update a DECIMAL, when the value is too big.
**/
    public void Var025 ()
    {
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_DECIMAL_40");
            rs_.updateInt ("C_DECIMAL_40", 533111);
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
updateInt() - Update a NUMERIC.
**/
    public void Var026 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_NUMERIC_105", -9933);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
            rs2.close ();
            assertCondition (v.intValue() == -9933);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateInt() - Update a NUMERIC, when the value is too big.
**/
    public void Var027 ()
    {
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_NUMERIC_40");
            rs_.updateInt ("C_NUMERIC_40", -331115);
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
updateInt() - Update a CHAR.
**/
    public void Var028 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_CHAR_50", 112);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_CHAR_50");
            rs2.close ();
            assertCondition (v.equals ("112                                               "));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateInt() - Update a CHAR, when the value is too big.
**/
    public void Var029 ()
    {
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_CHAR_1");
            rs_.updateInt ("C_CHAR_1", 12);
            rs_.updateRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 2)
                && (dt.getTransferSize() == 1));

        }
        }
    }



/**
updateInt() - Update a VARCHAR.
**/
    public void Var030 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_VARCHAR_50", 1122);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_VARCHAR_50");
            rs2.close ();
            assertCondition (v.equals ("1122"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateInt() - Update a BINARY.
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_BINARY_20", 1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateInt() - Update a VARBINARY.
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_VARBINARY_20", 2);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateInt() - Update a CLOB.
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateInt ("C_CLOB", -3);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateInt() - Update a DBCLOB.
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateInt ("C_DBCLOB", 0);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateInt() - Update a BLOB.
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateInt ("C_BLOB", 99);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateInt() - Update a DATE.
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_DATE", 6);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateInt() - Update a TIME.
**/
    public void Var037 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_TIME", 22);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateInt() - Update a TIMESTAMP.
**/
    public void Var038 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_TIMESTAMP", 987);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateInt() - Update a DATALINK.
**/
    public void Var039 ()
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
                rs_.updateInt ("C_DATALINK", 111);
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
updateInt() - Update a DISTINCT.
**/
    public void Var040 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateInt ("C_DISTINCT", -11);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_DISTINCT");
                rs2.close ();
                assertCondition (v.equals ("-11      "));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }



/**
updateInt() - Update a BIGINT.
**/
    public void Var041()
    {
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_BIGINT", 5128374);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            long v = rs2.getLong("C_BIGINT");
            rs2.close ();
            assertCondition (v == 5128374);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
        }
    }

    
    public void dfpTest(String table, int value, String expected) {
      if (checkDecFloatSupport()) {
        try {
          Statement s = connection_.createStatement(
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s
          .executeQuery("SELECT * FROM " + table + " FOR UPDATE ");
          rs.next();
          rs.updateInt(1, value);
          rs.updateRow();
          
          ResultSet rs2 = statement2_.executeQuery("SELECT * FROM " + table);
          rs2.next();
          String v = rs2.getString(1);
          rs2.close();
          s.close();
          try {
           connection_.commit(); 
          } catch (Exception e) {} 
          assertCondition(v.equals(expected), "Got " + v + " sb " + expected);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }

    
    /**
     * updateInt -- set a DFP16 value 
     */
    public void Var042 () { dfpTest(JDRSTest.RSTEST_DFP16, 123456789, "123456789"); }
  
    /**
     * updateInt -- set a DFP16 value 
     */
    public void Var043 () { dfpTest(JDRSTest.RSTEST_DFP34, 123456789, "123456789"); }

    /**
     * Check updating cursor via DRDA.  See CLI PTF SE69885 
     */
    public void Var044() { 
      if (checkNative()) {
        boolean passed = true; 
        StringBuffer sb = new StringBuffer(" -- added 08/15/2018 for CLI PTF SE69885"); 
        try { 
          String sql; 
          String tableName = collection_+".JDRSUI44";
          String remote = System.getProperty("DCLIWHDSN"); 
          if (remote == null) remote = "MEMEMEM"; 
          String userid = System.getProperty("CLIWHUID"); 
          String password = System.getProperty("CLIWHPWD"); 
          String url = "jdbc:db2:"+remote; 
          sb.append("Connecting to "+url+" using "+userid+" "+password+"\n"); 
          Connection c = testDriver_.getConnection (url, userid, encryptedPassword_);
          c.setAutoCommit(false); 
          Statement s = c.createStatement(); 
          initTable(s, tableName, "(C1 INT)", sb);
          
          
          sql = "INSERT INTO  "+tableName+" VALUES(0)";
          sb.append("Executing "+sql+"\n"); 
          s.executeUpdate(sql);
          c.commit(); 
          
          Statement sUpdate = c.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE); 
          sql = "SELECT * FROM "+tableName+" FOR UPDATE";
          sb.append("Executing "+sql+"\n"); 
          ResultSet rs = sUpdate.executeQuery(sql); 
          rs.next(); 
          rs.updateInt(1, 99);
          sb.append("Updating row\n"); 
          rs.updateRow(); 
          rs.close(); 
          c.commit(); 
          
          sql = "SELECT * FROM "+tableName;
          sb.append("Executing "+sql+"\n"); 
          rs = sUpdate.executeQuery(sql); 
          rs.next();
          int v = rs.getInt(1); 
          if (v != 99) {
            passed = false; 
            sb.append("Got v from query, sb 99\n"); 
          }
          
          c.close(); 
          assertCondition(passed, sb); 
          
        } catch (Exception e)  {
          failed(e, "Unexpected exception "+sb.toString()); 
        }
      }
    }

    /**
     * Check updating cursor create using Common table expression. 
     */
    public void Var045() { 

        boolean passed = true; 
        StringBuffer sb = new StringBuffer(" -- added 09/06/2018 to check updatable common table expressions"); 
        try { 
          String sql; 
          String tableName = collection_+".JDRSUI45";

          Statement s = connection_.createStatement(); 
          initTable(s, tableName, "(C1 INT)", sb);

          sql = "INSERT INTO  "+tableName+" VALUES(0)";
          sb.append("Executing "+sql+"\n"); 
          s.executeUpdate(sql);
          connection_.commit(); 
          
          Statement sUpdate = connection_.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE); 
          sql = "WITH X AS (SELECT * FROM "+tableName+") SELECT * FROM X FOR UPDATE";
          sb.append("Executing "+sql+"\n"); 
          ResultSet rs = sUpdate.executeQuery(sql); 
          rs.next(); 
          rs.updateInt(1, 99);
          sb.append("Updating row\n"); 
          rs.updateRow(); 
          rs.close(); 
          connection_.commit(); 
          
          sql = "SELECT * FROM "+tableName;
          sb.append("Executing "+sql+"\n"); 
          rs = sUpdate.executeQuery(sql); 
          rs.next();
          int v = rs.getInt(1); 
          if (v != 99) {
            passed = false; 
            sb.append("Got v from query, sb 99\n"); 
          }

          sUpdate.close(); 
          s.close(); 
          assertCondition(passed, sb); 
          
        } catch (Exception e)  {
          failed(e, "Unexpected exception "+sb.toString()); 
        }

    }



    /**
updateInt() - Update a BOOLEAN.
**/
    public void Var046()
    {
        if (checkJdbc20 ()) {
        if (checkBooleanSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_BOOLEAN", 5128374);
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
updateInt() - Update a BOOLEAN.
**/
    public void Var047()
    {
        if (checkJdbc20 ()) {
        if (checkBooleanSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateInt ("C_BOOLEAN", 0);
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



