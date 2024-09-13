///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetBlob.java
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
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DataTruncation;

import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSGetBlob.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getBlob()
</ul>
**/
public class JDRSGetBlob
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetBlob";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private Statement           statement_;
    private Statement           statement0_;

    private Connection          connection2_;
    private Statement           statement2_;



    private static final byte[] eleven = { (byte) 'E', (byte) 'l', (byte) 'e', (byte) 'v', (byte) 'e', (byte) 'n',
        (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
        (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
        (byte) ' ', (byte) ' '};

    private static final byte[] twelve = { (byte) 'T', (byte) 'w', (byte) 'e', (byte) 'l', (byte) 'v',
        (byte) 'e'};



/**
Constructor.
**/
    public JDRSGetBlob (AS400 systemObject,
                        Hashtable namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password)
    {
        super (systemObject, "JDRSGetBlob",
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

        if (isJdbc20 ()) {
            // SQL400 - driver neutral...
            String url = baseURL_;
	    connection_ = testDriver_.getConnection (url + ";lob threshold=30000;data truncation=true",systemObject_.getUserId(),encryptedPassword_,"JDRSGetBlob1");

	    setAutoCommit(connection_, false); // @C1A

            statement0_ = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                      ResultSet.CONCUR_READ_ONLY);
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_GET
                                      + " (C_KEY) VALUES ('DUMMY_ROW')");

            // Force LOB locators.
            connection2_ = testDriver_.getConnection (url + ";lob threshold=0","JDRSGetBlob1");
	    setAutoCommit(connection2_, false); // @C1A

            statement2_ = connection2_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                        ResultSet.CONCUR_READ_ONLY);
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
            statement2_.close ();
            connection2_.commit(); // @C1A
            connection2_.close ();
            statement0_.close ();
            statement_.close ();
            connection_.commit(); // @C1A
            connection_.close ();
        }
    }


    protected void cleanupConnections ()
    throws Exception
    {
        if (isJdbc20 ()) {
            connection2_.commit(); // @C1A
            connection2_.close ();
            connection_.commit(); // @C1A
            connection_.close ();
        }
    }







/**
getBlob() - Should throw exception when the result set is
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
                rs.getBlob (1);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, null);
                Blob v = rs.getBlob (1);
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "BINARY_NOTRANS");
                Blob v = rs.getBlob (100);
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "BINARY_NOTRANS");
                Blob v = rs.getBlob (0);
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "BINARY_NOTRANS");
                Blob v = rs.getBlob (-1);
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Should work when the column index is valid.
**/
    public void Var006()
    {
        StringBuffer sb = new StringBuffer(); 
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("jcc throws invalid data conversion when getting blob from BINARY_NOTRANS");
          return; 
        }
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "BINARY_NOTRANS");
                Blob v = rs.getBlob (18);
                assertCondition (compare (v, twelve,sb), sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getBlob() - Should throw an exception when the column
name is null.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC throws null pointer exception when column name is null ");
            return; 
          }  

            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "BINARY_NOTRANS");
                rs.getBlob (null);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Should throw an exception when the column
