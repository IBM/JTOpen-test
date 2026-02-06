///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetBigDecimal.java
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
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JDRSGetBigDecimal.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getBigDecimal()
<li>getBigDecimal() with 2 parameters
</ul>
**/
public class JDRSGetBigDecimal
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetBigDecimal";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private Statement           statement_;
    private Statement           statement0_;
    private ResultSet           rs_;
    private int                 driver_;
    private String              rsQuery_; 



/**
Constructor.
**/
    public JDRSGetBigDecimal (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSGetBigDecimal",
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

        // SQL400 - driver neutral...
        String url = baseURL_
        // String url = "jdbc:as400://" + systemObject_.getSystemName()
           ;
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(),encryptedPassword_,"JDRSGetBigDecimal");
	setAutoCommit(connection_, false); // @E1A

        statement0_ = connection_.createStatement ();

        if (isJdbc20 ()) {
	    driver_ = getDriver();
	    if (driver_ == JDTestDriver.DRIVER_JTOPENLITE) {
		statement_ = connection_.createStatement(); 
	    } else { 
		statement_ = connection_.createStatement
		  (ResultSet.TYPE_SCROLL_SENSITIVE,
		   ResultSet.CONCUR_UPDATABLE);
	    }
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_GET
                + " (C_KEY) VALUES ('DUMMYROW_GDB')");
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_GET
                + " (C_KEY) VALUES ('DUMMYROW_GDB2')");
	    rsQuery_ = "SELECT * FROM "
                + JDRSTest.RSTEST_GET + " FOR UPDATE"; 
            rs_ = statement_.executeQuery (rsQuery_);
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
getBigDecimal() - Should throw exception when the result set is
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
            rs.getBigDecimal ("C_DECIMAL_105");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Should throw exception when the result 
set is closed.
**/
    @SuppressWarnings("deprecation")
   public void Var002()
    {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.close ();
            rs.getBigDecimal ("C_DECIMAL_105", 3);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBigDecimal() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var003() 
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, null);
            BigDecimal v = rs_.getBigDecimal ("C_NUMERIC_105");
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Should throw exception when cursor 
is not pointing to a row.
**/
    @SuppressWarnings("deprecation")
    public void Var004()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            BigDecimal v = rs.getBigDecimal ("C_NUMERIC_105", 3);
            failed ("Didn't throw SQLException " +v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBigDecimal() - Should throw an exception when the column
is an invalid index.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_POS");
            BigDecimal v = rs_.getBigDecimal (100);
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Should throw an exception when the 
column is an invalid index.
**/
    @SuppressWarnings("deprecation")
    public void Var006()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            BigDecimal v = rs.getBigDecimal (100, 3);
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBigDecimal() - Should throw an exception when the column
is 0.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_POS");
            BigDecimal v = rs_.getBigDecimal (0);
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Should throw an exception when the column
is 0.
**/
    @SuppressWarnings("deprecation")
    public void Var008()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            BigDecimal v = rs.getBigDecimal (0, 2);
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBigDecimal() - Should throw an exception when the column
is -1.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_POS");
            BigDecimal v = rs_.getBigDecimal (-1);
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Should throw an exception when the column
is -1.
**/
    @SuppressWarnings("deprecation")
    public void Var010()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            BigDecimal v = rs.getBigDecimal (-1, 1);
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBigDecimal() - Should work when the column index is valid.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_POS");
            BigDecimal v = rs_.getBigDecimal (8);
            assertCondition (v.doubleValue () == 8.8888);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Should work when the column index is valid.
**/
    @SuppressWarnings("deprecation")
    public void Var012()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            BigDecimal v = rs.getBigDecimal (8, 3);
            double expected = 8.889; 
            if (getDriver() == JDTestDriver.DRIVER_JCC || getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
              expected = 8.888; 
            }
            assertCondition (v.doubleValue () == expected, 
                "Expected double values of "+expected+" but got "+v.doubleValue());
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Should throw an exception when the column
name is null.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_POS");
            rs_.getBigDecimal (null);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          if (getDriver() == JDTestDriver.DRIVER_JCC ) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
            
          } else { 
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Should throw an exception when the column
name is null.
**/
    @SuppressWarnings("deprecation")
    public void Var014()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_POS");
            rs_.getBigDecimal (null, 2);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
            
          } else { 
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
        }
    }



/**
getBigDecimal() - Should throw an exception when the column
name is an empty string.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_POS");
            rs_.getBigDecimal ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Should throw an exception when the column
name is an empty string.
**/
    @SuppressWarnings("deprecation")
    public void Var016()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            rs.getBigDecimal ("", 3);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBigDecimal() - Should throw an exception when the column
name is invalid.
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_POS");
            rs_.getBigDecimal ("INVALID");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getBigDecimal() - Should throw an exception when the column
