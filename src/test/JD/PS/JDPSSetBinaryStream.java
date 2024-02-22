///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetBinaryStream.java
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
// File Name:    JDPSSetBinaryStream.java
//
// Classes:      JDPSSetBinaryStream
//
////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;
import test.JD.JDTestUtilities;
import test.JD.JDWeirdInputStream;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Testcase JDPSSetBinaryStream. This tests the following method of the JDBC
 * PreparedStatement class:
 * 
 * <ul>
 * <li>setBinaryStream()
 * </ul>
 **/
public class JDPSSetBinaryStream extends JDTestcase {

  // Constants.
  private static final String PACKAGE = "JDPSSBS";

  // Private data.
  private Connection connection_;
  private Connection connectionNoDT_;
  private Statement statement_;
  String testUrl = null;

  /**
   * Constructor.
   **/
  public JDPSSetBinaryStream(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDPSSetBinaryStream", namesAndVars, runMode,
        fileOutputStream, password);
  }

  public JDPSSetBinaryStream(AS400 systemObject, String testname, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, testname, namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    String url = baseURL_  + ";data truncation=true" + ";errors=full";
    connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
    testUrl = url;
    statement_ = connection_.createStatement();

    url = baseURL_ + ";data truncation=false" + ";errors=full";
    connectionNoDT_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
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
    connectionNoDT_.close();
  }

  /**
   * setBinaryStream() - Should throw exception when the prepared statement is
   * closed.
   **/
  public void Var001() {
    try {
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      ps.close();
      InputStream is = new ByteArrayInputStream(
          new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2 });
      ps.setBinaryStream(1, is, 4);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Should throw exception when an invalid index is
   * specified.
   **/
  public void Var002() {
    try {
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
      InputStream is = new ByteArrayInputStream(
          new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2 });
      ps.setBinaryStream(100, is, 4);
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Should throw exception when index is 0.
   **/
  public void Var003() {
    try {
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
      InputStream is = new ByteArrayInputStream(
          new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2 });
      ps.setBinaryStream(0, is, 4);
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Should throw exception when index is -1.
   **/
  public void Var004() {
    try {
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
      InputStream is = new ByteArrayInputStream(
          new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) -2 });
      ps.setBinaryStream(-1, is, 4);
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Should set to SQL NULL when the value is null.
   **/
  public void Var005() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      ps.setBinaryStream(1, null, 0);
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
   * setBinaryStream() - Should work with a valid parameter index greater than
   * 1.
   **/
  public void Var006() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_KEY, C_VARBINARY_20) VALUES (?, ?)");
      ps.setString(1, "Muchas");
      byte[] b = new byte[] { (byte) -22, (byte) 4, (byte) 98, (byte) -2 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(2, is, 4);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      byte[] check = rs.getBytes(1);

      rs.close();

      assertCondition(areEqual(b, check));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setBinaryStream() - Should throw exception when the length is not valid.
   **/
  public void Var007() {
    try {
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      byte[] b = new byte[] { (byte) -22, (byte) 98, (byte) -2 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, -1);
      ps.executeUpdate();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Should throw exception when the parameter is not an
   * input parameter.
   **/
  public void Var008() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
      byte[] b = new byte[] { (byte) 98, (byte) -2 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(2, is, 2);
      ps.close();
      ps.executeUpdate();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Verify that a data truncation warning is posted when
   * data is truncated.
   **/
  public void Var009() {
    int length = 0;
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      byte[] b = new byte[] { (byte) -22, (byte) 4, (byte) 98, (byte) -2,
          (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
          (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
          (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
          (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
          (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
          (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
          (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
          (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
          (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
          (byte) 50, (byte) 2, (byte) 22 };
      InputStream is = new ByteArrayInputStream(b);
      length = b.length;
      ps.setBinaryStream(1, is, length);
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
    try {
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (" + columnName + ") VALUES (?)");
      InputStream is = new ByteArrayInputStream(inArray);
      ps.setBinaryStream(1, is, inArray.length);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }

  }

  /**
   * setBinaryStream() - Set a SMALLINT parameter.
   **/
  public void Var010() {
    byte[] b = new byte[] { (byte) 98, (byte) 123 };
    testSetFailed("C_SMALLINT", b);

  }

  /**
   * setBinaryStream() - Set a INTEGER parameter.
   **/
  public void Var011() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 3);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a REAL parameter.
   **/
  public void Var012() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_REAL) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 3);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a FLOAT parameter.
   **/
  public void Var013() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_FLOAT) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 4);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a DOUBLE parameter.
   **/
  public void Var014() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DOUBLE) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
          (byte) 12 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 5);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a DECIMAL parameter.
   **/
  public void Var015() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DECIMAL_105) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
          (byte) 12, (byte) -33 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 6);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a NUMERIC parameter.
   **/
  public void Var016() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_NUMERIC_50) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
          (byte) 12, (byte) -33, (byte) 0 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 7);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a CHAR(1) parameter.
   **/
  public void Var017() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_1) VALUES (?)");
      byte[] b = new byte[] { (byte) 98 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 1);
      ps.executeUpdate();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a CHAR(50) parameter.
   **/
  public void Var018() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_50) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -12, (byte) 45,
          (byte) 12, (byte) -33 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 6);
      ps.executeUpdate();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a VARCHAR(50) parameter.
   **/
  public void Var019() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_VARCHAR_50) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 108, (byte) -12, (byte) 12,
          (byte) -33 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 5);
      ps.executeUpdate();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a CLOB parameter.
   **/
  public void Var020() {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CLOB) VALUES (?)");
        byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 12, (byte) -33 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 4);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a DBCLOB parameter.
   **/
  public void Var021() {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DBCLOB) VALUES (?)");
        byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 66, (byte) 12,
            (byte) -33 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 5);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a BINARY parameter.
   **/
  public void Var022() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BINARY_20) VALUES (?)");
      byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 1, (byte) 0,
          (byte) 12, (byte) -33, (byte) 57, (byte) 9 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 8);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      byte[] check = rs.getBytes(1);
      rs.close();

      byte[] check2 = new byte[20];
      System.arraycopy(b, 0, check2, 0, b.length);

      assertCondition(areEqual(check, check2));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setBinaryStream() - Set a VARBINARY parameter.
   **/
  public void Var023() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 1, (byte) 0,
          (byte) -33, (byte) 57, (byte) 9 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 7);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      byte[] check = rs.getBytes(1);
      rs.close();

      assertCondition(areEqual(b, check));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setBinaryStream() - Set a VARBINARY parameter, with the length less than
   * the full stream.
   **/
  public void Var024() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -12, (byte) 45,
          (byte) -33 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 3);
      /*
       * @E1 if(isToolboxDriver()) {
       */
      succeeded();
      /*
       * @E1 } else { failed ("Didn't throw SQLException"); }
       */
    } catch (Exception e) {
      if (isToolboxDriver()) {
        failed(e, "Unexpected Exception");
      } else {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a VARBINARY parameter, with the length greater than
   * the full stream.
   **/
  public void Var025() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 28, (byte) -12, (byte) 45,
          (byte) -33 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 10);
      ps.executeUpdate();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a VARBINARY parameter, with the length set to 1
   * character.
   **/
  public void Var026() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      byte[] b = new byte[] { (byte) -18, (byte) -12, (byte) 25, (byte) -33 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 1);

      /*
       * @E1 if(isToolboxDriver()) {
       */
      succeeded();
      /*
       * @E1 } else { failed ("Didn't throw SQLException"); }
       */
    } catch (Exception e) {
      if (isToolboxDriver()) {
        failed(e, "Unexpected Exception");
      } else {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a VARBINARY parameter, with the length set to 0.
   **/
  public void Var027() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) -33 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 0);
      /*
       * @E1 if(isToolboxDriver()) {
       */
      succeeded();
      /*
       * @E1 } else { failed ("Didn't throw SQLException"); }
       */
    } catch (Exception e) {
      if (isToolboxDriver()) {
        failed(e, "Unexpected Exception");
      } else {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a VARBINARY parameter to the empty string.
   **/
  public void Var028() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      byte[] b = new byte[0];
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 0);
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
   * setBinaryStream() - Set a VARBINARY parameter to a bad input stream.
   **/
  public void Var029() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");

      class BadInputStream extends InputStream {
        public BadInputStream() {
          super();
        }

        public int available() throws IOException {
          throw new IOException();
        };

        public int read() throws IOException {
          throw new IOException();
        };

        public int read(byte[] buffer) throws IOException {
          throw new IOException();
        };
      }

      InputStream r = new BadInputStream();
      ps.setBinaryStream(1, r, 2);
      ps.executeUpdate();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a BLOB parameter.
   **/
  public void Var030() {
    if (checkLobSupport()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BLOB) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) -33, (byte) 0 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 4);
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

        assertCondition(areEqual(b, check));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setBinaryStream() - Set a DATE parameter.
   **/
  public void Var031() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DATE) VALUES (?)");
      byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) -33,
          (byte) 0 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 5);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a TIME parameter.
   **/
  public void Var032() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIME) VALUES (?)");
      byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) -33,
          (byte) 0, (byte) 5 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 6);
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a TIMESTAMP parameter.
   **/
  public void Var033() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIMESTAMP) VALUES (?)");
      byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) 11,
          (byte) -33, (byte) 0, (byte) 5 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 7);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a DATALINK parameter.
   **/
  public void Var034() {
    if (checkDatalinkSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DATALINK) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) 11,
            (byte) -33, (byte) 0, (byte) 5, (byte) 100 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 8);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a DISTINCT parameter.
   **/
  public void Var035() {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DISTINCT) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 1, (byte) 11, (byte) -33,
            (byte) 0, (byte) 5, (byte) 100 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 7);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a BIGINT parameter.
   **/
  public void Var036() {
    if (checkBigintSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BIGINT) VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 3);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a VARBINARY parameter with package caching.
   **/
  public void Var037() {
    try {
      String insert = "INSERT INTO " + JDPSTest.PSTEST_SET
          + " (C_VARBINARY_20) VALUES (?)";

      if (isToolboxDriver())
        JDSetupPackage.prime(systemObject_,  PACKAGE,
            JDPSTest.COLLECTION, insert);
      else
        JDSetupPackage.prime(systemObject_,  encryptedPassword_, PACKAGE,
            JDPSTest.COLLECTION, insert, "", getDriver());

      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      Connection c2 = testDriver_.getConnection(baseURL_
          + ";extended dynamic=true;package=" + PACKAGE + ";package library="
          + JDPSTest.COLLECTION + ";package cache=true", userId_, encryptedPassword_);
      // Fixed 10/11/2006 to use c2 instead of connection_
      PreparedStatement ps = c2.prepareStatement(insert);
      byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 11, (byte) -33,
          (byte) 0, (byte) 5, (byte) 100 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 7);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      byte[] check = rs.getBytes(1);
      rs.close();

      assertCondition(areEqual(check, b));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * D1A SQL400 - testcase added. setBinaryStream() - Verify that no data
   * truncation warning is posted when data is truncated but the data truncation
   * flag is turned off.
   **/
  public void Var038() {
    int length = 0;
    if (checkNative()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connectionNoDT_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
        byte[] b = new byte[] { (byte) -22, (byte) 4, (byte) 98, (byte) -2,
            (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
            (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
            (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
            (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
            (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
            (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
            (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
            (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
            (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
            (byte) 50, (byte) 2, (byte) 22 };
        InputStream is = new ByteArrayInputStream(b);
        length = b.length;
        ps.setBinaryStream(1, is, length);
        ps.executeUpdate();
        ps.close();
        assertCondition(true);
      } catch (DataTruncation dt) {
        failed(dt, "Unexpected Data Truncation Exception");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setBinaryStream() - Set a SMALLINT parameter. streamLength>lengthSpecified
   * case
   **/
  public void Var039() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_SMALLINT) VALUES (?)");
      byte[] b = new byte[] { (byte) 98, (byte) 123 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 1);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a INTEGER parameter. streamLength>lengthSpecified
   * case
   **/
  public void Var040() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 2);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a REAL parameter. streamLength>lengthSpecified case
   **/
  public void Var041() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_REAL) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 2);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a FLOAT parameter. streamLength>lengthSpecified
   * case
   **/
  public void Var042() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_FLOAT) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 3);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a DOUBLE parameter. streamLength>lengthSpecified
   * case
   **/
  public void Var043() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DOUBLE) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
          (byte) 12 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 4);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a DECIMAL parameter. streamLength>lengthSpecified
   * case
   **/
  public void Var044() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DECIMAL_105) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
          (byte) 12, (byte) -33 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 2);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a NUMERIC parameter. streamLength>lengthSpecified
   * case
   **/
  public void Var045() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_NUMERIC_50) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
          (byte) 12, (byte) -33, (byte) 0 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 1);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a CHAR(1) parameter. streamLength>lengthSpecified
   * case
   **/
  public void Var046() // @E1: added this Var
  {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_1) VALUES (?)");
      byte[] b = new byte[] { (byte) 98, (byte) 98 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 1);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a CHAR(50) parameter. streamLength>lengthSpecified
   * case
   **/
  public void Var047() // @E1: added this Var
  {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_50) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -12, (byte) 45,
          (byte) 12, (byte) -33 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 3);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a VARCHAR(50) parameter.
   * streamLength>lengthSpecified case
   **/
  public void Var048() // @E1: added this Var
  {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_VARCHAR_50) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 108, (byte) -12, (byte) 12,
          (byte) -33 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 1);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a CLOB parameter. streamLength>lengthSpecified case
   **/
  public void Var049() // @E1: added this Var
  {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CLOB) VALUES (?)");
        byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 12, (byte) -33 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 2);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a DBCLOB parameter. streamLength>lengthSpecified
   * case
   **/
  public void Var050() // @E1: added this Var
  {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DBCLOB) VALUES (?)");
        byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 66, (byte) 12,
            (byte) -33 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 1);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a BINARY parameter. streamLength>lengthSpecified
   * case
   **/
  public void Var051() // @E1: added this Var
  {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BINARY_20) VALUES (?)");
      byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 1, (byte) 0,
          (byte) 12, (byte) -33, (byte) 57, (byte) 9 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 3);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      byte[] check = rs.getBytes(1);
      rs.close();

      byte[] check2 = new byte[20];
      System.arraycopy(b, 0, check2, 0, 3); // we are supplying 3 & not b.length
                                            // as we are writing only 3 bytes

      assertCondition(areEqual(check, check2));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setBinaryStream() - Set a BLOB parameter. streamLength>lengthSpecified case
   **/
  public void Var052() // @E1: added this Var
  {
    if (checkLobSupport()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BLOB) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) -33, (byte) 0 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 3);
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

        assertCondition(areEqual(b, check));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setBinaryStream() - Set a DATE parameter. streamLength>lengthSpecified case
   **/
  public void Var053() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DATE) VALUES (?)");
      byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) -33,
          (byte) 0 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 3);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a TIME parameter. streamLength>lengthSpecified case
   **/
  public void Var054() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIME) VALUES (?)");
      byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) -33,
          (byte) 0, (byte) 5 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 4);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a TIMESTAMP parameter. streamLength>lengthSpecified
   * case
   **/
  public void Var055() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIMESTAMP) VALUES (?)");
      byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) 11,
          (byte) -33, (byte) 0, (byte) 5 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 4);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a DATALINK parameter. streamLength>lengthSpecified
   * case
   **/
  public void Var056() // @E1: added this Var
  {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DATALINK) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) 11,
            (byte) -33, (byte) 0, (byte) 5, (byte) 100 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 4);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a DISTINCT parameter. streamLength>lengthSpecified
   * case
   **/
  public void Var057() // @E1: added this Var
  {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DISTINCT) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 1, (byte) 11, (byte) -33,
            (byte) 0, (byte) 5, (byte) 100 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 4);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a BIGINT parameter. streamLength>lengthSpecified
   * case
   **/
  public void Var058() // @E1: added this Var
  {
    if (checkBigintSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BIGINT) VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 2);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a SMALLINT parameter. streamLength<lengthSpecified
   * case
   **/
  public void Var059() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_SMALLINT) VALUES (?)");
      byte[] b = new byte[] { (byte) 98, (byte) 123 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 1);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a INTEGER parameter. streamLength<lengthSpecified
   * case
   **/
  public void Var060() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 2);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a REAL parameter. streamLength<lengthSpecified case
   **/
  public void Var061() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_REAL) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 2);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a FLOAT parameter. streamLength<lengthSpecified
   * case
   **/
  public void Var062() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_FLOAT) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 3);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a DOUBLE parameter. streamLength<lengthSpecified
   * case
   **/
  public void Var063() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DOUBLE) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
          (byte) 12 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 4);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a DECIMAL parameter. streamLength<lengthSpecified
   * case
   **/
  public void Var064() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DECIMAL_105) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
          (byte) 12, (byte) -33 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 2);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a NUMERIC parameter. streamLength<lengthSpecified
   * case
   **/
  public void Var065() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_NUMERIC_50) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
          (byte) 12, (byte) -33, (byte) 0 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 1);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a CHAR(1) parameter. streamLength<lengthSpecified
   * case
   **/
  public void Var066() // @E1: added this Var
  {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_1) VALUES (?)");
      byte[] b = new byte[] { (byte) 98, (byte) 98 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 1);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a CHAR(50) parameter. streamLength<lengthSpecified
   * case
   **/
  public void Var067() // @E1: added this Var
  {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_50) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -12, (byte) 45,
          (byte) 12, (byte) -33 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 3);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a VARCHAR(50) parameter.
   * streamLength<lengthSpecified case
   **/
  public void Var068() // @E1: added this Var
  {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_VARCHAR_50) VALUES (?)");
      byte[] b = new byte[] { (byte) 7, (byte) 108, (byte) -12, (byte) 12,
          (byte) -33 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 1);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a CLOB parameter. streamLength<lengthSpecified case
   **/
  public void Var069() // @E1: added this Var
  {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CLOB) VALUES (?)");
        byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 12, (byte) -33 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 2);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a DBCLOB parameter. streamLength<lengthSpecified
   * case
   **/
  public void Var070() // @E1: added this Var
  {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DBCLOB) VALUES (?)");
        byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 66, (byte) 12,
            (byte) -33 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 1);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a BINARY parameter. streamLength<lengthSpecified
   * case
   **/
  public void Var071() // @E1: added this Var
  {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BINARY_20) VALUES (?)");
      byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 1, (byte) 0,
          (byte) 12, (byte) -33, (byte) 57, (byte) 9 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 3);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      byte[] check = rs.getBytes(1);
      rs.close();

      byte[] check2 = new byte[20];
      System.arraycopy(b, 0, check2, 0, 3); // We are supplying 3 & not b.length
                                            // as we are writing only 3 bytes

      assertCondition(areEqual(check, check2));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setBinaryStream() - Set a BLOB parameter. streamLength<lengthSpecified case
   **/
  public void Var072() // @E1: added this Var
  {
    if (checkLobSupport()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BLOB) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) -33, (byte) 0 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 3);
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

        assertCondition(areEqual(b, check));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setBinaryStream() - Set a DATE parameter. streamLength<lengthSpecified case
   **/
  public void Var073() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DATE) VALUES (?)");
      byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) -33,
          (byte) 0 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 3);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a TIME parameter. streamLength<lengthSpecified case
   **/
  public void Var074() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIME) VALUES (?)");
      byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) -33,
          (byte) 0, (byte) 5 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 4);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a TIMESTAMP parameter. streamLength<lengthSpecified
   * case
   **/
  public void Var075() // @E1: added this Var
  {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIMESTAMP) VALUES (?)");
      byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) 11,
          (byte) -33, (byte) 0, (byte) 5 };
      InputStream is = new ByteArrayInputStream(b);
      ps.setBinaryStream(1, is, 4);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setBinaryStream() - Set a DATALINK parameter. streamLength<lengthSpecified
   * case
   **/
  public void Var076() // @E1: added this Var
  {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DATALINK) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) 11,
            (byte) -33, (byte) 0, (byte) 5, (byte) 100 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 4);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a DISTINCT parameter. streamLength<lengthSpecified
   * case
   **/
  public void Var077() // @E1: added this Var
  {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DISTINCT) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 1, (byte) 11, (byte) -33,
            (byte) 0, (byte) 5, (byte) 100 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 4);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a BIGINT parameter. streamLength<lengthSpecified
   * case
   **/
  public void Var078() // @E1: added this Var
  {
    if (checkBigintSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BIGINT) VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 2);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Should work with a stream that returns 0 sometimes
   * greater than 1.
   **/
  public void Var079() {
    // Per Toolbox implementation...
    // The spec says to throw an exception when the
    // actual length does not match the specified length.
    // I think this is strange since this means the length
    // parameter is essentially not needed. I.e., we always
    // read the exact number of bytes in the stream.
    // if( isToolboxDriver()){
    // notApplicable("Toolbox possible todo later");
    // return;
    // }
    String added = "Added by native driver 10/11/2006 to test input stream that sometimes returns 0 bytes ";

    try {

      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_KEY, C_VARBINARY_20) VALUES (?, ?)");
      ps.setString(1, "Muchas");

      byte[] b = new byte[] { (byte) 32, (byte) 33, (byte) 34, (byte) 35,
          (byte) 36, (byte) 37 };

      InputStream is = new JDWeirdInputStream("0102030");

      ps.setBinaryStream(2, is, 6);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      byte[] check = rs.getBytes(1);

      rs.close();

      assertCondition(areEqual(b, check), "Not equal " + added);
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + added);
    }
  }

  /**
   * setBinaryStream() - Should work with a stream that returns 0 sometimes
   * greater than 1 and uses a portion of the last buffer returned.
   **/
  public void Var080() {
    // Per Toolbox implementation...
    // The spec says to throw an exception when the
    // actual length does not match the specified length.
    // I think this is strange since this means the length
    // parameter is essentially not needed. I.e., we always
    // read the exact number of bytes in the stream.
    // if( isToolboxDriver()){
    // notApplicable("Toolbox possible todo later");
    // return;
    // }
    String added = "Added by native driver 10/11/2006 to test input stream that sometimes returns 0 bytes ";

    try {

      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_KEY, C_VARBINARY_20) VALUES (?, ?)");
      ps.setString(1, "Muchas");

      byte[] b = new byte[] { (byte) 32, (byte) 33, (byte) 34, (byte) 35,
          (byte) 36 };

      InputStream is = new JDWeirdInputStream("0102030");

      ps.setBinaryStream(2, is, 5);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      byte[] check = rs.getBytes(1);

      rs.close();

      assertCondition(areEqual(b, check), "Not equal " + added);
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + added);
    }
  }

  /**
   * setBinaryStream() - Set a DECFLOAT16 parameter.
   **/
  public void Var081() {
    if (checkDecFloatSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SETDFP16 + " VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 3);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a DECFLOAT34 parameter.
   **/
  public void Var082() {
    if (checkDecFloatSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SETDFP34 + " VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2 };
        InputStream is = new ByteArrayInputStream(b);
        ps.setBinaryStream(1, is, 3);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBinaryStream() - Set a BLOB parameter that is too large.. Should get
   * truncation.
   **/
  public void Var083() {
    if (isToolboxDriver()) {
      if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
        notApplicable("Toolbox V5R5M0 or later test");
        return;
      } else {
        notApplicable("Toolbox possible todo later...");
        return;
      }
    }

    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getDriverFixLevel() <= 27438
        && getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable("Native JDBC driver needs fix > 27438 current fix = "
          + getDriverFixLevel());
      return;
    }
    String added = " -- added 5/8/07 by native driver for SE28810 ";
    int length = 0;
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BLOB) VALUES (?)");
      byte[] b = new byte[9000];
      InputStream is = new ByteArrayInputStream(b);
      length = b.length;
      ps.setBinaryStream(1, is, length);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException " + added);
    } catch (DataTruncation dt) {
      assertCondition(
          (dt.getIndex() == 1) && (dt.getParameter() == true)
              && (dt.getRead() == false) && (dt.getDataSize() == length)
              && (dt.getTransferSize() == 200),
          "dt.getIndex()=" + dt.getIndex() + " sb 1 " + "dt.getParameter()="
              + dt.getParameter() + " sb true " + "dt.getRead()=" + dt.getRead()
              + " sb false " + "dt.getDataSize()" + dt.getDataSize() + " sb "
              + length + " " + "dt.getTransferSize()" + dt.getTransferSize()
              + " sb 5242880" + added);
    } catch (Throwable e) {
      failed(e, "Unexpected Exception" + added);
    }
  }

  /**
   * setBinaryStream() - Set a BLOB parameter that is too large.. Should get
   * truncation. - This used to have a nasty FATAL ERROR in native method: EAO
   * exception would occur on data copy
   **/
  public void Var084() {
    if (isToolboxDriver() && getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable("Toolbox V5R5M0 or later test");
      return;
    }
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getDriverFixLevel() <= 27438
        && getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable("Native JDBC driver needs fix > 27438 current fix = "
          + getDriverFixLevel());
      return;
    }
    String added = " -- added 5/8/07 by native driver for SE28810 ";
    String tablename = JDPSTest.COLLECTION + ".JDPSSBS84";

    int length = 0;
    try {
      try {
        statement_.executeUpdate("DROP TABLE " + tablename);
      } catch (Exception e) {

      }
      statement_
          .executeUpdate("CREATE TABLE " + tablename + " (C1 BLOB(5242880))");

      PreparedStatement ps = connection_
          .prepareStatement("INSERT INTO " + tablename + " VALUES (?)");
      byte[] b = new byte[6000000];
      InputStream is = new ByteArrayInputStream(b);
      length = b.length;
      ps.setBinaryStream(1, is, length);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException" + added);
    } catch (DataTruncation dt) {
      assertCondition(
          (dt.getIndex() == 1) && (dt.getParameter() == true)
              && (dt.getRead() == false) && (dt.getDataSize() == length)
              && (dt.getTransferSize() == 5242880),
          "dt.getIndex()=" + dt.getIndex() + " sb 1 " + "dt.getParameter()="
              + dt.getParameter() + " sb true " + "dt.getRead()=" + dt.getRead()
              + " sb false " + "dt.getDataSize()" + dt.getDataSize() + " sb "
              + length + " " + "dt.getTransferSize()" + dt.getTransferSize()
              + " sb 5242880" + added);
    } catch (Throwable e) {
      if (isToolboxDriver()) {
        assertCondition(e.getMessage().indexOf("too long") != -1); // sql0404
                                                                   // too long
                                                                   // expected
                                                                   // from
                                                                   // hostsrv
        return;
      }
      failed(e, "Unexpected Exception" + added);
    }
  }

  /**
   * setBinaryStream() - Set a BLOB parameter that is one byte too large..
   * Should get truncation. - This used to have a nasty FATAL ERROR in native
   * method: EAO exception would occur on data copy
   **/
  public void Var085() {
    if (isToolboxDriver() && getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable("Toolbox V5R5M0 or later test");
      return;
    }
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getDriverFixLevel() <= 27438
        && getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable("Native JDBC driver needs fix > 27438 current fix = "
          + getDriverFixLevel());
      return;
    }
    String added = " -- added 5/8/07 by native driver for SE28810 ";
    String tablename = JDPSTest.COLLECTION + ".JDPSSBS85";

    int length = 0;
    try {
      try {
        statement_.executeUpdate("DROP TABLE " + tablename);
      } catch (Exception e) {

      }
      statement_
          .executeUpdate("CREATE TABLE " + tablename + " (C1 BLOB(5242880))");

      PreparedStatement ps = connection_
          .prepareStatement("INSERT INTO " + tablename + " VALUES (?)");
      byte[] b = new byte[5242881];
      InputStream is = new ByteArrayInputStream(b);
      length = b.length;
      ps.setBinaryStream(1, is, length);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException" + added);
    } catch (DataTruncation dt) {
      assertCondition(
          (dt.getIndex() == 1) && (dt.getParameter() == true)
              && (dt.getRead() == false) && (dt.getDataSize() == length)
              && (dt.getTransferSize() == 5242880),
          "dt.getIndex()=" + dt.getIndex() + " sb 1 " + "dt.getParameter()="
              + dt.getParameter() + " sb true " + "dt.getRead()=" + dt.getRead()
              + " sb false " + "dt.getDataSize()" + dt.getDataSize() + " sb "
              + length + " " + "dt.getTransferSize()" + dt.getTransferSize()
              + " sb 5242880" + added);
    } catch (Throwable e) {
      if (isToolboxDriver()) {
        assertCondition(e.getMessage().indexOf("too long") != -1); // sql0404
                                                                   // too long
                                                                   // expected
                                                                   // from
                                                                   // hostsrv
        return;
      }
      failed(e, "Unexpected Exception" + added);
    }
  }

  /**
   * setBinaryStream() - Set a BLOB parameter that is exactly the right size.
   * Should work.
   **/
  public void Var086() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getDriverFixLevel() <= 27438
        && getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable("Native JDBC driver needs fix > 27438 current fix = "
          + getDriverFixLevel());
      return;
    }
    String added = " -- added 5/8/07 by native driver for SE28810 ";
    String tablename = JDPSTest.COLLECTION + ".JDPSSBS85";

    int length = 0;
    try {
      try {
        statement_.executeUpdate("DROP TABLE " + tablename);
      } catch (Exception e) {

      }
      statement_
          .executeUpdate("CREATE TABLE " + tablename + " (C1 BLOB(5242880))");

      PreparedStatement ps = connection_
          .prepareStatement("INSERT INTO " + tablename + " VALUES (?)");
      byte[] b = new byte[5242880];
      InputStream is = new ByteArrayInputStream(b);
      length = b.length;
      ps.setBinaryStream(1, is, length);
      ps.executeUpdate();
      ps.close();
      assertCondition(true);
    } catch (Throwable e) {
      failed(e, "Unexpected Exception" + added);
    }
  }

  /**
   * setBinaryStream() - Set a BLOB parameter that is too large.. Should warning
   * from a query. Fixed by native via PTF in V5R3
   **/
  public void Var087() {
    if (isToolboxDriver()) {
      if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
        notApplicable("Toolbox V5R5M0 or later test");
        return;
      } else {
        // notApplicable("Toolbox possible todo later...");
        // return;
      }
    }
    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getDriverFixLevel() <= 27438
        && getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable("Native JDBC driver needs fix > 27438 current fix = "
          + getDriverFixLevel());
      return;
    }
    String added = " -- added 5/8/07 by native driver for SE28810 ";
    int length = 0;
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);
      statement_.executeUpdate("INSERT INTO " + JDPSTest.PSTEST_SET
          + "(C_BLOB) VALUES(BLOB(X'0001'))");
      PreparedStatement ps = connection_.prepareStatement(
          "SELECT * FROM " + JDPSTest.PSTEST_SET + " WHERE C_BLOB = ?");
      byte[] b = new byte[9000];
      InputStream is = new ByteArrayInputStream(b);
      length = b.length;
      ps.setBinaryStream(1, is, length);
      SQLWarning warning = ps.getWarnings();
      ps.executeQuery();
      if (warning == null) {
        warning = ps.getWarnings();
      }
      ps.close();
      if (warning == null) {
        failed("Didn't report warning  " + added);
      } else {
        if (warning instanceof DataTruncation) {
          DataTruncation dt = (DataTruncation) warning;

          if (isToolboxDriver()) {
            /* Toolbox fixed Dec 2018 to return correct values */
            assertCondition(
                (dt.getIndex() == 1) && (dt.getParameter() == true)
                    && (dt.getRead() == false) && (dt.getDataSize() == 9000)
                    && (dt.getTransferSize() == 200),
                "\ndt.getIndex()=" + dt.getIndex() + " sb 1 "
                    + "\ndt.getParameter()=" + dt.getParameter() + " sb true "
                    + "\ndt.getRead()=" + dt.getRead() + " sb false "
                    + "\ndt.getDataSize()" + dt.getDataSize() + " sb " + 9000
                    + " " + "\ndt.getTransferSize()" + dt.getTransferSize()
                    + " sb 200 (Toolbox fixed values Dec 2018) " + added);
          } else {
            assertCondition(
                (dt.getIndex() == 1) && (dt.getParameter() == true)
                    && (dt.getRead() == false) && (dt.getDataSize() == length)
                    && (dt.getTransferSize() == 200),
                "\ndt.getIndex()=" + dt.getIndex() + " sb 1 "
                    + "\ndt.getParameter()=" + dt.getParameter() + " sb true "
                    + "\ndt.getRead()=" + dt.getRead() + " sb false "
                    + "\ndt.getDataSize()" + dt.getDataSize() + " sb " + length
                    + " " + "\ndt.getTransferSize()" + dt.getTransferSize()
                    + " sb 200" + added);
          }
        } else {
          failed("Warning not DataTruncation " + warning + added);
        }
      }
    } catch (Throwable e) {
      failed(e, "Unexpected Exception" + added);
    }
  }

  /**
   * setBinaryStream() - Set an XML parameter.
   **/
  public void setXML(String tablename, String byteEncoding, String data,
      String expected) {

    String added = " -- added by native driver 08/21/2009";
    if (checkXmlSupport()) {
      try {

        String jvm = System.getProperty("java.vm.name");

        if ((byteEncoding.equals("UCS-2")
            && jvm.indexOf("Eclipse OpenJ9") >= 0)) {
          /* Eclipse OpenJ9 does not UCS-2, change to UTF-16BE */
          byteEncoding = "UTF-16BE";
        }

        statement_.executeUpdate("DELETE FROM " + tablename);

        PreparedStatement ps = connection_
            .prepareStatement("INSERT INTO " + tablename + "  VALUES (?)");
        byte[] bytes = data.getBytes(byteEncoding);
        InputStream is = new ByteArrayInputStream(bytes);
        ps.setBinaryStream(1, is, bytes.length);
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

  /**
   * setBinaryStream() - Set an XML parameter using invalid data.
   **/
  public void setInvalidXML(String tablename, String byteEncoding, String data,
      String expectedException) {
    String added = " -- added by native driver 08/21/2009";
    if (checkXmlSupport()) {
      try {
        statement_.executeUpdate("DELETE FROM " + tablename);

        PreparedStatement ps = connection_
            .prepareStatement("INSERT INTO " + tablename + "  VALUES (?)");
        byte[] bytes = data.getBytes(byteEncoding);
        InputStream is = new ByteArrayInputStream(bytes);
        ps.setBinaryStream(1, is, bytes.length);
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

  /* Insert various types against a UTF-8 table */

  public void Var088() {
    setXML(JDPSTest.PSTEST_SETXML, "ISO8859_1", "<Test>VAR088</Test>",
        "<Test>VAR088</Test>");
  }

  public void Var089() {
    setXML(JDPSTest.PSTEST_SETXML, "ISO8859_1",
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>VAR089\u00fb</Test>",
        "<Test>VAR089\u00fb</Test>");
  }

  public void Var090() {
    setXML(JDPSTest.PSTEST_SETXML, "UTF-8",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>VAR090\u00fb</Test>",
        "<Test>VAR090\u00fb</Test>");
  }

  public void Var091() {
    setXML(JDPSTest.PSTEST_SETXML, "IBM-037",
        "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>VAR091\u00fb</Test>",
        "<Test>VAR091\u00fb</Test>");
  }

  public void Var092() {
    setXML(JDPSTest.PSTEST_SETXML, "IBM-937",
        "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>VAR092\u672b</Test>",
        "<Test>VAR092\u672b</Test>");
  }

  public void Var093() {
    if (isToolboxDriver()) {
      notApplicable("non-supported"); // windows does not like CCSID 290
    } else
      setXML(JDPSTest.PSTEST_SETXML, "IBM-290",
          "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>VAR093\uff7a</Test>",
          "<Test>VAR093\uff7a</Test>");
  }

  public void Var094() {
    if (isToolboxDriver())
      notApplicable("non-supported"); // windows does not like UCS-2
    else
      setXML(JDPSTest.PSTEST_SETXML, "UCS-2",
          "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR094\u00fb\uff7a</Test>",
          "<Test>VAR094\u00fb\uff7a</Test>");
  }

  public void Var095() {
    setXML(JDPSTest.PSTEST_SETXML, "UTF-16BE",
        "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>VAR095\u00fb\uff7a\ud800\udf30</Test>",
        "<Test>VAR095\u00fb\uff7a\ud800\udf30</Test>");
  }

  public void Var096() {
    setXML(JDPSTest.PSTEST_SETXML, "UTF-16LE",
        "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>VAR096\u00fb\uff7a\ud800\udf30</Test>",
        "<Test>VAR096\u00fb\uff7a\ud800\udf30</Test>");
  }

  /* Insert various types against a 13488 table */

  public void Var097() {

    setXML(JDPSTest.PSTEST_SETXML13488, "ISO8859_1", "<Test>VAR097</Test>",
        "<Test>VAR097</Test>");
  }

  public void Var098() {

    setXML(JDPSTest.PSTEST_SETXML13488, "ISO8859_1",
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>VAR098\u00fb</Test>",
        "<Test>VAR098\u00fb</Test>");
  }

  public void Var099() {

    setXML(JDPSTest.PSTEST_SETXML13488, "UTF-8",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>VAR099\u00fb</Test>",
        "<Test>VAR099\u00fb</Test>");
  }

  public void Var100() {

    setXML(JDPSTest.PSTEST_SETXML13488, "IBM-037",
        "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>VAR100\u00fb</Test>",
        "<Test>VAR100\u00fb</Test>");
  }

  public void Var101() {

    setXML(JDPSTest.PSTEST_SETXML13488, "IBM-937",
        "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>VAR101\u672b</Test>",
        "<Test>VAR101\u672b</Test>");
  }

  public void Var102() {
    if (isToolboxDriver()) {
      notApplicable("non-supported"); // windows does not like CCSID 290
    } else
      setXML(JDPSTest.PSTEST_SETXML13488, "IBM-290",
          "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>VAR102\uff7a</Test>",
          "<Test>VAR102\uff7a</Test>");
  }

  public void Var103() {
    if (isToolboxDriver()) {
      notApplicable("non-supported"); // windows does not like UCS-2
    } else
      setXML(JDPSTest.PSTEST_SETXML13488, "UCS-2",
          "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR103\u00fb\uff7a</Test>",
          "<Test>VAR103\u00fb\uff7a</Test>");
  }

  public void Var104() {

    setXML(JDPSTest.PSTEST_SETXML13488, "UTF-16BE",
        "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>VAR104\u00fb\uff7a</Test>",
        "<Test>VAR104\u00fb\uff7a</Test>");
  }

  public void Var105() {

    setXML(JDPSTest.PSTEST_SETXML13488, "UTF-16LE",
        "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>VAR105\u00fb\uff7a</Test>",
        "<Test>VAR105\u00fb\uff7a</Test>");
  }

  /* Insert various types against a UTF-16 table */

  public void Var106() {

    setXML(JDPSTest.PSTEST_SETXML1200, "ISO8859_1", "<Test>VAR106</Test>",
        "<Test>VAR106</Test>");
  }

  public void Var107() {

    setXML(JDPSTest.PSTEST_SETXML1200, "ISO8859_1",
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>VAR089\u00fb</Test>",
        "<Test>VAR089\u00fb</Test>");
  }

  public void Var108() {

    setXML(JDPSTest.PSTEST_SETXML1200, "UTF-8",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>VAR090\u00fb</Test>",
        "<Test>VAR090\u00fb</Test>");
  }

  public void Var109() {

    setXML(JDPSTest.PSTEST_SETXML1200, "IBM-037",
        "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>VAR091\u00fb</Test>",
        "<Test>VAR091\u00fb</Test>");
  }

  public void Var110() {

    setXML(JDPSTest.PSTEST_SETXML1200, "IBM-937",
        "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>VAR092\u672b</Test>",
        "<Test>VAR092\u672b</Test>");
  }

  public void Var111() {
    if (isToolboxDriver()) {
      // ascii data sent to 1208 xml (cent sign conversion from ascii to 1208)
      notApplicable("NA - XML Toolbox future iteration -- CCSID 290");
      return;
    }
    setXML(JDPSTest.PSTEST_SETXML1200, "IBM-290",
        "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>VAR093\uff7a</Test>",
        "<Test>VAR093\uff7a</Test>");
  }

  public void Var112() {
    if (isToolboxDriver()) {
      // ascii data sent to 1208 xml (cent sign conversion from ascii to 1208)
      notApplicable("NA - XML Toolbox future iteration -- CCSID UCS-2");
      return;
    }
    setXML(JDPSTest.PSTEST_SETXML1200, "UCS-2",
        "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR094\u00fb\uff7a</Test>",
        "<Test>VAR094\u00fb\uff7a</Test>");
  }

  public void Var113() {

    setXML(JDPSTest.PSTEST_SETXML1200, "UTF-16BE",
        "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>VAR095\u00fb\uff7a\ud800\udf30</Test>",
        "<Test>VAR095\u00fb\uff7a\ud800\udf30</Test>");
  }

  public void Var114() {

    setXML(JDPSTest.PSTEST_SETXML1200, "UTF-16LE",
        "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>VAR096\u00fb\uff7a\ud800\udf30</Test>",
        "<Test>VAR096\u00fb\uff7a\ud800\udf30</Test>");
  }

  /* Insert various types against a EBCDIC-37 table */

  public void Var115() {
    setXML(JDPSTest.PSTEST_SETXML37, "ISO8859_1", "<Test>VAR115</Test>",
        "<Test>VAR115</Test>");
  }

  public void Var116() {
    setXML(JDPSTest.PSTEST_SETXML37, "ISO8859_1",
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>VAR089\u00fb</Test>",
        "<Test>VAR089\u00fb</Test>");
  }

  public void Var117() {
    setXML(JDPSTest.PSTEST_SETXML37, "UTF-8",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>VAR090\u00fb</Test>",
        "<Test>VAR090\u00fb</Test>");
  }

  public void Var118() {
    setXML(JDPSTest.PSTEST_SETXML37, "IBM-037",
        "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>VAR091\u00fb</Test>",
        "<Test>VAR091\u00fb</Test>");
  }

  public void Var119() {
    setXML(JDPSTest.PSTEST_SETXML37, "IBM-937",
        "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>VAR092</Test>",
        "<Test>VAR092</Test>");
  }

  public void Var120() {
    if (isToolboxDriver()) {
      notApplicable("non-supported"); // windows does not like
    } else
      setXML(JDPSTest.PSTEST_SETXML37, "IBM-290",
          "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>VAR093</Test>",
          "<Test>VAR093</Test>");
  }

  public void Var121() {
    if (isToolboxDriver()) {
      notApplicable("non-supported"); // windows does not like
    } else
      setXML(JDPSTest.PSTEST_SETXML37, "UCS-2",
          "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR094\u00fb</Test>",
          "<Test>VAR094\u00fb</Test>");
  }

  public void Var122() {
    setXML(JDPSTest.PSTEST_SETXML37, "UTF-16BE",
        "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>VAR095\u00fb</Test>",
        "<Test>VAR095\u00fb</Test>");
  }

  public void Var123() {
    setXML(JDPSTest.PSTEST_SETXML37, "UTF-16LE",
        "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>VAR096\u00fb</Test>",
        "<Test>VAR096\u00fb</Test>");
  }

  /* Insert various types against a EBCDIC-937 table */

  public void Var124() {
    setXML(JDPSTest.PSTEST_SETXML937, "ISO8859_1", "<Test>VAR088</Test>",
        "<Test>VAR088</Test>");
  }

  public void Var125() {
    setXML(JDPSTest.PSTEST_SETXML937, "ISO8859_1",
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>VAR089</Test>",
        "<Test>VAR089</Test>");
  }

  public void Var126() {
    setXML(JDPSTest.PSTEST_SETXML937, "UTF-8",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>VAR090\u672b</Test>",
        "<Test>VAR090\u672b</Test>");
  }

  public void Var127() {
    setXML(JDPSTest.PSTEST_SETXML937, "IBM-037",
        "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>VAR091</Test>",
        "<Test>VAR091</Test>");
  }

  public void Var128() {
    setXML(JDPSTest.PSTEST_SETXML937, "IBM-937",
        "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>VAR092\u672b</Test>",
        "<Test>VAR092\u672b</Test>");
  }

  public void Var129() {
    if (isToolboxDriver()) {
      notApplicable("non-supported"); // windows does not like
    } else
      setXML(JDPSTest.PSTEST_SETXML937, "IBM-290",
          "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>VAR093</Test>",
          "<Test>VAR093</Test>");
  }

  public void Var130() {
    if (isToolboxDriver()) {
      notApplicable("non-supported"); // windows does not like
    } else
      setXML(JDPSTest.PSTEST_SETXML937, "UCS-2",
          "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR094\u672b</Test>",
          "<Test>VAR094\u672b</Test>");
  }

  public void Var131() {
    setXML(JDPSTest.PSTEST_SETXML937, "UTF-16BE",
        "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>VAR095\u672b</Test>",
        "<Test>VAR095\u672b</Test>");
  }

  public void Var132() {
    setXML(JDPSTest.PSTEST_SETXML937, "UTF-16LE",
        "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>VAR096\u672b</Test>",
        "<Test>VAR096\u672b</Test>");
  }

  /* Insert various types against a EBCDIC 290 table */

  public void Var133() {
    setXML(JDPSTest.PSTEST_SETXML290, "ISO8859_1", "<Test>VAR133</Test>",
        "<Test>VAR133</Test>");
  }

  public void Var134() {
    setXML(JDPSTest.PSTEST_SETXML290, "ISO8859_1",
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>VAR089</Test>",
        "<Test>VAR089</Test>");
  }

  public void Var135() {
    setXML(JDPSTest.PSTEST_SETXML290, "UTF-8",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <Test>VAR090\uff7a</Test>",
        "<Test>VAR090\uff7a</Test>");
  }

  public void Var136() {
    setXML(JDPSTest.PSTEST_SETXML290, "IBM-037",
        "<?xml version=\"1.0\" encoding=\"IBM-037\"?>  <Test>VAR091</Test>",
        "<Test>VAR091</Test>");
  }

  public void Var137() {
    setXML(JDPSTest.PSTEST_SETXML290, "IBM-937",
        "<?xml version=\"1.0\" encoding=\"IBM-937\"?>  <Test>VAR092</Test>",
        "<Test>VAR092</Test>");
  }

  public void Var138() {
    if (isToolboxDriver()) {
      notApplicable("non-supported"); // windows does not like
    } else
      setXML(JDPSTest.PSTEST_SETXML290, "IBM-290",
          "<?xml version=\"1.0\" encoding=\"IBM-290\"?>  <Test>VAR093\uff7a</Test>",
          "<Test>VAR093\uff7a</Test>");
  }

  public void Var139() {
    if (isToolboxDriver()) {
      notApplicable("non-supported"); // windows does not like
    } else
      setXML(JDPSTest.PSTEST_SETXML290, "UCS-2",
          "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR094\uff7a</Test>",
          "<Test>VAR094\uff7a</Test>");
  }

  public void Var140() {
    setXML(JDPSTest.PSTEST_SETXML290, "UTF-16BE",
        "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>VAR095\uff7a</Test>",
        "<Test>VAR095\uff7a</Test>");
  }

  public void Var141() {
    setXML(JDPSTest.PSTEST_SETXML290, "UTF-16LE",
        "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>VAR096\uff7a</Test>",
        "<Test>VAR096\uff7a</Test>");
  }

  /**
   * setBinaryStream() - Set a VARBINARY parameter using an input stream that
   * returns 0 in available. Fixed in Toolbox 4/30/2010 for CPS discussion CPS
   * DB Item: 84ZKPP
   **/
  public void Var142() {
    class ByteArrayInputStream2 extends ByteArrayInputStream {
      public ByteArrayInputStream2(byte[] buf) {
        super(buf);
      }

      public int available() {
        return 0;
      };
    }

    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 1, (byte) 0,
          (byte) -33, (byte) 57, (byte) 9 };

      InputStream is = new ByteArrayInputStream2(b);
      ps.setBinaryStream(1, is, 7);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      byte[] check = rs.getBytes(1);
      rs.close();

      assertCondition(areEqual(b, check));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setBinaryStream() - Set a Binary stream. Make sure the blob is not
   * materialized and consume memory at the time of the set. This is a toolbox
   * only testcase for CPS 8BZNZT.
   **/
  public void Var143() {

    if (isToolboxDriver()) {
      if (getSubDriver() == JDTestDriver.SUBDRIVER_JTOPENSF) {
        notApplicable("JTOPENSF subdriver materializes blob");
        return;
      }
      try {
        Runtime runtime = Runtime.getRuntime();
        String table = JDPSTest.COLLECTION + ".JDPSSBS143";
        try {
          statement_.executeUpdate("Drop table " + table);
        } catch (Exception e) {
        }
        statement_.executeUpdate("CREATE TABLE " + table + " (c1 blob(200M))");

        PreparedStatement ps = connection_
            .prepareStatement("INSERT INTO " + table + " VALUES (?)");

        byte[] stuff = new byte[1000000];
        InputStream is = new ByteArrayInputStream(stuff);
        System.gc();
        long freeMemoryBefore = runtime.freeMemory();
        ps.setBinaryStream(1, is, 1000000);
        long freeMemoryAfter = runtime.freeMemory();
        System.out.println("Before freeMemory=" + freeMemoryBefore);
        System.out.println("After  freeMemory=" + freeMemoryAfter);

        ps.close();
        statement_.executeUpdate("Drop table " + table);
        assertCondition(freeMemoryBefore - freeMemoryAfter < 250000,
            "Memory used = " + (freeMemoryBefore - freeMemoryAfter)
                + " -- toolbox test added 12/09/2010 for CPS 8BNS4T");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    } else {
      notApplicable("Toolbox only test");
    }
  }

  public  boolean runBlockingTest(String testcase, String tableName,
      Connection connection, String jdbcUrl, int blockSize, StringBuffer sb)
      throws Exception {
    boolean passed = true;
    String testTable = JDPSTest.COLLECTION + "." + tableName;
    String sql;
    Statement s = connection.createStatement();

    try {
      sql = "DROP TABLE " + testTable;
      sb.append("Executing sql: " + sql + "\n");
      s.executeUpdate(sql);
    } catch (Exception e) {
      sb.append("Ignored exception " + e + "\n");
    }

    sql = "CREATE TABLE " + testTable + "(C1 BLOB(2G))";
    sb.append("Executing sql: " + sql + "\n");
    s.executeUpdate(sql);
    connection.commit();


    sql = "GRANT ALL ON " + testTable + " TO PUBLIC";
    sb.append("Executing sql: " + sql + "\n");
    s.executeUpdate(sql);
    connection.commit();



    Vector resultVector = new Vector();

    {
      sb.append("Getting blocking connection");
      if (blockSize > 0) {
        jdbcUrl = jdbcUrl + ";lob block size=" + blockSize;
      }
      Connection blockingConnection = testDriver_.getConnection (jdbcUrl,systemObject_.getUserId(), encryptedPassword_);

      sql = "INSERT INTO " + testTable + " VALUES(?)";
      sb.append("Preparing sql: " + sql + "\n");
      PreparedStatement ps = blockingConnection.prepareStatement(sql);

      long size = 1024 * 1024;
      long endTime = System.currentTimeMillis()
          + 60 * 1000 * (3 / 2); /* 1.5 minutes */
      while (size <= 0x4000000L && System.currentTimeMillis() < endTime) {
        long startTime = System.currentTimeMillis();

        InputStream binaryStream = new JDPSBinaryStream(size);
        sb.append("setBinaryStream size=" + size + "\n");
        if (testcase.equals("JDPSSetBinaryStream")) {
          ps.setBinaryStream(1, binaryStream, (int) size);
        } else if (testcase.equals("JDPSSetBinaryStream40")) {
          JDReflectionUtil.callMethod_V(ps, "setBinaryStream", 1, binaryStream);
        } else {
          binaryStream.close();
          throw new Exception("Unexpected testcase " + testcase);
        }
        if (System.currentTimeMillis() < endTime) {
          sb.append("executing\n");
          ps.executeUpdate();

          String[] result = new String[2];
          long elapsedTime = System.currentTimeMillis() - startTime;
          result[0] = "" + size;
          result[1] = "" + elapsedTime;
          System.out.println(size + " took " + elapsedTime + " ms for "
              + testcase + " blockSize=" + blockSize);
          sb.append(size + " took " + elapsedTime + " ms\n");
          resultVector.addElement(result);
          size = size * 4;
          System.gc();
        }
      }

      ps.close();

      Statement s2 = blockingConnection.createStatement();
      try {
        sql = "DROP TABLE " + testTable;
        sb.append("Executing sql: " + sql + "\n");
        s2.executeUpdate(sql);
      } catch (Exception e) {
        System.out.println("WARNING:  TABLE did not drop");
        e.printStackTrace(System.out);
      }
      s2.close();
      blockingConnection.commit();
      blockingConnection.close();

    } //
      // Todo: insert results into table
      //

    String resultTable = "JDPERFRES.JDPSSBS";
    PreparedStatement ps;
    try {
      sql = "INSERT INTO " + resultTable + " VALUES(?,?,?,?,?,?)";
      sb.append("Preparing sql: " + sql + "\n");
      ps = connection.prepareStatement(sql);
    } catch (Exception e) {
      sql = "CREATE TABLE " + resultTable
          + " ( TESTCASE VARCHAR(80), LIBRARY VARCHAR(80), JVM VARCHAR(80), "
          + "BLOCKSIZE INTEGER, SIZE BIGINT, MS INTEGER)";
      sb.append("Executing sql: " + sql + "\n");
      s.executeUpdate(sql);
      sql = "INSERT INTO " + resultTable + " VALUES(?,?,?,?,?,?)";
      sb.append("Preparing sql: " + sql + "\n");
      ps = connection.prepareStatement(sql);

    }
    Enumeration enumeration = resultVector.elements();
    while (enumeration.hasMoreElements()) {
      String[] result = (String[]) enumeration.nextElement();
      ps.setString(1, testcase);
      ps.setString(2, JDPSTest.COLLECTION);
      ps.setString(3, System.getProperty("java.home"));
      ps.setString(4, "" + blockSize);
      ps.setString(5, result[0]);
      ps.setString(6, result[1]);
      ps.executeUpdate();
    }
    ps.close();

    connection.commit();

    s.close();

    connection.commit();

    return passed;
  }

  /**
   * setBinaryStream() - Set a Binary stream and record how long it takes. Run
   * for a maximum of five minutes. This began as a native JDBC Test for CPS
   * 99QBCP.
   **/
  public void Var144() {

    StringBuffer sb = new StringBuffer(" -- Testcases added 8/6/2013 \n");
    try {
      boolean passed = runBlockingTest("JDPSSetBinaryStream", "JDPSSBS144",
          connection_, testUrl, 0, sb);
      assertCondition(passed, sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception \n" + sb.toString());
    }

  }

  /**
   * setBinaryStream() - Set a Binary stream and record how long it takes. Run
   * for a maximum of five minutes. This began as a native JDBC Test for CPS
   * 99QBCP.
   **/
  public void Var145() {

    StringBuffer sb = new StringBuffer(" -- Testcases added 8/6/2013 \n");
    try {
      boolean passed = runBlockingTest("JDPSSetBinaryStream", "JDPSSBS145",
          connection_, testUrl, 10485760, sb);
      assertCondition(passed, sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception\n" + sb.toString());
    }

  }

  /**
   * setBinaryStream() - Set a Binary stream and record how long it takes. Run
   * for a maximum of five minutes. This began as a native JDBC Test for CPS
   * 99QBCP.
   **/
  public void Var146() {

    StringBuffer sb = new StringBuffer(" -- Testcases added 8/6/2013 \n");
    try {
      boolean passed = runBlockingTest("JDPSSetBinaryStream", "JDPSSBS146",
          connection_, testUrl, 1048576, sb);
      assertCondition(passed, sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception\n" + sb.toString());
    }

  }
  
  /**
   * setBinaryStream() - Set a SMALLINT parameter.
   **/
  public void Var147() {
    if (checkBooleanSupport()) {
      byte[] b = new byte[] { (byte) 98, (byte) 123 };
      testSetFailed("C_BOOLEAN", b);
    }

  }

}
