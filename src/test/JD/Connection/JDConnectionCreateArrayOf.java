///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionCreateArrayOf.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionCreateArrayOf.java
//
// Classes:      JDConnectionCreateArrayOf
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDLobTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDTestUtilities;
import test.JDLobTest.JDTestBlob;
import test.JDLobTest.JDTestClob;

import java.awt.TextArea;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Hashtable;
import java.sql.*;

/**
 * Testcase JDConnectionCreateArrayOf. This tests the following methods of the
 * JDBC Connection class:
 * 
 * <ul>
 * <li>CreateArrayOf()
 * </ul>
 * 
 * This will be called via reflection because the method will be available in
 * V7R1 in all JDK levels.
 * 
 * This tests the type conversions listed in Table B-5 of the JDBC 4.0
 * specification for the following SQLTYPES
 * 
 * CHAR (Vars 1-20) VARCHAR (Vars 21-40) SMALLINT (Vars 41-60) INTEGER (Vars
 * 61-80) BIGINT (Vars 81-100) REAL (Vars 101-120) FLOAT (Vars 121-140) DOUBLE
 * (Vars 141-160) DECIMAL (Vars 161-180) NUMERIC (Vars 181-200) DECFLOAT (Vars
 * 201-220) BOOLEAN (vars 221-240) BINARY VARBINARY DATE TIME TIMESTAMP BLOB
 * CLOB NCHAR NVARCHAR NCLOB SQLXML
 * 
 * 
 * Testcases to add..
 * 
 * -------------------------- Date Format testcases Time format testcases
 * 
 **/
public class JDConnectionCreateArrayOf extends JDTestcase {

  // Private data.
  private Connection connection_;
  private Statement stmt_;
  private String collection = "JDCONTST";

  public final static String TYPE_BASE = "JDCCAT";
  public final static String PROCEDURE_BASE = "JDCCAP";

  public final static String CHAR30_PROCEDURE = "char30";
  public final static String CHAR30_ARRAY_TYPE = "CHAR(30)";

  public final static String VARCHAR30_PROCEDURE = "varchar30";
  public final static String VARCHAR30_ARRAY_TYPE = "VARCHAR(30)";

  public final static String SMALLINT_PROCEDURE = "smallint";
  public final static String SMALLINT_ARRAY_TYPE = "SMALLINT";

  public final static String INTEGER_PROCEDURE = "integer";
  public final static String INTEGER_ARRAY_TYPE = "INT";

  public final static String BIGINT_PROCEDURE = "bigint";
  public final static String BIGINT_ARRAY_TYPE = "BIGINT";

  public final static String REAL_PROCEDURE = "REAL";
  public final static String REAL_ARRAY_TYPE = "REAL";

  public final static String FLOAT_PROCEDURE = "FLOAT";
  public final static String FLOAT_ARRAY_TYPE = "FLOAT";

  public final static String DOUBLE_PROCEDURE = "DOUBLE";
  public final static String DOUBLE_ARRAY_TYPE = "DOUBLE";

  public final static String DECIMAL_PROCEDURE = "DECIMAL92";
  public final static String DECIMAL_ARRAY_TYPE = "DECIMAL(9,2)";

  public final static String NUMERIC_PROCEDURE = "NUMERIC92";
  public final static String NUMERIC_ARRAY_TYPE = "NUMERIC(9,2)";

  public final static String DECFLOAT_PROCEDURE = "DECFLOAT34";
  public final static String DECFLOAT_ARRAY_TYPE = "DECFLOAT(34)";

  public final static String BOOLEAN_PROCEDURE = "BOOLEAN";
  public final static String BOOLEAN_ARRAY_TYPE = "BOOLEAN";

  private Hashtable createdProcedures = new Hashtable();