name is invalid.
**/
    @SuppressWarnings("deprecation")
    public void Var018()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.getBigDecimal ("INVALID", 1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBigDecimal() - Should work when the column name is valid.
**/
    public void Var019()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_POS");
            BigDecimal v = rs_.getBigDecimal ("C_NUMERIC_105");
            assertCondition (v.doubleValue () == 10.10105);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Should work when the column name is valid.
**/
    @SuppressWarnings("deprecation")
    public void Var020()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            BigDecimal v = rs.getBigDecimal ("C_NUMERIC_105", 2);
            assertCondition (v.doubleValue () == 10.10);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() with 2 parameters - Should throw an exception when the
scale is -1.
**/
    @SuppressWarnings("deprecation")
 public void Var021()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC  ) {
         notApplicable("LUW does not throw exception when scale is -1");
         return; 
       }
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            BigDecimal v = rs.getBigDecimal ("C_NUMERIC_105", -1);
            failed ("Didn't throw SQLException but got "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBigDecimal() with 2 parameters - Should work when the scale is 0.
**/
    @SuppressWarnings("deprecation")
   public void Var022()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            BigDecimal v = rs.getBigDecimal ("C_NUMERIC_105", 0);
            assertCondition ((v.doubleValue () == 10) && (v.scale () == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() with 2 parameters - Should work when the scale is 3.
**/
    @SuppressWarnings("deprecation")
 public void Var023()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            BigDecimal v = rs.getBigDecimal ("C_NUMERIC_105", 3);
            assertCondition ((v.doubleValue () == 10.101) && (v.scale () == 3));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Should work when an update is pending.
**/
    public void Var024()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "UPDATE_SANDBOX");
            rs_.updateBigDecimal ("C_DECIMAL_105", new BigDecimal ("1982.55"));
            BigDecimal v = rs_.getBigDecimal ("C_DECIMAL_105");
            assertCondition (v.doubleValue () == 1982.55);
         }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Should work when an update is pending.
**/
    @SuppressWarnings("deprecation")
   public void Var025()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "UPDATE_SANDBOX");
            rs_.updateBigDecimal ("C_DECIMAL_105", new BigDecimal ("1982.55"));
            BigDecimal v = rs_.getBigDecimal ("C_DECIMAL_105", 3);
            assertCondition (v.doubleValue () == 1982.550);
         }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() - Should work when an update has been done.
**/
    public void Var026()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "UPDATE_SANDBOX");
            rs_.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("-11.00215"));
            rs_.updateRow ();
            BigDecimal v = rs_.getBigDecimal ("C_NUMERIC_105");
            assertCondition (v.doubleValue () == -11.00215);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Should work when an update has been done.
**/
    @SuppressWarnings("deprecation")
   public void Var027()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "UPDATE_SANDBOX");
            rs_.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("-11.00215"));
            rs_.updateRow ();
            BigDecimal v = rs_.getBigDecimal ("C_NUMERIC_105", 3);
            assertCondition (v.doubleValue () == -11.002);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() - Should work when the current row is the insert
row, when an insert is pending.
**/
    public void Var028()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateBigDecimal ("C_DECIMAL_105", new BigDecimal ("-1993.0"));
            BigDecimal v = rs_.getBigDecimal ("C_DECIMAL_105");
            assertCondition (v.doubleValue () == -1993.0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Should work when the current row is 
the insert row, when an insert is pending.
**/
    @SuppressWarnings("deprecation")
   public void Var029()
    {
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateBigDecimal ("C_DECIMAL_105", new BigDecimal ("-1993.0"));
            BigDecimal v = rs_.getBigDecimal ("C_DECIMAL_105", 0);
            assertCondition (v.doubleValue () == -1993);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() - Should work when the current row is the insert
row, when an insert has been done.
**/
    public void Var030()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC  ) {
        notApplicable("JCC does not work when moving to insert row"); 
        return; 
      }
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("25310.1111"));
            rs_.insertRow ();
            BigDecimal v = rs_.getBigDecimal ("C_NUMERIC_105");
            assertCondition (v.doubleValue () == 25310.1111);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Should work when the current row is 
the insert row, when an insert has been done.
**/
    @SuppressWarnings("deprecation")
 public void Var031()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC  ) {
        notApplicable("JCC does not work when moving to insert row"); 
        return; 
      }
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("25310.1111"));
            rs_.insertRow ();
            BigDecimal v = rs_.getBigDecimal ("C_NUMERIC_105", 5);
            assertCondition (v.doubleValue () == 25310.11110);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() - Should throw an exception on a deleted row.
**/
    public void Var032()
    {
      
      if (getDriver() == JDTestDriver.DRIVER_JCC  ) {
        notApplicable("JCC does not throw exception on deleted row"); 
        return; 
      }

        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "DUMMYROW_GDB");
            rs_.deleteRow ();
            BigDecimal v = rs_.getBigDecimal ("C_DECIMAL_105");
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


/**
getBigDecimal() with 2 parameters - Should throw an exception on a deleted row.
**/
    @SuppressWarnings("deprecation")
    public void Var033()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC  ) {
        notApplicable("JCC does not throw exception on deleted row"); 
        return; 
      }

        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "DUMMYROW_GDB2");
            rs_.deleteRow ();
            BigDecimal v = rs_.getBigDecimal ("C_DECIMAL_105", 4);
            failed ("Didn't throw SQLException "+v  );
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


