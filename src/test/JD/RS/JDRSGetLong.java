///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetLong.java
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

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSGetLong.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getLong()
</ul>
**/
public class JDRSGetLong
extends JDRSTestcase
{



/**
Constructor.
**/
    public JDRSGetLong (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSGetLong",
            namesAndVars, runMode, fileOutputStream,
            password);
    }






/**
getLong() - Should throw exception when the result set is
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
            rs.getLong (1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            long v = rs.getLong (1);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            long v = rs.getLong (100);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            long v = rs.getLong (0);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            long v = rs.getLong (-1);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Should work when the column index is valid.
**/
    public void Var006()
    {

      try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            long v = rs.getLong (3);
            assertCondition (v == 98765);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLong() - Should throw an exception when the column
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
          rs.getLong (null);
          failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }


/**
getLong() - Should throw an exception when the column
name is an empty string.
**/
    public void Var008()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.getLong ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Should throw an exception when the column
name is invalid.
**/
    public void Var009()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.getLong ("INVALID");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Should work when the column name is valid.
**/
    public void Var010()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            long v = rs.getLong ("C_INTEGER");
            assertCondition (v == 98765);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLong() - Should work when an update is pending.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateLong ("C_INTEGER", 19222228);
            long v = rs_.getLong ("C_INTEGER");
            assertCondition (v == 19222228);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getLong() - Should work when an update has been done.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateLong ("C_INTEGER", -1111222334);
            rs_.updateRow ();
            long v = rs_.getLong ("C_INTEGER");
            assertCondition (v == -1111222334);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getLong() - Should work when the current row is the insert
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
            rs_.updateLong ("C_INTEGER", 1893);
            long v = rs_.getLong ("C_INTEGER");
            assertCondition (v == 1893);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getLong() - Should work when the current row is the insert
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
            rs_.updateLong ("C_INTEGER", 21027);
            rs_.insertRow ();
            long v = rs_.getLong ("C_INTEGER");
            assertCondition (v == 21027);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getLong() - Should throw an exception on a deleted row.
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
            long v = rs_.getLong ("C_INTEGER");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


/**
getLong() - Should return null when the column is NULL.
**/
    public void Var016 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            long v = rs.getLong ("C_INTEGER");
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLong() - Get from a SMALLINT.
**/
    public void Var017 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            long v = rs.getLong ("C_SMALLINT");
            assertCondition (v == -198);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLong() - Get from a INTEGER.
**/
    public void Var018 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            long v = rs.getLong ("C_INTEGER");
            assertCondition (v == 98765);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLong() - Get from a REAL.
**/
    public void Var019 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            long v = rs.getLong ("C_REAL");
            assertCondition (v == -4);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLong() - Get from a FLOAT.
**/
    public void Var020 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            long v = rs.getLong ("C_FLOAT");
            assertCondition (v == 5);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLong() - Get from a DOUBLE.
**/
    public void Var021 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            long v = rs.getLong ("C_DOUBLE");
            assertCondition (v == -6);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLong() - Get from a DECIMAL.
**/
    public void Var022 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            long v = rs.getLong ("C_DECIMAL_50");
            assertCondition (v == 7);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLong() - Get from a NUMERIC.
**/
    public void Var023 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            long v = rs.getLong ("C_NUMERIC_105");
            assertCondition (v == -10);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLong() - Get from a CHAR, where the value does not translate
to an int.
**/
    public void Var024 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            long v = rs.getLong ("C_CHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get from a CHAR, where the value does translate
to an int.
**/
    public void Var025 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_INT");
            long v = rs.getLong ("C_CHAR_50");
            assertCondition (v == -55);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLong() - Get from a CHAR, where the value does translate
to an int, except that it has a decimal point
**/
    public void Var026 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("Jcc throws invalid conversion for Char 55.xxx"); 
          return; 
        }
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FLOAT");
            long v = rs.getLong ("C_CHAR_50");
            assertCondition (v == 55);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLong() - Get from a VARCHAR, where the value does not translate
