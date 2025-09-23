///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateTimestamp.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import java.io.FileOutputStream;
import java.sql.DataTruncation;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;



/**
Testcase JDRSUpdateTimestamp.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateTimestamp()
</ul>
**/
public class JDRSUpdateTimestamp
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSUpdateTimestamp";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static final String key_            = "JDRSUpdateTimestamp";
    private static String select_         = "SELECT * FROM "
                                            + JDRSTest.RSTEST_UPDATE;

    private Statement           statement_;
    private Statement           statement2_;
    private Statement           statementMisc_;
    private ResultSet           rs_;




/**
Constructor.
**/
    public JDRSUpdateTimestamp (AS400 systemObject,
                                Hashtable<String,Vector<String>> namesAndVars,
                                int runMode,
                                FileOutputStream fileOutputStream,
                                
                                String password)
    {
        super (systemObject, "JDRSUpdateTimestamp",
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
        select_         = "SELECT * FROM "  + JDRSTest.RSTEST_UPDATE;

        if (isJdbc20 ()) {
            // SQL400 - driver neutral...
            String url = baseURL_
                         // String url = "jdbc:as400://" + systemObject_.getSystemName()
                         
                         
                         + ";data truncation=true";
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
            connection_.setAutoCommit(false); // @C1A
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);
            statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                       ResultSet.CONCUR_READ_ONLY);
            statementMisc_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            
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
updateTimestamp() - Should throw exception when the result set is
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
                rs.updateTimestamp ("C_TIMESTAMP", new Timestamp (11243452));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Should throw exception when the result set is
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
                rs.updateTimestamp ("C_TIMESTAMP", new Timestamp (445645723));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, null);
                rs_.updateTimestamp ("C_TIMESTAMP", new Timestamp (453443723));
                rs_.updateRow(); 
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Should throw an exception when the column
is an invalid index.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTimestamp (100, new Timestamp (1924325512));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Should throw an exception when the column
is 0.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTimestamp (0, new Timestamp (244353423));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Should throw an exception when the column
is -1.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTimestamp (-1, new Timestamp (1424456459));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Should work when the column index is valid.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                Timestamp d = new Timestamp (19342240);
                rs_.updateTimestamp (21, d);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Timestamp v = rs2.getTimestamp ("C_TIMESTAMP");
                rs2.close ();
                assertCondition (v.equals (d));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateTimestamp() - Should throw an exception when the column
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
            rs_.updateTimestamp (null, new Timestamp (19459));
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
      }
    }


/**
updateTimestamp() - Should throw an exception when the column
name is an empty string.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTimestamp ("", new Timestamp (95343934));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Should throw an exception when the column
name is invalid.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTimestamp ("INVALID", new Timestamp (3315934));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Should work when the column name is valid.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                Timestamp d = new Timestamp (1084440);
                rs_.updateTimestamp ("C_TIMESTAMP", d);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Timestamp v = rs2.getTimestamp ("C_TIMESTAMP");
                rs2.close ();
                assertCondition (v.equals (d));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
updateTimestamp() - Should update to SQL NULL when the column
value is null.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTimestamp ("C_TIMESTAMP", null);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Timestamp v = rs2.getTimestamp ("C_TIMESTAMP");
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
updateTimestamp() - Should be reflected by get, even if update has
not yet been issued (i.e. update is still pending).
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                Timestamp d = new Timestamp (140434300);
                rs_.updateTimestamp ("C_TIMESTAMP", d);
                Timestamp v = rs_.getTimestamp ("C_TIMESTAMP");
                assertCondition (v.equals (d));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
updateTimestamp() - Should be reflected by get, after update has
been issued, but cursor has not been repositioned.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                Timestamp d = new Timestamp (648);
                rs_.updateTimestamp ("C_TIMESTAMP", d);
                rs_.updateRow ();
                Timestamp v = rs_.getTimestamp ("C_TIMESTAMP");
                assertCondition (v.equals (d));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
updateTimestamp() - Should be reflected by get, after update has
been issued and cursor has been repositioned.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                Timestamp d = new Timestamp (-483034344);
                rs_.updateTimestamp ("C_TIMESTAMP", d);
                rs_.updateRow ();
                rs_.beforeFirst ();
                JDRSTest.position (rs_, key_);
                Timestamp v = rs_.getTimestamp ("C_TIMESTAMP");
                assertCondition (v.equals (d));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
updateTimestamp() - Should work when the current row is the insert
row.
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.moveToInsertRow ();
                rs_.updateString ("C_KEY", "JDRSUpdateTimestamp 1");
                Timestamp d = new Timestamp (104833440);
                rs_.updateTimestamp ("C_TIMESTAMP", d);
                rs_.insertRow ();
                JDRSTest.position (rs_, "JDRSUpdateTimestamp 1");
                Timestamp v = rs_.getTimestamp ("C_TIMESTAMP");
                assertCondition (v.equals (d));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateTimestamp() - Should be reflected by get on insert row, even if
insert has not yet been issued (i.e. insert is still pending).
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
            try {
                rs_.moveToInsertRow ();
                Timestamp d = new Timestamp (48055533);
                rs_.updateTimestamp ("C_TIMESTAMP", d);
                Timestamp v = rs_.getTimestamp ("C_TIMESTAMP");
                assertCondition (v.equals (d));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateTimestamp() - Should throw an exception on a deleted row.
**/
    public void Var018()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, "DUMMY_ROW");
                rs_.deleteRow ();
                rs_.updateTimestamp ("C_TIMESTAMP", new Timestamp (14345));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
