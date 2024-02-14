///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetTypeInfo.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
//File Name:    JDDMDGetTypeInfo.java
//Classes:      JDDMDGetTypeInfo

package test.JD.DMD;
///////////////////////////////////////////////////////////////////////////////

import com.ibm.as400.access.AS400;

import test.JDDMDTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.sql.Types;
import java.util.Hashtable;

/**
 * Testcase JDDMDGetTypeInfo. This tests the following methods of the JDBC
 * DatabaseMetaData class:
 * 
 * <ul>
 * <li>getTypeInfo()
 * </ul>
 **/
public class JDDMDGetTypeInfo extends JDTestcase {

  // Private data.
  private Connection connection_;
  private Connection closedConnection_;
  private DatabaseMetaData dmd_;
  private DatabaseMetaData dmd2_;

  StringBuffer message = new StringBuffer();

  /**
   * Constructor.
   **/
  public JDDMDGetTypeInfo(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDDMDGetTypeInfo", namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    String url;

    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
      url = "jdbc:as400://" + systemObject_.getSystemName() ;
      connection_ = testDriver_.getConnection (url + ";naming=system",systemObject_.getUserId(), encryptedPassword_);
      closedConnection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

    } else if (getDriver() == JDTestDriver.DRIVER_JCC) {
      connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
      closedConnection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

    } else if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
      connection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
      closedConnection_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);

    } else {
      url = "jdbc:db2://" + systemObject_.getSystemName() ;
      connection_ = testDriver_.getConnection (url + ";naming=system",systemObject_.getUserId(), encryptedPassword_);
      closedConnection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);

    }

    dmd_ = connection_.getMetaData();

    dmd2_ = closedConnection_.getMetaData();
    closedConnection_.close();
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      connection_.commit();
    }
    connection_.close();
  }

  /**
   * getTypeInfo() - Check the result set format.
   **/
  public void Var001() {

    /* This is now tested by Var006 and Var007 */
    switch (getRelease() * 100 + getDriver()) {
    case 100 * JDTestDriver.RELEASE_V7R1M0 + JDTestDriver.DRIVER_NATIVE:
      assertCondition(true);
      return;
    }

    message.setLength(0);
    int expectedCount = 18;
    try {
      ResultSet rs = dmd_.getTypeInfo();
      ResultSetMetaData rsmd = rs.getMetaData();

      String[] expectedNames = { "TYPE_NAME", "DATA_TYPE", "PRECISION",
          "LITERAL_PREFIX", "LITERAL_SUFFIX", "CREATE_PARAMS", "NULLABLE",
          "CASE_SENSITIVE", "SEARCHABLE", "UNSIGNED_ATTRIBUTE",
          "FIXED_PREC_SCALE", "AUTO_INCREMENT", "LOCAL_TYPE_NAME",
          "MINIMUM_SCALE", "MAXIMUM_SCALE", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
          "NUM_PREC_RADIX" };
      int[] expectedTypes = { Types.VARCHAR, Types.SMALLINT, Types.INTEGER,
          Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.SMALLINT,
          Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.SMALLINT,
          Types.SMALLINT, Types.VARCHAR, Types.SMALLINT, Types.SMALLINT,
          Types.INTEGER, Types.INTEGER, Types.INTEGER };

      String[] expectedSysibmNames = { "TYPE_NAME", "DATA_TYPE", "PRECISION",
          "LITERAL_PREFIX", "LITERAL_SUFFIX", "CREATE_PARAMS", "NULLABLE",
          "CASE_SENSITIVE", "SEARCHABLE", "UNSIGNED_ATTRIBUTE",
          "FIXED_PREC_SCALE", "AUTO_INCREMENT", "LOCAL_TYPE_NAME",
          "MINIMUM_SCALE", "MAXIMUM_SCALE", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
          "NUM_PREC_RADIX", "INTERVAL_PRECISION" };
      int[] expectedSysibmTypes = { Types.VARCHAR, /* 12 */
          Types.SMALLINT, /* 5 */
          Types.INTEGER, /* 4 */
          Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.SMALLINT,
          Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.SMALLINT,
          Types.SMALLINT, Types.VARCHAR, Types.SMALLINT, Types.SMALLINT,
          Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.SMALLINT };

      int[] expectedSysibmTypes61 = { Types.VARCHAR, Types.INTEGER,
          Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
          Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.SMALLINT,
          Types.SMALLINT, Types.SMALLINT, Types.VARCHAR, Types.SMALLINT,
          Types.SMALLINT, Types.INTEGER, Types.INTEGER, Types.INTEGER,
          Types.SMALLINT };

      int[] expectedSysibmTypes71 = { Types.VARCHAR, Types.SMALLINT, /*
                                                                      * Changed
                                                                      * 9/21/
                                                                      * 2010 to
                                                                      * smallint
                                                                      */
          /* Changed 2/22/2011 to INTEGER */
          /* for java test.JDRunit 7146N JDDMDGetTypeInfo 1 */
          /* Change 1/11/2012 to smallint */
          Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
          Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.SMALLINT,
          Types.SMALLINT, Types.SMALLINT, Types.VARCHAR, Types.SMALLINT,
          Types.SMALLINT, Types.INTEGER, Types.INTEGER, Types.INTEGER,
          Types.SMALLINT };

      int[] expectedSysibmTypes7130 = { Types.VARCHAR, Types.INTEGER, /*
                                                                       * Changed
                                                                       * 9/21/
                                                                       * 2010 to
                                                                       * smallint
                                                                       */
          /* Changed 2/22/2011 to INTEGER */
          /* for java test.JDRunit 7146N JDDMDGetTypeInfo 1 */
          /* Change 1/11/2012 to smallint */
          Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
          Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.SMALLINT,
          Types.SMALLINT, Types.SMALLINT, Types.VARCHAR, Types.SMALLINT,
          Types.SMALLINT, Types.INTEGER, Types.INTEGER, Types.INTEGER,
          Types.SMALLINT };

      int[] expectedSysibmTypes71TB = { Types.VARCHAR, Types.SMALLINT, /*
                                                                        * Changed
                                                                        * 2/13/
                                                                        * 2012
                                                                        * for
                                                                        * INTEGER
                                                                        */
          /* for java test.JDRunit 7166T JDDMDGetTypeInfo 1 */
          Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
          Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.SMALLINT,
          Types.SMALLINT, Types.SMALLINT, Types.VARCHAR, Types.SMALLINT,
          Types.SMALLINT, Types.INTEGER, Types.INTEGER, Types.INTEGER,
          Types.SMALLINT };

      int[] expectedSysibmTypes71TB30 = { Types.VARCHAR, Types.INTEGER, /*
                                                                         * Changed
                                                                         * 2/24/
                                                                         * 2011
                                                                         * for
                                                                         * INTEGER
                                                                         */
          /* for java test.JDRunit 7146U JDDMDGetTypeInfo 1 */
          Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
          Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.SMALLINT,
          Types.SMALLINT, Types.SMALLINT, Types.VARCHAR, Types.SMALLINT,
          Types.SMALLINT, Types.INTEGER, Types.INTEGER, Types.INTEGER,
          Types.SMALLINT };

      int count = rsmd.getColumnCount();

      boolean namesCheck;
      if (isSysibmMetadata()) {
        namesCheck = JDDMDTest.checkColumnNames(rsmd, expectedSysibmNames,
            message);
        message.append(" checkedBy(expectedSysibmNames) ");
        expectedCount = 19;
      } else {
        namesCheck = JDDMDTest.checkColumnNames(rsmd, expectedNames, message);
        message.append(" checkedBy(expectedNames) ");
      }

      boolean typesCheck;
      if (isSysibmMetadata()) {
        if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
          if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            if (isJdbc40()) {
              typesCheck = JDDMDTest.checkColumnTypes(rsmd,
                  expectedSysibmTypes71TB, message);
              message.append(" checkedBy(expectedSysibmTypes71TB) ");
            } else {
              typesCheck = JDDMDTest.checkColumnTypes(rsmd,
                  expectedSysibmTypes71TB30, message);
              message.append(" checkedBy(expectedSysibmTypes71TB30) ");
            }
          } else {
            if (isJdbc40()) {
              typesCheck = JDDMDTest.checkColumnTypes(rsmd,
                  expectedSysibmTypes71, message);
              message.append(" checkedBy(expectedSysibmTypes71) ");
            } else {
              typesCheck = JDDMDTest.checkColumnTypes(rsmd,
                  expectedSysibmTypes7130, message);
              message.append(" checkedBy(expectedSysibmTypes7130) ");

            }
          }
        } else if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
          /* sysibm changes PTF backed 01/11/2010 */
          typesCheck = JDDMDTest.checkColumnTypes(rsmd, expectedSysibmTypes71,
              message);
          message.append(" checkedBy(expectedSysibmTypes71) ");

        } else if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
          /* sysibm changes PTF backed 01/11/2010 */
          typesCheck = JDDMDTest.checkColumnTypes(rsmd, expectedSysibmTypes61,
              message);
          message.append(" checkedBy(expectedSysibmTypes61) ");

        } else {
          typesCheck = JDDMDTest.checkColumnTypes(rsmd, expectedSysibmTypes,
              message);
          message.append(" checkedBy(expectedSysibmTypes) ");
        }
      } else {
        typesCheck = JDDMDTest.checkColumnTypes(rsmd, expectedTypes, message);
        message.append(" checkedBy(expectedType) ");
      }

      rs.close();
      boolean condition = (count == expectedCount) && (namesCheck)
          && (typesCheck);
      assertCondition(condition,
          "count = " + count + " sb " + expectedCount + " " + "namesCheck = "
              + namesCheck + "typesCheck = " + typesCheck + "\n" + message
              + "\n END OF VARIATION INFO\n");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getTypeInfo() - Should contain the expected contents.
   **/
  public void Var002() {

    try {
      ResultSet rs = dmd_.getTypeInfo();
      boolean success = true;
      String comment = "";
      boolean thisCase;
      int rows = 0;
      while (rs.next()) {
        ++rows;

        // The toolbox is going to check every single value returned. We
        // are done doing that and will rely on the values returned by the
        // CLI to be correct for our purposes. This means that our two
        // drivers do not do the same thing here.
        //
        // The native JDBC driver is changing to call the CLI because of
        // a bug reported by ShowCase corporation where we can't 'fake'
        // the ResultSet when running with commitment control.
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {

          switch (rs.getShort("DATA_TYPE")) {

          case Types.BIGINT: // @B0A
            thisCase = (rs.getString("TYPE_NAME").equals("BIGINT")) // @B0A
                && (rs.getInt("PRECISION") == 19) // @B0A
                && (rs.getString("LITERAL_PREFIX") == null) // @B0A
                && (rs.getString("LITERAL_SUFFIX") == null) // @B0A
                && (rs.getString("CREATE_PARAMS") == null) // @B0A
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable) // @B0A
                && (rs.getBoolean("CASE_SENSITIVE") == false) // @B0A
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable) // @B0A
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false) // @B0A
                && (rs.getBoolean("FIXED_PREC_SCALE") == false) // @B0A
                && (rs.getBoolean("AUTO_INCREMENT") == false) // @B0A
                && (rs.getString("LOCAL_TYPE_NAME").equals("BIGINT")) // @B0A
                && (rs.getShort("MINIMUM_SCALE") == 0) // @B0A
                && (rs.getShort("MAXIMUM_SCALE") == 0) // @B0A
                && (rs.getInt("SQL_DATA_TYPE") == 0) // @B0A
                && (rs.getInt("SQL_DATETIME_SUB") == 0) // @B0A
                && (rs.getInt("NUM_PREC_RADIX") == 10); // @B0A
            if (!thisCase)
              comment += " BIGINT IS WRONG";
            success = success & thisCase; // @B0A
            break; // @B0A

          case Types.BINARY:
            String typeName = rs.getString("TYPE_NAME");
            if (typeName.equals("BINARY")) {
              thisCase = (rs.getInt("PRECISION") == 32765)
                  && (rs.getString("LITERAL_PREFIX").equals("X'"))
                  && (rs.getString("LITERAL_SUFFIX").equals("'"))
                  && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                  && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                  && (rs.getBoolean("CASE_SENSITIVE") == true)
                  && (rs.getShort(
                      "SEARCHABLE") == DatabaseMetaData.typeSearchable)
                  && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                  && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                  && (rs.getBoolean("AUTO_INCREMENT") == false)
                  && (rs.getString("LOCAL_TYPE_NAME").equals("BINARY"))
                  && (rs.getShort("MINIMUM_SCALE") == 0)
                  && (rs.getShort("MAXIMUM_SCALE") == 0)
                  && (rs.getInt("SQL_DATA_TYPE") == 0)
                  && (rs.getInt("SQL_DATETIME_SUB") == 0)
                  && (rs.getInt("NUM_PREC_RADIX") == 0);
              if (!thisCase)
                comment += " BINARY IS WRONG";
              success = success & thisCase;
            } else if (typeName.equals("CHAR() FOR BIT DATA")
                || typeName.equals("CHAR () FOR BIT DATA")) {
              thisCase = (rs.getInt("PRECISION") == 32765)
                  && (rs.getString("LITERAL_PREFIX").equals("X'"))
                  && (rs.getString("LITERAL_SUFFIX").equals("'"))
                  && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                  && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                  && (rs.getBoolean("CASE_SENSITIVE") == true)
                  && (rs.getShort(
                      "SEARCHABLE") == DatabaseMetaData.typeSearchable)
                  && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                  && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                  && (rs.getBoolean("AUTO_INCREMENT") == false)
                  && (rs.getString("LOCAL_TYPE_NAME")
                      .equals("CHAR() FOR BIT DATA")
                      || rs.getString("LOCAL_TYPE_NAME")
                          .equals("CHAR () FOR BIT DATA")) // @K5C changed from
                                                           // CHAR
                  && (rs.getShort("MINIMUM_SCALE") == 0)
                  && (rs.getShort("MAXIMUM_SCALE") == 0)
                  && (rs.getInt("SQL_DATA_TYPE") == 0)
                  && (rs.getInt("SQL_DATETIME_SUB") == 0)
                  && (rs.getInt("NUM_PREC_RADIX") == 0);
              if (!thisCase)
                comment += " BINARY/CHAR() FOR BIT DATA IS WRONG";
              success = success & thisCase;
            } else if (typeName.equals("ROWID")) {
              thisCase = (rs.getInt("PRECISION") == 40)
                  && (rs.getString("LITERAL_PREFIX") == null)
                  && (rs.getString("LITERAL_SUFFIX") == null)
                  && (rs.getString("CREATE_PARAMS") == null)
                  && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                  && (rs.getBoolean("CASE_SENSITIVE") == true)
                  && (rs.getShort(
                      "SEARCHABLE") == DatabaseMetaData.typeSearchable)
                  && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                  && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                  && (rs.getBoolean("AUTO_INCREMENT") == false)
                  && (rs.getString("LOCAL_TYPE_NAME").equals("ROWID"))
                  && (rs.getShort("MINIMUM_SCALE") == 0)
                  && (rs.getShort("MAXIMUM_SCALE") == 0)
                  && (rs.getInt("SQL_DATA_TYPE") == 0)
                  && (rs.getInt("SQL_DATETIME_SUB") == 0)
                  && (rs.getInt("NUM_PREC_RADIX") == 0);
              if (!thisCase)
                comment += " BINARY/ROWID IS WRONG";
              success = success & thisCase;
            } else {
              comment += " BINARY/" + typeName + " is UNRECOGNIZED";
              success = false;
            }
            break;

          case Types.BLOB:
            thisCase = (rs.getString("TYPE_NAME").equals("BLOB"))
                && (rs.getInt("PRECISION") == 2147483646)
                && (rs.getString("LITERAL_PREFIX") == null)
                && (rs.getString("LITERAL_SUFFIX") == null)
                && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                && (rs.getBoolean("CASE_SENSITIVE") == true)
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable)
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                && (rs.getBoolean("AUTO_INCREMENT") == false)
                && (rs.getString("LOCAL_TYPE_NAME").equals("BLOB"))
                && (rs.getShort("MINIMUM_SCALE") == 0)
                && (rs.getShort("MAXIMUM_SCALE") == 0)
                && (rs.getInt("SQL_DATA_TYPE") == 0)
                && (rs.getInt("SQL_DATETIME_SUB") == 0)
                && (rs.getInt("NUM_PREC_RADIX") == 0);
            if (!thisCase)
              comment += " BLOB IS WRONG";
            success = success & thisCase;
            break;

          case Types.CHAR:
            typeName = rs.getString("TYPE_NAME");
            if (typeName.equals("CHAR")) {
              thisCase = (rs.getInt("PRECISION") == 32765)
                  && (rs.getString("LITERAL_PREFIX").equals("'"))
                  && (rs.getString("LITERAL_SUFFIX").equals("'"))
                  && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                  && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                  && (rs.getBoolean("CASE_SENSITIVE") == true)
                  && (rs.getShort(
                      "SEARCHABLE") == DatabaseMetaData.typeSearchable)
                  && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                  && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                  && (rs.getBoolean("AUTO_INCREMENT") == false)
                  && (rs.getString("LOCAL_TYPE_NAME").equals("CHAR"))
                  && (rs.getShort("MINIMUM_SCALE") == 0)
                  && (rs.getShort("MAXIMUM_SCALE") == 0)
                  && (rs.getInt("SQL_DATA_TYPE") == 0)
                  && (rs.getInt("SQL_DATETIME_SUB") == 0)
                  && (rs.getInt("NUM_PREC_RADIX") == 0);
              if (!thisCase)
                comment += " CHAR IS WRONG";
              success = success & thisCase;
            } else if (typeName.equals("GRAPHIC")) {
              thisCase = (rs.getInt("PRECISION") == 16382)
                  && (rs.getString("LITERAL_PREFIX").equals("'"))
                  && (rs.getString("LITERAL_SUFFIX").equals("'"))
                  && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                  && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                  && (rs.getBoolean("CASE_SENSITIVE") == true)
                  && (rs.getShort(
                      "SEARCHABLE") == DatabaseMetaData.typeSearchable)
                  && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                  && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                  && (rs.getBoolean("AUTO_INCREMENT") == false)
                  && (rs.getString("LOCAL_TYPE_NAME").equals("GRAPHIC"))
                  && (rs.getShort("MINIMUM_SCALE") == 0)
                  && (rs.getShort("MAXIMUM_SCALE") == 0)
                  && (rs.getInt("SQL_DATA_TYPE") == 0)
                  && (rs.getInt("SQL_DATETIME_SUB") == 0)
                  && (rs.getInt("NUM_PREC_RADIX") == 0);
              if (!thisCase)
                comment += " CHAR/GRAPHIC IS WRONG";
              success = success & thisCase;
            } else {
              comment += " CHAR/" + typeName + " is UNRECOGNIZED";
              success = false;
            }

            break;

          case Types.CLOB:
          case 2011: /* NCLOB */
            typeName = rs.getString("TYPE_NAME");
            if (typeName.equals("CLOB")) {
              thisCase = (rs.getInt("PRECISION") == 2147483646)
                  && (rs.getString("LITERAL_PREFIX") == null)
                  && (rs.getString("LITERAL_SUFFIX") == null)
                  && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                  && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                  && (rs.getBoolean("CASE_SENSITIVE") == true)
                  && (rs.getShort(
                      "SEARCHABLE") == DatabaseMetaData.typeSearchable)
                  && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                  && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                  && (rs.getBoolean("AUTO_INCREMENT") == false)
                  && (rs.getString("LOCAL_TYPE_NAME").equals("CLOB"))
                  && (rs.getShort("MINIMUM_SCALE") == 0)
                  && (rs.getShort("MAXIMUM_SCALE") == 0)
                  && (rs.getInt("SQL_DATA_TYPE") == 0)
                  && (rs.getInt("SQL_DATETIME_SUB") == 0)
                  && (rs.getInt("NUM_PREC_RADIX") == 0);
              if (!thisCase)
                comment += " CLOB IS WRONG";
              success = success & thisCase;
            } else if (typeName.equals("DBCLOB")) {
              thisCase = (rs.getInt("PRECISION") == 1073741822)
                  && (rs.getString("LITERAL_PREFIX") == null)
                  && (rs.getString("LITERAL_SUFFIX") == null)
                  && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                  && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                  && (rs.getBoolean("CASE_SENSITIVE") == true)
                  && (rs.getShort(
                      "SEARCHABLE") == DatabaseMetaData.typeSearchable)
                  && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                  && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                  && (rs.getBoolean("AUTO_INCREMENT") == false)
                  && (rs.getString("LOCAL_TYPE_NAME").equals("DBCLOB"))
                  && (rs.getShort("MINIMUM_SCALE") == 0)
                  && (rs.getShort("MAXIMUM_SCALE") == 0)
                  && (rs.getInt("SQL_DATA_TYPE") == 0)
                  && (rs.getInt("SQL_DATETIME_SUB") == 0)
                  && (rs.getInt("NUM_PREC_RADIX") == 0);
              if (!thisCase)
                comment += " CLOB/DBCLOB IS WRONG";
              success = success & thisCase;
            } else {
              comment += " CLOB/" + typeName + " is UNRECOGNIZED";
              success = false;
            }

            break;

          case Types.DATE:
            thisCase = (rs.getString("TYPE_NAME").equals("DATE"))
                && (rs.getInt("PRECISION") == 10)
                && (rs.getString("LITERAL_PREFIX").equals("'"))
                && (rs.getString("LITERAL_SUFFIX").equals("'"))
                && (rs.getString("CREATE_PARAMS") == null)
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                && (rs.getBoolean("CASE_SENSITIVE") == false)
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable)
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                && (rs.getBoolean("AUTO_INCREMENT") == false)
                && (rs.getString("LOCAL_TYPE_NAME").equals("DATE"))
                && (rs.getShort("MINIMUM_SCALE") == 0)
                && (rs.getShort("MAXIMUM_SCALE") == 0)
                && (rs.getInt("SQL_DATA_TYPE") == 0)
                && (rs.getInt("SQL_DATETIME_SUB") == 0)
                && (rs.getInt("NUM_PREC_RADIX") == 10);
            if (!thisCase)
              comment += " DATE IS WRONG";
            success = success & thisCase;
            break;

          case Types.DECIMAL:
            thisCase = (rs.getString("TYPE_NAME").equals("DECIMAL"))
                && (rs.getInt("PRECISION") == 31)
                && (rs.getString("LITERAL_PREFIX") == null)
                && (rs.getString("LITERAL_SUFFIX") == null)
                && (rs.getString("CREATE_PARAMS").equals("PRECISION,SCALE"))
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                && (rs.getBoolean("CASE_SENSITIVE") == false)
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable)
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false)
                && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                && (rs.getBoolean("AUTO_INCREMENT") == false)
                && (rs.getString("LOCAL_TYPE_NAME").equals("DECIMAL"))
                && (rs.getShort("MINIMUM_SCALE") == 0)
                && (rs.getShort("MAXIMUM_SCALE") == 31)
                && (rs.getInt("SQL_DATA_TYPE") == 0)
                && (rs.getInt("SQL_DATETIME_SUB") == 0)
                && (rs.getInt("NUM_PREC_RADIX") == 10);
            if (!thisCase)
              comment += " DECIMAL IS WRONG";
            success = success & thisCase;
            break;

          case Types.DOUBLE:
            thisCase = (rs.getString("TYPE_NAME").equals("DOUBLE"))
                && (rs.getInt("PRECISION") == 53)
                && (rs.getString("LITERAL_PREFIX") == null)
                && (rs.getString("LITERAL_SUFFIX") == null)
                && (rs.getString("CREATE_PARAMS") == null)
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                && (rs.getBoolean("CASE_SENSITIVE") == false)
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable)
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false)
                && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                && (rs.getBoolean("AUTO_INCREMENT") == false)
                && (rs.getString("LOCAL_TYPE_NAME").equals("FLOAT"))
                && (rs.getShort("MINIMUM_SCALE") == 0)
                && (rs.getShort("MAXIMUM_SCALE") == 0)
                && (rs.getInt("SQL_DATA_TYPE") == 0)
                && (rs.getInt("SQL_DATETIME_SUB") == 0)
                && (rs.getInt("NUM_PREC_RADIX") == 2);
            if (!thisCase)
              comment += " DOUBLE IS WRONG";
            success = success & thisCase;
            break;

          case Types.FLOAT:
            thisCase = (rs.getString("TYPE_NAME").equals("FLOAT"))
                && (rs.getInt("PRECISION") == 53)
                && (rs.getString("LITERAL_PREFIX") == null)
                && (rs.getString("LITERAL_SUFFIX") == null)
                && (rs.getString("CREATE_PARAMS") == null)
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                && (rs.getBoolean("CASE_SENSITIVE") == false)
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable)
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false)
                && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                && (rs.getBoolean("AUTO_INCREMENT") == false)
                && (rs.getString("LOCAL_TYPE_NAME").equals("FLOAT"))
                && (rs.getShort("MINIMUM_SCALE") == 0)
                && (rs.getShort("MAXIMUM_SCALE") == 0)
                && (rs.getInt("SQL_DATA_TYPE") == 0)
                && (rs.getInt("SQL_DATETIME_SUB") == 0)
                && (rs.getInt("NUM_PREC_RADIX") == 2);
            if (!thisCase)
              comment += " FLOAT IS WRONG";
            success = success & thisCase;
            break;

          case Types.INTEGER:
            thisCase = (rs.getString("TYPE_NAME").equals("INTEGER"))
                && (rs.getInt("PRECISION") == 10)
                && (rs.getString("LITERAL_PREFIX") == null)
                && (rs.getString("LITERAL_SUFFIX") == null)
                && (rs.getString("CREATE_PARAMS") == null)
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                && (rs.getBoolean("CASE_SENSITIVE") == false)
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable)
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false)
                && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                && (rs.getBoolean("AUTO_INCREMENT") == false)
                && (rs.getString("LOCAL_TYPE_NAME").equals("INTEGER"))
                && (rs.getShort("MINIMUM_SCALE") == 0)
                && (rs.getShort("MAXIMUM_SCALE") == 0)
                && (rs.getInt("SQL_DATA_TYPE") == 0)
                && (rs.getInt("SQL_DATETIME_SUB") == 0)
                && (rs.getInt("NUM_PREC_RADIX") == 10);
            if (!thisCase)
              comment += " INTEGER IS WRONG";
            success = success & thisCase;
            break;

          case Types.NUMERIC:
            thisCase = (rs.getString("TYPE_NAME").equals("NUMERIC"))
                && (rs.getInt("PRECISION") == 31)
                && (rs.getString("LITERAL_PREFIX") == null)
                && (rs.getString("LITERAL_SUFFIX") == null)
                && (rs.getString("CREATE_PARAMS").equals("PRECISION,SCALE"))
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                && (rs.getBoolean("CASE_SENSITIVE") == false)
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable)
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false)
                && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                && (rs.getBoolean("AUTO_INCREMENT") == false)
                && (rs.getString("LOCAL_TYPE_NAME").equals("NUMERIC"))
                && (rs.getShort("MINIMUM_SCALE") == 0)
                && (rs.getShort("MAXIMUM_SCALE") == 31)
                && (rs.getInt("SQL_DATA_TYPE") == 0)
                && (rs.getInt("SQL_DATETIME_SUB") == 0)
                && (rs.getInt("NUM_PREC_RADIX") == 10);
            if (!thisCase)
              comment += " NUMERIC IS WRONG";
            success = success & thisCase;
            break;

          case Types.REAL:
            thisCase = (rs.getString("TYPE_NAME").equals("REAL"))
                && (rs.getInt("PRECISION") == 24)
                && (rs.getString("LITERAL_PREFIX") == null)
                && (rs.getString("LITERAL_SUFFIX") == null)
                && (rs.getString("CREATE_PARAMS") == null)
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                && (rs.getBoolean("CASE_SENSITIVE") == false)
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable)
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false)
                && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                && (rs.getBoolean("AUTO_INCREMENT") == false)
                && (rs.getString("LOCAL_TYPE_NAME").equals("REAL"))
                && (rs.getShort("MINIMUM_SCALE") == 0)
                && (rs.getShort("MAXIMUM_SCALE") == 0)
                && (rs.getInt("SQL_DATA_TYPE") == 0)
                && (rs.getInt("SQL_DATETIME_SUB") == 0)
                && (rs.getInt("NUM_PREC_RADIX") == 2);
            if (!thisCase)
              comment += " REAL IS WRONG";
            success = success & thisCase;
            break;

          case Types.SMALLINT:
            thisCase = (rs.getString("TYPE_NAME").equals("SMALLINT"))
                && (rs.getInt("PRECISION") == 5)
                && (rs.getString("LITERAL_PREFIX") == null)
                && (rs.getString("LITERAL_SUFFIX") == null)
                && (rs.getString("CREATE_PARAMS") == null)
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                && (rs.getBoolean("CASE_SENSITIVE") == false)
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable)
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false)
                && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                && (rs.getBoolean("AUTO_INCREMENT") == false)
                && (rs.getString("LOCAL_TYPE_NAME").equals("SMALLINT"))
                && (rs.getShort("MINIMUM_SCALE") == 0)
                && (rs.getShort("MAXIMUM_SCALE") == 0)
                && (rs.getInt("SQL_DATA_TYPE") == 0)
                && (rs.getInt("SQL_DATETIME_SUB") == 0)
                && (rs.getInt("NUM_PREC_RADIX") == 10);
            if (!thisCase)
              comment += " SMALLINT IS WRONG";
            success = success & thisCase;
            break;

          case Types.TIME:
            thisCase = (rs.getString("TYPE_NAME").equals("TIME"))
                && (rs.getInt("PRECISION") == 8)
                && (rs.getString("LITERAL_PREFIX").equals("'"))
                && (rs.getString("LITERAL_SUFFIX").equals("'"))
                && (rs.getString("CREATE_PARAMS") == null)
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                && (rs.getBoolean("CASE_SENSITIVE") == false)
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable)
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                && (rs.getBoolean("AUTO_INCREMENT") == false)
                && (rs.getString("LOCAL_TYPE_NAME").equals("TIME"))
                && (rs.getShort("MINIMUM_SCALE") == 0)
                && (rs.getShort("MAXIMUM_SCALE") == 0)
                && (rs.getInt("SQL_DATA_TYPE") == 0)
                && (rs.getInt("SQL_DATETIME_SUB") == 0)
                && (rs.getInt("NUM_PREC_RADIX") == 10);
            break;

          case Types.TIMESTAMP:
            thisCase = (rs.getString("TYPE_NAME").equals("TIMESTAMP"))
                && (rs.getInt("PRECISION") == 26)
                && (rs.getString("LITERAL_PREFIX").equals("'"))
                && (rs.getString("LITERAL_SUFFIX").equals("'"))
                && (rs.getString("CREATE_PARAMS") == null)
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                && (rs.getBoolean("CASE_SENSITIVE") == false)
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable)
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                && (rs.getBoolean("AUTO_INCREMENT") == false)
                && (rs.getString("LOCAL_TYPE_NAME").equals("TIMESTAMP"))
                && (rs.getShort("MINIMUM_SCALE") == 6)
                && (rs.getShort("MAXIMUM_SCALE") == 6)
                && (rs.getInt("SQL_DATA_TYPE") == 0)
                && (rs.getInt("SQL_DATETIME_SUB") == 0)
                && (rs.getInt("NUM_PREC_RADIX") == 10);
            if (!thisCase)
              comment += " TIME IS WRONG";
            success = success & thisCase;
            break;

          case Types.VARBINARY:
            typeName = rs.getString("TYPE_NAME");
            if (typeName.equals("VARBINARY")) {
              thisCase = (rs.getInt("PRECISION") == 32739)
                  && (rs.getString("LITERAL_PREFIX").equals("X'"))
                  && (rs.getString("LITERAL_SUFFIX").equals("'"))
                  && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                  && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                  && (rs.getBoolean("CASE_SENSITIVE") == true)
                  && (rs.getShort(
                      "SEARCHABLE") == DatabaseMetaData.typeSearchable)
                  && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                  && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                  && (rs.getBoolean("AUTO_INCREMENT") == false)
                  && (rs.getString("LOCAL_TYPE_NAME").equals("VARBINARY"))
                  && (rs.getShort("MINIMUM_SCALE") == 0)
                  && (rs.getShort("MAXIMUM_SCALE") == 0)
                  && (rs.getInt("SQL_DATA_TYPE") == 0)
                  && (rs.getInt("SQL_DATETIME_SUB") == 0)
                  && (rs.getInt("NUM_PREC_RADIX") == 0);
              if (!thisCase)
                comment += " VARBINARY IS WRONG";
              success = success & thisCase;
            } else if (typeName.equals("VARCHAR() FOR BIT DATA")
                || typeName.equals("VARCHAR () FOR BIT DATA")) {
              thisCase = (rs.getInt("PRECISION") == 32739)
                  && (rs.getString("LITERAL_PREFIX").equals("X'"))
                  && (rs.getString("LITERAL_SUFFIX").equals("'"))
                  && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                  && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                  && (rs.getBoolean("CASE_SENSITIVE") == true)
                  && (rs.getShort(
                      "SEARCHABLE") == DatabaseMetaData.typeSearchable)
                  && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                  && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                  && (rs.getBoolean("AUTO_INCREMENT") == false)
                  && (rs.getString("LOCAL_TYPE_NAME")
                      .equals("VARCHAR() FOR BIT DATA")
                      || rs.getString("LOCAL_TYPE_NAME")
                          .equals("VARCHAR () FOR BIT DATA")) // @K5A changed
                                                              // from VARCHAR
                  && (rs.getShort("MINIMUM_SCALE") == 0)
                  && (rs.getShort("MAXIMUM_SCALE") == 0)
                  && (rs.getInt("SQL_DATA_TYPE") == 0)
                  && (rs.getInt("SQL_DATETIME_SUB") == 0)
                  && (rs.getInt("NUM_PREC_RADIX") == 0);
              if (!thisCase)
                comment += " VARBINARY/VARCHAR() FOR BIT DATA IS WRONG";
              success = success & thisCase;
            } else {
              comment += " VARBINARY/" + typeName + " is UNRECOGNIZED";
              success = false;
            }
            break;

          case Types.LONGVARBINARY:
            thisCase = rs.getString("TYPE_NAME")
                .equals("LONG VARCHAR FOR BIT DATA")
                && (rs.getInt("PRECISION") == 32739)
                && (rs.getString("LITERAL_PREFIX").equals("X'"))
                && (rs.getString("LITERAL_SUFFIX").equals("'"))
                && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                && (rs.getBoolean("CASE_SENSITIVE") == true)
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable)
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                && (rs.getBoolean("AUTO_INCREMENT") == false)
                && (rs.getString("LOCAL_TYPE_NAME").equals("LONG VARCHAR"))
                && (rs.getShort("MINIMUM_SCALE") == 0)
                && (rs.getShort("MAXIMUM_SCALE") == 0)
                && (rs.getInt("SQL_DATA_TYPE") == 0)
                && (rs.getInt("SQL_DATETIME_SUB") == 0)
                && (rs.getInt("NUM_PREC_RADIX") == 0);

            if (!thisCase)
              comment += " VARBINARY IS WRONG";
            success = success & thisCase;
            break;

          case Types.VARCHAR:
            typeName = rs.getString("TYPE_NAME");
            if (typeName.equals("VARCHAR")) {
              thisCase = (rs.getInt("PRECISION") == 32739)
                  && (rs.getString("LITERAL_PREFIX").equals("'"))
                  && (rs.getString("LITERAL_SUFFIX").equals("'"))
                  && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                  && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                  && (rs.getBoolean("CASE_SENSITIVE") == true)
                  && (rs.getShort(
                      "SEARCHABLE") == DatabaseMetaData.typeSearchable)
                  && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                  && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                  && (rs.getBoolean("AUTO_INCREMENT") == false)
                  && (rs.getString("LOCAL_TYPE_NAME").equals("VARCHAR"))
                  && (rs.getShort("MINIMUM_SCALE") == 0)
                  && (rs.getShort("MAXIMUM_SCALE") == 0)
                  && (rs.getInt("SQL_DATA_TYPE") == 0)
                  && (rs.getInt("SQL_DATETIME_SUB") == 0)
                  && (rs.getInt("NUM_PREC_RADIX") == 0);
              if (!thisCase)
                comment += " VARCHAR/VARCHAR IS WRONG";
              success = success & thisCase;

            } else if (typeName.equals("DATALINK")) {
              thisCase = (rs.getInt("PRECISION") == 32739)
                  && (rs.getString("LITERAL_PREFIX").equals("'"))
                  && (rs.getString("LITERAL_SUFFIX").equals("'"))
                  && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                  && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                  && (rs.getBoolean("CASE_SENSITIVE") == true)
                  && (rs.getShort(
                      "SEARCHABLE") == DatabaseMetaData.typeSearchable)
                  && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                  && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                  && (rs.getBoolean("AUTO_INCREMENT") == false)
                  && (rs.getString("LOCAL_TYPE_NAME").equals("DATALINK"))
                  && (rs.getShort("MINIMUM_SCALE") == 0)
                  && (rs.getShort("MAXIMUM_SCALE") == 0)
                  && (rs.getInt("SQL_DATA_TYPE") == 0)
                  && (rs.getInt("SQL_DATETIME_SUB") == 0)
                  && (rs.getInt("NUM_PREC_RADIX") == 0);
              if (!thisCase)
                comment += " VARCHAR/DATALINK IS WRONG";
              success = success & thisCase;

            } else if (typeName.equals("VARGRAPHIC")) {
              thisCase = (rs.getInt("PRECISION") == 16369)
                  && (rs.getString("LITERAL_PREFIX").equals("'"))
                  && (rs.getString("LITERAL_SUFFIX").equals("'"))
                  && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                  && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                  && (rs.getBoolean("CASE_SENSITIVE") == true)
                  && (rs.getShort(
                      "SEARCHABLE") == DatabaseMetaData.typeSearchable)
                  && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                  && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                  && (rs.getBoolean("AUTO_INCREMENT") == false)
                  && (rs.getString("LOCAL_TYPE_NAME").equals("VARGRAPHIC"))
                  && (rs.getShort("MINIMUM_SCALE") == 0)
                  && (rs.getShort("MAXIMUM_SCALE") == 0)
                  && (rs.getInt("SQL_DATA_TYPE") == 0)
                  && (rs.getInt("SQL_DATETIME_SUB") == 0)
                  && (rs.getInt("NUM_PREC_RADIX") == 0);
              if (!thisCase)
                comment += " VARCHAR/VARGRAPHIC IS WRONG";
              success = success & thisCase;
            } else {
              comment += " VARCHAR/" + typeName + " is UNRECOGNIZED";
              success = false;
            }

            break;

          case Types.LONGVARCHAR:
            typeName = rs.getString("TYPE_NAME");
            if (typeName.equals("LONG VARCHAR")) {
              thisCase = (rs.getInt("PRECISION") == 32739)
                  && (rs.getString("LITERAL_PREFIX").equals("'"))
                  && (rs.getString("LITERAL_SUFFIX").equals("'"))
                  && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                  && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                  && (rs.getBoolean("CASE_SENSITIVE") == true)
                  && (rs.getShort(
                      "SEARCHABLE") == DatabaseMetaData.typeSearchable)
                  && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                  && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                  && (rs.getBoolean("AUTO_INCREMENT") == false)
                  && (rs.getString("LOCAL_TYPE_NAME").equals("LONG VARCHAR"))
                  && (rs.getShort("MINIMUM_SCALE") == 0)
                  && (rs.getShort("MAXIMUM_SCALE") == 0)
                  && (rs.getInt("SQL_DATA_TYPE") == 0)
                  && (rs.getInt("SQL_DATETIME_SUB") == 0)
                  && (rs.getInt("NUM_PREC_RADIX") == 0);
              if (!thisCase)
                comment += " LONGVARCHAR/LONG VARCHAR IS WRONG";
              success = success & thisCase;
            } else if (typeName.equals("LONG VARGRAPHIC")) {
              thisCase = (rs.getInt("PRECISION") == 16369)
                  && (rs.getString("LITERAL_PREFIX").equals("'"))
                  && (rs.getString("LITERAL_SUFFIX").equals("'"))
                  && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                  && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                  && (rs.getBoolean("CASE_SENSITIVE") == true)
                  && (rs.getShort(
                      "SEARCHABLE") == DatabaseMetaData.typeSearchable)
                  && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                  && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                  && (rs.getBoolean("AUTO_INCREMENT") == false)
                  && (rs.getString("LOCAL_TYPE_NAME").equals("LONG VARGRAPHIC"))
                  && (rs.getShort("MINIMUM_SCALE") == 0)
                  && (rs.getShort("MAXIMUM_SCALE") == 0)
                  && (rs.getInt("SQL_DATA_TYPE") == 0)
                  && (rs.getInt("SQL_DATETIME_SUB") == 0)
                  && (rs.getInt("NUM_PREC_RADIX") == 0);
              if (!thisCase)
                comment += " VARCHAR/DATALINK IS WRONG";
              success = success & thisCase;
            } else {
              comment += " VARCHAR/" + typeName + " is UNRECOGNIZED";
              success = false;
            }
            break;

          case ((short) 70 & 0x7fff): // DATALINK in java.sql.Types without
                                      // requiring 1.4
            thisCase = (rs.getInt("PRECISION") == 32717)
                && (rs.getString("LITERAL_PREFIX").equals("'"))
                && (rs.getString("LITERAL_SUFFIX").equals("'"))
                && (rs.getString("CREATE_PARAMS").equals("MAX LENGTH"))
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable)
                && (rs.getBoolean("CASE_SENSITIVE") == true)
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable)
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true)
                && (rs.getBoolean("FIXED_PREC_SCALE") == false)
                && (rs.getBoolean("AUTO_INCREMENT") == false)
                && (rs.getString("LOCAL_TYPE_NAME").equals("DATALINK"))
                && (rs.getShort("MINIMUM_SCALE") == 0)
                && (rs.getShort("MAXIMUM_SCALE") == 0)
                && (rs.getInt("SQL_DATA_TYPE") == 0)
                && (rs.getInt("SQL_DATETIME_SUB") == 0)
                && (rs.getInt("NUM_PREC_RADIX") == 0);
            if (!thisCase)
              comment += " DATALINK IS WRONG";
            success = success & thisCase;

            break;

          case -8: // ROWID in java.sql.Types without requiring java 1.6
            thisCase = (rs.getString("TYPE_NAME").equals("ROWID")) // @L1A
                && (rs.getInt("PRECISION") == 40) // @L1A
                && (rs.getString("LITERAL_PREFIX") == null) // @L1A
                && (rs.getString("LITERAL_SUFFIX") == null) // @L1A
                && (rs.getString("CREATE_PARAMS") == null) // @L1A
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable) // @L1A
                && (rs.getBoolean("CASE_SENSITIVE") == true) // @L1A
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable) // @L1A
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true) // @L1A
                && (rs.getBoolean("FIXED_PREC_SCALE") == false) // @L1A
                && (rs.getBoolean("AUTO_INCREMENT") == false) // @L1A
                && (rs.getString("LOCAL_TYPE_NAME").equals("ROWID")) // @L1A
                && (rs.getShort("MINIMUM_SCALE") == 0) // @L1A
                && (rs.getShort("MAXIMUM_SCALE") == 0) // @L1A
                && (rs.getInt("SQL_DATA_TYPE") == 0) // @L1A
                && (rs.getInt("SQL_DATETIME_SUB") == 0) // @L1A
                && (rs.getInt("NUM_PREC_RADIX") == 0); // @L1A
            if (!thisCase)
              comment += " ROWID IS WRONG";
            success = success & thisCase; // @L1A
            break;
          case Types.BOOLEAN: // ROWID in java.sql.Types without requiring java 1.6
            thisCase = (rs.getString("TYPE_NAME").equals("BOOLEAN")) // @L1A
                && (rs.getInt("PRECISION") == 1) // @L1A
                && (rs.getString("LITERAL_PREFIX") == null) // @L1A
                && (rs.getString("LITERAL_SUFFIX") == null) // @L1A
                && (rs.getString("CREATE_PARAMS") == null) // @L1A
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable) // @L1A
                && (rs.getBoolean("CASE_SENSITIVE") == false) // @L1A
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable) // @L1A
                && (rs.getString("UNSIGNED_ATTRIBUTE") == null) // @L1A
                && (rs.getBoolean("FIXED_PREC_SCALE") == false) // @L1A
                && (rs.getBoolean("AUTO_INCREMENT") == false) // @L1A
                && (rs.getString("LOCAL_TYPE_NAME") == null ) // @L1A
                && (rs.getShort("MINIMUM_SCALE") == 0) // @L1A
                && (rs.getShort("MAXIMUM_SCALE") == 0) // @L1A
                && (rs.getInt("SQL_DATA_TYPE") == 16) // @L1A
                && (rs.getInt("SQL_DATETIME_SUB") == 0) // @L1A
                && (rs.getInt("NUM_PREC_RADIX") == 0); // @L1A
            if (!thisCase)
              comment += " BOOLEAN IS WRONG\n";
              comment += dumpColumnValues(rs); 
              
            success = success & thisCase; // @L1A
            break;
          case Types.OTHER: // UNKNOWN for DECFLOAT
            thisCase = (rs.getString("TYPE_NAME").equals("DECFLOAT")) // @L1A
                && (rs.getInt("PRECISION") == 34) // @L1A
                && (rs.getString("LITERAL_PREFIX") == null) // @L1A
                && (rs.getString("LITERAL_SUFFIX") == null) // @L1A
                && (rs.getString("CREATE_PARAMS") == null) // @L1A
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable) // @L1A
                && (rs.getBoolean("CASE_SENSITIVE") == false) // @L1A
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable) // @L1A
                && (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false) // @L1A
                && (rs.getBoolean("FIXED_PREC_SCALE") == false) // @L1A
                && (rs.getBoolean("AUTO_INCREMENT") == false) // @L1A
                && (rs.getString("LOCAL_TYPE_NAME").equals("DECFLOAT")) // @L1A
                && (rs.getShort("MINIMUM_SCALE") == 0) // @L1A
                && (rs.getShort("MAXIMUM_SCALE") == 0) // @L1A
                && (rs.getInt("SQL_DATA_TYPE") == 0) // @L1A
                && (rs.getInt("SQL_DATETIME_SUB") == 0) // @L1A
                && (rs.getInt("NUM_PREC_RADIX") == 10); // @L1A
            if (!thisCase)
              comment += " OTHER->DECFLOAT IS WRONG";
            success = success & thisCase; // @L1A
            break;

          case 2003:

            break;
          default:
            comment += " UNRECOGNIZED TOOLBOX TYPE[" + rs.getShort("DATA_TYPE")
                + "]->" + rs.getString("TYPE_NAME");
            success = false;
          }

        } else {

          //
          // Native driver values -- NC marks native driver changes (as compared
          // to toolbox).
          //
          String jdbcType;
          String typeName = null;
          switch (rs.getShort("DATA_TYPE")) {

          case Types.BIGINT:
            jdbcType = "Types.BIGINT";

            thisCase = (rs.getString("TYPE_NAME").equals("BIGINT"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": TYPE_NAME WRONG";
            success = success & thisCase;

            thisCase = (rs.getInt("PRECISION") == 19);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad PRECISION ="
                  + rs.getInt("PRECISION");
            success = success & thisCase;

            thisCase = (rs.getString("LITERAL_PREFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": LITERAL_PREFIX="
                  + rs.getString("LITERNAL_PREFIX");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_SUFFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                  + rs.getString("LITERAL_SUFFIX");
            success = success & thisCase;
            thisCase = (rs.getString("CREATE_PARAMS") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                  + rs.getString("CREATE_PARAMS");
            success = success & thisCase;
            thisCase = (rs
                .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NULLABLE "
                  + rs.getShort("NULLABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                  + rs.getBoolean("CASE_SENSITIVE");
            success = success & thisCase;
            thisCase = (rs.getShort(
                "SEARCHABLE") == DatabaseMetaData.typePredBasic); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SEARCHABLE "
                  + rs.getShort("SEARCHABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                  + rs.getBoolean("UNSIGNED_ATTRIBUTE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == true); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                  + rs.getBoolean("FIXED_PREC_SCALE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                  + rs.getBoolean("AUTO_INCREMENT");
            success = success & thisCase;
            thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                  + rs.getString("LOCAL_TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                  + rs.getShort("MINIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                  + rs.getShort("MAXIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATA_TYPE") == -5);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                  + rs.getInt("SQL_DATA_TYPE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                  + rs.getInt("SQL_DATETIME_SUB");
            success = success & thisCase;
            thisCase = (rs.getInt("NUM_PREC_RADIX") == 10);
            ;
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                  + rs.getInt("NUM_PREC_RADIX");
            success = success & thisCase;
            break;

          case Types.BINARY:
            String mytypeName = rs.getString("TYPE_NAME");
            if (mytypeName.equals("CHAR () FOR BIT DATA")) {
              jdbcType = "Types.CHAR () FOR BIT DATA";
              thisCase = (rs.getString("TYPE_NAME")
                  .equals("CHAR () FOR BIT DATA")); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad TYPE_NAME "
                    + rs.getString("TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getInt("PRECISION") == 32765);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
                thisCase = (rs.getString("LITERAL_PREFIX")
                    .equals("X'")); /* @K6 */
              } else {
                thisCase = (rs.getString("LITERAL_PREFIX").equals("'"));
              }
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_SUFFIX").equals("'"));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS")
                  .equalsIgnoreCase("LENGTH")); /* NC */ /* @K4 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -2);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;
            } else if (mytypeName.equals("BINARY")) {
              jdbcType = "Types.BINARY";
              thisCase = (rs.getString("TYPE_NAME").equals("BINARY"));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad TYPE_NAME "
                    + rs.getString("TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getInt("PRECISION") == 32765);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;

              if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K3
                thisCase = (rs.getString("LITERAL_PREFIX").equals("BINARY(X'")); // @K3
              else // @K3
                thisCase = (rs.getString("LITERAL_PREFIX") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;

              if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K3
                thisCase = (rs.getString("LITERAL_SUFFIX").equals("')")); // @K3
              else // @K3
                thisCase = (rs.getString("LITERAL_SUFFIX") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS")
                  .equalsIgnoreCase("LENGTH")); /* NC */ /* @K4 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -2);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;
            } else {
              comment += "+\nBINARY/" + mytypeName + " is UNRECOGNIZED";
              success = false;
            }
            break;

          case Types.BLOB:
            jdbcType = "Types.BLOB";
            thisCase = (rs.getString("TYPE_NAME").equals("BLOB"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad TYPE_NAME "
                  + rs.getString("TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getInt("PRECISION") == 2147483647); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad PRECISION "
                  + rs.getInt("PRECISION");
            success = success & thisCase;

            if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K3
              thisCase = ("BLOB(X'".equals(rs.getString("LITERAL_PREFIX"))); // @K3
                                                                             // @K6
            else // @K3
              thisCase = (rs.getString("LITERAL_PREFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                  + rs.getString("LITERAL_PREFIX");
            success = success & thisCase;

            if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K3
              thisCase = "')".equals(rs.getString("LITERAL_SUFFIX")); // @K3
            else // @K3
              thisCase = (rs.getString("LITERAL_SUFFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                  + rs.getString("LITERAL_SUFFIX");
            success = success & thisCase;
            thisCase = (rs.getString("CREATE_PARAMS")
                .equalsIgnoreCase("LENGTH")); /* NC */ /* @K4 */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                  + rs.getString("CREATE_PARAMS");
            success = success & thisCase;
            thisCase = (rs
                .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NULLABLE "
                  + rs.getShort("NULLABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("CASE_SENSITIVE") == false); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                  + rs.getBoolean("CASE_SENSITIVE");
            success = success & thisCase;
            thisCase = (rs
                .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SEARCHABLE "
                  + rs.getShort("SEARCHABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                  + rs.getBoolean("UNSIGNED_ATTRIBUTE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                  + rs.getBoolean("FIXED_PREC_SCALE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                  + rs.getBoolean("AUTO_INCREMENT");
            success = success & thisCase;
            thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                  + rs.getString("LOCAL_TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                  + rs.getShort("MINIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                  + rs.getShort("MAXIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATA_TYPE") == -98);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                  + rs.getInt("SQL_DATA_TYPE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                  + rs.getInt("SQL_DATETIME_SUB");
            success = success & thisCase;
            thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
            ;
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                  + rs.getInt("NUM_PREC_RADIX");
            success = success & thisCase;
            break;

          case Types.CHAR:
          case -15: /* NCHAR */
            jdbcType = "Types.CHAR";
            String charTypeName = rs.getString("TYPE_NAME");
            thisCase = (charTypeName.equals("CHAR")
                || charTypeName.equals("GRAPHIC")
                || charTypeName.equals("GRAPHIC () CCSID 13488")
                || charTypeName.equals("NCHAR")); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad TYPE_NAME "
                  + rs.getString("TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getInt("PRECISION") == 32765
                || rs.getInt("PRECISION") == 16382); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad PRECISION "
                  + rs.getInt("PRECISION");
            success = success & thisCase;
            String charLiteralPrefix = rs.getString("LITERAL_PREFIX");
            thisCase = (charLiteralPrefix.equals("'")
                || charLiteralPrefix.equals("N'"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                  + rs.getString("LITERAL_PREFIX");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_SUFFIX").equals("'"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                  + rs.getString("LITERAL_SUFFIX");
            success = success & thisCase;
            thisCase = (rs.getString("CREATE_PARAMS")
                .equalsIgnoreCase("LENGTH")); /* NC */ /* @K4 */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                  + rs.getString("CREATE_PARAMS");
            success = success & thisCase;
            thisCase = (rs
                .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NULLABLE "
                  + rs.getShort("NULLABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("CASE_SENSITIVE") == true);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                  + rs.getBoolean("CASE_SENSITIVE");
            success = success & thisCase;
            thisCase = (rs
                .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SEARCHABLE "
                  + rs.getShort("SEARCHABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                  + rs.getBoolean("UNSIGNED_ATTRIBUTE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                  + rs.getBoolean("FIXED_PREC_SCALE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                  + rs.getBoolean("AUTO_INCREMENT");
            success = success & thisCase;
            thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                  + rs.getString("LOCAL_TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                  + rs.getShort("MINIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                  + rs.getShort("MAXIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATA_TYPE") == 1
                || rs.getInt("SQL_DATA_TYPE") == -95
                || rs.getInt("SQL_DATA_TYPE") == -8); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                  + rs.getInt("SQL_DATA_TYPE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                  + rs.getInt("SQL_DATETIME_SUB");
            success = success & thisCase;
            thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
            ;
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                  + rs.getInt("NUM_PREC_RADIX");
            success = success & thisCase;
            break;

          case Types.CLOB:
          case 2011: /* NCLOB */
            jdbcType = "Types.CLOB";
            typeName = rs.getString("TYPE_NAME");
            if (typeName.equals("CLOB")) {
              jdbcType = "Types.CLOB->CLOB";
              thisCase = (rs.getInt("PRECISION") == 2147483647); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = ("'".equals(rs.getString("LITERAL_PREFIX"))); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = ("'".equals(rs.getString("LITERAL_SUFFIX"))); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS")
                  .equalsIgnoreCase("LENGTH")); /* NC */ /* @K4 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == true);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -99);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;
            } else if (typeName.equals("NCLOB")) {
              jdbcType = "Types.CLOB->NCLOB";
              thisCase = (rs.getInt("PRECISION") == 1073741823); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = ("N'".equals(rs.getString("LITERAL_PREFIX"))); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = ("'".equals(rs.getString("LITERAL_SUFFIX"))); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS")
                  .equalsIgnoreCase("LENGTH")); /* NC */ /* @K4 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == true);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -10);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;
            }

            else if (typeName.equals("DBCLOB")) {
              jdbcType = "Types.CLOB->DBCLOB";
              thisCase = (rs.getInt("PRECISION") == 1073741823); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = ("'".equals(rs.getString("LITERAL_PREFIX"))); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = ("'".equals(rs.getString("LITERAL_SUFFIX"))); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS")
                  .equalsIgnoreCase("LENGTH")); /* NC */ /* @K4 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == true);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -350);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;
            } else if (typeName.equals("DBCLOB () CCSID 13488")) {
              jdbcType = "Types.CLOB->DBCLOB CCSID 13488";
              thisCase = (rs.getInt("PRECISION") == 1073741823); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = ("'".equals(rs.getString("LITERAL_PREFIX"))); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = ("'".equals(rs.getString("LITERAL_SUFFIX"))); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS")
                  .equalsIgnoreCase("LENGTH")); /* NC */ /* @K4 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == true);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -10);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;
            } else {
              comment += "+\nCLOB/" + typeName + " is UNRECOGNIZED";
              success = false;
            }
            break;

          case Types.DATE:
            jdbcType = "Types.DATE";
            thisCase = (rs.getString("TYPE_NAME").equals("DATE"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad TYPE_NAME "
                  + rs.getString("TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getInt("PRECISION") == 10);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad PRECISION "
                  + rs.getInt("PRECISION");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_PREFIX").equals("'"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                  + rs.getString("LITERAL_PREFIX");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_SUFFIX").equals("'"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                  + rs.getString("LITERAL_SUFFIX");
            success = success & thisCase;
            thisCase = (rs.getString("CREATE_PARAMS") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                  + rs.getString("CREATE_PARAMS");
            success = success & thisCase;
            thisCase = (rs
                .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NULLABLE "
                  + rs.getShort("NULLABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                  + rs.getBoolean("CASE_SENSITIVE");
            success = success & thisCase;
            thisCase = (rs.getShort(
                "SEARCHABLE") == DatabaseMetaData.typePredBasic); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SEARCHABLE "
                  + rs.getShort("SEARCHABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                  + rs.getBoolean("UNSIGNED_ATTRIBUTE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                  + rs.getBoolean("FIXED_PREC_SCALE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                  + rs.getBoolean("AUTO_INCREMENT");
            success = success & thisCase;
            thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                  + rs.getString("LOCAL_TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                  + rs.getShort("MINIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                  + rs.getShort("MAXIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATA_TYPE") == 9);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                  + rs.getInt("SQL_DATA_TYPE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATETIME_SUB") == 1);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                  + rs.getInt("SQL_DATETIME_SUB");
            success = success & thisCase;
            thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
            ; /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                  + rs.getInt("NUM_PREC_RADIX");
            success = success & thisCase;
            break;

          case Types.DECIMAL:
            jdbcType = "Types.DECIMAL";
            thisCase = (rs.getString("TYPE_NAME").equals("DECIMAL"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad TYPE_NAME "
                  + rs.getString("TYPE_NAME");
            success = success & thisCase;
            if (getRelease() >= JDTestDriver.RELEASE_V7R1M0)
              thisCase = (rs.getInt("PRECISION") == 63);
            else
              thisCase = (rs.getInt("PRECISION") == 31); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad PRECISION "
                  + rs.getInt("PRECISION");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_PREFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                  + rs.getString("LITERAL_PREFIX");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_SUFFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                  + rs.getString("LITERAL_SUFFIX");
            success = success & thisCase;
            thisCase = (rs.getString("CREATE_PARAMS")
                .equalsIgnoreCase("PRECISION,SCALE")); /* @K4 */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                  + rs.getString("CREATE_PARAMS");
            success = success & thisCase;
            thisCase = (rs
                .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NULLABLE "
                  + rs.getShort("NULLABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                  + rs.getBoolean("CASE_SENSITIVE");
            success = success & thisCase;
            thisCase = (rs.getShort(
                "SEARCHABLE") == DatabaseMetaData.typePredBasic); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SEARCHABLE "
                  + rs.getShort("SEARCHABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                  + rs.getBoolean("UNSIGNED_ATTRIBUTE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                  + rs.getBoolean("FIXED_PREC_SCALE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                  + rs.getBoolean("AUTO_INCREMENT");
            success = success & thisCase;
            thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                  + rs.getString("LOCAL_TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                  + rs.getShort("MINIMUM_SCALE");
            success = success & thisCase;
            if (getRelease() >= JDTestDriver.RELEASE_V7R1M0)
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 63);
            else
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 31);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                  + rs.getShort("MAXIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATA_TYPE") == 3);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                  + rs.getInt("SQL_DATA_TYPE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                  + rs.getInt("SQL_DATETIME_SUB");
            success = success & thisCase;
            thisCase = (rs.getInt("NUM_PREC_RADIX") == 10);
            ;
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                  + rs.getInt("NUM_PREC_RADIX");
            success = success & thisCase;
            break;

          case Types.DOUBLE:
            jdbcType = "Types.DOUBLE";
            thisCase = (rs.getString("TYPE_NAME").equals("DOUBLE"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad TYPE_NAME "
                  + rs.getString("TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getInt("PRECISION") == 53); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad PRECISION "
                  + rs.getInt("PRECISION");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_PREFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                  + rs.getString("LITERAL_PREFIX");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_SUFFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                  + rs.getString("LITERAL_SUFFIX");
            success = success & thisCase;
            thisCase = (rs.getString("CREATE_PARAMS") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                  + rs.getString("CREATE_PARAMS");
            success = success & thisCase;
            thisCase = (rs
                .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NULLABLE "
                  + rs.getShort("NULLABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                  + rs.getBoolean("CASE_SENSITIVE");
            success = success & thisCase;
            thisCase = (rs.getShort(
                "SEARCHABLE") == DatabaseMetaData.typePredBasic);/* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SEARCHABLE "
                  + rs.getShort("SEARCHABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                  + rs.getBoolean("UNSIGNED_ATTRIBUTE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                  + rs.getBoolean("FIXED_PREC_SCALE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                  + rs.getBoolean("AUTO_INCREMENT");
            success = success & thisCase;
            thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                  + rs.getString("LOCAL_TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                  + rs.getShort("MINIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                  + rs.getShort("MAXIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATA_TYPE") == 8);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                  + rs.getInt("SQL_DATA_TYPE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                  + rs.getInt("SQL_DATETIME_SUB");
            success = success & thisCase;
            thisCase = (rs.getInt("NUM_PREC_RADIX") == 2);
            ; /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                  + rs.getInt("NUM_PREC_RADIX");
            success = success & thisCase;
            break;

          case Types.FLOAT:
            jdbcType = "Types.FLOAT";
            thisCase = (rs.getString("TYPE_NAME").equals("FLOAT"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad TYPE_NAME "
                  + rs.getString("TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getInt("PRECISION") == 15);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad PRECISION "
                  + rs.getInt("PRECISION");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_PREFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                  + rs.getString("LITERAL_PREFIX");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_SUFFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                  + rs.getString("LITERAL_SUFFIX");
            success = success & thisCase;
            thisCase = (rs.getString("CREATE_PARAMS") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                  + rs.getString("CREATE_PARAMS");
            success = success & thisCase;
            thisCase = (rs
                .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NULLABLE "
                  + rs.getShort("NULLABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                  + rs.getBoolean("CASE_SENSITIVE");
            success = success & thisCase;
            thisCase = (rs.getShort(
                "SEARCHABLE") == DatabaseMetaData.typeSearchable); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SEARCHABLE "
                  + rs.getShort("SEARCHABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                  + rs.getBoolean("UNSIGNED_ATTRIBUTE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                  + rs.getBoolean("FIXED_PREC_SCALE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                  + rs.getBoolean("AUTO_INCREMENT");
            success = success & thisCase;
            thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                  + rs.getString("LOCAL_TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                  + rs.getShort("MINIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                  + rs.getShort("MAXIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATA_TYPE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                  + rs.getInt("SQL_DATA_TYPE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                  + rs.getInt("SQL_DATETIME_SUB");
            success = success & thisCase;
            thisCase = (rs.getInt("NUM_PREC_RADIX") == 10);
            ;
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                  + rs.getInt("NUM_PREC_RADIX");
            success = success & thisCase;
            break;

          case Types.INTEGER:
            jdbcType = "Types.INTEGER";
            thisCase = (rs.getString("TYPE_NAME").equals("INTEGER"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad TYPE_NAME "
                  + rs.getString("TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getInt("PRECISION") == 10);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad PRECISION "
                  + rs.getInt("PRECISION");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_PREFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                  + rs.getString("LITERAL_PREFIX");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_SUFFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                  + rs.getString("LITERAL_SUFFIX");
            success = success & thisCase;
            thisCase = (rs.getString("CREATE_PARAMS") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                  + rs.getString("CREATE_PARAMS");
            success = success & thisCase;
            thisCase = (rs
                .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NULLABLE "
                  + rs.getShort("NULLABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                  + rs.getBoolean("CASE_SENSITIVE");
            success = success & thisCase;
            thisCase = (rs.getShort(
                "SEARCHABLE") == DatabaseMetaData.typePredBasic); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SEARCHABLE "
                  + rs.getShort("SEARCHABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                  + rs.getBoolean("UNSIGNED_ATTRIBUTE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == true); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                  + rs.getBoolean("FIXED_PREC_SCALE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                  + rs.getBoolean("AUTO_INCREMENT");
            success = success & thisCase;
            thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                  + rs.getString("LOCAL_TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                  + rs.getShort("MINIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                  + rs.getShort("MAXIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATA_TYPE") == 4);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                  + rs.getInt("SQL_DATA_TYPE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                  + rs.getInt("SQL_DATETIME_SUB");
            success = success & thisCase;
            thisCase = (rs.getInt("NUM_PREC_RADIX") == 10);
            ;
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                  + rs.getInt("NUM_PREC_RADIX");
            success = success & thisCase;
            break;

          case Types.NUMERIC:
            jdbcType = "Types.NUMERIC";
            thisCase = (rs.getString("TYPE_NAME").equals("NUMERIC"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad TYPE_NAME "
                  + rs.getString("TYPE_NAME");
            success = success & thisCase;
            if (getRelease() >= JDTestDriver.RELEASE_V7R1M0)
              thisCase = (rs.getInt("PRECISION") == 63);
            else
              thisCase = (rs.getInt("PRECISION") == 31);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad PRECISION "
                  + rs.getInt("PRECISION");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_PREFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                  + rs.getString("LITERAL_PREFIX");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_SUFFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                  + rs.getString("LITERAL_SUFFIX");
            success = success & thisCase;
            thisCase = (rs.getString("CREATE_PARAMS")
                .equalsIgnoreCase("PRECISION,SCALE")); /* @K4 */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                  + rs.getString("CREATE_PARAMS");
            success = success & thisCase;
            thisCase = (rs
                .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NULLABLE "
                  + rs.getShort("NULLABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                  + rs.getBoolean("CASE_SENSITIVE");
            success = success & thisCase;
            thisCase = (rs.getShort(
                "SEARCHABLE") == DatabaseMetaData.typePredBasic); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SEARCHABLE "
                  + rs.getShort("SEARCHABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                  + rs.getBoolean("UNSIGNED_ATTRIBUTE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                  + rs.getBoolean("FIXED_PREC_SCALE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                  + rs.getBoolean("AUTO_INCREMENT");
            success = success & thisCase;
            thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                  + rs.getString("LOCAL_TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                  + rs.getShort("MINIMUM_SCALE");
            success = success & thisCase;
            if (getRelease() >= JDTestDriver.RELEASE_V7R1M0)
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 63);
            else
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 31);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                  + rs.getShort("MAXIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATA_TYPE") == 2);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                  + rs.getInt("SQL_DATA_TYPE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                  + rs.getInt("SQL_DATETIME_SUB");
            success = success & thisCase;
            thisCase = (rs.getInt("NUM_PREC_RADIX") == 10);
            ;
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                  + rs.getInt("NUM_PREC_RADIX");
            success = success & thisCase;
            break;

          case Types.REAL:
            jdbcType = "Types.REAL";
            thisCase = (rs.getString("TYPE_NAME").equals("REAL"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad TYPE_NAME "
                  + rs.getString("TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getInt("PRECISION") == 24); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad PRECISION "
                  + rs.getInt("PRECISION");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_PREFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                  + rs.getString("LITERAL_PREFIX");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_SUFFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                  + rs.getString("LITERAL_SUFFIX");
            success = success & thisCase;
            thisCase = (rs.getString("CREATE_PARAMS") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                  + rs.getString("CREATE_PARAMS");
            success = success & thisCase;
            thisCase = (rs
                .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NULLABLE "
                  + rs.getShort("NULLABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                  + rs.getBoolean("CASE_SENSITIVE");
            success = success & thisCase;
            thisCase = (rs.getShort(
                "SEARCHABLE") == DatabaseMetaData.typePredBasic); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SEARCHABLE "
                  + rs.getShort("SEARCHABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                  + rs.getBoolean("UNSIGNED_ATTRIBUTE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                  + rs.getBoolean("FIXED_PREC_SCALE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                  + rs.getBoolean("AUTO_INCREMENT");
            success = success & thisCase;
            thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                  + rs.getString("LOCAL_TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                  + rs.getShort("MINIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                  + rs.getShort("MAXIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATA_TYPE") == 7); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                  + rs.getInt("SQL_DATA_TYPE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                  + rs.getInt("SQL_DATETIME_SUB");
            success = success & thisCase;
            thisCase = (rs.getInt("NUM_PREC_RADIX") == 2 /* 10 */ ); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                  + rs.getInt("NUM_PREC_RADIX");
            success = success & thisCase;
            break;

          case Types.SMALLINT:
            jdbcType = "Types.SMALLINT";
            thisCase = (rs.getString("TYPE_NAME").equals("SMALLINT"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad TYPE_NAME "
                  + rs.getString("TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getInt("PRECISION") == 5);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad PRECISION "
                  + rs.getInt("PRECISION");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_PREFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                  + rs.getString("LITERAL_PREFIX");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_SUFFIX") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                  + rs.getString("LITERAL_SUFFIX");
            success = success & thisCase;
            thisCase = (rs.getString("CREATE_PARAMS") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                  + rs.getString("CREATE_PARAMS");
            success = success & thisCase;
            thisCase = (rs
                .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NULLABLE "
                  + rs.getShort("NULLABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                  + rs.getBoolean("CASE_SENSITIVE");
            success = success & thisCase;
            thisCase = (rs.getShort(
                "SEARCHABLE") == DatabaseMetaData.typePredBasic); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SEARCHABLE "
                  + rs.getShort("SEARCHABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                  + rs.getBoolean("UNSIGNED_ATTRIBUTE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == true); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                  + rs.getBoolean("FIXED_PREC_SCALE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                  + rs.getBoolean("AUTO_INCREMENT");
            success = success & thisCase;
            thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                  + rs.getString("LOCAL_TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                  + rs.getShort("MINIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                  + rs.getShort("MAXIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATA_TYPE") == 5);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                  + rs.getInt("SQL_DATA_TYPE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                  + rs.getInt("SQL_DATETIME_SUB");
            success = success & thisCase;
            thisCase = (rs.getInt("NUM_PREC_RADIX") == 10);
            ;
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                  + rs.getInt("NUM_PREC_RADIX");
            success = success & thisCase;
            break;

          case Types.TIME:
            jdbcType = "Types.TIME";
            thisCase = (rs.getString("TYPE_NAME").equals("TIME"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad TYPE_NAME "
                  + rs.getString("TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getInt("PRECISION") == 8);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad PRECISION "
                  + rs.getInt("PRECISION");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_PREFIX").equals("'"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                  + rs.getString("LITERAL_PREFIX");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_SUFFIX").equals("'"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                  + rs.getString("LITERAL_SUFFIX");
            success = success & thisCase;
            thisCase = (rs.getString("CREATE_PARAMS") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                  + rs.getString("CREATE_PARAMS");
            success = success & thisCase;
            thisCase = (rs
                .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NULLABLE "
                  + rs.getShort("NULLABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                  + rs.getBoolean("CASE_SENSITIVE");
            success = success & thisCase;
            thisCase = (rs.getShort(
                "SEARCHABLE") == DatabaseMetaData.typePredBasic); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SEARCHABLE "
                  + rs.getShort("SEARCHABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                  + rs.getBoolean("UNSIGNED_ATTRIBUTE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                  + rs.getBoolean("FIXED_PREC_SCALE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                  + rs.getBoolean("AUTO_INCREMENT");
            success = success & thisCase;
            thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                  + rs.getString("LOCAL_TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                  + rs.getShort("MINIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                  + rs.getShort("MAXIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATA_TYPE") == 9);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                  + rs.getInt("SQL_DATA_TYPE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATETIME_SUB") == 2);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                  + rs.getInt("SQL_DATETIME_SUB");
            success = success & thisCase;
            thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
            ; /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                  + rs.getInt("NUM_PREC_RADIX");
            success = success & thisCase;
            break;

          case Types.TIMESTAMP:
            jdbcType = "Types.TIMESTAMP";
            thisCase = (rs.getString("TYPE_NAME").equals("TIMESTAMP"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad TYPE_NAME "
                  + rs.getString("TYPE_NAME");
            success = success & thisCase;
            int expectedTimestampPrecision = 26;
            int timestampMinimumScale = 6;
            int timestampMaximumScale = 6;

            if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
              expectedTimestampPrecision = 32;
              timestampMinimumScale = 0;
              timestampMaximumScale = 12;
            }
            thisCase = (rs.getInt("PRECISION") == expectedTimestampPrecision);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad PRECISION "
                  + rs.getInt("PRECISION");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_PREFIX").equals("'"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                  + rs.getString("LITERAL_PREFIX");
            success = success & thisCase;
            thisCase = (rs.getString("LITERAL_SUFFIX").equals("'"));
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                  + rs.getString("LITERAL_SUFFIX");
            success = success & thisCase;
            thisCase = (rs.getString("CREATE_PARAMS") == null);
            if (!thisCase) {
              // In V7R2 now have the precision
              thisCase = ("precision".equals(rs.getString("CREATE_PARAMS")));
              if (!thisCase) {
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              }
            }
            success = success & thisCase;
            thisCase = (rs
                .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NULLABLE "
                  + rs.getShort("NULLABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                  + rs.getBoolean("CASE_SENSITIVE");
            success = success & thisCase;
            thisCase = (rs.getShort(
                "SEARCHABLE") == DatabaseMetaData.typePredBasic); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SEARCHABLE "
                  + rs.getShort("SEARCHABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                  + rs.getBoolean("UNSIGNED_ATTRIBUTE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                  + rs.getBoolean("FIXED_PREC_SCALE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                  + rs.getBoolean("AUTO_INCREMENT");
            success = success & thisCase;
            thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                  + rs.getString("LOCAL_TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getShort("MINIMUM_SCALE") == timestampMinimumScale);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                  + rs.getShort("MINIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getShort("MAXIMUM_SCALE") == timestampMaximumScale);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                  + rs.getShort("MAXIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATA_TYPE") == 9);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                  + rs.getInt("SQL_DATA_TYPE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATETIME_SUB") == 3);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                  + rs.getInt("SQL_DATETIME_SUB");
            success = success & thisCase;
            thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
            ; /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                  + rs.getInt("NUM_PREC_RADIX");
            success = success & thisCase;
            break;

          case Types.VARBINARY:
            String my_type_Name = rs.getString("TYPE_NAME");
            if (my_type_Name.equals("VARBINARY")) {
              jdbcType = "Types.VARBINARY";
              thisCase = (rs.getString("TYPE_NAME")
                  .equals("VARBINARY")); /* @K2 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad TYPE_NAME "
                    + rs.getString("TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getInt("PRECISION") == 32739);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;

              if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K3
                thisCase = (rs.getString("LITERAL_PREFIX")
                    .equals("VARBINARY(X'")); // @K3@K6
              else // @K3
                thisCase = (rs.getString("LITERAL_PREFIX") == null); /* @K2 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;

              if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K3
                thisCase = (rs.getString("LITERAL_SUFFIX").equals("')")); // @K3
              else // @K3
                thisCase = (rs.getString("LITERAL_SUFFIX") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS")
                  .equalsIgnoreCase("LENGTH")); /* NC */ /* @K4 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -3);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;
            } else if (my_type_Name.equals("VARCHAR () FOR BIT DATA")) {
              jdbcType = "Types.VARCHAR () FOR BIT DATA";
              thisCase = (rs.getString("TYPE_NAME")
                  .equals("VARCHAR () FOR BIT DATA")); /* @K2 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad TYPE_NAME "
                    + rs.getString("TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getInt("PRECISION") == 32739);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
                thisCase = (rs.getString("LITERAL_PREFIX")
                    .equals("X'")); /* @K6 */
              } else {
                thisCase = (rs.getString("LITERAL_PREFIX")
                    .equals("'")); /* @K2 */
              }
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_SUFFIX").equals("'"));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS")
                  .equalsIgnoreCase("LENGTH")); /* NC */ /* @K4 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -3);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;
            } else {
              comment += "+\nVARBINARY/" + my_type_Name + " is UNRECOGNIZED";
              success = false;
            }
            break;

          case Types.VARCHAR:
          case -9: /* NVARCHAR */
            typeName = rs.getString("TYPE_NAME");
            if (typeName.equals("VARCHAR")) {
              jdbcType = "Types.VARCHAR->VARCHAR";
              thisCase = (rs.getInt("PRECISION") == 32739);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_PREFIX").equals("'"));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_SUFFIX").equals("'"));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS")
                  .equalsIgnoreCase("LENGTH")); /* NC */ /* @K4 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == true);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;

              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == 12);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;

            } else if (typeName.equals("NVARCHAR")) {
              jdbcType = "Types.VARCHAR->NVARCHAR";
              thisCase = (rs.getInt("PRECISION") == 16369);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_PREFIX").equals("N'"));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_SUFFIX").equals("'"));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS")
                  .equalsIgnoreCase("LENGTH")); /* NC */ /* @K4 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == true);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;

              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -9);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;

            }

            else if (typeName.equals("VARGRAPHIC")) {
              jdbcType = "Types.VARCHAR->VARGRAPHIC";
              thisCase = (rs.getInt("PRECISION") == 16369);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_PREFIX").equals("'"));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_SUFFIX").equals("'"));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS")
                  .equalsIgnoreCase("LENGTH")); /* NC */ /* @K4 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == true);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;

              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -96);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;

            } else if (typeName.equals("VARGRAPHIC () CCSID 13488")) {
              jdbcType = "Types.VARCHAR->VARGRAPHIC CCSID 13488";
              thisCase = (rs.getInt("PRECISION") == 16369);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_PREFIX").equals("'"));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_SUFFIX").equals("'"));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS")
                  .equalsIgnoreCase("LENGTH")); /* NC */ /* @K4 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == true);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;

              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -9);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;

            } else if (typeName.equals("DATALINK")) {
              jdbcType = "Types.VARCHAR->DATALINK";
              thisCase = (rs.getInt("PRECISION") == 32739);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_PREFIX").equals("'"));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_SUFFIX").equals("'"));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS")
                  .equalsIgnoreCase("MAX LENGTH")); /* @K4 */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == true);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs
                  .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("UNSIGNED_ATTRIBUTE") == true);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;

            } else {
              comment += "+\nVARCHAR/" + typeName + " is UNRECOGNIZED";
              success = false;
            }
            break;

          case ((short) 70 & 0x7fff): // DATALINK in java.sql.Types without
                                      // requiring 1.4
            jdbcType = "Types.DATALINK";
            thisCase = (rs.getInt("PRECISION") == 32717); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad PRECISION "
                  + rs.getInt("PRECISION");
            success = success & thisCase;
            thisCase = (null == (rs.getString("LITERAL_PREFIX"))); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                  + rs.getString("LITERAL_PREFIX");
            success = success & thisCase;
            thisCase = (null == (rs.getString("LITERAL_SUFFIX"))); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                  + rs.getString("LITERAL_SUFFIX");
            success = success & thisCase;
            thisCase = ("LENGTH".equalsIgnoreCase(
                rs.getString("CREATE_PARAMS"))); /* NC */ /* @K4 */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                  + rs.getString("CREATE_PARAMS");
            success = success & thisCase;
            thisCase = (rs
                .getShort("NULLABLE") == DatabaseMetaData.typeNullable);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NULLABLE "
                  + rs.getShort("NULLABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("CASE_SENSITIVE") == false); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                  + rs.getBoolean("CASE_SENSITIVE");
            success = success & thisCase;
            thisCase = (rs.getShort(
                "SEARCHABLE") == DatabaseMetaData.typePredNone); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SEARCHABLE "
                  + rs.getShort("SEARCHABLE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                  + rs.getBoolean("UNSIGNED_ATTRIBUTE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                  + rs.getBoolean("FIXED_PREC_SCALE");
            success = success & thisCase;
            thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                  + rs.getBoolean("AUTO_INCREMENT");
            success = success & thisCase;
            thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                  + rs.getString("LOCAL_TYPE_NAME");
            success = success & thisCase;
            thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                  + rs.getShort("MINIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                  + rs.getShort("MAXIMUM_SCALE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATA_TYPE") == 70);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                  + rs.getInt("SQL_DATA_TYPE");
            success = success & thisCase;
            thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                  + rs.getInt("SQL_DATETIME_SUB");
            success = success & thisCase;
            thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
            ;
            if (!thisCase)
              comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                  + rs.getInt("NUM_PREC_RADIX");
            success = success & thisCase;

            break;

          case Types.OTHER:
            typeName = rs.getString("TYPE_NAME");
            /* JDBC 4.0 should not return Types.OTHER, but Types.ROWID */
            /* This is fixed in V7R1 using the JDBCVER keyword passed */
            /* to the SYSIBM procedures. */
            if (typeName.equals("ROWID") && isJdbc40()
                && getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
              comment += "\n JDBC types is Types.other, but typeName is ROWID for JDBC 4.0";
              success = false;
            } else if (typeName.equals("XML") && isJdbc40()
                && getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
              comment += "\n JDBC types is Types.Other, but typeName is XML for JDBC 4.0";
              success = false;

            } else if (typeName.equals("ROWID")) {
              jdbcType = "Types.OTHER->ROWID";
              thisCase = (rs.getInt("PRECISION") == 40);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_PREFIX") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_SUFFIX") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS") == null); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs.getShort("NULLABLE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs.getShort("SEARCHABLE") == 2);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;

              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -100);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;

            } else if (typeName.equals("DECFLOAT")) {
              jdbcType = "Types.OTHER->DECFLOAT";
              thisCase = (rs.getInt("PRECISION") == 34);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_PREFIX") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_SUFFIX") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = ("precision"
                  .equals(rs.getString("CREATE_PARAMS"))); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs.getShort("NULLABLE") == 1);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs.getShort("SEARCHABLE") == 2);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;

              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 34);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -360);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 10);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;

            } else if (typeName.equals("XML")) {
              jdbcType = "Types.OTHER->XML";
              thisCase = (rs.getInt("PRECISION") == 2147483647);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = ("'".equals(rs.getString("LITERAL_PREFIX")));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = ("'".equals(rs.getString("LITERAL_SUFFIX")));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;

              // XML does nor accept length 4/16/2014 x1423p1 c7r1
              thisCase = (null == (rs.getString("CREATE_PARAMS"))); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;

              thisCase = (rs.getShort("NULLABLE") == 1);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == true);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs.getShort("SEARCHABLE") == 3);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;

              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -370);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;

            } else {
              comment += "+\nTypes.OTHER/" + typeName + " is UNRECOGNIZED";
              success = false;
            }
            break;

          case Types.DISTINCT:
            typeName = rs.getString("TYPE_NAME");
            if (typeName.equals("DISTINCT")) {
              jdbcType = "Types.DISTINCT->DISTINCT";
              thisCase = (rs.getInt("PRECISION") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_PREFIX") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_SUFFIX") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS") == null); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs.getShort("NULLABLE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs.getShort("SEARCHABLE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;

              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;

            } else {
              comment += "+\nDISTINCT/" + typeName + " is UNRECOGNIZED";
              success = false;
            }
            break;

          case -8: /* Types.ROWID for JDBC 4.0 */
            if (isJdbc40()) {
              jdbcType = "Types.OTHER->ROWID";
              thisCase = (rs.getInt("PRECISION") == 40);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_PREFIX") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_SUFFIX") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS") == null); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs.getShort("NULLABLE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs.getShort("SEARCHABLE") == 2);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;

              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -100);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;
            } else {
              comment += "\nFound Type of -8, but JDBC is not JDBC 4.0";
              success = false;
            }
            break;

          case 2009: /* Types.SQLXML for JDBC 4.0 */
            if (isJdbc40()) {
              jdbcType = "Types.OTHER->SQLXML";
              thisCase = (rs.getInt("PRECISION") == 2147483647);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_PREFIX").equals("'"));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_SUFFIX").equals("'"));
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              // For example, this has changed to return null.
              // For now, accept both
              String xmlCreateParms = rs.getString("CREATE_PARAMS");
              if (xmlCreateParms == null) {
                thisCase = true;
              } else {
                thisCase = (xmlCreateParms.equalsIgnoreCase("LENGTH"));
              }
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs.getShort("NULLABLE") == 1);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == true);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs.getShort("SEARCHABLE") == 3);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;

              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == -370);// ??javadoc says
                                                              // unused
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;

            } else {
              comment += "\nFound Type of 2009, but jdbc is not JDBC 4.0";
              success = false;
            }
            break;

          case 2003:

            typeName = rs.getString("TYPE_NAME");
            if (typeName.equals("ARRAY")) {
              jdbcType = "Types.ARRAY->ARRAY";
              thisCase = (rs.getInt("PRECISION") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad PRECISION "
                    + rs.getInt("PRECISION");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_PREFIX") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_PREFIX "
                    + rs.getString("LITERAL_PREFIX");
              success = success & thisCase;
              thisCase = (rs.getString("LITERAL_SUFFIX") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LITERAL_SUFFIX "
                    + rs.getString("LITERAL_SUFFIX");
              success = success & thisCase;
              thisCase = (rs.getString("CREATE_PARAMS") == null); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CREATE_PARAMS "
                    + rs.getString("CREATE_PARAMS");
              success = success & thisCase;
              thisCase = (rs.getShort("NULLABLE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NULLABLE "
                    + rs.getShort("NULLABLE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("CASE_SENSITIVE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad CASE_SENSITIVE "
                    + rs.getBoolean("CASE_SENSITIVE");
              success = success & thisCase;
              thisCase = (rs.getShort("SEARCHABLE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SEARCHABLE "
                    + rs.getShort("SEARCHABLE");
              success = success & thisCase;
              thisCase = (rs
                  .getBoolean("UNSIGNED_ATTRIBUTE") == false); /* NC */
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad UNSIGNED_ATTRIBUTE "
                    + rs.getBoolean("UNSIGNED_ATTRIBUTE");
              success = success & thisCase;

              thisCase = (rs.getBoolean("FIXED_PREC_SCALE") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad FIXED_PREC_SCALE "
                    + rs.getBoolean("FIXED_PREC_SCALE");
              success = success & thisCase;
              thisCase = (rs.getBoolean("AUTO_INCREMENT") == false);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad AUTO_INCREMENT "
                    + rs.getBoolean("AUTO_INCREMENT");
              success = success & thisCase;
              thisCase = (rs.getString("LOCAL_TYPE_NAME") == null);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad LOCAL_TYPE_NAME "
                    + rs.getString("LOCAL_TYPE_NAME");
              success = success & thisCase;
              thisCase = (rs.getShort("MINIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MINIMUM_SCALE "
                    + rs.getShort("MINIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getShort("MAXIMUM_SCALE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad MAXIMUM_SCALE "
                    + rs.getShort("MAXIMUM_SCALE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATA_TYPE") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATA_TYPE "
                    + rs.getInt("SQL_DATA_TYPE");
              success = success & thisCase;
              thisCase = (rs.getInt("SQL_DATETIME_SUB") == 0);
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad SQL_DATETIME_SUB "
                    + rs.getInt("SQL_DATETIME_SUB");
              success = success & thisCase;
              thisCase = (rs.getInt("NUM_PREC_RADIX") == 0);
              ;
              if (!thisCase)
                comment += "\n" + jdbcType + ": bad NUM_PREC_RADIX "
                    + rs.getInt("NUM_PREC_RADIX");
              success = success & thisCase;

            } else {
              comment += "+\nARRAY(2003)/" + typeName + " is UNRECOGNIZED";
              success = false;
            }

            break;
          case Types.BOOLEAN: // ROWID in java.sql.Types without requiring java 1.6
            thisCase = (rs.getString("TYPE_NAME").equals("BOOLEAN")) // @L1A
                && (rs.getInt("PRECISION") == 1) // @L1A
                && (rs.getString("LITERAL_PREFIX") == null) // @L1A
                && (rs.getString("LITERAL_SUFFIX") == null) // @L1A
                && (rs.getString("CREATE_PARAMS") == null) // @L1A
                && (rs.getShort("NULLABLE") == DatabaseMetaData.typeNullable) // @L1A
                && (rs.getBoolean("CASE_SENSITIVE") == false) // @L1A
                && (rs
                    .getShort("SEARCHABLE") == DatabaseMetaData.typeSearchable) // @L1A
                && (rs.getString("UNSIGNED_ATTRIBUTE") == null) // @L1A
                && (rs.getString("FIXED_PREC_SCALE") == null) // @L1A
                && (rs.getString("AUTO_INCREMENT") == null) // @L1A
                && (rs.getString("LOCAL_TYPE_NAME") == null ) // @L1A
                && (rs.getShort("MINIMUM_SCALE") == 0) // @L1A
                && (rs.getShort("MAXIMUM_SCALE") == 0) // @L1A
                && (rs.getInt("SQL_DATA_TYPE") == 16) // @L1A
                && (rs.getInt("SQL_DATETIME_SUB") == 0) // @L1A
                && (rs.getInt("NUM_PREC_RADIX") == 0); // @L1A
            if (!thisCase)
              comment += " BOOLEAN IS WRONG\n";
              comment += dumpColumnValues(rs); 
              
            success = success & thisCase; // @L1A
            break;

          default:
            comment += "\n UNRECOGNIZED DRIVER TYPE[" + rs.getShort("DATA_TYPE")
                + "]->" + rs.getString("TYPE_NAME");
            success = false;
          }

        }
      } // while
      rs.close();

      // @K1D int expectedRows = areBigintsSupported() ? 20 : 19; // @B0A
      // check if V4R4 or greater in V4R4 we support 22 types, otherwise we
      // support 18
      int expectedRows = areLobsSupported() ? 22 : 18; // @K1a
      // check if V5R5 or greater. We added one type in V4R5
      expectedRows += areBigintsSupported() ? 1 : 0; // @B2A //@K1C
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @K1A Added one type in
                                                       // V5R2
        expectedRows += 1; // @K1A
      // need to add code here to add 2 to expectedRows when we are on a v5r3
      // machine and we support the new BINARY and VARBINARY types
      // if(getRelease() >= JDTestDriver.RELEASE_V5R3M0) //@K1A
      // expectedRows += 2; //@K1A
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) // @L1A added DECFLOAT
        expectedRows += 1;

      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX && !isSysibmMetadata()) {
        boolean condition = success && (rows == expectedRows);
        if (!condition) {
          System.out.println("success = " + success);
          System.out.println("rows = " + rows + " should be " + expectedRows);
          System.out.println("Comment is " + comment);
        }

        assertCondition(success && (rows == expectedRows)); // @A1C @B0C
      } else /* @K2 */
      {
        if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
          boolean condition = success && (rows >= 27);
          if (!condition) {
            System.out.println("success = " + success);
            System.out.println("rows = " + rows + " sb >= 27");
            System.out.println("Comment is " + comment);
          }
          assertCondition(condition); // This is what the native driver will
                                      // get...
        } else {
          boolean condition = success && (rows == 25);
          if (!condition) {
            System.out.println("success = " + success);
            System.out.println("rows = " + rows);
            System.out.println("Comment is " + comment);
          }
          assertCondition(condition); // This is what the native driver will
                                      // get...
        }
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  private String dumpColumnValues(ResultSet rs) throws SQLException {
StringBuffer sb = new StringBuffer(); 
sb.append("TYPE_NAME = "+    rs.getString("TYPE_NAME")+"\n");
sb.append("PRECISION = "+    rs.getInt("PRECISION")+"\n");
sb.append("LITERAL_PREFIX = "+    rs.getString("LITERAL_PREFIX")+"\n");
sb.append("LITERAL_SUFFIX = "+    rs.getString("LITERAL_SUFFIX")+"\n");
sb.append("CREATE_PARAMS = "+    rs.getString("CREATE_PARAMS")+"\n");
sb.append("NULLABLE = "+    rs.getString("NULLABLE")+"\n");
sb.append("CASE_SENSITIVE = "+    rs.getString("CASE_SENSITIVE")+"\n");
sb.append("SEARCHABLE = "+    rs.getString("SEARCHABLE")+"\n");
sb.append("UNSIGNED_ATTRIBUTE = "+    rs.getString("UNSIGNED_ATTRIBUTE")+"\n");
sb.append("FIXED_PREC_SCALE = "+    rs.getString("FIXED_PREC_SCALE")+"\n");
sb.append("AUTO_INCREMENT = "+    rs.getString("AUTO_INCREMENT")+"\n");
sb.append("LOCAL_TYPE_NAME = "+    rs.getString("LOCAL_TYPE_NAME")+"\n");
sb.append("MINIMUM_SCALE = "+    rs.getString("MINIMUM_SCALE")+"\n");
sb.append("MAXIMUM_SCALE = "+    rs.getString("MAXIMUM_SCALE")+"\n");
sb.append("SQL_DATA_TYPE = "+    rs.getString("SQL_DATA_TYPE")+"\n");
sb.append("SQL_DATETIME_SUB = "+    rs.getString("SQL_DATETIME_SUB")+"\n");
sb.append("NUM_PREC_RADIX = "+    rs.getString("NUM_PREC_RADIX")+"\n");
    
    return sb.toString(); 
  }

  /**
   * getTypeInfo() - Should throw an exception when the connection is closed.
   **/
  public void Var003() {
    try {
      ResultSet resultSet = dmd2_.getTypeInfo();
      failed("Didn't throw SQLException but got " + resultSet);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getTypeInfo() - Verify that this method works when autocommit is disabled
   * (because of native JDBC bug previously fixed).
   **/
  public void Var004() {
    try {
      boolean success = false;

      connection_.setAutoCommit(false);
      dmd_ = connection_.getMetaData();
      ResultSet rs = dmd_.getTypeInfo();

      // Verify that the data can be accessed.
      if (rs.next())
        success = true;

      rs.close();
      assertCondition(success);

    } catch (Exception e) {
      failed(e,
          "Unexpected Exception when calling API with autocommit disabled");
    }
  }

  /**
   * getTypeInfo() - Run getTypeInfo multiple times. Make sure there is not a
   * handle leak. Created 1/31/2011 for CPS 8DHTTE.
   * 
   **/
  public void Var005() {
    String added = " -- added by 1/31/2011 to test for native statement leak in metadata";
    if (checkNative()) {
      try {

        System.gc();
        Statement fill[] = new Statement[20];
        for (int i = 0; i < fill.length; i++) {
          fill[i] = connection_.createStatement();
        }

        Statement stmt = connection_.createStatement();

        for (int i = 0; i < 1000; i++) {
          // System.out.println("Calling getTypeInfo");
          ResultSet rs = dmd_.getTypeInfo();
          rs.close();
        }

        Statement stmt2 = connection_.createStatement();
        int beginningHandle = JDReflectionUtil.callMethod_I(stmt,"getStatementHandle"); 
        int endingHandle = JDReflectionUtil.callMethod_I(stmt2,"getStatementHandle"); 
        
        for (int i = 0; i < fill.length; i++) {
          fill[i].close();
        }

        assertCondition((endingHandle - beginningHandle) < 10,
            " endingHandle = " + endingHandle + " beginningHandle = "
                + beginningHandle + " endingHandle = " + endingHandle + added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + added);
      }
    }
  }

  public void Var006() {
    checkRSMD(false);
  }

  public void Var007() {
    if (checkNotGroupTest()) {
      checkRSMD(true);
    }
  }

  public void checkRSMD(boolean extendedMetadata) {

    Connection connection = connection_;
    DatabaseMetaData dmd = dmd_;
    int TESTSIZE = 40;
    StringBuffer prime = new StringBuffer();
    int j = 0;
    int col = 0;
    String added = " -- Reworked 03/23/2012";
    boolean passed = true;
    message.setLength(0);

    /* Primed from 6163N */
    String[][] methodTests = {

        { "isAutoIncrement", "1", "false" }, { "isCaseSensitive", "1", "true" },
        { "isSearchable", "1", "true" }, { "isCurrency", "1", "false" },
        { "isNullable", "1", "0" }, { "isSigned", "1", "false" },
        { "getColumnDisplaySize", "1", "128" },
        { "getColumnLabel", "1", "TYPE_NAME" },
        { "getColumnName", "1", "TYPE_NAME" }, { "getPrecision", "1", "128" },
        { "getScale", "1", "0" }, { "getCatalogName", "1", "LOCALHOST" },
        { "getColumnType", "1", "12" }, { "getColumnTypeName", "1", "VARCHAR" },
        { "isReadOnly", "1", "true" }, { "isWritable", "1", "false" },
        { "isDefinitelyWritable", "1", "false" },
        { "getColumnClassName", "1", "java.lang.String" },
        { "isAutoIncrement", "2", "false" },
        { "isCaseSensitive", "2", "false" }, { "isSearchable", "2", "true" },
        { "isCurrency", "2", "false" }, { "isNullable", "2", "0" },
        { "isSigned", "2", "true" }, { "getColumnDisplaySize", "2", "6" },
        { "getColumnLabel", "2", "DATA_TYPE" },
        { "getColumnName", "2", "DATA_TYPE" }, { "getPrecision", "2", "5" },
        { "getScale", "2", "0" }, { "getCatalogName", "2", "LOCALHOST" },
        { "getColumnType", "2", "5" }, { "getColumnTypeName", "2", "SMALLINT" },
        { "isReadOnly", "2", "true" }, { "isWritable", "2", "false" },
        { "isDefinitelyWritable", "2", "false" },
        { "getColumnClassName", "2", "java.lang.Integer" },
        { "isAutoIncrement", "3", "false" },
        { "isCaseSensitive", "3", "false" }, { "isSearchable", "3", "true" },
        { "isCurrency", "3", "false" }, { "isNullable", "3", "1" },
        { "isSigned", "3", "true" }, { "getColumnDisplaySize", "3", "11" },
        { "getColumnLabel", "3", "PRECISION" },
        { "getColumnName", "3", "PRECISION" }, { "getPrecision", "3", "10" },
        { "getScale", "3", "0" }, { "getCatalogName", "3", "LOCALHOST" },
        { "getColumnType", "3", "4" }, { "getColumnTypeName", "3", "INTEGER" },
        { "isReadOnly", "3", "true" }, { "isWritable", "3", "false" },
        { "isDefinitelyWritable", "3", "false" },
        { "getColumnClassName", "3", "java.lang.Integer" },
        { "isAutoIncrement", "4", "false" }, { "isCaseSensitive", "4", "true" },
        { "isSearchable", "4", "true" }, { "isCurrency", "4", "false" },
        { "isNullable", "4", "1" }, { "isSigned", "4", "false" },
        { "getColumnDisplaySize", "4", "128" },
        { "getColumnLabel", "4", "LITERAL_PREFIX" },
        { "getColumnName", "4", "LITERAL_PREFIX" },
        { "getPrecision", "4", "128" }, { "getScale", "4", "0" },
        { "getCatalogName", "4", "LOCALHOST" }, { "getColumnType", "4", "12" },
        { "getColumnTypeName", "4", "VARCHAR" }, { "isReadOnly", "4", "true" },
        { "isWritable", "4", "false" },
        { "isDefinitelyWritable", "4", "false" },
        { "getColumnClassName", "4", "java.lang.String" },
        { "isAutoIncrement", "5", "false" }, { "isCaseSensitive", "5", "true" },
        { "isSearchable", "5", "true" }, { "isCurrency", "5", "false" },
        { "isNullable", "5", "1" }, { "isSigned", "5", "false" },
        { "getColumnDisplaySize", "5", "128" },
        { "getColumnLabel", "5", "LITERAL_SUFFIX" },
        { "getColumnName", "5", "LITERAL_SUFFIX" },
        { "getPrecision", "5", "128" }, { "getScale", "5", "0" },
        { "getCatalogName", "5", "LOCALHOST" }, { "getColumnType", "5", "12" },
        { "getColumnTypeName", "5", "VARCHAR" }, { "isReadOnly", "5", "true" },
        { "isWritable", "5", "false" },
        { "isDefinitelyWritable", "5", "false" },
        { "getColumnClassName", "5", "java.lang.String" },
        { "isAutoIncrement", "6", "false" }, { "isCaseSensitive", "6", "true" },
        { "isSearchable", "6", "true" }, { "isCurrency", "6", "false" },
        { "isNullable", "6", "1" }, { "isSigned", "6", "false" },
        { "getColumnDisplaySize", "6", "128" },
        { "getColumnLabel", "6", "CREATE_PARAMS" },
        { "getColumnName", "6", "CREATE_PARAMS" },
        { "getPrecision", "6", "128" }, { "getScale", "6", "0" },
        { "getCatalogName", "6", "LOCALHOST" }, { "getColumnType", "6", "12" },
        { "getColumnTypeName", "6", "VARCHAR" }, { "isReadOnly", "6", "true" },
        { "isWritable", "6", "false" },
        { "isDefinitelyWritable", "6", "false" },
        { "getColumnClassName", "6", "java.lang.String" },
        { "isAutoIncrement", "7", "false" },
        { "isCaseSensitive", "7", "false" }, { "isSearchable", "7", "true" },
        { "isCurrency", "7", "false" }, { "isNullable", "7", "1" },
        { "isSigned", "7", "true" }, { "getColumnDisplaySize", "7", "6" },
        { "getColumnLabel", "7", "NULLABLE" },
        { "getColumnName", "7", "NULLABLE" }, { "getPrecision", "7", "5" },
        { "getScale", "7", "0" }, { "getCatalogName", "7", "LOCALHOST" },
        { "getColumnType", "7", "5" }, { "getColumnTypeName", "7", "SMALLINT" },
        { "isReadOnly", "7", "true" }, { "isWritable", "7", "false" },
        { "isDefinitelyWritable", "7", "false" },
        { "getColumnClassName", "7", "java.lang.Integer" },
        { "isAutoIncrement", "8", "false" },
        { "isCaseSensitive", "8", "false" }, { "isSearchable", "8", "true" },
        { "isCurrency", "8", "false" }, { "isNullable", "8", "1" },
        { "isSigned", "8", "true" }, { "getColumnDisplaySize", "8", "6" },
        { "getColumnLabel", "8", "CASE_SENSITIVE" },
        { "getColumnName", "8", "CASE_SENSITIVE" },
        { "getPrecision", "8", "5" }, { "getScale", "8", "0" },
        { "getCatalogName", "8", "LOCALHOST" }, { "getColumnType", "8", "5" },
        { "getColumnTypeName", "8", "SMALLINT" }, { "isReadOnly", "8", "true" },
        { "isWritable", "8", "false" },
        { "isDefinitelyWritable", "8", "false" },
        { "getColumnClassName", "8", "java.lang.Integer" },
        { "isAutoIncrement", "9", "false" },
        { "isCaseSensitive", "9", "false" }, { "isSearchable", "9", "true" },
        { "isCurrency", "9", "false" }, { "isNullable", "9", "1" },
        { "isSigned", "9", "true" }, { "getColumnDisplaySize", "9", "6" },
        { "getColumnLabel", "9", "SEARCHABLE" },
        { "getColumnName", "9", "SEARCHABLE" }, { "getPrecision", "9", "5" },
        { "getScale", "9", "0" }, { "getCatalogName", "9", "LOCALHOST" },
        { "getColumnType", "9", "5" }, { "getColumnTypeName", "9", "SMALLINT" },
        { "isReadOnly", "9", "true" }, { "isWritable", "9", "false" },
        { "isDefinitelyWritable", "9", "false" },
        { "getColumnClassName", "9", "java.lang.Integer" },
        { "isAutoIncrement", "10", "false" },
        { "isCaseSensitive", "10", "false" }, { "isSearchable", "10", "true" },
        { "isCurrency", "10", "false" }, { "isNullable", "10", "1" },
        { "isSigned", "10", "true" }, { "getColumnDisplaySize", "10", "6" },
        { "getColumnLabel", "10", "UNSIGNED_ATTRIBUTE" },
        { "getColumnName", "10", "UNSIGNED_ATTRIBUTE" },
        { "getPrecision", "10", "5" }, { "getScale", "10", "0" },
        { "getCatalogName", "10", "LOCALHOST" }, { "getColumnType", "10", "5" },
        { "getColumnTypeName", "10", "SMALLINT" },
        { "isReadOnly", "10", "true" }, { "isWritable", "10", "false" },
        { "isDefinitelyWritable", "10", "false" },
        { "getColumnClassName", "10", "java.lang.Integer" },
        { "isAutoIncrement", "11", "false" },
        { "isCaseSensitive", "11", "false" }, { "isSearchable", "11", "true" },
        { "isCurrency", "11", "false" }, { "isNullable", "11", "1" },
        { "isSigned", "11", "true" }, { "getColumnDisplaySize", "11", "6" },
        { "getColumnLabel", "11", "FIXED_PREC_SCALE" },
        { "getColumnName", "11", "FIXED_PREC_SCALE" },
        { "getPrecision", "11", "5" }, { "getScale", "11", "0" },
        { "getCatalogName", "11", "LOCALHOST" }, { "getColumnType", "11", "5" },
        { "getColumnTypeName", "11", "SMALLINT" },
        { "isReadOnly", "11", "true" }, { "isWritable", "11", "false" },
        { "isDefinitelyWritable", "11", "false" },
        { "getColumnClassName", "11", "java.lang.Integer" },
        { "isAutoIncrement", "12", "false" },
        { "isCaseSensitive", "12", "false" }, { "isSearchable", "12", "true" },
        { "isCurrency", "12", "false" }, { "isNullable", "12", "1" },
        { "isSigned", "12", "true" }, { "getColumnDisplaySize", "12", "6" },
        { "getColumnLabel", "12", "AUTO_INCREMENT" },
        { "getColumnName", "12", "AUTO_INCREMENT" },
        { "getPrecision", "12", "5" }, { "getScale", "12", "0" },
        { "getCatalogName", "12", "LOCALHOST" }, { "getColumnType", "12", "5" },
        { "getColumnTypeName", "12", "SMALLINT" },
        { "isReadOnly", "12", "true" }, { "isWritable", "12", "false" },
        { "isDefinitelyWritable", "12", "false" },
        { "getColumnClassName", "12", "java.lang.Integer" },
        { "isAutoIncrement", "13", "false" },
        { "isCaseSensitive", "13", "true" }, { "isSearchable", "13", "true" },
        { "isCurrency", "13", "false" }, { "isNullable", "13", "1" },
        { "isSigned", "13", "false" }, { "getColumnDisplaySize", "13", "128" },
        { "getColumnLabel", "13", "LOCAL_TYPE_NAME" },
        { "getColumnName", "13", "LOCAL_TYPE_NAME" },
        { "getPrecision", "13", "128" }, { "getScale", "13", "0" },
        { "getCatalogName", "13", "LOCALHOST" },
        { "getColumnType", "13", "12" },
        { "getColumnTypeName", "13", "VARCHAR" },
        { "isReadOnly", "13", "true" }, { "isWritable", "13", "false" },
        { "isDefinitelyWritable", "13", "false" },
        { "getColumnClassName", "13", "java.lang.String" },
        { "isAutoIncrement", "14", "false" },
        { "isCaseSensitive", "14", "false" }, { "isSearchable", "14", "true" },
        { "isCurrency", "14", "false" }, { "isNullable", "14", "1" },
        { "isSigned", "14", "true" }, { "getColumnDisplaySize", "14", "6" },
        { "getColumnLabel", "14", "MINIMUM_SCALE" },
        { "getColumnName", "14", "MINIMUM_SCALE" },
        { "getPrecision", "14", "5" }, { "getScale", "14", "0" },
        { "getCatalogName", "14", "LOCALHOST" }, { "getColumnType", "14", "5" },
        { "getColumnTypeName", "14", "SMALLINT" },
        { "isReadOnly", "14", "true" }, { "isWritable", "14", "false" },
        { "isDefinitelyWritable", "14", "false" },
        { "getColumnClassName", "14", "java.lang.Integer" },
        { "isAutoIncrement", "15", "false" },
        { "isCaseSensitive", "15", "false" }, { "isSearchable", "15", "true" },
        { "isCurrency", "15", "false" }, { "isNullable", "15", "1" },
        { "isSigned", "15", "true" }, { "getColumnDisplaySize", "15", "6" },
        { "getColumnLabel", "15", "MAXIMUM_SCALE" },
        { "getColumnName", "15", "MAXIMUM_SCALE" },
        { "getPrecision", "15", "5" }, { "getScale", "15", "0" },
        { "getCatalogName", "15", "LOCALHOST" }, { "getColumnType", "15", "5" },
        { "getColumnTypeName", "15", "SMALLINT" },
        { "isReadOnly", "15", "true" }, { "isWritable", "15", "false" },
        { "isDefinitelyWritable", "15", "false" },
        { "getColumnClassName", "15", "java.lang.Integer" },
        { "isAutoIncrement", "16", "false" },
        { "isCaseSensitive", "16", "false" }, { "isSearchable", "16", "true" },
        { "isCurrency", "16", "false" }, { "isNullable", "16", "1" },
        { "isSigned", "16", "true" }, { "getColumnDisplaySize", "16", "11" },
        { "getColumnLabel", "16", "SQL_DATA_TYPE" },
        { "getColumnName", "16", "SQL_DATA_TYPE" },
        { "getPrecision", "16", "10" }, { "getScale", "16", "0" },
        { "getCatalogName", "16", "LOCALHOST" }, { "getColumnType", "16", "4" },
        { "getColumnTypeName", "16", "INTEGER" },
        { "isReadOnly", "16", "true" }, { "isWritable", "16", "false" },
        { "isDefinitelyWritable", "16", "false" },
        { "getColumnClassName", "16", "java.lang.Integer" },
        { "isAutoIncrement", "17", "false" },
        { "isCaseSensitive", "17", "false" }, { "isSearchable", "17", "true" },
        { "isCurrency", "17", "false" }, { "isNullable", "17", "1" },
        { "isSigned", "17", "true" }, { "getColumnDisplaySize", "17", "11" },
        { "getColumnLabel", "17", "SQL_DATETIME_SUB" },
        { "getColumnName", "17", "SQL_DATETIME_SUB" },
        { "getPrecision", "17", "10" }, { "getScale", "17", "0" },
        { "getCatalogName", "17", "LOCALHOST" }, { "getColumnType", "17", "4" },
        { "getColumnTypeName", "17", "INTEGER" },
        { "isReadOnly", "17", "true" }, { "isWritable", "17", "false" },
        { "isDefinitelyWritable", "17", "false" },
        { "getColumnClassName", "17", "java.lang.Integer" },
        { "isAutoIncrement", "18", "false" },
        { "isCaseSensitive", "18", "false" }, { "isSearchable", "18", "true" },
        { "isCurrency", "18", "false" }, { "isNullable", "18", "1" },
        { "isSigned", "18", "true" }, { "getColumnDisplaySize", "18", "11" },
        { "getColumnLabel", "18", "NUM_PREC_RADIX" },
        { "getColumnName", "18", "NUM_PREC_RADIX" },
        { "getPrecision", "18", "10" }, { "getScale", "18", "0" },
        { "getCatalogName", "18", "LOCALHOST" }, { "getColumnType", "18", "4" },
        { "getColumnTypeName", "18", "INTEGER" },
        { "isReadOnly", "18", "true" }, { "isWritable", "18", "false" },
        { "isDefinitelyWritable", "18", "false" },
        { "getColumnClassName", "18", "java.lang.Integer" },

    };

    String[][] fixup = {};

    String[][] fixup745TS = {
	{"getColumnDisplaySize","2","6"},
	{"getPrecision","2","5"},
	{"getColumnType","2","5"},
	{"getColumnTypeName","2","SMALLINT"},
    };
	


    String[][] fixup715TS = { { "getColumnDisplaySize", "2", "11" },
        { "getPrecision", "2", "10" }, { "getColumnType", "2", "4" },
        { "getColumnTypeName", "2", "INTEGER" }, };
	
	
    String[][] fixupExtended715T = { { "getColumnDisplaySize", "2", "11" },
        { "getPrecision", "2", "10" }, { "getColumnType", "2", "4" },
        { "getColumnTypeName", "2", "INTEGER" }, };

    String[][] fixupExtended716T = {

    };

    String[][] fixupExtended746T = {
	{"getColumnLabel","1","Type Name"},
      {"getColumnLabel","4","Literal Prefix"},
      {"getColumnLabel","5","Literal Suffix"},
      {"getColumnLabel","6","Create Params"},
      {"getColumnLabel","7","Nullable"},
      {"getColumnLabel","8","Case Sensitive"},
      {"getColumnLabel","9","Searchable"},
      {"getColumnLabel","10","Unsigned Attribute"},
      {"getColumnLabel","11","Fixed Prec Scale"},
      {"getColumnLabel","13","Local Type Name"},
      {"getColumnLabel","14","Minimum Scale"},
      {"getColumnLabel","15","Maximum Scale"},

    };

    String[][] fixupExtended746N = {
      {"getColumnLabel","7","Nullable"},
      {"getColumnLabel","9","Searchable"},
      {"isSearchable","1","false"},
      {"getColumnLabel","1","Type                Name"},
      {"isSearchable","2","false"},
      {"isSearchable","3","false"},
      {"isSearchable","4","false"},
      {"getColumnLabel","4","Literal             Prefix"},
      {"isSearchable","5","false"},
      {"getColumnLabel","5","Literal             Suffix"},
      {"isSearchable","6","false"},
      {"getColumnLabel","6","Create              Params"},
      {"isSearchable","7","false"},
      {"isSearchable","8","false"},
      {"getColumnLabel","8","Case                Sensitive"},
      {"isSearchable","9","false"},
      {"isSearchable","10","false"},
      {"getColumnLabel","10","Unsigned            Attribute"},
      {"isSearchable","11","false"},
      {"getColumnLabel","11","Fixed               Prec                Scale"},
      {"isSearchable","12","false"},
      {"isSearchable","13","false"},
      {"getColumnLabel","13","Local               Type                Name"},
      {"isSearchable","14","false"},
      {"getColumnLabel","14","Minimum             Scale"},
      {"isSearchable","15","false"},
      {"getColumnLabel","15","Maximum             Scale"},
      {"isSearchable","16","false"},
      {"isSearchable","17","false"},
      {"isSearchable","18","false"},

    };




    String[][] fixupExtended545N = { { "isSearchable", "1", "false" },
        { "isSearchable", "2", "false" }, { "isSearchable", "3", "false" },
        { "isSearchable", "4", "false" }, { "isSearchable", "5", "false" },
        { "isSearchable", "6", "false" }, { "isSearchable", "7", "false" },
        { "isSearchable", "8", "false" }, { "isSearchable", "9", "false" },
        { "isSearchable", "10", "false" }, { "isSearchable", "11", "false" },
        { "isSearchable", "12", "false" }, { "isSearchable", "13", "false" },
        { "isSearchable", "14", "false" }, { "isSearchable", "15", "false" },
        { "isSearchable", "16", "false" }, { "isSearchable", "17", "false" },
        { "isSearchable", "18", "false" }, };
    String[][] fixupExtended615N = { { "isSearchable", "1", "false" },
        { "isSearchable", "2", "false" }, { "getColumnDisplaySize", "2", "11" },
        { "getPrecision", "2", "10" }, { "getColumnType", "2", "4" },
        { "getColumnTypeName", "2", "INTEGER" },
        { "isSearchable", "3", "false" }, { "isSearchable", "4", "false" },
        { "isSearchable", "5", "false" }, { "isSearchable", "6", "false" },
        { "isSearchable", "7", "false" }, { "isSearchable", "8", "false" },
        { "isSearchable", "9", "false" }, { "isSearchable", "10", "false" },
        { "isSearchable", "11", "false" }, { "isSearchable", "12", "false" },
        { "isSearchable", "13", "false" }, { "isSearchable", "14", "false" },
        { "isSearchable", "15", "false" }, { "isSearchable", "16", "false" },
        { "isSearchable", "17", "false" }, { "isSearchable", "18", "false" }, };

    String[][] fixupExtended616N = { { "isSearchable", "1", "false" },
        { "isSearchable", "2", "false" }, { "isSearchable", "3", "false" },
        { "isSearchable", "4", "false" }, { "isSearchable", "5", "false" },
        { "isSearchable", "6", "false" }, { "isSearchable", "7", "false" },
        { "isSearchable", "8", "false" }, { "isSearchable", "9", "false" },
        { "isSearchable", "10", "false" }, { "isSearchable", "11", "false" },
        { "isSearchable", "12", "false" }, { "isSearchable", "13", "false" },
        { "isSearchable", "14", "false" }, { "isSearchable", "15", "false" },
        { "isSearchable", "16", "false" }, { "isSearchable", "17", "false" },
        { "isSearchable", "18", "false" },

    };

    String[][] fixup614TX = { { "isNullable", "3", "0" },
        { "isNullable", "7", "0" }, { "isNullable", "8", "0" },
        { "isNullable", "9", "0" }, { "isNullable", "10", "0" },
        { "isNullable", "11", "0" }, { "isNullable", "12", "0" },
        { "isNullable", "14", "0" }, { "isNullable", "15", "0" },
        { "isNullable", "16", "0" }, { "isNullable", "17", "0" },
        { "isNullable", "18", "0" }, };

    Object[][] fixupArrayExtended = {

        { "543T", fixup614TX }, { "544T", fixup614TX }, { "545T", fixup614TX },
        { "546T", fixup614TX }, { "614T", fixup614TX }, { "615T", fixup614TX },
        { "617T", fixup614TX }, { "618T", fixup614TX }, { "619T", fixup614TX },

        { "714T", fixupExtended715T }, { "715T", fixupExtended715T },
        { "716T", fixupExtended716T }, { "717T", fixupExtended716T },
        { "718T", fixupExtended716T },
        { "746T", fixupExtended746T },
	{ "719T", fixupExtended716T },

        { "544N", fixupExtended545N }, { "545N", fixupExtended545N },
        { "546N", fixupExtended545N, "Guess 9/4/2012" },
        { "614N", fixupExtended615N }, { "615N", fixupExtended615N },
        { "616N", fixupExtended616N },

        { "714N", fixupExtended615N }, { "715N", fixupExtended615N },
        { "716N", fixupExtended616N }, { "717N", fixupExtended616N },
        { "718N", fixupExtended616N }, { "719N", fixupExtended616N },

        { "725N", fixupExtended615N }, { "726N", fixupExtended616N },
        { "727N", fixupExtended616N },
	{ "747N", fixupExtended746N}, 

    };

    String[][] fixup614N = { { "getColumnDisplaySize", "2", "11" },
        { "getPrecision", "2", "10" }, { "getColumnType", "2", "4" },
        { "getColumnTypeName", "2", "INTEGER" }, };

    Object[][] fixupArray = {

        { "543TX", fixup614TX },
        { "544TX", fixup614TX },
        { "545TX", fixup614TX },
        { "546TX", fixup614TX },

        { "614TX", fixup614TX },
        { "615TX", fixup614TX },
        { "617TX", fixup614TX },
        { "618TX", fixup614TX },
        { "619TX", fixup614TX },

        { "714TS", fixup715TS },
	{ "715TS", fixup715TS },
	{ "745TS", fixup745TS }, 
        // {"716TS", fixup715TS},
        // {"717TS", fixup715TS},

        { "614NS", fixup614N },
        { "615NS", fixup614N },

        { "714NS", fixup614N },
        { "715NS", fixup614N },
        { "725NS", fixup614N },
	{ "747NS",  fixup745TS}, 

    };

    if (checkJdbc30()) {
      try {

        if (extendedMetadata) {
          String url = baseURL_ + ";extended metadata=true";
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            url = baseURL_;
          }
          connection = testDriver_.getConnection(url, userId_, encryptedPassword_);
          dmd = connection.getMetaData();

          fixup = getFixup(fixupArrayExtended, null, "fixupArrayExtended",
              message);

        } else {
          String extra = "X";
          if (isSysibmMetadata()) {
            extra = "S";
          }

          fixup = getFixup(fixupArray, extra, "fixupArray", message);

        }

        if (fixup != null) {

          for (j = 0; j < fixup.length; j++) {
            boolean found = false;
            for (int k = 0; !found && k < methodTests.length; k++) {
              if (fixup[j][0].equals(methodTests[k][0])
                  && fixup[j][1].equals(methodTests[k][1])) {
                methodTests[k] = fixup[j];
                found = true;
              }
            }
          }
        }

        String catalog1 = connection.getCatalog();
        if (catalog1 == null) {
          catalog1 = system_.toUpperCase();
        }

        ResultSet[] rsA = new ResultSet[TESTSIZE];
        ResultSetMetaData[] rsmdA = new ResultSetMetaData[TESTSIZE];
        for (j = 0; j < TESTSIZE; j++) {
          rsA[j] = dmd.getTypeInfo();
          rsmdA[j] = rsA[j].getMetaData();

          ResultSetMetaData rsmd = rsmdA[j];
          passed = verifyRsmd(rsmd, methodTests, catalog1, j, message, prime)
              && passed;

        } /* for j */
        assertCondition(passed,
            message.toString() + added + "\nPRIME=\n" + prime.toString());

      } catch (Exception e) {
        failed(e,
            "Unexpected Error on loop " + j + " col = " + col + " " + added);
      } catch (Error e) {
        failed(e,
            "Unexpected Error on loop " + j + " col = " + col + " " + added);
      }
    }
  }

  /**
   * getTypeInfo() - check readonly conneciton
   **/
  public void Var008() {
    try {
      boolean success = false;

      Connection c = testDriver_.getConnection(baseURL_ + ";access=read only",
          userId_, encryptedPassword_);
      DatabaseMetaData dmd = c.getMetaData();

      ResultSet rs = dmd.getTypeInfo();

      // Verify that the data can be accessed.
      if (rs.next())
        success = true;

      rs.close();
      c.close();
      assertCondition(success);

    } catch (Exception e) {
      failed(e,
          "Unexpected Exception when calling API with autocommit disabled");
    }
  }

  /**
   * getTypeInfo() - get the types -- create a table with the types and check
   * the RSMD against GetTypeInfo
   **/
  public void Var009() {
    StringBuffer sb = new StringBuffer();
    try {
      boolean success = true;
      int[] typeNumbers = new int[100];
      String[] typeNames = new String[100];
      String testTableCollection = JDDMDTest.COLLECTION;
      String testTableName = "JDDMDGTIRS";
      String sql;

      Connection c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
      Statement s = c.createStatement();
      try {
        sql = "DROP TABLE " + testTableCollection + "." + testTableName;
        sb.append("Executing '" + sql + "'\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append("..Caught " + e.toString() + "\n");
      }

      DatabaseMetaData dmd = c.getMetaData();

      ResultSet rs = dmd.getTypeInfo();
      sql = "CREATE TABLE " + testTableCollection + "." + testTableName + " (";
      int colNumber = 1;
      while (rs.next()) {
        int typeNumber = rs.getInt("DATA_TYPE");
        String typeName = rs.getString("TYPE_NAME");
        typeNumbers[colNumber] = typeNumber;
        typeNames[colNumber] = typeName;
        String expandedTypeName = typeName;
        if (typeName.indexOf("(") > 0) {
          int parenIndex = typeName.indexOf("(");
          expandedTypeName = typeName.substring(0, parenIndex + 1) + "10"
              + typeName.substring(parenIndex + 1);
        } else if ((typeName.indexOf("CHAR") >= 0)
            || (typeName.indexOf("BINARY") >= 0)
            || (typeName.indexOf("LOB") >= 0)
            || (typeName.indexOf("GRAPHIC") >= 0)) {
          expandedTypeName += "(10)";
        } else if ((typeName.indexOf("NUMERIC") >= 0)
            || (typeName.indexOf("DECIMAL") >= 0)) {
          expandedTypeName += "(10,2)";
        }

        if ((typeName.indexOf("DISTINCT") >= 0) ||

            (typeName.indexOf("ARRAY") >= 0)) {
          // Skip this column
        } else {
          if (colNumber > 1) {
            sql += ",";
          }

          sql += "C" + colNumber + " " + expandedTypeName;
          colNumber++;
        }

      }
      int columnCount = colNumber - 1;

      rs.close();

      sql += ")";
      sb.append("Executing '" + sql + "'\n");
      s.executeUpdate(sql);

      sql = "SELECT * FROM " + testTableCollection + "." + testTableName;
      rs = s.executeQuery(sql);
      sb.append("--------------------------------------------\n");
      sb.append(" Now verifying resultSetMetaData \n");
      sb.append("--------------------------------------------\n");

      ResultSetMetaData rsmd = rs.getMetaData();
      for (int i = 1; i <= columnCount; i++) {
        int typeNumber = rsmd.getColumnType(i);
        String typeName = rsmd.getColumnTypeName(i);
        int checkNumber = typeNumbers[i];
        if (checkNumber == 1111) {
          /* Unknown to catalog call. Fix if XML or rowId */
          if ("XML".equals(typeName)) {
            checkNumber = 2005;
          } else if ("ROWID".equals(typeName)) {
            checkNumber = -2;
          } else if ("VARCHAR () FOR BIT DATA".equals(typeName)) {
            checkNumber = -2;
          }
        }

        sb.append("For column " + i + " typeNumber=" + typeNumber + " typeName="
            + typeName + "\n");
        if (typeNumber != checkNumber) {
          success = false;
          sb.append("FAILED: for column " + i + " got typeNumber=" + typeNumber
              + " sb " + checkNumber + " typeName=" + typeName + "\n");
        }
        if (!typeName.equals(typeNames[i])) {
          success = false;
          sb.append("FAILED: for column " + i + " got typeName=" + typeName
              + " sb " + typeNames[i] + "\n");

        }
      }

      sb.append("--------------------------------------------\n");
      sb.append(" Now verifying getColumns \n");
      sb.append("--------------------------------------------\n");
      rs = dmd.getColumns(null, testTableCollection, testTableName, "%");
      while (rs.next()) {
        int column = rs.getInt("ORDINAL_POSITION");
        int typeNumber = rs.getInt("DATA_TYPE");
        String typeName = rs.getString("TYPE_NAME");
        sb.append("For column " + column + " typeNumber=" + typeNumber
            + " typeName=" + typeName + "\n");
        if (typeNumber != typeNumbers[column]) {
          success = false;
          sb.append("FAILED: for column " + column + " got typeNumber="
              + typeNumber + " sb " + typeNumbers[column] + " typeName="
              + typeName + "\n");
        }
        if (!typeName.equals(typeNames[column])) {
          success = false;
          sb.append("FAILED: for column " + column + " got typeName=" + typeName
              + " sb " + typeNames[column] + "\n");

        }

      }
      rs.close();

      sql = "DROP TABLE " + testTableCollection + "." + testTableName;
      sb.append("Executing '" + sql + "'\n");
      s.executeUpdate(sql);

      c.close();

      assertCondition(success, sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception " + sb.toString());
    }
  } /* var009 */

  /* Check the results against an expected list */
  /* The expected list is an array of array of strings where */
  /* each sub array contains two elements TYPE_NAME and JDBC_DATA_TYPE */

  boolean checkResultSet(ResultSet rs, String[][] expected, StringBuffer sb)
      throws SQLException {
    boolean passed = true;
    boolean[] foundArray = new boolean[expected.length];
    for (int i = 0; i < foundArray.length; i++) {
      foundArray[i] = false;
    }
    while (rs.next()) {
      String typeName = rs.getString("TYPE_NAME");
      String typeNumber = rs.getString("JDBC_DATA_TYPE");
      boolean found = false;
      for (int i = 0; !found && i < expected.length; i++) {
        if (typeName.equals(expected[i][0])) {
          found = true;
          foundArray[i] = true;

          if (!typeNumber.equals(expected[i][1])) {
            sb.append("For type " + typeName + " found " + typeNumber + " sb "
                + expected[i][1] + "\n");
            passed = false;
          } /* value found */
        } /* type found */
      } /* searching expected */
      if (!found) {
        sb.append("In RS, did not find " + typeName + " " + typeNumber
            + " as an expected type\n");
        passed = false;
      }

    } /* while rs.next() */

    for (int i = 0; i < foundArray.length; i++) {
      if (foundArray[i] == false) {
        sb.append("Did not find " + expected[i][0] + "=" + expected[i][1]
            + " in result Set\n");
        passed = false;
      }
    }
    return passed;
  }

  /*
   * check the metadata table for JDBC types
   */
  public void Var010() {
    StringBuffer sb = new StringBuffer(
        " -- added 10/20/2015 to test SYSIBM.SQLTYPES\n");

    String[][] expected = { { "ARRAY", "2003" }, { "BIGINT", "-5" },
        { "BINARY", "-2" }, { "BLOB", "2004" },
        { "CHAR () FOR BIT DATA", "-2" }, { "CHAR", "1" }, { "CLOB", "2005" },
        { "DATALINK", "70" }, { "DATE", "91" }, { "DBCLOB", "2005" },
        { "DECFLOAT", "1111" }, { "DECIMAL", "3" }, { "DISTINCT", "2001" },
        { "DOUBLE", "8" }, { "GRAPHIC", "1" }, { "INTEGER", "4" },
        { "NCHAR", "-15" }, { "NCLOB", "2011" }, { "NUMERIC", "2" },
        { "NVARCHAR", "-9" }, { "REAL", "7" }, { "ROWID", "-8" },
        { "SMALLINT", "5" }, { "TIME", "92" }, { "TIMESTAMP", "93" },
        { "VARBINARY", "-3" }, { "VARCHAR () FOR BIT DATA", "-3" },
        { "VARCHAR", "12" }, { "VARGRAPHIC", "12" }, { "XML", "2009" },

    };
    
    String[][] expectedBoolean = { { "ARRAY", "2003" }, { "BIGINT", "-5" },
        { "BINARY", "-2" }, { "BLOB", "2004" }, { "BOOLEAN", "16"}, 
        { "CHAR () FOR BIT DATA", "-2" }, { "CHAR", "1" }, { "CLOB", "2005" },
        { "DATALINK", "70" }, { "DATE", "91" }, { "DBCLOB", "2005" },
        { "DECFLOAT", "1111" }, { "DECIMAL", "3" }, { "DISTINCT", "2001" },
        { "DOUBLE", "8" }, { "GRAPHIC", "1" }, { "INTEGER", "4" },
        { "NCHAR", "-15" }, { "NCLOB", "2011" }, { "NUMERIC", "2" },
        { "NVARCHAR", "-9" }, { "REAL", "7" }, { "ROWID", "-8" },
        { "SMALLINT", "5" }, { "TIME", "92" }, { "TIMESTAMP", "93" },
        { "VARBINARY", "-3" }, { "VARCHAR () FOR BIT DATA", "-3" },
        { "VARCHAR", "12" }, { "VARGRAPHIC", "12" }, { "XML", "2009" },

    };

    if (supportedFeatures_.booleanSupport) {
      expected=expectedBoolean; 
    }
    try {
      boolean passed;
      Statement stmt = connection_.createStatement();
      String sql = "SELECT TYPE_NAME, JDBC_DATA_TYPE from SYSIBM.SQLTYPEINFO";
      sb.append("Executing " + sql + "\n");
      ResultSet rs = stmt.executeQuery(sql);
      passed = checkResultSet(rs, expected, sb);
      assertCondition(passed, sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception " + sb.toString());
    }
  }

}