  /**
   * Constructor.
   **/
  public JDConnectionCreateArrayOf(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDConnectionCreateArrayOf", namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   * Variables for tests
   **/
  String[] stringElements = { "1", "2", "3", "4", null, "6", "7", "8", "-9",
      "0" };

  String[] char30elements = { "1                             ",
      "2                             ", "3                             ",
      "4                             ", null, "6                             ",
      "7                             ", "8                             ",
      "-9                            ", "0                             " };

  String[] booleanChar30Elements = { "true                          ",
      "false                         ", "true                          ",
      "false                         ", null, "false                         ",
      "true                          ", "false                         ",
      "true                          ", "false                         " };

  String[] luwBooleanChar30Elements = { "1                             ",
      "0                             ", "1                             ",
      "0                             ", null, "0                             ",
      "1                             ", "0                             ",
      "1                             ", "0                             " };

  String[] floatChar30Elements = { "1.0                           ",
      "2.0                           ", "3.0                           ",
      "4.0                           ", null, "6.0                           ",
      "7.0                           ", "8.0                           ",
      "-9.0                          ", "0.0                           " };

  String[] dateChar30Elements = { "2001-01-01                    ",
      "2002-02-02                    ", "2003-03-03                    ",
      "2004-04-04                    ", null, "2006-06-06                    ",
      "2007-07-07                    ", "2008-08-08                    ",
      "2009-09-09                    ", "2010-10-10                    ", };

  String[] timeChar30Elements = { "01:01:00                      ",
      "02:02:00                      ", "03:03:00                      ",
      "04:04:00                      ", null, "06:06:00                      ",
      "07:07:00                      ", "08:08:00                      ",
      "09:09:00                      ", "10:10:00                      ", };

  String[] timestampChar30Elements = { "2001-01-01 01:01:01.0001      ",
      "2002-02-02 02:02:02.0001      ", "2003-03-03 03:03:03.0001      ",
      "2004-04-04 04:04:04.0001      ", null, "2006-06-06 06:06:06.0001      ",
      "2007-07-07 07:07:07.0001      ", "2008-08-08 08:08:08.0001      ",
      "2009-09-09 09:09:09.0001      ", "2010-10-10 10:10:10.0001      ", };

  String[] toolboxTimestampChar30Elements = { "2001-01-01 01:01:01.0001      ",
      "2002-02-02 02:02:02.0001      ", "2003-03-03 03:03:03.0001      ",
      "2004-04-04 04:04:04.0001      ", null, "2006-06-06 06:06:06.0001      ",
      "2007-07-07 07:07:07.0001      ", "2008-08-08 08:08:08.0001      ",
      "2009-09-09 09:09:09.0001      ", "2010-10-10 10:10:10.0001      ", };

  String[] byteArrayChar30Elements = { "01                            ",
      "02                            ", "03                            ",
      "04                            ", null, "06                            ",
      "07                            ", "08                            ",
      "09                            ", "00                            "

  };

  String[] booleanStringElements = { "true", "false", "true", "false", null,
      "false", "true", "false", "true", "false" };

  String[] luwBooleanStringElements = { "1", "0", "1", "0", null, "0", "1", "0",
      "1", "0" };

  String[] floatBooleanStringElements = { "1.0", "0.0", "1.0", "0.0", null,
      "0.0", "1.0", "0.0", "1.0", "0.0" };

  String[] bigDecimalBooleanStringElements = { "1.00", "0.00", "1.00", "0.00",
      null, "0.00", "1.00", "0.00", "1.00", "0.00" };

  String[] floatStringElements = { "1.0", "2.0", "3.0", "4.0", null, "6.0",
      "7.0", "8.0", "-9.0", "0.0" };

  String[] bigDecimalStringElements = { "1.00", "2.00", "3.00", "4.00", null,
      "6.00", "7.00", "8.00", "-9.00", "0.00" };

  String[] dateStringElements = { "2001-01-01", "2002-02-02", "2003-03-03",
      "2004-04-04", null, "2006-06-06", "2007-07-07", "2008-08-08",
      "2009-09-09", "2010-10-10", };

  String[] timeStringElements = { "01:01:00", "02:02:00", "03:03:00",
      "04:04:00", null, "06:06:00", "07:07:00", "08:08:00", "09:09:00",
      "10:10:00", };

  String[] timestampStringElements = { "2001-01-01 01:01:01.0001",
      "2002-02-02 02:02:02.0001", "2003-03-03 03:03:03.0001",
      "2004-04-04 04:04:04.0001", null, "2006-06-06 06:06:06.0001",
      "2007-07-07 07:07:07.0001", "2008-08-08 08:08:08.0001",
      "2009-09-09 09:09:09.0001", "2010-10-10 10:10:10.0001", };

  // On 7/22/2010 Changed from .0001 to .000100
  // as seen by Var 37 on minibld2 (V7R2).

  String[] toolboxTimestampStringElements = { "2001-01-01 01:01:01.0001",
      "2002-02-02 02:02:02.0001", "2003-03-03 03:03:03.0001",
      "2004-04-04 04:04:04.0001", null, "2006-06-06 06:06:06.0001",
      "2007-07-07 07:07:07.0001", "2008-08-08 08:08:08.0001",
      "2009-09-09 09:09:09.0001", "2010-10-10 10:10:10.0001", };

  String[] byteArrayStringElements = { "01", "02", "03", "04", null, "06", "07",
      "08", "09", "00" };

  String[] blobStringElements = { "01", "02", "03", "04", null, "06", "07",
      "08", "09", "00" };

  Time[] timeElements = { Time.valueOf("01:01:00"), Time.valueOf("02:02:00"),
      Time.valueOf("03:03:00"), Time.valueOf("04:04:00"), null,
      Time.valueOf("06:06:00"), Time.valueOf("07:07:00"),
      Time.valueOf("08:08:00"), Time.valueOf("09:09:00"),
      Time.valueOf("10:10:00"), };

  Boolean[] booleanElements = { new Boolean(true), new Boolean(false),
      new Boolean(true), new Boolean(false), null, new Boolean(false),
      new Boolean(true), new Boolean(false), new Boolean(true),
      new Boolean(false) };

  BigDecimal[] bigDecimalElements = { new BigDecimal("1"), new BigDecimal("2"),
      new BigDecimal("3"), new BigDecimal("4"), null, new BigDecimal("6"),
      new BigDecimal("7"), new BigDecimal("8"), new BigDecimal("-9"),
      new BigDecimal("0") };

  Byte[] byteElements = { new Byte((byte) 1), new Byte((byte) 2),
      new Byte((byte) 3), new Byte((byte) 4), null, new Byte((byte) 6),
      new Byte((byte) 7), new Byte((byte) 8), new Byte((byte) -9),
      new Byte((byte) 0) };

  Short[] shortElements = { new Short((short) 1), new Short((short) 2),
      new Short((short) 3), new Short((short) 4), null, new Short((short) 6),
      new Short((short) 7), new Short((short) 8), new Short((short) -9),
      new Short((short) 0) };

  String[] booleanFromShortStringElements = {"true","true",
      "true","true",null,"true",
      "true","true","true","false"}; 
  
  Integer[] integerElements = { new Integer(1), new Integer(2), new Integer(3),
      new Integer(4), null, new Integer(6), new Integer(7), new Integer(8),
      new Integer(-9), new Integer(0) };

  Long[] longElements = { new Long(1), new Long(2), new Long(3), new Long(4),
      null, new Long(6), new Long(7), new Long(8), new Long(-9), new Long(0) };

  Float[] floatElements = { new Float(1), new Float(2), new Float(3),
      new Float(4), null, new Float(6), new Float(7), new Float(8),
      new Float(-9), new Float(0) };

  Double[] doubleElements = { new Double(1), new Double(2), new Double(3),
      new Double(4), null, new Double(6), new Double(7), new Double(8),
      new Double(-9), new Double(0) };

  Date[] dateElements = { Date.valueOf("2001-01-01"),
      Date.valueOf("2002-02-02"), Date.valueOf("2003-03-03"),
      Date.valueOf("2004-04-04"), null, Date.valueOf("2006-06-06"),
      Date.valueOf("2007-07-07"), Date.valueOf("2008-08-08"),
      Date.valueOf("2009-09-09"), Date.valueOf("2010-10-10"), };

  Timestamp[] timestampElements = {
      Timestamp.valueOf("2001-01-01 01:01:01.000100000"),
      Timestamp.valueOf("2002-02-02 02:02:02.000100000"),
      Timestamp.valueOf("2003-03-03 03:03:03.000100000"),
      Timestamp.valueOf("2004-04-04 04:04:04.000100000"), null,
      Timestamp.valueOf("2006-06-06 06:06:06.000100000"),
      Timestamp.valueOf("2007-07-07 07:07:07.000100000"),
      Timestamp.valueOf("2008-08-08 08:08:08.000100000"),
      Timestamp.valueOf("2009-09-09 09:09:09.000100000"),
      Timestamp.valueOf("2010-10-10 10:10:10.000100000"), };

  byte[][] byteArrayElements = { { (byte) 1 }, { (byte) 2 }, { (byte) 3 },
      { (byte) 4 }, null, { (byte) 6 }, { (byte) 7 }, { (byte) 8 },
      { (byte) 9 }, { (byte) 10 } };

  byte[] blob1 = { 1 };
  byte[] blob2 = { 2 };
  byte[] blob3 = { 3 };
  byte[] blob4 = { 4 };

  byte[] blob6 = { 6 };
  byte[] blob7 = { 7 };
  byte[] blob8 = { 8 };
  byte[] blob9 = { 9 };
  byte[] blob10 = { 0 };

  Blob[] blobElements = { new JDLobTest.JDTestBlob(blob1),
      new JDLobTest.JDTestBlob(blob2), new JDLobTest.JDTestBlob(blob3),
      new JDLobTest.JDTestBlob(blob4), null, new JDLobTest.JDTestBlob(blob6),
      new JDLobTest.JDTestBlob(blob7), new JDLobTest.JDTestBlob(blob8),
      new JDLobTest.JDTestBlob(blob9), new JDLobTest.JDTestBlob(blob10), };

  Clob[] clobElements = { new JDLobTest.JDTestClob("1"),
      new JDLobTest.JDTestClob("2"), new JDLobTest.JDTestClob("3"),
      new JDLobTest.JDTestClob("4"), null, new JDLobTest.JDTestClob("6"),
      new JDLobTest.JDTestClob("7"), new JDLobTest.JDTestClob("8"),
      new JDLobTest.JDTestClob("-9"), new JDLobTest.JDTestClob("0"), };

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {

    connection_ = testDriver_.getConnection(baseURL_ + ";date format=jis",
        userId_, encryptedPassword_);

    collection = JDConnectionTest.COLLECTION;

    stmt_ = connection_.createStatement();

    if (areArraysSupported()) {

    }

  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {

    if (areArraysSupported()) {

      // Drop all the procedures and all the types used by this testcase
      dropProcedure(CHAR30_PROCEDURE);
      dropType(CHAR30_PROCEDURE);

    }

    stmt_.close();
    connection_.close();
  }

  void dropProcedure(String name) {
    String procedureName = collection + "." + PROCEDURE_BASE + name;
    executeExpectedException(stmt_, "DROP PROCEDURE " + procedureName,
        "not found", -204);

  }

  void dropType(String name) {
    String typeName = collection + "." + TYPE_BASE + name;
    executeExpectedException(stmt_, "DROP TYPE " + typeName, "not found", -204);

  }

  void executeExpectedException(Statement stmt, String sql,
      String expectedException, int sqlcode) {
    try {
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      String message = e.toString();
      if ((message.indexOf(expectedException) < 0)
          && (sqlcode != e.getErrorCode())) {
        System.out.println("Warning: Unexpected exception from " + sql);
        e.printStackTrace();
      }
    }

  }

  void verifyExistsInputOutputProcedure(String typeDefinition, String name)
      throws SQLException {
    String existString = (String) createdProcedures.get(name);
    if (existString == null) {
      String sql = "";
      try {
        String typeName = collection + "." + TYPE_BASE + name;
        String procedureName = collection + "." + PROCEDURE_BASE + name;

        executeExpectedException(stmt_, "DROP PROCEDURE " + procedureName,
            "not found", -204);
        executeExpectedException(stmt_, "DROP TYPE " + typeName, "not found",
            -204);

        sql = "CREATE TYPE " + typeName + " AS " + typeDefinition
            + " ARRAY[100]";
        stmt_.executeUpdate(sql);

        sql = "CREATE PROCEDURE " + procedureName + "  (in arrIn " + typeName
            + ", out arrOut  " + typeName + ") " + " begin  "
            + "set arrOut = arrIn; " + " end ";

        stmt_.executeUpdate(sql);

        createdProcedures.put(name, typeDefinition);
      } catch (SQLException e) {
        System.out.println(
            "Exception in verifyExistsInputOutputProcedure sql=" + sql);
        throw e;
      }
    } else {
      if (!existString.equals(typeDefinition)) {
        throw new SQLException("Testcase error.  typeDefinition of "
            + typeDefinition + " does not match previous definition of "
            + existString + " for procedure " + name);
      }
    }

  }

  Array callInputOutputProcedure(String typeDefinition, String procedure,
      Array inputArray) throws SQLException {
    verifyExistsInputOutputProcedure(typeDefinition, procedure);
    String sql = "";
    Array outArray = null;
    try {
      String procedureName = collection + "." + PROCEDURE_BASE + procedure;
      sql = "CALL " + procedureName + "(?,?)";
      CallableStatement cstmt = connection_.prepareCall(sql);

      cstmt.setArray(1, inputArray);
      cstmt.registerOutParameter(2, Types.ARRAY);

      cstmt.execute();

      outArray = cstmt.getArray(2);

      cstmt.close();

    } catch (SQLException e) {
      System.out.println("Exception in callInputOutputProcedure sql=" + sql);
      throw e;

    }
    return outArray;

  }

  boolean compareArrays(String[] expected, Object[] elements,
      String expectedArrayClass, String errorMessage, StringBuffer sb) {
    boolean passed = true;
    Class arrayClass = elements.getClass().getComponentType();
    String arrayTypeName = arrayClass.getName();
    if (!arrayTypeName.equals(expectedArrayClass)) {
      sb.append("\n " + errorMessage + ": Got array type " + arrayTypeName
          + " sb " + expectedArrayClass);
      passed = false;
    }
    if (expected.length == elements.length) {
      for (int i = 0; i < expected.length; i++) {
        String stringElement;
        if (elements[i] == null) {
          if (expected[i] != null) {
            sb.append("\n" + errorMessage + ": For element " + i + " "
                + "got NULL " + "sb '" + expected[i] + "'");
            passed = false;
          }
        } else {
          if (elements[i] instanceof java.sql.Blob) {
            try {
              java.sql.Blob blob = (java.sql.Blob) elements[i];
              byte[] bytes = blob.getBytes(1, (int) blob.length());
              stringElement = JDTestUtilities.dumpBytes(bytes);
            } catch (SQLException e) {
              stringElement = e.toString();
            }
          } else if (elements[i] instanceof byte[]) {
            stringElement = JDTestUtilities.dumpBytes((byte[]) elements[i]);
          } else if (elements[i] instanceof java.sql.Clob) {
            try {
              Clob clob = (Clob) elements[i];
              int length = (int) clob.length();
              if (length > 0) {
                stringElement = clob.getSubString(1, length);
              } else {
                stringElement = "Clob of length " + length;
              }
            } catch (SQLException e) {
              stringElement = e.toString();
            }
          } else {
            stringElement = elements[i].toString();
          }
          if (expected[i] == null) {
            sb.append("\n" + errorMessage + ": For element " + i + " " + "got '"
                + stringElement + "' " + "sb NULL");
            passed = false;
          } else {
            if (!expected[i].equals(stringElement)) {
              sb.append(
                  "\n" + errorMessage + ": For element " + i + " " + "got '"
                      + stringElement + "' " + "sb '" + expected[i] + "'");
              passed = false;
            }
          }
        }
      }
    } else {
      sb.append("\n" + errorMessage + ": Got size " + elements.length + " sb "
          + expected.length);
      passed = false;
    }

    return passed;
  }

  /**
   * CreateArrayOf() -- Create an empty array of type CHAR
   **/

  public void Var001() {
    String description = " -- Create empty array of type CHAR -- Added 03/16/2009 ";
    if (checkArraySupport()) {

      try {

        String[] elements = new String[0];
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "CHAR", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(elementsOut.length == 0,
            "Length is " + elementsOut.length + " sb 0 " + description);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var002() {
    String description = " -- Create 1 element array of type CHAR -- Added 03/16/2009 ";
    if (checkArraySupport()) {

      try {

        String[] elements = { "Hello Charles" };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "CHAR", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(
            elementsOut.length == 1 && elements[0].equals(elementsOut[0]),
            "Length is " + elementsOut.length + " sb 0 " + "elements[0]='"
                + elementsOut[0] + "' " + "sb '" + elements[0] + "' "
                + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var003() {
    String description = " -- Create 10 element array of type CHAR -- Note:  JCC fails this test -- Added 03/16/2009 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        String[] elements = { "Hello Charles", "Element 2", "Third Element 3",
            "4", "5", "6", "7", "8", "9", "10" };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "CHAR", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed = compareArrays(elements, elementsOut,
            "java.lang.String", "InputArray", sb);

        assertCondition(passed, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var004() {
    String description = " -- Create 10 element array of type CHAR and make sure array is copy -- Added 03/16/2009 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        String[] elements = { "Hello Charles", "Element 2", "Third Element 3",
            "4", "5", "6", "7", "8", "9", "10" };

        String[] setElements = { "Hello Charles", "Element 2",
            "Third Element 3", "4", "5", "6", "7", "8", "9", "10" };

        if (getDriver() == JDTestDriver.DRIVER_JCC || isToolboxDriver()) {
          notApplicable("jcc/TB doesn't copy array when constructing array");
          return;
        }
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "CHAR", setElements);
        setElements[0] = "I CHANGED THIS";

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed = compareArrays(elements, elementsOut,
            "java.lang.String", "InputArray", sb);

        assertCondition(passed, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var005() {
    String description = " -- Create 10 element array of type CHAR and make sure ouput array is copy -- Added 03/16/2009 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        String[] elements = { "Hello Charles", "Element 2", "Third Element 3",
            "4", "5", "6", "7", "8", "9", "10" };

        String[] setElements = { "Hello Charles", "Element 2",
            "Third Element 3", "4", "5", "6", "7", "8", "9", "10" };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "CHAR", setElements);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        elementsOut[0] = "I CHANGED THIS";

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(arrayObject,
            "getArray");

        boolean passed = compareArrays(elements, elementsOut,
            "java.lang.String", "InputArray", sb);

        assertCondition(passed, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void testTypeCombination(
      String description, String declaredArrayType, Object[] specifiedArray,
      String[] firstCompareArray, /* Comparision after creating array */
      String nativeDriverCompareType, /* Comparision after creating array */
      String toolboxDriverCompareType, /* Comparision after creating array */
      String compareType, /* Comparision after creating array */
      String arrayTypeName, /* array typename for call */
      String procedureName, /* reflect procedure to call */
      String[] secondCompareArray, /* Comparision after call */
      String outputCompareType) {

    if (checkArraySupport()) {
      StringBuffer sb = new StringBuffer();
      try {
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", declaredArrayType, specifiedArray);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");
        boolean passed1;
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          // Native driver does the conversion on the input.
          passed1 = compareArrays(firstCompareArray, elementsOut,
              nativeDriverCompareType,
              "Procedure InputArray after calling CreateArrayOf with declaredArrayType="
                  + declaredArrayType,
              sb);
        } else if (isToolboxDriver()) {
          passed1 = compareArrays(firstCompareArray, elementsOut,
              toolboxDriverCompareType,
              "Procedure InputArray after calling CreateArrayOf with declaredArrayType="
                  + declaredArrayType,
              sb);
        } else {
          passed1 = compareArrays(
				  firstCompareArray, elementsOut, compareType,
              "Procedure InputArray after calling CreateArrayOf with declaredArrayType="
                  + declaredArrayType,
              sb);
        }
        Object outputArray = callInputOutputProcedure(arrayTypeName,
            procedureName, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(secondCompareArray, elementsOut,
            outputCompareType, "OutputArray", sb);



	/* Check the metadata for the array */ 
	ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_O(outputArray,"getResultSet");
	ResultSetMetaData rsmd = rs.getMetaData();
	boolean passed3 = true;
	if (rsmd == null) { 
	    if (isToolboxDriver()) {
		System.out.println("Warning.  For toolbox rs.getMetaData returned null");
	    } else { 
		passed3 = false;
		sb.append("rs.getMetaData returned null");
	    }
	} else { 
	    String typeName = rsmd.getColumnTypeName(2);
	    String expectedArrayTypeName = arrayTypeName;
	    int parenIndex = expectedArrayTypeName.indexOf('(');
	    if (parenIndex > 0) {
		expectedArrayTypeName = expectedArrayTypeName.substring(0,
									parenIndex);
	    }
	    if (expectedArrayTypeName.equals("FLOAT")) expectedArrayTypeName="DOUBLE"; 
	    if (expectedArrayTypeName.equals("DECFLOAT")) expectedArrayTypeName="DECIMAL"; 
	    if (expectedArrayTypeName.equals("INT")) expectedArrayTypeName="INTEGER"; 

	    if (!typeName.equals(expectedArrayTypeName)) {
		passed3 = false; 
		sb.append("rsmd.getColumnTypeName()="+typeName+" sb "+expectedArrayTypeName+"\n"); 
	    }

	    String className = rsmd.getColumnClassName(2);
	    if (!className.equals(outputCompareType)) {
		passed3 = false;
		sb.append("rsmd.getColumnClassName()="+className+" sb "+outputCompareType+"\n"); 
	    }
	}
        assertCondition(passed1 && passed2 && passed3, sb.toString() + description);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + description);
      }
    }

  }

  public void testTypeCombinationException(String description,
      String declaredArrayType, Object[] specifiedArray,
      String[] firstCompareArray, /* Comparision after creating array */
      String nativeDriverCompareType, /* Comparision after creating array */
      String toolboxDriverCompareType, /* Comparision after creating array */
      String compareType, /* Comparision after creating array */
      String arrayTypeName, /* array typename for call */
      String procedureName, /* reflect procedure to call */
      String[] secondCompareArray, /* Comparision after call */
      String outputCompareType, String exceptionString) {
    if (checkArraySupport()) {
      StringBuffer sb = new StringBuffer();
      try {
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", declaredArrayType, specifiedArray);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");
        boolean passed1;
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          // Native driver does the conversion on the input.
          passed1 = compareArrays(firstCompareArray, elementsOut,
              nativeDriverCompareType, "InputArray", sb);
        } else if (isToolboxDriver()) {
          passed1 = compareArrays(firstCompareArray, elementsOut,
              toolboxDriverCompareType, "InputArray", sb);
        } else {
          passed1 = compareArrays(firstCompareArray, elementsOut, compareType,
              "InputArray", sb);
        }
        Object outputArray = callInputOutputProcedure(arrayTypeName,
            procedureName, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(secondCompareArray, elementsOut,
            outputCompareType, "OutputArray", sb);

        assertCondition(false,
            "Expected exception " + exceptionString + " passed1=" + passed1
                + " passed2=" + passed2 + " " + sb.toString() + description);
      } catch (Exception e) {
        String exMessage = e.getMessage();
        if (exMessage.indexOf(exceptionString) >= 0) {
          assertCondition(true);
        } else {
          failed(e,
              "Expected exception with " + exceptionString + " " + description);
        }
      }
    }

  }

  public void Var006() {
    String description = " -- Type CHAR but array type is BigDecimal -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ";
    testTypeCombination(description, "CHAR", /* declaredArrayType */
        bigDecimalElements, /* specifiedArray */
        stringElements, /* firstCompareArray */
        "java.lang.String", /* nativeDriverCompareType */
        "java.lang.String", /* nativeDriverCompareType */
        "java.math.BigDecimal", /* compareType */
        CHAR30_ARRAY_TYPE, CHAR30_PROCEDURE, char30elements,
        "java.lang.String");
  }

  public void Var007() {
    String description = " -- Type CHAR but array type is Boolean -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ";
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombination(description, "CHAR", booleanElements,
          booleanStringElements, "java.lang.String", "java.lang.String",
          "java.lang.Boolean", CHAR30_ARRAY_TYPE, CHAR30_PROCEDURE,
          luwBooleanChar30Elements, "java.lang.String");

    } else {
      testTypeCombination(description, "CHAR", booleanElements,
          booleanStringElements, "java.lang.String", "java.lang.String",
          "java.lang.Boolean", CHAR30_ARRAY_TYPE, CHAR30_PROCEDURE,
          booleanChar30Elements, "java.lang.String");
    }
  }

  public void Var008() {
    String description = " -- Type CHAR but array type is Byte -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ";
    testTypeCombination(description, "CHAR", byteElements, stringElements,
        "java.lang.String", "java.lang.String", "java.lang.Byte",
        CHAR30_ARRAY_TYPE, CHAR30_PROCEDURE, char30elements,
        "java.lang.String");
  }

  public void Var009() {
    testTypeCombination(
        " -- Type CHAR but array type is Short -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
        "CHAR", shortElements, stringElements, "java.lang.String",
        "java.lang.String", "java.lang.Short", CHAR30_ARRAY_TYPE,
        CHAR30_PROCEDURE, char30elements, "java.lang.String");
  }

  public void Var010() {
    testTypeCombination(
        " -- Type CHAR but array type is Integer -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
        "CHAR", integerElements, stringElements, "java.lang.String",
        "java.lang.String", "java.lang.Integer", CHAR30_ARRAY_TYPE,
        CHAR30_PROCEDURE, char30elements, "java.lang.String");
  }

  public void Var011() {
    testTypeCombination(
        " -- Type CHAR but array type is Long -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
        "CHAR", longElements, stringElements, "java.lang.String",
        "java.lang.String", "java.lang.Long", CHAR30_ARRAY_TYPE,
        CHAR30_PROCEDURE, char30elements, "java.lang.String");
  }

  public void Var012() {
    testTypeCombination(
        " -- Type CHAR but array type is Float -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
        "CHAR", floatElements, floatStringElements, "java.lang.String",
        "java.lang.String", "java.lang.Float", CHAR30_ARRAY_TYPE,
        CHAR30_PROCEDURE, floatChar30Elements, "java.lang.String");
  }

  public void Var013() {
    testTypeCombination(
        " -- Type CHAR but array type is Double -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
        "CHAR", doubleElements, floatStringElements, "java.lang.String",
        "java.lang.String", "java.lang.Double", CHAR30_ARRAY_TYPE,
        CHAR30_PROCEDURE, floatChar30Elements, "java.lang.String");
  }

  public void Var014() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombination(
          " -- Type CHAR but array type is ByteArray -- Conversion CHAR to byte[] not supported   -- Added 03/16/2009 ",
          "CHAR", byteArrayElements, byteArrayStringElements,
          "java.lang.String", "java.lang.String", "[B", CHAR30_ARRAY_TYPE,
          CHAR30_PROCEDURE, byteArrayChar30Elements, "java.lang.String");
    } else {
      testTypeCombinationException(
          " -- Type CHAR but array type is ByteArray -- Conversion CHAR to byte[] not supported   -- Added 03/16/2009 ",
          "CHAR", byteArrayElements, byteArrayStringElements,
          "java.lang.String", "java.lang.String", "[B", CHAR30_ARRAY_TYPE,
          CHAR30_PROCEDURE, char30elements, "java.lang.String",
          "Data type mismatch");
    }
  }

  public void Var015() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("Jcc throws null pointer exception for Date");
    } else {
      testTypeCombination(
          " -- Type CHAR but array type is Date -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
          "CHAR", dateElements, dateStringElements, "java.lang.String",
          "java.lang.String", "java.sql.Date", CHAR30_ARRAY_TYPE,
          CHAR30_PROCEDURE, dateChar30Elements, "java.lang.String");
    }
  }

  public void Var016() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("Jcc throws null pointer exception for Time");
    } else {
      testTypeCombination(
          " -- Type CHAR but array type is Time -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
          "CHAR", timeElements, timeStringElements, "java.lang.String",
          "java.lang.String", "java.sql.Time", CHAR30_ARRAY_TYPE,
          CHAR30_PROCEDURE, timeChar30Elements, "java.lang.String");
    }
  }

  public void Var017() {
    if (isToolboxDriver()) {
      testTypeCombination(
          " -- Type CHAR but array type is Timestamp -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
          "CHAR", timestampElements, timestampStringElements,
          "java.lang.String", "java.lang.String", "java.sql.Timestamp",
          CHAR30_ARRAY_TYPE, CHAR30_PROCEDURE, toolboxTimestampChar30Elements,
          "java.lang.String");

    } else {
      testTypeCombination(
          " -- Type CHAR but array type is Timestamp -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
          "CHAR", timestampElements, timestampStringElements,
          "java.lang.String", "java.lang.String", "java.sql.Timestamp",
          CHAR30_ARRAY_TYPE, CHAR30_PROCEDURE, timestampChar30Elements,
          "java.lang.String");
    }
  }

