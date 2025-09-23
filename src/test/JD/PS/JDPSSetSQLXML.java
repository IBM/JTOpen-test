///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetSQLXML.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.PS;

import com.ibm.as400.access.AS400;

import test.JDPSTest;
import test.JDRSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.XML.JDXMLErrorListener;

import java.io.*;
import java.sql.Connection;
import java.sql.DataTruncation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

/**
 * Testcase JDPSSetSQLXML. This tests the following method of the JDBC
 * PreparedStatement class:
 * 
 * <ul>
 * <li>setSQLXML()
 * </ul>
 **/
public class JDPSSetSQLXML extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetSQLXML";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }

  // Private data.
  private Connection connection_;
  private Statement statement_;

  private Object sqlxml_;
  private StringBuffer messageBuffer = new StringBuffer();

  private JDXMLErrorListener errorListener = new JDXMLErrorListener();
  private boolean nodecl = false;

  /**
   * Constructor.
   **/
  public JDPSSetSQLXML(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDPSSetSQLXML", namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    if (isJdbc40()) {
      nodecl = (true);

      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        String url = baseURL_;
        connection_ = testDriver_.getConnection(url, userId_, encryptedPassword_);

      } else {

        String url = baseURL_  + ";lob threshold=30000"
            + ";data truncation=true";
        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
      }
      statement_ = connection_.createStatement();
      sqlxml_ = JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
      JDReflectionUtil.callMethod_V(sqlxml_, "setString", JDRSTest.SAMPLE_XML2);

    }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    if (isJdbc40()) {

      statement_.close();
      connection_.close();
    }
  }

  /**
   * Compares a SQLXML with a String.
   * 
   * @throws Exception
   **/
  private boolean compare(Object sqlxml, String b) throws Exception {
    boolean answer = false;
    messageBuffer.setLength(0);
    if (sqlxml == null) {
      if (b == null) {
        answer = true;
      } else {
        messageBuffer.append("\ngot : null \nexpected:\n'" + b + "'\n");
      }
    } else {
      String s;
      if (sqlxml instanceof String) {
        s = (String) sqlxml;
      } else {
        s = (String) JDReflectionUtil.callMethod_O(sqlxml, "getString");
      }
      answer = s.equals(b);
      if (!answer) {
        messageBuffer.append("\ngot:\n'" + s + "'\nexpected:\n'" + b + "'\n");
      }
    }
    return answer;
  }

  /**
   * setSQLXML() - Should throw exception when the prepared statement is closed.
   **/
  public void Var001() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CLOB) VALUES (?)");
        ps.close();
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Should throw exception when an invalid index is specified.
   **/
  public void Var002() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_
            .prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?, ?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 100, sqlxml_);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Should throw exception when index is 0.
   **/
  public void Var003() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_
            .prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?, ?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 0, sqlxml_);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Should throw exception when index is -1.
   **/
  public void Var004() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_
            .prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT) VALUES (?, ?, ?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", -1, sqlxml_);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Should work when value is null.
   **/
  public void Var005() {
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CLOB) VALUES (?)");

        Class<?>[] argTypes = new Class[2];
        argTypes[0] = Integer.TYPE;
        try {
          argTypes[1] = Class.forName("java.sql.SQLXML");
        } catch (Exception e) {
          argTypes[1] = Class.forName("com.ibm.db2.jdbc.app.SQLXML");
        }
        Object[] args = new Object[2];
        args[0] = Integer.valueOf(1);
        args[1] = null;

        JDReflectionUtil.callMethod_V(ps, "setSQLXML", argTypes, args);

        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        Object check = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 1);
        boolean wn = rs.wasNull();
        rs.close();

        assertCondition((check == null) && (wn == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setSQLXML() - Should work with a valid parameter index greater than 1.
   **/
  public void Var006() {
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_KEY, C_CLOB) VALUES (?, ?)");

        String expected = "<?xml version=\"1.0\" ?> <name>Var006</name>";
        if (nodecl) {
          /* native V6r1 and V7R1 */
          expected = " <name>Var006</name>";
        }
        Object sqlxml = JDReflectionUtil.callMethod_O(connection_,
            "createSQLXML");
        JDReflectionUtil.callMethod_V(sqlxml, "setString", expected);

        ps.setString(1, "Muchas");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 2, sqlxml);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_CLOB FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        String check = rs.getString(1);
        rs.close();

        assertCondition(compare(check, expected),
            "\ngot     :\n" + check + "\nexpected:\n" + expected);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setSQLXML() - Should throw exception when the parameter is not an input
   * parameter.
   **/
  public void Var007() {
    if (checkJdbc40()) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC permits setting of an output parameter");
        return;
      }
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 2, sqlxml_);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Verify that a data truncation warning is posted when data is
   * truncated.
   **/
  public void Var008() {
    if (isToolboxDriver()) {
      notApplicable("Toolbox does not currently check this");
      return;
    }
    String expected = "";
    String xmlHeader = "";
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CLOB) VALUES (?)");

        StringBuffer sb = new StringBuffer();
        xmlHeader = "<?xml version=\"1.0\" ?>";
        sb.append(xmlHeader);
        sb.append(" <name>");
        for (int i = 0; i < 20000; i++) {
          sb.append(".");
          sb.append("" + i);
        }
        sb.append("</name>");
        expected = sb.toString();
        Object sqlxml = JDReflectionUtil.callMethod_O(connection_,
            "createSQLXML");
        JDReflectionUtil.callMethod_V(sqlxml, "setString", expected);

        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw truncation Exception ");
      } catch (DataTruncation dt) {
        int expectedDataSize = expected.length() - xmlHeader.length();
        assertCondition((dt.getIndex() == 1) && (dt.getParameter() == true)
            && (dt.getRead() == false) && (dt.getDataSize() == expectedDataSize)
            && (dt.getTransferSize() == 200),
            "dt.getIndex = " + dt.getIndex() + " sb 1\n" + "dt.getParameter = "
                + dt.getParameter() + " sb true\n" + "dt.getRead = "
                + dt.getRead() + " sb false\n" + "dt.getDataSize = "
                + dt.getDataSize() + " sb " + expectedDataSize
                + "dt.getTransferSize() =" + dt.getTransferSize() + " sb 200"
                + " expected.length=" + expected.length() + " xmlHeader.length="
                + xmlHeader.length());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void testSetFailed(String columnName, Object sqlxml) {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (" + columnName + ") VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * setSQLXML() - Set a SMALLINT parameter.
   **/
  public void Var009() {
    testSetFailed("C_SMALLINT", sqlxml_);
  }

  /**
   * setSQLXML() - Set a INTEGER parameter.
   **/
  public void Var010() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Set a REAL parameter.
   **/
  public void Var011() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_REAL) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Set a FLOAT parameter.
   **/
  public void Var012() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_FLOAT) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Set a DOUBLE parameter.
   **/
  public void Var013() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DOUBLE) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Set a DECIMAL parameter.
   **/
  public void Var014() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_DECIMAL_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Set a NUMERIC parameter.
   **/
  public void Var015() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_NUMERIC_50) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Set a CHAR(1) parameter, where the string is longer.
   **/
  public void Var016() {
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_1) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
        ps.executeUpdate();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Set a CHAR(50) parameter.
   **/
  public void Var017() {
    if (isToolboxDriver()) {
      notApplicable("Toolbox does not currently check this");
      return;
    }
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_50) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
        ps.executeUpdate();
        failed("Didn't throw SQLException for setting CHAR(50)");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Set a VARCHAR(50) parameter.
   **/
  public void Var018() {
    if (isToolboxDriver()) {
      notApplicable("Toolbox does not currently check this");
      return;
    }
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_VARCHAR_50) VALUES (?)");

        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
        ps.executeUpdate();
        failed("Didn't throw SQLException for setting VARCHAR(50)");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Set a CLOB parameter, when the data is passed directly.
   **/
  public void Var019() {
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CLOB) VALUES (?)");
          String expected = "<?xml version=\"1.0\" ?> <name>Var019</name>";
          if (nodecl) {
            expected = " <name>Var019</name>";
          }

          Object sqlxml = JDReflectionUtil.callMethod_O(connection_,
              "createSQLXML");
          JDReflectionUtil.callMethod_V(sqlxml, "setString", expected);
          JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_
              .executeQuery("SELECT C_CLOB FROM " + JDPSTest.PSTEST_SET);
          rs.next();
          String check = rs.getString(1);
          rs.close();

          assertCondition(compare(check, expected), messageBuffer.toString());
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setSQLXML() - Set a CLOB parameter, when the data is passed in a locator.
   **/
  public void Var020() {
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

          String url = baseURL_ +  ";lob threshold=1";
          Connection c = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
          PreparedStatement ps = c.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CLOB) VALUES (?)");

          String expected = "<?xml version=\"1.0\" ?> <name>Var020</name>";
          if (nodecl) {
            expected = " <name>Var020</name>";
          }

          Object sqlxml = JDReflectionUtil.callMethod_O(connection_,
              "createSQLXML");
          JDReflectionUtil.callMethod_V(sqlxml, "setString", expected);

          JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml);

          ps.executeUpdate();
          ps.close();
          c.close();

          ResultSet rs = statement_
              .executeQuery("SELECT C_CLOB FROM " + JDPSTest.PSTEST_SET);
          rs.next();
          String check = rs.getString(1);

          rs.close();

          assertCondition(compare(check, expected), messageBuffer.toString());

        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setSQLXML() - Set a DBCLOB parameter, when the data is passed directly.
   **/
  public void Var021() {
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DBCLOB) VALUES (?)");

        String expected = "<?XML VERSION=\"1.0\" ?> <NAME>VAR021</NAME>";
        if (nodecl) {
          expected = " <NAME>VAR021</NAME>";
        }

        Object sqlxml = JDReflectionUtil.callMethod_O(connection_,
            "createSQLXML");
        JDReflectionUtil.callMethod_V(sqlxml, "setString", expected);
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery(
            "SELECT C_DBCLOB, length(C_DBCLOB) FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        int length = rs.getInt(2);
        String check = rs.getString(1);

        rs.close();

        assertCondition(compare(check, expected),
            messageBuffer.toString() + " DBCLOB length = " + length
                + " expected length=" + expected.length());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setSQLXML() - Set a DBCLOB parameter, when the data is passed as a locator.
   **/
  public void Var022() {
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        String url = baseURL_ +  ";lob threshold=1";
        Connection c = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        PreparedStatement ps = c.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DBCLOB) VALUES (?)");

        String expected = "<?XML VERSION=\"1.0\" ?> <NAME>VAR022</NAME>";
        if (nodecl) {
          expected = " <NAME>VAR022</NAME>";
        }

        Object sqlxml = JDReflectionUtil.callMethod_O(connection_,
            "createSQLXML");
        JDReflectionUtil.callMethod_V(sqlxml, "setString", expected);

        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml);
        ps.executeUpdate();
        ps.close();
        c.close();

        ResultSet rs = statement_.executeQuery(
            "SELECT C_DBCLOB, length(C_DBCLOB) FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        int length = rs.getInt(2);
        Object check = rs.getString(1);
        rs.close();
        boolean answer = compare(check, expected);
        assertCondition(answer, messageBuffer.toString() + " DBCLOB length = "
            + length + " expected length=" + expected.length());
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setSQLXML() - Set a BINARY parameter.
   **/
  public void Var023() {
    if (checkJdbc40()) {
      try {

        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BINARY_20) VALUES (?)");

        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Set a VARBINARY parameter.
   **/
  public void Var024() {
    if (checkJdbc40()) {
      try {

        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");

        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
        ps.executeUpdate();
        ps.close();

        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Set a BLOB parameter.
   **/
  public void Var025() {
    if (isToolboxDriver()) {
      notApplicable("Toolbox does not currently check this");
      return;
    }
    messageBuffer.setLength(0);
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BLOB) VALUES (?)");

        String expected = "<?xml version=\"1.0\" ?> <name>Var025</name>";
        if (true) {
          /*
           * In V7R1 the getString method of SQLXML will remove the XML
           * declaration
           */
          expected = " <name>Var025</name>";
        }
        Object sqlxml = JDReflectionUtil.callMethod_O(connection_,
            "createSQLXML");
        JDReflectionUtil.callMethod_V(sqlxml, "setString", expected);
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_BLOB FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        Object check = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 1);
        byte[] bytes = rs.getBytes(1);
        String x = bytesToString(bytes);
        messageBuffer.append(" Retrieved bytes are ");
        messageBuffer.append(x);
        String byteInfo = messageBuffer.toString();
        boolean answer = compare(check, expected);
        rs.close();
        assertCondition(answer,
            messageBuffer.toString() + " Bytes=" + byteInfo);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + messageBuffer);
      }
    }
  }

  /**
   * setSQLXML() - Set a DATE parameter.
   **/
  public void Var026() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DATE) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Set a TIME parameter.
   **/
  public void Var027() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIME) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Set a TIMESTAMP parameter.
   **/
  public void Var028() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIMESTAMP) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setSQLXML() - Set a DATALINK parameter.
   **/
  public void Var029() {
    if ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getJdbcLevel() <= 2)) {
      notApplicable("Native driver pre-JDBC 3.0");
      return;
    }
    if (checkJdbc40()) {
      if (checkDatalinkSupport()) {
        try {
          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + JDPSTest.PSTEST_SET + " (C_DATALINK) VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
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
   * setSQLXML() - Set a DISTINCT parameter.
   * 
   * SQL400 - Native JDBC has built the setSQLXML support on top of the
   * setCharacterStream. setCharacterStream is built on top of setString. With
   * setString, this call works. I could have put a call into setSQLXML to check
   * the data type and cause this path to fail, but I have been so mean to that
   * block of code lately that I decided that I would just leave it working and
   * change this testcase to expect it to work for now. Sorry to anyone that
   * this annoys. :)
   **/
  public void Var030() {
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + JDPSTest.PSTEST_SET + " (C_DISTINCT) VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
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
   * setSQLXML() - Set a BIGINT parameter.
   **/
  public void Var031() {
    if (checkJdbc40()) {
      if (checkBigintSupport()) {
        try {
          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BIGINT) VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
          ps.executeUpdate();
          ps.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  public void Var032() {
    notApplicable();
  }

  public void Var033() {
    notApplicable();
  }

  /**
   * setSQLXML() - Set a DECFLOAT parameter.
   **/
  public void Var034() {
    if (checkJdbc40()) {
      if (checkDecFloatSupport()) {
        try {
          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SETDFP16 + "  VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
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
   * setSQLXML() - Set a DECFLOAT parameter.
   **/
  public void Var035() {
    if (checkJdbc40()) {
      if (checkDecFloatSupport()) {
        try {
          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SETDFP34 + "  VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml_);
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
   * Test variations used by the xml tests
   */
  public void testSetSQLXMLString(String tableName, String var) {
    String added = " -- added by native driver 08/21/2009";
    if (checkJdbc40()) {
      if (checkXmlSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + tableName);

          String url = baseURL_ ;
          Connection c = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
          PreparedStatement ps = c
              .prepareStatement("INSERT INTO " + tableName + "  VALUES (?)");

          String input = "<?xml version=\"1.0\" ?> <NAME>" + var + "</NAME>";
          String expected = "<NAME>" + var + "</NAME>";

          Object sqlxml = JDReflectionUtil.callMethod_O(connection_,
              "createSQLXML");
          JDReflectionUtil.callMethod_V(sqlxml, "setString", input);

          JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml);
          ps.executeUpdate();
          ps.close();
          c.close();

          ResultSet rs = statement_.executeQuery("SELECT * from " + tableName);
          rs.next();
          Object check = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 1);
          boolean answer = compare(check, expected);
          rs.close();
          assertCondition(answer, messageBuffer.toString() + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception" + added);
        }
      }
    }

  }

  public void testSetSQLXMLBinaryStream(String tableName, String var) {
    String added = " -- added by native driver 08/21/2009";
    if (checkJdbc40()) {
      if (checkXmlSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + tableName);

          String url = baseURL_ ;
          Connection c = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
          PreparedStatement ps = c
              .prepareStatement("INSERT INTO " + tableName + "  VALUES (?)");

          String input = "<?xml version=\"1.0\" ?> <NAME>" + var + "</NAME>";
          String expected = "<NAME>" + var + "</NAME>";

          Object sqlxml = JDReflectionUtil.callMethod_O(connection_,
              "createSQLXML");
          OutputStream binaryStream = (OutputStream) JDReflectionUtil
              .callMethod_O(sqlxml, "setBinaryStream");
          binaryStream.write(input.getBytes("UTF-8"));
          binaryStream.close();
          JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml);
          ps.executeUpdate();
          ps.close();
          c.close();

          ResultSet rs = statement_.executeQuery("SELECT * from " + tableName);
          rs.next();
          Object check = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 1);
          boolean answer = compare(check, expected);
          rs.close();
          assertCondition(answer, messageBuffer.toString() + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception" + added);
        }
      }
    }

  }

  public void testSetSQLXMLCharacterStream(String tableName, String var) {
    String added = " -- added by native driver 08/21/2009";
    if (checkJdbc40()) {
      if (checkXmlSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + tableName);

          String url = baseURL_ ;
          Connection c = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
          PreparedStatement ps = c
              .prepareStatement("INSERT INTO " + tableName + "  VALUES (?)");

          String input = "<?xml version=\"1.0\" ?> <NAME>" + var + "</NAME>";
          String expected = "<NAME>" + var + "</NAME>";

          Object sqlxml = JDReflectionUtil.callMethod_O(connection_,
              "createSQLXML");
          Writer writer = (Writer) JDReflectionUtil.callMethod_O(sqlxml,
              "setCharacterStream");
          writer.write(input);
          writer.close();
          JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml);
          ps.executeUpdate();
          ps.close();
          c.close();

          ResultSet rs = statement_.executeQuery("SELECT * from " + tableName);
          rs.next();
          Object check = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 1);
          boolean answer = compare(check, expected);
          rs.close();
          assertCondition(answer, messageBuffer.toString() + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception" + added);
        }
      }
    }

  }

  public void testSetSQLXMLStAXSource(String tableName, String var) {

    String added = " -- added by native driver 08/21/2009";

    if (checkJdbc40()) {
      if (checkXmlSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + tableName);

          String url = baseURL_ ;
          Connection c = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
          PreparedStatement ps = c
              .prepareStatement("INSERT INTO " + tableName + "  VALUES (?)");

          String input = "<?xml version=\"1.0\" ?> <NAME>" + var + "</NAME>";
          String expected = "<NAME>" + var + "</NAME>";

          Object sqlxml = JDReflectionUtil.callMethod_O(connection_,
              "createSQLXML");
          Result result = (Result) JDReflectionUtil.callMethod_O(sqlxml,
              "setResult",
              Class.forName("javax.xml.transform.stax.StAXResult"));

          XMLInputFactory inputFactory = XMLInputFactory.newInstance(); 
          XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(
              new StringReader(input));

          Source source = (Source) JDReflectionUtil.createObject(
              "javax.xml.transform.stax.StAXSource",
              "javax.xml.stream.XMLStreamReader", xmlStreamReader);

          TransformerFactory factory = TransformerFactory.newInstance();
          Transformer transformer = factory.newTransformer();
          transformer.setErrorListener(errorListener);
          transformer.transform(source, result);

          JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml);
          ps.executeUpdate();
          ps.close();
          c.close();

          ResultSet rs = statement_.executeQuery("SELECT * from " + tableName);
          rs.next();
          Object check = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 1);
          boolean answer = compare(check, expected);
          rs.close();
          assertCondition(answer, messageBuffer.toString() + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception" + added);
        }
      }
    }

  }

  public void testSetSQLXMLSAXSource(String tableName, String var) {

    if (isToolboxDriver()) {
      notApplicable("Toolbox does not currently check sax result");
      return;
    }

    if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
      notApplicable("Sax source not implemented in V7R2");
      return;
    }
    String added = " -- Testing SQLXMLSAXSource -- added by native driver 08/21/2009";
    if (checkJdbc40()) {
      if (checkXmlSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + tableName);

          String url = baseURL_ ;
          Connection c = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
          PreparedStatement ps = c
              .prepareStatement("INSERT INTO " + tableName + "  VALUES (?)");

          String input = "<?xml version=\"1.0\" ?> <NAME>" + var + "</NAME>";
          String expected = "<NAME>" + var + "</NAME>";

          Object sqlxml = JDReflectionUtil.callMethod_O(connection_,
              "createSQLXML");
          Result result = (Result) JDReflectionUtil.callMethod_O(sqlxml,
              "setResult", Class.forName("javax.xml.transform.sax.SAXResult"));

          XMLInputFactory inputFactory = XMLInputFactory.newInstance(); 
          XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(
              new StringReader(input));

          Source source = (Source) JDReflectionUtil.createObject(
              "javax.xml.transform.sax.SAXSource",
              "javax.xml.stream.XMLStreamReader", xmlStreamReader);

          TransformerFactory factory = TransformerFactory.newInstance();
          Transformer transformer = factory.newTransformer();
          transformer.setErrorListener(errorListener);
          transformer.transform(source, result);

          JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml);
          ps.executeUpdate();
          ps.close();
          c.close();

          ResultSet rs = statement_.executeQuery("SELECT * from " + tableName);
          rs.next();
          Object check = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 1);
          boolean answer = compare(check, expected);
          rs.close();
          assertCondition(answer, messageBuffer.toString() + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception" + added);
        }
      }
    }

  }

  public void testSetSQLXMLStreamSource(String tableName, String var) {

    String added = " -- added by native driver 08/21/2009";
    if (checkJdbc40()) {
      if (checkXmlSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + tableName);

          String url = baseURL_ ;
          Connection c = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
          PreparedStatement ps = c
              .prepareStatement("INSERT INTO " + tableName + "  VALUES (?)");

          String input = "<?xml version=\"1.0\" ?> <NAME>" + var + "</NAME>";
          String expected = "<NAME>" + var + "</NAME>";

          Object sqlxml = JDReflectionUtil.callMethod_O(connection_,
              "createSQLXML");
          Result result = (Result) JDReflectionUtil.callMethod_O(sqlxml,
              "setResult",
              Class.forName("javax.xml.transform.stream.StreamResult"));

          Source source = (Source) JDReflectionUtil.createObject(
              "javax.xml.transform.stream.StreamSource", "java.io.Reader",
              new StringReader(input));

          TransformerFactory factory = TransformerFactory.newInstance();
          Transformer transformer = factory.newTransformer();
          transformer.setErrorListener(errorListener);
          transformer.transform(source, result);

          JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml);
          ps.executeUpdate();
          ps.close();
          c.close();

          ResultSet rs = statement_.executeQuery("SELECT * from " + tableName);
          rs.next();
          Object check = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 1);
          boolean answer = compare(check, expected);
          rs.close();
          assertCondition(answer, messageBuffer.toString() + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception" + added);
          if (isToolboxDriver()) {
            System.out.println(
                "NOTE:  Toolbox TC only works on windows non-ibm java for now.  J9 (on i and win) seems to include length of xml decl in xml length, but not in xml offset for xml streamSource.");

          }
        }
      }
    }

  }

  public void testSetSQLXMLDOMSource(String tableName, String var) {
    String added = " -- added by native driver 08/21/2009";

    if (checkJdbc40()) {
      if (checkXmlSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + tableName);

          String url = baseURL_ ;
          Connection c = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
          PreparedStatement ps = c
              .prepareStatement("INSERT INTO " + tableName + "  VALUES (?)");

          String input = "<?xml version=\"1.0\" ?> <NAME>" + var + "</NAME>";
          String expected = "<NAME>" + var + "</NAME>";

          Object sqlxml = JDReflectionUtil.callMethod_O(connection_,
              "createSQLXML");
          Result result = (Result) JDReflectionUtil.callMethod_O(sqlxml,
              "setResult", Class.forName("javax.xml.transform.dom.DOMResult"));

          DocumentBuilderFactory dbfactory = DocumentBuilderFactory
              .newInstance();
          DocumentBuilder parser = dbfactory.newDocumentBuilder();
          org.w3c.dom.Document doc = parser
              .parse(new ByteArrayInputStream(input.getBytes()));

          Source source = (Source) JDReflectionUtil.createObject(
              "javax.xml.transform.dom.DOMSource", "org.w3c.dom.Node", doc);

          TransformerFactory factory = TransformerFactory.newInstance();
          Transformer transformer = factory.newTransformer();
          transformer.setErrorListener(errorListener);
          transformer.transform(source, result);

          JDReflectionUtil.callMethod_V(ps, "setSQLXML", 1, sqlxml);
          ps.executeUpdate();
          ps.close();
          c.close();

          ResultSet rs = statement_.executeQuery("SELECT * from " + tableName);
          rs.next();
          Object check = JDReflectionUtil.callMethod_O(rs, "getSQLXML", 1);
          boolean answer = compare(check, expected);
          rs.close();
          assertCondition(answer, messageBuffer.toString() + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception" + added);
        }
      }
    }

  }

  /**
   * setSQLXML() - Set an XML parameter default ccsid using a SQLXML set by a
   * string
   **/
  public void Var036() {
    testSetSQLXMLString(JDPSTest.PSTEST_SETXML, "VAR036");
  }

  /**
   * setSQLXML() - Set an XML parameter using a SQLXML set by setBinaryStream
   **/
  public void Var037() {
    testSetSQLXMLBinaryStream(JDPSTest.PSTEST_SETXML, "VAR037");
  }

  /**
   * setSQLXML() - Set an XML parameter using a SQLXML set by setCharacterStream
   **/
  public void Var038() {
    testSetSQLXMLCharacterStream(JDPSTest.PSTEST_SETXML, "VAR038");
  }

  /**
   * setSQLXML() - Set an XML parameter using a SQLXML set by setResult --
   * StAXSource
   **/

  public void Var039() {
    testSetSQLXMLStAXSource(JDPSTest.PSTEST_SETXML, "VAR039");
  }

  /**
   * setSQLXML() - Set an XML parameter using a SQLXML set by setResult --
   * SAXSource
   **/

  public void Var040() {
    testSetSQLXMLSAXSource(JDPSTest.PSTEST_SETXML, "VAR040");
  }

  /**
   * setSQLXML() - Set an XML parameter using a SQLXML set by setResult --
   * StreamSource
   **/

  public void Var041() {
    if (isToolboxDriver()) {
      notApplicable(
          "NOTE:  Toolbox TC only works on windows non-ibm java for now.  J9 (on i and win) seems to include length of xml decl in xml length, but not in xml offset for xml streamSource.");
      return;
    }
    testSetSQLXMLStreamSource(JDPSTest.PSTEST_SETXML, "VAR041");
  }

  /**
   * setSQLXML() - Set an XML parameter using a SQLXML set by setResult --
   * DOMSource
   **/

  public void Var042() {
    testSetSQLXMLDOMSource(JDPSTest.PSTEST_SETXML, "VAR042");
  }

  /* Repeat XML tests for CCSID 13488 */
  public void Var043() {
    testSetSQLXMLString(JDPSTest.PSTEST_SETXML13488, "VAR043");
  }

  public void Var044() {
    testSetSQLXMLBinaryStream(JDPSTest.PSTEST_SETXML13488, "VAR044");
  }

  public void Var045() {
    testSetSQLXMLCharacterStream(JDPSTest.PSTEST_SETXML13488, "VAR045");
  }

  public void Var046() {
    testSetSQLXMLStAXSource(JDPSTest.PSTEST_SETXML13488, "VAR046");
  }

  public void Var047() {
    testSetSQLXMLSAXSource(JDPSTest.PSTEST_SETXML13488, "VAR047");
  }

  public void Var048() {
    if (isToolboxDriver()) {
      notApplicable(
          "NOTE:  Toolbox TC only works on windows non-ibm java for now.  J9 (on i and win) seems to include length of xml decl in xml length, but not in xml offset for xml streamSource.");
      return;
    }
    testSetSQLXMLStreamSource(JDPSTest.PSTEST_SETXML13488, "VAR048");
  }

  public void Var049() {
    testSetSQLXMLDOMSource(JDPSTest.PSTEST_SETXML13488, "VAR049");
  }

  /* Repeat XML tests for CCSID 1200 */
  public void Var050() {
    testSetSQLXMLString(JDPSTest.PSTEST_SETXML1200, "VAR050");
  }

  public void Var051() {
    testSetSQLXMLBinaryStream(JDPSTest.PSTEST_SETXML1200, "VAR051");
  }

  public void Var052() {
    testSetSQLXMLCharacterStream(JDPSTest.PSTEST_SETXML1200, "VAR052");
  }

  public void Var053() {
    testSetSQLXMLStAXSource(JDPSTest.PSTEST_SETXML1200, "VAR053");
  }

  public void Var054() {
    testSetSQLXMLSAXSource(JDPSTest.PSTEST_SETXML1200, "VAR054");
  }

  public void Var055() {
    if (isToolboxDriver()) {
      notApplicable(
          "NOTE:  Toolbox TC only works on windows non-ibm java for now.  J9 (on i and win) seems to include length of xml decl in xml length, but not in xml offset for xml streamSource.");
      return;
    }
    testSetSQLXMLStreamSource(JDPSTest.PSTEST_SETXML1200, "VAR055");
  }

  public void Var056() {
    testSetSQLXMLDOMSource(JDPSTest.PSTEST_SETXML1200, "VAR056");
  }

  /* Repeat XML tests for CCSID 37 */
  public void Var057() {
    testSetSQLXMLString(JDPSTest.PSTEST_SETXML37, "VAR057");
  }

  public void Var058() {
    testSetSQLXMLBinaryStream(JDPSTest.PSTEST_SETXML37, "VAR058");
  }

  public void Var059() {
    testSetSQLXMLCharacterStream(JDPSTest.PSTEST_SETXML37, "VAR059");
  }

  public void Var060() {
    testSetSQLXMLStAXSource(JDPSTest.PSTEST_SETXML37, "VAR060");
  }

  public void Var061() {
    testSetSQLXMLSAXSource(JDPSTest.PSTEST_SETXML37, "VAR061");
  }

  public void Var062() {
    if (isToolboxDriver()) {
      notApplicable(
          "NOTE:  Toolbox TC only works on windows non-ibm java for now.  J9 (on i and win) seems to include length of xml decl in xml length, but not in xml offset for xml streamSource.");
      return;
    }
    testSetSQLXMLStreamSource(JDPSTest.PSTEST_SETXML37, "VAR062");
  }

  public void Var063() {
    testSetSQLXMLDOMSource(JDPSTest.PSTEST_SETXML37, "VAR063");
  }

  /**
   * setSQLXML() - Set a BOOLEAN parameter.
   **/
  public void Var064() {
    if (checkBooleanSupport()) {
      testSetFailed("C_BOOLEAN", sqlxml_);
    }
  }

}
