///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetRowId.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDPSSetRowId.java
//
// Classes:      JDPSSetRowId
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
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;
import test.JD.JDSerializeFile;
import java.sql.SQLException;

/**
 * Testcase JDPSSetRowId. This tests the following method of the JDBC
 * PreparedStatement class:
 * 
 * <ul>
 * <li>setRowId()
 * </ul>
 **/
public class JDPSSetRowId extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetRowId";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }

  String added = " -- added 02/07/2007 by native Driver";

  // Constants.
  private static final String PACKAGE = "JDPSSBYTES";

  // Private data.
  private Statement statement_;

  /**
   * Constructor.
   **/
  public JDPSSetRowId(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDPSSetRowId", namesAndVars, runMode, fileOutputStream,
 password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    String url = baseURL_+ ";data truncation=true";
    connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
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
    connection_ = null; 

  }

  Object createRowId(byte[] arg) throws Exception {
    Object testRowId = null;
    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      testRowId = JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2RowId",
          arg);
    } else if (isToolboxDriver()) {
      testRowId = JDReflectionUtil
          .createObject("com.ibm.as400.access.AS400JDBCRowId", arg);
    } else {
      output_.println("DRIVER NEEDS TO ADD CODE TO CREATE ROWID OBJECT");
    }
    return testRowId;
  }

  /**
   * setRowId() - Should throw exception when the prepared statement is closed.
   **/
  public void Var001() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_VARBINARY_20) VALUES (?)");
        ps.close();
        byte[] b = new byte[] { (byte) 22, (byte) 98, (byte) -2 };
        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Should throw exception when an invalid index is specified.
   **/
  public void Var002() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_
            .prepareStatement("INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
        byte[] b = new byte[] { (byte) 22, (byte) 4, (byte) 4, (byte) 98,
            (byte) -2 };
        JDReflectionUtil.callMethod_V(ps, "setRowId", 100, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Should throw exception when index is 0.
   **/
  public void Var003() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_
            .prepareStatement("INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
        byte[] b = new byte[] { (byte) 4, (byte) 98, (byte) -2 };
        JDReflectionUtil.callMethod_V(ps, "setRowId", 0, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Should throw exception when index is -1.
   **/
  public void Var004() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_
            .prepareStatement("INSERT INTO " + pstestSet.getName()
                + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
        byte[] b = new byte[] { (byte) 22, (byte) 4, (byte) 98, (byte) 22,
            (byte) -2 };
        JDReflectionUtil.callMethod_V(ps, "setRowId", 0, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Should set to SQL NULL when the value is null.
   **/
  public void Var005() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_VARBINARY_20) VALUES (?)");
        Class<?>[] argTypes = new Class[2];
        argTypes[0] = Integer.TYPE;
        try {
          argTypes[1] = Class.forName("java.sql.RowId");
        } catch (Exception e) {
          argTypes[1] = Class.forName("com.ibm.db2.jdbc.app.RowId");
        }
        Object[] args = new Object[2];
        args[0] = Integer.valueOf(1);
        args[1] = null;

        JDReflectionUtil.callMethod_V(ps, "setRowId", argTypes, args);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_VARBINARY_20 FROM " + pstestSet.getName());
        rs.next();
        Object rowId = JDReflectionUtil.callMethod_O(rs, "getRowId", 1);

        boolean wn = rs.wasNull();
        rs.close();

        assertCondition((rowId == null) && (wn == true), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Should work with a valid parameter index greater than 1.
   **/
  public void Var006() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_KEY, C_VARBINARY_20) VALUES (?, ?)");
        ps.setString(1, "Muchas");
        byte[] b = new byte[] { (byte) -22, (byte) 4, (byte) -2 };
        JDReflectionUtil.callMethod_V(ps, "setRowId", 2, createRowId(b));
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_VARBINARY_20 FROM " + pstestSet.getName());
        rs.next();
        Object rowId = JDReflectionUtil.callMethod_O(rs, "getRowId", 1);
        byte[] check = (byte[]) JDReflectionUtil.callMethod_O(rowId,
            "getBytes");

        rs.close();

        assertCondition(areEqual(b, check), added);
      } catch (Exception e) {
        if (isToolboxDriver())
          assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
        else
          failed(e, "Unexpected Exception" + added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Should throw exception when the parameter is not an input
   * parameter.
   **/
  public void Var007() {
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
        byte[] b = new byte[] { (byte) 98, (byte) 98, (byte) -2 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 2, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   * setRowId() - Verify that a data truncation warning is posted when data is
   * truncated.
   **/
  public void Var008() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
int length = 0;
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_VARBINARY_20) VALUES (?)");
        byte[] b = new byte[] { (byte) -22, (byte) 4, (byte) 9, (byte) -2,
            (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
            (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
            (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
            (byte) 77, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
            (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
            (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
            (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
            (byte) 50, (byte) 2, (byte) 0, (byte) -111, (byte) 50, (byte) 2,
            (byte) 0, (byte) -111, (byte) 50, (byte) 2, (byte) 0, (byte) -111,
            (byte) 50, (byte) 2, (byte) 22 };

        length = b.length;
        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (DataTruncation dt) {
        assertCondition((dt.getIndex() == 1) && (dt.getParameter() == true)
            && (dt.getRead() == false) && (dt.getDataSize() == length)
            && (dt.getTransferSize() == 20));
      } catch (Exception e) {
        if (isToolboxDriver())
          assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
        else
          failed(e, "Unexpected Exception" + added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void testSetFailed(String columnName, byte[] inArray) {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (" + columnName + ") VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(inArray));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a SMALLINT parameter.
   **/
  public void Var009() {

    byte[] b = new byte[] { (byte) 98 };
    testSetFailed("C_SMALLINT", b);

  }

  /**
   * setRowId() - Set a INTEGER parameter.
   **/
  public void Var010() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_INTEGER) VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 123 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a REAL parameter.
   **/
  public void Var011() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_REAL) VALUES (?)");
        byte[] b = new byte[] { (byte) 98, (byte) -2 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a FLOAT parameter.
   **/
  public void Var012() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_FLOAT) VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) 7, (byte) -2,
            (byte) 45 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a DOUBLE parameter.
   **/
  public void Var013() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_DOUBLE) VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) 45, (byte) 12 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a DECIMAL parameter.
   **/
  public void Var014() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_DECIMAL_105) VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) -2, (byte) 98, (byte) -2,
            (byte) 45, (byte) 12, (byte) -33 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a NUMERIC parameter.
   **/
  public void Var015() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_NUMERIC_50) VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -2, (byte) 45,
            (byte) 12, (byte) -33 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a CHAR(1) parameter.
   **/
  public void Var016() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_CHAR_1) VALUES (?)");
        byte[] b = new byte[] { (byte) 98 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a CHAR(50) parameter.
   **/
  public void Var017() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_CHAR_50) VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) 98, (byte) -12, (byte) 45,
            (byte) 12, (byte) -33, (byte) 0 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a VARCHAR(50) parameter.
   **/
  public void Var018() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_VARCHAR_50) VALUES (?)");
        byte[] b = new byte[] { (byte) 7, (byte) -12, (byte) 12, (byte) -33 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a CLOB parameter.
   **/
  public void Var019() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_CLOB) VALUES (?)");
        byte[] b = new byte[] { (byte) 108, (byte) 0, (byte) -12, (byte) 12,
            (byte) -33 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a DBCLOB parameter.
   **/
  public void Var020() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_DBCLOB) VALUES (?)");
        byte[] b = new byte[] { (byte) 0, (byte) 66, (byte) 12, (byte) -33 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a BINARY parameter.
   **/
  public void Var021() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_BINARY_20) VALUES (?)");
        byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) -12, (byte) 1,
            (byte) 0, (byte) 12, (byte) -33, (byte) 57, (byte) 9 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_BINARY_20 FROM " + pstestSet.getName());
        rs.next();
        Object rowId = JDReflectionUtil.callMethod_O(rs, "getRowId", 1);
        byte[] check = (byte[]) JDReflectionUtil.callMethod_O(rowId,
            "getBytes");

        rs.close();

        byte[] check2 = new byte[20];
        System.arraycopy(b, 0, check2, 0, b.length);

        assertCondition(areEqual(check, check2), added);
      } catch (Exception e) {
        if (isToolboxDriver())
          assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
        else
          failed(e, "Unexpected Exception" + added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a VARBINARY parameter.
   **/
  public void Var022() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_VARBINARY_20) VALUES (?)");
        byte[] b = new byte[] { (byte) 0, (byte) -12, (byte) 0, (byte) -33,
            (byte) 57, (byte) 9 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_VARBINARY_20 FROM " + pstestSet.getName());
        rs.next();
        Object rowId = JDReflectionUtil.callMethod_O(rs, "getRowId", 1);
        byte[] check = (byte[]) JDReflectionUtil.callMethod_O(rowId,
            "getBytes");
        rs.close();

        assertCondition(areEqual(b, check), added);
      } catch (Exception e) {
        if (isToolboxDriver())
          assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
        else
          failed(e, "Unexpected Exception" + added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a VARBINARY parameter to an empty array.
   **/
  public void Var023() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_VARBINARY_20) VALUES (?)");
        byte[] b = new byte[0];

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_VARBINARY_20 FROM " + pstestSet.getName());
        rs.next();
        Object rowId = JDReflectionUtil.callMethod_O(rs, "getRowId", 1);
        byte[] check = (byte[]) JDReflectionUtil.callMethod_O(rowId,
            "getBytes");
        rs.close();

        assertCondition(check.length == 0, added);
      } catch (Exception e) {
        if (isToolboxDriver())
          assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
        else
          failed(e, "Unexpected Exception" + added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a BLOB parameter.
   **/
  public void Var024() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkJdbc40()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_BLOB) VALUES (?)");
        byte[] b = new byte[] { (byte) 1, (byte) -12, (byte) 45, (byte) -33,
            (byte) 0 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_BLOB FROM " + pstestSet.getName());
        rs.next();
        InputStream is2 = rs.getBinaryStream(1);
        byte[] check = new byte[b.length];
        is2.read(check);
        is2.close();

        rs.close();

        assertCondition(areEqual(b, check), added);
      } catch (Exception e) {
        if (isToolboxDriver())
          assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
        else
          failed(e, "Unexpected Exception" + added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a DATE parameter.
   **/
  public void Var025() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_DATE) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 1, (byte) -33, (byte) 0 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a TIME parameter.
   **/
  public void Var026() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_TIME) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 1, (byte) -33,
            (byte) 0, (byte) 45, (byte) 5 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a TIMESTAMP parameter.
   **/
  public void Var027() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_TIMESTAMP) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 45, (byte) 11, (byte) -33,
            (byte) 0, (byte) 5 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a DATALINK parameter.
   **/
  public void Var028() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_DATALINK) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 1, (byte) 45, (byte) 1,
            (byte) 11, (byte) -33, (byte) 0, (byte) 5, (byte) 100 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a DISTINCT parameter.
   **/
  public void Var029() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_DISTINCT) VALUES (?)");
        byte[] b = new byte[] { (byte) -12, (byte) 1, (byte) -33, (byte) 0,
            (byte) 5, (byte) 100 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a BIGINT parameter.
   **/
  public void Var030() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkJdbc40()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_BIGINT) VALUES (?)");
        byte[] b = new byte[] { (byte) -7, (byte) -98, (byte) 2, (byte) 0,
            (byte) -7, (byte) -98, (byte) 2, (byte) 10 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.close();
        failed("Didn't throw SQLException" + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a VARBINARY parameter with package caching.
   **/
  public void Var031() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkJdbc40()) {
      try {
        String insert = "INSERT INTO " + pstestSet.getName()
            + " (C_VARBINARY_20) VALUES (?)";

        if (isToolboxDriver())
          JDSetupPackage.prime(systemObject_, PACKAGE,
              JDPSTest.COLLECTION, insert);
        else
          JDSetupPackage.prime(systemObject_, encryptedPassword_, PACKAGE,
              JDPSTest.COLLECTION, insert, "", getDriver());

        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        Connection c2 = testDriver_.getConnection(baseURL_
            + ";extended dynamic=true;package=" + PACKAGE + ";package library="
            + JDPSTest.COLLECTION + ";package cache=true", userId_, encryptedPassword_);
        PreparedStatement ps = c2.prepareStatement(insert);
        byte[] b = new byte[] { (byte) 11, (byte) -12, (byte) 45, (byte) 11,
            (byte) -33, (byte) 0, (byte) 5, (byte) 100 };

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_VARBINARY_20 FROM " + pstestSet.getName());
        rs.next();
        Object rowId = JDReflectionUtil.callMethod_O(rs, "getRowId", 1);
        byte[] check = (byte[]) JDReflectionUtil.callMethod_O(rowId,
            "getBytes");
        rs.close();
        c2.close();

        assertCondition(areEqual(check, b), added);
      } catch (Exception e) {
        if (isToolboxDriver())
          assertCondition(e.getMessage().indexOf("Data type mismatch") != -1);
        else
          failed(e, "Unexpected Exception" + added);
      }
    }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (pstestSet != null) {
        try {
          pstestSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setRowId() - Set a ROWID parameter and make sure it works.
   **/
  public void Var032() {
    StringBuffer message = new StringBuffer();
    String sql = "";
    if (checkJdbc40()) {
      try {
        String tableName = JDPSTest.COLLECTION + ".JDPSSROWID";
        try {
          statement_.executeUpdate("DROP TABLE " + tableName);
        } catch (Exception e) {
        }

        sql = "CREATE TABLE " + tableName + " (R ROWID, STUFF INT)";

        statement_.executeUpdate(sql);
        sql = "INSERT INTO " + tableName + " VALUES(DEFAULT, 1)";
        statement_.executeUpdate(sql);
        sql = "SELECT * FROM " + tableName;
        ResultSet rs = statement_.executeQuery(sql);
        rs.next();
        Object rowId = JDReflectionUtil.callMethod_O(rs, "getRowId", 1);
        rs.close();
        byte[] b = (byte[]) JDReflectionUtil.callMethod_O(rowId, "getBytes");
        if (rowId != null) {
          message
              .append("ROWID ='" + rowId.toString() + "' length=" + b.length);
        }
        sql = "SELECT * FROM " + tableName + " WHERE R = ?";
        PreparedStatement ps = connection_.prepareStatement(sql);

        JDReflectionUtil.callMethod_V(ps, "setRowId", 1, rowId);
        rs = ps.executeQuery();
        rs.next();
        InputStream is2 = rs.getBinaryStream(1);
        byte[] check = new byte[40];
        int bytesRead = is2.read(check);
        if (bytesRead > 0) {
          byte[] answer = new byte[bytesRead];
          for (int i = 0; i < bytesRead; i++) {
            answer[i] = check[i];
          }
          check = answer;
        }
        is2.close();

        rs.close();
        sql = "DROP TABLE " + tableName;
        statement_.executeUpdate(sql);

        assertCondition(areEqual(b, check),
            message.toString() + "\n***\ngot\n***\n" + bytesToString(check)
                + "\n****\nexpected\n***\n" + bytesToString(b) + "\n****\n"
                + added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception last SQL was " + sql + "\nMessage="
            + message.toString() + added);
      }
    }
  }

  /**
   * setRowId() - Set an XML parameter.
   **/
  public void Var033() {
    if (checkJdbc40()) {
      if (checkXmlSupport()) {
        JDSerializeFile pstestSetxml = null;
        try {
          pstestSetxml = JDPSTest.getSerializeFile(connection_, JDPSTest.SETXML);
          String tablename = pstestSetxml.getName(); 
          PreparedStatement ps = connection_.prepareStatement(
              "INSERT INTO " + tablename + " VALUES (?)");
          try {
            byte[] b = new byte[] { (byte) -12, (byte) 1, (byte) 45, (byte) 1,
                (byte) 11, (byte) -33, (byte) 0, (byte) 5, (byte) 100 };

            JDReflectionUtil.callMethod_V(ps, "setRowId", 1, createRowId(b));
            ps.execute();
            ps.close();
            failed("Didn't throw SQLException" + added);
          } catch (Exception e) {
            assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
          }
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        } finally {
          if (pstestSetxml != null) {
            try {
              pstestSetxml.close();
            } catch (SQLException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
  }

  /**
   * setRowId() - Set a BOOLEAN parameter.
   **/
  public void Var034() {

    if (checkBooleanSupport()) {
      byte[] b = new byte[] { (byte) 98 };
      testSetFailed("C_BOOLEAN", b);
    }

  }

}