updateTimestamp() - Update a SMALLINT.
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTimestamp ("C_SMALLINT", new Timestamp (13419443));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Update a INTEGER.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTimestamp ("C_INTEGER", new Timestamp (12514339));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Update a REAL.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTimestamp ("C_REAL", new Timestamp (1444419));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Update a FLOAT.
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTimestamp ("C_FLOAT", new Timestamp (-18415));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Update a DOUBLE.
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTimestamp ("C_DOUBLE", new Timestamp (34144445));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Update a DECIMAL.
**/
    public void Var024 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTimestamp ("C_DECIMAL_105", new Timestamp (123449));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Update a NUMERIC.
**/
    public void Var025 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTimestamp ("C_NUMERIC_105", new Timestamp (1944449));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Update a CHAR.

SQL400 - The default look of a timestamp is different for the native
         driver than it is for the Toolbox driver.
**/
    public void Var026 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                Timestamp d = Timestamp.valueOf ("1998-06-30 12:25:12.1234");
                rs_.updateTimestamp ("C_CHAR_50", d);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_CHAR_50");
                rs2.close ();

                //@C3 Toolbox had changed to match native, now service asked us to change back,
                //@C3 so we changed back in GA PTF.
		if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
		    assertCondition (v.equals ("1998-06-30-12.25.12.1234                          "), "got "+v+" sb '1998-06-30-12.25.12.1234                          '");
		} else {

		    // 4/8/2013 -- when going against char, zeros are dropped 
                    assertCondition (v.equals (	"1998-06-30 12:25:12.1234                          "),
				     "got '"+v+"' sb '1998-06-30 12:25:12.1234                        '");
		}
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateTimestamp() - Update a CHAR, when the value is too big.
**/
    public void Var027 ()
    {
        if (checkJdbc20 ()) {
            int expectedColumn = -1;
            try {
                JDRSTest.position (rs_, key_);
                expectedColumn = rs_.findColumn ("C_CHAR_1");
                Timestamp d = Timestamp.valueOf ("2002-06-30 19:25:12.123456");
                rs_.updateTimestamp ("C_CHAR_1", d);
                rs_.updateRow ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                DataTruncation dt = (DataTruncation)e;
                assertCondition ((dt.getIndex() == expectedColumn)
                                 && (dt.getParameter() == false)
                                 && (dt.getRead() == false)
                                 && (dt.getDataSize() == 26)
                                 && (dt.getTransferSize() == 1),
				 "dt.getIndex()="+dt.getIndex()+" sb "+expectedColumn+"\n"+
				 "dt.getParameter()="+dt.getParameter()+" sb false\n"+
				 "dt.getRead()="+dt.getRead()+" sb false\n"+
				 "dt.getDataSize()="+dt.getDataSize()+" sb  26\n"+
			         "dt.getTransferSize()="+dt.getTransferSize()+" sb 1");
            }
        }

    }



/**
updateTimestamp() - Update a VARCHAR.

SQL400 - The default look of a timestamp is different for the native
         driver than it is for the Toolbox driver.
**/
    public void Var028 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                Timestamp d = Timestamp.valueOf ("2010-01-02 19:25:12.193845");
                rs_.updateTimestamp ("C_VARCHAR_50", d);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                String v = rs2.getString ("C_VARCHAR_50");
                rs2.close ();

                //@C3 Toolbox had changed to match native, now service asked us to change back,
                //@C3 so we changed back in GA PTF.
                if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                    assertCondition (v.equals ("2010-01-02-19.25.12.193845"));
                else
                    assertCondition (v.equals ("2010-01-02 19:25:12.193845"));

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateTimestamp() - Update a BINARY.
**/
    public void Var029 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTimestamp ("C_BINARY_20", new Timestamp (213234));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Update a VARBINARY.
