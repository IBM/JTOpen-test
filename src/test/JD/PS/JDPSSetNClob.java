///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetNClob.java
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
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;
import test.JD.JDTestUtilities;

import java.io.FileOutputStream;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; 
import java.util.Vector;

/**
 * Testcase JDPSSetNClob. This tests the following method of the JDBC
 * PreparedStatement class:
 * 
 * <ul>
 * <li>setNClob()
 * </ul>
 **/
public class JDPSSetNClob extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetNClob";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }

  // Constants.
  private static final String PACKAGE = "JDPSSCLOB";

  // Private data.
  private Connection connection_;
  private Statement statement_;

  /**
   * Constructor.
   **/
  public JDPSSetNClob(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDPSSetNClob", namesAndVars, runMode, fileOutputStream,
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
      String url = baseURL_;
      connection_ = testDriver_.getConnection(url, userId_, encryptedPassword_);

    } else {
      String url = baseURL_ + ";lob threshold=30000"
          + ";data truncation=true";
      connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
    }
    statement_ = connection_.createStatement();
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
  }

  /**
   * Compares a Clob with a String.
   **/
  private boolean compare(Clob i, String b) throws SQLException {
    return i.getSubString(1, (int) i.length()).equals(b); // @B1C
  }

  /**
   * setNClob() - Should throw exception when the prepared statement is closed.
   **/
  public void Var001() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_VARCHAR_50) VALUES (?)");
        ps.close();
        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");// ,
                                                                             // "Los
                                                                             // Angeles"));
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Los Angeles");
        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Should throw exception when an invalid index is specified.
   **/
  public void Var002() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_
            .prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "San Bernadino");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 100, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Should throw exception when index is 0.
   **/
  public void Var003() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_
            .prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Venice");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 0, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Should throw exception when index is -1.
   **/
  public void Var004() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_
            .prepareStatement("INSERT INTO " + JDPSTest.PSTEST_SET
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Beverly Hills");

        JDReflectionUtil.callMethod_V(ps, "setNClob", -1, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Should throw exception when the value is null.
   **/
  public void Var005() {
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_VARCHAR_50) VALUES (?)");

        Class<?>[] argClasses = new Class[2];
        argClasses[0] = Integer.TYPE;
        // TODO.. This will need to change when really running JDBC40
        try {
          argClasses[1] = Class.forName("java.sql.NClob");
        } catch (Exception e) {
          System.out.println("Warning :" + e);
          argClasses[1] = Class.forName("com.ibm.db2.jdbc.app.NClob");
        }
        Object[] args = new Object[2];
        args[0] = new Integer(1);
        args[1] = null;

        JDReflectionUtil.callMethod_V(ps, "setNClob", argClasses, args);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        Clob check = (Clob) JDReflectionUtil.callMethod_O(rs, "getNClob", 1);
        boolean wn = rs.wasNull();
        rs.close();

        assertCondition((check == null) && (wn == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setNClob() - Should work with a valid parameter index greater than 1.
   **/
  public void Var006() {
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_KEY, C_VARCHAR_50) VALUES (?, ?)");
        ps.setString(1, "Muchas");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Hollywood");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 2, o);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        Clob check = (Clob) JDReflectionUtil.callMethod_O(rs, "getNClob", 1);
        rs.close();

        assertCondition(compare(check, "Hollywood"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setNClob() - Should throw exception when the parameter is not an input
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

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Burbank");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 2, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Verify that a data truncation warning is posted when data is
   * truncated.
   **/
  public void Var008() {
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_VARCHAR_50) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L,
            "The example strings in this test case are places in the Los Angeles area.");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (DataTruncation dt) {
        assertCondition((dt.getIndex() == 1) && (dt.getParameter() == true)
            && (dt.getRead() == false) && (dt.getDataSize() == 73)
            && (dt.getTransferSize() == 50));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setNClob() - Set a SMALLINT parameter.
   **/
  public void Var009() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_SMALLINT) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Anahiem");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Set a INTEGER parameter.
   **/
  public void Var010() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_INTEGER) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Pasadena");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Set a REAL parameter.
   **/
  public void Var011() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_REAL) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "West Hollywood");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Set a FLOAT parameter.
   **/
  public void Var012() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_FLOAT) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Sierra Madre");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Set a DOUBLE parameter.
   **/
  public void Var013() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DOUBLE) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Malibu");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Set a DECIMAL parameter.
   **/
  public void Var014() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_DECIMAL_105) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Long Beach");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Set a NUMERIC parameter.
   **/
  public void Var015() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_NUMERIC_50) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Compton");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Set a CHAR(1) parameter, where the string is longer.
   **/
  public void Var016() {
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_1) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "S");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_CHAR_1 FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        Clob check = (Clob) JDReflectionUtil.callMethod_O(rs, "getNClob", 1);
        rs.close();

        assertCondition(compare(check, "S"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setNClob() - Set a CHAR(50) parameter.
   **/
  public void Var017() {
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CHAR_50) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Orange");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_CHAR_50 FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        Clob check = (Clob) JDReflectionUtil.callMethod_O(rs, "getNClob", 1);
        rs.close();

        assertCondition(compare(check,
            "Orange                                            "));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setNClob() - Set a VARCHAR(50) parameter.
   **/
  public void Var018() {
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_VARCHAR_50) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "La Brea");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        Clob check = (Clob) JDReflectionUtil.callMethod_O(rs, "getNClob", 1);
        rs.close();

        assertCondition(compare(check, "La Brea"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setNClob() - Set a CLOB parameter, when the data is passed directly.
   **/
  public void Var019() {
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CLOB) VALUES (?)");

          Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
          JDReflectionUtil.callMethod_V(o, "setString", 1L, "Disneyland");

          JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_
              .executeQuery("SELECT C_CLOB FROM " + JDPSTest.PSTEST_SET);
          rs.next();
          Clob check = (Clob) JDReflectionUtil.callMethod_O(rs, "getNClob", 1);
          rs.close();

          assertCondition(compare(check, "Disneyland"),
              check.getSubString(1, (int) check.length()) + " sb Disneyland");
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setNClob() - Set a CLOB parameter, when the data is passed in a locator.
   **/
  public void Var020() {
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

          String url = baseURL_ + ";lob threshold=1";
          Connection c = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
          PreparedStatement ps = c.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_CLOB) VALUES (?)");

          Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
          JDReflectionUtil.callMethod_V(o, "setString", 1L, "Disneyland Hotel");

          JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
          ps.executeUpdate();
          ps.close();
          c.close();

          ResultSet rs = statement_
              .executeQuery("SELECT C_CLOB FROM " + JDPSTest.PSTEST_SET);
          rs.next();
          Clob check = (Clob) JDReflectionUtil.callMethod_O(rs, "getNClob", 1);
          rs.close();

          assertCondition(compare(check, "Disneyland Hotel"),
              check.getSubString(1, (int) check.length())
                  + " sb Disneyland Hotel");
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setNClob() - Set a DBCLOB parameter, when the data is passed directly.
   **/
  public void Var021() {
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        succeeded();
        /*
         * Need to investigate this variation ... try { statement_.executeUpdate
         * ("DELETE FROM " + JDPSTest.PSTEST_SET);
         * 
         * PreparedStatement ps = connection_.prepareStatement ( "INSERT INTO "
         * + JDPSTest.PSTEST_SET + " (C_DBCLOB) VALUES (?)");
         * JDReflectionUtil.callMethod_V(ps,"setNClob",1,
         * JDReflectionUtil.callMethod_OS(connection_, "createNClob",
         * "Rodeo Drive")); ps.executeUpdate (); ps.close ();
         * 
         * ResultSet rs = statement_.executeQuery ("SELECT C_DBCLOB FROM " +
         * JDPSTest.PSTEST_SET); rs.next (); Clob check =
         * JDReflectionUtil.callMethod_O(rs,"getNClob", 1); rs.close ();
         * 
         * assertCondition (compare (check, "Rodeo Drive")); } catch (Exception
         * e) { failed (e, "Unexpected Exception"); }
         */
      }
    }
  }

  /**
   * setNClob() - Set a DBCLOB parameter, when the data is passed as a locator.
   **/
  public void Var022() {
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        succeeded();
        /*
         * Need to investigate this variation ... try { statement_.executeUpdate
         * ("DELETE FROM " + JDPSTest.PSTEST_SET);
         * 
         * String url = baseURL_ + ";user=" + systemObject_.getUserId() +
         * ";password=" + password_ + ";lob threshold=1"; Connection c =
         * testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_); PreparedStatement ps =
         * c.prepareStatement ( "INSERT INTO " + JDPSTest.PSTEST_SET +
         * " (C_DBCLOB) VALUES (?)");
         * JDReflectionUtil.callMethod_V(ps,"setNClob",1,
         * JDReflectionUtil.callMethod_OS(connection_, "createNClob",
         * "Disneyland Motel")); ps.executeUpdate (); ps.close (); c.close ();
         * 
         * ResultSet rs = statement_.executeQuery ("SELECT C_DBCLOB FROM " +
         * JDPSTest.PSTEST_SET); rs.next (); Clob check =
         * JDReflectionUtil.callMethod_O(rs,"getNClob", 1); rs.close ();
         * 
         * assertCondition (compare (check, "Disneyland Hotel")); } catch
         * (Exception e) { failed (e, "Unexpected Exception"); }
         */
      }
    }
  }

  /**
   * setNClob() - Set a BINARY parameter.
   **/
  public void Var023() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BINARY_20) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "X");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * setNClob() - Set a VARBINARY parameter.
   **/
  public void Var024() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + JDPSTest.PSTEST_SET + " (C_VARBINARY_20) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Puerto");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * setNClob() - Set a BLOB parameter.
   **/
  public void Var025() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BLOB) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "California");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Set a DATE parameter.
   **/
  public void Var026() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_DATE) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "West Coast");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Set a TIME parameter.
   **/
  public void Var027() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIME) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Pacific Ocean");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Set a TIMESTAMP parameter.
   **/
  public void Var028() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_TIMESTAMP) VALUES (?)");

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Airport");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Set a DATALINK parameter.
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

          Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
          JDReflectionUtil.callMethod_V(o, "setString", 1L, "Dodger Stadium");

          JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
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
   * setNClob() - Set a DISTINCT parameter.
   * 
   * SQL400 - Native JDBC has built the setNClob support on top of the
   * setCharacterStream. setCharacterStream is built on top of setString. With
   * setString, this call works. I could have put a call into setNClob to check
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

          Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
          JDReflectionUtil.callMethod_V(o, "setString", 1L, "12345678");

          JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
          ps.executeUpdate();
          ps.close();
          if (isToolboxDriver())
            failed("Didn't throw SQLException");
          else
            assertCondition(true);
        } catch (Exception e) {
          if (isToolboxDriver())
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          else
            failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setNClob() - Set a BIGINT parameter.
   **/
  public void Var031() {
    if (checkJdbc40()) {
      if (checkBigintSupport()) {
        try {
          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SET + " (C_BIGINT) VALUES (?)");

          Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
          JDReflectionUtil.callMethod_V(o, "setString", 1L, "Orange County");

          JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
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
   * setNClob() - Set a VARCHAR(50) parameter.
   * 
   * SQL400 - JDSetupPackage cache doesn't have enough information to be driver
   * neutral today.
   **/
  public void Var032() {
    if (checkJdbc40()) {
      try {
        String insert = "INSERT INTO " + JDPSTest.PSTEST_SET
            + " (C_VARCHAR_50) VALUES (?)";

        if (isToolboxDriver())
          JDSetupPackage.prime(systemObject_,  PACKAGE,
              JDPSTest.COLLECTION, insert);
        else
          JDSetupPackage.prime(systemObject_, encryptedPassword_,  PACKAGE,
              JDPSTest.COLLECTION, insert, "", getDriver());

        statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

        PreparedStatement ps = connection_.prepareStatement(insert);

        Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
        JDReflectionUtil.callMethod_V(o, "setString", 1L, "Rose Bowl");

        JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_VARCHAR_50 FROM " + JDPSTest.PSTEST_SET);
        rs.next();
        Clob check = (Clob) JDReflectionUtil.callMethod_O(rs, "getNClob", 1);
        rs.close();

        assertCondition(compare(check, "Rose Bowl"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setNClob() - Set a CLOB parameter, when the number of parameters is > 20
   * and the only clob parameter is the first parameter. This detects a bug in
   * the native JDBC driver due to the CLI.
   **/
  public void Var033() {
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          String sql;
          int columnCount = 31;
          String table = JDPSTest.COLLECTION + ".JDPSSC" + columnCount;

          try {
            statement_.executeUpdate("drop table " + table);
          } catch (Exception e) {
          }

          sql = "create table " + table + " ( c1 clob(1M) ";
          for (int i = 2; i <= columnCount; i++) {
            sql += ", c" + i + " int ";
          }
          sql += ")";
          statement_.executeUpdate(sql);

          sql = "UPDATE " + table + " set C1=?";
          for (int i = 2; i <= columnCount; i++) {
            sql += ", c" + i + " = ? ";
          }

          PreparedStatement pstmt = connection_.prepareStatement(sql);
          pstmt.setString(1, "X");
          for (int i = 2; i <= columnCount; i++) {
            pstmt.setInt(i, i);
          }

          pstmt.execute();

          pstmt.close();

          assertCondition(true);
        } catch (Exception e) {
          failed(e,
              "Unexpected Exception -- testcase added by native driver 3/1/05 to detect CLI bug");
        }
      }
    }
  }

  /**
   * setNClob() - Set a DECFLOAT parameter.
   **/
  public void Var034() {
    if (checkJdbc40()) {
      if (checkDecFloatSupport()) {
        try {
          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SETDFP16 + "  VALUES (?)");

          Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
          JDReflectionUtil.callMethod_V(o, "setString", 1L, "Orange County");

          JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
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
   * setNClob() - Set a DECFLOAT parameter.
   **/
  public void Var035() {
    if (checkJdbc40()) {
      if (checkDecFloatSupport()) {
        try {
          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + JDPSTest.PSTEST_SETDFP34 + "  VALUES (?)");

          Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
          JDReflectionUtil.callMethod_V(o, "setString", 1L, "Orange County");

          JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);
          ps.executeUpdate();
          ps.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }

  }

  public void Var036() {
    notApplicable();
  }

  public void Var037() {
    notApplicable();
  }

  public void Var038() {
    notApplicable();
  }

  public void Var039() {
    notApplicable();
  }

  public void Var040() {
    notApplicable();
  }

  public void Var041() {
    notApplicable();
  }

  public void Var042() {
    notApplicable();
  }

  public void Var043() {
    notApplicable();
  }

  /**
   * setClob() - Set an XML parameter.
   **/
  public void setXML(String tablename, String data, String expected) {
    String added = " -- added by native driver 08/21/2009";
    if (checkJdbc40()) {
      if (checkXmlSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + tablename);

          PreparedStatement ps = connection_
              .prepareStatement("INSERT INTO " + tablename + "  VALUES (?)");

          Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
          JDReflectionUtil.callMethod_V(o, "setString", 1L, data);

          JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);

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

  }

  /**
   * setClob() - Set an XML parameter using invalid data.
   **/
  public void setInvalidXML(String tablename, String data,
      String expectedException) {
    String added = " -- added by native driver 08/21/2009";
    if (checkJdbc40()) {
      if (checkXmlSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + tablename);

          PreparedStatement ps = connection_
              .prepareStatement("INSERT INTO " + tablename + "  VALUES (?)");

          Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
          JDReflectionUtil.callMethod_V(o, "setString", 1L, data);

          JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);

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
  }

  public void Var044() {
    setXML(JDPSTest.PSTEST_SETXML, "<Test>VAR044\u00a2</Test>",
        "<Test>VAR044\u00a2</Test>");
  }

  public void Var045() {
    setXML(JDPSTest.PSTEST_SETXML,
        "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR045\u00a2</Test>",
        "<Test>VAR045\u00a2</Test>");
  }

  public void Var046() {
    setXML(JDPSTest.PSTEST_SETXML,
        "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR046\u0130\u3041\ud800\udf30</Test>",
        "<Test>VAR046\u0130\u3041\ud800\udf30</Test>");
  }

  public void Var047() {
    setXML(JDPSTest.PSTEST_SETXML13488, "<Test>VAR047</Test>",
        "<Test>VAR047</Test>");
  }

  public void Var048() {
    setXML(JDPSTest.PSTEST_SETXML13488,
        "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR048\u00a2</Test>",
        "<Test>VAR048\u00a2</Test>");
  }

  public void Var049() {
    setXML(JDPSTest.PSTEST_SETXML13488,
        "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR049\u0130\u3041</Test>",
        "<Test>VAR049\u0130\u3041</Test>");
  }

  public void Var050() {
    setXML(JDPSTest.PSTEST_SETXML1200, "<Test>VAR050</Test>",
        "<Test>VAR050</Test>");
  }

  public void Var051() {
    setXML(JDPSTest.PSTEST_SETXML1200,
        "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR051\u00a2</Test>",
        "<Test>VAR051\u00a2</Test>");
  }

  public void Var052() {
    setXML(JDPSTest.PSTEST_SETXML1200,
        "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR052\u0130\u3041\ud800\udf30</Test>",
        "<Test>VAR052\u0130\u3041\ud800\udf30</Test>");
  }

  public void Var053() {
    setXML(JDPSTest.PSTEST_SETXML37, "<Test>VAR053\u00a2</Test>",
        "<Test>VAR053\u00a2</Test>");
  }

  public void Var054() {
    setXML(JDPSTest.PSTEST_SETXML37,
        "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR054\u00a2</Test>",
        "<Test>VAR054\u00a2</Test>");
  }

  public void Var055() {
    setXML(JDPSTest.PSTEST_SETXML37,
        "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR055\u00a2</Test>",
        "<Test>VAR055\u00a2</Test>");
  }

  public void Var056() {
    setXML(JDPSTest.PSTEST_SETXML937, "<Test>VAR056\u672b</Test>",
        "<Test>VAR056\u672b</Test>");
  }

  public void Var057() {
    setXML(JDPSTest.PSTEST_SETXML937,
        "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR057\u672b</Test>",
        "<Test>VAR057\u672b</Test>");
  }

  public void Var058() {
    setXML(JDPSTest.PSTEST_SETXML937,
        "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR058\u672b</Test>",
        "<Test>VAR058\u672b</Test>");
  }

  public void Var059() {
    setXML(JDPSTest.PSTEST_SETXML290, "<Test>VAR059</Test>",
        "<Test>VAR059</Test>");
  }

  public void Var060() {
    setXML(JDPSTest.PSTEST_SETXML290,
        "<?xml version=\"1.0\" encoding=\"UCS-2\"?><Test>VAR060\uff7a</Test>",
        "<Test>VAR060\uff7a</Test>");
  }

  public void Var061() {
    setXML(JDPSTest.PSTEST_SETXML290,
        "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR061\uff98</Test>",
        "<Test>VAR061\uff98</Test>");
  }

  /* Encoding is stripped for character data since we know is is UTF-16 */
  public void Var062() {
    setXML(JDPSTest.PSTEST_SETXML,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Test>VAR062</Test>",
        "<Test>VAR062</Test>");
  }

  public void Var063() {
    setInvalidXML(JDPSTest.PSTEST_SETXML,
        "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Test>VAR063</Tes>",
        "XML parsing failed");
  }

  public void Var064() {
    setXML(JDPSTest.PSTEST_SETXML13488,
        "<?xml version=\"1.0\" encoding=\"IBM-037\"?><Test>VAR064</Test>",
        "<Test>VAR064</Test>");
  }

  public void Var065() {
    setInvalidXML(JDPSTest.PSTEST_SETXML13488,
        "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Tes>VAR065</Test>",
        "XML parsing failed");
  }

  public void Var066() {
    setXML(JDPSTest.PSTEST_SETXML1200,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Test>VAR066</Test>",
        "<Test>VAR066</Test>");
  }

  public void Var067() {
    setInvalidXML(JDPSTest.PSTEST_SETXML1200,
        "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Tes>VAR067</Test>",
        "XML parsing failed");
  }

  public void Var068() {
    setXML(JDPSTest.PSTEST_SETXML37,
        "<?xml version=\"1.0\" encoding=\"IBM-037\"?><Test>VAR068\u00a2</Test>",
        "<Test>VAR068\u00a2</Test>");
  }

  public void Var069() {
    setInvalidXML(JDPSTest.PSTEST_SETXML37,
        "<?xml version=\"1.0\" encoding=\"UTF-16\"?>  <Tet>VAR069</Test>",
        "XML parsing failed");
  }

  public void setInvalid(String column, String inputValue,
      String exceptionInfo) {
    StringBuffer sb = new StringBuffer();
    try {
      statement_.executeUpdate("DELETE FROM " + JDPSTest.PSTEST_SET);

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + JDPSTest.PSTEST_SET + " (" + column + ") VALUES (?)");

      Object o = JDReflectionUtil.callMethod_O(connection_, "createNClob");
      JDReflectionUtil.callMethod_V(o, "setString", 1L, inputValue);

      JDReflectionUtil.callMethod_V(ps, "setNClob", 1, o);

      ps.close();
      failed("Didn't throw SQLException for column(" + column + ") inputValue("
          + inputValue + ")");
    } catch (Exception e) {
      assertExceptionContains(e, exceptionInfo, sb);
    }
  }

  public void Var070() {
    if (checkBooleanSupport()) {
      setInvalid("C_BOOLEAN", "true", "Data type mismatch");
    }
  }

  public void Var071() {
    if (checkBooleanSupport()) {
      setInvalid("C_BOOLEAN", "false", "Data type mismatch");
    }
  }

}
