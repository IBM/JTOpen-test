///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetURL.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetURL.java
//
// Classes:      JDCSSetURL
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDSetupProcedure;
import test.JDTestDriver;

/**
 * Testcase JDCSSetURL. This tests the following method of the JDBC
 * CallableStatement class:
 * 
 * 
 * setURL()
 * 
 **/
public class JDCSSetURL extends JDCSSetTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSSetURL";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }

  private URL sampleURL;

  /**
   * Constructor.
   **/
  public JDCSSetURL(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDCSSetURL", namesAndVars, runMode, fileOutputStream,
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
    sampleURL = new URL("http://java.sun.com");

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
   * setURL() - Set parameter PARAM_1 which doesn't exist. - Using an ordinal
   * parameter
   **/
  public void Var001() {
    if (checkJdbc30()) {
      try {
        cs.setURL(0, sampleURL);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setURL() - Set parameter PARAM_1 which doesn't exist. - Using an ordinal
   * parameter
   **/
  public void Var002() {
    if (checkJdbc30()) {
      try {
        cs.setURL(-1, sampleURL);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setURL() - Set parameter PARAM_1 which is too big. - Using an ordinal
   * parameter
   **/
  public void Var003() {
    if (checkJdbc30()) {
      try {
        cs.setURL(35, sampleURL);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setURL() - Should throw an exception when the callable statment is closed.
   * - Using an ordinal parameter
   **/
  public void Var004() {
    if (checkJdbc30()) {
      try {
        cs.close();
        cs.setURL(1, sampleURL);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setURL() - Should work with a valid parameter name. - Using an ordinal
   * parameter
   **/
  public void Var005() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("jcc says Method is not supported yet ");
      return;
    }
    if (checkJdbc30()) {

      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL(18, sampleURL);
        cs.execute();
        URL check = cs.getURL(18);
        cs.close();
        assertCondition(
            (check.toString()).equalsIgnoreCase("http://java.sun.com"));
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setURL() - Should work with a valid parameter name. - Using an ordinal
   * parameter
   **/
  public void Var006() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("jcc says Method is not supported yet ");
      return;
    }
    String info = " -- testcase updated 10/2011 for toolbox driver -- null is now value parameter";
    if (checkJdbc30()) {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());
          cs.setURL(18, null);
          cs.execute();
          URL check = cs.getURL(18);
          boolean wn = cs.wasNull();

          assertCondition((check == null) && (wn == true),
              "check=" + check + " sb null " + info);
        } catch (MalformedURLException e) {
          failed(e, "Malformed URL Exception" + info);
        } catch (Exception e) {
          failed(e, "Unexpected Exception" + info);
        }
      } else {
        notApplicable();
      }
    }
  }

  /**
   * setURL() - Should throw exception when the parameter is not an input
   * parameter. - Using an ordinal parameter
   **/
  public void Var007() {
    if (checkJdbc30()) {
      try {
        CallableStatement cs1 = connection_
            .prepareCall("CALL " + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");

        cs1.setURL(2, sampleURL);
        cs1.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setURL() - Set a SMALLINT parameter. - Using an ordinal parameter
   **/
  public void Var008() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL(1, sampleURL);
        cs.execute();
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a INTEGER parameter. - Using an ordinal parameter
   **/
  public void Var009() {
    if (checkJdbc30()) {
      try {

        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL(2, sampleURL);
        cs.execute();
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a REAL parameter. - Using an ordinal parameter
   **/
  public void Var010() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL(3, sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a FLOAT parameter. - Using an ordinal parameter
   **/
  public void Var011() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL(4, sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a DOUBLE parameter. - Using an ordinal parameter
   **/
  public void Var012() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL(5, sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a DECIMAL parameter. - Using an ordinal parameter
   **/
  public void Var013() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL(6, sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a NUMERIC parameter - Using an ordinal parameter
   **/
  public void Var014() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL(8, sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a CHAR(1) parameter. - Using an ordinal parameter
   **/
  public void Var015() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL(10, sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a CHAR(50) parameter. - Using an ordinal parameter
   **/
  // @A1
  public void Var016() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("jcc says Method is not supported yet ");
      return;
    }
    if (checkJdbc30()) {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {

        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());

          cs.setURL(11, sampleURL);
          if (isToolboxDriver()) {
            cs.execute();
            URL check = cs.getURL(11);
            cs.close();
            assertCondition(
                (check.toString()).equalsIgnoreCase("http://java.sun.com"));
          } else {
            cs.execute();
            URL nativecheck = cs.getURL(11);
            cs.close();
            assertCondition((nativecheck.toString())
                .equalsIgnoreCase("http://java.sun.com"));
          }

        } catch (MalformedURLException e) {
          failed(e, "Malformed URL Exception");
        } catch (Exception e) {
          if (isToolboxDriver())
            failed(e, "Unexpected Exception");
          else
            failed(e, "Unexpected Exception");
        }
      } else {
        notApplicable();
      }
    }
  }

  /**
   * setURL() - Set a VARCHAR(50) parameter. - Using an ordinal parameter
   **/
  public void Var017() {
    if (checkJdbc30()) {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());

          cs.setURL(12, sampleURL);
          cs.execute();
          URL check = cs.getURL(12);
          cs.close();
          assertCondition(
              (check.toString()).equalsIgnoreCase("http://java.sun.com"));
        } catch (MalformedURLException e) {
          failed(e, "Malformed URL Exception");
        } catch (Exception e) {
          try {
            if (isToolboxDriver())
              failed(e, "Unexcpected Exception");
            else
              assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            cs.close();
          } catch (SQLException s) {
            failed(s, "Unexpected Exception");
          }
        }
      } else {
        notApplicable();
      }

    }
  }

  /**
   * setURL() - Set a CLOB parameter. - Using an ordinal parameter
   **/
  public void Var018() {
    if (checkJdbc30()) {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        if (checkLobSupport()) {
          try {
            cs = connection_.prepareCall(sql);
            JDSetupProcedure.setTypesParameters(cs,
                JDSetupProcedure.STP_CSINOUT, supportedFeatures_);
            JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
                supportedFeatures_, getDriver());

            cs.setURL(20, sampleURL);
            if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
              cs.execute();
              URL check = cs.getURL(20);
              assertCondition(
                  (check.toString()).equalsIgnoreCase("http://java.sun.com"));
            } else
              failed("Didn't throw SQLException");
          } catch (MalformedURLException e) {
            failed(e, "Malformed URL Exception");
          } catch (Exception e) {
            try {
              assertExceptionIsInstanceOf(e, "java.sql.SQLException");
              cs.close();
            } catch (SQLException s) {
              failed(s, "Unexpected Exception");
            }
          }
        }
      } else {
        notApplicable();
      }

    }
  }

  /**
   * setURL() - Set a BLOB parameter. - Using an ordinal parameter
   **/
  public void Var019() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL(19, sampleURL);
        failed("Didn't throw SQL Exception");
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
   * setURL() - Set a DATE parameter to a value that is not a valid date. This
   * should throw an exception. - Using an ordinal parameter
   **/
  public void Var020() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL(15, sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a TIME parameter to a value that is not a valid date. This
   * should throw an exception. - Using an ordinal parameter
   **/
  public void Var021() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL(16, sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a TIMESTAMP parameter to a value that is not valid. This
   * should throw an exception. - Using an ordinal parameter
   **/
  public void Var022() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL(17, sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a DATALINK parameter. - Using an ordinal parameter
   **/
  public void Var023() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("jcc says Method is not supported yet ");
      return;
    }
    if (checkJdbc30()) {
      if (checkLobSupport()) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());

          cs.setURL(18, sampleURL);
          cs.execute();
          URL check = cs.getURL(18);
          cs.close();
          assertCondition(
              (check.toString()).equalsIgnoreCase("http://java.sun.com"));
        } catch (MalformedURLException e) {
          failed(e, "Malformed URL Exception");
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setURL() - Set a BIGINT parameter. - Using an ordinal parameter
   **/
  public void Var024() {
    if (checkJdbc30()) {
      if (checkBigintSupport()) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());

          cs.setURL(22, sampleURL);
          failed("Didn't throw SQLException");
        } catch (MalformedURLException e) {
          failed(e, "Malformed URL Exception");
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
   * setURL() - Set a BINARY parameter, should throw error - Using an ordinal
   * parameter
   **/
  // @A1 This should work by default binary translate to character
  public void Var025() {
    if (checkJdbc30()) {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());

          cs.setURL(13, sampleURL);
          if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            cs.execute();
            URL check = cs.getURL(13);
            assertCondition(
                (check.toString()).equalsIgnoreCase("http://java.sun.com"));
          } else
            failed("Didn't throw SQLException");
        } catch (MalformedURLException e) {
          failed(e, "Malformed URL Exception");
        } catch (Exception e) {
          try {
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            cs.close();
          } catch (SQLException s) {
            failed(s, "Unexpected Exception");
          }
        }
      } else {
        notApplicable();
      }
    }
  }

  /**
   * setURL() - Set a VARBINARY parameter should throw error - Using an ordinal
   * parameter
   **/
  // @ A1
  public void Var026() {
    if (checkJdbc30()) {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());

          cs.setURL(14, sampleURL);
          if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            cs.execute();
            URL check = cs.getURL(14);
            assertCondition(
                (check.toString()).equalsIgnoreCase("http://java.sun.com"));
          } else
            failed("Didn't throw SQLException");
        } catch (MalformedURLException e) {
          failed(e, "Malformed URL Exception");
        } catch (Exception e) {
          try {
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            cs.close();
          } catch (SQLException s) {
            failed(s, "Unexpected Exception");
          }
        }
      } else {
        notApplicable();
      }
    }
  }

  /**
   * setURL() - Set more than one parameter. - Using an ordinal parameter
   **/
  public void Var027() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("jcc says Method is not supported yet ");
      return;
    }
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL(18, sampleURL);
        cs.setURL(18, new URL("http://www.ibm.com/us"));
        cs.execute();
        URL check = cs.getURL(18);
        cs.close();
        assertCondition(
            (check.toString()).equalsIgnoreCase("http://www.ibm.com/us"),
            "check = " + check + " and SB http://www.ibm.com/us");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  // now using named parameters

  /**
   * setURL() - Set a SMALLINT parameter. - Using an named parameter
   **/
  public void Var028() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL("P_SMALLINT", sampleURL);
        cs.execute();
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a INTEGER parameter. - Using an named parameter
   **/
  public void Var029() {
    if (checkJdbc30()) {
      try {

        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL("P_INTEGER", sampleURL);
        cs.execute();
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a REAL parameter. - Using an named parameter
   **/
  public void Var030() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL("P_REAL", sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a FLOAT parameter. - Using an named parameter
   **/
  public void Var031() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL("P_FLOAT", sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a DOUBLE parameter. - Using an named parameter
   **/
  public void Var032() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL("P_DOUBLE", sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a DECIMAL parameter. - Using an named parameter
   **/
  public void Var033() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL("P_DECIMAL_50", sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a NUMERIC parameter - Using an named parameter
   **/
  public void Var034() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL("P_NUMERIC_50", sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a CHAR(1) parameter. - Using an named parameter
   **/
  public void Var035() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL("P_CHAR_1", sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a CHAR(50) parameter. - Using an named parameter
   **/
  public void Var036() {
    if (checkJdbc30()) {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());

          cs.setURL("P_CHAR_50", sampleURL);
          cs.execute();
          URL check = cs.getURL("P_CHAR_50");
          cs.close();
          assertCondition(
              (check.toString()).equalsIgnoreCase("http://java.sun.com"));
        } catch (MalformedURLException e) {
          failed(e, "Malformed URL Exception");
        } catch (Exception e) {

          try {
            if (isToolboxDriver())
              failed(e, "Unexpected Exception");
            else
              assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            cs.close();
          } catch (SQLException s) {
            failed(s, "Unexpected Exception");
          }
        }
      } else {
        notApplicable();
      }
    }
  }

  /**
   * setURL() - Set a VARCHAR(50) parameter. - Using an named parameter
   **/
  public void Var037() {
    if (checkJdbc30()) {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());

          cs.setURL("P_VARCHAR_50", sampleURL);
          cs.execute();
          URL check = cs.getURL("P_VARCHAR_50");
          cs.close();
          assertCondition(
              (check.toString()).equalsIgnoreCase("http://java.sun.com"));

        } catch (MalformedURLException e) {
          failed(e, "Malformed URL Exception");
        } catch (Exception e) {
          try {
            if (isToolboxDriver())
              failed(e, "Unexpected Exception");
            else
              assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            cs.close();
          } catch (SQLException s) {
            failed(s, "Unexpected Exception");
          }
        }
      } else {
        notApplicable();
      }
    }
  }

  /**
   * setURL() - Set a CLOB parameter. - Using an named parameter
   **/
  public void Var038() {
    if (checkJdbc30()) {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        if (checkLobSupport()) {
          try {
            cs = connection_.prepareCall(sql);
            JDSetupProcedure.setTypesParameters(cs,
                JDSetupProcedure.STP_CSINOUT, supportedFeatures_);
            JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
                supportedFeatures_, getDriver());

            cs.setURL("P_CLOB", sampleURL);

            if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
              cs.execute();
              URL check = cs.getURL("P_CLOB");
              assertCondition(
                  (check.toString()).equalsIgnoreCase("http://java.sun.com"));
            } else
              failed("Didn't throw SQLException");
          } catch (MalformedURLException e) {
            failed(e, "Malformed URL Exception");
          } catch (Exception e) {
            try {
              assertExceptionIsInstanceOf(e, "java.sql.SQLException");
              cs.close();
            } catch (SQLException s) {
              failed(s, "Unexpected Exception");
            }
          }
        }
      } else {
        notApplicable();
      }
    }
  }

  /**
   * setURL() - Set a BLOB parameter. - Using an named parameter
   **/
  public void Var039() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL("P_BLOB", sampleURL);
        failed("Didn't throw SQL Exception");
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
   * setURL() - Set a DATE parameter to a value that is not a valid date. This
   * should throw an exception. - Using an named parameter
   **/
  public void Var040() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL("P_DATE", sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a TIME parameter to a value that is not a valid date. This
   * should throw an exception. - Using an named parameter
   **/
  public void Var041() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL("P_TIME", sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a TIMESTAMP parameter to a value that is not valid. This
   * should throw an exception. - Using an named parameter
   **/
  public void Var042() {
    if (checkJdbc30()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_, getDriver());

        cs.setURL("P_TIMESTAMP", sampleURL);
        failed("Didn't throw SQLException");
      } catch (MalformedURLException e) {
        failed(e, "Malformed URL Exception");
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
   * setURL() - Set a DATALINK parameter. - Using an named parameter
   **/
  public void Var043() {
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      notApplicable("jcc says Method is not supported yet ");
      return;
    }
    if (checkJdbc30()) {
      if (checkLobSupport()) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());

          cs.setURL("P_DATALINK", sampleURL);
          cs.execute();
          URL check = cs.getURL(18);
          cs.close();
          assertCondition(
              (check.toString()).equalsIgnoreCase("http://java.sun.com"));
        } catch (MalformedURLException e) {
          failed(e, "Malformed URL Exception");
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setURL() - Set a BIGINT parameter. - Using an named parameter
   **/
  public void Var044() {
    if (checkJdbc30()) {
      if (checkBigintSupport()) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());

          cs.setURL("P_BIGINT", sampleURL);
          failed("Didn't throw SQLException");
        } catch (MalformedURLException e) {
          failed(e, "Malformed URL Exception");
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
   * setURL() - Set a BINARY parameter, should throw error - Using an named
   * parameter
   **/
  public void Var045() {
    if (checkJdbc30()) {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());

          cs.setURL("P_BINARY_20", sampleURL);
          if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            cs.execute();
            URL check = cs.getURL("P_BINARY_20");
            assertCondition(
                (check.toString()).equalsIgnoreCase("http://java.sun.com"));
          } else
            failed("Didn't throw SQLException");
        } catch (MalformedURLException e) {
          failed(e, "Malformed URL Exception");
        } catch (Exception e) {
          try {
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            cs.close();
          } catch (SQLException s) {
            failed(s, "Unexpected Exception");
          }
        }
      } else {
        notApplicable();
      }
    }
  }

  /**
   * setURL() - Set a VARBINARY parameter should throw error - Using an named
   * parameter
   **/
  public void Var046() {
    if (checkJdbc30()) {
      if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());

          cs.setURL("P_VARBINARY_20", sampleURL);
          if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            cs.execute();
            URL check = cs.getURL("P_VARBINARY");
            assertCondition(
                (check.toString()).equalsIgnoreCase("http://java.sun.com"));
          } else
            failed("Didn't throw SQLException");
        } catch (MalformedURLException e) {
          failed(e, "Malformed URL Exception");
        } catch (Exception e) {
          try {
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            cs.close();
          } catch (SQLException s) {
            failed(s, "Unexpected Exception");
          }
        }
      } else {
        notApplicable();
      }
    }
  }

  /**
   * setURL() - Set a BOOLEAN parameter. - Using an ordinal parameter
   */
  public void Var047() {

    if (checkBooleanSupport()) {

      setTestFailure("setURL", 27, sampleURL,
          "Added 2021/01/07 to test boolean support");
    }

  }

  /**
   * setURL() - Set a BOOLEAN parameter. - Using a named parameter
   */
  public void Var048() {

    if (checkBooleanSupport()) {

      setTestFailure("setURL", "P_BOOLEAN", sampleURL,
          "Added 2021/01/07 to test boolean support");
    }

  }

}
