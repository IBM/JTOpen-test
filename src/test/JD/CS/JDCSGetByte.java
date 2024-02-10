///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetByte.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetByte.java
//
// Classes:      JDCSGetByte
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;

/**
 * Testcase JDCSGetByte. This tests the following method of the JDBC
 * CallableStatement class:
 * 
 * <ul>
 * <li>getByte()
 * </ul>
 **/
public class JDCSGetByte extends JDCSGetTestcase {

  
  private boolean skipVar059 = true;

  /**
   * Constructor.
   **/
  public JDCSGetByte(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream,  String password) {
    super(systemObject, "JDCSGetByte", namesAndVars, runMode, fileOutputStream,
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
    connection_.close();
  }

  /**
   * getByte() - Get parameter -1.
   **/
  public void Var001() {
    try {
      csTypesB_.execute();
      byte p = csTypesB_.getByte(-1);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getByte() - Get parameter 0.
   **/
  public void Var002() {
    try {
      byte p = csTypesB_.getByte(0);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getByte() - Use a parameter that is too big.
   **/
  public void Var003() {
    try {
      byte p = csTypesB_.getByte(35);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getByte() - Get a parameter when there are no parameters.
   **/
  public void Var004() {
    // I created a whole new Connection object here to work
    // around a server bug.
    Connection c = null;
    try {
      c = testDriver_.getConnection(baseURL_ + ";errors=full", userId_, encryptedPassword_);
      CallableStatement cs = c.prepareCall("CALL " + JDSetupProcedure.STP_CS0);
      cs.execute();
      byte p = cs.getByte(1);
      failed("Didn't throw SQLException " + p);
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
   * getByte() - Get a parameter that was not registered.
   **/
  public void Var005() {
    try {
      CallableStatement cs = JDSetupProcedure.prepare(connection_,
          JDSetupProcedure.STP_CSTYPESOUTB, 
          supportedFeatures_);
      cs.execute();
      byte p = cs.getByte(1);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getByte() - Get a parameter when the statement has not been executed.
   **/
  public void Var006() {
    try {
      CallableStatement cs = JDSetupProcedure.prepare(connection_,
          JDSetupProcedure.STP_CSTYPESOUTB, 
          supportedFeatures_);
      cs.registerOutParameter(1, Types.SMALLINT);
      byte p = cs.getByte(1);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getByte() - Get a parameter when the statement is closed.
   **/
  public void Var007() {
    try {
      CallableStatement cs = JDSetupProcedure.prepare(connection_,
          JDSetupProcedure.STP_CSTYPESOUTB, 
          supportedFeatures_);
      cs.registerOutParameter(1, Types.SMALLINT);
      cs.execute();
      cs.close();
      byte p = cs.getByte(1);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getByte() - Get an IN parameter that was correctly registered.
   **/
  public void Var008() {
    try {
      CallableStatement cs = JDSetupProcedure.prepare(connection_,
          JDSetupProcedure.STP_CSTYPESIN, 
          supportedFeatures_);
      JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSTYPESIN,
           supportedFeatures_);
      cs.registerOutParameter(1, Types.SMALLINT);
      cs.execute();
      byte p = cs.getByte(1);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getByte() - Get an INOUT parameter, where the OUT parameter is longer than
   * the IN parameter.
   **/
  public void Var009() {
    try {
      CallableStatement cs = JDSetupProcedure.prepare(connection_,
          JDSetupProcedure.STP_CSTYPESINOUT, 
          supportedFeatures_);
      JDSetupProcedure.setTypesParameters(cs,
          JDSetupProcedure.STP_CSTYPESINOUT, 
          supportedFeatures_);
      JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSTYPESINOUT,
           supportedFeatures_, getDriver());
      cs.registerOutParameter(1, Types.SMALLINT);
      cs.execute();
      byte p = cs.getByte(1);
      assertCondition(p == 24);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getByte() - Get a type that was registered as a SMALLINT.
   **/
  public void Var010() {

    String info = " -- call getByte against SMALLINT -- Changed October 2011";

    // As of 10/3/2011 for the toolbox driver, it is now possible to get a byte
    // from a parameter registered as int.
    // Note: JCC does not throw exception either.
    // Native JDBC will now need to change.
    //
    // Note: A truncation exception will be throw if run against STP_CSTYPESOUT

    try {
      byte p = csTypesB_.getByte(1);
      byte expected = 1;
      assertCondition(p == expected, "Got " + p + " expected " + expected
          + info);
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + info);
    }
  }

  /**
   * getByte() - Get a type that was registered as an INTEGER.
   **/
  public void Var011() {
    String info = " -- call getByte against INTEGER -- Changed October 2011";

    try {
      byte p = csTypesB_.getByte(2);
      byte expected = 2;
      assertCondition(p == expected, "Got " + p + " expected " + expected
          + info);
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + info);
    }
  }

  /**
   * getByte() - Get a type that was registered as an REAL.
   **/
  public void Var012() {
    String info = " -- call getByte against REAL -- Changed October 2011";
    try {
      byte p = csTypesB_.getByte(3);
      byte expected = 3;
      assertCondition(p == expected, "Got " + p + " expected " + expected
          + info);
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + info);
    }
  }

  /**
   * getByte() - Get a type that was registered as an FLOAT.
   **/
  public void Var013() {
    String info = " -- call getByte against FLOAT -- Changed October 2011";

    try {
      byte p = csTypesB_.getByte(4);
      byte expected = 4;
      assertCondition(p == expected, "Got " + p + " expected " + expected
          + info);
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + info);
    }
  }

  /**
   * getByte() - Get a type that was registered as an DOUBLE.
   **/
  public void Var014() {
    String info = " -- call getByte against DOUBLE -- Changed October 2011";

    try {
      byte p = csTypesB_.getByte(5);
      byte expected = 5;
      assertCondition(p == expected, "Got " + p + " expected " + expected
          + info);
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + info);
    }
  }

  /**
   * getByte() - Get a type that was registered as an DECIMAL.
   **/
  public void Var015() {
    String info = " -- call getByte against DECMAL -- Changed October 2011";

    try {
      byte p = csTypesB_.getByte(7);
      byte expected = 7;
      assertCondition(p == expected, "Got " + p + " expected " + expected
          + info);
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + info);
    }
  }

  /**
   * getByte() - Get a type that was registered as an NUMERIC.
   **/
  public void Var016() {
    String info = " -- call getByte against NUMERIC -- Changed October 2011";

    try {
      byte p = csTypesB_.getByte(9);
      byte expected = 9;
      assertCondition(p == expected, "Got " + p + " expected " + expected
          + info);
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + info);
    }
  }

  /**
   * getByte() - Get a type that was registered as a CHAR.
   **/
  public void Var017() {
    String info = " -- call getByte against CHAR -- Changed October 2011";

    try {
      byte p = csTypesB_.getByte(11);
      byte expected = 11;
      assertCondition(p == expected, "Got " + p + " expected " + expected
          + info);
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + info);
    }
  }

  /**
   * getByte() - Get a type that was registered as a VARCHAR.
   **/
  public void Var018() {
    String info = " -- call getByte against VARCHAR -- Changed October 2011";

    try {
      byte p = csTypesB_.getByte(12);
      byte expected = 12;
      assertCondition(p == expected, "Got " + p + " expected " + expected
          + info);
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + info);
    }
  }

  /**
   * getByte() - Get a type that was registered as a BINARY.
   **/
  public void Var019() {
    try {
      byte p = csTypesB_.getByte(13);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getByte() - Get a type that was registered as a VARBINARY.
   **/
  public void Var020() {
    try {
      byte p = csTypesB_.getByte(14);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getByte() - Get a type that was registered as a DATE.
   **/
  public void Var021() {
    try {
      byte p = csTypesB_.getByte(15);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getByte() - Get a type that was registered as a TIME.
   **/
  public void Var022() {
    try {
      byte p = csTypesB_.getByte(16);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getByte() - Get a type that was registered as a TIMESTAMP.
   **/
  public void Var023() {
    try {
      byte p = csTypesB_.getByte(17);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  /**
   * getByte() - Get a type that was registered as an OTHER.
   **/
  public void Var024() {
    if (checkLobSupport()) {
      try {
        byte p = csTypesB_.getByte(18);
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a BLOB.
   **/
  public void Var025() {
    if (checkLobSupport()) {
      try {
        byte p = csTypesB_.getByte(19);
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a CLOB.
   **/
  public void Var026() {
    if (checkLobSupport()) {
      try {
        byte p = csTypesB_.getByte(20);
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a BIGINT.
   **/
  public void Var027() {

    if (checkBigintSupport()) {

      String info = " -- call getByte against BIGINT -- Changed October 2011";
      try {
        byte p = csTypesB_.getByte(22);
        byte expected = 21;
        assertCondition(p == expected, "Got " + p + " expected " + expected
            + info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + info);
      }
    }
  }

  /**
   * getByte() - Get an INOUT parameter, where the OUT parameter is longer than
   * the IN parameter, when the output parameter is registered first.
   * 
   * SQL400 - We added this testcase because of a customer bug.
   **/
  public void Var028() {
    try {
      CallableStatement cs = JDSetupProcedure.prepare(connection_,
          JDSetupProcedure.STP_CSTYPESINOUT, 
          supportedFeatures_);
      JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSTYPESINOUT,
          supportedFeatures_, getDriver());
      cs.registerOutParameter(1, Types.SMALLINT);
      JDSetupProcedure.setTypesParameters(cs,
          JDSetupProcedure.STP_CSTYPESINOUT, 
          supportedFeatures_);
      cs.execute();
      byte p = cs.getByte(1);
      assertCondition(p == 24);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getByte() -- Get an output parameter where the database output type isn't
   * necessarily a short
   */
  public void Var029() {
    try {
      Statement stmt = connection_.createStatement();

      try {
        stmt
            .executeUpdate("drop table " + JDCSTest.COLLECTION + ".tinyint_tab");
      } catch (Exception e) {
      }
      try {
        stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION
            + ".tinyint_Proc");
      } catch (Exception e) {
      }

      stmt
          .executeUpdate("create table "
              + JDCSTest.COLLECTION
              + ".tinyint_Tab(MAX_VAL NUMERIC, MIN_VAL NUMERIC, NULL_VAL NUMERIC )");

      stmt
          .executeUpdate("create procedure "
              + JDCSTest.COLLECTION
              + ".tinyint_Proc (out MAX_PARAM NUMERIC, out MIN_PARAM NUMERIC, out NULL_PARAM NUMERIC) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "
              + JDCSTest.COLLECTION + ".tinyint_Tab; end");

      stmt.executeUpdate("insert into " + JDCSTest.COLLECTION
          + ".tinyint_Tab values(127,0,null)");
      CallableStatement cstmt = connection_.prepareCall("{call "
          + JDCSTest.COLLECTION + ".tinyint_Proc(?,?,?)}");

      // register the output parameters
      cstmt.registerOutParameter(1, java.sql.Types.TINYINT);
      cstmt.registerOutParameter(2, java.sql.Types.TINYINT);
      cstmt.registerOutParameter(3, java.sql.Types.TINYINT);

      // execute the procedure
      cstmt.execute();

      // invoke getByte method
      short xRetVal = cstmt.getByte(1);
      short yRetVal = cstmt.getByte(2);
      short zRetVal = cstmt.getByte(3);

      assertCondition(xRetVal == 127 && yRetVal == 0 && zRetVal == 0);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getByte() -- Get an output parameter where the database output type isn't
   * necessarily a short
   */
  public void Var030() {
    try {
      Statement stmt = connection_.createStatement();

      try {
        stmt
            .executeUpdate("drop table " + JDCSTest.COLLECTION + ".tinyint_tab");
      } catch (Exception e) {
      }
      try {
        stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION
            + ".tinyint_Proc");
      } catch (Exception e) {
      }

      stmt
          .executeUpdate("create table "
              + JDCSTest.COLLECTION
              + ".tinyint_Tab(MAX_VAL VARCHAR(30), MIN_VAL VARCHAR(30), NULL_VAL VARCHAR(30) )");

      stmt
          .executeUpdate("create procedure "
              + JDCSTest.COLLECTION
              + ".tinyint_Proc (out MAX_PARAM VARCHAR(30), out MIN_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "
              + JDCSTest.COLLECTION + ".tinyint_Tab; end");

      stmt.executeUpdate("insert into " + JDCSTest.COLLECTION
          + ".tinyint_Tab values('127','0',null)");
      CallableStatement cstmt = connection_.prepareCall("{call "
          + JDCSTest.COLLECTION + ".tinyint_Proc(?,?,?)}");

      // register the output parameters
      cstmt.registerOutParameter(1, java.sql.Types.TINYINT);
      cstmt.registerOutParameter(2, java.sql.Types.TINYINT);
      cstmt.registerOutParameter(3, java.sql.Types.TINYINT);

      // execute the procedure
      cstmt.execute();

      // invoke getByte method
      short xRetVal = cstmt.getByte(1);
      short yRetVal = cstmt.getByte(2);
      short zRetVal = cstmt.getByte(3);

      assertCondition(xRetVal == 127 && yRetVal == 0 && zRetVal == 0);

    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * getByte() -- Get an output parameter where the database output type isn't
   * necessarily a short, but value cannot be mapped
   */
  public void Var031() {
    try {
      Statement stmt = connection_.createStatement();

      try {
        stmt
            .executeUpdate("drop table " + JDCSTest.COLLECTION + ".tinyint_tab");
      } catch (Exception e) {
      }
      try {
        stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION
            + ".tinyint_Proc");
      } catch (Exception e) {
      }

      stmt
          .executeUpdate("create table "
              + JDCSTest.COLLECTION
              + ".tinyint_Tab(MAX_VAL VARCHAR(30), MIN_VAL VARCHAR(30), NULL_VAL VARCHAR(30) )");

      stmt
          .executeUpdate("create procedure "
              + JDCSTest.COLLECTION
              + ".tinyint_Proc (out MAX_PARAM VARCHAR(30), out MIN_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from "
              + JDCSTest.COLLECTION + ".tinyint_Tab; end");

      stmt.executeUpdate("insert into " + JDCSTest.COLLECTION
          + ".tinyint_Tab values('ISMALL','0',null)");
      CallableStatement cstmt = connection_.prepareCall("{call "
          + JDCSTest.COLLECTION + ".tinyint_Proc(?,?,?)}");

      // register the output parameters
      cstmt.registerOutParameter(1, java.sql.Types.TINYINT);
      cstmt.registerOutParameter(2, java.sql.Types.TINYINT);
      cstmt.registerOutParameter(3, java.sql.Types.TINYINT);

      // execute the procedure
      cstmt.execute();

      // invoke getByte method
      short xRetVal = cstmt.getByte(1);
      short yRetVal = cstmt.getByte(2);
      short zRetVal = cstmt.getByte(3);

      failed("Didn't throw exception " + xRetVal + "," + yRetVal + ","
          + zRetVal);

    } catch (Exception e) {
      //
      // For the native driver, this is a data type mismatch
      // 
      assertExceptionIsInstanceOf(e, "java.sql.SQLException");
    }
  }

  // NERYKAL - named parameters
  /**
   * getByte() - Get a type that was registered as a SMALLINT.
   **/
  public void Var032() {
    if (checkNamedParametersSupport()) {
      String info = " -- call getByte against SMALLINT -- Changed October 2011";
      try {
        byte p = csTypesB_.getByte("P_SMALLINT");
        byte expected = 1;
        assertCondition(p == expected, "Got " + p + " expected " + expected
            + info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + info);
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as an INTEGER.
   **/
  public void Var033() {
    if (checkNamedParametersSupport()) {
      String info = " -- call getByte against INTEGER-- Changed October 2011";
      try {
        byte p = csTypesB_.getByte("P_INTEGER");
        byte expected = 2;
        assertCondition(p == expected, "Got " + p + " expected " + expected
            + info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + info);
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as an REAL.
   **/
  public void Var034() {
    if (checkNamedParametersSupport()) {
      String info = " -- call getByte against REAL -- Changed October 2011";
      try {
        byte p = csTypesB_.getByte("P_REAL");
        byte expected = 3;
        assertCondition(p == expected, "Got " + p + " expected " + expected
            + info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + info);
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as an FLOAT.
   **/
  public void Var035() {
    if (checkNamedParametersSupport()) {
      String info = " -- call getByte against FLOAT -- Changed October 2011";
      try {
        byte p = csTypesB_.getByte("P_FLOAT");
        byte expected = 4;
        assertCondition(p == expected, "Got " + p + " expected " + expected
            + info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + info);
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as an DOUBLE.
   **/
  public void Var036() {
    if (checkNamedParametersSupport()) {
      String info = " -- call getByte against DOUBLE -- Changed October 2011";
      try {
        byte p = csTypesB_.getByte("P_DOUBLE");
        byte expected = 5;
        assertCondition(p == expected, "Got " + p + " expected " + expected
            + info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + info);
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as an DECIMAL.
   **/
  public void Var037() {
    if (checkNamedParametersSupport()) {
      String info = " -- call getByte against DECIMAL -- Changed October 2011";
      try {
        byte p = csTypesB_.getByte("P_DECIMAL_50");
        byte expected = 6;
        assertCondition(p == expected, "Got " + p + " expected " + expected
            + info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + info);
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as an DECIMAL.
   **/
  public void Var038() {
    if (checkNamedParametersSupport()) {
      String info = " -- call getByte against DECIMAL -- Changed October 2011";
      try {
        byte p = csTypesB_.getByte("P_DECIMAL_105");
        byte expected = 7;
        assertCondition(p == expected, "Got " + p + " expected " + expected
            + info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + info);
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as an NUMERIC.
   **/
  public void Var039() {
    if (checkNamedParametersSupport()) {
      String info = " -- call getByte against NUMERIC -- Changed October 2011";
      try {
        byte p = csTypesB_.getByte("P_NUMERIC_50");
        byte expected = 8;
        assertCondition(p == expected, "Got " + p + " expected " + expected
            + info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + info);
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as an NUMERIC.
   **/
  public void Var040() {
    if (checkNamedParametersSupport()) {
      String info = " -- call getByte against NUMERIC -- Changed October 2011";
      try {
        byte p = csTypesB_.getByte("P_NUMERIC_105");
        byte expected = 9;
        assertCondition(p == expected, "Got " + p + " expected " + expected
            + info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + info);
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a CHAR.
   **/
  public void Var041() {
    if (checkNamedParametersSupport()) {
      String info = " -- call getByte against CHAR -- Changed October 2011";
      try {
        byte p = csTypesB_.getByte("P_CHAR_1");
        byte expected = 0;
        assertCondition(p == expected, "Got " + p + " expected " + expected
            + info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + info);
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a CHAR.
   **/
  public void Var042() {
    if (checkNamedParametersSupport()) {
      String info = " -- call getByte against CHAR -- Changed October 2011";
      try {
        byte p = csTypesB_.getByte("P_CHAR_50");
        byte expected = 11;
        assertCondition(p == expected, "Got " + p + " expected " + expected
            + info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + info);
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a VARCHAR.
   **/
  public void Var043() {
    if (checkNamedParametersSupport()) {
      String info = " -- call getByte against VARCHAR -- Changed October 2011";
      try {
        byte p = csTypesB_.getByte("P_VARCHAR_50");
        byte expected = 12;
        assertCondition(p == expected, "Got " + p + " expected " + expected
            + info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + info);
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a BINARY.
   **/
  public void Var044() {
    if (checkNamedParametersSupport()) {
      try {
        byte p = csTypesB_.getByte("P_BINARY_20");
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a VARBINARY.
   **/
  public void Var045() {
    if (checkNamedParametersSupport()) {
      try {
        byte p = csTypesB_.getByte("P_VARBINARY_20");
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a DATE.
   **/
  public void Var046() {
    if (checkNamedParametersSupport()) {
      try {
        byte p = csTypesB_.getByte("P_DATE");
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a TIME.
   **/
  public void Var047() {
    if (checkNamedParametersSupport()) {
      try {
        byte p = csTypesB_.getByte("P_TIME");
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a TIMESTAMP.
   **/
  public void Var048() {
    if (checkNamedParametersSupport()) {
      try {
        byte p = csTypesB_.getByte("P_TIMESTAMP");
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as an OTHER.
   **/
  public void Var049() {

    if (checkNamedParametersSupport()) {
      try {
        byte p = csTypesB_.getByte("D_DATALINK");
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a BLOB.
   **/
  public void Var050() {
    if (checkNamedParametersSupport()) {
      try {
        byte p = csTypesB_.getByte("P_BLOB");
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a CLOB.
   **/
  public void Var051() {
    if (checkNamedParametersSupport()) {
      try {
        byte p = csTypesB_.getByte("P_CLOB");
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a CLOB.
   **/
  public void Var052() {
    if (checkNamedParametersSupport()) {
      try {
        byte p = csTypesB_.getByte("P_DBCLOB");
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a BIGINT.
   **/
  public void Var053() {
    if (checkNamedParametersSupport()) {
      String info = " -- call getByte against BIGINT -- Changed October 2011";
      try {
        byte p = csTypesB_.getByte("P_BIGINT");
        byte expected = 21;
        assertCondition(p == expected, "Got " + p + " expected " + expected
            + info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception " + info);
      }
    }
  }

  /**
   * Repeat the tests for where truncation occurs
   */

  /**
   * getByte() - Get a type that was registered as an INTEGER.
   **/
  public void Var054() {
    String info = " -- call getByte against INTEGER -- Changed October 2011";

    try {
      byte p = csTypesTruncation_.getByte(2);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      checkGetTruncationException(e, info);
    }
  }

  /**
   * getByte() - Get a type that was registered as an REAL.
   **/
  public void Var055() {
    String info = " -- call getByte against REAL -- Changed October 2011";
    try {
      byte p = csTypesTruncation_.getByte(3);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      checkGetTruncationException(e, info);
    }
  }

  /**
   * getByte() - Get a type that was registered as an FLOAT.
   **/
  public void Var056() {
    String info = " -- call getByte against FLOAT -- Changed October 2011";

    try {
      byte p = csTypesTruncation_.getByte(4);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      checkGetTruncationException(e, info);
    }
  }

  /**
   * getByte() - Get a type that was registered as an DOUBLE.
   **/
  public void Var057() {
    String info = " -- call getByte against DOUBLE -- Changed October 2011";

    try {
      byte p = csTypesTruncation_.getByte(5);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      checkGetTruncationException(e, info);
    }
  }

  /**
   * getByte() - Get a type that was registered as an DECIMAL.
   **/
  public void Var058() {
    String info = " -- call getByte against DECMAL -- Changed October 2011";

    try {
      byte p = csTypesTruncation_.getByte(7);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      checkGetTruncationException(e, info);
    }
  }

  /**
   * getByte() - Get a type that was registered as an NUMERIC.
   **/
  public void Var059() {
    if (skipVar059 ) {
      notApplicable("Numeric returns 9 and does not cause trunaction");
    } else {
      String info = " -- call getByte against NUMERIC -- Changed October 2011";

      try {
        byte p = csTypesTruncation_.getByte(9);
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        checkGetTruncationException(e, info);
      }
    }
  }

  /**
   * getByte() - Get a type that was registered as a CHAR.
   **/
  public void Var060() {
    String info = " -- call getByte against CHAR -- Changed October 2011";

    try {
      byte p = csTypesTruncation_.getByte(11);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      checkGetConversionException(e, info);
    }
  }

  /**
   * getByte() - Get a type that was registered as a VARCHAR.
   **/
  public void Var061() {
    String info = " -- call getByte against VARCHAR -- Changed October 2011";

    try {
      byte p = csTypesTruncation_.getByte(12);
      failed("Didn't throw SQLException " + p);
    } catch (Exception e) {
      checkGetConversionException(e, info);
    }
  }

  /**
   * getByte() - Get a type that was registered as a BIGINT.
   **/
  public void Var062() {

    if (checkBigintSupport()) {

      String info = " -- call getByte against BIGINT -- Changed October 2011";
      try {
        byte p = csTypesTruncation_.getByte(22);
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        checkGetTruncationException(e, info);
      }
    }
  }

  
  /**
   * getByte() - Get a type that was registered as a BOOLEAN.
   **/
  public void Var063() {
    if (checkBooleanSupport()) {
      String info = " -- Test added 2021/01/06 for boolean";
      getTestSuccessful(csTypes_, "getByte", 25, "1",info);
    }
  }


  /**
   * getString() - Get a type that was registered as a BOOLEAN that returns
   * false.
   **/
  public void Var064() {
    if (checkBooleanSupport()) { 
      String info = " -- Test added 2021/01/06 for boolean";
      getTestSuccessful(csTypesB_, "getByte", 25, "0", info);
    }
  }

  /**
   * getString() - Get a type that was registered as a BOOLEAN that returns
   * null.
   **/
  public void Var065() {
    if (checkBooleanSupport()) { 
      String info = " -- Test added 2021/01/06 for boolean";
      getTestSuccessful(csTypesNull_, "getByte", 25, "0", info);
    }
  }

  private void checkGetTruncationException(Exception e, String info) {
    String expectedMessage = "Data type mismatch";

    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      expectedMessage = "would result in a loss of precision";
    }

    String exceptionMessage = e.toString();
    boolean passed = exceptionMessage.indexOf(expectedMessage) >= 0;
    if (passed) {
      assertCondition(true);
    } else {
      failed(e, "Expecting '" + expectedMessage + "' " + info);
    }
  }

  private void checkGetConversionException(Exception e, String info) {
    String expectedMessage = "Data type mismatch";

    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      expectedMessage = "is invalid for the requested conversion";
    }

    String exceptionMessage = e.toString();
    boolean passed = exceptionMessage.indexOf(expectedMessage) >= 0;
    if (passed) {
      assertCondition(true);
    } else {
      failed(e, "Expecting '" + expectedMessage + "' " + info);
    }
  }

}
