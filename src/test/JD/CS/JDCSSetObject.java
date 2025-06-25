///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetObject.java
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
// File Name:    JDCSSetObject.java
//
// Classes:      JDCSSetObject
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDLobTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;

/**
 * Testcase JDCSSetObject. This tests the following method of the JDBC
 * CallableStatement class:
 * 
 * setObject()
 * 
 **/
public class JDCSSetObject extends JDCSSetTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSSetObject";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }

  private Connection connectionDateTime_;

  /**
   * Constructor.
   **/
  public JDCSSetObject(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDCSSetObject", namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    super.setup();

    String url = baseURL_ + ";data truncation=true";
    connectionDateTime_ = testDriver_
        .getConnection(url + ";date format=jis;time format=jis", userId_, encryptedPassword_);

  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    super.cleanup();
    connectionDateTime_.close();
  }

  /**
   * Compares a Clob with a String.
   **/
  private boolean compare(Clob i, String b) throws SQLException {
    return i.getSubString(1, (int) i.length()).equals(b); // @D1C
  }

  /**
   * setObject() - Set parameter -1.
   **/
  public void Var001() {
    try {

      cs.setObject(-1, new BigDecimal(2.3));
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * setObject() - Set parameter 0.
   **/
  public void Var002() {
    try {
      cs.setObject(0, Integer.valueOf(7));
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * SetObject() - Use a parameter that is too big.
   **/
  public void Var003() {
    try {
      cs.setObject(100, Integer.valueOf(4));
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * SetObject() - Set a parameter when there are no parameters.
   **/
  public void Var004() {
    // I created a whole new Connection object here to work
    // around a server bug.
    Connection c = null;
    try {
      c = testDriver_.getConnection(baseURL_ + ";errors=full", userId_, encryptedPassword_);
      CallableStatement cs1 = c.prepareCall("CALL " + JDSetupProcedure.STP_CS0);
      cs1.execute();
      cs1.setObject(1, Integer.valueOf(4));
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }

    if (c != null)
      try {
        c.close();
      } catch (Exception e) {
      }
  }

  /**
   * SetObject() - Set a parameter that was not registered.
   **/
  public void Var005() {
    try {
      CallableStatement cs1 = JDSetupProcedure.prepare(connection_,
          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_); // @KK
      cs1.execute();
      cs1.setObject(1, Integer.valueOf(4));
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * SetObject() - Set a parameter when the statement has not been executed.
   **/
  public void Var006() {
    try {
      CallableStatement cs1 = JDSetupProcedure.prepare(connection_,
          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_); // @KK
      cs1.registerOutParameter(12, Types.VARCHAR);
      cs1.setObject(12, "Test varchar");
      cs1.execute();
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * SetObject() - Set a parameter when the statement is closed.
   **/
  public void Var007() {
    try {
      CallableStatement cs1 = JDSetupProcedure.prepare(connection_,
          JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_); // @KK
      cs1.registerOutParameter(12, Types.VARCHAR);
      cs1.execute();
      cs1.close();
      cs1.setObject(12, "test varchar");
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * SetObject() - Set an IN parameter that was correctly registered.
   **/
  public void Var008() {
    try {
      CallableStatement cs1 = JDSetupProcedure.prepare(connection_,
          JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_); // @KK
      JDSetupProcedure.setTypesParameters(cs1, JDSetupProcedure.STP_CSTYPESIN,
          supportedFeatures_); // @KK
      cs1.registerOutParameter(12, Types.VARCHAR);
      cs1.execute();
      cs1.setObject(12, "test varchar");
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * SetObject() - Set an INOUT parameter, where the OUT parameter is longer
   * than the IN parameter.
   **/
  public void Var009() {
    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
      try {
        CallableStatement cs1 = JDSetupProcedure.prepare(connection_,
            JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_); // @KK
        JDSetupProcedure.setTypesParameters(cs1,
            JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_); // @KK
        JDSetupProcedure.register(cs1, JDSetupProcedure.STP_CSTYPESINOUT,
            supportedFeatures_, getDriver()); // @KK
        cs1.execute();
        cs1.setObject(12, "test variation 9");
        // Note: the following cs.registerOutParameter is not required for
        // native driver
        // It works even without it.
        cs1.registerOutParameter(12, Types.VARCHAR);
        cs1.execute();
        /* As of 7/5/2007 on V5R4 this testcase is now working like toolbox */
        /* I'm not sure why since no JDBC change was made. I have however */
        /* noticed changes in how SQ handles truncation in stored procedures */
        /* Perhaps this is why it has changed */
        /*
         * As of 10/18/2007 on V5R4, SQ has changed back so the SQVALUES will
         * throw and error
         */
        /* Changing this back to the way it was */
        /* As of 2/11/2008 on V6R1 an exception is not thrown */
        if ((getDriver() == JDTestDriver.DRIVER_NATIVE)
            && (getRelease() >= JDTestDriver.RELEASE_V7R1M0)
            && (getRelease() < JDTestDriver.RELEASE_V7R1M0)) { // @KK
          /*
           * The native driver expects an exception to be thrown because the
           * first call to setTypesParameters sets the value of column
           * 14(1-based) to Bonjour first call to execute makes that value be
           * "Dave WallBonjour" // procedure prepends Dave Wall to existing
           * value ... NOTE: 'Dave Wall' (EBCDIC coding) and 'Bonjour' (ASCII
           * coding) second call would result in "Dave WallDave WallBonjour"
           * which is 25 characters past beyond the limit of that column, which
           * is 20 characters
           * 
           */
          Object p = cs1.getObject(12);
          failed(" Didn't throw an exception but got " + p); // @KK
        } else { // @KK
          Object p = cs1.getObject(12);
          assertCondition(p.equals("test variation 9JDBC"));
        }
      } catch (Exception e) {
        if ((getDriver() == JDTestDriver.DRIVER_NATIVE)
            && (getRelease() >= JDTestDriver.RELEASE_V7R1M0)) { // @KK
          // With the new JDBC 4.0 support, this exception changes
          String classname = e.getClass().getName();
          if (classname.indexOf("DB2SQLData") >= 0) {
            assertExceptionIsInstanceOf(e,
                "com.ibm.db2.jdbc.app.DB2SQLDataException"); // @KK
          } else {
            assertExceptionIsInstanceOf(e,
                "com.ibm.db2.jdbc.app.DB2DBException"); // @KK
          }
        } else {
          failed(e, "Unexpected Exception");
        }
      }
    } else {
      notApplicable();
    }
  }

  /**
   * SetObject() - Set a type that was registered as a SMALLINT.
   * 
   **/
  public void Var010() {
    try {
      cs.setObject(1, Short.valueOf((short) -33));
      cs.execute();
      Integer p = (Integer) cs.getObject(1);
      assertCondition(p.intValue() == -33);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * SetObject() - Set a type that was registered as an INTEGER.
   **/
  public void Var011() {
    try {
      cs.setObject(2, Integer.valueOf(-456));
      cs.execute();
      Integer p = (Integer) cs.getObject(2);
      assertCondition(p.intValue() == -456);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * SetObject() - Set a type that was registered as an REAL.
   **/
  public void Var012() {
    try {
      cs.setObject(3, Float.valueOf(789.54f));
      cs.execute();
      Float p = (Float) cs.getObject(3);
      assertCondition(p.floatValue() == 789.54f);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * SetObject() - Set a type that was registered as an FLOAT.
   **/
  public void Var013() {
    try {

      cs.setObject(4, Double.valueOf(253.1027));
      cs.execute();
      Double p = (Double) cs.getObject(4);
      assertCondition(p.doubleValue() == 253.1027);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * SetObject() - Set a type that was registered as an DOUBLE.
   **/
  public void Var014() {
    try {
      cs.setObject(5, Double.valueOf(-987.3434));
      cs.execute();
      Double p = (Double) cs.getObject(5);
      assertCondition(p.doubleValue() == -987.3434);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * SetObject() - Set a type that was registered as an DECIMAL.
   **/
  public void Var015() {
    try {
      cs.setObject(7, new BigDecimal("322.344"));
      cs.execute();
      BigDecimal p = (BigDecimal) cs.getObject(7);
      assertCondition(p.doubleValue() == 322.344);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * SetObject() - Set a type that was registered as an NUMERIC.
   **/
  public void Var016() {
    try {
      cs.setObject(9, new BigDecimal("19.98765"));
      cs.execute();
      BigDecimal p = (BigDecimal) cs.getObject(9);
      assertCondition(p.doubleValue() == 19.98765,
          "p = " + p + " and should be 19.98765");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * SetObject() - Set a type that was registered as a CHAR.
   **/
  public void Var017() {
    try {
      cs.setObject(10, "y");
      cs.execute();
      String p = (String) cs.getObject(10);
      assertCondition(p.equals("y"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * SetObject() - Set a type that was registered as a CHAR.
   **/
  public void Var018() {
    try {
      cs.setObject(11, "set var 19");
      cs.execute();
      String p = (String) cs.getObject(11);
      assertCondition(
          p.equals("set var 19                                        "));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * SetObject() - Set a type that was registered as a VARCHAR.
   **/
  public void Var019() {
    try {
      cs.setObject(12, "Charlie");
      cs.execute();
      String p = (String) cs.getObject(12);
      assertCondition(p.equals("Charlie"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * SetObject() - Set a type that was registered as a BINARY.
   **/
  public void Var020() {
    try {
      byte[] p = new byte[] { (byte) -44, (byte) -92, (byte) -103, (byte) -125,
          (byte) -120, (byte) 64, (byte) -125, (byte) -120, (byte) 64,
          (byte) -125, (byte) -120, (byte) 64, (byte) -125, (byte) -120,
          (byte) 64, (byte) -125, (byte) -120, (byte) 64, (byte) -125,
          (byte) -120 };

      cs.setObject(13, p);
      cs.execute();

      byte[] check = (byte[]) cs.getObject(13);

      assertCondition(areEqual(p, check));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * SetObject() - Set a type that was registered as a VARBINARY.
   **/
  public void Var021() {
    try {
      byte[] expected = new byte[] { (byte) -60, (byte) -127, (byte) -91,
          (byte) -123, (byte) 64, (byte) -26, (byte) -127, (byte) -109,
          (byte) -109 };

      cs.setObject(14, expected);
      cs.execute();
      byte[] check = (byte[]) cs.getObject(14);

      assertCondition(areEqual(expected, check));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * SetObject() - Set a type that was registered as a DATE.
   **/
  public void Var022() {
    try {
      Date t = Date.valueOf("1980-06-25");
      cs.setObject(15, t);
      cs.execute();

      Date p = (Date) cs.getObject(15);
      // The following allows leeway of a day, to account for conversion
      // anomalies by the JVM.
      assertCondition(p.getTime() - 892616400000L < 86400000);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * SetObject() - Set a type that was registered as a TIME.
   **/
  public void Var023() {
    try {

      cs.setObject(16, Time.valueOf("22:33:44"));
      cs.execute();
      Time p = (Time) cs.getObject(16);
      assertCondition(p.toString().equals("22:33:44"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * SetObject() - Set a type that was registered as a TIMESTAMP.
   **/
  public void Var024() {
    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
      try {
        Timestamp t = new Timestamp(4342343);
        cs.setObject(17, t);
        cs.execute();
        Timestamp p = (Timestamp) cs.getObject(17);
        assertCondition(p.equals(t), " p = " + p);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    } else {
      notApplicable();
    }
  }

  /**
   * SetObject() - Set a type that was registered as an OTHER.
   **/
  public void Var025() {
    if (checkLobSupport()) {
      try {
        cs.setObject(18, "http://www.sony.com/pix.html");
        cs.execute();

        Object p = cs.getObject(18);
        assertCondition(p.equals("http://www.sony.com/pix.html"), "p = " + p);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as a BLOB.
   **/
  public void Var026() {
    if (checkJdbc20()) {
      try {
        byte[] b = { (byte) 12, (byte) 23, (byte) 34, (byte) 45 };

        cs.setObject(19, new JDLobTest.JDTestBlob(b));
        cs.execute();

        Object p = cs.getObject(19);
        Blob test = (Blob) p;
        assertCondition(areEqual(b, test.getBytes(1, (int) test.length())));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as a CLOB.
   **/
  public void Var027() {
    if (checkJdbc20()) {
      try {
        cs.setObject(20, new JDLobTest.JDTestClob("Chris Smyth"));
        cs.execute();

        Object p = cs.getObject(20);

        assertCondition(compare((Clob) p, "Chris Smyth"));

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as a BIGINT.
   **/
  public void Var028() {
    if (checkBigintSupport()) {
      try {
        cs.setObject(22, Long.valueOf(-495959));
        cs.execute();
        Long p = (Long) cs.getObject(22);
        assertCondition(p.longValue() == -495959, " p = " + p);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set an INOUT parameter, where the OUT parameter is longer
   * than the IN parameter, when the output parameter is registered first
   * 
   * SQL400 - We added this testcase because of a customer bug.
   **/
  public void Var029() {
    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
      try {
        CallableStatement cs1 = JDSetupProcedure.prepare(connection_,
            JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_); // @KK
        JDSetupProcedure.register(cs1, JDSetupProcedure.STP_CSTYPESINOUT,
            supportedFeatures_, getDriver()); // @KK
        cs1.registerOutParameter(12, Types.VARCHAR);
        JDSetupProcedure.setTypesParameters(cs1,
            JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_); // @KK
        cs1.setObject(12, "MiJDBC");
        cs1.execute();
        Object p = cs1.getObject(12);
        assertCondition(p.equals("MiJDBCJDBC"), " p = " + p);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    } else {
      notApplicable();
    }
  }

  // Named Parameter
  /**
   * SetObject() - Set a type that was registered as a SMALLINT. In v5r2,
   * SetObject was changed to return an Integer instead of a Short.
   **/
  public void Var030() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setObject("P_SMALLINT", Short.valueOf((short) 123));
        cs.execute();
        Integer p = (Integer) cs.getObject("P_SMALLINT");
        assertCondition(p.intValue() == 123);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as an INTEGER.
   **/
  public void Var031() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setObject("P_INTEGER", Integer.valueOf(-456));
        cs.execute();
        Integer p = (Integer) cs.getObject("P_INTEGER");
        assertCondition(p.intValue() == -456);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as an REAL.
   **/
  public void Var032() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setObject("P_REAL", Float.valueOf(789.54f));
        cs.execute();
        Float p = (Float) cs.getObject("P_REAL");
        assertCondition(p.floatValue() == 789.54f);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as an FLOAT.
   **/
  public void Var033() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setObject("P_FLOAT", Double.valueOf(253.1027));
        cs.execute();
        Double p = (Double) cs.getObject("P_FLOAT");
        assertCondition(p.doubleValue() == 253.1027);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as an DOUBLE.
   **/
  public void Var034() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setObject("P_DOUBLE", Double.valueOf(-987.3434));
        cs.execute();
        Double p = (Double) cs.getObject("P_DOUBLE");
        assertCondition(p.doubleValue() == -987.3434);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as an DECIMAL.
   **/
  public void Var035() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setObject("P_DECIMAL_105", new BigDecimal("-94732.12345"));
        cs.execute();
        BigDecimal p = (BigDecimal) cs.getObject("P_DECIMAL_105");
        assertCondition(p.doubleValue() == -94732.12345, " p = " + p);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as an NUMERIC.
   **/
  public void Var036() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setObject("P_NUMERIC_105", new BigDecimal("19.98765"));
        cs.execute();
        BigDecimal p = (BigDecimal) cs.getObject("P_NUMERIC_105");
        assertCondition(p.doubleValue() == 19.98765, "p = " + p);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as a CHAR.
   **/
  public void Var037() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setObject("P_CHAR_50", "Jim");
        cs.execute();
        String p = (String) cs.getObject("P_CHAR_50");
        assertCondition(
            p.equals("Jim                                               "));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as a VARCHAR.
   **/
  public void Var038() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setObject("P_VARCHAR_50", "Charlie");
        cs.execute();
        String p = (String) cs.getObject("P_VARCHAR_50");
        assertCondition(p.equals("Charlie"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as a BINARY.
   **/
  public void Var039() {
    if (checkNamedParametersSupport()) {
      try {
        byte[] p = new byte[] { (byte) -44, (byte) -92, (byte) -103,
            (byte) -125, (byte) -120, (byte) 64, (byte) 64, (byte) 64,
            (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64,
            (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64 };
        cs.setObject("P_BINARY_20", p);
        cs.execute();
        byte[] check = (byte[]) cs.getObject("P_BINARY_20");

        assertCondition(areEqual(p, check));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as a VARBINARY.
   **/
  public void Var040() {
    if (checkNamedParametersSupport()) {
      try {
        byte[] p = new byte[] { (byte) -60, (byte) -127, (byte) -91,
            (byte) -123, (byte) 64, (byte) -26, (byte) -127, (byte) -109,
            (byte) -109 };
        cs.setObject("P_VARBINARY_20", p);
        cs.execute();
        byte[] check = (byte[]) cs.getObject("P_VARBINARY_20");

        assertCondition(areEqual(p, check));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as a DATE.
   **/
  public void Var041() {
    if (checkNamedParametersSupport()) {
      try {
        Date t = Date.valueOf("2036-10-25");

        cs.setObject("P_DATE", t);
        cs.execute();
        Date p = (Date) cs.getObject("P_DATE");
        assertCondition(p.toString().equals("2036-10-25"),
            "p = " + p + "sb 2036-10-25");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as a TIME.
   **/
  public void Var042() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setObject("P_TIME", Time.valueOf("22:33:44"));
        cs.execute();
        Time p = (Time) cs.getObject("P_TIME");
        assertCondition(p.toString().equals("22:33:44"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as a TIMESTAMP.
   **/
  public void Var043() {
    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
      if (checkNamedParametersSupport()) {
        try {
          Timestamp t = new Timestamp(444);
          cs.setObject("P_TIMESTAMP", t);
          cs.execute();
          Timestamp p = (Timestamp) cs.getObject("P_TIMESTAMP");
          assertCondition(p.equals(t), " p = " + p);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    } else {
      notApplicable();
    }
  }

  /**
   * SetObject() - Set a type that was registered as an OTHER.
   **/
  public void Var044() {
    if (checkNamedParametersSupport()) {
      if (checkLobSupport()) {
        try {
          cs.setObject("P_DATALINK", "https://github.com/IBM/JTOpen-test/blob/main/README.md");
          cs.execute();
          Object p = cs.getObject("P_DATALINK");
          assertCondition(p.equals("https://github.com/IBM/JTOpen-test/blob/main/README.md"));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as a BLOB.
   **/
  public void Var045() {
    if (checkNamedParametersSupport()) {
      if (checkLobSupport()) {
        try {
          byte[] b = { (byte) 4, (byte) 43, (byte) 40, (byte) 32 };

          cs.setObject("P_BLOB", new JDLobTest.JDTestBlob(b));
          cs.execute();
          Object p = cs.getObject("P_BLOB");
          Blob test = (Blob) p;
          assertCondition(areEqual(b, test.getBytes(1, (int) test.length())));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as a CLOB.
   **/
  public void Var046() {
    if (checkNamedParametersSupport()) {
      if (checkLobSupport()) {
        try {
          cs.setObject("P_CLOB", new JDLobTest.JDTestClob("Chris Smyth"));
          cs.execute();
          Object p = cs.getObject("P_CLOB");
          assertCondition(compare((Clob) p, "Chris Smyth"));

        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as a BIGINT.
   **/
  public void Var047() {
    if (checkNamedParametersSupport()) {
      if (checkBigintSupport()) {
        try {
          cs.setObject("P_BIGINT", Long.valueOf(622345671));
          cs.execute();
          Long p = (Long) cs.getObject("P_BIGINT");
          assertCondition(p.longValue() == 622345671L, " p = " + p);

        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  // TESTING native driver
  public void Var048() { // @KK - added this Var
    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
      try {
        boolean success = true;
        // This variation checks that none of the i-value in for loop causes an
        // exception to e thrown

        int count = 24; // actually shldn't matter for 23,24 (1-based)

        for (int i = 0; i < count; i++) {

          CallableStatement cs1 = JDSetupProcedure.prepare(connection_,
              JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
          JDSetupProcedure.setTypesParameters(cs1,
              JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
          JDSetupProcedure.register(cs1, JDSetupProcedure.STP_CSTYPESINOUT,
              supportedFeatures_, getDriver());
          cs1.execute();

          // System.out.println("Setting all parameters but for parameter
          // #(1-based): "+(i+1));

          // Sets all parameters but for i+1
          JDSetupProcedure.setTypesParametersButForOne(cs1,
              JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_, i + 1);

          // Sets parameter for i+1 alone
          JDSetupProcedure.setTypesParametersForOne(cs1,
              JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_, i + 1);

          cs1.execute();

          // checkAll2 checks values for all but i+1 parameter
          // checkAll2forOne checks values for only i+1 parameter
          if (!(JDSetupProcedure.checkAllButForOne(cs1,
              JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_, i + 1)
              && JDSetupProcedure.checkAllForOne(cs1,
                  JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_,
                  i + 1))) {
            System.out.println("*** Failed on : " + (i + 1));
            success = false;
          }
          cs1.close();
        }
        assertCondition(success);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    } else {
      notApplicable();
    }
  }

  /**
   * setObject() -- Use various decimal formats. This detects a bug in the
   * native JDBC driver in the SQLDataFactory.convertScientificNotation method
   **/

  public void Var049() {
    String added = " -- Added by native driver 3/21/2006";
    String message = "";
    boolean passed = true;
    int i = 0;

    String[] inputExpected = { /* 0 */ "1.0", "1.000000000000000",
        /* 2 */ "1.0E1", "10.000000000000000", /* 4 */ "100.0E-1",
        "10.000000000000000", /* 6 */ "1.0E-15", "0.000000000000001",
        /* 8 */ "1.0E15", "1000000000000000.000000000000000", /* 10 */ "1E1",
        "10.000000000000000", /* 12 */ "100E-1", "10.000000000000000",
        /* 14 */ "1E-15", "0.000000000000001", /* 16 */ "1E15",
        "1000000000000000.000000000000000", /* 18 */ "0.035E-1",
        "0.003500000000000", /* 20 */ "0.035E+1", "0.350000000000000",
        /* 22 */ "0.035E+2", "3.500000000000000", /* 24 */ "-1.0",
        "-1.000000000000000", /* 26 */ "-1.0E1", "-10.000000000000000",
        /* 28 */ "-100.0E-1", "-10.000000000000000", /* 30 */ "-1.0E-15",
        "-0.000000000000001", /* 32 */ "-1.0E15",
        "-1000000000000000.000000000000000", /* 34 */ "-1E1",
        "-10.000000000000000", /* 36 */ "-100E-1", "-10.000000000000000",
        /* 38 */ "-1E-15", "-0.000000000000001", /* 40 */ "-1E15",
        "-1000000000000000.000000000000000", /* 42 */ "-0.035E-1",
        "-0.003500000000000", /* 44 */ "-0.035E+1", "-0.350000000000000",
        /* 46 */ "-0.035E+2", "-3.500000000000000", };

    ;

    try {

      //
      // Create the procedure
      //
      String procedureName = JDCSTest.COLLECTION + ".DECIO";
      Statement stmt = connection_.createStatement();
      try {
        stmt.executeUpdate("Drop procedure " + procedureName);
      } catch (Exception e) {
      }
      stmt.executeUpdate("CREATE PROCEDURE " + procedureName
          + " ( IN INDEC DECIMAL(31,15), OUT OUTDEC DECIMAL(31,15)) LANGUAGE SQL BEGIN SET OUTDEC=INDEC; END");

      stmt.close();
      CallableStatement cs1 = connection_
          .prepareCall("CALL " + procedureName + "(?,?)");

      //
      // Loop and test the combinations
      //
      for (i = 0; i < inputExpected.length; i += 2) {
        String input = inputExpected[i];
        String expected = inputExpected[i + 1];

        cs1.registerOutParameter(2, Types.VARCHAR);
        cs1.setObject(1, input);
        cs1.execute();
        String output = cs1.getString(2);
        if (!output.equals(expected)) {
          message += "i = " + i + " input=" + input + " output=" + output
              + " expected=" + expected + "\n";
          passed = false;
        }

      }

      assertCondition(passed, message + added);
    } catch (Exception e) {
      failed(e, "Unexpected Exception i=" + i + " input[i]=" + inputExpected[i]
          + added);
    }

  }

  /**
   * SetObject() - Set a type that was registered as a BOOLEAN.
   **/
  public void Var050() {
    if (checkBooleanSupport()) {
      try {
        cs.setObject(27, Boolean.valueOf(true));
        cs.execute();
        Boolean p = (Boolean) cs.getObject(27);
        assertCondition(p.booleanValue() == true, " p = " + p);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * SetObject() - Set a type that was registered as a BOOLEAN.
   **/
  public void Var051() {
    if (checkBooleanSupport()) {
      try {
        cs.setObject(27, Boolean.valueOf(false));
        cs.execute();
        Boolean p = (Boolean) cs.getObject(27);
        assertCondition(p.booleanValue() == false, " p = " + p);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var052() {
    notApplicable();
  }

  public void Var053() {
    notApplicable();
  }

  public void Var054() {
    notApplicable();
  }

  public void Var055() {
    notApplicable();
  }

  public void Var056() {
    notApplicable();
  }

  public void Var057() {
    notApplicable();
  }

  public void Var058() {
    notApplicable();
  }

  public void Var059() {
    notApplicable();
  }

  public void Var060() {
    notApplicable();
  }

  /* Test a parameter */
  public void testParameter(String parameterName, Object o,
      String expectedResult) {
    try {

      assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
      CallableStatement cs = connectionDateTime_.prepareCall(callSql);
      JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
          supportedFeatures_);
      JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
          supportedFeatures_, getDriver());

      cs.setObject(parameterName, o);
      cs.execute();
      String s = cs.getString(parameterName);
      cs.close();
      assertCondition(expectedResult.equals(s),
          "got '" + s + "' expected '" + expectedResult + "'");
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  public void testLocalTimeParameter(String parameterName, int hour, int minute,
      int second, String expectedResult) {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", hour, minute, second);
        testParameter(parameterName, o, expectedResult);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void testLocalTimeFailure(String parameterName, String info) {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
            "of", 1, 2, 3);
        setTestFailure("setObject", parameterName, o, info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void testLocalDateParameter(String parameterName, int year, int month,
      int day, String expectedResult) {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", year, month, day);
        testParameter(parameterName, o, expectedResult);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void testLocalDateFailure(String parameterName, String info) {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 2001, 2, 3);
        setTestFailure("setObject", parameterName, o, info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void testLocalDateTimeParameter(String parameterName, int year,
      int month, int day, int hour, int minute, int second, int nano,
      String expectedResult) {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O(
            "java.time.LocalDateTime", "of", year, month, day, hour, minute,
            second, nano);
        testParameter(parameterName, o, expectedResult);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void testLocalDateTimeFailure(String parameterName, String info) {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O(
            "java.time.LocalDateTime", "of", 2001, 2, 3, 4, 5, 6, 123456789);
        setTestFailure("setObject", parameterName, o, info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /* Test the types using LocalTime */

  public void Var061() {
    testLocalTimeFailure("P_SMALLINT", "LocalTime");
  }

  public void Var062() {
    testLocalTimeFailure("P_INTEGER", "LocalTime");
  }

  public void Var063() {
    testLocalTimeFailure("P_REAL", "LocalTime");
  }

  public void Var064() {
    testLocalTimeFailure("P_FLOAT", "LocalTime");
  }

  public void Var065() {
    testLocalTimeFailure("P_DOUBLE", "LocalTime");
  }

  public void Var066() {
    testLocalTimeFailure("P_DECIMAL_50", "LocalTime");
  }

  public void Var067() {
    testLocalTimeFailure("P_DECIMAL_105", "LocalTime");
  }

  public void Var068() {
    testLocalTimeFailure("P_NUMERIC_50", "LocalTime");
  }

  public void Var069() {
    testLocalTimeFailure("P_NUMERIC_105", "LocalTime");
  }

  public void Var070() {
    testLocalTimeFailure("P_CHAR_1", "LocalTime");
  }

  public void Var071() {
    testLocalTimeParameter("P_CHAR_50", 0, 0, 0,
        "00:00:00                                          ");
  }

  public void Var072() {
    testLocalTimeParameter("P_CHAR_50", 2, 3, 4,
        "02:03:04                                          ");
  }

  public void Var073() {
    testLocalTimeParameter("P_CHAR_50", 23, 59, 59,
        "23:59:59                                          ");
  }

  public void Var074() {
    testLocalTimeParameter("P_VARCHAR_50", 0, 0, 0, "00:00:00");
  }

  public void Var075() {
    testLocalTimeParameter("P_VARCHAR_50", 2, 3, 4, "02:03:04");
  }

  public void Var076() {
    testLocalTimeParameter("P_VARCHAR_50", 23, 59, 59, "23:59:59");
  }

  public void Var077() {
    testLocalTimeFailure("P_BINARY_20", "LocalTime");
  }

  public void Var078() {
    testLocalTimeFailure("P_VARBINARY_20", "LocalTime");
  }

  public void Var079() {
    testLocalTimeFailure("P_DATE", "LocalTime");
  }

  public void Var080() {
    testLocalTimeParameter("P_TIME", 0, 0, 0, "00:00:00");
  }

  public void Var081() {
    testLocalTimeParameter("P_TIME", 2, 3, 4, "02:03:04");
  }

  public void Var082() {
    testLocalTimeParameter("P_TIME", 23, 59, 59, "23:59:59");
  }

  public void Var083() {
    testLocalTimeFailure("P_TIMESTAMP", "LocalTime");
  }

  public void Var084() {
    notApplicable("Skipping 'P_DATALINK' as is VARCHAR");
  }

  public void Var085() {
    testLocalTimeFailure("P_BLOB", "LocalTime");
  }

  public void Var086() {
    testLocalTimeFailure("P_CLOB", "LocalTime");
  }

  public void Var087() {
    testLocalTimeFailure("P_DBCLOB", "LocalTime");
  }

  public void Var088() {
    testLocalTimeFailure("P_BIGINT", "LocalTime");
  }

  public void Var089() {
    testLocalTimeFailure("P_DECFLOAT16", "LocalTime");
  }

  public void Var090() {
    testLocalTimeFailure("P_DECFLOAT34", "LocalTime");
  }

  public void Var091() {
    if (checkBooleanSupport())
      testLocalTimeFailure("P_BOOLEAN", "LocalTime");
  }

  public void Var092() {
    notApplicable();
  }

  public void Var093() {
    notApplicable();
  }

  public void Var094() {
    notApplicable();
  }

  public void Var095() {
    notApplicable();
  }

  public void Var096() {
    notApplicable();
  }

  public void Var097() {
    notApplicable();
  }

  public void Var098() {
    notApplicable();
  }

  public void Var099() {
    notApplicable();
  }

  public void Var100() {
    notApplicable();
  }

  /* Test using Local Date */

  public void Var101() {
    testLocalDateFailure("P_SMALLINT", "LocalDate");
  }

  public void Var102() {
    testLocalDateFailure("P_INTEGER", "LocalDate");
  }

  public void Var103() {
    testLocalDateFailure("P_REAL", "LocalDate");
  }

  public void Var104() {
    testLocalDateFailure("P_FLOAT", "LocalDate");
  }

  public void Var105() {
    testLocalDateFailure("P_DOUBLE", "LocalDate");
  }

  public void Var106() {
    testLocalDateFailure("P_DECIMAL_50", "LocalDate");
  }

  public void Var107() {
    testLocalDateFailure("P_DECIMAL_105", "LocalDate");
  }

  public void Var108() {
    testLocalDateFailure("P_NUMERIC_50", "LocalDate");
  }

  public void Var109() {
    testLocalDateFailure("P_NUMERIC_105", "LocalDate");
  }

  public void Var110() {
    testLocalDateFailure("P_CHAR_1", "LocalDate");
  }

  public void Var111() {
    testLocalDateParameter("P_CHAR_50", 1900, 1, 1,
        "1900-01-01                                        ");
  }

  public void Var112() {
    testLocalDateParameter("P_CHAR_50", 2022, 3, 4,
        "2022-03-04                                        ");
  }

  public void Var113() {
    testLocalDateParameter("P_CHAR_50", 2099, 12, 31,
        "2099-12-31                                        ");
  }

  public void Var114() {
    testLocalDateParameter("P_VARCHAR_50", 1900, 1, 1, "1900-01-01");
  }

  public void Var115() {
    testLocalDateParameter("P_VARCHAR_50", 2022, 3, 4, "2022-03-04");
  }

  public void Var116() {
    testLocalDateParameter("P_VARCHAR_50", 2099, 12, 31, "2099-12-31");
  }

  public void Var117() {
    testLocalDateFailure("P_BINARY_20", "LocalDate");
  }

  public void Var118() {
    testLocalDateFailure("P_VARBINARY_20", "LocalDate");
  }

  public void Var119() {
    testLocalDateFailure("P_TIME", "LocalDate");
  }

  public void Var120() {
    testLocalDateParameter("P_DATE", 1970, 1, 1, "1970-01-01");
  }

  public void Var121() {
    testLocalDateParameter("P_DATE", 2022, 3, 4, "2022-03-04");
  }

  public void Var122() {
    testLocalDateParameter("P_DATE", 2099, 12, 31, "2099-12-31");
  }

  public void Var123() {
    testLocalDateFailure("P_TIMESTAMP", "LocalDate");
  }

  public void Var124() {
    notApplicable("Skipping 'P_DATALINK' as is VARCHAR");
  }

  public void Var125() {
    testLocalDateFailure("P_BLOB", "LocalDate");
  }

  public void Var126() {
    testLocalDateFailure("P_CLOB", "LocalDate");
  }

  public void Var127() {
    testLocalDateFailure("P_DBCLOB", "LocalDate");
  }

  public void Var128() {
    testLocalDateFailure("P_BIGINT", "LocalDate");
  }

  public void Var129() {
    testLocalDateFailure("P_DECFLOAT16", "LocalDate");
  }

  public void Var130() {
    testLocalDateFailure("P_DECFLOAT34", "LocalDate");
  }

  public void Var131() {
    if (checkBooleanSupport())
      testLocalDateFailure("P_BOOLEAN", "LocalDate");
  }

  public void Var132() {
    notApplicable();
  }

  public void Var133() {
    notApplicable();
  }

  public void Var134() {
    notApplicable();
  }

  public void Var135() {
    notApplicable();
  }

  public void Var136() {
    notApplicable();
  }

  public void Var137() {
    notApplicable();
  }

  public void Var138() {
    notApplicable();
  }

  public void Var139() {
    notApplicable();
  }

  public void Var140() {
    notApplicable();
  }

  /* Test using LocalDateTime */

  public void Var141() {
    testLocalDateTimeParameter("P_CHAR_50", 1900, 1, 1, 0, 0, 0, 0,
        "1900-01-01 00:00:00.0                             ");
  }

  public void Var142() {
    testLocalDateTimeParameter("P_CHAR_50", 2022, 3, 4, 4, 5, 6, 123456789,
        "2022-03-04 04:05:06.123456789                     ");
  }

  public void Var143() {
    testLocalDateTimeParameter("P_CHAR_50", 2099, 12, 31, 23, 59, 59, 999999999,
        "2099-12-31 23:59:59.999999999                     ");
  }

  public void Var144() {
    testLocalDateTimeParameter("P_VARCHAR_50", 1900, 1, 1, 0, 0, 0, 0,
        "1900-01-01 00:00:00.0");
  }

  public void Var145() {
    testLocalDateTimeParameter("P_VARCHAR_50", 2022, 3, 4, 4, 5, 6, 123456789,
        "2022-03-04 04:05:06.123456789");
  }

  public void Var146() {
    testLocalDateTimeParameter("P_VARCHAR_50", 2099, 12, 31, 23, 59, 59,
        999999999, "2099-12-31 23:59:59.999999999");
  }

  public void Var147() {
    testLocalDateTimeFailure("P_BINARY_20", "LocalDateTime");
  }

  public void Var148() {
    testLocalDateTimeFailure("P_VARBINARY_20", "LocalDateTime");
  }

  public void Var149() {
    testLocalDateTimeParameter("P_TIME", 1900, 1, 1, 0, 0, 0, 0, "00:00:00");
  }

  public void Var150() {
    testLocalDateTimeParameter("P_TIME", 2022, 3, 4, 4, 5, 6, 123456789,
        "04:05:06");
  }

  public void Var151() {
    testLocalDateTimeParameter("P_TIME", 2099, 12, 31, 23, 59, 59, 999999999,
        "23:59:59");
  }

  public void Var152() {
    testLocalDateTimeParameter("P_DATE", 1900, 1, 1, 0, 0, 0, 0, "1900-01-01");
  }

  public void Var153() {
    testLocalDateTimeParameter("P_DATE", 2022, 3, 4, 4, 5, 6, 123456789,
        "2022-03-04");
  }

  public void Var154() {
    testLocalDateTimeParameter("P_DATE", 2099, 12, 31, 23, 59, 59, 999999999,
        "2099-12-31");
  }

  public void Var155() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      testLocalDateTimeParameter("P_TIMESTAMP", 1900, 1, 1, 0, 0, 0, 0,
          "1900-01-01-00.00.00.000000");
    } else {
      testLocalDateTimeParameter("P_TIMESTAMP", 1900, 1, 1, 0, 0, 0, 0,
          "1900-01-01 00:00:00.000000");
    }
  }

  public void Var156() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      testLocalDateTimeParameter("P_TIMESTAMP", 2022, 3, 4, 4, 5, 6, 123456789,
          "2022-03-04-04.05.06.123456");
    } else {
      testLocalDateTimeParameter("P_TIMESTAMP", 2022, 3, 4, 4, 5, 6, 123456789,
          "2022-03-04 04:05:06.123456");
    }
  }

  public void Var157() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      testLocalDateTimeParameter("P_TIMESTAMP", 2099, 12, 31, 23, 59, 59,
          999999999, "2099-12-31-23.59.59.999999");
    } else {
      testLocalDateTimeParameter("P_TIMESTAMP", 2099, 12, 31, 23, 59, 59,
          999999999, "2099-12-31 23:59:59.999999");
    }
  }

  public void Var158() {
    notApplicable("Skipping 'P_DATALINK' as is VARCHAR");
  }

  public void Var159() {
    testLocalDateTimeFailure("P_BLOB", "LocalDateTime");
  }

  public void Var160() {
    testLocalDateTimeFailure("P_CLOB", "LocalDateTime");
  }

  public void Var161() {
    testLocalDateTimeFailure("P_DBCLOB", "LocalDateTime");
  }

  public void Var162() {
    testLocalDateTimeFailure("P_BIGINT", "LocalDateTime");
  }

  public void Var163() {
    testLocalDateTimeFailure("P_DECFLOAT16", "LocalDateTime");
  }

  public void Var164() {
    testLocalDateTimeFailure("P_DECFLOAT34", "LocalDateTime");
  }

  public void Var165() {
    if (checkBooleanSupport())
      testLocalDateTimeFailure("P_BOOLEAN", "LocalDateTime");
  }

  public void Var166() {
    notApplicable();
  }

  public void Var167() {
    notApplicable();
  }

  public void Var168() {
    notApplicable();
  }

  public void Var169() {
    notApplicable();
  }

  public void Var170() {
    notApplicable();
  }

}
