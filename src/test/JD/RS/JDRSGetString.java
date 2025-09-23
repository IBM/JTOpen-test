///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetString.java
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
import test.JD.JDTestUtilities;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDRSGetString.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getString()
</ul>
**/
public class JDRSGetString
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetString";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private String              properties_;
    private Statement           statement_;
    private String              statementQuery_;
    private Statement           statement0_;
    private ResultSet           rs_;
    private static  String table_  = JDRSTest.COLLECTION + ".JDRS1";
    private static  String clobTable4_ = JDRSTest.COLLECTION + ".JDRSGSCL42";
    private static  String clobTable_ = JDRSTest.COLLECTION + ".JDRSGSCL17";

	boolean runningJ9 = false;
	int driver_ = 0;


/**
Constructor.
**/
    public JDRSGetString (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDRSGetString",
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
	driver_ = getDriver();
        properties_ = "X";
        reconnect ("");
        table_  = JDRSTest.COLLECTION + ".JDRS1";
	clobTable_ =  JDRSTest.COLLECTION + ".JDRSGSCL17";
	clobTable4_ = JDRSTest.COLLECTION + ".JDRSGSCL42";

	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
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

    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        if (isJdbc20 ()) {
            if (rs_ != null)  {
                rs_.close ();
		rs_ = null; 
            }
            if (statement_ != null) {
              statement_.close ();
	      statement_ = null; 
            }
        }
        statement0_.close ();
	statement0_ = null; 
        connection_.commit(); // @E1A
        connection_.close ();
	connection_ = null; 
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
                         
                          ;
            if (getDriver() != JDTestDriver.DRIVER_JCC) {
              url += ";" + properties_;
            }
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
		    statement_ = connection_.createStatement ();

		} else {
		    statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							      ResultSet.CONCUR_UPDATABLE);
		}
		statementQuery_ = "INSERT INTO " + JDRSTest.RSTEST_GET
                                          + " (C_KEY) VALUES ('DUMMY_ROW')";
                statement_.executeUpdate (statementQuery_);
		statementQuery_ = "SELECT * FROM " + JDRSTest.RSTEST_GET + " FOR UPDATE";
                rs_ = statement_.executeQuery (statementQuery_);
            }
        }
    }



