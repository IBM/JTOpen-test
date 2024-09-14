///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetLong.java
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
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetLong.java
//
// Classes:      JDCSSetLong
//
////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.DataTruncation;
import java.sql.Statement;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;

/**
 * Testcase JDCSSetLong. This tests the following method of the JDBC
 * CallableStatement class:
 * 
 * <ul>
 * <li>setLong()
 * </ul>
 **/
public class JDCSSetLong extends JDCSSetTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSSetLong";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }

  /**
   * Constructor.
   **/
  public JDCSSetLong(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDCSSetLong", namesAndVars, runMode, fileOutputStream,
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
   * setLong() - Should throw an exception when the prepared statement is
   * closed. - Using an ordinal parameter
   **/
  public void Var001() {
    if (checkJdbc20()) {
      try {
        cs.close();
        cs.setLong(22, (long) 533);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Should throw an exception when an invalid index is specified. -
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
        cs.setLong(100, (long) 525);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Should throw an exception when the index is 0. - Using an
   * ordinal parameter
   **/
  public void Var003() {
    if (checkJdbc20()) {
      try {
        cs.setLong(0, (long) 487);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Should throw an exception when the index is -1. - Using an
   * ordinal parameter
   **/
  public void Var004() {
    if (checkJdbc20()) {
      try {
        cs.setLong(-1, (long) 943);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Should work with a valid parameter index. - Using an ordinal
   * parameter
   **/
  public void Var005() {
    if (checkJdbc20()) {
      if (areBigintsSupported()) {
        try {
          cs.setLong(22, 898956);
          cs.execute();
          long check = cs.getLong(22);
          assertCondition(check == 898956);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setLong() - Should throw an exception when the parameter is not an input
   * parameter. - Using an ordinal parameter
   **/
  public void Var006() {
    if (checkJdbc20()) {
      try {
        CallableStatement cs1 = connection_
            .prepareCall("Call " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
        cs1.setLong(2, 5454);
        cs1.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a SMALLINT parameter - Using an ordinal parameter
   **/
  public void Var007() {
    if (checkJdbc20()) {
      try {
        cs.setLong(1, 4654);
        cs.execute();
        short check = cs.getShort(1);
        assertCondition(check == 4654);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a SMALLINT parameter when the integer is too large. - Using
   * an ordinal parameter
   **/
  public void Var008() {
    if (checkJdbc20()) {
      try {
        cs.setLong(1, 546465465);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertSqlException(e, -99999, "07006", "Data type mismatch",
            "Mismatch instead of truncation in V7R1");

      }
    }
  }

  /**
   * setLong() - Set an INTEGER parameter. - Using an ordinal parameter
   **/
  public void Var009() {
    if (checkJdbc20()) {
      try {
        cs.setLong(2, 54655);
        cs.execute();
        int check = cs.getInt(2);
        assertCondition(check == 54655);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set an INTEGER parameter when the value is too large. - Using
   * an ordinal parameter
   **/
  public void Var010() {
    if (checkJdbc20()) {
      try {
        cs.setLong(2, 1234567890123456L);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertSqlException(e, -99999, "07006", "Data type mismatch",
            "Mismatch instead of truncation in V7R1");

      }
    }
  }

  /**
   * setLong() - Set a REAL parameter. - Using an ordinal parameter
   **/
  public void Var011() {
    if (checkJdbc20()) {
      try {
        cs.setLong(3, -45879);
        cs.execute();
        float check = cs.getFloat(3);
        assertCondition(check == -45879f);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a FLOAT parameter. - Using an ordinal parameter
   **/
  public void Var012() {
    if (checkJdbc20()) {
      try {
        cs.setLong(4, 54879);
        cs.execute();
        double check = cs.getDouble(4);
        assertCondition(check == 54879);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a DOUBLE parameter. - Using an ordinal parameter
   **/
  public void Var013() {
    if (checkJdbc20()) {
      try {
        cs.setLong(5, 12345467);
        cs.execute();
        double check = cs.getDouble(5);
        assertCondition(check == 12345467);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a DECIMAL parameter. - Using an ordinal parameter
   **/
  public void Var014() {
    if (checkJdbc20()) {
      try {
        cs.setLong(7, 40012);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(7);
        assertCondition(check.longValue() == 40012);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a DECIMAL parameter when the value is too large. - Using an
   * ordinal parameter
   **/
  public void Var015() {
    if (checkJdbc20()) {
      try {
        cs.setLong(6, 1032122134);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          assertSqlException(e, -99999, "07006", "Data type mismatch",
              "Mismatch instead of truncation now in toolbox");

        } else {

          assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
        }
      }
    }
  }

  /**
   * setLong() - Set a NUMERIC parameter. - Using an ordinal parameter
   **/
  public void Var016() {
    if (checkJdbc20()) {
      try {
        cs.setLong(8, 89564);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(8);
        assertCondition(check.longValue() == 89564);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a NUMERIC parameter when the value is too large.
   * 
   * SQL400 - The Native JDBC driver reports trucation information based on the
   * precision - scale size of the column and value. Not the total size of the
   * values because scale values will be silently truncated.
   * 
   * - Using an ordinal paramter
   **/
  public void Var017() {
    if (checkJdbc20()) {
      try {
        cs.setLong(9, -234562321733234L);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          assertSqlException(e, -99999, "07006", "Data type mismatch",
              "Mismatch instead of truncation now in toolbox");

        } else {

          DataTruncation dt = (DataTruncation) e;
          if (isToolboxDriver()) {
            assertCondition((dt.getIndex() == 9) && (dt.getParameter() == true)
                && (dt.getRead() == false) && (dt.getTransferSize() == 10));
          } else {
            if (getDriver() == JDTestDriver.DRIVER_NATIVE
                && getDriverFixLevel() < 49160) {
              notApplicable("Native driver fix in PTF V7R1 SI49160");
              return;
            }

            assertCondition(
                (dt.getIndex() == 9) && (dt.getParameter() == true)
                    && (dt.getRead() == false) && (dt.getTransferSize() == 10)
                    && (dt.getDataSize() == 20),
                "Updated 1/18/2013 for V7R1 fix -- Correct truncation information");

          }
        }
      }
    }
  }

  /**
   * setLong() - Set a CHAR(50) parameter. - Using an ordinal paramter
   **/
  public void Var018() {
    if (checkJdbc20()) {
      try {
        // had to reopen the connection to get this to work properly
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);

        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setLong(11, -18754);
        cs.execute();
        String check = cs.getString(11);
        assertCondition(
            check.equals("-18754                                            "));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a CHAR(1) parameter. - Using an ordinal parameter
   **/
  public void Var019() {
    if (checkJdbc20()) {
      try {
        cs.setLong(10, 8);
        cs.execute();
        String check = cs.getString(10);
        assertCondition(check.equals("8"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a CHAR(1) parameter when the value is too large. - Using an
   * ordinal paramter
   **/
  public void Var020() {
    if (checkJdbc20()) {
      try {
        cs.setLong(10, 54);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
      }
    }
  }

  /**
   * setLong() - Set a VARCHAR parameter. - Using an ordinal parameter
   **/
  public void Var021() {
    if (checkJdbc20()) {
      try {
        cs.setLong(12, 54648);
        cs.execute();
        String check = cs.getString(12);
        assertCondition(check.equals("54648"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLont() - Set a CLOB parameter. - Using an ordinal parameter
   **/
  public void Var022() {
    if (checkJdbc20()) {
      try {
        cs.setLong(20, 5464);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a DBCLOB parameter. - Using an ordinal parameter
   **/
  public void Var023() {
    if (checkJdbc20()) {
      try {
        cs.setLong(21, 465464);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a BINARY parameter. - Using an ordinal parameter
   **/
  public void Var024() {
    if (checkJdbc20()) {
      try {
        cs.setLong(13, 4654);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a VARBINARY parameter. - Using an ordinal parameter
   **/
  public void Var025() {
    if (checkJdbc20()) {
      try {
        cs.setLong(14, 65464);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a BLOB parameter. - Using an ordinal parameter
   **/
  public void Var026() {
    if (checkJdbc20()) {
      try {
        cs.setLong(19, 56465);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a DATE parameter. - Using an ordinal parameter
   **/
  public void Var027() {
    if (checkJdbc20()) {
      try {
        cs.setLong(15, 5456);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a TIME parameter. - Using an ordinal parameter
   **/
  public void Var028() {
    if (checkJdbc20()) {
      try {
        cs.setLong(16, 767456);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a TIMESTAMP parameter. - Using an ordinal paramter
   **/
  public void Var029() {
    if (checkJdbc20()) {
      try {
        cs.setLong(17, 454);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a DATALINK parameter. A datalink parameter can not be used
   * as an out parameter, so it is represented here as a varchar. That is why
   * this test passes. - Using an ordinal parameter
   **/
  public void Var030() {
    if (checkJdbc20()) {
      try {
        cs.setLong(18, 54654);
        cs.execute();
        String check = cs.getString(18);
        assertCondition(check.equals("54654"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a BIGINT parameter. - Using an ordinal parameter
   **/
  public void Var031() {
    if (checkJdbc20()) {
      if (checkBigintSupport()) {
        try {
          cs.setLong(22, 545645);
          cs.execute();
          long check = cs.getLong(22);
          assertCondition(check == 545645);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setLong() - Set a BIGINT parameter. - Using an ordinal parameter //
   * 01/14/2002 -- we no longer chop what is beyond the decimal point. This //
   * is now an exception. You are, after all, putting // a decimal point number
   * in an int. // 11/13/2002 -- we changed behavior back to chop what is beyond
   * the decimal // since this is the behavior of all of the other integer types
   **/
  public void Var032() {
    if (checkJdbc20()) {
      if (checkBigintSupport()) {
        try {
          cs.setString(22, "-303613.1213");
          cs.execute();
          long check = cs.getLong(22);
          assertCondition(check == -303613);
          // failed("no exception");
        } catch (Exception e) {
          failed(e,
              "Unexpected Exception - Toolbox changed testcase 11/13/2002");
          // assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setLong() - Should throw an exception when the prepared statement is
   * closed. - Using a named parameter
   **/
  public void Var033() {
    if (checkNamedParametersSupport()) {
      try {
        cs.close();
        cs.setLong("P_BIGINT", (long) 533);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Should throw an exception when an invalid index is specified. -
   * Using a named parameter
   **/
  public void Var034() {
    if (checkNamedParametersSupport()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);

        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setLong("PARAM_1", (long) 525);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Should work with a valid parameter index. - Using a named
   * parameter
   **/
  public void Var035() {
    if (checkNamedParametersSupport()) {
      if (areBigintsSupported()) {
        try {
          cs.setLong("P_BIGINT", 898956);
          cs.execute();
          long check = cs.getLong(22);
          assertCondition(check == 898956);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setLong() - Should throw an exception when the parameter is not an input
   * parameter. - Using a named parameter
   **/
  public void Var036() {
    if (checkNamedParametersSupport()) {
      try {
        CallableStatement cs1 = connection_
            .prepareCall("Call " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
        cs1.setLong("P2", 5454);
        cs1.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a SMALLINT parameter - Using a named parameter
   **/
  public void Var037() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_SMALLINT", 4654);
        cs.execute();
        short check = cs.getShort(1);
        assertCondition(check == 4654);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a SMALLINT parameter when the integer is too large. - Using
   * a named parameter
   **/
  public void Var038() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_SMALLINT", 546465465);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertSqlException(e, -99999, "07006", "Data type mismatch",
            "Mismatch instead of truncation in V7R1");

      }
    }
  }

  /**
   * setLong() - Set an INTEGER parameter. - Using a named parameter
   **/
  public void Var039() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_INTEGER", 54655);
        cs.execute();
        int check = cs.getInt(2);
        assertCondition(check == 54655);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set an INTEGER parameter when the value is too large. - Using a
   * named parameter
   **/
  public void Var040() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_INTEGER", 1234567890123456L);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertSqlException(e, -99999, "07006", "Data type mismatch",
            "Mismatch instead of truncation");

      }
    }
  }

  /**
   * setLong() - Set a REAL parameter. - Using a named parameter
   **/
  public void Var041() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_REAL", -45879);
        cs.execute();
        float check = cs.getFloat(3);
        assertCondition(check == -45879f);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a FLOAT parameter. - Using a named parameter
   **/
  public void Var042() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_FLOAT", 54879);
        cs.execute();
        double check = cs.getDouble(4);
        assertCondition(check == 54879);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a DOUBLE parameter. - Using a named parameter
   **/
  public void Var043() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_DOUBLE", 12345467);
        cs.execute();
        double check = cs.getDouble(5);
        assertCondition(check == 12345467);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a DECIMAL parameter. - Using a named parameter
   **/
  public void Var044() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_DECIMAL_105", 40012);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(7);
        assertCondition(check.longValue() == 40012);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a DECIMAL parameter when the value is too large. - Using a
   * named parameter
   **/
  public void Var045() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_DECIMAL_50", 1032122134);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          assertSqlException(e, -99999, "07006", "Data type mismatch",
              "Mismatch instead of truncation now in toolbox");

        } else {

          assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
        }
      }
    }
  }

  /**
   * setLong() - Set a NUMERIC parameter. - Using a named parameter
   **/
  public void Var046() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_NUMERIC_50", 89564);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(8);
        assertCondition(check.longValue() == 89564);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a NUMERIC parameter when the value is too large.
   * 
   * SQL400 - The Native JDBC driver reports trucation information based on the
   * precision - scale size of the column and value. Not the total size of the
   * values because scale values will be silently truncated.
   * 
   * - Using a named paramter
   **/
  public void Var047() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_NUMERIC_105", -234562321733234L);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          assertSqlException(e, -99999, "07006", "Data type mismatch",
              "Mismatch instead of truncation now in toolbox");

        } else {

          DataTruncation dt = (DataTruncation) e;
          if (isToolboxDriver()) {
            assertCondition((dt.getIndex() == 9) && (dt.getParameter() == true)
                && (dt.getRead() == false) && (dt.getTransferSize() == 10));
          } else {
            if (getDriver() == JDTestDriver.DRIVER_NATIVE
                && getDriverFixLevel() < 49160) {
              notApplicable("Native driver fix in PTF V7R1 SI49160");
              return;
            }

            assertCondition(
                (dt.getIndex() == 9) && (dt.getParameter() == true)
                    && (dt.getRead() == false) && (dt.getTransferSize() == 10)
                    && (dt.getDataSize() == 20),
                "Updated 1/18/2013 for V7R1 fix");
          }
        }
      }
    }
  }

  /**
   * setLong() - Set a CHAR(50) parameter. - Using a named paramter
   **/
  public void Var048() {
    if (checkNamedParametersSupport()) {
      try {
        // had to reopen the connection to get this to work properly
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);

        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        cs.setLong("P_CHAR_50", -18754);
        cs.execute();
        String check = cs.getString(11);
        assertCondition(
            check.equals("-18754                                            "));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a CHAR(1) parameter. - Using a named parameter
   **/
  public void Var049() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_CHAR_1", 8);
        cs.execute();
        String check = cs.getString(10);
        assertCondition(check.equals("8"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a CHAR(1) parameter when the value is too large. - Using a
   * named paramter
   **/
  public void Var050() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_CHAR_1", 54);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
      }
    }
  }

  /**
   * setLong() - Set a VARCHAR parameter. - Using a named parameter
   **/
  public void Var051() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_VARCHAR_50", 54648);
        cs.execute();
        String check = cs.getString(12);
        assertCondition(check.equals("54648"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLont() - Set a CLOB parameter. - Using a named parameter
   **/
  public void Var052() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_CLOB", 5464);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a DBCLOB parameter. - Using a named parameter
   **/
  public void Var053() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_DBCLOB", 465464);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a BINARY parameter. - Using a named parameter
   **/
  public void Var054() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_BINARY_20", 4654);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a VARBINARY parameter. - Using a named parameter
   **/
  public void Var055() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_VARBINARY_20", 65464);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a BLOB parameter. - Using a named parameter
   **/
  public void Var056() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_BLOB", 56465);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a DATE parameter. - Using a named parameter
   **/
  public void Var057() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_DATE", 5456);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a TIME parameter. - Using a named parameter
   **/
  public void Var058() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_TIME", 767456);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a TIMESTAMP parameter. - Using a named paramter
   **/
  public void Var059() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_TIMESTAMP", 454);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setLong() - Set a DATALINK parameter. A datalink parameter can not be used
   * as an out parameter, so it is represented here as a varchar. That is why
   * this test passes. - Using a named parameter
   **/
  public void Var060() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong("P_DATALINK", 54654);
        cs.execute();
        String check = cs.getString(18);
        assertCondition(check.equals("54654"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Set a BIGINT parameter. - Using a named parameter
   **/
  public void Var061() {
    if (checkNamedParametersSupport()) {
      if (checkBigintSupport()) {
        try {
          cs.setLong("P_BIGINT", 545645);
          cs.execute();
          long check = cs.getLong(22);
          assertCondition(check == 545645);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setLong() - Set a BIGINT parameter. - Using a named parameter // 01/14/2002
   * -- we no longer chop what is beyond the decimal point. This // is now an
   * exception. You are, after all, putting // a decimal point number in an int.
   **/
  public void Var062() {
    if (checkNamedParametersSupport()) {
      if (checkBigintSupport()) {
        try {
          cs.setString("P_BIGINT", "-303613.1213");
          cs.execute();
          long check = cs.getLong(22);
          assertCondition(check == -303613);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setLong() - Should throw an exception if an ordinal and a named parameter
   * are used for the same statement according to the specification, but we are
   * not going to enforce it directly in the native JDBC driver. How the Toolbox
   * JDBC driver will handle the situation is not decided yet.
   * 
   * - Using an ordinal parameter - Using a named parameter
   **/
  public void Var063() {
    if (checkNamedParametersSupport()) {
      try {
        cs.setLong(1, 456);
        cs.setLong("P_BIGINT", 46546);
        cs.execute();
        short check1 = cs.getShort(1);
        long check2 = cs.getLong(22);
        assertCondition((check1 == 456) && (check2 == 46546));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setLong() - Should work since the lower case name is not in quotes. - Using
   * a named parameter
   **/
  public void Var064() {
    if (checkNamedParametersSupport()) {
      if (areBigintsSupported()) {
        try {
          cs.setLong("p_bigint", 898956);
          cs.execute();
          long check = cs.getLong(22);
          assertCondition(check == 898956);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setLong() - Should work since the mixed case name is not in quotes. - Using
   * a named parameter
   **/
  public void Var065() {
    if (checkNamedParametersSupport()) {
      if (areBigintsSupported()) {
        try {
          cs.setLong("p_BiGiNt", 898956);
          cs.execute();
          long check = cs.getLong(22);
          assertCondition(check == 898956);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setLong() - Should throw an exception since the mixed case name is in
   * quotes. - Using a named parameter
   **/
  public void Var066() {
    if (checkNamedParametersSupport()) {
      if (areBigintsSupported()) {
        try {
          cs.setLong("'p_BiGiNt'", 898956);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setLong() - Should throw an exception since the lower case name is in
   * quotes. - Using a named parameter
   **/
  public void Var067() {
    if (checkNamedParametersSupport()) {
      if (areBigintsSupported()) {
        try {
          cs.setLong("'p_bigint'", 898956);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setLong() - Set a BIGINT parameter. - Using an ordinal parameter and a
   * string which is the maximum value of Long.
   **/
  public void Var068() {
    if (checkJdbc20()) {
      if (checkBigintSupport()) {
        try {
          cs.setString(22, "" + Long.MAX_VALUE);
          cs.execute();
          long check = cs.getLong(22);
          assertCondition(check == Long.MAX_VALUE);
          // failed("no exception");
        } catch (Exception e) {
          failed(e, "Unexpected Exception - Native added testcase 06/12/2003");
          // assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setLong() - Set a BIGINT parameter. - Using an ordinal parameter and a
   * string which is the maximum value of Long plus a little more.
   **/
  public void Var069() {
    if (checkJdbc20()) {
      if (checkBigintSupport()) {
        try {
          cs.setString(22, "" + Long.MAX_VALUE + ".001");
          cs.execute();
          long check = cs.getLong(22);
          boolean condition = (check == Long.MAX_VALUE);
          assertCondition(condition,
              "Returned " + check + " Expected " + Long.MAX_VALUE);
          // failed("no exception");
        } catch (Exception e) {
          failed(e, "Unexpected Exception - Native added testcase 06/12/2003");
          // assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setLong() - Set a BIGINT parameter. - Using an ordinal parameter and a
   * string which is the minimum value of Long.
   **/
  public void Var070() {
    if (checkJdbc20()) {
      if (checkBigintSupport()) {
        try {
          cs.setString(22, "" + Long.MIN_VALUE);
          cs.execute();
          long check = cs.getLong(22);
          assertCondition(check == Long.MIN_VALUE);
          // failed("no exception");
        } catch (Exception e) {
          failed(e, "Unexpected Exception - Native added testcase 06/12/2003");
          // assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setLong() - Set a BIGINT parameter. - Using an ordinal parameter and a
   * string which is the minimum value of Long plus a little more.
   **/
  public void Var071() {
    if (checkJdbc20()) {
      if (checkBigintSupport()) {
        try {
          cs.setString(22, "" + Long.MIN_VALUE + ".001");
          cs.execute();
          long check = cs.getLong(22);
          boolean condition = (check == Long.MIN_VALUE);
          assertCondition(condition,
              "Returned " + check + " Expected " + Long.MIN_VALUE);
          // failed("no exception");
        } catch (Exception e) {
          failed(e, "Unexpected Exception - Native added testcase 06/12/2003");
          // assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setLong() - Set a BIGINT parameter. - Using an ordinal parameter and a
   * string which just under the maximum value of Long plus a little more.
   **/
  public void Var072() {
    if (checkJdbc20()) {
      if (checkBigintSupport()) {
        try {
          cs.setString(22, "" + (Long.MAX_VALUE - 1) + ".001");
          cs.execute();
          long check = cs.getLong(22);
          boolean condition = (check == (Long.MAX_VALUE - 1));
          assertCondition(condition,
              "Returned " + check + " Expected " + (Long.MAX_VALUE - 1));
          // failed("no exception");
        } catch (Exception e) {
          failed(e, "Unexpected Exception - Native added testcase 06/12/2003");
          // assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
      }
    }
  }

  public void testNamedParameters(String procedureName,
      String procedureDefinition, String parameterName, long parameterValue) {

    String added = " -- added by native driver 6/24/2015 to test named arguments";
    if (checkRelease710()) {

      try {
        Statement stmt = connection_.createStatement();
        try {
          stmt.executeUpdate("DROP PROCEDURE " + procedureName);
        } catch (Exception e) {
          // e.printStackTrace();
        }
        stmt.executeUpdate(
            "CREATE PROCEDURE " + procedureName + procedureDefinition);

        CallableStatement ps;
        ps = connection_.prepareCall(
            "CALL " + procedureName + "(?, " + parameterName + " => ?)");

        ps.registerOutParameter(1, java.sql.Types.BIGINT);
        ps.setLong(parameterName, parameterValue);

        ps.execute();
        String value = ps.getString(1);
        String expectedValue = "" + parameterValue;
        stmt.executeUpdate("DROP PROCEDURE " + procedureName);
        ps.close();
        stmt.close();
        assertCondition(expectedValue.equals(value),
            "value(" + value + ") != " + expectedValue + added);

      } catch (Exception e) {
        failed(e, "Unexpected Exception " + added);
      }
    }

  }

  public void Var073() {
    testNamedParameters(JDCSTest.COLLECTION + ".JDCSSBD73",
        "(OUT OUTARG BIGINT , IN INARG BIGINT ) BEGIN SET OUTARG=INARG; END ",
        "INARG", 37);

  }

  public void Var074() {
    testNamedParameters(JDCSTest.COLLECTION + ".JDCSSBD74",
        "(OUT OUTARG BIGINT , IN INARG BIGINT, IN UNUSED1 BIGINT DEFAULT 0.0, IN UNUSED2 BIGINT DEFAULT 0.0, IN UNUSED3 BIGINT DEFAULT 0.0, IN UNUSED4 BIGINT DEFAULT 0.0, IN UNUSED5 BIGINT DEFAULT 0.0 ) BEGIN SET OUTARG=INARG; END ",
        "INARG", 74);

  }

  public void Var075() {
    testNamedParameters(JDCSTest.COLLECTION + ".JDCSSBD75",
        "(OUT OUTARG BIGINT ,  IN UNUSED1 BIGINT DEFAULT 0.0, IN UNUSED2 BIGINT DEFAULT 0.0, IN INARG BIGINT, IN UNUSED3 BIGINT DEFAULT 0.0, IN UNUSED4 BIGINT DEFAULT 0.0, IN UNUSED5 BIGINT DEFAULT 0.0 ) BEGIN SET OUTARG=INARG; END ",
        "INARG", 75);

  }

  public void Var076() {
    testNamedParameters(JDCSTest.COLLECTION + ".JDCSSBD75",
        "(OUT OUTARG BIGINT ,  IN UNUSED1 BIGINT DEFAULT 0.0, IN UNUSED2 BIGINT DEFAULT 0.0, IN UNUSED3 BIGINT DEFAULT 0.0, IN UNUSED4 BIGINT DEFAULT 0.0, IN UNUSED5 BIGINT DEFAULT 0.0, IN INARG BIGINT ) BEGIN SET OUTARG=INARG; END ",
        "INARG", 76);

  }

  /**
   * setLong() - Set a BOOLEAN parameter. - Using a named parameter
   **/
  public void Var077() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setLong", "P_BOOLEAN", "1", "1",
          " -- call setLong against BOOLEAN parameter -- added January 2021");
    }
  }

  /**
   * setLong() - Set a BOOLEAN parameter. - Using a named parameter
   **/
  public void Var078() {

    if (checkBooleanSupport()) {
      setTestSuccessful("setLong", "P_BOOLEAN", "0", "0",
          " -- call setLong against BOOLEAN parameter -- added January 2021");
    }
  }

}
