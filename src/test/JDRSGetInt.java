///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetInt.java
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
import java.sql.ResultSet;

import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSGetInt.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getInt
</ul>
**/
public class JDRSGetInt
extends JDRSTestcase
{


/**
Constructor.
**/
    public JDRSGetInt (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSGetInt",
            namesAndVars, runMode, fileOutputStream,
            password);
    }





/**
getInt() - Should throw exception when the result set is
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
            rs.getInt (1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getInt() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            int v = rs.getInt ("C_INTEGER");
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getInt() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            rs.next ();
            int v = rs.getInt (100);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getInt() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            rs.next ();
            int v = rs.getInt (0);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getInt() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            rs.next ();
            int v = rs.getInt (-1);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getInt() - Should work when the column index is valid.
**/
    public void Var006()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "NUMBER_POS");
            int v = rs.getInt (3);
            assertCondition (v == 98765);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getInt() - Should throw an exception when the column
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
          rs.next ();
          rs.getInt (null);
          failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }


/**
getInt() - Should throw an exception when the column
name is an empty string.
**/
    public void Var008()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            rs.next ();
            rs.getInt ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getInt() - Should throw an exception when the column
name is invalid.
**/
    public void Var009()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            rs.next ();
            rs.getInt ("INVALID");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getInt() - Should work when the column name is valid.
**/
    public void Var010()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "NUMBER_POS");
            int v = rs.getInt ("C_INTEGER");
            assertCondition (v == 98765);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getInt() - Should work when an update is pending.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateInt ("C_INTEGER", 1928);
            int v = rs_.getInt ("C_INTEGER");
            assertCondition (v == 1928);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getInt() - Should work when an update has been done.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateInt ("C_INTEGER", -11);
            rs_.updateRow ();
            int v = rs_.getInt ("C_INTEGER");
            assertCondition (v == -11);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getInt() - Should work when the current row is the insert
row, when an insert is pending.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow"); 
            return; 
          }
        try {
            rs_.moveToInsertRow ();
            rs_.updateInt ("C_INTEGER", -192893);
            int v = rs_.getInt ("C_INTEGER");
            assertCondition (v == -192893);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getInt() - Should work when the current row is the insert
row, when an insert has been done.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow"); 
            return; 
          }
        try {
            rs_.moveToInsertRow ();
            rs_.updateInt ("C_INTEGER", 2531027);
            rs_.insertRow ();
            int v = rs_.getInt ("C_INTEGER");
            assertCondition (v == 2531027);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getInt() - Should throw an exception on a deleted row.
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
            int v = rs_.getInt ("C_INTEGER");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


/**
getInt() - Should return null when the column is NULL.
**/
    public void Var016 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "NUMBER_NULL");
            int v = rs.getInt ("C_INTEGER");
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getInt() - Get from a SMALLINT.
**/
    public void Var017 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "NUMBER_NEG");
            int v = rs.getInt ("C_SMALLINT");
            assertCondition (v == -198);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getInt() - Get from a INTEGER.
**/
    public void Var018 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "NUMBER_POS");
            int v = rs.getInt ("C_INTEGER");
            assertCondition (v == 98765);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getInt() - Get from a REAL.
**/
    public void Var019 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "NUMBER_NEG");
            int v = rs.getInt ("C_REAL");
            assertCondition (v == -4);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getInt() - Get from a FLOAT.
**/
    public void Var020 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "NUMBER_POS");
            int v = rs.getInt ("C_FLOAT");
            assertCondition (v == 5);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getInt() - Get from a DOUBLE.
**/
    public void Var021 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "NUMBER_NEG");
            int v = rs.getInt ("C_DOUBLE");
            assertCondition (v == -6);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getInt() - Get from a DECIMAL.
**/
    public void Var022 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "NUMBER_POS");
            int v = rs.getInt ("C_DECIMAL_50");
            assertCondition (v == 7);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getInt() - Get from a NUMERIC.
