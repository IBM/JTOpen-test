///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetTimestamp.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDPSSetTimestamp.java
//
// Classes:      JDPSSetTimestamp
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCTimestamp;

import test.JDPSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Hashtable; import java.util.Vector;
import java.util.TimeZone;
import test.JD.JDSerializeFile;
import java.sql.SQLException;

/**
 * Testcase JDPSSetTimestamp. This tests the following method of the JDBC
 * PreparedStatement class:
 * 
 * <ul>
 * <li>setTimestamp()
 * </ul>
 **/
public class JDPSSetTimestamp extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetTimestamp";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }

  // Constants.
  private static final String PACKAGE = "JDPSSTS";

  // Private data.
  private Statement statement_;

  private boolean nativeDriver = false;

  /**
   * Constructor.
   **/
  public JDPSSetTimestamp(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDPSSetTimestamp", namesAndVars, runMode,
        fileOutputStream, password);
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
      String url = baseURL_ ;
      connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
    }
    statement_ = connection_.createStatement();

    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      nativeDriver = true;
    }
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

  /**
   * setTimestamp() - Should throw exception when the prepared statement is
   * closed.
   **/
  public void Var001() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + pstestSet.getName() + " (C_TIMESTAMP) VALUES (?)");
      ps.close();
      ps.setTimestamp(1, new Timestamp(234));
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    
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
   * setTimestamp() - Should throw exception when an invalid index is specified.
   **/
  public void Var002() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      PreparedStatement ps = connection_
          .prepareStatement("INSERT INTO " + pstestSet.getName()
              + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
      ps.setTimestamp(100, new Timestamp(234234));
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    
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
   * setTimestamp() - Should throw exception when index is 0.
   **/
  public void Var003() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      PreparedStatement ps = connection_
          .prepareStatement("INSERT INTO " + pstestSet.getName()
              + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
      ps.setTimestamp(0, new Timestamp(24534543));
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    
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
   * setTimestamp() - Should throw exception when index is -1.
   **/
  public void Var004() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      PreparedStatement ps = connection_
          .prepareStatement("INSERT INTO " + pstestSet.getName()
              + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
      ps.setTimestamp(0, new Timestamp(7899789));
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    
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
   * setTimestamp() - Should set to SQL NULL when the value is null.
   **/
  public void Var005() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + pstestSet.getName() + " (C_TIMESTAMP) VALUES (?)");
      ps.setTimestamp(1, null);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_TIMESTAMP FROM " + pstestSet.getName());
      rs.next();
      Timestamp check = rs.getTimestamp(1);
      boolean wn = rs.wasNull();
      rs.close();

      assertCondition((check == null) && (wn == true));
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
   * setTimestamp() - Should work with a valid parameter index greater than 1.
   **/
  public void Var006() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + pstestSet.getName() + " (C_KEY, C_TIMESTAMP) VALUES (?, ?)");
      ps.setString(1, "Hola");
      Timestamp t = new Timestamp(9789874);
      ps.setTimestamp(2, t);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_TIMESTAMP FROM " + pstestSet.getName());
      rs.next();
      Timestamp check = rs.getTimestamp(1);
      rs.close();

      assertCondition(check.toString().equals(t.toString()));
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
   * setTimestamp() - Should throw exception when the calendar is null.
   **/
  public void Var007() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc20()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_TIMESTAMP) VALUES (?)");
        ps.setTimestamp(1, new Timestamp(797899789), null);
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
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
   * setTimestamp() - Should throw exception when the parameter is not an input
   * parameter.
   **/
  public void Var008() {
    try {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        notApplicable("JCC permits setting of an output parameter");
        return;
      }
      PreparedStatement ps = connection_.prepareStatement(
          "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
      ps.setTimestamp(2, new Timestamp(2243543));
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  public void testSetFailure(String columnName, Timestamp inTimestamp) {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + pstestSet.getName() + " (" + columnName + ") VALUES (?)");
      ps.setTimestamp(1, inTimestamp);
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    
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
   * setTimestamp() - Set a SMALLINT parameter.
   **/
  public void Var009() {
    testSetFailure("C_SMALLINT", new Timestamp(21744));
  }

  /**
   * setTimestamp() - Set a INTEGER parameter.
   **/
  public void Var010() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + pstestSet.getName() + " (C_INTEGER) VALUES (?)");
      ps.setTimestamp(1, new Timestamp(214748364));
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    
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
   * setTimestamp() - Set a REAL parameter.
   **/
  public void Var011() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + pstestSet.getName() + " (C_REAL) VALUES (?)");
      ps.setTimestamp(1, new Timestamp(21768644));
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    
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
   * setTimestamp() - Set a FLOAT parameter.
   **/
  public void Var012() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + pstestSet.getName() + " (C_FLOAT) VALUES (?)");
      ps.setTimestamp(1, new Timestamp(26786744));
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    
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
   * setTimestamp() - Set a DOUBLE parameter.
   **/
  public void Var013() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    try {
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + pstestSet.getName() + " (C_DOUBLE) VALUES (?)");
      ps.setTimestamp(1, new Timestamp(22567544));
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
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
   * setTimestamp() - Set a DECIMAL parameter.
   **/
  public void Var014() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + pstestSet.getName() + " (C_DECIMAL_105) VALUES (?)");
      ps.setTimestamp(1, new Timestamp(2253444));
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    
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
   * setTimestamp() - Set a NUMERIC parameter.
   **/
  public void Var015() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + pstestSet.getName() + " (C_NUMERIC_50) VALUES (?)");
      ps.setTimestamp(1, new Timestamp(290904));
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    
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
   * setTimestamp() - Set a CHAR(50) parameter.
   **/
  public void Var016() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + pstestSet.getName() + " (C_CHAR_50) VALUES (?)");
      Timestamp t = new Timestamp(2289781);
      ps.setTimestamp(1, t);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_CHAR_50 FROM " + pstestSet.getName());
      rs.next();
      Timestamp check = rs.getTimestamp(1);
      rs.close();

      assertCondition(check.toString().equals(t.toString()));
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
   * setTimestamp() - Set a VARCHAR(50) parameter.
   **/
  public void Var017() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + pstestSet.getName() + " (C_VARCHAR_50) VALUES (?)");
      Timestamp t = new Timestamp(224565414);
      ps.setTimestamp(1, t);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_VARCHAR_50 FROM " + pstestSet.getName());
      rs.next();
      Timestamp check = rs.getTimestamp(1);
      rs.close();

      assertCondition(check.toString().equals(t.toString()));
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
   * setTimestamp() - Set a CLOB parameter.
   **/
  public void Var018() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_CLOB) VALUES (?)");
        ps.setTimestamp(1, new Timestamp(22213444));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
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
   * setTimestamp() - Set a DBCLOB parameter.
   **/
  public void Var019() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_DBCLOB) VALUES (?)");
        ps.setTimestamp(1, new Timestamp(2298724));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
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
   * setTimestamp() - Set a BINARY parameter.
   **/
  public void Var020() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + pstestSet.getName() + " (C_BINARY_20) VALUES (?)");
      ps.setTimestamp(1, new Timestamp(22097044));
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    
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
   * setTimestamp() - Set a VARBINARY parameter.
   **/
  public void Var021() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

      PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
          + pstestSet.getName() + " (C_VARBINARY_20) VALUES (?)");
      ps.setTimestamp(1, new Timestamp(2245614));
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    
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
   * setTimestamp() - Set a BLOB parameter.
   **/
  public void Var022() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + pstestSet.getName() + " (C_BLOB) VALUES (?)");
      ps.setTimestamp(1, new Timestamp(123444));
      ps.executeUpdate();
      ps.close();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    
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
   * setTimestamp() - Set a DATE parameter.
   * 
   * SQL400 - for some reason that I do not understand, the toolbox JDBC driver
   * has a time of 06:00:00.0 for the timestamp they retrieve. I have 00:00:00.0
   * which is what I think I would want.
   **/
  public void Var023() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + pstestSet.getName() + " (C_DATE) VALUES (?)");
      Timestamp t = new Timestamp(8640000000L);
      ps.setTimestamp(1, t);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_DATE FROM " + pstestSet.getName());
      rs.next();
      Timestamp check = rs.getTimestamp(1);
      rs.close();

      assertCondition(check.toString().substring(0, 10)
          .equals(t.toString().substring(0, 10)));
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
   * setTimestamp() - Set a TIME parameter.
   **/
  public void Var024() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    try {
      statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + pstestSet.getName() + " (C_TIME) VALUES (?)");
      Timestamp t = new Timestamp(1198733);
      ps.setTimestamp(1, t);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_TIME FROM " + pstestSet.getName());
      rs.next();
      Time check = rs.getTime(1);
      rs.close();
      assertCondition(t.toString().indexOf(check.toString()) >= 0);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
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
   * setTimestamp() - Set a TIMESTAMP parameter.
   **/
  public void Var025() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    try {
      statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

      PreparedStatement ps = connection_.prepareStatement(
          "INSERT INTO " + pstestSet.getName() + " (C_TIMESTAMP) VALUES (?)");
      Timestamp t = new Timestamp(103464332);
      ps.setTimestamp(1, t);
      ps.executeUpdate();
      ps.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_TIMESTAMP FROM " + pstestSet.getName());
      rs.next();
      Timestamp check = rs.getTimestamp(1);
      rs.close();

      assertCondition(check.toString().equals(t.toString()));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
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
   * setTimestamp() - Set a TIMESTAMP parameter, with a calendar specified.
   **/
  public void Var026() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkJdbc20()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_TIMESTAMP) VALUES (?)");
        Timestamp t = new Timestamp(103454332);
        ps.setTimestamp(1, t, Calendar.getInstance());
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_
            .executeQuery("SELECT C_TIMESTAMP FROM " + pstestSet.getName());
        rs.next();
        Timestamp check = rs.getTimestamp(1);
        rs.close();

        assertCondition(check.toString().equals(t.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
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
   * setTimestamp() - Set a DATALINK parameter.
   **/
  public void Var027() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if ((getDriver() == JDTestDriver.DRIVER_NATIVE) && (getJdbcLevel() <= 2)) {
      notApplicable("Native driver pre-JDBC 3.0");
      return;
    }
    if (checkDatalinkSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_DATALINK) VALUES (?)");
        ps.setTimestamp(1, new Timestamp(265454));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
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
   * setTimestamp() - Set a DISTINCT parameter.
   **/
  public void Var028() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkLobSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_DISTINCT) VALUES (?)");
        ps.setTimestamp(1, new Timestamp(2246454));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
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
   * setTimestamp() - Set a BIGINT parameter.
   **/
  public void Var029() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkBigintSupport()) {
      try {
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + pstestSet.getName() + " (C_BIGINT) VALUES (?)");
        ps.setTimestamp(1, new Timestamp(214748364));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
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
   * setTimestamp() - Set a parameter in a statement that comes from the package
   * cache.
   **/
  public void Var030() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    try {
      String insert = "INSERT INTO " + pstestSet.getName()
          + " (C_TIMESTAMP) VALUES (?)";

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
      Timestamp t = new Timestamp(234548000);
      ps.setTimestamp(1, t);
      ps.executeUpdate();
      ps.close();
      c2.close();

      ResultSet rs = statement_
          .executeQuery("SELECT C_TIMESTAMP FROM " + pstestSet.getName());
      rs.next();
      Timestamp check = rs.getTimestamp(1);
      rs.close();

      assertCondition(check.getTime() == t.getTime());
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
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
   * setTimestamp() - Set a DECFLOAT parameter.
   **/
  public void Var031() {
    if (checkDecFloatSupport()) {
      JDSerializeFile pstestSetdfp = null;
      try {
        pstestSetdfp = JDPSTest.getPstestSetdfp16(connection_);
        String tablename16=pstestSetdfp.getName();
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + tablename16 + " (C_BIGINT) VALUES (?)");
        ps.setTimestamp(1, new Timestamp(214748364));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      } finally {
        if (pstestSetdfp != null) {
          try {
            pstestSetdfp.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * setTimestamp() - Set a DECFLOAT parameter.
   **/
  public void Var032() {
    if (checkDecFloatSupport()) {
      JDSerializeFile pstestSetdfp = null;
      try {
        pstestSetdfp = JDPSTest.getPstestSetdfp34(connection_);
        String tablename34=pstestSetdfp.getName();
        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + tablename34 + " (C_BIGINT) VALUES (?)");
        ps.setTimestamp(1, new Timestamp(214748364));
        ps.executeUpdate();
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      } finally {
        if (pstestSetdfp != null) {
          try {
            pstestSetdfp.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * setTimestamp() - Set an XML parameter.
   **/
  public void Var033() {
    if (checkXmlSupport()) {
      JDSerializeFile pstestSetxml = null;
      try {
        pstestSetxml = JDPSTest.getSerializeFile(connection_, JDPSTest.SETXML);
        String tablename = pstestSetxml.getName(); 
        PreparedStatement ps = connection_.prepareStatement(
            "INSERT INTO " + tablename + " VALUES (?)");
        try {
          ps.setTimestamp(1, new Timestamp(214748364));
          ps.executeUpdate();
          ps.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      } catch (Exception e) {
        failed(e, "unexpected exception");
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

  /**
   * setTimestamp() - Set a timestamp using GMT timezone that does not exist in
   * a timezone with daylight savings times. Tests fix for toolbox CPS 8WDKCS.
   **/
  public void Var034() {

    // Fix in V7R1 is in SI47828
    // Fix will be in next PTF for other releases.
    if ((getDriver() == JDTestDriver.DRIVER_NATIVE)
        && (getDriverFixLevel() < 47828)) {
      notApplicable("daylight savings time not fixed yet in native driver");
      return;
    }

    String[][] testString = {
        { "1299983537000", "2011-03-13-02.32.17.000000" }, };
    String info = " -- added by toolbox 7/30/2012 for CPS 8WDKCS";
    TimeZone defaultTimeZone = TimeZone.getDefault();
    try {
      StringBuffer sb = new StringBuffer();
      sb.append("\n-- Default Timezone is " + defaultTimeZone + "\n");
      TimeZone.setDefault(TimeZone.getTimeZone("America/Chicago"));
      sb.append("-- Timezone is now     " + TimeZone.getDefault() + "\n");
      PreparedStatement ps = connection_.prepareStatement(
          "SELECT CAST(CAST(? AS TIMESTAMP) AS VARCHAR(80)) FROM SYSIBM.SYSDUMMY1");

      try {
        boolean passed = true;
        Calendar gmtCalendar = Calendar
            .getInstance(TimeZone.getTimeZone("GMT"));
        for (int i = 0; i < testString.length; i++) {
          ps.setTimestamp(1, new Timestamp(Long.parseLong(testString[i][0])),
              gmtCalendar);
          ResultSet rs = ps.executeQuery();
          rs.next();
          String output = rs.getString(1);
          if (!testString[i][1].equals(output)) {
            passed = false;
            sb.append(" -- FAILED:  For " + testString[i][0] + "\n");
            sb.append(" --          Got " + output + "\n");
            sb.append(" --          sb  " + testString[i][1] + "\n");

          }

        }
        assertCondition(passed, sb.toString() + info);

      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    } catch (Exception e) {
      failed(e, "unexpected exception");
    }
    TimeZone.setDefault(defaultTimeZone);

  }

  public void testTimestampX(String tableName, String tableDefinition,
      String[] values, String info) {
    if (checkTimestamp12Support()) {
      StringBuffer sb = new StringBuffer();
      boolean passed = true;
      String sql = "";
      try {

        Statement stmt = connection_.createStatement();
        try {
          sql = "DROP TABLE " + tableName;
          sb.append("Executing " + sql + "\n");
          stmt.executeUpdate(sql);
        } catch (Exception e) {
          sb.append("Execute failed -- ok \n");
        }
        sql = "CREATE TABLE " + tableName + " " + tableDefinition;
        sb.append("Executing " + sql + "\n");
        stmt.executeUpdate(sql);
        sql = "INSERT INTO " + tableName + " VALUES(?)";
        sb.append("Preparing " + sql + "\n");
        PreparedStatement ps = connection_.prepareStatement(sql);
        for (int i = 0; i < values.length; i++) {
          Timestamp ts;
          String inputString = values[i];
          if (inputString.length() <= 29) {
            ts = Timestamp.valueOf(inputString);
          } else {
            if (nativeDriver) {
              ts = (Timestamp) JDReflectionUtil.callStaticMethod_O(
                  "com.ibm.db2.jdbc.app.DB2JDBCTimestamp", "valueOf",
                  inputString);
            } else {
              ts = AS400JDBCTimestamp.valueOf(inputString);
            }
          }
          sb.append("Inserting " + ts + "\n");
          ps.setTimestamp(1, ts);
          ps.executeUpdate();
        }
        ps.close();

        sql = "select * from " + tableName;
        sb.append("executing " + sql + "\n");
        ResultSet rs = stmt.executeQuery(sql);
        for (int i = 0; i < values.length; i++) {
          rs.next();
          Timestamp ts = rs.getTimestamp(1);
          String expectedString = values[i];
          if (!expectedString.equals(ts.toString())) {
            passed = false;
            sb.append("\nFAILED:  got " + ts.toString() + "\n    expected "
                + expectedString + "\n");
          }
        }
        rs.close();
        stmt.executeUpdate("DROP TABLE " + tableName);
        stmt.close();
        assertCondition(passed, sb.toString() + " " + info);

      } catch (Exception e) {
        failed(e, "unexpected exception " + sb.toString());
      }
    }
  }

  /* Test timestamp */

  public void Var035() {
    String[] values = { "1998-11-18 03:13:47.123456",
        "1998-11-18 03:13:43.12345", "1998-11-18 03:13:42.1234",
        "1998-11-18 03:13:42.123", "1998-11-18 03:13:42.12",
        "1998-11-18 03:13:42.1", "1998-11-18 03:13:42.0", };
    testTimestampX(collection_ + ".JDPSST6", "(C1 TIMESTAMP(6))", values,
        " -- added 3/26/2013");
  }

  public void Var036() {
    String[] values = { "1998-11-18 03:13:47.0", "1998-11-18 03:13:43.0",
        "1998-11-18 03:13:42.0", };
    testTimestampX(collection_ + ".JDPSST0", "(C1 TIMESTAMP(0))", values,
        " -- added 3/26/2013");
  }

  public void Var037() {
    String[] values = { "1998-11-18 03:13:47.0", "1998-11-18 03:13:43.1",
        "1998-11-18 03:13:42.2", };
    testTimestampX(collection_ + ".JDPSST1", "(C1 TIMESTAMP(1))", values,
        " -- added 3/26/2013");
  }

  public void Var038() {
    String[] values = { "1998-11-18 03:13:47.0", "1998-11-18 03:13:43.1",
        "1998-11-18 03:13:42.9", "1998-11-18 03:13:42.01",
        "1998-11-18 03:13:42.99", };
    testTimestampX(collection_ + ".JDPSST2", "(C1 TIMESTAMP(2))", values,
        " -- added 3/26/2013");
  }

  public void Var039() {
    String[] values = { "1998-11-18 03:13:47.0", "1998-11-18 03:13:43.01",
        "1998-11-18 03:13:42.09", "1998-11-18 03:13:42.001",
        "1998-11-18 03:13:42.009", };
    testTimestampX(collection_ + ".JDPSST3", "(C1 TIMESTAMP(3))", values,
        " -- added 3/26/2013");
  }

  public void Var040() {
    String[] values = { "1998-11-18 03:13:47.0", "1998-11-18 03:13:43.01",
        "1998-11-18 03:13:42.09", "1998-11-18 03:13:43.001",
        "1998-11-18 03:13:42.009", "1998-11-18 03:13:43.0001",
        "1998-11-18 03:13:42.0009", };
    testTimestampX(collection_ + ".JDPSST4", "(C1 TIMESTAMP(4))", values,
        " -- added 3/26/2013");
  }

  public void Var041() {
    String[] values = { "1998-11-18 03:13:47.0", "1998-11-18 03:13:43.01",
        "1998-11-18 03:13:42.09", "1998-11-18 03:13:43.001",
        "1998-11-18 03:13:42.009", "1998-11-18 03:13:43.0001",
        "1998-11-18 03:13:42.0009", "1998-11-18 03:13:43.00001",
        "1998-11-18 03:13:42.00009", };
    testTimestampX(collection_ + ".JDPSST5", "(C1 TIMESTAMP(5))", values,
        " -- added 3/26/2013");
  }

  public void Var042() {
    String[] values = { "1998-11-18 03:13:47.0", "1998-11-18 03:13:43.01",
        "1998-11-18 03:13:42.09", "1998-11-18 03:13:43.001",
        "1998-11-18 03:13:42.009", "1998-11-18 03:13:43.0001",
        "1998-11-18 03:13:42.0009", "1998-11-18 03:13:43.00001",
        "1998-11-18 03:13:42.00009", "1998-11-18 03:13:43.000001",
        "1998-11-18 03:13:42.000009", };
    testTimestampX(collection_ + ".JDPSST6", "(C1 TIMESTAMP(6))", values,
        " -- added 3/26/2013");
  }

  public void Var043() {
    String[] values = { "1998-11-18 03:13:47.0", "1998-11-18 03:13:43.01",
        "1998-11-18 03:13:42.09", "1998-11-18 03:13:43.001",
        "1998-11-18 03:13:42.009", "1998-11-18 03:13:43.0001",
        "1998-11-18 03:13:42.0009", "1998-11-18 03:13:43.00001",
        "1998-11-18 03:13:42.00009", "1998-11-18 03:13:43.000001",
        "1998-11-18 03:13:42.000009", "1998-11-18 03:13:43.0000001",
        "1998-11-18 03:13:42.0000009", };
    testTimestampX(collection_ + ".JDPSST7", "(C1 TIMESTAMP(7))", values,
        " -- added 3/26/2013");
  }

  public void Var044() {
    String[] values = { "1998-11-18 03:13:47.0", "1998-11-18 03:13:43.01",
        "1998-11-18 03:13:42.09", "1998-11-18 03:13:43.001",
        "1998-11-18 03:13:42.009", "1998-11-18 03:13:43.0001",
        "1998-11-18 03:13:42.0009", "1998-11-18 03:13:43.00001",
        "1998-11-18 03:13:42.00009", "1998-11-18 03:13:43.000001",
        "1998-11-18 03:13:42.000009", "1998-11-18 03:13:43.0000001",
        "1998-11-18 03:13:42.0000009", "1998-11-18 03:13:43.00000001",
        "1998-11-18 03:13:42.00000009", "1998-11-18 03:13:42.12345678", };
    testTimestampX(collection_ + ".JDPSST8", "(C1 TIMESTAMP(8))", values,
        " -- added 3/26/2013");
  }

  public void Var045() {
    String[] values = { "1998-11-18 03:13:47.0", "1998-11-18 03:13:43.01",
        "1998-11-18 03:13:42.09", "1998-11-18 03:13:43.001",
        "1998-11-18 03:13:42.009", "1998-11-18 03:13:43.0001",
        "1998-11-18 03:13:42.0009", "1998-11-18 03:13:43.00001",
        "1998-11-18 03:13:42.00009", "1998-11-18 03:13:43.000001",
        "1998-11-18 03:13:42.000009", "1998-11-18 03:13:43.0000001",
        "1998-11-18 03:13:42.0000009", "1998-11-18 03:13:43.00000001",
        "1998-11-18 03:13:42.00000009", "1998-11-18 03:13:43.000000001",
        "1998-11-18 03:13:42.000000009", "1998-11-18 03:13:42.123456789", };
    testTimestampX(collection_ + ".JDPSST9", "(C1 TIMESTAMP(9))", values,
        " -- added 3/26/2013");
  }

  public void Var046() {
    String[] values = { "1998-11-18 03:13:47.0", "1998-11-18 03:13:43.01",
        "1998-11-18 03:13:42.09", "1998-11-18 03:13:43.001",
        "1998-11-18 03:13:42.009", "1998-11-18 03:13:43.0001",
        "1998-11-18 03:13:42.0009", "1998-11-18 03:13:43.00001",
        "1998-11-18 03:13:42.00009", "1998-11-18 03:13:43.000001",
        "1998-11-18 03:13:42.000009", "1998-11-18 03:13:43.0000001",
        "1998-11-18 03:13:42.0000009", "1998-11-18 03:13:43.00000001",
        "1998-11-18 03:13:42.00000009", "1998-11-18 03:13:43.000000001",
        "1998-11-18 03:13:42.000000009", "1998-11-18 03:13:43.0000000001",
        "1998-11-18 03:13:42.0000000009", "1998-11-18 03:13:42.1234567891", };
    testTimestampX(collection_ + ".JDPSST10", "(C1 TIMESTAMP(10))", values,
        " -- added 3/26/2013");
  }

  public void Var047() {
    String[] values = { "1998-11-18 03:13:47.0", "1998-11-18 03:13:43.01",
        "1998-11-18 03:13:42.09", "1998-11-18 03:13:43.001",
        "1998-11-18 03:13:42.009", "1998-11-18 03:13:43.0001",
        "1998-11-18 03:13:42.0009", "1998-11-18 03:13:43.00001",
        "1998-11-18 03:13:42.00009", "1998-11-18 03:13:43.000001",
        "1998-11-18 03:13:42.000009", "1998-11-18 03:13:43.0000001",
        "1998-11-18 03:13:42.0000009", "1998-11-18 03:13:43.00000001",
        "1998-11-18 03:13:42.00000009", "1998-11-18 03:13:43.000000001",
        "1998-11-18 03:13:42.000000009", "1998-11-18 03:13:43.0000000001",
        "1998-11-18 03:13:42.0000000009", "1998-11-18 03:13:43.00000000001",
        "1998-11-18 03:13:42.00000000009", "1998-11-18 03:13:42.12345678901", };
    testTimestampX(collection_ + ".JDPSST11", "(C1 TIMESTAMP(11))", values,
        " -- added 3/26/2013");
  }

  public void Var048() {
    String[] values = { "1998-11-18 03:13:47.0", "1998-11-18 03:13:43.01",
        "1998-11-18 03:13:42.09", "1998-11-18 03:13:43.001",
        "1998-11-18 03:13:42.009", "1998-11-18 03:13:43.0001",
        "1998-11-18 03:13:42.0009", "1998-11-18 03:13:43.00001",
        "1998-11-18 03:13:42.00009", "1998-11-18 03:13:43.000001",
        "1998-11-18 03:13:42.000009", "1998-11-18 03:13:43.0000001",
        "1998-11-18 03:13:42.0000009", "1998-11-18 03:13:43.00000001",
        "1998-11-18 03:13:42.00000009", "1998-11-18 03:13:43.00000001",
        "1998-11-18 03:13:42.000000009", "1998-11-18 03:13:43.0000000001",
        "1998-11-18 03:13:42.0000000009", "1998-11-18 03:13:43.00000000001",
        "1998-11-18 03:13:42.00000000009", "1998-11-18 03:13:43.000000000001",
        "1998-11-18 03:13:42.000000000009",
        "1998-11-18 03:13:42.123456789012", };
    testTimestampX(collection_ + ".JDPSST12", "(C1 TIMESTAMP(12))", values,
        " -- added 3/26/2013");
  }

  /**
   * setTimestamp() - Set a BOOLEAN parameter.
   **/
  public void Var049() {
    if (checkBooleanSupport()) {
      testSetFailure("C_BOOLEAN", new Timestamp(21744));
    }
  }

}
