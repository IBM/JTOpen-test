///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetInt.java
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
// File Name:    JDPSSetInt.java
//
// Classes:      JDPSSetInt
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;

/**
 * Testcase JDPSetInt. This tests the following method of the JDBC
 * PreparedStatement class:
 * 
 * <ul>
 * <li>setInt()
 * </ul>
 **/
public class JDPSSetInt extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetInt";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }

  // Constants.
  private static final String PACKAGE = "JDPSSI";

  // Private data.
  private Connection connection_;
  private Statement statement_;

  private Connection connectionNoTruncation_;
  private Statement statementNoTruncation_;

  /**
   * Constructor.
   **/
  public JDPSSetInt(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDPSSetInt", namesAndVars, runMode, fileOutputStream,
 password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      connection_ = testDriver_.getConnection(baseURL_,
          systemObject_.getUserId(), encryptedPassword_);
      connectionNoTruncation_ = testDriver_.getConnection(baseURL_,
          systemObject_.getUserId(), encryptedPassword_);

    } else {
      String url = baseURL_ + ";data truncation=true";
      connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

      String urlNoTruncation = baseURL_ +  ";data truncation=false";
      connectionNoTruncation_ = testDriver_.getConnection (urlNoTruncation,systemObject_.getUserId(), encryptedPassword_);
    }
    statement_ = connection_.createStatement();
    statementNoTruncation_ = connectionNoTruncation_.createStatement();
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

    statementNoTruncation_.close();
    connectionNoTruncation_.close();
  }

  /**
   * setInt() - Should throw exception when the prepared statement is closed.
   **/
  public void Var001() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
      ps.close();
      ps.setInt(1, (short) 533);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setInt() - Should throw exception when an invalid index is specified.
   **/
  public void Var002() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET
              + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
      ps.setInt(100, (short) 334);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setInt() - Should throw exception when index is 0.
   **/
  public void Var003() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET
              + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
      ps.setInt(0, (short) -385);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setInt() - Should throw exception when index is -1.
   **/
  public void Var004() {
    try {
      PreparedStatement ps = connection_
          .prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET
              + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
      ps.setInt(-1, (short) 943);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setInt() - Should work with a valid parameter index greater than 1.
   **/
  public void Var005() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_KEY, C_INTEGER) VALUES (?, ?)");
      ps.setString(1, "Test");
      ps.setInt(2, 4342);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      int check = rs.getInt(1);
      rs.close();

      assertCondition(check == 4342);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setInt() - Should throw exception when the parameter is not an input
   * parameter.
   **/
  public void Var006() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("JCC permits setting of an output parameter");
      return;
    }
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
      ps.setInt(2, (short) 23322);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setInt() - Set a SMALLINT parameter.
   **/
  public void Var007() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_SMALLINT) VALUES (?)");
      ps.setInt(1, -22342);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_SMALLINT FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      int check = rs.getInt(1);
      rs.close();

      assertCondition(check == -22342);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setInt() - Set a SMALLINT parameter, when the integer is too large.
   **/
  public void Var008() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_SMALLINT) VALUES (?)");
      ps.setInt(1, -22122342);
      ps.executeUpdate();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      boolean passed = e.getMessage().indexOf("Data type mismatch") != -1;
      if (passed) {
        assertCondition(passed, "passed");
      } else {
        failed(e, "got " + e + " expected data type mismatch for jtopenlite");
      }
    }
  }

  /**
   * setInt() - Set an INTEGER parameter.
   **/
  public void Var009() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
      ps.setInt(1, -10613);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      int check = rs.getInt(1);
      rs.close();

      assertCondition(check == -10613);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setInt() - Set an REAL parameter.
   **/
  public void Var010() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_REAL) VALUES (?)");
      ps.setInt(1, 1792);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_REAL FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      int check = rs.getInt(1);
      rs.close();

      assertCondition(check == 1792);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setInt() - Set an FLOAT parameter.
   **/
  public void Var011() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_FLOAT) VALUES (?)");
      ps.setInt(1, -1);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_FLOAT FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      int check = rs.getInt(1);
      rs.close();

      assertCondition(check == -1);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setInt() - Set an DOUBLE parameter.
   **/
  public void Var012() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DOUBLE) VALUES (?)");
      ps.setInt(1, 12228);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_DOUBLE FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      int check = rs.getInt(1);
      rs.close();

      assertCondition(check == 12228);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setInt() - Set an DECIMAL parameter.
   **/
  public void Var013() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DECIMAL_105) VALUES (?)");
      ps.setInt(1, -10012);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_DECIMAL_105 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      int check = rs.getInt(1);
      rs.close();

      assertCondition(check == -10012);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setInt() - Set an DECIMAL parameter, when the value is too big.
   **/
  public void Var014() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DECIMAL_50) VALUES (?)");
      ps.setInt(1, -100122134);
      ps.execute();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        assertSqlException(e, -99999, "07006", "Data type mismatch",
            "Mismatch instead of truncation now in toolbox");

      } else {
        assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
      }
    }
  }

  /**
   * setInt() - Set an NUMERIC parameter.
   **/
  public void Var015() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_NUMERIC_50) VALUES (?)");
      ps.setInt(1, 12777);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_NUMERIC_50 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      int check = rs.getInt(1);
      rs.close();

      assertCondition(check == 12777);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setInt() - Set an NUMERIC parameter, when the value is too big
   **/
  public void Var016() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_NUMERIC_50) VALUES (?)");
      ps.setInt(1, 2147483647);
      ps.execute();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        assertSqlException(e, -99999, "07006", "Data type mismatch",
            "Mismatch instead of truncation now in toolbox");

      } else {

        DataTruncation dt = (DataTruncation) e;
        assertCondition((dt.getIndex() == 1) && (dt.getParameter() == true)
            && (dt.getRead() == false) && (dt.getTransferSize() == 5));
      }
    }
  }

  /**
   * setInt() - Set an CHAR(50) parameter.
   **/
  public void Var017() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_50) VALUES (?)");
      ps.setInt(1, 1847);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_CHAR_50 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      int check = rs.getInt(1);
      rs.close();

      assertCondition(check == 1847);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setInt() - Set an CHAR(1) parameter.
   **/
  public void Var018() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_1) VALUES (?)");
      ps.setInt(1, 2);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_CHAR_1 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      int check = rs.getInt(1);
      rs.close();

      assertCondition(check == 2);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setInt() - Set an CHAR(1) parameter, when the value is too big.
   **/
  public void Var019() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_1) VALUES (?)");
      ps.setInt(1, -223);
      ps.execute();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
    }
  }

  /**
   * setInt() - Set an VARCHAR parameter.
   **/
  public void Var020() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_VARCHAR_50) VALUES (?)");
      ps.setInt(1, -7367);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      int check = rs.getInt(1);
      rs.close();

      assertCondition(check == -7367);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * setInt() - Set a CLOB parameter.
   **/
  public void Var021() {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CLOB) VALUES (?)");
        ps.setInt(1, -223);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setInt() - Set a DBCLOB parameter.
   **/
  public void Var022() {
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DBCLOB) VALUES (?)");
        ps.setInt(1, -52);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setInt() - Set a BINARY parameter.
   **/
  public void Var023() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BINARY_20) VALUES (?)");
      ps.setInt(1, 554);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setInt() - Set a VARBINARY parameter.
   **/
  public void Var024() {
    try {
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");
      ps.setInt(1, 1934);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setInt() - Set a BLOB parameter.
   **/
  public void Var025() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BLOB) VALUES (?)");
      ps.setInt(1, 5544);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setInt() - Set a DATE parameter.
   **/
  public void Var026() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DATE) VALUES (?)");
      ps.setInt(1, 0);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setInt() - Set a TIME parameter.
   **/
  public void Var027() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIME) VALUES (?)");
      ps.setInt(1, 354);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setInt() - Set a TIMESTAMP parameter.
   **/
  public void Var028() {
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIMESTAMP) VALUES (?)");
      ps.setInt(1, -34543);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setInt() - Set a DATALINK parameter.
   **/
  public void Var029() {
    if (checkDatalinkSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DATALINK) VALUES (?)");
        ps.setInt(1, 755);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setInt() - Set a DISTINCT parameter.
   **/
  // @C0C
  public void Var030() {
    if (checkLobSupport()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DISTINCT) VALUES (?)");
        ps.setInt(1, -7);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_DISTINCT FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        int check = rs.getInt(1);
        rs.close();

        assertCondition(check == -7);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setInt() - Set a BIGINT parameter.
   **/
  public void Var031() {
    if (checkBigintSupport()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BIGINT) VALUES (?)");
        ps.setInt(1, 104613);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_BIGINT FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        int check = rs.getInt(1);
        rs.close();

        assertCondition(check == 104613);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setInt() - Set a parameter in a statement that comes from the package
   * cache.
   * 
   * SQL400 - JDSetupPackage cache doesn't have enough information to be driver
   * neutral today.
   **/
  public void Var032() {
    try {
      String insert = "INSERT INTO " + JDPSTest.PSTEST_SET
          + " (C_INTEGER) VALUES (?)";

      if (isToolboxDriver())
        JDSetupPackage.prime(systemObject_,  PACKAGE,
            JDPSTest.COLLECTION, insert);
      else
        JDSetupPackage.prime(systemObject_, encryptedPassword_,  PACKAGE,
            JDPSTest.COLLECTION, insert, "", getDriver());

      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      Connection c2 = testDriver_.getConnection(baseURL_
          + ";extended dynamic=true;package=" + PACKAGE + ";package library="
          + JDPSTest.COLLECTION + ";package cache=true", userId_, encryptedPassword_);
      PreparedStatement ps = c2.prepareStatement(insert);
      ps.setInt(1, -1213);
      ps.executeUpdate();
      ps.close();
      c2.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      int check = rs.getInt(1);
      rs.close();

      assertCondition(check == -1213);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  // @E1A
  /**
   * setInt() - Attempt to set the return value parameter.
   **/
  public void Var033() {
    if (checkReturnValueSupport()) {
      try {
        PreparedStatement ps = connection_
            .prepareStatement("?=CALL " + JDSetupProcedure.STP_CSRV);
        ps.setInt(1, 444);
        ps.execute();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * // @D1a setInt() - Set a SMALLINT parameter, when the integer is too large,
   * via setInt
   **/
  public void Var034() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
      ps.setString(1, "12345678901234567890");
      ps.execute();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      // In V5R5 native will throw a datatype mismatch
      // ditto on toolbox
      if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE
          || ((isToolboxDriver() || getDriver() == JDTestDriver.DRIVER_NATIVE)
              && getRelease() >= JDTestDriver.RELEASE_V7R1M0)) {
        boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
        if (success) {
          assertCondition(true);
        } else {
          failed(e);
        }

      } else {
        assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
      }
    }
  }

  /**
   * // @D1a setInt() - Should work with a valid parameter index greater than 1.
   **/
  public void Var035() {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
      ps.setString(1, "4342.2");
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_INTEGER FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      int check = rs.getInt(1);
      rs.close();

      assertCondition(check == 4342);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * // @D2a setInt() - Set a SMALLINT parameter, when the integer is too large,
   * via setInt. Exception should be thrown even though the property to suppress
   * them is set since we are setting a numeric value.
   **/
  public void Var036() {
    PreparedStatement ps = null;
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      ps = connectionNoTruncation_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
      ps.setString(1, "12345678901234567890");
      ps.execute();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      // In V5R5 native will throw a datatype mismatch
      // ditto toolbox
      if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE
          || ((isToolboxDriver() || getDriver() == JDTestDriver.DRIVER_NATIVE)
              && getRelease() >= JDTestDriver.RELEASE_V7R1M0)) {
        boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
        if (success) {
          assertCondition(true);
        } else {
          failed(e);
        }

      } else {
        assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
      }
    }

    if (ps != null)
      try {
        ps.close();
      } catch (Exception e) {
      }
  }

  /**
   * // @D2a setInt() - Set an CHAR(1) parameter, when the value is too big. The
   * exception is suppressed since we set the property to suppress truncation
   * exceptions. You can suppress truncation exceptions for string data.
   **/
  public void Var037() {
    PreparedStatement ps = null;
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      ps = connectionNoTruncation_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_1) VALUES (?)");
      ps.setInt(1, -223);
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }

    if (ps != null)
      try {
        ps.close();
      } catch (Exception e) {
      }
  }

  /**
   * // @D2a setInt() - Set an DECIMAL parameter, when the value is too big.
   * Exception should be thrown even though the property to suppress them is set
   * since we are setting a numeric value.
   **/
  public void Var038() {
    PreparedStatement ps = null;
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      ps = connectionNoTruncation_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DECIMAL_50) VALUES (?)");
      ps.setInt(1, -100122134);
      ps.execute();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        assertSqlException(e, -99999, "07006", "Data type mismatch",
            "Mismatch instead of truncation now in toolbox");

      } else {

        assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
      }
    }

    if (ps != null)
      try {
        ps.close();
      } catch (Exception e) {
      }
  }

  /**
   * // @D2a setInt() - Set an NUMERIC parameter, when the value is too big
   * Exception should be thrown even though the property to suppress them is set
   * since we are setting a numeric value.
   **/
  public void Var039() {
    PreparedStatement ps = null;

    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      ps = connectionNoTruncation_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_NUMERIC_50) VALUES (?)");
      ps.setInt(1, 2147483647);
      ps.execute();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
        assertSqlException(e, -99999, "07006", "Data type mismatch",
            "Mismatch instead of truncation now in toolbox");

      } else {

        DataTruncation dt = (DataTruncation) e;
        assertCondition((dt.getIndex() == 1) && (dt.getParameter() == true)
            && (dt.getRead() == false) && (dt.getTransferSize() == 5));
      }
    }

    if (ps != null)
      try {
        ps.close();
      } catch (Exception e) {
      }
  }

  /**
   * // @D2a setInt() - Query based on an Integer parameter, when the integer is
   * too large. A Warning should be posted even though the property to suppress
   * them is set since we are working with a numeric value.
   **/
  public void Var040() {
    PreparedStatement ps = null;
    // Setup -- put a row in the DB
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      ps = connection_.prepareStatement(
          "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
      ps.setString(1, "123");
      ps.executeUpdate();
      ps.close();
    } catch (Exception e) {
      failed(e, "setup failed");
    }

    try {
      ps = connectionNoTruncation_.prepareStatement("SELECT C_INTEGER FROM "
          + JDPSTest.PSTEST_SET + " WHERE C_INTEGER = ? or C_INTEGER = 123"); // added
                                                                              // "=123"
                                                                              // so
                                                                              // we
                                                                              // get
                                                                              // at
                                                                              // least
                                                                              // one
                                                                              // row
                                                                              // back,
                                                                              // and
                                                                              // no
                                                                              // warning
                                                                              // when
                                                                              // executed

      try {
        ps.setString(1, "12345678901234567890");
        if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
          // This removes warning for V5R4 toolbox case.
          ps.executeQuery();
        }
        if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
          failed("Didn't throw DataTruncation");
          return;
        }
      } catch (Exception e) {
        if (getRelease() >= JDTestDriver.RELEASE_V7R1M0
            || getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
          // In V5R5 native will throw a datatype mismatch
          // ditto toolbox
          if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE || isToolboxDriver()
              || getDriver() == JDTestDriver.DRIVER_NATIVE) {
            boolean success = e.getMessage()
                .indexOf("Data type mismatch") != -1;
            if (success) {
              assertCondition(true);
              return;
            } else {
              failed(e);
              return;
            }

          } else { /* Not jtopenlit, toolbox, native driver */

            if (e instanceof DataTruncation) {
              // System.out.println("SQL Exception occurred");
              // System.out.println("Message : "+ e.getMessage());
              // System.out.println("SQLState: "+
              // ((SQLException)e).getSQLState());
              // System.out.println("SQLCode: "+
              // ((SQLException)e).getErrorCode());
              succeeded("got DataTruncation as expected");
              return;
            } else {
              throw e;
            }
          }
        } /* release >= V7R1M0 */
      }

      SQLWarning warnings = ps.getWarnings();

      if (warnings == null) {
        if (getDriver() != JDTestDriver.DRIVER_JCC) {
          failed("No warnings after string too long");
          ps.close();
          return;
        }
      } else {

        //
        // For JDK 1.6, the default java.sql.DataTrucation warning will return
        // a state of 22001 when read is set to false. In this case, it isn't
        // a read so we will have the expected warning.
        //
        String expectedWarning = "01004";

        if (getJDK() >= 160) { // isJdbc40()) {
          expectedWarning = "22001";
        }

        if (!(warnings.getSQLState().startsWith(expectedWarning))) {
          failed("SQLState wrong.  Expected " + expectedWarning + ".  Received "
              + warnings.getSQLState());
          ps.close();
          return;
        }

        if (warnings.getNextWarning() != null) {
          failed("Too many warnings in list");
          ps.close();
          return;
        }
      }
      ResultSet rs = ps.executeQuery();

      warnings = ps.getWarnings();

      if (warnings != null) {
        failed("Warnings not cleared after execute query");
        rs.close();
        ps.close();
        return;
      }
      rs.close();
      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }

    if (ps != null)
      try {
        ps.close();
      } catch (Exception e) {
      }
  }

  /*
   * Query from a SMALLINT parm where the INT is too big
   */
  public void Var041() {
    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
      String added = " -- added 06/17/2006 by native driver";
      String tableName = JDPSTest.COLLECTION + ".JDPSSI041";
      PreparedStatement ps = null;

      try {
        statement_.executeUpdate("DROP TABLE " + tableName);
      } catch (Exception e) {

      }

      try {
        statement_.executeUpdate("CREATE TABLE " + tableName + "(c1 smallint)");
        statement_.executeUpdate("INSERT INTO " + tableName + " VALUES(0)");
        statement_.executeUpdate("INSERT INTO " + tableName + " VALUES(1)");

        ps = connection_
            .prepareStatement("Select * from " + tableName + " where c1=?");
        ps.setInt(1, 2147483647);
        SQLWarning warn = ps.getWarnings();
        if (warn != null) {
          System.out.println("warning=" + warn.toString() + " STATE="
              + warn.getSQLState() + " CODE=" + warn.getErrorCode());
        }
        ps.executeQuery();
        warn = ps.getWarnings();
        if (warn != null) {
          System.out.println("warning=" + warn.toString() + " STATE="
              + warn.getSQLState() + " CODE=" + warn.getErrorCode());
        }
        failed("Didn't throw SQLException " + added);
      } catch (Exception e) {
        boolean passed = e.getMessage().indexOf("Data type mismatch") != -1;
        if (passed) {
          assertCondition(passed, "passed");
        } else {
          failed(e, "got " + e
              + " expected data type mismatch for all drivers.  Updated 7/16/2014");
        }

      }
      try {
        statement_.executeUpdate("DROP TABLE " + tableName);
      } catch (Exception e2) {

      }
    } else {
      notApplicable("V5R5 and later 'numeric truncation'");
    }

  }

  /**
   * setInt() - Set an DECFLOAT16 parameter.
   **/
  public void Var042() {
    if (checkDecFloatSupport()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SETDFP16);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SETDFP16 + "  VALUES (?)");
        ps.setInt(1, -10012);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDPSTest.PSTEST_SETDFP16);
        rs.next();
        int check = rs.getInt(1);
        rs.close();

        assertCondition(check == -10012);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setInt() - Set an DECFLOAT34 parameter.
   **/
  public void Var043() {
    if (checkDecFloatSupport()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SETDFP34);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SETDFP34 + "  VALUES (?)");
        ps.setInt(1, -10012);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT * FROM " + JDPSTest.PSTEST_SETDFP34);
        rs.next();
        int check = rs.getInt(1);
        rs.close();

        assertCondition(check == -10012);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * setInt() - Set a SQLXML parameter.
   **/
  public void Var044() {
    if (checkXmlSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SETXML + " VALUES (?)");
        try {
          ps.setInt(1, 755);

          ps.executeUpdate();
          ps.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setInt() - Set an parameter for a column of a specified type.
   **/
  public void testSetInt(String columnName, int value, String outputValue) {
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (" + columnName + ") VALUES (?)");
      ps.setInt(1, value);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_.executeQuery(
          "SELECT " + columnName + " FROM " + JDPSTest.PSTEST_SET);
      rs.next();
      String check = "" + rs.getString(1);
      rs.close();

      assertCondition(outputValue.equals(check),
          " got " + check + " sb " + outputValue+" from "+value);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  public void Var045() {
    if (checkBooleanSupport()) {
      testSetInt("C_BOOLEAN", 1, "1");
    }
  }

  public void Var046() {
    if (checkBooleanSupport()) {
      testSetInt("C_BOOLEAN", 0, "0");
    }
  }

  public void Var047() {
    if (checkBooleanSupport()) {
      testSetInt("C_BOOLEAN", 101, "1");
    }
  }

  public void Var048() {
    if (checkBooleanSupport()) {
      testSetInt("C_BOOLEAN", -1, "1");
    }
  }
  
  public void Var049() {
    if (checkBooleanSupport()) {
      testSetInt("C_BOOLEAN", Integer.MAX_VALUE, "1");
    }
  }

  public void Var050() {
    if (checkBooleanSupport()) {
      testSetInt("C_BOOLEAN", Integer.MIN_VALUE, "1");
    }
  }

}
