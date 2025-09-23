///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetFloat.java
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

import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JDRSGetFloat.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getFloat
</ul>
**/
public class JDRSGetFloat
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetFloat";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private Statement           statement_;
    private String              statementQuery_; 
    private Statement           statement0_;
    private ResultSet           rs_;
    int driver_;



/**
Constructor.
**/
    public JDRSGetFloat (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSGetFloat",
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
	driver_ = getDriver();

        // SQL400 - driver neutral...
	
        String url = baseURL_
        // String url = "jdbc:as400://" + systemObject_.getSystemName()
            
            ;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
	setAutoCommit(connection_, false); // @E1A

        statement0_ = connection_.createStatement ();

        if (isJdbc20 ()) {
	    if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		statement_ = connection_.createStatement (); 
	    } else { 
		statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							  ResultSet.CONCUR_UPDATABLE);
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
Compares 2 floats, and allows a little rounding error.
**/
    private boolean compare (float d1, float d2)
    {
        return (Math.abs (d1 - d2) < 0.000001);
    }


/**
getFloat() - Should throw exception when the result set is
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
            rs.getFloat (1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getFloat() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            float v = rs.getFloat ("C_FLOAT");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getFloat() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            float v = rs.getFloat (100);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getFloat() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            float v = rs.getFloat (0);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getFloat() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            float v = rs.getFloat (-1);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getFloat() - Should work when the column index is valid.
**/
    public void Var006()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            float v = rs.getFloat (5);
            assertCondition (v == 5.55f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getFloat() - Should throw an exception when the column
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
          rs.getFloat (null);
          failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }


/**
getFloat() - Should throw an exception when the column
name is an empty string.
**/
    public void Var008()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.getFloat ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getFloat() - Should throw an exception when the column
name is invalid.
**/
    public void Var009()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            rs.getFloat ("INVALID");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getFloat() - Should work when the column name is valid.
**/
    public void Var010()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            float v = rs.getFloat ("C_INTEGER");
            assertCondition (v == 98765);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getFloat() - Should work when an update is pending.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, statementQuery_, rs_, "UPDATE_SANDBOX");
            rs_.updateFloat ("C_FLOAT", 1928.55f);
            float v = rs_.getFloat ("C_FLOAT");
            assertCondition (v == 1928.55f);
         }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getFloat() - Should work when an update has been done.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, statementQuery_, rs_, "UPDATE_SANDBOX");
            rs_.updateFloat ("C_FLOAT", -11.001252f);
            rs_.updateRow ();
            float v = rs_.getFloat ("C_FLOAT");
            assertCondition (v == -11.001252f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getFloat() - Should work when the current row is the insert
row, when an insert is pending.
**/
    public void Var013()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support moveToInsertRow"); 
      } else { 
        
        if (checkJdbc20 ()) {
          try {
            rs_.moveToInsertRow ();
            rs_.updateFloat ("C_FLOAT", -192893.0f);
            float v = rs_.getFloat ("C_FLOAT");
            assertCondition (v == -192893.0f);
          }
          catch (Exception e) {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }


/**
getFloat() - Should work when the current row is the insert
row, when an insert has been done.
**/
    public void Var014()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support moveToInsertRow"); 
      } else { 
        if (checkJdbc20 ()) {
          try {
            rs_.moveToInsertRow ();
            rs_.updateFloat ("C_FLOAT", 2531027.25111111f);
            rs_.insertRow ();
            float v = rs_.getFloat ("C_FLOAT");
            assertCondition (v == 2531027.25111111f);
          }
          catch (Exception e) {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }



/**
getFloat() - Should throw an exception on a deleted row.
**/
    public void Var015()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not throw exception for deleted row"); 
      } else { 

        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, statementQuery_, rs_, "DUMMY_ROW");
            rs_.deleteRow ();
            float v = rs_.getFloat ("C_FLOAT");
            failed ("Didn't throw SQLException on deleted row but got "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
      }
    }


/**
getFloat() - Should return null when the column is NULL.
**/
    public void Var016 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 ( rs, "NUMBER_NULL");
            float v = rs.getFloat ("C_FLOAT");
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getFloat() - Get from a SMALLINT.
**/
    public void Var017 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            float v = rs.getFloat ("C_SMALLINT");
	    String s = rs.getString ("C_SMALLINT");
            assertCondition (v == -198, "Got "+v+" from string "+s+" sb -198");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getFloat() - Get from a INTEGER.
**/
    public void Var018 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            float v = rs.getFloat ("C_INTEGER");
            assertCondition (v == 98765);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getFloat() - Get from a REAL.
**/
    public void Var019 ()
    {
	String s = ""; 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
	    s = rs.getString ("C_REAL");
            float v = rs.getFloat ("C_REAL");
            assertCondition (compare (v, -4.4f));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception s="+s);
        }
    }



