///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateDouble.java
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
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSerializeFile;



/**
Testcase JDRSUpdateDouble.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateDouble()
</ul>
**/
public class JDRSUpdateDouble
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSUpdateDouble";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static final String key_            = "JDRSUpdateDouble";
    private static final String key1_            = "JDRSUpdateDouble 1";
    private static String select_         = "SELECT * FROM "
                                            + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private ResultSet           rs_;



/**
Constructor.
**/
    public JDRSUpdateDouble (AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
    {
        super (systemObject, "JDRSUpdateDouble",
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
        select_         = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;
	if (connection_ != null) connection_.close();
        if (isJdbc20 ()) {
            String url = baseURL_
                         
                         
                         + ";data truncation=true";
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
            connection_.setAutoCommit(false); // @C1A
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);
            statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                       ResultSet.CONCUR_READ_ONLY);

            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
                                      + " (C_KEY) VALUES ('DUMMY_ROW')");
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_UPDATE
                                      + " (C_KEY) VALUES ('" + key_ + "')");

            rs_ = statement_.executeQuery (select_ + " FOR UPDATE");
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
            connection_.commit(); // @C1A
            connection_.close ();
        }
    }



/**
updateDouble() - Should throw exception when the result set is
closed.
**/
    public void Var001()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                           ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_UPDATE + " FOR UPDATE");
                rs.next ();
                rs.close ();
                rs.updateDouble ("C_DOUBLE", 5);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateDouble() - Should throw exception when the result set is
not updatable.
**/
    public void Var002()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                           ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_UPDATE);
                rs.next ();
                rs.updateDouble ("C_DOUBLE", 5);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateDouble() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, null);
                rs_.updateDouble ("C_DOUBLE", 6);
                rs_.updateRow();  /* exception */ 
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateDouble() - Should throw an exception when the column
is an invalid index.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble (100, 43);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateDouble() - Should throw an exception when the column
is 0.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble (0, -232);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateDouble() - Should throw an exception when the column
is -1.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble (-1, 76);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateDouble() - Should work when the column index is valid.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble (6, 545);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                double v = rs2.getDouble ("C_DOUBLE");
                rs2.close ();
                assertCondition (v == 545);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Should throw an exception when the column
name is null.
**/
    public void Var008()
    {
      if (checkJdbc20 ()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC throws null pointer exception when column name is null "); 
        } else { 
          try {
            JDRSTest.position (rs_, key_);
            rs_.updateDouble (null, 32);
            failed ("Didn't throw SQLException");
          } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
      }
    }


