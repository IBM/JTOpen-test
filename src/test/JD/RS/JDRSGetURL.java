///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetURL.java
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
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;
import java.net.URL;

/**
 * Testcase JDRSGetURL. This tests the following method of the JDBC ResultSet
 * class:
 * 
 * <ul>
 * <li>getURL()
 * </ul>
 **/
public class JDRSGetURL extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetURL";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }

  // Private data.
  private Statement statement_;
  private Statement statement0_;
  private ResultSet rs_;

  /**
   * Constructor.
   **/
  public JDRSGetURL(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDRSGetURL", namesAndVars, runMode, fileOutputStream,
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
         
        + ";date format=iso;time format=iso";
    connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
    setAutoCommit(connection_, false); // @E1A

    statement0_ = connection_.createStatement();

    if (isJdbc20()) {
      statement_ = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
          ResultSet.CONCUR_UPDATABLE);
      statement_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_GET
          + " (C_KEY) VALUES ('DUMMY_ROW')");
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
   * getURL() - Should throw exception when the result set is closed.
   **/
  public void Var001() {
    if (checkJdbc30()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
        rs.next();
        rs.close();
        rs.getURL(1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Should throw exception when cursor is not pointing to a row.
   **/
  public void Var002() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
        URL v = rs.getURL(1);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Should throw an exception when the column is an invalid index.
   **/
  public void Var003() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
        JDRSTest.position0(rs, "LOB_FULL");
        URL v = rs.getURL(100);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * getURL() - Should throw an exception when the column is 0.
   **/
  public void Var004() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
        JDRSTest.position0(rs, "LOB_FULL");
        URL v = rs.getURL(0);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * getURL() - Should throw an exception when the column is -1.
   **/
  public void Var005() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
        JDRSTest.position0(rs, "LOB_FULL");
        URL v = rs.getURL(-1);
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Should work when the column index is valid.
   **/
  public void Var006() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
        JDRSTest.position0(rs, "LOB_FULL");
        URL v = rs.getURL(2);
        assertCondition(v.toString()
            .equalsIgnoreCase("https://github.com/IBM/JTOpen-test/blob/main/README.testing.txt"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getURL() - Should throw an exception when the column name is null.
   **/
  public void Var007() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable(
          "JCC throws null pointer exception when column name is null ");
    } else {

      if (checkJdbc30()) {
        try {
          ResultSet rs = statement0_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
          JDRSTest.position0(rs, "LOB_FULL");
          rs.getURL(null);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getURL() - Should throw an exception when the column name is an empty
   * string.
   **/
  public void Var008() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
        JDRSTest.position0(rs, "LOB_FULL");
        rs.getURL("");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Should throw an exception when the column name is invalid.
   **/
  public void Var009() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
        JDRSTest.position0(rs, "LOB_FULL");
        rs.getURL("INVALID");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Should work when the column name is valid.
   **/
  public void Var010() {
    if (checkDatalinkSupport()) {
      if (checkJdbc30()) {
        try {
          ResultSet rs = statement0_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
          JDRSTest.position0(rs, "LOB_FULL");
          URL v = rs.getURL("C_DATALINK");
          assertCondition(v.toString()
              .equalsIgnoreCase("https://github.com/IBM/JTOpen-test/blob/main/README.testing.txt"));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getURL() - Should return null when the column is NULL.
   **/
  public void Var011() {
    if (checkDatalinkSupport()) {
      if (checkJdbc30()) {
        try {
          ResultSet rs = statement0_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
          JDRSTest.position0(rs, "LOB_NULL");
          URL v = rs.getURL("C_DATALINK");
          assertCondition(v == null);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * getURL() - Get from a SMALLINT.
   **/
  public void Var012() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "NUMBER_POS");
        URL v = rs.getURL("C_SMALLINT");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from a INTEGER.
   **/
  public void Var013() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "NUMBER_POS");
        URL v = rs.getURL("C_INTEGER");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from a REAL.
   **/
  public void Var014() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "NUMBER_POS");
        URL v = rs.getURL("C_REAL");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from a FLOAT.
   **/
  public void Var015() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "NUMBER_POS");
        URL v = rs.getURL("C_FLOAT");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from a DOUBLE.
   **/
  public void Var016() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "NUMBER_POS");
        URL v = rs.getURL("C_DOUBLE");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from a DECIMAL.
   **/
  public void Var017() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "NUMBER_POS");
        URL v = rs.getURL("C_DECIMAL_50");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from a NUMERIC.
   **/
  public void Var018() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "NUMBER_POS");
        URL v = rs.getURL("C_NUMERIC_105");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from a CHAR, where the value does not translate to a time.
   **/
  public void Var019() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "CHAR_FULL");
        URL v = rs.getURL("C_CHAR_50");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from a VARCHAR, where the value does not translate to a
   * time.
   **/
  public void Var020() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "CHAR_FULL");
        URL v = rs.getURL("C_VARCHAR_50");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * getURL() - Get from a BINARY.
   **/
  public void Var021() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "BINARY_TRANS");
        URL v = rs.getURL("C_BINARY_20");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from a VARBINARY.
   **/
  public void Var022() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "BINARY_TRANS");
        URL v = rs.getURL("C_VARBINARY_20");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from a CLOB.
   **/
  public void Var023() {
    if (checkJdbc30()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement0_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position0(rs, "LOB_FULL");
          URL v = rs.getURL("C_CLOB");
          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getURL() - Get from a DBCLOB.
   **/
  public void Var024() {
    if (checkJdbc30()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement0_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position0(rs, "LOB_FULL");
          URL v = rs.getURL("C_DBCLOB");
          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }

  }

  /**
   * getURL() - Get from a BLOB.
   **/
  public void Var025() {
    if (checkJdbc30()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement0_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position0(rs, "LOB_FULL");
          URL v = rs.getURL("C_BLOB");
          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }

  }

  /**
   * getURL() - Get from a DATE.
   **/
  public void Var026() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "DATE_1998");
        URL v = rs.getURL("C_DATE");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from a TIME.
   **/
  public void Var027() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "DATE_2000");
        URL v = rs.getURL("C_TIME");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from a TIMESTAMP.
   **/
  public void Var028() {
    if (checkJdbc30()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
        JDRSTest.position0(rs, "DATE_2000");
        URL v = rs.getURL("C_TIMESTAMP");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }

    }

  }

  /**
   * getURL() - Get from a DISTINCT.
   **/
  public void Var029() {
    if (checkJdbc30()) {
      if (checkLobSupport()) {
        try {
          ResultSet rs = statement0_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GETDL);
          JDRSTest.position0(rs, "LOB_FULL");
          URL v = rs.getURL("C_DISTINCT");
          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }

      }
    }

  }

  /**
   * getURL() - Get from a BIGINT.
   **/
  public void Var030() {
    if (checkJdbc30()) {
      if (checkBigintSupport()) {
        try {
          ResultSet rs = statement0_
              .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_GET);
          JDRSTest.position0(rs, "NUMBER_NEG");
          URL v = rs.getURL("C_BIGINT");
          failed("Didn't throw SQLException" + v);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * getURL() - Get from DFP16:
   **/
  public void Var031() {
    if (checkDecFloatSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP16);
        rs.next();
        URL v = rs.getURL(1);
        failed("Didn't throw SQLException " + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from DFP34:
   **/
  public void Var032() {
    if (checkDecFloatSupport()) {
      try {
        Statement s = connection_.createStatement();
        ResultSet rs = s.executeQuery(
            "SELECT * FROM " + JDRSTest.RSTEST_DFP34 + " where C1 is NOT NULL");
        rs.next();
        URL v = rs.getURL(1);
        failed("Didn't throw SQLException call getURL on a DFP34 " + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from a TIMESTAMP(12).
   **/
  public void Var033() {
    if (checkTimestamp12Support()) {
      testGet(statement0_,
          "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
          "getURL", "EXCEPTION:Data type mismatch.", " -- added 11/19/2012");

    }
  }

  /**
   * getUnicodeStream() - Get from a TIMESTAMP(0).
   **/
  public void Var034() {
    if (checkTimestamp12Support()) {
      testGet(statement0_,
          "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
          "getURL", "EXCEPTION:Data type mismatch.", " -- added 11/19/2012");

    }
  }

  /**
   * getURL() - Get from a BOOLEAN.
   **/
  public void Var035() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_TRUE");
        URL v = rs.getURL("C_BOOLEAN");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from a BOOLEAN.
   **/
  public void Var036() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_FALSE");
        URL v = rs.getURL("C_BOOLEAN");
        failed("Didn't throw SQLException" + v);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getURL() - Get from a BOOLEAN.
   **/
  public void Var037() {
    if (checkBooleanSupport()) {
      try {
        ResultSet rs = statement0_
            .executeQuery("SELECT * FROM " + JDRSTest.RSTEST_BOOLEAN);
        JDRSTest.position0(rs, "BOOLEAN_NULL");
        URL v = rs.getURL("C_BOOLEAN");
        assertCondition(v == null);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

}
