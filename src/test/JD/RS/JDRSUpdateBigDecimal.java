///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateBigDecimal.java
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
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JVMInfo;



/**
Testcase JDRSUpdateBigDecimal.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateBigDecimal()
</ul>
**/
public class JDRSUpdateBigDecimal
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSUpdateBigDecimal";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static final String key_            = "JDRSUpdateBigDecimal";
    private static String select_         = "SELECT * FROM "
                                                    + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private Statement           statement2f_; 
    private ResultSet           rs_ = null;


    private Connection          connectionCommaSeparator_;
    private Statement           statementCommaSeparator_;
    private Statement           statementCommaSeparator2_;
    private Statement           statementCommaSeparator2f_; 
    private ResultSet           rsCommaSeparator_ = null ;

    private boolean tableUpdated = false; 
    int jdk_ = 0; 


/**
Constructor.
**/
    public JDRSUpdateBigDecimal (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSUpdateBigDecimal",
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
	jdk_ = JVMInfo.getJDK();

        if (isJdbc20 ()) {
            // SQL400 - driver neutral...
            String url = baseURL_
            // String url = "jdbc:as400://" + systemObject_.getSystemName()
                
                
                + ";data truncation=true";
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
            connection_.setAutoCommit(false); // @C1A




            connectionCommaSeparator_ = testDriver_.getConnection (url+";decimal separator=,");
            connectionCommaSeparator_.setAutoCommit(false); // @C1A


        }
    }


    protected void setupRs() {
	if (rsCommaSeparator_ != null) {
          try { 
	    rsCommaSeparator_.close ();
	    statementCommaSeparator_.close ();
	    statementCommaSeparator2_.close ();
	    statementCommaSeparator2f_.close ();
	    connectionCommaSeparator_.commit();
	    rsCommaSeparator_ = null;
	    statementCommaSeparator_= null;
	    statementCommaSeparator2_= null;
	    statementCommaSeparator2f_= null;
	  } catch (SQLException e) { 
	    System.out.flush(); 
	    e.printStackTrace();
	    System.err.flush(); 
	  }
	} 
	if (rs_ == null) {
          try { 

	    statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
						      ResultSet.CONCUR_UPDATABLE);
	    statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_READ_ONLY);
	    statement2f_ = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
							ResultSet.CONCUR_READ_ONLY);

	    if (! tableUpdated) { 
		statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
					  + " (C_KEY) VALUES ('DUMMY_ROW')");
		statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
					  + " (C_KEY) VALUES ('DUMMY_ROW1')");
		statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
					  + " (C_KEY) VALUES ('" + key_ + "')");
		tableUpdated = true; 
	    }
	    rs_ = statement_.executeQuery (select_ + " FOR UPDATE");
          } catch (SQLException e) { 
	    System.out.flush(); 
            e.printStackTrace();
	    System.err.flush();
          }

	} 
    } 


    protected void setupRsCommaSeparator() {
	if (rs_ != null) {
	  try { 
	    rs_.close ();
	    statement_.close ();
	    statement2_.close ();
	    statement2f_.close ();
	    connection_.commit();
	    rs_ = null;
	    statement_= null;
	    statement2_= null;
	    statement2f_= null;
	     } catch (SQLException e) { 
	    System.out.flush(); 
	            e.printStackTrace();
	    System.err.flush(); 

	          }

	} 
	if (rsCommaSeparator_ == null) {
	  try { 
	    statementCommaSeparator_ = connectionCommaSeparator_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
						      ResultSet.CONCUR_UPDATABLE);
	    statementCommaSeparator2_ = connectionCommaSeparator_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_READ_ONLY);
	    statementCommaSeparator2f_ = connectionCommaSeparator_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
							ResultSet.CONCUR_READ_ONLY);

	    if (!tableUpdated) { 
		statementCommaSeparator_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
							+ " (C_KEY) VALUES ('DUMMY_ROW')");
		statementCommaSeparator_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
							+ " (C_KEY) VALUES ('DUMMY_ROW1')");
		statementCommaSeparator_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
							+ " (C_KEY) VALUES ('" + key_ + "')");
		tableUpdated = true; 
	    }
	    rsCommaSeparator_ = statementCommaSeparator_.executeQuery (select_ + " FOR UPDATE");
          } catch (SQLException e) {
	    System.out.flush(); 
	            e.printStackTrace();
	    System.err.flush(); 

          }

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
	    if (rs_ != null) { 
		rs_.close ();
		statement_.close ();
		statement2_.close ();
		statement2f_.close ();
	    }
            connection_.commit(); // @C1A
            connection_.close ();
            if (rsCommaSeparator_ != null) { 
              rsCommaSeparator_.close ();
              statementCommaSeparator_.close ();
              statementCommaSeparator2_.close ();
              statementCommaSeparator2f_.close ();
          }
          connectionCommaSeparator_.commit(); // @C1A
          connectionCommaSeparator_.close ();
        }
    }


    protected void cleanupConnection ()
        throws Exception
    {
            connection_.commit(); // @C1A
            connection_.close ();
          connectionCommaSeparator_.commit(); // @C1A
          connectionCommaSeparator_.close ();
    }



/**
Compares a BigDecimal with a double, and allows a little rounding error.
**/
    private boolean compare (BigDecimal bd, double d)
    {
        return (Math.abs (bd.doubleValue () - d) < 0.001);
    }



