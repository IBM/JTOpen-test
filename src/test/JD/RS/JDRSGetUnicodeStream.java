///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetUnicodeStream.java
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
import test.Testcase;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DataTruncation;

import java.sql.ResultSet;

import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;

/**
 * Testcase JDRSGetUnicodeStream. This tests the following method of the JDBC
 * ResultSet class:
 * 
 * <ul>
 * <li>getUnicodeStream()
 * </ul>
 **/
public class JDRSGetUnicodeStream extends JDTestcase {

  // Private data.
  private Statement statement_;
  private Statement statement0_;
  private ResultSet rs_;

  private Connection connection2_;
  private Statement statement2_;
  private boolean isJDK14;
  StringBuffer sb = new StringBuffer(); 
  private static final byte[] eleven = { (byte) 'E', (byte) 'l', (byte) 'e',
      (byte) 'v', (byte) 'e', (byte) 'n', (byte) ' ', (byte) ' ', (byte) ' ',
      (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
      (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ' };

  private static final byte[] twelve = { (byte) 'T', (byte) 'w', (byte) 'e',
      (byte) 'l', (byte) 'v', (byte) 'e' };

  /**
   * Constructor.
   **/
  public JDRSGetUnicodeStream(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDRSGetUnicodeStream", namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {

	if (connection_ != null) connection_.close();
    // 
    // look for jdk1.4
    //
        isJDK14 = true;

    // SQL400 - driver neutral...
    String url = baseURL_
    // String url = "jdbc:as400://" + systemObject_.getSystemName()
         ;
    connection_ = testDriver_.getConnection (url + ";time format=jis;lob threshold=30000",systemObject_.getUserId(), encryptedPassword_);
    setAutoCommit(connection_, false); // @E1A

    statement0_ = connection_.createStatement();

    if (isJdbc20()) {
      statement_ = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
          ResultSet.CONCUR_UPDATABLE);
      statement_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_GET
          + " (C_KEY) VALUES ('DUMMY_ROW')");
      rs_ = statement_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET
          + " FOR UPDATE");
    }

    // Force LOB locators.
    connection2_ = testDriver_.getConnection (url + ";lob threshold=0",systemObject_.getUserId(), encryptedPassword_);
    setAutoCommit(connection2_,false); // @E1A

    statement2_ = connection2_.createStatement();
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    statement2_.close();
    connection2_.commit(); // @E1A
    connection2_.close();

    if (isJdbc20()) {
      rs_.close();
      statement_.close();
    }
    statement0_.close();
    connection_.commit(); // @E1A
    connection_.close();
  }

  protected static boolean compareBeginsWithBytes(InputStream i, byte[] b, StringBuffer sb) // @K2
  // this compare doesn't check if inputStream has more bytes or not beyond
  // containing b
  // unlike JDTestcase.compare which see thats inputStream should contain only b
  // and nothing
  // beyond it.
  // this exclusion is esp. needed for native driver from v5r3m0 onwards
  {
    try {
      byte[] buf = new byte[b.length];
      int num = i.read(buf, 0, buf.length);
      if (num == -1)
        return b.length == 0;
      int total = num;
      while (num > -1 && total < buf.length) {
        num = i.read(buf, total, buf.length - total);
        total += num;
      }
      if (num == -1)
        --total;
      return total == b.length && Testcase.areEqual(b, buf);
    } catch (java.io.IOException e) {
      return false;
    }
  }

