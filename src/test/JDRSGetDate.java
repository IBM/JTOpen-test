///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetDate.java
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

import java.sql.Connection;
import java.sql.Date;

import java.sql.ResultSet;

import java.sql.Statement;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.GregorianCalendar; 



/**
Testcase JDRSGetDate.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getDate()
</ul>
**/
public class JDRSGetDate
extends JDTestcase
{

                  
                  
    // Private data.
    private String              properties_;
    private Statement           statement_;
    private Statement           statement0_;
    private ResultSet           rs_;
    private String              rsQuery_;
    private int                 driver_;




/**
Constructor.
**/
    public JDRSGetDate (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSGetDate",
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
        if (getDriver()  != JDTestDriver.DRIVER_JCC) { 
            url += ";date format=iso;time format=iso";
        }
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
	setAutoCommit(connection_, false); // @E1A
        statement0_ = connection_.createStatement (); 
        properties_ = "X";

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
                + " (C_KEY) VALUES ('DUMMY_ROW')");
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
Reconnects with different properties, if needed.

@exception Exception If an exception occurs.
**/
    private void reconnect (String properties)
    throws Exception
    {
        if (! properties_.equals (properties)) {
            properties_ = properties;
            if (connection_ != null)
                cleanup ();

            String url = baseURL_
                         
                          + ";" + properties_;
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
            setAutoCommit(connection_, false); //@E1A


            // Until the autocommit mess gets cleaned up, we need to never
            // run with tx level *none through the native driver and use
            // result sets that have lobs in them.  This should be fixed in days.
            if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
                // @E1D connection_.setAutoCommit (false);
                connection_.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);
            }

            statement0_ = connection_.createStatement ();

            if (isJdbc20 ()) {
		if (driver_ == JDTestDriver.DRIVER_JTOPENLITE) {
		    statement_ = connection_.createStatement(); 
		} else { 
		    statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							      ResultSet.CONCUR_UPDATABLE);
		}
                statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_GET
                                          + " (C_KEY) VALUES ('DUMMY_ROW')");
                rs_ = statement_.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_GET + " FOR UPDATE");
            }
        }
    }



/**
getDate() - Should throw exception when the result set is
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
            rs.getDate (1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            Date v = rs.getDate (1);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            Date v = rs.getDate (100);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            Date v = rs.getDate (0);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            Date v = rs.getDate (-1);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Should work when the column index is valid.
**/
    public void Var006()
    {
	String value = "UNSET"; 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
	    value = rs.getString(19); 
            Date v = rs.getDate (19);
            String stringValue = v.toString(); 
            assertCondition (stringValue.equals ("2000-02-21"), "Got '"+stringValue+"' sb '2000-02-21'");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception stringValue="+value);
        }
    }



/**
getDate() - Should throw an exception when the column
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
          rs.getDate (null);
          failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }


/**
getDate() - Should throw an exception when the column
name is an empty string.
**/
    public void Var008()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.getDate ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Should throw an exception when the column
name is invalid.
**/
    public void Var009()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.getDate ("INVALID");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Should throw an exception when the calendar is null.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "DATE_1998");
            Date v = rs_.getDate (19, null);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getDate() - Should work when the column name is valid.