/**
getFloat() - Get from a FLOAT.
**/
    public void Var020 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            float v = rs.getFloat ("C_FLOAT");
            assertCondition (v == 5.55f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getFloat() - Get from a DOUBLE.
**/
    public void Var021 ()
    {
	String s = "" ; 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
	    s = rs.getString ("C_DOUBLE");
            float v = rs.getFloat ("C_DOUBLE");
            assertCondition (v == -6.666f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception s="+s);
        }
    }



/**
getFloat() - Get from a DECIMAL.
**/
    public void Var022 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            float v = rs.getFloat ("C_DECIMAL_50");
            assertCondition (v == 7);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getFloat() - Get from a NUMERIC.
**/
    public void Var023 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            float v = rs.getFloat ("C_NUMERIC_105");
            assertCondition (v == -10.10105f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getFloat() - Get from a CHAR, where the value does not translate
to an int.
**/
    public void Var024 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            float v = rs.getFloat ("C_CHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getFloat() - Get from a CHAR, where the value does translate
to an int.
**/
    public void Var025 ()
    {
	String s=""; 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_INT");
	     s= rs.getString ("C_CHAR_50");
            float v = rs.getFloat ("C_CHAR_50");
            assertCondition (v == -55);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception s="+s);
        }
    }



/**
getFloat() - Get from a CHAR, where the value does translate
to an int, except that it has a decimal point
**/
    public void Var026 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FLOAT");
            float v = rs.getFloat ("C_CHAR_50");
            assertCondition (v == 55.901f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getFloat() - Get from a VARCHAR, where the value does not translate
to an int.
**/
    public void Var027 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            float v = rs.getFloat ("C_VARCHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getFloat() - Get from a VARCHAR, where the value does translate
to an int.
**/
    public void Var028 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_INT");
            float v = rs.getFloat ("C_VARCHAR_50");
            assertCondition (v == 567);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getFloat() - Get from a VARCHAR, where the value does translate
to an int, except that it has a decimal point
**/
    public void Var029 ()
    {
	String s = ""; 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FLOAT");
	    s = rs.getString ("C_VARCHAR_50");
            float v = rs.getFloat ("C_VARCHAR_50");
            assertCondition (v == -567.56f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception s="+s);
        }
    }



/**
getFloat() - Get from a BINARY.
**/
    public void Var030 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            float v = rs.getFloat ("C_BINARY_20");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getFloat() - Get from a VARBINARY.
**/
    public void Var031 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            float v = rs.getFloat ("C_VARBINARY_20");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getFloat() - Get from a CLOB.
**/
    public void Var032 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                float v = rs.getFloat ("C_CLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getFloat() - Get from a DBCLOB.
**/
    public void Var033 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                float v = rs.getFloat ("C_DBCLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getFloat() - Get from a BLOB.
**/
    public void Var034 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                float v = rs.getFloat ("C_BLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getFloat() - Get from a DATE.
**/
    public void Var035 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            float v = rs.getFloat ("C_DATE");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getFloat() - Get from a TIME.
**/
    public void Var036 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            float v = rs.getFloat ("C_TIME");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getFloat() - Get from a TIMESTAMP.
**/
    public void Var037 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            float v = rs.getFloat ("C_TIMESTAMP");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getFloat() - Get from a DATALINK.
**/
    public void Var038 ()
    {
      
      if (checkDatalinkSupport ()) {
        try {
          Statement s = connection_.createStatement ();
          ResultSet rs = s.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_GETDL);
          JDRSTest.position0 (rs, "LOB_FULL");
          float v = rs.getFloat ("C_DATALINK");
          failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }


/**
getFloat() - Get from a DISTINCT.
**/
    public void Var039 ()
    {	String s=""; 
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
		s = rs.getString ("C_DISTINCT");
                float v = rs.getFloat ("C_DISTINCT");
                assertCondition (v == 123456789);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception s="+s);
            }
        }
    }



/**
getFloat() - Get from a BIGINT.
**/
    public void Var040()
    {
        if (checkBigintSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            float v = rs.getFloat ("C_BIGINT");
            assertCondition (v == -44332123);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }
    
    /**
     getFloat() - Get from DFP16 -- Use preset values
     **/
    public void Var041 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC doesn't support getting double from DECFLOAT -- Invalid data conversion: Wrong result column type for requested conversion.");
      } else {
        
        if (checkDecFloatSupport ()) {
          boolean success = true; 
          StringBuffer sb = new StringBuffer();
          
          try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP16);
            int i = 0; 
            while (rs.next()) { 
              double v = rs.getFloat (1);
              String expected = JDRSTest.VALUES_DFP16_REAL[i];
              if (expected == null) {
                if (v != 0) {
                  success = false;
                  sb.append("\nexpected "+expected+" but got "+v); 
                } 
              } else {
                if (expected.equals(""+v)) {
                } else {
                  sb.append("\nexpected "+expected+" but got "+v);
                  success = false;
                } 
              } 
              i++; 
            }
            assertCondition (success, "Error reading DFP16 values -- "+ sb.toString() );
          }
          catch (Exception e) {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }
    
    
    /**
     getFloat() - Get from DFP34 -- Use preset values
     **/
    public void Var042 ()
    {
      if (checkDecFloatSupport ()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC doesn't support getting double from DECFLOAT -- Invalid data conversion: Wrong result column type for requested conversion.");
        } else {
          boolean success = true; 
          StringBuffer sb = new StringBuffer();
          
          try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_DFP34);
            int i = 0; 
            while (rs.next()) { 
              double v = rs.getFloat (1);
              String expected = JDRSTest.VALUES_DFP34_REAL[i];
              if ("1234567890123456789012345678901234".equals(expected)) {
                // A float doesn't hold as much precision 
                expected = "1.2345678906183669E33"; 
              }

              if (expected == null) {
                if (v != 0.0) {
                  success = false;
                  sb.append("\nexpected "+expected+" but got "+v); 
                } 
              } else {
                if (expected.equals(""+v)) {
                } else {
                  sb.append("\nexpected "+expected+" but got "+v);
                  success = false;
                } 
              } 
              i++; 
            }
            assertCondition (success, "Error reading DFP34 values -- "+ sb.toString() );
          }
          catch (Exception e) {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }
    
    
    /**
     getFloat() - Get from DFP16: NAN 
     **/
    public void Var043 ()
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
            float v = rs.getFloat (1);
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
     getFloat() - Get from DFP16: -NAN 
     **/
    public void Var044 ()
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
            String v = ""+rs.getFloat (1);
            assertCondition(v.equals("NaN"), "Expected -NaN got "+v); 
          }
          catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
    
    /**
     getFloat() - Get from DFP16: INF 
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
                + JDRSTest.RSTEST_DFP16INF);
            rs.next(); 
            float v = rs.getFloat (1);
            assertCondition(v == Float.POSITIVE_INFINITY, "Expected Infinity got "+v);
          }
          catch (Exception e) {
            failed(e, "Unexpected Exception -- Expect infinity");
          }
        }
      }
    }
    
    /**
     getFloat() - Get from DFP16: -INF 
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
                + JDRSTest.RSTEST_DFP16NINF);
            rs.next(); 
            float v = rs.getFloat (1);
            assertCondition(v == Float.NEGATIVE_INFINITY, "Expected -Infinity got "+v); 
          }
          catch (Exception e) {
            failed(e, "Unexpected Exception expected -Infinity");
          }
        }
      }
    }
    
    /**
     getFloat() - Get from DFP34: NAN 
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
                + JDRSTest.RSTEST_DFP34NAN);
            rs.next(); 
            String  v = ""+rs.getFloat (1);
            assertCondition(v.equals("NaN"), "Expected NaN got "+v); 
          }
          catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
    
    /**
     getFloat() - Get from DFP34: -NAN 
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
                + JDRSTest.RSTEST_DFP34NNAN);
            rs.next(); 
            String v = ""+rs.getFloat (1);
            assertCondition(v.equals("NaN"), "Expected NaN got "+v); 
          }
          catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
    
    /**
     getFloat() - Get from DFP34: INF 
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
                + JDRSTest.RSTEST_DFP34INF);
            rs.next(); 
            float v = rs.getFloat (1);
            
            assertCondition(v == Float.POSITIVE_INFINITY, "Expected Infinity got "+v); 
          }
          catch (Exception e) {
            failed(e, "Unexpected Exception -- expected infinity");
          }
        }
      }
    }
    
    /**
     getFloat() - Get from DFP34: -INF 
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
                + JDRSTest.RSTEST_DFP34NINF);
            rs.next(); 
            float v = rs.getFloat (1);
            assertCondition(v == Float.NEGATIVE_INFINITY, "Expected -Infinity got "+v); 
          }
          catch (Exception e) {
            failed(e, "Unexpected Exception expected -Infinity");
          }
        }
      }
    }




    /*
     * Add tests to convert from different double values
     * Infinity, +Infinity, NaN, 1E301, -1E301,
     */

    public void Var051 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("jcc doesn't obtain special values from decfloat "); 
        return; 
      }
	String added = " -- added 07/09/2007 to test getFloat on double values"; 
  String[][] testVars =
 /* SQL VALUE */  /* Get value */ 
  {
{"Infinity","Infinity"},
{"-Infinity","-Infinity"},
{"NaN",      "NaN"}, 
{"1.7E300", "Exception"},
{"-1.7E300", "Exception"},
  };

  String sql =""; 
  boolean success = true;
  StringBuffer sb = new StringBuffer();
  String tablename = JDRSTest.COLLECTION+".JDRSGFLT51"; 
  try {
Statement stmt = connection_.createStatement(); 
initTable(stmt, tablename," ( C1 DOUBLE  )"); 

for (int i = 0; i < testVars.length; i++) {
    sql = "insert into "+tablename+" VALUES("+testVars[i][0]+")"; 
    stmt.executeUpdate(sql); 
}

ResultSet rs = statement0_.executeQuery ("SELECT * FROM "+tablename);
for (int i = 0; i < testVars.length; i++) {
    String expected = testVars[i][1];
    sql="Retrieving "+expected; 
    try {
  rs.next();
  String answer = ""+rs.getFloat(1);
  if (! expected.equals(answer)) {
      success=false;
      sb.append("\n Expected "+expected+" but got "+answer+" for "+testVars[i][0]); 
  } 
    } catch (Exception e) {
  if (!expected.equals("Exception")) {
      e.printStackTrace(); 
      success=false; 
      sb.append("\n Exception caught"+e+" for "+testVars[i][0]);

  } 
    } 
}
rs.close();
stmt.close();
assertCondition(success, sb.toString()+added); 
  }
  catch (Exception e) {

failed (e, "Unexpected Exception sql="+sql+added);
  } 
    }



    /**
    getFloat() - Get from a SQLXML.
    **/
        public void Var052 ()
        {
         
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GETXML);
                rs.next(); 
                float v = rs.getFloat ("SQL_XML");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }


        /**
         * getFloat() - Get from a TIMESTAMP(12).
         **/
        public void Var053() {
            if (checkTimestamp12Support()) {
              testGet(statement0_,
                  "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
                  "getFloat", 
                  "EXCEPTION:Data type mismatch.", 
                  " -- added 11/19/2012"); 

            }
        }


        /**
         * getFloat() - Get from a TIMESTAMP(0).
         **/
        public void Var054() {
            if (checkTimestamp12Support()) {
          testGet(statement0_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
            "getFloat",
		  "EXCEPTION:Data type mismatch.", 
            " -- added 11/19/2012"); 
            
            
            }
        }

        /**
         * getFloat() - Get from a TIMESTAMP(0).
         **/
        public void Var055() {
            if (checkTimestamp12Support()) {
          testGet(statement0_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
            "getFloat",
		  "EXCEPTION:Data type mismatch.", 
            " -- added 11/19/2012"); 
            
            
            }
        }


/**
getFloat() - Get from a BOOLEAN.
**/
    public void Var056()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_FALSE");
            float v = rs.getFloat ("C_BOOLEAN");
            assertCondition (v == 0.0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


/**
getFloat() - Get from a BOOLEAN.
**/
    public void Var057()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_TRUE");
            float v = rs.getFloat ("C_BOOLEAN");
            assertCondition (v == 1.0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

/**
getFloat() - Get from a BOOLEAN.
**/
    public void Var058()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_NULL");
            float v = rs.getFloat ("C_BOOLEAN");
            assertCondition (v == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }





}










