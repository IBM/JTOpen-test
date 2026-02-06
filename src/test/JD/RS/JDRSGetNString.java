///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetNString.java
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
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JDRSGetNString.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getNString()
</ul>
**/
public class JDRSGetNString
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetNString";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private String              properties_;
    private Statement           statement_;
    private Statement           statement0_;
    private ResultSet           rs_;
    private static  String table_  = JDRSTest.COLLECTION + ".JDRS1";
    private static  String clobTable4_ = JDRSTest.COLLECTION + ".JDRSGNSCL4";
    private static  String clobTable_ = JDRSTest.COLLECTION +  ".JDRSGNSCL1";
    private StringBuffer errorBuffer = new StringBuffer(); 
    boolean runningJ9 = false;


/**
Constructor.
**/
    public JDRSGetNString (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDRSGetNString",
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
        properties_ = "X";
        reconnect ("");
        table_  = JDRSTest.COLLECTION + ".JDRS1";
	clobTable_ =  JDRSTest.COLLECTION + ".JDRSGSNCL1";
	clobTable4_ = JDRSTest.COLLECTION + ".JDRSGSNCL4";

	if (getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) {
		String vmName = System.getProperty("java.vm.name");
		if (vmName ==  null) {
		    runningJ9 = false; 
		} else { 
		    if (vmName.indexOf("Classic VM") >= 0) {
			runningJ9 = false;
		    } else {
			runningJ9 = true;
		    }
		}
	}
      // Re-Create a couple tables that were created in the JDRSTest test driver.
      // Several testcases use these tables (including this testcase).  They need to be
      // recreated due to changes along the way.
      JDRSTest.createAndPopulateTable(JDRSTest.RSTEST_DFP16, "C1 DECFLOAT(16)", JDRSTest.VALUES_DFP16, statement0_); 
      JDRSTest.createAndPopulateTable(JDRSTest.RSTEST_DFP34, "C1 DECFLOAT(34)", JDRSTest.VALUES_DFP34, statement0_);
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
                statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                          ResultSet.CONCUR_UPDATABLE);
                statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_GET
                                          + " (C_KEY) VALUES ('DUMMROW_GNSTRING')");
                rs_ = statement_.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_GET + " FOR UPDATE");
            }
        }
    }