name is an empty string.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "BINARY_NOTRANS");
                rs.getBlob ("");
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Should throw an exception when the column
name is invalid.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "BINARY_NOTRANS");
                rs.getBlob ("INVALID");
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Should work when the column name is valid.
**/
    public void Var010()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC doesn't permit getting a blob from a binary ");
            return; 
          }  

            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "BINARY_NOTRANS");
                Blob v = rs.getBlob ("C_BINARY_20");
                assertCondition (compare (v, eleven,sb),sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getBlob() - Should work when an update is pending.
**/
    public void Var011()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_GET + " FOR UPDATE");
                JDRSTest.position (rs, "UPDATE_SANDBOX");
                byte[] test = new byte[] { (byte) 0x34, (byte) 0x45, (byte) 0x50,
                    (byte) 0x56, (byte) 0x67, (byte) 0x78, (byte) 0x89, (byte) 0x9A,
                    (byte) 0xAB, (byte) 0xBC};
                rs.updateBytes ("C_VARBINARY_20", test);
                Blob v = rs.getBlob ("C_VARBINARY_20");
                assertCondition (compare (v, test,sb),sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getBlob() - Should work when an update has been done.
**/
    public void Var012()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_GET + " FOR UPDATE");
                JDRSTest.position (rs, "UPDATE_SANDBOX");
                byte[] test = new byte[] { (byte) 0x01, (byte) 0x12, (byte) 0x34, (byte) 0x45, (byte) 0x50,
                    (byte) 0x56, (byte) 0x67, (byte) 0x78, (byte) 0x89, (byte) 0x9A,
                    (byte) 0xAB, (byte) 0xBC, (byte) 0xCD, (byte) 0xDE, (byte) 0xEF,
                    (byte) 0xFF, (byte) 0x00, (byte) 0xFF, (byte) 0x00, (byte) 0xFF};
                rs.updateBytes ("C_BINARY_20", test);
                rs.updateRow ();
                Blob v = rs.getBlob ("C_BINARY_20");
                assertCondition (compare (v, test,sb),sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getBlob() - Should work when the current row is the insert
row, when an insert is pending.
**/
    public void Var013()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                notApplicable("JCC does not support moveToInserRow"); 
                return; 
              }
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_GET + " FOR UPDATE");
                rs.moveToInsertRow ();            
                byte[] test = new byte[] { (byte) 0xBC, (byte) 0xCD};
                rs.updateBytes ("C_VARBINARY_20", test);
                Blob v = rs.getBlob ("C_VARBINARY_20");
                assertCondition (compare (v, test,sb),sb);        
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getBlob() - Should work when the current row is the insert
row, when an insert has been done.
**/
    public void Var014()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            try {
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                notApplicable("JCC does not support moveToInserRow"); 
                return; 
              }
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_GET + " FOR UPDATE");
                rs.moveToInsertRow ();
                byte[] test = new byte[] { (byte) 0xBC, (byte) 0xCD, (byte) 0xDE, (byte) 0xEF,
                    (byte) 0xFF, (byte) 0x00, (byte) 0xFF, (byte) 0x00, (byte) 0xFF};
                rs.updateBytes ("C_VARBINARY_20", test);
                rs.insertRow ();
                Blob v = rs.getBlob ("C_VARBINARY_20");
                assertCondition (compare (v, test,sb),sb);        
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getBlob() - Should throw an exception on a deleted row.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) { 
            notApplicable("JCC doesn't throw exception for get on deleted row");
            return; 
          }
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_GET + " FOR UPDATE");
                JDRSTest.position (rs, "DUMMY_ROW");
                rs.deleteRow ();
                Blob v = rs.getBlob ("C_VARBINARY_20");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
getBlob() - Should return null when the column is NULL.
**/
    public void Var016 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "CHAR_NULL");
                Blob v = rs.getBlob ("C_VARBINARY_20");
                assertCondition (v == null);        
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getBlob() - Get from a SMALLINT.
**/
    public void Var017 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "NUMBER_NEG");
                Blob v = rs.getBlob ("C_SMALLINT");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Get from a INTEGER.
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "NUMBER_POS");
                Blob v = rs.getBlob ("C_INTEGER");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Get from a REAL.
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "NUMBER_NEG");
                Blob v = rs.getBlob ("C_REAL");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Get from a FLOAT.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "NUMBER_POS");
                Blob v = rs.getBlob ("C_FLOAT");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Get from a DOUBLE.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "NUMBER_NEG");
                Blob v = rs.getBlob ("C_DOUBLE");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Get from a DECIMAL.
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "NUMBER_POS");
                Blob v = rs.getBlob ("C_DECIMAL_50");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Get from a NUMERIC.
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "NUMBER_NEG");
                Blob v = rs.getBlob ("C_NUMERIC_105");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
getBlob() - Get from a CHAR.
**/
    public void Var024 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "CHAR_FULL");
                Blob v = rs.getBlob ("C_CHAR_50");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Get from a VARCHAR.
**/
    public void Var025 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "CHAR_FULL");
                Blob v = rs.getBlob ("C_VARCHAR_50");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Get from a BINARY.
**/
    public void Var026 ()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC doesn't permit getting a blob from a binary ");
            return; 
          }  

            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "BINARY_NOTRANS");
                Blob v = rs.getBlob ("C_BINARY_20");
                assertCondition (compare (v, eleven,sb),sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getBlob() - Get from a VARBINARY.
**/
    public void Var027 ()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC doesn't permit getting a blob from a varbinary ");
            return; 
          }  
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "BINARY_NOTRANS");
                Blob v = rs.getBlob ("C_VARBINARY_20");
                assertCondition (compare (v, twelve,sb),sb);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getBlob() - Get from a CLOB.
**/
    public void Var028 ()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                             + JDRSTest.RSTEST_GET);
                    JDRSTest.position (rs, "LOB_FULL");
                    Blob v = rs.getBlob ("C_CLOB");
                    failed ("Didn't throw SQLException" +v);
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
getBlob() - Get from a DBCLOB.
**/
    public void Var029 ()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                             + JDRSTest.RSTEST_GET);
                    JDRSTest.position (rs, "LOB_FULL");
                    Blob v = rs.getBlob ("C_DBCLOB");
                    failed ("Didn't throw SQLException" +v);
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
getBlob() - Get from a BLOB, when the BLOB data is
returned in the result set.
**/
    public void Var030 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("jcc throws invalid data conversion when getting blob from BINARY_NOTRANS");
        return; 
      }
      StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                             + JDRSTest.RSTEST_GET);
                    JDRSTest.position (rs, "LOB_FULL");
                    Blob v = rs.getBlob ("C_BLOB");
                    assertCondition (compare (v, JDRSTest.BLOB_FULL,sb),sb);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
