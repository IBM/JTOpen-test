///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetTime.java
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
import test.JTOpenTestEnvironment;

import java.io.FileOutputStream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.TimeZone;

/**
 * Testcase JDRSGetTime. This tests the following method of the JDBC ResultSet
 * class:
 * 
 * <ul>
 * <li>getTime()
 * </ul>
 **/
public class JDRSGetTime extends JDTestcase {

  // Private data.
  private Statement statement0_;
  private Statement statement_;
  private String statementQuery_;
  private ResultSet rs_;
  int driver_;

  /**
   * Constructor.
   **/
  public JDRSGetTime(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDRSGetTime", namesAndVars, runMode, fileOutputStream,
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
    driver_ = getDriver();

    // SQL400 - driver neutral...
    String url = baseURL_
        // String url = "jdbc:as400://" + systemObject_.getSystemName()
         
        + ";date format=iso;time format=iso";
    connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

    setAutoCommit(connection_, false); // @E1A

    statement0_ = connection_.createStatement();

    if (isJdbc20()) {
      try {
        statement_ = connection_.createStatement(
            ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      } catch (SQLException sqlex) {
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
   * getTime() - Should throw exception when the result set is closed.
   **/
  public void Var001() {
    try {
      Statement s = connection_.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next();
      rs.close();
      rs.getTime(1);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Should throw exception when cursor is not pointing to a row.
   **/
  public void Var002() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      Time v = rs.getTime(1);
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Should throw an exception when the column is an invalid index.
   **/
  public void Var003() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_1998");
      Time v = rs.getTime(100);
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Should throw an exception when the column is 0.
   **/
  public void Var004() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_2000");
      Time v = rs.getTime(0);
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Should throw an exception when the column is -1.
   **/
  public void Var005() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_1998");
      Time v = rs.getTime(-1);
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Should work when the column index is valid.
   **/
  public void Var006() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_2000");
      Time v = rs.getTime(20);
      assertCondition(v.toString().equals("14:04:55"),
          "Got " + v + " expected 14:04:55");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTime() - Should throw an exception when the column name is null.
   **/
  public void Var007() {

    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable(
          "JCC throws null pointer exception when column name is null ");
    } else {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "DATE_1998");
        rs.getTime(null);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTime() - Should throw an exception when the column name is an empty
   * string.
   **/
  public void Var008() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_2000");
      rs.getTime("");
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Should throw an exception when the column name is invalid.
   **/
  public void Var009() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_2000");
      rs.getTime("INVALID");
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Should throw an exception when the calendar is null.
   **/
  public void Var010() {
    if (checkJdbc20()) {
      try {
        rs_ = JDRSTest.position(driver_, statement_, statementQuery_, rs_,
            "DATE_1998");
        Time v = rs_.getTime(20, null);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTime() - Should work when the column name is valid.
   **/
  public void Var011() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_1998");
      Time v = rs.getTime("C_TIME");
      assertCondition(v.toString().equals("08:14:03"),
          "Got " + v + " expected 8:14:03");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTime() - Should work when an update is pending.
   **/
  public void Var012() {
    if (checkJdbc20()) {
      try {
        rs_ = JDRSTest.position(driver_, statement_, statementQuery_, rs_,
            "UPDATE_SANDBOX");

        rs_.updateTime("C_TIME", Time.valueOf("11:32:12"));
        Time v = rs_.getTime("C_TIME");
        assertCondition(v.toString().equals("11:32:12"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getTime() - Should work when an update has been done.
   **/
  public void Var013() {
    if (checkJdbc20()) {
      try {
        rs_ = JDRSTest.position(driver_, statement_, statementQuery_, rs_,
            "UPDATE_SANDBOX");
        rs_.updateTime("C_TIME", Time.valueOf("04:04:13"));
        rs_.updateRow();
        Time v = rs_.getTime("C_TIME");
        assertCondition(v.toString().equals("04:04:13"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getTime() - Should work when the current row is the insert row, when an
   * insert is pending.
   **/
  public void Var014() {
    if (checkJdbc20()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support moveToInserRow");
        return;
      }
      try {
        rs_.moveToInsertRow();
        rs_.updateTime("C_TIME", Time.valueOf("14:22:14"));
        Time v = rs_.getTime("C_TIME");
        assertCondition(v.toString().equals("14:22:14"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getTime() - Should work when the current row is the insert row, when an
   * insert has been done.
   **/
  public void Var015() {
    if (checkJdbc20()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support moveToInserRow");
        return;
      }
      try {
        rs_.moveToInsertRow();
        rs_.updateTime("C_TIME", Time.valueOf("22:00:00"));
        rs_.insertRow();
        Time v = rs_.getTime("C_TIME");
        assertCondition(v.toString().equals("22:00:00"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getTime() - Should throw an exception on a deleted row.
   **/
  public void Var016() {
    if (checkJdbc20()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC doesn't throw exception for get on deleted row");
        return;
      }
      try {
        rs_ = JDRSTest.position(driver_, statement_, statementQuery_, rs_,
            "DUMMY_ROW");
        rs_.deleteRow();
        Time v = rs_.getTime("C_TIME");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTime() - Should return null when the column is NULL.
   **/
  public void Var017() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_NULL");
      Time v = rs.getTime("C_TIME");
      assertCondition(v == null);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTime() - Should work when a calendar other than the default is passed.
   * 
   * SQL400 - For this test to return UTC timezone information, additional
   * AS/400 setup would be required. The directions for doing this can be found
   * at:
   * http://publib.boulder.ibm.com/pubs/html/as400/ic2924/info/java/rzaha/devkit.htm
   * For now, we just take the defaults and expect the calendar to be ignored in
   * our testing.
   **/
  public void Var018() {
    Calendar c = new GregorianCalendar();
    c.getTimeZone();

    if (checkJdbc20()) {
      try {
        rs_ = JDRSTest.position(driver_, statement_, statementQuery_, rs_,
            "DATE_1998");
        Calendar gmt = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Time v = rs_.getTime("C_TIME", gmt);

        // Make this work for V5R4 group test -- same result as before @PDA
        // change
        // The weekly V5R4 group test runs with tz=UTC
        String tz = System.getProperty("user.timezone");

        Calendar centralCal = Calendar
            .getInstance(TimeZone.getTimeZone("GMT-6:00"));// tc cal

        Calendar thisClientCal = new GregorianCalendar();
        // System.out.println( gmt.getTimeZone().getRawOffset());

        // System.out.println( centralCal.getTimeZone().getRawOffset());
        // System.out.println( thisClientCal.getTimeZone().getRawOffset());
        int offset = (centralCal.getTimeZone().getRawOffset()
            - thisClientCal.getTimeZone().getRawOffset()) / 3600000;
        // System.out.println(offset);

        if ((isToolboxDriver()) && (getRelease() == JDTestDriver.RELEASE_V7R1M0)
            && JTOpenTestEnvironment.isOS400
            && ("UTC".equals(tz))) {
          assertCondition(v.toString().equals("08:14:03"),
              "Got " + v.toString() + " expected 08:14:03 tz=" + tz);
          return;
        }

        if (((getDriver() == JDTestDriver.DRIVER_NATIVE && "UTC".equals(tz)))) {
          assertCondition(v.toString().equals("08:14:03"),
              "Got " + v.toString() + " expected 08:14:03 tz=" + tz);
        } else {
          // toolbox adjust for client and server in diff timezones (at least
          // for +-2)
          // at least for us time zones
          // String timeReceived = v.toString();
          int hour = v.getHours();
          hour = hour + offset;
          String adjustedTime = "0" + String.valueOf(hour)
              + v.toString().substring(2);
          // if(timeReceived.indexOf("))
          // if too messed up use v.toGMTString() later
          assertCondition(adjustedTime.equals("02:14:03"),
              "Got " + adjustedTime.toString() + " expected 02:14:03 tz=" + tz);
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getTime() - Get from a SMALLINT.
   **/
  public void Var019() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      Time v = rs.getTime("C_SMALLINT");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Get from a INTEGER.
   **/
  public void Var020() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      Time v = rs.getTime("C_INTEGER");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Get from a REAL.
   **/
  public void Var021() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      Time v = rs.getTime("C_REAL");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Get from a FLOAT.
   **/
  public void Var022() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      Time v = rs.getTime("C_FLOAT");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Get from a DOUBLE.
   **/
  public void Var023() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      Time v = rs.getTime("C_DOUBLE");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Get from a DECIMAL.
   **/
  public void Var024() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      Time v = rs.getTime("C_DECIMAL_50");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Get from a NUMERIC.
   **/
  public void Var025() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      Time v = rs.getTime("C_NUMERIC_105");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Get from a CHAR, where the value does not translate to a time.
   **/
  public void Var026() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      Time v = rs.getTime("C_CHAR_50");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Get from a CHAR, where the value does translate to a time.
   **/
  public void Var027() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_TIME");
      Time v = rs.getTime("C_CHAR_50");
      assertCondition(v.toString().equals("15:55:04"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTime() - Get from a VARCHAR, where the value does not translate to a
   * time.
   **/
  public void Var028() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      Time v = rs.getTime("C_VARCHAR_50");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Get from a VARCHAR, where the value does translate to a time.
   **/
  public void Var029() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_TIME");
      Time v = rs.getTime("C_VARCHAR_50");
      assertCondition(v.toString().equals("00:01:05"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTime() - Get from a BINARY.
   **/
  public void Var030() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "BINARY_TRANS");
      Time v = rs.getTime("C_BINARY_20");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Get from a VARBINARY.
   **/
  public void Var031() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "BINARY_TRANS");
      Time v = rs.getTime("C_VARBINARY_20");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Get from a CLOB.
   **/
  public void Var032() {
    if (checkLobSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_FULL");
        Time v = rs.getTime("C_CLOB");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTime() - Get from a DBCLOB.
   **/
  public void Var033() {
    if (checkLobSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_FULL");
        Time v = rs.getTime("C_DBCLOB");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTime() - Get from a BLOB.
   **/
  public void Var034() {
    if (checkLobSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_FULL");
        Time v = rs.getTime("C_BLOB");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTime() - Get from a DATE.
   **/
  public void Var035() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_1998");
      Time v = rs.getTime("C_DATE");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTime() - Get from a TIME.
   **/
  public void Var036() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_2000");
      Time v = rs.getTime("C_TIME");
      assertCondition(v.toString().equals("14:04:55"),
          "got " + v + " expected 14:04:55");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTime() - Get from a TIMESTAMP.
   **/
  public void Var037() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_2000");
      Time v = rs.getTime("C_TIMESTAMP");
      assertCondition(v.toString().equals("10:30:12"),
          "got " + v + " expected 10:30:12");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTime() - Get from a DATALINK.
   **/
  public void Var038() {
    if (checkDatalinkSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
        JDRSTest.position0(rs, "LOB_FULL");
        Time v = rs.getTime("C_DATALINK");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTime() - Get from a DISTINCT.
   **/
  public void Var039() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("distinct testcase");
      return;
    }
    if (checkLobSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_FULL");
        Time v = rs.getTime("C_DISTINCT");
        assertCondition(v.toString().equals("12:46:18"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getTime() - Get from a BIGINT.
   **/
  public void Var040() {
    if (checkBigintSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "NUMBER_NEG");
        Time v = rs.getTime("C_BIGINT");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTime() - Get from DFP16:
   **/
  public void Var041() {
    if (checkDecFloatSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP16);
        rs.next();
        Time v = rs.getTime(1);
        failed("Didn't throw SQLException " + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTime() - Get from DFP34:
   **/
  public void Var042() {
    if (checkDecFloatSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP34);
        rs.next();
        Time v = rs.getTime(1);
        failed("Didn't throw SQLException " + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTime() -- dummy testcase
   **/

  public void Var043() {
    assertCondition(true);
  }

  /**
   * getTime() - Get from a DATALINK.
   **/
  public void Var044() {
    if (checkXmlSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETXML);
        rs.next();
        Time v = rs.getTime("C_XML");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTime() - Get from a TIMESTAMP(12).
   **/
  public void Var045() {
    if (checkTimestamp12Support()) {
      testGet(statement0_,
          "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
          "getTime", "03:13:42", " -- added 11/19/2012");

    }
  }

  /**
   * getTime() - Get from a TIMESTAMP(0).
   **/
  public void Var046() {
    if (checkTimestamp12Support()) {
      testGet(statement0_,
          "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
          "getTime", "03:13:42", " -- added 11/19/2012");

    }
  }

  /**
   * getTime() - Get from a BOOLEAN.
   **/
  public void Var047() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_TRUE");
        Time v = rs.getTime("C_BOOLEAN");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTime() - Get from a BOOLEAN.
   **/
  public void Var048() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_FALSE");
        Time v = rs.getTime("C_BOOLEAN");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTime() - Get from a BOOLEAN.
   **/
  public void Var049() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_NULL");
        Time v = rs.getTime("C_BOOLEAN");
        assertCondition(v == null);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

}
