///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetDate.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetDate.java
//
// Classes:      JDCSSetDate
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDSetupProcedure;

import java.io.FileOutputStream;
  import java.util.Hashtable;
import java.sql.CallableStatement;
import java.sql.Date;
import java.util.Calendar;
import java.sql.Timestamp;

/**
 * Testcase JDCSSetDate. This tests the following method of the JDBC
 * CallableStatement class:
 * 
 * <ul>
 * <li>setDate()
 * </ul>
 **/
public class JDCSSetDate extends JDCSSetTestcase {

  /**
   * Constructor.
   **/
  public JDCSSetDate(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDCSSetDate", namesAndVars, runMode, fileOutputStream,
 password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    super.setup();
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    super.cleanup();
  }

  /**
   * setDate() - Should throw an exception when the callable statement is
   * closed. - Using an ordinal parameter
   **/
  public void Var001() {
    if (checkJdbc20()) {
      try {
        cs.close();
        cs.setDate(15, new Date(235445454));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Should throw an exception when an invalid index is used. -
   * Using an ordinal parameter
   **/
  public void Var002() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);

        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setDate(100, new Date(3454644));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Should throw an exception when the index is 0. - Using an
   * ordinal parameter
   **/
  public void Var003() {
    if (checkJdbc20()) {
      try {
        cs.setDate(0, new Date(24574744));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Should throw an exception when the index is -1. - Using an
   * ordinal parameter
   **/
  public void Var004() {
    if (checkJdbc20()) {
      try {
        cs.setDate(-1, new Date(26565665));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Should set to SQL NULL when the value is null. - Using an
   * ordinal parameter
   **/
  public void Var005() {
    if (checkJdbc20()) {
      try {
        cs.setDate(15, null);
        cs.execute();
        Date check = cs.getDate(15);
        boolean wn = cs.wasNull();
        assertCondition((check == null) && (wn == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setDate() - Should work with a valid parameter index. - Using an ordinal
   * parameter
   **/
  public void Var006() {
    if (checkJdbc20()) {
      try {
        Date t = new Date(-2242644);
        cs.setDate(15, t);
        cs.execute();
        Date check = cs.getDate(15);
        assertCondition(check.toString().equals(t.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setDate() - Should throw an exception when the calendar is null. - Using an
   * ordinal parameter
   **/
  public void Var007() {
    if (checkJdbc20()) {
      try {
        cs.setDate(15, new Date(7), null);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Should throw an exception when the parameter is not an input
   * parameter. - Using an ordinal parameter
   **/
  public void Var008() {
    if (checkJdbc20()) {
      try {
        CallableStatement cs1 = connection_
            .prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
        cs1.setDate(2, new Date(22));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a SMALLINT parameter. - Using an ordinal parameter
   **/
  public void Var009() {
    if (checkJdbc20()) {
      try {
        cs.setDate(1, new Date(21));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set an INTEGER parameter. - Using an ordinal parameter
   **/
  public void Var010() {
    if (checkJdbc20()) {
      try {
        cs.setDate(2, new Date(33));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a REAL parameter. - Using an ordinal parameter
   **/
  public void Var011() {
    if (checkJdbc20()) {
      try {
        cs.setDate(3, new Date(66));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a FLOAT parameter. - Using an ordinal parameter
   **/
  public void Var012() {
    if (checkJdbc20()) {
      try {
        cs.setDate(4, new Date(21));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a DOUBLE parameter. - Using an ordinal parameter
   **/
  public void Var013() {
    if (checkJdbc20()) {
      try {
        cs.setDate(5, new Date(2));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a DECIMAL parameter. - Using an ordinal parameter
   **/
  public void Var014() {
    if (checkJdbc20()) {
      try {
        cs.setDate(7, new Date(22));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a NUMERIC parameter. - Using an ordinal parameter
   **/
  public void Var015() {
    if (checkJdbc20()) {
      try {
        cs.setDate(8, new Date(44));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a CHAR(50) parameter. - Using an ordinal parameter
   **/
  public void Var016() {
    if (checkJdbc20()) {
      try {
        Date t = Date.valueOf("1999-05-31");
        cs.setDate(11, t);
        cs.execute();
        String check = cs.getString(11);
        if (isToolboxDriver())
          assertCondition(check
              .equals("05/31/99                                          "));
        else
          assertCondition(check
              .equals("1999-05-31                                        "));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setDate() - Set a VARCHAR(50) parameter. - Using an ordinal parameter
   **/
  public void Var017() {
    if (checkJdbc20()) {
      try {
        Date t = Date.valueOf("1980-06-25");
        cs.setDate(12, t);
        cs.execute();
        String check = cs.getString(12);
        if (isToolboxDriver())
          assertCondition(check.equals("06/25/80"));
        else
          assertCondition(check.equals("1980-06-25"));
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a CLOB parameter. - Using an ordinal parameter
   **/
  public void Var018() {
    if (checkJdbc20()) {
      try {
        cs.setDate(20, new Date(3));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a DBCLOB parameter. - Using an ordinal parameter
   **/
  public void Var019() {
    if (checkJdbc20()) {
      try {
        cs.setDate(21, new Date(8));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a BINARY parameter. - Using an ordinal parameter
   **/
  public void Var020() {
    if (checkJdbc20()) {
      try {
        cs.setDate(13, new Date(33));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a VARBINARY parameter. - Using an ordinal parameter
   **/
  public void Var021() {
    if (checkJdbc20()) {
      try {
        cs.setDate(14, new Date(4));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a BLOB parameter. - Using an ordinal parameter
   **/
  public void Var022() {
    if (checkJdbc20()) {
      try {
        cs.setDate(19, new Date(12));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a DATE parameter. - Using an ordinal parameter
   **/
  public void Var023() {
    if (checkJdbc20()) {
      try {
        Date t = new Date(34894589);
        cs.setDate(15, t, Calendar.getInstance());
        cs.execute();
        Date check = cs.getDate(15);
        assertCondition(check.toString().equals(t.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setDate() - Set a Date parameter with a calendar specified. - Using an
   * ordinal parameter
   **/
  public void Var024() {
    if (checkJdbc20()) {
      try {
        Date t = new Date(1035645632);
        cs.setDate(15, t, Calendar.getInstance());
        cs.execute();
        Date check = cs.getDate(15);
        assertCondition(check.toString().equals(t.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setDate() - Set a TIME parameter. - Using an ordinal parameter
   **/
  public void Var025() {
    if (checkJdbc20()) {
      try {
        cs.setDate(16, new Date(3434343));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a TIMESTAMP parameter. - Using an ordinal parameter
   **/
  public void Var026() {
    if (checkJdbc20()) {
      try {
        Date t = new Date(34324324);
        cs.setDate(17, t, Calendar.getInstance());
        cs.execute();
        Timestamp check = cs.getTimestamp(17);

        // If running with V5R4 group test tz is "UTC"
        String tz = System.getProperty("user.timezone");
        if ("UTC".equals(tz)) {
          assertCondition(check.toString().equals("1970-01-01 09:32:04.324"),
              " got " + check.toString() + " sb 1970-01-01 09:32:04.324  tz="
                  + tz);

        } else if ("America/Chicago".equals(tz) || "GMT-6".equals(tz)) {
          assertCondition(check.toString().equals("1970-01-01 03:32:04.324"),
              " got " + check.toString() + " sb   1970-01-01 03:32:04.324 tz="
                  + tz);
        } else if (isToolboxDriver()   || (System.getProperty("java.home").indexOf("openjdk")>=0)) {
          assertCondition(check.toString().equals("1970-01-01 03:32:04.324"),
              " got " + check.toString() + " sb   1970-01-01 03:32:04.324 tz="
                  + tz);
        } else {
          // if native or running Toolbox on 400.
          assertCondition(check.toString().equals("1970-01-01 09:32:04.324"),
              " got " + check.toString() + " sb 1970-01-01 09:32:04.324  tz="
                  + tz);
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * setDate() - Set a DATALINK parameter. In the procedure the datalink is
   * represented as a VARCHAR(). That is why this test will pass. - Using an
   * ordinal parameter
   **/
  public void Var027() {
    if (checkJdbc20()) {
      try {
        Date t = Date.valueOf("1922-07-14");
        cs.setDate(18, t);
        cs.execute();
        String check = cs.getString(18);
        if (isToolboxDriver())
          assertCondition(check.equals("07/14/22"));
        else
          assertCondition(check.equals("1922-07-14"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setDate() - Set a BIGINT parameter. - Using an ordinal parameter
   **/
  public void Var028() {
    if (checkJdbc20()) {
      if (checkBigintSupport()) {
        try {
          cs.setDate(22, new Date(34));
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setDate() - Should throw an exception when the callable statement is
   * closed. - Using a named parameter
   **/
  public void Var029() {
    if (checkNamedParametersSupport()) {
      try {
        cs.close();
        cs.setDate("P_DATE", new Date(235445454));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Should throw an exception when an invalid index is used. -
   * Using a named parameter
   **/
  public void Var030() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);

        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setDate("PARAM", new Date(3454644));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Should throw an exception when the value is null. - Using a
   * named parameter
   **/
  public void Var031() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setDate("P_DATE", null);
        cs.execute();
        Date check = cs.getDate(15);
        boolean wn = cs.wasNull();
        assertCondition((check == null) && (wn == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setDate() - Should work with a valid parameter index. - Using a named
   * parameter
   **/
  public void Var032() {
    if (checkNamedParametersSupport()) {
      try {
        Date t = new Date(-2242644);
        cs.setDate("P_DATE", t);
        cs.execute();
        Date check = cs.getDate(15);
        assertCondition(check.toString().equals(t.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setDate() - Should throw an exception when the calendar is null. - Using a
   * named parameter
   **/
  public void Var033() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setDate("P_DATE", new Date(7), null);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Should throw an exception when the parameter is not an input
   * parameter. - Using a named parameter
   **/
  public void Var034() {
    if (checkNamedParametersSupport()) {
      try {
        CallableStatement cs1 = connection_
            .prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
        cs1.setDate("P2", new Date(22));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a SMALLINT parameter. - Using a named parameter
   **/
  public void Var035() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setDate("P_SMALLINT", new Date(21));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set an INTEGER parameter. - Using a named parameter
   **/
  public void Var036() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setDate("P_INTEGER", new Date(33));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a REAL parameter. - Using a named parameter
   **/
  public void Var037() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setDate("P_REAL", new Date(66));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a FLOAT parameter. - Using a named parameter
   **/
  public void Var038() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setDate("P_FLOAT", new Date(21));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a DOUBLE parameter. - Using a named parameter
   **/
  public void Var039() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setDate("P_DOUBLE", new Date(2));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a DECIMAL parameter. - Using a named parameter
   **/
  public void Var040() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setDate("P_DECIMAL_105", new Date(22));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a NUMERIC parameter. - Using a named parameter
   **/
  public void Var041() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setDate("P_NUMERIC_50", new Date(44));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a CHAR(50) parameter. - Using a named parameter
   **/
  public void Var042() {
    if (checkNamedParametersSupport()) {
      try {
        Date t = Date.valueOf("1999-05-31");
        cs.setDate("P_CHAR_50", t);
        cs.execute();
        String check = cs.getString(11);
        if (isToolboxDriver())
          assertCondition(check
              .equals("05/31/99                                          "));
        else
          assertCondition(check
              .equals("1999-05-31                                        "));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setDate() - Set a VARCHAR(50) parameter. - Using a named parameter
   **/
  public void Var043() {
    if (checkNamedParametersSupport()) {
      try {
        Date t = Date.valueOf("1980-06-25");
        cs.setDate("P_VARCHAR_50", t);
        cs.execute();
        String check = cs.getString(12);
        if (isToolboxDriver())
          assertCondition(check.equals("06/25/80"));
        else
          assertCondition(check.equals("1980-06-25"));
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a CLOB parameter. - Using a named parameter
   **/
  public void Var044() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setDate("P_CLOB", new Date(3));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a DBCLOB parameter. - Using a named parameter
   **/
  public void Var045() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setDate("P_DBCLOB", new Date(8));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a BINARY parameter. - Using a named parameter
   **/
  public void Var046() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setDate("P_BINARY_20", new Date(33));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a VARBINARY parameter. - Using a named parameter
   **/
  public void Var047() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setDate("P_VARBINARY_20", new Date(4));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a BLOB parameter. - Using a named parameter
   **/
  public void Var048() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setDate("P_BLOB", new Date(12));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a DATE parameter. - Using a named parameter
   **/
  public void Var049() {
    if (checkNamedParametersSupport()) {
      try {
        Date t = new Date(34894589);
        cs.setDate("P_DATE", t, Calendar.getInstance());
        cs.execute();
        Date check = cs.getDate(15);
        assertCondition(check.toString().equals(t.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setDate() - Set a Date parameter with a calendar specified. - Using a named
   * parameter
   **/
  public void Var050() {
    if (checkNamedParametersSupport()) {
      try {
        Date t = new Date(1035645632);
        cs.setDate("P_DATE", t, Calendar.getInstance());
        cs.execute();
        Date check = cs.getDate(15);
        assertCondition(check.toString().equals(t.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setDate() - Set a TIME parameter. - Using a named parameter
   **/
  public void Var051() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setDate("P_TIME", new Date(3434343));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a TIMESTAMP parameter. - Using a named parameter
   **/
  public void Var052() {
    if (checkNamedParametersSupport()) {
      try {
        Date t = new Date(34324324);
        cs.setDate("P_TIMESTAMP", t, Calendar.getInstance());
        cs.execute();
        Timestamp check = cs.getTimestamp(17);
        // System.out.println("Value is " + check);
        String userTimeZone = System.getProperty("user.timezone");
        String tz = userTimeZone;
        if (userTimeZone == null)
          userTimeZone = "CST";
        if ((isToolboxDriver() && (!userTimeZone.equals("UTC")))  || (System.getProperty("java.home").indexOf("openjdk")>=0)){
          assertCondition(check.toString().equals("1970-01-01 03:32:04.324"),
              "check.toString() is " + check.toString()
                  + " sb  1970-01-01 03:32:04.324");
        } else if ("America/Chicago".equals(tz) || "GMT-6".equals(tz)) {
          assertCondition(check.toString().equals("1970-01-01 03:32:04.324"),
              " got " + check.toString() + " sb   1970-01-01 03:32:04.324 tz="
                  + tz);
        } else {
          assertCondition(check.toString().equals("1970-01-01 09:32:04.324"),
              "check.toString() is " + check.toString()
                  + " sb  1970-01-01 09:32:04.324 tz=" + tz);
        }

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * setDate() - Set a DATALINK parameter. In the procedure the datalink is
   * represented as a VARCHAR(). That is why this test will pass. - Using a
   * named parameter
   **/
  public void Var053() {
    if (checkNamedParametersSupport()) {
      try {
        Date t = Date.valueOf("1922-07-14");
        cs.setDate("P_DATALINK", t);
        cs.execute();
        String check = cs.getString(18);
        if (isToolboxDriver())
          assertCondition(check.equals("07/14/22"));
        else
          assertCondition(check.equals("1922-07-14"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setDate() - Set a BIGINT parameter. - Using a named parameter
   **/
  public void Var054() {
    if (checkNamedParametersSupport()) {
      if (checkBigintSupport()) {
        try {
          cs.setDate("P_BIGINT", new Date(33));
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setDate() - Should throw an exception if an ordinal and a named parameter
   * are used for the same callable statement. - Using an ordinal parameter -
   * Using a named parameter
   **/
  public void Var055() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setString(12, "Hello");
        Date t = new Date(1035645632);
        cs.setDate("P_DATE", t, Calendar.getInstance());
        cs.execute();
        // I'm feeling lazy... if we were able to execute, the test worked.
        assertCondition(true);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setDate() - Should be recognized since the lower case name is not in
   * quotes. - Using a named parameter
   **/
  public void Var056() {
    if (checkNamedParametersSupport()) {
      try {
        Date t = new Date(-2242644);
        cs.setDate("p_date", t);
        cs.execute();
        Date check = cs.getDate(15);
        assertCondition(check.toString().equals(t.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setDate() - Should be recognized since the mixed case name is not in
   * quotes. - Using a named parameter
   **/
  public void Var057() {
    if (checkNamedParametersSupport()) {
      try {
        Date t = new Date(-2242644);
        cs.setDate("P_dAtE", t);
        cs.execute();
        Date check = cs.getDate(15);
        assertCondition(check.toString().equals(t.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setDate() - Should throw an exception since the lower case name is in
   * quotes. - Using a named parameter
   **/
  public void Var058() {
    if (checkNamedParametersSupport()) {
      try {
        Date t = new Date(-2242644);
        cs.setDate("'p_date'", t);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Should throw an exception since the mixed case name is in
   * quotes. - Using a named parameter
   **/
  public void Var059() {
    if (checkNamedParametersSupport()) {
      try {
        Date t = new Date(-2242644);
        cs.setDate("'P_dAtE'", t);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setDate() - Set a BOOLEAN parameter. - Using an ordinal parameter
   */
  public void Var060() {

    if (checkBooleanSupport()) {

      setTestFailure("setDate", 27, new Date(7),
          "Added 2021/01/07 to test boolean support");
    }

  }

  /**
   * setDate() - Set a BOOLEAN parameter. - Using a named parameter
   */
  public void Var061() {

    if (checkBooleanSupport()) {

      setTestFailure("setDate", "P_BOOLEAN", new Date(7),
          "Added 2021/01/07 to test boolean support");
    }

  }
}
