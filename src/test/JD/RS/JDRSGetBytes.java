///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetBytes.java
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
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDTestUtilities;

/**
 * Testcase JDRSGetBytes. This tests the following method of the JDBC ResultSet
 * class:
 * 
 * <ul>
 * <li>getBytes()
 * </ul>
 **/
public class JDRSGetBytes extends JDTestcase {

  // Private data.
  private Statement statement_;
  private String statementQuery_;
  private Statement statement0_;
  private ResultSet rs_;

  private static final byte[] eleven = { (byte) 'E', (byte) 'l', (byte) 'e',
      (byte) 'v', (byte) 'e', (byte) 'n', (byte) ' ', (byte) ' ', (byte) ' ',
      (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
      (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ' };

  private static final byte[] twelve = { (byte) 'T', (byte) 'w', (byte) 'e',
      (byte) 'l', (byte) 'v', (byte) 'e' };

  /**
   * Constructor.
   **/
  public JDRSGetBytes(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDRSGetBytes", namesAndVars, runMode, fileOutputStream,
 password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    if (connection_ != null)
      connection_.close();

    // SQL400 - driver neutral...
    String url = baseURL_ ;
    connection_ = testDriver_.getConnection(url + ";lob threshold=30000",systemObject_.getUserId(),encryptedPassword_,"JDRSGetBytesSetup");

    setAutoCommit(connection_, false); // @E1A

    statement0_ = connection_.createStatement();

    if (isJdbc20()) {
      try {
        statement_ = connection_.createStatement(
            ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      } catch (Exception e) {
        statement_ = connection_.createStatement();
      }
      statement_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_GET
          + " (C_KEY) VALUES ('DUMMY_ROW')");
      statementQuery_ = "SELECT * FROM " + JDRSTest.RSTEST_GET + " FOR UPDATE";
      rs_ = statement_.executeQuery(statementQuery_);
    }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    if (isJdbc20()) {
      rs_.close();
      statement_.close();
    }

    statement0_.close();
    connection_.commit(); // @E1A
    connection_.close();
  }

  /**
   * dumBytes() - Utility function used to see the bytes
   **/
  public static String dumpBytes(byte[] b) {
    return JDTestUtilities.dumpBytes(b);
  }

  /**
   * getBytes() - Should throw exception when the result set is closed.
   **/
  public void Var001() {
    try {
      Statement s = connection_.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next();
      rs.close();
      rs.getBytes(1);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getBytes() - Should throw exception when cursor is not pointing to a row.
   **/
  public void Var002() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      byte[] v = rs.getBytes(1);
      failed("Didn't throw SQLException but got " + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getBytes() - Should throw an exception when the column is an invalid index.
   **/
  public void Var003() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next();
      byte[] v = rs.getBytes(100);
      failed("Didn't throw SQLException " + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getBytes() - Should throw an exception when the column is 0.
   **/
  public void Var004() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next();
      byte[] v = rs.getBytes(0);
      failed("Didn't throw SQLException " + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getBytes() - Should throw an exception when the column is -1.
   **/
  public void Var005() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next();
      byte[] v = rs.getBytes(-1);
      failed("Didn't throw SQLException " + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getBytes() - Should work when the column index is valid.
   **/
  public void Var006() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "BINARY_NOTRANS");
      byte[] v = rs.getBytes(18);
      assertCondition(areEqual(v, twelve));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Should throw an exception when the column name is null.
   **/
  public void Var007() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable(
          "JCC throws null pointer exception when column name is null ");
    } else {

      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rs.next();
        rs.getBytes(null);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getBytes() - Should throw an exception when the column name is an empty
   * string.
   **/
  public void Var008() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next();
      rs.getBytes("");
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getBytes() - Should throw an exception when the column name is invalid.
   **/
  public void Var009() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next();
      rs.getBytes("INVALID");
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getBytes() - Should work when the column name is valid.
   **/
  public void Var010() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "BINARY_NOTRANS");
      byte[] v = rs.getBytes("C_BINARY_20");
      assertCondition(areEqual(v, eleven));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Should work when an update is pending.
   **/
  public void Var011() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, "UPDATE_SANDBOX");
        byte[] test = new byte[] { (byte) 0x34, (byte) 0x45, (byte) 0x50,
            (byte) 0x56, (byte) 0x67, (byte) 0x78, (byte) 0x89, (byte) 0x9A,
            (byte) 0xAB, (byte) 0xBC };
        rs_.updateBytes("C_VARBINARY_20", test);
        byte[] v = rs_.getBytes("C_VARBINARY_20");
        assertCondition(areEqual(v, test));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBytes() - Should work when an update has been done.
   **/
  public void Var012() {
    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, "UPDATE_SANDBOX");
        byte[] test = new byte[] { (byte) 0x01, (byte) 0x12, (byte) 0x34,
            (byte) 0x45, (byte) 0x50, (byte) 0x56, (byte) 0x67, (byte) 0x78,
            (byte) 0x89, (byte) 0x9A, (byte) 0xAB, (byte) 0xBC, (byte) 0xCD,
            (byte) 0xDE, (byte) 0xEF, (byte) 0xFF, (byte) 0x00, (byte) 0xFF,
            (byte) 0x00, (byte) 0xFF };
        rs_.updateBytes("C_BINARY_20", test);
        rs_.updateRow();
        byte[] v = rs_.getBytes("C_BINARY_20");
        assertCondition(areEqual(v, test));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBytes() - Should work when the current row is the insert row, when an
   * insert is pending.
   **/
  public void Var013() {
    if (checkJdbc20()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support moveToInserRow");
        return;
      }
      try {
        rs_.moveToInsertRow();
        byte[] test = new byte[] { (byte) 0xBC, (byte) 0xCD };
        rs_.updateBytes("C_VARBINARY_20", test);
        byte[] v = rs_.getBytes("C_VARBINARY_20");
        assertCondition(areEqual(v, test));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBytes() - Should work when the current row is the insert row, when an
   * insert has been done.
   **/
  public void Var014() {
    if (checkJdbc20()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support moveToInserRow");
        return;
      }
      try {
        rs_.moveToInsertRow();
        byte[] test = new byte[] { (byte) 0xBC, (byte) 0xCD, (byte) 0xDE,
            (byte) 0xEF, (byte) 0xFF, (byte) 0x00, (byte) 0xFF, (byte) 0x00,
            (byte) 0xFF };
        rs_.updateBytes("C_VARBINARY_20", test);
        rs_.insertRow();
        byte[] v = rs_.getBytes("C_VARBINARY_20");
        assertCondition(areEqual(v, test));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBytes() - Should throw an exception on a deleted row.
   **/
  public void Var015() {
    if (checkJdbc20()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC doesn't throw exception for get on deleted row");
        return;
      }
      try {
        JDRSTest.position(rs_, "DUMMY_ROW");
        rs_.deleteRow();
        byte[] v = rs_.getBytes("C_VARBINARY_20");
        failed("Didn't throw SQLException " + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getBytes() - Should return null when the column is NULL.
   **/
  public void Var016() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_NULL");
      byte[] v = rs.getBytes("C_VARBINARY_20");
      assertCondition(v == null, "expected null, did not get null");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from a SMALLINT.
   **/
  public void Var017() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_NEG");
      byte[] v = rs.getBytes("C_SMALLINT");
      byte[] expected = { (byte) 0xFF, (byte) 0x3A }; // SMALLINT -198
      assertCondition(areEqual(v, expected));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from a INTEGER.
   **/
  public void Var018() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      byte[] v = rs.getBytes("C_INTEGER");
      byte[] expected = { (byte) 0x00, (byte) 0x01, (byte) 0x81, (byte) 0xCD }; // INTEGER
                                                                                // 98765
      assertCondition(areEqual(v, expected));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from a REAL.
   **/
  public void Var019() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_NEG");
      byte[] v = rs.getBytes("C_REAL");
      byte[] expected = { (byte) 0xC0, (byte) 0x8C, (byte) 0xCC, (byte) 0xCD }; // REAL
                                                                                // -4.4
      StringBuffer sb = new StringBuffer();
      boolean condition = areEqual(v, expected, sb);
      assertCondition(condition, sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from a FLOAT.
   **/
  public void Var020() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      byte[] v = rs.getBytes("C_FLOAT");
      byte[] expected = { (byte) 0x40, (byte) 0x16, (byte) 0x33, (byte) 0x33,
          (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33 }; // FLOAT 5.55
      StringBuffer sb = new StringBuffer();
      boolean condition = areEqual(v, expected, sb);
      assertCondition(condition, sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from a DOUBLE.
   **/
  public void Var021() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_NEG");
      byte[] v = rs.getBytes("C_DOUBLE");
      byte[] expected = { (byte) 0xC0, (byte) 0x1A, (byte) 0xA9, (byte) 0xFB,
          (byte) 0xE7, (byte) 0x6C, (byte) 0x8B, (byte) 0x44 }; // DOUBLE -6.666
      assertCondition(areEqual(v, expected));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from a DECIMAL.
   * 
   * SQL400 - The Native JDBC driver now handles DECIMAL data directly instead
   * of relying on the NUMERIC data type. There is no difference between the
   * drivers here anymore.
   **/
  public void Var022() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      byte[] v = rs.getBytes("C_DECIMAL_50");

      byte[] expected = { (byte) 0x00, (byte) 0x00, (byte) 0x7F }; // DECIMAL(5,0)
                                                                   // 7

      assertCondition(areEqual(v, expected));

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from a NUMERIC.
   **/
  public void Var023() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_NEG");
      byte[] v = rs.getBytes("C_NUMERIC_105");
      byte[] expected = { (byte) 0xF0, (byte) 0xF0, (byte) 0xF0, (byte) 0xF1,
          (byte) 0xF0, (byte) 0xF1, (byte) 0xF0, (byte) 0xF1, (byte) 0xF0,
          (byte) 0xD5 }; // NUMERIC(10,5) -10.10105
      assertCondition(areEqual(v, expected));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from a CHAR.
   **/
  public void Var024() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      byte[] v = rs.getBytes("C_CHAR_50");
      byte[] expectedToolbox = { (byte) 0xE3, (byte) 0x96, (byte) 0x96,
          (byte) 0x93, (byte) 0x82, (byte) 0x96, (byte) 0xA7, (byte) 0x40,
          (byte) 0x86, (byte) 0x96, (byte) 0x99, (byte) 0x40, (byte) 0xD1,
          (byte) 0x81, (byte) 0xA5, (byte) 0x81, (byte) 0x40, (byte) 0x40,
          (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40,
          (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40,
          (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40,
          (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40,
          (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40,
          (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40,
          (byte) 0x40, (byte) 0x40 }; // CHAR(50) "Toolbox for Java"

      byte[] expectedNative = { (byte) 0xE3, (byte) 0x96, (byte) 0x96,
          (byte) 0x93, (byte) 0x82, (byte) 0x96, (byte) 0xA7, (byte) 0x40,
          (byte) 0x86, (byte) 0x96, (byte) 0x99, (byte) 0x40, (byte) 0xD1,
          (byte) 0x81, (byte) 0xA5, (byte) 0x81, (byte) 0x40, (byte) 0x40,
          (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40,
          (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40,
          (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40,
          (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40,
          (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40,
          (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40,
          (byte) 0x40, (byte) 0x40 };

      if (getDriver() == JDTestDriver.DRIVER_NATIVE
          && getRelease() >= JDTestDriver.RELEASE_V7R1M0)
        assertCondition(areEqual(v, expectedNative),
            " v = " + JDTestUtilities.dumpBytes(v) + " and expected = "
                + JDTestUtilities.dumpBytes(expectedNative));
      else
        assertCondition(areEqual(v, expectedToolbox),
            "(byte[] comparsion) digest v doesn't equal digest expectedToolbox");

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from a VARCHAR.
   * 
   * SQL400 - Because it is easiest for us right now, we are going to return a
   * varchar in a similar way to how varbinary is handled. That is we are going
   * to return the significant values instead of dropping the data exactly as is
   * in like the toolbox does. If this is ever an issue, we could probably
   * change but it just seems like a waste of time to play with it right now.
   **/
  public void Var025() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      byte[] v = rs.getBytes("C_VARCHAR_50");
      // byte[] expectedToolbox = { (byte)0xD1, (byte)0x81,
      // (byte)0xA5, (byte)0x81, (byte)0x40, (byte)0xE3,
      // (byte)0x96, (byte)0x96, (byte)0x93, (byte)0x82,
      // (byte)0x96, (byte)0xA7}; // VARCHAR(50) "Java Toolbox"

      byte[] expectedNative = { (byte) 0xD1, (byte) 0x81, (byte) 0xA5,
          (byte) 0x81, (byte) 0x40, (byte) 0xE3, (byte) 0x96, (byte) 0x96,
          (byte) 0x93, (byte) 0x82, (byte) 0x96, (byte) 0xA7 }; // VARCHAR(50)
                                                                // "Java
                                                                // Toolbox"

      // 12/22/2011 -- Native and toolbox are now the same.
      // Toolbox previous included the length on the front, which did not make
      // sence.

      assertCondition(areEqual(v, expectedNative),
          " v = " + JDTestUtilities.dumpBytes(v) + " and expected = "
              + JDTestUtilities.dumpBytes(expectedNative));

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from a BINARY.
   **/
  public void Var026() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "BINARY_NOTRANS");
      byte[] v = rs.getBytes("C_BINARY_20");
      assertCondition(areEqual(v, eleven));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from a VARBINARY.
   **/
  public void Var027() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "BINARY_NOTRANS");
      byte[] v = rs.getBytes("C_VARBINARY_20");
      assertCondition(areEqual(v, twelve));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from a CLOB.
   * 
   * SQL400 - The native driver will return the lob bytes here. At no point do
   * we externalize a locator today.
   **/
  public void Var028() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    if (checkLobSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_MEDIUM");
        byte[] v = rs.getBytes("C_CLOB");
        byte[] expected = { (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3,
            (byte) 0x40, (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99,
            (byte) 0x85, (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8,
            (byte) 0x40, (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3,
            (byte) 0x4b,

            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,

            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b,
            (byte) 0xd1, (byte) 0xc4, (byte) 0xc2, (byte) 0xc3, (byte) 0x40,
            (byte) 0x89, (byte) 0xa2, (byte) 0x40, (byte) 0x99, (byte) 0x85,
            (byte) 0x81, (byte) 0x93, (byte) 0x93, (byte) 0xa8, (byte) 0x40,
            (byte) 0x86, (byte) 0x81, (byte) 0xa2, (byte) 0xa3, (byte) 0x4b, }; // CLOB
                                                                                // "JDBC
                                                                                // is
                                                                                // really
                                                                                // fast."
                                                                                // repeated
                                                                                // 24
                                                                                // times

        byte[] expectedToolbox = { (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e, (byte) 0x4a, (byte) 0x44, (byte) 0x42,
            (byte) 0x43, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20,
            (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x6c, (byte) 0x6c,
            (byte) 0x79, (byte) 0x20, (byte) 0x66, (byte) 0x61, (byte) 0x73,
            (byte) 0x74, (byte) 0x2e,

        };

        // For the Toolbox JDBC driver, what comes back is a
        // locator. So we will get the bytes for the locator.
        // We will just make sure it is 4 bytes.
        //
        // For the Native JDBC driver, a locator is used as
        // well but what is returned is the lob data through
        // the use of the locator. At no point does the native
        // driver expose a locator to the user. For now, we just
        // compare the length.
        if (isToolboxDriver()) {
          expected = expectedToolbox;
        }

        assertCondition(v.length == 480 && areEqual(v, expected),
            "v.length = " + v.length + " SB 480" + "\nGot      =  "
                + JDTestUtilities.dumpBytes(v) + "\nExpected = "
                + JDTestUtilities.dumpBytes(expected));

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBytes() - Get from a DBCLOB.
   * 
   * SQL400 - The native driver will return the lob bytes here. At no point do
   * we externalize a locator today.
   **/
  public void Var029() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    if (checkLobSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_MEDIUM");
        byte[] v = rs.getBytes("C_DBCLOB");
        String expectedString = "DBCLOBs are best";
        int expectedStringLength = expectedString.length();
        byte[] expected = new byte[expectedStringLength * 2 * 24];
        for (int i = 0; i < 24; i++) {
          for (int j = 0; j < expectedStringLength; j++) {
            expected[i * 2 * expectedStringLength + 2 * j] = 0;
            expected[i * 2 * expectedStringLength + 2 * j
                + 1] = (byte) expectedString.charAt(j);
          }
        }
        // Updated 01/04/2012 to check against actual valule
        assertCondition(areEqual(v, expected),
            "\nGot: " + JDTestUtilities.dumpBytes(v) + "\nExp: "
                + JDTestUtilities.dumpBytes(expected));

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBytes() - Get from a BLOB.
   * 
   * SQL400 - The native driver will return the lob bytes here. At no point do
   * we externalize a locator today.
   **/
  public void Var030() {
    if (checkLobSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_MEDIUM");
        byte[] v = rs.getBytes("C_BLOB");

        // @E2D // For the Toolbox JDBC driver, what comes back is a
        // @E2D // locator. So we will get the bytes for the locator.
        // @E2D // We will just make sure it is 4 bytes.
        //
        // For the Native JDBC driver, a locator is used as
        // well but what is returned is the lob data through
        // the use of the locator. At no point does the native
        // driver expose a locator to the user.

        // @E2D if (getDriver () == JDTestDriver.DRIVER_NATIVE)
        assertCondition(areEqual(v, JDRSTest.BLOB_MEDIUM));
        // @E2D else
        // @E2D assertCondition(v.length == 4);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBytes() - Get from a DATE.
   * 
   * SQL400 - the internal representation of DATE and TIME fields is different
   * by default. These tests are changed to represent that.
   **/
  public void Var031() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_1998");
      byte[] v = rs.getBytes("C_DATE");

      byte[] expectedToolbox = { (byte) 0xF0, (byte) 0xF4, (byte) 0x61,
          (byte) 0xF0, (byte) 0xF8, (byte) 0x61, (byte) 0xF9, (byte) 0xF8,
          (byte) 0x40, (byte) 0x40 }; // DATE "04-08-98 "

      byte[] expectedToolboxV5R2 = { (byte) 0xF0, (byte) 0xF4, (byte) 0x61,
          (byte) 0xF0, (byte) 0xF8, (byte) 0x61, (byte) 0xF9, (byte) 0xF8 }; // DATE
                                                                             // "04-08-98"

      byte[] expectedNative = { (byte) 0xF1, (byte) 0xF9, (byte) 0xF9,
          (byte) 0xF8, (byte) 0x60, (byte) 0xF0, (byte) 0xF4, (byte) 0x60,
          (byte) 0xF0, (byte) 0xF8 }; // DATE "1998-04-08"

      if (getDriver() == JDTestDriver.DRIVER_NATIVE)
        assertCondition(areEqual(v, expectedNative));
      else {
        if (v.length == 8)
          assertCondition(areEqual(v, expectedToolboxV5R2));
        else
          assertCondition(areEqual(v, expectedToolbox));
      }

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from a TIME.
   * 
   * SQL400 - the internal representation of DATE and TIME fields is different
   * by default. These tests are changed to represent that.
   **/
  public void Var032() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_1998");
      byte[] v = rs.getBytes("C_TIME");

      byte[] expectedToolbox = { (byte) 0xF0, (byte) 0xF8, (byte) 0x7A,
          (byte) 0xF1, (byte) 0xF4, (byte) 0x7A, (byte) 0xF0, (byte) 0xF3 }; // TIME
                                                                             // "08:14:03"

      byte[] expectedNative = { (byte) 0xF0, (byte) 0xF8, (byte) 0x4B,
          (byte) 0xF1, (byte) 0xF4, (byte) 0x4B, (byte) 0xF0, (byte) 0xF3 }; // TIME
                                                                             // "08.14.03"

      if (getDriver() == JDTestDriver.DRIVER_NATIVE)
        assertCondition(areEqual(v, expectedNative));
      else
        assertCondition(areEqual(v, expectedToolbox));

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from a TIMESTAMP.
   **/
  public void Var033() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_1998");
      byte[] v = rs.getBytes("C_TIMESTAMP");
      byte[] expected = { (byte) 0xF1, (byte) 0xF9, (byte) 0xF9, (byte) 0xF8,
          (byte) 0x60, (byte) 0xF1, (byte) 0xF1, (byte) 0x60, (byte) 0xF1,
          (byte) 0xF8, (byte) 0x60, (byte) 0xF0, (byte) 0xF3, (byte) 0x4B,
          (byte) 0xF1, (byte) 0xF3, (byte) 0x4B, (byte) 0xF4, (byte) 0xF2,
          (byte) 0x4B, (byte) 0xF9, (byte) 0xF8, (byte) 0xF7, (byte) 0xF6,
          (byte) 0xF5, (byte) 0xF4 }; // TIMESTAMP "1998-11-18 03:13:42.987654"
      assertCondition(areEqual(v, expected));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from a DATALINK.
   **/
  public void Var034() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    if (checkDatalinkSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
        JDRSTest.position0(rs, "LOB_FULL");
        byte[] v = rs.getBytes("C_DATALINK");

        byte[] expectedToolbox = { (byte) 0x88,(byte) 0xA3,(byte) 0xA3,   
            (byte) 0x97,(byte) 0xA2,(byte) 0x7A,(byte) 0x61,(byte) 0x61,
            (byte) 0x87,(byte) 0x89,(byte) 0xA3,(byte) 0x88,(byte) 0xA4,
            (byte) 0x82,(byte) 0x4B,(byte) 0x83,(byte) 0x96,(byte) 0x94,
            (byte) 0x61,(byte) 0xC9,(byte) 0xC2,(byte) 0xD4,(byte) 0x61,
            (byte) 0xD1,(byte) 0xE3,(byte) 0xD6,(byte) 0x97,(byte) 0x85,
            (byte) 0x95,(byte) 0x60,(byte) 0xA3,(byte) 0x85,(byte) 0xA2,
            (byte) 0xA3,(byte) 0x61,(byte) 0x82,(byte) 0x93,(byte) 0x96,
            (byte) 0x82,(byte) 0x61,(byte) 0x94,(byte) 0x81,(byte) 0x89,
            (byte) 0x95,(byte) 0x61,(byte) 0xD9,(byte) 0xC5,(byte) 0xC1,
            (byte) 0xC4,(byte) 0xD4,(byte) 0xC5,(byte) 0x4B,(byte) 0xA3,
            (byte) 0x85,(byte) 0xA2,(byte) 0xA3,(byte) 0x89,(byte) 0x95,
            (byte) 0x87,(byte) 0x4B,(byte) 0xA3,(byte) 0xA7,(byte) 0xA3,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,(byte) 0x40,
            (byte) 0x40,(byte) 0x40,
          }; // DATALINK
                                        // "https://github.com/IBM/JTOpen-test/blob/main/README.testing.txt"
                                        // 200 characters + 2 for the length.
        // toolbox: in 550, if VLC is on, then the ending x40 bytes are gone,
        // but the length is still the same.
        // toolbox: in pre 550, getbytes() ignores the length and return the
        // length of the column.
        if ((isToolboxDriver())
            && (getRelease() >= JDTestDriver.RELEASE_V7R1M0)) {
          // 42 bytes
          byte[] tmp = new byte[40];
          System.arraycopy(expectedToolbox, 0, tmp, 0, 40);
          expectedToolbox = tmp;
        }

        byte[] expectedNative = { (byte) 0xc8,(byte) 0xe3,(byte) 0xe3,   
            (byte) 0xd7,(byte) 0xe2,(byte) 0x7A,(byte) 0x61,(byte) 0x61,
            (byte) 0xc7,(byte) 0xc9,(byte) 0xe3,(byte) 0xc8,(byte) 0xe4,
            (byte) 0xc2,(byte) 0x4B,(byte) 0xc3,(byte) 0xd6,(byte) 0xd4,
            (byte) 0x61,(byte) 0xC9,(byte) 0xC2,(byte) 0xD4,(byte) 0x61,
            (byte) 0xD1,(byte) 0xE3,(byte) 0xD6,(byte) 0x97,(byte) 0x85,
            (byte) 0x95,(byte) 0x60,(byte) 0xA3,(byte) 0x85,(byte) 0xA2,
            (byte) 0xA3,(byte) 0x61,(byte) 0x82,(byte) 0x93,(byte) 0x96,
            (byte) 0x82,(byte) 0x61,(byte) 0x94,(byte) 0x81,(byte) 0x89,
            (byte) 0x95,(byte) 0x61,(byte) 0xD9,(byte) 0xC5,(byte) 0xC1,
            (byte) 0xC4,(byte) 0xD4,(byte) 0xC5,(byte) 0x4B,(byte) 0xA3,
            (byte) 0x85,(byte) 0xA2,(byte) 0xA3,(byte) 0x89,(byte) 0x95,
            (byte) 0x87,(byte) 0x4B,(byte) 0xA3,(byte) 0xA7,(byte) 0xA3,
            }; // "https://github.com/IBM/JTOpen-test/blob/main/README.testing.txt"

        // if (isToolboxDriver())
        // assertCondition (isEqual (v, expectedToolbox));
        // else
        assertCondition(areEqual(v, expectedNative),
            "\n v              = " + JDTestUtilities.dumpBytes(v) + 
            "\n expectedNative = "
                + JDTestUtilities.dumpBytes(expectedNative));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBytes() - Get from a DISTINCT.
   **/
  public void Var035() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    if (checkLobSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_FULL");
        byte[] v = rs.getBytes("C_DISTINCT");
        byte[] expected = { (byte) 0xF1, (byte) 0xF2, (byte) 0xF3, (byte) 0xF4,
            (byte) 0xF5, (byte) 0xF6, (byte) 0xF7, (byte) 0xF8, (byte) 0xF9 }; // DISTINCT
                                                                               // "123456789"
        // unicode bytes

        assertCondition(areEqual(v, expected),
            " v = " + JDTestUtilities.dumpBytes(v) + " and expected = "
                + JDTestUtilities.dumpBytes(expected));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBytes() - Get from a BIGINT.
   **/
  public void Var036() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    if (checkBigintSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "NUMBER_POS");
        byte[] v = rs.getBytes("C_BIGINT");
        byte[] expected = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0xBC, (byte) 0xD1, (byte) 0x31 }; // BIGINT
                                                                  // 12374321
        assertCondition(areEqual(v, expected));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBytes() - Verify that no DataTruncation is posted when the max field
   * size is set to 0.
   **/
  public void Var037() {
    try {
      Statement s = connection_.createStatement();
      s.setMaxFieldSize(0);
      ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "BINARY_NOTRANS");
      byte[] v = rs.getBytes("C_VARBINARY_20");
      DataTruncation dt = (DataTruncation) rs.getWarnings();
      rs.close();
      s.close();
      assertCondition((areEqual(v, twelve)) && (dt == null));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Verify that data is truncated without a DataTruncation posted
   * when the max field size is set to a value shorter than the byte array.
   **/
  public void Var038() {
    try {
      Statement s = connection_.createStatement();
      s.setMaxFieldSize(18);
      ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "BINARY_NOTRANS");
      byte[] v = rs.getBytes("C_BINARY_20");
      SQLWarning w = rs.getWarnings();
      rs.close();
      s.close();
      byte[] expected = { (byte) 'E', (byte) 'l', (byte) 'e', (byte) 'v',
          (byte) 'e', (byte) 'n', (byte) ' ', (byte) ' ', (byte) ' ',
          (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
          (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ' };
      assertCondition((areEqual(v, expected)) && (w == null));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Verify that getBytes() does not work on values in a Database
   * MetaData result set..
   * 
   * SQL400 - The Native JDBC driver will work to do a getBytes with every
   * metadata result set except for those created with getTypes(). We will not
   * verify the value here as the results of the getTables() call could change
   * over time. Add a variation to verify that that will fail correctly.
   **/
  public void Var039() {
    try {
      DatabaseMetaData dmd = connection_.getMetaData();
      ResultSet rs = dmd.getTables(null, null, null, null);
      rs.next();
      byte[] v = rs.getBytes("TABLE_NAME");

      if (getDriver() == JDTestDriver.DRIVER_NATIVE || isToolboxDriver()
          || getDriver() == JDTestDriver.DRIVER_JTOPENLITE) // toolbox sysibm
                                                            // update
        assertCondition(true);
      else
        failed("Didn't throw SQLException " + v);

    } catch (Exception e) {
      if (getDriver() == JDTestDriver.DRIVER_NATIVE)
        failed(e,
            "Unexpected Exception calling rs.getBytes from dmd.getTables");
      else
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  public void Var040() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GRAPHIC);
        JDRSTest.position0(rs, "GRAPHIC_FULL");
        byte[] v = rs.getBytes("C_GRAPHIC");

        byte[] expectedToolbox = { (byte) 0x00, (byte) 0x54, (byte) 0x00,
            (byte) 0x4f, (byte) 0x00, (byte) 0x4f, (byte) 0x00, (byte) 0x4c,
            (byte) 0x00, (byte) 0x42, (byte) 0x00, (byte) 0x4f, (byte) 0x00,
            (byte) 0x58, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x46,
            (byte) 0x00, (byte) 0x4f, (byte) 0x00, (byte) 0x52, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x4a, (byte) 0x00, (byte) 0x41,
            (byte) 0x00, (byte) 0x56, (byte) 0x00, (byte) 0x41, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20 };

        byte[] expectedNative = { (byte) 0x00, (byte) 0x54, (byte) 0x00,
            (byte) 0x4f, (byte) 0x00, (byte) 0x4f, (byte) 0x00, (byte) 0x4c,
            (byte) 0x00, (byte) 0x42, (byte) 0x00, (byte) 0x4f, (byte) 0x00,
            (byte) 0x58, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x46,
            (byte) 0x00, (byte) 0x4f, (byte) 0x00, (byte) 0x52, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x4a, (byte) 0x00, (byte) 0x41,
            (byte) 0x00, (byte) 0x56, (byte) 0x00, (byte) 0x41, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20 };

        if (getDriver() == JDTestDriver.DRIVER_NATIVE)
          assertCondition(areEqual(v, expectedNative),
              "\nv        = " + JDTestUtilities.dumpBytes(v) + "\nexpected = "
                  + JDTestUtilities.dumpBytes(expectedNative));
        else
          assertCondition(areEqual(v, expectedToolbox),
              "\nv        = " + JDTestUtilities.dumpBytes(v) + "\nexpected = "
                  + JDTestUtilities.dumpBytes(expectedToolbox));
      } else
        notApplicable();

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  public void Var041() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GRAPHIC);
        JDRSTest.position0(rs, "GRAPHIC_FULL");
        byte[] v = rs.getBytes("C_VARGRAPHIC");

        byte[] expectedToolbox = { (byte) 0x00, (byte) 0x4A, (byte) 0x00,
            (byte) 0x41, (byte) 0x00, (byte) 0x56, (byte) 0x00, (byte) 0x41,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x54, (byte) 0x00,
            (byte) 0x4F, (byte) 0x00, (byte) 0x4F, (byte) 0x00, (byte) 0x4C,
            (byte) 0x00, (byte) 0x42, (byte) 0x00, (byte) 0x4F, (byte) 0x00,
            (byte) 0x58, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00,
            (byte) 0x20, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x20,
            (byte) 0x00, (byte) 0x20 };

        // toolbox: in 550, if VLC is on, then the ending x0020 bytes are gone,
        // but the length is still the same.
        // toolbox: in pre 550, getbytes() ignores the length and return the
        // length of the column.
        if ((isToolboxDriver())
            && (getRelease() >= JDTestDriver.RELEASE_V7R1M0)) {
          // 26 bytes
          byte[] tmp = new byte[26];
          System.arraycopy(expectedToolbox, 0, tmp, 0, 26);
          expectedToolbox = tmp;
        }

        byte[] expectedNative53 = { (byte) 0x00, (byte) 0x4a, (byte) 0x00,
            (byte) 0x41, (byte) 0x00, (byte) 0x56, (byte) 0x00, (byte) 0x41,
            (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x54, (byte) 0x00,
            (byte) 0x4f, (byte) 0x00, (byte) 0x4f, (byte) 0x00, (byte) 0x4c,
            (byte) 0x00, (byte) 0x42, (byte) 0x00, (byte) 0x4f, (byte) 0x00,
            (byte) 0x58 };

        assertCondition(areEqual(v, expectedNative53),
            " v = " + JDTestUtilities.dumpBytes(v) + " and expected = "
                + JDTestUtilities.dumpBytes(expectedNative53)
                + " Updated 12/22/2011 -- toolbox and native now the same");
      } else
        notApplicable();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  public void Var042() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GRAPHIC);
        JDRSTest.position0(rs, "GRAPHIC_FULL");
        byte[] v = rs.getBytes("C_GRAPHIC_835");

        byte[] expectedToolbox = { (byte) 0x4D, (byte) 0x6C, (byte) 0x4D,
            (byte) 0x71, (byte) 0x4D, (byte) 0x7D, (byte) 0x50, (byte) 0x5B,
            (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40,
            (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40,
            (byte) 0x40, (byte) 0x40 }; // Graphic(50) ccsid 835

        byte[] expectedNative = { (byte) 0x5e, (byte) 0x03, (byte) 0x5f,
            (byte) 0x17, (byte) 0x67, (byte) 0x2b, (byte) 0x53, (byte) 0x78,
            (byte) 0x30, (byte) 0x00, (byte) 0x30, (byte) 0x00, (byte) 0x30,
            (byte) 0x00, (byte) 0x30, (byte) 0x00, (byte) 0x30, (byte) 0x00,
            (byte) 0x30, (byte) 0x00 };

        if (getDriver() == JDTestDriver.DRIVER_NATIVE)
          assertCondition(areEqual(v, expectedNative),
              "\nv        = " + JDTestUtilities.dumpBytes(v) + "\nexpected = "
                  + JDTestUtilities.dumpBytes(expectedNative));
        else
          assertCondition(areEqual(v, expectedToolbox),
              "\nv        = " + JDTestUtilities.dumpBytes(v) + "\nexpected = "
                  + JDTestUtilities.dumpBytes(expectedToolbox));
      } else
        notApplicable();

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  public void Var043() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns invalid data conversion");
      return;
    }
    try {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GRAPHIC);
        JDRSTest.position0(rs, "GRAPHIC_FULL");
        byte[] v = rs.getBytes("C_VARGRAPHIC_835");

        // Updated expectedToolbox 12/22/2011 (removed length and padding)
        byte[] expectedToolbox = { (byte) 0x4D, (byte) 0x6C, (byte) 0x4D,
            (byte) 0x71, (byte) 0x4D, (byte) 0x7D, (byte) 0x50, (byte) 0x5B }; // Graphic(50)
                                                                               // ccsid
                                                                               // 835

        byte[] expectedNative = { (byte) 0x5e, (byte) 0x03, (byte) 0x5f,
            (byte) 0x17, (byte) 0x67, (byte) 0x2b, (byte) 0x53, (byte) 0x78 };

        if (getDriver() == JDTestDriver.DRIVER_NATIVE)
          assertCondition(areEqual(v, expectedNative),
              " v = " + JDTestUtilities.dumpBytes(v) + " and expected = "
                  + JDTestUtilities.dumpBytes(expectedNative));
        else
          assertCondition(areEqual(v, expectedToolbox),
              "v = " + JDTestUtilities.dumpBytes(v) + " and expected = "
                  + JDTestUtilities.dumpBytes(expectedToolbox));
      } else
        notApplicable();

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getBytes() - Get from DFP16:
   **/
  public void Var044() {
    if (checkDecFloatSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP16);
        rs.next();
        byte[] v = rs.getBytes(1);
        if ((!isToolboxDriver()))
          failed("Didn't throw SQLException " + v);
        else
          succeeded();
      } catch (Exception e) {
        /*
         * JCC running to LUW throws
         * com.ibm.db2.jcc.am.ColumnTypeConversionException:
         * [ibm][db2][jcc][1092][11638] Invalid data conversion: Wrong result
         * column type for requested conversion.
         */
        if ((!isToolboxDriver()))
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        else
          failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBytes() - Get from DFP34:
   **/
  public void Var045() {
    if (checkDecFloatSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP34);
        rs.next();
        byte[] v = rs.getBytes(1);
        if ((!isToolboxDriver()))
          failed("Didn't throw SQLException " + v);
        else
          succeeded();
      } catch (Exception e) {
        /*
         * JCC running to LUW throws
         * com.ibm.db2.jcc.am.ColumnTypeConversionException:
         * [ibm][db2][jcc][1092][11638] Invalid data conversion: Wrong result
         * column type for requested conversion.
         */
        if ((!isToolboxDriver()))
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        else
          failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getByte() - Get from an SQLXML.
   **/
  public void Var046() {
    if (checkXmlSupport()) {
      try {
        Statement stmt = connection_.createStatement();
        ResultSet rs = stmt
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETXML);
        rs.next();
        byte[] v = rs.getBytes("C_XML");
        byte[] expected = { (byte) 0x3c, (byte) 0x3f, (byte) 0x78, (byte) 0x6d,
            (byte) 0x6c, (byte) 0x20, (byte) 0x76, (byte) 0x65, (byte) 0x72,
            (byte) 0x73, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x3d,
            (byte) 0x22, (byte) 0x31, (byte) 0x2e, (byte) 0x30, (byte) 0x22,
            (byte) 0x20, (byte) 0x65, (byte) 0x6e, (byte) 0x63, (byte) 0x6f,
            (byte) 0x64, (byte) 0x69, (byte) 0x6e, (byte) 0x67, (byte) 0x3d,
            (byte) 0x22, (byte) 0x55, (byte) 0x54, (byte) 0x46, (byte) 0x2d,
            (byte) 0x38, (byte) 0x22, (byte) 0x3f, (byte) 0x3e,

            (byte) 0x3c, (byte) 0x67, (byte) 0x72, (byte) 0x65, (byte) 0x65,
            (byte) 0x74, (byte) 0x69, (byte) 0x6e, (byte) 0x67, (byte) 0x3e,
            (byte) 0x48, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f,
            (byte) 0x2c, (byte) 0x20, (byte) 0x77, (byte) 0x6f, (byte) 0x72,
            (byte) 0x6c, (byte) 0x64, (byte) 0x21, (byte) 0x3c, (byte) 0x2f,
            (byte) 0x67, (byte) 0x72, (byte) 0x65, (byte) 0x65, (byte) 0x74,
            (byte) 0x69, (byte) 0x6e, (byte) 0x67, (byte) 0x3e };

        assertCondition(areEqual(v, expected),
            "\n         = " + JDTestUtilities.dumpBytes(v) + "\n         = "
                + JDTestUtilities.dumpBytesAsAscii(v) + "\nexpected = "
                + JDTestUtilities.dumpBytes(expected) + "\n         = "
                + JDTestUtilities.dumpBytesAsAscii(expected));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");

      }

    }

  }

  /**
   * getByte() - Get from a CCSID 1208 clob
   **/
  public void Var047() {
    String added = " -- Added 5/15/2008 by native driver -- Fix made in V7R1 to retrieveLobFromLocator with getRawBytesForUTF8 flag.  Not PTF'd back to previous releases because of fix complexity.";
    if (checkLobSupport()) {
      if (checkRelease710()) {
        try {
          Statement stmt = connection_.createStatement();
          ResultSet rs = stmt.executeQuery(
              "SELECT CAST('HELLO' AS CLOB(1M) CCSID 1208) FROM SYSIBM.SYSDUMMY1");
          rs.next();
          byte[] v = rs.getBytes(1);
          // Toolbox by design a getBytes() of a clob (or char) reads the bytes
          // from the stream
          // Strangely, any other getBinaryStream (or other binary) converts the
          // strings to hex ("010A0F" -> {1,A,F}}
          // So since this is not JDBC "per spec" usage, just call getString()
          if (isToolboxDriver()) {
            String st = rs.getString(1);// "HELLO"
            v = st.getBytes("UTF-8");
          }

          byte[] expected = { (byte) 0x48, (byte) 0x45, (byte) 0x4c,
              (byte) 0x4c, (byte) 0x4f };

          assertCondition(areEqual(v, expected),
              "\n         = " + JDTestUtilities.dumpBytes(v) + "\n         = "
                  + JDTestUtilities.dumpBytesAsAscii(v) + "\nexpected = "
                  + JDTestUtilities.dumpBytes(expected) + "\n         = "
                  + JDTestUtilities.dumpBytesAsAscii(expected) + "\n" + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception" + added);

        }

      }
    }
  }

  /**
   * getBytes() - Get from a TIMESTAMP(12).
   **/
  public void Var048() {
    if (checkTimestamp12Support()) {
      testGet(statement0_,
          "SELECT CAST('1998-11-18 03:13:42.987654321098' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
          "getBytes",
          "F1F9F9F860F1F160F1F860F0F34BF1F34BF4F24BF9F8F7F6F5F4F3F2F1F0F9F8",
          " -- added 11/19/2012");

    }
  }

  /**
   * getBytes() - Get from a TIMESTAMP(0).
   **/
  public void Var049() {
    if (checkTimestamp12Support()) {
      testGet(statement0_,
          "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
          "getBytes", "F1F9F9F860F1F160F1F860F0F34BF1F34BF4F2",
          " -- added 11/19/2012");

    }
  }

  /**
   * getBytes() - Get from a BOOLEAN.
   **/
  public void Var050() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_TRUE");
        byte[] v = rs.getBytes("C_BOOLEAN");
        byte[] expected = { (byte) 0xF1 }; 
        assertCondition(areEqual(v, expected));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBytes() - Get from a BOOLEAN.
   **/
  public void Var051() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_FALSE");
        byte[] v = rs.getBytes("C_BOOLEAN");
        byte[] expected = { (byte) 0xF0 }; 
        assertCondition(areEqual(v, expected));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getBytes() - Get from a BOOLEAN.
   **/
  public void Var052() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_NULL");
        byte[] v = rs.getBytes("C_BOOLEAN");
        assertCondition(v == null);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

}