/**
getBigDecimal() - Should return null when the column is NULL.
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_NULL");
            BigDecimal v = rs_.getBigDecimal ("C_NUMERIC_105");
            assertCondition (v == null, "Column should have been null but got "+ v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Should return null when the column is NULL.
**/
    @SuppressWarnings("deprecation")
    public void Var035 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NULL");
            BigDecimal v = rs.getBigDecimal ("C_NUMERIC_105", 3);
            assertCondition (v == null, "Should be null but got "+v);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Get from a SMALLINT.
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_NEG");
            BigDecimal v = rs_.getBigDecimal ("C_SMALLINT");
	    double value = v.doubleValue ();
	    int scale = v.scale(); 
            assertCondition ((value == -198) && (scale == 0), "value="+value+" sb -198 scale="+scale+" sb 0" );
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a SMALLINT, scale = 0.
**/
    @SuppressWarnings("deprecation")
    public void Var037 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            BigDecimal v = rs.getBigDecimal ("C_SMALLINT", 0);

	    double value = v.doubleValue ();
	    int scale = v.scale(); 
            assertCondition ((value == -198) && (scale == 0), "value="+value+" sb -198 scale="+scale+" sb 0" );

        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a SMALLINT, scale = 3.
**/
    @SuppressWarnings("deprecation")
    public void Var038 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            BigDecimal v = rs.getBigDecimal ("C_SMALLINT", 3);

	    double value = v.doubleValue ();
	    int scale = v.scale(); 
            assertCondition ((value == -198.000) && (scale == 3), "value="+value+" sb -198.000 scale="+scale+" sb 3" );

        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Get from a INTEGER.
