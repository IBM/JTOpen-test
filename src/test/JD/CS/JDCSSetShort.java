///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetShort.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetShort.java
//
// Classes:      JDCSSetShort
//
////////////////////////////////////////////////////////////////////////
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDSetupProcedure;

/**
 * Testcase JDCSSetShort. This tests the following method of the JDBC
 * CallableStatement class:
 * 
 * <ul>
 * <li>setShort()
 * </ul>
 **/
public class JDCSSetShort extends JDCSSetTestcase {

  /**
   * Constructor.
   **/
  public JDCSSetShort(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDCSSetShort", namesAndVars, runMode, fileOutputStream,
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
   * setShort() - Should throw exception when the prepared statement is closed.
   * - Using an ordinal parameter
   **/
  public void Var001() {
    if (checkJdbc20()) {
      try {
        cs.close();
        cs.setShort(1, (short) 5);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Should throw exception when an invalid index is specified. -
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
        cs.setShort(100, (short) 333);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Should work with a valid parameter index greater than 1. -
   * Using an ordinal parameter
   **/
  public void Var003() {
    if (checkJdbc20()) {
      try {
        cs.setShort(1, (short) 442);
        cs.execute();
        short check = cs.getShort(1);
        assertCondition(check == 442);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Should throw exception when the parameter is not an input
   * parameter. - Using an ordinal parameter
   **/
  public void Var004() {
    if (checkJdbc20()) {
      try {
        CallableStatement cs1 = connection_
            .prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
        cs1.setShort(2, (short) 442);
        cs1.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Set a SMALLINT parameter. - Using an ordinal parameter
   **/
  public void Var005() {
    if (checkJdbc20()) {
      try {
        cs.setShort(1, (short) -2232);
        cs.execute();
        short check = cs.getShort(1);
        assertCondition(check == -2232);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an INTEGER parameter. - Using an ordinal parameter
   **/
  public void Var006() {
    if (checkJdbc20()) {
      try {
        cs.setShort(2, (short) -1061);
        cs.execute();
        int check = cs.getInt(2);
        assertCondition(check == -1061);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an REAL parameter. - Using an ordinal parameter
   **/
  public void Var007() {
    if (checkJdbc20()) {
      try {
        cs.setShort(3, (short) 179);
        cs.execute();
        float check = cs.getFloat(3);
        assertCondition(check == 179);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an FLOAT parameter. - Using an ordinal parameter
   **/
  public void Var008() {
    if (checkJdbc20()) {
      try {
        cs.setShort(4, (short) 1);
        cs.execute();
        double check = cs.getDouble(4);
        assertCondition(check == 1);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an DOUBLE parameter. - Using an ordinal parameter
   **/
  public void Var009() {
    if (checkJdbc20()) {
      try {
        cs.setShort(5, (short) 18);
        cs.execute();
        double check = cs.getDouble(5);
        assertCondition(check == 18);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an DECIMAL parameter. - Using an ordinal parameter
   **/
  public void Var010() {
    if (checkJdbc20()) {
      try {
        cs.setShort(6, (short) -1001);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(6);
        assertCondition(check.intValue() == -1001);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an NUMERIC parameter. - Using an ordinal parameter
   **/
  public void Var011() {
    if (checkJdbc20()) {
      try {
        cs.setShort(8, (short) 1777);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(8);
        assertCondition(check.intValue() == 1777);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an CHAR(50) parameter. - Using an ordinal parameter
   **/
  public void Var012() {
    if (checkJdbc20()) {
      try {
        cs.setShort(11, (short) 187);
        cs.execute();
        String check = cs.getString(11);
        assertCondition(
            check.equals("187                                               "));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an CHAR(1) parameter. - Using an ordinal parameter
   **/
  public void Var013() {
    if (checkJdbc20()) {
      try {
        cs.setShort(10, (short) -83);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
      }
    }
  }

  /**
   * setShort() - Set an VARCHAR parameter. - Using an ordinal parameter
   **/
  public void Var014() {
    if (checkJdbc20()) {
      try {
        cs.setShort(12, (short) -767);
        cs.execute();
        String check = cs.getString(12);
        assertCondition(check.equals("-767"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set a CLOB parameter. - Using an ordinal parameter
   **/
  public void Var015() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          cs.setShort(20, (short) -22);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setShort() - Set a DBCLOB parameter. - Using an ordinal parameter
   **/
  public void Var016() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          cs.setShort(21, (short) -5);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setShort() - Set a BINARY parameter. - Using an ordinal parameter
   **/
  public void Var017() {
    if (checkJdbc20()) {
      try {
        cs.setShort(13, (short) 4);
        cs.execute();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Set a VARBINARY parameter. - Using an ordinal parameter
   **/
  public void Var018() {
    if (checkJdbc20()) {
      try {
        cs.setShort(14, (short) 19);
        cs.execute();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Set a BLOB parameter. - Using an ordinal parameter
   **/
  public void Var019() {
    if (checkJdbc20()) {
      try {
        cs.setShort(19, (short) 4);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Set a DATE parameter. - Using an ordinal parameter
   **/
  public void Var020() {
    if (checkJdbc20()) {
      try {
        cs.setShort(15, (short) 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Set a TIME parameter. - Using an ordinal parameter
   **/
  public void Var021() {
    if (checkJdbc20()) {
      try {
        cs.setShort(16, (short) 3);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Set a TIMESTAMP parameter. - Using an ordinal parameter
   **/
  public void Var022() {
    if (checkJdbc20()) {
      try {
        cs.setShort(17, (short) -3443);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Set a DATALINK parameter. - Using an ordinal parameter
   **/
  public void Var023() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        try {
          cs.setShort(18, (short) 75);
          cs.execute();
          String check = cs.getString(18);
          assertCondition(check.equals("75"));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setShort() - Set a BIGINT parameter. - Using an ordinal parameter
   **/
  public void Var024() {
    if (checkJdbc20()) {
      if (checkBigintSupport()) {
        try {
          cs.setShort(22, (short) 4031);
          cs.execute();
          long check = cs.getLong(22);
          assertCondition(check == 4031);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setShort() - Set a SMALLINT parameter. - Using an ordinal parameter
   **/
  public void Var025() {
    if (checkJdbc20()) {
      try {
        cs.setString(1, "2232.2");
        cs.execute();
        short check = cs.getShort(1);
        assertCondition(check == 2232);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Should throw exception when the prepared statement is closed.
   * - Using a named parmater
   **/
  public void Var026() {
    if (checkNamedParametersSupport()) {
      try {
        cs.close();
        cs.setShort("P_SMALLINT", (short) 5);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Should throw exception when an invalid index is specified. -
   * Using a named parmater
   **/
  public void Var027() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setShort("PARAM_1", (short) 333);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Should work with a valid parameter index greater than 1. -
   * Using a named parmater
   **/
  public void Var028() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_SMALLINT", (short) 442);
        cs.execute();
        short check = cs.getShort(1);
        assertCondition(check == 442);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Should throw exception when the parameter is not an input
   * parameter. - Using a named parmater
   **/
  public void Var029() {
    if (checkNamedParametersSupport()) {
      try {
        CallableStatement cs1 = connection_
            .prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
        cs.setShort(2, (short) 222);
        cs1.setShort("P2", (short) 442);
        cs1.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Set a SMALLINT parameter. - Using a named parmater
   **/
  public void Var030() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_SMALLINT", (short) -2232);
        cs.execute();
        short check = cs.getShort(1);
        assertCondition(check == -2232);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an INTEGER parameter. - Using a named parmater
   **/
  public void Var031() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_INTEGER", (short) -1061);
        cs.execute();
        int check = cs.getInt(2);
        assertCondition(check == -1061);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an REAL parameter. - Using a named parmater
   **/
  public void Var032() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_REAL", (short) 179);
        cs.execute();
        float check = cs.getFloat(3);
        assertCondition(check == 179);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an FLOAT parameter. - Using a named parmater
   **/
  public void Var033() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_FLOAT", (short) 1);
        cs.execute();
        double check = cs.getDouble(4);
        assertCondition(check == 1);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an DOUBLE parameter. - Using a named parmater
   **/
  public void Var034() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_DOUBLE", (short) 18);
        cs.execute();
        double check = cs.getDouble(5);
        assertCondition(check == 18);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an DECIMAL parameter. - Using a named parmater
   **/
  public void Var035() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_DECIMAL_50", (short) -1001);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(6);
        assertCondition(check.intValue() == -1001);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an NUMERIC parameter. - Using a named parmater
   **/
  public void Var036() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_NUMERIC_50", (short) 1777);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(8);
        assertCondition(check.intValue() == 1777);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an CHAR(50) parameter. - Using a named parmater
   **/
  public void Var037() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_CHAR_50", (short) 187);
        cs.execute();
        String check = cs.getString(11);
        assertCondition(
            check.equals("187                                               "));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set an CHAR(1) parameter. - Using a named parmater
   **/
  public void Var038() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_CHAR_1", (short) -83);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
      }
    }
  }

  /**
   * setShort() - Set an VARCHAR parameter. - Using a named parmater
   **/
  public void Var039() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_VARCHAR_50", (short) -767);
        cs.execute();
        String check = cs.getString(12);
        assertCondition(check.equals("-767"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Set a CLOB parameter. - Using a named parmater
   **/
  public void Var040() {
    if (checkNamedParametersSupport()) {
      if (checkLobSupport()) {
        try {
          cs.setShort("P_CLOB", (short) -22);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setShort() - Set a DBCLOB parameter. - Using a named parmater
   **/
  public void Var041() {
    if (checkNamedParametersSupport()) {
      if (checkLobSupport()) {
        try {
          cs.setShort("P_DBCLOB", (short) -5);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setShort() - Set a BINARY parameter. - Using a named parmater
   **/
  public void Var042() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_BINARY_20", (short) 4);
        cs.execute();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Set a VARBINARY parameter. - Using a named parmater
   **/
  public void Var043() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_VARBINARY", (short) 19);
        cs.execute();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Set a BLOB parameter. - Using a named parmater
   **/
  public void Var044() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_BLOB", (short) 4);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Set a DATE parameter. - Using a named parmater
   **/
  public void Var045() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_DATE", (short) 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Set a TIME parameter. - Using a named parmater
   **/
  public void Var046() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_TIME", (short) 3);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Set a TIMESTAMP parameter. - Using a named parmater
   **/
  public void Var047() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("P_TIMESTAMP", (short) -3443);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Set a DATALINK parameter. - Using a named parmater
   **/
  public void Var048() {
    if (checkNamedParametersSupport()) {
      if (checkLobSupport()) {
        try {
          cs.setShort("P_DATALINK", (short) 75);
          cs.execute();
          String check = cs.getString(18);
          assertCondition(check.equals("75"));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setShort() - Set a BIGINT parameter. - Using a named parmater
   **/
  public void Var049() {
    if (checkNamedParametersSupport()) {
      if (checkBigintSupport()) {
        try {
          cs.setShort("P_BIGINT", (short) 4031);
          cs.execute();
          long check = cs.getLong(22);
          assertCondition(check == 4031);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setShort() - Set a SMALLINT parameter. - Using a named parmater
   **/
  public void Var050() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setString("P_SMALLINT", "2232.2");
        cs.execute();
        short check = cs.getShort(1);
        assertCondition(check == 2232);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Attempt to set the return value parameter. - Using an ordinal
   * parameter
   **/
  public void Var051() {
    if (checkJdbc20()) {
      if (checkReturnValueSupport()) {
        try {
          CallableStatement cs2 = connection_
              .prepareCall("?=CALL " + JDSetupProcedure.STP_CSRV);
          cs2.setShort(1, (short) 444);
          cs2.close();
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setShort() - Should work since the lower case name is not in quotes. -
   * Using a named parmater
   **/
  public void Var052() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("p_smallint", (short) 442);
        cs.execute();
        short check = cs.getShort(1);
        assertCondition(check == 442);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Should work since the mixed case name is not in quotes. -
   * Using a named parmater
   **/
  public void Var053() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("p_SMALlint", (short) 442);
        cs.execute();
        short check = cs.getShort(1);
        assertCondition(check == 442);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setShort() - Should throw an exception since the mixed case name is in
   * quotes. - Using a named parmater
   **/
  public void Var054() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("'p_SMALlint'", (short) 442);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Should throw an exception since the lower case name is in
   * quotes. - Using a named parmater
   **/
  public void Var055() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setShort("'p_smallint'", (short) 442);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setShort() - Set a BOOLEAN parameter. - Using a named parameter
   **/
  public void Var056() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setShort", "P_BOOLEAN", "1", "1",
          " -- call setShort against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setShort() - Set a BOOLEAN parameter. - Using a named parameter
   **/
  public void Var057() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setShort", "P_BOOLEAN", "0", "0",
          " -- call setShort against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setShort() - Set a BOOLEAN parameter. - Using a numbered parameter
   **/
  public void Var058() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setShort", 27, "7", "1",
          " -- call setShort against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setShort() - Set a BOOLEAN parameter. - Using a numbered parameter
   **/
  public void Var059() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setShort", 27, "0", "0",
          " -- call setShort against BOOLEAN parameter -- added January 2021");
    }
  }

}
