///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateString.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import com.ibm.as400.access.AS400;

import test.JDJobName;
import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JVMInfo;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Hashtable;



/**
Testcase JDRSUpdateString.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateString()
</ul>
**/
public class JDRSUpdateString
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSUpdateString";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static final String key_ = "JDRSUpdateString";
    private static String select_    = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;

    boolean isjdk14_ = false;


    /**
    Constructor.
    **/
    public JDRSUpdateString (AS400 systemObject,
			     Hashtable namesAndVars,
			     int runMode,
			     FileOutputStream fileOutputStream,
			     
			     String password)
    {
	super (systemObject, "JDRSUpdateString",
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


	select_         = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;

	if(isJdbc20 ())
	{
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
				      + " (C_KEY) VALUES ('DUMMY_ROW2')");
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
	if(isJdbc20 ())
	{
	    rs_.close ();
	    statement_.close ();
	    connection_.commit(); // @C1A
	    connection_.close ();
	}
    }



    /**
    updateString() - Should throw exception when the result set is
    closed.
    **/
    public void Var001()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							   ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = s.executeQuery ("SELECT * FROM "
					       + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
		rs.next ();
		rs.close ();
		rs.updateString ("C_VARCHAR_50", "JDBC");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Should throw exception when the result set is
    not updatable.
    **/
    public void Var002()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
							   ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = s.executeQuery ("SELECT * FROM "
					       + JDRSTest.RSTEST_UPDATE);
		rs.next ();
		rs.updateString ("C_VARCHAR_50", "Database");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Should throw exception when cursor is not pointing
    to a row.
    **/
    public void Var003()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, null);
		rs_.updateString ("C_VARCHAR_50", "Tables");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Should throw an exception when the column
    is an invalid index.
    **/
    public void Var004()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString (100, "Schemas");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Should throw an exception when the column
    is 0.
    **/
    public void Var005()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString (0, "Columns");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Should throw an exception when the column
    is -1.
    **/
    public void Var006()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString (-1, "Rows");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Should work when the column index is valid.
    **/
    public void Var007()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString (14, "Queries");
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		String v = rs2.getString ("C_VARCHAR_50");
		rs2.close ();
		assertCondition (v.equals ("Queries"));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Should throw an exception when the column
    name is null.
    **/
    public void Var008()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString (null, "Updates");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Should throw an exception when the column
    name is an empty string.
    **/
    public void Var009()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("", "Inserts");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Should throw an exception when the column
    name is invalid.
    **/
    public void Var010()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("INVALID", "Joins");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Should work when the column name is valid.
    **/
    public void Var011()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_VARCHAR_50", "Inner joins");
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		String v = rs2.getString ("C_VARCHAR_50");
		rs2.close ();
		assertCondition (v.equals ("Inner joins"));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Should update to SQL NULL when the column
    value is null.
    **/
    public void Var012()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_VARCHAR_50", null);
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		String v = rs2.getString ("C_VARCHAR_50");
		boolean wn = rs2.wasNull ();
		rs2.close ();
		assertCondition ((v == null) && (wn == true));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Should be reflected by get, even if update has
    not yet been issued (i.e. update is still pending).
    **/
    public void Var013()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_VARCHAR_50", "DB2/400");
		assertCondition (rs_.getString ("C_VARCHAR_50").equals ("DB2/400"));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }




    /**
    updateString() - Should be reflected by get, after update has
    been issued, but cursor has not been repositioned.
    **/
    public void Var014()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_VARCHAR_50", "SQL");
		rs_.updateRow ();
		assertCondition (rs_.getString ("C_VARCHAR_50").equals ("SQL"));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }




    /**
    updateString() - Should be reflected by get, after update has
    been issued and cursor has been repositioned.
    **/
    public void Var015()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_VARCHAR_50", "Collections");
		rs_.updateRow ();
		rs_.beforeFirst ();
		JDRSTest.position (rs_, key_);
		String v = rs_.getString ("C_VARCHAR_50");
		assertCondition (v.equals ("Collections"));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }




    /**
    updateString() - Should work when the current row is the insert
    row.
    **/
    public void Var016()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		rs_.moveToInsertRow ();
		rs_.updateString ("C_KEY", "JDRSUpdateString 1");
		rs_.updateString ("C_VARCHAR_50", "Foreign keys");
		rs_.insertRow ();
		JDRSTest.position (rs_, "JDRSUpdateString 1");
		assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Foreign keys"));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Should be reflected by get on insert row, even if
    insert has not yet been issued (i.e. insert is still pending).
    **/
    public void Var017()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		rs_.moveToInsertRow ();
		rs_.updateString ("C_VARCHAR_50", "Primary keys");
		assertCondition (rs_.getString ("C_VARCHAR_50").equals ("Primary keys"));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Should throw an exception on a deleted row.
    **/
    public void Var018()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, "DUMMY_ROW");
		rs_.deleteRow ();
		rs_.updateString ("C_VARCHAR_50", "STRSQL");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


    /**
    updateString() - Update a SMALLINT.
    **/
    public void Var019 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_SMALLINT", "423");
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		short v = rs2.getShort ("C_SMALLINT");
		rs2.close ();
		assertCondition (v == 423);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Update a SMALLINT, when the integer is too big.
    **/
    public void Var020 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_SMALLINT", "487623");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Update a SMALLINT, when the string is not a number.
    **/
    public void Var021 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_SMALLINT", "Record level access");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Update an INTEGER.
    **/
    public void Var022 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_INTEGER", "-1228374");
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		int v = rs2.getInt ("C_INTEGER");
		rs2.close ();
		assertCondition (v == -1228374);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Update an INTEGER, when the integer is too big.
    **/
    public void Var023 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_INTEGER", "45353487623");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Update an INTEGER, when the string is not a number.
    **/
    public void Var024 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_INTEGER", "Data queues");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Update a REAL.
    **/
    public void Var025 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_REAL", "-1.5");
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		float v = rs2.getFloat ("C_REAL");
		rs2.close ();
		assertCondition (v == -1.5);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    updateString() - Update a REAL, when the number is too big.

    Note:  Given the new data truncation rules for JDBC, this testcase
	   is expected to work without throwing a DataTruncation exception.
    **/
    public void Var026 ()
    {
	if(checkJdbc20 ())
	{
	    // int expectedColumn = -1;
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.findColumn ("C_REAL");
		rs_.updateString ("C_REAL", "4534335.443487623");
		rs_.updateRow ();

		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		float v = rs2.getFloat ("C_REAL");
		rs2.close ();
		assertCondition (v == 4534335.5);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}

	/*
	failed ("Didn't throw SQLException");
    }
    catch (Exception e) {
	DataTruncation dt = (DataTruncation)e;
	assertCondition ((dt.getIndex() == expectedColumn)
	    && (dt.getParameter() == false)
	    && (dt.getRead() == false)
	    && (dt.getDataSize() == 16)
	    && (dt.getTransferSize() == 8));

    }
    }
    */
    }



    /**
    updateString() - Update a REAL, when the string is not a number.
    **/
    public void Var027 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_INTEGER", "Data areas");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }




    /**
    updateString() - Update a FLOAT.
    **/
    public void Var028 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_FLOAT", "9845.0");
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		float v = rs2.getFloat ("C_FLOAT");
		rs2.close ();
		assertCondition (v == 9845.0);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Update a FLOAT, when the number is too big.

    Note:  Given the new data truncation rules for JDBC, this testcase
	   is expected to work without throwing a DataTruncation exception.
    **/
    public void Var029 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.findColumn ("C_FLOAT");
		rs_.updateString ("C_FLOAT", "453435.4434556337633");
		rs_.updateRow ();

		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		float v = rs2.getFloat ("C_FLOAT");
		rs2.close ();
		assertCondition ((v > 453435) && (v < 453436));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
	/*
	    failed ("Didn't throw SQLException");
	}
	catch (Exception e) {
	    DataTruncation dt = (DataTruncation)e;
	    assertCondition ((dt.getIndex() == expectedColumn)
		&& (dt.getParameter() == false)
		&& (dt.getRead() == false)
		&& (dt.getDataSize() == 19)
		&& (dt.getTransferSize() == 17));

	}
	}
	*/
    }



    /**
    updateString() - Update a FLOAT, when the string is not a number.
    **/
    public void Var030 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_FLOAT", "User spaces");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


    /**
    updateString() - Update a DOUBLE.
    **/
    public void Var031 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_DOUBLE", "-19845");
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		double v = rs2.getDouble ("C_DOUBLE");
		rs2.close ();
		assertCondition (v == -19845);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Update a DOUBLE, when the number is too big.

    Note:  Given the new data truncation rules for JDBC, this testcase
	   is expected to work without throwing a DataTruncation exception.
    **/
    public void Var032 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.findColumn ("C_DOUBLE");
		rs_.updateString ("C_DOUBLE", "4533435.4433445532333");
		rs_.updateRow ();

		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		float v = rs2.getFloat ("C_DOUBLE");
		rs2.close ();
		assertCondition (v == 4533435.5);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
	/*
	failed ("Didn't throw SQLException");
    }
    catch (Exception e) {
	DataTruncation dt = (DataTruncation)e;
	assertCondition ((dt.getIndex() == expectedColumn)
	    && (dt.getParameter() == false)
	    && (dt.getRead() == false)
	    && (dt.getDataSize() == 20)
	    && (dt.getTransferSize() == 16));

    }
    }
    */
    }



    /**
    updateString() - Update a DOUBLE, when the string is not a number.
    **/
    public void Var033 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_DOUBLE", "IFS");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


    /**
    updateString() - Update a DECIMAL.
    **/
    public void Var034 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_DECIMAL_105", "-4533.4");
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
		rs2.close ();
		assertCondition (v.floatValue() == -4533.4f);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Update a DECIMAL, when the value is too big.
    **/
    public void Var035 ()
    {
	if(checkJdbc20 ())
	{
	    int expectedColumn = -1;
	    try
	    {
		JDRSTest.position (rs_, key_);
		expectedColumn = rs_.findColumn ("C_DECIMAL_40");
		rs_.updateString ("C_DECIMAL_40", "543511");
		rs_.updateRow ();
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
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
    updateString() - Update a DECIMAL, when the string is not a number.
    **/
    public void Var036 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_DECIMAL1050", "GUI components");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Update a NUMERIC.
    **/
    public void Var037 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_NUMERIC_105", "-9933.455");
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
		rs2.close ();
		assertCondition (v.doubleValue() == -9933.455);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Update a NUMERIC, when the value is too big.
    **/
    public void Var038 ()
    {
	if(checkJdbc20 ())
	{
	    int expectedColumn = -1;
	    try
	    {
		JDRSTest.position (rs_, key_);
		expectedColumn = rs_.findColumn ("C_NUMERIC_40");
		rs_.updateString ("C_NUMERIC_40", "-331515");
		rs_.updateRow ();
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
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
    updateString() - Update a NUMERIC, when the string is not a number.
    **/
    public void Var039 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_NUMERIC_105", "Jar maker");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Update a CHAR.
    **/
    public void Var040 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_CHAR_50", "JDBC 2.0");
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		String v = rs2.getString ("C_CHAR_50");
		rs2.close ();
		assertCondition (v.equals ("JDBC 2.0                                          "));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Update a CHAR, when the value is too big.
    **/
    public void Var041 ()
    {
	if(checkJdbc20 ())
	{
	    int expectedColumn = -1;
	    try
	    {
		JDRSTest.position (rs_, key_);
		expectedColumn = rs_.findColumn ("C_CHAR_1");
		rs_.updateString ("C_CHAR_1", "Data conversion");
		rs_.updateRow ();
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		DataTruncation dt = (DataTruncation)e;
		assertCondition ((dt.getIndex() == expectedColumn)
				 && (dt.getParameter() == false)
				 && (dt.getRead() == false)
				 && (dt.getDataSize() == 15)
				 && (dt.getTransferSize() == 1));

	    }
	}
    }



    /**
    updateString() - Update a VARCHAR.
    **/
    public void Var042 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_VARCHAR_50", "Message files");
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		String v = rs2.getString ("C_VARCHAR_50");
		rs2.close ();
		assertCondition (v.equals ("Message files"));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Update a VARCHAR, when the value is too big.
    **/
    public void Var043 ()
    {
	if(checkJdbc20 ())
	{
	    int expectedColumn = -1;
	    try
	    {
		JDRSTest.position (rs_, key_);
		expectedColumn = rs_.findColumn ("C_VARCHAR_50");
		rs_.updateString ("C_VARCHAR_50", "This string is really long and is testing data truncation.");
		rs_.updateRow ();
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		DataTruncation dt = (DataTruncation)e;
		assertCondition ((dt.getIndex() == expectedColumn)
				 && (dt.getParameter() == false)
				 && (dt.getRead() == false)
				 && (dt.getDataSize() == 58)
				 && (dt.getTransferSize() == 50));

	    }
	}
    }



    /**
    updateString() - Update a BINARY.
    **/
    public void Var044 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_BINARY_20", "F1F2F3F4F5");
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		String v = rs2.getString ("C_BINARY_20");
		rs2.close ();
		assertCondition (v.startsWith ("F1F2F3F4F5"));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Update a BINARY, when the value is too big.
    **/
    public void Var045 ()
    {
	if(checkJdbc20 ())
	{
	    int expectedColumn = -1;
	    try
	    {
		JDRSTest.position (rs_, key_);
		expectedColumn = rs_.findColumn ("C_BINARY_1");
		rs_.updateString ("C_BINARY_1", "F1F2F3F4");
		rs_.updateRow ();
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		DataTruncation dt = (DataTruncation)e;
		assertCondition ((dt.getIndex() == expectedColumn)
				 && (dt.getParameter() == false)
				 && (dt.getRead() == false)
				 && (dt.getTransferSize() == 1));
	    }
	}

    }



    /**
    updateString() - Update a VARBINARY.
    **/
    public void Var046 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_VARBINARY_20", "F1");
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		String v = rs2.getString ("C_VARBINARY_20");
		rs2.close ();
		assertCondition (v.equals ("F1"));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Update a VARBINARY, when the value is too big.
    **/
    public void Var047 ()
    {
	if(checkJdbc20 ())
	{
	    int expectedColumn = -1;
	    try
	    {
		JDRSTest.position (rs_, key_);
		expectedColumn = rs_.findColumn ("C_VARBINARY_20");
		rs_.updateString ("C_VARBINARY_20", "F1F2F3F4F5F1F2F3F4F5F1F2F3F4F5F1F2F3F4F5F1F2F3F4F5F1F2F3F4F5");
		rs_.updateRow ();
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		DataTruncation dt = (DataTruncation)e;
		assertCondition ((dt.getIndex() == expectedColumn)
				 && (dt.getParameter() == false)
				 && (dt.getRead() == false)
				 && (dt.getTransferSize() == 20));
	    }
	}
    }



    /**
    updateString() - Update a CLOB.

    SQL400 - the native driver expects this update to work correctly.
    **/
    public void Var048 ()
    {
	if(checkJdbc20 ())
	{
	    if(checkLobSupport ())
	    {
		try
		{
		    JDRSTest.position (rs_, key_);
		    rs_.updateString ("C_CLOB", "Toolbox");

		    rs_.updateRow ();
		    ResultSet rs2 = statement2_.executeQuery (select_);
		    JDRSTest.position (rs2, key_);
		    String v = rs2.getString ("C_CLOB");
		    rs2.close ();
		    assertCondition (v.equals ("Toolbox"));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }



    /**
    updateString() - Update a DBCLOB.

    SQL400 - the native driver expects this update to work correctly.
    **/
    public void Var049 ()
    {
	if(checkJdbc20 ())
	{
	    if(checkLobSupport ())
	    {
		try
		{
		    JDRSTest.position (rs_, key_);
		    rs_.updateString ("C_DBCLOB", "For Java");

		    rs_.updateRow ();
		    ResultSet rs2 = statement2_.executeQuery (select_);
		    JDRSTest.position (rs2, key_);
		    String v = rs2.getString ("C_DBCLOB");
		    rs2.close ();
		    assertCondition (v.equals ("For Java"));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }



    /**
    updateString() - Update a BLOB.
    **/
    public void Var050 ()
    {
	if(checkJdbc20 ())
	{
	    if(checkLobSupport ())
	    {
		try
		{
		    JDRSTest.position (rs_, key_);
		    rs_.updateString ("C_BLOB", "JDK 1.2");
		    failed ("Didn't throw SQLException");
		}
		catch(Exception e)
		{
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
    }



    /**
    updateString() - Update a DATE.
    **/
    public void Var051 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);

		// TODO:  Start supporting these different date styles.
		if(isToolboxDriver())
		    rs_.updateString ("C_DATE", "02-10-70");
		else
		    rs_.updateString ("C_DATE", "1970-02-10");

		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		Date v = rs2.getDate ("C_DATE");
		rs2.close ();
		assertCondition (v.toString ().equals ("1970-02-10"));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Update a DATE, when the string is not a valid date.
    **/
    public void Var052 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_DATE", "Emerging markets");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Update a TIME.
    **/
    public void Var053 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_TIME", "07:24:34");
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		Time v = rs2.getTime ("C_TIME");
		rs2.close ();
		assertCondition (v.toString ().equals ("07:24:34"));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Update a TIME, when the string is not a valid time.
    **/
    public void Var054 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_TIME", "V4R4");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Update a TIMESTAMP.
    **/
    public void Var055 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_TIMESTAMP", "1975-04-30 07:24:34.543");
		rs_.updateRow ();
		ResultSet rs2 = statement2_.executeQuery (select_);
		JDRSTest.position (rs2, key_);
		Timestamp v = rs2.getTimestamp ("C_TIMESTAMP");
		rs2.close ();
		assertCondition (v.toString ().equals ("1975-04-30 07:24:34.543"));
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }



    /**
    updateString() - Update a TIMESTAMP, when the string is not a vliad timestamp.
    **/
    public void Var056 ()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		JDRSTest.position (rs_, key_);
		rs_.updateString ("C_TIMESTAMP", "Beta web page");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



    /**
    updateString() - Update a DATALINK.
    **/
    public void Var057 ()
    {
	if(checkJdbc20 ())
	{
	    if(checkLobSupport ())
	    {
		// We do not test updating datalinks, since it is not 
		// possible to open a updatable cursor/result set with
		// a datalink column.
		notApplicable("DATALINK update not supported.");
		/*
		try {
		    JDRSTest.position (rs_, key_);
		    rs_.updateString ("C_DATALINK", "Secure sockets");
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
    updateString() - Update a DISTINCT.
    **/
    public void Var058 ()
    {
	if(checkJdbc20 ())
	{
	    if(checkLobSupport ())
	    {
		try
		{
		    JDRSTest.position (rs_, key_);
		    rs_.updateString ("C_DISTINCT", "AS/400");
		    rs_.updateRow ();
		    ResultSet rs2 = statement2_.executeQuery (select_);
		    JDRSTest.position (rs2, key_);
		    String v = rs2.getString ("C_DISTINCT");
		    rs2.close ();
		    assertCondition (v.equals ("AS/400   "));
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }



    /**
    updateString() - Update a BIGINT.
    **/
    public void Var059()
    {
	if(checkJdbc20 ())
	{
	    if(checkBigintSupport())
	    {
		try
		{
		    JDRSTest.position (rs_, key_);
		    rs_.updateString ("C_BIGINT", "999228374");
		    rs_.updateRow ();
		    ResultSet rs2 = statement2_.executeQuery (select_);
		    JDRSTest.position (rs2, key_);
		    long v = rs2.getLong ("C_BIGINT");
		    rs2.close ();
		    assertCondition (v == 999228374);
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }



    /**
    updateString() - Update a BIGINT, when the integer is too big.
    **/
    public void Var060()
    {
	if(checkJdbc20 ())
	{
	    if(checkBigintSupport())
	    {
		try
		{
		    JDRSTest.position (rs_, key_);
		    rs_.updateString ("C_BIGINT", "-44332245453454325353487623");
		    failed ("Didn't throw SQLException");
		}
		catch(Exception e)
		{
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
    }



    /**
    updateString() - Update an BIGINT, when the string is not a number.
    **/
    public void Var061()
    {
	if(checkJdbc20 ())
	{
	    if(checkBigintSupport())
	    {
		try
		{
		    JDRSTest.position (rs_, key_);
		    rs_.updateString ("C_BIGINT", "System values");
		    failed ("Didn't throw SQLException");
		}
		catch(Exception e)
		{
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
    }


    /**
    updateString() - column names with quotes.
    **/
    public void Var062()
    {
	if(checkJdbc20 ())
	{
	    try
	    {
		Statement statement = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
								  ResultSet.CONCUR_UPDATABLE);
		
		initTable(statement,  JDRSTest.COLLECTION + ".\"MixedC\" ","( "   +
				  "\"MixedCaseField\" VARCHAR (10), "   +
				  "MIXEDCASEFIELD  VARCHAR (10), "    +
				  "\"Mixed2\" VARCHAR(20) )" );

		ResultSet rs = statement.executeQuery("SELECT * FROM " + JDRSTest.COLLECTION + ".\"MixedC\" FOR UPDATE");
		rs.moveToInsertRow();

		// Since the names do not include quotes the first two updates will
		// both update the FIRST column.  The first column name is
		// MixedCaseField which matches MixedCaseField.                                            
		rs.updateString("MixedCaseField", "Data");
		rs.updateString("MIXEDCASEFIELD", "MoreData");
		rs.updateString("\"Mixed2\"", "mixed2 data");

		rs.insertRow();

		// Now the first updateString is correct.  I will update the 
		// first column.                                                         
		rs.updateString("\"MixedCaseField\"", "Data");
		rs.updateString("\"MIXEDCASEFIELD\"", "MoreData");
		rs.updateString("\"Mixed2\"", "mixed2 data");

		rs.insertRow();

		rs.close();
		statement.close();

		PreparedStatement insert = connection_.prepareStatement ("INSERT INTO " + JDRSTest.COLLECTION + ".\"MixedC\" "
									 + " (\"MixedCaseField\", MIXEDCASEFIELD, \"Mixed2\") "
									 + " VALUES (?, ?, ?)");
		insert.setString(1, "Data");
		insert.setString(2, "MoreData");
		insert.setString(3, "mixed2 data");
		insert.executeUpdate();
		insert.close();

		statement = connection_.createStatement();

		rs = statement.executeQuery("SELECT * FROM " + JDRSTest.COLLECTION + ".\"MixedC\" ");

		boolean successful = true;

		rs.next();

		String col1 = rs.getString(1);
		String col2 = rs.getString(2);
		String col3 = rs.getString(3);

		//toolbox has fixed this
		if( isToolboxDriver() )
		{
		    if(!"Data".equals(col1) || 
		       !"MoreData".equals(col2) || 
		       !"mixed2 data".equals(col3))
		    {
			failed("ROW 1: data does not match:  '"+col1+"'!='Data' '"+col2+"'!='MoreData' '"+col3+"'!='mixed2 data'");
			successful = false;
		    }
		}else
		{
		    if(!"MoreData".equals(col1) || 
		       col2 != null || 
		       !"mixed2 data".equals(col3))
		    {
			failed("ROW 1: data does not match:  '"+col1+"'!='MoreData' '"+col2+"'!=null '"+col3+"'!='mixed2 data'");
			successful = false;
		    }
		}

		if(successful)
		{
		    rs.next();
		    col1 = rs.getString(1);
		    col2 = rs.getString(2);
		    col3 = rs.getString(3);

		    if(!"Data".equals(col1) || 
		       !"MoreData".equals(col2) || 
		       !"mixed2 data".equals(col3))
		    {
			failed("ROW 2: data does not match:  '"+col1+"'!='Data' '"+col2+"'!='MoreData' '"+col3+"'!='mixed2 data'");
			successful = false;
		    }
		}

		if(successful)
		{
		    rs.next();
		    col1 = rs.getString(1);
		    col2 = rs.getString(2);
		    col3 = rs.getString(3);

		    if(!"Data".equals(col1) || 
		       !"MoreData".equals(col2) || 
		       !"mixed2 data".equals(col3))
		    {
			failed("ROW 3:  data does not match:  '"+col1+"'!='Data' '"+col2+"'!='MoreData' '"+col3+"'!='mixed2 data'");
			successful = false;
		    }
		}

		if(successful)
		    succeeded();

		rs.close();
		statement.close();
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}

    }

    /**
     * updateString -- update a CCSID 5035 CHAR field when the job ccsid is 5035
     *                 then use updateRow 
     *
     * NOTE:  This testcase should be last since it changes the job ccsid
     */
    public void Var063()
    {
	if(checkJdbc20 ()) {
	    if (checkNative()) {
		try { 
		// Determine our job name
		    String jobname = JDJobName.getJobName();
		// Change the CCSID 
		    String command = "chgjob "+jobname+" CCSID(5035)";
		    String sql = "CALL QSYS.QCMDEXC('"+command+"', 00000000"+command.length()+".00000)";
		    // System.out.println("executing "+sql);
		    Statement stmt = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
								 ResultSet.CONCUR_UPDATABLE);
		    stmt.execute(sql); 


		    //
		    // Create a new statement that will use the new job CCSID... 
		    // 
		    stmt = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_UPDATABLE);


		    String tablename =JDRSTest.COLLECTION+".JDCCSID5035";

		    

		    initTable(stmt, tablename,"(id vargraphic(30) CCSID 13488, name char(20) CCSID 5035)");
		    stmt.executeUpdate("insert into "+tablename+" values('A01', '')"); 

		    String   DAT0 = "\uFF11\uFF12\uFF13";
		    sql = "select NAME from "+tablename+" where id = 'A01' FOR UPDATE";
		    ResultSet rs = stmt.executeQuery(sql);
		    rs.first();
		    rs.updateString("NAME",DAT0);
		    rs.updateRow();

		    rs.close();
		    // add the commit here so we can debug this.. 
		    connection_.commit();

		    sql = "select NAME from "+tablename+" where id = 'A01'";
		    rs = stmt.executeQuery(sql);
		    rs.next();
		    String updatedString = rs.getString(1); 
		    rs.close();
		    cleanupTable(stmt, tablename);
		    stmt.close();
		    connection_.commit();
		    String check = updatedString.substring(0,3); 
		    assertCondition(DAT0.equals(check), "Error "+hexString(DAT0)+"!="+hexString(check)+"<-"+hexString(updatedString)+" new native testcase 12/03/2003");

		} catch (Exception e) {
		    failed(e, "Unexpected exception -- new native testcase added 12/2/2003"); 
		}
	    }
	}

    }

    /**
     * updateString -- update a CCSID 5035 CHAR field when the job ccsid is 5035
     *                 then use insertRow 
     *
     * NOTE:  This testcase should be last since it changes the job ccsid
     */
    public void Var064()
    {
	if(checkJdbc20 ()) {
	    if (checkNative()) {
		try { 
		    // Determine our job name
		    String jobname = JDJobName.getJobName();
		    // Change the CCSID 
		    String command = "chgjob "+jobname+" CCSID(5035)";
		    String sql = "CALL QSYS.QCMDEXC('"+command+"', 00000000"+command.length()+".00000)";
		    // System.out.println("executing "+sql);
		    Statement stmt = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
								 ResultSet.CONCUR_UPDATABLE);
		    stmt.execute(sql); 

		    //
		    // Create a new statement that will use the new job CCSID... 
		    // 
		    stmt = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_UPDATABLE);


		    String tablename =JDRSTest.COLLECTION+".JDCCSID50352";

		   

		    initTable(stmt, tablename,"(id vargraphic(30) CCSID 13488, name char(20) CCSID 5035)");
		    stmt.executeUpdate("insert into "+tablename+" values('A01', '')"); 

		    String   DAT0 = "\uFF11\uFF12\uFF13";
		    sql = "select ID, NAME from "+tablename+" where id = 'A01' FOR UPDATE";
		    ResultSet rs = stmt.executeQuery(sql);
		    rs.moveToInsertRow();
		    rs.updateString(2, DAT0);
		    rs.updateString(1, "A02"); 
		    rs.insertRow();

		    rs.close();
		    sql = "select NAME from "+tablename+" where id = 'A02'";
		    rs = stmt.executeQuery(sql);
		    rs.next();
		    String updatedString = rs.getString(1); 
		    rs.close();
		    cleanupTable(stmt, tablename);
		    stmt.close();
		    connection_.commit();

		    String check = updatedString.trim(); 
		    assertCondition(DAT0.equals(check), "Error "+hexString(DAT0)+"!="+hexString(check)+"<-"+hexString(updatedString)+" new native testcase 12/03/2003");


		} catch (Exception e) {
		    failed(e, "Unexpected exception -- new native testcase added 12/2/2003"); 
		}
	    }
	}

    }


    public void dfpTest(String table, String value, String expected) {
	if (checkDecFloatSupport()) {
	    try {
		Statement s = connection_.createStatement(
							  ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = s.executeQuery("SELECT * FROM " + table + " FOR UPDATE ");
		rs.next();
		String originalValue = rs.getString(1); 
		rs.updateString(1, value);
		rs.updateRow();
		rs.close(); 

		ResultSet rs2 = s.executeQuery("SELECT * FROM " + table);
		rs2.next();
		String v = rs2.getString(1);
		rs2.close();


		ResultSet rs3 = s.executeQuery("SELECT * FROM " + table + " FOR UPDATE ");
		rs3.next();
		rs3.updateString(1, originalValue);
		rs3.updateRow();
		rs3.close(); 

		s.close();

		try {
		    connection_.commit(); 
		} catch (Exception e) {} 
		assertCondition(((v==null) && (expected == null)) || 
				(v != null && v.equals(expected)), 
				"Got " + v + " from "+ value +" sb " + expected);
	    } catch (Exception e) {
		failed(e, "Unexpected Exception for value "+value);
	    }
	}
    }

    public void dfpRoundTest(String roundingMode, String table, String value, String expected) {
	if (checkDecFloatSupport()) {
	    try {
		String roundingModeProp = roundingMode;
		if(isToolboxDriver())
		    roundingModeProp = roundingModeProp.substring(6);  //without "rounding " string

		String originalValue = null; 
		String url = baseURL_
		  
		  
		  + ";data truncation=true" 
		  + ";decfloat rounding mode="+roundingModeProp;
		Connection connection = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

		Statement s = connection.createStatement(
							 ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = s.executeQuery("SELECT * FROM " + table + " FOR UPDATE ");
		rs.next();
		originalValue = rs.getString(1); 
		rs.updateString(1, value);
		rs.updateRow();
	  // Close the cursor so the next job can get at it 
		rs.close(); 

		ResultSet rs2 = s.executeQuery("SELECT * FROM " + table);
		rs2.next();
		String v = rs2.getString(1);
		rs2.close();


		ResultSet rs3 = s.executeQuery("SELECT * FROM " + table + " FOR UPDATE ");
		rs3.next();
		rs3.updateString(1, originalValue);
		rs3.updateRow();
		rs3.close(); 

		s.close();

		connection.close(); 
		assertCondition(v.equals(expected), "Got " + v + " sb " + expected +" from "+value+" for mode "+roundingMode);
	    } catch (Exception e) {
		failed(e, "Unexpected Exception for value "+ value);
	    }
	}
    }

    /**
     * updateString -- set DFP16 to different values and retrieve
     */
    public void Var065 () { dfpTest(JDRSTest.RSTEST_DFP16, "4533.43", "4533.43"); }
    public void Var066 () { dfpTest(JDRSTest.RSTEST_DFP16, "NaN", "NaN");} 
    public void Var067 () { dfpTest(JDRSTest.RSTEST_DFP16, "NAN", "NaN");} 
    public void Var068 () { dfpTest(JDRSTest.RSTEST_DFP16, "+NaN", "NaN");} 
    public void Var069 () { dfpTest(JDRSTest.RSTEST_DFP16, "-NaN", "-NaN");} 
    public void Var070 () { dfpTest(JDRSTest.RSTEST_DFP16, "QNaN", "NaN");} 
    public void Var071 () { dfpTest(JDRSTest.RSTEST_DFP16, "+QNaN", "NaN");} 
    public void Var072 () { dfpTest(JDRSTest.RSTEST_DFP16, "-QNaN", "-NaN");} 
    public void Var073 () { dfpTest(JDRSTest.RSTEST_DFP16, "SNaN", "SNaN");} 
    public void Var074 () { dfpTest(JDRSTest.RSTEST_DFP16, "+SNaN", "SNaN");} 
    public void Var075 () { dfpTest(JDRSTest.RSTEST_DFP16, "-SNaN", "-SNaN");} 
    public void Var076 () { dfpTest(JDRSTest.RSTEST_DFP16, "INF", "Infinity");}
    public void Var077 () { dfpTest(JDRSTest.RSTEST_DFP16, "+INF", "Infinity");}
    public void Var078 () { dfpTest(JDRSTest.RSTEST_DFP16, "-INF", "-Infinity");}
    public void Var079 () { dfpTest(JDRSTest.RSTEST_DFP16, "Infinity", "Infinity");}
    public void Var080 () { dfpTest(JDRSTest.RSTEST_DFP16, "+Infinity", "Infinity");}
    public void Var081 () { dfpTest(JDRSTest.RSTEST_DFP16, "-Infinity", "-Infinity");}
    public void Var082 () { dfpTest(JDRSTest.RSTEST_DFP16, "1234567890123456", "1234567890123456");} 
    public void Var083 () { dfpTest(JDRSTest.RSTEST_DFP16, "-1234567890123456", "-1234567890123456");}
    public void Var084 () { dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456","1234567890123456");}
    public void Var085 () {
	
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456E28","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456E28","1.234567890123456E+43");
	}
    }
    public void Var086 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456E+28","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456E+28","1.234567890123456E+43");
	}
    }
    public void Var087 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123456789012345.6E+29","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123456789012345.6E+29","1.234567890123456E+43");
	}
    }
    public void Var088 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+12345678901234.56E+30","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+12345678901234.56E+30","1.234567890123456E+43");
	}
    }
    public void Var089 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123.456E+31","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123.456E+31","1.234567890123456E+43");
	}
    }
    public void Var090 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123456789012.3456E+32","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123456789012.3456E+32","1.234567890123456E+43");
	}
    }
    public void Var091 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+12345678901.23456E+33","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+12345678901.23456E+33","1.234567890123456E+43");
	}
    }
    public void Var092 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890.123456E+34","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890.123456E+34","1.234567890123456E+43");
	}
    }
    public void Var093 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123456789.0123456E+35","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123456789.0123456E+35","1.234567890123456E+43");
	}
    }
    public void Var094 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+12345678.90123456E+36","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+12345678.90123456E+36","1.234567890123456E+43");
	}
    }
    public void Var095 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567.890123456E+37","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567.890123456E+37","1.234567890123456E+43");
	}
    }
    public void Var096 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123456.7890123456E+38","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123456.7890123456E+38","1.234567890123456E+43");
	}
    }
    public void Var097 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+12345.67890123456E+39","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+12345.67890123456E+39","1.234567890123456E+43");
	}
    }
    public void Var098 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234.567890123456E+40","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234.567890123456E+40","1.234567890123456E+43");
	}
    }
    public void Var099 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123.4567890123456E+41","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123.4567890123456E+41","1.234567890123456E+43");
	}
    }
    public void Var100 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+12.34567890123456E+42","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+12.34567890123456E+42","1.234567890123456E+43");
	}
    }
    public void Var101 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1.234567890123456E+43","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1.234567890123456E+43","1.234567890123456E+43");
	}
    }
    public void Var102 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+.1234567890123456E+44","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+.1234567890123456E+44","1.234567890123456E+43");
	}
    }
    public void Var103 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+0.1234567890123456E+44","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+0.1234567890123456E+44","1.234567890123456E+43");
	}
    }
    public void Var104 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+0.01234567890123456E+45","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "+0.01234567890123456E+45","1.234567890123456E+43");
	}
    }
    public void Var105 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "-1234567890123456E28","-12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP16, "-1234567890123456E28","-1.234567890123456E+43");
	}
    }
    public void Var106 () { dfpTest(JDRSTest.RSTEST_DFP16, "1E0", "1");}
    public void Var107 () { dfpTest(JDRSTest.RSTEST_DFP16, "1.1", "1.1");} 
    public void Var108 () { dfpTest(JDRSTest.RSTEST_DFP16, "1.1E0", "1.1");}
    public void Var109 () { dfpTest(JDRSTest.RSTEST_DFP16, null, null);}


    /*
     * updateString -- using different rounding modes 
     */
    String RHE="round half even"; 
    public void Var110 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP16,  "1.2345678901234545",  "1.234567890123454"); }
    public void Var111 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP16,  "1.2345678901234555",  "1.234567890123456"); }
    public void Var112 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP16, "-1.2345678901234545", "-1.234567890123454"); }
    public void Var113 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP16, "-1.2345678901234555", "-1.234567890123456"); }

    /** 
     *  updateFloat -- set a DFP16 with rounding mode "round half up"   
     */
    String RHU = "round half up"; 
    public void Var114 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP16, "1.2345678901234555", "1.234567890123456"); }
    public void Var115 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP16, "1.2345678901234545", "1.234567890123455"); }
    public void Var116 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP16, "1.2345678901234565", "1.234567890123457"); }
    public void Var117 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP16, "-1.2345678901234555", "-1.234567890123456"); }
    public void Var118 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP16, "-1.2345678901234545", "-1.234567890123455"); }
    public void Var119 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP16, "-1.2345678901234565", "-1.234567890123457"); }

    /** 
     *  updateFloat -- set a DFP16 with rounding mode "round down"   
     */
    String RD = "round down";
    public void Var120 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP16, "1.2345678901234555",       "1.234567890123455"); }
    public void Var121 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP16, "1.2345678901234559999999", "1.234567890123455"); }
    public void Var122 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP16, "-1.2345678901234555",       "-1.234567890123455"); }
    public void Var123 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP16, "-1.2345678901234559999999", "-1.234567890123455"); }



    /** 
     *  updateFloat -- set a DFP16 with rounding mode "round ceiling"   
     */
    String RC = "round ceiling";
    public void Var124 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP16, "1.2345678901234555",       "1.234567890123456"); }
    public void Var125 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP16, "1.2345678901234559999999", "1.234567890123456"); }
    public void Var126 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP16, "-1.2345678901234555",       "-1.234567890123455"); }
    public void Var127 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP16, "-1.2345678901234559999999", "-1.234567890123455"); }


    /** 
     *  updateFloat -- set a DFP16  with rounding mode "round floor"   
     */
    String RF = "round floor";
    public void Var128 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP16, "1.2345678901234555",       "1.234567890123455"); }
    public void Var129 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP16, "1.2345678901234559999999", "1.234567890123455"); }
    public void Var130 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP16, "-1.2345678901234555",       "-1.234567890123456"); }
    public void Var131 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP16, "-1.2345678901234559999999", "-1.234567890123456"); }


    /** 
     *  updateFloat -- set a DFP16 with rounding mode "round half down"   
     */
    String RHD = "round half down"; 
    public void Var132 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP16, "1.2345678901234555", "1.234567890123455"); }
    public void Var133 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP16, "1.2345678901234545", "1.234567890123454"); }
    public void Var134 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP16, "1.2345678901234565", "1.234567890123456"); }
    public void Var135 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP16, "-1.2345678901234555", "-1.234567890123455"); }
    public void Var136 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP16, "-1.2345678901234545", "-1.234567890123454"); }
    public void Var137 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP16, "-1.2345678901234565", "-1.234567890123456"); }



   /** 
     *  updateFloat -- set a DFP16 with rounding mode "round up"   
     */
    String RU = "round up";
    public void Var138 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP16, "1.2345678901234555",       "1.234567890123456"); }
    public void Var139 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP16, "1.2345678901234559999999", "1.234567890123456"); }
    public void Var140 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP16, "-1.2345678901234555",       "-1.234567890123456"); }
    public void Var141 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP16, "-1.2345678901234559999999", "-1.234567890123456"); }



    /**
     * updateString -- set DFP34 to different values and retrieve
     */
    public void Var142 () { dfpTest(JDRSTest.RSTEST_DFP34, "4533.43", "4533.43"); }
    public void Var143 () { dfpTest(JDRSTest.RSTEST_DFP34, "NaN", "NaN");} 
    public void Var144 () { dfpTest(JDRSTest.RSTEST_DFP34, "NAN", "NaN");} 
    public void Var145 () { dfpTest(JDRSTest.RSTEST_DFP34, "+NaN", "NaN");} 
    public void Var146 () { dfpTest(JDRSTest.RSTEST_DFP34, "-NaN", "-NaN");} 
    public void Var147 () { dfpTest(JDRSTest.RSTEST_DFP34, "QNaN", "NaN");} 
    public void Var148 () { dfpTest(JDRSTest.RSTEST_DFP34, "+QNaN", "NaN");} 
    public void Var149 () { dfpTest(JDRSTest.RSTEST_DFP34, "-QNaN", "-NaN");} 
    public void Var150 () { dfpTest(JDRSTest.RSTEST_DFP34, "SNaN", "SNaN");} 
    public void Var151 () { dfpTest(JDRSTest.RSTEST_DFP34, "+SNaN", "SNaN");} 
    public void Var152 () { dfpTest(JDRSTest.RSTEST_DFP34, "-SNaN", "-SNaN");} 
    public void Var153 () { dfpTest(JDRSTest.RSTEST_DFP34, "INF", "Infinity");}
    public void Var154 () { dfpTest(JDRSTest.RSTEST_DFP34, "+INF", "Infinity");}
    public void Var155 () { dfpTest(JDRSTest.RSTEST_DFP34, "-INF", "-Infinity");}
    public void Var156 () { dfpTest(JDRSTest.RSTEST_DFP34, "Infinity", "Infinity");}
    public void Var157 () { dfpTest(JDRSTest.RSTEST_DFP34, "+Infinity", "Infinity");}
    public void Var158 () { dfpTest(JDRSTest.RSTEST_DFP34, "-Infinity", "-Infinity");}
    public void Var159 () { dfpTest(JDRSTest.RSTEST_DFP34, "1234567890123456", "1234567890123456");} 
    public void Var160 () { dfpTest(JDRSTest.RSTEST_DFP34, "-1234567890123456", "-1234567890123456");}
    public void Var161 () { dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890123456","1234567890123456");}
    public void Var162 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890123456E28","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890123456E28","1.234567890123456E+43");
	}
    }
    public void Var163 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890123456E+28","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890123456E+28","1.234567890123456E+43");
	}
    }
    public void Var164 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+123456789012345.6E+29","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+123456789012345.6E+29","1.234567890123456E+43");
	}
    }
    public void Var165 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+12345678901234.56E+30","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+12345678901234.56E+30","1.234567890123456E+43");
	}
    }
    public void Var166 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890123.456E+31","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890123.456E+31","1.234567890123456E+43");
	}
    }
    public void Var167 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+123456789012.3456E+32","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+123456789012.3456E+32","1.234567890123456E+43");
	}
    }
    public void Var168 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+12345678901.23456E+33","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+12345678901.23456E+33","1.234567890123456E+43");
	}
    }
    public void Var169 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890.123456E+34","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890.123456E+34","1.234567890123456E+43");
	}
    }
    public void Var170 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+123456789.0123456E+35","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+123456789.0123456E+35","1.234567890123456E+43");
	}
    }
    public void Var171 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+12345678.90123456E+36","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+12345678.90123456E+36","1.234567890123456E+43");
	}
    }
    public void Var172 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234567.890123456E+37","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234567.890123456E+37","1.234567890123456E+43");
	}
    }
    public void Var173 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+123456.7890123456E+38","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+123456.7890123456E+38","1.234567890123456E+43");
	}
    }
    public void Var174 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+12345.67890123456E+39","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+12345.67890123456E+39","1.234567890123456E+43");
	}
    }
    public void Var175 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234.567890123456E+40","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234.567890123456E+40","1.234567890123456E+43");
	}
    }
    public void Var176 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+123.4567890123456E+41","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+123.4567890123456E+41","1.234567890123456E+43");
	}
    }
    public void Var177 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+12.34567890123456E+42","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+12.34567890123456E+42","1.234567890123456E+43");
	}
    }
    public void Var178 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1.234567890123456E+43","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1.234567890123456E+43","1.234567890123456E+43");
	}
    }
    public void Var179 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+.1234567890123456E+44","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+.1234567890123456E+44","1.234567890123456E+43");
	}
    }
    public void Var180 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+0.1234567890123456E+44","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+0.1234567890123456E+44","1.234567890123456E+43");
	}
    }
    public void Var181 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+0.01234567890123456E+45","12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "+0.01234567890123456E+45","1.234567890123456E+43");
	}
    }
    public void Var182 () {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver())  && isjdk14_) { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "-1234567890123456E28","-12345678901234560000000000000000000000000000");
	} else { 
	    dfpTest(JDRSTest.RSTEST_DFP34, "-1234567890123456E28","-1.234567890123456E+43");
	}
    }
    public void Var183 () { dfpTest(JDRSTest.RSTEST_DFP34, "1E0", "1");}
    public void Var184 () { dfpTest(JDRSTest.RSTEST_DFP34, "1.1", "1.1");} 
    public void Var185 () { dfpTest(JDRSTest.RSTEST_DFP34, "1.1E0", "1.1");}
    public void Var186 () { dfpTest(JDRSTest.RSTEST_DFP34, null, null);}


    /*
     * updateString -- using different rounding modes
     */
    public void Var187 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP34,  "1.1818181818181818182345678901234545",  "1.181818181818181818234567890123454"); }
    public void Var188 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP34,  "1.1818181818181818182345678901234555",  "1.181818181818181818234567890123456"); }
    public void Var189 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234545", "-1.181818181818181818234567890123454"); }
    public void Var190 () { dfpRoundTest(RHE, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234555", "-1.181818181818181818234567890123456"); }
 
    /** 
     *  updateFloat -- set a DFP34 with rounding mode "round half up"   
     */
    public void Var191 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234555",   "1.181818181818181818234567890123456"); }
    public void Var192 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234545",   "1.181818181818181818234567890123455"); }
    public void Var193 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234565",   "1.181818181818181818234567890123457"); }
    public void Var194 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234555", "-1.181818181818181818234567890123456"); }
    public void Var195 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234545", "-1.181818181818181818234567890123455"); }
    public void Var196 () { dfpRoundTest(RHU, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234565", "-1.181818181818181818234567890123457"); }

    /** 
     *  updateFloat -- set a DFP34 with rounding mode "round down"   
     */
    public void Var197 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234555",       "1.181818181818181818234567890123455"); }
    public void Var198 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234559999999", "1.181818181818181818234567890123455"); }
    public void Var199 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234555",       "-1.181818181818181818234567890123455"); }
    public void Var200 () { dfpRoundTest(RD, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234559999999", "-1.181818181818181818234567890123455"); }


      
    /** 
     *  updateFloat -- set a DFP34 with rounding mode "round ceiling"   
     */
    public void Var201 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234555",       "1.181818181818181818234567890123456"); }
    public void Var202 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234559999999", "1.181818181818181818234567890123456"); }
    public void Var203 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234555",       "-1.181818181818181818234567890123455"); }
    public void Var204 () { dfpRoundTest(RC, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234559999999", "-1.181818181818181818234567890123455"); }


    /** 
     *  updateFloat -- set a DFP34  with rounding mode "round floor"   
     */
    public void Var205 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234555",       "1.181818181818181818234567890123455"); }
    public void Var206 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234559999999", "1.181818181818181818234567890123455"); }
    public void Var207 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234555",       "-1.181818181818181818234567890123456"); }
    public void Var208 () { dfpRoundTest(RF, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234559999999", "-1.181818181818181818234567890123456"); }


    /** 
     *  updateFloat -- set a DFP34 with rounding mode "round half down"   
     */
    public void Var209 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234555", "1.181818181818181818234567890123455"); }
    public void Var210 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234545", "1.181818181818181818234567890123454"); }
    public void Var211 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234565", "1.181818181818181818234567890123456"); }
    public void Var212 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234555", "-1.181818181818181818234567890123455"); }
    public void Var213 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234545", "-1.181818181818181818234567890123454"); }
    public void Var214 () { dfpRoundTest(RHD, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234565", "-1.181818181818181818234567890123456"); }



   /** 
     *  updateFloat -- set a DFP34 with rounding mode "round up"   
     */
    public void Var215 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234555",       "1.181818181818181818234567890123456"); }
    public void Var216 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP34, "1.1818181818181818182345678901234559999999", "1.181818181818181818234567890123456"); }
    public void Var217 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234555",       "-1.181818181818181818234567890123456"); }
    public void Var218 () { dfpRoundTest(RU, JDRSTest.RSTEST_DFP34, "-1.1818181818181818182345678901234559999999", "-1.181818181818181818234567890123456"); }



        /**
    updateBoolean() - Update a BOOLEAN.
    **/
  public void updateBoolean(String inString, String outString) {
    if (checkJdbc20()) {
      if (checkBooleanSupport()) {
        try {
          JDRSTest.position(rs_, key_);
          rs_.updateString("C_BOOLEAN", inString);
          rs_.updateRow();
          ResultSet rs2 = statement2_.executeQuery(select_);
          JDRSTest.position(rs2, key_);
          String v = rs2.getString("C_BOOLEAN");
          rs2.close();
          assertCondition(outString.equals(v), "Got "+v+" sb "+outString);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  public void Var219() {
    updateBoolean("1", "1");
  }

  public void Var220() {
    updateBoolean("0", "0");
  }

  public void Var221() {
    updateBoolean("1000", "1");
  }

  public void Var222() {
    updateBoolean("-100", "1");
  }

  public void Var223() {
    updateBoolean("true", "1");
  }

  public void Var224() {
    updateBoolean("false", "0");
  }

  public void Var225() {
    updateBoolean("T", "1");
  }

  public void Var226() {
    updateBoolean("F", "0");
  }

  public void Var227() {
    updateBoolean("yes", "1");
  }

  public void Var228() {
    updateBoolean("no", "0");
  }

    private String hexString(String s) {
	String out="";
	for (int i = 0; i < s.length(); i++) {
	    out += " "+Integer.toHexString(s.charAt(i)); 
	}
	return out; 
    } 

}



