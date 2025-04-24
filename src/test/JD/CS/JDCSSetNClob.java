///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetNClob.java
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
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetNClob.java
//
// Classes:      JDCSSetNClob
//
////////////////////////////////////////////////////////////////////////
//
//
//                 	        
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.io.FileOutputStream;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;

/**
 * Testcase JDCSSetNClob. This tests the following method of the JDBC
 * CallableStatement class:
 * 
 * 
 * setNClob()
 * 
 **/
public class JDCSSetNClob extends JDCSSetTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSSetNClob";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }
  private static int LARGE_CLOB_SIZE = 20000000;

  /**
   * Constructor.
   **/
  public JDCSSetNClob(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDCSSetNClob", namesAndVars, runMode, fileOutputStream,
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
   * Compares a Clob with a String.
   **/
  private boolean compare(Clob i, String b) throws SQLException {
    return i.getSubString(1, (int) i.length()).equals(b);
  }

  /**
   * setNClob() - Set parameter PARAM_1 which doesn't exist. - Using an ordinal
   * parameter
   **/
  public void Var001() {
    if (checkJdbc40()) {
      try {
        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 0, nClob);// "Test
                                                                // Clob"));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Set parameter PARAM_1 which doesn't exist. - Using an ordinal
   * parameter
   **/
  public void Var002() {
    if (checkJdbc40()) {
      try {
        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", -1, nClob);// "Test
                                                                 // Clob"));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Set parameter PARAM_1 which is too big. - Using an ordinal
   * parameter
   **/
  public void Var003() {
    if (checkJdbc40()) {
      try {
        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 35, nClob);// "Test
                                                                 // Clob"));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Should throw an exception when the callable statment is
   * closed. - Using an ordinal parameter
   **/
  public void Var004() {
    if (checkJdbc40()) {
      try {
        cs.close();
        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 1, nClob);// "Test
                                                                // Clob"));
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setNClob() - Should work with a valid parameter name. - Using an ordinal
   * parameter
   **/
  public void Var005() {
    if (checkJdbc40()) {

      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 20, nClob);// "Test
                                                                 // Clob"));
        cs.execute();
        Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 20);
        cs.close();
        assertCondition(compare(check, "Test Clob"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setNClob() - Should work with a valid parameter name. - Using an ordinal
   * parameter
   **/
  public void Var006() {
    if (checkJdbc40()) {

      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

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
        args[0] = new Integer(20);
        args[1] = null;

        JDReflectionUtil.callMethod_V(cs, "setNClob", argClasses, args);
        cs.execute();
        Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 20);
        boolean wn = cs.wasNull();
        assertCondition((check == null) && (wn == true),
            "value = " + check + " sb null wasNull=" + wn + " sb true ");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setNClob() - Should throw exception when the parameter is not an input
   * parameter. - Using an ordinal parameter
   **/
  public void Var007() {
    if (checkJdbc40()) {
      try {
        CallableStatement cs1 = connection_
            .prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs1, "setNClob", 2, nClob);// ,"Test
                                                                 // Clob"));
        cs1.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }

  }

  /**
   * setNClob() - Set a SMALLINT parameter. - Using an ordinal parameter
   **/
  public void Var008() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 1, nClob);// "Test
                                                                // Clob"));
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
   * setNClob() - Set a INTEGER parameter. - Using an ordinal parameter
   **/
  public void Var009() {
    if (checkJdbc40()) {
      try {

        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 2, nClob);// ,"Test
                                                                // Clob"));
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
   * setNClob() - Set a REAL parameter. - Using an ordinal parameter
   **/
  public void Var010() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 3, nClob);// "Test
                                                                // Clob"));
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
   * setNClob() - Set a FLOAT parameter. - Using an ordinal parameter
   **/
  public void Var011() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 4, nClob);// "Test
                                                                // Clob"));
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
   * setNClob() - Set a DOUBLE parameter. - Using an ordinal parameter
   **/
  public void Var012() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 5, nClob);// "Test
                                                                // Clob"));
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
   * setNClob() - Set a DECIMAL parameter. - Using an ordinal parameter
   **/
  public void Var013() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 6, nClob);// "Test
                                                                // Clob"));
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
   * setNClob() - Set a NUMERIC parameter - Using an ordinal parameter
   **/
  public void Var014() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 8, nClob);// "Test
                                                                // Clob"));
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
   * setNClob() - Set a CHAR(1) parameter. - Using an ordinal parameter
   **/
  public void Var015() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 10, nClob);// "Test
                                                                 // Clob"));
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
   * setNClob() - Set a CHAR(50) parameter. - Using an ordinal parameter
   **/
  public void Var016() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 11, nClob);// "Test
                                                                 // Clob"));
        cs.execute();
        Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 11);

        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          String expected = "Test Clob                                         ";
          assertCondition(expected.equals(check.toString()),
              "got '" + check + "' sb '" + expected + "'");
        } else {
          failed("Didn't Throw SQLException check=" + check);
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
   * setNClob() - Set a VARCHAR(50) parameter. - Using an ordinal parameter
   **/
  public void Var017() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 12, nClob);// "Test
                                                                 // Clob"));
        cs.execute();
        Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 12);
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          String expected = "Test Clob";
          assertCondition(expected.equals(check.toString()),
              "got '" + check + "' sb '" + expected + "'");
        } else {

          failed("Didn't Throw SQLException check=" + check);
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
   * setNClob() - Set a CLOB parameter. - Using an ordinal parameter
   **/
  public void Var018() {
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());
          Object nClob = JDReflectionUtil.callMethod_O(connection_,
              "createNClob");
          JDReflectionUtil.callMethod_V(nClob, "setString", 1L,
              "Test Clob Test");
          JDReflectionUtil.callMethod_V(cs, "setNClob", 20, nClob);// "Test Clob
                                                                   // Test"));
          cs.execute();
          Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 20);
          cs.close();
          assertCondition(compare(check, "Test Clob Test"));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setNClob() - Set a Clob parameter. - Using an ordinal parameter
   **/
  public void Var019() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");

        JDReflectionUtil.callMethod_V(cs, "setNClob", 19, nClob);// "Test
                                                                 // Clob"));
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
   * setNClob() - Set a DATE parameter to a value that is not a valid date. This
   * should throw an exception. - Using an ordinal parameter
   **/
  public void Var020() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 15, nClob);// "Test
                                                                 // Clob"));
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
   * setNClob() - Set a TIME parameter to a value that is not a valid date. This
   * should throw an exception. - Using an ordinal parameter
   **/
  public void Var021() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 16, nClob);// , "Test
                                                                 // Clob"));
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
   * setNClob() - Set a TIMESTAMP parameter to a value that is not valid. This
   * should throw an exception. - Using an ordinal parameter
   **/
  public void Var022() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 17, nClob);// "Test
                                                                 // Clob");
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
   * setNClob() - Set a DATALINK parameter. - Using an ordinal parameter
   **/
  public void Var023() {
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());

          Object nClob = JDReflectionUtil.callMethod_O(connection_,
              "createNClob");
          JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
          JDReflectionUtil.callMethod_V(cs, "setNClob", 18, nClob);// , "Test
                                                                   // Clob"));
          cs.execute();
          Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 18);
          assertCondition(compare(check, "Test Clob"));
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
   * setNClob() - Set a BIGINT parameter. - Using an ordinal parameter
   **/
  public void Var024() {
    if (checkJdbc40()) {
      if (checkBigintSupport()) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());

          Object nClob = JDReflectionUtil.callMethod_O(connection_,
              "createNClob");
          JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
          JDReflectionUtil.callMethod_V(cs, "setNClob", 22, nClob);// "Test
                                                                   // Clob"));
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
   * setNClob() - Set a BINARY parameter, should throw error - Using an ordinal
   * parameter
   **/
  public void Var025() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 13, nClob);// "Test
                                                                 // Clob"));
        cs.execute();
        Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 13);
        assertCondition(compare(check, "Test Clob           "));
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
   * setNClob() - Set a VARBINARY parameter should throw error - Using an
   * ordinal parameter
   **/
  public void Var026() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "Test Clob");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 14, nClob);// "Test
                                                                 // Clob"));
        cs.execute();
        Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 14);
        assertCondition(compare(check, "Test Clob"));

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
   * setNClob() - Set more than one parameter. - Using an ordinal parameter
   **/
  public void Var027() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
        // String b = " clob test";
        // String c = "new clob test";

        Object nClob = JDReflectionUtil.callMethod_O(connection_,
            "createNClob");
        JDReflectionUtil.callMethod_V(nClob, "setString", 1L, "new clob test");
        JDReflectionUtil.callMethod_V(cs, "setNClob", 20, nClob);// " clob
                                                                 // test"));
        JDReflectionUtil.callMethod_V(cs, "setNClob", 20, nClob);// "new clob
                                                                 // test"));
        cs.execute();
        Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 20);
        cs.close();
        assertCondition(compare(check, "new clob test"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setNClob() - Should set to SQL NULL when the value is null. - Using an
   * ordinal parameter
   **/
  public void Var028() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());
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
        args[0] = new Integer(20); 
        args[1] = null;

        JDReflectionUtil.callMethod_V(cs, "setNClob", argClasses, args);
        cs.execute();
        Clob check = (Clob) JDReflectionUtil.callMethod_O(cs, "getNClob", 20);
        assertCondition((check == null), "value = " + check + " sb null");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setNClob() - Using a LARGE CLOB
   **/
  public void Var029() {
    String baseProcName = "JDCSSC029";
    String procName = JDCSTest.COLLECTION + "." + baseProcName;

    if (checkJdbc40()) {
      if (isToolboxDriver()) {
        notApplicable("TOOLBOX can't handle large blobs on 32-bit platforms");
      } else {

        String sql1 = "";
        try {
          Statement stmt = connection_.createStatement();

          try {
            stmt.executeUpdate("drop procedure " + procName);
          } catch (Exception e) {
          }

          StringBuffer sb = new StringBuffer();
          for (int i = 0; i < LARGE_CLOB_SIZE; i++) {
            sb.append('A');

          }
          Object inClob = JDReflectionUtil.callMethod_OS(connection_,
              "createNClob", sb.toString());

          sql1 = "CREATE PROCEDURE " + procName + "(IN B CLOB("
              + LARGE_CLOB_SIZE + "), "
              + " OUT I INT, OUT C CHAR(1)) LANGUAGE SQL " + "SPECIFIC "
              + baseProcName + " XXXX1: BEGIN " + "SET I = LENGTH(B); "
              + "SET C = SUBSTRING(B, 1,1); " + "END XXXX1";

          stmt.executeUpdate(sql1);
          sql1 = "{call " + procName + " (?,?,?)}";
          CallableStatement cstmt = connection_.prepareCall(sql1);
          JDReflectionUtil.callMethod_V(cstmt, "setNClob", 1, inClob);
          cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
          cstmt.registerOutParameter(3, java.sql.Types.CHAR);
          cstmt.execute();
          int answer = cstmt.getInt(2);
          String s = cstmt.getString(3);
          assertCondition((answer == LARGE_CLOB_SIZE) && (s.equals("A")),
              "Error answer is " + answer + " sb " + LARGE_CLOB_SIZE + " s=" + s
                  + " sb A  -- Added by native driver 12/14/2005");
        } catch (Exception e) {
          failed(connection_, e, "Unexpected Exception -- sql1='" + sql1
              + "' -- added by native driver 12/14/2005");
        }
      }
    }
  }

  /**
   * setNClob() - Set a BOOLEAN parameter. - Using an ordinal parameter
   */
  public void Var030() {

    if (checkJdbc40())
      if (checkBooleanSupport()) {

        setTestFailure("setNClob", 27, new StringReader("Test Clob"),
            "Added 2021/01/07 to test boolean support");
      }

  }

  /**
   * setNClob() - Set a BOOLEAN parameter. - Using a named parameter
   */
  public void Var031() {

    if (checkJdbc40())
      if (checkBooleanSupport()) {

        setTestFailure("setNClob", "P_BOOLEAN", new StringReader("Test Clob"),
            "Added 2021/01/07 to test boolean support");
      }

  }

  /**
   * setNClob() - Set a BOOLEAN parameter. - Using an ordinal parameter
   */
  public void Var032() {

    if (checkJdbc40())
      if (checkBooleanSupport()) {

        setTestFailure("setNClob", 27, new StringReader("Test Clob"), 9,
            "Added 2021/01/07 to test boolean support");
      }

  }

  /**
   * setNClob() - Set a BOOLEAN parameter. - Using a named parameter
   */
  public void Var033() {
    if (checkJdbc40())
      if (checkBooleanSupport()) {

        setTestFailure("setNClob", "P_BOOLEAN", new StringReader("Test Clob"),
            9, "Added 2021/01/07 to test boolean support");
      }

  }
}