/**
getNString() - Should throw exception when the result set is
closed.
**/
    public void Var001()
    {
      if ( checkJdbc40()) { 
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                                           + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.close ();
            JDReflectionUtil.callMethod_O(rs, "getNString", 1);
            failed ("Didn't throw SQLException");
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }



/**
getNString() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
      if ( checkJdbc40()) { 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            String s = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
            failed ("Didn't throw SQLException but returned "+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
getNString() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
      if ( checkJdbc40()) { 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            rs.next ();
            String s = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 100);
            failed ("Didn't throw SQLException but returned "+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
getNString() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
      if ( checkJdbc40()) { 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 0);
            failed ("Didn't throw SQLException but returned "+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
getNString() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
      if ( checkJdbc40()) { 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_O(rs, "getNString", -1);
            failed ("Didn't throw SQLException but returned "+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
getNString() - Should work when the column index is valid.
**/
    public void Var006()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if ( checkJdbc40()) { 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
            assertCondition (s.equals ("DATE_1998"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Should throw an exception when the column
name is null.
**/
    public void Var007()
    {
      if ( checkJdbc40()) { 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", null);
            failed ("Didn't throw SQLException but returned "+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
getNString() - Should throw an exception when the column
name is an empty string.
**/
    public void Var008()
    {
      if ( checkJdbc40()) { 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "");
            failed ("Didn't throw SQLException but returned"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
getNString() - Should throw an exception when the column
name is invalid.
**/
    public void Var009()
    {
      if ( checkJdbc40()) { 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "INVALID");
            failed ("Didn't throw SQLException but returned"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }
    }


/**
getNString() - Should work when the column name is valid.
**/
    public void Var010()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if ( checkJdbc40()) { 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_KEY");
            assertCondition (s.equals ("DATE_1998"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Should work when an update is pending.
**/
    public void Var011()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
        if (checkJdbc40 ()) {
            try {
                JDRSTest.position (rs_, "UPDATE_SANDBOX");
                rs_.updateString ("C_VARCHAR_50", "Ninja");
                String v = (String) JDReflectionUtil.callMethod_OS(rs_, "getNString", "C_VARCHAR_50");
                assertCondition (v.equals ("Ninja"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getNString() - Should work when an update has been done.
**/
    public void Var012()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
        if (checkJdbc40 ()) {
            try {
                JDRSTest.position (rs_, "UPDATE_SANDBOX");
                rs_.updateString ("C_VARCHAR_50", "Money");
                rs_.updateRow ();
                String v = (String) JDReflectionUtil.callMethod_OS(rs_, "getNString","C_VARCHAR_50");
                assertCondition (v.equals ("Money"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getNString() - Should work when the current row is the insert
row, when an insert is pending.
**/
    public void Var013()
    {
        if (checkJdbc40 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow"); 
            return; 
          }
            try {
                rs_.moveToInsertRow ();
                rs_.updateString ("C_VARCHAR_50", "Slipped");
                String v = (String) JDReflectionUtil.callMethod_OS(rs_, "getNString","C_VARCHAR_50");
                assertCondition (v.equals ("Slipped"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getNString() - Should work when the current row is the insert
row, when an insert has been done.
**/
    public void Var014()
    {
        if (checkJdbc40 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow"); 
            return; 
          }
            try {

              rs_.moveToInsertRow ();
                rs_.updateString ("C_VARCHAR_50", "Koala");
                rs_.insertRow ();
                String v = (String) JDReflectionUtil.callMethod_OS(rs_, "getNString","C_VARCHAR_50");
                assertCondition (v.equals ("Koala"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getNString() - Should throw an exception on a deleted row.
**/
    public void Var015()
    {
        if (checkJdbc40 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) { 
            notApplicable("JCC doesn't throw exception for get on deleted row");
            return; 
          }
            try { 
                JDRSTest.position (rs_, "DUMMROW_GNSTRING");
                rs_.deleteRow ();
                String v = (String) JDReflectionUtil.callMethod_OS(rs_, "getNString", "C_VARCHAR_50");
                failed ("Didn't throw SQLException but returned"+v);
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
getNString() - Should return null when the column is NULL.
**/
    public void Var016 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {

        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_NULL");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_CHAR_50");
            assertCondition (s == null);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a SMALLINT.
**/
    public void Var017 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_SMALLINT");
            assertCondition (s.equals ("198"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a INTEGER.
**/
    public void Var018 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_INTEGER");
            assertCondition (s.equals ("-98765"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a REAL, with decimal separator == '.'.
**/
    public void Var019 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
        try {
            reconnect ("decimal separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_REAL");
            assertCondition (s.equals ("4.4"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a FLOAT, with decimal separator == '.'.
**/
    public void Var020 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
        try {
            reconnect ("decimal separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_FLOAT");
            assertCondition (s.equals ("-5.55"), "got "+s+" sb -5.55");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a DOUBLE, with decimal separator == '.'.
**/
    public void Var021 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
        try {
            reconnect ("decimal separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DOUBLE");
            assertCondition (s.equals ("6.666"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a DECIMAL, with decimal separator == '.'.
**/
    public void Var022 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
        try {
            reconnect ("decimal separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DECIMAL_50");
            assertCondition (s.equals ("-7"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a NUMERIC, with decimal separator == '.'.
**/
    public void Var023 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
        try {
            reconnect ("decimal separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_NUMERIC_105");
            assertCondition (s.equals ("10.10105"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a REAL, with decimal separator == ','.
**/
    public void Var024 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
       try {
            reconnect ("decimal separator=,");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_REAL");
            assertCondition (s.equals ("-4,4"), "got "+s+" sb -4.4");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a FLOAT, with decimal separator == ','.
**/
    public void Var025 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
        try {
            reconnect ("decimal separator=,");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_FLOAT");
            assertCondition (s.equals ("5,55"), "got "+s+" sb 5,55");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a DOUBLE, with decimal separator == ','.
**/
    public void Var026 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
        try {
            reconnect ("decimal separator=,");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DOUBLE");
            assertCondition (s.equals ("-6,666"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a DECIMAL, with decimal separator == ','.
**/
    public void Var027 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
        try {
            reconnect ("decimal separator=,");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DECIMAL_105");
            assertCondition (s.equals ("8,88880"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a NUMERIC, with decimal separator == ','.
**/
    public void Var028 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
        try {
            reconnect ("decimal separator=,");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_NUMERIC_50");
            assertCondition (s.equals ("-9"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a CHAR.
**/
    public void Var029 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_CHAR_50");
            assertCondition (s.equals ("Toolbox for Java                                  "));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a VARCHAR.
**/
    public void Var030 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_VARCHAR_50");
            assertCondition (s.equals ("Java Toolbox"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a CLOB, when the CLOB data is returned in
the result set.
**/
    public void Var031 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
        if (checkJdbc40 ()) {
            try {
                reconnect ("lob threshold=1048577");      //@E2C previously 30000
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_CLOB");
                assertCondition (s.equals (JDRSTest.CLOB_FULL), "got \""+s+"\" sb \""+JDRSTest.CLOB_FULL+"\"");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getNString() - Get from a CLOB, when the CLOB locator is returned in
the result set.
**/
    public void Var032 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
        if (checkJdbc40 ()) {
            try {
                reconnect ("lob threshold=0");
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_CLOB");
                assertCondition (s.equals (JDRSTest.CLOB_FULL), "got \""+s+"\" sb \""+JDRSTest.CLOB_FULL+"\"");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getNString() - Get from a DBCLOB, when the DBCLOB data is returned in
the result set.
**/
    public void Var033 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
        if (checkJdbc40 ()) {
            try {
                reconnect ("lob threshold=1048577");   //@E2C previously 30000
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DBCLOB");
                assertCondition (s.equals (JDRSTest.DBCLOB_FULL), "got \""+s+"\" sb \""+JDRSTest.DBCLOB_FULL+"\"");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getNString() - Get from a DBCLOB, when the DBCLOB locator is returned in
the result set.
**/
    public void Var034 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
        if (checkJdbc40 ()) {
            try {
                reconnect ("lob threshold=0");
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DBCLOB");
                assertCondition (s.equals (JDRSTest.DBCLOB_FULL), "got \""+s+"\" sb \""+JDRSTest.DBCLOB_FULL+"\"");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getNString() - Get from a BINARY, with translation turned off.
**/
    public void Var035 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
        try {
            String expected = null;

            if (isToolboxDriver())
               expected = "456C6576656E2020202020202020202020202020";
            else
               expected = "Eleven              ";

            reconnect ("translate binary=false");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_BINARY_20");
            assertCondition (s.equals (expected));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a VARBINARY, with translation turned off.
**/
    public void Var036 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) {
        try {
            String expected = null;

            if (isToolboxDriver())
               expected = "5477656C7665";
            else
               expected = "Twelve";

            reconnect ("translate binary=false");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_VARBINARY_20");
            assertCondition (s.equals (expected));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getNString() - Get from a BLOB, with translation turned off, when the
BLOB data is returned in the result set.
**/
    public void Var037 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
        if (checkJdbc40 ()) {
            try {
                reconnect ("translate binary=false;lob threshold=30000");
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_MEDIUM");
                String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_BLOB");

                // A String comparison does not really make sense here,
                // so we just check to see if it is not null.
                // assertCondition (s.equals (new String (JDRSTest.BLOB_MEDIUM)));
                assertCondition(s != null);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getNString() - Get from a BINARY, with translation turned on.
**/
    public void Var038 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("translate binary=true");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_BINARY_20");
            assertCondition (s.equals ("Thirteen            "));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a VARBINARY, with translation turned on.
**/
    public void Var039 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("translate binary=true");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_VARBINARY_20");
            assertCondition (s.equals ("Fourteen"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a BLOB, with translation turned on,
when the BLOB locator is returned in the result set.
**/
    public void Var040 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
        if (checkJdbc40 ()) {
            try {
                reconnect ("translate binary=true;lob threshold=0");
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_BLOB");

                // A String comparison does not really make sense here,
                // so we just check to see if it is not null.
                // assertCondition (s.equals (new String (JDRSTest.BLOB_FULL)));
                assertCondition(s != null);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getNString() - Get from a DATE, in "iso" format.
**/
    public void Var041 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("date format=iso");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DATE");
            assertCondition (s.equals ("1998-04-08"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a DATE, in "usa" format.
**/
    public void Var042 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("date format=usa");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DATE");
            assertCondition (s.equals ("04/08/1998"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a DATE, in "eur" format.
**/
    public void Var043 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("date format=eur");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DATE");
            assertCondition (s.equals ("08.04.1998"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a DATE, in "jis" format.
**/
    public void Var044 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("date format=jis");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DATE");
            assertCondition (s.equals ("1998-04-08"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a DATE, in "julian" format, "/"
separator, and 1998 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var045 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("date format=julian;date separator=/");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DATE");
            assertCondition (s.equals ("98/098"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a DATE, in "julian" format, "-"
separator, and 2000 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var046 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("date format=julian;date separator=-");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DATE");
            assertCondition (s.equals ("00-052"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a DATE, in "mdy" format, "."
separator, and 1998 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var047 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("date format=mdy;date separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DATE");
            assertCondition (s.equals ("04.08.98"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a DATE, in "mdy" format, ","
separator, and 2000 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var048 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("date format=mdy;date separator=,");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DATE");
            assertCondition (s.equals ("02,21,00"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a DATE, in "dmy" format, " "
separator, and 1998 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var049 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("date format=dmy;date separator=b");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DATE");
            assertCondition (s.equals ("08 04 98"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a DATE, in "dmy" format, "/"
separator, and 2000 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var050 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("date format=dmy;date separator=/");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DATE");
            assertCondition (s.equals ("21/02/00"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a DATE, in "ymd" format, "-"
separator, and 1998 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var051 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("date format=ymd;date separator=-");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DATE");
            assertCondition (s.equals ("98-04-08"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a DATE, in "ymd" format, "."
separator, and 2000 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var052 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("date format=ymd;date separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DATE");
            assertCondition (s.equals ("00.02.21"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a TIME, in "iso" format.
**/
    public void Var053 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("time format=iso");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_TIME");
            assertCondition (s.equals ("08.14.03"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a TIME, in "usa" format, with an AM time.
**/
    public void Var054 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("time format=usa");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_TIME");
            assertCondition (s.equals ("08:14 AM"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a TIME, in "usa" format, with an PM time.
**/
    public void Var055 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("time format=usa");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_TIME");
            assertCondition (s.equals ("02:04 PM"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a TIME, in "eur" format.
**/
    public void Var056 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("time format=eur");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_TIME");
            assertCondition (s.equals ("08.14.03"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a TIME, in "jis" format.
**/
    public void Var057 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("time format=jis");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_TIME");
            assertCondition (s.equals ("08:14:03"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a TIME, in "hms" format with ":" separator.
**/
    public void Var058 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("time format=hms;time separator=:");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_TIME");
            assertCondition (s.equals ("08:14:03"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a TIME, in "hms" format with "." separator.
**/
    public void Var059 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("time format=hms;time separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_TIME");
            assertCondition (s.equals ("08.14.03"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a TIME, in "hms" format with "," separator.
**/
    public void Var060 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("time format=hms;time separator=,");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_TIME");
            assertCondition (s.equals ("08,14,03"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a TIME, in "hms" format with " " separator.
**/
    public void Var061 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            reconnect ("time format=hms;time separator=b");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_TIME");
            assertCondition (s.equals ("08 14 03"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a TIMESTAMP.
**/
    public void Var062 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_TIMESTAMP");

            assertCondition (s.equals ("1998-11-18 03:13:42.987654"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get from a DATALINK.
**/
    public void Var063 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkDatalinkSupport()) { 
        if (checkJdbc40 ()) {
          try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GETDL);
            JDRSTest.position0 (rs, "LOB_FULL");
            String str = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DATALINK");
            assertCondition (str.equalsIgnoreCase("https://github.com/IBM/JTOpen-test/blob/main/README.testing.txt"), "Failed to obtain datalink str="+str);
          } catch (Exception e) {
            failed (e, "Unexpected Exception");
          }
        }
      }
    }



/**
getNString() - Get from a DISTINCT.
**/
    public void Var064 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
        if (checkJdbc40 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_DISTINCT");
                assertCondition (s.equals ("123456789"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



// @D0A
/**
getNString() - Get from a BIGINT.
**/
    public void Var065 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
        if (checkJdbc40()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "NUMBER_NEG");
                String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_BIGINT");
                assertCondition (s.equals ("-44332123"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getNString() - Verify that no DataTruncation is posted
when the max field size is set to 0.
**/
    public void Var066()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if (checkJdbc40 ()) 
        try {
            Statement s = connection_.createStatement ();
            s.setMaxFieldSize (0);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                                           + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            String s1 = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_VARCHAR_50");
            DataTruncation dt = (DataTruncation) rs.getWarnings ();
            rs.close ();
            s.close ();
            assertCondition ((s1.equals ("Java Toolbox"))
                    && (dt == null));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Verify that data is truncated without
a DataTruncation posted when the max field size is set
to a value shorter than the string.
**/
    public void Var067()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
                  if (checkJdbc40 ()) 
 try {
            Statement s = connection_.createStatement ();
            s.setMaxFieldSize (11);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                                           + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            int columnIndex = rs.findColumn ("C_VARCHAR_50");
            String s1 = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_VARCHAR_50");
            SQLWarning w = rs.getWarnings ();
            rs.close ();
            s.close ();
            assertCondition ((s1.equals ("Java Toolbo")) && (w == null), 
            			"s1="+s1+" sb 'Java Toolbo' w="+w+"sb null columnIndex="+columnIndex);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Get strings from a table that has columns
with different CCSIDs.
**/
  public void Var068() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC does not support getNString");
      return;
    }
    if (checkJdbc40()) {
      StringBuffer errorBuffer = new StringBuffer(); 
      try {
        initTable(statement0_, JDRSTest.COLLECTION + ".CCSIDFUN68 ",
            "(COL1 VARCHAR(20) CCSID 37, " + " COL2 VARCHAR(20) CCSID "
                + systemObject_.getCcsid() + ", "
                + " COL3 VARCHAR(20) CCSID 937)", errorBuffer);

           statement0_.executeUpdate ("INSERT INTO "
                                       + JDRSTest.COLLECTION + ".CCSIDFUN68 "
                                       + "(COL1, COL2, COL3) "
                                       + "VALUES ('Clifton', 'Malcolm', 'Nock')");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.COLLECTION + ".CCSIDFUN68");
            rs.next ();
            String col1 = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
            String col2 = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 2);
            String col3 = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 3);
            rs.close ();
            assertCondition ((col1.equals ("Clifton"))
                    && (col2.equals ("Malcolm"))
                    && (col3.equals ("Nock")));
        } catch (Exception e) {
            failed (e, "Unexpected Exception -- " + errorBuffer.toString());
            
        } finally {
            
              cleanupTable(statement0_,  JDRSTest.COLLECTION + ".CCSIDFUN68 ");
              
           
            
        }
    }
  }


/**
getNString() - Verify that we can get the user correctly when the column is
 the SQL Special value USER.

SQL400 - This wasn't working in an SQLJ test so we added the testcase here.
What we are really testing is that database converts literal constants to
unicode correctly.
**/
    public void Var069()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
        String userName = null;

                  if (checkJdbc40 ()) 
 try {
            Statement s = connection_.createStatement ();

            errorBuffer.setLength(0); 
            try {
                initTable(s,  table_ , " (ONE CHAR (10 ) NOT NULL WITH DEFAULT)", errorBuffer);
                s.executeUpdate("INSERT INTO " + table_ + " VALUES('one')");
                ResultSet rs = s.executeQuery("SELECT USER FROM " + table_);
                rs.next();
                userName = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
                rs.close();

            } catch (SQLException two) {
                failed (two, "Unexpected Exception -- errorBuffer = "+errorBuffer.toString());
                return; 
            }

            cleanupTable(s, table_);
            s.close();

            //output_.println(userName);
            if (userName == null) userName="nullUserName"; 
            assertCondition(userName.equals(systemObject_.getUserId ()));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getNString() - Verify that we can get a string literal in place of a column
correctly.

SQL400 - This wasn't working in an SQLJ test so we added the testcase here.
What we are really testing is that database converts literal constants to
unicode correctly.
**/
    public void Var070()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
        String literal = null;
        String expectedLiteral = "THE BLUE ZOO";

                  if (checkJdbc40 ()) 
 try {
            Statement s = connection_.createStatement ();
            errorBuffer.setLength(0); 
            try {
              initTable(s,  table_ , " (ONE CHAR (10 ) NOT NULL WITH DEFAULT)", errorBuffer);
                s.executeUpdate("INSERT INTO " + table_ + " VALUES('one')");
                ResultSet rs = s.executeQuery("SELECT '" + expectedLiteral + "' FROM " + table_);
                rs.next();
                literal = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
                rs.close();

            } catch (SQLException two) {
                failed (two, "Unexpected Exception errorBuffer="+errorBuffer.toString());
                return; 
            }

            cleanupTable(s, table_);
            s.close();

            //output_.println(literal);
            if (literal == null) literal = "null"; 
            assertCondition(literal.equals(expectedLiteral));

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    String string17000000 = null;
    String string4250000 = null;

    public String getNString17000000() {
	if (string17000000 == null) {
	    String piece = getNString4250000(); 
	    StringBuffer sb = new StringBuffer(17000000);
	    sb.append(piece);
	    sb.append(piece);
	    sb.append(piece);
	    sb.append(piece);
	    string17000000=sb.toString(); 
	}
	return string17000000; 
    }

    public String getNString4250000() {
	if (string4250000== null) { 
	    StringBuffer sb = new StringBuffer( 4300000);
	    for (int i = 0; i < (4250000); i++) {
		char x = (char) ((int)'A'+(i%26)); 
		sb.append(x); 
	    }
	    string4250000 = sb.toString();
	}
	return string4250000; 
    } 

/**
getNString() - Get from a 17 Meg CLOB, when the CLOB data is returned in
the result set.
**/
    public void Var071() {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
		if (checkJdbc40()) {
			// 
			// Using a non-locator CLOB where the size > maximum string size
			// is not valid in J9
			// Check for HY090 exception
		    //also in TB, we get outofmem error and then it hangs the tc script
                    // For class in V6R1 we also see the same problem 
		    if(runningJ9 && isToolboxDriver() ||
		       (!runningJ9 &&  isToolboxDriver()  && false ))
		    {
		        notApplicable("TOOLBOX_FIX_NEEDED:  sometimes gets outofmem and hangs tc script");
		        return;
		    }
		        Statement stmt = null; 
		        errorBuffer.setLength(0); 
			try {
			    stmt =   connection_.createStatement();
				String piece = getNString4250000();
				String expected = getNString17000000();

			
				initTable(stmt, clobTable_, " (ONE CLOB(17000000) NOT NULL WITH DEFAULT)",errorBuffer);
				initTable(stmt, clobTable4_, "(C1 CLOB(4250000) NOT NULL WITH DEFAULT)", errorBuffer);

				PreparedStatement pstmt = connection_
						.prepareStatement("INSERT INTO " + clobTable4_
								+ " values(?)");
				pstmt.setString(1, piece);
				pstmt.executeUpdate();
				pstmt.close();

				stmt.executeUpdate("INSERT INTO " + clobTable_
						+ " SELECT C1 || C1 || C1 || C1 FROM " + clobTable4_);

				stmt.close();

				reconnect("lob threshold=18000000");

				ResultSet rs = statement0_.executeQuery("SELECT * FROM "
						+ clobTable_);
				rs.next();
				String s = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
				assertCondition(s.equals(expected),
							"compare failed -- Get 17 Meg Clob inline added 10/12/2004 by native driver ");
			} catch (Exception e) {
				if (runningJ9) {
					if (e instanceof SQLException) {
						SQLException sqlEx = (SQLException) e;
						if (sqlEx.getSQLState().equals("HY090")) {
							assertCondition(true);
						} else {
							failed(
									e,
									"Unexpected Exception Expect HY090  -- Get 17 Meg Clob inline modified 11/12/2005 by native driver "+errorBuffer.toString());
						
						}

					} else {
						failed(
								e,
								"Unexpected Exception -- Get 17 Meg Clob inline modified 11/12/2005 by native driver "+errorBuffer.toString());
					}
				} else {
					failed(
							e,
							"Unexpected Exception -- Get 17 Meg Clob inline added 10/12/2004 by native driver "+errorBuffer.toString());
				}
			}


			// Make sure the tables are gone
			try {
			    stmt =   connection_.createStatement();
			      if (stmt != null)  {
			        cleanupTable(stmt,  clobTable_);
				  stmt.close(); 
			      }
			} catch (SQLException one) {
			    output_.println("Warning, unable to drop the table ");
			    one.printStackTrace(output_); 
			}
			try {
			    stmt =   connection_.createStatement();
			      if (stmt != null)  {
				  cleanupTable(stmt, clobTable4_);
				  stmt.close(); 
			      }
			} catch (SQLException one) {
					// Ignore it...
			}

		}
	}

/**
getNString() - Get from a CLOB, when the CLOB locator is returned in
the result set.
**/
    public void Var072 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }
      if(runningJ9 && isToolboxDriver())
	    {//causes hangs and leaves file locks
	        notApplicable("sometimes gets hangs and file locks tc script");
	        return;
	    }

	String initials = "";
	int l = JDRSTest.COLLECTION.length();
	if (l > 5) {
	    initials =  JDRSTest.COLLECTION.substring(l - 5); 
	}
	output_.println("initials are "+initials); 
	if (initials.equals("614CU") ||
	    initials.equals("615CU") ||
	    initials.equals("616CU")    ) {
	    notApplicable("Fails with out of memory error for "+initials);
	    return; 
	}

      
        if (checkJdbc40 ()) {
	    Statement stmt = null; 
      errorBuffer.setLength(0);
      try {

        connection_.commit();
        reconnect("lob threshold=0");

        stmt = connection_.createStatement();

        String piece = getNString4250000();
        String expected = getNString17000000();

        initTable(stmt, clobTable_,
            " (ONE CLOB(17000000) NOT NULL WITH DEFAULT)", errorBuffer);
        initTable(stmt, clobTable4_,
            " (C1 CLOB(4250000) NOT NULL WITH DEFAULT)", errorBuffer);

        PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO "
            + clobTable4_ + " values(?)");
        pstmt.setString(1, piece);
        pstmt.executeUpdate();
        pstmt.close();

        stmt.executeUpdate("INSERT INTO " + clobTable_
            + " SELECT C1 || C1 || C1 || C1 FROM " + clobTable4_);

        stmt.close();

        reconnect("lob threshold=0");

        ResultSet rs = statement0_.executeQuery("SELECT * FROM " + clobTable_);
        rs.next();
        String s = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
        assertCondition(s.equals(expected),
            "compare failed -- Get 17 Meg Clob locator added 10/12/2004 by native driver ");

      } catch (Exception e) {
        if (runningJ9) {
          if (e instanceof SQLException) {
            SQLException sqlEx = (SQLException) e;
            if (sqlEx.getSQLState().equals("HY090")) {
              assertCondition(true);
            } else {
              failed(
                  e,
                  "Unexpected Exception Expect HY090  -- Get 17 Meg Clob inline modified 11/12/2005 by native driver "+errorBuffer.toString());

            }

					} else {
						failed(
								e,
								"Unexpected Exception -- Get 17 Meg Clob inline modified 11/12/2005 by native driver "+errorBuffer.toString());
					}
     		
            	} else {
                failed (e, "Unexpected Exception  -- Get 17 Meg Clob locator added 10/12/2004 by native driver"+errorBuffer.toString());
            	}
	    }
			// Make sure the tables are gone
	    try {
		stmt =   connection_.createStatement();
		  if (stmt != null) {
		      cleanupTable(stmt, clobTable_);
		      stmt.close();
		  }
	    } catch (SQLException one) {
			    output_.println("Warning, unable to drop the table ");
			    one.printStackTrace(output_); 


	    }
	    try {
		stmt =   connection_.createStatement();
		  if (stmt != null)  {
		      cleanupTable(stmt, clobTable4_);
		      stmt.close(); 
		  } 
	    } catch (SQLException one) {
					// Ignore it...
	    }


	}
    }


    
    /**
     getNString() - Get from DFP16 -- Use preset values
     **/
    public void Var073 ()
            {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support getNString"); 
        return; 
      }

                if (  checkJdbc40 () && checkDecFloatSupport ()) {
                    boolean success = true; 
                    StringBuffer sb = new StringBuffer();

                    try {
                        ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                                 + JDRSTest.RSTEST_DFP16);
                        int i = 0; 
                        while (rs.next()) { 
                            String v = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
                            String expected = JDRSTest.VALUES_DFP16[i];
                            if (expected == null) {
                                if (v != null) {
                                    success = false;
                                    sb.append("expected "+expected+" but got "+v); 
                                } 
                            } else {
                                    if (expected.equals(v)) {
                                    } else {
					if ("1.0".equals(expected) && "1".equals(v)) {
					    /* ok */
					} else { 
					    sb.append("expected "+expected+" but got "+v);
					    success = false;
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
        getNString() - Get from DFP34 -- Use preset values
        **/
            public void Var074 ()
            {
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                notApplicable("JCC does not support getNString"); 
                return; 
              }
                if (checkJdbc40() && checkDecFloatSupport ()) {
                    boolean success = true; 
                    StringBuffer sb = new StringBuffer();

                    try {
                        ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                                 + JDRSTest.RSTEST_DFP34);
                        int i = 0; 
                        while (rs.next()) { 
                             
                            String v = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
                            String expected = JDRSTest.VALUES_DFP34[i];
                            if (expected == null) {
                                if (v != null) {
                                    success = false;
                                    sb.append("expected "+expected+" but got "+v); 
                                } 
                            } else {
                                    if (expected.equals(v)) {
                                    } else {
					if ("1.0".equals(expected) && "1".equals(v)) {
					    /* ok */
					} else { 

					    sb.append("expected "+expected+" but got "+v);
					    success = false;
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
        getNString() - Get from DFP16: NAN  
        **/
            public void Var075 ()
            {
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                notApplicable("JCC does not support getNString"); 
                return; 
              }
                if (checkJdbc40() && checkDecFloatSupport()) {
                    try {
                        Statement s = connection_.createStatement ();
                        ResultSet rs = s.executeQuery ("SELECT * FROM "
                                                       + JDRSTest.RSTEST_DFP16NAN);
                        rs.next(); 
                       
                        String v = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
                        assertCondition(v.equals("NaN"), "Expected NaN got "+v); 
                    }
                    catch (Exception e) {
                        failed(e, "Unexpected Exception");
                    }
                }
            }


        /**
        getNString() - Get from DFP16: -NAN  
        **/
            public void Var076 ()
            {
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                notApplicable("JCC does not support getNString"); 
                return; 
              }
                if (checkJdbc40() && checkDecFloatSupport()) {
                    try {
                        Statement s = connection_.createStatement ();
                        ResultSet rs = s.executeQuery ("SELECT * FROM "
                                                       + JDRSTest.RSTEST_DFP16NNAN);
                        rs.next(); 
        
                        String v = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
                        assertCondition(v.equals("-NaN"), "Expected -NaN got "+v); 
                    }
                    catch (Exception e) {
                        failed(e, "Unexpected Exception");
                    }
                }
            }


        /**
        getNString() - Get from DFP16: INF  
        **/
            public void Var077 ()
            {
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                notApplicable("JCC does not support getNString"); 
                return; 
              }
                if (checkJdbc40() && checkDecFloatSupport()) {
                    try {
                        Statement s = connection_.createStatement ();
                        ResultSet rs = s.executeQuery ("SELECT * FROM "
                                                       + JDRSTest.RSTEST_DFP16INF);
                        rs.next(); 
                      
                        String v = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
                        assertCondition(v.equals("Infinity"), "Expected Infinity got "+v);
                    }
                    catch (Exception e) {
                        failed(e, "Unexpected Exception");
                    }
                }
            }


        /**
        getNString() - Get from DFP16: -INF  
        **/
            public void Var078 ()
            {
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                notApplicable("JCC does not support getNString"); 
                return; 
              }
                if (checkJdbc40() && checkDecFloatSupport()) {
                    try {
                        Statement s = connection_.createStatement ();
                        ResultSet rs = s.executeQuery ("SELECT * FROM "
                                                       + JDRSTest.RSTEST_DFP16NINF);
                        rs.next(); 
                       
                        String v = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
                        assertCondition(v.equals("-Infinity"), "Expected -Infinity got "+v); 
                    }
                    catch (Exception e) {
                        failed(e, "Unexpected Exception");
                    }
                }
            }


        /**
        getNString() - Get from DFP34: NAN  
        **/
            public void Var079 ()
            {
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                notApplicable("JCC does not support getNString"); 
                return; 
              }
                if (checkJdbc40() && checkDecFloatSupport()) {
                    try {
                        Statement s = connection_.createStatement ();
                        ResultSet rs = s.executeQuery ("SELECT * FROM "
                                                       + JDRSTest.RSTEST_DFP34NAN);
                        rs.next(); 
                      
                        String v = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
                        assertCondition(v.equals("NaN"), "Expected NaN got "+v); 
                    }
                    catch (Exception e) {
                        failed(e, "Unexpected Exception");
                    }
                }
            }


        /**
        getNString() - Get from DFP34: -NAN  
        **/
            public void Var080 ()
            {
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                notApplicable("JCC does not support getNString"); 
                return; 
              }
                if (checkJdbc40() && checkDecFloatSupport()) {
                    try {
                        Statement s = connection_.createStatement ();
                        ResultSet rs = s.executeQuery ("SELECT * FROM "
                                                       + JDRSTest.RSTEST_DFP34NNAN);
                        rs.next(); 
                       
                        String v = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
                        assertCondition(v.equals("-NaN"), "Expected -NaN got "+v); 
                    }
                    catch (Exception e) {
                        failed(e, "Unexpected Exception");
                    }
                }
            }


        /**
        getNString() - Get from DFP34: INF  
        **/
            public void Var081 ()
            {
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                notApplicable("JCC does not support getNString"); 
                return; 
              }
                if (checkJdbc40() && checkDecFloatSupport()) {
                    try {
                        Statement s = connection_.createStatement ();
                        ResultSet rs = s.executeQuery ("SELECT * FROM "
                                                       + JDRSTest.RSTEST_DFP34INF);
                        rs.next(); 
                      
                        String v = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);

                        assertCondition(v.equals("Infinity") , "Expected Infinity got "+v); 
                    }
                    catch (Exception e) {
                        failed(e, "Unexpected Exception");
                    }
                }
            }


        /**
        getNString() - Get from DFP34: -INF  
        **/
            public void Var082 ()
            {
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                notApplicable("JCC does not support getNString"); 
                return; 
              }
                if (checkJdbc40() && checkDecFloatSupport()) {
                    try {
                        Statement s = connection_.createStatement ();
                        ResultSet rs = s.executeQuery ("SELECT * FROM "
                                                       + JDRSTest.RSTEST_DFP34NINF);
                        rs.next(); 

                        String v = (String) JDReflectionUtil.callMethod_O(rs, "getNString", 1);
                        assertCondition(v.equals("-Infinity"), "Expected -Infinity got "+v); 
                    }
                    catch (Exception e) {
                        failed(e, "Unexpected Exception");
                    }
                }
            }


            /**
            getNString() - Get from SQLXML
            **/
                public void Var083 ()
		{
		    if (getDriver() == JDTestDriver.DRIVER_JCC) {
			notApplicable("JCC does not support getNString"); 
			return; 
		    }
		    if (checkXmlSupport()) { 
			if (checkJdbc40 ()) {
			    String sql = "SELECT * FROM " + JDRSTest.RSTEST_GETXML+" A ORDER BY RRN(A)"; 
			    try {
				
				ResultSet rs = statement0_.executeQuery (sql);
				rs.next(); 
				String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_XML");
				String expected = JDRSTest.SAMPLE_XML1_OUTXML; 
				assertCondition (s.equals (expected), "got \""+s+"\" sb \""+expected+"\"");
			    } catch (Exception e) {
				failed (e, "Unexpected Exception");
			    }
			}
		    }
		}


        /**
         * getNString() - Get from a TIMESTAMP(12).
         **/
        public void Var084() {
            if (checkTimestamp12Support()) {
              testGet(statement0_,
                  "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
                  "getNString", 
                  "1998-11-18 03:13:42.987654000000",
                  " -- added 11/19/2012"); 

            }
        }


        /**
         * getNString() - Get from a TIMESTAMP(0).
         **/
        public void Var085() {
            if (checkTimestamp12Support()) {
          testGet(statement0_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
            "getNString",
            "1998-11-18 03:13:42",
            " -- added 11/19/2012"); 
            
            
            }
        }





  /**
   * getString() - Get from a BOOLEAN.
   **/
  public void Var086() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_FALSE");
	String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_BOOLEAN");
        assertCondition(JDRSTest.BOOLEAN_FALSE_STRING.equals(s), JDRSTest.BOOLEAN_FALSE_STRING+" != " + s);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getString() - Get from a BOOLEAN.
   **/
  public void Var087() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_TRUE");

	String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_BOOLEAN");
        assertCondition(JDRSTest.BOOLEAN_TRUE_STRING.equals(s), JDRSTest.BOOLEAN_TRUE_STRING+" != " + s);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getString() - Get from a BOOLEAN.
   **/
  public void Var088() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_NULL");
	String s = (String) JDReflectionUtil.callMethod_OS(rs, "getNString", "C_BOOLEAN");
        assertCondition(s == null);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }






}



