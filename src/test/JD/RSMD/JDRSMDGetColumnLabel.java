///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSMDGetColumnLabel.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDRSMDGetColumnLabel.java
//
// Classes:      JDRSMDGetColumnLabel
//
////////////////////////////////////////////////////////////////////////
package test.JD.RSMD;

import com.ibm.as400.access.AS400;

import test.JDRSMDTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;

/**
 * Testcase JDRSMDGetColumnLabel. This tests the following method of the JDBC
 * ResultSetMetaData class:
 * 
 * <ul>
 * <li>getColumnLabel()
 * </ul>
 **/
public class JDRSMDGetColumnLabel extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSMDGetColumnLabel";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSMDTest.main(newArgs); 
   }

  private static final String PACKAGE = "JDRSMDGCL";
  private static final String PACKAGE_CACHE_NO = "extended dynamic=false";
  private static String PACKAGE_CACHE_YES = "extended dynamic=true;package="
      + PACKAGE + ";package library=" + JDRSMDTest.COLLECTION
      + ";package cache=true";
  private static String TABLE = JDRSMDTest.COLLECTION + ".JDRSMDGCL";
  private static String TABLE2 = JDRSMDTest.COLLECTION + ".JDRSMDGCL2";
  private static String TABLE3BASE = JDRSMDTest.COLLECTION + ".JDRSMGCL";

  private static String TABLE3 = JDRSMDTest.COLLECTION + ".JDRSMDGCL3";
  private static String T2L28 = "M234567890123456789012345678"; // @C3A
  private static String T2L32 = "M2345678911234567892123456789312"; // @C3A
  // For v5r1-v5r3, the maximum label size is 60
  private static String T2L50 = "M2345678911234567892123456789312345678941234567895"; // @C3A

  private static String T2L60 = "M23456789112345678921234567893123456789412345678951234567896"; // @C3A
  private static String TABLELCN = JDRSMDTest.COLLECTION + ".JDRSMDGCLLCN"; // @D1A

  // Private data.
  private Connection connection2_ = null; // @C1A
  private String properties_ = "";
  private String properties2_ = ""; // @C1A
  private Statement statement_ = null;
  private Statement statement2_ = null; // @C1A
  private ResultSet rs_ = null;
  private ResultSet rs2_ = null; // @C1A
  private ResultSetMetaData rsmd_ = null;
  private ResultSetMetaData rsmd2_ = null; // @C1A

  /**
   * Constructor.
   **/
  public JDRSMDGetColumnLabel(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDRSMDGetColumnLabel", namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {

    // reset connection related variables after -lib is processed.

    PACKAGE_CACHE_YES = "extended dynamic=true;package=" + PACKAGE
        + ";package library=" + JDRSMDTest.COLLECTION + ";package cache=true";
    TABLE = JDRSMDTest.COLLECTION + ".JDRSMDGCL";
    TABLE2 = JDRSMDTest.COLLECTION + ".JDRSMDGCL2";
    TABLE3BASE = JDRSMDTest.COLLECTION + ".JDRSMGCL";
    TABLE3 = JDRSMDTest.COLLECTION + ".JDRSMDGL1";
    TABLELCN = JDRSMDTest.COLLECTION + ".JDRSMDGCLLCN"; // @D1A
    // SQL400 - driver neutral...
    String url = baseURL_
    // String url = "jdbc:as400://" + systemObject_.getSystemName()
         ;
    connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
    statement_ = connection_.createStatement();
    initTable(statement_,  TABLE, " (C INTEGER, COL2 INTEGER, COL3 INTEGER)"); // @C1C
    // Put a label on column 'C' to test extended meta data.
    statement_.executeUpdate("LABEL ON COLUMN " + TABLE + ".C IS 'M'"); // @C1A
    statement_.executeUpdate("LABEL ON COLUMN " + TABLE
        + ".COL3 IS 'MY THIRD INT'"); // @C1A

    initTable(statement_, TABLE2, " (L28 INTEGER, L32 INTEGER, L50 INTEGER, L60 INTEGER)"); // @D3A
    // Put a label on columns to test metadata
    statement_.executeUpdate("LABEL ON COLUMN " + TABLE2 + ".L28 IS '" + T2L28
        + "'"); // @C3A
    statement_.executeUpdate("LABEL ON COLUMN " + TABLE2 + ".L32 IS '" + T2L32
        + "'"); // @C3A
    statement_.executeUpdate("LABEL ON COLUMN " + TABLE2 + ".L50 IS '" + T2L50
        + "'"); // @C3A
    statement_.executeUpdate("LABEL ON COLUMN " + TABLE2 + ".L60 IS '" + T2L60
        + "'"); // @C3A

    initTable(statement_, TABLE3, "(LABELCOLUMN INTEGER NOT NULL)");

    // @D1A
   initTable( statement_,TABLELCN, " (THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST INTEGER)");
        statement_
            .executeUpdate("LABEL ON COLUMN "
                + TABLELCN
                + ".THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST IS 'LONG COLUMN NAME'");
    
   
    connection_.commit(); // for xa

    if (isToolboxDriver()) {
      JDSetupPackage.prime(systemObject_, PACKAGE,
          JDRSMDTest.COLLECTION, "SELECT * FROM " + TABLE);
    } else {
      JDSetupPackage.prime(systemObject_, encryptedPassword_, PACKAGE,
          JDRSMDTest.COLLECTION, "SELECT * FROM " + TABLE, "", getDriver());
    }

    reconnect(PACKAGE_CACHE_NO);
  }

  /**
   * Reconnects with different properties, if needed.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  private void reconnect(String properties) throws Exception {
    if (!properties_.equals(properties)) {
      properties_ = properties;
      if (connection_ != null) {
        if (rs_ != null)
          rs_.close();
        statement_.close();
        connection_.close();
      }

      // SQL400 - driver neutral...
      String url = baseURL_
      // String url = "jdbc:as400://" + systemObject_.getSystemName()
           ;
      connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
      statement_ = connection_.createStatement();
      rs_ = statement_.executeQuery("SELECT * FROM " + TABLE);
      rsmd_ = rs_.getMetaData();
    }
  }

  // @C1A
  /**
   * Reconnects with different properties, if needed.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  private void reconnect2(String properties2) throws Exception {
    if (!properties2_.equals(properties2) || connection2_ == null) {
      properties2_ = properties2;
      if (connection2_ != null) {
        if (rs2_ != null)
          rs2_.close();
        statement2_.close();
        connection2_.close();
      }

      String url = baseURL_  + ";extended metadata=true" + ";"
          + properties2;
      connection2_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
      statement2_ = connection2_.createStatement();
      rs2_ = statement2_.executeQuery("SELECT * FROM " + TABLE);
      rsmd2_ = rs2_.getMetaData();
    }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    if (rs_ != null)
      rs_.close();
    rs_ = null;
    if (rs2_ != null)
      rs2_.close(); // @C1A
    rs2_ = null;
    if (statement2_ != null)
      statement2_.close(); // @C1A
    statement2_ = null;

    cleanupTable(statement_, TABLE);
    cleanupTable(statement_, TABLE2);
    // @D1A
    cleanupTable(statement_, TABLELCN);

    statement_.close();
    connection2_.commit(); // for xa //@C1A
    connection_.commit(); // for xa
    connection2_.close(); // @C1A
    connection_.close();
  }

  
  protected void cleanupConnections() throws Exception {
    connection2_.commit(); // for xa //@C1A
    connection_.commit(); // for xa
    connection2_.close(); // @C1A
    connection_.close();
  }


  /**
   * getColumnLabel() - Check column -1. Should throw an exception. (Package
   * cache = false)
   **/
  public void Var001() {
    try {
      reconnect(PACKAGE_CACHE_NO);
      rsmd_.getColumnLabel(-1);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getColumnLabel() - Check column 0. Should throw an exception. (Package
   * cache = false)
   **/
  public void Var002() {
    try {
      reconnect(PACKAGE_CACHE_NO);
      rsmd_.getColumnLabel(0);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getColumnLabel() - Check a column greater than the max. Should throw an
   * exception. (Package cache = false)
   **/
  public void Var003() {
    try {
      reconnect(PACKAGE_CACHE_NO);
      rsmd_.getColumnLabel(4); // @C1C
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getColumnLabel() - Check a column with a 1 character name. (Package cache =
   * false)
   **/
  public void Var004() {
    try {
      reconnect(PACKAGE_CACHE_NO);
      String s = rsmd_.getColumnLabel(1);
      assertCondition(s.equals("C"), "Got " + s + " sb 'C'");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getColumnLabel() - Check a column with a multiple character name. (Package
   * cache = false)
   **/
  public void Var005() {
    try {
      reconnect(PACKAGE_CACHE_NO);
      String s = rsmd_.getColumnLabel(2);
      assertCondition(s.equals("COL2"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getColumnLabel() - Check column -1. Should throw an exception. (Package
   * cache = true)
   **/
  public void Var006() {
    try {
      reconnect(PACKAGE_CACHE_YES);
      rsmd_.getColumnLabel(-1);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getColumnLabel() - Check column 0. Should throw an exception. (Package
   * cache = true)
   **/
  public void Var007() {
    try {
      reconnect(PACKAGE_CACHE_YES);
      rsmd_.getColumnLabel(0);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getColumnLabel() - Check a column greater than the max. Should throw an
   * exception. (Package cache = true)
   **/
  public void Var008() {
    try {
      reconnect(PACKAGE_CACHE_YES);
      rsmd_.getColumnLabel(4); // @C1C
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getColumnLabel() - Check a column with a 1 character name. (Package cache =
   * true)
   **/
  public void Var009() {
    try {
      reconnect(PACKAGE_CACHE_YES);
      String s = rsmd_.getColumnLabel(1);
      assertCondition(s.equals("C"), "Got " + s + " sb 'C'");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getColumnLabel() - Check a column with a multiple character name. (Package
   * cache = true)
   **/
  public void Var010() {
    try {
      reconnect(PACKAGE_CACHE_YES);
      String s = rsmd_.getColumnLabel(2);
      assertCondition(s.equals("COL2"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getColumnLabel() - Check when the result set is closed.
   **/
  public void Var011() {
    try {
      Statement s = connection_.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + TABLE);
      ResultSetMetaData rsmd = rs.getMetaData();
      rs.close();
      String v = rsmd.getColumnLabel(2);
      s.close();
      assertCondition(v.equals("COL2"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getColumnLabel() - Check when the meta data comes from a prepared
   * statement.
   **/
  public void Var012() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps;
        try {
          ps = connection_.prepareStatement("SELECT * FROM " + TABLE,
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception e) {
          ps = connection_.prepareStatement("SELECT * FROM " + TABLE);
        }
        ResultSetMetaData rsmd = ps.getMetaData();
        String v = rsmd.getColumnLabel(3); // @C1C
        ps.close();
        String expected = "COL3";
        if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
          expected = "MY THIRD INT";
        }
        assertCondition(v.equals(expected), "got '" + v + "' expected '"
            + expected + "'"); // @C1C
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getColumnLabel() - Check when the meta data comes from a database meta data
   * result.
   **/
  public void Var013() {
    try {
      DatabaseMetaData dmd = connection_.getMetaData();
      ResultSet rs = dmd.getTableTypes();
      ResultSetMetaData rsmd = rs.getMetaData();
      String v = rsmd.getColumnLabel(1);
      rs.close();
      assertCondition(v.equals("TABLE_TYPE"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  // @C1A New variations to test "extended metadata" property which will change
  // what this method
  // @C1A returns in some cases when running to v5r2 and above servers.
  /**
   * getColumnLabel() - Check column -1. Should throw an exception. (Package
   * cache = false)
   **/

  public void Var014() {
    try {
      reconnect2(PACKAGE_CACHE_NO);
      rsmd2_.getColumnLabel(-1);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getColumnLabel() - Check column 0. Should throw an exception. (Package
   * cache = false)
   **/

  public void Var015() {
    try {
      reconnect2(PACKAGE_CACHE_NO);
      rsmd2_.getColumnLabel(0);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getColumnLabel() - Check a column greater than the max. Should throw an
   * exception. (Package cache = false)
   **/
  public void Var016() {
    try {
      reconnect2(PACKAGE_CACHE_NO);
      rsmd2_.getColumnLabel(4);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getColumnLabel() - Check a column with a 1 character name. (Package cache =
   * false)
   **/
  public void Var017() {
    try {
      reconnect2(PACKAGE_CACHE_NO);
      String s = rsmd2_.getColumnLabel(1);
      assertCondition(s.equals("M"), "returned " + s + " sb M");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getColumnLabel() - Check a column with a multiple character name. (Package
   * cache = false)
   **/
  public void Var018() {
    try {
      reconnect2(PACKAGE_CACHE_NO);
      String s = rsmd2_.getColumnLabel(2);
      assertCondition(s.equals("COL2"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getColumnLabel() - Check column -1. Should throw an exception. (Package
   * cache = true)
   **/
  public void Var019() {
    try {
      reconnect2(PACKAGE_CACHE_YES);
      rsmd2_.getColumnLabel(-1);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getColumnLabel() - Check column 0. Should throw an exception. (Package
   * cache = true)
   **/
  public void Var020() {
    try {
      reconnect2(PACKAGE_CACHE_YES);
      rsmd2_.getColumnLabel(0);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getColumnLabel() - Check a column greater than the max. Should throw an
   * exception. (Package cache = true)
   **/
  public void Var021() {
    try {
      reconnect2(PACKAGE_CACHE_YES);
      rsmd2_.getColumnLabel(4);
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getColumnLabel() - Check a column with a 1 character name. (Package cache =
   * true)
   * 
   * //Even though package caching is on, users will get the new values
   * supported by "extended //metadata=true".
   **/
  public void Var022() {
    try {
      reconnect2(PACKAGE_CACHE_YES);
      String s = rsmd2_.getColumnLabel(1);
      assertCondition(s.equals("M"), "returned " + s + " sb M");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getColumnLabel() - Check a column with a multiple character name. (Package
   * cache = true)
   **/
  public void Var023() {
    try {
      reconnect2(PACKAGE_CACHE_YES);
      String s = rsmd2_.getColumnLabel(2);
      assertCondition(s.equals("COL2"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getColumnLabel() - Check when the result set is closed.
   **/
  public void Var024() {
    try {
      Statement s = connection2_.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + TABLE);
      ResultSetMetaData rsmd = rs.getMetaData();
      rs.close();
      String v = rsmd.getColumnLabel(3);
      s.close();
      assertCondition(v.equals("MY THIRD INT"), "returned " + v
          + " sb 'MY THIRD INT'");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getColumnLabel() - Check when the meta data comes from a prepared statement
   * that we still get the new data because "extended metadata=true".
   **/
  public void Var025() {
    if (checkJdbc20()) {
      try {
        PreparedStatement ps;
        try {
          ps = connection2_.prepareStatement("SELECT * FROM " + TABLE,
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception e) {
          ps = connection2_.prepareStatement("SELECT * FROM " + TABLE);
        }
        ResultSetMetaData rsmd = ps.getMetaData();
        String v = rsmd.getColumnLabel(3);
        ps.close();
        assertCondition(v.equals("MY THIRD INT"), "returned " + v
            + " sb 'MY THIRD INT'");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getColumnLabel() - Check when the meta data comes from a database meta data
   * result.
   **/
  public void Var026() {
    try {
      DatabaseMetaData dmd = connection2_.getMetaData();
      ResultSet rs = dmd.getTableTypes();
      ResultSetMetaData rsmd = rs.getMetaData();
      String v = rsmd.getColumnLabel(1);
      rs.close();
      if (getRelease() < JDTestDriver.RELEASE_V7R3M0) { 
	  assertCondition(v.equals("TABLE_TYPE"));
      } else {
	  // For sq730 changed to "Table Type" on 5/29/2019
	  assertCondition(v.equals("Table Type") || v.equals("Table               Type") , "Got '"+v+"' sb 'Table Type' for SYSIBM.SQLTBLTYPE.  To fix  -- COMMENT ON TABLE SYSIBM.SQLSCHEMAS IS 'REBUILDIT' -- CALL QSQSYSIBM");
      }



    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getColumnLabel() - Check a column with a multiple character name. (Package
   * cache = false)
   **/
  public void Var027() {
    try {
      reconnect2(PACKAGE_CACHE_NO);
      String s = rsmd2_.getColumnLabel(3);
      assertCondition(s.equals("MY THIRD INT"), "returned " + s
          + " sb 'MY THIRD INT'");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getColumnLabel() - Check a column with a multiple character name. (Package
   * cache = true)
   * 
   * //Even though package caching is on, users will get the new values
   * supported by "extended //metadata=true" .
   **/
  public void Var028() {
    try {
      reconnect2(PACKAGE_CACHE_YES);
      String s = rsmd2_.getColumnLabel(3);
      assertCondition(s.equals("MY THIRD INT"), "returned " + s
          + " sb 'MY THIRD INT'");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getColumnLabel() - Make sure this method still works with a value in the
   * table.
   **/
  public void Var029() {
    try {
      statement_.executeUpdate("INSERT INTO " + TABLE + " VALUES (2,5,7)");
      connection_.commit();
      reconnect2(PACKAGE_CACHE_NO);
      String s = rsmd2_.getColumnLabel(3);
      assertCondition(s.equals("MY THIRD INT"), "returned " + s
          + " sb 'MY THIRD INT'");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getColumnLabel() - Make sure we don't get new values when extended metadata
   * is false.
   **/
  public void Var030() {
    if (checkJdbc20()) {
      try {
        Connection connection3 = testDriver_.getConnection(baseURL_ +  ";extended metadata= false",systemObject_.getUserId(),encryptedPassword_);
        PreparedStatement ps;
        try {
          ps = connection3.prepareStatement("SELECT * FROM " + TABLE,
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception e) {
          ps = connection3.prepareStatement("SELECT * FROM " + TABLE);
        }
        ResultSetMetaData rsmd = ps.getMetaData();
        String v = rsmd.getColumnLabel(2);
        ps.close();
        assertCondition(v.equals("COL2"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getColumnLabel() - Check a column with a multiple character name. (Package
   * cache = true)
   **/
  public void Var031() {
    try {
      reconnect(PACKAGE_CACHE_YES);
      String s = rsmd2_.getColumnLabel(3);
      assertCondition(s.equals("MY THIRD INT"), "returned " + s
          + " sb 'MY THIRD INT'");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Check getting a name from a union and the labels don't exist
   */
  public void Var032() {
    String tableName = JDRSMDTest.COLLECTION + ".JDRSMDColLab32"; // @D2C
    try {
      initTable(statement_,  tableName,
           " (customerid int not null primary key,"
          + "CustFirstName varchar (25)," + "CustLastName varchar (25),"
          + "CustStreetAddress varchar (50)," + "CustCity varchar (30),"
          + "CustState varchar (2)," + "CustZipCode varchar (10),"
          + "CustAreaCode smallint," + "CustPhoneNumber varchar (8))");
      String expectedLabel[] = { "", "CUSTOMERID", "CUSTFIRSTNAME",
          "CUSTLASTNAME", "CUSTSTREETADDRESS", "CUSTCITY", "CUSTSTATE",
          "CUSTZIPCODE", "CUSTAREACODE", "CUSTPHONENUMBER" };
      ResultSet rs = statement_.executeQuery("(select * from " + tableName
          + " where " + tableName + ".customerid = 7)"
          + "UNION (select * from " + tableName + " where " + tableName
          + ".custfirstname = 'George')");
      ResultSetMetaData rsmd = rs.getMetaData();
      int numberOfColumns = rsmd.getColumnCount();
      boolean success = true;
      String errorMessage = "";
      for (int i = 1; i <= numberOfColumns; i++) {
        String label = rsmd.getColumnLabel(i);
        if (!label.equals(expectedLabel[i])) {
          success = false;
          errorMessage += "Column " + i + "actual(" + label + ")!=expected("
              + expectedLabel[i] + ") ";
        }
      }

      cleanupTable(statement_,  tableName);
      assertCondition(success, "Label failed: " + errorMessage
          + " -- added 7/08/04 by native driver");
    } catch (Exception e) {
      failed(e, "Unexpected Exception  -- added 7/08/04 by native driver");
    }

  }

  /**
   * Check getting a name from a union and the labels don't exist - extended
   * metadata is set
   */

  public void Var033() {
    String tableName = JDRSMDTest.COLLECTION + ".JDRSMDColLab33"; // @D2C

    try {
      reconnect2("extended metadata=true");
      initTable(statement2_, tableName, " (customerid int not null primary key,"
          + "CustFirstName varchar (25)," + "CustLastName varchar (25),"
          + "CustStreetAddress varchar (50)," + "CustCity varchar (30),"
          + "CustState varchar (2)," + "CustZipCode varchar (10),"
          + "CustAreaCode smallint," + "CustPhoneNumber varchar (8))");
      String expectedLabel[] = { "", "CUSTOMERID", "CUSTFIRSTNAME",
          "CUSTLASTNAME", "CUSTSTREETADDRESS", "CUSTCITY", "CUSTSTATE",
          "CUSTZIPCODE", "CUSTAREACODE", "CUSTPHONENUMBER" };
      ResultSet rs = statement2_.executeQuery("(select * from " + tableName
          + " where " + tableName + ".customerid = 7)"
          + "UNION (select * from " + tableName + " where " + tableName
          + ".custfirstname = 'George')");
      ResultSetMetaData rsmd = rs.getMetaData();
      int numberOfColumns = rsmd.getColumnCount();
      boolean success = true;
      String errorMessage = "";
      for (int i = 1; i <= numberOfColumns; i++) {
        String label = rsmd.getColumnLabel(i);
        if (!label.equals(expectedLabel[i])) {
          success = false;
          errorMessage += "Column " + i + "actual(" + label + ")!=expected("
              + expectedLabel[i] + ") ";
        }
      }

      cleanupTable(statement2_, tableName);
      assertCondition(success, "Label failed: " + errorMessage
          + " -- added 7/08/04 by native driver");
    } catch (Exception e) {
      failed(e, "Unexpected Exception  -- added 7/08/04 by native driver");
    }

  }

  // @D1A
  /**
   * getColumnLabel() - Check a column with a 128 byte column name. (Package
   * cache = false)
   **/
  public void Var034() {
    try {
      reconnect(PACKAGE_CACHE_NO);
      ResultSet rs = statement_.executeQuery("SELECT * FROM " + TABLELCN);
      ResultSetMetaData rsmd = rs.getMetaData();
      String s = rsmd.getColumnLabel(1);
      String expected = "THISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMETHISISACOLUMNWITHANONEHUNDREDTWENTYEIGHTBYTECOLUMNNAMEABCDEFGHIJKLMNOPQRST";
      assertCondition(s.equals(expected), "\nGot      " + s + "\nExpected "
          + expected
          + "Added by Toolbox 8/12/2004 to test 128 byte column names.");
    } catch (Exception e) {
      failed(
          e,
          "Unexpected Exception.  Added by Toolbox 8/12/2004 to test 128 byte column names.");
    }
  }

  /**
   * Get an 28 character label
   */
  public void Var035() {
    try {
      reconnect2(PACKAGE_CACHE_YES);
      ResultSet rs2 = statement2_.executeQuery("SELECT * FROM " + TABLE2);
      ResultSetMetaData rsmd2 = rs2.getMetaData();

      String s = rsmd2.getColumnLabel(1);
      rs2.close();
      assertCondition(s.equals(T2L28), "Column Label '" + s + "' != '" + T2L28
          + "' -- added 9/27/2004 by native driver");
    } catch (Exception e) {
      failed(e, "Unexpected Exception -- added 9/27/2004 by native driver ");
    }
  }

  /**
   * Get an 32 character label
   */
  public void Var036() {
    try {
      reconnect2(PACKAGE_CACHE_YES);
      ResultSet rs2 = statement2_.executeQuery("SELECT * FROM " + TABLE2);
      ResultSetMetaData rsmd2 = rs2.getMetaData();

      String s = rsmd2.getColumnLabel(2);
      rs2.close();
      assertCondition(s.equals(T2L32), "Column Label '" + s + "' != '" + T2L32
          + "' -- added 9/27/2004 by native driver");
    } catch (Exception e) {
      failed(e, "Unexpected Exception -- added 9/27/2004 by native driver ");
    }
  }

  /**
   * Get an 50 character label
   */
  public void Var037() {
    try {
      reconnect2(PACKAGE_CACHE_YES);
      ResultSet rs2 = statement2_.executeQuery("SELECT * FROM " + TABLE2);
      ResultSetMetaData rsmd2 = rs2.getMetaData();

      String s = rsmd2.getColumnLabel(3);
      rs2.close();
      assertCondition(s.equals(T2L50), "Column Label '" + s + "' != '" + T2L50
          + "' -- added 9/27/2004 by native driver");
    } catch (Exception e) {
      failed(e, "Unexpected Exception -- added 9/27/2004 by native driver ");
    }
  }

  /**
   * Get an 60 character label
   */
  public void Var038() {
    try {
      reconnect2(PACKAGE_CACHE_YES);
      ResultSet rs2 = statement2_.executeQuery("SELECT * FROM " + TABLE2);
      ResultSetMetaData rsmd2 = rs2.getMetaData();

      String s = rsmd2.getColumnLabel(4);
      rs2.close();
      assertCondition(s.equals(T2L60), "Column Label '" + s + "' != '" + T2L60
          + "' -- added 9/27/2004 by native driver");
    } catch (Exception e) {
      failed(e, "Unexpected Exception -- added 9/27/2004 by native driver ");
    }
  }

  /**
   * Test to check all the lengths
   */

  public void Var039() {
    try {
      reconnect2(PACKAGE_CACHE_YES);
      boolean passed = true;
      String label = "L";
      for (int i = 1; i < 60; i++) {
        if (true) {
          // Drop old table
          cleanupTable(statement_,  TABLE3);
          
          // get new table name.. Drop just in case it exists
          TABLE3 = TABLE3BASE + i;

          initTable(statement_,  TABLE3,  " (LABELCOLUMN INTEGER NOT NULL)");
        }

        statement2_.executeUpdate("LABEL ON COLUMN " + TABLE3
            + " (LABELCOLUMN IS '" + label + "')");
        // alter column is needed to make the change visible visible
        // this does not work in V5R3. Because of this, we use different
        // tables on each iteration.

        statement2_.executeUpdate("ALTER TABLE " + TABLE3
            + " ALTER COLUMN LABELCOLUMN SET NOT  NULL");

        reconnect2(PACKAGE_CACHE_YES);
        ResultSet rs2 = statement2_.executeQuery("SELECT * FROM " + TABLE3);
        ResultSetMetaData rsmd2 = rs2.getMetaData();

        String s = rsmd2.getColumnLabel(1);
        if (!s.equals(label)) {
          output_.println("Column Label size = " + i + " '" + s + "' != '"
              + label + "'");
          passed = false;
        }
        rs2.close();
        label += (i % 10);
      }

      assertCondition(passed,
          "testcase failed -- see previous messages -- added 9/27/2004 by native driver ");

    } catch (Exception e) {
      failed(e, "Unexpected Exception -- added 9/27/2004 by native driver ");
    }
  }

  /**
   * Get a label from a stored procedure call
   */
  public void Var040() {
    StringBuffer sb = new StringBuffer(" -- added 11/18/2013 ");
    String sql = "";
    try {
      Connection connection3 = testDriver_.getConnection(baseURL_ 
          + ";extended metadata=false",systemObject_.getUserId(),encryptedPassword_);
      Statement s = connection3.createStatement();
      String procedure = JDRSMDTest.COLLECTION + ".JDRSMDCL40";
      try {
        sql = "Drop procedure " + procedure;
        sb.append("Executing: " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append(" .. ignored exception " + e + "\n");
      }
      sql = "CREATE PROCEDURE "
          + procedure
          + " RESULT SETS 1 LANGUAGE SQL BEGIN DECLARE c1 cursor for select 'one' as \"this one\" from sysibm.sysdummy1; open c1; set result sets cursor c1; end";
      sb.append("Executing: " + sql + "\n");
      s.execute(sql);
      sql = "CALL " + procedure + "()";
      sb.append("Executing: " + sql + "\n");
      s.execute(sql);

      sql = "Getting result set";
      ResultSet rs = s.getResultSet();
      ResultSetMetaData rsmd = rs.getMetaData();
      String v = rsmd.getColumnLabel(1);
      sql = "Drop procedure " + procedure;
      sb.append("Executing: " + sql + "\n");
      s.executeUpdate(sql);
      s.close();
      connection3.close();
      assertCondition(v.equals("this one"), "got '" + v + "' sb 'this one'");

    } catch (Exception e) {
      failed(e, "Unexpected Exception " + sb.toString() + " statement was "
          + sql);
    }
  }

  /**
   * Get a label from a stored procedure call with extended metadata=true
   */
  public void Var041() {
    StringBuffer sb = new StringBuffer(
        " -- Get label with extended metadata = true -- added 11/18/2013 ");
    String sql = "";
    try {
      Connection connection3 = testDriver_.getConnection(baseURL_  + ";extended metadata=true",systemObject_.getUserId(),encryptedPassword_);
      Statement s = connection3.createStatement();
      String procedure = JDRSMDTest.COLLECTION + ".JDRSMDCL40";
      try {
        sql = "Drop procedure " + procedure;
        sb.append("Executing: " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append(" .. ignored exception " + e + "\n");
      }
      sql = "CREATE PROCEDURE "
          + procedure
          + " RESULT SETS 1 LANGUAGE SQL BEGIN DECLARE c1 cursor for select 'one' as \"this one\" from sysibm.sysdummy1; open c1; set result sets cursor c1; end";
      sb.append("Executing: " + sql + "\n");
      s.execute(sql);
      sql = "CALL " + procedure + "()";
      sb.append("Executing: " + sql + "\n");
      s.execute(sql);

      sql = "Getting result set";
      ResultSet rs = s.getResultSet();
      ResultSetMetaData rsmd = rs.getMetaData();
      String v = rsmd.getColumnLabel(1);
      sql = "Drop procedure " + procedure;
      sb.append("Executing: " + sql + "\n");
      s.executeUpdate(sql);
      s.close();
      connection3.close();
      assertCondition(v.equals("this one"), "got '" + v + "' sb 'this one'"
          + sb.toString());

    } catch (Exception e) {
      failed(e, "Unexpected Exception " + sb.toString() + " statement was "
          + sql);
    }
  }

  /**
   * Get an alias from a stored procedure call
   */
  public void Var042() {
    StringBuffer sb = new StringBuffer(" -- added 11/18/2013 ");
    String sql = "";
    try {
      Connection connection3 = testDriver_.getConnection(baseURL_ 
          + ";extended metadata=false",systemObject_.getUserId(),encryptedPassword_);
      Statement s = connection3.createStatement();
      String procedure = JDRSMDTest.COLLECTION + ".JDRSMDCL42";
      try {
        sql = "Drop procedure " + procedure;
        sb.append("Executing: " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append(" .. ignored exception " + e + "\n");
      }
      sql = "CREATE PROCEDURE "
          + procedure
          + " RESULT SETS 1 LANGUAGE SQL BEGIN DECLARE c1 cursor for select 'one' as \"this one\" from sysibm.sysdummy1; open c1; set result sets cursor c1; end";
      sb.append("Executing: " + sql + "\n");
      s.execute(sql);
      sql = "CALL " + procedure + "()";
      sb.append("Preparing: " + sql + "\n");
      CallableStatement cstmt = connection3.prepareCall(sql);
      sb.append("Executing: " + sql + "\n");
      cstmt.execute();
      sql = "Getting result set";
      ResultSet rs = cstmt.getResultSet();
      ResultSetMetaData rsmd = rs.getMetaData();
      String v = rsmd.getColumnLabel(1);
      sql = "Drop procedure " + procedure;
      sb.append("Executing: " + sql + "\n");
      s.executeUpdate(sql);
      s.close();
      connection3.close();
      assertCondition(v.equals("this one"), "got '" + v + "' sb 'this one'");

    } catch (Exception e) {
      failed(e, "Unexpected Exception " + sb.toString() + " statement was "
          + sql);
    }
  }

  /**
   * Get an alias from a stored procedure call with extended metadata=true
   */
  public void Var043() {
    StringBuffer sb = new StringBuffer(" -- added 11/18/2013 ");
    String sql = "";
    try {
      Connection connection3 = testDriver_.getConnection(baseURL_ 
          + ";extended metadata=true",systemObject_.getUserId(),encryptedPassword_);
      Statement s = connection3.createStatement();
      String procedure = JDRSMDTest.COLLECTION + ".JDRSMDCL43";
      try {
        sql = "Drop procedure " + procedure;
        sb.append("Executing: " + sql + "\n");
        s.executeUpdate(sql);
      } catch (Exception e) {
        sb.append(" .. ignored exception " + e + "\n");
      }
      sql = "CREATE PROCEDURE "
          + procedure
          + " RESULT SETS 1 LANGUAGE SQL BEGIN "
          + "DECLARE c1 cursor for "
          + "select 'one' as \"this one\", 'two' as \"this two\", 'three' as \"this three\" from sysibm.sysdummy1; "
          + "open c1; set result sets cursor c1; end";
      sb.append("Executing: " + sql + "\n");
      s.execute(sql);
      sql = "CALL " + procedure + "()";
      sb.append("Preparing: " + sql + "\n");
      CallableStatement cstmt = connection3.prepareCall(sql);
      sb.append("Executing: " + sql + "\n");
      cstmt.execute();
      sql = "Getting result set";
      ResultSet rs = cstmt.getResultSet();
      ResultSetMetaData rsmd = rs.getMetaData();
      String v = rsmd.getColumnLabel(1);
      sql = "Drop procedure " + procedure;
      sb.append("Executing: " + sql + "\n");
      s.executeUpdate(sql);
      s.close();
      connection3.close();
      assertCondition(v.equals("this one"), "got '" + v + "' sb 'this one'"
          + sb.toString());

    } catch (Exception e) {
      failed(e, "Unexpected Exception " + sb.toString() + " statement was "
          + sql);
    }
  }

  /**
   * Get column name / label / alias from a stored procedure call with extended
   * metadata=true
   */
  public void Var044() {
    boolean passed = true; 
    StringBuffer sb = new StringBuffer(" -- added 11/2/2015 ");
    String sql = "";
    try {
      String procedure = JDRSMDTest.COLLECTION + ".JDRSMDCL44";
      String tableA    = JDRSMDTest.COLLECTION + ".JDRSMCL44A";
      String tableB    = JDRSMDTest.COLLECTION + ".JDRSMCL44B";
      String tableC    = JDRSMDTest.COLLECTION + ".JDRSMCL44C";
      Connection connection3 = testDriver_.getConnection(baseURL_ 
          + ";extended metadata=true",systemObject_.getUserId(),encryptedPassword_);
      Statement s = connection3.createStatement();
      
      String[] dropStatements = {
          "Drop procedure " + procedure,
      }; 
      
      for (int i = 0; i < dropStatements.length; i++) {
        try {
          sql = dropStatements[i];
          sb.append("Executing: " + sql + "\n");
          s.executeUpdate(sql);
        } catch (Exception e) {
          sb.append(" .. ignored exception " + e + "\n");

        }
      }
      
      
      initTable(s,  tableA, "(ANAME1 INT, ANAME2 INT, ANAME3 INT)");
      initTable(s,  tableB,"(BNAME1 INT, BNAME2 INT, BNAME3 INT, BNAME4 INT, BNAME5 INT, BNAME6 INT)");
      initTable(s, tableC,"(CNAME1 INT, CNAME2 INT, CNAME3 INT, CNAME4 INT, CNAME5 INT, CNAME6 INT, CNAME7 INT, CNAME8 INT, CNAME9 INT)");
          String[] setupStatements = {
          "INSERT INTO "+tableA+" VALUES(1,2,3)", 
          "INSERT INTO "+tableB+" VALUES(1,2,3,4,5,6)", 
          "INSERT INTO "+tableC+" VALUES(1,2,3,4,5,6,7,8,9)", 
          "LABEL ON COLUMN " + tableA  + " (ANAME2 IS 'A label 2')",
          "LABEL ON COLUMN " + tableA  + " (ANAME3 IS 'A label 3')",
          "LABEL ON COLUMN " + tableB  + " (BNAME2 IS 'B label 2')",
          "LABEL ON COLUMN " + tableB  + " (BNAME3 IS 'B label 3')",
          "LABEL ON COLUMN " + tableB  + " (BNAME5 IS 'B label 5')",
          "LABEL ON COLUMN " + tableB  + " (BNAME6 IS 'B label 6')",
          "LABEL ON COLUMN " + tableC  + " (CNAME2 IS 'C label 2')",
          "LABEL ON COLUMN " + tableC  + " (CNAME3 IS 'C label 3')",
          "LABEL ON COLUMN " + tableC  + " (CNAME5 IS 'C label 5')",
          "LABEL ON COLUMN " + tableC  + " (CNAME6 IS 'C label 6')",
          "LABEL ON COLUMN " + tableC  + " (CNAME8 IS 'C label 8')",
          "LABEL ON COLUMN " + tableC  + " (CNAME9 IS 'C label 9')",
          "CREATE PROCEDURE " + procedure
              + " DYNAMIC RESULT SETS 3 LANGUAGE SQL BEGIN "
              + "DECLARE c1 cursor for select ANAME1,ANAME2,ANAME3 as \"A alias 3\" from "+tableA+" ; "
              + "DECLARE c2 cursor for select BNAME1,BNAME2,BNAME3 as \"B alias 3\", BNAME4,BNAME5,BNAME6 as \"B alias 6\" from "+tableB+" ; "
              + "DECLARE c3 cursor for select CNAME1,CNAME2,CNAME3 as \"C alias 3\", CNAME4,CNAME5,CNAME6 as \"C alias 6\", CNAME7,CNAME8,CNAME9 as \"C alias 9\" from "+tableC+" ; "
              + "open c1; " 
              + "open c2;  " 
              + "open c3;  " 
              + "end", 

      }; 
      
      for (int i = 0; i < setupStatements.length; i++) { 
         sql = setupStatements[i];        
         sb.append("Executing: " + sql + "\n");
         s.execute(sql);
      }
      
      
      
      sql = "CALL " + procedure + "()";
      sb.append("Preparing: " + sql + "\n");
      CallableStatement cstmt = connection3.prepareCall(sql);
      sb.append("Executing: " + sql + "\n");
      cstmt.execute();
      sql = "Getting result set";
      ResultSet rs = cstmt.getResultSet();
      ResultSetMetaData rsmd = rs.getMetaData();
      {
      String a1 = rsmd.getColumnLabel(1);
      String a2 = rsmd.getColumnLabel(2);
      String a3 = rsmd.getColumnLabel(3);
      if (! a1.equals("ANAME1")) {
        passed = false; sb.append("For A1 got "+a1+" sb ANAME1\n"); 
      }
      if (! a2.equals("A label 2")) {
        passed = false; sb.append("For A2 got '"+a2+"' sb 'A label 2'\n"); 
      }
      if (! a3.equals("A alias 3")) {
        passed = false; sb.append("For A3 got '"+a3+"' sb 'A alias 3'\n"); 
      }
      }
      cstmt.getMoreResults(); 
      rs = cstmt.getResultSet();
      rsmd = rs.getMetaData();
      {
      String b1 = rsmd.getColumnLabel(1);
      String b2 = rsmd.getColumnLabel(2);
      String b3 = rsmd.getColumnLabel(3);
      String b4 = rsmd.getColumnLabel(4);
      String b5 = rsmd.getColumnLabel(5);
      String b6 = rsmd.getColumnLabel(6);
      if (! b1.equals("BNAME1")) {
        passed = false; sb.append("For B1 got "+b1+" sb BNAME1\n"); 
      }
      if (! b2.equals("B label 2")) {
        passed = false; sb.append("For B2 got '"+b2+"' sb 'B label 2'\n"); 
      }
      if (! b3.equals("B alias 3")) {
        passed = false; sb.append("For B3 got '"+b3+"' sb 'B alias 3'\n"); 
      }
      if (! b4.equals("BNAME4")) {
        passed = false; sb.append("For B4 got "+b4+" sb BNAME4\n"); 
      }
      if (! b5.equals("B label 5")) {
        passed = false; sb.append("For B5 got '"+b5+"' sb 'B label 5'\n"); 
      }
      if (! b6.equals("B alias 6")) {
        passed = false; sb.append("For B6 got '"+b6+"' sb 'B alias 6'\n"); 
      }
      }
      cstmt.getMoreResults(); 
      rs = cstmt.getResultSet();
      rsmd = rs.getMetaData();
      String c1 = rsmd.getColumnLabel(1);
      String c2 = rsmd.getColumnLabel(2);
      String c3 = rsmd.getColumnLabel(3);
      String c4 = rsmd.getColumnLabel(4);
      String c5 = rsmd.getColumnLabel(5);
      String c6 = rsmd.getColumnLabel(6);
      String c7 = rsmd.getColumnLabel(7);
      String c8 = rsmd.getColumnLabel(8);
      String c9 = rsmd.getColumnLabel(9);
      if (! c1.equals("CNAME1")) {
        passed = false; sb.append("For C1 got "+c1+" sb CNAME1\n"); 
      }
      if (! c2.equals("C label 2")) {
        passed = false; sb.append("For C2 got '"+c2+"' sb 'C label 2'\n"); 
      }
      if (! c3.equals("C alias 3")) {
        passed = false; sb.append("For C3 got '"+c3+"' sb 'C alias 3'\n"); 
      }
      if (! c4.equals("CNAME4")) {
        passed = false; sb.append("For C4 got "+c4+" sb CNAME4\n"); 
      }
      if (! c5.equals("C label 5")) {
        passed = false; sb.append("For C5 got '"+c5+"' sb 'C label 5'\n"); 
      }
      if (! c6.equals("C alias 6")) {
        passed = false; sb.append("For C6 got '"+c6+"' sb 'C alias 6'\n"); 
      }
      if (! c7.equals("CNAME7")) {
        passed = false; sb.append("For C7 got "+c7+" sb CNAME7\n"); 
      }
      if (! c8.equals("C label 8")) {
        passed = false; sb.append("For C8 got '"+c8+"' sb 'C label 8'\n"); 
      }
      if (! c9.equals("C alias 9")) {
        passed = false; sb.append("For C9 got '"+c9+"' sb 'C alias 9'\n"); 
      }
      
      cstmt.close(); 
      

      for (int i = 0; i < dropStatements.length; i++) {
        try {
          sql = dropStatements[i];
          sb.append("Executing: " + sql + "\n");
          s.executeUpdate(sql);
        } catch (Exception e) {
          passed = false; 
          sb.append(" .. Unexpected exception " + e + "\n");

        }
      }
      
      
      
      s.close();
      connection3.close();
      assertCondition(passed,sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception " + sb.toString() + " statement was "
          + sql);
    }
  }

}