/**
updateBigDecimal() - Should throw exception when the result set is
closed.
**/
    public void Var001()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
            rs.next ();
            rs.close ();
            rs.updateBigDecimal ("C_DECIMAL_105", new BigDecimal ("1.23"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBigDecimal() - Should throw exception when the result set is
not updatable.
**/
    public void Var002()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_UPDATE);
            rs.next ();
            rs.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("4.56"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBigDecimal() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var003()
    {
      System.out.flush(); 
      System.err.flush(); 
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, null);
            rs_.updateBigDecimal ("C_DECIMAL_105", new BigDecimal ("7.89"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          try {
            Thread.sleep(1000); 
          } catch (Exception e2) { 
            
          }
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBigDecimal() - Should throw an exception when the column
is an invalid index.
**/
    public void Var004()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal (100, new BigDecimal ("-1.54"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBigDecimal() - Should throw an exception when the column
is 0.
**/
    public void Var005()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal (0, new BigDecimal ("5.67"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBigDecimal() - Should throw an exception when the column
is -1.
**/
    public void Var006()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal (-1, new BigDecimal ("9.87"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBigDecimal() - Should work when the column index is valid.
**/
    public void Var007()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal (8, new BigDecimal ("1.22"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
            rs2.close ();
            assertCondition (compare (v, 1.22));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBigDecimal() - Should throw an exception when the column
name is null.
**/
    public void Var008()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal (null, new BigDecimal ("2.425"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBigDecimal() - Should throw an exception when the column
name is an empty string.
**/
    public void Var009()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("", new BigDecimal ("3.2"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBigDecimal() - Should throw an exception when the column
name is invalid.
**/
    public void Var010()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("INVALID", new BigDecimal ("345.33"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBigDecimal() - Should work when the column name is valid.
**/
    public void Var011()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("-5.3322"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
            rs2.close ();
            assertCondition (v.doubleValue () == -5.3322);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBigDecimal() - Should update to SQL NULL when the column
value is null.
**/
    public void Var012()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_NUMERIC_105", null);
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
            boolean wn = rs2.wasNull ();
            rs2.close ();
            assertCondition ((v == null) && (wn == true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBigDecimal() - Should be reflected by get, even if update has
not yet been issued (i.e. update is still pending).
**/
    public void Var013()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("7.6511"));
            assertCondition (compare (rs_.getBigDecimal ("C_NUMERIC_105"), 7.6511));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateBigDecimal() - Should be reflected by get, after update has
been issued, but cursor has not been repositioned.
**/
    public void Var014()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_DECIMAL_105", new BigDecimal ("-12.342"));
            rs_.updateRow ();
            assertCondition (rs_.getBigDecimal ("C_DECIMAL_105").doubleValue () == -12.342);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateBigDecimal() - Should be reflected by get, after update has
been issued and cursor has been repositioned.
**/
    public void Var015()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("4982.455"));
            rs_.updateRow ();
            rs_.beforeFirst ();
            JDRSTest.position (rs_, key_);
            BigDecimal v = rs_.getBigDecimal ("C_NUMERIC_105");
            assertCondition (compare (v, 4982.455));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




/**
updateBigDecimal() - Should work when the current row is the insert
row.
**/
    public void Var016()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateString ("C_KEY", "JDRSUpdateBigDecimal 1");
            rs_.updateBigDecimal ("C_DECIMAL_105", new BigDecimal ("6.43431"));
            rs_.insertRow ();
            JDRSTest.position (rs_, "JDRSUpdateBigDecimal 1");
            assertCondition (compare (rs_.getBigDecimal ("C_DECIMAL_105"), 6.43431));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBigDecimal() - Should be reflected by get on insert row, even if
insert has not yet been issued (i.e. insert is still pending).
**/
    public void Var017()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            rs_.moveToInsertRow ();
            rs_.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("9898.9898"));
            assertCondition (compare (rs_.getBigDecimal ("C_NUMERIC_105"), 9898.9898));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }

           

/**
updateBigDecimal() - Should throw an exception on a deleted row.
**/
    public void Var018()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "DUMMY_ROW");
            rs_.deleteRow ();
            rs_.updateBigDecimal ("C_DECIMAL_105", new BigDecimal ("-544.3"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


/**
updateBigDecimal() - Update a SMALLINT.
**/
    public void Var019 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_SMALLINT", new BigDecimal ("542"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            short v = rs2.getShort ("C_SMALLINT");
            rs2.close ();
            assertCondition (v == 542);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBigDecimal() - Update a SMALLINT, when the integer is too big.
**/
    public void Var020 ()
    {                   
	setupRs(); 
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_SMALLINT");
            rs_.updateBigDecimal ("C_SMALLINT", new BigDecimal ("487623"));
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
updateBigDecimal() - Update a SMALLINT, when there is a decimal part.  This is OK.
**/
    public void Var021 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_SMALLINT", new BigDecimal ("33.2"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            short v = rs2.getShort ("C_SMALLINT");
            rs2.close ();
            assertCondition (v == 33);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBigDecimal() - Update an INTEGER.
**/
    public void Var022 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_INTEGER", new BigDecimal ("-128374"));
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
updateBigDecimal() - Update an INTEGER, when the integer is too big.
**/
    public void Var023 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_INTEGER");
            rs_.updateBigDecimal ("C_INTEGER", new BigDecimal ("-9945358743353"));
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
                && (dt.getTransferSize() == 4));
          } else {

            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }

        }
        }
    }



/**
updateBigDecimal() - Update an INTEGER, when there is a decimal part.
**/
    public void Var024 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_INTEGER", new BigDecimal ("546.2323"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            int v = rs2.getInt ("C_INTEGER");
            rs2.close ();
            assertCondition (v == 546);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBigDecimal() - Update a REAL.
**/
    public void Var025 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_REAL", new BigDecimal ("-2.5"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_REAL");
            rs2.close ();
            assertCondition (v == -2.5);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }


/**
updateBigDecimal() - Update a REAL, when the number is too big.  There is loss of 
data here but we do not lose any portion of the whole number so we do not consider
this significant truncation (we are allowing this to work just like setting a 
NUMERIC or DECIMAL field would).
**/
    public void Var026 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_REAL", new BigDecimal ("4534335.443487623"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_REAL");
            rs2.close ();
            // System.out.println("Value is " + v);
            assertCondition (v == 4534335.5);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }
            /*
            rs_.updateRow ();
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
    }
    */



/**
updateBigDecimal() - Update a FLOAT.
**/
    public void Var027 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_FLOAT", new BigDecimal ("9845.32"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_FLOAT");
            rs2.close ();
            assertCondition (v == 9845.32f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBigDecimal() - Update a FLOAT, when the number is too big.

The native driver no longer throws an exception.  This was needed to
pass the J2EE CTS.

**/
    public void Var028 ()
    {
	setupRs(); 
	if (checkJdbc20 ()) {
	    try {
		JDRSTest.position (rs_, key_);
		rs_.updateBigDecimal ("C_FLOAT", new BigDecimal ("4555444534345454545.4434556337633"));
		rs_.updateRow ();
		
		assertCondition(true); //@tb same as native 
		 
	    }
	    catch (Exception e) {
		if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
		    failed(e); 
		} else { 

		    assertCondition(e instanceof DataTruncation);
	    /*
	    DataTruncation dt = (DataTruncation)e;
	    assertCondition ((dt.getIndex() == expectedColumn)
		&& (dt.getParameter() == false)
		&& (dt.getRead() == false)
		&& (dt.getDataSize() == 32)
		&& (dt.getTransferSize() == 19));
		*/
		}

	    }        
	}
    }



/**
updateBigDecimal() - Update a DOUBLE.
**/
    public void Var029 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_DOUBLE", new BigDecimal ("-19845.333"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            double v = rs2.getDouble ("C_DOUBLE");
            rs2.close ();
            assertCondition (v == -19845.333);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBigDecimal() - Update a DOUBLE, when the number is too big.

native driver updated on V5R2 SI06110 to not throw truncation errors. 
**/
    public void Var030 ()
    {
	setupRs(); 
	if (checkJdbc20 ()) {
	    try {
		JDRSTest.position (rs_, key_);
		rs_.updateBigDecimal ("C_DOUBLE", new BigDecimal ("453343443443454433.4443433445532333"));
		rs_.updateRow ();
		 
		    assertCondition(true);  //@tb same as native 
	 
	    }
	    catch (Exception e) {
		if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
		    failed(e);
		} else { 

		    assertCondition(e instanceof DataTruncation);
	    /*
	    DataTruncation dt = (DataTruncation)e;
	    assertCondition ((dt.getIndex() == expectedColumn)
		&& (dt.getParameter() == false)
		&& (dt.getRead() == false)
		&& (dt.getDataSize() == 35)
		&& (dt.getTransferSize() == 19));
		*/
		}
	    }        
	}

    }



/**
updateBigDecimal() - Update a DECIMAL(4,0).
**/
    public void Var031 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_DECIMAL_40", new BigDecimal ("-4232"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_40");
            rs2.close ();
            assertCondition (v.floatValue() == -4232);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBigDecimal() - Update a DECIMAL(10,5).
**/
    public void Var032 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_DECIMAL_105", new BigDecimal ("-4233.43"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
            rs2.close ();
            assertCondition (v.floatValue() == -4233.43f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBigDecimal() - Update a DECIMAL, when the value is too big.
**/
    public void Var033 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_DECIMAL_105");
            rs_.updateBigDecimal ("C_DECIMAL_105", new BigDecimal ("54351221.32244"));
            rs_.updateRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
	    if (e instanceof DataTruncation) { 
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 13)
                && (dt.getTransferSize() == 10));
	    } else {
		assertSqlException(e, -99999, "07006", "Data type mismatch",
				   "Mismatch instead of truncation in latest toolbox");
	    }

        }        
        }

    }


/**
updateBigDecimal() - Update a DECIMAL, when the decimal part is too big.
**/
    public void Var034 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_DECIMAL_40");
            rs_.updateBigDecimal ("C_DECIMAL_40", new BigDecimal ("543511.32"));
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
                && (dt.getTransferSize() == 4));
	    } else {
		assertSqlException(e, -99999, "07006", "Data type mismatch",
				   "Mismatch instead of truncation in latest toolbox");
	    }

        }       
        }
    }


