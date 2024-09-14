///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetBlob.java
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
// File Name:    JDPSSetBlob.java
//
// Classes:      JDPSSetBlob
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
//     - added Var033
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDLobTest;
import test.JDPSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;
import test.JD.JDTestUtilities;
import test.JDLobTest.JDTestBlob;

import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;

/**
 * Testcase JDPSSetBlob. This tests the following method of the JDBC
 * PreparedStatement class:
 * 
 * <ul>
 * <li>setBlob()
 * </ul>
 * 
 * 
 * Note: The JDBC 4.0 version of setBlob is in JDPSSetBlob40
 * 
 **/
public class JDPSSetBlob extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetBlob";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }

  // Constants.
  private static final String PACKAGE = "JDPSSBLOB";

  // Private data.
  private Connection connection_;
  private Connection connectionNoDT_;
  private Statement statement_;
  private String jvm;
  private String jvmVendor;

  /**
   * Constructor.
   **/
  public JDPSSetBlob(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDPSSetBlob", namesAndVars, runMode, fileOutputStream,
 password);

    jvm = System.getProperty("java.vm.name");
    jvmVendor = System.getProperty("java.vm.vendor");
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {

    String url = baseURL_ + ";lob threshold=30000" + ";data truncation=true";
    connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
    statement_ = connection_.createStatement();

    url = url + ";data truncation=false";
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
   * setBlob() - Should throw exception when the prepared statement is closed.
   **/
  public void Var001() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
        ps.close();
        byte[] b = new byte[] { (byte) 22, (byte) 98, (byte) -2 };
        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Should throw exception when an invalid index is specified.
   **/
  public void Var002() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
        byte[] b = new byte[] { (byte) 22, (byte) -1, (byte) 4, (byte) 98,
            (byte) -2 };
        ps.setBlob(100, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Should throw exception when index is 0.
   **/
  public void Var003() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
        byte[] b = new byte[] { (byte) 4, (byte) 98, (byte) 4, (byte) -2 };
        ps.setBlob(0, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Should throw exception when index is -1.
   **/
  public void Var004() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
        byte[] b = new byte[] { (byte) 4, (byte) 22, (byte) 4, (byte) 22,
            (byte) -2 };
        ps.setBlob(0, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Should set to SQL NULL when the value is null.
   **/
  public void Var005() {
    if (checkJdbc20()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
        ps.setBlob(1, (java.sql.Blob) null);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        Blob check = rs.getBlob(1);
        boolean wn = rs.wasNull();
        rs.close();

        assertCondition((check == null) && (wn == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setBlob() - Should work with a valid parameter index greater than 1.
   **/
  public void Var006() {
    if (checkJdbc20()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_KEY, C_VARBINARY_20) VALUES (?, ?)");
        ps.setString(1, "Muchas");
        byte[] b = new byte[] { (byte) -22, (byte) 4, (byte) 98, (byte) -2 };
        ps.setBlob(2, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        Blob check = rs.getBlob(1);

        rs.close();

        assertCondition(areEqual(b, check.getBytes(1, (int) check.length()))); // @B1C
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setBlob() - Should throw exception when the parameter is not an input
   * parameter.
   **/
  public void Var007() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
        byte[] b = new byte[] { (byte) 98, (byte) 98, (byte) 98, (byte) -2 };

        ps.setBlob(2, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Verify that a data truncation warning is posted when data is
   * truncated.
   **/
  public void Var008() {
    if (checkJdbc20()) {
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
            (byte) 50, (byte) 2, (byte) 0, (byte) 50, (byte) 2, (byte) 0,
            (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50,
            (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0,
            (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50,
            (byte) 2, (byte) 22 };

        length = b.length;
        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
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
  }

  public void testSetFailed(String columnName, byte[] inArray) {

    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (" + columnName + ") VALUES (?)");

        ps.setBlob(1, new JDLobTest.JDTestBlob(inArray));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * setBlob() - Set a SMALLINT parameter.
   **/
  public void Var009() {
    byte[] b = new byte[] { (byte) 98, (byte) -111 };
    testSetFailed("C_SMALLINT", b);
  }

  /**
   * setBlob() - Set a INTEGER parameter.
   **/
  public void Var010() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -111, (byte) -2,
            (byte) 123 };

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Set a REAL parameter.
   **/
  public void Var011() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_REAL) VALUES (?)");
        byte[] b = new byte[] { (byte) 98 };

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Set a FLOAT parameter.
   **/
  public void Var012() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_FLOAT) VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) -2, (byte) 98, (byte) 7,
            (byte) -2, (byte) 45 };

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Set a DOUBLE parameter.
   **/
  public void Var013() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DOUBLE) VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) 45, (byte) -2,
            (byte) 12 };

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Set a DECIMAL parameter.
   **/
  public void Var014() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_DECIMAL_105) VALUES (?)");
        byte[] b = new byte[] { (byte) -2, (byte) 98, (byte) -2, (byte) 45,
            (byte) 12, (byte) -33 };

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Set a NUMERIC parameter.
   **/
  public void Var015() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_NUMERIC_50) VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) 7, (byte) -2,
            (byte) 45, (byte) 12, (byte) -33 };

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Set a CHAR(1) parameter.
   **/
  public void Var016() {
    if (checkJdbc20()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_1) VALUES (?)");
        byte[] b = new byte[] { (byte) 33 };

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Set a CHAR(50) parameter.
   **/
  public void Var017() {
    if (checkJdbc20()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_50) VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) 12, (byte) -12, (byte) 45,
            (byte) 12, (byte) -33, (byte) 0 };

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Set a VARCHAR(50) parameter.
   **/
  public void Var018() {
    if (checkJdbc20()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_VARCHAR_50) VALUES (?)");
        byte[] b = new byte[] { (byte) 127, (byte) -12, (byte) 12, (byte) -33 };

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Set a CLOB parameter.
   **/
  public void Var019() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CLOB) VALUES (?)");
          byte[] b = new byte[] { (byte) 108, (byte) 0, (byte) -12, (byte) 12,
              (byte) -33 };

          ps.setBlob(1, new JDLobTest.JDTestBlob(b));
          ps.executeUpdate();
          ps.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setBlob() - Set a DBCLOB parameter.
   **/
  public void Var020() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DBCLOB) VALUES (?)");
          byte[] b = new byte[] { (byte) 0, (byte) -66, (byte) -12,
              (byte) -33 };

          ps.setBlob(1, new JDLobTest.JDTestBlob(b));
          ps.executeUpdate();
          ps.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setBlob() - Set a BINARY parameter.
   **/
  public void Var021() {
    if (checkJdbc20()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BINARY_20) VALUES (?)");
        byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 0, (byte) 12,
            (byte) -33, (byte) 57, (byte) 9 };

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_BINARY_20 FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        Blob check = rs.getBlob(1);
        rs.close();

        byte[] check2 = new byte[20];
        System.arraycopy(b, 0, check2, 0, b.length);

        assertCondition(
            areEqual(check.getBytes(1, (int) check.length()), check2)); // @B1C
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setBlob() - Set a VARBINARY parameter.
   **/
  public void Var022() {
    if (checkJdbc20()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
        byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 0, (byte) -33,
            (byte) 9 };

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        Blob check = rs.getBlob(1);
        rs.close();

        assertCondition(areEqual(b, check.getBytes(1, (int) check.length()))); // @B1C
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setBlob() - Set a VARBINARY parameter to an empty array.
   **/
  public void Var023() {
    if (checkJdbc20()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
        byte[] b = new byte[0];

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        Blob check = rs.getBlob(1);
        rs.close();

        assertCondition(check.length() == 0);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setBlob() - Set a BLOB parameter, when the data is passed directly.
   **/
  public void Var024() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BLOB) VALUES (?)");
          byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57, (byte) 45,
              (byte) -33, (byte) 0 };

          ps.setBlob(1, new JDLobTest.JDTestBlob(b));
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_
              .executeQuery("SELECT C_BLOB FROM " + JDPSTest.PSTEST_SET);
          rs.next();
          Blob check = rs.getBlob(1);
          rs.close();

          assertCondition(areEqual(b, check.getBytes(1, (int) check.length()))); // @B1C
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setBlob() - Set a BLOB parameter, when the data is passed in a locator.
   **/
  public void Var025() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

          String url = baseURL_ +  ";lob threshold=1";
          Connection c = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
          PreparedStatement ps = c.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BLOB) VALUES (?)");
          byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 57, (byte) 45,
              (byte) -33, (byte) 0 };

          ps.setBlob(1, new JDLobTest.JDTestBlob(b));
          ps.executeUpdate();
          ps.close();
          c.close();

          ResultSet rs = statement_
              .executeQuery("SELECT C_BLOB FROM " + JDPSTest.PSTEST_SET);
          rs.next();
          Blob check = rs.getBlob(1);
          rs.close();

          assertCondition(areEqual(b, check.getBytes(1, (int) check.length()))); // @B1C
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setBlob() - Set a DATE parameter.
   **/
  public void Var026() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DATE) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 57, (byte) 1, (byte) -33,
            (byte) 0 };

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Set a TIME parameter.
   **/
  public void Var027() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIME) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) -33,
            (byte) 0, (byte) 45, (byte) 5 };

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Set a TIMESTAMP parameter.
   **/
  public void Var028() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIMESTAMP) VALUES (?)");
        byte[] b = new byte[] { (byte) 45, (byte) 11, (byte) -33, (byte) 0,
            (byte) 5 };

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setBlob() - Set a DATALINK parameter.
   **/
  public void Var029() {
    if (checkJdbc20()) {
      if (checkDatalinkSupport()) {
        try {
          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + JDPSTest.PSTEST_SET + " (C_DATALINK) VALUES (?)");
          byte[] b = new byte[] { (byte) -12, (byte) -12, (byte) 1, (byte) 45,
              (byte) 1, (byte) 11, (byte) -33, (byte) 0, (byte) 5, (byte) 100 };

          ps.setBlob(1, new JDLobTest.JDTestBlob(b));
          ps.executeUpdate();
          ps.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setBlob() - Set a DISTINCT parameter.
   **/
  public void Var030() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + JDPSTest.PSTEST_SET + " (C_DISTINCT) VALUES (?)");
          byte[] b = new byte[] { (byte) -12, (byte) -12, (byte) 1, (byte) -33,
              (byte) 0, (byte) 5, (byte) 100 };

          ps.setBlob(1, new JDLobTest.JDTestBlob(b));
          ps.executeUpdate();
          ps.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setBlob() - Set a BIGINT parameter.
   **/
  public void Var031() {
    if (checkJdbc20()) {
      if (checkBigintSupport()) {
        try {
          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BIGINT) VALUES (?)");
          byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -111, (byte) -2,
              (byte) 123 };

          ps.setBlob(1, new JDLobTest.JDTestBlob(b));
          ps.executeUpdate();
          ps.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setBlob() - Set a VARBINARY parameter with package caching.
   * 
   * SQL400 - JDSetupPackage cache doesn't have enough information to be driver
   * neutral today.
   **/
  public void Var032() {
    if (checkJdbc20()) {
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
        PreparedStatement ps = connection_.prepareStatement(insert);
        byte[] b = new byte[] { (byte) 11, (byte) -12, (byte) 45, (byte) 11,
            (byte) -12, (byte) 0, (byte) 5, (byte) 100 };

        ps.setBlob(1, new JDLobTest.JDTestBlob(b));
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_VARBINARY_20 FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        Blob check = rs.getBlob(1);
        rs.close();

        assertCondition(areEqual(check.getBytes(1, (int) check.length()), b),
            "c2=" + c2); // @B1C
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * B2A setBlob() - Verify that no data truncation warning is posted when data
   * is truncated but the data truncation flag is turned off.
   **/
  public void Var033() {
    if (checkNative()) {
      if (checkJdbc20()) {
        int length = 0;
        try {
          statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

          PreparedStatement ps = connectionNoDT_.prepareStatement("INSERT INTO "
              + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
          byte[] b = new byte[] { (byte) -22, (byte) 4, (byte) 9, (byte) -2,
              (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
              (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
              (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
              (byte) 77, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
              (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
              (byte) 50, (byte) 2, (byte) 0, (byte) 50, (byte) 2, (byte) 0,
              (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
              (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
              (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
              (byte) 50, (byte) 2, (byte) 22 };

          length = b.length;
          ps.setBlob(1, new JDLobTest.JDTestBlob(b));
          ps.close();
          assertCondition(true, "length is " + length);
        } catch (DataTruncation dt) {
          failed(dt, "Unexpected Data Truncation Exception");
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setBlob() - Set a BLOB parameter, when truncation occurs.
   **/
  public void Var034() {

    if (getDriver() == JDTestDriver.DRIVER_NATIVE
        && getDriverFixLevel() <= 27438
        && getRelease() < JDTestDriver.RELEASE_V7R1M0) {
      notApplicable(
          "Not working for native JDBC PTF level < 27438 : current level "
              + getDriverFixLevel());
      return;
    }

    String added = "Testcase to test truncation when setting a blob parameter -- Native bug returns internal server error -- added 05/08/2006 by native driver fix in native PTF level 27438 (V5R3) ";

    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BLOB) VALUES (?)");
          byte[] b = new byte[300];

          ps.setBlob(1, new JDLobTest.JDTestBlob(b));
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_
              .executeQuery("SELECT C_BLOB FROM " + JDPSTest.PSTEST_SET);
          rs.next();
          Blob check = rs.getBlob(1);
          rs.close();
          failed("Did not throw exception -- " + check + " " + added);
        } catch (Exception e) {
          // check for a trucation message from toolbox
          String message = e.toString();
          // v7r1 has diff text. just check error id
          if (message.indexOf("SQL0311") >= 0) {
            assertCondition(true);
          } else if (message.indexOf("Data truncation") >= 0) {
            assertCondition(true);
          } else {
            failed(e, "Unexpected Exception " + added);
          }
        }
      }
    }
  }

  /**
   * setBlob() - Set a DECFLOAT16 parameter.
   **/
  public void Var035() {
    if (checkJdbc20()) {
      if (checkDecFloatSupport()) {
        try {
          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SETDFP16 + " VALUES (?)");
          byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -111, (byte) -2,
              (byte) 123 };

          ps.setBlob(1, new JDLobTest.JDTestBlob(b));
          ps.executeUpdate();
          ps.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setBlob() - Set a DECFLOAT34 parameter.
   **/
  public void Var036() {
    if (checkJdbc20()) {
      if (checkDecFloatSupport()) {
        try {
          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SETDFP34 + " VALUES (?)");
          byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -111, (byte) -2,
              (byte) 123 };

          ps.setBlob(1, new JDLobTest.JDTestBlob(b));
          ps.executeUpdate();
          ps.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setBlob() - Set an XML parameter.
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
      /* Latest jdk11 does support UCS-2, change to UTF-16 */
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

    if (checkXmlSupport()) {
      try {
        statement_.executeUpdate("DELETE FROM " + tablename);

        PreparedStatement ps = connection_
            .prepareStatement("INSERT INTO " + tablename + "  VALUES (?)");
        byte[] bytes = data.getBytes(byteEncoding);
        ps.setBlob(1, new JDLobTest.JDTestBlob(bytes));
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
        System.out.println("Encoding is " + byteEncoding + " jvm is " + jvm
            + " jvmVendor is " + jvmVendor);
        failed(e, "Unexpected Exception" + added);
      }
    }
  }

  /**
   * setBlob() - Set an XML parameter using invalid data.
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
        ps.setBlob(1, new JDLobTest.JDTestBlob(bytes));
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
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Test>Var039</Test>",
        "<Test>Var039</Test>");
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
        "<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>  <Test>Var045\uff7a</Test>",
        "<Test>Var045\uff7a</Test>");
  }

  public void Var091() {
    setXML(JDPSTest.PSTEST_SETXML290, "UTF-16LE",
        "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>  <Test>Var046\uff7a</Test>",
        "<Test>Var046\uff7a</Test>");
  }

  /**
   * setBlob() - Set a BOOLEAN parameter.
   **/
  public void Var092() {
    if (checkBooleanSupport()) {
      byte[] b = new byte[] { (byte) 98, (byte) -111 };
      testSetFailed("C_BOOLEAN", b);
    }
  }

}