to an int.
**/
    public void Var027 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            long v = rs.getLong ("C_VARCHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get from a VARCHAR, where the value does translate
to an int.
**/
    public void Var028 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_INT");
            long v = rs.getLong ("C_VARCHAR_50");
            assertCondition (v == 567);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLong() - Get from a VARCHAR, where the value does translate
to an int, except that it has a decimal point
**/
    public void Var029 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("Jcc throws invalid conversion for varChar -567.xxx"); 
        return; 
      }
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FLOAT");
            long v = rs.getLong ("C_VARCHAR_50");
            assertCondition (v == -567);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLong() - Get from a BINARY.
**/
    public void Var030 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            long v = rs.getLong ("C_BINARY_20");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get from a VARBINARY.
**/
    public void Var031 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            long v = rs.getLong ("C_VARBINARY_20");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get from a CLOB.
**/
    public void Var032 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                long v = rs.getLong ("C_CLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getLong() - Get from a DBCLOB.
**/
    public void Var033 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                long v = rs.getLong ("C_DBCLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getLong() - Get from a BLOB.
**/
    public void Var034 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                long v = rs.getLong ("C_BLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getLong() - Get from a DATE.
**/
    public void Var035 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            long v = rs.getLong ("C_DATE");
            failed ("Didn't throw SQLException"+v+ " from date");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get from a TIME.
**/
    public void Var036 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            long v = rs.getLong ("C_TIME");
            failed ("Didn't throw SQLException"+v+" from time");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get from a TIMESTAMP.
**/
    public void Var037 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            long v = rs.getLong ("C_TIMESTAMP");
            failed ("Didn't throw SQLException"+v+ " from timestamp " );
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getLong() - Get from a DATALINK.
**/
    public void Var038 ()
    {
        if (checkDatalinkSupport ()) {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GETDL);
                JDRSTest.position0 (rs, "LOB_FULL");
                long v = rs.getLong ("C_DATALINK");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getLong() - Get from a DISTINCT.
**/
    public void Var039 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("Jcc doesn't throw exception for distinct"); 
        return; 
      }

      if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_EMPTY");
                long v = rs.getLong ("C_DISTINCT");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getLong() - Get from a BIGINT.
**/
    public void Var040()
    {
        if (checkBigintSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            long v = rs.getLong ("C_BIGINT");
            assertCondition (v == -44332123);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

    /**
    getLong() - Get from DFP16 -- Use preset values
    **/
   public void Var041 ()
   {
        testDfp(JDRSTest.RSTEST_DFP16, JDRSTest.VALUES_DFP16_LONG, statement0_, "getLong");
   }
   
   
   /**
    getLong() - Get from DFP34 -- Use preset values
    **/
   public void Var042 ()
   {
     if (getDriver() == JDTestDriver.DRIVER_JCC) {
       notApplicable("Jcc doesn't throw exception for DFP34"); 
       return; 
     }
        testDfp(JDRSTest.RSTEST_DFP34, JDRSTest.VALUES_DFP34_LONG, statement0_, "getLong");
   }
    

   /**
   getLong() - Get from a SQLXML.
   **/
       public void Var043 ()
       {

           if (checkXmlSupport ()) {
               try {
                   ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                       + JDRSTest.RSTEST_GETXML);
                   rs.next(); 
                   long v = rs.getLong ("C_XML"); 
                   failed ("Didn't throw SQLException"+v);
               }
               catch (Exception e) {
                   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
               }
           }
       }


        /**
         * getLong() - Get from a TIMESTAMP(12).
         **/
        public void Var044() {
            if (checkTimestamp12Support()) {
              testGet(statement0_,
                  "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
                  "getLong", 
                  "EXCEPTION:Data type mismatch.", 
                  " -- added 11/19/2012"); 

            }
        }


        /**
         * getLong() - Get from a TIMESTAMP(0).
         **/
        public void Var045() {
            if (checkTimestamp12Support()) {
          testGet(statement0_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
            "getLong",
		  "EXCEPTION:Data type mismatch.", 
            " -- added 11/19/2012"); 
            
            
            }
        }



/**
getLong() - Get from a BOOLEAN.
**/
    public void Var046()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_FALSE");
            long v = rs.getLong ("C_BOOLEAN");
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


/**
getLong() - Get from a BOOLEAN.
**/
    public void Var047()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_TRUE");
            long v = rs.getLong ("C_BOOLEAN");
            assertCondition (v == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

/**
getLong() - Get from a BOOLEAN.
**/
    public void Var048()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_NULL");
            long v = rs.getLong ("C_BOOLEAN");
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }





}