/**
updateBigDecimal() - Update a NUMERIC(10,5).
**/
    public void Var035 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("-9633.475"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
            rs2.close ();
            assertCondition (v.doubleValue() == -9633.475);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBigDecimal() - Update a NUMERIC(4,0).
**/
    public void Var036 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_NUMERIC_40", new BigDecimal ("-9638"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_40");
            rs2.close ();
            assertCondition (v.doubleValue() == -9638);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



// @C2C
/**
updateBigDecimal() - Update a NUMERIC, when the value is too big.
**/
    public void Var037 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_NUMERIC_105");
            rs_.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("-331515.7657647"));
            rs_.updateRow ();
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
	    if (e instanceof DataTruncation) { 
            DataTruncation dt = (DataTruncation)e;
            assertCondition ((dt.getIndex() == expectedColumn)
                && (dt.getParameter() == false)
                && (dt.getRead() == false)
                && (dt.getDataSize() == 13)
                && (dt.getTransferSize() == 10));
	    } else {
		assertSqlException(e, -99999, "07006", "Data type mismatch",
				   "Mismatch instead of truncation in latest toolbox");
	    }

        }
        }
    }



/**
updateBigDecimal() - Update a NUMERIC, when the decimal part is too big.
**/
    public void Var038 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_NUMERIC_40", new BigDecimal ("-3315.76"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_40");
            rs2.close ();
            assertCondition (v.doubleValue() == -3315);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBigDecimal() - Update a CHAR.
**/
    public void Var039 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_CHAR_50", new BigDecimal ("987.1234"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_CHAR_50");
            rs2.close ();
            assertCondition (v.equals ("987.1234                                          "));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBigDecimal() - Update a CHAR, when the value is too big.
**/
    public void Var040 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rs_, key_);
            expectedColumn = rs_.findColumn ("C_CHAR_1");
            rs_.updateBigDecimal ("C_CHAR_1", new BigDecimal ("35"));
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
updateBigDecimal() - Update a VARCHAR.
**/
    public void Var041 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_VARCHAR_50", new BigDecimal ("-533423.77893"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_VARCHAR_50");
            rs2.close ();
            assertCondition (v.equals ("-533423.77893"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
updateBigDecimal() - Update a BINARY.
**/
    public void Var042 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_BINARY_20", new BigDecimal ("33"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBigDecimal() - Update a VARBINARY.
**/
    public void Var043 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_VARBINARY_20", new BigDecimal ("45"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBigDecimal() - Update a CLOB.
**/
    public void Var044 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBigDecimal ("C_CLOB", new BigDecimal ("-233"));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateBigDecimal() - Update a DBCLOB.
**/
    public void Var045 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBigDecimal ("C_DBCLOB", new BigDecimal ("43.54"));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateBigDecimal() - Update a BLOB.
**/
    public void Var046 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBigDecimal ("C_BLOB", new BigDecimal ("332.43"));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
updateBigDecimal() - Update a DATE.
**/
    public void Var047 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_DATE", new BigDecimal ("43.2"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBigDecimal() - Update a TIME.
**/
    public void Var048 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_TIME", new BigDecimal ("43.22"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBigDecimal() - Update a TIMESTAMP.
**/
    public void Var049 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_TIMESTAMP", new BigDecimal ("-44.322"));
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
updateBigDecimal() - Update a DATALINK.
**/
    public void Var050 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            // We do not test updating datalinks, since it is not 
            // possible to open a updatable cursor/result set with
            // a datalink column.
            notApplicable("DATALINK update not supported.");
            /*
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBigDecimal ("C_DATALINK", new BigDecimal ("43"));
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
updateBigDecimal() - Update a DISTINCT.
**/
    public void Var051 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBigDecimal ("C_DISTINCT", new BigDecimal ("-32.3"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_DISTINCT");
                rs2.close ();
                assertCondition (v.equals ("-32.3    "));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }



/**
updateBigDecimal() - Update a BIGINT.
**/
    public void Var052 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_BIGINT", new BigDecimal ("-12837434553"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            long v = rs2.getLong ("C_BIGINT");
            rs2.close ();
            assertCondition (v == -12837434553l);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
        }
    }



/**
updateBigDecimal() - Update a BIGINT, when the integer is too big.
This causes a DataTruncation exception.
**/
    public void Var053 ()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBigDecimal ("C_BIGINT", new BigDecimal ("-994535874335432445653"));
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
		boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
		if (success) { 
		    assertCondition(true); 
		} else { 
		    failed(e, "Expected data type mismatch -- updated 7/1/2014 to match other data types"); 
		}

            }       
        }
        }
    }