/**
updateDouble() - Should throw an exception when the column
name is an empty string.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("", 93);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateDouble() - Should throw an exception when the column
name is invalid.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("INVALID", 101);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateDouble() - Should work when the column name is valid.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_DOUBLE", -599);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                double v = rs2.getDouble ("C_DOUBLE");
                rs2.close ();
                assertCondition (v == -599);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Should be reflected by get, even if update has
not yet been issued (i.e. update is still pending).
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_DOUBLE", 11111);
                assertCondition (rs_.getDouble ("C_DOUBLE") == 11111);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
updateDouble() - Should be reflected by get, after update has
been issued, but cursor has not been repositioned.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_DOUBLE", 11111);
                rs_.updateRow ();
                assertCondition (rs_.getDouble ("C_DOUBLE") == 11111);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
updateDouble() - Should be reflected by get, after update has
been issued and cursor has been repositioned.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_DOUBLE", 11111);
                rs_.updateRow ();
                rs_.beforeFirst ();
                JDRSTest.position (rs_, key_);
                double v = rs_.getDouble ("C_DOUBLE");
                assertCondition (v == 11111);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
updateDouble() - Should work when the current row is the insert
row.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.moveToInsertRow ();
                rs_.updateString ("C_KEY", key1_);
                rs_.updateDouble ("C_DOUBLE", 98765);
                rs_.insertRow ();
                JDRSTest.position (rs_, key1_);
                assertCondition (rs_.getDouble ("C_DOUBLE") == 98765);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Should be reflected by get on insert row, even if
insert has not yet been issued (i.e. insert is still pending).
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.moveToInsertRow ();
                rs_.updateDouble ("C_DOUBLE", 222);
                assertCondition (rs_.getDouble ("C_DOUBLE") == 222);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Should throw an exception on a deleted row.
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, "DUMMY_ROW");
                rs_.deleteRow ();
                rs_.updateDouble ("C_DOUBLE", 2892);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
updateDouble() - Update a SMALLINT, when the value does not
have a decimal part.
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_SMALLINT", 876);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                short v = rs2.getShort ("C_SMALLINT");
                rs2.close ();
                assertCondition (v == 876);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Update a SMALLINT, when the value has a decimal part.  This is 
expected to work.
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
            int expectedColumn = -1;
            try {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_SMALLINT");
                rs_.updateDouble (expectedColumn, 87.443);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                short v = rs2.getShort ("C_SMALLINT");
                rs2.close ();
                assertCondition (v == 87);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Update a SMALLINT, when the value is too large.  This will throw
a data truncation exception.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
            int expectedColumn = -1;
            try {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_SMALLINT");
                rs_.updateDouble ("C_SMALLINT", 874432929.3);
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
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
updateDouble() - Update a INTEGER, when the value does not have a
decimal part.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_INTEGER", -128374);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                int v = rs2.getInt ("C_INTEGER");
                rs2.close ();
                assertCondition (v == -128374);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Update a INTEGER, when the value does have a
decimal part.  This is not an exception condition.
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_INTEGER", -128.374);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                int v = rs2.getInt ("C_INTEGER");
                rs2.close ();
                assertCondition (v == -128);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Update a INTEGER, when the value is too large.  This will throw
a data truncation exception.
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) {
            int expectedColumn = -1;
            try {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_INTEGER");
                rs_.updateDouble ("C_INTEGER", -128120120120120120l);
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
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
updateDouble() - Update a REAL.
**/
    public void Var024 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_REAL", -1.3043);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_REAL");
                rs2.close ();
                assertCondition (v == -1.3043f);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Update a REAL, when the double gets truncated after the 
decimal point.  This will not cause a DataTruncation exception.
**/
    public void Var025 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_REAL", -1.3043123455623);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_REAL");
                rs2.close ();
                assertCondition (v == -1.3043123f);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Update a REAL, when the double gets truncated before the 
decimal point.  This will cause a DataTruncation exception.

For the native driver, this will not cause a truncation since REAL is always an estimate.

**/
    public void Var026 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_REAL", -1304312345562.3);
                rs_.updateRow ();
                if (getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()) {
                    assertCondition(true); 
                    // OK... 
                } else { 
                    failed ("Didn't throw SQLException");
                }
            } catch (Exception e) {
                if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
                    failed(e); 
                } else { 
                    assertCondition(e instanceof DataTruncation);
                }
            }
        }
    }



/**
updateDouble() - Update a FLOAT.
**/
    public void Var027 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_FLOAT", 9845.02221);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                float v = rs2.getFloat ("C_FLOAT");
                rs2.close ();
                assertCondition (v == 9845.02221f);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Update a DOUBLE.
**/
    public void Var028 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_DOUBLE", -19845.19284);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                double v = rs2.getDouble ("C_DOUBLE");
                rs2.close ();
                assertCondition (v == -19845.19284);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Update a DECIMAL.
**/
    public void Var029 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_DECIMAL_105", 533.11);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_105");
                rs2.close ();
                assertCondition (v.floatValue() == 533.11f);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Update a DECIMAL, when the fraction is too big.  This
is OK.
**/
    public void Var030 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_DECIMAL_40", 5331.11);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_DECIMAL_40");
                rs2.close ();
                DataTruncation dt = (DataTruncation) rs_.getWarnings ();
                assertCondition ((v.intValue() == 5331) && (dt == null));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Update a DECIMAL, when the value is too big.  This 
