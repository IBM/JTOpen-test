///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetTimestamp.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCTimestamp;

import test.JDRSTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JTOpenTestEnvironment;

import java.io.FileOutputStream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.TimeZone;

/**
 * Testcase JDRSGetTimestamp. This tests the following method of the JDBC
 * ResultSet class:
 * 
 * <ul>
 * <li>getTimestamp()
 * </ul>
 **/
public class JDRSGetTimestamp extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetTimestamp";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }
  static String NATIVE_TIMESTAMP_CLASSNAME = "com.ibm.db2.jdbc.app.DB2JDBCTimestamp";

  // Private data.
  private Statement statement_;
  private String statementQuery_;
  private ResultSet rs_;
  private Statement statement0_;
  private int driver_;

  /**
   * Constructor.
   **/
  public JDRSGetTimestamp(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDRSGetTimestamp", namesAndVars, runMode,
        fileOutputStream, password);
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
    collection_ = JDRSTest.COLLECTION;
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
   * getTimestamp() - Should throw exception when the result set is closed.
   **/
  public void Var001() {
    try {
      Statement s = connection_.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next();
      rs.close();
      rs.getTimestamp(1);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Should throw exception when cursor is not pointing to a
   * row.
   **/
  public void Var002() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      Timestamp v = rs.getTimestamp(1);
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Should throw an exception when the column is an invalid
   * index.
   **/
  public void Var003() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_1998");
      Timestamp v = rs.getTimestamp(100);
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Should throw an exception when the column is 0.
   **/
  public void Var004() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_2000");
      Timestamp v = rs.getTimestamp(0);
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Should throw an exception when the column is -1.
   **/
  public void Var005() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_1998");
      Timestamp v = rs.getTimestamp(-1);
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Should work when the column index is valid.
   **/
  public void Var006() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_2000");
      Timestamp v = rs.getTimestamp(21);
      assertCondition(v.toString().equals("2000-06-25 10:30:12.345676"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTimestamp() - Should throw an exception when the column name is null.
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
        rs.getTimestamp(null);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTimestamp() - Should throw an exception when the column name is an empty
   * string.
   **/
  public void Var008() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_2000");
      rs.getTimestamp("");
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Should throw an exception when the column name is invalid.
   **/
  public void Var009() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_2000");
      rs.getTimestamp("INVALID");
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Should throw an exception when the calendar is null.
   **/
  public void Var010() {
    if (checkJdbc20()) {
      try {
        rs_ = JDRSTest.position(driver_, statement_, statementQuery_, rs_,
            "DATE_1998");
        Timestamp v = rs_.getTimestamp(19, null);
        failed("Didn't throw SQLException" + v + " for null calendar");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTimestamp() - Should work when the column name is valid.
   **/
  public void Var011() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_1998");
      Timestamp v = rs.getTimestamp("C_TIMESTAMP");
      assertCondition(v.toString().equals("1998-11-18 03:13:42.987654"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTimestamp() - Should work when an update is pending.
   **/
  public void Var012() {
    if (checkJdbc20()) {
      try {
        rs_ = JDRSTest.position(driver_, statement_, statementQuery_, rs_,
            "UPDATE_SANDBOX");
        rs_.updateTimestamp("C_TIMESTAMP",
            Timestamp.valueOf("1998-05-26 11:41:12.123456"));
        Timestamp v = rs_.getTimestamp("C_TIMESTAMP");
        assertCondition(v.toString().equals("1998-05-26 11:41:12.123456"));
      } catch (Exception e) {
        failed(e,
            "Unexpected Exception processing timestmap of form 1998-05-26 11:41:12.123456");
      }
    }
  }

  /**
   * getTimestamp() - Should work when an update has been done.
   **/
  public void Var013() {
    if (checkJdbc20()) {
      try {
        rs_ = JDRSTest.position(driver_, statement_, statementQuery_, rs_,
            "UPDATE_SANDBOX");
        rs_.updateTimestamp("C_TIMESTAMP",
            Timestamp.valueOf("1969-11-18 03:15:13.000000"));
        rs_.updateRow();
        Timestamp v = rs_.getTimestamp("C_TIMESTAMP");
        assertCondition(v.toString().equals("1969-11-18 03:15:13.0"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getTimestamp() - Should work when the current row is the insert row, when
   * an insert is pending.
   **/
  public void Var014() {
    if (checkJdbc20()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support moveToInserRow");
        return;
      }
      try {
        rs_.moveToInsertRow();
        rs_.updateTimestamp("C_TIMESTAMP",
            Timestamp.valueOf("1996-06-25 10:30:14.000123"));
        Timestamp v = rs_.getTimestamp("C_TIMESTAMP");
        assertCondition(v.toString().equals("1996-06-25 10:30:14.000123"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getTimestamp() - Should work when the current row is the insert row, when
   * an insert has been done.
   **/
  public void Var015() {
    if (checkJdbc20()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support moveToInserRow");
        return;
      }
      try {
        rs_.moveToInsertRow();
        rs_.updateTimestamp("C_TIMESTAMP",
            Timestamp.valueOf("1998-02-21 04:04:15.949494"));
        rs_.insertRow();
        Timestamp v = rs_.getTimestamp("C_TIMESTAMP");
        assertCondition(v.toString().equals("1998-02-21 04:04:15.949494"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getTimestamp() - Should throw an exception on a deleted row.
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
        Timestamp v = rs_.getTimestamp("C_TIMESTAMP");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTimestamp() - Should return null when the column is NULL.
   **/
  public void Var017() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_NULL");
      Timestamp v = rs.getTimestamp("C_TIMESTAMP");
      assertCondition(v == null);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTimestamp() - Should work when a calendar other than the default is
   * passed.
   * 
   * SQL400 - For this test to return UTC timezone information, additional
   * AS/400 setup would be required. The directions for doing this can be found
   * at:
   * http://publib.boulder.ibm.com/pubs/html/as400/ic2924/info/java/rzaha/devkit.htm
   * For now, we just take the defaults and expect the calendar to be ignored in
   * our testing.
   **/
  public void Var018() {
    if (checkJdbc20()) {
      try {
        rs_ = JDRSTest.position(driver_, statement_, statementQuery_, rs_,
            "DATE_1998");

        Calendar gmt = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

        Timestamp v = rs_.getTimestamp("C_TIMESTAMP", gmt);

        // Make this work for V5R4 group test -- same result as before @PDA
        // change
        // Group test runs with tz='UTC'
        String tz = System.getProperty("user.timezone");
        if ((isToolboxDriver())
            && (getRelease() == JDTestDriver.RELEASE_V7R1M0
                || getRelease() >= JDTestDriver.RELEASE_V7R1M0)
            && JTOpenTestEnvironment.isOS400
            && ("UTC".equals(tz))) {
          assertCondition(v.toString().equals("1998-11-18 03:13:42.987654"),
              " got " + v.toString() + "expected 1998-11-18 03:13:42.987654 tz="
                  + tz);

          return;
        }

        if (((getDriver() == JDTestDriver.DRIVER_NATIVE && "UTC".equals(tz)))) { 
          assertCondition(v.toString().equals("1998-11-18 03:13:42.987654"),
              " got " + v.toString() + "expected 1998-11-18 03:13:42.987654 tz="
                  + tz);
        } else {
          // toolbox adjust for client and server in diff timezones (at least
          // for +-2)
          // at least for us time zones
          Calendar centralCal = new GregorianCalendar(
              TimeZone.getTimeZone("GMT-6:00"));// tc cal
          Calendar thisClientCal = new GregorianCalendar();

          int offset = (centralCal.getTimeZone().getRawOffset()
              - thisClientCal.getTimeZone().getRawOffset()) / 3600000;
          // String timeReceived = v.toString();
          int hour = v.getHours();
          hour = hour + offset;

          // String s1 = v.toString().substring(0,12);
          // String s11 = String.valueOf(hour);
          // String s2 = v.toString().substring(14);

          String expectedTime = v.toString().substring(0, 11)
              + String.valueOf(hour) + v.toString().substring(13);

          // if(timeReceived.indexOf("))
          // if too messed up use v.toGMTString() later

          assertCondition(
              expectedTime.toString().equals("1998-11-17 21:13:42.987654"),
              "got " + expectedTime.toString()
                  + " expected 1998-11-17 21:13:42.987654 tz=" + tz);
        }

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getTimestamp() - Get from a SMALLINT.
   **/
  public void Var019() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      Timestamp v = rs.getTimestamp("C_SMALLINT");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Get from a INTEGER.
   **/
  public void Var020() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      Timestamp v = rs.getTimestamp("C_INTEGER");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Get from a REAL.
   **/
  public void Var021() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      Timestamp v = rs.getTimestamp("C_REAL");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Get from a FLOAT.
   **/
  public void Var022() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      Timestamp v = rs.getTimestamp("C_FLOAT");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Get from a DOUBLE.
   **/
  public void Var023() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      Timestamp v = rs.getTimestamp("C_DOUBLE");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Get from a DECIMAL.
   **/
  public void Var024() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      Timestamp v = rs.getTimestamp("C_DECIMAL_50");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Get from a NUMERIC.
   **/
  public void Var025() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "NUMBER_POS");
      Timestamp v = rs.getTimestamp("C_NUMERIC_105");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Get from a CHAR, where the value does not translate to a
   * timestamp.
   **/
  public void Var026() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      Timestamp v = rs.getTimestamp("C_CHAR_50");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Get from a CHAR, where the value does translate to a
   * timestamp.
   **/
  public void Var027() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_TIMESTAMP");
      Timestamp v = rs.getTimestamp("C_CHAR_50");
      assertCondition(v.toString().equals("2010-02-28 03:23:03.48392"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTimestamp() - Get from a VARCHAR, where the value does not translate to
   * a timestamp.
   **/
  public void Var028() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_FULL");
      Timestamp v = rs.getTimestamp("C_VARCHAR_50");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Get from a VARCHAR, where the value does translate to a
   * timestamp.
   **/
  public void Var029() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "CHAR_TIMESTAMP");
      Timestamp v = rs.getTimestamp("C_VARCHAR_50");
      assertCondition(v.toString().equals("1984-07-04 23:23:01.102034"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTimestamp() - Get from a BINARY.
   **/
  public void Var030() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "BINARY_TRANS");
      Timestamp v = rs.getTimestamp("C_BINARY_20");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Get from a VARBINARY.
   **/
  public void Var031() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "BINARY_TRANS");
      Timestamp v = rs.getTimestamp("C_VARBINARY_20");
      failed("Didn't throw SQLException" + v);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Get from a CLOB.
   **/
  public void Var032() {
    if (checkLobSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_FULL");
        Timestamp v = rs.getTimestamp("C_CLOB");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTimestamp() - Get from a DBCLOB.
   **/
  public void Var033() {
    if (checkLobSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_FULL");
        Timestamp v = rs.getTimestamp("C_DBCLOB");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTimestamp() - Get from a BLOB.
   **/
  public void Var034() {
    if (checkLobSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_FULL");
        Timestamp v = rs.getTimestamp("C_BLOB");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTimestamp() - Get from a DATE.
   **/
  public void Var035() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_1998");
      Timestamp v = rs.getTimestamp("C_DATE");
      assertCondition(v.toString().equals("1998-04-08 00:00:00.0"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTimestamp() - Get from a TIME.
   **/
  public void Var036() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_2000");
      Timestamp v = rs.getTimestamp("C_TIME");
      // getTimestamp() should be permitted on a Time column - Changed by Native
      // Driver!
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0)
        assertCondition(v.toString().equals("1970-01-01 14:04:55.0"),
            v.toString() + " sb 1970-01-01 14:04:55.0 ");
      else
        failed("Didn't throw SQLException but retrieved " + v);
    } catch (Exception e) {
      if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      } else if (getRelease() >= JDTestDriver.RELEASE_V7R1M0)
        failed(e, "Unexpected Exception");
      else
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTimestamp() - Get from a TIMESTAMP.
   **/
  public void Var037() {
    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      JDRSTest.position0(rs, "DATE_2000");
      Timestamp v = rs.getTimestamp("C_TIMESTAMP");
      assertCondition(v.toString().equals("2000-06-25 10:30:12.345676"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTimestamp() - Get from a DATALINK.
   **/
  public void Var038() {
    if (checkDatalinkSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
        JDRSTest.position0(rs, "LOB_FULL");
        Timestamp v = rs.getTimestamp("C_DATALINK");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTimestamp() - Get from a DISTINCT.
   **/
  public void Var039() {
    if (checkLobSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "LOB_FULL");
        Timestamp v = rs.getTimestamp("C_DISTINCT");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTimestamp() - Get from a BIGINT.
   **/
  public void Var040() {
    if (checkBigintSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "NUMBER_POS");
        Timestamp v = rs.getTimestamp("C_BIGINT");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTimestamp() - Get from DFP16:
   **/
  public void Var041() {
    if (checkDecFloatSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP16);
        rs.next();
        Timestamp v = rs.getTimestamp(1);
        failed("Didn't throw SQLException " + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTimestamp() - Get from DFP34:
   **/
  public void Var042() {
    if (checkDecFloatSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP34);
        rs.next();
        Timestamp v = rs.getTimestamp(1);
        failed("Didn't throw SQLException " + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTimestamp() - Get from SQLXML:
   **/
  public void Var043() {
    if (checkXmlSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETXML);
        rs.next();
        Timestamp v = rs.getTimestamp("C_XML");
        failed("Didn't throw SQLException " + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTimestamp() - Get from a TIMESTAMP(12).
   **/
  public void Var044() {
    if (checkTimestamp12Support()) {
      testGet(statement0_,
          "SELECT CAST('1998-11-18 03:13:42.987654123456' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
          "getTimestamp", "1998-11-18 03:13:42.987654123456",
          " -- added 11/19/2012");

    }
  }

  /**
   * getUnicodeStream() - Get from a TIMESTAMP(0).
   **/
  public void Var045() {
    if (checkTimestamp12Support()) {
      testGet(statement0_,
          "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
          "getTimestamp", "1998-11-18 03:13:42.0", " -- added 11/19/2012");

    }
  }

  /* Get timestamp with precision 0 */
  public void Var046() {
    if (checkTimestamp12Support()) {
      String[] setup = { "SILENT: DELETE FROM " + collection_ + ".JDRSGTS0",
          "SILENT: CREATE TABLE " + collection_ + ".JDRSGTS0 (C1 TIMESTAMP(0))",
          "INSERT INTO  " + collection_
              + ".JDRSGTS0 VALUES('1998-11-18 03:13:42')", };
      String[] cleanup = { "DROP TABLE " + collection_ + ".JDRSGTS0", };
      testGet(statement0_, setup, "SELECT C1 FROM " + collection_ + ".JDRSGTS0",
          cleanup, "getTimestamp", "1998-11-18 03:13:42.0",
          " -- added 03/26/2013");

    }
  }

  public void testTimestampX(String tableName, String tableDefinition,
      String value) {
    if (checkTimestamp12Support()) {
      String[] setup = { "SILENT: DELETE FROM  " + tableName,
          "SILENT: CREATE TABLE " + tableName + " " + tableDefinition,
          "INSERT INTO  " + tableName + " VALUES('" + value + "')", };
      String[] cleanup = { "DROP TABLE " + tableName, };
      testGet(statement0_, setup, "SELECT C1 FROM " + tableName, cleanup,
          "getTimestamp", value, " -- added 03/26/2013");

    }

    try {
      connection_.commit();
    } catch (Exception e) {
      System.out.println("Warning:  Exception on commit");
      e.printStackTrace(System.out);

    }
  }

  public void testTimestampX(String tableName, String tableDefinition,
      String[] values) {
    if (checkTimestamp12Support()) {

      String[] setup = new String[values.length + 2];
      setup[0] = "SILENT: DELETE FROM  " + tableName;
      setup[1] = "SILENT: CREATE TABLE " + tableName + " " + tableDefinition;
      for (int i = 0; i < values.length; i++) {
        setup[2 + i] = "INSERT INTO  " + tableName + " VALUES('" + values[i]
            + "')";
      }
      ;
      String[] cleanup = { "DROP TABLE " + tableName, };
      testGet(statement0_, setup, "SELECT C1 FROM " + tableName, cleanup,
          "getTimestamp", values, " -- added 03/26/2013");

    }
  }

  /* Get timestamp with precision 1 */
  public void Var047() {
    testTimestampX(collection_ + ".JDRSGTS1", "(C1 TIMESTAMP(1))",
        "1998-11-18 03:13:42.1");
  }

  /* Get timestamp with precision 2 */
  public void Var048() {
    testTimestampX(collection_ + ".JDRSGTS2", "(C1 TIMESTAMP(2))",
        "1998-11-18 03:13:42.12");
  }

  /* Get timestamp with precision 3 */
  public void Var049() {
    testTimestampX(collection_ + ".JDRSGTS3", "(C1 TIMESTAMP(3))",
        "1998-11-18 03:13:42.123");
  }

  /* Get timestamp with precision 4 */
  public void Var050() {
    testTimestampX(collection_ + ".JDRSGTS4", "(C1 TIMESTAMP(4))",
        "1998-11-18 03:13:42.1234");
  }

  /* Get timestamp with precision 5 */
  public void Var051() {
    testTimestampX(collection_ + ".JDRSGTS5", "(C1 TIMESTAMP(5))",
        "1998-11-18 03:13:42.12345");
  }

  /* Get timestamp with precision 6 */
  public void Var052() {
    testTimestampX(collection_ + ".JDRSGTS6", "(C1 TIMESTAMP(6))",
        "1998-11-18 03:13:42.123456");
  }

  /* Get timestamp with precision 7 */
  public void Var053() {
    String[] values = { "1998-11-18 03:13:42.1234567",
        "1998-11-18 03:13:42.123456", "1998-11-18 03:13:42.12345",
        "1998-11-18 03:13:42.1234", "1998-11-18 03:13:42.123",
        "1998-11-18 03:13:42.12", "1998-11-18 03:13:42.1",
        "1998-11-18 03:13:42.0", };
    testTimestampX(collection_ + ".JDRSGTS7", "(C1 TIMESTAMP(7))", values);

  }

  /* Get timestamp with precision 8 */
  public void Var054() {
    String[] values = { "1998-11-18 03:13:42.12345678",
        "1998-11-18 03:13:42.1234567", "1998-11-18 03:13:42.123456",
        "1998-11-18 03:13:42.12345", "1998-11-18 03:13:42.1234",
        "1998-11-18 03:13:42.123", "1998-11-18 03:13:42.12",
        "1998-11-18 03:13:42.1", "1998-11-18 03:13:42.0", };
    testTimestampX(collection_ + ".JDRSGTS8", "(C1 TIMESTAMP(8))", values);

  }

  /* Get timestamp with precision 9 */
  public void Var055() {
    String[] values = { "1998-11-18 03:13:42.123456789",
        "1998-11-18 03:13:42.12345678", "1998-11-18 03:13:42.1234567",
        "1998-11-18 03:13:42.123456", "1998-11-18 03:13:42.12345",
        "1998-11-18 03:13:42.1234", "1998-11-18 03:13:42.123",
        "1998-11-18 03:13:42.12", "1998-11-18 03:13:42.1",
        "1998-11-18 03:13:42.0", };
    testTimestampX(collection_ + ".JDRSGTS9", "(C1 TIMESTAMP(9))", values);

  }

  /* Get timestamp with precision 10 */
  public void Var056() {
    String[] values = { "1998-11-18 03:13:42.1234567891",
        "1998-11-18 03:13:42.123456789", "1998-11-18 03:13:42.12345678",
        "1998-11-18 03:13:42.1234567", "1998-11-18 03:13:42.123456",
        "1998-11-18 03:13:42.12345", "1998-11-18 03:13:42.1234",
        "1998-11-18 03:13:42.123", "1998-11-18 03:13:42.12",
        "1998-11-18 03:13:42.1", "1998-11-18 03:13:42.0", };
    testTimestampX(collection_ + ".JDRSGTS10", "(C1 TIMESTAMP(10))", values);

  }

  /* Get timestamp with precision 11 */
  public void Var057() {
    String[] values = { "1998-11-18 03:13:42.12345678901",
        "1998-11-18 03:13:42.1234567891", "1998-11-18 03:13:42.123456789",
        "1998-11-18 03:13:42.12345678", "1998-11-18 03:13:42.1234567",
        "1998-11-18 03:13:42.123456", "1998-11-18 03:13:42.12345",
        "1998-11-18 03:13:42.1234", "1998-11-18 03:13:42.123",
        "1998-11-18 03:13:42.12", "1998-11-18 03:13:42.1",
        "1998-11-18 03:13:42.0", };
    testTimestampX(collection_ + ".JDRSGTS11", "(C1 TIMESTAMP(11))", values);

  }

  /* Get timestamp with precision 12 */
  public void Var058() {
    String[] values = { "1998-11-18 03:13:42.123456789012",
        "1998-11-18 03:13:42.12345678901", "1998-11-18 03:13:42.1234567891",
        "1998-11-18 03:13:42.123456789", "1998-11-18 03:13:42.12345678",
        "1998-11-18 03:13:42.1234567", "1998-11-18 03:13:42.123456",
        "1998-11-18 03:13:42.12345", "1998-11-18 03:13:42.1234",
        "1998-11-18 03:13:42.123", "1998-11-18 03:13:42.12",
        "1998-11-18 03:13:42.1", "1998-11-18 03:13:42.0", };
    testTimestampX(collection_ + ".JDRSGTS12", "(C1 TIMESTAMP(12))", values);

  }

  /**
   * Timestamp unit tests
   */

  public void Var059() {
    if (checkTimestamp12Support()) {
	StringBuffer sb = new StringBuffer();
      try {
        int SETTIME = -1;
        String[][] testcases = {
            /* time len picos result */
            { "1351719245000", "19", "0", "2012-10-31 16:34:05" },
            { "1351719245000", "20", "0", "2012-10-31 16:34:05" },
            { "1351719245000", "21", "0", "2012-10-31 16:34:05" },
            { "1351719245000", "22", "0", "2012-10-31 16:34:05" },
            { "1351719245000", "23", "0", "2012-10-31 16:34:05" },
            { "1351719245000", "24", "0", "2012-10-31 16:34:05" },
            { "1351719245000", "25", "0", "2012-10-31 16:34:05" },
            { "1351719245000", "26", "0", "2012-10-31 16:34:05" },
            { "1351719245000", "27", "0", "2012-10-31 16:34:05" },
            { "1351719245000", "28", "0", "2012-10-31 16:34:05" },
            { "1351719245000", "29", "0", "2012-10-31 16:34:05" },
            { "1351719245000", "30", "0", "2012-10-31 16:34:05" },
            { "1351719245000", "31", "0", "2012-10-31 16:34:05" },
            { "1351719245000", "32", "0", "2012-10-31 16:34:05" },
            { "1351719245000", "21", "100000000000", "2012-10-31 16:34:05.1" },
            { "1351719245000", "21", "123456789012", "2012-10-31 16:34:05.1" },
            { "1351719245000", "22", "123456789012", "2012-10-31 16:34:05.12" },
            { "1351719245000", "23", "123456789012",
                "2012-10-31 16:34:05.123" },
            { "1351719245000", "24", "123456789012",
                "2012-10-31 16:34:05.1234" },
            { "1351719245000", "25", "123456789012",
                "2012-10-31 16:34:05.12345" },
            { "1351719245000", "26", "123456789012",
                "2012-10-31 16:34:05.123456" },
            { "1351719245000", "27", "123456789012",
                "2012-10-31 16:34:05.1234567" },
            { "1351719245000", "28", "123456789012",
                "2012-10-31 16:34:05.12345678" },
            { "1351719245000", "29", "123456789012",
                "2012-10-31 16:34:05.123456789" },
            { "1351719245000", "30", "123456789112",
                "2012-10-31 16:34:05.1234567891" },
            { "1351719245000", "31", "123456789012",
                "2012-10-31 16:34:05.12345678901" },
            { "1351719245000", "32", "123456789012",
                "2012-10-31 16:34:05.123456789012" },
            { "1351719245000", "0", "0", "2012-10-31 16:34:05" },
            { "1351719245000", "0", "123456789012",
                "2012-10-31 16:34:05.123456789" },
            { "1351719245000", "0", "123456789012",
                "2012-10-31 16:34:05.123456789" },
            { "1351719245000", "0", "123456789012",
                "2012-10-31 16:34:05.123456789" },
            { "1351719245000", "0", "123456789012",
                "2012-10-31 16:34:05.123456789" },
            { "1351719245000", "0", "123456789012",
                "2012-10-31 16:34:05.123456789" },
            { "1351719245000", "0", "123456789012",
                "2012-10-31 16:34:05.123456789" },
            { "1351719245000", "0", "123456789012",
                "2012-10-31 16:34:05.123456789" },
            { "1351719245000", "0", "123456789012",
                "2012-10-31 16:34:05.123456789" },
            { "1351719245000", "0", "123456789012",
                "2012-10-31 16:34:05.123456789" },
            { "1351719245000", "0", "123456789012",
                "2012-10-31 16:34:05.123456789" },
            { "1351719245000", "0", "123456789012",
                "2012-10-31 16:34:05.123456789" },
            { "1351719245000", "0", "123456789012",
                "2012-10-31 16:34:05.123456789" },
            { "1351719245123", "0", "-1", "2012-10-31 16:34:05.123" },
            { "1351719245123", "21", "-1", "2012-10-31 16:34:05.1" },
            { "1351719245123", "21", "-1", "2012-10-31 16:34:05.1" },
            { "1351719245123", "22", "-1", "2012-10-31 16:34:05.12" },
            { "1351719245123", "23", "-1", "2012-10-31 16:34:05.123" },
            { "1351719245000", "32", "123456789010",
                "2012-10-31 16:34:05.12345678901" },
            { "1351719245000", "32", "123456789110",
                "2012-10-31 16:34:05.12345678911" },
            { "1351719245000", "32", "123456789100",
                "2012-10-31 16:34:05.1234567891" },
            { "1351719245000", "32", "123456789000",
                "2012-10-31 16:34:05.123456789" },
            { "1351719245000", "32", "123456780000",
                "2012-10-31 16:34:05.12345678" },
            { "1351719245000", "32", "123456700000",
                "2012-10-31 16:34:05.1234567" },
            { "1351719245000", "32", "123456000000",
                "2012-10-31 16:34:05.123456" },
            { "1351719245000", "32", "123450000000",
                "2012-10-31 16:34:05.12345" },
            { "1351719245000", "32", "123400000000",
                "2012-10-31 16:34:05.1234" },
            { "1351719245000", "32", "123000000000",
                "2012-10-31 16:34:05.123" },
            { "1351719245000", "32", "120000000000", "2012-10-31 16:34:05.12" },
            { "1351719245000", "32", "100000000000", "2012-10-31 16:34:05.1" },
            { "1351719245000", "32", "000000000000", "2012-10-31 16:34:05" },
            { "1351719245000", "-1", "000000000000", "2012-10-31 16:34:05" },

        };

        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          sb.append("Unit testing AS400JDBCTimestamp\n");
        } else {
          sb.append("Unit testing " + NATIVE_TIMESTAMP_CLASSNAME + "\n");
        }
        TimeZone currentTimeZone = TimeZone.getDefault();
        sb.append("Current timezone is " + currentTimeZone + "\n");
        int passed = 0;
        int total = testcases.length;
        for (int i = 0; i < total; i++) {

          long time = Long.parseLong(testcases[i][0]);
          sb.append("Original Time = " + time + "\n");
          long offset = currentTimeZone.getOffset(time);
          time = time - 18000000 /* get to universal time */
              - offset; /* adjust for local time */
          sb.append("Offset        = " + offset + "\n");
          sb.append("Adjusted Time = " + time + "\n");
          int len = Integer.parseInt(testcases[i][1]);
          long picos = Long.parseLong(testcases[i][2]);
          String expected = testcases[i][3];

          Timestamp ts;
          if (len == 0) {
            if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
              ts = new AS400JDBCTimestamp(time);
            } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {

              ts = (Timestamp) JDReflectionUtil
                  .createObject(NATIVE_TIMESTAMP_CLASSNAME, time);
            } else {
              throw new Exception(
                  "Driver not supported by this testcase, please fix");
            }
            if (picos >= 0) {
              ts.setNanos((int) (picos / 1000));
            }
          } else if (len == SETTIME) {
            if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
              ts = new AS400JDBCTimestamp(0);
            } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
              ts = (Timestamp) JDReflectionUtil
                  .createObject(NATIVE_TIMESTAMP_CLASSNAME, 0L);
            } else {
              throw new Exception(
                  "Driver not supported by this testcase, please fix");
            }
            ts.setTime(time);
          } else {
            if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
              ts = new AS400JDBCTimestamp(time, len);
            } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
              ts = (Timestamp) JDReflectionUtil
                  .createObject(NATIVE_TIMESTAMP_CLASSNAME, time, len);

            } else {
              throw new Exception(
                  "Driver not supported by this testcase, please fix");
            }
            if (picos >= 0) {
              JDReflectionUtil.callMethod_V(ts, "setPicos", picos);
            }
          }

          String output = ts.toString();
          if (!output.equals(expected)) {
            sb.append("For case " + i + " got " + output + " sb " + expected
                + " for time=" + time + " len=" + len + " picos=" + picos
                + "\n");
          } else {
            passed++;
          }

        }

        String[][] valueOfTestcases = {
            { "2012-10-31 16:34:05", "2012-10-31 16:34:05.0" },
            { "2012-10-31 16:34:05.1", "2012-10-31 16:34:05.1" },
            { "2012-10-31 16:34:05.12", "2012-10-31 16:34:05.12" },
            { "2012-10-31 16:34:05.123", "2012-10-31 16:34:05.123" },
            { "2012-10-31 16:34:05.1234", "2012-10-31 16:34:05.1234" },
            { "2012-10-31 16:34:05.12345", "2012-10-31 16:34:05.12345" },
            { "2012-10-31 16:34:05.123456", "2012-10-31 16:34:05.123456" },
            { "2012-10-31 16:34:05.1234567", "2012-10-31 16:34:05.1234567" },
            { "2012-10-31 16:34:05.12345678", "2012-10-31 16:34:05.12345678" },
            { "2012-10-31 16:34:05.123456789",
                "2012-10-31 16:34:05.123456789" },
            { "2012-10-31 16:34:05.1234567891",
                "2012-10-31 16:34:05.1234567891" },
            { "2012-10-31 16:34:05.12345678901",
                "2012-10-31 16:34:05.12345678901" },
            { "2012-10-31 16:34:05.123456789012",
                "2012-10-31 16:34:05.123456789012" },
            { "2012-10-31 16:34:05.10", "2012-10-31 16:34:05.1" },
            { "2012-10-31 16:34:05.100", "2012-10-31 16:34:05.1" },
            { "2012-10-31 16:34:05.1000", "2012-10-31 16:34:05.1" },
            { "2012-10-31 16:34:05.10000", "2012-10-31 16:34:05.1" },
            { "2012-10-31 16:34:05.100000", "2012-10-31 16:34:05.1" },
            { "2012-10-31 16:34:05.1000000", "2012-10-31 16:34:05.1" },
            { "2012-10-31 16:34:05.10000000", "2012-10-31 16:34:05.1" },
            { "2012-10-31 16:34:05.100000000", "2012-10-31 16:34:05.1" },
            { "2012-10-31 16:34:05.1000000000", "2012-10-31 16:34:05.1" },
            { "2012-10-31 16:34:05.10000000000", "2012-10-31 16:34:05.1" },
            { "2012-10-31 16:34:05.100000000000", "2012-10-31 16:34:05.1" },
            { "2012-10-31 16:34:05.1000000000000", "2012-10-31 16:34:05.1" },
            { "2012-10-31 16:34:05.10000000000000", "2012-10-31 16:34:05.1" },
            { "2012-10-31 16:34:05.100000000000000", "2012-10-31 16:34:05.1" },
            { "2012-10-31 16:34:05.100000000000123", "2012-10-31 16:34:05.1" },

        };
        total += valueOfTestcases.length;
        for (int i = 0; i < valueOfTestcases.length; i++) {
          String inString = valueOfTestcases[i][0];
          String expected = valueOfTestcases[i][1];
          Timestamp ts;
          if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            ts = AS400JDBCTimestamp.valueOf(inString);
          } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            ts = (Timestamp) JDReflectionUtil.callStaticMethod_O(
                NATIVE_TIMESTAMP_CLASSNAME, "valueOf", inString);

          } else {
            throw new Exception(
                "Driver not supported by this testcase, please fix");
          }

          String stringValue = ts.toString();
          if (!stringValue.equals(valueOfTestcases[i][1])) {
            sb.append("For valueOf case " + i + " got " + stringValue + " sb "
                + expected + " for " + inString + "\n");
          } else {
            passed++;
          }
        }

        //
        // Other miscellaneous testcases
        //
        Timestamp ts;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          ts = (AS400JDBCTimestamp) AS400JDBCTimestamp
              .valueOf("2012-10-31 16:34:05.123456789012");
        } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          ts = (Timestamp) JDReflectionUtil.callStaticMethod_O(
              NATIVE_TIMESTAMP_CLASSNAME, "valueOf",
              "2012-10-31 16:34:05.123456789012");
        } else {
          throw new Exception(
              "Driver not supported by this testcase, please fix");
        }

        long picos = JDReflectionUtil.callMethod_L(ts, "getPicos");
        total++;
        if (picos == 123456789012L) {
          passed++;
        } else {
          sb.append("picos = " + picos + " sb 123456789012\n");
        }

        int nanos = ts.getNanos();
        total++;
        if (nanos == 123456789L) {
          passed++;
        } else {
          sb.append("nanos = " + nanos + " sb 123456789\n");
        }

        String[][] comparisionTestcases = {
            /* ts1 ts2 equals equalsO before after compareTo compareTo(Date */
            { "A2012-10-31 16:34:05", "A2012-10-31 16:34:05", "true", "true",
                "false", "false", "0", "0" },
            { "A2012-10-31 16:34:05.1", "A2012-10-31 16:34:05.2", "false",
                "false", "true", "false", "-1", "-1" },
            { "A2012-10-31 16:34:05.2", "A2012-10-31 16:34:05.1", "false",
                "false", "false", "true", "1", "1" },
            { "A2012-10-31 16:34:05", "2012-10-31 16:34:05", "true", "true",
                "false", "false", "0", "0" },
            { "A2012-10-31 16:34:05.1", "2012-10-31 16:34:05.2", "false",
                "false", "true", "false", "-1", "-1" },
            { "A2012-10-31 16:34:05.2", "2012-10-31 16:34:05.1", "false",
                "false", "false", "true", "1", "1" },
            { "2012-10-31 16:34:05", "A2012-10-31 16:34:05", "true", "true",
                "false", "false", "0", "0" },
            { "2012-10-31 16:34:05.1", "A2012-10-31 16:34:05.2", "false",
                "false", "true", "false", "-1", "-1" },
            { "2012-10-31 16:34:05.2", "A2012-10-31 16:34:05.1", "false",
                "false", "false", "true", "1", "1" },
            { "2012-10-31 16:34:05", "2012-10-31 16:34:05", "true", "true",
                "false", "false", "0", "0" },
            { "2012-10-31 16:34:05.1", "2012-10-31 16:34:05.2", "false",
                "false", "true", "false", "-1", "-1" },
            { "2012-10-31 16:34:05.2", "2012-10-31 16:34:05.1", "false",
                "false", "false", "true", "1", "1" },

            { "A2012-10-31 16:34:05.000000000001",
                "A2012-10-31 16:34:05.000000000001", "true", "true", "false",
                "false", "0", "0" },
            { "A2012-10-31 16:34:05.000000000001",
                "A2012-10-31 16:34:05.000000000002", "false", "false", "true",
                "false", "-1", "-1" },
            { "A2012-10-31 16:34:05.000000000002",
                "A2012-10-31 16:34:05.000000000001", "false", "false", "false",
                "true", "1", "1" },

        };

        for (int i = 0; i < comparisionTestcases.length; i++) {
          String ts1String = comparisionTestcases[i][0];
          String ts2String = comparisionTestcases[i][1];
          boolean equalsResult = Boolean.valueOf(comparisionTestcases[i][2])
              .booleanValue();
          boolean equalsObjectResult = Boolean
              .valueOf(comparisionTestcases[i][3]).booleanValue();
          boolean beforeResult = Boolean.valueOf(comparisionTestcases[i][4])
              .booleanValue();
          boolean afterResult = Boolean.valueOf(comparisionTestcases[i][5])
              .booleanValue();
          int compareToResult = Integer.parseInt(comparisionTestcases[i][6]);
          int compareToObjectResult = Integer
              .parseInt(comparisionTestcases[i][7]);

          Timestamp ts1;
          if (ts1String.charAt(0) == 'A') {
            ts1String = ts1String.substring(1);
            if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
              ts1 = AS400JDBCTimestamp.valueOf(ts1String);
              if (!(ts1 instanceof AS400JDBCTimestamp)) {
                Timestamp temp = new AS400JDBCTimestamp(ts1.getTime());
                temp.setNanos(ts1.getNanos());
                ts1 = temp;
              }
            } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
              ts1 = (Timestamp) JDReflectionUtil.callStaticMethod_O(
                  NATIVE_TIMESTAMP_CLASSNAME, "valueOf", ts1String);

              if (!(JDReflectionUtil.instanceOf(ts1,
                  NATIVE_TIMESTAMP_CLASSNAME))) {
                Timestamp temp = (Timestamp) JDReflectionUtil
                    .createObject(NATIVE_TIMESTAMP_CLASSNAME, ts1.getTime());
                temp.setNanos(ts1.getNanos());
                ts1 = temp;
              }

            } else {
              throw new Exception(
                  "Driver not supported by this testcase, please fix");
            }

            ts1String = "A" + ts1String;
          } else {
            ts1 = Timestamp.valueOf(ts1String);

          }
          Timestamp ts2;
          if (ts2String.charAt(0) == 'A') {
            ts2String = ts2String.substring(1);
            if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
              ts2 = AS400JDBCTimestamp.valueOf(ts2String);
              if (!(ts2 instanceof AS400JDBCTimestamp)) {
                Timestamp temp = new AS400JDBCTimestamp(ts2.getTime());
                temp.setNanos(ts2.getNanos());
                ts2 = temp;
              }
            } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
              ts2 = (Timestamp) JDReflectionUtil.callStaticMethod_O(
                  NATIVE_TIMESTAMP_CLASSNAME, "valueOf", ts2String);
              if (!JDReflectionUtil.instanceOf(ts2,
                  NATIVE_TIMESTAMP_CLASSNAME)) {
                Timestamp temp = (Timestamp) JDReflectionUtil
                    .createObject(NATIVE_TIMESTAMP_CLASSNAME, ts2.getTime());
                temp.setNanos(ts2.getNanos());
                ts2 = temp;
              }

            } else {
              throw new Exception(
                  "Driver not supported by this testcase, please fix");
            }

            ts2String = "A" + ts2String;
          } else {
            ts2 = Timestamp.valueOf(ts2String);

          }

          total++;
          if (ts1.equals(ts2) == equalsResult) {
            passed++;
          } else {
            sb.append(ts1 + ".equals(" + ts2 + ") returned " + ts1.equals(ts2)
                + " sb " + equalsResult + "\n");
          }

          total++;
          if (ts1.equals((Object) ts2) == equalsObjectResult) {
            passed++;
          } else {
            sb.append(ts1 + ".equals((Object)" + ts2 + ") returned "
                + ts1.equals((Object) ts2) + " sb " + equalsObjectResult
                + "\n");
          }

          total++;
          if (ts1.before(ts2) == beforeResult) {
            passed++;
          } else {
            sb.append(ts1 + ".before(" + ts2 + ") returned " + ts1.before(ts2)
                + " sb " + beforeResult + "\n");
          }

          total++;
          if (ts1.after(ts2) == afterResult) {
            passed++;
          } else {
            sb.append(ts1 + ".after(" + ts2 + ") returned " + ts1.after(ts2)
                + " sb " + afterResult + "\n");
          }

          total++;
          if (ts1.compareTo(ts2) == compareToResult) {
            passed++;
          } else {
            sb.append(ts1 + ".compareTo(" + ts2 + ") returned "
                + ts1.compareTo(ts2) + " sb " + compareToResult + "\n");
          }

          total++;
          Class[] argTypes = new Class[1];
          argTypes[0] = Class.forName("java.lang.Object");
          Object[] args = new Object[1];
          args[0] = ts2;
          int answer = JDReflectionUtil.callMethod_I(ts1, "compareTo", argTypes,
              args);
          if (answer == compareToObjectResult) {
            passed++;
          } else {
            sb.append(ts1 + ".compareTo((object)" + ts2 + ") returned " + answer
                + " sb " + compareToObjectResult + "\n");
          }

        }

        total++;

        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          if (AS400JDBCTimestamp.valueOf("2013-04-20 12:31:12.123456789012")
              .equals("A STRING")) {
            sb.append("Error equal to a string\n");
          } else {
            passed++;
          }
          total++;
          try {
            AS400JDBCTimestamp.valueOf((String) null);
          } catch (IllegalArgumentException e) {
            passed++;
          }
        } else if (getDriver() == JDTestDriver.DRIVER_NATIVE) {

          if (JDReflectionUtil.callStaticMethod_O(NATIVE_TIMESTAMP_CLASSNAME,
              "valueOf", "2013-04-20 12:31:12.123456789012")
              .equals("A STRING")) {
            sb.append("Error equal to a string\n");
          } else {
            passed++;
          }
          total++;
          try {
            JDReflectionUtil.callStaticMethod_O(NATIVE_TIMESTAMP_CLASSNAME,
                "valueOf", (String) null);

          } catch (IllegalArgumentException e) {
            passed++;
          }

        }

        sb.append(
            "Test completed " + passed + " of " + total + " successful\n");
        assertCondition(passed == total, sb);
      } catch (Throwable e) {
        failed(e, "Unexpected Exception" +sb.toString());
      }

    }
  }

  /**
   * getTimestamp() - Get from a BOOLEAN.
   **/
  public void Var060() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_TRUE");
        Timestamp v = rs.getTimestamp("C_BOOLEAN");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTimestamp() - Get from a BOOLEAN.
   **/
  public void Var061() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_FALSE");
        Timestamp v = rs.getTimestamp("C_BOOLEAN");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getTimestamp() - Get from a BOOLEAN.
   **/
  public void Var062() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_NULL");
        Timestamp v = rs.getTimestamp("C_BOOLEAN");
        assertCondition(v == null);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

}