/**
updateBigDecimal() - Update a BIGINT, when there is a decimal part.
This is ok.
**/
    public void Var054()
    {
	setupRs(); 
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateBigDecimal ("C_BIGINT", new BigDecimal ("543246.2323"));
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                long v = rs2.getLong ("C_BIGINT");
                rs2.close ();
                assertCondition (v == 543246);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
            }
            }
        }


    /**
     * set a DECIMAL field using a BigDecimal which in JDK 1.5 will be formatted
     * with an Exponent.  The toString method was changed in JDK 1.5 to 
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc. 
     * 
     * Otherwise (that is, if the scale is negative, or the adjusted exponent 
     * is less than -6), the number will be converted to a character form using 
     * 
     * The native PTR for this problem is 9B20698
     */
    public void Var055()
    {
        String added=" -- added by native driver 2/8/2006";
	setupRs(); 
        if (checkJdbc20 ()) {
            if (checkBigintSupport()) {
                try {
                    String tableName = JDRSTest.COLLECTION +".JDPSBD055"; 
                    initTable(statement_, tableName," (c1 DECIMAL(12,10))");
                    statement_.executeUpdate("INSERT INTO "+tableName+" values(0.00000016)");

                    PreparedStatement ps;
                    ps = connection_.prepareStatement ( "select c1 from " + tableName, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

                    ResultSet rs = ps.executeQuery(); 
                    rs.next(); 
                    rs.updateBigDecimal (1, new BigDecimal ("0.00000017"));
                    rs.updateRow(); 
                    rs.close(); 

                    rs = ps.executeQuery();
                    rs.next();
                    String value=rs.getString(1);
                    String expectedValue = "0.0000001700";
                    if(isToolboxDriver())
                        expectedValue = "0.0000001700";  //@big TB always this format
                    else if ( isJdbc40()) {
                        if ((true) && (jdk_ >= JVMInfo.JDK_16  )) {
                            expectedValue = "0.0000001700";
                        } else { 
                            expectedValue = "1.700E-7";
                        }
                    }  
                    rs.close(); 
                    cleanupTable(statement_,tableName);
                    assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);

                } catch (Exception e) {
                    failed (e, "Unexpected Exception "+added); 
                }
            }
        }
    }

    /**
     * set a DECIMAL field using a BigDecimal which in JDK 1.5 will be formatted
     * with a posite Exponent.  The toString method was changed in JDK 1.5 to 
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc. 
     * 
     * Otherwise (that is, if the scale is negative, or the adjusted exponent 
     * is less than -6), the number will be converted to a character form using 
     * 
     * The native PTR for this problem is 9B20698
     */

    public void Var056()
    {
      String added=" -- added by native driver 2/8/2006"; 
      setupRs(); 
      if (checkJdbc20 ()) {

	  if (checkBigintSupport()) {
	      try {

		  String tableName = JDRSTest.COLLECTION +".JDPSBD056"; 
		  initTable(statement_, tableName," (c1 DECIMAL(14,2))");
		  statement_.executeUpdate("INSERT INTO "+tableName+" values(1600000000)");

		  PreparedStatement ps;
		  ps = connection_.prepareStatement ( "select c1 from " + tableName, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

		  ResultSet rs = ps.executeQuery(); 
		  rs.next(); 
		  BigDecimal bd; 
		      bd = new BigDecimal (new BigInteger("17"),-8 ); 

		  rs.updateBigDecimal (1, bd);
		  rs.updateRow();
		  rs.close(); 

		  rs = ps.executeQuery();
		  rs.next();
		  String value=rs.getString(1);
		  String expectedValue = "1700000000.00"; 
		  rs.close(); 
		  cleanupTable(statement_, tableName);
		  
		  assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);

	      } catch (Exception e) {
		  failed (e, "Unexpected Exception "+added); 
	      }
	  }
      }
    }

    /**
     * set a NUMERIC field using a BigDecimal which in JDK 1.5 will be formatted
     * with an Exponent.  The toString method was changed in JDK 1.5 to 
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc. 
     * 
     * Otherwise (that is, if the scale is negative, or the adjusted exponent 
     * is less than -6), the number will be converted to a character form using 
     * 
     * The native PTR for this problem is 9B20698
     */

    public void Var057()
    {
	setupRs(); 
        if (checkJdbc20 ()) {

            String added=" -- added by native driver 2/8/2006"; 

            if (checkBigintSupport()) {
                try {
                    String tableName = JDRSTest.COLLECTION +".JDPSBD057"; 
                    initTable(statement_, tableName," (c1 NUMERIC(12,10))");
                    statement_.executeUpdate("INSERT INTO "+tableName+" values(0.00000016)");

                    PreparedStatement ps;
                    ps = connection_.prepareStatement ( "select c1 from " + tableName, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    rs.updateBigDecimal (1, new BigDecimal ("0.00000017"));
                    rs.updateRow(); 
                    rs.close(); 

                    rs = ps.executeQuery();
                    rs.next();
                    String value=rs.getString(1);
                    String expectedValue = "0.0000001700"; 
                    if(isToolboxDriver())
                        expectedValue = "0.0000001700";  //@big TB always this format
                    else if(isJdbc40()) {
                        if ((true) && (jdk_ >= JVMInfo.JDK_16  )) {
                            expectedValue = "0.0000001700";
                        } else { 
                            expectedValue = "1.700E-7";
                        }
                    }
                    rs.close(); 
                    cleanupTable(statement_, tableName);
                    assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);

                } catch (Exception e) {
                    failed (e, "Unexpected Exception "+added); 
                }
            }
        }
    }

    /**
     * set a NUMERIC field using a BigDecimal which in JDK 1.5 will be formatted
     * with a positive Exponent.  The toString method was changed in JDK 1.5 to 
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc. 
     * 
     * Otherwise (that is, if the scale is negative, or the adjusted exponent 
     * is less than -6), the number will be converted to a character form using 
     * 
     * The native PTR for this problem is 9B20698
     */

    public void Var058()
    {
      String added=" -- added by native driver 2/8/2006"; 
      setupRs(); 
      if (checkJdbc20 ()) {

	  if (checkBigintSupport()) {
	      try {

		  String tableName = JDRSTest.COLLECTION +".JDPSBD058"; 
		  initTable(statement_, tableName, " (c1 NUMERIC(14,2))");
		  statement_.executeUpdate("INSERT INTO "+tableName+" values(1600000000)");

		  PreparedStatement ps;
		  ps = connection_.prepareStatement ( "select c1 from " + tableName, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		  ResultSet rs = ps.executeQuery();
		  rs.next();
		  BigDecimal bd; 
		      bd = new BigDecimal (new BigInteger("17"),-8 ); 
		  rs.updateBigDecimal (1, bd);
		  rs.updateRow(); 
		  rs.close(); 


		  rs = ps.executeQuery();
		  rs.next();
		  String value=rs.getString(1);
		  String expectedValue = "1700000000.00"; 
		  rs.close(); 
		  cleanupTable(statement_, tableName);
		  assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);

	      } catch (Exception e) {
		  failed (e, "Unexpected Exception "+added); 
	      }
	  }
      }
    }



  public void dfpTest(String table, String value, String expected) {
      setupRs(); 
      if (checkDecFloatSupport()) {
        try {
          Statement s = connection_.createStatement(
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s
          .executeQuery("SELECT * FROM " + table + " FOR UPDATE ");
          rs.next();
          BigDecimal bd; 
          if (value != null) { 
	    bd = new BigDecimal(value);
          } else {
            bd = null; 
          }
          rs.updateBigDecimal(1, bd);
          rs.updateRow();
          
          ResultSet rs2 = statement2f_.executeQuery("SELECT * FROM " + table);
          rs2.next();
          String v = rs2.getString(1);
          rs2.close();
          s.close();
          try {
           connection_.commit(); 
          } catch (Exception e) {} 
          assertCondition((v == null && expected == null )||(v!=null && v.equals(expected)),
          "Got " + v + " from "+ value +" sb " + expected);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }


    /**
     * updateBigDecimal -- set DFP16 to different values and retrieve
     */
    public void Var059 () { dfpTest(JDRSTest.RSTEST_DFP16, "4533.43", "4533.43"); }
    public void Var060 () { dfpTest(JDRSTest.RSTEST_DFP16, "1234567890123456", "1234567890123456");} 
    public void Var061 () { dfpTest(JDRSTest.RSTEST_DFP16, "-1234567890123456", "-1234567890123456");}
    public void Var062 () { dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456","1234567890123456");}
    public void Var063 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456E28","1.234567890123456E+43");
    }
    public void Var064 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123456E+28","1.234567890123456E+43");
    }
    public void Var065 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123456789012345.6E+29","1.234567890123456E+43");
    }
    public void Var066 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+12345678901234.56E+30","1.234567890123456E+43");
    }
    public void Var067 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890123.456E+31","1.234567890123456E+43");
    }
    public void Var068 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123456789012.3456E+32","1.234567890123456E+43");
    }
    public void Var069 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+12345678901.23456E+33","1.234567890123456E+43");
    }
    public void Var070 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567890.123456E+34","1.234567890123456E+43");
    }
    public void Var071 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123456789.0123456E+35","1.234567890123456E+43");
    }
    public void Var072 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+12345678.90123456E+36","1.234567890123456E+43");
    }
    public void Var073 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234567.890123456E+37","1.234567890123456E+43");
    }
    public void Var074 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123456.7890123456E+38","1.234567890123456E+43");
    }
    public void Var075 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+12345.67890123456E+39","1.234567890123456E+43");
    }
    public void Var076 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1234.567890123456E+40","1.234567890123456E+43");
    }
    public void Var077 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+123.4567890123456E+41","1.234567890123456E+43");
    }
    public void Var078 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+12.34567890123456E+42","1.234567890123456E+43");
    }
    public void Var079 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+1.234567890123456E+43","1.234567890123456E+43");
    }
    public void Var080 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+.1234567890123456E+44","1.234567890123456E+43");
    }
    public void Var081 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+0.1234567890123456E+44","1.234567890123456E+43");
    }
    public void Var082 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "+0.01234567890123456E+45","1.234567890123456E+43");
    }
    public void Var083 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, "-1234567890123456E28","-1.234567890123456E+43");
    }
    
    public void Var084 () { dfpTest(JDRSTest.RSTEST_DFP16, "1E0", "1");}
    public void Var085 () { dfpTest(JDRSTest.RSTEST_DFP16, "1.1", "1.1");} 
    public void Var086 () { dfpTest(JDRSTest.RSTEST_DFP16, "1.1E0", "1.1");}
    public void Var087 () { dfpTest(JDRSTest.RSTEST_DFP16, null, null);}

        /**
     * updateBigDecimal - set DFP34 to different values and retrieve
     */
    public void Var088 () { dfpTest(JDRSTest.RSTEST_DFP34, "4533.43", "4533.43"); }
    public void Var089 () { dfpTest(JDRSTest.RSTEST_DFP34, "1234567890123456", "1234567890123456");} 
    public void Var090 () { dfpTest(JDRSTest.RSTEST_DFP34, "-1234567890123456", "-1234567890123456");}
    public void Var091 () { dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890123456","1234567890123456");}
    public void Var092 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890123456E28","1.234567890123456E+43");
    }
    public void Var093 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890123456E+28","1.234567890123456E+43");
    }
    public void Var094 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+123456789012345.6E+29","1.234567890123456E+43");
    }
    public void Var095 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+12345678901234.56E+30","1.234567890123456E+43");
    }
    public void Var096 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890123.456E+31","1.234567890123456E+43");
    }
    public void Var097 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+123456789012.3456E+32","1.234567890123456E+43");
    }
    public void Var098 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+12345678901.23456E+33","1.234567890123456E+43");
    }
    public void Var099 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234567890.123456E+34","1.234567890123456E+43");
    }
    public void Var100 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+123456789.0123456E+35","1.234567890123456E+43");
    }
    public void Var101 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+12345678.90123456E+36","1.234567890123456E+43");
    }
    public void Var102 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234567.890123456E+37","1.234567890123456E+43");
    }
    public void Var103 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+123456.7890123456E+38","1.234567890123456E+43");
    }
    public void Var104 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+12345.67890123456E+39","1.234567890123456E+43");
    }
    public void Var105 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1234.567890123456E+40","1.234567890123456E+43");
    }
    public void Var106 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+123.4567890123456E+41","1.234567890123456E+43");
    }
    public void Var107 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+12.34567890123456E+42","1.234567890123456E+43");
    }
    public void Var108 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+1.234567890123456E+43","1.234567890123456E+43");
    }
    public void Var109 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+.1234567890123456E+44","1.234567890123456E+43");
    }
    public void Var110 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+0.1234567890123456E+44","1.234567890123456E+43");
    }
    public void Var111 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "+0.01234567890123456E+45","1.234567890123456E+43");
    }
    public void Var112 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, "-1234567890123456E28","-1.234567890123456E+43");
    }
    public void Var113 () { dfpTest(JDRSTest.RSTEST_DFP34, "1E0", "1");}
    public void Var114 () { dfpTest(JDRSTest.RSTEST_DFP34, "1.1", "1.1");} 
    public void Var115 () { dfpTest(JDRSTest.RSTEST_DFP34, "1.1E0", "1.1");}
    public void Var116 () { dfpTest(JDRSTest.RSTEST_DFP34, null, null);}




