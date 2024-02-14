///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetObject.java
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
import java.sql.Blob;
import java.sql.Clob;
import java.sql.DataTruncation;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Map; 



/**
Testcase JDRSGetObject.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getObject()
</ul>
**/
public class JDRSGetObject
extends JDTestcase {


    private static final byte[] eleven = { (byte) 'E', (byte) 'l', (byte) 'e', (byte) 'v', (byte) 'e',
        (byte) 'n', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
        (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
        (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' '};

    private static final byte[] twelve = { (byte) 'T', (byte) 'w', (byte) 'e', (byte) 'l', (byte) 'v',
        (byte) 'e'};



    // Private data.
    private Statement           statement_;
    private String              statementQuery_;
    private Statement           statement0_;
    private ResultSet           rs_;
    private int                 driver_;
    StringBuffer sb = new StringBuffer(); 

/**
Constructor.
**/
    public JDRSGetObject (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDRSGetObject",
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
	    try {
		statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							  ResultSet.CONCUR_UPDATABLE);
	    } catch (SQLException e) {
		statement_ = connection_.createStatement ();
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
getObject() - Should throw exception when the result set is
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
            rs.getObject (1);
            failed ("Didn't throw SQLException");
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            Object s = rs.getObject (1);
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s = rs.getObject (100);
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s = rs.getObject (0);
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s = rs.getObject (-1);
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Should work when the column index is valid.
**/
    public void Var006()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s = rs.getObject (1);
            assertCondition (s.equals ("DATE_1998"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() with a column index and a type map - Should throw an exception
if the type map is null.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "DATE_2000");
                Object s = rs.getObject (1, (Map) null);
                failed ("Didn't throw SQLException"+s);
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getObject() with a column index and a type map - Should work when the column
index and type map are valid.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
            try {
                /* We can not compile this without JDK 1.2, and
                   we are currently compiling with JDK 1.1.6.
                   No big deal, since we don't really support this
                   method anyway.

                JDRSTest.position (rs_, "DATE_2000");
                Object s = rs_.getObject (1, new Hashtable ());
                assertCondition (s.equals ("DATE_2000"));
                */
                notApplicable ("Type map not supported.");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getObject() - Should throw an exception when the column
name is null.
**/
    public void Var009()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s = rs.getObject (null);
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
            } else {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getObject() - Should throw an exception when the column
name is an empty string.
**/
    public void Var010()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s = rs.getObject ("");
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Should throw an exception when the column
name is invalid.
**/
    public void Var011()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s = rs.getObject ("INVALID");
            failed ("Didn't throw SQLException"+s);
        } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Should work when the column name is valid.
**/
    public void Var012()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s = rs.getObject ("C_KEY");
            assertCondition (s.equals ("DATE_1998"));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() with a column name and a type map - Should throw an exception
if the type map is null.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
            try {
		rs_ = JDRSTest.position (driver_, statement_, statementQuery_,  rs_, "DATE_2000");
                Object s = rs_.getObject ("C_KEY", (Map) null);
                failed ("Didn't throw SQLException"+s);
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getObject() with a column name and a type map - Should work when the column
index and type map are valid.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
            try {
                /* We can not compile this without JDK 1.2, and
                   we are currently compiling with JDK 1.1.6.
                   No big deal, since we don't really support this
                   method anyway.

                JDRSTest.position (rs_, "DATE_2000");
                Object s = rs_.getObject ("C_KEY", new Hashtable ());
                assertCondition (s.equals ("DATE_2000"));
                */
                notApplicable ("Type map not supported.");
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getObject() - Should work when an update is pending.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
            try {
                rs_ = JDRSTest.position (driver_, statement_, statementQuery_, rs_, "UPDATE_SANDBOX");
                rs_.updateObject ("C_VARCHAR_50", "Cam");
                Object v = rs_.getObject ("C_VARCHAR_50");
                assertCondition (v.equals ("Cam"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getObject() - Should work when an update has been done.
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
            try {
                rs_ = JDRSTest.position (driver_, statement_, statementQuery_, rs_, "UPDATE_SANDBOX");
                rs_.updateObject ("C_VARCHAR_50", "Jeru");
                rs_.updateRow ();
                Object v = rs_.getObject ("C_VARCHAR_50");
                assertCondition (v.equals ("Jeru"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getObject() - Should work when the current row is the insert
row, when an insert is pending.
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow");
            return;
          }
            try {
                rs_.moveToInsertRow ();
                rs_.updateObject ("C_VARCHAR_50", "Visitors");
                Object v = rs_.getObject ("C_VARCHAR_50");
                assertCondition (v.equals ("Visitors"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getObject() - Should work when the current row is the insert
row, when an insert has been done.
**/
    public void Var018()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow");
            return;
          }
            try {
                rs_.moveToInsertRow ();
                rs_.updateObject ("C_VARCHAR_50", "Twins");
                rs_.insertRow ();
                Object v = rs_.getObject ("C_VARCHAR_50");
                assertCondition (v.equals ("Twins"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getObject() - Should throw an exception on a deleted row.
**/
    public void Var019()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC doesn't throw exception for get on deleted row");
            return;
          }
            try {
                rs_ = JDRSTest.position (driver_, statement_, statementQuery_, rs_, "DUMMY_ROW");
                rs_.deleteRow ();
                Object v = rs_.getObject ("C_VARCHAR_50");
                failed ("Didn't throw SQLException"+v);
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
getObject() - Should return null when the column is NULL.
**/
    public void Var020 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_NULL");
            Object s = rs.getObject ("C_CHAR_50");
            assertCondition (s == null);
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get from a SMALLINT.
<P><B>Note:</B> The JDBC specification states that getting a value from a SMALLINT
database column with getObject() will return an java.lang.Integer object, not a
java.lang.Short object as we have used in the past. This change could potentially
break customers but we are making it for spec compliance in the V5R2 timeframe and
customers will have to change applications that break.
**/
    public void Var021 ()
    {
	try {
	    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
						     + JDRSTest.RSTEST_GET);
	    JDRSTest.position0 (rs, "NUMBER_POS");
	    Object s = rs.getObject ("C_SMALLINT");
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE &&  getRelease() ==  JDTestDriver.RELEASE_V7R1M0) {
	       assertCondition ((s instanceof Short) && (s.equals (new Short ((short) 198))));
	    } else {
		assertCondition ((s instanceof Integer) && (s.equals (new Integer (198))));
	    }

	} catch (Exception e) {
	    failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get from a INTEGER.
**/
    public void Var022 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            Object s = rs.getObject ("C_INTEGER");
            assertCondition ((s instanceof Integer) && (s.equals (new Integer (-98765))));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get from a REAL.
**/
    public void Var023 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            Object s = rs.getObject ("C_REAL");
            assertCondition ((s instanceof Float) && (s.equals (new Float (4.4))));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get from a FLOAT.
**/
    public void Var024 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            Object s = rs.getObject ("C_FLOAT");
            assertCondition ((s instanceof Double) && (s.equals (new Double (-5.55))), "s="+s+" sb -5.55");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get from a DOUBLE.
**/
    public void Var025 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            Object s = rs.getObject ("C_DOUBLE");
            assertCondition ((s instanceof Double) && (s.equals (new Double (6.666))));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get from a DECIMAL.
**/
    public void Var026 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_NEG");
            Object s = rs.getObject ("C_DECIMAL_50");
            assertCondition ((s instanceof BigDecimal) && (s.equals (new BigDecimal (-7.0))));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get from a NUMERIC.
**/
    public void Var027 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "NUMBER_POS");
            Object s = rs.getObject ("C_NUMERIC_105");
            assertCondition ((s instanceof BigDecimal) && (s.equals (new BigDecimal ("10.10105"))), "got "+s+" sb 10.10105 type="+s.getClass());
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get from a CHAR.
**/
    public void Var028 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            Object s = rs.getObject ("C_CHAR_50");
            assertCondition ((s instanceof String) && (s.equals ("Toolbox for Java                                  ")));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get from a VARCHAR.
**/
    public void Var029 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            Object s = rs.getObject ("C_VARCHAR_50");
            assertCondition ((s instanceof String) && (s.equals ("Java Toolbox")));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get from a CLOB.
**/
    public void Var030 ()
    {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("Clob test but column defined as String? ");
          return;
        }
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                Object s = rs.getObject ("C_CLOB");
                if (isJdbc20 ()) {
                    Clob c = (Clob) s;
		    String answer=c.getSubString (1, (int) c.length ());
                    assertCondition (answer.equals (JDRSTest.CLOB_FULL), "\""+answer+"\" <> expected \""+JDRSTest.CLOB_FULL+"\""); // @D1C
                } else {
                    assertCondition (((String) s).equals (JDRSTest.CLOB_FULL));
                }
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getObject() - Get from a DBCLOB.
**/
    public void Var031 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("Clob test but column defined as String? ");
        return;
      }
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                Object s = rs.getObject ("C_DBCLOB");
                if (isJdbc20 ()) {
                    Clob c = (Clob) s;
		    String answer = c.getSubString (1, (int) c.length ());
                    assertCondition (answer.equals (JDRSTest.DBCLOB_FULL),"\""+answer+"\" <> expected \""+JDRSTest.DBCLOB_FULL+"\""); // @D1C
                } else {
                    assertCondition (((String) s).equals (JDRSTest.DBCLOB_FULL));
                }
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getObject() - Get from a BINARY.
**/
    public void Var032 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            Object s = rs.getObject ("C_BINARY_20");
            assertCondition ((s instanceof byte[]) && (areEqual ((byte[]) s, eleven)));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get from a VARBINARY.
**/
    public void Var033 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "BINARY_NOTRANS");
            Object s = rs.getObject ("C_VARBINARY_20");
            assertCondition ((s instanceof byte[]) && (areEqual ((byte[]) s, twelve)));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get from a BLOB.
**/
    public void Var034 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("Blob test but column defined as binary? ");
        return;
      }
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                Object s = rs.getObject ("C_BLOB");
                if (isJdbc20 ()) {
                    Blob b = (Blob) s;
                    assertCondition (areEqual (b.getBytes (1, (int) b.length ()), JDRSTest.BLOB_FULL)); // @D1C
                } else {
                    assertCondition (areEqual ((byte[]) s, JDRSTest.BLOB_FULL));
                }
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getObject() - Get from a DATE.
**/
    public void Var035 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s = rs.getObject ("C_DATE");
            assertCondition ((s instanceof Date) && (s.toString ().equals ("1998-04-08")));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get from a TIME.
**/
    public void Var036 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s = rs.getObject ("C_TIME");
            assertCondition ((s instanceof Time) && (s.toString ().equals ("08:14:03")), "s is "+s+" class="+s.getClass()+" sb 8:14:03");
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get from a TIMESTAMP.
**/
    public void Var037 ()
    {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "DATE_1998");
            Object s = rs.getObject ("C_TIMESTAMP");
            assertCondition ((s instanceof Timestamp) && (s.equals (Timestamp.valueOf ("1998-11-18 03:13:42.987654000"))));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get from a DATALINK.

With JDBC 3.0 and beyond, DATALINK columns should be returned as java.net.URL objects, not Strings.
Because the ResultSet.getObject method can't be updated to throw MalformedURLExceptions, there needs
to be some determination as to how to handle the possibility that this type of exception may be thrown.
I have decided to catch the exception and, instead of thrown a comparable SQLException, return a String
object to the user as would be done for JDBC releases prior to 3.0.
NOTE: This really shouldn't be an issue as all datalinks on the AS/400 are http-based URLs today.
**/
    public void Var038 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("datalink NA for jcc  ");
        return;
      }

        if (checkDatalinkSupport ()) {
            try {
                Statement s = connection_.createStatement ();
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_GETDL);
                JDRSTest.position0 (rs, "LOB_FULL");
                Object o = rs.getObject ("C_DATALINK");
		//Native driver will return datalinks as strings until v5r3.
                if ( (getJdbcLevel() < 3) || ( getDriver() == JDTestDriver.DRIVER_NATIVE && getRelease() < JDTestDriver.RELEASE_V7R1M0))
                    assertCondition ((o instanceof String) && (o.toString().equalsIgnoreCase("https://github.com/IBM/JTOpen-test/blob/main/README.testing.txt")));
                else
                    assertCondition ((o instanceof java.net.URL) && (o.toString().equalsIgnoreCase("https://github.com/IBM/JTOpen-test/blob/main/README.testing.txt")));

            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getObject() - Get from a DISTINCT.
**/
    public void Var039 ()
    {
        if (checkLobSupport ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "LOB_FULL");
                Object s = rs.getObject ("C_DISTINCT");
                assertCondition ((s instanceof String) && (s.equals ("123456789")));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getObject() - Get from a BIGINT.
**/
    public void Var040()
    {
        if (checkBigintSupport()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "NUMBER_NEG");
                Object s = rs.getObject ("C_BIGINT");
                assertCondition ((s instanceof Long) && (s.equals (new Long (-44332123))));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getObject() - Verify that no DataTruncation is posted
when the max field size is set to 0.
**/
    public void Var041()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setMaxFieldSize (0);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                                           + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            Object s1 = rs.getObject ("C_VARCHAR_50");
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
getObject() - Verify that data is truncated without
a DataTruncation posted when the max field size is set
to a value shorter than the string.
**/
    public void Var042()
    {
        try {
            Statement s = connection_.createStatement ();
            s.setMaxFieldSize (11);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                                           + JDRSTest.RSTEST_GET);
            JDRSTest.position0 (rs, "CHAR_FULL");
            Object s1 = rs.getObject ("C_VARCHAR_50");
            SQLWarning w = rs.getWarnings ();
            rs.close ();
            s.close ();
            assertCondition ((s1.equals ("Java Toolbo")) && (w == null));
        } catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }

    /**
    getObject() - Get from DFP16 -- Use preset values
    **/
   public void Var043 ()
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
             Object v = rs.getObject (1);
             String expected = JDRSTest.VALUES_DFP16[i];
             if (expected == null) {
               if (v != null) {
                 success = false;
                 sb.append("\nexpected "+expected+" but got "+v);
               }
             } else {
               if (expected.equals(""+v)) {
                 if (! (v instanceof BigDecimal)) {
                   sb.append("\nexpected BigDecimal but got "+v.getClass().getName());
                   success=false;
                 }
               } else {
		   if ( "1.0".equals(expected) && "1".equals(v.toString())) {
		       // Still good
		   } else {
		       sb.append("\nexpected "+expected+" but got "+v);
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
   }


   /**
    getObject() - Get from DFP34 -- Use preset values
    **/
   public void Var044 ()
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
             Object v = rs.getObject (1);
             String expected = JDRSTest.VALUES_DFP34[i];

             if (expected == null) {
               if (v != null) {
                 success = false;
                 sb.append("\nexpected "+expected+" but got "+v);
               }
             } else {
               if (expected.equals(""+v)) {
                 if (! (v instanceof BigDecimal)) {
                   sb.append("\nexpected BigDecimal but got "+v.getClass().getName());
                   success=false;
                 }
               } else {
		   if ( "1.0".equals(expected) &&  "1".equals(v.toString())) {
		       // Still good
		   } else {
		       sb.append("\nexpected "+expected+" but got "+v);
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
   }


   /**
   getObject() - Get from a SQLXML.
   **/
   public void Var045() {
       if (checkXmlSupport()) {
	   if (checkJdbc40()) {
	       try {
		   ResultSet rs = statement0_.executeQuery("SELECT * FROM "
							   + JDRSTest.RSTEST_GETXML);
		   rs.next();
		   Object s = rs.getObject("C_XML");
		   Class c = s.getClass();

		   boolean passed = Class.forName("java.sql.SQLXML").isInstance(s);
		   StringBuffer sb = new StringBuffer();
		   if (!passed) {
		       try {
			   passed = Class.forName("com.ibm.db2.jcc.DB2Xml").isInstance(s);
		       } catch (Exception e) {
	      // Ignore class not found exception
		       }
		       if (!passed) {
			   Class interfaces[] = c.getInterfaces();
			   sb.append("There are " + interfaces.length + " interfaces");
			   for (int i = 0; i < interfaces.length; i++) {
			       sb.append("\nImplements " + interfaces[i]);
			   }
		       }
		   }
		   assertCondition(passed, "Got " + s + " of class " + c
				   + " that is not SQLXML " + sb.toString());
	       } catch (Exception e) {
		   failed(e, "Unexpected Exception");
	       }
	   }
       }
   }

        /**
         * getObject() - Get from a TIMESTAMP(12).
         **/
        public void Var046() {
            if (checkTimestamp12Support()) {
              testGet(statement0_,
                  "SELECT CAST('1998-11-18 03:13:42.987654321098' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
                  "getObject", 
                  "1998-11-18 03:13:42.987654321098",
                  " -- added 11/19/2012"); 

            }
        }


        /**
         * getObject() - Get from a TIMESTAMP(0).
         **/
        public void Var047() {
            if (checkTimestamp12Support()) {
          testGet(statement0_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
            "getObject",
            "1998-11-18 03:13:42.0",
            " -- added 11/19/2012"); 
            
            
            }
        }





  /**
   * getObject() - Get from a BOOLEAN false.
   **/
  public void Var048() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_FALSE");
        Object s = rs.getObject("C_BOOLEAN");
        boolean passed = true; 
        sb.setLength(0); 
        if (s instanceof Boolean) {
          if (((Boolean) s).booleanValue() != false) {
            passed = false; 
            sb.append("Expected false but got "+s);
          }
        } else { 
          passed = false; sb.append("Class is not boolean "+s.getClass().toString()); 
        }
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getObject() - Get from a BOOLEAN true.
   **/
  public void Var049() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_TRUE");

        Object s = rs.getObject("C_BOOLEAN");
        boolean passed = true; 
        sb.setLength(0); 
        if (s instanceof Boolean) {
          if (((Boolean) s).booleanValue() != true) {
            passed = false; 
            sb.append("Expected true but got "+s);
          }
        } else { 
          passed = false; sb.append("Class is not boolean "+s.getClass().toString()); 
        }
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getObject() - Get from a BOOLEAN.
   **/
  public void Var050() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_NULL");
        Object s = rs.getObject("C_BOOLEAN");
        assertCondition(s == null);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }




}