**/
    public void Var011()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Date v = rs.getDate ("C_DATE");
            String vString = v.toString (); 
            assertCondition (vString.equals ("1998-04-08"), "Got '"+vString+"' sb '1998-04-08'");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDate() - Should work when an update is pending.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateDate ("C_DATE", Date.valueOf ("1998-05-26"));
            Date v = rs_.getDate ("C_DATE");
            assertCondition (v.toString ().equals ("1998-05-26"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getDate() - Should work when an update has been done.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateDate ("C_DATE", Date.valueOf ("1969-11-18"));
            rs_.updateRow ();
            Date v = rs_.getDate ("C_DATE");
            assertCondition (v.toString ().equals ("1969-11-18"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getDate() - Should work when the current row is the insert
row, when an insert is pending.
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
            rs_.updateDate ("C_DATE", Date.valueOf ("1996-06-25"));
            Date v = rs_.getDate ("C_DATE");
            assertCondition (v.toString ().equals ("1996-06-25"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getDate() - Should work when the current row is the insert
row, when an insert has been done.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow"); 
            return; 
          }
        try {
            rs_.moveToInsertRow ();
            rs_.updateDate ("C_DATE", Date.valueOf ("1998-02-21"));
            rs_.insertRow ();
            Date v = rs_.getDate ("C_DATE");
            assertCondition (v.toString ().equals ("1998-02-21"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getDate() - Should throw an exception on a deleted row.
**/
    public void Var016()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) { 
        notApplicable("JCC doesn't throw exception for get on deleted row");
        return; 
      }
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "DUMMY_ROW");
            rs_.deleteRow ();
            Date v = rs_.getDate ("C_DATE");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getDate() - Should return null when the column is NULL.
**/
    public void Var017 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_NULL");
            Date v = rs.getDate ("C_DATE");
            assertCondition (v == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDate() - Should work when a calendar other than the
default is passed.

SQL400 - For this test to return UTC timezone information, 
         additional AS/400 setup would be required.  The
         directions for doing this can be found at:
         http://publib.boulder.ibm.com/pubs/html/as400/ic2924/info/java/rzaha/devkit.htm
         For now, we just take the defaults and expect the
         calendar to be ignored in our testing.   D1C
**/
    public void Var018()
    {
        if (checkJdbc20 ()) {
        try {
            rs_ = JDRSTest.position (driver_, statement_, rsQuery_, rs_, "DATE_1998");
            Calendar gmt = Calendar.getInstance (TimeZone.getTimeZone ("GMT"));
            Date v = rs_.getDate ("C_DATE", gmt);

	    String tz = System.getProperty("user.timezone");

 	    // Make this work for V5R4 group test -- same result as before @PDC change
            // For the V5R4 group test, the timezone is set to UTC  @H2A 
 	    if ((isToolboxDriver()) &&
 		(getRelease() == JDTestDriver.RELEASE_V5R4M0 || getRelease() >= JDTestDriver.RELEASE_V7R1M0) &&
 		(JDTestDriver.OSName_.indexOf("OS/400") >= 0) &&
                 "UTC".equals(tz)) {
                assertCondition (v.toString ().equals ("1998-04-08"));
	       return;
 	    }
 

	    if (((getDriver () == JDTestDriver.DRIVER_NATIVE  && "UTC".equals(tz)) )) {  //@F1A //@pdc
               assertCondition (v.toString ().equals ("1998-04-08"),
				"got "+v.toString()+" expected 1998-04-08 tz="+tz);
	    } else	 { 
		assertCondition (v.toString ().equals ("1998-04-07"), "got "+v.toString()+" expected 1998-04-07 tz="+tz );
	    }
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }



/**
getDate() - Get from a SMALLINT.
**/
    public void Var019 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            Date v = rs.getDate ("C_SMALLINT");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Get from a INTEGER.
**/
    public void Var020 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            Date v = rs.getDate ("C_INTEGER");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Get from a REAL.
**/
    public void Var021 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            Date v = rs.getDate ("C_REAL");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Get from a FLOAT.
**/
    public void Var022 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            Date v = rs.getDate ("C_FLOAT");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Get from a DOUBLE.
**/
    public void Var023 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            Date v = rs.getDate ("C_DOUBLE");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Get from a DECIMAL.
**/
    public void Var024 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            Date v = rs.getDate ("C_DECIMAL_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Get from a NUMERIC.
**/
    public void Var025 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            Date v = rs.getDate ("C_NUMERIC_105");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Get from a CHAR, where the value does not translate
to a date.
**/
    public void Var026 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            Date v = rs.getDate ("C_CHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Get from a CHAR, where the value does translate
to a date.
**/
    public void Var027 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_DATE");
            Date v = rs.getDate ("C_CHAR_50");
            assertCondition (v.toString ().equals ("1991-03-15"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDate() - Get from a VARCHAR, where the value does not translate
to a date.
**/
    public void Var028 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            Date v = rs.getDate ("C_VARCHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Get from a VARCHAR, where the value does translate
to a date.
**/
    public void Var029 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_DATE");
            Date v = rs.getDate ("C_VARCHAR_50");
            assertCondition (v.toString ().equals ("2023-11-21"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDate() - Get from a BINARY.
**/
    public void Var030 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            Date v = rs.getDate ("C_BINARY_20");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Get from a VARBINARY.
**/
    public void Var031 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            Date v = rs.getDate ("C_VARBINARY_20");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Get from a CLOB.
**/
    public void Var032 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                Date v = rs.getDate ("C_CLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getDate() - Get from a DBCLOB.
**/
    public void Var033 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                Date v = rs.getDate ("C_DBCLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getDate() - Get from a BLOB.
**/
    public void Var034 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                Date v = rs.getDate ("C_BLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getDate() - Get from a DATE.
**/
    public void Var035 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Date v = rs.getDate ("C_DATE");
            String vString = v.toString(); 
            assertCondition (vString.equals ("1998-04-08"), "Got '"+vString+"' sb '1998-04-08'");
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDate() - Get from a TIME.
**/
    public void Var036 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            Date v = rs.getDate ("C_TIME");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getDate() - Get from a TIMESTAMP.
**/
    public void Var037 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            Date v = rs.getDate ("C_TIMESTAMP");
            assertCondition (v.toString ().equals ("2000-06-25"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getDate() - Get from a DATALINK.
**/
    public void Var038 ()
    {
        if (checkDatalinkSupport ()) {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GETDL);
                JDRSTest.position0 (rs, "LOB_FULL");
                Date v = rs.getDate ("C_DATALINK");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getDate() - Get from a DISTINCT.
**/
    public void Var039 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                Date v = rs.getDate ("C_DISTINCT");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
getDate() - Get from a BIGINT.
**/
    public void Var040 ()
    {
        if (checkBigintSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            Date v = rs.getDate ("C_BIGINT");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }

/**
 * getDate() -- get from a 1998 date using Format usa 
 */ 
    public void Var041 ()
    {
        try {
            reconnect ("date format=usa");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("1998-04-08");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 1998-04-08"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   

/**
 * getDate() -- get from a 1998 date using Format eur
 */ 
    public void Var042 ()
    {
        try {
            reconnect ("date format=eur");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("1998-04-08");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 1998-04-08"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   

/**
 * getDate() -- get from a 1998 date using Format jis
 */ 
    public void Var043 ()
    {
        try {
            reconnect ("date format=jis");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("1998-04-08");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 1998-04-08"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 1998 date using Format julian
 */ 
    public void Var044 ()
    {
        try {
            reconnect ("date format=julian;date separator=/");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("1998-04-08");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 1998-04-08"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 1998 date using Format julia
 */ 
    public void Var045 ()
    {
        try {
            reconnect ("date format=julian;date separator=-");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("1998-04-08");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 1998-04-08"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 1998 date using Format mdy
 */ 
    public void Var046 ()
    {
        try {
            reconnect ("date format=mdy;date separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("1998-04-08");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 1998-04-08"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 1998 date using Format mdy
 */ 
    public void Var047 ()
    {
        try {
            reconnect ("date format=mdy;date separator=,");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("1998-04-08");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 1998-04-08"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 1998 date using Format dmy
 */ 
    public void Var048 ()
    {
        try {
            reconnect ("date format=dmy;date separator=b");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("1998-04-08");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 1998-04-08"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 1998 date using Format dmy 
 */ 
    public void Var049 ()
    {
        try {
            reconnect ("date format=dmy;date separator=/");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("1998-04-08");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 1998-04-08"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 1998 date using Format ymd 
 */ 
    public void Var050 ()
    {
        try {
            reconnect ("date format=ymd;date separator=-");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("1998-04-08");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 1998-04-08"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 1998 date using Format ymd 
 */ 
    public void Var051 ()
    {
        try {
            reconnect ("date format=ymd;date separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("1998-04-08");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 1998-04-08"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 1998 date using Format iso
 */ 
    public void Var052 ()
    {
        try {
            reconnect ("time format=iso");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("1998-04-08");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 1998-04-08"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   



/**
 * getDate() -- get from a 2000 date using Format usa 
 */ 
    public void Var053 ()
    {
        try {
            reconnect ("date format=usa");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("2000-02-21");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 2000-02-21"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   

/**
 * getDate() -- get from a 2000 date using Format eur
 */ 
    public void Var054 ()
    {
        try {
            reconnect ("date format=eur");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("2000-02-21");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 2000-02-21"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   

/**
 * getDate() -- get from a 2000 date using Format jis
 */ 
    public void Var055 ()
    {
        try {
            reconnect ("date format=jis");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("2000-02-21");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 2000-02-21"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 2000 date using Format julian
 */ 
    public void Var056 ()
    {
        try {
            reconnect ("date format=julian;date separator=/");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("2000-02-21");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 2000-02-21"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 2000 date using Format julia
 */ 
    public void Var057 ()
    {
        try {
            reconnect ("date format=julian;date separator=-");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("2000-02-21");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 2000-02-21"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 2000 date using Format mdy
 */ 
    public void Var058 ()
    {
        try {
            reconnect ("date format=mdy;date separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("2000-02-21");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 2000-02-21"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 2000 date using Format mdy
 */ 
    public void Var059 ()
    {
        try {
            reconnect ("date format=mdy;date separator=,");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("2000-02-21");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 2000-02-21"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 2000 date using Format dmy
 */ 
    public void Var060 ()
    {
        try {
            reconnect ("date format=dmy;date separator=b");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("2000-02-21");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 2000-02-21"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 2000 date using Format dmy 
 */ 
    public void Var061 ()
    {
        try {
            reconnect ("date format=dmy;date separator=/");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("2000-02-21");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 2000-02-21"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 2000 date using Format ymd 
 */ 
    public void Var062 ()
    {
        try {
            reconnect ("date format=ymd;date separator=-");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("2000-02-21");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 2000-02-21"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 2000 date using Format ymd 
 */ 
    public void Var063 ()
    {
        try {
            reconnect ("date format=ymd;date separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("2000-02-21");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 2000-02-21"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
   
/**
 * getDate() -- get from a 2000 date using Format iso
 */ 
    public void Var064 ()
    {
        try {
            reconnect ("time format=iso");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            Date v = rs.getDate ("C_DATE");
            boolean ok = v.toString ().equals ("2000-02-21");
	    if ( ok ) {
		assertCondition (ok);
	    } else {
		failed("v.toString() "+v.toString()+" != 2000-02-21"); 
	    } 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

                      
    // Make sure we don't throw an exception when getting more data.  Here is the theory.
    // We create a date with a date that won't fit in mdy format.  We fill the table with
    // that data.  On the executeQuery, the server tells Statement that the date is bad,
    // but that is a warning.  We go through the RS.  Eventually we will need more data.
    // On the fetch we used to throw an exception even though the server still returns 
    // a warning.  With the fix we will get the rest of the data.
    public void Var065 ()
    {      
        Connection connection = null;
        Statement  statement  = null;
        ResultSet  rs         = null;

        String table = JDRSTest.COLLECTION + ".WARNINGS";

        String url = baseURL_ + ";date format=mdy"
                              ;

        try 
        {      
           connection = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
           statement  = connection.createStatement(); 
           try {
             statement.executeUpdate("DELETE FROM "+table); 
           } catch (Exception e) { 
            statement.executeUpdate("CREATE TABLE " + table + " ( P1_INT INTEGER , P2_DATE DATE NOT NULL ) ");
           } 
           for (int i=0; i<3000; i++)
              try { statement.executeUpdate("INSERT INTO " + table + " (P1_INT , P2_DATE) VALUES ( " + i + " , {d '1936-01-01'})"); } catch (Exception e) {}

           rs = statement.executeQuery("Select * from " + table);
           int rowCount = 0;
           while (rs.next())
           {
              rs.getDate(2); 
           // System.out.println(rs.getString(1) + " " + d);
              rowCount++;
           }
           
           assertCondition(rowCount > 0, "Error row"); 
           
           cleanupTable(statement,  table ); 
        }
        catch (Exception e) 
        {
            failed (e, "Unexpected Exception");
        }
        
        if (statement != null)
           cleanupTable(statement,  table ); 

        if (rs != null)        
           try { rs.close(); } catch (Exception e) {}
        if (statement != null)        
           try { statement.close(); } catch (Exception e) {}
        if (connection != null)        
           try { connection.close(); } catch (Exception e) {}

    }


//@H1A  Test that milliseconds are zero when getting a Date from a TIMESTAMP field.
// According to the java.sql.Date class, "To conform with the definition of SQL DATE, the millisecond values 
// wrapped by a java.sql.Date instance must be 'normalized' by setting the hours, minutes, seconds, and milliseconds 
// to zero in the particular time zone with which the instance is associated." 
/**
getDate() - Get from a TIMESTAMP.
**/
    public void Var066 ()
    {
      if (getDriver () == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC doesn't normalize when getting from timestamep"); 
        return; 
      }
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            Date v = rs.getDate ("C_TIMESTAMP");
            java.lang.Long l = new Long(v.getTime());

            if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
                /* NOTE:  For native, the toolbox check fails because timezone is UTC not CST */
                /* Get the right answer and compare it */ 
                GregorianCalendar cal = new GregorianCalendar(2000,5 /* zero based */ ,25);
                java.util.Date eDate = cal.getTime(); 
                assertCondition (l.equals(new Long(eDate.getTime())), "Added by Toolbox 8/26/2004 \n" +
                                "-- date should be normalized \n" +
                                "l = "+l+"("+v+")  \n" +
                                "sb  "+eDate.getTime()+"  =" + eDate); 

            } else {
                // String timezone = System.getProperty("user.timezone");
                //@pdd long is platform dependent, change to generic getTime method
                //if (timezone == null || (! timezone.equals("UTC"))) { 
                 //   System.out.println("aaa1" + l);
                 //   assertCondition (l.equals(new Long("961909200000")), "Added by Toolbox 8/26/2004 -- date should be normalized l = "+l+"("+v+")  sb 961909200000  =" + new Date(961909200000L));
                //} else {
                   
                    GregorianCalendar cal = new GregorianCalendar(2000,5 /* zero based */ ,25);
                    java.util.Date eDate = cal.getTime(); 
                    assertCondition (l.equals(new Long(eDate.getTime())), 
                        "Added by Toolbox 8/26/2004 -- date should be normalized \n" +
                        "l = "+l+"("+v+")\n" +
                        "sb  "+eDate.getTime()+"  =" + eDate); 
               // } 
            }
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception.  Added by Toolbox 8/26/2004");
        }
    }

    
    /**
    getDate() - Get from DFP16:
    **/
   public void Var067 ()
   {
     if (checkDecFloatSupport()) {
       try {
         Statement s = connection_.createStatement ();
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_DFP16);
         rs.next(); 
         Date v = rs.getDate (1);
         failed ("Didn't throw SQLException "+v);
       }
       catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
       }
     }
   }
   
   
   
   /**
    getDate() - Get from DFP34:
    **/
   public void Var068 ()
   {
     if (checkDecFloatSupport()) {
       try {
         Statement s = connection_.createStatement ();
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_DFP34);
         rs.next(); 
         Date v = rs.getDate (1);
         failed ("Didn't throw SQLException "+v);
       }
       catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
       }
     }
   }
   
   /**
   getDate() - Get from SQLXML:
   **/
  public void Var069 ()
  {
    if (checkDecFloatSupport()) {
      try {
        Statement s = connection_.createStatement ();
        ResultSet rs = s.executeQuery ("SELECT * FROM "
            + JDRSTest.RSTEST_GETXML);
        rs.next(); 
        Date v = rs.getDate ("C_XML");
        failed ("Didn't throw SQLException "+v);
      }
      catch (Exception e) {
        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
    }
  }
  

        /**
         * getDate() - Get from a TIMESTAMP(12).
         **/
        public void Var070() {
            if (checkTimestamp12Support()) {
              testGet(statement0_,
                  "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
                  "getDate", 
                  "1998-11-18",
                  " -- added 11/19/2012"); 

            }
        }


        /**
         * getDate() - Get from a TIMESTAMP(0).
         **/
        public void Var071() {
            if (checkTimestamp12Support()) {
          testGet(statement0_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
            "getDate",
            "1998-11-18",
            " -- added 11/19/2012"); 
            
            
            }
        }

/**
getDate() - Get from a BOOLEAN.
**/
	public void Var072 ()
	{
	    if (checkBooleanSupport()) {
		try {
		    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_BOOLEAN);
		    JDRSTest.position0 (rs, "BOOLEAN_TRUE");
		    Date v = rs.getDate ("C_BOOLEAN");
		    failed ("Didn't throw SQLException"+v);
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}


/**
getDate() - Get from a BOOLEAN.
**/
	public void Var073 ()
	{
	    if (checkBooleanSupport()) {
		try {
		    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_BOOLEAN);
		    JDRSTest.position0 (rs, "BOOLEAN_FALSE");
		    Date v = rs.getDate ("C_BOOLEAN");
		    failed ("Didn't throw SQLException"+v);
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}

/**
getDate() - Get from a BOOLEAN.
**/
    public void Var074()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_NULL");
            Date v = rs.getDate ("C_BOOLEAN");
            assertCondition (v == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }





}



