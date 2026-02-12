///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSSetObject4SQLType.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.PS;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDLobTest;
import test.JDPSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestcase;
import test.JD.JDSerializeFile;

/**
 * Testcase JDPSSetObject4SQLType. This tests the following method of the JDBC
 * PreparedStatement class (4 parameters) with SQLType
 * 
 * <ul>
 * <li>setObject(int,Object,SQLType,int)
 * </ul>
 **/
@SuppressWarnings("deprecation")
public class JDPSSetObject4SQLType extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDPSSetObject4SQLType";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDPSTest.main(newArgs); 
   }

  // Private data.
  private Statement statement_;

  private Connection connectionCommaSeparator_;

  /**
   * Constructor.
   **/
  public JDPSSetObject4SQLType(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDPSSetObject4SQLType", namesAndVars, runMode,
        fileOutputStream, password);
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
    connectionCommaSeparator_ = testDriver_.getConnection(url
        + ";decimal separator=,");
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

    connectionCommaSeparator_.close();
  }

  /*
   * Create the SQLType corresponding to Type.
   */
  private Object getSQLType(int typesNumber) throws Exception {
    return JDReflectionUtil.callStaticMethod_O("java.sql.JDBCType", "valueOf",
        typesNumber);
  }

  /**
   * setObject() - Should throw exception when the prepared statement is closed.
   **/
  public void Var001() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_NUMERIC_105) VALUES (?)");
        ps.close();
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(2.3),
            getSQLType(Types.NUMERIC), 1);
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
   * setObject() - Should throw exception when an invalid index is specified.
   **/
  public void Var002() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName()
            + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 100, Integer.valueOf(4),
            getSQLType(Types.INTEGER), 0);
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
   * setObject() - Should throw exception when index is 0.
   **/
  public void Var003() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName()
            + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 0, "Hi Mom",
            getSQLType(Types.VARCHAR), 0);
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
   * setObject() - Should throw exception when index is -1.
   **/
  public void Var004() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName()
            + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", -1, "Yo Dad",
            getSQLType(Types.VARCHAR), 0);
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
   * setObject() - Should work with a valid parameter index greater than 1.
   **/
  public void Var005() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_KEY, C_NUMERIC_105) VALUES (?, ?)");
        ps.setString(1, "Test");
        JDReflectionUtil.callMethod_V(ps, "setObject", 2,
            new BigDecimal("4.3"), getSQLType(Types.NUMERIC), 1);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_NUMERIC_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 1);
        rs.close();

        assertCondition(check.doubleValue() == 4.3);
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
   * setObject() - Should set to SQL NULL when the object is null.
   **/
  public void Var006() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_NUMERIC_105) VALUES (?)");

        Class<?>[] argClasses = new Class[4];
        argClasses[0] = Integer.TYPE;
        argClasses[1] = Class.forName("java.lang.Object");
        argClasses[2] = Class.forName("java.sql.SQLType");
        argClasses[3] = Integer.TYPE;
        Object[] args = new Object[4];
        args[0] = Integer.valueOf(1);
        args[1] = null;
        args[2] = getSQLType(Types.NUMERIC);
        args[3] = Integer.valueOf(1);
        JDReflectionUtil.callMethod_V(ps, "setObject", argClasses, args);

        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_NUMERIC_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 0);
        boolean wn = rs.wasNull();
        rs.close();

        assertCondition((check == null) && (wn == true));
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
   * setObject() - Should throw exception when the type is invalid.
   **/
  public void Var007() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (isToolboxDriver()) {
      // toolbox
      notApplicable("Toolbox does not use col types");
      return;
    }
    if (checkJdbc42()) {
      try {
          notApplicable("Native doesn't use col types ");
          /*
           * PreparedStatement ps = connection_.prepareStatement (
           * "INSERT INTO " + pstestSet.getName() +
           * " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
           * JDReflectionUtil.callMethod_V(ps, "setObject", 1, Integer.valueOf(4),
           * 4848484, 0); failed ("Didn't throw SQLException");
           */
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
   * setObject() - Should throw exception when the scale is invalid.
   **/
  public void Var008() {
if (checkJdbc42()) {
  JDSerializeFile pstestSet = null;
  PreparedStatement ps = null; 
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
        statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

        ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName()
            + " (C_INTEGER, C_SMALLINT, C_VARCHAR_50) VALUES (?, ?, ?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, Integer.valueOf(4),
            getSQLType(Types.INTEGER), -1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      } finally {
       try {
          if (pstestSet != null)
            pstestSet.close();
          if (ps != null) ps.close(); 
        } catch (SQLException e) {
          e.printStackTrace();
        }

      }
    }
  }

  /**
   * setObject() - Should throw exception when the parameter is not an input
   * parameter.
   **/
  public void Var009() {
    if (checkJdbc42()) {
      try {
        PreparedStatement ps = connection_.prepareStatement("CALL "
            + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 2, Integer.valueOf(3),
            getSQLType(Types.INTEGER), 0);
        ps.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Should throw exception when the parameter is not anything
   * close to being a JDBC-style type.
   **/
  @SuppressWarnings("rawtypes")
  public void Var010() {
    JDSerializeFile pstestSet = null;
    PreparedStatement ps = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
      if (checkJdbc42()) {
        try {
          statement_.executeUpdate ("DELETE FROM " + pstestSet.getName());

          ps = connection_.prepareStatement("INSERT INTO " + pstestSet.getName() + " (C_SMALLINT) VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, new Hashtable(), getSQLType(Types.SMALLINT), 0);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      {
        try {
          if (pstestSet != null)
            pstestSet.close();
          if (ps != null)
            ps.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * setObject() - Set a SMALLINT parameter.
   **/
  public void Var011() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_SMALLINT) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1,
            Short.valueOf((short) -33), getSQLType(Types.SMALLINT), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_SMALLINT FROM "
            + pstestSet.getName());
        rs.next();
        short check = rs.getShort(1);
        rs.close();

        assertCondition(check == -33);
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
   * setObject() - Set a SMALLINT parameter with a nonzero scale specified.
   **/
  public void Var012() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_SMALLINT) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1,
            Short.valueOf((short) 76), getSQLType(Types.SMALLINT), 2);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_SMALLINT FROM "
            + pstestSet.getName());
        rs.next();
        short check = rs.getShort(1);
        rs.close();

        assertCondition(check == 76);
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
   * setObject() - Set an INTEGER parameter.
   **/
  public void Var013() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_INTEGER) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, Integer.valueOf(-595),
            getSQLType(Types.INTEGER), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_INTEGER FROM "
            + pstestSet.getName());
        rs.next();
        int check = rs.getInt(1);
        rs.close();

        assertCondition(check == -595);
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
   * setObject() - Set an INTEGER parameter with a nonzero scale.
   **/
  public void Var014() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_INTEGER) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, Integer.valueOf(-595),
            getSQLType(Types.INTEGER), 4);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_INTEGER FROM "
            + pstestSet.getName());
        rs.next();
        int check = rs.getInt(1);
        rs.close();

        assertCondition(check == -595);
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
   * setObject() - Set an REAL parameter.
   **/
  public void Var015() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_REAL) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, Float.valueOf(-4.385f),
            getSQLType(Types.REAL), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_REAL FROM "
            + pstestSet.getName());
        rs.next();
        float check = rs.getFloat(1);
        rs.close();

        assertCondition(check == -4.385f);
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
   * setObject() - Set an REAL parameter with nonzero scale.
   **/
  public void Var016() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_REAL) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, Float.valueOf(-4.3235f),
            getSQLType(Types.REAL), 3);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_REAL FROM "
            + pstestSet.getName());
        rs.next();
        float check = rs.getFloat(1);
        rs.close();

        assertCondition(check == -4.3235f);
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
   * setObject() - Set an FLOAT parameter.
   **/
  public void Var017() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_FLOAT) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, Float.valueOf(-3.42f),
            getSQLType(Types.DOUBLE), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_FLOAT FROM "
            + pstestSet.getName());
        rs.next();
        float check = rs.getFloat(1);
        rs.close();

        assertCondition(check == -3.42f);
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
   * setObject() - Set an FLOAT parameter with a nonzero scale.
   **/
  public void Var018() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_FLOAT) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, Float.valueOf(3.43212f),
            getSQLType(Types.DOUBLE), 3);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_FLOAT FROM "
            + pstestSet.getName());
        rs.next();
        float check = rs.getFloat(1);
        rs.close();

        assertCondition(check == 3.43212f);
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
   * setObject() - Set an DOUBLE parameter.
   **/
  public void Var019() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_DOUBLE) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, Double.valueOf(-314.159),
            getSQLType(Types.DOUBLE), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_DOUBLE FROM "
            + pstestSet.getName());
        rs.next();
        double check = rs.getDouble(1);
        rs.close();

        assertCondition(check == -314.159);
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
   * setObject() - Set an DOUBLE parameter with a nonzero scale.
   **/
  public void Var020() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_DOUBLE) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, Double.valueOf(3159.343),
            getSQLType(Types.DOUBLE), 2);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_DOUBLE FROM "
            + pstestSet.getName());
        rs.next();
        double check = rs.getDouble(1);
        rs.close();

        assertCondition(check == 3159.343);
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
   * setObject() - Set an DECIMAL parameter with scale 0.
   **/
  public void Var021() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_DECIMAL_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
            "32.234"), getSQLType(Types.DECIMAL), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_DECIMAL_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 3);
        rs.close();

        assertCondition(check.doubleValue() == 32);
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
   * setObject() - Set an DECIMAL parameter with scale less than the number.
   **/
  public void Var022() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_DECIMAL_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
            "-32.2319"), getSQLType(Types.DECIMAL), 2);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_DECIMAL_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 2);
        rs.close();

        assertCondition(check.doubleValue() == -32.23);
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
   * setObject() - Set an DECIMAL parameter with scale equal to the number.
   **/
  public void Var023() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_DECIMAL_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
            "822.3187"), getSQLType(Types.DECIMAL), 4);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_DECIMAL_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 4);
        rs.close();

        assertCondition(check.doubleValue() == 822.3187);
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
   * setObject() - Set an DECIMAL parameter with scale greater than the number.
   **/
  public void Var024() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_DECIMAL_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
            "-12.87"), getSQLType(Types.DECIMAL), 4);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_DECIMAL_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 2);
        rs.close();

        assertCondition(check.doubleValue() == -12.87);
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
   * setObject() - Set an NUMERIC parameter with scale 0.
   **/
  public void Var025() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_NUMERIC_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
            "-432.34"), getSQLType(Types.NUMERIC), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_NUMERIC_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 0);
        rs.close();

        assertCondition(check.doubleValue() == -432);
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
   * setObject() - Set an NUMERIC parameter with scale less than the number.
   **/
  public void Var026() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_NUMERIC_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
            "52.219"), getSQLType(Types.NUMERIC), 1);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_NUMERIC_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 1);
        rs.close();

        assertCondition(check.doubleValue() == 52.2);
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
   * setObject() - Set an NUMERIC parameter with scale equal to the number.
   **/
  public void Var027() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_NUMERIC_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
            "82.31387"), getSQLType(Types.NUMERIC), 5);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_NUMERIC_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 5);
        rs.close();

        assertCondition(check.doubleValue() == 82.31387);
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
   * setObject() - Set an NUMERIC parameter with scale greater than the number.
   **/
  public void Var028() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_NUMERIC_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
            "32.87"), getSQLType(Types.NUMERIC), 4);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_NUMERIC_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 2);
        rs.close();

        assertCondition(check.doubleValue() == 32.87);
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
   * setObject() - Set a CHAR(50) parameter.
   **/
  public void Var029() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_CHAR_50) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, "Dell Rapids",
            getSQLType(Types.CHAR), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_CHAR_50 FROM "
            + pstestSet.getName());
        rs.next();
        String check = rs.getString(1);
        rs.close();

        assertCondition(check
            .equals("Dell Rapids                                       "));
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
   * setObject() - Set a CHAR(50) parameter with a nonzero scale.
   **/
  public void Var030() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_CHAR_50) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, "Harrisburg",
            getSQLType(Types.CHAR), 4);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_CHAR_50 FROM "
            + pstestSet.getName());
        rs.next();
        String check = rs.getString(1);
        rs.close();

        assertCondition(check
            .equals("Harrisburg                                        "));
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
   * setObject() - Set an VARCHAR parameter.
   **/
  public void Var031() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_VARCHAR_50) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, "Aberdeen",
            getSQLType(Types.VARCHAR), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_VARCHAR_50 FROM "
            + pstestSet.getName());
        rs.next();
        String check = rs.getString(1);
        rs.close();

        assertCondition(check.equals("Aberdeen"));
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
   * setObject() - Set an VARCHAR parameter with a nonzero scale.
   **/
  public void Var032() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_VARCHAR_50) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, "Desmet",
            getSQLType(Types.VARCHAR), 4);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_VARCHAR_50 FROM "
            + pstestSet.getName());
        rs.next();
        String check = rs.getString(1);
        rs.close();

        assertCondition(check.equals("Desmet"));
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
   * setObject() - Set a CLOB parameter.
   **/
  public void Var033() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      if (checkLobSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName() + " (C_CLOB) VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1,
              new JDLobTest.JDTestClob("Milbank"), getSQLType(Types.CLOB), 0);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_CLOB FROM "
              + pstestSet.getName());
          rs.next();
          Clob check = rs.getClob(1);
          String answer = check.getSubString(1, (int) check.length());
          rs.close(); 
          ps.close(); 
          assertCondition(answer.equals(
              "Milbank")); // @D1C
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set a CLOB parameter with a nonzero scale.
   **/
  public void Var034() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      if (checkLobSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName() + " (C_CLOB) VALUES (?)");
          ps.setObject(1, new JDLobTest.JDTestClob("Garretson"), Types.CLOB, 4);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_CLOB FROM "
              + pstestSet.getName());
          rs.next();
          Clob check = rs.getClob(1);

          String string = check.getSubString(1, (int) check.length());
          rs.close(); 
          if (string.equals("Garretson"))
            succeeded();
          else
            failed("Strings did not match. Garretson != " + string);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set a DBCLOB parameter.
   **/
  public void Var035() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      if (checkLobSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName() + " (C_DBCLOB) VALUES (?)");
          ps.setObject(1, new JDLobTest.JDTestClob("Tea"), Types.CLOB, 0);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_DBCLOB FROM "
              + pstestSet.getName());
          rs.next();
          Clob check = rs.getClob(1);

          String string = check.getSubString(1, (int) check.length());
          rs.close(); 
          if (string.equals("Tea"))
            succeeded();
          else
            failed("Strings did not match. Tea != " + string);

        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set a DBCLOB parameter with a nonzero scale.
   **/
  public void Var036() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      if (checkLobSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName() + " (C_DBCLOB) VALUES (?)");
          ps.setObject(1, new JDLobTest.JDTestClob("Lennox"), Types.CLOB, 20);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_DBCLOB FROM "
              + pstestSet.getName());
          rs.next();
          Clob check = rs.getClob(1);

          String string = check.getSubString(1, (int) check.length());
          rs.close(); 
          if (string.equals("Lennox"))
            succeeded();
          else
            failed("Strings did not match. Lennox != " + string);

        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set a BINARY parameter.
   **/
  public void Var037() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_BINARY_20) VALUES (?)");
        byte[] b = { (byte) 32, (byte) 9, (byte) -1, (byte) -11, (byte) 12 };
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, b,
            getSQLType(Types.BINARY), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_BINARY_20 FROM "
            + pstestSet.getName());
        rs.next();
        byte[] check = rs.getBytes(1);
        rs.close();

        byte[] b2 = new byte[20];
        System.arraycopy(b, 0, b2, 0, b.length);
        assertCondition(areEqual(check, b2));
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
   * setObject() - Set a BINARY parameter with a nonzero scale.
   **/
  public void Var038() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_BINARY_20) VALUES (?)");
        byte[] b = { (byte) 9, (byte) -1, (byte) -11, (byte) 12 };
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, b,
            getSQLType(Types.BINARY), 7);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_BINARY_20 FROM "
            + pstestSet.getName());
        rs.next();
        byte[] check = rs.getBytes(1);
        rs.close();

        byte[] b2 = new byte[20];
        System.arraycopy(b, 0, b2, 0, b.length);
        assertCondition(areEqual(check, b2));
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
   * setObject() - Set a VARBINARY parameter.
   **/
  public void Var039() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_VARBINARY_20) VALUES (?)");
        byte[] b = { (byte) -13, (byte) 32, (byte) 0, (byte) -1, (byte) -11,
            (byte) 99 };
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, b,
            getSQLType(Types.VARBINARY), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_VARBINARY_20 FROM "
            + pstestSet.getName());
        rs.next();
        byte[] check = rs.getBytes(1);
        rs.close();

        assertCondition(areEqual(check, b));
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
   * setObject() - Set a VARBINARY parameter with a nonzero scale.
   **/
  public void Var040() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_VARBINARY_20) VALUES (?)");
        byte[] b = { (byte) -13, (byte) 32, (byte) 100, (byte) -1, (byte) -11,
            (byte) 99 };
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, b,
            getSQLType(Types.VARBINARY), 3);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_VARBINARY_20 FROM "
            + pstestSet.getName());
        rs.next();
        byte[] check = rs.getBytes(1);
        rs.close();

        assertCondition(areEqual(check, b));
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
   * setObject() - Set a BLOB parameter.
   **/
  public void Var041() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      if (checkLobSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName() + " (C_BLOB) VALUES (?)");
          byte[] b = new byte[] { (byte) 12, (byte) 94, (byte) -1, (byte) 0 };
          JDReflectionUtil.callMethod_V(ps, "setObject", 1,
              new JDLobTest.JDTestBlob(b), getSQLType(Types.BLOB), 0);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_BLOB FROM "
              + pstestSet.getName());
          rs.next();
          Blob check = rs.getBlob(1);
           byte[] answer = check.getBytes(1, (int) check.length());
          // rs.close (); // @F1D
          rs.close(); 
          assertCondition(areEqual(answer, b)); // @D1C
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set a BLOB parameter with a nonzero scale.
   **/
  public void Var042() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      if (checkLobSupport()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName() + " (C_BLOB) VALUES (?)");
          byte[] b = new byte[] { (byte) 94, (byte) -1, (byte) 0 };
          JDReflectionUtil.callMethod_V(ps, "setObject", 1,
              new JDLobTest.JDTestBlob(b), getSQLType(Types.BLOB), 4);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_BLOB FROM "
              + pstestSet.getName());
          rs.next();
          Blob check = rs.getBlob(1);
          byte[] answer = check.getBytes(1, (int) check.length());
          // rs.close (); // @F1D
          rs.close(); 
          assertCondition(areEqual(answer, b)); // @D1C
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set a DATE parameter.
   **/
  public void Var043() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_DATE) VALUES (?)");
        Date d = new Date(3423499);
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, d,
            getSQLType(Types.DATE), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_DATE FROM "
            + pstestSet.getName());
        rs.next();
        Date check = rs.getDate(1);
        rs.close();

        assertCondition(d.toString().equals(check.toString()));
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
   * setObject() - Set a DATE parameter with nonzero scale.
   **/
  public void Var044() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_DATE) VALUES (?)");
        Date d = new Date(142333499);
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, d,
            getSQLType(Types.DATE), 4);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_DATE FROM "
            + pstestSet.getName());
        rs.next();
        Date check = rs.getDate(1);
        rs.close();

        assertCondition(d.toString().equals(check.toString()));
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
   * setObject() - Set a TIME parameter.
   **/
  public void Var045() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_TIME) VALUES (?)");
        Time t = new Time(345333);
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, t,
            getSQLType(Types.TIME), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_TIME FROM "
            + pstestSet.getName());
        rs.next();
        Time check = rs.getTime(1);
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
   * setObject() - Set a TIME parameter with a nonzero scale.
   **/
  public void Var046() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_TIME) VALUES (?)");
        Time t = new Time(344311133);
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, t,
            getSQLType(Types.TIME), 8);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_TIME FROM "
            + pstestSet.getName());
        rs.next();
        Time check = rs.getTime(1);
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
   * setObject() - Set a TIMESTAMP parameter.
   **/
  public void Var047() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_TIMESTAMP) VALUES (?)");
        Timestamp ts = new Timestamp(3434230);
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, ts,
            getSQLType(Types.TIMESTAMP), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_TIMESTAMP FROM "
            + pstestSet.getName());
        rs.next();
        Timestamp check = rs.getTimestamp(1);
        rs.close();

        assertCondition(check.toString().equals(ts.toString()));
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
   * setObject() - Set a TIMESTAMP parameter with a nonzero scale.
   **/
  public void Var048() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_TIMESTAMP) VALUES (?)");
        Timestamp ts = new Timestamp(934230);
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, ts,
            getSQLType(Types.TIMESTAMP), 3);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_TIMESTAMP FROM "
            + pstestSet.getName());
        rs.next();
        Timestamp check = rs.getTimestamp(1);
        rs.close();

        assertCondition(check.toString().equals(ts.toString()));
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
   * setObject() - Set a DATALINK parameter.
   **/
  public void Var049() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName()
              + " (C_DATALINK) VALUES (DLVALUE( CAST(? AS VARCHAR(40))))");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1,
              "http://www.yahoo.com/lists.html", getSQLType(Types.VARCHAR), 0);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_DATALINK FROM "
              + pstestSet.getName());
          rs.next();
          String check = rs.getString(1);
          rs.close();

          assertCondition(check
              .equalsIgnoreCase("http://www.yahoo.com/lists.html"));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set a DATALINK parameter with a nonzero scale.
   **/
  public void Var050() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName()
              + " (C_DATALINK) VALUES (DLVALUE( CAST(? AS VARCHAR(35))))");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1,
              "http://java.sun.com/jdbc.html", getSQLType(Types.VARCHAR), 7);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_DATALINK FROM "
              + pstestSet.getName());
          rs.next();
          String check = rs.getString(1);
          rs.close();

          assertCondition(check
              .equalsIgnoreCase("http://java.sun.com/jdbc.html"));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set a DISTINCT parameter.
   **/
  public void Var051() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName() + " (C_DISTINCT) VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, Integer.valueOf(-92),
              getSQLType(Types.INTEGER), 0);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_DISTINCT FROM "
              + pstestSet.getName());
          rs.next();
          int check = rs.getInt(1);
          rs.close();

          assertCondition(check == -92);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set a DISTINCT parameter with a nonzero scale.
   **/
  public void Var052() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName() + " (C_DISTINCT) VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, Integer.valueOf(12),
              getSQLType(Types.INTEGER), 2);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_DISTINCT FROM "
              + pstestSet.getName());
          rs.next();
          int check = rs.getInt(1);
          rs.close();

          assertCondition(check == 12);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set a BIGINT parameter.
   **/
  public void Var053() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkBigintSupport()) {
      if (checkJdbc42()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName() + " (C_BIGINT) VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, Long.valueOf(5323495),
              getSQLType(Types.BIGINT), 0);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_BIGINT FROM "
              + pstestSet.getName());
          rs.next();
          long check = rs.getLong(1);
          rs.close();

          assertCondition(check == 5323495);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set a BIGINT parameter with a nonzero scale.
   **/
  public void Var054() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkBigintSupport()) {
      if (checkJdbc42()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName() + " (C_BIGINT) VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, Long.valueOf(
              -59322445665l), getSQLType(Types.BIGINT), 4);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_BIGINT FROM "
              + pstestSet.getName());
          rs.next();
          long check = rs.getLong(1);
          rs.close();

          assertCondition(check == -59322445665l);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set an VARCHAR parameter using Types.LONGVARCHAR. This is by
   * user request.
   * 
   * SQL400 - Not run through the Toolbox as I don't think they make this work.
   **/
  public void Var055() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkNative()) {
      if (checkJdbc42()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName() + " (C_VARCHAR_50) VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, "Aberdeen",
              getSQLType(Types.LONGVARCHAR), 0);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_VARCHAR_50 FROM "
              + pstestSet.getName());
          rs.next();
          String check = rs.getString(1);
          rs.close();

          assertCondition(check.equals("Aberdeen"));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set an VARCHAR parameter with a nonzero scale using
   * Types.LONGVARCHAR. This is by user request.
   * 
   * SQL400 - Not run through the Toolbox as I don't think they make this work.
   **/
  public void Var056() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkNative()) {
      if (checkJdbc42()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName() + " (C_VARCHAR_50) VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, "Desmet",
              getSQLType(Types.LONGVARCHAR), 4);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_VARCHAR_50 FROM "
              + pstestSet.getName());
          rs.next();
          String check = rs.getString(1);
          rs.close();

          assertCondition(check.equals("Desmet"));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set a VARBINARY parameter using Types.LONGVARBINARY. This is
   * by user request.
   * 
   * SQL400 - Not run through the Toolbox as I don't think they make this work.
   **/
  public void Var057() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    if (checkNative()) {
      if (checkJdbc42()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName() + " (C_VARBINARY_20) VALUES (?)");
          byte[] b = { (byte) -13, (byte) 32, (byte) 0, (byte) -1, (byte) -11,
              (byte) 99 };
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, b,
              getSQLType(Types.LONGVARBINARY), 0);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_VARBINARY_20 FROM "
              + pstestSet.getName());
          rs.next();
          byte[] check = rs.getBytes(1);
          rs.close();

          assertCondition(areEqual(check, b));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set a VARBINARY parameter with a nonzero scale using
   * Types.LONGVARBINARY. This is by user request.
   * 
   * SQL400 - Not run through the Toolbox as I don't think they make this work.
   **/
  public void Var058() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkNative()) {
      if (checkJdbc42()) {
        try {
          statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + pstestSet.getName() + " (C_VARBINARY_20) VALUES (?)");
          byte[] b = { (byte) -13, (byte) 32, (byte) 100, (byte) -1,
              (byte) -11, (byte) 99 };
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, b,
              getSQLType(Types.LONGVARBINARY), 3);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT C_VARBINARY_20 FROM "
              + pstestSet.getName());
          rs.next();
          byte[] check = rs.getBytes(1);
          rs.close();

          assertCondition(areEqual(check, b));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
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
   * setObject() - Set an DECIMAL parameter using exponential notation
   **/
  public void Var059() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_DECIMAL_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, (Object) "8.223187E+02");
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_DECIMAL_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 4);
        rs.close();

        assertCondition(check.doubleValue() == 822.3187);
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
   * setObject() - Set an DECIMAL parameter using exponential notation with 0 in
   * the exponent
   **/
  public void Var060() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_DECIMAL_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, (Object) "8.22E+00");
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_DECIMAL_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 4);
        rs.close();

        assertCondition(check.doubleValue() == 8.22);
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
   * setObject() - Set an DECIMAL parameter using exponential notation with 0 as
   * the value
   **/
  public void Var061() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_DECIMAL_105) VALUES (?)");
        ps.setObject(1, "0E+01");
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_DECIMAL_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 4);
        rs.close();

        assertCondition(check.doubleValue() == 0.0);
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
   * setObject() - Set an DECIMAL parameter using exponential notation with 0 as
   * the value and 0 as the exponent
   **/
  public void Var062() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (C_DECIMAL_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, (Object) "0E+00");
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_DECIMAL_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 4);
        rs.close();

        assertCondition(check.doubleValue() == 0.0);
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
   * setObject() -- Set a timestamp parameter to a char field and get the
   * timestamp back out
   */
  public void Var063() {
    if (checkJdbc42()) {
      try {
        String col = JDPSTest.COLLECTION;
        Statement stmt = statement_;
        try {
          stmt.executeUpdate("drop table " + col + ".CHAR_TAB");
        } catch (Exception e) {
        }
        stmt.executeUpdate("create table " + col
            + ".CHAR_TAB (COFFEE_NAME CHAR(30), NULL_VAL CHAR(30) )");

        stmt.executeUpdate("delete from " + col + ".CHAR_TAB");
        stmt.executeUpdate("insert into " + col
            + ".CHAR_TAB values('Test Coffee', null)");

        String sPrepStmt = "update " + col + ".CHAR_TAB set NULL_VAL=?";

        String maxStringVal = "1999-12-31 12:59:59.000000";
        Timestamp maxTimestampVal = java.sql.Timestamp.valueOf(maxStringVal);

        java.sql.PreparedStatement pstmt = connection_
            .prepareStatement(sPrepStmt);
        JDReflectionUtil.callMethod_V(pstmt, "setObject", 1, maxTimestampVal,
            getSQLType(Types.CHAR), 6);
        pstmt.executeUpdate();
        pstmt.close(); 
        // to query from the database to check the call of pstmt.executeUpdate
        String Null_Val_Query = "Select NULL_VAL from " + col + ".CHAR_TAB";
        ResultSet rs = stmt.executeQuery(Null_Val_Query);
        rs.next();

        String rStringVal = (String) rs.getObject(1);
        rStringVal = rStringVal.trim();
        Timestamp rTimestampVal = java.sql.Timestamp.valueOf(rStringVal);

        rs.close();

        stmt.executeUpdate("drop table " + col + ".CHAR_TAB");

        boolean condition = rTimestampVal.equals(maxTimestampVal);
        if (!condition) {
          output_.println("Returned String Value after Updation: "
              + rStringVal);
          output_.println("Returned Timestamp Value after Updation: "
              + rTimestampVal);
        }
        assertCondition(condition);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }

    }
  }

  /**
   * setObject() - Set an DECFLOAT(16) parameter.
   **/
  public void Var064() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
