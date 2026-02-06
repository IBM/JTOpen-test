///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetArray.java
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
import java.sql.Array;

import java.sql.ResultSet;

import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JDRSGetArray.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getArray()
</ul>
**/
public class JDRSGetArray
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetArray";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private Statement           statement_;
    private ResultSet           rs_;
    private String              resultSetQuery_;
    private int                 driver_; 


/**
Constructor.
**/
    public JDRSGetArray (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSGetArray",
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
        if (isJdbc20 ()) {
            // SQL400 - driver neutral...
          	if (connection_ != null) connection_.close();

            String url = baseURL_;
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(),encryptedPassword_,"JDRSGetArray");
            setAutoCommit(connection_, false);               // @C1A
	    driver_ = getDriver();
	    if (driver_ == JDTestDriver.DRIVER_JTOPENLITE) {
		statement_ = connection_.createStatement (); 
	    } else {
		statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
		ResultSet.CONCUR_UPDATABLE);
	    } 
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_GET
                + " (C_KEY) VALUES ('DUMMYROW_GA')");
	    resultSetQuery_ = "SELECT * FROM "
                + JDRSTest.RSTEST_GET + " FOR UPDATE"; 
            rs_ = statement_.executeQuery (resultSetQuery_);
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
            connection_.commit();           // @C1A
            connection_.close ();
        }
    }



/**
getArray() - Should throw exception when the result set is
closed.
**/
    public void Var001()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.close ();
            rs.getArray (1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, null);
            Array v = rs_.getArray (1);
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_POS");
            Array v = rs_.getArray (100);
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_POS");
            Array v = rs_.getArray (0);
            failed ("Didn't throw SQLException" +v +3);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_POS");
            Array v = rs_.getArray (-1);
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Should throw an exception when the column index is valid.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_POS");
            Array v = rs_.getArray (3);
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Should throw an exception when the column
name is null.
**/
    public void Var007()
    {
      if (checkJdbc20 ()) {
        try {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC throws null pointer exception when column name is null "); 
          } else { 
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_POS");
            rs_.getArray (null);
            
            failed ("Didn't throw SQLException");
          }
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }



/**
getArray() - Should throw an exception when the column
name is an empty string.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_POS");
            rs_.getArray ("");
            failed ("Didn't throw SQLException" );
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Should throw an exception when the column
name is invalid.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_POS");
            rs_.getArray ("INVALID");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Should throw an exception when the column name is valid.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_POS");
            Array v = rs_.getArray ("C_INTEGER");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Should throw an exception when an update is pending.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "UPDATE_SANDBOX");
            rs_.updateLong ("C_INTEGER", 19222228);
            Array v = rs_.getArray ("C_INTEGER");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Should throw an exception when an update has been done.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "UPDATE_SANDBOX");
            rs_.updateLong ("C_INTEGER", -1111222334);
            rs_.updateRow ();
            Array v = rs_.getArray ("C_INTEGER");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Should throw an exception when the current row is the insert
row, when an insert is pending.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateLong ("C_INTEGER", 1893);
            Array v = rs_.getArray ("C_INTEGER");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Should throw an exception when the current row is the insert
row, when an insert has been done.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateLong ("C_INTEGER", 21027);
            rs_.insertRow ();
            Array v = rs_.getArray ("C_INTEGER");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Should throw an exception on a deleted row.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "DUMMYROW_GA");
            rs_.deleteRow ();
            Array v = rs_.getArray ("C_INTEGER");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


/**
getArray() - Should throw an exception when the column is NULL.
**/
    public void Var016 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_NULL");
            Array v = rs_.getArray ("C_INTEGER");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Get from a SMALLINT.
**/
    public void Var017 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_NEG");
            Array v = rs_.getArray ("C_SMALLINT");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Get from a INTEGER.
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_POS");
            Array v = rs_.getArray ("C_INTEGER");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Get from a REAL.
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_NEG");
            Array v = rs_.getArray ("C_REAL");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Get from a FLOAT.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_POS");
            Array v = rs_.getArray ("C_FLOAT");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Get from a DOUBLE.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_NEG");
            Array v = rs_.getArray ("C_DOUBLE");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Get from a DECIMAL.
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_POS");
            Array v = rs_.getArray ("C_DECIMAL_50");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Get from a NUMERIC.
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_NEG");
            Array v = rs_.getArray ("C_NUMERIC_105");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Get from a CHAR.
**/
    public void Var024 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "CHAR_FULL");
            Array v = rs_.getArray ("C_CHAR_50");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Get from a VARCHAR.
