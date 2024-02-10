///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetCharacterStream.java
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

import java.io.Reader;
import java.sql.Connection;
import java.sql.DataTruncation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;

/**
 * Testcase JDRSGetCharacterStream. This tests the following method of the JDBC
 * ResultSet class:
 * 
 * <ul>
 * <li>getCharacterStream()
 * </ul>
 **/
public class JDRSGetCharacterStream extends JDTestcase {

  // Private data.
  private Statement statement_;
  private Statement statement1_;

  private Connection connection2_;
  private Statement statement2_;
  StringBuffer sb = new StringBuffer();

  protected String methodName = "getCharacterStream";

  /**
   * Constructor.
   **/
  public JDRSGetCharacterStream(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDRSGetCharacterStream", namesAndVars, runMode,
        fileOutputStream, password);
  }

  public JDRSGetCharacterStream(AS400 systemObject, String testcaseName,
      Hashtable namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password) {
    super(systemObject, testcaseName, namesAndVars, runMode, fileOutputStream,
 password);
  }

  boolean checkLevel() {
    return checkJdbc20();
  }

  boolean isLevel() {
    return isJdbc20();
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

    //
    // look for jdk1.4
    //

    if (isLevel()) {
      // SQL400 - driver neutral...
      String url = baseURL_
          // String url = "jdbc:as400://" + systemObject_.getSystemName()
         ;
      connection_ = testDriver_.getConnection(url + ";lob threshold=30000",systemObject_.getUserId(),encryptedPassword_,
          "JDRSGetCharacterStream");
      setAutoCommit(connection_, false); // @E1A

      statement_ = connection_.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      statement1_ = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
          ResultSet.CONCUR_UPDATABLE);
      statement_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_GET
          + " (C_KEY) VALUES ('DUMMY_ROW')");

      // Force LOB locators.
      connection2_ = testDriver_.getConnection(url + ";lob threshold=0",
          "JDRSGetCharacterStream2");

      setAutoCommit(connection2_, false); // @E1A

      statement2_ = connection2_.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    if (isLevel()) {
      statement2_.close();
      connection2_.commit(); // @E1A
      connection2_.close();

      statement_.close();
      statement1_.close();
      connection_.commit();
      connection_.close(); // @E1A
    }
  }

  protected void cleanupConnections() throws Exception {
    if (isLevel()) {
      connection2_.commit(); // @E1A
      connection2_.close();

      connection_.commit();
      connection_.close(); // @E1A
    }
  }

  /**
   * getCharacterStream() - Should throw exception when the result set is
   * closed.
   **/
  public void Var001() {
    if (checkLevel()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        rs.next();
        rs.close();
        JDReflectionUtil.callMethod_O(rs, methodName, 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getCharacterStream() - Should throw exception when cursor is not pointing
   * to a row.
   **/
  public void Var002() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, null);
        Reader v = (Reader) JDReflectionUtil.callMethod_O(rs, methodName, 1);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getCharacterStream() - Should throw an exception when the column is an
   * invalid index.
   **/
  public void Var003() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Reader v = (Reader) JDReflectionUtil.callMethod_O(rs, methodName, 100);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getCharacterStream() - Should throw an exception when the column is 0.
   **/
  public void Var004() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Reader v = (Reader) JDReflectionUtil.callMethod_O(rs, methodName, 0);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getCharacterStream() - Should throw an exception when the column is -1.
   **/
  public void Var005() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Reader v = (Reader) JDReflectionUtil.callMethod_O(rs, methodName, -1);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getCharacterStream() - Should work when the column index is valid.
   **/
  public void Var006() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Reader v = (Reader) JDReflectionUtil.callMethod_O(rs, methodName, 12);
        sb.setLength(0);
        assertCondition(compare(v,
            "Toolbox for Java                                  ", sb), sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Should throw an exception when the column name is
   * null.
   **/
  public void Var007() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable(
          "JCC throws null pointer exception when column name is null ");
    } else {

      if (checkLevel()) {
        try {
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs, "CHAR_FULL");
          JDReflectionUtil.callMethod_OS(rs, methodName, null);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getCharacterStream() - Should throw an exception when the column name is an
   * empty string.
   **/
  public void Var008() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        JDReflectionUtil.callMethod_OS(rs, methodName, "");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getCharacterStream() - Should throw an exception when the column name is
   * invalid.
   **/
  public void Var009() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        JDReflectionUtil.callMethod_OS(rs, methodName, "INVALID");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getCharacterStream() - Should throw an exception when the column name is
   * valid.
   **/
  public void Var010() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_CHAR_50");
        sb.setLength(0);
        assertCondition(compare(v,
            "Toolbox for Java                                  ", sb), sb);
        rs.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Should work when an update is pending.
   **/
  public void Var011() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement1_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GET + " FOR UPDATE");
        JDRSTest.position(rs, "UPDATE_SANDBOX");
        rs.updateString("C_VARCHAR_50", "World Peace");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        sb.setLength(0);
        boolean check = (compare(v, "World Peace", sb));
        rs.close();
        assertCondition(check, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Should work when an update has been done.
   **/
  public void Var012() {
    if (checkLevel()) {
      try {
        sb.setLength(0);
        ResultSet rs = statement1_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GET + " FOR UPDATE");
        JDRSTest.position(rs, "UPDATE_SANDBOX");
        rs.updateString("C_CHAR_50", "New Planet");
        rs.updateRow();
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_CHAR_50");
        boolean check;
        String expected = "New Planet                                        ";
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          expected = "New Planet";
        }
        check = (compare(v, expected, sb));
        rs.close();
        assertCondition(check, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Should work when the current row is the insert row,
   * when an insert is pending.
   **/
  public void Var013() {
    if (checkLevel()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support moveToInserRow");
        return;
      }
      try {
        ResultSet rs = statement1_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GET + " FOR UPDATE");
        rs.moveToInsertRow();
        rs.updateString("C_VARCHAR_50", "El Nino");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        sb.setLength(0);
        boolean check = (compare(v, "El Nino", sb));
        rs.close();
        assertCondition(check, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Should work when the current row is the insert row,
   * when an insert has been done.
   **/
  public void Var014() {
    if (checkLevel()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support moveToInserRow");
        return;
      }
      try {
        ResultSet rs = statement1_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GET + " FOR UPDATE");
        rs.moveToInsertRow();
        rs.updateString("C_VARCHAR_50", "Year 2000 Problem");
        rs.insertRow();
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        sb.setLength(0);
        boolean check = (compare(v, "Year 2000 Problem", sb));
        rs.close();
        assertCondition(check, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Should throw an exception on a deleted row.
   **/
  public void Var015() {
    if (checkLevel()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC doesn't throw exception for get on deleted row");
        return;
      }
      ResultSet rs = null;
      try {
        rs = statement1_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GET + " FOR UPDATE");
        JDRSTest.position(rs, "DUMMY_ROW");
        rs.deleteRow();
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      } finally {
        try {
          if (rs != null)
            rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * getCharacterStream() - Should return null when the column is NULL.
   **/
  public void Var016() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_NULL");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        assertCondition(v == null);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Get from a SMALLINT.
   **/
  public void Var017() {
    sb.setLength(0);
    sb.append(" -- Update 11/17/2011 ");
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "NUMBER_POS");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_SMALLINT");
        String expected = "198";
        boolean check = (compare(v, expected, sb));
        assertCondition(check, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- " + sb.toString());
      }
    }
  }

  /**
   * getCharacterStream() - Get from a INTEGER.
   **/
  public void Var018() {
    if (checkLevel()) {
      sb.setLength(0);
      sb.append(" Updated 11/17/2011");
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "NUMBER_NEG");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_INTEGER");
        String expected = "-98765";
        boolean check = (compare(v, expected, sb));
        assertCondition(check, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- " + sb.toString());
      }
    }
  }

  /**
   * getCharacterStream() - Get from a REAL.
   **/
  public void Var019() {
    if (checkLevel()) {
      sb.setLength(0);
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "NUMBER_POS");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_REAL");
        String expected = "4.4";
        boolean check = (compare(v, expected, sb));
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- Updated 11/17/2011");
      }
    }
  }

  /**
   * getCharacterStream() - Get from a FLOAT.
   **/
  public void Var020() {
    if (checkLevel()) {
      sb.setLength(0);
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "NUMBER_NEG");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_FLOAT");
        String expected = "-5.55";
        boolean check = (compare(v, expected, sb));
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- Updated 11/17/2011");
      }
    }
  }

  /**
   * getCharacterStream() - Get from a DOUBLE.
   **/
  public void Var021() {
    if (checkLevel()) {
      sb.setLength(0);
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "NUMBER_POS");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_DOUBLE");
        String expected = "6.666";
        boolean check = (compare(v, expected, sb));
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- Updated 11/17/2011");
      }
    }
  }

  /**
   * getCharacterStream() - Get from a DECIMAL.
   **/
  public void Var022() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "NUMBER_POS");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_DECIMAL_50"); // @F1A
        sb.setLength(0);
        assertCondition(compare(v, "7", sb), "Changed 11/17/2011 " + sb); // @F1A
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- changed 11/17/2011"); // @F1A
      }
    }
  }

  /**
   * getCharacterStream() - Get from a NUMERIC.
   **/
  public void Var023() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "NUMBER_NEG");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_NUMERIC_105"); // @F1A
        sb.setLength(0);
        assertCondition(compare(v, "-10.10105", sb), sb); // @F1a
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e,
            "java.sql.SQLException -- changed 11/17/2011");
      }
    }
  }

  /**
   * getCharacterStream() - Get from an empty CHAR.
   **/
  public void Var024() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_EMPTY");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_CHAR_50");
        sb.setLength(0);
        assertCondition(compare(v,
            "                                                  ", sb), sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Get from a full CHAR.
   **/
  public void Var025() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_CHAR_50");
        sb.setLength(0);
        assertCondition(compare(v,
            "Toolbox for Java                                  ", sb), sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Get from an empty VARCHAR.
   **/
  public void Var026() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_EMPTY");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        sb.setLength(0);
        assertCondition(compare(v, "", sb), sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Get from a full VARCHAR.
   **/
  public void Var027() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        sb.setLength(0);
        assertCondition(compare(v, "Java Toolbox", sb), sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Get from a BINARY.
   **/
  public void Var028() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns garbage from binary");
      return;
    }
    if (checkLevel()) {
      try {
        String expected;

        if (isToolboxDriver())
          expected = "456C6576656E2020202020202020202020202020";
        else
          expected = "Eleven              ";

        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "BINARY_NOTRANS");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_BINARY_20");
        sb.setLength(0);
        boolean passed = compare(v, expected, sb);
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Get from a VARBINARY.
   **/
  public void Var029() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC returns garbage from varbinary");
      return;
    }
    if (checkLevel()) {
      try {
        String expected;
        if (isToolboxDriver())
          expected = "5477656C7665";
        else
          expected = "Twelve";

        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "BINARY_NOTRANS");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARBINARY_20");
        sb.setLength(0);
        boolean passed = compare(v, expected, sb);

        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Get from a CLOB, when the CLOB data is returned in
   * the result set.
   **/
  public void Var030() {
    if (checkLevel()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs, "LOB_FULL");
          Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
              "C_CLOB");
          sb.setLength(0);
          assertCondition(compare(v, JDRSTest.CLOB_FULL, sb), sb);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getCharacterStream() - Get from a CLOB, when the CLOB locator is returned
   * in the result set.
   **/
  public void Var031() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC testcases hangs with locator");
      return;
    }
    if (checkLevel()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs2 = statement2_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs2, "LOB_FULL");
          Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs2, methodName,
              "C_CLOB");
          sb.setLength(0);
          boolean check = (compare(v, JDRSTest.CLOB_FULL, sb));
          rs2.close();
          assertCondition(check, sb);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getCharacterStream() - Get from a DBCLOB, when the DBCLOB data is returned
   * in the result set.
   **/
  public void Var032() {
    if (checkLevel()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs, "LOB_FULL");
          Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
              "C_DBCLOB");
          sb.setLength(0);
          assertCondition(compare(v, JDRSTest.DBCLOB_FULL, sb), sb);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getCharacterStream() - Get from a DBCLOB, when the DBCLOB locator is
   * returned in the result set.
   **/
  public void Var033() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC testcases hangs with locator");
      return;
    }
    if (checkLevel()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs2 = statement2_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs2, "LOB_FULL");
          Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs2, methodName,
              "C_DBCLOB");
          sb.setLength(0);
          boolean check = (compare(v, JDRSTest.DBCLOB_FULL, sb));
          rs2.close();
          assertCondition(check, sb);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getCharacterStream() - Get from a BLOB, when the BLOB data is returned in
   * the result set.
   **/
  public void Var034() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC testcases returns garbage from BLOB");
      return;
    }
    if (checkLevel()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs, "LOB_FULL");
          Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
              "C_BLOB");
          sb.setLength(0);
          if (isToolboxDriver()) // @K1A
            assertCondition(compare(v, JDRSTest.BLOB_FULL, sb), sb); // @K1A
          else // @K1A
            assertCondition(compare(v, new String(JDRSTest.BLOB_FULL), sb), sb);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getCharacterStream() - Get from a BLOB, when the BLOB locator is returned
   * in the result set.
   **/
  public void Var035() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC testcases hangs with locator");
      return;
    }
    if (checkLevel()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs2 = statement2_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs2, "LOB_FULL");
          Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs2, methodName,
              "C_BLOB");
          sb.setLength(0);
          boolean check = false; // @K1A
          if (isToolboxDriver()) // @K1A
            check = (compare(v, JDRSTest.BLOB_FULL, sb)); // @K1A
          else // @K1A
            check = (compare(v, new String(JDRSTest.BLOB_FULL), sb));
          rs2.close();
          assertCondition(check, sb);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getCharacterStream() - Get from a DATE.
   **/
  public void Var036() {
    sb.setLength(0);
    sb.append(" -- getCharacterStream from a Date.  Changed 12/14/2011");
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "DATE_1998");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_DATE");
        if (isToolboxDriver()) // @K1A
          assertCondition(compare(v, "04/08/98", sb), sb); // @K1A
        else // @K1A
          assertCondition(compare(v, "1998-04-08", sb), sb); // @K1A
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + sb.toString()); // @K1A
      }
    }
  }

  /**
   * getCharacterStream() - Get from a TIME.
   **/
  public void Var037() {
    sb.setLength(0);
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "DATE_1998");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_TIME");
        String expected = "08:14:03";
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          expected = "08.14.03";
        }
        boolean check = (compare(v, expected, sb));
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- Updated 11/17/2011");
      }
    }
  }

  /**
   * getCharacterStream() - Get from a TIMESTAMP.
   **/
  public void Var038() {
    sb.setLength(0);
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "DATE_1998");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_TIMESTAMP");
        String expected = "1998-11-18 03:13:42.987654";
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          expected = "1998-11-18-03.13.42.987654";
        }
        boolean check = (compare(v, expected, sb));
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- Updated 11/17/2011");
      }
    }
  }

  /**
   * getCharacterStream() - Get from a DATALINK.
   * 
   * SQL400 - From the native driver's perspective, a datalink column is treated
   * the same was that it is for standard SQL. When you make an unqualified
   * select of a datalink, the SQL statement is implicitly cast to look at the
   * full URL value for the datalink. This is, in essence, a String and can be
   * retrieved with getCharacterStream as other Strings can.
   **/
  public void Var039() {
    sb.setLength(0);
    sb.append(" == getCharacterStream from datalink -- updated 12/14/2011");
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("datalink test N/A for JCC");
      return;
    }
    if (checkLevel()) {
      if (checkDatalinkSupport()) {
        try {
          Statement s = connection_.createStatement();
          ResultSet rs = s
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
          JDRSTest.position0(rs, "LOB_FULL");
          Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
              "C_DATALINK");

          // Note the case... AS/400 DB does its own thing here...
          assertCondition(
              compare(v, "HTTP://SCHUMAN.RCHLAND.IBM.COM/help.html", sb), sb);

        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getCharacterStream() - Get from an empty DATALINK.
   * 
   * SQL400 - From the native driver's perspective, a datalink column is treated
   * the same was that it is for standard SQL. When you make an unqualified
   * select of a datalink, the SQL statement is implicitly cast to look at the
   * full URL value for the datalink. This is, in essence, a String and can be
   * retrieved with getCharacterStream as other Strings can.
   **/
  public void Var040() {
    sb.setLength(0);
    sb.append(" == getCharacterStream from datalink -- updated 12/14/2011");

    if (checkLevel()) {
      if (checkDatalinkSupport()) {
        try {
          Statement s = connection_.createStatement();
          ResultSet rs = s
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
          JDRSTest.position0(rs, "LOB_EMPTY");
          Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
              "C_DATALINK");

          assertCondition(compare(v, "", sb), sb);

        } catch (Exception e) {
          failed(e, "Unexpected Exception " + sb);
        }
      }
    }
  }

  /**
   * getCharacterStream() - Get from a DISTINCT.
   **/
  public void Var041() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC testcases returns garbage from distinct");
      return;
    }

    if (checkLevel()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs, "LOB_EMPTY");
          Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
              "C_DISTINCT");
          sb.setLength(0);
          assertCondition(compare(v, "         ", sb), sb);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getCharacterStream() - Get from a BIGINT.
   **/
  public void Var042() {
    sb.setLength(0);
    sb.append(" == getCharacterStream from datalink -- updated 12/14/2011");
    if (checkLevel()) {
      if (checkBigintSupport()) {
        try {
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs, "NUMBER_POS");
          Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
              "C_BIGINT");
          assertCondition(compare(v, "12374321", sb), sb); // @K1A
        } catch (Exception e) {
          failed(e, "Unexpected Exception"); // @K1A
        }
      }
    }
  }

  /**
   * getCharacterStream() - Verify that no DataTruncation is posted when the max
   * field size is set to 0.
   **/
  public void Var043() {
    if (checkLevel()) {
      try {
        Statement s = connection_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        s.setMaxFieldSize(0);
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        DataTruncation dt = (DataTruncation) rs.getWarnings();
        rs.close();
        s.close();
        sb.setLength(0);
        assertCondition((compare(v, "Java Toolbox", sb)) && (dt == null), sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Verify that data is truncated without a
   * DataTruncation posted when the max field size is set to a value shorter
   * than the byte array.
   **/
  public void Var044() {
    if (checkLevel()) {
      try {
        Statement s = connection_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        s.setMaxFieldSize(18);
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");

        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_CHAR_50");
        SQLWarning w = rs.getWarnings();
        rs.close();
        s.close();
        sb.setLength(0);
        assertCondition((compare(v, "Toolbox for Java  ", sb)) && (w == null),
            sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Get from DFP16:
   **/
  public void Var045() {
    sb.setLength(0);
    if (checkDecFloatSupport() && checkLevel()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP16);
        rs.next();
        Reader v = (Reader) JDReflectionUtil.callMethod_O(rs, methodName, 1);
        String expected = "1.1";
        boolean check = (compare(v, expected, sb));
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- Updated 11/17/2011");
      }
    }
  }

  /**
   * getCharacterStream() - Get from DFP34:
   **/
  public void Var046() {
    if (checkDecFloatSupport() && checkLevel()) {
      sb.setLength(0);
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP34);
        rs.next();
        Reader v = (Reader) JDReflectionUtil.callMethod_O(rs, methodName, 1);
        String expected = "1.1";
        boolean check = (compare(v, expected, sb));
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- Updated 11/17/2011");
      }
    }
  }

  /**
   * getCharacterStream() - Get from SQLXML:
   **/
  public void Var047() {
    if (checkXmlSupport() && checkLevel()) {
      try {
        sb.setLength(0);

        Statement s = connection_.createStatement();
	String sql = "SELECT * FROM " + JDRSTest.RSTEST_GETXML+" a order by RRN(a)";
	System.out.println("SQL is "+sql); 
        ResultSet rs = s
            .executeQuery(sql);
        rs.next();
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_XML");
        assertCondition((compare(v, "<greeting>Hello, world!</greeting>", sb)),
            sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");

      }
    }
  }

  /**
   * getCharacterStream() - Get from a TIMESTAMP(12).
   **/
  public void Var048() {
    if (checkTimestamp12Support()) {
      String expected = "1998-11-18 03:13:42.987654000000";
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        expected = "1998-11-18-03.13.42.987654000000";
      }
      testGet(statement_,
          "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
          "getCharacterStream", expected, " -- added 11/19/2012");

    }
  }

  /**
   * getCharacterStream() - Get from a TIMESTAMP(0).
   **/
  public void Var049() {
    if (checkTimestamp12Support()) {
      String expected = "1998-11-18 03:13:42";
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        expected = "1998-11-18-03.13.42";
      }
      testGet(statement_,
          "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
          "getCharacterStream", expected, " -- added 11/19/2012");

    }
  }

  /**
   * getCharacterStream() - Get from a BOOLEAN true .
   **/
  public void Var050() {
    if (checkBooleanSupport()) {
      sb.setLength(0);

      try {
        ResultSet rs = statement1_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_TRUE");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_BOOLEAN");
        sb.setLength(0);
        assertCondition(compare(v, JDRSTest.BOOLEAN_TRUE_STRING, sb), sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * getCharacterStream() - Get from a BOOLEAN false .
   **/
  public void Var051() {
    if (checkBooleanSupport()) {
      sb.setLength(0);

      try {
        ResultSet rs = statement1_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_FALSE");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_BOOLEAN");

        sb.setLength(0);
        assertCondition(compare(v, JDRSTest.BOOLEAN_FALSE_STRING, sb), sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getCharacterStream() - Get from a BOOLEAN null .
   **/
  public void Var052() {
    if (checkBooleanSupport()) {
      sb.setLength(0);

      try {
        ResultSet rs = statement1_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_NULL");
        Reader v = (Reader) JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_BOOLEAN");
        if (v != null) {
          sb.append("Got " + v + " sb null");
        }

        assertCondition(v == null, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

}