getBlob() - Get from a BLOB, when the BLOB locator is
returned in the result set.
**/
    public void Var031 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC Driver hangs");
        return; 
      }        StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                                                             + JDRSTest.RSTEST_GET);
                    JDRSTest.position (rs, "LOB_FULL");
                    Blob v = rs.getBlob ("C_BLOB");
                    assertCondition (compare (v, JDRSTest.BLOB_FULL,sb),sb);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**
getBlob() - Get from a DATE.
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "DATE_1998");
                Blob v = rs.getBlob ("C_DATE");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Get from a TIME.
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "DATE_1998");
                Blob v = rs.getBlob ("C_TIME");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Get from a TIMESTAMP.
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_GET);
                JDRSTest.position (rs, "DATE_1998");
                Blob v = rs.getBlob ("C_TIMESTAMP");
                failed ("Didn't throw SQLException" +v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getBlob() - Get from a DATALINK.
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
            if (checkDatalinkSupport ()) {
                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                               ResultSet.CONCUR_READ_ONLY);
                    ResultSet rs = s.executeQuery ("SELECT * FROM "
                                                   + JDRSTest.RSTEST_GETDL);
                    JDRSTest.position (rs, "LOB_FULL");
                    Blob v = rs.getBlob ("C_DATALINK");
                    failed ("Didn't throw SQLException" +v);
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
getBlob() - Get from a DISTINCT.
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {
                    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                             + JDRSTest.RSTEST_GET);
                    JDRSTest.position (rs, "LOB_EMPTY");
                    Blob v = rs.getBlob ("C_DISTINCT");
                    failed ("Didn't throw SQLException" +v);
                }
                catch (Exception e) {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }



/**
getBlob() - Get from a INTEGER.
**/
    public void Var037 ()
    {        StringBuffer sb = new StringBuffer(); 

        if (checkJdbc20 ()) {
            if (checkBigintSupport()) {
                try {
                    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                                                             + JDRSTest.RSTEST_GET);
                    JDRSTest.position (rs, "NUMBER_POS");
                    Blob v = rs.getBlob ("C_BIGINT");
                    byte[] bigint = longToByteArray(12374321);
//@K1A
                        failed ("Didn't throw SQLException got"+v+" "+bigint+" "+sb);
                }
                catch (Exception e) {
//@K1A
                        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
            }
        }
    }


/** B2A
getBlob() - Verify that no DataTruncation is posted
when the max field size is set to 0.
**/
    public void Var038()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkNative()) {
            if (checkJdbc20 ()) {
                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                               ResultSet.CONCUR_READ_ONLY);
                    s.setMaxFieldSize (0);
                    ResultSet rs = s.executeQuery ("SELECT * FROM "
                                                   + JDRSTest.RSTEST_GET);
                    JDRSTest.position (rs, "BINARY_NOTRANS");
                    Blob v = rs.getBlob ("C_VARBINARY_20");
                    DataTruncation dt = (DataTruncation) rs.getWarnings ();
                    rs.close ();
                    s.close ();
                    assertCondition ((compare (v, twelve,sb))
                            && (dt == null), sb);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }



