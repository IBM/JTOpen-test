///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetDouble.java
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSGetDouble.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getDouble
</ul>
**/
public class JDRSGetDouble
extends JDRSTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetDouble";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.

    private Connection          connection1_;       // @E1A
    private Statement           statement1_;        // @E1A



/**
Constructor.
**/
    public JDRSGetDouble (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSGetDouble",
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
      super.setup(); 
        String url = baseURL_
            
            ;
        
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          connection1_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);                        // @E1A
        } else { 
        connection1_ = testDriver_.getConnection (url+";big decimal=false",systemObject_.getUserId(), encryptedPassword_);     // @E1A
        }
	setAutoCommit(connection1_, false);                                      // @E2A
        statement1_ = connection1_.createStatement();                           // @E1A

    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
      super.cleanup(); 
        statement1_.close ();                   // @E1A
        connection1_.commit();                  // @E2A
        connection1_.close ();                  // @E1A
    }



/**
Compares 2 doubles, and allows a little rounding error.
**/
    private boolean compare (double d1, double d2)
    {
        return (Math.abs (d1 - d2) < 0.000001);
    }


/**
getDouble() - Should throw exception when the result set is
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
            rs.getDouble (1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDouble() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            double v = rs.getDouble ("C_DOUBLE");
            failed ("Didn't throw SQLException for "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDouble() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            double v = rs.getDouble (100);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDouble() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            double v = rs.getDouble (0);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDouble() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            double v = rs.getDouble (-1);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDouble() - Should work when the column index is valid.
**/
    public void Var006()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            double v = rs.getDouble (6);
            assertCondition (v == 6.666 || v == 6.6659999999999995, "got "+v+" sb 6.666/6.6659999999999995");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDouble() - Should throw an exception when the column
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
          rs.getDouble (null);
          failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }


/**
getDouble() - Should throw an exception when the column
name is an empty string.
**/
    public void Var008()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.getDouble ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDouble() - Should throw an exception when the column
name is invalid.
**/
    public void Var009()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            rs.getDouble ("INVALID");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDouble() - Should work when the column name is valid.
**/
    public void Var010()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            double v = rs.getDouble ("C_INTEGER");
            assertCondition (v == 98765);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDouble() - Should work when an update is pending.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateDouble ("C_DOUBLE", 1928.55);
            double v = rs_.getDouble ("C_DOUBLE");
            assertCondition (v == 1928.55);
         }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getDouble() - Should work when an update has been done.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateDouble ("C_DOUBLE", -11.001252);
            rs_.updateRow ();
            double v = rs_.getDouble ("C_DOUBLE");
            assertCondition (v == -11.001252);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }      
        }
    }