**/
    public void Var023 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "NUMBER_NEG");
            int v = rs.getInt ("C_NUMERIC_105");
            assertCondition (v == -10);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getInt() - Get from a CHAR, where the value does not translate
to an int.
**/
    public void Var024 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "CHAR_FULL");
            int v = rs.getInt ("C_CHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getInt() - Get from a CHAR, where the value does translate
to an int.
**/
    public void Var025 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "CHAR_INT");
            int v = rs.getInt ("C_CHAR_50");
            assertCondition (v == -55);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getInt() - Get from a CHAR, where the value does translate
to an int, except that it has a decimal point
**/
    public void Var026 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("jcc throws exception when char 55.901 to int"); 
        return; 
      }

        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "CHAR_FLOAT");
            int v = rs.getInt ("C_CHAR_50");
            assertCondition (v == 55);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getInt() - Get from a VARCHAR, where the value does not translate
to an int.
**/
    public void Var027 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "CHAR_FULL");
            int v = rs.getInt ("C_VARCHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getInt() - Get from a VARCHAR, where the value does translate
to an int.
**/
    public void Var028 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "CHAR_INT");
            int v = rs.getInt ("C_VARCHAR_50");
            assertCondition (v == 567);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getInt() - Get from a VARCHAR, where the value does translate
to an int, except that it has a decimal point
**/
    public void Var029 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("jcc throws exception when varchar 55.901 to int"); 
        return; 
      }
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "CHAR_FLOAT");
            int v = rs.getInt ("C_VARCHAR_50");
            assertCondition (v == -567);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getInt() - Get from a BINARY.
**/
    public void Var030 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "BINARY_TRANS");
            int v = rs.getInt ("C_BINARY_20");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getInt() - Get from a VARBINARY.
**/
    public void Var031 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "BINARY_TRANS");
            int v = rs.getInt ("C_VARBINARY_20");
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getInt() - Get from a CLOB.
**/
    public void Var032 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);            
                JDRSTest.position0 (rs, "LOB_FULL");
                int v = rs.getInt ("C_CLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getInt() - Get from a DBCLOB.
**/
    public void Var033 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);            
                JDRSTest.position0 (rs, "LOB_FULL");
                int v = rs.getInt ("C_DBCLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getInt() - Get from a BLOB.
**/
    public void Var034 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);            
                JDRSTest.position0 (rs, "LOB_FULL");
                int v = rs.getInt ("C_BLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getInt() - Get from a DATE.
**/
    public void Var035 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "DATE_1998");
            int v = rs.getInt ("C_DATE");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getInt() - Get from a TIME.
**/
    public void Var036 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "DATE_1998");
            int v = rs.getInt ("C_TIME");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getInt() - Get from a TIMESTAMP.
**/
    public void Var037 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "DATE_1998");
            int v = rs.getInt ("C_TIMESTAMP");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getInt() - Get from a DATALINK.