**/
    public void Var030 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                rs_.updateTimestamp ("C_VARBINARY_20", new Timestamp (9213334));
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updateTimestamp() - Update a CLOB.
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateTimestamp ("C_CLOB", new Timestamp (21333334));
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
updateTimestamp() - Update a DBCLOB.
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateTimestamp ("C_DBCLOB", new Timestamp (2343434));
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
updateTimestamp() - Update a BLOB.
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateTimestamp ("C_BLOB", new Timestamp (223234));
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
updateTimestamp() - Update a DATE.
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                Timestamp d = Timestamp.valueOf ("1969-11-18 03:30:30.123456");
                rs_.updateTimestamp ("C_DATE", d);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Date v = rs2.getDate ("C_DATE");
                rs2.close ();
                assertCondition (v.toString ().equals ("1969-11-18"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateTimestamp() - Update a TIME.
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                Timestamp d = Timestamp.valueOf ("1996-06-25 10:40:04.654321");
                rs_.updateTimestamp ("C_TIME", d);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Time v = rs2.getTime ("C_TIME");
                rs2.close ();
                assertCondition (v.toString ().equals ("10:40:04"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateTimestamp() - Update a TIMESTAMP.
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
            try {
                JDRSTest.position (rs_, key_);
                Timestamp d = new Timestamp (1922404333);
                rs_.updateTimestamp ("C_TIMESTAMP", d);
                rs_.updateRow ();
                ResultSet rs2 = statement2_.executeQuery (select_);
                JDRSTest.position (rs2, key_);
                Timestamp v = rs2.getTimestamp ("C_TIMESTAMP");
                rs2.close ();
                assertCondition (v.equals (d));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updateTimestamp() - Update a DATALINK.
**/
    public void Var037 ()
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
                    rs_.updateTimestamp ("C_DATALINK", new Timestamp (213234));
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
updateTimestamp() - Update a DISTINCT.
**/
    public void Var038 ()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                int expectedColumn = -1;
                try {
                    JDRSTest.position (rs_, key_);
                    expectedColumn = rs_.findColumn ("C_DISTINCT");
                    Timestamp d = Timestamp.valueOf ("1998-06-30 12:25:12.123456");
                    rs_.updateTimestamp ("C_DISTINCT", d);
                    rs_.updateRow ();
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    DataTruncation dt = (DataTruncation)e;
                    assertCondition ((dt.getIndex() == expectedColumn)
                                     && (dt.getParameter() == false)
                                     && (dt.getRead() == false)
                                     && (dt.getDataSize() == 26)
                                     && (dt.getTransferSize() == 9),
				     "dt.getIndex()="+dt.getIndex()+" sb "+expectedColumn+"\n"+
				     "dt.getParameter()="+dt.getParameter()+" sb false\n"+
				     "dt.getRead()="+dt.getRead()+" sb false\n"+
				     "dt.getDataSize()="+dt.getDataSize()+" sb  26\n"+
				 "dt.getTransferSize()="+dt.getTransferSize()+" sb 9");

                }
            }
        }
    }



/**
updateTimestamp() - Update a BIGINT.
**/
    public void Var039()
    {
        if (checkJdbc20()) {
            if (checkBigintSupport()) {
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateTimestamp ("C_BIGINT", new Timestamp (12514339));
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



    /**
    updateTimestamp() - Update a DFP16.
    **/
    public void Var040 ()
    {
      if(checkDecFloatSupport())
        try {
          Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
              ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s.executeQuery ("SELECT * FROM "
              + JDRSTest.RSTEST_DFP16+" FOR UPDATE ");
          rs.next(); 
          rs.updateTimestamp (1, new Timestamp(1423449495)); 
          failed ("Didn't throw SQLException");
        }
      catch (Exception e) {
        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
    }


    /**
    updateTimestamp() - Update a DFP34.
    **/
        public void Var041 ()
        {
          if(checkDecFloatSupport())
            try {
              Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                  ResultSet.CONCUR_UPDATABLE);
              ResultSet rs = s.executeQuery ("SELECT * FROM "
                  + JDRSTest.RSTEST_DFP34+" FOR UPDATE ");
              rs.next(); 
              rs.updateTimestamp (1, new Timestamp(1423449495)); 
              failed ("Didn't throw SQLException");
            }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }


  public void testTimestampX(String tableName, String tableDefinition,
      String[] values, String info ) {
    if (checkTimestamp12Support()) {

	String[] setup = {
	    "SILENT: DROP TABLE " + tableName,
	    "CREATE TABLE " + tableName + " " + tableDefinition,
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
	    "INSERT INTO  " + tableName + " VALUES( CURRENT TIMESTAMP)",
      }
      ;
      String[] cleanup = { "DROP TABLE " + tableName, };
      testUpdate(statementMisc_, setup, "SELECT C1 FROM " + tableName, cleanup,
          "updateTimestamp", "getTimestamp", values, info);

    }
  }



   /**  UPdate timestamp 0 -- 12 */ 

    public void Var042 ()
    {
      String[] values={
              "1998-11-18 03:13:47.0",
              "1998-11-18 03:13:43.0",
              "1998-11-18 03:13:42.0",
      }; 
      testTimestampX(collection_+".JDRSUT0","(C1 TIMESTAMP(0))", values, " -- added 3/26/2013");  
    }

    public void Var043 ()
    {
      String[] values={
              "1998-11-18 03:13:47.1",
              "1998-11-18 03:13:43.2",
              "1998-11-18 03:13:42.3",
      }; 
      testTimestampX(collection_+".JDRSUT1","(C1 TIMESTAMP(1))", values, " -- added 4/20/2013");  
    }

    public void Var044 ()
    {
      String[] values={
              "1998-11-18 03:13:47.11",
              "1998-11-18 03:13:43.12",
              "1998-11-18 03:13:42.13",
      }; 
      testTimestampX(collection_+".JDRSUT2","(C1 TIMESTAMP(2))", values, " -- added 4/20/2013");  
    }

    public void Var045 ()
    {
      String[] values={
              "1998-11-18 03:13:47.121",
              "1998-11-18 03:13:43.122",
              "1998-11-18 03:13:42.123",
      }; 
      testTimestampX(collection_+".JDRSUT3","(C1 TIMESTAMP(3))", values, " -- added 4/20/2013");  
    }

    public void Var046 ()
    {
      String[] values={
              "1998-11-18 03:13:47.1231",
              "1998-11-18 03:13:43.1232",
              "1998-11-18 03:13:42.1233",
      }; 
      testTimestampX(collection_+".JDRSUT4","(C1 TIMESTAMP(4))", values, " -- added 4/20/2013");  
    }

    public void Var047 ()
    {
      String[] values={
              "1998-11-18 03:13:47.12341",
              "1998-11-18 03:13:43.12342",
              "1998-11-18 03:13:42.12423",
      }; 
      testTimestampX(collection_+".JDRSUT5","(C1 TIMESTAMP(5))", values, " -- added 4/20/2013");  
    }

    public void Var048 ()
    {
      String[] values={
              "1998-11-18 03:13:47.123451",
              "1998-11-18 03:13:43.123452",
              "1998-11-18 03:13:42.123453",
      }; 
      testTimestampX(collection_+".JDRSUT6","(C1 TIMESTAMP(6))", values, " -- added 4/20/2013");  
    }

    public void Var049 ()
    {
      String[] values={
              "1998-11-18 03:13:47.1234561",
              "1998-11-18 03:13:43.1234562",
              "1998-11-18 03:13:42.1234563",
      }; 
      testTimestampX(collection_+".JDRSUT7","(C1 TIMESTAMP(7))", values, " -- added 4/20/2013");  
    }

    public void Var050 ()
    {
      String[] values={
              "1998-11-18 03:13:47.12345671",
              "1998-11-18 03:13:43.12345672",
              "1998-11-18 03:13:42.12345673",
      }; 
      testTimestampX(collection_+".JDRSUT8","(C1 TIMESTAMP(8))", values, " -- added 4/20/2013");  
    }

    public void Var051 ()
    {
      String[] values={
              "1998-11-18 03:13:47.123456781",
              "1998-11-18 03:13:43.123456782",
              "1998-11-18 03:13:42.123456783",
      }; 
      testTimestampX(collection_+".JDRSUT9","(C1 TIMESTAMP(9))", values, " -- added 4/20/2013");  
    }

    public void Var052 ()
    {
      String[] values={
              "1998-11-18 03:13:47.1234567891",
              "1998-11-18 03:13:43.1234567892",
              "1998-11-18 03:13:42.1234567893",
      }; 
      testTimestampX(collection_+".JDRSUT10","(C1 TIMESTAMP(10))", values, " -- added 4/20/2013");  
    }

    public void Var053 ()
    {
      String[] values={
              "1998-11-18 03:13:47.12345678901",
              "1998-11-18 03:13:43.12345678902",
              "1998-11-18 03:13:42.12345678903",
      }; 
      testTimestampX(collection_+".JDRSUT11","(C1 TIMESTAMP(11))", values, " -- added 4/20/2013");  
    }

    public void Var054 ()
    {
      String[] values={
              "1998-11-18 03:13:47.123456789011",
              "1998-11-18 03:13:43.123456789012",
              "1998-11-18 03:13:42.123456789013",
      }; 
      testTimestampX(collection_+".JDRSUT12","(C1 TIMESTAMP(12))", values, " -- added 4/20/2013");  
    }

/**
updateTimestamp() - Update a BOOLEAN.
**/
    public void Var055()
    {
        if (checkJdbc20()) {
            if (checkBooleanSupport()) {
                try {
                    JDRSTest.position (rs_, key_);
                    rs_.updateTimestamp ("C_BOOLEAN", new Timestamp (12514339));
                    failed ("Didn't throw SQLException");
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



}



