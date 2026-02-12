///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetRowId.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;


import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDReflectionUtil;
import test.JDTestcase;

/**
 * Testcase JDRSGetRowId. This tests the following method of the JDBC ResultSet
 * class:
 * 
 * <ul>
 * <li>getRowId()
 * </ul>
 **/
public class JDRSGetRowId extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetRowId";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }

  // Private data.
  private Statement statement_;
  private Statement statement0_;
  private ResultSet rs_;
  private String addedA1 = " -- added by native 02/07/2007";

  /**
   * Constructor.
   **/
  public JDRSGetRowId(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDRSGetRowId", namesAndVars, runMode, fileOutputStream,
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
    String url = baseURL_
        // String url = "jdbc:as400://" + systemObject_.getSystemName()
         ;
    connection_ = testDriver_.getConnection (url + ";lob threshold=30000",systemObject_.getUserId(), encryptedPassword_);
    setAutoCommit(connection_, false); // @E1A

    statement0_ = connection_.createStatement();

    if (isJdbc20()) {
      statement_ = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
          ResultSet.CONCUR_UPDATABLE);
      statement_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_GET
          + " (C_KEY) VALUES ('DUMMYROW_GETROWID')");
      rs_ = statement_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET + " FOR UPDATE");
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
   * getRowId() - Should throw exception when the result set is closed.
   **/
  public void Var001() {
    if (!checkJdbc40())
      return;

    try {
      Statement s = connection_.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next();
      rs.close();
      JDReflectionUtil.callMethod_O(rs, "getRowId", 1);
      // rs.getRowId (1);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getRowId() - Should throw exception when cursor is not pointing to a row.
   **/
  public void Var002() {
    if (!checkJdbc40())
      return;

    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      Object ri = JDReflectionUtil.callMethod_O(rs, "getRowId", 1);

      // RowId ri = rs.getRowId (1);
      failed("Didn't throw SQLException but got " + ri);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }

  }

  /**
   * getRowId() - Should throw an exception when the column is an invalid index.
   **/
  public void Var003() {
    if (!checkJdbc40())
      return;

    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next();
      Object ri = JDReflectionUtil.callMethod_O(rs, "getRowId", 100);
      // RowId ri = rs.getRowId (100);
      failed("Didn't throw SQLException " + ri);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getRowId() - Should throw an exception when the column is 0.
   **/
  public void Var004() {
    if (!checkJdbc40())
      return;

    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next();
      Object ri = JDReflectionUtil.callMethod_O(rs, "getRowId", 0);
      // RowId ri = rs.getRowId (0);
      failed("Didn't throw SQLException " + ri);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getRowId() - Should throw an exception when the column is -1.
   **/
  public void Var005() {
    if (!checkJdbc40())
      return;

    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next();
      Object ri = JDReflectionUtil.callMethod_O(rs, "getRowId", -1);
      // RowId ri = rs.getRowId (-1);
      failed("Didn't throw SQLException " + ri);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getRowId() - Should work when the column name is valid.
   **/
  public void Var006() {
    if (!checkJdbc40())
      return;

    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next(); // any row

      // rs.getRowId(1);
      Object ri = JDReflectionUtil.callMethod_OS(rs, "getRowId", "C_ROWID");
      String rsReturnString = (String) rs.getString("C_ROWID");
      // rs.getBytes(1);
      byte[] rsReturnBytes = (byte[]) JDReflectionUtil.callMethod_OS(rs,
          "getBytes", "C_ROWID");

      byte[] riReturnBytes = (byte[]) JDReflectionUtil.callMethod_O(ri,
          "getBytes");

      String riReturnString = (String) JDReflectionUtil.callMethod_O(ri,
          "toString");

      boolean passed1 = rsReturnString
          .equalsIgnoreCase(bytesToString(rsReturnBytes));
      boolean passed2 = riReturnString
          .equalsIgnoreCase(bytesToString(riReturnBytes));
      boolean passed3 = riReturnString.equalsIgnoreCase(rsReturnString);

      assertCondition(passed1 && passed2 && passed3,
          " passed1 = " + passed1 + " passed2 = " + passed2 + " passed3 = "
              + passed3 + " bytesToString(rsReturnBytes) = "
              + bytesToString(rsReturnBytes)
              + " bytesToString(riReturnBytes) = "
              + bytesToString(riReturnBytes) + " rsReturnString = "
              + rsReturnString + " riReturnString = " + riReturnString);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getRowId() - Should get SQLExceptions on all mismatching getters.
   **/
  public void Var007() {
    if (!checkJdbc40())
      return;

    String failString = "";

    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next(); // any row

      failString = failString
          + callInvalidGetter(rs, "getBigDecimal", "C_ROWID");
      failString = failString + callInvalidGetter(rs, "getBoolean", "C_ROWID");
      failString = failString + callInvalidGetter(rs, "getByte", "C_ROWID");
      failString = failString + callInvalidGetter(rs, "getDate", "C_ROWID");
      failString = failString + callInvalidGetter(rs, "getDouble", "C_ROWID");
      failString = failString + callInvalidGetter(rs, "getFloat", "C_ROWID");
      failString = failString + callInvalidGetter(rs, "getInt", "C_ROWID");
      failString = failString + callInvalidGetter(rs, "getLong", "C_ROWID");
      failString = failString + callInvalidGetter(rs, "getShort", "C_ROWID");
      failString = failString + callInvalidGetter(rs, "getTime", "C_ROWID");
      failString = failString
          + callInvalidGetter(rs, "getTimestamp", "C_ROWID");
      failString = failString + callInvalidGetter(rs, "getSQLXML", "C_ROWID");
      // failString = failString + callInvalidGetter(rs, "getDouble",
      // "C_DOUBLE");

      if (!failString.equals(""))
        failed(failString);
      else
        assertCondition(true);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getRowId() - Should not get SQLExceptions on all valid getters.
   **/
  public void Var008() {
    if (!checkJdbc40())
      return;

    String failString = "";

    try {
      ResultSet rs = statement0_
          .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
      rs.next(); // any row

      Object o;
      o = JDReflectionUtil.callMethod_OS(rs, "getAsciiStream", "C_ROWID");
      o = JDReflectionUtil.callMethod_OS(rs, "getBinaryStream", "C_ROWID");
      o = JDReflectionUtil.callMethod_OS(rs, "getBlob", "C_ROWID");
      o = JDReflectionUtil.callMethod_OS(rs, "getBytes", "C_ROWID");
      o = JDReflectionUtil.callMethod_OS(rs, "getCharacterStream", "C_ROWID");
      o = JDReflectionUtil.callMethod_OS(rs, "getClob", "C_ROWID");
      o = JDReflectionUtil.callMethod_OS(rs, "getObject", "C_ROWID");
      o = JDReflectionUtil.callMethod_OS(rs, "getString", "C_ROWID");
      o = JDReflectionUtil.callMethod_OS(rs, "getUnicodeStream", "C_ROWID");
      o = JDReflectionUtil.callMethod_OS(rs, "getNCharacterStream", "C_ROWID");
      o = JDReflectionUtil.callMethod_OS(rs, "getNString", "C_ROWID");
      o = JDReflectionUtil.callMethod_OS(rs, "getRowId", "C_ROWID");

      assertCondition(true, failString + o);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  private String callInvalidGetter(ResultSet rs, String methodName,
      String colName) {
    try {
      JDReflectionUtil.callMethod_OS(rs, methodName, colName);
      // rs.getX(1);
      // failed ("Didn't throw SQLException");
      return "Method " + methodName + "() did not throw exception.  ";
    } catch (SQLException e) {
      return ""; // got sqlexeption as expected
    } catch (Exception ex) {
      return "Error, got non-SQLException in method " + methodName + ".  ";
    }
  }

  /**
   * getRowId() - Get from DFP16:
   **/
  public void Var009() {
    if (!checkJdbc40())
      return;
    if (checkDecFloatSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDFP16);
        rs.next();
        Object v = JDReflectionUtil.callMethod_O(rs, "getRowId", 1);

        failed("Didn't throw exception for getting RowId from DECFLOAT but got "
            + v);
      } catch (Exception e) {
        assertCondition(e instanceof java.sql.SQLException,
            "Exception not SQL Exception");
      }
    }
  }

  /**
   * getRowId() - Get from DFP34:
   **/
  public void Var010() {
    if (!checkJdbc40())
      return;
    if (checkDecFloatSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDFP34);
        rs.next();
        Object v = JDReflectionUtil.callMethod_O(rs, "getRowId", 1);

        failed("Didn't throw exception for getting RowId from DECFLOAT but got "
            + v);
      } catch (Exception e) {
        assertCondition(e instanceof java.sql.SQLException);
      }
    }
  }

  /**
   * getRowId() - Should match hex value
   **/
  public void Var011() {
    if (!checkJdbc40())
      return;

    try {
      ResultSet rs = statement0_.executeQuery(
          "SELECT C_ROWID, HEX(C_ROWID) FROM " + JDRSTest.RSTEST_GET);
      rs.next(); // any row

      // rs.getRowId(1);
      Object ri = JDReflectionUtil.callMethod_OS(rs, "getRowId", "C_ROWID");
      byte[] riReturnBytes = (byte[]) JDReflectionUtil.callMethod_O(ri,
          "getBytes");
      String riReturnString = bytesToString(riReturnBytes);
      String rsReturnString = rs.getString(2);

      assertCondition(rsReturnString.equalsIgnoreCase(riReturnString),
          " HEX(C_ROWID) = " + rsReturnString + " C_ROWID=" + riReturnString
              + addedA1);

    } catch (Exception e) {
      failed(e, "Unexpected Exception" + addedA1);
    }
  }

  /**
   * getRowId() - Get from a TIMESTAMP(12).
   **/
  public void Var012() {
    if (checkTimestamp12Support()) {
      testGet(statement0_,
          "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
          "getRowId", "EXCEPTION:Data type mismatch.", " -- added 11/19/2012");

    }
  }

  /**
   * getRowId() - Get from a TIMESTAMP(0).
   **/
  public void Var013() {
    if (checkTimestamp12Support()) {
      testGet(statement0_,
          "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
          "getRowId", "EXCEPTION:Data type mismatch.", " -- added 11/19/2012");

    }
  }

  /**
   * getRowId() - Get from a BOOLEAN.
   **/
  public void Var014() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_TRUE");
        Object v = JDReflectionUtil.callMethod_O(rs, "getRowId", "C_BOOLEAN");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getRowId() - Get from a BOOLEAN.
   **/
  public void Var015() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_FALSE");
        Object v = JDReflectionUtil.callMethod_O(rs, "getRowId", "C_BOOLEAN");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getRowId() - Get from a BOOLEAN.
   **/
  public void Var016() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_NULL");
        Object v = JDReflectionUtil.callMethod_O(rs, "getRowId", "C_BOOLEAN");
        assertCondition(v == null);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

}
