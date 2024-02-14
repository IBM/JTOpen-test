///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetBoolean.java
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




import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSGetBoolean.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>JDRSGetBoolean
</ul>
**/
public class JDRSGetBoolean
extends JDTestcase
{



    // Private data.
    private Statement           statement_;
    String                      statementQuery_;
    private Statement           statement0_;
    private Statement           statement1_;
    private ResultSet           rs_;


    private boolean             isJDK14;

/**
Constructor.
**/
    public JDRSGetBoolean (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSGetBoolean",
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


	//
	// look for jdk1.4
	//
		isJDK14 = true;


        // SQL400 - driver neutral...
        String url = baseURL_;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(),encryptedPassword_,"JDRSGetBoolean");
	setAutoCommit(connection_, false); // @E1A


        statement0_ = connection_.createStatement ();

	statement1_ = connection_.createStatement
	  (ResultSet.TYPE_SCROLL_SENSITIVE,
	   ResultSet.CONCUR_UPDATABLE);

        if (isJdbc20 ()) {
	    try {
		statement_ = connection_.createStatement
		  (ResultSet.TYPE_SCROLL_SENSITIVE,
		   ResultSet.CONCUR_UPDATABLE);
	    } catch (SQLException ex) {
	      statement_ = connection_.createStatement();
	    }
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_GET
                + " (C_KEY) VALUES ('DUMMY_ROW')");
	    statementQuery_ = "SELECT * FROM "
                + JDRSTest.RSTEST_GET + " FOR UPDATE";
            rs_ = statement_.executeQuery (statementQuery_);
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

        statement0_.close ();
        connection_.commit(); // @E1A
        connection_.close ();
    }



/**
getBoolean() - Should throw exception when the result set is
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
            rs.getBoolean (2);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBoolean() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            boolean v = rs.getBoolean ("C_SMALLINT");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBoolean() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            boolean v = rs.getBoolean (100);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBoolean() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            boolean v = rs.getBoolean (0);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBoolean() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            boolean v = rs.getBoolean (-1);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBoolean() - Should work when the column index is valid.
**/
    public void Var006()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            boolean v = rs.getBoolean (2);
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Should throw an exception when the column
name is null.
**/
    public void Var007()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC throws null pointer exception when column name is null ");
      } else {
        try {

          ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_GET);
          JDRSTest.position0 (rs, "NUMBER_POS");
          rs.getBoolean (null);
          failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }


/**
getBoolean() - Should throw an exception when the column
name is an empty string.
**/
    public void Var008()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            rs.getBoolean ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBoolean() - Should throw an exception when the column
name is invalid.
**/
    public void Var009()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            rs.getBoolean ("INVALID");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBoolean() - Should work when the column name is valid.
**/
    public void Var010()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            boolean v = rs.getBoolean ("C_INTEGER");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Should work when an update is pending.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateBoolean ("C_SMALLINT", true);
            boolean v = rs_.getBoolean ("C_SMALLINT");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBoolean() - Should work when an update has been done.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateBoolean ("C_INTEGER", true);
            rs_.updateRow ();
            boolean v = rs_.getBoolean ("C_INTEGER");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBoolean() - Should work when the current row is the insert
