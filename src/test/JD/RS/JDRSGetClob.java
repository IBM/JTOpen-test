///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetClob.java
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

import java.sql.Connection;
import java.sql.DataTruncation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;

/**
 * Testcase JDRSGetClob. This tests the following method of the JDBC ResultSet
 * class:
 * 
 * <ul>
 * <li>getClob()
 * </ul>
 **/
public class JDRSGetClob extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetClob";
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
  protected String methodName = "getClob";
  StringBuffer sb = new StringBuffer();

  /**
   * Constructor.
   **/
  public JDRSGetClob(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDRSGetClob", namesAndVars, runMode, fileOutputStream,
 password);
  }

  /**
   * Constructor.
   **/
  public JDRSGetClob(AS400 systemObject, String testcaseName,
      Hashtable namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password) {
    super(systemObject, testcaseName, namesAndVars, runMode, fileOutputStream,
 password);
  }

  boolean isLevel() {
    return isJdbc20();
  }

  boolean checkLevel() {
    return checkJdbc20();
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

    if (isLevel()) {
      // SQL400 - driver neutral...
      String url = baseURL_
          // String url = "jdbc:as400://" + systemObject_.getSystemName()
           
          + ";time format=jis;date format=iso";
      connection_ = testDriver_.getConnection (url + ";lob threshold=30000",systemObject_.getUserId(), encryptedPassword_);

      setAutoCommit(connection_, false); // @E1A

      statement_ = connection_.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      statement_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_GET
          + " (C_KEY) VALUES ('DUMMY_ROW')");

      statement1_ = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
          ResultSet.CONCUR_UPDATABLE);

      // Force LOB locators.
      connection2_ = testDriver_.getConnection (url + ";lob threshold=0",systemObject_.getUserId(), encryptedPassword_);
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
      connection_.commit(); // @E1A
      connection_.close();
    }
  }

  protected void cleanupConnections() throws Exception {
    if (isLevel()) {
      connection2_.commit(); // @E1A
      connection2_.close();
      connection_.commit(); // @E1A
      connection_.close();
    }
  }

  /**
   * Compares a NClob with a String.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  private boolean compare(Object i, String b) throws SQLException {
    try {
      int length = (int) JDReflectionUtil.callMethod_L(i, "length");
      String s = JDReflectionUtil.callMethod_S(i, "getSubString", 1L, length);
      boolean theSame = s.equals(b);
      if (!theSame) {
        System.out.println("CLOB \"" + s + "\" <> \"" + b + "\"");
      }
      return theSame;
    } catch (Exception e) {
      e.printStackTrace();
      throw new SQLException(e.toString());
    }
  }

  /**
   * Compares a NClob with a String.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  private boolean compare(Object i, String b, StringBuffer sb)
      throws SQLException {
    if (i == null) {
      if (b != null) {
        sb.append("CLOB=null <> \"" + b + "\"");
        return false;
      } else {
        return true;
      }
    } else {
      try {
        int length = (int) JDReflectionUtil.callMethod_L(i, "length");
        String s = JDReflectionUtil.callMethod_S(i, "getSubString", 1L, length);
        boolean theSame = s.equals(b);
        if (!theSame) {
          sb.append("CLOB \"" + s + "\" <> \"" + b + "\"");
        }
        return theSame;
      } catch (Exception e) {
        SQLException sqlex = new SQLException(e.toString());
        sqlex.initCause(e);
        throw sqlex;
      }
    }
  }

  /**
   * getClob() - Should throw exception when the result set is closed.
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
   * getClob() - Should throw exception when cursor is not pointing to a row.
   **/
  public void Var002() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, null);
        Object v = JDReflectionUtil.callMethod_O(rs, methodName, 1);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getClob() - Should throw an exception when the column is an invalid index.
   **/
  public void Var003() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Object v = JDReflectionUtil.callMethod_O(rs, methodName, 100);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getClob() - Should throw an exception when the column is 0.
   **/
  public void Var004() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Object v = JDReflectionUtil.callMethod_O(rs, methodName, 0);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getClob() - Should throw an exception when the column is -1.
   **/
  public void Var005() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Object v = JDReflectionUtil.callMethod_O(rs, methodName, -1);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getClob() - Should work when the column index is valid.
   **/
  public void Var006() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Object v = JDReflectionUtil.callMethod_O(rs, methodName, 14);
        assertCondition(compare(v, "Java Toolbox"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Should throw an exception when the column name is null.
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
          JDReflectionUtil.callMethod_OS(rs, methodName, (String) null);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getClob() - Should throw an exception when the column name is an empty
   * string.
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
   * getClob() - Should throw an exception when the column name is invalid.
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
   * getClob() - Should work when the column name is valid.
   **/
  public void Var010() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        assertCondition(compare(v, "Java Toolbox"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Should work when an update is pending.
   **/
  public void Var011() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement1_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GET + " FOR UPDATE");
        JDRSTest.position(rs, "UPDATE_SANDBOX");
        String test = "This is a test of the Emergency Broadcast Network.";
        rs.updateString("C_VARCHAR_50", test);
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        boolean check = compare(v, test);
        rs.close();
        assertCondition(check);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Should work when an update has been done.
   **/
  public void Var012() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement1_.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_GET + " FOR UPDATE");
        JDRSTest.position(rs, "UPDATE_SANDBOX");
        String test = "The new and improved AS/400 Toolbox for Java.";
        rs.updateString("C_VARCHAR_50", test);
        rs.updateRow();
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        boolean check = compare(v, test);
        rs.close();
        assertCondition(check);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Should work when the current row is the insert row, when an
   * insert is pending.
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
        String test = "Hola mis amigos";
        rs.updateString("C_VARCHAR_50", test);
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        rs.close();
        boolean check = compare(v, test);
        assertCondition(check);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Should work when the current row is the insert row, when an
   * insert has been done.
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
        String test = "Pescados y leche";
        rs.updateString("C_VARCHAR_50", test);
        rs.insertRow();
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        rs.close();
        boolean check = compare(v, test);
        assertCondition(check);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Should throw an exception on a deleted row.
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
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
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
   * getClob() - Should return null when the column is NULL.
   **/
  public void Var016() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_NULL");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        assertCondition(v == null);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Get from a SMALLINT.
   **/
  public void Var017() {
    if (checkLevel()) {
      StringBuffer sb = new StringBuffer();
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "NUMBER_POS");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_SMALLINT");

        String expected = "198";
        boolean check = compare(v, expected, sb);
        assertCondition(check, sb.toString() + " Updated 11/17/2011");

      } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
      }
    }
  }

  /**
   * getClob() - Get from a INTEGER.
   **/
  public void Var018() {
    if (checkLevel()) {
      StringBuffer sb = new StringBuffer();
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "NUMBER_NEG");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_INTEGER");

        String expected = "-98765";
        boolean check = compare(v, expected, sb);
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
      }
    }
  }

  /**
   * getClob() - Get from a REAL.
   **/
  public void Var019() {
    if (checkLevel()) {
      StringBuffer sb = new StringBuffer();
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "NUMBER_POS");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_REAL");
        String expected = "4.4";
        boolean check = compare(v, expected, sb);
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
      }
    }
  }

  /**
   * getClob() - Get from a FLOAT.
   **/
  public void Var020() {
    if (checkLevel()) {
      StringBuffer sb = new StringBuffer();
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "NUMBER_NEG");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_FLOAT");

        String expected = "-5.55";
        boolean check = compare(v, expected, sb);
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
      }
    }
  }

  /**
   * getClob() - Get from a DOUBLE.
   **/
  public void Var021() {
    if (checkLevel()) {
      StringBuffer sb = new StringBuffer();
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "NUMBER_NEG");

        Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_DOUBLE");

        String expected = "-6.666";
        boolean check = compare(v, expected, sb);
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
      }
    }
  }

  /**
   * getClob() - Get from a DECIMAL.
   **/
  public void Var022() {
    StringBuffer sb = new StringBuffer(
        "getClob from DECIMAL -- updated 12/14/2011");
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "NUMBER_POS");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_DECIMAL_50"); // @F1A

        assertCondition(compare(v, "7", sb), sb); // @F1A

      } catch (Exception e) {

        failed(e, "Unexpected Exception"); // @F1A
      }
    }
  }

  /**
   * getClob() - Get from a NUMERIC.
   **/
  public void Var023() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "NUMBER_NEG");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_NUMERIC_105"); // @F1A
        assertCondition(compare(v, "-10.10105")); // @F1A

      } catch (Exception e) {
        failed(e, "Unexpected Exception"); // @F1A
      }
    }
  }

  /**
   * getClob() - Get from an empty CHAR.
   **/
  public void Var024() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_EMPTY");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_CHAR_50");

        assertCondition(
            compare(v, "                                                  "));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Get from a full CHAR.
   **/
  public void Var025() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_CHAR_50");

        assertCondition(
            compare(v, "Toolbox for Java                                  "));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Get from an empty VARCHAR.
   **/
  public void Var026() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_EMPTY");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        long length = JDReflectionUtil.callMethod_L(v, "length");
        assertCondition(length == 0);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Get from a full VARCHAR.
   **/
  public void Var027() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        assertCondition(compare(v, "Java Toolbox"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Get from a BINARY.
   **/
  public void Var028() {
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
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_BINARY_20");

        assertCondition(compare(v, expected));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Get from a VARBINARY.
   **/
  public void Var029() {
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
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARBINARY_20");

        assertCondition(compare(v, expected));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Get from a CLOB, when the CLOB data is returned in the result
   * set.
   **/
  public void Var030() {
    if (checkLevel()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs, "LOB_FULL");
          Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_CLOB");
          assertCondition(compare(v, JDRSTest.CLOB_FULL));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getClob() - Get from a CLOB, when the CLOB locator is returned in the
   * result set.
   **/
  public void Var031() {
    if (checkLevel()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement2_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs, "LOB_FULL");
          Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_CLOB");
          assertCondition(compare(v, JDRSTest.CLOB_FULL));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getClob() - Get from a DBCLOB, when the DBCLOB data is returned in the
   * result set.
   **/
  public void Var032() {
    if (checkLevel()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs, "LOB_FULL");
          Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_DBCLOB");
          assertCondition(compare(v, JDRSTest.DBCLOB_FULL));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getClob() - Get from a DBCLOB, when the DBCLOB locator is returned in the
   * result set.
   **/
  public void Var033() {
    if (checkLevel()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement2_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs, "LOB_FULL");
          Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_DBCLOB");
          assertCondition(compare(v, JDRSTest.DBCLOB_FULL));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getClob() - Get from a BLOB, when the BLOB data is returned in the result
   * set.
   **/
  public void Var034() {
    if (checkLevel()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs, "LOB_FULL");
          Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_BLOB");

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
   * getClob() - Get from a BLOB, when the BLOB locator is returned in the
   * result set.
   **/
  public void Var035() {
    if (checkLevel()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement2_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs, "LOB_FULL");
          Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_BLOB");

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
   * getClob() - Get from a DATE.
   **/
  public void Var036() {
    if (checkLevel()) {
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "DATE_1998");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_DATE");
        assertCondition(compare(v, "1998-04-08"),
            " -- changed time format 11/14/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- changed time format 11/14/2011");
      }
    }
  }

  /**
   * getClob() - Get from a TIME.
   **/
  public void Var037() {
    if (checkLevel()) {
      StringBuffer sb = new StringBuffer();
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "DATE_1998");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_TIME");
        String expected = "08:14:03";
        boolean check = compare(v, expected, sb);
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
      }
    }
  }

  /**
   * getClob() - Get from a TIMESTAMP.
   **/
  public void Var038() {
    if (checkLevel()) {
      StringBuffer sb = new StringBuffer();
      try {
        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "DATE_1998");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_TIMESTAMP");
        String expected = "1998-11-18 03:13:42.987654";
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          expected = "1998-11-18-03.13.42.987654";
        }
        boolean check = compare(v, expected, sb);
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
      }
    }
  }

  /**
   * getClob() - Get from a DATALINK.
   * 
   * SQL400 - From the native driver's perspective, a datalink column is treated
   * the same was that it is for standard SQL. When you make an unqualified
   * select of a datalink, the SQL statement is implicitly cast to look at the
   * full URL value for the datalink. This is, in essence, a String and can be
   * retrieved with getClob as other Strings can.
   **/
  public void Var039() {
    StringBuffer sb = new StringBuffer(
        "getClob from datalink updated 12/14/2011 ");
    if (checkLevel()) {
      if (checkDatalinkSupport()) {
        try {
          Statement s = connection_.createStatement(
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
          ResultSet rs = s
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
          JDRSTest.position(rs, "LOB_FULL");
          Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
              "C_DATALINK");
          // Note the case... AS/400 DB does its own thing here...
          assertCondition(
              compare(v, JDRSTest.LOB_FULL_DATALINK_UPPER_DOMAIN, sb), sb);

        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getClob() - Get from an empty DATALINK.
   * 
   * SQL400 - From the native driver's perspective, a datalink column is treated
   * the same was that it is for standard SQL. When you make an unqualified
   * select of a datalink, the SQL statement is implicitly cast to look at the
   * full URL value for the datalink. This is, in essence, a String and can be
   * retrieved with getClob as other Strings can.
   **/
  public void Var040() {
    StringBuffer sb = new StringBuffer(
        "getClob from datalink updated 12/14/2011 ");

    if (checkLevel()) {
      if (checkDatalinkSupport()) {
        try {
          Statement s = connection_.createStatement(
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
          ResultSet rs = s
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
          JDRSTest.position(rs, "LOB_EMPTY");
          Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
              "C_DATALINK");
          int length = (int) JDReflectionUtil.callMethod_L(v, "length");
          assertCondition(length == 0,
              "Expected length of zero got " + length + " " + sb.toString());

        } catch (Exception e) {
          failed(e, "Unexpected Exception " + sb.toString());
        }
      }
    }
  }

  /**
   * getClob() - Get from a DISTINCT.
   **/
  public void Var041() {
    if (checkLevel()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs, "LOB_EMPTY");
          Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
              "C_DISTINCT");
          assertCondition(compare(v, "         "));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getClob() - Get from a BIGINT.
   **/
  public void Var042() {
    if (checkLevel()) {
      if (checkBigintSupport()) {
        try {
          ResultSet rs = statement_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position(rs, "NUMBER_POS");
          Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_BIGINT");
          assertCondition(compare(v, "12374321")); // @K1A
        } catch (Exception e) {
          failed(e, "Unexpected Exception"); // @K1A
        }
      }
    }
  }

  /**
   * getClob() - Verify that no DataTruncation is posted when the max field size
   * is set to 0.
   **/
  public void Var043() {
    if (checkLevel()) {
      try {
        Statement s = connection_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        s.setMaxFieldSize(0);
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName,
            "C_VARCHAR_50");
        DataTruncation dt = (DataTruncation) rs.getWarnings();
        rs.close();
        s.close();
        assertCondition((compare(v, "Java Toolbox")) && (dt == null));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Verify that data is truncated without a DataTruncation posted
   * when the max field size is set to a value shorter than the byte array.
   **/
  public void Var044() {
    if (checkLevel()) {
      try {
        Statement s = connection_.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        s.setMaxFieldSize(19);
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position(rs, "CHAR_FULL");
        int columnIndex = rs.findColumn("C_CHAR_50");
        Object v = JDReflectionUtil.callMethod_O(rs, methodName, columnIndex);
        SQLWarning w = rs.getWarnings();
        rs.close();
        s.close();
        assertCondition((compare(v, "Toolbox for Java   ")) && (w == null));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getClob() - Should work on a UTF-8 column when the column name is valid.
   **/
  public void Var045() {
    if (checkLevel()) {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        try {
          String tablename = JDRSTest.COLLECTION + ".JDRSGCLB45";
          try {
            statement_.executeUpdate("DELETE FROM " + tablename);
          } catch (Exception e) {
            statement_.executeUpdate(
                "CREATE TABLE " + tablename + " (c1 clob(50000) CCSID 1208)");
          }

          statement_.executeUpdate(
              "INSERT INTO " + tablename + " VALUES('Java Toolbox')");

          ResultSet rs = statement_.executeQuery("SELECT * from " + tablename);
          rs.next();

          Object v = JDReflectionUtil.callMethod_O(rs, methodName, 1);

          assertCondition(compare(v, "Java Toolbox"),
              "Blob != expected -- added by native driver 08/03/2005 to detect getClob() problem for UTF8 ");
        } catch (Exception e) {
          failed(e,
              "Unexpected Exception -- added by native driver 08/03/2005 to detect getClob() problem for UTF8");
        }
      } else {
        notApplicable("V5R3 UTF-8 Testcase");
      }
    }
  }

  /**
   * getClob() - Get from DFP16:
   **/
  public void Var046() {
    if (checkLevel() && checkDecFloatSupport()) {
      StringBuffer sb = new StringBuffer();
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP16);
        rs.next();
        Object v = JDReflectionUtil.callMethod_O(rs, methodName, 1);
        String expected = "1.1";
        boolean check = compare(v, expected, sb);
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
      }
    }
  }

  /**
   * getClob() - Get from DFP34:
   **/
  public void Var047() {
    if (checkLevel() && checkDecFloatSupport()) {
      StringBuffer sb = new StringBuffer();
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP34);
        rs.next();
        Object v = JDReflectionUtil.callMethod_O(rs, methodName, 1);
        String expected = "1.1";
        boolean check = compare(v, expected, sb);
        assertCondition(check, sb.toString() + " Updated 11/17/2011");
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- updated 11/17/2011 ");
      }
    }
  }

  /**
   * test getClob for different XML ccsid
   */

  public void testGetClob(int ccsid, String input, String expectedOutput) {
    if (checkLevel() && checkXmlSupport()) {
      String sql = "";
      StringBuffer sb = new StringBuffer(); 
      try {
        Statement s = connection_.createStatement();
        String tableName = JDRSTest.COLLECTION + ".JDRSXML" + ccsid;
	sb.append("Creating table "+tableName + " (C1 XML CCSID " + ccsid + ")\n");

        initTable(s, tableName, " (C1 XML CCSID " + ccsid + ")");

        sql = "insert into " + tableName + " VALUES(?)";
	sb.append("Preparing "+sql+"\n"); 
        PreparedStatement ps = connection_.prepareStatement(sql);
	sb.append("Setting input to "+input+"\n"); 
        ps.setString(1, input);
        ps.executeUpdate();

        sql = "select * from " + tableName;
	sb.append("Running: "+sql+"\n"); 
        ResultSet rs = s.executeQuery(sql);

        rs.next();
	sb.append("Calling method "+methodName+"\n");

        Object v = JDReflectionUtil.callMethod_O(rs, methodName, 1);

        int l = (int) JDReflectionUtil.callMethod_L(v, "length");
        String output = JDReflectionUtil.callMethod_S(v, "getSubString", 1, l);

	boolean passed = expectedOutput.equals(output);
	if (passed) { 
	    cleanupTable(s, tableName);
	} else {
	    sb.append("Did not clean up table "+tableName+"\n"); 
	} 
        s.close();
        ps.close();

        assertCondition(passed,
            "output='" + output + "' sb '" + expectedOutput + "' for CCSID "
                + ccsid + " -- added by native driver 01/21/2010 "+sb.toString());

      } catch (Exception e) {
        failed(e,
            "Unexpected Exception -- added by native driver 01/21/2010 testing getClob for CCSID "
                + ccsid + " SQL = " + sql);
      }

    }
  }

  public void Var048() {
    testGetClob(37, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var049() {
    testGetClob(256, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var050() {
    testGetClob(273, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var051() {
    testGetClob(277, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var052() {
    testGetClob(278, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var053() {
    testGetClob(280, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var054() {
    testGetClob(284, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var055() {
    testGetClob(285, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var056() {
    testGetClob(290, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var057() {
    testGetClob(297, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var058() {
    testGetClob(420, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var059() {
    testGetClob(423, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var060() {
    testGetClob(424, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var061() {
    testGetClob(425, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var062() {
    testGetClob(500, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var063() {
    testGetClob(833, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var064() {
    testGetClob(836, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var065() {
    testGetClob(838, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var066() {
    testGetClob(870, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var067() {
    testGetClob(871, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var068() {
    testGetClob(875, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var069() {
    testGetClob(880, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var070() {
    testGetClob(905, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var071() {
    testGetClob(918, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var072() {
    notApplicable("CCSID 924 not supported -- see issue 43200");
    /* testGetClob(924 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var073() {
    testGetClob(930, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var074() {
    testGetClob(933, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var075() {
    testGetClob(935, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var076() {
    testGetClob(937, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var077() {
    testGetClob(939, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var078() {
    testGetClob(1025, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var079() {
    testGetClob(1026, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var080() {
    testGetClob(1027, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var081() {
    testGetClob(1097, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var082() {
    testGetClob(1112, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var083() {
    testGetClob(1122, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var084() {
    testGetClob(1123, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var085() {
    notApplicable("CCSID 1130 not supported -- see issue 43200");
    /* testGetClob(1130 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var086() {
    notApplicable("CCSID not supported -- see issue 43200");
    /* testGetClob(1132 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var087() {
    notApplicable("CCSID not supported -- see issue 43200");
    /* testGetClob(1137 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var088() {
    testGetClob(1140, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var089() {
    testGetClob(1141, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var090() {
    testGetClob(1142, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var091() {
    testGetClob(1143, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var092() {
    testGetClob(1144, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var093() {
    testGetClob(1145, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var094() {
    testGetClob(1146, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var095() {
    testGetClob(1147, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var096() {
    testGetClob(1148, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var097() {
    testGetClob(1149, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var098() {
    notApplicable("CCSID not supported -- see issue 43200");
    /* testGetClob(1153 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var099() {
    notApplicable("CCSID not supported -- see issue 43200");
    /* testGetClob(1154 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var100() {
    notApplicable("CCSID not supported -- see issue 43200");
    /* testGetClob(1155 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var101() {
    notApplicable("CCSID not supported -- see issue 43200");
    /* testGetClob(1156 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var102() {
    notApplicable("CCSID not supported -- see issue 43200");
    /* testGetClob(1157 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var103() {
    notApplicable("CCSID not supported -- see issue 43200");
    /* testGetClob(1158 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var104() {
    notApplicable("CCSID not supported -- see issue 43200");
    /* testGetClob(1160 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var105() {
    notApplicable("CCSID not supported -- see issue 43200");
    /* testGetClob(1164 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var106() {
    testGetClob(1200, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var107() {
    testGetClob(1208, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var108() {
    testGetClob(1364, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var109() {
    testGetClob(1388, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var110() {
    testGetClob(1399, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var111() {
    notApplicable("CCSID not supported -- see issue 43200");
    /* testGetClob(4971 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var112() {
    testGetClob(5026, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var113() {
    testGetClob(5035, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var114() {
    notApplicable("CCSID not supported -- see issue 43200");
    /* testGetClob(5123 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var115() {
    testGetClob(8612, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var116() {
    notApplicable("CCSID not supported -- see issue 43200");
    /* testGetClob(9030 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var117() {
    notApplicable(
        "CCSID 12708 -- arabic type 8"); /*
                                          * testGetClob(12708 ,
                                          * "<t>testing</t>", "<t>testing</t>");
                                          */
  }

  public void Var118() {
    notApplicable("CCSID not supported -- see issue 43200");
    /* testGetClob(13121 , "<t>testing</t>", "<t>testing</t>"); */
  }

  public void Var119() {
    testGetClob(13124, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var120() {
    testGetClob(28709, "<t>testing</t>", "<t>testing</t>");
  }

  public void Var121() {
    notApplicable("CCSID 62211"); /*
                                   * testGetClob(62211 , "<t>testing</t>",
                                   * "<t>testing</t>");
                                   */
  }

  public void Var122() {
    notApplicable("CCSID 62224"); /*
                                   * testGetClob(62224 , "<t>testing</t>",
                                   * "<t>testing</t>");
                                   */
  }

  public void Var123() {
    notApplicable("CCSID 62235"); /*
                                   * testGetClob(62235 , "<t>testing</t>",
                                   * "<t>testing</t>");
                                   */
  }

  public void Var124() {
    notApplicable("CCSID 62245"); /*
                                   * testGetClob(62245 , "<t>testing</t>",
                                   * "<t>testing</t>");
                                   */
  }

  public void Var125() {
    notApplicable("CCSID 62251"); /*
                                   * testGetClob(62251 , "<t>testing</t>",
                                   * "<t>testing</t>");
                                   */
  }

  /**
   * getClob() - Get from a TIMESTAMP(12).
   **/
  public void Var126() {
    if (checkTimestamp12Support()) {
      String expected = "1998-11-18 03:13:42.987654000000";
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        expected = "1998-11-18-03.13.42.987654000000";
      }
      testGet(statement_,
          "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
          "getClob", expected, " -- added 11/19/2012");

    }
  }

  /**
   * getClob() - Get from a TIMESTAMP(0).
   **/
  public void Var127() {
    if (checkTimestamp12Support()) {
      String expected = "1998-11-18 03:13:42";
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        expected = "1998-11-18-03.13.42";
      }
      testGet(statement_,
          "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
          "getClob", expected, " -- added 11/19/2012");

    }
  }

  /**
   * getClob() - Get from a BOOLEAN true .
   * JDDBC spec says this is not supported. 
   **/
  public void Var128() {
    if (checkBooleanSupport()) {
      sb.setLength(0);

      try {
        ResultSet rs = statement1_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_TRUE");
        Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_BOOLEAN");

	failed ("Didn't throw SQLException"+v);
      }
      catch (Exception e) {
	  assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
    }

  }

  /**
   * getClob() - Get from a BOOLEAN false .
   **/
  public void Var129() {
    if (checkBooleanSupport()) {
      sb.setLength(0);

      try {
        ResultSet rs = statement1_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_FALSE");

        Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_BOOLEAN");

	failed ("Didn't throw SQLException"+v);
      }
      catch (Exception e) {
	  assertExceptionIsInstanceOf (e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getClob() - Get from a BOOLEAN null .
   **/
  public void Var130() {
    if (checkBooleanSupport()) {
      sb.setLength(0);

      try {
        ResultSet rs = statement1_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_NULL");

        Object v = JDReflectionUtil.callMethod_OS(rs, methodName, "C_BOOLEAN");

        if (v != null) {
          sb.append("Got " + v + " sb null");
        }

        assertCondition(v == null, sb); // @F1A
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

}
