///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetBytes.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDPSSetBytes.java
//
// Classes:      JDPSSetBytes
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DataTruncation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

/**
 * Testcase JDPSSetBytes. This tests the following method of the JDBC
 * PreparedStatement class:
 * 
 * <ul>
 * <li>setBytes()
 * </ul>
 **/
public class JDPSSetBytes extends JDTestcase {

  // Constants.
  private static final String PACKAGE = "JDPSSBYTES";

  // Private data.
  private Connection connection_;
  private Statement statement_;
  private String jvm = "";

  /**
   * Constructor.
   **/
  public JDPSSetBytes(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDPSSetBytes", namesAndVars, runMode, fileOutputStream,
 password);

    jvm = System.getProperty("java.vm.name");

  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      String url = baseURL_;
      connection_ = testDriver_.getConnection(url, userId_, encryptedPassword_);

    } else {

      String url = baseURL_  + ";data truncation=true" + ";errors=full";
      connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
    }
    statement_ = connection_.createStatement();
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    statement_.close();
    connection_.close();
  }

  /**
   * setBytes() - Should throw exception when the prepared statement is closed.
   **/
  public void Var001() {
    try {
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      ps.close();
      byte[] b = new byte[] { (byte) 22, (byte) 98, (byte) -2 };
      ps.setBytes(1, b);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Should throw exception when an invalid index is specified.
   **/
  public void Var002() {
    try {
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
      byte[] b = new byte[] { (byte) 22, (byte) 4, (byte) 4, (byte) 98,
          (byte) -2 };
      ps.setBytes(100, b);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Should throw exception when index is 0.
   **/
  public void Var003() {
    try {
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
      byte[] b = new byte[] { (byte) 4, (byte) 98, (byte) -2 };
      ps.setBytes(0, b);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Should throw exception when index is -1.
   **/
  public void Var004() {
    try {
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
      byte[] b = new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) 22,
          (byte) -2 };
      ps.setBytes(0, b);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Should set to SQL NULL when the value is null.
   **/
  public void Var005() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      ps.setBytes(1, null);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      byte[] check = rs.getBytes(1);
      boolean wn = rs.wasNull();
      rs.close();

      assertCondition((check == null) && (wn == true));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setBytes() - Should work with a valid parameter index greater than 1.
   **/
  public void Var006() {
    StringBuffer sb = new StringBuffer();
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_KEY, C_VARBINARY_20) VALUES (?, ?)");
      ps.setString(1, "Muchas");
      byte[] b = new byte[] { (byte) -22, (byte) 4, (byte) -2 };
      ps.setBytes(2, b);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      byte[] check = rs.getBytes(1);

      rs.close();

      assertCondition(areEqual(b, check, sb), sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setBytes() - Should throw exception when the parameter is not an input
   * parameter.
   **/
  public void Var007() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC permits setting of an output parameter");
      return;
    }
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
      byte[] b = new byte[] { (byte) 98, (byte) 98, (byte) -2 };

      ps.setBytes(2, b);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Verify that a data truncation warning is posted when data is
   * truncated.
   **/
  public void Var008() {
    int length = 0;
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      byte[] b = new byte[] { (byte) -22, (byte) 4, (byte) 9, (byte) -2,
          (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
          (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
          (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
          (byte) 77, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
          (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
          (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
          (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
          (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
          (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
          (byte) 50, (byte) 2, (byte) 22 };

      length = b.length;
      ps.setBytes(1, b);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (DataTruncation dt) {
      assertCondition((dt.getIndex() == 1) && (dt.getParameter() == true)
          && (dt.getRead() == false) && (dt.getDataSize() == length)
          && (dt.getTransferSize() == 20));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  public void testSetFailed(String columnName, byte[] inArray) {
    PreparedStatement ps = null;
    try {
      ps = connection_.prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET
          + " (" + columnName + ") VALUES (?)");
      ps.setBytes(1, inArray);
      ps.executeUpdate();
      ps.close();
      ps = null;
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException e1) {
        }
      }
    }

  }

  /**
   * setBytes() - Set a SMALLINT parameter.
   **/
  public void Var009() {
    byte[] b = new byte[] { (byte) 98 };
    testSetFailed("C_SMALLINT", b);
  }

  /**
   * setBytes() - Set a INTEGER parameter.
   **/
  public void Var010() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 123 };

      ps.setBytes(1, b);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Set a REAL parameter.
   **/
  public void Var011() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_REAL) VALUES (?)");
      byte[] b = new byte[] { (byte) 98, (byte) -2 };

      ps.setBytes(1, b);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Set a FLOAT parameter.
   **/
  public void Var012() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_FLOAT) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) 7, (byte) -2,
          (byte) 45 };

      ps.setBytes(1, b);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Set a DOUBLE parameter.
   **/
  public void Var013() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DOUBLE) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) 45, (byte) 12 };

      ps.setBytes(1, b);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Set a DECIMAL parameter.
   **/
  public void Var014() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DECIMAL_105) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) -2, (byte) 98, (byte) -2,
          (byte) 45, (byte) 12, (byte) -33 };

      ps.setBytes(1, b);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Set a NUMERIC parameter.
   **/
  public void Var015() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_NUMERIC_50) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
          (byte) 12, (byte) -33 };

      ps.setBytes(1, b);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Set a CHAR(1) parameter.
   **/
  public void Var016() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_1) VALUES (?)");
      byte[] b = new byte[] { (byte) 98 };

      ps.setBytes(1, b);
      ps.execute();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Set a CHAR(50) parameter.
   **/
  public void Var017() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_50) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -12, (byte) 45,
          (byte) 12, (byte) -33, (byte) 0 };

      ps.setBytes(1, b);
      ps.execute();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Set a VARCHAR(50) parameter.
   **/
  public void Var018() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_VARCHAR_50) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) -12, (byte) 12, (byte) -33 };

      ps.setBytes(1, b);
      ps.execute();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Set a CLOB parameter.
   **/
  public void Var019() {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CLOB) VALUES (?)");
        byte[] b = new byte[] { (byte) 108, (byte) 0, (byte) -12, (byte) 12,
            (byte) -33 };

        ps.setBytes(1, b);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBytes() - Set a DBCLOB parameter.
   **/
  public void Var020() {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DBCLOB) VALUES (?)");
        byte[] b = new byte[] { (byte) 0, (byte) 66, (byte) 12, (byte) -33 };

        ps.setBytes(1, b);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBytes() - Set a BINARY parameter.
   **/
  public void Var021() {
    StringBuffer sb = new StringBuffer();
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BINARY_20) VALUES (?)");
      byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) -12, (byte) 1,
          (byte) 0, (byte) 12, (byte) -33, (byte) 57, (byte) 9 };

      ps.setBytes(1, b);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      byte[] check = rs.getBytes(1);
      rs.close();

      byte[] check2 = new byte[20];
      System.arraycopy(b, 0, check2, 0, b.length);

      assertCondition(areEqual(check, check2, sb), sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setBytes() - Set a VARBINARY parameter.
   **/
  public void Var022() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 0, (byte) -33,
          (byte) 57, (byte) 9 };

      ps.setBytes(1, b);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      byte[] check = rs.getBytes(1);
      rs.close();
      StringBuffer sb = new StringBuffer();

      assertCondition(areEqual(b, check, sb), sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setBytes() - Set a VARBINARY parameter to an empty array.
   **/
  public void Var023() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      byte[] b = new byte[0];

      ps.setBytes(1, b);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      byte[] check = rs.getBytes(1);
      rs.close();

      assertCondition(check.length == 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setBytes() - Set a BLOB parameter.
   **/
  public void Var024() {
    if (checkLobSupport()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BLOB) VALUES (?)");
        byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 45, (byte) -33,
            (byte) 0 };

        ps.setBytes(1, b);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_BLOB FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        InputStream is2 = rs.getBinaryStream(1);
        byte[] check = new byte[b.length];
        is2.read(check);
        is2.close();

        rs.close();
        StringBuffer sb = new StringBuffer();

        assertCondition(areEqual(b, check, sb), sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setBytes() - Set a DATE parameter.
   **/
  public void Var025() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DATE) VALUES (?)");
      byte[] b = new byte[] { (byte) -12, (byte) 1, (byte) -33, (byte) 0 };

      ps.setBytes(1, b);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Set a TIME parameter.
   **/
  public void Var026() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIME) VALUES (?)");
      byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) -33,
          (byte) 0, (byte) 45, (byte) 5 };

      ps.setBytes(1, b);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Set a TIMESTAMP parameter.
   **/
  public void Var027() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIMESTAMP) VALUES (?)");
      byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 11, (byte) -33,
          (byte) 0, (byte) 5 };

      ps.setBytes(1, b);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBytes() - Set a DATALINK parameter.
   **/
  public void Var028() {
    if (checkDatalinkSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DATALINK) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 1, (byte) 45, (byte) 1,
            (byte) 11, (byte) -33, (byte) 0, (byte) 5, (byte) 100 };

        ps.setBytes(1, b);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBytes() - Set a DISTINCT parameter.
   **/
  public void Var029() {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DISTINCT) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 1, (byte) -33, (byte) 0,
            (byte) 5, (byte) 100 };

        ps.setBytes(1, b);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBytes() - Set a BIGINT parameter.
   **/
  public void Var030() {
    if (checkBigintSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BIGINT) VALUES (?)");
        byte[] b = new byte[] { (byte) -7, (byte) -98, (byte) 2, (byte) 0,
            (byte) -7, (byte) -98, (byte) 2, (byte) 10 };

        ps.setBytes(1, b);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBytes() - Set a VARBINARY parameter with package caching.
   **/
  public void Var031() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC does not support pckage caching");
      return;
    }
    try {
      String insert = "INSERT INTO " + JDPSTest.PSTEST_SET
          + " (C_VARBINARY_20) VALUES (?)";

      if (isToolboxDriver())
        JDSetupPackage.prime(systemObject_, PACKAGE,
            JDPSTest.COLLECTION, insert);
      else
        JDSetupPackage.prime(systemObject_, encryptedPassword_, PACKAGE,
            JDPSTest.COLLECTION, insert, "", getDriver());

      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      Connection c2 = testDriver_.getConnection(baseURL_
          + ";extended dynamic=true;package=" + PACKAGE + ";package library="
          + JDPSTest.COLLECTION + ";package cache=true", userId_, encryptedPassword_);
      PreparedStatement ps = c2.prepareStatement(insert);
      byte[] b = new byte[] { (byte) 11, (byte) -12, (byte) 45, (byte) 11,
          (byte) -33, (byte) 0, (byte) 5, (byte) 100 };

      ps.setBytes(1, b);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      byte[] check = rs.getBytes(1);
      rs.close();
      StringBuffer sb = new StringBuffer();

      assertCondition(areEqual(check, b, sb), sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setBytes() - Set a DECFLOAT16 parameter.
   **/
  public void Var032() {
    if (checkDecFloatSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SETDFP16 + "  VALUES (?)");
        byte[] b = new byte[] { (byte) -7, (byte) -98, (byte) 2, (byte) 0,
            (byte) -7, (byte) -98, (byte) 2, (byte) 10 };

        ps.setBytes(1, b);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBytes() - Set a DECFLOAT34 parameter.
   **/
  public void Var033() {
    if (checkDecFloatSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SETDFP34 + "  VALUES (?)");
        byte[] b = new byte[] { (byte) -7, (byte) -98, (byte) 2, (byte) 0,
            (byte) -7, (byte) -98, (byte) 2, (byte) 10 };

        ps.setBytes(1, b);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBytes() - Set an XML parameter.
   **/
  public void setXML(String tablename, String byteEncoding, String data,
      String expected) {
    String added = " -- added by native driver 08/21/2009";

    if ((byteEncoding.equals("IBM-290") && jvm.indexOf("HotSpot(TM)") >= 0)) {
      notApplicable(
          "byteEncoding(" + byteEncoding + ") not supported on " + jvm);
      return;
    }

    if ((byteEncoding.equals("UCS-2") && jvm.indexOf("HotSpot(TM)") >= 0)) {
      /* Sun does not support UCS-2, change to UTF-16 */
      byteEncoding = "UTF-16";
    }

    if ((byteEncoding.equals("UCS-2") && jvm.indexOf("jdk11") >= 0)) {
      /* Sun does not support UCS-2, change to UTF-16 */
      byteEncoding = "UTF-16";
    }

    if ((byteEncoding.equals("IBM-290") && jvm.indexOf("OpenJDK") >= 0)) {
      notApplicable(
          "byteEncoding(" + byteEncoding + ") not supported on " + jvm);
      return;
    }

    if ((byteEncoding.equals("UCS-2") && jvm.indexOf("OpenJDK") >= 0)) {
      /* Sun does not support UCS-2, change to UTF-16 */
      byteEncoding = "UTF-16";
    }

    if ((byteEncoding.equals("UCS-2") && jvm.indexOf("Eclipse OpenJ9") >= 0)) {
      /* Sun does not support UCS-2, change to UTF-16BE */
      byteEncoding = "UTF-16BE";
    }

    if (checkJdbc40()) {
      if (checkXmlSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + tablename);

          PreparedStatement ps = connection_
              .prepareStatement("INSERT INTO " + tablename + "  VALUES (?)");
          byte[] b = data.getBytes(byteEncoding);

          ps.setBytes(1, b);

          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT * FROM " + tablename);
          rs.next();
          String check = rs.getString(1);
          rs.close();

          assertCondition(check.equals(expected),
              "check = " + JDTestUtilities.getMixedString(check) + " And SB "
                  + JDTestUtilities.getMixedString(expected) + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception" + added);
        }
      }
    }
  }

  /**
   * setBytes() - Set an XML parameter using invalid data.
   **/
  public void setInvalidXML(String tablename, String byteEncoding, String data,
      String expectedException) {
    String added = " -- added by native driver 08/21/2009";
    if (checkJdbc40()) {
      if (checkXmlSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + tablename);

          PreparedStatement ps = connection_
              .prepareStatement("INSERT INTO " + tablename + "  VALUES (?)");
          byte[] b = data.getBytes(byteEncoding);

          ps.setBytes(1, b);

          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT * FROM " + tablename);
          rs.next();
          String check = rs.getString(1);
          rs.close();
          failed("Didn't throw exception but got "
              + JDTestUtilities.getMixedString(check) + added);

        } catch (Exception e) {

          String message = e.toString();
          if (message.indexOf(expectedException) >= 0) {
            assertCondition(true);
          } else {
            failed(e,
                "Unexpected Exception.  Expected " + expectedException + added);
          }
        }
      }
    }
  }

  /* Insert various types against a UTF-8 table */
  public void Var034() {
    notApplicable();
  }

  public void Var035() {
    notApplicable();
  }

  public void Var036() {
    notApplicable();
  }

  public void Var037() {
    notApplicable();
  }

  public void Var038() {
    setXML(JDPSTest.PSTEST_SETXML, "ISO8859_1", "<Test>Var038</Test>",
        "<Test>Var038</Test>");
  }

  public void Var039() {
    setXML(JDPSTest.PSTEST_SETXML, "ISO8859_1",
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>Var039\u00fb</Test>",
        "<Test>Var039\u00fb</Test>");
  }

  public void Var040() {
    setXML(JDPSTest.PSTEST_SETXML, "UTF-8",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>Var040\u00fb</Test>",
        "<Test>Var040\u00fb</Test>");
  }

  public void Var041() {
    setXML(JDPSTest.PSTEST_SETXML, "IBM-037",
        "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>Var041\u00fb</Test>",
        "<Test>Var041\u00fb</Test>");
  }

  public void Var042() {
    setXML(JDPSTest.PSTEST_SETXML, "IBM-937",
        "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>Var042\u672b</Test>",
        "<Test>Var042\u672b</Test>");
  }

  public void Var043() {
    setXML(JDPSTest.PSTEST_SETXML, "IBM-290",
        "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>Var043\uff7a</Test>",
        "<Test>Var043\uff7a</Test>");
  }

  public void Var044() {
    setXML(JDPSTest.PSTEST_SETXML, "UCS-2",
        "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var044\u00fb\uff7a</Test>",
        "<Test>Var044\u00fb\uff7a</Test>");
  }

  public void Var045() {
    setXML(JDPSTest.PSTEST_SETXML, "UTF-16BE",
        "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>Var045\u00fb\uff7a\ud800\udf30</Test>",
        "<Test>Var045\u00fb\uff7a\ud800\udf30</Test>");
  }

  public void Var046() {
    setXML(JDPSTest.PSTEST_SETXML, "UTF-16LE",
        "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>Var046\u00fb\uff7a\ud800\udf30</Test>",
        "<Test>Var046\u00fb\uff7a\ud800\udf30</Test>");
  }

  /* Insert various types against a 13488 table */

  public void Var047() {
    setXML(JDPSTest.PSTEST_SETXML13488, "ISO8859_1", "<Test>Var047</Test>",
        "<Test>Var047</Test>");
  }

  public void Var048() {
    setXML(JDPSTest.PSTEST_SETXML13488, "ISO8859_1",
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>Var048\u00fb</Test>",
        "<Test>Var048\u00fb</Test>");
  }

  public void Var049() {
    setXML(JDPSTest.PSTEST_SETXML13488, "UTF-8",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>Var049\u00fb</Test>",
        "<Test>Var049\u00fb</Test>");
  }

  public void Var050() {
    setXML(JDPSTest.PSTEST_SETXML13488, "IBM-037",
        "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>Var050\u00fb</Test>",
        "<Test>Var050\u00fb</Test>");
  }

  public void Var051() {
    setXML(JDPSTest.PSTEST_SETXML13488, "IBM-937",
        "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>Var051\u672b</Test>",
        "<Test>Var051\u672b</Test>");
  }

  public void Var052() {
    setXML(JDPSTest.PSTEST_SETXML13488, "IBM-290",
        "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>Var052\uff7a</Test>",
        "<Test>Var052\uff7a</Test>");
  }

  public void Var053() {
    setXML(JDPSTest.PSTEST_SETXML13488, "UCS-2",
        "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var053\u00fb\uff7a</Test>",
        "<Test>Var053\u00fb\uff7a</Test>");
  }

  public void Var054() {
    setXML(JDPSTest.PSTEST_SETXML13488, "UTF-16BE",
        "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>Var054\u00fb\uff7a</Test>",
        "<Test>Var054\u00fb\uff7a</Test>");
  }

  public void Var055() {
    setXML(JDPSTest.PSTEST_SETXML13488, "UTF-16LE",
        "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>Var055\u00fb\uff7a</Test>",
        "<Test>Var055\u00fb\uff7a</Test>");
  }

  /* Insert various types against a UTF-16 table */

  public void Var056() {
    setXML(JDPSTest.PSTEST_SETXML1200, "ISO8859_1", "<Test>Var056</Test>",
        "<Test>Var056</Test>");
  }

  public void Var057() {
    setXML(JDPSTest.PSTEST_SETXML1200, "ISO8859_1",
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>Var039\u00fb</Test>",
        "<Test>Var039\u00fb</Test>");
  }

  public void Var058() {
    setXML(JDPSTest.PSTEST_SETXML1200, "UTF-8",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>Var040\u00fb</Test>",
        "<Test>Var040\u00fb</Test>");
  }

  public void Var059() {
    setXML(JDPSTest.PSTEST_SETXML1200, "IBM-037",
        "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>Var041\u00fb</Test>",
        "<Test>Var041\u00fb</Test>");
  }

  public void Var060() {
    setXML(JDPSTest.PSTEST_SETXML1200, "IBM-937",
        "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>Var042\u672b</Test>",
        "<Test>Var042\u672b</Test>");
  }

  public void Var061() {
    setXML(JDPSTest.PSTEST_SETXML1200, "IBM-290",
        "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>Var043\uff7a</Test>",
        "<Test>Var043\uff7a</Test>");
  }

  public void Var062() {
    setXML(JDPSTest.PSTEST_SETXML1200, "UCS-2",
        "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var044\u00fb\uff7a</Test>",
        "<Test>Var044\u00fb\uff7a</Test>");
  }

  public void Var063() {
    setXML(JDPSTest.PSTEST_SETXML1200, "UTF-16BE",
        "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>Var045\u00fb\uff7a\ud800\udf30</Test>",
        "<Test>Var045\u00fb\uff7a\ud800\udf30</Test>");
  }

  public void Var064() {
    setXML(JDPSTest.PSTEST_SETXML1200, "UTF-16LE",
        "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>Var046\u00fb\uff7a\ud800\udf30</Test>",
        "<Test>Var046\u00fb\uff7a\ud800\udf30</Test>");
  }

  /* Insert various types against a EBCDIC-37 table */

  public void Var065() {
    setXML(JDPSTest.PSTEST_SETXML37, "ISO8859_1", "<Test>Var065</Test>",
        "<Test>Var065</Test>");
  }

  public void Var066() {
    setXML(JDPSTest.PSTEST_SETXML37, "ISO8859_1",
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>Var039\u00fb</Test>",
        "<Test>Var039\u00fb</Test>");
  }

  public void Var067() {
    setXML(JDPSTest.PSTEST_SETXML37, "UTF-8",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>Var040\u00fb</Test>",
        "<Test>Var040\u00fb</Test>");
  }

  public void Var068() {
    setXML(JDPSTest.PSTEST_SETXML37, "IBM-037",
        "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>Var041\u00fb</Test>",
        "<Test>Var041\u00fb</Test>");
  }

  public void Var069() {
    setXML(JDPSTest.PSTEST_SETXML37, "IBM-937",
        "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>Var042</Test>",
        "<Test>Var042</Test>");
  }

  public void Var070() {
    setXML(JDPSTest.PSTEST_SETXML37, "IBM-290",
        "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>Var043</Test>",
        "<Test>Var043</Test>");
  }

  public void Var071() {
    setXML(JDPSTest.PSTEST_SETXML37, "UCS-2",
        "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var044\u00fb</Test>",
        "<Test>Var044\u00fb</Test>");
  }

  public void Var072() {
    setXML(JDPSTest.PSTEST_SETXML37, "UTF-16BE",
        "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>Var045\u00fb</Test>",
        "<Test>Var045\u00fb</Test>");
  }

  public void Var073() {
    setXML(JDPSTest.PSTEST_SETXML37, "UTF-16LE",
        "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>Var046\u00fb</Test>",
        "<Test>Var046\u00fb</Test>");
  }

  /* Insert various types against a EBCDIC-937 table */

  public void Var074() {
    setXML(JDPSTest.PSTEST_SETXML937, "ISO8859_1", "<Test>Var038</Test>",
        "<Test>Var038</Test>");
  }

  public void Var075() {
    setXML(JDPSTest.PSTEST_SETXML937, "ISO8859_1",
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>Var039</Test>",
        "<Test>Var039</Test>");
  }

  public void Var076() {
    setXML(JDPSTest.PSTEST_SETXML937, "UTF-8",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>Var040\u672b</Test>",
        "<Test>Var040\u672b</Test>");
  }

  public void Var077() {
    setXML(JDPSTest.PSTEST_SETXML937, "IBM-037",
        "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>Var041</Test>",
        "<Test>Var041</Test>");
  }

  public void Var078() {
    setXML(JDPSTest.PSTEST_SETXML937, "IBM-937",
        "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>Var042\u672b</Test>",
        "<Test>Var042\u672b</Test>");
  }

  public void Var079() {
    setXML(JDPSTest.PSTEST_SETXML937, "IBM-290",
        "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>Var043</Test>",
        "<Test>Var043</Test>");
  }

  public void Var080() {
    setXML(JDPSTest.PSTEST_SETXML937, "UCS-2",
        "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var044\u672b</Test>",
        "<Test>Var044\u672b</Test>");
  }

  public void Var081() {
    setXML(JDPSTest.PSTEST_SETXML937, "UTF-16BE",
        "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>Var045\u672b</Test>",
        "<Test>Var045\u672b</Test>");
  }

  public void Var082() {
    setXML(JDPSTest.PSTEST_SETXML937, "UTF-16LE",
        "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>Var046\u672b</Test>",
        "<Test>Var046\u672b</Test>");
  }

  /* Insert various types against a EBCDIC 290 table */

  public void Var083() {
    setXML(JDPSTest.PSTEST_SETXML290, "ISO8859_1", "<Test>Var083</Test>",
        "<Test>Var083</Test>");
  }

  public void Var084() {
    setXML(JDPSTest.PSTEST_SETXML290, "ISO8859_1",
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>Var089</Test>",
        "<Test>Var089</Test>");
  }

  public void Var085() {
    setXML(JDPSTest.PSTEST_SETXML290, "UTF-8",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>Var040\uff7a</Test>",
        "<Test>Var040\uff7a</Test>");
  }

  public void Var086() {
    setXML(JDPSTest.PSTEST_SETXML290, "IBM-037",
        "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>Var041</Test>",
        "<Test>Var041</Test>");
  }

  public void Var087() {
    setXML(JDPSTest.PSTEST_SETXML290, "IBM-937",
        "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>Var042</Test>",
        "<Test>Var042</Test>");
  }

  public void Var088() {
    setXML(JDPSTest.PSTEST_SETXML290, "IBM-290",
        "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>Var043\uff7a</Test>",
        "<Test>Var043\uff7a</Test>");
  }

  public void Var089() {
    setXML(JDPSTest.PSTEST_SETXML290, "UCS-2",
        "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>Var044\uff7a</Test>",
        "<Test>Var044\uff7a</Test>");
  }

  public void Var090() {
    setXML(JDPSTest.PSTEST_SETXML290, "UTF-16BE",
        "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>Var095\uff7a</Test>",
        "<Test>Var095\uff7a</Test>");
  }

  public void Var091() {
    setXML(JDPSTest.PSTEST_SETXML290, "UTF-16LE",
        "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>Var096\uff7a</Test>",
        "<Test>Var096\uff7a</Test>");
  }

  /**
   * setBytes() - Set a BOOLEAN parameter.
   **/
  public void Var092() {
      if (checkBooleanSupport()) { 
	  byte[] b = new byte[] { (byte) 98 };
	  testSetFailed("C_BOOLEAN", b);
      }
  }

}