String added = " -- DECFLOAT(16) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {

      if (checkJdbc42()) {
        JDSerializeFile pstestSetdfp = null;
        try {
          pstestSetdfp = JDPSTest.getPstestSetdfp16(connection_);
          String tablename16=pstestSetdfp.getName();

          statement_.executeUpdate("DELETE FROM " + tablename16);

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + tablename16 + " VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
              "322.344"), getSQLType(Types.OTHER), 0);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT * FROM "
              + tablename16);
          rs.next();
          BigDecimal check = rs.getBigDecimal(1);
          rs.close();

          assertCondition(check.doubleValue() == 322.344,
              "Got " + check.doubleValue() + " expected 322.34 " + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception " + added);
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
   * setObject() - Set an DECFLOAT(16) parameter, when the data gets truncated.
   **/
  public void Var065() {
    String added = " -- DECFLOAT(16) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {

      if (checkJdbc42()) {
        JDSerializeFile pstestSetdfp = null;
        try {
          pstestSetdfp = JDPSTest.getPstestSetdfp16(connection_);
          String tablename16=pstestSetdfp.getName();
          statement_.executeUpdate("DELETE FROM " + tablename16);

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + tablename16 + " VALUES (?)");

          JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
              "9.999999999999999E800"), getSQLType(Types.OTHER), 0);
          ps.executeUpdate();
          ResultSet rs = statement_.executeQuery("SELECT * FROM "
              + tablename16);
          rs.next();
          BigDecimal check = rs.getBigDecimal(1);
          rs.close();
          ps.close(); 
          failed("Didn't throw SQLException but got " + check + added);
        } catch (DataTruncation dt) {
          assertCondition((dt.getIndex() == 1) && (dt.getParameter() == true)
              && (dt.getRead() == false) && (dt.getTransferSize() == 5), added);

        } catch (SQLException e) {
          // The native driver will throw a Data Type mismatch since this cannot
          // be expressed
          boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
          if (success) {
            assertCondition(true);
          } else {
            failed(e, "Expected data type mismatch (for native driver)" + added);
          }

        } catch (Exception e) {
          failed(e, "Unexpected Exception " + added);

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
  }

  /**
   * setObject() - Set an DECFLOAT(16) parameter, when the fraction gets
   * truncated. This does not cause a data truncation exception.
   **/
  public void Var066() {
    String added = " -- DECFLOAT(16) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {
      if (checkJdbc42()) {
        JDSerializeFile pstestSetdfp = null;
        try {
          pstestSetdfp = JDPSTest.getPstestSetdfp16(connection_);
          String tablename16=pstestSetdfp.getName();
          statement_.executeUpdate("DELETE FROM " + tablename16);

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + tablename16 + " VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
              "322.34412345678901234567890"), getSQLType(Types.OTHER), 0);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT * FROM "
              + tablename16);
          rs.next();
          BigDecimal check = rs.getBigDecimal(1);
          rs.close();

          assertCondition(check.toString().equals("322.3441234567890")
              || check.toString().equals("322.344123456789"), "Got " + check
              + " sb 322.3441234567890 " + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception" + added);
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
  }

  /**
   * setObject() - Set a DECFLOAT(16) parameter, when the object is the wrong
   * type.
   **/
  public void Var067() {
    String added = " -- DECFLOAT(16) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {
      if (checkJdbc42()) {
        JDSerializeFile pstestSetdfp = null;
        PreparedStatement ps = null; 
        try {
          pstestSetdfp = JDPSTest.getPstestSetdfp16(connection_);
          String tablename16=pstestSetdfp.getName();
          statement_.executeUpdate("DELETE FROM " + tablename16);

          ps = connection_.prepareStatement("INSERT INTO "
              + tablename16 + " VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, "Friends",
              getSQLType(Types.OTHER), 0);
          failed("Didn't throw SQLException" + added);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        } finally {
            try {
              if (pstestSetdfp != null) pstestSetdfp.close();
              if (ps != null) ps.close(); 
            } catch (SQLException e) {
              e.printStackTrace();
            }
          }
        }
      }
  }

  /**
   * setObject() - Set an DECFLOAT(34) parameter.
   **/
  public void Var068() {

    String added = " -- DECFLOAT(34) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {

      if (checkJdbc42()) {
        JDSerializeFile pstestSetdfp = null;
        try {
          pstestSetdfp = JDPSTest.getPstestSetdfp34(connection_);
          String tablename34=pstestSetdfp.getName();
          statement_.executeUpdate("DELETE FROM " + tablename34);

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + tablename34 + " VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
              "322.344"), getSQLType(Types.OTHER), 0);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT * FROM "
              + tablename34);
          rs.next();
          BigDecimal check = rs.getBigDecimal(1);
          rs.close();

          assertCondition(check.doubleValue() == 322.344,
              "Got " + check.doubleValue() + " expected 322.34 " + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception " + added);
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
  }

  /**
   * setObject() - Set an DECFLOAT(34) parameter, when the data gets truncated.
   **/
  public void Var069() {
    String added = " -- DECFLOAT(34) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {

      if (checkJdbc42()) {
        JDSerializeFile pstestSetdfp = null;
        PreparedStatement ps = null; 
        try {
          pstestSetdfp = JDPSTest.getPstestSetdfp34(connection_);
          String tablename34=pstestSetdfp.getName();
          statement_.executeUpdate("DELETE FROM " + tablename34);

          ps = connection_.prepareStatement("INSERT INTO "
              + tablename34 + " VALUES (?)");

          JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
              "9.999999999999999E56642"), getSQLType(Types.OTHER), 0);
          ps.executeUpdate();

          ResultSet rs = statement_.executeQuery("SELECT * FROM "
              + tablename34);
          rs.next();
          BigDecimal check = rs.getBigDecimal(1);
          rs.close();

          failed("Didn't throw SQLException got " + check + added);
        } catch (DataTruncation dt) {
          assertCondition((dt.getIndex() == 1) && (dt.getParameter() == true)
              && (dt.getRead() == false) && (dt.getTransferSize() == 5), added);

        } catch (SQLException e) {
          // The native driver will throw a Data Type mismatch since this cannot
          // be expressed
          boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
          if (success) {
            assertCondition(true);
          } else {
            failed(e, "Expected data type mismatch (for native driver)" + added);
          }

        } catch (Exception e) {
          failed(e, "Unexpected Exception " + added);
        } finally {

          try {
            if (pstestSetdfp != null)
              pstestSetdfp.close();
            if (ps != null)
              ps.close();
          } catch (SQLException e) {
            e.printStackTrace();
          }

        }
      }

    }
  }

  /**
   * setObject() - Set an DECFLOAT(34) parameter, when the fraction gets
   * rounded. This does not cause a data truncation exception.
   **/
  public void Var070() {
    String added = " -- DECFLOAT(34) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {
      if (checkJdbc42()) {
        JDSerializeFile pstestSetdfp = null;
        try {
          pstestSetdfp = JDPSTest.getPstestSetdfp34(connection_);
          String tablename34=pstestSetdfp.getName();
          statement_.executeUpdate("DELETE FROM " + tablename34);

          PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
              + tablename34 + " VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
              "322.344123456789012345678901234567890123456789088323"),
              getSQLType(Types.OTHER), 0);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT * FROM "
              + tablename34);
          rs.next();
          BigDecimal check = rs.getBigDecimal(1);
          rs.close();

          String expected = "322.3441234567890123456789012345679";
          assertCondition(check.toString().equals(expected), "Got " + check
              + " sb " + expected + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception" + added);
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
  }

  /**
   * setObject() - Set a DECFLOAT(34) parameter, when the object is the wrong
   * type.
   **/
  public void Var071() {
    String added = " -- DECFLOAT(34) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {
      if (checkJdbc42()) {
        JDSerializeFile pstestSetdfp = null;
        PreparedStatement ps = null; 
        try {
          pstestSetdfp = JDPSTest.getPstestSetdfp34(connection_);
          String tablename34=pstestSetdfp.getName();
          statement_.executeUpdate("DELETE FROM " + tablename34);

          ps = connection_.prepareStatement("INSERT INTO "
              + tablename34 + " VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, "Friends",
              getSQLType(Types.OTHER), 0);
          failed("Didn't throw SQLException" + added);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        } finally {
           {
            try {
              if (pstestSetdfp != null) pstestSetdfp.close();
              if (ps != null) ps.close(); 
            } catch (SQLException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
  }

  /**
   * setObject() - Should work with a valid parameter index greater than 1.
   **/
  public void Var072() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connectionCommaSeparator_);

    String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";

    if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connectionCommaSeparator_
            .prepareStatement("INSERT INTO " + pstestSet.getName()
                + " (C_KEY, C_NUMERIC_105) VALUES (?, ?)");
        ps.setString(1, "Test");
        JDReflectionUtil.callMethod_V(ps, "setObject", 2,
            new BigDecimal("4.3"), getSQLType(Types.NUMERIC), 1);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_NUMERIC_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 1);
        rs.close();

        assertCondition(check.doubleValue() == 4.3, added);
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
   * setObject() - Set an DECIMAL parameter with scale 0.
   **/
  public void Var073() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connectionCommaSeparator_);
    String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";

    if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connectionCommaSeparator_
            .prepareStatement("INSERT INTO " + pstestSet.getName()
                + " (C_DECIMAL_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
            "32.234"), getSQLType(Types.DECIMAL), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_DECIMAL_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 3);
        rs.close();

        assertCondition(check.doubleValue() == 32, added);
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
   * setObject() - Set an DECIMAL parameter with scale greater than the number.
   **/
  public void Var074() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connectionCommaSeparator_);
    String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";

    if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connectionCommaSeparator_
            .prepareStatement("INSERT INTO " + pstestSet.getName()
                + " (C_DECIMAL_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
            "-12.87"), getSQLType(Types.DECIMAL), 4);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_DECIMAL_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 2);
        rs.close();

        assertCondition(check.doubleValue() == -12.87, added);
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
   * setObject() - Set an NUMERIC parameter with scale 0.
   **/
  public void Var075() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connectionCommaSeparator_);
    String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";

    if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connectionCommaSeparator_
            .prepareStatement("INSERT INTO " + pstestSet.getName()
                + " (C_NUMERIC_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
            "-432.34"), getSQLType(Types.NUMERIC), 0);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_NUMERIC_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 0);
        rs.close();

        assertCondition(check.doubleValue() == -432, added);
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
   * setObject() - Set an NUMERIC parameter with scale less than the number.
   **/
  public void Var076() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connectionCommaSeparator_);
    String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";

    if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connectionCommaSeparator_
            .prepareStatement("INSERT INTO " + pstestSet.getName()
                + " (C_NUMERIC_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
            "52.219"), getSQLType(Types.NUMERIC), 1);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_NUMERIC_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 1);
        rs.close();

        assertCondition(check.doubleValue() == 52.2, added);
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
   * setObject() - Set an DECIMAL parameter using exponential notation
   **/
  public void Var077() {
    JDSerializeFile pstestSet = null;
    try {
      pstestSet = JDPSTest.getPstestSet(connection_);
    String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";

    if (checkJdbc42()) {
      try {
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connectionCommaSeparator_
            .prepareStatement("INSERT INTO " + pstestSet.getName()
                + " (C_DECIMAL_105) VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, (Object) "8.223187E+02");
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery("SELECT C_DECIMAL_105 FROM "
            + pstestSet.getName());
        rs.next();
        BigDecimal check = rs.getBigDecimal(1, 4);
        rs.close();

        assertCondition(check.doubleValue() == 822.3187, added);
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
   * setObject() - Set an DECFLOAT(16) parameter.
   **/
  public void Var078() {

    String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";

    if (checkDecFloatSupport()) {

      if (checkJdbc42()) {
        JDSerializeFile pstestSetdfp = null;
        try {
          pstestSetdfp = JDPSTest.getPstestSetdfp16(connection_);
          String tablename16=pstestSetdfp.getName();

          statement_.executeUpdate("DELETE FROM " + tablename16);

          PreparedStatement ps = connectionCommaSeparator_
              .prepareStatement("INSERT INTO " + tablename16
                  + " VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
              "322.344"), getSQLType(Types.OTHER), 0);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT * FROM "
              + tablename16);
          rs.next();
          BigDecimal check = rs.getBigDecimal(1);
          rs.close();

          assertCondition(check.doubleValue() == 322.344,
              "Got " + check.doubleValue() + " expected 322.34 " + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception " + added);
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
  }

  /**
   * setObject() - Set an DECFLOAT(34) parameter.
   **/
  public void Var079() {

    String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";

    if (checkDecFloatSupport()) {

      if (checkJdbc42()) {
        JDSerializeFile pstestSetdfp = null;
        try {
          pstestSetdfp = JDPSTest.getPstestSetdfp34(connectionCommaSeparator_);
          String tablename34=pstestSetdfp.getName();

          statement_.executeUpdate("DELETE FROM " + tablename34);

          PreparedStatement ps = connectionCommaSeparator_
              .prepareStatement("INSERT INTO " + tablename34
                  + " VALUES (?)");
          JDReflectionUtil.callMethod_V(ps, "setObject", 1, new BigDecimal(
              "322.344"), getSQLType(Types.OTHER), 0);
          ps.executeUpdate();
          ps.close();

          ResultSet rs = statement_.executeQuery("SELECT * FROM "
              + tablename34);
          rs.next();
          BigDecimal check = rs.getBigDecimal(1);
          rs.close();

          assertCondition(check.doubleValue() == 322.344,
              "Got " + check.doubleValue() + " expected 322.34 " + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception " + added);
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

  }

            /**
setParameterTest () - Set the specified parameter using an object. 
         * @param setType 
**/
  public void setParameterTest(String columnName, Object setObject, int setType, int setScale,
      String expectedString) {
    if (checkJdbc42()) {
      JDSerializeFile pstestSet = null;
      try {
        pstestSet = JDPSTest.getPstestSet(connection_);
        statement_.executeUpdate("DELETE FROM " + pstestSet.getName());

        PreparedStatement ps = connection_.prepareStatement("INSERT INTO "
            + pstestSet.getName() + " (" + columnName + ") VALUES (?)");
        JDReflectionUtil.callMethod_V(ps, "setObject", 1, setObject,
            getSQLType(setType), setScale);
        ps.executeUpdate();
        ps.close();

        ResultSet rs = statement_.executeQuery(
            "SELECT " + columnName + " FROM " + pstestSet.getName());
        rs.next();
        String check = rs.getString(1);
        rs.close();

        assertCondition(expectedString.equals(check),
            " got " + check + " sb " + expectedString);
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
  }

  public void Var080() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", Boolean.valueOf(true), Types.BOOLEAN, 0, "1");
    }
  }

  public void Var081() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", Boolean.valueOf(false),Types.BOOLEAN, 0, "0");
    }
  }

  public void Var082() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", "true", Types.BOOLEAN, 0,"1");
    }
  }

  public void Var083() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", "false", Types.BOOLEAN, 0, "0");
    }
  }

  public void Var084() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", Integer.valueOf(100),Types.BOOLEAN, 0, "1");
    }
  }

  public void Var085() {
    if (checkBooleanSupport()) {
      setParameterTest("C_BOOLEAN", Integer.valueOf(0),Types.BOOLEAN, 0, "0");
    }
  }


  
  
}
