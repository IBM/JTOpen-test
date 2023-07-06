///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetString.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetString.java
//
// Classes:      JDCSSetString
//
////////////////////////////////////////////////////////////////////////
//
// 
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.sql.DataTruncation;
import java.util.Hashtable;

/**
 * Testcase JDCSSetString. This tests the following method of the JDBC
 * CallableStatement class:
 * 
 * <ul>
 * <li>setString()
 * </ul>
 **/
public class JDCSSetString extends JDCSSetTestcase {

  /**
   * Constructor.
   **/
  public JDCSSetString(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDCSSetString", namesAndVars, runMode,
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
   * setString() - Set parameter PARAM_1 which doesn't exist. - Using an ordinal
   * parameter
   **/
  public void Var001() {
    if (checkJdbc20()) {

      try {

        cs.setString(0, "price");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setString() - Should throw an exception when the callable statment is
   * closed. - Using an ordinal parameter
   **/
  public void Var002() {
    if (checkJdbc20()) {

      try {
        cs.close();
        cs.setString(1, "test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setString() - Should work with a valid parameter name. - Using an ordinal
   * parameter
   **/
  public void Var003() {
    if (checkJdbc20()) {

      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(11, "Kim");
        cs.execute();
        String check = cs.getString(11);
        cs.close();
        assertCondition(
            check.equals("Kim                                               "));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Should throw exception when the parameter is not an input
   * parameter. - Using an ordinal parameter
   **/
  public void Var004() {
    if (checkJdbc20()) {

      try {

        CallableStatement cs1 = connection_
            .prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
        cs1.setString(2, "Charlie");
        cs1.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * setString() - Verify that a data truncation warning is posted when data is
   * truncated. - Using an ordinal parameter
   **/
  public void Var005() {
    if (checkJdbc20()) {

      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(11,
            "This string is a little bit longer than fifty characters.");
        failed("Didn't throw SQLException");
      } catch (DataTruncation dt) {
        try {
          assertCondition(
              (dt.getIndex() == 11) && (dt.getParameter() == true)
                  && (dt.getRead() == false) && (dt.getDataSize() == 57)
                  && (dt.getTransferSize() == 50),
              dt.getIndex() + " " + dt.getParameter() + " " + dt.getRead() + " "
                  + dt.getDataSize() + " " + dt.getTransferSize());

          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Set a SMALLINT parameter. - Using an ordinal parameter
   **/
  public void Var006() {
    if (checkJdbc20()) {

      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(1, "Test");
        cs.execute();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a INTEGER parameter. - Using an ordinal parameter
   **/
  public void Var007() {
    if (checkJdbc20()) {
      try {

        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(2, "Test");
        cs.execute();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a REAL parameter. - Using an ordinal parameter
   **/
  public void Var008() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(3, "Test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a FLOAT parameter. - Using an ordinal parameter
   **/
  public void Var009() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(4, "test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a DOUBLE parameter. - Using an ordinal parameter
   **/
  public void Var010() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(5, "Test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a DECIMAL parameter. - Using an ordinal parameter
   **/
  public void Var011() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(6, "Test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a NUMERIC parameter - Using an ordinal parameter
   **/
  public void Var012() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(8, "test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a CHAR(1) parameter. - Using an ordinal parameter
   **/
  public void Var013() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(10, "K");
        cs.execute();
        String check = cs.getString(10);
        cs.close();
        assertCondition(check.equals("K"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Set a CHAR(50) parameter. - Using an ordinal parameter
   **/
  public void Var014() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(11, "Kim");
        cs.execute();
        String check = cs.getString(11);
        cs.close();
        assertCondition(
            check.equals("Kim                                               "));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Set a VARCHAR(50) parameter. - Using an ordinal parameter
   **/
  public void Var015() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(12, "Kimberly");
        cs.execute();
        String check = cs.getString(12);
        cs.close();
        assertCondition(check.equals("Kimberly"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Set a CLOB parameter. - Using an ordinal parameter
   **/
  public void Var016() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());
          cs.setString(20, "Kim Coover");
          cs.execute();

          String p = cs.getString(20);
          assertCondition(p.equals("Kim Coover"),
              "p = " + p + ", AND SHOULD equal Kim Coover");
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a BLOB parameter. - Using an ordinal parameter
   **/
  public void Var017() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(19, "Test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a DATE parameter to a value that is not a valid date.
   * This should throw an exception. - Using an ordinal parameter
   **/
  public void Var018() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(15, "Test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a TIME parameter to a value that is not a valid date.
   * This should throw an exception. - Using an ordinal parameter
   **/
  public void Var019() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(16, "test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a TIMESTAMP parameter to a value that is not valid. This
   * should throw an exception. - Using an ordinal parameter
   **/
  public void Var020() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(17, "test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a DATALINK parameter. - Using an ordinal parameter
   **/
  public void Var021() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());
          cs.setString(18, "http://w3.rchland.ibm.com");
          cs.execute();
          String check = cs.getString(18);
          cs.close();
          assertCondition(check.equalsIgnoreCase("http://w3.rchland.ibm.com"));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a BIGINT parameter. - Using an ordinal parameter
   **/
  public void Var022() {
    if (checkJdbc20()) {
      if (checkBigintSupport()) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());
          cs.setString(22, "Test");
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          try {
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            cs.close();
          } catch (SQLException s) {
            failed(s, "Unexpected Exception");
          }
        }
      }
    }
  }

  /**
   * setString() - Set a BINARY parameter. - Using an ordinal parameter
   **/
  public void Var023() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        /* This set throws an exception for Toolbox, but not for native */
        cs.setString(13, "Symantec Cafe1234567");
        cs.execute();
        String check = cs.getString(13);
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          String expected = "Symantec Cafe1234567";
          assertCondition(expected.equals(check),
              "Got " + check + " sb " + expected);
        } else {
          failed("Didn't throw SQLException " + check);
        }

      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a VARBINARY parameter. - Using an ordinal parameter
   **/
  public void Var024() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(14, "Symantec Cafe");
        cs.execute();
        String check = cs.getString(14);

        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          assertCondition("Symantec Cafe".equals(check), "Got " + check);
        } else {

          failed("Didn't throw SQLException " + check);
        }

      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set more than one parameter. - Using an ordinal parameter
   **/
  public void Var025() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(10, "K");
        cs.setString(12, "Coover");
        cs.execute();
        String s1 = cs.getString(10);
        String s2 = cs.getString(12);
        cs.close();
        assertCondition((s1.equals("K")) && (s2.equals("Coover")));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Should set to SQL NULL when the value is null. - Using an
   * ordinal parameter
   **/
  public void Var026() {
    if (checkJdbc20()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString(12, null);
        cs.execute();
        String check = cs.getString(12);
        boolean wn = cs.wasNull();
        assertCondition((check == null) && (wn == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Setting parameters with ordinals and with names in the same
   * statement, should throw an SQLException. - Using an ordinal parameter -
   * Using a named parameter
   **/
  public void Var027() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_CHAR_1", "K");
        cs.setString(12, null);
        cs.execute();
        String check1 = cs.getString(10);
        String check2 = cs.getString(12);
        assertCondition((check1.equals("K")) && (check2 == null));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Set parameter PARAM_1 which doesn't exist. - Using a named
   * parameter
   **/
  public void Var028() {
    if (checkNamedParametersSupport()) {
      try {

        cs.setString("PARAM_1", "price");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setString() - Should throw an exception when the callable statment is
   * closed. - Using a named parameter
   **/
  public void Var029() {
    if (checkNamedParametersSupport()) {
      try {
        cs.close();
        cs.setString("P_SMALLINT", "test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setString() - Should work with a valid parameter name. - Using a named
   * parameter
   **/
  public void Var030() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_CHAR_50", "Kim");
        cs.execute();
        String check = cs.getString(11);
        cs.close();
        assertCondition(
            check.equals("Kim                                               "));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Should throw exception when the parameter is not an input
   * parameter. - Using a named parameter
   **/
  public void Var031() {
    if (checkNamedParametersSupport()) {
      try {

        CallableStatement cs1 = connection_
            .prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
        cs1.setString("P2", "Charlie");
        cs1.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * setString() - Verify that a data truncation warning is posted when data is
   * truncated. - Using a named parameter
   **/
  public void Var032() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_CHAR_50",
            "This string is a little bit longer than fifty characters.");
        failed("Didn't throw SQLException");
      } catch (DataTruncation dt) {
        try {
          assertCondition(
              (dt.getIndex() == 11) && (dt.getParameter() == true)
                  && (dt.getRead() == false) && (dt.getDataSize() == 57)
                  && (dt.getTransferSize() == 50),
              dt.getIndex() + " " + dt.getParameter() + " " + dt.getRead() + " "
                  + dt.getDataSize() + " " + dt.getTransferSize());
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Set a SMALLINT parameter. - Using a named parameter
   **/
  public void Var033() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_SMALLINT", "Test");
        cs.execute();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a INTEGER parameter. - Using a named parameter
   **/
  public void Var034() {
    if (checkNamedParametersSupport()) {
      try {

        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_INTEGER", "Test");
        cs.execute();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a REAL parameter. - Using a named parameter
   **/
  public void Var035() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_REAL", "Test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a FLOAT parameter. - Using a named parameter
   **/
  public void Var036() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_FLOAT", "Test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a DOUBLE parameter. - Using a named parameter
   **/
  public void Var037() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_DOUBLE", "Test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a DECIMAL parameter. - Using a named parameter
   **/
  public void Var038() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_DECIMAL_50", "Test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a NUMERIC parameter - Using a named parameter
   **/
  public void Var039() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_NUMERIC_50", "Test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a CHAR(1) parameter. - Using a named parameter
   **/
  public void Var040() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_CHAR_1", "K");
        cs.execute();
        String check = cs.getString(10);
        cs.close();
        assertCondition(check.equals("K"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Set a CHAR(50) parameter. - Using a named parameter
   **/
  public void Var041() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_CHAR_50", "Kim");
        cs.execute();
        String check = cs.getString(11);
        cs.close();
        assertCondition(
            check.equals("Kim                                               "));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Set a VARCHAR(50) parameter. - Using a named parameter
   **/
  public void Var042() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_VARCHAR_50", "Kimberly");
        cs.execute();
        String check = cs.getString(12);
        cs.close();
        assertCondition(check.equals("Kimberly"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Set a CLOB parameter. - Using a named parameter
   **/
  public void Var043() {
    if (checkNamedParametersSupport()) {
      if (checkLobSupport()) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());
          cs.setString("P_CLOB", "Kim Coover");
          cs.execute();
          String p = cs.getString(20);
          assertCondition(p.equals("Kim Coover"),
              "p = " + p + ", AND SHOULD be Kim Coover");
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a BLOB parameter. - Using a named parameter
   **/
  public void Var044() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_BLOB", "Test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a DATE parameter to a value that is not a valid date.
   * This should throw an exception. - Using a named parameter
   **/
  public void Var045() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_DATE", "Test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a TIME parameter to a value that is not a valid date.
   * This should throw an exception. - Using a named parameter
   **/
  public void Var046() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_TIME", "Test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a TIMESTAMP parameter to a value that is not valid. This
   * should throw an exception. - Using a named parameter
   **/
  public void Var047() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_TIMESTAMP", "Test");
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a DATALINK parameter. - Using a named parameter
   **/
  public void Var048() {
    if (checkNamedParametersSupport()) {
      if (checkLobSupport()) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());
          cs.setString("P_DATALINK", "http://w3.rchland.ibm.com");
          cs.execute();
          String check = cs.getString(18);
          cs.close();
          assertCondition(check.equalsIgnoreCase("http://w3.rchland.ibm.com"));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a BIGINT parameter. - Using a named parameter
   **/
  public void Var049() {
    if (checkNamedParametersSupport()) {
      if (checkBigintSupport()) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());
          cs.setString("P_BIGINT", "Test");
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          try {
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            cs.close();
          } catch (SQLException s) {
            failed(s, "Unexpected Exception");
          }
        }
      }
    }
  }

  /**
   * setString() - Set a BINARY parameter. - Using a named parameter
   **/
  public void Var050() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_BINARY_20", "Symantec Cafe1234567");
        cs.execute();
        String check = cs.getString(13);

        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          String expected = "Symantec Cafe1234567";
          assertCondition(expected.equals(check),
              "Got " + check + " sb " + expected);
        } else {

          failed("Didn't throw SQLException " + check);
        }
      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set a VARBINARY parameter. - Using a named parameter
   **/
  public void Var051() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_VARBINARY_20", "Symantec Cafe");
        cs.execute();
        String check = cs.getString(14);
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          assertCondition("Symantec Cafe".equals(check), "Got " + check);
        } else {

          failed("Didn't throw SQLException " + check);
        }

      } catch (Exception e) {
        try {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          cs.close();
        } catch (SQLException s) {
          failed(s, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setString() - Set more than one parameter. - Using a named parameter
   **/
  public void Var052() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_CHAR_1", "K");
        cs.setString("P_VARCHAR_50", "Coover");
        cs.execute();
        String s1 = cs.getString(10);
        String s2 = cs.getString(12);
        cs.close();
        assertCondition((s1.equals("K")) && (s2.equals("Coover")));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Should set to SQL NULL when the value is null. - Using a
   * named parameter
   **/
  public void Var053() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_VARCHAR_50", null);
        cs.execute();
        String check = cs.getString(12);
        boolean wn = cs.wasNull();
        cs.close();
        assertCondition((check == null) && (wn == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Attempt to set the return value parameter. - Using an ordinal
   * parameter
   **/
  public void Var054() {
    if (checkJdbc20()) {
      if (checkReturnValueSupport()) {
        try {
          CallableStatement cs2 = connection_
              .prepareCall("?=CALL " + JDSetupProcedure.STP_CSRV);
          cs2.setString(1, "444f");
          cs2.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setString() - Attempt to set a mixed case name not in quotes. - Using a
   * named parameter
   **/
  public void Var055() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("P_mIxEd", "smile");
        cs.execute();
        String check = cs.getString(24);
        cs.close();
        assertCondition(check.equals("smile"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Attempt to set a mixed case name when it is in quotes. -
   * Using a named parameter
   **/
  public void Var056() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("\"P_mIxEd\"", "smile");
        cs.execute();
        String check = cs.getString(24);
        cs.close();
        assertCondition(check.equals("smile"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Attempt to set a lower case name when it is not in quotes. -
   * Using a named parameter
   **/
  public void Var057() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("p_lower", "cool");
        cs.execute();
        String check = cs.getString(23);
        cs.close();
        assertCondition(check.equals("cool"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Attempt to set a lower case name when it is in quotes. -
   * Using a named parameter
   **/
  public void Var058() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("\"p_lower\"", "hello!");
        cs.execute();
        String check = cs.getString(23);
        cs.close();
        assertCondition(check.equals("hello!"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Attempt to set an upper case name when it is in quotes. -
   * Using a named parameter
   **/
  public void Var059() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setString("\"P_VARCHAR_50\"", "awesome");
        cs.execute();
        String check = cs.getString(12);
        assertCondition(check.equals("awesome"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Attempt to set a parameter twice. - Using an ordinal
   * parameter
   **/
  public void Var060() {
    if (checkJdbc20()) {
      try {
        cs.setString(10, "C");
        cs.setString(10, "K");
        cs.execute();
        String check = cs.getString(10);
        assertCondition(check.equals("K"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() - Attempt to set a parameter twice. - Using a named parameter
   **/
  public void Var061() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setString("P_CHAR_1", "C");
        cs.setString("P_CHAR_1", "K");
        cs.execute();
        String check = cs.getString(10);
        assertCondition(check.equals("K"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setString() -- Use various decimal formats. This detects a bug in the
   * native JDBC driver in the SQLDataFactory.convertScientificNotation method
   **/

  public void Var062() {
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
        // Note: On V5R4 the native driver puts out all the decimal places, even
        // when running 1.6

        cs1.registerOutParameter(2, Types.VARCHAR);
        cs1.setString(1, input);
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
   * setString() - Attempt to set a named parameter where the name does NOT
   * match the stored procdure definition. Toolbox fixed August 2012.
   **/
  public void Var063() {
    if (checkNamedParametersSupport()) {
      StringBuffer sb = new StringBuffer();
      sb.append(
          " -- Testcase added  08/20/2012 to test parameter where the name does not match the stored procedure definition\n");
      try {
        String procedureName = JDCSTest.COLLECTION + ".COMBINESTRINGS";
        Statement stmt = connection_.createStatement();
        sql = "";
        try {
          sql = "DROP PROCEDURE " + procedureName;
          sb.append("Executing " + sql + "\n");
          stmt.executeUpdate(sql);
        } catch (Exception e) {
          sb.append("Tolerated exception for " + sql + "\n");
        }

        sql = "CREATE PROCEDURE " + procedureName
            + " (IN A VARCHAR(80), IN B VARCHAR(80), OUT C VARCHAR(170)) LANGUAGE SQL  BEGIN SET C = A || ' || ' || B  ; END";
        sb.append("Executing " + sql + "\n");
        stmt.executeUpdate(sql);

        String[][] testcases = {
            { "CALL " + procedureName + "('A',?,?)", "B", "B", "A || B" },
            { "CALL " + procedureName + "(?,'B',?)", "A", "A", "A || B" }, };

        boolean passed = true;
        for (int i = 0; i < testcases.length; i++) {
          sql = testcases[i][0];
          String parameterName = testcases[i][1];
          String value = testcases[i][2];
          String expectedResult = testcases[i][3];
          sb.append("Preparing " + sql + "\n");
          CallableStatement cstmt = connection_.prepareCall(sql);
          cstmt.registerOutParameter(2, Types.VARCHAR);
          sb.append(
              "Setting parameter " + parameterName + " to " + value + "\n");
          cstmt.setString(parameterName, value);
          sb.append("Executing statement\n");
          cstmt.execute();
          sb.append("Getting output string \n");
          String result = cstmt.getString("C");
          if (expectedResult.equals(result)) {
            sb.append("Success:  " + expectedResult + " = " + result + "\n");
          } else {
            sb.append(
                "Failure:  got " + result + " sb " + expectedResult + "\n");
            passed = false;
          }
          cstmt.close();
        }

        sql = "DROP PROCEDURE " + procedureName;
        sb.append("Executing " + sql + "\n");
        stmt.executeUpdate(sql);

        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception " + sb.toString());
      }
    }
  }

  /**
   * setString() - Set a BOOLEAN parameter. - Using a named parameter
   **/
  public void Var064() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setString", "P_BOOLEAN", "1", "1",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setString() - Set a BOOLEAN parameter. - Using a named parameter
   **/
  public void Var065() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setString", "P_BOOLEAN", "0", "0",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setString() - Set a BOOLEAN parameter. - Using a named parameter
   **/
  public void Var066() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setString", "P_BOOLEAN", "true", "1",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setString() - Set a BOOLEAN parameter. - Using a named parameter
   **/
  public void Var067() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setString", "P_BOOLEAN", "false", "0",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setString() - Set a BOOLEAN parameter. - Using a named parameter
   **/
  public void Var068() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setString", "P_BOOLEAN", "T", "1",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setString() - Set a BOOLEAN parameter. - Using a named parameter
   **/
  public void Var069() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setString", "P_BOOLEAN", "F", "0",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setString() - Set a BOOLEAN parameter. - Using a named parameter
   **/
  public void Var070() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setString", "P_BOOLEAN", "yes", "1",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setString() - Set a BOOLEAN parameter. - Using a named parameter
   **/
  public void Var071() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setString", "P_BOOLEAN", "no", "0",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

 
  /**
   * setString() - Set a BOOLEAN parameter. - Using a parameter number
   **/
  public void Var072() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setString", 27, "1", "1",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setString() - Set a BOOLEAN parameter. - Using a parameter number
   **/
  public void Var073() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setString", 27, "0", "0",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setString() - Set a BOOLEAN parameter. - Using a parameter number
   **/
  public void Var074() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setString", 27, "true", "1",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setString() - Set a BOOLEAN parameter. - Using a parameter number
   **/
  public void Var075() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setString", 27, "false", "0",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setString() - Set a BOOLEAN parameter. - Using a parameter number
   **/
  public void Var076() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setString", 27, "T", "1",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setString() - Set a BOOLEAN parameter. - Using a parameter number
   **/
  public void Var077() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setString", 27, "F", "0",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setString() - Set a BOOLEAN parameter. - Using a parameter number
   **/
  public void Var078() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setString", 27, "yes", "1",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setString() - Set a BOOLEAN parameter. - Using a parameter number
   **/
  public void Var079() {
    
    if (checkBooleanSupport()) {
      setTestSuccessful("setString", 27, "no", "0",
          " -- call setString against BOOLEAN parameter -- added January 2021");
    }
  }

  
}