/**
updateBigDecimal() - Should work when the column name is valid and decimal separator is comma .
**/
    public void Var117()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 

	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("-5.3322"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
            rs2.close ();
            assertCondition (v.doubleValue () == -5.3322, added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception"+added);
        }
        }
    }




/**
updateBigDecimal() - Should be reflected by get, even if update has
not yet been issued (i.e. update is still pending).
**/
    public void Var118()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 

	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("7.6511"));
            assertCondition (compare (rsCommaSeparator_.getBigDecimal ("C_NUMERIC_105"), 7.6511), added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception"+added);
        }
        }
    }




/**
updateBigDecimal() - Should be reflected by get, after update has
been issued, but cursor has not been repositioned.
**/
    public void Var119()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_DECIMAL_105", new BigDecimal ("-12.342"));
            rsCommaSeparator_.updateRow ();
            assertCondition (rsCommaSeparator_.getBigDecimal ("C_DECIMAL_105").doubleValue () == -12.342, added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception"+added);
        }
        }
    }




/**
updateBigDecimal() - Should be reflected by get, after update has
been issued and cursor has been repositioned.
**/
    public void Var120()
    {
  	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
      if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("4982.455"));
            rsCommaSeparator_.updateRow ();
            rsCommaSeparator_.beforeFirst ();
            JDRSTest.position (rsCommaSeparator_, key_);
            BigDecimal v = rsCommaSeparator_.getBigDecimal ("C_NUMERIC_105");
            assertCondition (compare (v, 4982.455), added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception"+added);
        }
        }
    }




/**
updateBigDecimal() - Should work when the current row is the insert
row.
**/
    public void Var121()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            rsCommaSeparator_.moveToInsertRow ();
            rsCommaSeparator_.updateString ("C_KEY", "JDRSUpdateBigDecimal 1");
            rsCommaSeparator_.updateBigDecimal ("C_DECIMAL_105", new BigDecimal ("6.43431"));
            rsCommaSeparator_.insertRow ();
            JDRSTest.position (rsCommaSeparator_, "JDRSUpdateBigDecimal 1");
            assertCondition (compare (rsCommaSeparator_.getBigDecimal ("C_DECIMAL_105"), 6.43431), added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception"+added);
        }
        }
    }



/**
updateBigDecimal() - Should be reflected by get on insert row, even if
insert has not yet been issued (i.e. insert is still pending).
**/
    public void Var122()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            rsCommaSeparator_.moveToInsertRow ();
            rsCommaSeparator_.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("9898.9898"));
            assertCondition (compare (rsCommaSeparator_.getBigDecimal ("C_NUMERIC_105"), 9898.9898), added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception"+added);
        }
        }
    }

           

