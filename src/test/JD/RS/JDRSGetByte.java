///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetByte.java
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
import java.util.Vector;



/**
Testcase JDRSGetByte.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>JDRSGetByte
</ul>
**/
public class JDRSGetByte
extends JDRSTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetByte";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }





/**
Constructor.
**/
    public JDRSGetByte (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSGetByte",
            namesAndVars, runMode, fileOutputStream,
            password);
    }




/**
getByte() - Should throw exception when the result set is
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
            rs.getByte (1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getByte() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            byte v = rs.getByte (1);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getByte() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            byte v = rs.getByte (100);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getByte() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            byte v = rs.getByte (0);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getByte() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            byte v = rs.getByte (-1);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getByte() - Should work when the column index is valid.
**/
    public void Var006()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_0");
            byte v = rs.getByte (2);
            assertCondition (v == (byte) 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getByte() - Should throw an exception when the column
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
            rs.getByte (null);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }



/**
getByte() - Should throw an exception when the column
name is an empty string.
**/
    public void Var008()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            rs.getByte ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getByte() - Should throw an exception when the column
name is invalid.
**/
    public void Var009()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            rs.getByte ("INVALID");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getByte() - Should work when the column name is valid.
**/
    public void Var010()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_0");
            byte v = rs.getByte ("C_SMALLINT");
            assertCondition (v == (byte) 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getByte() - Should work when an update is pending.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateByte ("C_INTEGER", (byte) 44);
            byte v = rs_.getByte ("C_INTEGER");
            assertCondition (v == 44);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getByte() - Should work when an update has been done.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateByte ("C_INTEGER", (byte) -41);
            rs_.updateRow ();
            byte v = rs_.getByte ("C_INTEGER");
            assertCondition (v == -41);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getByte() - Should work when the current row is the insert
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
            rs_.updateByte ("C_INTEGER", (byte) 76);
            byte v = rs_.getByte ("C_INTEGER");
            assertCondition (v == 76);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getByte() - Should work when the current row is the insert
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
            rs_.updateByte ("C_INTEGER", (byte) 79);
            rs_.insertRow ();
            byte v = rs_.getByte ("C_INTEGER");
            assertCondition (v == 79);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getByte() - Should throw an exception on a deleted row.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
/* 
          if (getDriver() == JDTestDriver.DRIVER_JCC) { 
            notApplicable("JCC doesn't throw exception for get on deleted row");
            return; 
          }
*/

        try {
            JDRSTest.position (rs_, "DUMMY_ROW");
            rs_.deleteRow ();
            byte v = rs_.getByte ("C_INTEGER");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getByte() - Should return null when the column is NULL.
**/
    public void Var016 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            byte v = rs.getByte ("C_INTEGER");
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getByte() - Get from a SMALLINT.
**/
    public void Var017 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_0");
            byte v = rs.getByte ("C_SMALLINT");
            assertCondition (v == (byte) 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getByte() - Get from a INTEGER.
**/
    public void Var018 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_0");
            byte v = rs.getByte ("C_INTEGER");
            assertCondition (v == (byte) 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getByte() - Get from a REAL.
**/
    public void Var019 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            byte v = rs.getByte ("C_REAL");
            assertCondition (v == -4);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getByte() - Get from a FLOAT.
**/
    public void Var020 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            byte v = rs.getByte ("C_FLOAT");
            assertCondition (v == 5);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getByte() - Get from a DOUBLE.
**/
    public void Var021 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            byte v = rs.getByte ("C_DOUBLE");
            assertCondition (v == -6);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getByte() - Get from a DECIMAL.
**/
    public void Var022 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            byte v = rs.getByte ("C_DECIMAL_50");
            assertCondition (v == 7);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getByte() - Get from a NUMERIC.
**/
    public void Var023 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            byte v = rs.getByte ("C_NUMERIC_105");
            assertCondition (v == -10);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getByte() - Get from a CHAR, where the value does not translate
to an short.
**/
    public void Var024 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            byte v = rs.getByte ("C_CHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getByte() - Get from a CHAR, where the value does translate
to an short.
**/
    public void Var025 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_INT");
            byte v = rs.getByte ("C_CHAR_50");
            assertCondition (v == -55);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getByte() - Get from a CHAR, where the value does translate
to an short, except that it has a decimal point
**/
    public void Var026 ()
    {
/* 
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable ("LUW throws Invalid data conversion");
          return; 
        }
*/ 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FLOAT");
            byte v = rs.getByte ("C_CHAR_50");
            assertCondition (v == 55);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getByte() - Get from a VARCHAR, where the value does not translate
to an int.
**/
    public void Var027 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            byte v = rs.getByte ("C_VARCHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getByte() - Get from a VARCHAR, where the value does translate
to an int.
**/
    public void Var028 ()
    {
        String comment = "-- Getting a 567 value from getByte should be error "+
                         " changed 1/10/2007 by native driver"; 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_INT");
            byte v = rs.getByte ("C_VARCHAR_50");
            failed ("Didn't throw SQLException but got "+v+comment);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException", comment );
        }
    }



/**
getByte() - Get from a VARCHAR, where the value does translate
to an int, except that it has a decimal point
**/
    public void Var029 ()
    {
      String comment = " -- Getting a -567 value from getByte should be error "+
      " changed 1/10/2007 by native driver"; 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FLOAT");
            byte v = rs.getByte ("C_VARCHAR_50");
	    String s = rs.getString ("C_VARCHAR_50");
            failed ("Didn't throw SQLException but got "+v+" string is "+s+comment);   
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException", comment );
        }
    }



/**
getByte() - Get from a BINARY.
**/
    public void Var030 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            byte v = rs.getByte ("C_BINARY_20");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getByte() - Get from a VARBINARY.
**/
    public void Var031 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            byte v = rs.getByte ("C_VARBINARY_20");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getByte() - Get from a CLOB.
**/
    public void Var032 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                byte v = rs.getByte ("C_CLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getByte() - Get from a DBCLOB.
**/
    public void Var033 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                byte v = rs.getByte ("C_DBCLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getByte() - Get from a BLOB.
**/
    public void Var034 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                byte v = rs.getByte ("C_BLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getByte() - Get from a DATE.
**/
    public void Var035 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            byte v = rs.getByte ("C_DATE");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getByte() - Get from a TIME.
**/
    public void Var036 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            byte v = rs.getByte ("C_TIME");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getByte() - Get from a TIMESTAMP.
**/
    public void Var037 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            byte v = rs.getByte ("C_TIMESTAMP");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getByte() - Get from a DATALINK.
**/
    public void Var038 ()
    {
        if (checkDatalinkSupport ()) {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GETDL);
                JDRSTest.position0 (rs, "LOB_FULL");
                byte v = rs.getByte ("C_DATALINK");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getByte() - Get from a DISTINCT.
**/
    public void Var039 ()
    {
      String comment = "-- Getting a 123456789 value from getByte should be error "+
      " changed 1/10/2007 by native driver"; 
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                byte v = rs.getByte ("C_DISTINCT");
                failed ("Didn't throw SQLException but got "+v+comment);
            }
            catch (Exception e) {
              assertExceptionIsInstanceOf (e, "java.sql.SQLException", comment );
            }
        }
    }



/**
getByte() - Get from a BIGINT.
**/
    public void Var040 ()
    {
      String comment = "-- Getting a 12374321 value from getByte should be error "+
      " changed 1/10/2007 by native driver"; 
        if (checkBigintSupport())
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            byte v = rs.getByte ("C_BIGINT");
            failed ("Didn't throw SQLException but got "+v+comment);
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException", comment );
        }
    }

    /**
    getByte() - Get from DFP16: value fits 
    **/
   public void Var041 ()
   {
     String comment  = " -- DFP test added 1/10/2007 by native driver ";
     if (checkDecFloatSupport()) {
	 String sql=""; 
       try {
         Statement s = connection_.createStatement ();
	 sql = "SELECT * FROM "
             + JDRSTest.RSTEST_GETDFP16; 
         ResultSet rs = s.executeQuery (sql);
         rs.next(); 
         byte v = rs.getByte (1);
         s.close(); 
	 assertCondition (v == (byte)1, "Got "+v+" sb 1"+comment);
       }
       catch (Exception e) {
            failed (e, "Unexpected Exception sql = "+sql+ comment);
       }
     }
   }
   
    /**
    getByte() - Get from DFP16: does not fit
    **/

   public void Var042 ()
   {
     String comment  = " -- DFP test added 1/10/2007 by native driver ";
     if (checkDecFloatSupport()) {
	 String values = ""; 
       try {
         Statement s = connection_.createStatement ();
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_GETDFP16);
	 while (rs.next()) { 
	     byte v = rs.getByte (1);
	     values += v+", "; 
	 }
         s.close(); 
         failed ("Didn't throw SQLException values retrieved using getByte were:" +values+comment );
       }
       catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException", comment);
       }
     }
   }
   
   

   
   /**
    getByte() - Get from DFP34: value fits 
    **/
   public void Var043 ()
   {
     String comment  = " -- DFP test added 1/10/2007 by native driver ";
     if (checkDecFloatSupport()) {
       try {
         Statement s = connection_.createStatement ();
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_GETDFP34);
         rs.next(); 
         byte v = rs.getByte (1);
         s.close(); 
	 assertCondition (v == (byte)1, "Got "+v+" sb 1"+comment);
       }
       catch (Exception e) {
            failed (e, "Unexpected Exception"+ comment);
       }
     }
   }
   

    /**
    getByte() - Get from DFP34: does not fit
    **/

   public void Var044 ()
   {
     String comment  = " -- DFP test added 1/10/2007 by native driver ";
     if (checkDecFloatSupport()) {
	 String values = ""; 
       try {
         Statement s = connection_.createStatement ();
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_GETDFP34);
	 while (rs.next()) { 
	     byte v = rs.getByte (1);
	     values += v+", "; 
	 }
         s.close(); 
         failed ("Didn't throw SQLException values were:" +values+comment);
       }
       catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException", comment);
       }
     }
   }
   
   
   
   /** getByte() -- limit test -- high limit ok from SMALLINT   */
   public void Var045() { checkLimitOK("SELECT CAST(127 as SMALLINT) from SYSIBM.SYSDUMMY1", "127", "getByte"); }
   /** getByte() -- limit test -- low limit ok from SMALLINT   */
   public void Var046() { checkLimitOK("SELECT CAST(-128 as SMALLINT) from SYSIBM.SYSDUMMY1", "-128", "getByte"); }
   /** getByte() -- limit test -- high limit bad from SMALLINT   */
   public void Var047() { checkLimitBAD("SELECT CAST(128 as SMALLINT) from SYSIBM.SYSDUMMY1", "java.sql.SQLException", "getByte"); }
   /** getByte() -- limit test -- low limit bad from SMALLINT   */
   public void Var048() { checkLimitBAD("SELECT CAST(-129 as SMALLINT) from SYSIBM.SYSDUMMY1","java.sql.SQLException", "getByte"); }
   
   /** getByte() -- limit test -- high limit ok from INTEGER   */
   public void Var049() { checkLimitOK("SELECT CAST(127 as INTEGER) from SYSIBM.SYSDUMMY1", "127", "getByte"); }
   /** getByte() -- limit test -- low limit ok from INTEGER   */
   public void Var050() { checkLimitOK("SELECT CAST(-128 as INTEGER) from SYSIBM.SYSDUMMY1", "-128", "getByte"); }
   /** getByte() -- limit test -- high limit bad from INTEGER   */
   public void Var051() { checkLimitBAD("SELECT CAST(128 as INTEGER) from SYSIBM.SYSDUMMY1", "java.sql.SQLException", "getByte"); }
   /** getByte() -- limit test -- low limit bad from INTEGER   */
   public void Var052() { checkLimitBAD("SELECT CAST(-129 as INTEGER) from SYSIBM.SYSDUMMY1","java.sql.SQLException", "getByte"); }

   /** getByte() -- limit test -- high limit ok from BIGINT   */
   public void Var053() { checkLimitOK("SELECT CAST(127 as BIGINT) from SYSIBM.SYSDUMMY1", "127", "getByte"); }
   /** getByte() -- limit test -- low limit ok from BIGINT   */
   public void Var054() { checkLimitOK("SELECT CAST(-128 as BIGINT) from SYSIBM.SYSDUMMY1", "-128", "getByte"); }
   /** getByte() -- limit test -- high limit bad from BIGINT   */
   public void Var055() { checkLimitBAD("SELECT CAST(128 as BIGINT) from SYSIBM.SYSDUMMY1", "java.sql.SQLException", "getByte"); }
   /** getByte() -- limit test -- low limit bad from BIGINT   */
   public void Var056() { checkLimitBAD("SELECT CAST(-129 as BIGINT) from SYSIBM.SYSDUMMY1","java.sql.SQLException", "getByte"); }

   /** getByte() -- limit test -- high limit ok from REAL   */
   public void Var057() { checkLimitOK("SELECT CAST(127 as REAL) from SYSIBM.SYSDUMMY1", "127", "getByte"); }
   /** getByte() -- limit test -- low limit ok from REAL   */
   public void Var058() { checkLimitOK("SELECT CAST(-128 as REAL) from SYSIBM.SYSDUMMY1", "-128", "getByte"); }
   /** getByte() -- limit test -- high limit bad from REAL   */
   public void Var059() { checkLimitBAD("SELECT CAST(128 as REAL) from SYSIBM.SYSDUMMY1", "java.sql.SQLException", "getByte"); }
   /** getByte() -- limit test -- low limit bad from REAL   */
   public void Var060() { checkLimitBAD("SELECT CAST(-129 as REAL) from SYSIBM.SYSDUMMY1","java.sql.SQLException", "getByte"); }

   
   /** getByte() -- limit test -- high limit ok from FLOAT   */
   public void Var061() { checkLimitOK("SELECT CAST(127 as FLOAT) from SYSIBM.SYSDUMMY1", "127", "getByte"); }
   /** getByte() -- limit test -- low limit ok from FLOAT   */
   public void Var062() { checkLimitOK("SELECT CAST(-128 as FLOAT) from SYSIBM.SYSDUMMY1", "-128", "getByte"); }
   /** getByte() -- limit test -- high limit bad from FLOAT   */
   public void Var063() { checkLimitBAD("SELECT CAST(128 as FLOAT) from SYSIBM.SYSDUMMY1", "java.sql.SQLException", "getByte"); }
   /** getByte() -- limit test -- low limit bad from FLOAT   */
   public void Var064() { checkLimitBAD("SELECT CAST(-129 as FLOAT) from SYSIBM.SYSDUMMY1","java.sql.SQLException", "getByte"); }


   /** getByte() -- limit test -- high limit ok from DOUBLE   */
   public void Var065() { checkLimitOK("SELECT CAST(127 as DOUBLE) from SYSIBM.SYSDUMMY1", "127", "getByte"); }
   /** getByte() -- limit test -- low limit ok from DOUBLE   */
   public void Var066() { checkLimitOK("SELECT CAST(-128 as DOUBLE) from SYSIBM.SYSDUMMY1", "-128", "getByte"); }
   /** getByte() -- limit test -- high limit bad from DOUBLE   */
   public void Var067() { checkLimitBAD("SELECT CAST(128 as DOUBLE) from SYSIBM.SYSDUMMY1", "java.sql.SQLException", "getByte"); }
   /** getByte() -- limit test -- low limit bad from DOUBLE   */
   public void Var068() { checkLimitBAD("SELECT CAST(-129 as DOUBLE) from SYSIBM.SYSDUMMY1","java.sql.SQLException", "getByte"); }

   /** getByte() -- limit test -- high limit ok from DECIMAL   */
   public void Var069() { checkLimitOK("SELECT CAST(127 as DECIMAL(6,3)) from SYSIBM.SYSDUMMY1", "127", "getByte"); }
   /** getByte() -- limit test -- low limit ok from DECIMAL   */
   public void Var070() { checkLimitOK("SELECT CAST(-128 as DECIMAL(6,3)) from SYSIBM.SYSDUMMY1", "-128", "getByte"); }
   /** getByte() -- limit test -- high limit bad from DECIMAL   */
   public void Var071() { checkLimitBAD("SELECT CAST(128 as DECIMAL(6,3)) from SYSIBM.SYSDUMMY1", "java.sql.SQLException", "getByte"); }
   /** getByte() -- limit test -- low limit bad from DECIMAL   */
   public void Var072() { checkLimitBAD("SELECT CAST(-129 as DECIMAL(6,3)) from SYSIBM.SYSDUMMY1","java.sql.SQLException", "getByte"); }

   /** getByte() -- limit test -- high limit ok from NUMERIC   */
   public void Var073() { checkLimitOK("SELECT CAST(127 as NUMERIC(6,3)) from SYSIBM.SYSDUMMY1", "127", "getByte"); }
   /** getByte() -- limit test -- low limit ok from NUMERIC   */
   public void Var074() { checkLimitOK("SELECT CAST(-128 as NUMERIC(6,3)) from SYSIBM.SYSDUMMY1", "-128", "getByte"); }
   /** getByte() -- limit test -- high limit bad from NUMERIC   */
   public void Var075() { checkLimitBAD("SELECT CAST(128 as NUMERIC(6,3)) from SYSIBM.SYSDUMMY1", "java.sql.SQLException", "getByte"); }
   /** getByte() -- limit test -- low limit bad from NUMERIC   */
   public void Var076() { checkLimitBAD("SELECT CAST(-129 as NUMERIC(6,3)) from SYSIBM.SYSDUMMY1","java.sql.SQLException", "getByte"); }

   /** getByte() -- limit test -- high limit ok from CHAR   */
   public void Var077() { checkLimitOK("SELECT CAST(127 as CHAR(6)) from SYSIBM.SYSDUMMY1", "127", "getByte"); }
   /** getByte() -- limit test -- low limit ok from CHAR   */
   public void Var078() { checkLimitOK("SELECT CAST(-128 as CHAR(6)) from SYSIBM.SYSDUMMY1", "-128", "getByte"); }
   /** getByte() -- limit test -- high limit bad from CHAR   */
   public void Var079() { checkLimitBAD("SELECT CAST(128 as CHAR(6)) from SYSIBM.SYSDUMMY1", "java.sql.SQLException", "getByte"); }
   /** getByte() -- limit test -- low limit bad from CHAR   */
   public void Var080() { checkLimitBAD("SELECT CAST(-129 as CHAR(6)) from SYSIBM.SYSDUMMY1","java.sql.SQLException", "getByte"); }

   /** getByte() -- limit test -- high limit ok from VARCHAR   */
   public void Var081() { 
     if (getDriver() == JDTestDriver.DRIVER_JCC) {
       notApplicable("jcc returns SQL0461 CAST not support"); 
       return; 
     }
     checkLimitOK("SELECT CAST(127 as VARCHAR(6)) from SYSIBM.SYSDUMMY1", "127", "getByte"); }
   /** getByte() -- limit test -- low limit ok from VARCHAR   */
   public void Var082() { 
     if (getDriver() == JDTestDriver.DRIVER_JCC) {
       notApplicable("jcc returns SQL0461 CAST not support"); 
       return; 
     }
     checkLimitOK("SELECT CAST(-128 as VARCHAR(6)) from SYSIBM.SYSDUMMY1", "-128", "getByte"); }
   /** getByte() -- limit test -- high limit bad from VARCHAR   */
   public void Var083() {  baseTruncationTest(" CAST (128  AS VARCHAR(6))", "getByte"); }
   /** getByte() -- limit test -- low limit bad from VARCHAR   */
   public void Var084() {  baseTruncationTest(" CAST (-129  AS VARCHAR(6))", "getByte"); }


   /**
   getByte() - Get from an SQLXML. 
   **/
  public void Var085() {
    if (checkXmlSupport()) {
      try {
        Statement stmt = connection_.createStatement(); 
        ResultSet rs = stmt.executeQuery("SELECT * FROM "
            + JDRSTest.RSTEST_GETXML);
        rs.next();
        byte v = rs.getByte("C_XML");
        failed("Didn't throw SQLException " + v);
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String exInfo = e.toString(); 
          String expected = "Invalid data conversion";
          assertCondition(exInfo.indexOf(expected)>=0, 
              "Got exception '"+exInfo+"' expected '"+expected+"'"); 
            
        } else { 
          e.printStackTrace(); 
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }

    }

  }


        /**
         * getShort() - Get from a TIMESTAMP(12).
         **/
        public void Var086() {
            if (checkTimestamp12Support()) {
              testGet(statement0_,
                  "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
                  "getShort", 
                  "EXCEPTION:Data type mismatch.", 
                  " -- added 11/19/2012"); 

            }
        }


        /**
         * getShort() - Get from a TIMESTAMP(0).
         **/
        public void Var087() {
            if (checkTimestamp12Support()) {
          testGet(statement0_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
            "getShort",
		  "EXCEPTION:Data type mismatch.", 
            " -- added 11/19/2012"); 
            
            
            }
        }

/**
getByte() - Get from a BOOLEAN.
**/
    public void Var088()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_FALSE");
            byte v = rs.getByte ("C_BOOLEAN");
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


/**
getByte() - Get from a BOOLEAN.
**/
    public void Var089()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_TRUE");
            byte v = rs.getByte ("C_BOOLEAN");
            assertCondition (v == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

/**
getByte() - Get from a BOOLEAN.
**/
    public void Var090()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_NULL");
            byte v = rs.getByte ("C_BOOLEAN");
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



}