will throw a data truncation exception.
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_DECIMAL_40", 533111);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
		if (e instanceof DataTruncation) { 
                assertCondition(e instanceof DataTruncation);
                /*
                DataTruncation dt = (DataTruncation)e;
                output_.println("Data Size is " + dt.getDataSize());
                output_.println("Transfer Size is " + dt.getTransferSize());
                assertCondition ((dt.getIndex() == expectedColumn)
                        && (dt.getParameter() == false)
                        && (dt.getRead() == false)
                        && (dt.getDataSize() == 6)
                        && (dt.getTransferSize() == 4));
                        */
		} else {
		    assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in latest toolbox ");

		}
            }
        }
    }



/**
updateDouble() - Update a NUMERIC.
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_NUMERIC_105", -9933.1112);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_105");
                rs2.close ();
                assertCondition (v.floatValue() == -9933.1112f);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Update a NUMERIC where the fraction truncates.  This 
is Ok.
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_NUMERIC_40", -9933.1);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                BigDecimal v = rs2.getBigDecimal ("C_NUMERIC_40");
                rs2.close ();
                assertCondition (v.floatValue() == -9933);
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Update a NUMERIC, when the value is too big.  This
is an exception condition.
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
            int expectedColumn = -1;
            try {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_NUMERIC_40");
                rs_.updateDouble ("C_NUMERIC_40", -33111.5);
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
		if (e instanceof DataTruncation) { 
                DataTruncation dt = (DataTruncation)e;
                assertCondition ((dt.getIndex() == expectedColumn)
                        && (dt.getParameter() == false)
                        && (dt.getRead() == false)
                        && (dt.getDataSize() == 6)
                        && (dt.getTransferSize() == 4));
		} else {
		    assertSqlException(e, -99999, "07006", "Data type mismatch",
				       "Mismatch instead of truncation in latest toolbox");
		}

            }
        }
    }



/**
updateDouble() - Update a CHAR.
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_CHAR_50", 112.33);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_CHAR_50");
                rs2.close ();
                assertCondition (v.equals ("112.33                                            "));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Update a CHAR, when the value is too big.
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
            int expectedColumn = -1;
            try {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_CHAR_1");
                rs_.updateDouble ("C_CHAR_1", 1.211);
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
		if (e instanceof DataTruncation) { 
                DataTruncation dt = (DataTruncation)e;
                assertCondition ((dt.getIndex() == expectedColumn)
                        && (dt.getParameter() == false)
                        && (dt.getRead() == false)
                        && (dt.getDataSize() == 5)
                        && (dt.getTransferSize() == 1));
		} else {
		    failed (e, "Unexpected Exception");
		}

            }
        }
    }



/**
updateDouble() - Update a VARCHAR.
**/
    public void Var037 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_VARCHAR_50", -112.2);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                rs2.close ();
                assertCondition (v.equals ("-112.2"));
            } catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateDouble() - Update a BINARY.
**/
    public void Var038 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_BINARY_20", 1);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateDouble() - Update a VARBINARY.
**/
    public void Var039 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_VARBINARY_20", 2);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateDouble() - Update a CLOB.
**/
    public void Var040 ()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateDouble ("C_CLOB", -3);
                    failed ("Didn't throw SQLException");
                } catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
updateDouble() - Update a DBCLOB.
**/
    public void Var041 ()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateDouble ("C_DBCLOB", 0);
                    failed ("Didn't throw SQLException");
                } catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
updateDouble() - Update a BLOB.
**/
    public void Var042 ()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateDouble ("C_BLOB", 99);
                    failed ("Didn't throw SQLException");
                } catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
updateDouble() - Update a DATE.
**/
    public void Var043 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_DATE", 6);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateDouble() - Update a TIME.
**/
    public void Var044 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_TIME", 22);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateDouble() - Update a TIMESTAMP.
**/
    public void Var045 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateDouble ("C_TIMESTAMP", 987);
                failed ("Didn't throw SQLException");
            } catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateDouble() - Update a DATALINK.