/**
updateBigDecimal() - Update a SMALLINT.
**/
    public void Var123 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_SMALLINT", new BigDecimal ("542"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            short v = rs2.getShort ("C_SMALLINT");
            rs2.close ();
            assertCondition (v == 542, added );
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception"+added);
        }
        }
    }


/**
updateBigDecimal() - Update a SMALLINT, when there is a decimal part.  This is OK.
**/
    public void Var124 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_SMALLINT", new BigDecimal ("33.2"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            short v = rs2.getShort ("C_SMALLINT");
            rs2.close ();
            assertCondition (v == 33, added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception"+added);
        }
        }
    }



/**
updateBigDecimal() - Update an INTEGER.
**/
    public void Var125 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_INTEGER", new BigDecimal ("-128374"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            int v = rs2.getInt ("C_INTEGER");
            rs2.close ();
            assertCondition (v == -128374, added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception"+added);
        }
        }
    }



/**
updateBigDecimal() - Update an INTEGER, when there is a decimal part.
**/
    public void Var126 ()
    {
 	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
       if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_INTEGER", new BigDecimal ("546.2323"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            int v = rs2.getInt ("C_INTEGER");
            rs2.close ();
            assertCondition (v == 546,added );
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception" + added);
        }
        }
    }



/**
updateBigDecimal() - Update a REAL.
**/
    public void Var127 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_REAL", new BigDecimal ("-2.5"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_REAL");
            rs2.close ();
            assertCondition (v == -2.5, added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception" + added);
        }
        }
    }


/**
updateBigDecimal() - Update a REAL, when the number is too big.  There is loss of 
data here but we do not lose any portion of the whole number so we do not consider
this significant truncation (we are allowing this to work just like setting a 
NUMERIC or DECIMAL field would).
**/
    public void Var128 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_REAL", new BigDecimal ("4534335.443487623"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_REAL");
            rs2.close ();
            // System.out.println("Value is " + v);
            assertCondition (v == 4534335.5, added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception" + added);
        }
        }
    }



/**
updateBigDecimal() - Update a FLOAT.
**/
    public void Var129 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_FLOAT", new BigDecimal ("9845.32"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            float v = rs2.getFloat ("C_FLOAT");
            rs2.close ();
            assertCondition (v == 9845.32f, added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception" + added);
        }
        }
    }



/**
updateBigDecimal() - Update a FLOAT, when the number is too big.

The native driver no longer throws an exception.  This was needed to
pass the J2EE CTS.

**/
    public void Var130 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
	if (checkJdbc20 ()) {
	    try {
		JDRSTest.position (rsCommaSeparator_, key_);
		rsCommaSeparator_.updateBigDecimal ("C_FLOAT", new BigDecimal ("4555444534345454545.4434556337633"));
		rsCommaSeparator_.updateRow ();
		
		assertCondition(true, added); //@tb same as native 
		 
	    }
	    catch (Exception e) {
		if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
		    failed(e); 
		} else { 

		    assertCondition(e instanceof DataTruncation);
	    /*
	    DataTruncation dt = (DataTruncation)e;
	    assertCondition ((dt.getIndex() == expectedColumn)
		&& (dt.getParameter() == false)
		&& (dt.getRead() == false)
		&& (dt.getDataSize() == 32)
		&& (dt.getTransferSize() == 19));
		*/
		}

	    }        
	}
    }



/**
updateBigDecimal() - Update a DOUBLE.
**/
    public void Var131 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_DOUBLE", new BigDecimal ("-19845.333"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            double v = rs2.getDouble ("C_DOUBLE");
            rs2.close ();
            assertCondition (v == -19845.333, added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception" + added);
        }
        }
    }



/**
updateBigDecimal() - Update a DOUBLE, when the number is too big.

native driver updated on V5R2 SI06110 to not throw truncation errors. 
**/
    public void Var132 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
	if (checkJdbc20 ()) {
	    try {
		JDRSTest.position (rsCommaSeparator_, key_);
		rsCommaSeparator_.updateBigDecimal ("C_DOUBLE", new BigDecimal ("453343443443454433.4443433445532333"));
		rsCommaSeparator_.updateRow ();
		 
		    assertCondition(true, added);  //@tb same as native 
	 
	    }
	    catch (Exception e) {
		if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
		    failed(e);
		} else { 

		    assertCondition(e instanceof DataTruncation);
	    /*
	    DataTruncation dt = (DataTruncation)e;
	    assertCondition ((dt.getIndex() == expectedColumn)
		&& (dt.getParameter() == false)
		&& (dt.getRead() == false)
		&& (dt.getDataSize() == 35)
		&& (dt.getTransferSize() == 19));
		*/
		}
	    }        
	}

    }



/**
updateBigDecimal() - Update a DECIMAL(4,0).
**/
    public void Var133 ()
    {
 	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
       if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_DECIMAL_40", new BigDecimal ("-4232"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_40");
            rs2.close ();
            assertCondition (v.floatValue() == -4232, added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception" + added);
        }
        }
    }



/**
updateBigDecimal() - Update a DECIMAL(10,5).
**/
    public void Var134 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_DECIMAL_105", new BigDecimal ("-4233.43"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
            rs2.close ();
            assertCondition (v.floatValue() == -4233.43f, added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception" + added);
        }
        }
    }




/**
updateBigDecimal() - Update a NUMERIC(10,5).
**/
    public void Var135 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_NUMERIC_105", new BigDecimal ("-9633.475"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
            rs2.close ();
            assertCondition (v.doubleValue() == -9633.475,added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception" + added);
        }
        }
    }



/**
updateBigDecimal() - Update a NUMERIC(4,0).
**/
    public void Var136 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_NUMERIC_40", new BigDecimal ("-9638"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_40");
            rs2.close ();
            assertCondition (v.doubleValue() == -9638, added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception" + added);
        }
        }
    }




/**
updateBigDecimal() - Update a CHAR.
**/
    public void Var137 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_CHAR_50", new BigDecimal ("987.1234"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_CHAR_50");
            rs2.close ();
            assertCondition (v.equals ("987.1234                                          "), added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception" + added);
        }
        }
    }



/**
updateBigDecimal() - Update a CHAR, when the value is too big.
**/
    public void Var138 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        int expectedColumn = -1;
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            expectedColumn = rsCommaSeparator_.findColumn ("C_CHAR_1");
            rsCommaSeparator_.updateBigDecimal ("C_CHAR_1", new BigDecimal ("35"));
            rsCommaSeparator_.updateRow ();
            failed ("Didn't throw SQLException"+added);
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
updateBigDecimal() - Update a VARCHAR.
**/
    public void Var139 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_VARCHAR_50", new BigDecimal ("-533423.77893"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            String v = rs2.getString ("C_VARCHAR_50");
            rs2.close ();
            assertCondition (v.equals ("-533423.77893"), added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception" + added);
        }
        }
    }



/**
updateBigDecimal() - Update a DISTINCT.
**/
    public void Var140 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rsCommaSeparator_, key_);
                rsCommaSeparator_.updateBigDecimal ("C_DISTINCT", new BigDecimal ("-32.3"));
                rsCommaSeparator_.updateRow ();
                ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_DISTINCT");
                rs2.close ();
                assertCondition (v.equals ("-32.3    "), added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception" + added);
            }
        }
        }
    }



