///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateSQLXML.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.RS;

import java.io.FileOutputStream;
/* import java.sql.DataTruncation; */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSerializeFile;
import test.JD.JDTestUtilities;

/**
 * Testcase JDRSUpdateSQLXML. This tests the following method of the JDBC
 * ResultSet class:
 * 
 * <ul>
 * <li>updateSQLXML()
 * </ul>
 **/
public class JDRSUpdateSQLXML extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length + 2];
    newArgs[0] = "-tc";
    newArgs[1] = "JDRSUpdateSQLXML";
    for (int i = 0; i < args.length; i++) {
      newArgs[2 + i] = args[i];
    }
    test.JDRSTest.main(newArgs);
  }

  // Private data.
  private static final String key_ = "JDRSUpdateSQLXML";
  private static String select_ = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;
  private static String selectXML_ = "SELECT * FROM " + JDRSTest.RSTEST_UPDATEXML;

  private Statement statementOther_;
  private Statement statement2Other_;
  private ResultSet rsOther_;
  private Statement statementXML_;
  private Statement statement2XML_;
  private ResultSet rsXML_;
  private Object sqlxml_;

  private boolean nodecl = false;

  /**
   * Constructor.
   **/
  public JDRSUpdateSQLXML(AS400 systemObject, Hashtable<String, Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream,

      String password) {
    super(systemObject, "JDRSUpdateSQLXML", namesAndVars, runMode, fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void setup() throws Exception {
    select_ = "SELECT * FROM " + JDRSTest.RSTEST_UPDATE;
    selectXML_ = "SELECT * FROM " + JDRSTest.RSTEST_UPDATEXML;

    nodecl = (isToolboxDriver()) || (true);

    if (isJdbc40()) {
      String url = baseURL_

          + ";date format=iso" + ";data truncation=true";
      connection_ = testDriver_.getConnection(url, systemObject_.getUserId(), encryptedPassword_);
      connection_.setAutoCommit(false); // @C1A

      statementOther_ = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      statement2Other_ = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

      statementOther_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('DUMMY_ROW')");
      statementOther_.executeUpdate("INSERT INTO " + JDRSTest.RSTEST_UPDATE + " (C_KEY) VALUES ('" + key_ + "')");

      rsOther_ = statementOther_.executeQuery(select_ + " FOR UPDATE");

      statementXML_ = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

      statement2XML_ = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

      if (true) {
        // table is only on 54+
        rsXML_ = statementXML_.executeQuery(selectXML_ + " FOR UPDATE");
      }

      sqlxml_ = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
      JDReflectionUtil.callMethod_V(sqlxml_, "setString", JDRSTest.SAMPLE_XML2);

    }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    if (isJdbc40()) {
      rsOther_.close();
      statementOther_.close();
      if (true) {
        // table is only on 54+
        rsXML_.close();
      }
      statementXML_.close();
      connection_.commit(); // @C1A
      connection_.close();
    }
  }

  /**
   * updateSQLXML() - Should throw exception when the result set is closed.
   **/
  public void Var001() {

    if (checkJdbc40()) {

      ResultSet rs = null;
      try {
        Statement s = null;
        if (isToolboxDriver()) {
          // per issue 36333, diff between zda/cli in scrollable/updatable cursors
          // Note: if we open up the rs as scrollable/updatable, then vars7,11,12,15,16
          // will fail
          // with [SQL0508] Cursor SCRSR0001 not positioned on locked row.
          s = connection_.createStatement();
          rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_UPDATEXML);
        } else {
          s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_UPDATEXML + " FOR UPDATE");
        }

        rs.next();
        rs.close();

        JDReflectionUtil.callMethod_V(rs, "updateSQLXML", "C_CLOB0037", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      } finally {
        try {
          if (rs != null)
            rs.close();
        } catch (Exception e) {
        }
      }
    }
  }

  /**
   * updateSQLXML() - Should throw exception when the result set is not updatable.
   **/
  public void Var002() {
    if (checkJdbc40()) {
      try {
        Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_UPDATEXML);
        rs.next();
        JDReflectionUtil.callMethod_V(rs, "updateSQLXML", "C_CLOB0037", sqlxml_);

        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Should throw exception when cursor is not pointing to a row.
   **/
  public void Var003() {
    if (checkJdbc40()) {
      try {
        rsXML_.beforeFirst();
        JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", "C_CLOB0037", sqlxml_);
        rsXML_.updateRow(); /* exception */
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Should throw an exception when the column is an invalid
   * index.
   **/
  public void Var004() {
    if (checkJdbc40()) {
      try {
        rsXML_.first();
        JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", 100, sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Should throw an exception when the column is 0.
   **/
  public void Var005() {
    if (checkJdbc40()) {
      try {
        rsXML_.first();
        JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", 0, sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Should throw an exception when the column is -1.
   **/
  public void Var006() {
    if (checkJdbc40()) {
      try {
        rsXML_.first();
        JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", -1, sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Should work when the column index is valid.
   **/
  public void Var007() {
    if (checkJdbc40()) {
      try {
        // rsXML_ = statementXML_.executeQuery (selectXML_ + " FOR UPDATE");
        rsXML_.first();
        Object sqlxml = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
        JDReflectionUtil.callMethod_V(sqlxml, "setString", JDRSTest.SAMPLE_XML2);

        JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", 2, sqlxml);
        rsXML_.updateRow();
        ResultSet rs2 = statement2XML_.executeQuery(selectXML_);
        rs2.first();
        String v = rs2.getString("C_CLOB0037");
        rs2.close();
        String expected = JDRSTest.SAMPLE_XML2;
        if (nodecl) {
          expected = JDRSTest.SAMPLE_XML2_NODECL;
        }
        assertCondition(v.equals(expected), "Expected " + expected + " got " + v);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateSQLXML() - Should throw an exception when the column name is null.
   **/
  public void Var008() {
    if (checkJdbc40()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC throws null pointer exception when column name is null ");
      } else {
        try {
          rsXML_.first();
          JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", null, sqlxml_);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * updateSQLXML() - Should throw an exception when the column name is an empty
   * string.
   **/
  public void Var009() {
    if (checkJdbc40()) {
      try {
        rsXML_.first();
        JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", "", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Should throw an exception when the column name is invalid.
   **/
  public void Var010() {
    if (checkJdbc40()) {
      try {
        rsXML_.first();
        JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", "INVALID", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Should work when the column name is valid.
   **/
  public void Var011() {
    if (checkJdbc40()) {
      try {
        rsXML_.first();
        JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", "C_CLOB0037", sqlxml_);
        rsXML_.updateRow();
        ResultSet rs2 = statement2XML_.executeQuery(selectXML_);
        rs2.next();
        String v = rs2.getString("C_CLOB0037");
        rs2.close();
        String expected = JDRSTest.SAMPLE_XML2;
        if (nodecl) {
          expected = JDRSTest.SAMPLE_XML2_NODECL;
        }
        assertCondition(v.equals(expected), "\ngot:\n " + v + " \nexpected:\n" + expected);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateSQLXML() - Should update to SQL NULL when the column value is null.
   **/
  public void Var012() {
    if (checkJdbc40()) {
      try {
        rsXML_.first();

        Class<?>[] argTypes = new Class<?>[2];
        argTypes[0] = Integer.TYPE;
        try {
          argTypes[1] = Class.forName("java.sql.SQLXML");
        } catch (Exception e) {
          argTypes[1] = Class.forName("com.ibm.db2.jdbc.app.SQLXML");
        }
        Object[] args = new Object[2];
        args[0] = Integer.valueOf(2);
        args[1] = null;

        JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", argTypes, args);

        rsXML_.updateRow();
        ResultSet rs2 = statement2XML_.executeQuery(selectXML_);
        rs2.first();
        String v = rs2.getString("C_CLOB0037");
        boolean wn = rs2.wasNull();
        rs2.close();
        assertCondition((v == null) && (wn == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateSQLXML() - Should throw an exception when the length is invalid.
   **/
  public void Var013() {
    notApplicable();
  }

  /**
   * updateSQLXML() - Should be reflected by get, even if update has not yet been
   * issued (i.e. update is still pending).
   **/
  public void Var014() {
    if (checkJdbc40()) {

      try {
        String expected = "<?xml version=\"1.0\" ?> <name>Var014</name>";
        if (nodecl) {
          expected = " <name>Var014</name>";
        }
        Object sqlxml = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
        JDReflectionUtil.callMethod_V(sqlxml, "setString", expected);

        rsXML_.first();
        JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", "C_CLOB0037", sqlxml);
        assertCondition(rsXML_.getString("C_CLOB0037").equals(expected));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateSQLXML() - Should be reflected by get, after update has been issued,
   * but cursor has not been repositioned.
   **/
  public void Var015() {
    if (checkJdbc40()) {

      try {
        String expected = "<?xml version=\"1.0\" ?> <name>Var015</name>";
        if (nodecl) {
          expected = " <name>Var015</name>";
        }

        Object sqlxml = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
        JDReflectionUtil.callMethod_V(sqlxml, "setString", expected);

        rsXML_.first();
        JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", "C_CLOB0037", sqlxml);
        rsXML_.updateRow();
        assertCondition(rsXML_.getString("C_CLOB0037").equals(expected));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateSQLXML() - Should be reflected by get, after update has been issued and
   * cursor has been repositioned.
   **/
  public void Var016() {
    if (checkJdbc40()) {
      try {
        String expected = "<?xml version=\"1.0\" ?> <name>Var016</name>";
        if (nodecl) {
          expected = " <name>Var016</name>";
        }
        Object sqlxml = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
        JDReflectionUtil.callMethod_V(sqlxml, "setString", expected);

        rsXML_.first();
        JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", "C_CLOB0037", sqlxml);
        rsXML_.updateRow();
        rsXML_.beforeFirst();
        rsXML_.first();
        String v = rsXML_.getString("C_CLOB0037");
        assertCondition(v.equals(expected), "v=" + v + " sb=" + expected);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateSQLXML() - Should work when the current row is the insert row.
   **/
  public void Var017() {
    if (checkJdbc40()) {
      try {
        String expected = "<?xml version=\"1.0\" ?> <name>Var017</name>";
        if (nodecl) {
          expected = " <name>Var017</name>";
        }

        Object sqlxml = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
        JDReflectionUtil.callMethod_V(sqlxml, "setString", expected);

        rsXML_.moveToInsertRow();
        rsXML_.updateInt("C_KEY", 170);
        JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", "C_CLOB0037", sqlxml);
        rsXML_.insertRow();
        rsXML_.beforeFirst();
        while (rsXML_.next() && rsXML_.getInt(1) != 170) {
        }
        assertCondition(rsXML_.getString("C_CLOB0037").equals(expected),
            "v=" + rsXML_.getString("C_CLOB0037") + " sb=" + expected);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateSQLXML() - Should be reflected by get on insert row, even if insert has
   * not yet been issued (i.e. insert is still pending).
   **/
  public void Var018() {

    if (checkJdbc40()) {

      try {
        String expected = "<?xml version=\"1.0\" ?> <name>Var018</name>";
        if (nodecl) {
          expected = " <name>Var018</name>";
        }
        Object sqlxml = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
        JDReflectionUtil.callMethod_V(sqlxml, "setString", expected);

        rsXML_.moveToInsertRow();
        JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", "C_CLOB0037", sqlxml);
        assertCondition(rsXML_.getString("C_CLOB0037").equals(expected),
            "v=" + rsXML_.getString("C_CLOB0037") + " sb=" + expected);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateSQLXML() - Should throw an exception on a deleted row.
   **/
  public void Var019() {
    if (checkJdbc40()) {
      try {
        rsXML_.last();
        rsXML_.deleteRow();
        JDReflectionUtil.callMethod_V(rsXML_, "updateSQLXML", "C_CLOB0037", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a SMALLINT.
   **/
  public void Var020() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_SMALLINT", sqlxml_);
        rsOther_.updateRow();

        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");

      }
    }
  }

  /**
   * updateSQLXML() - Update a SMALLINT, when the integer is too big.
   **/
  public void Var021() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_SMALLINT", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a SMALLINT, when the string is not a number.
   **/
  public void Var022() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_SMALLINT", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update an INTEGER.
   **/
  public void Var023() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_INTEGER", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update an INTEGER, when the integer is too big.
   **/
  public void Var024() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_INTEGER", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update an INTEGER, when the string is not a number.
   **/
  public void Var025() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_INTEGER", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a REAL.
   **/
  public void Var026() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_REAL", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a REAL, when the number is too big.
   * 
   * Note: Given the new data truncation rules for JDBC, this testcase is expected
   * to work without throwing a DataTruncation exception.
   **/
  public void Var027() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_REAL", sqlxml_);
        rsOther_.updateRow();

        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
    /*
     * failed ("Didn't throw SQLException"); } catch (Exception e) { DataTruncation
     * dt = (DataTruncation)e; assertCondition ((dt.getIndex() == expectedColumn) &&
     * (dt.getParameter() == false) && (dt.getRead() == false) && (dt.getDataSize()
     * == 15) && (dt.getTransferSize() == 10));
     * 
     * } }
     */
  }

  /**
   * updateSQLXML() - Update a REAL, when the string is not a number.
   **/
  public void Var028() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_INTEGER", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a FLOAT.
   **/
  public void Var029() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_FLOAT", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a FLOAT, when the number is too big.
   * 
   * Note: Given the new data truncation rules for JDBC, this testcase is expected
   * to work without throwing a DataTruncation exception.
   **/
  public void Var030() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_FLOAT", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
    /*
     * failed ("Didn't throw SQLException"); } catch (Exception e) { DataTruncation
     * dt = (DataTruncation)e; assertCondition ((dt.getIndex() == expectedColumn) &&
     * (dt.getParameter() == false) && (dt.getRead() == false) && (dt.getDataSize()
     * == 19) && (dt.getTransferSize() == 17));
     * 
     * } }
     */
  }

  /**
   * updateSQLXML() - Update a FLOAT, when the string is not a number.
   **/
  public void Var031() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_FLOAT", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a DOUBLE.
   **/
  public void Var032() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_DOUBLE", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a DOUBLE, when the number is too big.
   * 
   * Note: Given the new data truncation rules for JDBC, this testcase is expected
   * to work without throwing a DataTruncation exception.
   **/
  public void Var033() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_DOUBLE", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
    /*
     * failed ("Didn't throw SQLException"); } catch (Exception e) { DataTruncation
     * dt = (DataTruncation)e; assertCondition ((dt.getIndex() == expectedColumn) &&
     * (dt.getParameter() == false) && (dt.getRead() == false) && (dt.getDataSize()
     * == 19) && (dt.getTransferSize() == 17));
     * 
     * } }
     */
  }

  /**
   * updateSQLXML() - Update a DOUBLE, when the string is not a number.
   **/
  public void Var034() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_DOUBLE", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a DECIMAL.
   **/
  public void Var035() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_DECIMAL_105", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a DECIMAL, when the value is too big.
   **/
  public void Var036() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_DECIMAL_40", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a DECIMAL, when the string is not a number.
   **/
  public void Var037() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_DECIMAL_105", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a NUMERIC.
   **/
  public void Var038() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_NUMERIC_105", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a NUMERIC, when the value is too big.
   **/
  public void Var039() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_NUMERIC_40", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a NUMERIC, when the string is not a number.
   **/
  public void Var040() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_NUMERIC_105", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a CHAR.
   **/
  public void Var041() {

    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_CHAR_50", sqlxml_);
        rsOther_.updateRow();
        if (isToolboxDriver()) {
          succeeded();
          return;
        }
        failed("Didn't throw SQLException when updating a char field ");
      } catch (Exception e) {
        if (isToolboxDriver()) {
          failed("toolbox should be able to update char");
          return;
        }
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a CHAR, when the value is too big.
   **/
  public void Var042() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_CHAR_1", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a VARCHAR, with the length equal to the full stream.
   **/
  public void Var043() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_CLOB0037", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a VARCHAR, with the length less than the full stream.
   * 
   * @D2 - Native driver doesn't throw an exception if stream contains extra
   *     characters then there needs to be there.
   **/
  public void Var044() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_CLOB0037", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a VARCHAR, with the length greater than the full
   * stream.
   **/
  public void Var045() {
    notApplicable();
  }

  /**
   * updateSQLXML() - Update a VARCHAR, with the length set to 1.
   * 
   * @D2 - Native driver doesn't throw an exception if stream contains extra
   *     characters then there needs to be there.
   **/
  public void Var046() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_CLOB0037", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a VARCHAR, with the length set to 0.
   **/
  public void Var047() {
    notApplicable();
  }

  /**
   * updateSQLXML() - Update a VARCHAR, with an empty string.
   **/
  public void Var048() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_CLOB0037", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a VARCHAR, when the value is too big.
   **/
  public void Var049() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_CLOB0037", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a VARCHAR parameter to a bad reader.
   **/
  public void Var050() {
    notApplicable();
  }

  /**
   * updateSQLXML() - Update a BINARY.
   **/
  public void Var051() {
    if (checkJdbc40()) {
      try {

        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_BINARY_20", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a BINARY, when the value is too big.
   **/
  public void Var052() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_BINARY_1", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");

      }
    }
  }

  /**
   * updateSQLXML() - Update a VARBINARY.
   **/
  public void Var053() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_VARBINARY_20", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a VARBINARY, when the value is too big.
   **/
  public void Var054() {
    if (checkJdbc40()) {
      try {

        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_VARBINARY_20", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");

      }
    }
  }

  /**
   * updateSQLXML() - Update a CLOB.
   * 
   * SQL400 - the native driver expects this update to work correctly.
   **/
  public void Var055() {
    if (checkJdbc40()) {
      JDSerializeFile serializeFile = null;
      try {
        serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_UPDATE);
        String expected = "<?xml version=\"1.0\" ?> <name>Var055</name>";
        if (nodecl) {
          expected = " <name>Var055</name>";
        }

        Object sqlxml = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
        JDReflectionUtil.callMethod_V(sqlxml, "setString", expected);

        JDRSTest.position(rsOther_, key_);

        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_CLOB", sqlxml);
        rsOther_.updateRow(); /* serialized */ 
        ResultSet rs2 = statement2Other_.executeQuery(select_);
        JDRSTest.position(rs2, key_);
        String v = rs2.getString("C_CLOB");
        rs2.close();
        assertCondition(expected.equals(v), "v=" + v + " sb=" + expected);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (serializeFile != null) {
          try {
            serializeFile.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * updateSQLXML() - Update a DBCLOB.
   * 
   * SQL400 - the native driver expects this update to work correctly.
   **/
  public void Var056() {
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          String expected = "<?xml version=\"1.0\" ?> <name>Var056</name>";
          if (nodecl) {
            expected = " <name>Var056</name>";
          }
          Object sqlxml = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
          JDReflectionUtil.callMethod_V(sqlxml, "setString", expected);

          JDRSTest.position(rsOther_, key_);
          JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_DBCLOB", sqlxml);

          rsOther_.updateRow();
          ResultSet rs2 = statement2Other_.executeQuery(select_);
          JDRSTest.position(rs2, key_);
          String v = rs2.getString("C_DBCLOB");
          rs2.close();
          assertCondition(v.equals(expected));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * updateSQLXML() - Update a BLOB.
   **/
  public void Var057() {
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          JDRSTest.position(rsOther_, key_);
          JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_BLOB", sqlxml_);
          rsOther_.updateRow();
          ResultSet rs2 = statement2Other_.executeQuery(select_);
          JDRSTest.position(rs2, key_);
          Object outSqlxml = JDReflectionUtil.callMethod_OS(rs2, "getSQLXML", "C_BLOB");
          String v = JDReflectionUtil.callMethod_S(outSqlxml, "getString");
          rs2.close();
          // trim decl since accessing blob via SQLXML.getString()
          assertCondition(v.equals(JDTestUtilities.stripXmlDeclaration(JDRSTest.SAMPLE_XML2)),
              "Got " + v + " expected " + JDTestUtilities.stripXmlDeclaration(JDRSTest.SAMPLE_XML2));
        } catch (Exception e) {
          if (isToolboxDriver()) {
            assertCondition(e.getMessage().startsWith("Data type mismatch"), "xml from non-char not supported.");
            return;
          }
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * updateSQLXML() - Update a DATE.
   **/
  public void Var058() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_DATE", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a DATE, when the string is not a valid date.
   **/
  public void Var059() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_DATE", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a TIME.
   **/
  public void Var060() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_TIME", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a TIME, when the string is not a valid time.
   **/
  public void Var061() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_TIME", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a TIMESTAMP.
   **/
  public void Var062() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_TIMESTAMP", sqlxml_);
        rsOther_.updateRow();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a TIMESTAMP, when the string is not a valid
   * timestamp.
   **/
  public void Var063() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_TIMESTAMP", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a DATALINK.
   **/
  public void Var064() {
    // We do not test updating datalinks, since it is not
    // possible to open a updatable cursor/result set with
    // a datalink column.
    notApplicable("DATALINK update not supported.");
  }

  /**
   * updateSQLXML() - Update a DISTINCT.
   **/
  public void Var065() {
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          JDRSTest.position(rsOther_, key_);
          JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_DISTINCT", sqlxml_);
          rsOther_.updateRow();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * updateSQLXML() - Update a BIGINT.
   **/
  public void Var066() {
    if (checkJdbc40()) {
      if (checkBigintSupport()) {
        try {
          JDRSTest.position(rsOther_, key_);
          JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_BIGINT", sqlxml_);
          rsOther_.updateRow();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * updateSQLXML() - Update a BIGINT, when the integer is too big.
   **/
  public void Var067() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_BIGINT", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a BIGINTEGER, when the string is not a number.
   **/
  public void Var068() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_BIGINT", sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * updateSQLXML() - Update a BLOB.
   **/
  public void Var069() {
    if (checkJdbc40()) {
      try {
        JDRSTest.position(rsOther_, key_);
        JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_BLOB", sqlxml_);
        succeeded();
      } catch (Exception e) {
        if (isToolboxDriver()) {
          assertCondition(e.getMessage().startsWith("Data type mismatch"), "xml from non-char not supported.");
          return;
        }
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * updateSQLXML() - Update a DFP16.
   **/
  public void Var070() {
    if (checkJdbc40()) {
      if (checkDecFloatSupport()) {
        try (Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP16 + " FOR UPDATE ")) {
          rs.next();
          JDReflectionUtil.callMethod_V(rs, "updateSQLXML", 1, sqlxml_);
          rs.updateRow(); /* exception thrown */

          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * updateSQLXML() - Update a DFP16, when the value is too big.
   **/
  public void Var071() {
    if (checkJdbc40()) {
      if (checkDecFloatSupport()) {
        try {
          Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP16 + " FOR UPDATE ");
          rs.next();
          JDReflectionUtil.callMethod_V(rs, "updateSQLXML", 1, sqlxml_);
          rs.updateRow(); /* exception thrown */
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * updateSQLXML() - Update a DFP16, when the string is not a number.
   **/
  public void Var072() {
    if (checkJdbc40()) {
      if (checkDecFloatSupport()) {
        try {
          Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP16 + " FOR UPDATE ");
          rs.next();
          JDReflectionUtil.callMethod_V(rs, "updateSQLXML", 1, sqlxml_);
          rs.updateRow(); /* exception thrown */

          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * updateSQLXML() - Update a DFP34.
   **/
  public void Var073() {
    if (checkJdbc40()) {
      if (checkDecFloatSupport()) {
        try {
          Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP34 + " FOR UPDATE ");
          rs.next();
          JDReflectionUtil.callMethod_V(rs, "updateSQLXML", 1, sqlxml_);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * updateSQLXML() - Update a DFP34, when the value is too big.
   **/
  public void Var074() {
    if (checkJdbc40()) {
      if (checkDecFloatSupport()) {

        try {
          Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP34 + " FOR UPDATE ");
          rs.next();
          JDReflectionUtil.callMethod_V(rs, "updateSQLXML", 1, sqlxml_);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * updateSQLXML() - Update a DFP34, when the string is not a number.
   **/
  public void Var075() {
    if (checkJdbc40()) {
      if (checkDecFloatSupport()) {
        JDSerializeFile serializeFile = null;
        try {
          serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_DFP34);
          Statement s = connection_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rs = s.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_DFP34 + " FOR UPDATE ");
          rs.next();
          JDReflectionUtil.callMethod_V(rs, "updateSQLXML", 1, sqlxml_);
          rs.updateRow(); /* serialized */
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        } finally {
          if (serializeFile != null) {
            try {
              serializeFile.close();
            } catch (SQLException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
  }

  /**
   * updateSQLXML() - Update a BOOLEAN.
   **/
  public void Var076() {
    if (checkJdbc40()) {
      if (checkBooleanSupport()) {
        try {
          JDRSTest.position(rsOther_, key_);
          JDReflectionUtil.callMethod_V(rsOther_, "updateSQLXML", "C_BOOLEAN", sqlxml_);
          rsOther_.updateRow();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

}