**/
    public void Var038 ()
    {
        if (checkDatalinkSupport ()) {
            try {
               Statement s = connection_.createStatement ();
               ResultSet rs = s.executeQuery ("SELECT * FROM "
                   + JDRSTest.RSTEST_GETDL);
               JDRSTest.position0 (rs, "LOB_FULL");
               int v = rs.getInt ("C_DATALINK");
               s.close();
               failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getInt() - Get from a DISTINCT.
**/
    public void Var039 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);            
                JDRSTest.position0 (rs, "LOB_FULL");
                int v = rs.getInt ("C_DISTINCT");
                assertCondition (v == 123456789);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getInt() - Get from a BIGINT.
**/
    public void Var040()
    {
        if (checkBigintSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);            
            JDRSTest.position0 (rs, "NUMBER_POS");
            int v = rs.getInt ("C_BIGINT");
            assertCondition (v == 12374321);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




    /**
    getInt() - Get from a different types where the value will not convert.
    **/
    public void Var041()	{  baseTruncationTest(" CAST (3000111222 AS BIGINT)", "getInt"); }
    public void Var042()	{  baseTruncationTest(" CAST (3000111222 AS REAL)", "getInt"); }
    public void Var043()	{  baseTruncationTest(" CAST (3000111222 AS FLOAT)", "getInt"); }
    public void Var044()	{  baseTruncationTest(" CAST (3000111222 AS DOUBLE)", "getInt"); }
    public void Var045()	{  baseTruncationTest(" CAST (3000111222 AS DECIMAL(12,0))", "getInt"); }
    public void Var046()	{  baseTruncationTest(" CAST (3000111222 AS NUMERIC(12,0))", "getInt"); }
    public void Var047()	{  baseTruncationTest(" CAST ('3000111222' AS CHAR(10))", "getInt"); }
    public void Var048()	{  baseTruncationTest(" CAST ('3000111222' AS VARCHAR(20))", "getInt"); }
    public void Var049()	{  baseTruncationTest(" CAST (-3000111222 AS BIGINT)", "getInt"); }
    public void Var050()	{  baseTruncationTest(" CAST (-3000111222 AS REAL)", "getInt"); }
    public void Var051()	{  baseTruncationTest(" CAST (-3000111222 AS FLOAT)", "getInt"); }
    public void Var052()	{  baseTruncationTest(" CAST (-3000111222 AS DOUBLE)", "getInt"); }
    public void Var053()	{  baseTruncationTest(" CAST (-3000111222 AS DECIMAL(12,0))", "getInt"); }
    public void Var054()	{  baseTruncationTest(" CAST (-3000111222 AS NUMERIC(12,0))", "getInt"); }
    public void Var055()	{  baseTruncationTest(" CAST ('-3000111222' AS CHAR(11))", "getInt"); }
    public void Var056()	{  baseTruncationTest(" CAST ('-3000111222' AS VARCHAR(20))", "getInt"); }

    
    
    /**
     getInt() - Get from DFP16 -- Use preset values
     **/
    public void Var057 ()
    {
         testDfp(JDRSTest.RSTEST_DFP16, JDRSTest.VALUES_DFP16_INT, statement0_, "getInt");
    }
    
    
    /**
     getInt() - Get from DFP34 -- Use preset values
     **/
    public void Var058 ()
    {

         testDfp(JDRSTest.RSTEST_DFP34, JDRSTest.VALUES_DFP34_INT, statement0_, "getInt");
    }
    

    /**
    getInt() - Get from a SQLXML.
    **/
        public void Var059 ()
        {
          if (checkXmlSupport()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GETXML);
                rs.next(); 
                int v = rs.getInt ("C_XML");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
          }
        }


        /**
         * getInt() - Get from a TIMESTAMP(12).
         **/
        public void Var060() {
            if (checkTimestamp12Support()) {
              testGet(statement0_,
                  "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
                  "getInt", 
                  "EXCEPTION:Data type mismatch.", 
                  " -- added 11/19/2012"); 

            }
        }


        /**
         * getInt() - Get from a TIMESTAMP(0).
         **/
        public void Var061() {
            if (checkTimestamp12Support()) {
          testGet(statement0_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
            "getInt",
		  "EXCEPTION:Data type mismatch.", 
            " -- added 11/19/2012"); 
            
            
            }
        }


/**
getInt() - Get from a BOOLEAN.
**/
    public void Var062()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_FALSE");
            int v = rs.getInt ("C_BOOLEAN");
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


/**
getInt() - Get from a BOOLEAN.
**/
    public void Var063()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_TRUE");
            int v = rs.getInt ("C_BOOLEAN");
            assertCondition (v == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

/**
getInt() - Get from a BOOLEAN.
**/
    public void Var064()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_NULL");
            int v = rs.getInt ("C_BOOLEAN");
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


}