**/
    public void Var046 ()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                // We do not test updating datalinks, since it is not 
                // possible to open a updatable cursor/result set with
                // a datalink column.
                notApplicable("DATALINK update not supported.");
                /*
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateDouble ("C_DATALINK", 111);
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
updateDouble() - Update a DISTINCT.
**/
    public void Var047 ()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateDouble ("C_DISTINCT", -11.55);
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    String v = rs2.getString ("C_DISTINCT");
                    rs2.close ();
                    assertCondition (v.equals ("-11.55   "));
                } catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
updateDouble() - Update a BIGINT, when the value does not have a
decimal part.
**/
    public void Var048()
    {
        if (checkJdbc20 ()) {
            if (checkBigintSupport()) {
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateDouble ("C_BIGINT", 12837443);
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    long v = rs2.getLong ("C_BIGINT");
                    rs2.close ();
                    assertCondition (v == 12837443);
                } catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
updateDouble() - Update a BIGINT, when the value does have a
decimal part.
**/
    public void Var049()
    {
        if (checkJdbc20 ()) {
            if (checkBigintSupport()) {
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateDouble ("C_BIGINT", 3128.374);
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    long v = rs2.getLong ("C_BIGINT");
                    rs2.close ();
                    assertCondition (v == 3128);
                } catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
updateDouble() - Update a BIGINT, when the value is too large.  This 
causes a data truncation error.
**/
    public void Var050()
    {
        if (checkJdbc20 ()) {
            if (checkBigintSupport()) {
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateDouble ("C_BIGINT", 3128127243983947239745236.374);
                    rs_.updateRow ();
                    failed ("Didn't throw SQLException");
                } catch (Exception e) {
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

    public void dfpTest(String table, double value, String expected) {
      if (checkDecFloatSupport()) {
        JDSerializeFile serializeFile = null;
        try {
         serializeFile = new JDSerializeFile(connection_, table);
          Statement s = connection_.createStatement(
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s.executeQuery("SELECT * FROM " + table + " FOR UPDATE ");
          rs.next();
          rs.updateDouble(1, value);
          rs.updateRow(); /* serialized */ 
          
          ResultSet rs2 = statement2_.executeQuery("SELECT * FROM " + table);
          rs2.next();
          String v = rs2.getString(1);
          rs2.close();
          s.close();
          try {
           connection_.commit(); 
          } catch (Exception e) {} 
          assertCondition(v.equals(expected), "Got " + v + " sb " + expected + 
              "from value "+value+ " for table "+table);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        } finally {
          if (serializeFile != null) {
            try {
              serializeFile.close();
            } catch (SQLException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }

    public void dfpRoundTest(String roundingMode, String table, double value, String expected) {
      if (checkDecFloatSupport()) {
        JDSerializeFile serializeFile = null;
        try {
         serializeFile = new JDSerializeFile(connection_, table);
          
          String roundingModeProp = roundingMode;
          if(isToolboxDriver())
              roundingModeProp = roundingModeProp.substring(6);  //without "rounding " string
          
          String url = baseURL_
          
          
          + ";data truncation=true" 
          + ";decfloat rounding mode="+roundingModeProp;
          Connection connection = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
          
          Statement s = connection.createStatement(
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s.executeQuery("SELECT * FROM " + table + " FOR UPDATE ");
          rs.next();
          rs.updateDouble(1, value);
          rs.updateRow(); /* serialized */ 
          
          ResultSet rs2 = statement2_.executeQuery("SELECT * FROM " + table);
          rs2.next();
          String v = rs2.getString(1);
          rs2.close();
          s.close();
          connection.close(); 
          assertCondition(v.equals(expected),
			  "\nGot " + v +
			  "\nsb  " + expected+
              "\nfrom value "+value+
              "\nfor table "+table+" roundingMode = '"+roundingMode+"'");
        } catch (Exception e) {
          failed(e, "Unexpected Exception for table "+table+" roundingMode = '"+roundingMode+"'");
        } finally {
          if (serializeFile != null) {
            try {
              serializeFile.close();
            } catch (SQLException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }

    
    /**
     * updateDouble -- set a DFP16 value (normal)
     */
    public void Var051 () { dfpTest(JDRSTest.RSTEST_DFP16, 4533.43, "4533.43"); }
  
    /**
     * updateDouble -- set a DFP16 value (Nan)
     */    
    public void Var052 () { dfpTest(JDRSTest.RSTEST_DFP16, Double.NaN, "NaN"); }

    
    /**
     * updateDouble -- set a DFP16 value (NEGATIVE_INFINITY)
     */    
    public void Var053 () { dfpTest(JDRSTest.RSTEST_DFP16, Double.NEGATIVE_INFINITY, "-Infinity"); }

    /**
     * updateDouble -- set a DFP16 value (POSITIVE_INFINITY)
     */    
    public void Var054 () { dfpTest(JDRSTest.RSTEST_DFP16, Double.POSITIVE_INFINITY, "Infinity"); }

    /**
     * updateDouble -- set a DFP16 value (MAX_VALUE)
     */    
    public void Var055 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, Double.MAX_VALUE, "1.797693134862316E+308");
    }

    /**
     * updateDouble -- set a DFP16 value (MIN_VALUE)
     */    
    public void Var056 () {
	    dfpTest(JDRSTest.RSTEST_DFP16, Double.MIN_VALUE, "4.9E-324");
    }

    /** 
     *  updateDouble -- set a DFP16 positive with rounding mode "round half even"  
     */
    public void Var057 () { dfpRoundTest("round half even", JDRSTest.RSTEST_DFP16, 
        1.2345678901234555,"1.234567890123456"); }
    
    public void Var058 () { dfpRoundTest("round half even", JDRSTest.RSTEST_DFP16, 
        1.2345678901234545, "1.234567890123454"); }

    /** 
     *  updateDouble -- set a DFP16 negative with rounding mode "round half even"  
     */
    public void Var059 () { dfpRoundTest("round half even", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234555, "-1.234567890123456"); }
    public void Var060 () { dfpRoundTest("round half even", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234545, "-1.234567890123454"); }


    /** 
     *  updateDouble -- set a DFP16 positive with rounding mode "round half up"   
     */
    public void Var061 () { dfpRoundTest("round half up", JDRSTest.RSTEST_DFP16, 
        1.2345678901234555, "1.234567890123456"); }
    public void Var062 () { dfpRoundTest("round half up", JDRSTest.RSTEST_DFP16, 
        1.2345678901234545, "1.234567890123455"); }

    /** 
     *  updateDouble -- set a DFP16 negative with rounding mode "round half up"   
     */
    public void Var063 () { dfpRoundTest("round half up", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234555, "-1.234567890123456"); }
    public void Var064 () { dfpRoundTest("round half up", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234545, "-1.234567890123455"); }


    /** 
     *  updateDouble -- set a DFP16 positive with rounding mode "round down"   
     */
    public void Var065 () { dfpRoundTest("round down", JDRSTest.RSTEST_DFP16, 
        1.2345678901234555, "1.234567890123455"); }
    public void Var066 () { dfpRoundTest("round down", JDRSTest.RSTEST_DFP16, 
        1.2345678901234561, "1.234567890123456"); }

    
    /** 
     *  updateDouble -- set a DFP16 negative with rounding mode "round down"   
     */
    public void Var067 () { dfpRoundTest("round down", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234555, "-1.234567890123455"); }
    public void Var068 () { dfpRoundTest("round down", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234561, "-1.234567890123456"); }
  
    
    /** 
     *  updateDouble -- set a DFP16 positive with rounding mode "round ceiling"   
     */
    public void Var069 () { dfpRoundTest("round ceiling", JDRSTest.RSTEST_DFP16, 
        1.2345678901234555, "1.234567890123456"); }
    public void Var070 () { dfpRoundTest("round ceiling", JDRSTest.RSTEST_DFP16, 
        1.2345678901234545, "1.234567890123455"); }

    
    /** 
     *  updateDouble -- set a DFP16 negative with rounding mode "round ceiling"   
     */
    public void Var071 () { dfpRoundTest("round ceiling", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234555, "-1.234567890123455"); }
    public void Var072 () { dfpRoundTest("round ceiling", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234545, "-1.234567890123454"); }

    /** 
     *  updateDouble -- set a DFP16 positive with rounding mode "round floor"   
     */
    public void Var073 () { dfpRoundTest("round floor", JDRSTest.RSTEST_DFP16, 
        1.2345678901234555, "1.234567890123455"); }
    public void Var074 () { dfpRoundTest("round floor", JDRSTest.RSTEST_DFP16, 
        1.2345678901234545, "1.234567890123454"); }
    

    /** 
     *  updateDouble -- set a DFP16 negative with rounding mode "round floor"   
     */
    public void Var075 () { dfpRoundTest("round floor", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234555, "-1.234567890123456"); }
    public void Var076 () { dfpRoundTest("round floor", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234545, "-1.234567890123455"); }
    

    /** 
     *  updateDouble -- set a DFP16 positive with rounding mode "round half down"   
     */
    public void Var077 () { dfpRoundTest("round half down", JDRSTest.RSTEST_DFP16, 
        1.2345678901234565, "1.234567890123456"); }
    public void Var078 () { dfpRoundTest("round half down", JDRSTest.RSTEST_DFP16, 
        1.2345678901234545, "1.234567890123454"); }

    
    
    /** 
     *  updateDouble -- set a DFP16 negative with rounding mode "round half down"   
     */
    public void Var079 () { dfpRoundTest("round half down", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234565, "-1.234567890123456"); }
    public void Var080 () { dfpRoundTest("round half down", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234545, "-1.234567890123454"); }

    
    /** 
     *  updateDouble -- set a DFP16 positive with rounding mode "round up"   
     */
    public void Var081 () { dfpRoundTest("round up", JDRSTest.RSTEST_DFP16, 
        1.2345678901234555, "1.234567890123456"); }
    public void Var082 () { dfpRoundTest("round up", JDRSTest.RSTEST_DFP16, 
        1.2345678901234545, "1.234567890123455"); }

    
    /** 
     *  updateDouble -- set a DFP16 negative with rounding mode "round up"   
     */
    public void Var083 () { dfpRoundTest("round up", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234555, "-1.234567890123456"); }
    public void Var084 () { dfpRoundTest("round up", JDRSTest.RSTEST_DFP16, 
        -1.2345678901234545, "-1.234567890123455"); }

    
    /**
     * updateDouble -- set a DFP34 value (normal)
     */
    public void Var085 () { dfpTest(JDRSTest.RSTEST_DFP34, 4533.43, "4533.43"); }
  
    /**
     * updateDouble -- set a DFP34 value (Nan)
     */    
    public void Var086 () { dfpTest(JDRSTest.RSTEST_DFP34, Double.NaN, "NaN"); }

    
    /**
     * updateDouble -- set a DFP34 value (NEGATIVE_INFINITY)
     */    
    public void Var087 () { dfpTest(JDRSTest.RSTEST_DFP34, Double.NEGATIVE_INFINITY, "-Infinity"); }

    /**
     * updateDouble -- set a DFP34 value (POSITIVE_INFINITY)
     */    
    public void Var088 () { dfpTest(JDRSTest.RSTEST_DFP34, Double.POSITIVE_INFINITY, "Infinity"); }

    /**
     * updateDouble -- set a DFP34 value (MAX_VALUE)
     */    
    public void Var089 () {
  	    dfpTest(JDRSTest.RSTEST_DFP34, Double.MAX_VALUE, "1.7976931348623157E+308");

    }

    /**
     * updateDouble -- set a DFP34 value (MIN_VALUE)
     */    
    public void Var090 () {
	    dfpTest(JDRSTest.RSTEST_DFP34, Double.MIN_VALUE, "4.9E-324");
    }

    /** 
     *  updateDouble -- set a DFP34 positive with rounding modes 
     *  rounding tests not needed with DFP34, since all can be represented precisely with
     *  that accuracy.                 
     */
    public void Var091 () {
	    dfpRoundTest("round half even", JDRSTest.RSTEST_DFP34, 5.552576577473519E-221, "5.552576577473519E-221");
    }

    public void Var092 () {
	    dfpRoundTest("round half even", JDRSTest.RSTEST_DFP34,
			 -3.105034981303255E+231, "-3.105034981303255E+231");
    }
    public void Var093 () {
	    dfpRoundTest("round half up", JDRSTest.RSTEST_DFP34, 
			 5.552576577473519E-221, "5.552576577473519E-221");
    }
    public void Var094 () {
	    dfpRoundTest("round half up", JDRSTest.RSTEST_DFP34, 
			 -3.105034981303255E+231, "-3.105034981303255E+231");
    }
    public void Var095 () {
	    dfpRoundTest("round down", JDRSTest.RSTEST_DFP34, 
			 5.552576577473519E-221, "5.552576577473519E-221");
    }
    public void Var096 () {
	    dfpRoundTest("round down", JDRSTest.RSTEST_DFP34, 
			 -3.105034981303255E+231, "-3.105034981303255E+231");
    }
    public void Var097 () {
	    dfpRoundTest("round ceiling", JDRSTest.RSTEST_DFP34, 
			 5.552576577473519E-221, "5.552576577473519E-221");
    }
    public void Var098 () {
	    dfpRoundTest("round ceiling", JDRSTest.RSTEST_DFP34, 
			 -3.105034981303255E+231, "-3.105034981303255E+231");
    }
    public void Var099 () {
	    dfpRoundTest("round floor", JDRSTest.RSTEST_DFP34, 
			 5.552576577473519E-221, "5.552576577473519E-221");
    }
    public void Var100 () {
	    dfpRoundTest("round floor", JDRSTest.RSTEST_DFP34, 
			 -3.105034981303255E+231, "-3.105034981303255E+231");
    }
    public void Var101 () {
	    dfpRoundTest("round half down", JDRSTest.RSTEST_DFP34, 
			 5.552576577473519E-221, "5.552576577473519E-221");
    }

    public void Var102 () {
	    dfpRoundTest("round half down", JDRSTest.RSTEST_DFP34, 
			 -3.105034981303255E+231, "-3.105034981303255E+231");
    }

    public void Var103 () {
	    dfpRoundTest("round up", JDRSTest.RSTEST_DFP34, 
			 5.552576577473519E-221, "5.552576577473519E-221");
    }

    public void Var104 () {
	    dfpRoundTest("round up", JDRSTest.RSTEST_DFP34, 
			 -3.105034981303255E+231, "-3.105034981303255E+231");
    }

    
    /**
updateDouble() - Update a BOOLEAN
**/
    public void Var105()
    {
        if (checkJdbc20 ()) {
            if (checkBooleanSupport()) {
                try {
                    JDRSTest.position (rs_, key1_);
                    JDRSTest.position (rs_, key_);
                    rs_.updateDouble ("C_BOOLEAN", 12837443);
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    boolean v = rs2.getBoolean ("C_BOOLEAN");
                    rs2.close ();
                    assertCondition (v == true);
                } catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }


        /**
updateDouble() - Update a BOOLEAN
**/
    public void Var106()
    {
        if (checkJdbc20 ()) {
            if (checkBooleanSupport()) {
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateDouble ("C_BOOLEAN", 0);
                    rs_.updateRow ();
                    ResultSet rs2 = statement2_.executeQuery (select_);
                    JDRSTest.position (rs2, key_);
                    boolean v = rs2.getBoolean ("C_BOOLEAN");
                    rs2.close ();
                    assertCondition (v == false);
                } catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }

    

}