/**  B2A
getBlob() - Verify that data is truncated without
a DataTruncation posted when the max field size is set 
to a value shorter than the byte array.
**/
    public void Var039()
    {
      StringBuffer sb = new StringBuffer(); 

        if (checkNative()) {
            if (checkJdbc20 ()) {
                try {
                    Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                               ResultSet.CONCUR_READ_ONLY);
                    s.setMaxFieldSize (18);
                    ResultSet rs = s.executeQuery ("SELECT * FROM "
                                                   + JDRSTest.RSTEST_GET);
                    JDRSTest.position (rs, "BINARY_NOTRANS");
                    Blob v = rs.getBlob ("C_BINARY_20");
                    SQLWarning w = rs.getWarnings ();
                    rs.close ();
                    s.close ();
                    byte[] expected =  { (byte) 'E', (byte) 'l', (byte) 'e', (byte) 'v', (byte) 'e', (byte) 'n',
                        (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
                        (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' '};
                    assertCondition ((compare (v, expected,sb)) && (w == null),sb);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
    }


    /**
    getBlob() - Get from DFP16:
    **/
   public void Var040 ()
   {
     if (checkDecFloatSupport()) {
       try {
         Statement s = connection_.createStatement ();
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_DFP16);
         rs.next(); 
         Blob v = rs.getBlob (1);
         failed ("Didn't throw SQLException "+v);
       }
       catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
       }
     }
   }
   
   
   
   /**
    getBlob() - Get from DFP34:
    **/
   public void Var041 ()
   {
     if (checkDecFloatSupport()) {
       try {
         Statement s = connection_.createStatement ();
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_DFP34);
         rs.next(); 
         Blob v = rs.getBlob (1);
         failed ("Didn't throw SQLException "+v);
       }
       catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
       }
     }
   }
   

   /**
   getBlob() - Get from an SQLXML. 
   **/
  public void Var042() {

      StringBuffer sb = new StringBuffer(" -- getBlob from SQLXML changed 12/14/2011 "); 
    if (checkXmlSupport()) {
      try {
        Statement stmt = connection_.createStatement();
	String sql = "SELECT * FROM "
            + JDRSTest.RSTEST_GETXML+" A ORDER BY RRN(A)";
	sb.append("SQL = "+sql); 
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        Blob v = rs.getBlob("C_XML");
        if(isToolboxDriver())
        {
        	assertCondition ((compare (v, JDRSTest.SAMPLE_XML1_OUTXML_WITHDECL_UTF8,sb)),sb);
        }
	else {
	    assertCondition (compare (v, JDRSTest.SAMPLE_XML1_OUTXML_UTF8, sb),sb);
	}
      } catch (Exception e) {
    	  if(isToolboxDriver())
    		  failed(e, sb.toString());
    	  else if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String exInfo = e.toString(); 
          String expected = "Invalid data conversion";
          assertCondition(exInfo.indexOf(expected)>=0, 
              "Got exception '"+exInfo+"' expected '"+expected+"'"); 
            
        } else {
	    failed(e, sb.toString());
        }
      }

    }
  }


        /**
         * getBlob() - Get from a TIMESTAMP(12).
         **/
        public void Var043() {
            if (checkTimestamp12Support()) {
              testGet(statement0_,
                  "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
                  "getBlob", 
                  "EXCEPTION:Data type mismatch.", 
                  " -- added 11/19/2012"); 

            }
        }


        /**
         * getBlob() - Get from a TIMESTAMP(0).
         **/
        public void Var044() {
            if (checkTimestamp12Support()) {
          testGet(statement0_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
            "getBlob",
		  "EXCEPTION:Data type mismatch.", 
            " -- added 11/19/2012"); 
            
            
            }
        }

/**
getBlob() - Get from a BOOLEAN.
**/
	public void Var045 ()
	{
	    if (checkBooleanSupport()) {
		try {
		    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_BOOLEAN);
		    JDRSTest.position0 (rs, "BOOLEAN_TRUE");
		    Blob v = rs.getBlob ("C_BOOLEAN");
		    failed ("Didn't throw SQLException"+v);
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}


/**
getBlob() - Get from a BOOLEAN.
**/
	public void Var046 ()
	{
	    if (checkBooleanSupport()) {
		try {
		    ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_BOOLEAN);
		    JDRSTest.position0 (rs, "BOOLEAN_FALSE");
		    Blob v = rs.getBlob ("C_BOOLEAN");
		    failed ("Didn't throw SQLException"+v);
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}

/**
getBlob() - Get from a BOOLEAN.
**/
    public void Var047()
    {
        if (checkBooleanSupport()) {
        try {
            ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_BOOLEAN);            
            JDRSTest.position0 (rs, "BOOLEAN_NULL");
            Blob v = rs.getBlob ("C_BOOLEAN");
            assertCondition (v == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        }
    }




   

    
 
    /**
      Convert the specified long into server format in the specified byte array.
      @param  longValue  The value to be converted to server format.
      @param  serverValue  The array to receive the data type in server format.
      @param  offset  The offset into the byte array for the start of the server value.
     **/
    public static void longToByteArray(long longValue, byte[] serverValue, int offset)
    {
	// Do in two parts to avoid long temps.
	int high = (int)(longValue >>> 32);
	int low = (int)longValue;

	serverValue[offset]   = (byte)(high >>> 24);
	serverValue[offset+1] = (byte)(high >>> 16);
	serverValue[offset+2] = (byte)(high >>>  8);
	serverValue[offset+3] = (byte) high;

	serverValue[offset+4] = (byte)(low >>> 24);
	serverValue[offset+5] = (byte)(low >>> 16);
	serverValue[offset+6] = (byte)(low >>>  8);
	serverValue[offset+7] = (byte) low;
    }

    /**
      Convert the specified long into server format in the specified byte array.
      @param  longValue  The value to be converted to server format.
      @return  The array with the data type in server format.
     **/
    public static byte[] longToByteArray(long longValue)
    {
        byte[] serverValue = new byte[8];
        longToByteArray(longValue, serverValue, 0);
        return serverValue;
    }
}



