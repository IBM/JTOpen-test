///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetArray.java
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
// File Name:    JDPSSetArray.java
//
// Classes:      JDPSSetArray
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

import java.io.FileOutputStream;
import java.io.Serializable;
import java.sql.Array;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Types;
import java.util.Hashtable;
import java.util.Map;

/**
 * Testcase JDPSSetArray. This tests the following method of the JDBC
 * PreparedStatement class:
 * 
 * <ul>
 * <li>setArray()
 * </ul>
 **/
public class JDPSSetArray extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetArray";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }

  // Private data.
  private Connection connection_;

  /**
   * Constructor.
   **/
  public JDPSSetArray(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDPSSetArray", namesAndVars, runMode, fileOutputStream,
 password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    String url = baseURL_ ;
    connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    connection_.close();
  }

  /**
   * Test implementation of Array for use in testing.
   **/
  private static class TestArray implements Array, Serializable {
    public String getBaseTypeName() throws SQLException {
      return null;
    }

    public int getBaseType() throws SQLException {
      return Types.OTHER;
    }

    public Object getArray() throws SQLException {
      return new Object[0];
    }

    public Object getArray(Map map) throws SQLException {
      return new Object[0];
    }

    public Object getArray(long index, int count) throws SQLException {
      return new Object[0];
    }

    public Object getArray(long index, int count, Map map) throws SQLException {
      return new Object[0];
    }

    public ResultSet getResultSet() throws SQLException {
      return null;
    }

    public ResultSet getResultSet(Map map) throws SQLException {
      return null;
    }

    public ResultSet getResultSet(long index, int count) throws SQLException {
      return null;
    }

    public ResultSet getResultSet(long index, int count, Map map)
        throws SQLException {
      return null;
    }

    public void free() {

    }
  }

  /**
   * setArray() - Should throw exception when the prepared statement is closed.
   **/
  public void Var001() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
        ps.close();
        ps.setArray(1, new TestArray());
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setArray() - Should throw exception when an invalid index is specified.
   **/
  public void Var002() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
        ps.setArray(100, new TestArray());
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setArray() - Should throw exception when index is 0.
   **/
  public void Var003() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
        ps.setArray(0, new TestArray());
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setArray() - Should throw exception when index is -1.
   **/
  public void Var004() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_INTEGER, C_SMALLINT) VALUES (?, ?)");
        ps.setArray(0, new TestArray());
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setArray() - Should throw exception when the value is null.
   **/
  public void Var005() {

    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
        ps.setArray(1, null);
        ps.close();

        if (getRelease() >= JDTestDriver.RELEASE_V7R1M0
            && getDriver() == JDTestDriver.DRIVER_NATIVE) {
          assertCondition(true, "Valid to set null value for array");
        } else {
          failed(
              "PS.setArray didn't throw SQLException when value is null and type is INTEGER");
        }
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setArray() - Should throw an exception with a valid parameter index greater
   * than 1.
   **/
  public void Var006() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_KEY, C_VARCHAR_50) VALUES (?, ?)");
        ps.setArray(2, new TestArray());
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setArray() - Should throw exception when the parameter is not an input
   * parameter.
   **/
  public void Var007() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
        ps.setArray(2, new TestArray());
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  public void testSetFailed(String columnName, Array inArray) {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (" + columnName + ") VALUES (?)");
        ps.setArray(1, inArray);
        ps.executeUpdate();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * setArray() - Set a SMALLINT parameter.
   **/
  public void Var008() {
    testSetFailed("C_SMALLINT", new TestArray());

  }

  /**
   * setArray() - Set a INTEGER parameter.
   **/
  public void Var009() {
    testSetFailed("C_INTEGER", new TestArray());

  }

  /**
   * setArray() - Set a REAL parameter.
   **/
  public void Var010() {
    testSetFailed("C_REAL", new TestArray());
  }

  /**
   * setArray() - Set a FLOAT parameter.
   **/
  public void Var011() {
    testSetFailed("C_FLOAT", new TestArray());
  }

  /**
   * setArray() - Set a DOUBLE parameter.
   **/
  public void Var012() {
    testSetFailed("C_DOUBLE", new TestArray());
  }

  /**
   * setArray() - Set a DECIMAL parameter.
   **/
  public void Var013() {
    testSetFailed("C_DECIMAL_105", new TestArray());
  }

  /**
   * setArray() - Set a NUMERIC parameter.
   **/
  public void Var014() {
    testSetFailed("C_NUMERIC_50", new TestArray());
  }

  /**
   * setArray() - Set a CHAR parameter.
   **/
  public void Var015() {
    testSetFailed("C_CHAR_50", new TestArray());
  }

  /**
   * setArray() - Set a VARCHAR parameter.
   **/
  public void Var016() {
    testSetFailed("C_VARCHAR_50", new TestArray());
  }

  /**
   * setArray() - Set a CLOB parameter.
   **/
  public void Var017() {
    if (checkLobSupport()) {
      testSetFailed("C_CLOB", new TestArray());
    }
  }

  /**
   * setArray() - Set a DBCLOB parameter.
   **/
  public void Var018() {
    if (checkLobSupport()) {
      testSetFailed("C_DBCLOB", new TestArray());
    }
  }

  /**
   * setArray() - Set a BINARY parameter.
   **/
  public void Var019() {
    testSetFailed("C_BINARY_20", new TestArray());
  }

  /**
   * setArray() - Set a VARBINARY parameter.
   **/
  public void Var020() {
    testSetFailed("C_VARBINARY_20", new TestArray());
  }

  /**
   * setArray() - Set a BLOB parameter.
   **/
  public void Var021() {
    testSetFailed("C_BLOB", new TestArray());
  }

  /**
   * setArray() - Set a DATE parameter.
   **/
  public void Var022() {
    testSetFailed("C_DATE", new TestArray());
  }

  /**
   * setArray() - Set a TIME parameter.
   **/
  public void Var023() {
    testSetFailed("C_TIME", new TestArray());
  }

  /**
   * setArray() - Set a TIMESTAMP parameter.
   **/
  public void Var024() {
    testSetFailed("C_TIMESTAMP", new TestArray());
  }

  /**
   * setArray() - Set a DATALINK parameter.
   **/
  public void Var025() {
    if (checkDatalinkSupport()) {
      testSetFailed("C_DATALINK", new TestArray());
    }

  }

  /**
   * setArray() - Set a DISTINCT parameter.
   **/
  public void Var026() {
    if (checkLobSupport()) {
      testSetFailed("C_DISTINCT", new TestArray());
    }
  }

  /**
   * setArray() - Set a BIGINT parameter.
   **/
  public void Var027() {
    if (checkBigintSupport()) {
      testSetFailed("C_BIGINT", new TestArray());
    }
  }

  /**
   * setArray() - Set a DECFLOAT(16) parameter.
   **/
  public void Var028() {
    if (checkDecFloatSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SETDFP16 + " VALUES (?)");
        ps.setArray(1, new TestArray());
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setArray() - Set a DECFLOAT(34) parameter.
   **/
  public void Var029() {
    if (checkDecFloatSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SETDFP34 + " VALUES (?)");
        ps.setArray(1, new TestArray());
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setArray() - Set an SQLXML parameter.
   **/
  public void Var030() {
    String added = " -- added by native driver 08/21/2009";
    if (checkXmlSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SETXML + " VALUES (?)");

        try {
          ps.setArray(1, new TestArray());
          failed("Didn't throw SQLException" + added);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + added);
      }
    }
  }

  /**
   * setArray() - Set a BIGINT parameter.
   **/
  public void Var031() {
    if (checkBooleanSupport()) {
      testSetFailed("C_BOOLEAN", new TestArray());
    }
  }

}