/**
getString() - Should throw exception when the result set is
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
            rs.getString (1);
            failed ("Didn't throw SQLException");
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getString() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            String s = rs.getString (1);
            failed ("Didn't throw SQLException but returned "+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getString() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            rs.next ();
            String s = rs.getString (100);
            failed ("Didn't throw SQLException but returned "+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getString() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString (0);
            failed ("Didn't throw SQLException but returned "+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getString() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString (-1);
            failed ("Didn't throw SQLException but returned "+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getString() - Should work when the column index is valid.
**/
    public void Var006()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString (1);
            assertCondition (s.equals ("DATE_1998"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Should throw an exception when the column
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
          JDRSTest.position0 (rs, "DATE_1998");
          String s = rs.getString (null);
          failed ("Didn't throw SQLException but returned "+s);
        } catch (Exception e) {
          assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }


/**
getString() - Should throw an exception when the column
name is an empty string.
**/
    public void Var008()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString ("");
            failed ("Didn't throw SQLException but returned"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getString() - Should throw an exception when the column
name is invalid.
**/
    public void Var009()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString ("INVALID");
            failed ("Didn't throw SQLException but returned"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getString() - Should work when the column name is valid.
**/
    public void Var010()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString ("C_KEY");
            assertCondition (s.equals ("DATE_1998"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Should work when an update is pending.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
            try {

                rs_ = JDRSTest.position (driver_, statement_, statementQuery_, rs_, "UPDATE_SANDBOX");
                rs_.updateString ("C_VARCHAR_50", "Ninja");
                String v = rs_.getString ("C_VARCHAR_50");
                assertCondition (v.equals ("Ninja"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getString() - Should work when an update has been done.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            try {
                rs_ = JDRSTest.position (driver_, statement_, statementQuery_, rs_, "UPDATE_SANDBOX");
                rs_.updateString ("C_VARCHAR_50", "Money");
                rs_.updateRow ();
                String v = rs_.getString ("C_VARCHAR_50");
                assertCondition (v.equals ("Money"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getString() - Should work when the current row is the insert
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
                rs_.updateString ("C_VARCHAR_50", "Slipped");
                String v = rs_.getString ("C_VARCHAR_50");
                assertCondition (v.equals ("Slipped"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getString() - Should work when the current row is the insert
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
                rs_.updateString ("C_VARCHAR_50", "Koala");
                rs_.insertRow ();
                String v = rs_.getString ("C_VARCHAR_50");
                assertCondition (v.equals ("Koala"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getString() - Should throw an exception on a deleted row.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC doesn't throw exception for get on deleted row");
            return;
          }
            try {
                rs_ = JDRSTest.position (driver_, statement_, statementQuery_, rs_, "DUMMY_ROW");
                rs_.deleteRow ();
                String v = rs_.getString ("C_VARCHAR_50");
                failed ("Didn't throw SQLException but returned"+v);
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
getString() - Should return null when the column is NULL.
**/
    public void Var016 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_NULL");
            String s = rs.getString ("C_CHAR_50");
            assertCondition (s == null, "Got "+s+" expected null");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a SMALLINT.
**/
    public void Var017 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            String s = rs.getString ("C_SMALLINT");
            assertCondition (s.equals ("198"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a INTEGER.
**/
    public void Var018 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            String s = rs.getString ("C_INTEGER");
            assertCondition (s.equals ("-98765"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a REAL, with decimal separator == '.'.
**/
    public void Var019 ()
    {
        try {
            reconnect ("decimal separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            String s = rs.getString ("C_REAL");
            String expected;
            expected = "4.4";
            // Hmm.. on 1/29/2009 running Windows to LUW this returneds 4.4
            // if (getDriver() == JDTestDriver.DRIVER_JCC) {
            //  expected = "4.3999996";
            //}
            assertCondition (s.equals (expected), "s="+s+" sb "+expected);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a FLOAT, with decimal separator == '.'.
**/
    public void Var020 ()
    {
        try {
            reconnect ("decimal separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            String s = rs.getString ("C_FLOAT");
            assertCondition (s.equals ("-5.55"), "got "+s+" sb -5.55");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a DOUBLE, with decimal separator == '.'.
**/
    public void Var021 ()
    {
        try {
            reconnect ("decimal separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            String s = rs.getString ("C_DOUBLE");
            String expected = "6.666";
            // Hmm.. on 1/29/2009 running Windows to LUW this returneds 6.666
            // if (getDriver() == JDTestDriver.DRIVER_JCC) {
            //  expected = "6.6659999999999995";
            //}

            assertCondition (s.equals (expected), "s="+s+" sb "+expected);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a DECIMAL, with decimal separator == '.'.
**/
    public void Var022 ()
    {
        try {
            reconnect ("decimal separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            String s = rs.getString ("C_DECIMAL_50");
	    assertCondition (s.equals ("-7"), "Got "+s+" sb -7");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a NUMERIC, with decimal separator == '.'.
**/
    public void Var023 ()
    {
        try {
            reconnect ("decimal separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            String s = rs.getString ("C_NUMERIC_105");
            assertCondition (s.equals ("10.10105"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a REAL, with decimal separator == ','.
**/
    public void Var024 ()
    {
      if (checkPropertySupport("decimal separator")) {
        try {
          reconnect ("decimal separator=,");
          ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_GET);
          JDRSTest.position0 (rs, "NUMBER_NEG");
          String s = rs.getString ("C_REAL");
          assertCondition (s.equals ("-4,4"), "s="+s+" sb -4,4");
        } catch (Exception e) {
          failed (e, "Unexpected Exception");
        }
      }
    }



/**
getString() - Get from a FLOAT, with decimal separator == ','.
**/
    public void Var025 ()
    {
      if (checkPropertySupport("decimal separator")) {
        try {
            reconnect ("decimal separator=,");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            String s = rs.getString ("C_FLOAT");
            assertCondition (s.equals ("5,55"), "s="+s+" sb 5,55");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


    /**
     getString() - Get from a DOUBLE, with decimal separator == ','.
     **/
    public void Var026 ()
    {
      if (checkPropertySupport("decimal separator")) {
        try {
          reconnect ("decimal separator=,");
          ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_GET);
          JDRSTest.position0 (rs, "NUMBER_NEG");
          String s = rs.getString ("C_DOUBLE");
          assertCondition (s.equals ("-6,666"), "s="+s+" sb -6,666");
        } catch (Exception e) {
          failed (e, "Unexpected Exception");
        }
      }
    }


    /**
     getString() - Get from a DECIMAL, with decimal separator == ','.
     **/
    public void Var027 ()
    {
      if (checkPropertySupport("decimal separator")) {
        try {
          reconnect ("decimal separator=,");
          ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_GET);
          JDRSTest.position0 (rs, "NUMBER_POS");
          String s = rs.getString ("C_DECIMAL_105");
          assertCondition (s.equals ("8,88880"), "s="+s+" sb 8,88880");
        } catch (Exception e) {
          failed (e, "Unexpected Exception");
        }
      }
    }


/**
getString() - Get from a NUMERIC, with decimal separator == ','.
**/
    public void Var028 ()
    {
      if (checkPropertySupport("decimal separator")) {
        try {
            reconnect ("decimal separator=,");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            String s = rs.getString ("C_NUMERIC_50");
            assertCondition (s.equals ("-9"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getString() - Get from a CHAR.
**/
    public void Var029 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            String s = rs.getString ("C_CHAR_50");
            assertCondition (s.equals ("Toolbox for Java                                  "));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a VARCHAR.
**/
    public void Var030 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            String s = rs.getString ("C_VARCHAR_50");
            assertCondition (s.equals ("Java Toolbox"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a CLOB, when the CLOB data is returned in
the result set.
**/
    public void Var031 ()
    {
        if (checkLobSupport ()) {
            try {
                reconnect ("lob threshold=1048577");      //@E2C previously 30000
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                String s = rs.getString ("C_CLOB");
                assertCondition (s.equals (JDRSTest.CLOB_FULL), "got \""+s+"\" sb \""+JDRSTest.CLOB_FULL+"\"");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getString() - Get from a CLOB, when the CLOB locator is returned in
the result set.
**/
    public void Var032 ()
    {
        if (checkLobSupport ()) {
            try {
                reconnect ("lob threshold=0");
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                String s = rs.getString ("C_CLOB");
                assertCondition (s.equals (JDRSTest.CLOB_FULL), "got \""+s+"\" sb \""+JDRSTest.CLOB_FULL+"\"");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getString() - Get from a DBCLOB, when the DBCLOB data is returned in
the result set.
**/
    public void Var033 ()
    {
        if (checkLobSupport ()) {
            try {
                reconnect ("lob threshold=1048577");   //@E2C previously 30000
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                String s = rs.getString ("C_DBCLOB");
                assertCondition (s.equals (JDRSTest.DBCLOB_FULL), "got \""+s+"\" sb \""+JDRSTest.DBCLOB_FULL+"\"");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getString() - Get from a DBCLOB, when the DBCLOB locator is returned in
the result set.
**/
    public void Var034 ()
    {
        if (checkLobSupport ()) {
            try {
                reconnect ("lob threshold=0");
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                String s = rs.getString ("C_DBCLOB");
                assertCondition (s.equals (JDRSTest.DBCLOB_FULL), "got \""+s+"\" sb \""+JDRSTest.DBCLOB_FULL+"\"");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getString() - Get from a BINARY, with translation turned off.
**/
    public void Var035 ()
    {
        try {
            String expected = null;

            if (isToolboxDriver()  ||
		getDriver () == JDTestDriver.DRIVER_JTOPENLITE  ) {
              expected = "456C6576656E2020202020202020202020202020";

            } else if ( getDriver () == JDTestDriver.DRIVER_JCC ) {
              expected = "456c6576656e2020202020202020202020202020";

            } else {
               expected = "Eleven              ";
            }

            reconnect ("translate binary=false");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            String s = rs.getString ("C_BINARY_20");
            assertCondition (s.equals (expected), "s="+s+" sb "+expected);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a VARBINARY, with translation turned off.
**/
    public void Var036 ()
    {
        try {
            String expected = null;

            if (isToolboxDriver()   ||
		getDriver () == JDTestDriver.DRIVER_JTOPENLITE) {
              expected = "5477656C7665";
            } else if (   getDriver () == JDTestDriver.DRIVER_JCC ) {
               expected = "5477656c7665";
            } else {
               expected = "Twelve";
            }
            reconnect ("translate binary=false");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            String s = rs.getString ("C_VARBINARY_20");
            assertCondition (s.equals (expected), "s="+s+" sb "+expected);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a BLOB, with translation turned off, when the
BLOB data is returned in the result set.
**/
    public void Var037 ()
    {
        if (checkLobSupport ()) {
            try {
                reconnect ("translate binary=false;lob threshold=30000");
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_MEDIUM");
                String s = rs.getString ("C_BLOB");

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
getString() - Get from a BINARY, with translation turned on.
**/
    public void Var038 ()
    {

      if (checkPropertySupport("translate binary")) {
        try {
            reconnect ("translate binary=true");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            String s = rs.getString ("C_BINARY_20");
            String expected = "Thirteen            ";
            assertCondition (expected.equals (s), "s="+s+" sb "+expected);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getString() - Get from a VARBINARY, with translation turned on.
**/
    public void Var039 ()
    {
      if (checkPropertySupport("translate binary")) {
        try {
            reconnect ("translate binary=true");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_TRANS");
            String s = rs.getString ("C_VARBINARY_20");
            String expected="Fourteen";
            assertCondition (s.equals (expected),"s="+s+" sb "+expected);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getString() - Get from a BLOB, with translation turned on,
when the BLOB locator is returned in the result set.
**/
    public void Var040 ()
    {
      if (checkPropertySupport("translate binary")) {
        if (checkLobSupport ()) {
            try {
                reconnect ("translate binary=true;lob threshold=0");
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                String s = rs.getString ("C_BLOB");

                // A String comparison does not really make sense here,
                // so we just check to see if it is not null.
                // assertCondition (s.equals (new String (JDRSTest.BLOB_FULL)));
                assertCondition(s != null);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }
    }


/**
getString() - Get from a DATE, in "iso" format.
**/
    public void Var041 ()
    {
        try {
            reconnect ("date format=iso");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString ("C_DATE");
            assertCondition (s.equals ("1998-04-08"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a DATE, in "usa" format.
**/
    public void Var042 ()
    {

        if (checkPropertySupport("date format")) {
          try {
            reconnect ("date format=usa");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString ("C_DATE");
	    String expected = "04/08/1998";
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
          }
        }
    }



/**
getString() - Get from a DATE, in "eur" format.
**/
    public void Var043 ()
    {
      if (checkPropertySupport("date format")) {
        try {
          reconnect ("date format=eur");
          ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_GET);
          JDRSTest.position0 (rs, "DATE_1998");
          String s = rs.getString ("C_DATE");
          String expected ="08.04.1998";
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);

        } catch (Exception e) {
          failed (e, "Unexpected Exception");
        }
      }
    }


/**
getString() - Get from a DATE, in "jis" format.
**/
    public void Var044 ()
    {
        try {
            reconnect ("date format=jis");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString ("C_DATE");
            assertCondition (s.equals ("1998-04-08"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a DATE, in "julian" format, "/"
separator, and 1998 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var045 ()
    {
      if (checkPropertySupport("date format")) {
        try {
          reconnect ("date format=julian;date separator=/");
          ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_GET);
          JDRSTest.position0 (rs, "DATE_1998");
          String s = rs.getString ("C_DATE");
          String expected = "98/098";

	  assertCondition (s.equals (expected),"got "+s+" sb "+expected);

        } catch (Exception e) {
          failed (e, "Unexpected Exception");
        }
      }
    }


/**
getString() - Get from a DATE, in "julian" format, "-"
separator, and 2000 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var046 ()
    {
      if (checkPropertySupport("date format")) {
        try {
            reconnect ("date format=julian;date separator=-");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            String s = rs.getString ("C_DATE");
            String expected = "00-052";
	    
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getString() - Get from a DATE, in "mdy" format, "."
separator, and 1998 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var047 ()
    {
      if (checkPropertySupport("date format")) {
        try {
            reconnect ("date format=mdy;date separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString ("C_DATE");
            String expected = "04.08.98";
	    
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getString() - Get from a DATE, in "mdy" format, ","
separator, and 2000 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var048 ()
    {
      if (checkPropertySupport("date format")) {
        try {
            reconnect ("date format=mdy;date separator=,");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            String s = rs.getString ("C_DATE");
            String expected = "02,21,00";
	    
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getString() - Get from a DATE, in "dmy" format, " "
separator, and 1998 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var049 ()
    {
      if (checkPropertySupport("date format")) {
        try {
            reconnect ("date format=dmy;date separator=b");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString ("C_DATE");
            String expected = "08 04 98";
	    
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getString() - Get from a DATE, in "dmy" format, "/"
separator, and 2000 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var050 ()
    {
      if (checkPropertySupport("date format")) {
        try {
            reconnect ("date format=dmy;date separator=/");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_2000");
            String s = rs.getString ("C_DATE");
            String expected = "21/02/00";
	    
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getString() - Get from a DATE, in "ymd" format, "-"
separator, and 1998 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var051 ()
    {
      if (checkPropertySupport("date format")) {
        try {
            reconnect ("date format=ymd;date separator=-");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString ("C_DATE");
            String expected = "98-04-08";
	    
	    assertCondition (s.equals (expected),"got "+s+" sb "+expected);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getString() - Get from a DATE, in "ymd" format, "."
separator, and 2000 date.

SQL400 - Native JDBC does not space pad formated date, time
         and timestmp values.
**/
    public void Var052 ()
    {
      if (checkPropertySupport("date format")) {
        try {
          reconnect ("date format=ymd;date separator=.");
          ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_GET);
          JDRSTest.position0 (rs, "DATE_2000");
          String s = rs.getString ("C_DATE");
          String expected = "00.02.21";
	    
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);

        } catch (Exception e) {
          failed (e, "Unexpected Exception");
        }
      }
    }


/**
getString() - Get from a TIME, in "iso" format.
**/
    public void Var053 ()
    {
      if (checkPropertySupport("time format")) {
      try {
        reconnect ("time format=iso");
        ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
            + JDRSTest.RSTEST_GET);
        JDRSTest.position0 (rs, "DATE_1998");
        String s = rs.getString ("C_TIME");
        String expected = "08.14.03";
        
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);

      } catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }
    }


/**
getString() - Get from a TIME, in "usa" format, with an AM time.
**/
    public void Var054 ()
    {
      if (checkPropertySupport("time format")) {
        try {
          reconnect ("time format=usa");
          ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_GET);
          JDRSTest.position0 (rs, "DATE_1998");
          String s = rs.getString ("C_TIME");
          String expected = "08:14 AM";
	    
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);

        } catch (Exception e) {
          failed (e, "Unexpected Exception");
        }
      }
    }


/**
getString() - Get from a TIME, in "usa" format, with an PM time.
**/
    public void Var055 ()
    {
      if (checkPropertySupport("time format")) {
        try {
          reconnect ("time format=usa");
          ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_GET);
          JDRSTest.position0 (rs, "DATE_2000");
          String s = rs.getString ("C_TIME");
          String expected = "02:04 PM";
	    
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);

        } catch (Exception e) {
          failed (e, "Unexpected Exception");
        }
      }
    }


/**
getString() - Get from a TIME, in "eur" format.
**/
    public void Var056 ()
    {
      if (checkPropertySupport("time format")) {
        try {
          reconnect ("time format=eur");
          ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_GET);
          JDRSTest.position0 (rs, "DATE_1998");
          String s = rs.getString ("C_TIME");
          assertCondition (s.equals ("08.14.03"));
        } catch (Exception e) {
          failed (e, "Unexpected Exception");
        }
      }
    }


/**
getString() - Get from a TIME, in "jis" format.
**/
    public void Var057 ()
    {
        try {
            reconnect ("time format=jis");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString ("C_TIME");
            String expected = "08:14:03";
	    
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a TIME, in "hms" format with ":" separator.
**/
    public void Var058 ()
    {
      if (checkPropertySupport("time format")) {
        try {
            reconnect ("time format=hms;time separator=:");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString ("C_TIME");
            String expected = "08:14:03";
	    
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getString() - Get from a TIME, in "hms" format with "." separator.
**/
    public void Var059 ()
    {
      if (checkPropertySupport("time format")) {
        try {
            reconnect ("time format=hms;time separator=.");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString ("C_TIME");
            String expected = "08.14.03";
	    
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getString() - Get from a TIME, in "hms" format with "," separator.
**/
    public void Var060 ()
    {
      if (checkPropertySupport("time format")) {
        try {
            reconnect ("time format=hms;time separator=,");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString ("C_TIME");
            String expected = "08,14,03";
	    
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getString() - Get from a TIME, in "hms" format with " " separator.
**/
    public void Var061 ()
    {
      if (checkPropertySupport("time format")) {
        try {
            reconnect ("time format=hms;time separator=b");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString ("C_TIME");
            String expected = "08 14 03";
	    
            assertCondition (s.equals (expected),"got "+s+" sb "+expected);
 
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
    }


/**
getString() - Get from a TIMESTAMP.
**/
    public void Var062 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            String s = rs.getString ("C_TIMESTAMP");

            assertCondition (s.equals ("1998-11-18 03:13:42.987654"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a DATALINK.
**/
    public void Var063 ()
    {
        if (checkDatalinkSupport ()) {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_GETDL);
                JDRSTest.position0 (rs, "LOB_FULL");
                String str = rs.getString ("C_DATALINK");
                assertCondition (str.equalsIgnoreCase("https://github.com/IBM/JTOpen-test/blob/main/README.testing.txt"), "Failed to obtain datalink str="+str);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getString() - Get from a DISTINCT.
**/
    public void Var064 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                String s = rs.getString ("C_DISTINCT");
                assertCondition (s.equals ("123456789"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



// @D0A
/**
getString() - Get from a BIGINT.
**/
    public void Var065 ()
    {
        if (checkBigintSupport()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "NUMBER_NEG");
                String s = rs.getString ("C_BIGINT");
                assertCondition (s.equals ("-44332123"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getString() - Verify that no DataTruncation is posted
when the max field size is set to 0.
**/
    public void Var066()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setMaxFieldSize (0);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                                           + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            String s1 = rs.getString ("C_VARCHAR_50");
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
getString() - Verify that data is truncated without
a DataTruncation posted when the max field size is set
to a value shorter than the string.
**/
    public void Var067()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setMaxFieldSize (11);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                                           + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            int columnIndex = rs.findColumn ("C_VARCHAR_50");
            String s1 = rs.getString ("C_VARCHAR_50");
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
getString() - Get strings from a table that has columns
with different CCSIDs.
**/
    public void Var068()
    {
      if (checkSystemI()) {
        try {
            initTable(statement0_,  JDRSTest.COLLECTION + ".JDRSGS68" ,  "(COL1 VARCHAR(20) CCSID 37, "
                                       + " COL2 VARCHAR(20) CCSID " + systemObject_.getCcsid() + ", "
                                       + " COL3 VARCHAR(20) CCSID 937)");
            statement0_.executeUpdate ("INSERT INTO "
                                       + JDRSTest.COLLECTION + ".JDRSGS68 "
                                       + "(COL1, COL2, COL3) "
                                       + "VALUES ('Clifton', 'Malcolm', 'Nock')");
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.COLLECTION + ".JDRSGS68");
            rs.next ();
            String col1 = rs.getString (1);
            String col2 = rs.getString (2);
            String col3 = rs.getString (3);
            rs.close ();
            assertCondition ((col1.equals ("Clifton"))
                    && (col2.equals ("Malcolm"))
                    && (col3.equals ("Nock")));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        } finally {
            
                cleanupTable(statement0_, JDRSTest.COLLECTION + ".JDRSGS68 ");
           
        }
      }
    }


/**
getString() - Verify that we can get the user correctly when the column is
 the SQL Special value USER.

SQL400 - This wasn't working in an SQLJ test so we added the testcase here.
What we are really testing is that database converts literal constants to
unicode correctly.
**/
    public void Var069()
    {
        String userName = null;

        try {
            Statement s = connection_.createStatement ();

            
            try {
              initTable(s,  table_ , " (ONE CHAR (10 ) NOT NULL WITH DEFAULT)");
                s.executeUpdate("INSERT INTO " + table_ + " VALUES('one')");
                ResultSet rs = s.executeQuery("SELECT USER FROM " + table_);
                rs.next();
                userName = rs.getString(1);
                rs.close();


            cleanupTable(s,  table_);
            s.close();

            //System.out.println(userName);
            assertCondition(userName.equals(systemObject_.getUserId ()));

            } catch (SQLException two) {
                failed (two, "Unexpected Exception");
            }

        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Verify that we can get a string literal in place of a column
correctly.

SQL400 - This wasn't working in an SQLJ test so we added the testcase here.
What we are really testing is that database converts literal constants to
unicode correctly.
**/
    public void Var070()
    {
        String literal = null;
        String expectedLiteral = "THE BLUE ZOO";

        try {
            Statement s = connection_.createStatement ();

           

            try {
                initTable(s, table_ , " (ONE CHAR (10 ) NOT NULL WITH DEFAULT)");
                s.executeUpdate("INSERT INTO " + table_ + " VALUES('one')");
                ResultSet rs = s.executeQuery("SELECT '" + expectedLiteral + "' FROM " + table_);
                rs.next();
                literal = rs.getString(1);
                rs.close();


            cleanupTable(s,  table_);
            s.close();

            //System.out.println(literal);
            assertCondition(literal.equals(expectedLiteral));

	    } catch (SQLException two) {
		failed (two, "Unexpected Exception");
	    }


        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    String string17000000 = null;
    String string4250000 = null;

    public String getString17000000() {
	if (string17000000 == null) {
	    String piece = getString4250000();
	    StringBuffer sb = new StringBuffer(17000000);
	    sb.append(piece);
	    sb.append(piece);
	    sb.append(piece);
	    sb.append(piece);
	    string17000000=sb.toString();
	}
	return string17000000;
    }

    public String getString4250000() {
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
getString() - Get from a 17 Meg CLOB, when the CLOB data is returned in
the result set.
**/
    public void Var071() {
      if (getDriver() == JDTestDriver.DRIVER_JCC && getDatabaseType() == JDTestDriver.DB_ZOS  ) {
        notApplicable("Causes resource error on Z/OS ");
	return;
      }
      else {

		if (checkLobSupport()) {
			//
			// Using a non-locator CLOB where the size > maximum string size
			// is not valid in J9
			// Check for HY090 exception
		        Statement stmt = null;

			try {
			    stmt =   connection_.createStatement();
				String piece = getString4250000();
				String expected = getString17000000();

				

				initTable(stmt,  clobTable_
						, " (ONE CLOB(17000000) NOT NULL WITH DEFAULT)");
				initTable(stmt, clobTable4_
						, " (C1 CLOB(4250000) NOT NULL WITH DEFAULT)");

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
				String s = rs.getString(1);
				rs.close(); 
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
									"Unexpected Exception Expect HY090  -- Get 17 Meg Clob inline modified 11/12/2005 by native driver ");

						}

					} else {
						failed(
								e,
								"Unexpected Exception -- Get 17 Meg Clob inline modified 11/12/2005 by native driver ");
					}
				} else {
					failed(
							e,
							"Unexpected Exception -- Get 17 Meg Clob inline added 10/12/2004 by native driver ");
				}
			}


			// Make sure the tables are gone
			try {
			    stmt =   connection_.createStatement();
			      if (stmt != null)  {
				  cleanupTable(stmt,clobTable_);
				  stmt.close();
			      }
			} catch (SQLException one) {
			    System.out.println("Warning.  exception during cleanup");
			    one.printStackTrace(System.out); 
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
    }
/**
getString() - Get from a CLOB, when the CLOB locator is returned in
the result set.
**/
    public void Var072 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC && getDatabaseType() == JDTestDriver.DB_ZOS  ) {
        notApplicable("Causes resource error on Z/OS ");
      } else {
        if (checkLobSupport ()) {
	    Statement stmt = null;

            try {

		connection_.commit();
                reconnect ("lob threshold=0");

		stmt = connection_.createStatement();

		String piece = getString4250000();
		String expected = getString17000000();


		initTable(stmt, clobTable_ , " (ONE CLOB(17000000) NOT NULL WITH DEFAULT)");
		initTable(stmt, clobTable4_ , " (C1 CLOB(4250000) NOT NULL WITH DEFAULT)");

		PreparedStatement pstmt = connection_.prepareStatement("INSERT INTO " + clobTable4_ + " values(?)");
		pstmt.setString(1,piece);
		pstmt.executeUpdate();
		pstmt.close();

		stmt.executeUpdate("INSERT INTO "+clobTable_+" SELECT C1 || C1 || C1 || C1 FROM "+clobTable4_);

		stmt.close();


                reconnect ("lob threshold=0");

                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "+clobTable_);
 		        rs.next();
                String s = rs.getString (1);
		rs.close(); 
                assertCondition (s.equals (expected),"compare failed -- Get 17 Meg Clob locator added 10/12/2004 by native driver ");


            } catch (Exception e) {
            	if (runningJ9) {
					if (e instanceof SQLException) {
						SQLException sqlEx = (SQLException) e;
						if (sqlEx.getSQLState().equals("HY090")) {
							assertCondition(true);
						} else {
							failed(
									e,
									"Unexpected Exception Expect HY090  -- Get 17 Meg Clob inline modified 11/12/2005 by native driver ");

						}

					} else {
						failed(
								e,
								"Unexpected Exception -- Get 17 Meg Clob inline modified 11/12/2005 by native driver ");
					}

            	} else {
                failed (e, "Unexpected Exception  -- Get 17 Meg Clob locator added 10/12/2004 by native driver");
            	}
	    }
			// Make sure the tables are gone
            try {
		stmt =   connection_.createStatement();
		      cleanupTable(stmt, clobTable_);
		      cleanupTable(stmt, clobTable4_);
		      stmt.close();
            } catch (Exception e) { 
              
            }


	}
    }
    }

    /**
     getString() - Get from DFP16 -- Use preset values
     **/
    public void Var073 ()
    {
	if (checkDecFloatSupport ()) {
	    boolean success = true;
	    StringBuffer sb = new StringBuffer();

	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_DFP16);
		int i = 0;
		while (rs.next()) {
		    String v = rs.getString (1);
		    String expected = JDRSTest.VALUES_DFP16[i];
		    if (expected == null) {
			if (v != null) {
			    success = false;
			    sb.append("expected "+expected+" but got "+v);
			}
		    } else {
			if (expected.equals(v)) {
			} else {
			    if (expected.equals("1.0") && v.equals("1")) {
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
        getString() - Get from DFP34 -- Use preset values
        **/
    public void Var074 ()
    {
	if (checkDecFloatSupport ()) {
	    boolean success = true;
	    StringBuffer sb = new StringBuffer();

	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_DFP34);
		int i = 0;
		while (rs.next()) {
		    String v = rs.getString (1);
		    String expected = JDRSTest.VALUES_DFP34[i];
		    if (expected == null) {
			if (v != null) {
			    success = false;
			    sb.append("expected "+expected+" but got "+v);
			}
		    } else {
			if (expected.equals(v)) {
			} else {
			    if (expected.equals("1.0") && v.equals("1")) {
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
	getString() - Get from DFP16: NAN
	**/
    public void Var075 ()
    {
	if (checkDecFloatSupport()) {
	    try {
		Statement s = connection_.createStatement ();
		ResultSet rs = s.executeQuery ("SELECT * FROM "
					       + JDRSTest.RSTEST_DFP16NAN);
		rs.next();
		String v = rs.getString (1);
		s.close(); 
		assertCondition(v.equals("NaN"), "Expected NaN got "+v);
	    }
	    catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_JCC) {
		    notApplicable("JCC doesn't support use of NaN from getString");
		} else {
		    failed(e, "Unexpected Exception");
		}
	    }
	}
    }


	/**
	getString() - Get from DFP16: -NAN
	**/
    public void Var076 ()
    {
	if (checkDecFloatSupport()) {
	    try {
		Statement s = connection_.createStatement ();
		ResultSet rs = s.executeQuery ("SELECT * FROM "
					       + JDRSTest.RSTEST_DFP16NNAN);
		rs.next();
		String v = rs.getString (1);
		s.close(); 
		assertCondition(v.equals("-NaN"), "Expected -NaN got "+v);
	    }
	    catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_JCC) {
		    notApplicable("JCC doesn't support use of NaN");
		} else {
		    failed(e, "Unexpected Exception");
		}
	    }
	}
    }


	/**
	getString() - Get from DFP16: INF
	**/
    public void Var077 ()
    {
	if (checkDecFloatSupport()) {
	    try {
		Statement s = connection_.createStatement ();
		ResultSet rs = s.executeQuery ("SELECT * FROM "
					       + JDRSTest.RSTEST_DFP16INF);
		rs.next();
		String v = rs.getString (1);
		s.close(); 
		assertCondition(v.equals("Infinity"), "Expected Infinity got "+v);
	    }
	    catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_JCC) {
		    notApplicable("JCC doesn't support use of Inf");
		} else {
		    failed(e, "Unexpected Exception");
		}
	    }
	}
    }


	/**
	getString() - Get from DFP16: -INF
	**/
    public void Var078 ()
    {
	if (checkDecFloatSupport()) {
	    try {
		Statement s = connection_.createStatement ();
		ResultSet rs = s.executeQuery ("SELECT * FROM "
					       + JDRSTest.RSTEST_DFP16NINF);
		rs.next();
		String v = rs.getString (1);
		s.close(); 
		assertCondition(v.equals("-Infinity"), "Expected -Infinity got "+v);
	    }
	    catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_JCC) {
		    notApplicable("JCC doesn't support use of Inf");
		} else {
		    failed(e, "Unexpected Exception");
		}
	    }
	}
    }


	/**
	getString() - Get from DFP34: NAN
	**/
    public void Var079 ()
    {
	if (checkDecFloatSupport()) {
	    try {
		Statement s = connection_.createStatement ();
		ResultSet rs = s.executeQuery ("SELECT * FROM "
					       + JDRSTest.RSTEST_DFP34NAN);
		rs.next();
		String  v = rs.getString (1);
		s.close(); 
		assertCondition(v.equals("NaN"), "Expected NaN got "+v);
	    }
	    catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_JCC) {
		    notApplicable("JCC doesn't support use of NAN");
		} else {
		    failed(e, "Unexpected Exception");
		}
	    }
	}
    }


	/**
	getString() - Get from DFP34: -NAN
	**/
    public void Var080 ()
    {
	if (checkDecFloatSupport()) {
	    try {
		Statement s = connection_.createStatement ();
		ResultSet rs = s.executeQuery ("SELECT * FROM "
					       + JDRSTest.RSTEST_DFP34NNAN);
		rs.next();
		String v = rs.getString (1);
		s.close(); 
		assertCondition(v.equals("-NaN"), "Expected -NaN got "+v);
	    }
	    catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_JCC) {
		    notApplicable("JCC doesn't support use of NAN");
		} else {
		    failed(e, "Unexpected Exception");
		}
	    }
	}
    }


	/**
	getString() - Get from DFP34: INF
	**/
    public void Var081 ()
    {
	if (checkDecFloatSupport()) {
	    try {
		Statement s = connection_.createStatement ();
		ResultSet rs = s.executeQuery ("SELECT * FROM "
					       + JDRSTest.RSTEST_DFP34INF);
		rs.next();
		String v = rs.getString (1);
		s.close(); 
		assertCondition(v.equals("Infinity") , "Expected Infinity got "+v);
	    }
	    catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_JCC) {
		    notApplicable("JCC doesn't support use of Inf");
		} else {
		    failed(e, "Unexpected Exception");
		}
	    }
	}
    }


	/**
	getString() - Get from DFP34: -INF
	**/
    public void Var082 ()
    {
	if (checkDecFloatSupport()) {
	    try {
		Statement s = connection_.createStatement ();
		ResultSet rs = s.executeQuery ("SELECT * FROM "
					       + JDRSTest.RSTEST_DFP34NINF);
		rs.next();
		String v = rs.getString (1);
		s.close(); 
		assertCondition(v.equals("-Infinity"), "Expected -Infinity got "+v);
	    }
	    catch (Exception e) {
		if (getDriver() == JDTestDriver.DRIVER_JCC) {
		    notApplicable("JCC doesn't support use of Inf");
		} else {
		    failed(e, "Unexpected Exception");
		}
	    }
	}
    }

    public void appendHex(StringBuffer sb, String s) {
	if (s == null) {
	    sb.append("null");
	} else { 
	    for (int i = 0; i < s.length(); i++) {
		sb.append(" 0x");
		sb.append(Integer.toHexString(0xFFFF & s.charAt(i)));
	    }
	}
    }


     /**
      getString() -- Check use of substitution characters
      **/

    public void Var083 ()
    {
	// Don't check for toolbox until V7R2
	if (isToolboxDriver() &&
	    getRelease() < JDTestDriver.RELEASE_V7R2M0) {
	    notApplicable("substitution test not application for toolbox < v7r2");
	    return;
	}
	StringBuffer sb = new StringBuffer();
	/* Classic output is at [][1] */
	/* J9 is at [][2] */
	String[][] testcases = {

   	    /* Test CCSID 930 = 290/300 */
	    {"SELECT CAST( X'F1F20EF1F10F' AS VARCHAR(80) CCSID 930) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd",
	    "12\ufffd",
	    "12\ufffd",
	    "12\ufffd"},
	    {"SELECT CAST( X'F1F20EF1F15C7B0F' AS VARCHAR(80) CCSID 930) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u69D3",
	    "12\ufffd\u69D3",
	    "12\ufffd\u69D3",
	    "12\ufffd\u69D3"},
	    {"SELECT CAST( X'F1F20EF1F15C7Bf1f10F' AS VARCHAR(80) CCSID 930) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u69D3\ufffd",
	    "12\ufffd\u69D3\ufffd",
	    "12\ufffd\u69D3\ufffd",
	    "12\ufffd\u69D3\ufffd"},
	    {"SELECT CAST( X'F1F20EF1F15C7Bf1f1f1f15C7B0F' AS VARCHAR(80) CCSID 930) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3"},
		/* Leave translation open */
	    {"SELECT CAST( X'F1F20E5C7B' AS VARCHAR(80) CCSID 930) FROM SYSIBM.SYSDUMMY1",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3"},
		/* this one should work.  Fix 03/10/2010 for J9 and Classic in V6R1  */
	    {"SELECT CAST( X'F1F20E5C7B0f' AS VARCHAR(80) CCSID 930) FROM SYSIBM.SYSDUMMY1",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3"},
	        /* extra 0e in first of double byte data  */
                /* -- fails for native translation with Mixed data or UTF-8 data not properly formed in 7.4.*/
	    {"SELECT CAST( X'F1F20E5C7B0e7b5C7B0f' AS VARCHAR(80) CCSID 930) FROM SYSIBM.SYSDUMMY1",
	    null, 
	    "12\u69D3\ufffd\u69D3",
	    "12\u69D3\ufffd\u69D3",
	    "12\u69D3\ufffd\u69D3"},
	        /* extra 0e in second of double byte data */
              /* -- fails for native translation with Mixed data or UTF-8 data not properly formed in 7.4.*/
	    {"SELECT CAST( X'F1F20E5C7B5c0e5C7B0f' AS VARCHAR(80) CCSID 930) FROM SYSIBM.SYSDUMMY1",
	    null, 
	    "12\u69D3\ufffd\u69D3",
	    "12\u69D3\ufffd\u69D3",
	    "12\u69D3\ufffd\u69D3"},

		/* extra 0f in second of double byte data */
              /* -- fails for native translation with Mixed data or UTF-8 data not properly formed in 7.4.*/
	    {"SELECT CAST( X'F1F20E5C7B5c0f5C7B0f' AS VARCHAR(80) CCSID 930) FROM SYSIBM.SYSDUMMY1",
	    null, 
	    "12\u69D3\ufffd\u69D3",
	    "12\u69D3\ufffd\u69D3",
	    "12\u69D3\ufffd\u69D3"},

   	    /* Test CCSID 933 = 833/834 5c7b->6309*/
	    {"SELECT CAST( X'F1F20EF1F10F' AS VARCHAR(80) CCSID 933) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd",
	    "12\ufffd",
	    "12\ufffd",
	    "12\ufffd"},
	    {"SELECT CAST( X'F1F20EF1F15C7B0F' AS VARCHAR(80) CCSID 933) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u6309",
	    "12\ufffd\u6309",
	    "12\ufffd\u6309",
	    "12\ufffd\u6309"},
	    {"SELECT CAST( X'F1F20EF1F15C7Bf1f10F' AS VARCHAR(80) CCSID 933) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u6309\ufffd",
	    "12\ufffd\u6309\ufffd",
	    "12\ufffd\u6309\ufffd",
	    "12\ufffd\u6309\ufffd"},
	    {"SELECT CAST( X'F1F20EF1F15C7Bf1f1f1f15C7B0F' AS VARCHAR(80) CCSID 933) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u6309\ufffd\ufffd\u6309",
	    "12\ufffd\u6309\ufffd\ufffd\u6309",
	    "12\ufffd\u6309\ufffd\ufffd\u6309",
	    "12\ufffd\u6309\ufffd\ufffd\u6309"},
		/* Leave translation open */
	    {"SELECT CAST( X'F1F20E5C7B' AS VARCHAR(80) CCSID 933) FROM SYSIBM.SYSDUMMY1",
	    "12\u6309",
	    "12\u6309",
	    "12\u6309",
	    "12\u6309"},
		/* this one should work.  Fix 03/10/2010 for J9 and Classic in V6R1  */
	    {"SELECT CAST( X'F1F20E5C7B0f' AS VARCHAR(80) CCSID 933) FROM SYSIBM.SYSDUMMY1",
	    "12\u6309",
	    "12\u6309",
	    "12\u6309",
	    "12\u6309"},


   	    /* Test CCSID 935 = 836/837 5c7b->7efc*/
	    {"SELECT CAST( X'F1F20EF1F10F' AS VARCHAR(80) CCSID 935) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd",
	    "12\ufffd",
	    "12\ufffd",
	    "12\ufffd"},
	    {"SELECT CAST( X'F1F20EF1F15C7B0F' AS VARCHAR(80) CCSID 935) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u7efc",
	    "12\ufffd\u7efc",
	    "12\ufffd\u7efc",
	    "12\ufffd\u7efc"},
	    {"SELECT CAST( X'F1F20EF1F15C7Bf1f10F' AS VARCHAR(80) CCSID 935) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u7efc\ufffd",
	    "12\ufffd\u7efc\ufffd",
	    "12\ufffd\u7efc\ufffd",
	    "12\ufffd\u7efc\ufffd"},
	    {"SELECT CAST( X'F1F20EF1F15C7Bf1f1f1f15C7B0F' AS VARCHAR(80) CCSID 935) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u7efc\ufffd\ufffd\u7efc",
	    "12\ufffd\u7efc\ufffd\ufffd\u7efc",
	    "12\ufffd\u7efc\ufffd\ufffd\u7efc",
	    "12\ufffd\u7efc\ufffd\ufffd\u7efc"},
		/* Leave translation open */
	    {"SELECT CAST( X'F1F20E5C7B' AS VARCHAR(80) CCSID 935) FROM SYSIBM.SYSDUMMY1",
	    "12\u7efc",
	    "12\u7efc",
	    "12\u7efc",
	    "12\u7efc"},
		/* this one should work.  Fix 03/10/2010 for J9 and Classic in V6R1  */
	    {"SELECT CAST( X'F1F20E5C7B0f' AS VARCHAR(80) CCSID 935) FROM SYSIBM.SYSDUMMY1",
	    "12\u7efc",
	    "12\u7efc",
	    "12\u7efc",
	    "12\u7efc"},








	    /* Test CCSID 937 37/835 */
	    {"SELECT CAST( X'F1F20EF1F10F' AS VARCHAR(80) CCSID 937) FROM SYSIBM.SYSDUMMY1",
	     "12\ufffd",
	     "12\ufffd",
	     "12\ufffd",
	     "12\ufffd"},
	     {"SELECT CAST( X'F1F20EF1F1505b0F' AS VARCHAR(80) CCSID 937) FROM SYSIBM.SYSDUMMY1",
	     "12\ufffd\u5378",
	     "12\ufffd\u5378",
	     "12\ufffd\u5378",
	     "12\ufffd\u5378"},
	     {"SELECT CAST( X'F1F20EF1F1505bf1f10F' AS VARCHAR(80) CCSID 937) FROM SYSIBM.SYSDUMMY1",
	     "12\ufffd\u5378\ufffd",
	     "12\ufffd\u5378\ufffd",
	     "12\ufffd\u5378\ufffd",
	     "12\ufffd\u5378\ufffd"},
	     {"SELECT CAST( X'F1F20EF1F1505bf1f1f1f1505b0F' AS VARCHAR(80) CCSID 937) FROM SYSIBM.SYSDUMMY1",
	     "12\ufffd\u5378\ufffd\ufffd\u5378",
	     "12\ufffd\u5378\ufffd\ufffd\u5378",
	     "12\ufffd\u5378\ufffd\ufffd\u5378",
	     "12\ufffd\u5378\ufffd\ufffd\u5378"},
		/* Leave translation open */

	     {"SELECT CAST( X'F1F20E505b' AS VARCHAR(80) CCSID 937) FROM SYSIBM.SYSDUMMY1",
	     "12\u5378",
	     "12\u5378",
	     "12\u5378",
	     "12\u5378"},
		/* this one should work.  Fix 03/10/2010 for J9 and Classic in V6R1  */
	     {"SELECT CAST( X'F1F20E505b0f' AS VARCHAR(80) CCSID 937) FROM SYSIBM.SYSDUMMY1",
	     "12\u5378",
	     "12\u5378",
	     "12\u5378",
	     "12\u5378"},





   	    /* Test CCSID 939 = 1027/300 */
	    {"SELECT CAST( X'F1F20EF1F10F' AS VARCHAR(80) CCSID 939) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd",
	    "12\ufffd",
	    "12\ufffd",
	    "12\ufffd"},
	    {"SELECT CAST( X'F1F20EF1F15C7B0F' AS VARCHAR(80) CCSID 939) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u69D3",
	    "12\ufffd\u69D3",
	    "12\ufffd\u69D3",
	    "12\ufffd\u69D3"},
	    {"SELECT CAST( X'F1F20EF1F15C7Bf1f10F' AS VARCHAR(80) CCSID 939) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u69D3\ufffd",
	    "12\ufffd\u69D3\ufffd",
	    "12\ufffd\u69D3\ufffd",
	    "12\ufffd\u69D3\ufffd"},
	    {"SELECT CAST( X'F1F20EF1F15C7Bf1f1f1f15C7B0F' AS VARCHAR(80) CCSID 939) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3"},
		/* Leave translation open */
	    {"SELECT CAST( X'F1F20E5C7B' AS VARCHAR(80) CCSID 939) FROM SYSIBM.SYSDUMMY1",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3"},
		/* this one should work.  Fix 03/10/2010 for J9 and Classic in V6R1  */
	    {"SELECT CAST( X'F1F20E5C7B0f' AS VARCHAR(80) CCSID 939) FROM SYSIBM.SYSDUMMY1",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3"},



   	    /* Test CCSID 5026 = 290/300 */
	    {"SELECT CAST( X'F1F20EF1F10F' AS VARCHAR(80) CCSID 5026) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd",
	    "12\ufffd",
	    "12\ufffd",
	    "12\ufffd"},
	    {"SELECT CAST( X'F1F20EF1F15C7B0F' AS VARCHAR(80) CCSID 5026) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u69D3",
	    "12\ufffd\u69D3",
	    "12\ufffd\u69D3",
	    "12\ufffd\u69D3"},
	    {"SELECT CAST( X'F1F20EF1F15C7Bf1f10F' AS VARCHAR(80) CCSID 5026) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u69D3\ufffd",
	    "12\ufffd\u69D3\ufffd",
	    "12\ufffd\u69D3\ufffd",
	    "12\ufffd\u69D3\ufffd"},
	    {"SELECT CAST( X'F1F20EF1F15C7Bf1f1f1f15C7B0F' AS VARCHAR(80) CCSID 5026) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3"},
		/* Leave translation open */
	    {"SELECT CAST( X'F1F20E5C7B' AS VARCHAR(80) CCSID 5026) FROM SYSIBM.SYSDUMMY1",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3"},
		/* this one should work.  Fix 03/10/2010 for J9 and Classic in V6R1  */
	    {"SELECT CAST( X'F1F20E5C7B0f' AS VARCHAR(80) CCSID 5026) FROM SYSIBM.SYSDUMMY1",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3"},


   	    /* Test CCSID 5035 = 290/300 */
	    {"SELECT CAST( X'F1F20EF1F10F' AS VARCHAR(80) CCSID 5035) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd",
	    "12\ufffd",
	    "12\ufffd",
	    "12\ufffd"},
	    {"SELECT CAST( X'F1F20EF1F15C7B0F' AS VARCHAR(80) CCSID 5035) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u69D3",
	    "12\ufffd\u69D3",
	    "12\ufffd\u69D3",
	    "12\ufffd\u69D3"},
	    {"SELECT CAST( X'F1F20EF1F15C7Bf1f10F' AS VARCHAR(80) CCSID 5035) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u69D3\ufffd",
	    "12\ufffd\u69D3\ufffd",
	    "12\ufffd\u69D3\ufffd",
	    "12\ufffd\u69D3\ufffd"},
	    {"SELECT CAST( X'F1F20EF1F15C7Bf1f1f1f15C7B0F' AS VARCHAR(80) CCSID 5035) FROM SYSIBM.SYSDUMMY1",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3",
	    "12\ufffd\u69D3\ufffd\ufffd\u69D3"},
		/* Leave translation open */
	    {"SELECT CAST( X'F1F20E5C7B' AS VARCHAR(80) CCSID 5035) FROM SYSIBM.SYSDUMMY1",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3"},
		/* this one should work.  Fix 03/10/2010 for J9 and Classic in V6R1  */
	    {"SELECT CAST( X'F1F20E5C7B0f' AS VARCHAR(80) CCSID 5035) FROM SYSIBM.SYSDUMMY1",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3",
	    "12\u69D3"},

	    /* non working J9 case discovered */
	    /* 5/10/2013 changed from CHAR(32) to VARCHAR(40) to prevent toolbox erros */
	    /* Native translation does not work in 7.4 */ 
	    {"select cast( cast(X'404040400e484c46a645884d480f4040404d7ec20e447a45884d484783405dc3' AS CHAR(32) CCSID 65535) AS VARCHAR(40) CCSID 5026) from sysibm.sysdummy1",
	    null,
	    "    \u53d7\u53d6\u53e3\u92ad   \u0028\u003d\u0042\u00d7\u53e3\u92ad\u7387\ufffd",
	    "    \u53d7\u53d6\u53e3\u92ad   \u0028\u003d\u0042\u00d7\u53e3\u92ad\u7387\ufffd\ufffd", /* toolbox Updated 10/2/2013*/
	    "    \u53d7\u53d6\u53e3\u92ad   \u0028\u003d\u0042\u00d7\u53e3\u92ad\u7387\ufffd"},





	};


	int expectedOffset=1;

	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	    if (getRelease() >= JDTestDriver.RELEASE_V7R4M0) {
		expectedOffset=1;
	    } else {
		expectedOffset=2;
	    }
	} else if (isToolboxDriver()) {
		expectedOffset=3;

	} else if (getDriver() == JDTestDriver.DRIVER_JCC) {
		expectedOffset=4;
	}


	    try {
		boolean passed = true;
		Statement s = connection_.createStatement ();
		for (int i = 0; i < testcases.length; i++) {
		    ResultSet rs = s.executeQuery (testcases[i][0]);
		    rs.next();
		    String expected = testcases[i][expectedOffset];
		    String v;
		    boolean exceptionOccurred=false;
		    try {
			 v = rs.getString (1);
		    } catch (Exception e) {

			e.printStackTrace();
			System.err.flush();
			v="";
			exceptionOccurred = true;
		    }
		    if ((( expected == null) && (v != null)) ||
			 ((expected != null) && (!expected.equals(v)))) {
			passed = false;
			sb.append("\nFailed for query "+testcases[i][0]);
			sb.append(" \nExpected '");
			appendHex(sb, expected);
			if (exceptionOccurred) {
			    sb.append("'\nGot Exception");
			} else {
			    sb.append("'\nGot      '");
			    appendHex(sb, v);
			    sb.append("'");
			}

		    }
		}
		s.close(); 
		assertCondition(passed,sb.toString()+"\n -- added 01/07/08 by native Driver");


	    }  catch (Exception e) {
		failed(e, "Unexpected Exception \n -- added 01/07/08 by native Driver");
	    }
    }

    /**
    getString() - Should work on XML data.
    **/
        public void Var084()
        {
	    if (checkXmlSupport()) {
		String expected = JDRSTest.SAMPLE_XML1_OUTXML;
		StringBuffer sb = new StringBuffer();
		try {
		    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_GETXML+" ORDER BY C_KEY");
		    boolean passed = true;
		    int i = 0;
		    while (rs.next()) {
			expected = JDRSTest.VALUES_XML_EXPECTED[i];
			String s = rs.getString ("C_XML");
			if (expected == null ) {
			    if (s != null) {
				sb.append("For row "+i+" Expected null but got '"+s+"'\n");
			    }
			} else if (! expected.equals(s)) {
			    passed = false;
			    sb.append("For row "+i+" Expected '"+expected+"' but got '"+s+"'\n");
			}
			i++;
		    }
		    rs.close();
		    connection_.commit(); // @E1A

		    assertCondition (passed,sb);

		} catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
        }



        /**
         * getString() - Get from a TIMESTAMP(12).
         **/
        public void Var085() {
            if (checkTimestamp12Support()) {
              testGet(statement0_,
                  "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
                  "getString", 
                  "1998-11-18 03:13:42.987654000000",
                  " -- added 11/19/2012"); 

            }
        }


        /**
         * getString() - Get from a TIMESTAMP(0).
         **/
        public void Var086() {
            if (checkTimestamp12Support()) {
          testGet(statement0_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
            "getString",
            "1998-11-18 03:13:42",
            " -- added 11/19/2012"); 
            
            
            }
        }




	public void testTimestampX(String tableName, String tableDefinition,
				   String[] values) {
	    if (checkTimestamp12Support()) {

		String[] setup = new String[values.length + 2];
		setup[0] = "SILENT: DELETE FROM " + tableName;
		setup[1] = "SILENT: CREATE TABLE " + tableName + " " + tableDefinition;
		for (int i = 0; i < values.length; i++) {
		    setup[2 + i] = "INSERT INTO  " + tableName + " VALUES('" + values[i]
		      + "')";
		}
		;
		String[] cleanup = { "DROP TABLE " + tableName, };
		testGet(statement0_, setup, "SELECT C1 FROM " + tableName, cleanup,
			"getString", values, " -- added 03/26/2013");

	    }
	}

	/* Get timestamp with precision 1 */
	public void Var087() {
    String[] values =  {
  "1998-11-18 03:13:42.2",
  "1998-11-18 03:13:42.3",
  "1998-11-18 03:13:42.4",
  "1998-11-18 03:13:42.0",
    };
    testTimestampX(collection_+".JDRSGT1", "(C1 TIMESTAMP(1))",values);
	}

	/* Get timestamp with precision 2 */
	public void Var088() {
    String[] values =  {
  "1998-11-18 03:13:42.19",
  "1998-11-18 03:13:42.12",
  "1998-11-18 03:13:42.10",
  "1998-11-18 03:13:42.00",
    };
    testTimestampX(collection_+".JDRSGT2", "(C1 TIMESTAMP(2))",values);
	}

	/* Get timestamp with precision 3 */
	public void Var089() {
    String[] values =  {
  "1998-11-18 03:13:42.123",
  "1998-11-18 03:13:42.120",
  "1998-11-18 03:13:42.100",
  "1998-11-18 03:13:42.000",
    };
    testTimestampX(collection_+".JDRSGT3", "(C1 TIMESTAMP(3))",values);
	}

	/* Get timestamp with precision 4 */
	public void Var090() {
    String[] values =  {
  "1998-11-18 03:13:42.1234",
  "1998-11-18 03:13:42.1230",
  "1998-11-18 03:13:42.1200",
  "1998-11-18 03:13:42.1000",
  "1998-11-18 03:13:42.0000",
    };
    testTimestampX(collection_+".JDRSGT4", "(C1 TIMESTAMP(4))",values);
	}

	/* Get timestamp with precision 5 */
	public void Var091() {
    String[] values =  {
  "1998-11-18 03:13:42.12345",
  "1998-11-18 03:13:42.12340",
  "1998-11-18 03:13:42.12300",
  "1998-11-18 03:13:42.12000",
  "1998-11-18 03:13:42.10000",
  "1998-11-18 03:13:42.00000",
    };
    testTimestampX(collection_+".JDRSGT5", "(C1 TIMESTAMP(5))",values);
	}

	/* Get timestamp with precision 6 */
	public void Var092() {
    String[] values =  {
  "1998-11-18 03:13:42.123456",
  "1998-11-18 03:13:42.123450",
  "1998-11-18 03:13:42.123400",
  "1998-11-18 03:13:42.123000",
  "1998-11-18 03:13:42.120000",
  "1998-11-18 03:13:42.100000",
  "1998-11-18 03:13:42.000000",
    };
    testTimestampX(collection_+".JDRSGT6", "(C1 TIMESTAMP(6))",values);
	}

	/* Get timestamp with precision 7 */
	public void Var093() {
	    String[] values =  {
		"1998-11-18 03:13:42.1234567",
		"1998-11-18 03:13:42.1234560",
		"1998-11-18 03:13:42.1234500",
		"1998-11-18 03:13:42.1234000",
		"1998-11-18 03:13:42.1230000",
		"1998-11-18 03:13:42.1200000",
		"1998-11-18 03:13:42.1000000",
		"1998-11-18 03:13:42.0000000",
	    };
	    testTimestampX(collection_+".JDRSGT7", "(C1 TIMESTAMP(7))",values);

	}


  /* Get timestamp with precision 8 */
  public void Var094() {
      String[] values =  {
          "1998-11-18 03:13:42.12345678",
    "1998-11-18 03:13:42.12345670",
    "1998-11-18 03:13:42.12345600",
    "1998-11-18 03:13:42.12345000",
    "1998-11-18 03:13:42.12340000",
    "1998-11-18 03:13:42.12300000",
    "1998-11-18 03:13:42.12000000",
    "1998-11-18 03:13:42.10000000",
    "1998-11-18 03:13:42.00000000",
      };
      testTimestampX(collection_+".JDRSGT8", "(C1 TIMESTAMP(8))",values);

  }

  /* Get timestamp with precision 9 */
  public void Var095() {
    String[] values = { 
        "1998-11-18 03:13:42.123456789",
        "1998-11-18 03:13:42.123456780", 
        "1998-11-18 03:13:42.123456700",
        "1998-11-18 03:13:42.123456000", 
        "1998-11-18 03:13:42.123450000",
        "1998-11-18 03:13:42.123400000", 
        "1998-11-18 03:13:42.123000000",
        "1998-11-18 03:13:42.120000000", 
        "1998-11-18 03:13:42.100000000",
        "1998-11-18 03:13:42.000000000", };
    testTimestampX(collection_ + ".JDRSGT9", "(C1 TIMESTAMP(9))", values);

  }

  /* Get timestamp with precision 10 */
  public void Var096() {
    String[] values = { 
        "1998-11-18 03:13:42.1234567891",
        "1998-11-18 03:13:42.1234567890",
        "1998-11-18 03:13:42.1234567800", 
        "1998-11-18 03:13:42.1234567000",
        "1998-11-18 03:13:42.1234560000", 
        "1998-11-18 03:13:42.1234500000",
        "1998-11-18 03:13:42.1234000000", 
        "1998-11-18 03:13:42.1230000000",
        "1998-11-18 03:13:42.1200000000", 
        "1998-11-18 03:13:42.1000000000",
        "1998-11-18 03:13:42.0000000000", };
    testTimestampX(collection_ + ".JDRSGT10", "(C1 TIMESTAMP(10))", values);

  }

  /* Get timestamp with precision 11 */
  public void Var097() {
    String[] values = { 
        "1998-11-18 03:13:42.12345678911",
        "1998-11-18 03:13:42.12345678910",
        "1998-11-18 03:13:42.12345678900",
        "1998-11-18 03:13:42.12345678000", 
        "1998-11-18 03:13:42.12345670000",
        "1998-11-18 03:13:42.12345600000", 
        "1998-11-18 03:13:42.12345000000",
        "1998-11-18 03:13:42.12340000000", 
        "1998-11-18 03:13:42.12300000000",
        "1998-11-18 03:13:42.12000000000", 
        "1998-11-18 03:13:42.10000000000",
        "1998-11-18 03:13:42.00000000000", };
    testTimestampX(collection_ + ".JDRSGT11", "(C1 TIMESTAMP(11))", values);

  }

  /* Get timestamp with precision 12 */
  public void Var098() {
    String[] values = { 
        "1998-11-18 03:13:42.123456789112",
        "1998-11-18 03:13:42.123456789110",
        "1998-11-18 03:13:42.123456789100",
        "1998-11-18 03:13:42.123456789000",
        "1998-11-18 03:13:42.123456780000", 
        "1998-11-18 03:13:42.123456700000",
        "1998-11-18 03:13:42.123456000000", 
        "1998-11-18 03:13:42.123450000000",
        "1998-11-18 03:13:42.123400000000", 
        "1998-11-18 03:13:42.123000000000",
        "1998-11-18 03:13:42.120000000000", 
        "1998-11-18 03:13:42.100000000000",
        "1998-11-18 03:13:42.000000000000", };
    testTimestampX(collection_ + ".JDRSGT12", "(C1 TIMESTAMP(12))", values);

  }


  /* Test the get of DBCS data for a specified CCSID */
  /* For these tests a new connection is used */ 
  public void testDBCS(	String singleByteCCSID,
		        String query,
		        String expectedResult ) {
   StringBuffer sb = new StringBuffer();
   sb.append("Added 1/21/2015 to test changing CCSID of JOB \n"); 
   Connection connection = null;
   Statement stmt = null; 
   try { 
       String url = baseURL_
	 
	  ;

       sb.append("Getting connection to "+url+"\n"); 
       connection  = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
       stmt = connection.createStatement();
       String sql = "CALL QSYS.QCMDEXC('CHGJOB CCSID("+singleByteCCSID+")                 ', 0000000020.00000)"; 
       sb.append("Changing CCSID using "+sql+"\n"); 
       stmt.executeUpdate(sql);

       sb.append("Running query using "+query);
       ResultSet rs = stmt.executeQuery(query);
       rs.next();
       String result = rs.getString(1);
       assertCondition(expectedResult.equals(result), "Error: \n" +
       		"got       '"+JDTestUtilities.getMixedString(result)+"'\n" +
       		"should be '"+JDTestUtilities.getMixedString(expectedResult)+"'\n"+sb.toString()); 
	 
   } catch (Exception e) {
       failed(e, "Unexpected exception "+sb.toString());
       
   } finally {
       try { 
         if (stmt != null) stmt.close();
         if (connection != null) connection.close();
       } catch (Exception e) { 
         System.out.println("Unexpected error "); 
         e.printStackTrace(System.out); 
       }
   } 

  } 


/**
 * Test the following conversions
 *
 EBCDIC => Unicode Conversion.                                            
+---------+---------------+                                              
| EBCDIC  |TARGET UNICODE|                                               
| SOUCRE  | 5035 | 1399  |                                               
|         | 5026 |       |                                               
|         |  300 | 16684 |                                               
|         | 4396 |       |                                               
+---------+------+-------+                                               
| 4260    | FF0D | 2212  |                                               
| E9F3    | FF0D | FF0D  |                                               
| 444A    | 2015 | 2014  |                                               
| DDB7    | 2015 | 2015  |                                               
| 43A1    | FF5E | 301C  |                                               
| E9F4    | FF5E | FF5E  |                                               
| 447C    | 2225 | 2016  |                                               
| DFE5    | 2225 | 2225  |                                               
| 426A    | FFE4 | 00A6  |                                               
| E9F5    | FFE4 | FFE4  |                                               
+---------+--------------+

*/

public void Var099() {
  testDBCS("5035",
	   "select CAST(GX'4260E9F3444ADDB743A1E9F4447CDFE5426AE9F5'  AS VARGRAPHIC(80) CCSID 300) from sysibm.sysdummy1",
	   "\uFF0D\uFF0D\u2015\u2015\uFF5E\uFF5E\u2225\u2225\uFFE4\uFFE4"); 
}


public void Var100() {
  testDBCS("5035",
	   "select CAST(GX'4260E9F3444ADDB743A1E9F4447CDFE5426AE9F5'  AS VARCHAR(80) CCSID 5035) from sysibm.sysdummy1",
	   "\uFF0D\uFF0D\u2015\u2015\uFF5E\uFF5E\u2225\u2225\uFFE4\uFFE4"); 
}


public void Var101() {
  testDBCS("5026",
	   "SELECT CAST(GX'4260E9F3444ADDB743A1E9F4447CDFE5426AE9F5'  AS VARCHAR(80) CCSID 5026) FROM SYSIBM.SYSDUMMY1",
	   "\uFF0D\uFF0D\u2015\u2015\uFF5E\uFF5E\u2225\u2225\uFFE4\uFFE4"); 
}

public void Var102() {
  testDBCS("5035",
	   "select CAST(GX'4260E9F3444ADDB743A1E9F4447CDFE5426AE9F5'  AS VARGRAPHIC(80) CCSID 4396) from sysibm.sysdummy1",
	   "\uFF0D\uFF0D\u2015\u2015\uFF5E\uFF5E\u2225\u2225\uFFE4\uFFE4"); 
}

public void Var103() {
  testDBCS("1399",
	   "select CAST(GX'4260E9F3444ADDB743A1E9F4447CDFE5426AE9F5'  AS VARGRAPHIC(80) CCSID 16684) from sysibm.sysdummy1",
	   "\u2212\uFF0D\u2014\u2015\u301c\uFF5E\u2016\u2225\u00a6\uFFE4"); 
}


public void Var104() {
  testDBCS("1399",
	   "select CAST(GX'4260E9F3444ADDB743A1E9F4447CDFE5426AE9F5'  AS VARCHAR(80) CCSID 1399) from sysibm.sysdummy1",
	   "\u2212\uFF0D\u2014\u2015\u301c\uFF5E\u2016\u2225\u00a6\uFFE4"); 
}

/**
getString() - Should work when an update is pending.
**/
    public void Var105()
    {
        if (checkJdbc20 ()) {
            try {

                rs_ = JDRSTest.position (driver_, statement_, statementQuery_, rs_, "UPDATE_SANDBOX");
                rs_.updateString ("C_NVARCHAR_50", "Ninja");
                String v = rs_.getString ("C_NVARCHAR_50");
                assertCondition (v.equals ("Ninja"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getString() - Should work when an update has been done.
**/
    public void Var106()
    {
        if (checkJdbc20 ()) {
            try {
                rs_ = JDRSTest.position (driver_, statement_, statementQuery_, rs_, "UPDATE_SANDBOX");
                rs_.updateString ("C_NVARCHAR_50", "Money");
                rs_.updateRow ();
                String v = rs_.getString ("C_NVARCHAR_50");
                assertCondition (v.equals ("Money"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getString() - Should work when the current row is the insert
row, when an insert is pending.
**/
    public void Var107()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow");
            return;
          }
            try {
                rs_.moveToInsertRow ();
                rs_.updateString ("C_NVARCHAR_50", "Slipped");
                String v = rs_.getString ("C_NVARCHAR_50");
                assertCondition (v.equals ("Slipped"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getString() - Should work when the current row is the insert
row, when an insert has been done.
**/
    public void Var108()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow");
            return;
          }
            try {
                rs_.moveToInsertRow ();
                rs_.updateString ("C_NVARCHAR_50", "Koala");
                rs_.insertRow ();
                String v = rs_.getString ("C_NVARCHAR_50");
                assertCondition (v.equals ("Koala"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
getString() - Should return null when the column is NULL.
**/
    public void Var109 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_NULL");
            String s = rs.getString ("C_NCHAR_50");
            assertCondition (s == null, "Got "+s+" expected null");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }







/**
getString() - Get from a NCHAR.
**/
    public void Var110 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            String s = rs.getString ("C_NCHAR_50");
            assertCondition (s.equals ("Toolbox for Java                                  "));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a VARCHAR.
**/
    public void Var111 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            String s = rs.getString ("C_NVARCHAR_50");
            assertCondition (s.equals ("Java Toolbox"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Get from a NCLOB, when the CLOB data is returned in
the result set.
**/
    public void Var112 ()
    {
        if (checkLobSupport ()) {
            try {
                reconnect ("lob threshold=1048577");      //@E2C previously 30000
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                String s = rs.getString ("C_NCLOB");
                assertCondition (s.equals (JDRSTest.CLOB_FULL), "got \""+s+"\" sb \""+JDRSTest.CLOB_FULL+"\"");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getString() - Get from a NCLOB, when the CLOB locator is returned in
the result set.
**/
    public void Var113 ()
    {
        if (checkLobSupport ()) {
            try {
                reconnect ("lob threshold=0");
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                String s = rs.getString ("C_NCLOB");
                assertCondition (s.equals (JDRSTest.CLOB_FULL), "got \""+s+"\" sb \""+JDRSTest.CLOB_FULL+"\"");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getString() - Verify that no DataTruncation is posted
when the max field size is set to 0.
**/
    public void Var114()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setMaxFieldSize (0);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                                           + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            String s1 = rs.getString ("C_NVARCHAR_50");
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
getString() - Verify that data is truncated without
a DataTruncation posted when the max field size is set
to a value shorter than the string.
**/
    public void Var115()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setMaxFieldSize (11);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                                           + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            int columnIndex = rs.findColumn ("C_NVARCHAR_50");
            String s1 = rs.getString ("C_NVARCHAR_50");
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
getString() - Verify that array bounds exception does not occur
when dealing with mismatched mixed data.
**/
    public void Var116()
    {
        try {
	    String expected = "77\ufffd";
	    if (getDriver()  == JDTestDriver.DRIVER_NATIVE) {
		expected="77"; 
	    }

            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("select cast( X'f7f70ef7' AS VARCHAR(4) CCSID 5035)  from sysibm.sysdummy1");
            rs.next(); 
            String s1 = rs.getString (1);
	    SQLWarning w = s.getWarnings();
	    if (w == null) { 
		w = rs.getWarnings ();
	    }
            rs.close ();
            s.close ();
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
		getRelease() >= JDTestDriver.RELEASE_V7R4M0) {
		assertCondition( ( s1 == null) && (w != null),"s1="+s1+" sb null w="+w+" sb not null"); 
	    } else { 
		assertCondition ((expected.equals (s1)) && (w == null),
				 "s1="+s1+" sb '"+expected+"' w="+w+" sb null");
	    }
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getString() - Verify that we do not get bad data 
when dealing with mismatched mixed data.
**/
    public void Var117()
    {
        try {
	    String expected = "77\ufffd";
	    if (getDriver()  == JDTestDriver.DRIVER_NATIVE) {
		expected="77"; 
	    }
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("select cast( X'f7f70e5b' AS VARCHAR(4) CCSID 5035), BINARY(X'8500')  from sysibm.sysdummy1");
            rs.next(); 
            String s1 = rs.getString (1);
	    SQLWarning w = s.getWarnings();
	    if (w == null) { 
		w = rs.getWarnings ();
	    }
            rs.close ();
            s.close ();
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
		getRelease() >= JDTestDriver.RELEASE_V7R4M0) {
		assertCondition( ( s1 == null) && (w != null),"s1="+s1+" sb null w="+w+" sb not null");
	    } else { 

		assertCondition ((expected.equals (s1)) && (w == null),
				 "s1="+s1+" sb '"+expected +"' w="+w+" sb null");
	    }
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }





  /**
   * getString() - Get from a BOOLEAN.
   **/
  public void Var118() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_FALSE");
        String s = rs.getString("C_BOOLEAN");
        assertCondition(JDRSTest.BOOLEAN_FALSE_STRING.equals(s), JDRSTest.BOOLEAN_FALSE_STRING+" != " + s);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getString() - Get from a BOOLEAN.
   **/
  public void Var119() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_TRUE");

        String s = rs.getString("C_BOOLEAN");
        assertCondition(JDRSTest.BOOLEAN_TRUE_STRING.equals(s), JDRSTest.BOOLEAN_TRUE_STRING+" != " + s);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getString() - Get from a BOOLEAN.
   **/
  public void Var120() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_NULL");
        String s = rs.getString("C_BOOLEAN");
        assertCondition(s == null);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }



}