/**
updateBigDecimal() - Update a BIGINT.
**/
    public void Var141 ()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
        try {
            JDRSTest.position (rsCommaSeparator_, key_);
            rsCommaSeparator_.updateBigDecimal ("C_BIGINT", new BigDecimal ("-12837434553"));
            rsCommaSeparator_.updateRow ();
            ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            long v = rs2.getLong ("C_BIGINT");
            rs2.close ();
            assertCondition (v == -12837434553l, added);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception" + added);
        }
        }
        }
    }




/**
updateBigDecimal() - Update a BIGINT, when there is a decimal part.
This is ok.
**/
    public void Var142()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
            try {
                JDRSTest.position (rsCommaSeparator_, key_);
                rsCommaSeparator_.updateBigDecimal ("C_BIGINT", new BigDecimal ("543246.2323"));
                rsCommaSeparator_.updateRow ();
                ResultSet rs2 = statementCommaSeparator2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                long v = rs2.getLong ("C_BIGINT");
                rs2.close ();
                assertCondition (v == 543246, added);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception" + added);
            }
            }
            }
        }


    /**
     * set a DECIMAL field using a BigDecimal which in JDK 1.5 will be formatted
     * with an Exponent.  The toString method was changed in JDK 1.5 to 
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc. 
     * 
     * Otherwise (that is, if the scale is negative, or the adjusted exponent 
     * is less than -6), the number will be converted to a character form using 
     * 
     * The native PTR for this problem is 9B20698
     */
    public void Var143()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {
            if (checkBigintSupport()) {
                try {
                    String tableName = JDRSTest.COLLECTION +".JDPSBD143"; 
                    initTable(statementCommaSeparator_, tableName, " (c1 DECIMAL(12 , 10))");
                    statementCommaSeparator_.executeUpdate("INSERT INTO "+tableName+" values(0.00000016)");

                    PreparedStatement ps;
                    ps = connectionCommaSeparator_.prepareStatement ( "select c1 from " + tableName, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

                    ResultSet rs = ps.executeQuery(); 
                    rs.next(); 
                    rs.updateBigDecimal (1, new BigDecimal ("0.00000017"));
                    rs.updateRow(); 
                    rs.close(); 

                    rs = ps.executeQuery();
                    rs.next();
                    String value=rs.getString(1);
                    String expectedValue = "0,0000001700"; 
                    if(isToolboxDriver())
                        expectedValue = "0,0000001700";  //@big TB always this format
                    else if ( isJdbc40()) {
                        if ((true) && (jdk_ >= JVMInfo.JDK_16 )) {
                            expectedValue = "0,0000001700";
                        } else { 
                            expectedValue = "1.700E-7";
                        }
                    }  
                    rs.close(); 
                    cleanupTable(statementCommaSeparator_, tableName);
                    assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);

                } catch (Exception e) {
                    failed (e, "Unexpected Exception "+added); 
                }
            }
        }
    }

    /**
     * set a DECIMAL field using a BigDecimal which in JDK 1.5 will be formatted
     * with a posite Exponent.  The toString method was changed in JDK 1.5 to 
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc. 
     * 
     * Otherwise (that is, if the scale is negative, or the adjusted exponent 
     * is less than -6), the number will be converted to a character form using 
     * 
     * The native PTR for this problem is 9B20698
     */

    public void Var144()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
      setupRsCommaSeparator();
      if (checkJdbc20 ()) {

	  if (checkBigintSupport()) {
	      try {

		  String tableName = JDRSTest.COLLECTION +".JDPSBD144"; 
		  initTable(statementCommaSeparator_,tableName," (c1 DECIMAL(14 , 2))");
		  statementCommaSeparator_.executeUpdate("INSERT INTO "+tableName+" values(1600000000)");

		  PreparedStatement ps;
		  ps = connectionCommaSeparator_.prepareStatement ( "select c1 from " + tableName, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

		  ResultSet rs = ps.executeQuery(); 
		  rs.next(); 
		  BigDecimal bd; 
		      bd = new BigDecimal (new BigInteger("17"),-8 ); 

		  rs.updateBigDecimal (1, bd);
		  rs.updateRow();
		  rs.close(); 

		  rs = ps.executeQuery();
		  rs.next();
		  String value=rs.getString(1);
		  String expectedValue = "1700000000,00"; 
		  rs.close(); 
		  cleanupTable(statementCommaSeparator_, tableName);
		  assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);

	      } catch (Exception e) {
		  failed (e, "Unexpected Exception "+added); 
	      }
	  }
      }
    }

    /**
     * set a NUMERIC field using a BigDecimal which in JDK 1.5 will be formatted
     * with an Exponent.  The toString method was changed in JDK 1.5 to 
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc. 
     * 
     * Otherwise (that is, if the scale is negative, or the adjusted exponent 
     * is less than -6), the number will be converted to a character form using 
     * 
     * The native PTR for this problem is 9B20698
     */

    public void Var145()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
	setupRsCommaSeparator();
        if (checkJdbc20 ()) {


            if (checkBigintSupport()) {
                try {
                    String tableName = JDRSTest.COLLECTION +".JDPSBD145"; 
                    initTable(statementCommaSeparator_,tableName," (c1 NUMERIC(12 , 10))");
                    statementCommaSeparator_.executeUpdate("INSERT INTO "+tableName+" values(0.00000016)");

                    PreparedStatement ps;
                    ps = connectionCommaSeparator_.prepareStatement ( "select c1 from " + tableName, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    rs.updateBigDecimal (1, new BigDecimal ("0.00000017"));
                    rs.updateRow(); 
                    rs.close(); 

                    rs = ps.executeQuery();
                    rs.next();
                    String value=rs.getString(1);
                    String expectedValue = "0,0000001700"; 
                    if(isToolboxDriver())
                        expectedValue = "0,0000001700";  //@big TB always this format
                    else if(isJdbc40()) {
                        if ((true) && (jdk_ >= JVMInfo.JDK_16  )) {
                            expectedValue = "0,0000001700";
                        } else { 
                            expectedValue = "1.700E-7";
                        }
                    }
                    rs.close(); 
                    cleanupTable(statementCommaSeparator_, tableName);
                    assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);

                } catch (Exception e) {
                    failed (e, "Unexpected Exception "+added); 
                }
            }
        }
    }

    /**
     * set a NUMERIC field using a BigDecimal which in JDK 1.5 will be formatted
     * with a positive Exponent.  The toString method was changed in JDK 1.5 to 
     * format a BigDecimal with an exponent.  Here is a snippet from the
     * java doc. 
     * 
     * Otherwise (that is, if the scale is negative, or the adjusted exponent 
     * is less than -6), the number will be converted to a character form using 
     * 
     * The native PTR for this problem is 9B20698
     */

    public void Var146()
    {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
      setupRsCommaSeparator();
      if (checkJdbc20 ()) {

	  if (checkBigintSupport()) {
	      try {

		  String tableName = JDRSTest.COLLECTION +".JDPSBD146"; 
		  initTable(statementCommaSeparator_,tableName," (c1 NUMERIC(14 , 2))");
		  statementCommaSeparator_.executeUpdate("INSERT INTO "+tableName+" values(1600000000)");

		  PreparedStatement ps;
		  ps = connectionCommaSeparator_.prepareStatement ( "select c1 from " + tableName, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		  ResultSet rs = ps.executeQuery();
		  rs.next();
		  BigDecimal bd; 
		      bd = new BigDecimal (new BigInteger("17"),-8 ); 
		  rs.updateBigDecimal (1, bd);
		  rs.updateRow(); 
		  rs.close(); 


		  rs = ps.executeQuery();
		  rs.next();
		  String value=rs.getString(1);
		  String expectedValue = "1700000000,00"; 
		  rs.close(); 
		  cleanupTable(statementCommaSeparator_,tableName);
		  assertCondition(expectedValue.equals(value), "value("+value+") != "+expectedValue+added);

	      } catch (Exception e) {
		  failed (e, "Unexpected Exception "+added); 
	      }
	  }
      }
    }



  public void dfpTestCommaSeparator(String table, String value, String expected) {
	String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X"; 
      if (checkDecFloatSupport()) {

	  setupRsCommaSeparator();
        try {
          Statement s = connectionCommaSeparator_.createStatement(
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s
          .executeQuery("SELECT * FROM " + table + " FOR UPDATE ");
          rs.next();
          BigDecimal bd; 
          if (value != null) { 
	    bd = new BigDecimal(value);
          } else {
            bd = null; 
          }
          rs.updateBigDecimal(1, bd);
          rs.updateRow();
          
          ResultSet rs2 = statementCommaSeparator2f_.executeQuery("SELECT * FROM " + table);
          rs2.next();
          String v = rs2.getString(1);
          rs2.close();
          s.close();
          try {
           connectionCommaSeparator_.commit(); 
          } catch (Exception e) {} 
          assertCondition((v == null && expected == null )||(v!=null && v.equals(expected)),
          "Got " + v + " from "+ value +" sb " + expected);
        } catch (Exception e) {
          failed(e, "Unexpected Exception" + added);
        }
      }
    }


    /**
     * updateBigDecimal -- set DFP16 to different values and retrieve
     */
    public void Var147 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "4533.43", "4533,43"); }
    public void Var148 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "1234567890123456", "1234567890123456");} 
    public void Var149 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "-1234567890123456", "-1234567890123456");}
    public void Var150 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+1234567890123456","1234567890123456");}
    public void Var151 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+1234567890123456E28","1,234567890123456E+43");
    }
    public void Var152 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+1234567890123456E+28","1,234567890123456E+43");
    }
    public void Var153 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+123456789012345.6E+29","1,234567890123456E+43");
    }
    public void Var154 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+12345678901234.56E+30","1,234567890123456E+43");
    }
    public void Var155 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+1234567890123.456E+31","1,234567890123456E+43");
    }
    public void Var156 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+123456789012.3456E+32","1,234567890123456E+43");
    }
    public void Var157 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+12345678901.23456E+33","1,234567890123456E+43");
    }
    public void Var158 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+1234567890.123456E+34","1,234567890123456E+43");
    }
    public void Var159 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+123456789.0123456E+35","1,234567890123456E+43");
    }
    public void Var160 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+12345678.90123456E+36","1,234567890123456E+43");
    }
    public void Var161 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+1234567.890123456E+37","1,234567890123456E+43");
    }
    public void Var162 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+123456.7890123456E+38","1,234567890123456E+43");
    }
    public void Var163 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+12345.67890123456E+39","1,234567890123456E+43");
    }
    public void Var164 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+1234.567890123456E+40","1,234567890123456E+43");
    }
    public void Var165 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+123.4567890123456E+41","1,234567890123456E+43");
    }
    public void Var166 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+12.34567890123456E+42","1,234567890123456E+43");
    }
    public void Var167 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+1.234567890123456E+43","1,234567890123456E+43");
    }
    public void Var168 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+.1234567890123456E+44","1,234567890123456E+43");
    }
    public void Var169 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+0.1234567890123456E+44","1,234567890123456E+43");
    }
    public void Var170 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "+0.01234567890123456E+45","1,234567890123456E+43");
    }
    public void Var171 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "-1234567890123456E28","-1,234567890123456E+43");
    }
    
    public void Var172 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "1E0", "1");}
    public void Var173 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "1.1", "1,1");} 
    public void Var174 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, "1.1E0", "1,1");}
    public void Var175 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP16, null, null);}

        /**
     * updateBigDecimal - set DFP34 to different values and retrieve
     */
    public void Var176 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "4533.43", "4533,43"); }
    public void Var177 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "1234567890123456", "1234567890123456");} 
    public void Var178 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "-1234567890123456", "-1234567890123456");}
    public void Var179 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+1234567890123456","1234567890123456");}
    public void Var180 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+1234567890123456E28","1,234567890123456E+43");
    }
    public void Var181 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+1234567890123456E+28","1,234567890123456E+43");
    }
    public void Var182 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+123456789012345.6E+29","1,234567890123456E+43");
    }
    public void Var183 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+12345678901234.56E+30","1,234567890123456E+43");
    }
    public void Var184 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+1234567890123.456E+31","1,234567890123456E+43");
    }
    public void Var185 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+123456789012.3456E+32","1,234567890123456E+43");
    }
    public void Var186 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+12345678901.23456E+33","1,234567890123456E+43");
    }
    public void Var187 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+1234567890.123456E+34","1,234567890123456E+43");
    }
    public void Var188 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+123456789.0123456E+35","1,234567890123456E+43");
    }
    public void Var189 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+12345678.90123456E+36","1,234567890123456E+43");
    }
    public void Var190 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+1234567.890123456E+37","1,234567890123456E+43");
    }
    public void Var191 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+123456.7890123456E+38","1,234567890123456E+43");
    }
    public void Var192 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+12345.67890123456E+39","1,234567890123456E+43");
    }
    public void Var193 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+1234.567890123456E+40","1,234567890123456E+43");
    }
    public void Var194 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+123.4567890123456E+41","1,234567890123456E+43");
    }
    public void Var195 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+12.34567890123456E+42","1,234567890123456E+43");
    }
    public void Var196 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+1.234567890123456E+43","1,234567890123456E+43");
    }
    public void Var197 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+.1234567890123456E+44","1,234567890123456E+43");
    }
    public void Var198 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+0.1234567890123456E+44","1,234567890123456E+43");
    }
    public void Var199 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "+0.01234567890123456E+45","1,234567890123456E+43");
    }
    public void Var200 () {
	    dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "-1234567890123456E28","-1,234567890123456E+43");
    }
    public void Var201 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "1E0", "1");}
    public void Var202 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "1.1", "1,1");} 
    public void Var203 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, "1.1E0", "1,1");}
    public void Var204 () { dfpTestCommaSeparator(JDRSTest.RSTEST_DFP34, null, null);}

    /**
updateBigDecimal() - Update a BOOLEAN.
**/
    public void Var205 ()
    {
      setupRs(); 
        if (checkJdbc20 ()) {
        if (checkBooleanSupport()) {
        try {
            JDRSTest.position (rs_, key_);
            rs_.updateBigDecimal ("C_BOOLEAN", new BigDecimal ("12837434553"));
            rs_.updateRow ();
            ResultSet rs2 = statement2_.executeQuery (select_);
            JDRSTest.position (rs2, key_);
            boolean v = rs2.getBoolean ("C_BOOLEAN");
            rs2.close ();
            assertCondition (v == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
        }
    }

 
 
    

}