**/
    public void Var025 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "CHAR_FULL");
            Array v = rs_.getArray ("C_VARCHAR_50");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Get from a BINARY.
**/
    public void Var026 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "BINARY_TRANS");
            Array v = rs_.getArray ("C_BINARY_20");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Get from a VARBINARY.
**/
    public void Var027 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "BINARY_TRANS");
            Array v = rs_.getArray ("C_VARBINARY_20");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Get from a CLOB.
**/
    public void Var028 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "LOB_FULL");
                Array v = rs_.getArray ("C_CLOB");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
getArray() - Get from a DBCLOB.
**/
    public void Var029 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "LOB_FULL");
                Array v = rs_.getArray ("C_DBCLOB");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
getArray() - Get from a BLOB.
**/
    public void Var030 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "LOB_FULL");
                Array v = rs_.getArray ("C_BLOB");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
getArray() - Get from a DATE.
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "DATE_1998");
            Array v = rs_.getArray ("C_DATE");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Get from a TIME.
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "DATE_1998");
            Array v = rs_.getArray ("C_TIME");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Get from a TIMESTAMP.
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "DATE_1998");
            Array v = rs_.getArray ("C_TIMESTAMP");
            failed ("Didn't throw SQLException" +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getArray() - Get from a DATALINK.
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
        if (checkDatalinkSupport ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GETDL);
                JDRSTest.position (rs, "LOB_FULL");
                Array v = rs.getArray ("C_DATALINK");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
getArray() - Get from a DISTINCT.
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "LOB_EMPTY");
                Array v = rs_.getArray ("C_DISTINCT");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
getArray() - Get from a BIGINT.
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
        if (checkBigintSupport ()) {
            try {
                rs_ = JDRSTest.position (driver_, statement_, resultSetQuery_, rs_, "NUMBER_POS");
                Array v = rs_.getArray ("C_BIGINT");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }


	/**
	getArray() - Get from DFP16:
	**/
    public void Var037 ()
    {
	if (checkDecFloatSupport()) {
	    try {
		Statement s = connection_.createStatement ();
		ResultSet rs = s.executeQuery ("SELECT * FROM "
					       + JDRSTest.RSTEST_DFP16);
		rs.next(); 
		Array v = rs.getArray (1);
		failed ("Didn't throw SQLException "+v);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



	/**
	getArray() - Get from DFP34:
	**/
    public void Var038 ()
    {
	if (checkDecFloatSupport()) {
	    try {
		Statement s = connection_.createStatement ();
		ResultSet rs = s.executeQuery ("SELECT * FROM "
					       + JDRSTest.RSTEST_DFP34);
		rs.next(); 
		Array v = rs.getArray (1);
		failed ("Didn't throw SQLException "+v);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }



/**
getArray() - Get from a BOOLEAN.
**/
	public void Var039 ()
	{
	    if (checkBooleanSupport()) {
		try {
		    ResultSet rs = statement_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_BOOLEAN);
		    JDRSTest.position0 (rs, "BOOLEAN_TRUE");
		    Array v = rs.getArray ("C_BOOLEAN");
		    failed ("Didn't throw SQLException"+v);
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}


/**
getArray() - Get from a BOOLEAN.
**/
	public void Var040 ()
	{
	    if (checkBooleanSupport()) {
		try {
		    ResultSet rs = statement_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_BOOLEAN);
		    JDRSTest.position0 (rs, "BOOLEAN_FALSE");
		    Array v = rs.getArray ("C_BOOLEAN");
		    failed ("Didn't throw SQLException"+v);
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}

/**
getArray() - Get from a BOOLEAN.
**/
	public void Var041()
	{
	    if (checkBooleanSupport()) {
		try {
		    ResultSet rs = statement_.executeQuery ("SELECT * FROM "
							    + JDRSTest.RSTEST_BOOLEAN);
		    JDRSTest.position0 (rs, "BOOLEAN_NULL");
		    Array v = rs.getArray ("C_BOOLEAN");
		    failed ("Didn't throw SQLException"+v);
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }

	}




}



