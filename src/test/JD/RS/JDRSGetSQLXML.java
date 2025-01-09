///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetSQLXML.java
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
import test.JD.JDTestUtilities;

import java.io.FileOutputStream;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.DataTruncation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;

/**
 * Testcase JDRSGetSQLXML. This tests the following method of the JDBC ResultSet
 * class:
 * 
 * <ul>
 * <li>getSQLXML()
 * </ul>
 **/
public class JDRSGetSQLXML extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetSQLXML";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }

  // Private data.
  private Statement statement_;
  private Statement statement1_;

  private Connection connection2_;
  private Statement statement2_;

  private StringBuffer message = new StringBuffer();

  /**
   * Constructor.
   **/
  public JDRSGetSQLXML(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDRSGetSQLXML", namesAndVars, runMode,
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
    if (isJdbc40()) {
      // SQL400 - driver neutral...
      String url = baseURL_
          // String url = "jdbc:as400://" + systemObject_.getSystemName()
          + ";hold statements=true" // var49-50
         ;
      connection_ = testDriver_.getConnection (url + ";lob threshold=30000",systemObject_.getUserId(), encryptedPassword_);

      setAutoCommit(connection_, false); // @E1A

      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        // SQL0270 from JCC when SCROLL_INSENSITIVIE
        // DB2 SQL Error: SQLCODE=-270, SQLSTATE=42997, SQLERRMC=63,
        // 63 A column with a LOB type, distinct type on a LOB type, or
        // structured type cannot be specified in the select-list of an
        // insensitive scrollable cursor
        // 53 A column with a LONG VARCHAR, LONG VARGRAPHIC, DATALINK,
        // LOB, XML type, distinct type on any of these types, or
        // structured type cannot be specified in the select-list of a
        // scrollable cursor.
        statement_ = connection_.createStatement(ResultSet.TYPE_FORWARD_ONLY,
            ResultSet.CONCUR_READ_ONLY);
        statement1_ = connection_.createStatement(ResultSet.TYPE_FORWARD_ONLY,
            ResultSet.CONCUR_UPDATABLE);

      } else {
        statement_ = connection_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement1_ = connection_.createStatement(
            ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      }

      statement_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_GETX
          + " (C_KEY) VALUES ('DUMMY_ROW')");

      // Force LOB locators.
      connection2_ = testDriver_.getConnection (url + ";lob threshold=0",systemObject_.getUserId(), encryptedPassword_);

      setAutoCommit(connection2_, false); // @E1A

      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        statement2_ = connection2_.createStatement();
      } else {
        statement2_ = connection2_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      }
    }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    cleanupConnections();
  }

  protected void cleanupConnections() throws Exception {
    if (isJdbc40()) {
      statement2_.close();
      connection2_.commit(); // @E1A
      connection2_.close();
      statement_.close();
      statement1_.close();
      connection_.commit(); // @E1A
      connection_.close();
    }
  }

  /**
   * Compares a SQLXML with a String.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  private boolean compare(Object i, String b, boolean trim) throws Exception {
    String s = null;
    if (i != null) {
      s = JDReflectionUtil.callMethod_S(i, "getString");
    }
    //
    // For native JDBC driver,
    // only stip the declaration for V7R1 and later
    //
    if (getRelease() < JDTestDriver.RELEASE_V7R1M0
        && getDriver() == JDTestDriver.DRIVER_NATIVE) {
      // Don't strip declaration
    } else {
      b = JDTestUtilities.stripXmlDeclaration(b);
      if (trim) {
        b = b.trim();
      }

    }
    boolean theSame = b.equals(s);
    if (!theSame) {
      message.append("SQLXML is \n********\n" + s
          + "\n********\nExpected \n********\n" + b + "\n********\n");
    } else {
      message.setLength(0);
    }
    return theSame;
  }

  /**
   * getSQLXML() - Should throw exception when the result set is closed.
   **/
  public void Var001() {
    if (checkJdbc40()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        rs.next();
        rs.close();
        JDReflectionUtil.callMethod_O(rs, "getSQLXML", 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Should throw exception when cursor is not pointing to a row.
   **/
  public void Var002() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, null);
        Object v = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 1);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Should throw an exception when the column is an invalid
   * index.
   **/
  public void Var003() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "CHAR_FULL");
        Object v = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 100);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Should throw an exception when the column is 0.
   **/
  public void Var004() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "CHAR_FULL");
        Object v = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 0);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Should throw an exception when the column is -1.
   **/
  public void Var005() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "CHAR_FULL");
        Object v = JDReflectionUtil.callMethod_O(rs, "getSQLXML", -1);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Should work when the column index is valid and CCSID is 37
   **/
  public void Var006() {
    if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable(); // table only created in 54+
      return;
    }
    if (checkJdbc40()) {
      try {
        message = new StringBuffer();
        ResultSet rs = statement_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " ORDER BY C_KEY");
        rs.next();
        Object v = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 2);
        String expected = JDRSTest.SAMPLE_XML1;
        boolean trim = false;
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          trim = true;
        }
        assertCondition(compare(v, expected, trim), message.toString());
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String expected = "Invalid data conversion";
          assertCondition(e.toString().indexOf(expected) > 0,
              "Expected " + expected + " got " + e.toString());
        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Should work when the column index is valid.
   **/
  public void Var007() {
    if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable(); // table only created in 54+
      return;
    }
    if (checkJdbc40()) {
      try {
        message = new StringBuffer();
        ResultSet rs = statement_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " ORDER BY C_KEY");
        rs.next();
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_CLOB0937");
        boolean trim = false;
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          trim = true;
        }

        assertCondition(compare(v, JDRSTest.SAMPLE_XML1, trim),
            message.toString());
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String expected = "Invalid data conversion";
          assertCondition(e.toString().indexOf(expected) > 0,
              "Expected " + expected + " got " + e.toString());
        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Should work when the column index is valid.
   **/
  public void Var008() {
    if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable(); // table only created in 54+
      return;
    }
    if (checkJdbc40()) {
      try {
        message = new StringBuffer();
        ResultSet rs = statement_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " ORDER BY C_KEY");
        rs.next();
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_CLOB1208");
        boolean trim = false;
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          trim = true;
        }

        assertCondition(compare(v, JDRSTest.SAMPLE_XML1, trim),
            message.toString());
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String expected = "Invalid data conversion";
          assertCondition(e.toString().indexOf(expected) > 0,
              "Expected " + expected + " got " + e.toString());
        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Should work when the column index is valid.
   **/
  public void Var009() {
    if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable(); // table only created in 54+
      return;
    }
    if (checkJdbc40()) {
      try {
        message = new StringBuffer();
        ResultSet rs = statement_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " ORDER BY C_KEY");
        rs.next();
        boolean trim = false;
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          trim = true;
        }

        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_DBCLOB1200");
        assertCondition(compare(v, JDRSTest.SAMPLE_XML1, trim),
            message.toString());
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String expected = "Invalid data conversion";
          assertCondition(e.toString().indexOf(expected) > 0,
              "Expected " + expected + " got " + e.toString());
        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Should work when the column index is valid.
   **/
  public void Var010() {
    if (checkJdbc40()) {

      try {
        message = new StringBuffer();
        ResultSet rs = statement_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " ORDER BY C_KEY");
        rs.next();
        boolean trim = false;

        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_BLOB0037");
        assertCondition(compare(v, JDRSTest.SAMPLE_XML1, trim),
            message.toString());
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String expected = "Invalid data conversion";
          assertCondition(e.toString().indexOf(expected) > 0,
              "Expected " + expected + " got " + e.toString());
        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Should work when the column index is valid.
   **/
  public void Var011() {

    if (checkJdbc40()) {

      try {
        message = new StringBuffer();
        ResultSet rs = statement_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " ORDER BY C_KEY");
        rs.next();
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_BLOB1208");
        boolean trim = false;
        assertCondition(compare(v, JDRSTest.SAMPLE_XML1, trim),
            message.toString());
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String expected = "Invalid data conversion";
          assertCondition(e.toString().indexOf(expected) > 0,
              "Expected " + expected + " got " + e.toString());
        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Should work when the column index is valid.
   **/
  public void Var012() {
    if (checkJdbc40()) {

      try {
        message = new StringBuffer();
        ResultSet rs = statement_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " ORDER BY C_KEY");
        rs.next();
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_BLOB1200");
        boolean trim = false;
        assertCondition(compare(v, JDRSTest.SAMPLE_XML1, trim),
            message.toString());
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String expected = "Invalid data conversion";
          assertCondition(e.toString().indexOf(expected) > 0,
              "Expected " + expected + " got " + e.toString());
        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Should throw an exception when the column name is null.
   **/
  public void Var013() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable(
          "JCC throws null pointer exception when column name is null ");
    } else {

      if (checkJdbc40()) {
        try {
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
          JDRSTest.position0(rs, "CHAR_FULL");
          JDReflectionUtil.callMethod_OS(rs, "getSQLXML", null);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getSQLXML() - Should throw an exception when the column name is an empty
   * string.
   **/
  public void Var014() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "CHAR_FULL");
        JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Should throw an exception when the column name is invalid.
   **/
  public void Var015() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "CHAR_FULL");
        JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "INVALID");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Should work when the column name is valid.
   **/
  public void Var016() {
    if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable(); // table only created in 54+
      return;
    }
    if (checkJdbc40()) {
      try {
        message = new StringBuffer();
        ResultSet rs = statement_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " ORDER BY C_KEY");
        rs.next();
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_CLOB0037");
        boolean trim = false;
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          trim = true;
        }
        assertCondition(compare(v, JDRSTest.SAMPLE_XML1, trim),
            message.toString());
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String expected = "Invalid data conversion";
          assertCondition(e.toString().indexOf(expected) > 0,
              "Expected " + expected + " got " + e.toString());
        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Should work when an update is pending.
   **/
  public void Var017() {

    if (checkJdbc40()) {
      if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
        notApplicable("V5R4 or later only");
        return;
      }
      try {
        message = new StringBuffer();
        ResultSet rs = statement1_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " FOR UPDATE");
        rs.next();
        rs.next();
        String test = JDRSTest.SAMPLE_XML2;
        rs.updateString("C_CLOB0037", test);
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_CLOB0037");
        boolean trim = false;
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          trim = true;
        }
        boolean check = compare(v, test, trim);
        rs.close();
        assertCondition(check, message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSQLXML() - Should work when an update has been done.
   **/
  public void Var018() {

    if (checkJdbc40()) {
      if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
        notApplicable("V5R4 or later only");
        return;
      }
      try {
        message = new StringBuffer();
        ResultSet rs = statement1_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " FOR UPDATE");
        rs.next();
        rs.next();
        String test = JDRSTest.SAMPLE_XML2;
        rs.updateString("C_CLOB0037", test);
        rs.updateRow();
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_CLOB0037");
        boolean trim = false;
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          trim = true;
        }
        boolean check = compare(v, test, trim);
        rs.close();
        assertCondition(check, message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSQLXML() - Should work when the current row is the insert row, when an
   * insert is pending.
   **/
  public void Var019() {

    if (checkJdbc40()) {
      if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
        notApplicable("V5R4 or later only");
        return;
      }
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support moveToInserRow");
        return;
      }
      try {
        message = new StringBuffer();

        ResultSet rs = statement1_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " FOR UPDATE");
        rs.next();
        rs.next();
        String test = JDRSTest.SAMPLE_XML2;
        rs.updateString("C_CLOB0037", test);

        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_CLOB0037");
        rs.close();
        boolean trim = false;
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          trim = true;
        }
        boolean check = compare(v, test, trim);
        assertCondition(check, message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSQLXML() - Should work when the current row is the insert row, when an
   * insert has been done.
   **/
  public void Var020() {

    if (checkJdbc40()) {
      if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
        notApplicable("V5R4 or later only");
        return;
      }
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC does not support moveToInserRow");
        return;
      }
      try {
        message = new StringBuffer();
        ResultSet rs = statement1_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " FOR UPDATE");
        rs.moveToInsertRow();
        String test = JDRSTest.SAMPLE_XML2;
        rs.updateString("C_CLOB0037", test);

        rs.insertRow();
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_CLOB0037");
        rs.close();
        boolean trim = false;
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          trim = true;
        }
        boolean check = compare(v, test, trim);
        assertCondition(check, message.toString());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSQLXML() - Should throw an exception on a deleted row.
   **/
  public void Var021() {
    if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable(); // table only created in 54+
      return;
    }
    if (checkJdbc40()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC doesn't throw exception for get on deleted row");
        return;
      }
      ResultSet rs = null;
      try {
        rs = statement1_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " FOR UPDATE");
        rs.last();
        rs.deleteRow();
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_CLOB0037");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      } finally {
        try {
          if (rs != null) {
            rs.close();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * getSQLXML() - Should return null when the column is NULL.
   **/
  public void Var022() {

    if (checkJdbc40()) {
      if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
        notApplicable("V5R4 or later only");
        return;
      }
      try {
        ResultSet rs = statement1_.executeQuery("SELECT * FROM "
            + JDRSTest.RSTEST_GETXML + " WHERE C_CLOB0037 IS NULL");
        rs.next();
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_CLOB0037");
        assertCondition(v == null);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getSQLXML() - Get from a SMALLINT.
   **/
  public void Var023() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "NUMBER_NEG");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_SMALLINT");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Get from a INTEGER.
   **/
  public void Var024() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "NUMBER_POS");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_INTEGER");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Get from a REAL.
   **/
  public void Var025() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "NUMBER_NEG");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_REAL");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Get from a FLOAT.
   **/
  public void Var026() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "NUMBER_POS");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_FLOAT");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Get from a DOUBLE.
   **/
  public void Var027() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "NUMBER_NEG");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_DOUBLE");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Get from a DECIMAL.
   **/
  public void Var028() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "NUMBER_POS");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_DECIMAL_50");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Get from a NUMERIC.
   **/
  public void Var029() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "NUMBER_NEG");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_NUMERIC_105");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Get from an empty CHAR.
   * 
   * Updated 3/20/2008
   * 
   * Note: Both drivers allow this today but should probably disallow this
   * because the output is not valid SQL.
   * 
   * Update 12/15/2011 -- This now throws an exception because this is not valid
   * XML Update 08/09/2013 -- the XMLTEXT function returns non-XML in an XML
   * column, so we need to support those values.
   **/
  public void Var030() {
    if (checkJdbc40()) {
      message = new StringBuffer(" -- Updated 12/15/2011");
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "CHAR_EMPTY");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_CHAR_50");

        boolean check = compare(v, "", false);

        assertCondition(check, message.toString());

      } catch (Exception e) {
        String expected = "Data type mismatch";
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          expected = "Invalid data conversion";
        }
        assertCondition(e.toString().indexOf(expected) > 0,
            "Expected " + expected + " got " + e.toString() + message);
      }
    }
  }

  /**
   * getSQLXML() - Get from a full CHAR.
   **/
  public void Var031() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "CHAR_FULL");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_CHAR_50");

        boolean trim = false;
        assertCondition(compare(v, "<d>Toolbox for Java</d>", trim),
            message.toString());

      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String expected = "Invalid data conversion";
          assertCondition(e.toString().indexOf(expected) > 0,
              "Expected " + expected + " got " + e.toString());
        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Get from an empty VARCHAR.
   **/
  public void Var032() {
    if (checkJdbc40()) {
      message = new StringBuffer(" -- Var 32 updated 12/15/2011 ");
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "CHAR_EMPTY");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_VARCHAR_50");
        boolean check = compare(v, "", false);

        assertCondition(check, "got '" + v + "' Should have got empty string "
            + message.toString());

      } catch (Exception e) {
        String expected = "Data type mismatch";
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          expected = "Invalid data conversion";
        }

        assertCondition(e.toString().indexOf(expected) > 0,
            "Expected " + expected + " got " + e.toString());
      }
    }
  }

  /**
   * getSQLXML() - Get from a full VARCHAR.
   **/
  public void Var033() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "CHAR_FULL");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_VARCHAR_50");
        boolean trim = false;
        assertCondition(compare(v, "<d>Java Toolbox</d>", trim),
            message.toString());

      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String expected = "Invalid data conversion";
          assertCondition(e.toString().indexOf(expected) > 0,
              "Expected " + expected + " got " + e.toString());
        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Get from a BINARY.
   **/
  public void Var034() {
    if (checkJdbc40()) {
      try {

        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "BINARY_NOTRANS");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_BINARY_20");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Get from a VARBINARY.
   **/
  public void Var035() {
    if (checkJdbc40()) {
      try {

        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "BINARY_NOTRANS");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_VARBINARY_20");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Get from a CLOB, when the CLOB data is returned in the result
   * set.
   **/
  public void Var036() {
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          message = new StringBuffer();
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
          JDRSTest.position0(rs, "LOB_FULL");
          Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_CLOB");
          boolean trim = false;
          assertCondition(compare(v, JDRSTest.CLOB_FULLX, trim),
              message.toString());
        } catch (Exception e) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            String expected = "Invalid data conversion";
            assertCondition(e.toString().indexOf(expected) > 0,
                "Expected " + expected + " got " + e.toString());
          } else {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
  }

  /**
   * getSQLXML() - Get from a CLOB, when the CLOB locator is returned in the
   * result set.
   **/
  public void Var037() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC driver hangs");
      return;
    }
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          message = new StringBuffer();
          ResultSet rs = statement2_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
          JDRSTest.position0(rs, "LOB_FULL");
          Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_CLOB");
          boolean trim = false;
          assertCondition(compare(v, JDRSTest.CLOB_FULLX, trim),
              message.toString());
        } catch (Exception e) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            String expected = "Invalid data conversion";
            assertCondition(e.toString().indexOf(expected) > 0,
                "Expected " + expected + " got " + e.toString());
          } else {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
  }

  /**
   * getSQLXML() - Get from a DBCLOB, when the DBCLOB data is returned in the
   * result set.
   **/
  public void Var038() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC driver hangs");
      return;
    }
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          message = new StringBuffer();
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
          JDRSTest.position0(rs, "LOB_FULL");
          Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
              "C_DBCLOB");
          boolean trim = false;
          assertCondition(compare(v, JDRSTest.DBCLOB_FULLX, trim),
              message.toString());
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Get from a DBCLOB, when the DBCLOB locator is returned in the
   * result set.
   **/
  public void Var039() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC driver hangs");
      return;
    }
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          message = new StringBuffer();
          ResultSet rs = statement2_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
          JDRSTest.position0(rs, "LOB_FULL");
          Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
              "C_DBCLOB");
          boolean trim = false;
          assertCondition(compare(v, JDRSTest.DBCLOB_FULLX, trim),
              message.toString());
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Get from a BLOB, when the BLOB data is returned in the result
   * set.
   **/
  public void Var040() {
    if (checkJdbc40()) {

      if (checkLobSupport()) {
        try {
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
          JDRSTest.position0(rs, "LOB_FULL");
          Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_BLOB");

          // A String comparison does not really make sense here,
          // so we just check to see if it is not null.
          // assertCondition (compare (v, new String (JDRSTest.BLOB_FULL)));
          assertCondition(v != null);
        } catch (Exception e) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            String expected = "Invalid data conversion";
            assertCondition(e.toString().indexOf(expected) > 0,
                "Expected " + expected + " got " + e.toString());
          } else {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
  }

  /**
   * getSQLXML() - Get from a BLOB, when the BLOB locator is returned in the
   * result set.
   **/
  public void Var041() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC driver hangs");
      return;
    }
    if (checkJdbc40()) {

      if (checkLobSupport()) {
        try {
          ResultSet rs = statement2_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
          JDRSTest.position0(rs, "LOB_FULL");
          Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_BLOB");

          // A String comparison does not really make sense here,
          // so we just check to see if it is not null.
          // assertCondition (compare (v, new String (JDRSTest.BLOB_FULL)));
          assertCondition(v != null);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Get from a DATE.
   **/
  public void Var042() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "DATE_1998");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_DATE");
        failed("Didn't throw SQLException " + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Get from a TIME.
   **/
  public void Var043() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "DATE_1998");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_TIME");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Get from a TIMESTAMP.
   **/
  public void Var044() {
    if (checkJdbc40()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
        JDRSTest.position0(rs, "DATE_1998");
        Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
            "C_TIMESTAMP");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Get from a DATALINK.
   * 
   * SQL400 - From the native driver's perspective, a datalink column is treated
   * the same was that it is for standard SQL. When you make an unqualified
   * select of a datalink, the SQL statement is implicitly cast to look at the
   * full URL value for the datalink. This is, in essence, a String and can be
   * retrieved with getSQLXML as other Strings can.
   **/
  public void Var045() {
    if (checkJdbc40()) {
      if (checkDatalinkSupport()) {
        try {
          Statement s = connection_.createStatement(
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
          ResultSet rs = s
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
          JDRSTest.position0(rs, "LOB_FULL");
          Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
              "C_DATALINK");
          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getSQLXML() - Get from an empty DATALINK.
   * 
   * SQL400 - From the native driver's perspective, a datalink column is treated
   * the same was that it is for standard SQL. When you make an unqualified
   * select of a datalink, the SQL statement is implicitly cast to look at the
   * full URL value for the datalink. This is, in essence, a String and can be
   * retrieved with getSQLXML as other Strings can.
   **/
  public void Var046() {
    if (checkJdbc40()) {
      if (checkDatalinkSupport()) {
        try {
          Statement s = connection_.createStatement(
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
          ResultSet rs = s
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
          JDRSTest.position0(rs, "LOB_EMPTY");
          Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
              "C_DATALINK");
          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getSQLXML() - Get from a DISTINCT.
   **/
  public void Var047() {
    message = new StringBuffer(
        " -- Updated 12/15/2011 -- emtpy SQLXML throws exception");
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
          JDRSTest.position0(rs, "LOB_EMPTY");
          Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
              "C_DISTINCT");

          boolean check = compare(v, "", false);
          assertCondition(check, "got '" + v + "' sb '' " + message.toString());

        } catch (Exception e) {

          String expected = "Data type mismatch";
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            expected = "Invalid data conversion";
          }

          assertCondition(e.toString().indexOf(expected) > 0,
              "Expected " + expected + " got " + e.toString());
        }
      }
    }
  }

  /**
   * getSQLXML() - Get from a BIGINT.
   **/
  public void Var048() {
    if (checkJdbc40()) {
      if (checkBigintSupport()) {
        try {
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETX);
          JDRSTest.position0(rs, "NUMBER_POS");
          Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML",
              "C_BIGINT");
          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getSQLXML() - Verify that no DataTruncation is posted when the max field
   * size is set to 0.
   **/
  public void Var049() {
    if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable(); // table only created in 54+
      return;
    }
    if (checkJdbc40()) {
      try {
        message = new StringBuffer(
            " -- Updated 01/09/2012 -- verify not translation");

        Statement s = connection_.createStatement();
        s.setMaxFieldSize(0);
        ResultSet rs = s.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " ORDER BY C_KEY");
        rs.next();
        Object v = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 2);// clob37

        DataTruncation dt = (DataTruncation) rs.getWarnings();
        rs.close();
        s.close();
        boolean trim = false;
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          trim = true;
        }

        assertCondition(
            (compare(v, JDRSTest.SAMPLE_XML1, trim)) && (dt == null));

      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String expected = "Invalid data conversion";
          assertCondition(e.toString().indexOf(expected) > 0,
              "Expected " + expected + " got " + e.toString());
        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Verify that data is truncated with a DataTruncation posted
   * when the max field size is set to a value shorter than the data.
   **/
  public void Var050() {
    if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable(); // table only created in 54+
      return;
    }
    if (checkJdbc40()) {
      try {
        message = new StringBuffer();
        Statement s = connection_.createStatement();
        s.setMaxFieldSize(19);
        ResultSet rs = s.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " ORDER BY C_KEY");
        rs.next();
        Object v = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 2);
        SQLWarning w = rs.getWarnings();
        String expected = JDRSTest.SAMPLE_XML1.substring(0, 19);
        if (isToolboxDriver()) {
          // col 2 is clob. does not apply per javadoc of
          // statement.setMaxFieldSize()
          expected = JDRSTest.SAMPLE_XML1;
        }
        rs.close();
        s.close();
        boolean trim = false;
        if (isToolboxDriver()) {
          assertCondition((compare(v, expected, trim)) && (w == null),
              message.toString() + " Warning = " + w);
        } else {
          assertCondition((compare(v, expected, trim)) && (w != null),
              message.toString() + " Warning = " + w);
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          String expected = "Invalid data conversion";
          assertCondition(e.toString().indexOf(expected) > 0,
              "Expected " + expected + " got " + e.toString());
        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Should work on a UTF-8 column when the column name is valid.
   **/
  public void Var051() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("CCSID 1208 variation not applicable for JCC");
      return;
    }
    if (checkJdbc40()) {
      try {
        message = new StringBuffer();
        String tablename = JDRSTest.COLLECTION + ".JDRSGCLB45";
        initTable(statement_, tablename, " (c1 clob(50000) CCSID 1208)");

        statement_
            .executeUpdate("INSERT INTO " + tablename + " VALUES('<?xml>')");

        ResultSet rs = statement_.executeQuery("SELECT * from " + tablename);
        rs.next();

        Object v = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 1);

        boolean trim = false;
        assertCondition(compare(v, "<?xml>", trim),
            "Clob != expected -- added by native driver 08/03/2005 to detect getSQLXML() problem for UTF8 "
                + message.toString());
      } catch (Exception e) {
        failed(e,
            "Unexpected Exception -- added by native driver 08/03/2005 to detect getSQLXML() problem for UTF8");
      }
    }
  }

  /**
   * getSQLXML() - Get from DFP16:
   **/
  public void Var052() {
    if (checkJdbc40()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP16);
        rs.next();
        Object v = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 1);
        failed("Didn't throw SQLException " + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Get from DFP34:
   **/
  public void Var053() {
    if (checkJdbc40()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP34);
        rs.next();
        Object v = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 1);
        failed("Didn't throw SQLException " + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() -- get from BLOB columns
   */

  /**
   * getSQLXML() - Get from a SQLXML.
   **/
  public void Var054() {
    if (checkJdbc40()) {
      if (checkXmlSupport()) {
        try {
          ResultSet rs = statement_.executeQuery(
              "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " ORDER BY C_KEY");
          rs.next();

          Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_XML");

          boolean trim = false;
          assertCondition(compare(v, JDRSTest.SAMPLE_XML1_OUTXML, trim),
              message.toString());

        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Get from a SQLXML that was inserted with UTF-16LE bytes. -
   * output should be normal utf8 since the xml column is utf8
   **/
  public void Var055() {
    if (checkJdbc40()) {
      if (checkXmlSupport()) {
        try {
          // insert utf16le
          PreparedStatement ps55 = connection_
              .prepareStatement("INSERT INTO " + JDRSTest.RSTEST_GETXML
                  + " VALUES(?,    ?, ?, ?,   ?,   ?, ?, ?, ?) ");
          ps55.setInt(1, 9999);
          ps55.setString(2, null);
          ps55.setString(3, null);
          ps55.setString(4, null);
          ps55.setString(5, null);
          ps55.setBytes(6, null);
          ps55.setBytes(7, null);
          ps55.setBytes(8, null);
          if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
            ps55.setBytes(9, JDRSTest.SAMPLE_XML1_UTF16LE.getBytes("utf-16le")); // inserting
                                                                                 // bytes
                                                                                 // into
                                                                                 // utf8
                                                                                 // xml
                                                                                 // column
          }
          ps55.executeUpdate();

          ResultSet rs = statement_.executeQuery(
              "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " ORDER BY C_KEY");
          JDRSTest.position0(rs, "9999");
          Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_XML");

          statement_.execute(
              "delete from " + JDRSTest.RSTEST_GETXML + " where c_key = 9999");
          ps55.close();
          boolean trim = false;
          assertCondition(compare(v, JDRSTest.SAMPLE_XML1_OUTXML, trim),
              message.toString());

        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getSQLXML() - Get from a SQLXML that was inserted with UTF-16LE bytes. -
   * output should be normal utf8 since the xml column is utf8 - getbytes should
   * have decl with utf-8 encoding instead of utf16le
   **/
  public void Var056() {
    if (checkJdbc40()) {
      if (checkXmlSupport()) {
        try {
          // insert utf16le
          PreparedStatement ps55 = connection_
              .prepareStatement("INSERT INTO " + JDRSTest.RSTEST_GETXML
                  + " VALUES(?,    ?, ?, ?,   ?,   ?, ?, ?, ?) ");
          ps55.setInt(1, 9999);
          ps55.setString(2, null);
          ps55.setString(3, null);
          ps55.setString(4, null);
          ps55.setString(5, null);
          ps55.setBytes(6, null);
          ps55.setBytes(7, null);
          ps55.setBytes(8, null);
          if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
            ps55.setBytes(9, JDRSTest.SAMPLE_XML1_UTF16LE.getBytes("utf-16le")); // inserting
                                                                                 // bytes
                                                                                 // into
                                                                                 // utf8
                                                                                 // xml
                                                                                 // column
          }
          ps55.executeUpdate();

          ResultSet rs = statement_.executeQuery(
              "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " ORDER BY C_KEY");
          JDRSTest.position0(rs, "9999");
          Object v = JDReflectionUtil.callMethod_OS(rs, "getSQLXML", "C_XML");

          InputStream is = (InputStream) JDReflectionUtil.callMethod_O(v,
              "getBinaryStream");

          statement_.execute(
              "delete from " + JDRSTest.RSTEST_GETXML + " where c_key = 9999");
          ps55.close();
          assertCondition(
              compare(is, JDRSTest.SAMPLE_XML1_OUTXML_WITHDECL_UTF8, message),
              message.toString());

        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * @xml30 getSQLXML() - Get from a XML from a Non-JDBC40 Toolbox driver. This
   *        is for any Toolbox apps that do not use JDBC40. They will not break
   *        when querying against a tab that contains XML columns.
   **/
  public void Var057() {
    if ((!isToolboxDriver()) && isJdbc40()) {
      notApplicable("Toolbox only - jdbc30 get XML as ClobLocator workaround");
      return;
    }
    if (checkXmlSupport()) {
      try {
        String url = baseURL_
            // String url = "jdbc:as400://" + systemObject_.getSystemName()
            + ";hold statements=true" ;
        Connection connection57 = testDriver_
            .getConnection(url + ";lob threshold=30000",systemObject_.getUserId(),encryptedPassword_);
        connection57.setAutoCommit(false); // @E1A
        Statement statement57 = connection57.createStatement(
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        // statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_GETXML
        // + " (C_KEY) VALUES ('DUMMY_TBROW')");
        ResultSet rs = statement57.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GETXML + " ORDER BY C_KEY");
        JDRSTest.position0(rs, "1");
        String v = rs.getString("C_XML"); // xml column, but jdbc30 is hacked to
                                          // handle as cloblocator

        statement57.close();
        connection57.close();

        boolean pass = v.equals(JDRSTest.SAMPLE_XML1_OUTXML);
        if (!pass)
          message = message.append(
              "SQLXML is \n********\n" + v + "\n********\nExpected \n********\n"
                  + JDRSTest.SAMPLE_XML1_OUTXML + "\n********\n");

        assertCondition(pass, message.toString());

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * getSQLXML() - Get from a TIMESTAMP(12).
   **/
  public void Var058() {
    if (checkTimestamp12Support()) {

      testGet(statement_,
          "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
          "getSQLXML", "EXCEPTION:Data type mismatch.", " -- added 11/19/2012");

    }
  }

  /**
   * getSQLXML() - Get from a TIMESTAMP(0).
   **/
  public void Var059() {
    if (checkTimestamp12Support()) {
      testGet(statement_,
          "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
          "getSQLXML", "EXCEPTION:Data type mismatch.", " -- added 11/19/2012");

    }
  }

  /**
   * getSQLXML() - Get from a BOOLEAN.
   **/
  public void Var060() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_TRUE");
        Object v = JDReflectionUtil.callMethod_O(rs, "getSQLXML", "C_BOOLEAN");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Get from a BOOLEAN.
   **/
  public void Var061() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_FALSE");
        Object v = JDReflectionUtil.callMethod_O(rs, "getSQLXML", "C_BOOLEAN");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getSQLXML() - Get from a BOOLEAN.
   **/
  public void Var062() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_NULL");
        Object v = JDReflectionUtil.callMethod_O(rs, "getSQLXML", "C_BOOLEAN");
        assertCondition(v == null);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

}