  public void Var018() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombinationException(
          " -- Type CHAR but array type is Blob -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
          "CHAR", blobElements, blobStringElements, "java.lang.String",
          "java.lang.String", "java.sql.Blob", CHAR30_ARRAY_TYPE,
          CHAR30_PROCEDURE, blobStringElements, "java.lang.String",
          "Illegal cross conversion");
    } else {

      testTypeCombinationException(
          " -- Type CHAR but array type is Blob -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
          "CHAR", blobElements, blobStringElements, "java.lang.String",
          "java.lang.String", "java.sql.Blob", CHAR30_ARRAY_TYPE,
          CHAR30_PROCEDURE, blobStringElements, "java.lang.String",
          "Data type mismatch");
    }
  }

  public void Var019() {
    testTypeCombination(
        " -- Type CHAR but array type is Clob -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
        "CHAR", clobElements, stringElements, "java.lang.String",
        "java.lang.String", "java.sql.Clob", CHAR30_ARRAY_TYPE,
        CHAR30_PROCEDURE, char30elements, "java.lang.String");
  }

  public void Var020() {
    notApplicable();
  }

  /**
   * CreateArrayOf() -- Create an empty array of type VARCHAR
   **/

  public void Var021() {
    String description = " -- Create empty array of type VARCHAR -- Added 03/16/2009 ";
    if (checkArraySupport()) {

      try {

        String[] elements = new String[0];
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "VARCHAR", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(elementsOut.length == 0,
            "Length is " + elementsOut.length + " sb 0 " + description);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var022() {
    String description = " -- Create 1 element array of type VARCHAR -- Added 03/16/2009 ";
    if (checkArraySupport()) {

      try {

        String[] elements = { "Hello VARCHARles" };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "VARCHAR", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(
            elementsOut.length == 1 && elements[0].equals(elementsOut[0]),
            "Length is " + elementsOut.length + " sb 0 " + "elements[0]='"
                + elementsOut[0] + "' " + "sb '" + elements[0] + "' "
                + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var023() {
    String description = " -- Create 10 element array of type VARCHAR -- Note:  JCC fails this test -- Added 03/16/2009 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        String[] elements = { "Hello Charles", "Element 2", "Third Element 3",
            "4", "5", "6", "7", "8", "9", "10" };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "VARCHAR", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(elements, elementsOut, "java.lang.String",
            "InputArray", sb);

        Object outputArray = callInputOutputProcedure(VARCHAR30_ARRAY_TYPE,
            VARCHAR30_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(elements, elementsOut,
            "java.lang.String", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var024() {
    String description = " -- Create 10 element array of type VARCHAR and make sure array is copy -- Added 03/16/2009 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        String[] elements = { "Hello Charles", "Element 2", "Third Element 3",
            "4", "5", "6", "7", "8", "9", "10" };

        String[] setElements = { "Hello Charles", "Element 2",
            "Third Element 3", "4", "5", "6", "7", "8", "9", "10" };

        if (getDriver() == JDTestDriver.DRIVER_JCC || isToolboxDriver()) {
          notApplicable("jcc/TB doesn't copy array when constructing array");
          return;
        }
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "VARCHAR", setElements);
        setElements[0] = "I CHANGED THIS";

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(elements, elementsOut, "java.lang.String",
            "InputArray", sb);

        Object outputArray = callInputOutputProcedure(VARCHAR30_ARRAY_TYPE,
            VARCHAR30_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(elements, elementsOut,
            "java.lang.String", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var025() {
    String description = " -- Create 10 element array of type VARCHAR and make sure ouput array is copy -- Added 03/16/2009 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        String[] elements = { "Hello Charles", "Element 2", "Third Element 3",
            "4", "5", "6", "7", "8", "9", "10" };

        String[] setElements = { "Hello Charles", "Element 2",
            "Third Element 3", "4", "5", "6", "7", "8", "9", "10" };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "VARCHAR", setElements);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        elementsOut[0] = "I CHANGED THIS";

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(arrayObject,
            "getArray");

        boolean passed1;
        passed1 = compareArrays(elements, elementsOut, "java.lang.String",
            "InputArray", sb);

        Object outputArray = callInputOutputProcedure(VARCHAR30_ARRAY_TYPE,
            VARCHAR30_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(elements, elementsOut,
            "java.lang.String", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var026() {

    String description = " -- Type VARCHAR but array type is BigDecimal -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ";
    testTypeCombination(description, "VARCHAR", bigDecimalElements,
        stringElements, "java.lang.String", "java.lang.String",
        "java.math.BigDecimal", VARCHAR30_ARRAY_TYPE, VARCHAR30_PROCEDURE,
        stringElements, "java.lang.String");
  }

  public void Var027() {
    String description = " -- Type VARCHAR but array type is Boolean -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ";
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombination(description, "VARCHAR", booleanElements,
          booleanStringElements, "java.lang.String", "java.lang.String",
          "java.lang.Boolean", VARCHAR30_ARRAY_TYPE, VARCHAR30_PROCEDURE,
          luwBooleanStringElements, "java.lang.String");

    } else {
      testTypeCombination(description, "VARCHAR", booleanElements,
          booleanStringElements, "java.lang.String", "java.lang.String",
          "java.lang.Boolean", VARCHAR30_ARRAY_TYPE, VARCHAR30_PROCEDURE,
          booleanStringElements, "java.lang.String");
    }
  }

  public void Var028() {
    String description = " -- Type VARCHAR but array type is Byte -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ";
    testTypeCombination(description, "VARCHAR", byteElements, stringElements,
        "java.lang.String", "java.lang.String", "java.lang.Byte",
        VARCHAR30_ARRAY_TYPE, VARCHAR30_PROCEDURE, stringElements,
        "java.lang.String");
  }

  public void Var029() {
    testTypeCombination(
        " -- Type VARCHAR but array type is Short -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
        "VARCHAR", shortElements, stringElements, "java.lang.String",
        "java.lang.String", "java.lang.Short", VARCHAR30_ARRAY_TYPE,
        VARCHAR30_PROCEDURE, stringElements, "java.lang.String");
  }

  public void Var030() {
    testTypeCombination(
        " -- Type VARCHAR but array type is Integer -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
        "VARCHAR", integerElements, stringElements, "java.lang.String",
        "java.lang.String", "java.lang.Integer", VARCHAR30_ARRAY_TYPE,
        VARCHAR30_PROCEDURE, stringElements, "java.lang.String");
  }

  public void Var031() {
    testTypeCombination(
        " -- Type VARCHAR but array type is Long -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
        "VARCHAR", longElements, stringElements, "java.lang.String",
        "java.lang.String", "java.lang.Long", VARCHAR30_ARRAY_TYPE,
        VARCHAR30_PROCEDURE, stringElements, "java.lang.String");
  }

  public void Var032() {
    testTypeCombination(
        " -- Type VARCHAR but array type is Float -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
        "VARCHAR", floatElements, floatStringElements, "java.lang.String",
        "java.lang.String", "java.lang.Float", VARCHAR30_ARRAY_TYPE,
        VARCHAR30_PROCEDURE, floatStringElements, "java.lang.String");
  }

  public void Var033() {
    testTypeCombination(
        " -- Type VARCHAR but array type is Double -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
        "VARCHAR", doubleElements, floatStringElements, "java.lang.String",
        "java.lang.String", "java.lang.Double", VARCHAR30_ARRAY_TYPE,
        VARCHAR30_PROCEDURE, floatStringElements, "java.lang.String");
  }

  public void Var034() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombination(
          " -- Type VARCHAR but array type is ByteArray -- Conversion VARCHAR to byte[] not supported   -- Added 03/16/2009 ",
          "VARCHAR", byteArrayElements, byteArrayStringElements,
          "java.lang.String", "java.lang.String", "[B", VARCHAR30_ARRAY_TYPE,
          VARCHAR30_PROCEDURE, byteArrayStringElements, "java.lang.String");
    } else {
      testTypeCombinationException(
          " -- Type VARCHAR but array type is ByteArray -- Conversion VARCHAR to byte[] not supported   -- Added 03/16/2009 ",
          "VARCHAR", byteArrayElements, byteArrayStringElements,
          "java.lang.String", "java.lang.String", "[B", VARCHAR30_ARRAY_TYPE,
          VARCHAR30_PROCEDURE, stringElements, "java.lang.String",
          "Data type mismatch");
    }
  }

  public void Var035() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("Jcc throws null pointer exception for Date");
    } else {
      testTypeCombination(
          " -- Type VARCHAR but array type is Date -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
          "VARCHAR", dateElements, dateStringElements, "java.lang.String",
          "java.lang.String", "java.sql.Date", VARCHAR30_ARRAY_TYPE,
          VARCHAR30_PROCEDURE, dateStringElements, "java.lang.String");
    }
  }

  public void Var036() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("Jcc throws null pointer exception for Time");
    } else {
      testTypeCombination(
          " -- Type VARCHAR but array type is Time -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
          "VARCHAR", timeElements, timeStringElements, "java.lang.String",
          "java.lang.String", "java.sql.Time", VARCHAR30_ARRAY_TYPE,
          VARCHAR30_PROCEDURE, timeStringElements, "java.lang.String");
    }
  }

  public void Var037() {
    if (isToolboxDriver()) {
      testTypeCombination(
          " -- Type VARCHAR but array type is Timestamp -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
          "VARCHAR", timestampElements, timestampStringElements,
          "java.lang.String", "java.lang.String", "java.sql.Timestamp",
          VARCHAR30_ARRAY_TYPE, VARCHAR30_PROCEDURE,
          toolboxTimestampStringElements, "java.lang.String");

    } else {
      testTypeCombination(
          " -- Type VARCHAR but array type is Timestamp -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
          "VARCHAR", timestampElements, timestampStringElements,
          "java.lang.String", "java.lang.String", "java.sql.Timestamp",
          VARCHAR30_ARRAY_TYPE, VARCHAR30_PROCEDURE, timestampStringElements,
          "java.lang.String");
    }
  }

  public void Var038() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombinationException(
          " -- Type VARCHAR but array type is Blob -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
          "VARCHAR", blobElements, blobStringElements, "java.lang.String",
          "java.lang.String", "java.sql.Blob", VARCHAR30_ARRAY_TYPE,
          VARCHAR30_PROCEDURE, blobStringElements, "java.lang.String",
          "Illegal cross conversion");
    } else {

      testTypeCombinationException(
          " -- Type CHAR but array type is Blob -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
          "VARCHAR", blobElements, blobStringElements, "java.lang.String",
          "java.lang.String", "java.sql.Blob", VARCHAR30_ARRAY_TYPE,
          VARCHAR30_PROCEDURE, blobStringElements, "java.lang.String",
          "Data type mismatch");
    }
  }

  public void Var039() {
    testTypeCombination(
        " -- Type VARCHAR but array type is Clob -- LUW doesnt check that type of array matches specified type  -- Added 03/16/2009 ",
        "VARCHAR", clobElements, stringElements, "java.lang.String",
        "java.lang.String", "java.sql.Clob", VARCHAR30_ARRAY_TYPE,
        VARCHAR30_PROCEDURE, stringElements, "java.lang.String");
  }

  public void Var040() {
    notApplicable();
  }

  /**
   * CreateArrayOf() -- Create an empty array of type SMALLINT
   **/

  public void Var041() {
    String description = " -- Create empty array of type SMALLINT -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        Short[] elements = new Short[0];
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "SMALLINT", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(elementsOut.length == 0,
            "Length is " + elementsOut.length + " sb 0 " + description);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var042() {
    String description = " -- Create 1 element array of type SMALLINT -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        Short[] elements = { new Short((short) 1) };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "SMALLINT", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(
            elementsOut.length == 1
                && (1 == ((Number) elementsOut[0]).shortValue()),
            "Length is " + elementsOut.length + " sb 1 " + "elements[0]='"
                + elementsOut[0] + "' " + "sb '" + elements[0] + "' "
                + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var043() {
    String description = " -- Create 10 element array of type SMALLINT -- Note:  JCC fails this test -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Short[] elements = shortElements;

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "SMALLINT", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut,
            "java.lang.Integer", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(SMALLINT_ARRAY_TYPE,
            SMALLINT_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(stringElements, elementsOut,
            "java.lang.Integer", "OutputArray", sb);

        assertCondition(passed1 && passed2, "passed1=" + passed1 + " passed2="
            + passed2 + " " + sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var044() {
    String description = " -- Create 10 element array of type SMALLINT and make sure array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Short[] setElements = { new Short((short) 1), new Short((short) 2),
            new Short((short) 3), new Short((short) 4), null,
            new Short((short) 6), new Short((short) 7), new Short((short) 8),
            new Short((short) -9), new Short((short) 0), };

        if (getDriver() == JDTestDriver.DRIVER_JCC || isToolboxDriver()) {
          notApplicable("jcc/TB doesn't copy array when constructing array");
          return;
        }
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "SMALLINT", setElements);
        setElements[0] = new Short((short) 100);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut,
            "java.lang.Integer", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(SMALLINT_ARRAY_TYPE,
            SMALLINT_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(stringElements, elementsOut,
            "java.lang.Integer", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var045() {
    String description = " -- Create 10 element array of type SMALLINT and make sure ouput array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Short[] setElements = { new Short((short) 1), new Short((short) 2),
            new Short((short) 3), new Short((short) 4), null,
            new Short((short) 6), new Short((short) 7), new Short((short) 8),
            new Short((short) -9), new Short((short) 0), };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "SMALLINT", setElements);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        if (elementsOut == null || elementsOut.length < 1) {
          throw new Exception(
              "Bad elementsOut array from createArrayOf elementsOut="
                  + elementsOut + " size=" + elementsOut.length);
        }

        elementsOut[0] = new Integer((short) 99);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(arrayObject,
            "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut,
            "java.lang.Integer", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(SMALLINT_ARRAY_TYPE,
            SMALLINT_PROCEDURE, (Array) arrayObject);

        boolean passed2;
        if (outputArray == null) {
          passed2 = false;
          sb.append("Output array is null");
        } else {
          elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
              "getArray");

          passed2 = compareArrays(stringElements, elementsOut,
              "java.lang.Integer", "OutputArray", sb);
        }

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var046() {

    String description = " -- Type SMALLINT but array type is BigDecimal -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "SMALLINT", bigDecimalElements,
        stringElements, "java.lang.Integer", "java.lang.Integer",
        "java.math.BigDecimal", SMALLINT_ARRAY_TYPE, SMALLINT_PROCEDURE,
        stringElements, "java.lang.Integer");
  }

  public void Var047() {

    String description = " -- Type SMALLINT but array type is Boolean -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";

    testTypeCombination(description, "SMALLINT", booleanElements,
        luwBooleanStringElements, "java.lang.Integer", "java.lang.Integer",
        "java.lang.Boolean", SMALLINT_ARRAY_TYPE, SMALLINT_PROCEDURE,
        luwBooleanStringElements, "java.lang.Integer");

  }

  public void Var048() {
    String description = " -- Type SMALLINT but array type is Byte -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "SMALLINT", byteElements, stringElements,
        "java.lang.Integer", "java.lang.Integer", "java.lang.Byte",
        SMALLINT_ARRAY_TYPE, SMALLINT_PROCEDURE, stringElements,
        "java.lang.Integer");
  }

  public void Var049() {
    testTypeCombination(
        " -- Type SMALLINT but array type is Short -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "SMALLINT", shortElements, stringElements, "java.lang.Integer",
        "java.lang.Integer", "java.lang.Integer", SMALLINT_ARRAY_TYPE,
        SMALLINT_PROCEDURE, stringElements, "java.lang.Integer");
  }

  public void Var050() {
    testTypeCombination(
        " -- Type SMALLINT but array type is Integer -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "SMALLINT", integerElements, stringElements, "java.lang.Integer",
        "java.lang.Integer", "java.lang.Integer", SMALLINT_ARRAY_TYPE,
        SMALLINT_PROCEDURE, stringElements, "java.lang.Integer");
  }

  public void Var051() {
    testTypeCombination(
        " -- Type SMALLINT but array type is Long -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "SMALLINT", longElements, stringElements, "java.lang.Integer",
        "java.lang.Integer", "java.lang.Long", SMALLINT_ARRAY_TYPE,
        SMALLINT_PROCEDURE, stringElements, "java.lang.Integer");
  }

  public void Var052() {
    testTypeCombination(
        " -- Type SMALLINT but array type is Float -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "SMALLINT", floatElements, stringElements, "java.lang.Integer",
        "java.lang.Integer", "java.lang.Float", SMALLINT_ARRAY_TYPE,
        SMALLINT_PROCEDURE, stringElements, "java.lang.Integer");
  }

  public void Var053() {
    testTypeCombination(
        " -- Type SMALLINT but array type is Double -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "SMALLINT", doubleElements, stringElements, "java.lang.Integer",
        "java.lang.Integer", "java.lang.Double", SMALLINT_ARRAY_TYPE,
        SMALLINT_PROCEDURE, stringElements, "java.lang.Integer");
  }

  public void Var054() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombination(
          " -- Type SMALLINT but array type is ByteArray -- Conversion SMALLINT to byte[] not supported   -- Added 01/18/2010 ",
          "SMALLINT", byteArrayElements, byteArrayStringElements,
          "java.lang.Integer", "java.lang.Integer", "[B", SMALLINT_ARRAY_TYPE,
          SMALLINT_PROCEDURE, byteArrayStringElements, "java.lang.Integer");
    } else {
      testTypeCombinationException(
          " -- Type SMALLINT but array type is ByteArray -- Conversion SMALLINT to byte[] not supported   -- Added 01/18/2010 ",
          "SMALLINT", byteArrayElements, byteArrayStringElements,
          "java.lang.Integer", "java.lang.Integer", "[B", SMALLINT_ARRAY_TYPE,
          SMALLINT_PROCEDURE, stringElements, "java.lang.Integer",
          "Data type mismatch");
    }
  }

  public void Var055() {
    testTypeCombinationException(
        " -- Type SMALLINT but array type is Date -- -- Added 01/18/2010 ",
        "SMALLINT", dateElements, dateStringElements, "java.lang.Integer",
        "java.lang.Integer", "java.sql.Date", SMALLINT_ARRAY_TYPE,
        SMALLINT_PROCEDURE, dateStringElements, "java.lang.Integer",
        "Data type mismatch");

  }

  public void Var056() {
    testTypeCombinationException(
        " -- Type SMALLINT but array type is Time --  Added 01/18/2010 ",
        "SMALLINT", timeElements, timeStringElements, "java.lang.Integer",
        "java.lang.Integer", "java.sql.Time", SMALLINT_ARRAY_TYPE,
        SMALLINT_PROCEDURE, timeStringElements, "java.lang.Integer",
        "Data type mismatch");

  }

  public void Var057() {
    testTypeCombinationException(
        " -- Type SMALLINT but array type is Timestamp -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "SMALLINT", timestampElements, timestampStringElements,
        "java.lang.Integer", "java.lang.Integer", "java.sql.Timestamp",
        SMALLINT_ARRAY_TYPE, SMALLINT_PROCEDURE, timestampStringElements,
        "java.lang.Integer", "Data type mismatch");

  }

  public void Var058() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombinationException(
          " -- Type SMALLINT but array type is Blob -- Added 01/18/2010 ",
          "SMALLINT", blobElements, blobStringElements, "java.lang.Integer",
          "java.lang.Integer", "java.sql.Blob", SMALLINT_ARRAY_TYPE,
          SMALLINT_PROCEDURE, blobStringElements, "java.lang.Integer",
          "Illegal cross conversion");
    } else {

      testTypeCombinationException(
          " -- Type SMALLINT but array type is Blob -- Added 01/18/2010 ",
          "SMALLINT", blobElements, blobStringElements, "java.lang.Integer",
          "java.lang.Integer", "java.sql.Blob", SMALLINT_ARRAY_TYPE,
          SMALLINT_PROCEDURE, blobStringElements, "java.lang.Integer",
          "Data type mismatch");
    }
  }

  public void Var059() {
    testTypeCombinationException(
        " -- Type SMALLINT but array type is Clob -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "SMALLINT", clobElements, stringElements, "java.lang.Integer",
        "java.lang.Integer", "java.sql.Clob", SMALLINT_ARRAY_TYPE,
        SMALLINT_PROCEDURE, stringElements, "java.lang.Integer",
        "Data type mismatch");
  }

  public void Var060() {

    String description = " -- Type SMALLINT but array type is String -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "SMALLINT", stringElements, stringElements,
        "java.lang.Integer", "java.lang.Integer", "java.math.BigDecimal",
        SMALLINT_ARRAY_TYPE, SMALLINT_PROCEDURE, stringElements,
        "java.lang.Integer");
  }

  /**
   * CreateArrayOf() -- Create an empty array of type INTEGER
   **/

  public void Var061() {
    String description = " -- Create empty array of type INTEGER -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        Integer[] elements = new Integer[0];
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "INTEGER", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(elementsOut.length == 0,
            "Length is " + elementsOut.length + " sb 0 " + description);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var062() {
    String description = " -- Create 1 element array of type INTEGER -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        Integer[] elements = { new Integer(1) };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "INTEGER", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(
            elementsOut.length == 1
                && (1 == ((Number) elementsOut[0]).intValue()),
            "Length is " + elementsOut.length + " sb 1 " + "elements[0]='"
                + elementsOut[0] + "' " + "sb '" + elements[0] + "' "
                + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var063() {
    String description = " -- Create 10 element array of type INTEGER -- Note:  JCC fails this test -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Integer[] elements = integerElements;

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "INTEGER", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut,
            "java.lang.Integer", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(INTEGER_ARRAY_TYPE,
            INTEGER_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(stringElements, elementsOut,
            "java.lang.Integer", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var064() {
    String description = " -- Create 10 element array of type INTEGER and make sure array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Integer[] setElements = { new Integer(1), new Integer(2),
            new Integer(3), new Integer(4), null, new Integer(6),
            new Integer(7), new Integer(8), new Integer(-9), new Integer(0), };

        if (getDriver() == JDTestDriver.DRIVER_JCC || isToolboxDriver()) {
          notApplicable("jcc/TB doesn't copy array when constructing array");
          return;
        }
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "INTEGER", setElements);
        setElements[0] = new Integer(100);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut,
            "java.lang.Integer", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(INTEGER_ARRAY_TYPE,
            INTEGER_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(stringElements, elementsOut,
            "java.lang.Integer", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var065() {
    String description = " -- Create 10 element array of type INTEGER and make sure ouput array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Integer[] setElements = { new Integer(1), new Integer(2),
            new Integer(3), new Integer(4), null, new Integer(6),
            new Integer(7), new Integer(8), new Integer(-9), new Integer(0), };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "INTEGER", setElements);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        elementsOut[0] = new Integer(99);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(arrayObject,
            "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut,
            "java.lang.Integer", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(INTEGER_ARRAY_TYPE,
            INTEGER_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(stringElements, elementsOut,
            "java.lang.Integer", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var066() {

    String description = " -- Type INTEGER but array type is BigDecimal -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "INTEGER", bigDecimalElements,
        stringElements, "java.lang.Integer", "java.lang.Integer",
        "java.math.BigDecimal", INTEGER_ARRAY_TYPE, INTEGER_PROCEDURE,
        stringElements, "java.lang.Integer");
  }

  public void Var067() {
    String description = " -- Type INTEGER but array type is Boolean -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "INTEGER", booleanElements,
        luwBooleanStringElements, "java.lang.Integer", "java.lang.Integer",
        "java.lang.Boolean", INTEGER_ARRAY_TYPE, INTEGER_PROCEDURE,
        luwBooleanStringElements, "java.lang.Integer");

  }

  public void Var068() {
    String description = " -- Type INTEGER but array type is Byte -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "INTEGER", byteElements, stringElements,
        "java.lang.Integer", "java.lang.Integer", "java.lang.Byte",
        INTEGER_ARRAY_TYPE, INTEGER_PROCEDURE, stringElements,
        "java.lang.Integer");
  }

  public void Var069() {
    testTypeCombination(
        " -- Type INTEGER but array type is short -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "INTEGER", shortElements, stringElements, "java.lang.Integer",
        "java.lang.Integer", "java.lang.Integer", INTEGER_ARRAY_TYPE,
        INTEGER_PROCEDURE, stringElements, "java.lang.Integer");
  }

  public void Var070() {
    testTypeCombination(
        " -- Type INTEGER but array type is Integer -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "INTEGER", integerElements, stringElements, "java.lang.Integer",
        "java.lang.Integer", "java.lang.Integer", INTEGER_ARRAY_TYPE,
        INTEGER_PROCEDURE, stringElements, "java.lang.Integer");
  }

  public void Var071() {
    testTypeCombination(
        " -- Type INTEGER but array type is Long -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "INTEGER", longElements, stringElements, "java.lang.Integer",
        "java.lang.Integer", "java.lang.Long", INTEGER_ARRAY_TYPE,
        INTEGER_PROCEDURE, stringElements, "java.lang.Integer");
  }

  public void Var072() {
    testTypeCombination(
        " -- Type INTEGER but array type is Float -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "INTEGER", floatElements, stringElements, "java.lang.Integer",
        "java.lang.Integer", "java.lang.Float", INTEGER_ARRAY_TYPE,
        INTEGER_PROCEDURE, stringElements, "java.lang.Integer");
  }

  public void Var073() {
    testTypeCombination(
        " -- Type INTEGER but array type is Double -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "INTEGER", doubleElements, stringElements, "java.lang.Integer",
        "java.lang.Integer", "java.lang.Double", INTEGER_ARRAY_TYPE,
        INTEGER_PROCEDURE, stringElements, "java.lang.Integer");
  }

  public void Var074() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombination(
          " -- Type INTEGER but array type is ByteArray -- Conversion INTEGER to byte[] not supported   -- Added 01/18/2010 ",
          "INTEGER", byteArrayElements, byteArrayStringElements,
          "java.lang.Integer", "java.lang.Integer", "[B", INTEGER_ARRAY_TYPE,
          INTEGER_PROCEDURE, byteArrayStringElements, "java.lang.Integer");
    } else {
      testTypeCombinationException(
          " -- Type INTEGER but array type is ByteArray -- Conversion INTEGER to byte[] not supported   -- Added 01/18/2010 ",
          "INTEGER", byteArrayElements, byteArrayStringElements,
          "java.lang.Integer", "java.lang.Integer", "[B", INTEGER_ARRAY_TYPE,
          INTEGER_PROCEDURE, stringElements, "java.lang.Integer",
          "Data type mismatch");
    }
  }

  public void Var075() {
    testTypeCombinationException(
        " -- Type INTEGER but array type is Date -- -- Added 01/18/2010 ",
        "INTEGER", dateElements, dateStringElements, "java.lang.Integer",
        "java.lang.Integer", "java.sql.Date", INTEGER_ARRAY_TYPE,
        INTEGER_PROCEDURE, dateStringElements, "java.lang.Integer",
        "Data type mismatch");

  }

  public void Var076() {
    testTypeCombinationException(
        " -- Type INTEGER but array type is Time --  Added 01/18/2010 ",
        "INTEGER", timeElements, timeStringElements, "java.lang.Integer",
        "java.lang.Integer", "java.sql.Time", INTEGER_ARRAY_TYPE,
        INTEGER_PROCEDURE, timeStringElements, "java.lang.Integer",
        "Data type mismatch");

  }

  public void Var077() {
    testTypeCombinationException(
        " -- Type INTEGER but array type is Timestamp -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "INTEGER", timestampElements, timestampStringElements,
        "java.lang.Integer", "java.lang.Integer", "java.sql.Timestamp",
        INTEGER_ARRAY_TYPE, INTEGER_PROCEDURE, timestampStringElements,
        "java.lang.Integer", "Data type mismatch");

  }

  public void Var078() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombinationException(
          " -- Type INTEGER but array type is Blob -- Added 01/18/2010 ",
          "INTEGER", blobElements, blobStringElements, "java.lang.Integer",
          "java.lang.Integer", "java.sql.Blob", INTEGER_ARRAY_TYPE,
          INTEGER_PROCEDURE, blobStringElements, "java.lang.Integer",
          "Illegal cross conversion");
    } else {

      testTypeCombinationException(
          " -- Type CHAR but array type is Blob -- Added 01/18/2010 ",
          "INTEGER", blobElements, blobStringElements, "java.lang.Integer",
          "java.lang.Integer", "java.sql.Blob", INTEGER_ARRAY_TYPE,
          INTEGER_PROCEDURE, blobStringElements, "java.lang.Integer",
          "Data type mismatch");
    }
  }

  public void Var079() {
    testTypeCombinationException(
        " -- Type INTEGER but array type is Clob -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "INTEGER", clobElements, stringElements, "java.lang.Integer",
        "java.lang.Integer", "java.sql.Clob", INTEGER_ARRAY_TYPE,
        INTEGER_PROCEDURE, stringElements, "java.lang.Integer",
        "Data type mismatch");
  }

  public void Var080() {

    String description = " -- Type INTEGER but array type is String -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "INTEGER", stringElements, stringElements,
        "java.lang.Integer", "java.lang.Integer", "java.math.BigDecimal",
        SMALLINT_ARRAY_TYPE, SMALLINT_PROCEDURE, stringElements,
        "java.lang.Integer");
  }

  /**
   * CreateArrayOf() -- Create an empty array of type BIGINT
   **/

  public void Var081() {
    String description = " -- Create empty array of type BIGINT -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        Long[] elements = new Long[0];
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "BIGINT", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(elementsOut.length == 0,
            "Length is " + elementsOut.length + " sb 0 " + description);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var082() {
    String description = " -- Create 1 element array of type BIGINT -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        Long[] elements = { new Long(1) };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "BIGINT", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(
            elementsOut.length == 1
                && (1 == ((Number) elementsOut[0]).shortValue()),
            "Length is " + elementsOut.length + " sb 1 " + "elements[0]='"
                + elementsOut[0] + "' " + "sb '" + elements[0] + "' "
                + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var083() {
    String description = " -- Create 10 element array of type BIGINT -- Note:  JCC fails this test -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Long[] elements = longElements;

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "BIGINT", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut, "java.lang.Long",
            "InputArray", sb);

        Object outputArray = callInputOutputProcedure(BIGINT_ARRAY_TYPE,
            BIGINT_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(stringElements, elementsOut,
            "java.lang.Long", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var084() {
    String description = " -- Create 10 element array of type BIGINT and make sure array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Long[] setElements = { new Long(1), new Long(2), new Long(3),
            new Long(4), null, new Long(6), new Long(7), new Long(8),
            new Long(-9), new Long(0), };

        if (getDriver() == JDTestDriver.DRIVER_JCC || isToolboxDriver()) {
          notApplicable("jcc/TB doesn't copy array when constructing array");
          return;
        }
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "BIGINT", setElements);
        setElements[0] = new Long(100);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut, "java.lang.Long",
            "InputArray", sb);

        Object outputArray = callInputOutputProcedure(BIGINT_ARRAY_TYPE,
            BIGINT_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(stringElements, elementsOut,
            "java.lang.Long", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var085() {
    String description = " -- Create 10 element array of type BIGINT and make sure ouput array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Long[] setElements = { new Long(1), new Long(2), new Long(3),
            new Long(4), null, new Long(6), new Long(7), new Long(8),
            new Long(-9), new Long(0), };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "BIGINT", setElements);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        elementsOut[0] = new Long(99);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(arrayObject,
            "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut, "java.lang.Long",
            "InputArray", sb);

        Object outputArray = callInputOutputProcedure(BIGINT_ARRAY_TYPE,
            BIGINT_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(stringElements, elementsOut,
            "java.lang.Long", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var086() {

    String description = " -- Type BIGINT but array type is BigDecimal -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "BIGINT", bigDecimalElements,
        stringElements, "java.lang.Long", "java.lang.Long",
        "java.math.BigDecimal", BIGINT_ARRAY_TYPE, BIGINT_PROCEDURE,
        stringElements, "java.lang.Long");
  }

  public void Var087() {
    String description = " -- Type BIGINT but array type is Boolean -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "BIGINT", booleanElements,
        luwBooleanStringElements, "java.lang.Long", "java.lang.Long",
        "java.lang.Boolean", BIGINT_ARRAY_TYPE, BIGINT_PROCEDURE,
        luwBooleanStringElements, "java.lang.Long");

  }

  public void Var088() {
    String description = " -- Type BIGINT but array type is Byte -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "BIGINT", byteElements, stringElements,
        "java.lang.Long", "java.lang.Long", "java.lang.Byte", BIGINT_ARRAY_TYPE,
        BIGINT_PROCEDURE, stringElements, "java.lang.Long");
  }

  public void Var089() {
    testTypeCombination(
        " -- Type BIGINT but array type is Short -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "BIGINT", shortElements, stringElements, "java.lang.Long",
        "java.lang.Long", "java.lang.Long", BIGINT_ARRAY_TYPE, BIGINT_PROCEDURE,
        stringElements, "java.lang.Long");
  }

  public void Var090() {
    testTypeCombination(
        " -- Type BIGINT but array type is Integer -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "BIGINT", integerElements, stringElements, "java.lang.Long",
        "java.lang.Long", "java.lang.Long", BIGINT_ARRAY_TYPE, BIGINT_PROCEDURE,
        stringElements, "java.lang.Long");
  }

  public void Var091() {
    testTypeCombination(
        " -- Type BIGINT but array type is Long -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "BIGINT", longElements, stringElements, "java.lang.Long",
        "java.lang.Long", "java.lang.Long", BIGINT_ARRAY_TYPE, BIGINT_PROCEDURE,
        stringElements, "java.lang.Long");
  }

  public void Var092() {
    testTypeCombination(
        " -- Type BIGINT but array type is Float -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "BIGINT", floatElements, stringElements, "java.lang.Long",
        "java.lang.Long", "java.lang.Float", BIGINT_ARRAY_TYPE,
        BIGINT_PROCEDURE, stringElements, "java.lang.Long");
  }

  public void Var093() {
    testTypeCombination(
        " -- Type BIGINT but array type is Double -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "BIGINT", doubleElements, stringElements, "java.lang.Long",
        "java.lang.Long", "java.lang.Double", BIGINT_ARRAY_TYPE,
        BIGINT_PROCEDURE, stringElements, "java.lang.Long");
  }

  public void Var094() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombination(
          " -- Type BIGINT but array type is ByteArray -- Conversion BIGINT to byte[] not supported   -- Added 01/18/2010 ",
          "BIGINT", byteArrayElements, byteArrayStringElements,
          "java.lang.Long", "java.lang.Long", "[B", BIGINT_ARRAY_TYPE,
          BIGINT_PROCEDURE, byteArrayStringElements, "java.lang.Long");
    } else {
      testTypeCombinationException(
          " -- Type BIGINT but array type is ByteArray -- Conversion BIGINT to byte[] not supported   -- Added 01/18/2010 ",
          "BIGINT", byteArrayElements, byteArrayStringElements,
          "java.lang.Long", "java.lang.Long", "[B", BIGINT_ARRAY_TYPE,
          BIGINT_PROCEDURE, stringElements, "java.lang.Long",
          "Data type mismatch");
    }
  }

  public void Var095() {
    testTypeCombinationException(
        " -- Type BIGINT but array type is Date -- -- Added 01/18/2010 ",
        "BIGINT", dateElements, dateStringElements, "java.lang.Long",
        "java.lang.Long", "java.sql.Date", BIGINT_ARRAY_TYPE, BIGINT_PROCEDURE,
        dateStringElements, "java.lang.Long", "Data type mismatch");

  }

  public void Var096() {
    testTypeCombinationException(
        " -- Type BIGINT but array type is Time --  Added 01/18/2010 ",
        "BIGINT", timeElements, timeStringElements, "java.lang.Long",
        "java.lang.Long", "java.sql.Time", BIGINT_ARRAY_TYPE, BIGINT_PROCEDURE,
        timeStringElements, "java.lang.Long", "Data type mismatch");

  }

  public void Var097() {
    testTypeCombinationException(
        " -- Type BIGINT but array type is Timestamp -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "BIGINT", timestampElements, timestampStringElements, "java.lang.Long",
        "java.lang.Long", "java.sql.Timestamp", BIGINT_ARRAY_TYPE,
        BIGINT_PROCEDURE, timestampStringElements, "java.lang.Long",
        "Data type mismatch");

  }

  public void Var098() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombinationException(
          " -- Type BIGINT but array type is Blob -- Added 01/18/2010 ",
          "BIGINT", blobElements, blobStringElements, "java.lang.Long",
          "java.lang.Long", "java.sql.Blob", BIGINT_ARRAY_TYPE,
          BIGINT_PROCEDURE, blobStringElements, "java.lang.Long",
          "Illegal cross conversion");
    } else {

      testTypeCombinationException(
          " -- Type CHAR but array type is Blob -- Added 01/18/2010 ", "BIGINT",
          blobElements, blobStringElements, "java.lang.Long", "java.lang.Long",
          "java.sql.Blob", BIGINT_ARRAY_TYPE, BIGINT_PROCEDURE,
          blobStringElements, "java.lang.Long", "Data type mismatch");
    }
  }

  public void Var099() {
    testTypeCombinationException(
        " -- Type BIGINT but array type is Clob -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "BIGINT", clobElements, stringElements, "java.lang.Long",
        "java.lang.Long", "java.sql.Clob", BIGINT_ARRAY_TYPE, BIGINT_PROCEDURE,
        stringElements, "java.lang.Long", "Data type mismatch");
  }

  public void Var100() {
    String description = " -- Type BIGINT but array type is String -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "BIGINT", stringElements, stringElements,
        "java.lang.Long", "java.lang.Long", "java.lang.Boolean",
        BIGINT_ARRAY_TYPE, BIGINT_PROCEDURE, stringElements, "java.lang.Long");

  }

  /**
   * CreateArrayOf() -- Create an empty array of type REAL
   **/

  public void Var101() {
    String description = " -- Create empty array of type REAL -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        Float[] elements = new Float[0];
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "REAL", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(elementsOut.length == 0,
            "Length is " + elementsOut.length + " sb 0 " + description);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var102() {
    String description = " -- Create 1 element array of type REAL -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        Float[] elements = { new Float(1) };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "REAL", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(
            elementsOut.length == 1
                && (1 == ((Number) elementsOut[0]).shortValue()),
            "Length is " + elementsOut.length + " sb 1 " + "elements[0]='"
                + elementsOut[0] + "' " + "sb '" + elements[0] + "' "
                + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var103() {
    String description = " -- Create 10 element array of type REAL -- Note:  JCC fails this test -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Float[] elements = floatElements;

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "REAL", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Float", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(REAL_ARRAY_TYPE,
            REAL_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Float", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var104() {
    String description = " -- Create 10 element array of type REAL and make sure array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Float[] setElements = { new Float(1), new Float(2), new Float(3),
            new Float(4), null, new Float(6), new Float(7), new Float(8),
            new Float(-9), new Float(0), };

        if (getDriver() == JDTestDriver.DRIVER_JCC || isToolboxDriver()) {
          notApplicable("jcc/TB doesn't copy array when constructing array");
          return;
        }
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "REAL", setElements);
        setElements[0] = new Float(100);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Float", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(REAL_ARRAY_TYPE,
            REAL_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Float", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var105() {
    String description = " -- Create 10 element array of type REAL and make sure ouput array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Float[] setElements = { new Float(1), new Float(2), new Float(3),
            new Float(4), null, new Float(6), new Float(7), new Float(8),
            new Float(-9), new Float(0), };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "REAL", setElements);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        elementsOut[0] = new Float(99);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(arrayObject,
            "getArray");

        boolean passed1;
        passed1 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Float", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(REAL_ARRAY_TYPE,
            REAL_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Float", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var106() {

    String description = " -- Type REAL but array type is BigDecimal -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "REAL", bigDecimalElements,
        floatStringElements, "java.lang.Float", "java.lang.Float",
        "java.math.BigDecimal", REAL_ARRAY_TYPE, REAL_PROCEDURE,
        floatStringElements, "java.lang.Float");
  }

  public void Var107() {
    String description = " -- Type REAL but array type is Boolean -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "REAL", booleanElements,
        floatBooleanStringElements, "java.lang.Float", "java.lang.Float",
        "java.lang.Boolean", REAL_ARRAY_TYPE, REAL_PROCEDURE,
        floatBooleanStringElements, "java.lang.Float");

  }

  public void Var108() {
    String description = " -- Type REAL but array type is Byte -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "REAL", byteElements, floatStringElements,
        "java.lang.Float", "java.lang.Float", "java.lang.Byte", REAL_ARRAY_TYPE,
        REAL_PROCEDURE, floatStringElements, "java.lang.Float");
  }

  public void Var109() {
    testTypeCombination(
        " -- Type REAL but array type is Short -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "REAL", shortElements, floatStringElements, "java.lang.Float",
        "java.lang.Float", "java.lang.Float", REAL_ARRAY_TYPE, REAL_PROCEDURE,
        floatStringElements, "java.lang.Float");
  }

  public void Var110() {
    testTypeCombination(
        " -- Type REAL but array type is Integer -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "REAL", integerElements, floatStringElements, "java.lang.Float",
        "java.lang.Float", "java.lang.Float", REAL_ARRAY_TYPE, REAL_PROCEDURE,
        floatStringElements, "java.lang.Float");
  }

  public void Var111() {
    testTypeCombination(
        " -- Type REAL but array type is Long -- LUW doesnt check that type of array matches specified type  -- Updated 2021 to test zero (needs code fix) ",
        "REAL", longElements, floatStringElements, "java.lang.Float",
        "java.lang.Float", "java.lang.Float", REAL_ARRAY_TYPE, REAL_PROCEDURE,
        floatStringElements, "java.lang.Float");
  }

  public void Var112() {
    testTypeCombination(
        " -- Type REAL but array type is Float -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "REAL", floatElements, floatStringElements, "java.lang.Float",
        "java.lang.Float", "java.lang.Float", REAL_ARRAY_TYPE, REAL_PROCEDURE,
        floatStringElements, "java.lang.Float");
  }

  public void Var113() {
    testTypeCombination(
        " -- Type REAL but array type is Double -- LUW doesnt check that type of array matches specified type  -- - Updated 2021 to test zero (needs code fix)  ",
        "REAL", doubleElements, floatStringElements, "java.lang.Float",
        "java.lang.Float", "java.lang.Double", REAL_ARRAY_TYPE, REAL_PROCEDURE,
        floatStringElements, "java.lang.Float");
  }

  public void Var114() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombination(
          " -- Type REAL but array type is ByteArray -- Conversion REAL to byte[] not supported   -- Added 01/18/2010 ",
          "REAL", byteArrayElements, floatStringElements, "java.lang.Float",
          "java.lang.Float", "[B", REAL_ARRAY_TYPE, REAL_PROCEDURE,
          floatStringElements, "java.lang.Float");
    } else {
      testTypeCombinationException(
          " -- Type REAL but array type is ByteArray -- Conversion REAL to byte[] not supported   -- Added 01/18/2010 ",
          "REAL", byteArrayElements, floatStringElements, "java.lang.Float",
          "java.lang.Float", "[B", REAL_ARRAY_TYPE, REAL_PROCEDURE,
          floatStringElements, "java.lang.Float", "Data type mismatch");
    }
  }

  public void Var115() {
    testTypeCombinationException(
        " -- Type REAL but array type is Date -- -- Added 01/18/2010 ", "REAL",
        dateElements, dateStringElements, "java.lang.Float", "java.lang.Float",
        "java.sql.Date", REAL_ARRAY_TYPE, REAL_PROCEDURE, dateStringElements,
        "java.lang.Float", "Data type mismatch");

  }

  public void Var116() {
    testTypeCombinationException(
        " -- Type REAL but array type is Time --  Added 01/18/2010 ", "REAL",
        timeElements, timeStringElements, "java.lang.Float", "java.lang.Float",
        "java.sql.Time", REAL_ARRAY_TYPE, REAL_PROCEDURE, timeStringElements,
        "java.lang.Float", "Data type mismatch");

  }

  public void Var117() {
    testTypeCombinationException(
        " -- Type REAL but array type is Timestamp -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "REAL", timestampElements, timestampStringElements, "java.lang.Float",
        "java.lang.Float", "java.sql.Timestamp", REAL_ARRAY_TYPE,
        REAL_PROCEDURE, timestampStringElements, "java.lang.Float",
        "Data type mismatch");

  }

  public void Var118() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombinationException(
          " -- Type REAL but array type is Blob -- Added 01/18/2010 ", "REAL",
          blobElements, blobStringElements, "java.lang.Float",
          "java.lang.Float", "java.sql.Blob", REAL_ARRAY_TYPE, REAL_PROCEDURE,
          blobStringElements, "java.lang.Float", "Illegal cross conversion");
    } else {

      testTypeCombinationException(
          " -- Type CHAR but array type is Blob -- Added 01/18/2010 ", "REAL",
          blobElements, blobStringElements, "java.lang.Float",
          "java.lang.Float", "java.sql.Blob", REAL_ARRAY_TYPE, REAL_PROCEDURE,
          blobStringElements, "java.lang.Float", "Data type mismatch");
    }
  }

  public void Var119() {
    testTypeCombinationException(
        " -- Type REAL but array type is Clob -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "REAL", clobElements, floatStringElements, "java.lang.Float",
        "java.lang.Float", "java.sql.Clob", REAL_ARRAY_TYPE, REAL_PROCEDURE,
        floatStringElements, "java.lang.Float", "Data type mismatch");
  }

  public void Var120() {

    String description = " -- Type REAL but array type is String -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "REAL", floatStringElements,
        floatStringElements, "java.lang.Float", "java.lang.Float",
        "java.math.BigDecimal", REAL_ARRAY_TYPE, REAL_PROCEDURE,
        floatStringElements, "java.lang.Float");
  }

  /**
   * CreateArrayOf() -- Create an empty array of type FLOAT
   **/

  public void Var121() {
    String description = " -- Create empty array of type FLOAT -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        Double[] elements = new Double[0];
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "FLOAT", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(elementsOut.length == 0,
            "Length is " + elementsOut.length + " sb 0 " + description);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var122() {
    String description = " -- Create 1 element array of type FLOAT -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        Double[] elements = { new Double(1) };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "FLOAT", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(
            elementsOut.length == 1
                && (1 == ((Number) elementsOut[0]).shortValue()),
            "Length is " + elementsOut.length + " sb 1 " + "elements[0]='"
                + elementsOut[0] + "' " + "sb '" + elements[0] + "' "
                + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var123() {
    String description = " -- Create 10 element array of type FLOAT -- Note:  JCC fails this test -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Double[] elements = doubleElements;

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "FLOAT", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Double", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(FLOAT_ARRAY_TYPE,
            FLOAT_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Double", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var124() {
    String description = " -- Create 10 element array of type FLOAT and make sure array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Double[] setElements = { new Double(1), new Double(2), new Double(3),
            new Double(4), null, new Double(6), new Double(7), new Double(8),
            new Double(-9), new Double(0), };

        if (getDriver() == JDTestDriver.DRIVER_JCC || isToolboxDriver()) {
          notApplicable("jcc/TB doesn't copy array when constructing array");
          return;
        }
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "FLOAT", setElements);
        setElements[0] = new Double(100);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Double", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(FLOAT_ARRAY_TYPE,
            FLOAT_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Double", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var125() {
    String description = " -- Create 10 element array of type FLOAT and make sure ouput array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Double[] setElements = { new Double(1), new Double(2), new Double(3),
            new Double(4), null, new Double(6), new Double(7), new Double(8),
            new Double(-9), new Double(0), };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "FLOAT", setElements);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        elementsOut[0] = new Double(99);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(arrayObject,
            "getArray");

        boolean passed1;
        passed1 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Double", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(FLOAT_ARRAY_TYPE,
            FLOAT_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Double", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var126() {

    String description = " -- Type FLOAT but array type is BigDecimal -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "FLOAT", bigDecimalElements,
        floatStringElements, "java.lang.Double", "java.lang.Double",
        "java.math.BigDecimal", FLOAT_ARRAY_TYPE, FLOAT_PROCEDURE,
        floatStringElements, "java.lang.Double");
  }

  public void Var127() {
    String description = " -- Type FLOAT but array type is Boolean -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "FLOAT", booleanElements,
        floatBooleanStringElements, "java.lang.Double", "java.lang.Double",
        "java.lang.Boolean", FLOAT_ARRAY_TYPE, FLOAT_PROCEDURE,
        floatBooleanStringElements, "java.lang.Double");

  }

  public void Var128() {
    String description = " -- Type FLOAT but array type is Byte -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "FLOAT", byteElements, floatStringElements,
        "java.lang.Double", "java.lang.Double", "java.lang.Byte",
        FLOAT_ARRAY_TYPE, FLOAT_PROCEDURE, floatStringElements,
        "java.lang.Double");
  }

  public void Var129() {
    testTypeCombination(
        " -- Type FLOAT but array type is Short -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "FLOAT", shortElements, floatStringElements, "java.lang.Double",
        "java.lang.Double", "java.lang.Double", FLOAT_ARRAY_TYPE,
        FLOAT_PROCEDURE, floatStringElements, "java.lang.Double");
  }

  public void Var130() {
    testTypeCombination(
        " -- Type FLOAT but array type is Integer -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "FLOAT", integerElements, floatStringElements, "java.lang.Double",
        "java.lang.Double", "java.lang.Double", FLOAT_ARRAY_TYPE,
        FLOAT_PROCEDURE, floatStringElements, "java.lang.Double");
  }

  public void Var131() {
    testTypeCombination(
        " -- Type FLOAT but array type is Long -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "FLOAT", longElements, floatStringElements, "java.lang.Double",
        "java.lang.Double", "java.lang.Double", FLOAT_ARRAY_TYPE,
        FLOAT_PROCEDURE, floatStringElements, "java.lang.Double");
  }

  public void Var132() {
    testTypeCombination(
        " -- Type FLOAT but array type is Float -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "FLOAT", floatElements, floatStringElements, "java.lang.Double",
        "java.lang.Double", "java.lang.Double", FLOAT_ARRAY_TYPE,
        FLOAT_PROCEDURE, floatStringElements, "java.lang.Double");
  }

  public void Var133() {
    testTypeCombination(
        " -- Type FLOAT but array type is Double -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "FLOAT", doubleElements, floatStringElements, "java.lang.Double",
        "java.lang.Double", "java.lang.Double", FLOAT_ARRAY_TYPE,
        FLOAT_PROCEDURE, floatStringElements, "java.lang.Double");
  }

  public void Var134() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombination(
          " -- Type FLOAT but array type is ByteArray -- Conversion FLOAT to byte[] not supported   -- Added 01/18/2010 ",
          "FLOAT", byteArrayElements, floatStringElements, "java.lang.Double",
          "java.lang.Double", "[B", FLOAT_ARRAY_TYPE, FLOAT_PROCEDURE,
          floatStringElements, "java.lang.Double");
    } else {
      testTypeCombinationException(
          " -- Type FLOAT but array type is ByteArray -- Conversion FLOAT to byte[] not supported   -- Added 01/18/2010 ",
          "FLOAT", byteArrayElements, floatStringElements, "java.lang.Double",
          "java.lang.Double", "[B", FLOAT_ARRAY_TYPE, FLOAT_PROCEDURE,
          floatStringElements, "java.lang.Double", "Data type mismatch");
    }
  }

  public void Var135() {
    testTypeCombinationException(
        " -- Type FLOAT but array type is Date -- -- Added 01/18/2010 ",
        "FLOAT", dateElements, dateStringElements, "java.lang.Double",
        "java.lang.Double", "java.sql.Date", FLOAT_ARRAY_TYPE, FLOAT_PROCEDURE,
        dateStringElements, "java.lang.Double", "Data type mismatch");

  }

  public void Var136() {
    testTypeCombinationException(
        " -- Type FLOAT but array type is Time --  Added 01/18/2010 ", "FLOAT",
        timeElements, timeStringElements, "java.lang.Double",
        "java.lang.Double", "java.sql.Time", FLOAT_ARRAY_TYPE, FLOAT_PROCEDURE,
        timeStringElements, "java.lang.Double", "Data type mismatch");

  }

  public void Var137() {
    testTypeCombinationException(
        " -- Type FLOAT but array type is Timestamp -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "FLOAT", timestampElements, timestampStringElements, "java.lang.Double",
        "java.lang.Double", "java.sql.Timestamp", FLOAT_ARRAY_TYPE,
        FLOAT_PROCEDURE, timestampStringElements, "java.lang.Double",
        "Data type mismatch");

  }

  public void Var138() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombinationException(
          " -- Type FLOAT but array type is Blob -- Added 01/18/2010 ", "FLOAT",
          blobElements, blobStringElements, "java.lang.Double",
          "java.lang.Double", "java.sql.Blob", FLOAT_ARRAY_TYPE,
          FLOAT_PROCEDURE, blobStringElements, "java.lang.Double",
          "Illegal cross conversion");
    } else {

      testTypeCombinationException(
          " -- Type CHAR but array type is Blob -- Added 01/18/2010 ", "FLOAT",
          blobElements, blobStringElements, "java.lang.Double",
          "java.lang.Double", "java.sql.Blob", FLOAT_ARRAY_TYPE,
          FLOAT_PROCEDURE, blobStringElements, "java.lang.Double",
          "Data type mismatch");
    }
  }

  public void Var139() {
    testTypeCombinationException(
        " -- Type FLOAT but array type is Clob -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "FLOAT", clobElements, floatStringElements, "java.lang.Double",
        "java.lang.Double", "java.sql.Clob", FLOAT_ARRAY_TYPE, FLOAT_PROCEDURE,
        floatStringElements, "java.lang.Double", "Data type mismatch");
  }

  public void Var140() {

    String description = " -- Type FLOAT but array type is String -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "FLOAT", floatStringElements,
        floatStringElements, "java.lang.Double", "java.lang.Double",
        "java.math.BigDecimal", FLOAT_ARRAY_TYPE, FLOAT_PROCEDURE,
        floatStringElements, "java.lang.Double");
  }

  /**
   * CreateArrayOf() -- Create an empty array of type FLOAT
   **/

  public void Var141() {
    String description = " -- Create empty array of type DOUBLE -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        Double[] elements = new Double[0];
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "DOUBLE", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(elementsOut.length == 0,
            "Length is " + elementsOut.length + " sb 0 " + description);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var142() {
    String description = " -- Create 1 element array of type DOUBLE -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        Double[] elements = { new Double(1) };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "DOUBLE", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(
            elementsOut.length == 1
                && (1 == ((Number) elementsOut[0]).shortValue()),
            "Length is " + elementsOut.length + " sb 1 " + "elements[0]='"
                + elementsOut[0] + "' " + "sb '" + elements[0] + "' "
                + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var143() {
    String description = " -- Create 10 element array of type DOUBLE -- Note:  JCC fails this test -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Double[] elements = doubleElements;

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "DOUBLE", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Double", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(DOUBLE_ARRAY_TYPE,
            DOUBLE_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Double", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var144() {
    String description = " -- Create 10 element array of type DOUBLE and make sure array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Double[] setElements = { new Double(1), new Double(2), new Double(3),
            new Double(4), null, new Double(6), new Double(7), new Double(8),
            new Double(-9), new Double(0), };

        if (getDriver() == JDTestDriver.DRIVER_JCC || isToolboxDriver()) {
          notApplicable("jcc/TB doesn't copy array when constructing array");
          return;
        }
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "DOUBLE", setElements);
        setElements[0] = new Double(100);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Double", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(DOUBLE_ARRAY_TYPE,
            DOUBLE_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Double", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var145() {
    String description = " -- Create 10 element array of type DOUBLE and make sure ouput array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Double[] setElements = { new Double(1), new Double(2), new Double(3),
            new Double(4), null, new Double(6), new Double(7), new Double(8),
            new Double(-9), new Double(0), };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "DOUBLE", setElements);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        elementsOut[0] = new Double(99);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(arrayObject,
            "getArray");

        boolean passed1;
        passed1 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Double", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(DOUBLE_ARRAY_TYPE,
            DOUBLE_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(floatStringElements, elementsOut,
            "java.lang.Double", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var146() {

    String description = " -- Type DOUBLE but array type is BigDecimal -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "DOUBLE", bigDecimalElements,
        floatStringElements, "java.lang.Double", "java.lang.Double",
        "java.math.BigDecimal", DOUBLE_ARRAY_TYPE, DOUBLE_PROCEDURE,
        floatStringElements, "java.lang.Double");
  }

  public void Var147() {
    String description = " -- Type DOUBLE but array type is Boolean -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "DOUBLE", booleanElements,
        floatBooleanStringElements, "java.lang.Double", "java.lang.Double",
        "java.lang.Boolean", DOUBLE_ARRAY_TYPE, DOUBLE_PROCEDURE,
        floatBooleanStringElements, "java.lang.Double");

  }

  public void Var148() {
    String description = " -- Type DOUBLE but array type is Byte -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "DOUBLE", byteElements,
        floatStringElements, "java.lang.Double", "java.lang.Double",
        "java.lang.Byte", DOUBLE_ARRAY_TYPE, DOUBLE_PROCEDURE,
        floatStringElements, "java.lang.Double");
  }

  public void Var149() {
    testTypeCombination(
        " -- Type DOUBLE but array type is Short -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DOUBLE", shortElements, floatStringElements, "java.lang.Double",
        "java.lang.Double", "java.lang.Double", DOUBLE_ARRAY_TYPE,
        DOUBLE_PROCEDURE, floatStringElements, "java.lang.Double");
  }

  public void Var150() {
    testTypeCombination(
        " -- Type DOUBLE but array type is Integer -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DOUBLE", integerElements, floatStringElements, "java.lang.Double",
        "java.lang.Double", "java.lang.Double", DOUBLE_ARRAY_TYPE,
        DOUBLE_PROCEDURE, floatStringElements, "java.lang.Double");
  }

  public void Var151() {
    testTypeCombination(
        " -- Type DOUBLE but array type is Long -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DOUBLE", longElements, floatStringElements, "java.lang.Double",
        "java.lang.Double", "java.lang.Double", DOUBLE_ARRAY_TYPE,
        DOUBLE_PROCEDURE, floatStringElements, "java.lang.Double");
  }

  public void Var152() {
    testTypeCombination(
        " -- Type DOUBLE but array type is Float -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DOUBLE", floatElements, floatStringElements, "java.lang.Double",
        "java.lang.Double", "java.lang.Double", DOUBLE_ARRAY_TYPE,
        DOUBLE_PROCEDURE, floatStringElements, "java.lang.Double");
  }

  public void Var153() {
    testTypeCombination(
        " -- Type DOUBLE but array type is Double -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DOUBLE", doubleElements, floatStringElements, "java.lang.Double",
        "java.lang.Double", "java.lang.Double", DOUBLE_ARRAY_TYPE,
        DOUBLE_PROCEDURE, floatStringElements, "java.lang.Double");
  }

  public void Var154() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombination(
          " -- Type DOUBLE but array type is ByteArray -- Conversion DOUBLE to byte[] not supported   -- Added 01/18/2010 ",
          "DOUBLE", byteArrayElements, floatStringElements, "java.lang.Double",
          "java.lang.Double", "[B", DOUBLE_ARRAY_TYPE, DOUBLE_PROCEDURE,
          floatStringElements, "java.lang.Double");
    } else {
      testTypeCombinationException(
          " -- Type DOUBLE but array type is ByteArray -- Conversion DOUBLE to byte[] not supported   -- Added 01/18/2010 ",
          "DOUBLE", byteArrayElements, floatStringElements, "java.lang.Double",
          "java.lang.Double", "[B", DOUBLE_ARRAY_TYPE, DOUBLE_PROCEDURE,
          floatStringElements, "java.lang.Double", "Data type mismatch");
    }
  }

  public void Var155() {
    testTypeCombinationException(
        " -- Type DOUBLE but array type is Date -- -- Added 01/18/2010 ",
        "DOUBLE", dateElements, dateStringElements, "java.lang.Double",
        "java.lang.Double", "java.sql.Date", DOUBLE_ARRAY_TYPE,
        DOUBLE_PROCEDURE, dateStringElements, "java.lang.Double",
        "Data type mismatch");

  }

  public void Var156() {
    testTypeCombinationException(
        " -- Type DOUBLE but array type is Time --  Added 01/18/2010 ",
        "DOUBLE", timeElements, timeStringElements, "java.lang.Double",
        "java.lang.Double", "java.sql.Time", DOUBLE_ARRAY_TYPE,
        DOUBLE_PROCEDURE, timeStringElements, "java.lang.Double",
        "Data type mismatch");

  }

  public void Var157() {
    testTypeCombinationException(
        " -- Type DOUBLE but array type is Timestamp -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DOUBLE", timestampElements, timestampStringElements,
        "java.lang.Double", "java.lang.Double", "java.sql.Timestamp",
        DOUBLE_ARRAY_TYPE, DOUBLE_PROCEDURE, timestampStringElements,
        "java.lang.Double", "Data type mismatch");

  }

  public void Var158() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombinationException(
          " -- Type DOUBLE but array type is Blob -- Added 01/18/2010 ",
          "DOUBLE", blobElements, blobStringElements, "java.lang.Double",
          "java.lang.Double", "java.sql.Blob", DOUBLE_ARRAY_TYPE,
          DOUBLE_PROCEDURE, blobStringElements, "java.lang.Double",
          "Illegal cross conversion");
    } else {

      testTypeCombinationException(
          " -- Type CHAR but array type is Blob -- Added 01/18/2010 ", "DOUBLE",
          blobElements, blobStringElements, "java.lang.Double",
          "java.lang.Double", "java.sql.Blob", DOUBLE_ARRAY_TYPE,
          DOUBLE_PROCEDURE, blobStringElements, "java.lang.Double",
          "Data type mismatch");
    }
  }

  public void Var159() {
    testTypeCombinationException(
        " -- Type DOUBLE but array type is Clob -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DOUBLE", clobElements, floatStringElements, "java.lang.Double",
        "java.lang.Double", "java.sql.Clob", DOUBLE_ARRAY_TYPE,
        DOUBLE_PROCEDURE, floatStringElements, "java.lang.Double",
        "Data type mismatch");
  }

  public void Var160() {

    String description = " -- Type DOUBLE but array type is String -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "DOUBLE", floatStringElements,
        floatStringElements, "java.lang.Double", "java.lang.Double",
        "java.math.BigDecimal", DOUBLE_ARRAY_TYPE, DOUBLE_PROCEDURE,
        floatStringElements, "java.lang.Double");
  }

  /**
   * CreateArrayOf() -- Create an empty array of type DECIMAL
   **/

  public void Var161() {
    String description = " -- Create empty array of type DECIMAL -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        BigDecimal[] elements = new BigDecimal[0];
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "DECIMAL", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(elementsOut.length == 0,
            "Length is " + elementsOut.length + " sb 0 " + description);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var162() {
    String description = " -- Create 1 element array of type DECIMAL -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        BigDecimal[] elements = { new BigDecimal("1") };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "DECIMAL", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(
            elementsOut.length == 1
                && (1 == ((Number) elementsOut[0]).shortValue()),
            "Length is " + elementsOut.length + " sb 1 " + "elements[0]='"
                + elementsOut[0] + "' " + "sb '" + elements[0] + "' "
                + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var163() {
    String description = " -- Create 10 element array of type DECIMAL -- Note:  JCC fails this test -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        BigDecimal[] elements = bigDecimalElements;

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "DECIMAL", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut,
            "java.math.BigDecimal", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(DECIMAL_ARRAY_TYPE,
            DECIMAL_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(bigDecimalStringElements, elementsOut,
            "java.math.BigDecimal", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var164() {
    String description = " -- Create 10 element array of type DECIMAL and make sure array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        BigDecimal[] setElements = { new BigDecimal("1"), new BigDecimal("2"),
            new BigDecimal("3"), new BigDecimal("4"), null, new BigDecimal("6"),
            new BigDecimal("7"), new BigDecimal("8"), new BigDecimal("-9"),
            new BigDecimal("0"), };

        if (getDriver() == JDTestDriver.DRIVER_JCC || isToolboxDriver()) {
          notApplicable("jcc/TB doesn't copy array when constructing array");
          return;
        }
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "DECIMAL", setElements);
        setElements[0] = new BigDecimal("100");

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut,
            "java.math.BigDecimal", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(DECIMAL_ARRAY_TYPE,
            DECIMAL_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(bigDecimalStringElements, elementsOut,
            "java.math.BigDecimal", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var165() {
    String description = " -- Create 10 element array of type DECIMAL and make sure ouput array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        BigDecimal[] setElements = { new BigDecimal("1"), new BigDecimal("2"),
            new BigDecimal("3"), new BigDecimal("4"), null, new BigDecimal("6"),
            new BigDecimal("7"), new BigDecimal("8"), new BigDecimal("-9"),
            new BigDecimal("0"), };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "DECIMAL", setElements);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        elementsOut[0] = new BigDecimal("99");

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(arrayObject,
            "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut,
            "java.math.BigDecimal", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(DECIMAL_ARRAY_TYPE,
            DECIMAL_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(bigDecimalStringElements, elementsOut,
            "java.math.BigDecimal", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var166() {

    String description = " -- type DECIMAL but array type is BigDecimal -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "DECIMAL", bigDecimalElements,
        stringElements, "java.math.BigDecimal", "java.math.BigDecimal",
        "java.math.BigDecimal", DECIMAL_ARRAY_TYPE, DECIMAL_PROCEDURE,
        bigDecimalStringElements, "java.math.BigDecimal");
  }

  public void Var167() {
    String description = " -- type DECIMAL but array type is Boolean -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "DECIMAL", booleanElements,
        luwBooleanStringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.lang.Boolean", DECIMAL_ARRAY_TYPE,
        DECIMAL_PROCEDURE, bigDecimalBooleanStringElements,
        "java.math.BigDecimal");

  }

  public void Var168() {
    String description = " -- type DECIMAL but array type is Byte -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "DECIMAL", byteElements, stringElements,
        "java.math.BigDecimal", "java.math.BigDecimal", "java.lang.Byte",
        DECIMAL_ARRAY_TYPE, DECIMAL_PROCEDURE, bigDecimalStringElements,
        "java.math.BigDecimal");
  }

  public void Var169() {
    testTypeCombination(
        " -- type DECIMAL but array type is Short -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DECIMAL", shortElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.math.BigDecimal", DECIMAL_ARRAY_TYPE,
        DECIMAL_PROCEDURE, bigDecimalStringElements, "java.math.BigDecimal");
  }

  public void Var170() {
    testTypeCombination(
        " -- type DECIMAL but array type is Integer -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DECIMAL", integerElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.lang.Integer", DECIMAL_ARRAY_TYPE,
        DECIMAL_PROCEDURE, bigDecimalStringElements, "java.math.BigDecimal");
  }

  public void Var171() {
    testTypeCombination(
        " -- type DECIMAL but array type is Long -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DECIMAL", longElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.lang.Long", DECIMAL_ARRAY_TYPE,
        DECIMAL_PROCEDURE, bigDecimalStringElements, "java.math.BigDecimal");
  }

  public void Var172() {
    testTypeCombination(
        " -- type DECIMAL but array type is Float -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DECIMAL", floatElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.lang.Float", DECIMAL_ARRAY_TYPE,
        DECIMAL_PROCEDURE, bigDecimalStringElements, "java.math.BigDecimal");
  }

  public void Var173() {
    testTypeCombination(
        " -- type DECIMAL but array type is Double -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DECIMAL", doubleElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.long.Double", DECIMAL_ARRAY_TYPE,
        DECIMAL_PROCEDURE, bigDecimalStringElements, "java.math.BigDecimal");
  }

  public void Var174() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombination(
          " -- type DECIMAL but array type is ByteArray -- Conversion DECIMAL to byte[] not supported   -- Added 01/18/2010 ",
          "DECIMAL", byteArrayElements, stringElements, "java.math.BigDecimal",
          "java.math.BigDecimal", "[B", DECIMAL_ARRAY_TYPE, DECIMAL_PROCEDURE,
          bigDecimalStringElements, "java.math.BigDecimal");
    } else {
      testTypeCombinationException(
          " -- type DECIMAL but array type is ByteArray -- Conversion DECIMAL to byte[] not supported   -- Added 01/18/2010 ",
          "DECIMAL", byteArrayElements, stringElements, "java.math.BigDecimal",
          "java.math.BigDecimal", "[B", DECIMAL_ARRAY_TYPE, DECIMAL_PROCEDURE,
          bigDecimalStringElements, "java.math.BigDecimal",
          "Data type mismatch");
    }
  }

  public void Var175() {
    testTypeCombinationException(
        " -- type DECIMAL but array type is Date -- -- Added 01/18/2010 ",
        "DECIMAL", dateElements, dateStringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.sql.Date", DECIMAL_ARRAY_TYPE,
        DECIMAL_PROCEDURE, dateStringElements, "java.math.BigDecimal",
        "Data type mismatch");

  }

  public void Var176() {
    testTypeCombinationException(
        " -- type DECIMAL but array type is Time --  Added 01/18/2010 ",
        "DECIMAL", timeElements, timeStringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.sql.Time", DECIMAL_ARRAY_TYPE,
        DECIMAL_PROCEDURE, timeStringElements, "java.math.BigDecimal",
        "Data type mismatch");

  }

  public void Var177() {
    testTypeCombinationException(
        " -- type DECIMAL but array type is Timestamp -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DECIMAL", timestampElements, timestampStringElements,
        "java.math.BigDecimal", "java.math.BigDecimal", "java.sql.Timestamp",
        DECIMAL_ARRAY_TYPE, DECIMAL_PROCEDURE, timestampStringElements,
        "java.math.BigDecimal", "Data type mismatch");

  }

  public void Var178() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombinationException(
          " -- type DECIMAL but array type is Blob -- Added 01/18/2010 ",
          "DECIMAL", blobElements, blobStringElements, "java.math.BigDecimal",
          "java.math.BigDecimal", "java.sql.Blob", DECIMAL_ARRAY_TYPE,
          DECIMAL_PROCEDURE, blobStringElements, "java.math.BigDecimal",
          "Illegal cross conversion");
    } else {

      testTypeCombinationException(
          " -- Type CHAR but array type is Blob -- Added 01/18/2010 ",
          "DECIMAL", blobElements, blobStringElements, "java.math.BigDecimal",
          "java.math.BigDecimal", "java.sql.Blob", DECIMAL_ARRAY_TYPE,
          DECIMAL_PROCEDURE, blobStringElements, "java.math.BigDecimal",
          "Data type mismatch");
    }
  }

  public void Var179() {
    testTypeCombinationException(
        " -- type DECIMAL but array type is Clob -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DECIMAL", clobElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.sql.Clob", DECIMAL_ARRAY_TYPE,
        DECIMAL_PROCEDURE, bigDecimalStringElements, "java.math.BigDecimal",
        "Data type mismatch");
  }

  public void Var180() {

    String description = " -- type DECIMAL but array type is String -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "DECIMAL", stringElements, stringElements,
        "java.math.BigDecimal", "java.math.BigDecimal", "java.math.BigDecimal",
        DECIMAL_ARRAY_TYPE, DECIMAL_PROCEDURE, bigDecimalStringElements,
        "java.math.BigDecimal");
  }

  /**
   * CreateArrayOf() -- Create an empty array of type NUMERIC
   **/

  public void Var181() {
    String description = " -- Create empty array of type NUMERIC -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        BigDecimal[] elements = new BigDecimal[0];
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "NUMERIC", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(elementsOut.length == 0,
            "Length is " + elementsOut.length + " sb 0 " + description);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var182() {
    String description = " -- Create 1 element array of type NUMERIC -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        BigDecimal[] elements = { new BigDecimal("1") };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "NUMERIC", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(
            elementsOut.length == 1
                && (1 == ((Number) elementsOut[0]).shortValue()),
            "Length is " + elementsOut.length + " sb 1 " + "elements[0]='"
                + elementsOut[0] + "' " + "sb '" + elements[0] + "' "
                + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var183() {
    String description = " -- Create 10 element array of typeNUMERIC -- Note:  JCC fails this test -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        BigDecimal[] elements = bigDecimalElements;

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "NUMERIC", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut,
            "java.math.BigDecimal", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(DECIMAL_ARRAY_TYPE,
            DECIMAL_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(bigDecimalStringElements, elementsOut,
            "java.math.BigDecimal", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var184() {
    String description = " -- Create 10 element array of typeNUMERIC and make sure array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        BigDecimal[] setElements = { new BigDecimal("1"), new BigDecimal("2"),
            new BigDecimal("3"), new BigDecimal("4"), null, new BigDecimal("6"),
            new BigDecimal("7"), new BigDecimal("8"), new BigDecimal("-9"),
            new BigDecimal("0"), };

        if (getDriver() == JDTestDriver.DRIVER_JCC || isToolboxDriver()) {
          notApplicable("jcc/TB doesn't copy array when constructing array");
          return;
        }
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "NUMERIC", setElements);
        setElements[0] = new BigDecimal("100");

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut,
            "java.math.BigDecimal", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(DECIMAL_ARRAY_TYPE,
            DECIMAL_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(bigDecimalStringElements, elementsOut,
            "java.math.BigDecimal", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var185() {
    String description = " -- Create 10 element array of typeNUMERIC and make sure ouput array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        BigDecimal[] setElements = { new BigDecimal("1"), new BigDecimal("2"),
            new BigDecimal("3"), new BigDecimal("4"), null, new BigDecimal("6"),
            new BigDecimal("7"), new BigDecimal("8"), new BigDecimal("-9"),
            new BigDecimal("0"), };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "NUMERIC", setElements);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        elementsOut[0] = new BigDecimal("99");

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(arrayObject,
            "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut,
            "java.math.BigDecimal", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(DECIMAL_ARRAY_TYPE,
            DECIMAL_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(bigDecimalStringElements, elementsOut,
            "java.math.BigDecimal", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var186() {

    String description = " -- typeNUMERIC but array type is BigDecimal -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "NUMERIC", bigDecimalElements,
        stringElements, "java.math.BigDecimal", "java.math.BigDecimal",
        "java.math.BigDecimal", DECIMAL_ARRAY_TYPE, DECIMAL_PROCEDURE,
        bigDecimalStringElements, "java.math.BigDecimal");
  }

  public void Var187() {
    String description = " -- typeNUMERIC but array type is Boolean -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "NUMERIC", booleanElements,
        luwBooleanStringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.lang.Boolean", DECIMAL_ARRAY_TYPE,
        DECIMAL_PROCEDURE, bigDecimalBooleanStringElements,
        "java.math.BigDecimal");

  }

  public void Var188() {
    String description = " -- typeNUMERIC but array type is Byte -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "NUMERIC", byteElements, stringElements,
        "java.math.BigDecimal", "java.math.BigDecimal", "java.lang.Byte",
        DECIMAL_ARRAY_TYPE, DECIMAL_PROCEDURE, bigDecimalStringElements,
        "java.math.BigDecimal");
  }

  public void Var189() {
    testTypeCombination(
        " -- typeNUMERIC but array type is Short -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "NUMERIC", shortElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.math.BigDecimal", DECIMAL_ARRAY_TYPE,
        DECIMAL_PROCEDURE, bigDecimalStringElements, "java.math.BigDecimal");
  }

  public void Var190() {
    testTypeCombination(
        " -- type NUMERIC but array type is Integer -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "NUMERIC", integerElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.lang.Integer", NUMERIC_ARRAY_TYPE,
        NUMERIC_PROCEDURE, bigDecimalStringElements, "java.math.BigDecimal");
  }

  public void Var191() {
    testTypeCombination(
        " -- type NUMERIC but array type is Long -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "NUMERIC", longElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.lang.Long", NUMERIC_ARRAY_TYPE,
        NUMERIC_PROCEDURE, bigDecimalStringElements, "java.math.BigDecimal");
  }

  public void Var192() {
    testTypeCombination(
        " -- type NUMERIC but array type is Float -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "NUMERIC", floatElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.lang.Float", NUMERIC_ARRAY_TYPE,
        NUMERIC_PROCEDURE, bigDecimalStringElements, "java.math.BigDecimal");
  }

  public void Var193() {
    testTypeCombination(
        " -- type NUMERIC but array type is Double -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "NUMERIC", doubleElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.long.Double", NUMERIC_ARRAY_TYPE,
        NUMERIC_PROCEDURE, bigDecimalStringElements, "java.math.BigDecimal");
  }

  public void Var194() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombination(
          " -- type NUMERIC but array type is ByteArray -- Conversion NUMERIC to byte[] not supported   -- Added 01/18/2010 ",
          "NUMERIC", byteArrayElements, stringElements, "java.math.BigDecimal",
          "java.math.BigDecimal", "[B", NUMERIC_ARRAY_TYPE, NUMERIC_PROCEDURE,
          bigDecimalStringElements, "java.math.BigDecimal");
    } else {
      testTypeCombinationException(
          " -- type NUMERIC but array type is ByteArray -- Conversion NUMERIC to byte[] not supported   -- Added 01/18/2010 ",
          "NUMERIC", byteArrayElements, stringElements, "java.math.BigDecimal",
          "java.math.BigDecimal", "[B", NUMERIC_ARRAY_TYPE, NUMERIC_PROCEDURE,
          bigDecimalStringElements, "java.math.BigDecimal",
          "Data type mismatch");
    }
  }

  public void Var195() {
    testTypeCombinationException(
        " -- type NUMERIC but array type is Date -- -- Added 01/18/2010 ",
        "NUMERIC", dateElements, dateStringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.sql.Date", NUMERIC_ARRAY_TYPE,
        NUMERIC_PROCEDURE, dateStringElements, "java.math.BigDecimal",
        "Data type mismatch");

  }

  public void Var196() {
    testTypeCombinationException(
        " -- type NUMERIC but array type is Time --  Added 01/18/2010 ",
        "NUMERIC", timeElements, timeStringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.sql.Time", NUMERIC_ARRAY_TYPE,
        NUMERIC_PROCEDURE, timeStringElements, "java.math.BigDecimal",
        "Data type mismatch");

  }

  public void Var197() {
    testTypeCombinationException(
        " -- type NUMERIC but array type is Timestamp -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "NUMERIC", timestampElements, timestampStringElements,
        "java.math.BigDecimal", "java.math.BigDecimal", "java.sql.Timestamp",
        NUMERIC_ARRAY_TYPE, NUMERIC_PROCEDURE, timestampStringElements,
        "java.math.BigDecimal", "Data type mismatch");

  }

  public void Var198() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombinationException(
          " -- type NUMERIC but array type is Blob -- Added 01/18/2010 ",
          "NUMERIC", blobElements, blobStringElements, "java.math.BigDecimal",
          "java.math.BigDecimal", "java.sql.Blob", NUMERIC_ARRAY_TYPE,
          NUMERIC_PROCEDURE, blobStringElements, "java.math.BigDecimal",
          "Illegal cross conversion");
    } else {

      testTypeCombinationException(
          " -- Type CHAR but array type is Blob -- Added 01/18/2010 ",
          "NUMERIC", blobElements, blobStringElements, "java.math.BigDecimal",
          "java.math.BigDecimal", "java.sql.Blob", NUMERIC_ARRAY_TYPE,
          NUMERIC_PROCEDURE, blobStringElements, "java.math.BigDecimal",
          "Data type mismatch");
    }
  }

  public void Var199() {
    testTypeCombinationException(
        " -- type NUMERIC but array type is Clob -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "NUMERIC", clobElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.sql.Clob", NUMERIC_ARRAY_TYPE,
        NUMERIC_PROCEDURE, bigDecimalStringElements, "java.math.BigDecimal",
        "Data type mismatch");
  }

  public void Var200() {

    String description = " -- type NUMERIC but array type is String -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "NUMERIC", stringElements, stringElements,
        "java.math.BigDecimal", "java.math.BigDecimal", "java.math.BigDecimal",
        NUMERIC_ARRAY_TYPE, NUMERIC_PROCEDURE, bigDecimalStringElements,
        "java.math.BigDecimal");
  }

  /**
   * CreateArrayOf() -- Create an empty array of type DECFLOAT
   **/

  public void Var201() {
    String description = " -- Create empty array of type DECFLOAT -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        BigDecimal[] elements = new BigDecimal[0];
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "DECFLOAT", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(elementsOut.length == 0,
            "Length is " + elementsOut.length + " sb 0 " + description);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var202() {
    String description = " -- Create 1 element array of type DECFLOAT -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      try {

        BigDecimal[] elements = { new BigDecimal("1") };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "DECFLOAT", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(
            elementsOut.length == 1
                && (1 == ((Number) elementsOut[0]).shortValue()),
            "Length is " + elementsOut.length + " sb 1 " + "elements[0]='"
                + elementsOut[0] + "' " + "sb '" + elements[0] + "' "
                + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var203() {
    String description = " -- Create 10 element array of typeDECFLOAT -- Note:  JCC fails this test -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        BigDecimal[] elements = bigDecimalElements;

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "DECFLOAT", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut,
            "java.math.BigDecimal", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(DECIMAL_ARRAY_TYPE,
            DECIMAL_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(bigDecimalStringElements, elementsOut,
            "java.math.BigDecimal", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var204() {
    String description = " -- Create 10 element array of typeDECFLOAT and make sure array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        BigDecimal[] setElements = { new BigDecimal("1"), new BigDecimal("2"),
            new BigDecimal("3"), new BigDecimal("4"), null, new BigDecimal("6"),
            new BigDecimal("7"), new BigDecimal("8"), new BigDecimal("-9"),
            new BigDecimal("0"), };

        if (getDriver() == JDTestDriver.DRIVER_JCC || isToolboxDriver()) {
          notApplicable("jcc/TB doesn't copy array when constructing array");
          return;
        }
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "DECFLOAT", setElements);
        setElements[0] = new BigDecimal("100");

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut,
            "java.math.BigDecimal", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(DECIMAL_ARRAY_TYPE,
            DECIMAL_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(bigDecimalStringElements, elementsOut,
            "java.math.BigDecimal", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var205() {
    String description = " -- Create 10 element array of typeDECFLOAT and make sure ouput array is copy -- Added 01/18/2010 ";
    if (checkArraySupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        BigDecimal[] setElements = { new BigDecimal("1"), new BigDecimal("2"),
            new BigDecimal("3"), new BigDecimal("4"), null, new BigDecimal("6"),
            new BigDecimal("7"), new BigDecimal("8"), new BigDecimal("-9"),
            new BigDecimal("0"), };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "DECFLOAT", setElements);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        elementsOut[0] = new BigDecimal("99");

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(arrayObject,
            "getArray");

        boolean passed1;
        passed1 = compareArrays(stringElements, elementsOut,
            "java.math.BigDecimal", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(DECIMAL_ARRAY_TYPE,
            DECIMAL_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(bigDecimalStringElements, elementsOut,
            "java.math.BigDecimal", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var206() {

    String description = " -- typeDECFLOAT but array type is BigDecimal -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "DECFLOAT", bigDecimalElements,
        stringElements, "java.math.BigDecimal", "java.math.BigDecimal",
        "java.math.BigDecimal", DECIMAL_ARRAY_TYPE, DECIMAL_PROCEDURE,
        bigDecimalStringElements, "java.math.BigDecimal");
  }

  public void Var207() {
    String description = " -- typeDECFLOAT but array type is Boolean -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "DECFLOAT", booleanElements,
        luwBooleanStringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.lang.Boolean", DECIMAL_ARRAY_TYPE,
        DECIMAL_PROCEDURE, bigDecimalBooleanStringElements,
        "java.math.BigDecimal");

  }

  public void Var208() {
    String description = " -- typeDECFLOAT but array type is Byte -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "DECFLOAT", byteElements, stringElements,
        "java.math.BigDecimal", "java.math.BigDecimal", "java.lang.Byte",
        DECIMAL_ARRAY_TYPE, DECIMAL_PROCEDURE, bigDecimalStringElements,
        "java.math.BigDecimal");
  }

  public void Var209() {
    testTypeCombination(
        " -- typeDECFLOAT but array type is Short -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DECFLOAT", shortElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.math.BigDecimal", DECIMAL_ARRAY_TYPE,
        DECIMAL_PROCEDURE, bigDecimalStringElements, "java.math.BigDecimal");
  }

  public void Var210() {
    testTypeCombination(
        " -- type DECFLOAT but array type is Integer -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DECFLOAT", integerElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.lang.Integer", DECFLOAT_ARRAY_TYPE,
        DECFLOAT_PROCEDURE, stringElements, "java.math.BigDecimal");
  }

  public void Var211() {
    testTypeCombination(
        " -- type DECFLOAT but array type is Long -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DECFLOAT", longElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.lang.Long", DECFLOAT_ARRAY_TYPE,
        DECFLOAT_PROCEDURE, stringElements, "java.math.BigDecimal");
  }

  public void Var212() {
    testTypeCombination(
        " -- type DECFLOAT but array type is Float -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DECFLOAT", floatElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.lang.Float", DECFLOAT_ARRAY_TYPE,
        DECFLOAT_PROCEDURE, stringElements, "java.math.BigDecimal");
  }

  public void Var213() {
    testTypeCombination(
        " -- type DECFLOAT but array type is Double -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DECFLOAT", doubleElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.long.Double", DECFLOAT_ARRAY_TYPE,
        DECFLOAT_PROCEDURE, stringElements, "java.math.BigDecimal");
  }

  public void Var214() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombination(
          " -- type DECFLOAT but array type is ByteArray -- Conversion DECFLOAT to byte[] not supported   -- Added 01/18/2010 ",
          "DECFLOAT", byteArrayElements, stringElements, "java.math.BigDecimal",
          "java.math.BigDecimal", "[B", DECFLOAT_ARRAY_TYPE, DECFLOAT_PROCEDURE,
          bigDecimalStringElements, "java.math.BigDecimal");
    } else {
      testTypeCombinationException(
          " -- type DECFLOAT but array type is ByteArray -- Conversion DECFLOAT to byte[] not supported   -- Added 01/18/2010 ",
          "DECFLOAT", byteArrayElements, stringElements, "java.math.BigDecimal",
          "java.math.BigDecimal", "[B", DECFLOAT_ARRAY_TYPE, DECFLOAT_PROCEDURE,
          bigDecimalStringElements, "java.math.BigDecimal",
          "Data type mismatch");
    }
  }

  public void Var215() {
    testTypeCombinationException(
        " -- type DECFLOAT but array type is Date -- -- Added 01/18/2010 ",
        "DECFLOAT", dateElements, dateStringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.sql.Date", DECFLOAT_ARRAY_TYPE,
        DECFLOAT_PROCEDURE, dateStringElements, "java.math.BigDecimal",
        "Data type mismatch");

  }

  public void Var216() {
    testTypeCombinationException(
        " -- type DECFLOAT but array type is Time --  Added 01/18/2010 ",
        "DECFLOAT", timeElements, timeStringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.sql.Time", DECFLOAT_ARRAY_TYPE,
        DECFLOAT_PROCEDURE, timeStringElements, "java.math.BigDecimal",
        "Data type mismatch");

  }

  public void Var217() {
    testTypeCombinationException(
        " -- type DECFLOAT but array type is Timestamp -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DECFLOAT", timestampElements, timestampStringElements,
        "java.math.BigDecimal", "java.math.BigDecimal", "java.sql.Timestamp",
        DECFLOAT_ARRAY_TYPE, DECFLOAT_PROCEDURE, timestampStringElements,
        "java.math.BigDecimal", "Data type mismatch");

  }

  public void Var218() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      testTypeCombinationException(
          " -- type DECFLOAT but array type is Blob -- Added 01/18/2010 ",
          "DECFLOAT", blobElements, blobStringElements, "java.math.BigDecimal",
          "java.math.BigDecimal", "java.sql.Blob", DECFLOAT_ARRAY_TYPE,
          DECFLOAT_PROCEDURE, blobStringElements, "java.math.BigDecimal",
          "Illegal cross conversion");
    } else {

      testTypeCombinationException(
          " -- Type CHAR but array type is Blob -- Added 01/18/2010 ",
          "DECFLOAT", blobElements, blobStringElements, "java.math.BigDecimal",
          "java.math.BigDecimal", "java.sql.Blob", DECFLOAT_ARRAY_TYPE,
          DECFLOAT_PROCEDURE, blobStringElements, "java.math.BigDecimal",
          "Data type mismatch");
    }
  }

  public void Var219() {
    testTypeCombinationException(
        " -- type DECFLOAT but array type is Clob -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
        "DECFLOAT", clobElements, stringElements, "java.math.BigDecimal",
        "java.math.BigDecimal", "java.sql.Clob", DECFLOAT_ARRAY_TYPE,
        DECFLOAT_PROCEDURE, bigDecimalStringElements, "java.math.BigDecimal",
        "Data type mismatch");
  }

  public void Var220() {

    String description = " -- type DECFLOAT but array type is String -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
    testTypeCombination(description, "DECFLOAT", stringElements, stringElements,
        "java.math.BigDecimal", "java.math.BigDecimal", "java.math.BigDecimal",
        DECFLOAT_ARRAY_TYPE, DECFLOAT_PROCEDURE, stringElements,
        "java.math.BigDecimal");
  }

  /**
   * CreateArrayOf() -- Create an empty array of type BOOLEAN
   **/

  public void Var221() {
    String description = " -- Create empty array of type BOOLEAN -- Added 01/18/2010 ";
    if (checkBooleanSupport()) {

      try {

        Boolean[] elements = new Boolean[0];
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "BOOLEAN", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(elementsOut.length == 0,
            "Length is " + elementsOut.length + " sb 0 " + description);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var222() {
    String description = " -- Create 1 element array of type BOOLEAN -- Added 01/18/2010 ";
    if (checkBooleanSupport()) {

      try {

        Boolean[] elements = { new Boolean(true) };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "BOOLEAN", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        assertCondition(
            elementsOut.length == 1
                && (true == ((Boolean) elementsOut[0]).booleanValue()),
            "Length is " + elementsOut.length + " sb 1 " + "elements[0]='"
                + elementsOut[0] + "' " + "sb '" + elements[0] + "' "
                + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var223() {
    String description = " -- Create 10 element array of type BOOLEAN -- Note:  JCC fails this test -- Added 01/18/2010 ";
    if (checkBooleanSupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Boolean[] elements = booleanElements;

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "BOOLEAN", elements);
        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(booleanStringElements, elementsOut,
            "java.lang.Boolean", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(BOOLEAN_ARRAY_TYPE,
            BOOLEAN_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(booleanStringElements, elementsOut,
            "java.lang.Boolean", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var224() {
    String description = " -- Create 10 element array of typeBOOLEAN and make sure array is copy -- Added 01/18/2010 ";
    if (checkBooleanSupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Boolean[] setElements = { new Boolean("true"), new Boolean("false"),
            new Boolean("true"), new Boolean("false"), null,
            new Boolean("false"), new Boolean("true"), new Boolean("false"),
            new Boolean("true"), new Boolean("false"), };

        if (getDriver() == JDTestDriver.DRIVER_JCC || isToolboxDriver()) {
          notApplicable("jcc/TB doesn't copy array when constructing array");
          return;
        }
        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "BOOLEAN", setElements);
        setElements[0] = new Boolean("false");

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        boolean passed1;
        passed1 = compareArrays(booleanStringElements, elementsOut,
            "java.lang.Boolean", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(BOOLEAN_ARRAY_TYPE,
            BOOLEAN_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(booleanStringElements, elementsOut,
            "java.lang.Boolean", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var225() {
    String description = " -- Create 10 element array of type BOOLEAN and make sure ouput array is copy -- Added 01/18/2010 ";
    if (checkBooleanSupport()) {

      StringBuffer sb = new StringBuffer();
      try {

        Boolean[] setElements = { new Boolean(true), new Boolean(false),
            new Boolean(true), new Boolean(false), null,
            new Boolean(false), new Boolean(true), new Boolean(false),
            new Boolean(true), new Boolean(false), };

        Object arrayObject = JDReflectionUtil.callMethod_OSA(connection_,
            "createArrayOf", "BOOLEAN", setElements);

        Object[] elementsOut = (Object[]) JDReflectionUtil
            .callMethod_O(arrayObject, "getArray");

        elementsOut[0] = new Boolean(false);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(arrayObject,
            "getArray");

        boolean passed1;
        passed1 = compareArrays(booleanStringElements, elementsOut,
            "java.lang.Boolean", "InputArray", sb);

        Object outputArray = callInputOutputProcedure(BOOLEAN_ARRAY_TYPE,
            BOOLEAN_PROCEDURE, (Array) arrayObject);

        elementsOut = (Object[]) JDReflectionUtil.callMethod_O(outputArray,
            "getArray");

        boolean passed2 = compareArrays(booleanStringElements, elementsOut,
            "java.lang.Boolean", "OutputArray", sb);

        assertCondition(passed1 && passed2, sb.toString() + description);

      } catch (Exception e) {
        failed(e, "Unexpected Exception" + description);
      }
    }
  }

  public void Var226() {
    if (checkBooleanSupport()) {
      String description = " -- typeBOOLEAN but array type is BigDecimal -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
      testTypeCombination(description, "BOOLEAN", booleanElements,
          booleanStringElements, "java.lang.Boolean", "java.lang.Boolean",
          "java.lang.Boolean", BOOLEAN_ARRAY_TYPE, BOOLEAN_PROCEDURE,
          booleanStringElements, "java.lang.Boolean");
    }
  }

  public void Var227() {
    if (checkBooleanSupport()) {
      String description = " -- typeBOOLEAN but array type is Boolean -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
      testTypeCombination(description, "BOOLEAN", booleanElements,
          booleanStringElements, "java.lang.Boolean", "java.lang.Boolean",
          "java.lang.Boolean", BOOLEAN_ARRAY_TYPE, BOOLEAN_PROCEDURE,
          booleanStringElements, "java.lang.Boolean");
    }
  }

  public void Var228() {
    if (checkBooleanSupport()) {
      String description = " -- type BOOLEAN but array type is Byte";
      testTypeCombination(description, "BOOLEAN", byteElements, booleanFromShortStringElements,
          "java.lang.Boolean", "java.lang.Boolean", "java.lang.Byte",
          BOOLEAN_ARRAY_TYPE, BOOLEAN_PROCEDURE, booleanFromShortStringElements,
          "java.lang.Boolean");
    }
  }

  public void Var229() {
    if (checkBooleanSupport()) {
      testTypeCombination(
          " -- typeBOOLEAN but array type is Short -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
          "BOOLEAN", shortElements, booleanFromShortStringElements, "java.lang.Boolean",
          "java.lang.Boolean", "java.lang.Boolean", BOOLEAN_ARRAY_TYPE,
          BOOLEAN_PROCEDURE, booleanFromShortStringElements, "java.lang.Boolean");
    }
  }

  public void Var230() {
    if (checkBooleanSupport()) {
      testTypeCombination(
          " -- type BOOLEAN but array type is Integer -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
          "BOOLEAN", integerElements, booleanFromShortStringElements, "java.lang.Boolean",
          "java.lang.Boolean", "java.lang.Integer", BOOLEAN_ARRAY_TYPE,
          BOOLEAN_PROCEDURE, booleanFromShortStringElements, "java.lang.Boolean");
    }
  }

  public void Var231() {
    if (checkBooleanSupport()) {
      testTypeCombination(
          " -- type BOOLEAN but array type is Long -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
          "BOOLEAN", longElements, booleanFromShortStringElements, "java.lang.Boolean",
          "java.lang.Boolean", "java.lang.Long", BOOLEAN_ARRAY_TYPE,
          BOOLEAN_PROCEDURE, booleanFromShortStringElements, "java.lang.Boolean");
    }
  }

  public void Var232() {
    if (checkBooleanSupport()) {
      testTypeCombination(
          " -- type BOOLEAN but array type is Float -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
          "BOOLEAN", floatElements, booleanFromShortStringElements, "java.lang.Boolean",
          "java.lang.Boolean", "java.lang.Float", BOOLEAN_ARRAY_TYPE,
          BOOLEAN_PROCEDURE, booleanFromShortStringElements, "java.lang.Boolean");
    }
  }

  public void Var233() {
    if (checkBooleanSupport()) {
      testTypeCombination(
          " -- type BOOLEAN but array type is Double -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
          "BOOLEAN", doubleElements, booleanFromShortStringElements, "java.lang.Boolean",
          "java.lang.Boolean", "java.long.Double", BOOLEAN_ARRAY_TYPE,
          BOOLEAN_PROCEDURE, booleanFromShortStringElements, "java.lang.Boolean");
    }
  }

  public void Var234() {
    if (checkBooleanSupport()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        testTypeCombination(
            " -- type BOOLEAN but array type is ByteArray -- Conversion BOOLEAN to byte[] not supported   -- Added 01/18/2010 ",
            "BOOLEAN", byteArrayElements, stringElements, "java.lang.Boolean",
            "java.lang.Boolean", "[B", BOOLEAN_ARRAY_TYPE, BOOLEAN_PROCEDURE,
            booleanStringElements, "java.lang.Boolean");
      } else {
        testTypeCombinationException(
            " -- type BOOLEAN but array type is ByteArray -- Conversion BOOLEAN to byte[] not supported   -- Added 01/18/2010 ",
            "BOOLEAN", byteArrayElements, stringElements, "java.lang.Boolean",
            "java.lang.Boolean", "[B", BOOLEAN_ARRAY_TYPE, BOOLEAN_PROCEDURE,
            booleanStringElements, "java.lang.Boolean", "Data type mismatch");
      }
    }
  }

  public void Var235() {
    if (checkBooleanSupport()) {
      testTypeCombinationException(
          " -- type BOOLEAN but array type is Date -- -- Added 01/18/2010 ",
          "BOOLEAN", dateElements, dateStringElements, "java.lang.Boolean",
          "java.lang.Boolean", "java.sql.Date", BOOLEAN_ARRAY_TYPE,
          BOOLEAN_PROCEDURE, dateStringElements, "java.lang.Boolean",
          "Data type mismatch");

    }
  }

  public void Var236() {
    if (checkBooleanSupport()) {
      testTypeCombinationException(
          " -- type BOOLEAN but array type is Time --  Added 01/18/2010 ",
          "BOOLEAN", timeElements, timeStringElements, "java.lang.Boolean",
          "java.lang.Boolean", "java.sql.Time", BOOLEAN_ARRAY_TYPE,
          BOOLEAN_PROCEDURE, timeStringElements, "java.lang.Boolean",
          "Data type mismatch");

    }
  }

  public void Var237() {
    if (checkBooleanSupport()) {
      testTypeCombinationException(
          " -- type BOOLEAN but array type is Timestamp -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
          "BOOLEAN", timestampElements, timestampStringElements,
          "java.lang.Boolean", "java.lang.Boolean", "java.sql.Timestamp",
          BOOLEAN_ARRAY_TYPE, BOOLEAN_PROCEDURE, timestampStringElements,
          "java.lang.Boolean", "Data type mismatch");
    }
  }

  public void Var238() {
    if (checkBooleanSupport()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        testTypeCombinationException(
            " -- type BOOLEAN but array type is Blob -- Added 01/18/2010 ",
            "BOOLEAN", blobElements, blobStringElements, "java.lang.Boolean",
            "java.lang.Boolean", "java.sql.Blob", BOOLEAN_ARRAY_TYPE,
            BOOLEAN_PROCEDURE, blobStringElements, "java.lang.Boolean",
            "Illegal cross conversion");
      } else {

        testTypeCombinationException(
            " -- Type CHAR but array type is Blob -- Added 01/18/2010 ",
            "BOOLEAN", blobElements, blobStringElements, "java.lang.Boolean",
            "java.lang.Boolean", "java.sql.Blob", BOOLEAN_ARRAY_TYPE,
            BOOLEAN_PROCEDURE, blobStringElements, "java.lang.Boolean",
            "Data type mismatch");
      }
    }
  }

  public void Var239() {
    if (checkBooleanSupport()) {
      testTypeCombinationException(
          " -- type BOOLEAN but array type is Clob -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ",
          "BOOLEAN", clobElements, stringElements, "java.lang.Boolean",
          "java.lang.Boolean", "java.sql.Clob", BOOLEAN_ARRAY_TYPE,
          BOOLEAN_PROCEDURE, booleanStringElements, "java.lang.Boolean",
          "Data type mismatch");
    }
  }

  public void Var240() {
    if (checkBooleanSupport()) {

      String description = " -- type BOOLEAN but array type is String -- LUW doesnt check that type of array matches specified type  -- Added 01/18/2010 ";
      testTypeCombination(description, "BOOLEAN", stringElements,
          booleanFromShortStringElements, "java.lang.Boolean", "java.lang.Boolean",
          "java.lang.Boolean", BOOLEAN_ARRAY_TYPE, BOOLEAN_PROCEDURE,
          booleanFromShortStringElements, "java.lang.Boolean");
    }
  }

}