**/
    public void Var039 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_POS");
            BigDecimal v = rs_.getBigDecimal ("C_INTEGER");
            assertCondition ((v.doubleValue () == 98765) && (v.scale () == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a INTEGER, scale = 0.
**/
    @SuppressWarnings("deprecation")
    public void Var040 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            BigDecimal v = rs.getBigDecimal ("C_INTEGER", 0);
            assertCondition ((v.doubleValue () == 98765) && (v.scale () == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a INTEGER, scale = 3.
**/
    @SuppressWarnings("deprecation")
    public void Var041 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            BigDecimal v = rs.getBigDecimal ("C_INTEGER", 3);
            assertCondition ((v.doubleValue () == 98765) && (v.scale () == 3));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Get from a REAL.
**/
    public void Var042 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_NEG");
            BigDecimal v = rs_.getBigDecimal ("C_REAL");
            assertCondition ((v.doubleValue () == -4.4) && (v.scale () == 1), "Value = "+v.doubleValue()+" sb -4.4 scale="+v.scale()+" sb 1");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a REAL, scale = 0.
**/
    @SuppressWarnings("deprecation")
    public void Var043 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            BigDecimal v = rs.getBigDecimal ("C_REAL", 0);
            assertCondition ((v.doubleValue () == -4) && (v.scale () == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a REAL, scale = 3.
**/
    @SuppressWarnings("deprecation")
    public void Var044 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            BigDecimal v = rs.getBigDecimal ("C_REAL", 3);
            assertCondition ((v.doubleValue () == -4.4) && (v.scale () == 3));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Get from a FLOAT.
**/
    public void Var045 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_POS");
            BigDecimal v = rs_.getBigDecimal ("C_FLOAT");
            assertCondition ((v.doubleValue () == 5.55) && (v.scale () == 2), "Value = "+v.doubleValue()+" sb 5.55 scale="+v.scale()+" sb 2");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a FLOAT, scale = 0.
**/
    @SuppressWarnings("deprecation")
   public void Var046 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            BigDecimal v = rs.getBigDecimal ("C_FLOAT", 0);
            double expected = 6; 
            if (getDriver() == JDTestDriver.DRIVER_JCC || getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
              /* jcc driver always rounds down */ 
              expected = 5; 
            }
            assertCondition ((v.doubleValue () == expected) && (v.scale () == 0),
                             "got "+v.doubleValue()+"sb "+expected+" scale="+v.scale()+" sb 0 obtained="+rs.getString("C_FLOAT"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a FLOAT, scale = 3.
**/
    @SuppressWarnings("deprecation")
   public void Var047 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            BigDecimal v = rs.getBigDecimal ("C_FLOAT", 3);
            assertCondition ((v.doubleValue () == 5.55) && (v.scale () == 3));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Get from a DOUBLE.
**/
    public void Var048 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_NEG");
            BigDecimal v = rs_.getBigDecimal ("C_DOUBLE");
            assertCondition ((v.doubleValue () == -6.666) && (v.scale () == 3));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a DOUBLE, scale = 0.
**/
    @SuppressWarnings("deprecation")
   public void Var049 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            BigDecimal v = rs.getBigDecimal ("C_DOUBLE", 0);
            double expected = -7; 
            if (getDriver() == JDTestDriver.DRIVER_JCC  || getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
              /* jcc driver always rounds down */ 
              expected = -6; 
            }
            assertCondition ((v.doubleValue () == expected ) && (v.scale () == 0),
                "got "+v.doubleValue()+"sb "+expected+" scale="+v.scale()+" sb 0"+" getString="+rs.getString("C_DOUBLE"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a DOUBLE, scale = 3.
**/
    @SuppressWarnings("deprecation")
    public void Var050 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            BigDecimal v = rs.getBigDecimal ("C_DOUBLE", 3);
            assertCondition ((v.doubleValue () == -6.666) && (v.scale () == 3));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Get from a DECIMAL(5,0).
**/
    public void Var051 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_POS");
            BigDecimal v = rs_.getBigDecimal ("C_DECIMAL_50");
            assertCondition ((v.doubleValue () == 7) && (v.scale () == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a DECIMAL(5,0), scale = 0.
**/
    @SuppressWarnings("deprecation")
    public void Var052 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            BigDecimal v = rs.getBigDecimal ("C_DECIMAL_50", 0);
            assertCondition ((v.doubleValue () == 7) && (v.scale () == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a DECIMAL(5,0), scale = 3.
**/
    @SuppressWarnings("deprecation")
    public void Var053 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            BigDecimal v = rs.getBigDecimal ("C_DECIMAL_50", 3);
            assertCondition ((v.doubleValue () == 7) && (v.scale () == 3));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Get from a DECIMAL(10,5).
**/
    public void Var054 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_POS");
            BigDecimal v = rs_.getBigDecimal ("C_DECIMAL_105");
            assertCondition ((v.doubleValue () == 8.8888) && (v.scale () == 5));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a DECIMAL(10,5), scale = 0.
**/
    @SuppressWarnings("deprecation")
    public void Var055 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            BigDecimal v = rs.getBigDecimal ("C_DECIMAL_105", 0);
            double expected = 9; 
            if (getDriver() == JDTestDriver.DRIVER_JCC  || getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
              /* jcc driver always rounds down */ 
              expected = 8; 
            }

            assertCondition ((v.doubleValue () == expected) && (v.scale () == 0),
                "got "+v.doubleValue()+"sb "+expected+" scale="+v.scale()+" sb 0"
                +" getString="+rs.getString("C_DECIMAL_105"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a DECIMAL(10,5), scale = 3.
**/
    @SuppressWarnings("deprecation")
    public void Var056 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            BigDecimal v = rs.getBigDecimal ("C_DECIMAL_105", 3);
            double expected = 8.889; 
            if (getDriver() == JDTestDriver.DRIVER_JCC  || getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
              /* jcc driver always rounds down */ 
              expected = 8.888; 
            }
            assertCondition ((v.doubleValue () == expected) && (v.scale () == 3),
                "got "+v.doubleValue()+"sb "+expected+" scale="+v.scale()+" sb 3"
                +" getString="+rs.getString("C_DECIMAL_105"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Get from a NUMERIC(5,0).
**/
    public void Var057 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_NEG");
            BigDecimal v = rs_.getBigDecimal ("C_NUMERIC_50");
            assertCondition ((v.doubleValue () == -9) && (v.scale () == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a NUMERIC(5,0), scale = 0.
**/
    @SuppressWarnings("deprecation")
    public void Var058 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            BigDecimal v = rs.getBigDecimal ("C_NUMERIC_50", 0);
            assertCondition ((v.doubleValue () == -9) && (v.scale () == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a NUMERIC(5,0), scale = 3.
**/
    @SuppressWarnings("deprecation")
    public void Var059 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            BigDecimal v = rs.getBigDecimal ("C_NUMERIC_50", 3);
            assertCondition ((v.doubleValue () == -9) && (v.scale () == 3));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Get from a NUMERIC(10,5).
**/
    public void Var060 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_NEG");
            BigDecimal v = rs_.getBigDecimal ("C_NUMERIC_105");
            assertCondition ((v.doubleValue () == -10.10105) && (v.scale () == 5));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a NUMERIC(10,5), scale = 0.
**/
    @SuppressWarnings("deprecation")
    public void Var061 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            BigDecimal v = rs.getBigDecimal ("C_NUMERIC_105", 0);
            assertCondition ((v.doubleValue () == -10) && (v.scale () == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a NUMERIC(10,5), scale = 3.
**/
    @SuppressWarnings("deprecation")
    public void Var062 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            BigDecimal v = rs.getBigDecimal ("C_NUMERIC_105", 3);
            assertCondition ((v.doubleValue () == -10.101) && (v.scale () == 3));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Get from a CHAR, where the value does not translate
to an int.
**/
    public void Var063 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "CHAR_FULL");
            BigDecimal v = rs_.getBigDecimal ("C_CHAR_50");
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a CHAR, where the value does 
not translate to an int.
**/
    @SuppressWarnings("deprecation")
    public void Var064 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            BigDecimal v = rs.getBigDecimal ("C_CHAR_50", 3);
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBigDecimal() - Get from a CHAR, where the value does translate
to an int.
**/
    public void Var065 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "CHAR_INT");
            BigDecimal v = rs_.getBigDecimal ("C_CHAR_50");
            assertCondition ((v.doubleValue () == -55) && (v.scale () == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a CHAR, where the 
value does translate to an int.
**/
    @SuppressWarnings("deprecation")
    public void Var066 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_INT");
            BigDecimal v = rs.getBigDecimal ("C_CHAR_50", 5);
            assertCondition ((v.doubleValue () == -55) && (v.scale () == 5));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Get from a CHAR, where the value does translate
to an int, except that it has a decimal point.
**/
    public void Var067 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "CHAR_FLOAT");
            BigDecimal v = rs_.getBigDecimal ("C_CHAR_50");
            assertCondition ((v.doubleValue () == 55.901) && (v.scale () == 3));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a CHAR, where
the value does translate to an int, except that it has a decimal 
point.
**/
    @SuppressWarnings("deprecation")
    public void Var068 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FLOAT");
            BigDecimal v = rs.getBigDecimal ("C_CHAR_50", 2);
            assertCondition ((v.doubleValue () == 55.90) && (v.scale () == 2));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Get from a VARCHAR, where the value does not translate
to an int.
**/
    public void Var069 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "CHAR_FULL");
            BigDecimal v = rs_.getBigDecimal ("C_VARCHAR_50");
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a VARCHAR, where the value 
does not translate to an int.
**/
    @SuppressWarnings("deprecation")
    public void Var070 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            BigDecimal v = rs.getBigDecimal ("C_VARCHAR_50", 4);
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBigDecimal() - Get from a VARCHAR, where the value does translate
to an int.
**/
    public void Var071 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "CHAR_INT");
            BigDecimal v = rs_.getBigDecimal ("C_VARCHAR_50");
            assertCondition ((v.doubleValue () == 567) && (v.scale () == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a VARCHAR, where 
the value does translate to an int.
**/
    @SuppressWarnings("deprecation")
    public void Var072 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_INT");
            BigDecimal v = rs.getBigDecimal ("C_VARCHAR_50", 2);
            assertCondition ((v.doubleValue () == 567) && (v.scale () == 2));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Get from a VARCHAR, where the value does translate
to an int, except that it has a decimal point
**/
    public void Var073 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "CHAR_FLOAT");
            BigDecimal v = rs_.getBigDecimal ("C_VARCHAR_50");
            assertCondition ((v.doubleValue () == -567.56) && (v.scale () == 2));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a VARCHAR, where the value 
does translate to an int, except that it has a decimal point
**/
    @SuppressWarnings("deprecation")
    public void Var074 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FLOAT");
            BigDecimal v = rs.getBigDecimal ("C_VARCHAR_50", 1);
            double expected = -567.6; 
            if (getDriver() == JDTestDriver.DRIVER_JCC  || getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
              /* jcc driver always rounds down */ 
              expected = -567.5; 
            }
            assertCondition ((v.doubleValue () == expected ) && (v.scale () == 1),
                "got "+v.doubleValue()+"sb "+expected+" scale="+v.scale()+" sb 1"
                +" getString="+rs.getString("C_VARCHAR_50"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getBigDecimal() - Get from a BINARY.
**/
    public void Var075 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "BINARY_TRANS");
            BigDecimal v = rs_.getBigDecimal ("C_BINARY_20");
            failed ("Didn't throw SQLException -- shouldn't get big decimal from binary v="+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a BINARY.
**/
    @SuppressWarnings("deprecation")
    public void Var076 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            BigDecimal v = rs.getBigDecimal ("C_BINARY_20", 7);
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBigDecimal() - Get from a VARBINARY.
**/
    public void Var077 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "BINARY_TRANS");
            BigDecimal v = rs_.getBigDecimal ("C_VARBINARY_20");
            failed ("Didn't throw SQLException for retrieving from varbinary v="+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a VARBINARY.
**/
    @SuppressWarnings("deprecation")
    public void Var078 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            BigDecimal v = rs.getBigDecimal ("C_VARBINARY_20", 2);
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBigDecimal() - Get from a CLOB.
**/
    public void Var079 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "LOB_FULL");
                BigDecimal v = rs_.getBigDecimal ("C_CLOB");
                failed ("Didn't throw SQLException for retrieve from clob v="+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a CLOB.
**/
    @SuppressWarnings("deprecation")
    public void Var080 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                BigDecimal v = rs.getBigDecimal ("C_CLOB", 9);
                failed ("Didn't throw SQLException "+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBigDecimal() - Get from a DBCLOB.
**/
    public void Var081 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "LOB_FULL");
                BigDecimal v = rs_.getBigDecimal ("C_DBCLOB");
                failed ("Didn't throw SQLException for DBCLOB v="+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a DBCLOB.
**/
    @SuppressWarnings("deprecation")
   public void Var082 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                BigDecimal v = rs.getBigDecimal ("C_DBCLOB", 0);
                failed ("Didn't throw SQLException "+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBigDecimal() - Get from a BLOB.
**/
    public void Var083 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "LOB_FULL");
                BigDecimal v = rs_.getBigDecimal ("C_BLOB");
                failed ("Didn't throw SQLException for BLOB v="+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a BLOB.
**/
    @SuppressWarnings("deprecation")
   public void Var084 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                BigDecimal v = rs.getBigDecimal ("C_BLOB", 5);
                failed ("Didn't throw SQLException "+v );
             }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBigDecimal() - Get from a DATE.
**/
    public void Var085 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "DATE_1998");
            BigDecimal v = rs_.getBigDecimal ("C_DATE");
            failed ("Didn't throw SQLException for date v="+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a DATE.
**/
    @SuppressWarnings("deprecation")
    public void Var086 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            BigDecimal v = rs.getBigDecimal ("C_DATE", 3);
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBigDecimal() - Get from a TIME.
**/
    public void Var087 ()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "DATE_1998");
            BigDecimal v = rs_.getBigDecimal ("C_TIME");
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a TIME.
**/
    @SuppressWarnings("deprecation")
    public void Var088 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            BigDecimal v = rs.getBigDecimal ("C_TIME", 7);
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBigDecimal() - Get from a TIMESTAMP.
**/
    public void Var089 ()
    {    
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "DATE_1998");
            BigDecimal v = rs_.getBigDecimal ("C_TIMESTAMP");
            failed ("Didn't throw SQLException "+v );
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a TIMESTAMP.
**/
    @SuppressWarnings("deprecation")
    public void Var090 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            BigDecimal v = rs.getBigDecimal ("C_TIMESTAMP", 4);
            failed ("Didn't throw SQLException "+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getBigDecimal() - Get from a DATALINK.
**/
    public void Var091 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GETDL);
                JDRSTest.position (rs, "LOB_FULL");
                BigDecimal v = rs.getBigDecimal ("C_DATALINK");
                failed ("Didn't throw SQLException "+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a DATALINK.
**/
    @SuppressWarnings("deprecation")
    public void Var092 ()
    {
        if (checkLobSupport ()) {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GETDL);
                JDRSTest.position0 (rs, "LOB_FULL");
                BigDecimal v = rs.getBigDecimal ("C_DATALINK", 15);
                failed ("Didn't throw SQLException "+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBigDecimal() - Get from a DISTINCT.
**/
    public void Var093 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "LOB_FULL");
                BigDecimal v = rs_.getBigDecimal ("C_DISTINCT");
                assertCondition (v.doubleValue () == 123456789);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a DISTINCT.
**/
    @SuppressWarnings("deprecation")
    public void Var094 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                BigDecimal v = rs.getBigDecimal ("C_DISTINCT", 3);
                assertCondition (v.doubleValue () == 123456789);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getBigDecimal() - Get from a BIGINT.
**/
    public void Var095 ()
    {
        if (checkJdbc20 ()) {
        if (checkBigintSupport ()) {
        try {
            rs_ = JDRSTest.position (driver_,statement_,rsQuery_, rs_, "NUMBER_POS");
            BigDecimal v = rs_.getBigDecimal ("C_BIGINT");
            assertCondition ((v.doubleValue () == 12374321d) && (v.scale () == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a BIGINT, scale = 0.
**/
    @SuppressWarnings("deprecation")
    public void Var096 ()
    {
        if (checkBigintSupport ()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            BigDecimal v = rs.getBigDecimal ("C_BIGINT", 0);
            assertCondition ((v.doubleValue () == 12374321d) && (v.scale () == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getBigDecimal() with 2 parameters - Get from a BIGINT, scale = 3.
**/
    @SuppressWarnings("deprecation")
    public void Var097 ()
    {
        if (checkBigintSupport ()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            BigDecimal v = rs.getBigDecimal ("C_BIGINT", 3);
            assertCondition ((v.doubleValue () == -44332123) && (v.scale () == 3));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


/**
getBigDecimal() - Get from a DOUBLE NaN
**/
    public void Var098 ()
    {

	if (checkJdbc20 ()) {
	    BigDecimal bd = null; 
	    String     s  = null; 
	    double     d  = 0.0; 
	    try {
		String tableName = JDRSTest.COLLECTION + ".JDRSGBD098";
		try {
		    statement0_.executeUpdate("DELETE FROM "+tableName); 
		} catch (Exception e) {
		    statement0_.executeUpdate("CREATE TABLE "+tableName+" (C1 DOUBLE)");
		}
		
		PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+tableName+" VALUES(?)");
		pstmt.setDouble(1,Double.NaN);
		pstmt.executeUpdate();
		ResultSet rs = statement0_.executeQuery("SELECT * FROM "+tableName);
		rs.next();

		s  = rs.getString(1); 
		d  = rs.getDouble( 1); 
		bd = rs.getBigDecimal (1); 
		assertCondition ((bd.doubleValue () == Double.NaN) , "v="+bd +"  Added by native driver -- 05/08/2006" );
	    }
	    catch (Exception e) {
                if (getDriver() == JDTestDriver.DRIVER_JCC) {
                  String exString = e.toString();
                  String expected = "SQLCODE=-302"; /* SQL0302 = value too large for use */ 
                  assertCondition(exString.indexOf(expected)>=0, "Got exception '"
                      + exString + "' expected '" + expected + "'");
                } else {
		assertExceptionIsInstanceOf(e, "java.lang.NumberFormatException", "Unexpected Exception s="+s+" bd="+bd+" d="+d );
                }
                }
	}
    }
    
    /**
    getBigDecimal() - Get from a DOUBLE POSITIVE_INFINITY
    **/
        public void Var099 ()
	{
	    if (checkJdbc20 ()) {
		BigDecimal bd = null; 
		String     s  = null; 
		double     d  = 0.0; 
		try {
		    String tableName = JDRSTest.COLLECTION + ".JDRSGBD099";
		    try {
	        statement0_.executeUpdate("DELETE FROM "+tableName); 
		    } catch (Exception e) {
	        statement0_.executeUpdate("CREATE TABLE "+tableName+" (C1 DOUBLE)");
		    }
		    PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+tableName+" VALUES(?)");
		    pstmt.setDouble(1,Double.POSITIVE_INFINITY);
		    pstmt.executeUpdate();
		    ResultSet rs = statement0_.executeQuery("SELECT * FROM "+tableName);
		    rs.next();

		    s  = rs.getString(1); 
		    d  = rs.getDouble( 1); 
		    bd = rs.getBigDecimal (1); 
		    assertCondition ((bd.doubleValue () == Double.POSITIVE_INFINITY) , "v="+bd +"  Added by native driver -- 05/08/2006" );
		}
		catch (Exception e) {
		  if (getDriver() == JDTestDriver.DRIVER_JCC) {
                    String exString = e.toString(); 
                    String expected = "Invalid data conversion"; 
                    assertCondition(exString.indexOf(expected)>=0, "Got exception '"+exString+"' expected '"+expected+"'");  
                  } else {
		    assertExceptionIsInstanceOf(e, "java.lang.NumberFormatException", "Unexpected Exception s="+s+" bd="+bd+" d="+d );
                  }
		}
	    }

	}

        /**
        getBigDecimal() - Get from a DOUBLE NEGATIVE_INFINITY
        **/
            public void Var100 ()
	    {
		if (checkJdbc20 ()) {
		    BigDecimal bd = null; 
		    String     s  = null; 
		    double     d  = 0.0; 
		    try {
			String tableName = JDRSTest.COLLECTION + ".JDRSGBD100";
			try {
        statement0_.executeUpdate("DELETE FROM "+tableName); 
			} catch (Exception e) {
	      statement0_.executeUpdate("CREATE TABLE "+tableName+" (C1 DOUBLE)");
			}
			PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "+tableName+" VALUES(?)");
			pstmt.setDouble(1,Double.NEGATIVE_INFINITY);
			pstmt.executeUpdate();
			ResultSet rs = statement0_.executeQuery("SELECT * FROM "+tableName);
			rs.next();

			s  = rs.getString(1); 
			d  = rs.getDouble( 1); 
			bd = rs.getBigDecimal (1); 
			assertCondition ((bd.doubleValue () == Double.NEGATIVE_INFINITY) , "v="+bd +"  Added by native driver -- 05/08/2006" );
		    }
		    catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String exString = e.toString();
          String expected = "Invalid data conversion";
          assertCondition(exString.indexOf(expected)>=0, "Got exception '"
              + exString + "' expected '" + expected + "'");
        } else {
          assertExceptionIsInstanceOf(e, "java.lang.NumberFormatException",
              "Unexpected Exception s=" + s + " bd=" + bd + " d=" + d);
        }
      }
    }
	    }
            
        /**
         * getBigDecimal() - Get from DFP16 -- Use preset values
         */
	    public void Var101 ()
	    {
		if (checkDecFloatSupport ()) {
		    boolean success = true; 
		    StringBuffer sb = new StringBuffer();

		    try {
			ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
								 + JDRSTest.RSTEST_DFP16);
			int i = 0; 
			while (rs.next()) { 
			    BigDecimal v = rs.getBigDecimal (1);
			    String expected = JDRSTest.VALUES_DFP16[i];
			    if (expected == null) {
				if (v != null) {
				    success = false;
				    sb.append("expected "+expected+" but got "+v.toString()); 
				} 
			    } else {
				if (v == null) {
				    sb.append("expected "+expected+" but got "+v);
				    success = false;
				} else {
				    if (expected.equals(v.toString())) {
				    } else {
					if (expected.equals("1.0") && v.toString().equals("1")) {
					    /* ok */
					} else { 
					    sb.append("expected "+expected+" but got "+v.toString());
					    success = false;
					}
				    } 
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



        /**
        getBigDecimal() - Get from DFP34 -- Use preset values
        **/
	    public void Var102 ()
	    {
		if (checkDecFloatSupport ()) {
		    boolean success = true; 
		    StringBuffer sb = new StringBuffer();

		    try {
			ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
								 + JDRSTest.RSTEST_DFP34);
			int i = 0; 
			while (rs.next()) { 
			    BigDecimal v = rs.getBigDecimal (1);
			    String expected = JDRSTest.VALUES_DFP34[i];
			    if (expected == null) {
				if (v != null) {
				    success = false;
				    sb.append("expected "+expected+" but got "+v.toString()); 
				} 
			    } else {
				if (v == null) {
				    sb.append("expected "+expected+" but got "+v);
				    success = false;
				} else {
				    if (expected.equals(v.toString())) {
				    } else {
					if (expected.equals("1.0") && v.toString().equals("1")) {
					    /* ok */
					} else { 

					    sb.append("expected "+expected+" but got "+v.toString());
					    success = false;
					}
				    } 
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



        /**
        getBigDecimal() - Get from DFP16: NAN -- will throw an exception 
        **/
	    public void Var103 ()
	    {
		if (checkDecFloatSupport()) {
		    try {
			Statement s = connection_.createStatement ();
			ResultSet rs = s.executeQuery ("SELECT * FROM "
						       + JDRSTest.RSTEST_DFP16NAN);
			rs.next(); 
			BigDecimal v = rs.getBigDecimal (1);
			failed ("Didn't throw SQLException "+v);
		    }
		    catch (Exception e) {
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		    }
		}
	    }


        /**
        getBigDecimal() - Get from DFP16: -NAN -- will throw an exception 
        **/
	    public void Var104 ()
	    {
		if (checkDecFloatSupport()) {
		    try {
			Statement s = connection_.createStatement ();
			ResultSet rs = s.executeQuery ("SELECT * FROM "
						       + JDRSTest.RSTEST_DFP16NNAN);
			rs.next(); 
			BigDecimal v = rs.getBigDecimal (1);
			failed ("Didn't throw SQLException "+v);
		    }
		    catch (Exception e) {
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		    }
		}
	    }


        /**
        getBigDecimal() - Get from DFP16: INF -- will throw an exception 
        **/
	    public void Var105 ()
	    {
		if (checkDecFloatSupport()) {
		    try {
			Statement s = connection_.createStatement ();
			ResultSet rs = s.executeQuery ("SELECT * FROM "
						       + JDRSTest.RSTEST_DFP16INF);
			rs.next(); 
			BigDecimal v = rs.getBigDecimal (1);
			failed ("Didn't throw SQLException "+v);
		    }
		    catch (Exception e) {
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		    }
		}
	    }


        /**
        getBigDecimal() - Get from DFP16: -INF -- will throw an exception 
        **/
	    public void Var106 ()
	    {
		if (checkDecFloatSupport()) {
		    try {
			Statement s = connection_.createStatement ();
			ResultSet rs = s.executeQuery ("SELECT * FROM "
						       + JDRSTest.RSTEST_DFP16NINF);
			rs.next(); 
			BigDecimal v = rs.getBigDecimal (1);
			failed ("Didn't throw SQLException "+v);
		    }
		    catch (Exception e) {
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		    }
		}
	    }


        /**
        getBigDecimal() - Get from DFP34: NAN -- will throw an exception 
        **/
	    public void Var107 ()
	    {
		if (checkDecFloatSupport()) {
		    try {
			Statement s = connection_.createStatement ();
			ResultSet rs = s.executeQuery ("SELECT * FROM "
						       + JDRSTest.RSTEST_DFP34NAN);
			rs.next(); 
			BigDecimal v = rs.getBigDecimal (1);
			failed ("Didn't throw SQLException "+v);
		    }
		    catch (Exception e) {
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		    }
		}
	    }


        /**
        getBigDecimal() - Get from DFP34: -NAN -- will throw an exception 
        **/
	    public void Var108 ()
	    {
		if (checkDecFloatSupport()) {
		    try {
			Statement s = connection_.createStatement ();
			ResultSet rs = s.executeQuery ("SELECT * FROM "
						       + JDRSTest.RSTEST_DFP34NNAN);
			rs.next(); 
			BigDecimal v = rs.getBigDecimal (1);
			failed ("Didn't throw SQLException "+v);
		    }
		    catch (Exception e) {
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		    }
		}
	    }


        /**
        getBigDecimal() - Get from DFP34: INF -- will throw an exception 
        **/
	    public void Var109 ()
	    {
		if (checkDecFloatSupport()) {
		    try {
			Statement s = connection_.createStatement ();
			ResultSet rs = s.executeQuery ("SELECT * FROM "
						       + JDRSTest.RSTEST_DFP34INF);
			rs.next(); 
			BigDecimal v = rs.getBigDecimal (1);
			failed ("Didn't throw SQLException "+v);
		    }
		    catch (Exception e) {
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		    }
		}
	    }


        /**
        getBigDecimal() - Get from DFP34: -INF -- will throw an exception 
        **/
	    public void Var110 ()
	    {
		if (checkDecFloatSupport()) {
		    try {
			Statement s = connection_.createStatement ();
			ResultSet rs = s.executeQuery ("SELECT * FROM "
						       + JDRSTest.RSTEST_DFP34NINF);
			rs.next(); 
			BigDecimal v = rs.getBigDecimal (1);
			failed ("Didn't throw SQLException "+v);
		    }
		    catch (Exception e) {
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		    }
		}
	    }


   /**
   getBigDecimal() - Get from an SQLXML. 
   **/
  public void Var111() {
    if (checkXmlSupport()) {
      try {
        ResultSet rs = statement0_.executeQuery("SELECT * FROM "
            + JDRSTest.RSTEST_GETXML);
        rs.next();
        BigDecimal v = rs.getBigDecimal("C_XML");
        failed("Didn't throw SQLException " + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }

    }
  }


                  /**
         * getBigDecimal() - Get from a TIMESTAMP(12).
         **/
        public void Var112() {
            if (checkTimestamp12Support()) {
              testGet(statement0_,
                  "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
                  "getBigDecimal", 
                  "EXCEPTION:Data type mismatch", 
                  " -- added 11/19/2012"); 

            }
        }


        /**
         * getBigDecimal() - Get from a TIMESTAMP(0).
         **/
        public void Var113() {
            if (checkTimestamp12Support()) {
          testGet(statement0_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
            "getBigDecimal",
		  "EXCEPTION:Data type mismatch.", 
            " -- added 11/19/2012"); 
            
            
            }
        }


/**
getBigDecimal() - Get from a BOOLEAN.
**/
    public void Var114()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_FALSE");
            BigDecimal v = rs.getBigDecimal ("C_BOOLEAN");
            assertCondition (v.doubleValue() == 0.0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


/**
getBigDecimal() - Get from a BOOLEAN.
**/
    public void Var115()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_TRUE");
            BigDecimal v = rs.getBigDecimal ("C_BOOLEAN");
            assertCondition (v.doubleValue() == 1.0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

/**
getBigDecimal() - Get from a BOOLEAN.
**/
    public void Var116()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_NULL");
            BigDecimal v = rs.getBigDecimal ("C_BOOLEAN");
            assertCondition (v == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




}