row, when an insert is pending.
**/
    public void Var013()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support the moveToInsertRow method ");
      } else {

        if (checkJdbc20 ()) {
          try {
            rs_.moveToInsertRow ();
            rs_.updateBoolean ("C_INTEGER", true);
            boolean v = rs_.getBoolean ("C_INTEGER");
            assertCondition (v == true);
          }
          catch (Exception e) {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }


/**
getBoolean() - Should work when the current row is the insert
row, when an insert has been done.
**/
    public void Var014()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support the moveToInsertRow method ");
      } else {
        if (checkJdbc20 ()) {
          try {
            rs_.moveToInsertRow ();
            rs_.updateBoolean ("C_SMALLINT", true);
            rs_.insertRow ();
            boolean v = rs_.getBoolean ("C_SMALLINT");
            assertCondition (v == true);
          }
          catch (Exception e) {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }


/**
getBoolean() - Should throw an exception on a deleted row.
**/
    public void Var015()
    {
      if (checkJdbc20 ()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC doesn't throw exception for get on deleted row");
          return;
        }
        try {
          JDRSTest.position (rs_, "DUMMY_ROW");
          rs_.deleteRow ();
          boolean v = rs_.getBoolean ("C_SMALLINT");
          failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }


/**
getBoolean() - Should return false when the column is NULL.
**/
    public void Var016 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            boolean v = rs.getBoolean ("C_SMALLINT");
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getBoolean() - Get from a SMALLINT, when the value is true.
**/
    public void Var017 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            boolean v = rs.getBoolean ("C_SMALLINT");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a SMALLINT, when the value is false.
**/
    public void Var018 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_0");
            boolean v = rs.getBoolean ("C_SMALLINT");
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a INTEGER, when the value is true.
**/
    public void Var019 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            boolean v = rs.getBoolean ("C_INTEGER");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a INTEGER, when the value is false.
**/
    public void Var020 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_0");
            boolean v = rs.getBoolean ("C_INTEGER");
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a REAL, when the value is true.
**/
    public void Var021 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            boolean v = rs.getBoolean ("C_REAL");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a REAL, when the value is false.
**/
    public void Var022 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_0");
            boolean v = rs.getBoolean ("C_REAL");
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a FLOAT, when the value is true.
**/
    public void Var023 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            boolean v = rs.getBoolean ("C_FLOAT");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a FLOAT, when the value is false.
**/
    public void Var024 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_0");
            boolean v = rs.getBoolean ("C_FLOAT");
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a DOUBLE, when the value is true.
**/
    public void Var025 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            boolean v = rs.getBoolean ("C_DOUBLE");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a DOUBLE, when the value is false.
**/
    public void Var026 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_0");
            boolean v = rs.getBoolean ("C_DOUBLE");
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a DECIMAL, when the value is true.
**/
    public void Var027 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            boolean v = rs.getBoolean ("C_DECIMAL_50");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a DECIMAL, when the value is false.
**/
    public void Var028 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_0");
            boolean v = rs.getBoolean ("C_DECIMAL_50");
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a NUMERIC, when the value is true.
**/
    public void Var029 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            boolean v = rs.getBoolean ("C_NUMERIC_105");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a NUMERIC, when the value is false.
**/
    public void Var030 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_0");
            boolean v = rs.getBoolean ("C_NUMERIC_105");
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a CHAR, where the value is true (the word
"true").
**/
    public void Var031 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_BOOLEAN_TRUE");
            boolean v = rs.getBoolean ("C_CHAR_50");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a CHAR, where the value is false (the word
"false").
**/
    public void Var032 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_BOOLEAN_FALSE");
            boolean v = rs.getBoolean ("C_CHAR_50");
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a CHAR, where the value is true (the number
"1").
**/
    public void Var033 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            boolean v = rs.getBoolean ("C_CHAR_50");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a CHAR, where the value is false (the number "0").
**/
    public void Var034 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_0");
            boolean v = rs.getBoolean ("C_CHAR_50");
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a CHAR, where the value is true (a non empty
string).
**/
    public void Var035 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            boolean v = rs.getBoolean ("C_CHAR_50");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a CHAR, where the value is false (an empty
string).
**/
    public void Var036 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC returns true");
        return;
      }
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_EMPTY");
            boolean v = rs.getBoolean ("C_CHAR_50");
            assertCondition (v == false, "expected false but got "+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a VARCHAR, where the value is true (the word
"true").
**/
    public void Var037 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_BOOLEAN_TRUE");
            boolean v = rs.getBoolean ("C_VARCHAR_50");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a VARCHAR, where the value is false (the word
"false").
**/
    public void Var038 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC returns true");
        return;
      }
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_BOOLEAN_FALSE");
            boolean v = rs.getBoolean ("C_VARCHAR_50");
            String s =  rs.getString  ("C_VARCHAR_50");
            assertCondition (v == false, "Expected false, but got "+v+" for "+s);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a VARCHAR, where the value is true (the number "1").
**/
    public void Var039 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            boolean v = rs.getBoolean ("C_VARCHAR_50");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a VARCHAR, where the value is false (the number "0").
**/
    public void Var040 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_0");
            boolean v = rs.getBoolean ("C_VARCHAR_50");
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a VARCHAR, where the value is true (a non empty
string).
**/
    public void Var041 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            boolean v = rs.getBoolean ("C_VARCHAR_50");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a VARCHAR, where the value is false (an empty
string).
**/
    public void Var042 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC returns true");
        return;
      }
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_EMPTY");
            boolean v = rs.getBoolean ("C_VARCHAR_50");
            String  s = rs.getString(  "C_VARCHAR_50"  );
            assertCondition (v == false, "Expected false, but got "+v+" for "+s);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBoolean() - Get from a BINARY.
**/
    public void Var043 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            boolean v = rs.getBoolean ("C_BINARY_20");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBoolean() - Get from a VARBINARY.
**/
    public void Var044 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            boolean v = rs.getBoolean ("C_VARBINARY_20");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBoolean() - Get from a CLOB.
**/
    public void Var045 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC throws exception from CLOB");
        return;
      }
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                boolean v = rs.getBoolean ("C_CLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBoolean() - Get from a DBCLOB.
**/
    public void Var046 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC throws exception from DBCLOB");
        return;
      }
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                boolean v = rs.getBoolean ("C_DBCLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBoolean() - Get from a BLOB.
**/
    public void Var047 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                boolean v = rs.getBoolean ("C_BLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBoolean() - Get from a DATE.
**/
    public void Var048 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            boolean v = rs.getBoolean ("C_DATE");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBoolean() - Get from a TIME.
**/
    public void Var049 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            boolean v = rs.getBoolean ("C_TIME");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBoolean() - Get from a TIMESTAMP.
**/
    public void Var050 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            boolean v = rs.getBoolean ("C_TIMESTAMP");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBoolean() - Get from a DATALINK, where the datalink URL has a value (is not
an empty String).

SQL400 - From the native driver's perspective, a datalink column is treated
the same was that it is for standard SQL.  When you make an unqualified select
of a datalink, the SQL statement is implicitly cast to look at the full URL
value for the datalink.  This is, in essence, a String and can be tested as a
boolean in the same way that other Strings can be.
**/
    public void Var051 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not throw exception from DATALINK");
        return;
      }
      if (checkDatalinkSupport ()) {
        try {
          Statement s = connection_.createStatement ();
          ResultSet rs = s.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_GETDL);
          JDRSTest.position0 (rs, "LOB_FULL");
          boolean v = rs.getBoolean ("C_DATALINK");
          if (isToolboxDriver() || ( getRelease() >=  JDTestDriver.RELEASE_V7R1M0 && isJDK14) )
          {
            failed ("Didn't throw SQLException");
          }
          else
          {
            assertCondition (v == true);
          }

        }
        catch (Exception e) {
          if (isToolboxDriver() || (getRelease() >=  JDTestDriver.RELEASE_V7R1M0 && isJDK14))
          {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
          else
          {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }


/**
getBoolean() - Get from a DATALINK, where the datalink URL has a false value (it is
an empty String).

SQL400 - From the native driver's perspective, a datalink column is treated
the same was that it is for standard SQL.  When you make an unqualified select
of a datalink, the SQL statement is implicitly cast to look at the full URL
value for the datalink.  This is, in essence, a String and can be tested as a
boolean in the same way that other Strings can be.
**/
    public void Var052 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not throw exception from DATALINK");
        return;
      }
      if (getDatabaseType() == JDTestDriver.DB_ZOS) {
        notApplicable("Z/OS does not support data links");
      } else {
        if (checkLobSupport ()) {
          try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GETDL);
            JDRSTest.position0 (rs, "LOB_EMPTY");
            boolean v = rs.getBoolean ("C_DATALINK");
            if (isToolboxDriver() || (getRelease() >=  JDTestDriver.RELEASE_V7R1M0 && isJDK14))
            {
              failed ("Didn't throw SQLException");
            }
            else
            {
              assertCondition (v == false);
            }

          }
          catch (Exception e) {
            if (isToolboxDriver() || (getRelease() >=  JDTestDriver.RELEASE_V7R1M0 && isJDK14))
            {
              assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
            else
            {
              failed (e, "Unexpected Exception");
            }

          }
        }
      }
    }


/**
getBoolean() - Get from a DISTINCT.
**/
    public void Var053 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                boolean v = rs.getBoolean ("C_DISTINCT");
                assertCondition (v == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getBoolean() - Get from a BIGINT, when the value is true.
**/
    public void Var054 ()
    {
        if (checkBigintSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            boolean v = rs.getBoolean ("C_BIGINT");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBoolean() - Get from a BIGINT, when the value is false.
**/
    public void Var055 ()
    {
        if (checkBigintSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_0");
            boolean v = rs.getBoolean ("C_BIGINT");
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

    /**
    getBoolean() - Get from DFP16:

    jcc running to LUW returns com.ibm.db2.jcc.am.ColumnTypeConversionException:
                               [ibm][db2][jcc][1092][11625] Invalid data conversion:
                               Wrong result column type for requested conversion.
    **/
   public void Var056 ()
   {
     if (getDriver() == JDTestDriver.DRIVER_JCC) {
       notApplicable("JCC does not throw exception from DECFLOAT");
       return;
     }
     if (checkDecFloatSupport()) {
       try {
         Statement s = connection_.createStatement ();
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_DFP16);
         rs.next();
         boolean v = rs.getBoolean (1);
         if (isToolboxDriver() ||
             getDriver () == JDTestDriver.DRIVER_NATIVE  ||
             getDriver () == JDTestDriver.DRIVER_JTOPENLITE  )
             succeeded();
         else
             failed ("Didn't throw SQLException "+v);
       }
       catch (Exception e) {
           if (isToolboxDriver() ||
               getDriver () == JDTestDriver.DRIVER_NATIVE   ||
             getDriver () == JDTestDriver.DRIVER_JTOPENLITE  )
               failed (e, "Unexpected Exception");
           else {
               e.printStackTrace();
               assertExceptionIsInstanceOf (e, "java.sql.SQLException");
           }
       }
     }
   }



   /**
    getBoolean() - Get from DFP34:

    jcc running to LUW returns com.ibm.db2.jcc.am.ColumnTypeConversionException:
                               [ibm][db2][jcc][1092][11625] Invalid data conversion:
                               Wrong result column type for requested conversion.
    **/
   public void Var057 ()
   {
     if (getDriver() == JDTestDriver.DRIVER_JCC) {
       notApplicable("JCC does not throw exception from DECFLOAT");
       return;
     }
     if (checkDecFloatSupport()) {
       try {
         Statement s = connection_.createStatement ();
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_DFP34);
         rs.next();
         boolean v = rs.getBoolean (1);
         if (isToolboxDriver() ||
             getDriver () == JDTestDriver.DRIVER_NATIVE   ||
             getDriver () == JDTestDriver.DRIVER_JTOPENLITE  )
             succeeded();
         else
             failed ("Didn't throw SQLException "+v);
       }
       catch (Exception e) {
           if (isToolboxDriver() ||
               getDriver () == JDTestDriver.DRIVER_NATIVE  ||
             getDriver () == JDTestDriver.DRIVER_JTOPENLITE )
               failed (e, "Unexpected Exception");
           else {
              // e.printStackTrace();
              assertExceptionIsInstanceOf (e, "java.sql.SQLException");
           }
       }
     }
   }

   /**
   getBlob() - Get from an SQLXML.
   **/
  public void Var058() {
    if (checkXmlSupport()) {
      try {
        Statement stmt = connection_.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM "
            + JDRSTest.RSTEST_GETXML);
        rs.next();
        boolean v = rs.getBoolean("C_XML");
        failed("Didn't throw SQLException " + v);
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String exInfo = e.toString();
          String expected = "Invalid data conversion";
          assertCondition(exInfo.indexOf(expected)>=0,
              "Got exception '"+exInfo+"' expected '"+expected+"'");

        } else {
          // e.printStackTrace();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }

    }
  }


        /**
         * getByte() - Get from a TIMESTAMP(12).
         **/
        public void Var059() {
            if (checkTimestamp12Support()) {
              testGet(statement0_,
                  "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
                  "getByte", 
                  "EXCEPTION:Data type mismatch.", 
                  " -- added 11/19/2012"); 

            }
        }


        /**
         * getBoolean() - Get from a TIMESTAMP(0).
         **/
        public void Var060() {
            if (checkTimestamp12Support()) {
          testGet(statement0_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
            "getBoolean",
		  "EXCEPTION:Data type mismatch.", 
            " -- added 11/19/2012"); 
            
            
            }
        }


/**
getBoolean() - Get from a BOOLEAN.
**/
    public void Var061()
    {
        if (checkBooleanSupport()) {
	    String sql = "SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN;
        try {
            ResultSet rs = statement1_.executeQuery (sql);            
            JDRSTest.position0 (rs, "BOOLEAN_FALSE");
            boolean v = rs.getBoolean ("C_BOOLEAN");
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception sql="+sql);
        }
        }
    }


/**
getBoolean() - Get from a BOOLEAN.
**/
    public void Var062()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement1_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_TRUE");
            boolean v = rs.getBoolean ("C_BOOLEAN");
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

/**
getBoolean() - Get from a BOOLEAN.
**/
    public void Var063()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement1_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_NULL");
            boolean v = rs.getBoolean ("C_BOOLEAN");
            assertCondition (v == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




}