/**
getDouble() - Should work when the current row is the insert
row, when an insert is pending.
**/
    public void Var013()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("moveToInsertRow not supported by JCC"); 
      } else { 
        if (checkJdbc20 ()) {
          try {
            rs_.moveToInsertRow ();
            rs_.updateDouble ("C_DOUBLE", -192893.0);
            double v = rs_.getDouble ("C_DOUBLE");
            assertCondition (v == -192893.0);
          }
          catch (Exception e) {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }



/**
getDouble() - Should work when the current row is the insert
row, when an insert has been done.
**/
    public void Var014()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("moveToInsertRow not supported by JCC"); 
      } else { 
        
        if (checkJdbc20 ()) {
          try {
            rs_.moveToInsertRow ();
            rs_.updateDouble ("C_DOUBLE", 2531027.25111111);
            rs_.insertRow ();
            double v = rs_.getDouble ("C_DOUBLE");
            assertCondition (v == 2531027.25111111);
          }
          catch (Exception e) {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }


/**
getDouble() - Should throw an exception on a deleted row.
**/
    public void Var015()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC doesn't throw exception when getting deleted row"); 
      } else { 
        
        if (checkJdbc20 ()) {
          try {
            JDRSTest.position (rs_, "DUMMY_ROW");
            rs_.deleteRow ();
            double v = rs_.getDouble ("C_DOUBLE");
            failed ("Didn't throw SQLException"+v);
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
      }
    }

/**
getDouble() - Should return null when the column is NULL.
**/
    public void Var016 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            double v = rs.getDouble ("C_DOUBLE");
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDouble() - Get from a SMALLINT.
**/
    public void Var017 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            double v = rs.getDouble ("C_SMALLINT");
            assertCondition (v == -198);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDouble() - Get from a INTEGER.
**/
    public void Var018 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            double v = rs.getDouble ("C_INTEGER");
            assertCondition (v == 98765);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDouble() - Get from a REAL.
**/
    public void Var019 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            double v = rs.getDouble ("C_REAL");
            assertCondition (compare (v, -4.4));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDouble() - Get from a FLOAT.
**/
    public void Var020 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            double v = rs.getDouble ("C_FLOAT");
            assertCondition (v == 5.55, "Got "+v+" sb 5.55");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDouble() - Get from a DOUBLE.
**/
    public void Var021 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            double v = rs.getDouble ("C_DOUBLE");
            /* Z/OS returns 6.6659999999999995 */ 
            assertCondition (v == -6.666 || v == -6.6659999999999995, "Got "+v+" expected -6.666 ");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDouble() - Get from a DECIMAL.
**/
    public void Var022 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            double v = rs.getDouble ("C_DECIMAL_50");
            assertCondition (v == 7);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDouble() - Get from a NUMERIC.
**/
    public void Var023 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            double v = rs.getDouble ("C_NUMERIC_105");
            assertCondition (v == -10.10105, "got "+v+" sb -10.10105");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDouble() - Get from a CHAR, where the value does not translate
to an int.
**/
    public void Var024 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            double v = rs.getDouble ("C_CHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDouble() - Get from a CHAR, where the value does translate
to an int.
**/
    public void Var025 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_INT");
            double v = rs.getDouble ("C_CHAR_50");
            assertCondition (v == -55);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDouble() - Get from a CHAR, where the value does translate
to an int, except that it has a decimal point
**/
    public void Var026 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FLOAT");
            double v = rs.getDouble ("C_CHAR_50");
            assertCondition (v == 55.901);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDouble() - Get from a VARCHAR, where the value does not translate
to an int.
**/
    public void Var027 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            double v = rs.getDouble ("C_VARCHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDouble() - Get from a VARCHAR, where the value does translate
to an int.
**/
    public void Var028 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_INT");
            double v = rs.getDouble ("C_VARCHAR_50");
            assertCondition (v == 567);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDouble() - Get from a VARCHAR, where the value does translate
to an int, except that it has a decimal point
**/
    public void Var029 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FLOAT");
            double v = rs.getDouble ("C_VARCHAR_50");
            assertCondition (v == -567.56);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDouble() - Get from a BINARY.
**/
    public void Var030 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            double v = rs.getDouble ("C_BINARY_20");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDouble() - Get from a VARBINARY.
**/
    public void Var031 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            double v = rs.getDouble ("C_VARBINARY_20");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDouble() - Get from a CLOB.
**/
    public void Var032 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                double v = rs.getDouble ("C_CLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getDouble() - Get from a DBCLOB.
**/
    public void Var033 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                double v = rs.getDouble ("C_DBCLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getDouble() - Get from a BLOB.
**/
    public void Var034 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                double v = rs.getDouble ("C_BLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getDouble() - Get from a DATE.
**/
    public void Var035 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            double v = rs.getDouble ("C_DATE");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDouble() - Get from a TIME.
**/
    public void Var036 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            double v = rs.getDouble ("C_TIME");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDouble() - Get from a TIMESTAMP.
**/
    public void Var037 ()
    {
        ResultSet rs = null; 
        try {
            rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            double v = rs.getDouble ("C_TIMESTAMP");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDouble() - Get from a DATALINK.
**/
    public void Var038 ()
    {
      ResultSet rs = null; 
        if (checkDatalinkSupport ()) {
            try {
                Statement s = connection_.createStatement ();
                rs = s.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GETDL);
                JDRSTest.position0 (rs, "LOB_FULL");
                double v = rs.getDouble ("C_DATALINK");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                if (rs != null) {
                    try { 
                      rs.close();
                    } catch (Exception e2) { 
                      
                    }
                }
            }
        }
    }



/**
getDouble() - Get from a DISTINCT.
**/
    public void Var039 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                double v = rs.getDouble ("C_DISTINCT");
                rs.close(); 
                assertCondition (v == 123456789);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getDouble() - Get from a BIGINT.
**/
    public void Var040 ()
    {
        if (checkBigintSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            double v = rs.getDouble ("C_BIGINT");
            rs.close(); 
            /* commit to release the lock on the file */ 
            connection_.commit(); 
            assertCondition (v == 12374321);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



// @E1A
/**
getDouble() - Get from a DECIMAL, when the "big decimal" property
is set to "false".
**/
    public void Var041 ()
    {
        try {
            ResultSet rs = statement1_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            double v = rs.getDouble ("C_DECIMAL_50");
            assertCondition (v == 7);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



// @E1A
/**
getDouble() - Get from a NUMERIC, when the "big decimal" property
is set to "false".
**/
    public void Var042 ()
    {
        try {
            ResultSet rs = statement1_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            double v = rs.getDouble ("C_NUMERIC_105");
            assertCondition (compare(v, -10.10105));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


    
    /**
     getDouble() - Get from DFP16 -- Use preset values
     **/
    public void Var043 ()
    {
      
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC doesn't support getting double from DECFLOAT -- Invalid data conversion: Wrong result column type for requested conversion.");
      } else {
        testDfp(JDRSTest.RSTEST_DFP16, JDRSTest.VALUES_DFP16_DOUBLE, statement0_, "getDouble");
      }
    }
    
    
    /**
     getDouble() - Get from DFP34 -- Use preset values
     **/
    public void Var044 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC doesn't support getting double from DECFLOAT -- Invalid data conversion: Wrong result column type for requested conversion.");
      } else {
        testDfp(JDRSTest.RSTEST_DFP34, JDRSTest.VALUES_DFP34_DOUBLE, statement0_, "getDouble");
      }
    }
    
    
    /**
     getDouble() - Get from DFP16: NAN  
     **/
    public void Var045 ()
    {
      if (checkDecFloatSupport()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC doesn't support getting double from DECFLOAT -- Invalid data conversion: Wrong result column type for requested conversion.");
        } else {
          try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP16NAN);
            rs.next(); 
            double v = rs.getDouble (1);
            String vString = ""+v; 
            assertCondition(vString.equals("NaN"), "Expected NaN got "+v); 
          }
          catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
    
    /**
     getDouble() - Get from DFP16: -NAN 
     **/
    public void Var046 ()
    {
      if (checkDecFloatSupport()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC doesn't support getting double from DECFLOAT -- Invalid data conversion: Wrong result column type for requested conversion.");
        } else {
          try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP16NNAN);
            rs.next(); 
            String v = ""+rs.getDouble (1);
            assertCondition(v.equals("NaN"), "Expected -NaN got "+v); 
          }
          catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
    
    /**
     getDouble() - Get from DFP16: INF 
     **/
    public void Var047 ()
    {
      if (checkDecFloatSupport()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC doesn't support getting double from DECFLOAT -- Invalid data conversion: Wrong result column type for requested conversion.");
        } else {
          try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP16INF);
            rs.next(); 
            double v = rs.getDouble (1);
            assertCondition(v == Double.POSITIVE_INFINITY, "Expected Infinity got "+v);
          }
          catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
    
    /**
     getDouble() - Get from DFP16: -INF 
     **/
    public void Var048 ()
    {
      if (checkDecFloatSupport()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC doesn't support getting double from DECFLOAT -- Invalid data conversion: Wrong result column type for requested conversion.");
        } else {
          try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP16NINF);
            rs.next(); 
            double v = rs.getDouble (1);
            assertCondition(v == Double.NEGATIVE_INFINITY, "Expected -Infinity got "+v); 
          }
          catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
    
    /**
     getDouble() - Get from DFP34: NAN 
     **/
    public void Var049 ()
    {
      if (checkDecFloatSupport()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC doesn't support getting double from DECFLOAT -- Invalid data conversion: Wrong result column type for requested conversion.");
        } else {
          try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP34NAN);
            rs.next(); 
            String  v = ""+rs.getDouble (1);
            assertCondition(v.equals("NaN"), "Expected NaN got "+v); 
          }
          catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
    
    /**
     getDouble() - Get from DFP34: -NAN 
     **/
    public void Var050 ()
    {
      if (checkDecFloatSupport()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC doesn't support getting double from DECFLOAT -- Invalid data conversion: Wrong result column type for requested conversion.");
        } else {
          try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP34NNAN);
            rs.next(); 
            String v = ""+rs.getDouble (1);
            assertCondition(v.equals("NaN"), "Expected NaN got "+v); 
          }
          catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
    
    /**
     getDouble() - Get from DFP34: INF -- will throw an exception 
     **/
    public void Var051 ()
    {
      if (checkDecFloatSupport()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC doesn't support getting double from DECFLOAT -- Invalid data conversion: Wrong result column type for requested conversion.");
        } else {
          try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP34INF);
            rs.next(); 
            double v = rs.getDouble (1);
            
            assertCondition(v == Double.POSITIVE_INFINITY, "Expected Infinity got "+v); 
          }
          catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
    
    /**
     getDouble() - Get from DFP34: -INF 
     **/
    public void Var052 ()
    {
      if (checkDecFloatSupport()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC doesn't support getting double from DECFLOAT -- Invalid data conversion: Wrong result column type for requested conversion.");
        } else {
          try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP34NINF);
            rs.next(); 
            double v = rs.getDouble (1);
            assertCondition(v == Double.NEGATIVE_INFINITY, "Expected -Infinity got "+v); 
          }
          catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
    
    
    /**
    getDouble() - Get from a SQLXML
    **/
        public void Var053 ()
        {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GETXML);
                rs.next(); 
                double v = rs.getDouble ("C_XML"); 
                failed ("Didn't throw SQLException but got "+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }


    
        /**
         * getDouble() - Get from a TIMESTAMP(12).
         **/
        public void Var054() {
            if (checkTimestamp12Support()) {
              testGet(statement0_,
                  "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
                  "getDouble", 
                  "EXCEPTION:Data type mismatch.", 
                  " -- added 11/19/2012"); 

            }
        }


        /**
         * getDouble() - Get from a TIMESTAMP(0).
         **/
        public void Var055() {
            if (checkTimestamp12Support()) {
          testGet(statement0_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
            "getDouble",
		  "EXCEPTION:Data type mismatch.", 
            " -- added 11/19/2012"); 
            
            
            }
        }


/**
getDouble() - Get from a BOOLEAN.
**/
    public void Var056()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_FALSE");
            double v = rs.getDouble ("C_BOOLEAN");
            assertCondition (v == 0.0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


/**
getDouble() - Get from a BOOLEAN.
**/
    public void Var057()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_TRUE");
            double v = rs.getDouble ("C_BOOLEAN");
            assertCondition (v == 1.0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

/**
getDouble() - Get from a BOOLEAN.
**/
    public void Var058()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_NULL");
            double v = rs.getDouble ("C_BOOLEAN");
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }





}