  /**
   * getUnicodeStream() - Should throw exception when the result set is closed.
   **/
  public void Var001() {
    try {
      Statement s = connection_.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next();
      rs.close();
      rs.getUnicodeStream(1);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getUnicodeStream() - Should throw exception when cursor is not pointing to
   * a row.
   **/
  public void Var002() {
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      InputStream v = rs.getUnicodeStream(1);
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getUnicodeStream() - Should throw an exception when the column is an
   * invalid index.
   **/
  public void Var003() {
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      InputStream v = rs.getUnicodeStream(100);
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getUnicodeStream() - Should throw an exception when the column is 0.
   **/
  public void Var004() {
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      InputStream v = rs.getUnicodeStream(0);
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getUnicodeStream() - Should throw an exception when the column is -1.
   **/
  public void Var005() {
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      InputStream v = rs.getUnicodeStream(-1);
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getUnicodeStream() - Should work when the column index is valid.
   **/
  public void Var006() {
    try {
      sb.setLength(0); 
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      InputStream v = rs.getUnicodeStream(12);
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
          getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
        assertCondition(compareBeginsWithBytes(v,
            "Toolbox for Java                                  "
                .getBytes("UnicodeBigUnmarked"),sb),sb); // @K2
      else
        // @K2
        assertCondition(compare(v,
            "Toolbox for Java                                  ",
            "UnicodeBigUnmarked",sb),sb); // @B0C // @K2
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUnicodeStream() - Should throw an exception when the column name is
   * null.
   **/
  public void Var007() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC throws null pointer exception when column name is null ");
    } else {

      try {
        ResultSet rs = statement0_.executeQuery("SELECT * FROM "
            + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "CHAR_FULL");
        rs.getUnicodeStream(null);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getUnicodeStream() - Should throw an exception when the column name is an
   * empty string.
   **/
  public void Var008() {
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      rs.getUnicodeStream("");
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getUnicodeStream() - Should throw an exception when the column name is
   * invalid.
   **/
  public void Var009() {
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      rs.getUnicodeStream("INVALID");
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getUnicodeStream() - Should throw an exception when the column name is
   * valid.
   **/
  public void Var010() {
    sb.setLength(0); 

    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      InputStream v = rs.getUnicodeStream("C_CHAR_50");
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
          getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
        assertCondition(compareBeginsWithBytes(v,
            "Toolbox for Java                                  "
                .getBytes("UnicodeBigUnmarked"),sb),sb); // @K2
      else
        // @K2
        assertCondition(compare(v,
            "Toolbox for Java                                  ",
            "UnicodeBigUnmarked",sb),sb); // @B0C
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUnicodeStream() - Should work when an update is pending.
   **/
  public void Var011() {
    sb.setLength(0); 

    if (checkJdbc20()) {
      try {
        JDRSTest.position(rs_, "UPDATE_SANDBOX");
        rs_.updateString("C_VARCHAR_50", "World Peace");
        InputStream v = rs_.getUnicodeStream("C_VARCHAR_50");
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
            getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
          assertCondition(compareBeginsWithBytes(v, "World Peace"
              .getBytes("UnicodeBigUnmarked"),sb),sb); // @K2
        else
          // @K2
          assertCondition(compare(v, "World Peace", "UnicodeBigUnmarked",sb),sb); // @B0C
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getUnicodeStream() - Should work when an update has been done.
   **/
  public void Var012() {
    if (checkJdbc20()) {
      sb.setLength(0); 

      try {
        JDRSTest.position(rs_, "UPDATE_SANDBOX");
        rs_.updateString("C_CHAR_50", "New Planet");
        rs_.updateRow();
        InputStream v = rs_.getUnicodeStream("C_CHAR_50");
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
            getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
          assertCondition(compareBeginsWithBytes(v,
              "New Planet                                        "
                  .getBytes("UnicodeBigUnmarked"),sb),sb); // @K2
        else
          // @K2
          assertCondition(compare(v,
              "New Planet                                        ",
              "UnicodeBigUnmarked",sb),sb); // @B0C
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getUnicodeStream() - Should work when the current row is the insert row,
   * when an insert is pending.
   **/
  public void Var013() {
    if (checkJdbc20()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support moveToInserRow");
        return;
      }
      sb.setLength(0); 

      try {
        rs_.moveToInsertRow();
        rs_.updateString("C_VARCHAR_50", "El Nino");
        InputStream v = rs_.getUnicodeStream("C_VARCHAR_50");
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
            getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
          assertCondition(compareBeginsWithBytes(v, "El Nino"
              .getBytes("UnicodeBigUnmarked"),sb),sb); // @K2
        else
          // @K2
          assertCondition(compare(v, "El Nino", "UnicodeBigUnmarked",sb),sb); // @B0C
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getUnicodeStream() - Should work when the current row is the insert row,
   * when an insert has been done.
   **/
  public void Var014() {
    if (checkJdbc20()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support moveToInserRow");
        return;
      }
      sb.setLength(0); 

      try {
        rs_.moveToInsertRow();
        rs_.updateString("C_VARCHAR_50", "Year 2000 Problem");
        rs_.insertRow();
        InputStream v = rs_.getUnicodeStream("C_VARCHAR_50");
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
            getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
          assertCondition(compareBeginsWithBytes(v, "Year 2000 Problem"
              .getBytes("UnicodeBigUnmarked"),sb),sb); // @K2
        else
          // @K2
          assertCondition(compare(v, "Year 2000 Problem", "UnicodeBigUnmarked",sb),sb); // @B0C
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getUnicodeStream() - Should throw an exception on a deleted row.
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
        InputStream v = rs_.getUnicodeStream("C_VARCHAR_50");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getUnicodeStream() - Should return null when the column is NULL.
   **/
  public void Var016() {
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_NULL");
      InputStream v = rs.getUnicodeStream("C_VARCHAR_50");
      rs.close(); 
      assertCondition(v == null);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUnicodeStream() - Get from a SMALLINT.
   **/
  public void Var017() {
    sb.setLength(0); 
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      InputStream v = rs.getUnicodeStream("C_SMALLINT");

      String expected = "198";
      boolean check = compareUnicodeStream(v, expected, sb);
      rs.close(); 
      assertCondition(check, sb.toString() + " Updated 11/17/2011");

    } catch (Exception e) {
      failed(e, "Unexpected Exception -- updated 11/17/2011 ");
    }
  }

  /**
   * getUnicodeStream() - Get from a INTEGER.
   **/
  public void Var018() {

    sb.setLength(0); 
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);

      JDRSTest.position0(rs, "NUMBER_NEG");

      InputStream v = rs.getUnicodeStream("C_INTEGER");
      String expected = "-98765";
      boolean check = compareUnicodeStream(v, expected, sb);
      rs.close(); 
      assertCondition(check, sb.toString() + " Updated 11/17/2011");

    } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
    }
  }

  /**
   * getUnicodeStream() - Get from a REAL.
   **/
  public void Var019() {
    sb.setLength(0); 
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "NUMBER_POS");
	InputStream v = rs.getUnicodeStream("C_REAL");
        String expected = "4.4";
        boolean check = compareUnicodeStream(v, expected, sb);
        assertCondition(check, sb.toString() + " Updated 11/17/2011");

    } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
    }
  }

  /**
   * getUnicodeStream() - Get from a FLOAT.
   **/
  public void Var020() {
    sb.setLength(0); 
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);

        JDRSTest.position0(rs, "NUMBER_NEG");
	InputStream v = rs.getUnicodeStream("C_FLOAT");

        String expected = "-5.55";
        boolean check = compareUnicodeStream(v, expected, sb);
        assertCondition(check, sb.toString() + " Updated 11/17/2011");

    } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
    }
  }

  /**
   * getUnicodeStream() - Get from a DOUBLE.
   **/
  public void Var021() {
    sb.setLength(0); 
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_NEG");
      InputStream v = rs.getUnicodeStream("C_DOUBLE");
        String expected = "-6.666";
        boolean check = compareUnicodeStream(v, expected, sb );
        assertCondition(check, sb.toString() + " Updated 11/17/2011");

    } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
    }
  }

  /**
   * getUnicodeStream() - Get from a DECIMAL.
   **/
  public void Var022() {
    sb.setLength(0); 
    sb.append(" -- getUnicodeStream from decimal -- updated 12/14/2011"); 
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      InputStream v = rs.getUnicodeStream("C_DECIMAL_50");                //@F1A
          // @K2
          assertCondition(compare(v, "7", "UnicodeBigUnmarked", sb), sb); // @F1A
    } catch (Exception e) {
        failed(e, "Unexpected Exception"+sb.toString()); // @F1A
    }
  }

  /**
   * getUnicodeStream() - Get from a NUMERIC.
   **/
  public void Var023() {
    sb.setLength(0); 
    sb.append(" -- getUnicodeStream from numeric -- updated 12/14/2011"); 

    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_NEG");
        InputStream v = rs.getUnicodeStream("C_NUMERIC_105"); // @F1A
          // @K2
          assertCondition(compare(v, "-10.10105", "UnicodeBigUnmarked", sb), sb); // @F1A
    } catch (Exception e) {
        failed(e, "Unexpected Exception "+sb.toString()); // @F1A
    }
  }

  /**
   * getUnicodeStream() - Get from an empty CHAR.
   **/
  public void Var024() {
    sb.setLength(0);

    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_EMPTY");
      InputStream v = rs.getUnicodeStream("C_CHAR_50");
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
          getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
        assertCondition(compareBeginsWithBytes(v,
            "                                                  "
                .getBytes("UnicodeBigUnmarked"),sb),sb); // @K2
      else
        // @K2
        assertCondition(compare(v,
            "                                                  ",
            "UnicodeBigUnmarked",sb),sb); // @B0C
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUnicodeStream() - Get from a full CHAR.
   **/
  public void Var025() {
    sb.setLength(0);

    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      InputStream v = rs.getUnicodeStream("C_CHAR_50");
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
          getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
        assertCondition(compareBeginsWithBytes(v,
            "Toolbox for Java                                  "
                .getBytes("UnicodeBigUnmarked"),sb)); // @K2
      else
        // @K2
        assertCondition(compare(v,
            "Toolbox for Java                                  ",
            "UnicodeBigUnmarked",sb),sb); // @B0C
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUnicodeStream() - Get from an empty VARCHAR.
   **/
  public void Var026() {
    sb.setLength(0);

    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_EMPTY");
      InputStream v = rs.getUnicodeStream("C_VARCHAR_50");
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
          getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
        assertCondition(compareBeginsWithBytes(v, ""
            .getBytes("UnicodeBigUnmarked"),sb),sb); // @K2
      else
        // @K2
        assertCondition(compare(v, "", "UnicodeBigUnmarked",sb),sb); // @B0C
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUnicodeStream() - Get from a full VARCHAR.
   **/
  public void Var027() {
    try {
      sb.setLength(0);

      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      InputStream v = rs.getUnicodeStream("C_VARCHAR_50");
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
          getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
        assertCondition(compareBeginsWithBytes(v, "Java Toolbox"
            .getBytes("UnicodeBigUnmarked"),sb),sb); // @K2
      else
        // @K2
        assertCondition(compare(v, "Java Toolbox", "UnicodeBigUnmarked",sb),sb); // @B0C
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUnicodeStream() - Get from a BINARY.
   **/
  public void Var028() {
    try {
      sb.setLength(0);

      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "BINARY_NOTRANS");
      InputStream v = rs.getUnicodeStream("C_BINARY_20");
      if (isToolboxDriver()) // @F1A
        assertCondition(compare(v, "456C6576656E2020202020202020202020202020",
            "UnicodeBigUnmarked",sb),sb); // @F1A
      else if (getRelease() < JDTestDriver.RELEASE_V7R1M0) // @F1A // @K2
        assertCondition(compare(v, eleven,sb),sb);
      else
        // @K2
        assertCondition(compareBeginsWithBytes(v, eleven,sb),sb); // @K2
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUnicodeStream() - Get from a VARBINARY.
   **/
  public void Var029() {
    try {
      sb.setLength(0);

      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "BINARY_NOTRANS");
      InputStream v = rs.getUnicodeStream("C_VARBINARY_20");
      if (isToolboxDriver()) // @F1A
        assertCondition(compare(v, "5477656C7665", "UnicodeBigUnmarked",sb),sb); // @F1A
      else if (getRelease() < JDTestDriver.RELEASE_V7R1M0) // @F1A // @K2
        assertCondition(compare(v, twelve,sb), " v = " + v + " and SB eleven "+sb);
      else
        // @K2
        assertCondition(compareBeginsWithBytes(v, twelve,sb), " v = " + v
            + " and SB eleven "+sb);// @K2
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUnicodeStream() - Get from a CLOB, when the CLOB data is returned in the
   * result set.
   **/
  public void Var030() {
    if (checkLobSupport()) {
	sb.setLength(0);
      try {
        ResultSet rs = statement0_.executeQuery("SELECT * FROM "
            + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_FULL");
        InputStream v = rs.getUnicodeStream("C_CLOB");
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
            getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
          assertCondition(compareBeginsWithBytes(v, JDRSTest.CLOB_FULL
              .getBytes("UnicodeBigUnmarked"),sb),sb); // @K2
        else
          // @K2
          assertCondition(compare(v, JDRSTest.CLOB_FULL, "UnicodeBigUnmarked",sb),sb); // @B0C
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getUnicodeStream() - Get from a CLOB, when the CLOB locator is returned in
   * the result set.
   **/
  public void Var031() {
    if (checkLobSupport()) {
	sb.setLength(0);

      try {
        ResultSet rs2 = statement2_.executeQuery("SELECT * FROM "
            + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs2, "LOB_FULL");
        InputStream v = rs2.getUnicodeStream("C_CLOB");
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
            getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
          assertCondition(compareBeginsWithBytes(v, JDRSTest.CLOB_FULL
              .getBytes("UnicodeBigUnmarked"),sb),sb); // @K2
        else
          // @K2
          assertCondition(compare(v, JDRSTest.CLOB_FULL, "UnicodeBigUnmarked",sb),sb); // @B0C
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getUnicodeStream() - Get from a DBCLOB, when the DBCLOB data is returned in
   * the result set.
   **/
  public void Var032() {
    if (checkLobSupport()) {
	sb.setLength(0);

      try {
        ResultSet rs = statement0_.executeQuery("SELECT * FROM "
            + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_FULL");
        InputStream v = rs.getUnicodeStream("C_DBCLOB");
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
            getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
          assertCondition(compareBeginsWithBytes(v, JDRSTest.DBCLOB_FULL
              .getBytes("UnicodeBigUnmarked"),sb),sb); // @K2
        else
          // @K2
          assertCondition(compare(v, JDRSTest.DBCLOB_FULL, "UnicodeBigUnmarked",sb),sb); // @B0C
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getUnicodeStream() - Get from a DBCLOB, when the DBCLOB locator is returned
   * in the result set.
   **/
  public void Var033() {
    if (checkLobSupport()) {
	sb.setLength(0);

      try {
        ResultSet rs2 = statement2_.executeQuery("SELECT * FROM "
            + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs2, "LOB_FULL");
        InputStream v = rs2.getUnicodeStream("C_DBCLOB");
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
            getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
          assertCondition(compareBeginsWithBytes(v, JDRSTest.DBCLOB_FULL
              .getBytes("UnicodeBigUnmarked"),sb),sb); // @K2
        else
          // @K2
          assertCondition(compare(v, JDRSTest.DBCLOB_FULL, "UnicodeBigUnmarked",sb),sb); // @B0C
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getUnicodeStream() - Get from a BLOB, when the BLOB data is returned in the
   * result set.
   **/
  public void Var034() {
    if (checkLobSupport()) {
	sb.setLength(0);

      try {
        ResultSet rs = statement0_.executeQuery("SELECT * FROM "
            + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_FULL");
        InputStream v = rs.getUnicodeStream("C_BLOB");
        if (isToolboxDriver()) // @K1A
          assertCondition(compare(v, JDRSTest.BLOB_FULL, false,sb),sb); // @K1A
        else if (getRelease() < JDTestDriver.RELEASE_V7R1M0) // @K1A // @K2
          assertCondition(compare(v, JDRSTest.BLOB_FULL,sb),sb);
        else
          // @K2
          assertCondition(compareBeginsWithBytes(v, JDRSTest.BLOB_FULL,sb),sb); // @K2
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getUnicodeStream() - Get from a BLOB, when the BLOB locator is returned in
   * the result set.
   **/
  public void Var035() {
    if (checkLobSupport()) {
	sb.setLength(0);

      try {
        // This is a shameless hack to get around another shameless hack.
        // At the time that we get to this testcase, we have accumulated
        // locks that will interfere with our running the test. The locks
        // exist because we turned off autocommit in the setup code. Just
        // commit here so that we have things cleaned up. Sometime, lets
        // get both the change to not use autocommit and these commits out
        // of the code for good.
        connection_.commit();
        connection2_.commit();
        ResultSet rs2 = statement2_.executeQuery("SELECT * FROM "
            + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs2, "LOB_FULL");
        InputStream v = rs2.getUnicodeStream("C_BLOB");
        if (isToolboxDriver()) // @K1A
          assertCondition(compare(v, JDRSTest.BLOB_FULL, false,sb),sb); // @K1A
        else if (getRelease() < JDTestDriver.RELEASE_V7R1M0) // @K1A // @K2
          assertCondition(compare(v, JDRSTest.BLOB_FULL,sb),sb);
        else
          // @K2
          assertCondition(compareBeginsWithBytes(v, JDRSTest.BLOB_FULL,sb),sb); // @K2
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getUnicodeStream() - Get from a DATE.
   **/
  public void Var036() {
    sb.setLength(0);
      sb.append(" getUnicodeStream from DATE -- updated 12/14/2011"); 
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_1998");
      InputStream v = rs.getUnicodeStream("C_DATE");
      String expected = "04/08/98";
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	  expected = "1998-04-08"; 
      } 
        assertCondition(compare(v, expected, "UnicodeBigUnmarked", sb), sb); // @K1A
    } catch (Exception e) {
        failed(e, "Unexpected Exception"+sb); // @K1A
    }
  }

  /**
   * getUnicodeStream() - Get from a TIME.
   **/
  public void Var037() {
    sb.setLength(0);
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_1998");
      InputStream v = rs.getUnicodeStream("C_TIME");
      String expected = "08:14:03";
      boolean check = compareUnicodeStream(v, expected, sb);
      assertCondition(check, sb.toString() + " Updated 11/17/2011");

    } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
    }
  }

  /**
   * getUnicodeStream() - Get from a TIMESTAMP.
   **/
  public void Var038() {
    sb.setLength(0);
    try {
      ResultSet rs = statement0_.executeQuery("SELECT * FROM "
          + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_1998");
      InputStream v = rs.getUnicodeStream("C_TIMESTAMP");
      String expected = "1998-11-18 03:13:42.987654";
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	  expected = "1998-11-18-03.13.42.987654";
      } 
      boolean check = compareUnicodeStream(v, expected, sb);
      assertCondition(check, sb.toString() + " Updated 11/17/2011");

    } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
    }
  }

  /**
   * getUnicodeStream() - Get from a DATALINK.
   * 
   * SQL400 - From the native driver's perspective, a datalink column is treated
   * the same was that it is for standard SQL. When you make an unqualified
   * select of a datalink, the SQL statement is implicitly cast to look at the
   * full URL value for the datalink. This is, in essence, a String and can be
   * retrieved with getUnicodeStream as other Strings can.
   **/
  public void Var039() {
    if (checkDatalinkSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
        JDRSTest.position0(rs, "LOB_FULL");
        InputStream v = rs.getUnicodeStream("C_DATALINK");
        if (getDriver() == JDTestDriver.DRIVER_NATIVE
            && getRelease() >= JDTestDriver.RELEASE_V7R1M0 && isJDK14) // @K1C
          failed("Didn't throw SQLException" + v);
        else
          // Note the case... AS/400 DB does its own thing here...
          assertCondition(compare(v,
              "HTTP://SCHUMAN.RCHLAND.IBM.COM/help.html", "UnicodeBigUnmarked",sb),sb);

      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE
            && getRelease() >= JDTestDriver.RELEASE_V7R1M0 && isJDK14) // @K1C
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        else
          failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getUnicodeStream() - Get from an empty DATALINK.
   * 
   * SQL400 - From the native driver's perspective, a datalink column is treated
   * the same was that it is for standard SQL. When you make an unqualified
   * select of a datalink, the SQL statement is implicitly cast to look at the
   * full URL value for the datalink. This is, in essence, a String and can be
   * retrieved with getUnicodeStream as other Strings can.
   **/
  public void Var040() {
    if (checkDatalinkSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
        JDRSTest.position0(rs, "LOB_EMPTY");
        InputStream v = rs.getUnicodeStream("C_DATALINK");
        if (getDriver() == JDTestDriver.DRIVER_NATIVE
            && getRelease() >= JDTestDriver.RELEASE_V7R1M0 && isJDK14) // @K1C
          failed("Didn't throw SQLException" + v);
        else
          assertCondition(compare(v, "", "UnicodeBigUnmarked",sb),sb);

      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE
            && getRelease() >= JDTestDriver.RELEASE_V7R1M0 && isJDK14) // @K1C
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        else
          failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getUnicodeStream() - Get from a DISTINCT.
   **/
  public void Var041() {
    if (checkLobSupport()) {
      sb.setLength(0);

      try {
        ResultSet rs = statement0_.executeQuery("SELECT * FROM "
            + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_EMPTY");
        InputStream v = rs.getUnicodeStream("C_DISTINCT");
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
            getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
          assertCondition(compareBeginsWithBytes(v, "         "
              .getBytes("UnicodeBigUnmarked"),sb),sb); // @K2
        else
          // @K2
          assertCondition(compare(v, "         ", "UnicodeBigUnmarked",sb),sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getUnicodeStream() - Get from a BIGINT.
   **/
  public void Var042() {
    sb.setLength(0);
     sb.append(" -- getUnicodeStream from BIGINT  updated 12/14/2011"); 
    if (checkBigintSupport()) {
      try {
        ResultSet rs = statement0_.executeQuery("SELECT * FROM "
            + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "NUMBER_POS");
        InputStream v = rs.getUnicodeStream("C_BIGINT");
          assertCondition(compare(v, "12374321", "UnicodeBigUnmarked", sb), sb); // @K1A
      } catch (Exception e) {

          failed(e, "Unexpected Exception"); // @K1A
      }
    }
  }

  /**
   * getUnicodeStream() - Verify that no DataTruncation is posted when the max
   * field size is set to 0.
   **/
  public void Var043() {
    sb.setLength(0);

    try {
      Statement s = connection_.createStatement();
      s.setMaxFieldSize(0);
      ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      InputStream v = rs.getUnicodeStream("C_VARCHAR_50");
      DataTruncation dt = (DataTruncation) rs.getWarnings();
      rs.close();
      s.close();
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
          getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
        assertCondition(compareBeginsWithBytes(v, "Java Toolbox"
            .getBytes("UnicodeBigUnmarked"),sb)
            && dt == null,sb); // @K2
      else
        // @K2
        assertCondition((compare(v, "Java Toolbox", "UnicodeBigUnmarked",sb)) // @B0C
            && (dt == null),sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUnicodeStream() - Verify that data is truncated without a DataTruncation
   * posted when the max field size is set to a value shorter than the string.
   **/
  public void Var044() {
    sb.setLength(0);

    try {
      Statement s = connection_.createStatement();
      s.setMaxFieldSize(18);
      ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");

      InputStream v = rs.getUnicodeStream("C_CHAR_50");
      SQLWarning w = rs.getWarnings();
      rs.close();
      s.close();
      if (getDriver() == JDTestDriver.DRIVER_NATIVE && // @K2
          getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K2
        assertCondition(compareBeginsWithBytes(v, "Toolbox for Java  "
            .getBytes("UnicodeBigUnmarked"),sb)
            && w == null,sb);// @K2
      else
        // @K2
        assertCondition(compare(v, "Toolbox for Java  ", "UnicodeBigUnmarked",sb) // @B0C
            && (w == null),sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getUnicodeStream() - Get from DFP16:
   **/
  public void Var045() {
    if (checkDecFloatSupport()) {
      sb.setLength(0);
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP16);
        rs.next();
        InputStream v = rs.getUnicodeStream(1);
        String expected = "1.1";
        boolean check = compareUnicodeStream(v, expected, sb);
        assertCondition(check, sb.toString() + " Updated 11/17/2011");

      } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
      }
    }
  }

  /**
   * getUnicodeStream() - Get from DFP34:
   **/
  public void Var046() {
    
    if (checkDecFloatSupport()) {
      sb.setLength(0);
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP34);
        rs.next();
        InputStream v = rs.getUnicodeStream(1);
        String expected = "1.1";
        boolean check = compareUnicodeStream(v, expected, sb);
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
      }
    }
  }


  /**
   * getUnicodeStream() - Get from a TIMESTAMP(12).
   **/
  public void Var047() {
      if (checkTimestamp12Support()) {
	  String expected = "1998-11-18 03:13:42.987654000000";
	  if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	      expected = "1998-11-18-03.13.42.987654000000";
	  }
        testGet(statement0_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
            "getUnicodeStream", 
            expected,
            " -- added 11/19/2012"); 

      }
  }


  /**
   * getUnicodeStream() - Get from a TIMESTAMP(0).
   **/
  public void Var048() {
      if (checkTimestamp12Support()) {
	  String expected = "1998-11-18 03:13:42"; 
	  if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	      expected = "1998-11-18-03.13.42";
	  }
	  testGet(statement0_,
		  "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
		  "getUnicodeStream",
		  expected,
		  " -- added 11/19/2012"); 
		  
		  
      }
  }



/**
getUnicodeStream() - Get from a BOOLEAN true .
**/
    public void Var049 ()
    {
	if (checkBooleanSupport()) { 
	    sb.setLength(0); 

	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_BOOLEAN);
		JDRSTest.position0 (rs, "BOOLEAN_TRUE");
		InputStream v = rs.getUnicodeStream ("C_BOOLEAN");
		sb.setLength(0); 
		assertCondition(compareUnicodeStream(v, JDRSTest.BOOLEAN_TRUE_STRING , sb),sb);    //@F1A
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}


    }


/**
getUnicodeStream() - Get from a BOOLEAN false .
**/
    public void Var050 ()
    {
	if (checkBooleanSupport()) { 
	    sb.setLength(0); 

	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_BOOLEAN);
		JDRSTest.position0 (rs, "BOOLEAN_FALSE");
		InputStream v = rs.getUnicodeStream ("C_BOOLEAN");
		sb.setLength(0); 
		assertCondition(compareUnicodeStream(v, JDRSTest.BOOLEAN_FALSE_STRING , sb),sb);    //@F1A
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getUnicodeStream() - Get from a BOOLEAN null .
**/
    public void Var051 ()
    {
	if (checkBooleanSupport()) { 
	    sb.setLength(0); 

	    try {
		ResultSet rs = statement0_.executeQuery ("SELECT * FROM "
							 + JDRSTest.RSTEST_BOOLEAN);
		JDRSTest.position0 (rs, "BOOLEAN_NULL");
		InputStream v = rs.getUnicodeStream ("C_BOOLEAN");
		if (v != null) { 
		  sb.append("Got "+v+" sb null"); 
		}
		
		assertCondition(v == null,sb);    //@F1A
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}


    }





}
