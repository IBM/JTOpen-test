///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetRowId.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetRowId.java
//
// Classes:      JDCSSetRowId
//
////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DataTruncation;
import java.sql.Statement;
import java.util.Hashtable;

/**
 Testcase JDCSSetRowId.  This tests the following
 method of the JDBC CallableStatement class:

 <ul>
 <li>SetRowId()
 </ul>
 **/
public class JDCSSetRowId extends JDCSSetTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSSetRowId";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }

  // Private data.

  byte[] test = new byte[]
    { (byte) 0x34, (byte) 0x45, (byte) 0x50, (byte) 0x56, (byte) 0x67,
        (byte) 0x78, (byte) 0x89, (byte) 0x9A, (byte) 0xAB, (byte) 0xBC,
        (byte) 0xBB, (byte) 0xCB, (byte) 0xDC, (byte) 0xCD, (byte) 0xCC,
        (byte) 0xDD, (byte) 0xED, (byte) 0xDE, (byte) 0xEE, (byte) 0xDD };

  private String added = " -- added by native driver 02/07/2007";

  private Object sampleRowId; 
  /**
   Constructor.
   **/
  public JDCSSetRowId(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream,  String password) {
    super(systemObject, "JDCSSetRowId", namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   Performs setup needed before running variations.

   @exception Exception If an exception occurs.
   **/
  protected void setup() throws Exception {
    super.setup(); 
   
    sampleRowId = createRowId(test); 
    
  }

  Object createRowId(byte[] arg) throws Exception {
    Object testRowId = null;
    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      testRowId = JDReflectionUtil.createObject(
          "com.ibm.db2.jdbc.app.DB2RowId", arg);
    } else {
   //   System.out.println("DRIVER NEEDS TO ADD CODE TO CREATE ROWID OBJECT "+added);
      testRowId = JDReflectionUtil.createObject(
              "com.ibm.as400.access.AS400JDBCRowId", arg);
    }
    return testRowId;
  }

  /**
   Performs cleanup needed after running variations.

   @exception Exception If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    cs.close();
    connection_.close();
  }

  /**
   Utility Function

   returns the hex representation of an array of bytes
   **/
  public static String dumpBytes(byte[] temp) {
    int length = temp.length;
    String s = "";
    for (int i = 0; i < length; i++) {
      String ns = Integer.toHexString(((int) temp[i]) & 0xFF);
      if (ns.length() == 1) {
        s += "0" + ns;
      } else {
        s += ns;
      }
    }
    return s;
  }

  /**
   SetRowId() - Set parameter PARAM_1 which doesn't exist.
   - Using an ordinal parameter
   **/
  public void Var001() {
    if (checkJdbc40()) {

      try {

        JDReflectionUtil.callMethod_V(cs, "setRowId", 0, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   SetRowId() - Should throw an exception when the callable statment is closed.
   - Using an ordinal parameter
   **/
  public void Var002() {
    if (checkJdbc40()) {

      try {
        cs.close();
        JDReflectionUtil.callMethod_V(cs, "setRowId", 1, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   SetRowId() - Should work with a valid parameter name.
   - Using an ordinal parameter
   **/
  public void Var003() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {

      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 14, createRowId(test));
        cs.execute();
        byte[] v = cs.getBytes(14);
        cs.close();
        assertCondition(areEqual(v, test), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }

  /**
   SetRowId() - Should throw exception when the parameter is
   not an input parameter.
   - Using an ordinal parameter
   **/
  public void Var004() {
    if (checkJdbc40()) {

      try {

        CallableStatement cs1 = connection_.prepareCall("CALL "
            + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
        JDReflectionUtil.callMethod_V(cs1, "setRowId", 2, createRowId(test));
        cs1.close();
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }

  }

  /**
   SetRowId() - Verify that a data truncation warning is
   posted when data is truncated.
   - Using an ordinal parameter
   **/
  public void Var005() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {

      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        byte[] long_arr = new byte[]
          { (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04,
              (byte) 0x05, (byte) 0x05, (byte) 0x07, (byte) 0x08, (byte) 0x09,
              (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D, (byte) 0x0E,
              (byte) 0x0F, (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13,
              (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17 };
        JDReflectionUtil.callMethod_V(cs, "setRowId", 13, createRowId(long_arr));
        failed("Didn't throw SQLException"+added);
      } catch (DataTruncation dt) {
        try {
          cs.close();
          assertCondition((dt.getIndex() == 13) && (dt.getParameter() == true)
              && (dt.getRead() == false) && (dt.getDataSize() == 24)
              && (dt.getTransferSize() == 20), added);

        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+ added);
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }

  /**
   SetRowId() - Set a SMALLINT parameter.
   - Using an ordinal parameter
   **/
  public void Var006() {
    if (checkJdbc40()) {

      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 1, createRowId(test));
        cs.execute();
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+ added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a INTEGER parameter.
   - Using an ordinal parameter
   **/
  public void Var007() {
    if (checkJdbc40()) {
      try {

        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 2, createRowId(test));
        cs.execute();
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a REAL parameter.
   - Using an ordinal parameter
   **/
  public void Var008() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 3, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a FLOAT parameter.
   - Using an ordinal parameter
   **/
  public void Var009() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 4, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a DOUBLE parameter.
   - Using an ordinal parameter
   **/
  public void Var010() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 5, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a DECIMAL parameter.
   - Using an ordinal parameter
   **/
  public void Var011() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 6, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a NUMERIC parameter
   - Using an ordinal parameter
   **/
  public void Var012() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 8, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a CHAR(1) parameter.
   - Using an ordinal parameter
   **/
  public void Var013() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 10, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a CHAR(50) parameter.
   - Using an ordinal parameter
   **/
  public void Var014() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 11, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a VARCHAR(50) parameter.
   - Using an ordinal parameter
   **/
  public void Var015() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 12, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a CLOB parameter.
   - Using an ordinal parameter
   **/
  public void Var016() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 20, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a BLOB parameter.
   - Using an ordinal parameter
   **/
  public void Var017() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 19, createRowId(test));
        cs.execute();
        byte[] v = cs.getBytes(19);
        assertCondition(areEqual(v, test), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }

  /**
   SetRowId() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. 
   - Using an ordinal parameter
   **/
  public void Var018() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 15, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. 
   - Using an ordinal parameter
   **/
  public void Var019() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 16, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
   - Using an ordinal parameter
   **/
  public void Var020() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 17, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a DATALINK parameter.
   - Using an ordinal parameter
   **/
  public void Var021() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 18, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a BIGINT parameter.
   - Using an ordinal parameter
   **/
  public void Var022() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 22, createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a BINARY parameter.
   - Using an ordinal parameter
   **/
  public void Var023() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 13, createRowId(test));
        cs.execute();
        byte[] v = cs.getBytes(13);
        assertCondition(areEqual(v, test), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }

  /**
   SetRowId() - Set a VARBINARY parameter.
   - Using an ordinal parameter
   **/
  public void Var024() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", 14, createRowId(test));
        cs.execute();
        byte[] v = cs.getBytes(14);
        assertCondition(areEqual(v, test), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }

  /**
   SetRowId() - Should set to SQL NULL when the value is null.
   - Using an ordinal parameter
   **/
  public void Var025() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        
        Class[] argTypes = new Class[2];
        argTypes[0] = Integer.TYPE;
        try {
          argTypes[1] = Class.forName("java.sql.RowId"); 
        }  catch (Exception e) {
          argTypes[1] = Class.forName("com.ibm.db2.jdbc.app.RowId"); 
        }
        Object[] args = new Object[2];
        args[0] = new Integer(13);
        args[1] = null;
        
        JDReflectionUtil.callMethod_V(cs, "setRowId", argTypes, args);
        
        cs.execute();
        byte[] check = cs.getBytes(13);
        boolean wn = cs.wasNull();
        assertCondition((check == null) && (wn == true), 
            "\ncheck = "+check+" sb null \n"+
            "wasNull="+wn+" sb true"+ added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }

  /**
   SetRowId() - Set parameter PARAM_1 which doesn't exist.
   - Using a named parameter
   **/
  public void Var026() {
    if (checkJdbc40()) {
      try {

        JDReflectionUtil.callMethod_V(cs, "setRowId", "PARAM_1",
            createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   SetRowId() - Should throw an exception when the callable statment is closed.
   - Using a named parameter
   **/
  public void Var027() {
    if (checkJdbc40()) {
      try {
        cs.close();
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_BINARY_20",
            createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   SetRowId() - Should work with a valid parameter name.
   - Using a named parameter
   **/
  public void Var028() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_BINARY_20",
            createRowId(test));
        cs.execute();
        byte[] v = cs.getBytes(13);
        cs.close();
        assertCondition(areEqual(v, test), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }

  /**
   SetRowId() - Should throw exception when the parameter is
   not an input parameter.
   - Using a named parameter
   **/
  public void Var029() {
    if (checkJdbc40()) {
      try {

        CallableStatement cs1 = connection_.prepareCall("CALL "
            + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
        JDReflectionUtil.callMethod_V(cs1, "setRowId", "P2", createRowId(test));
        cs1.close();
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }

  }

  /**
   SetRowId() - Verify that a data truncation warning is
   posted when data is truncated.
   - Using a named parameter
   **/
  public void Var030() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        byte[] long_arr = new byte[]
          { (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04,
              (byte) 0x05, (byte) 0x05, (byte) 0x07, (byte) 0x08, (byte) 0x09,
              (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D, (byte) 0x0E,
              (byte) 0x0F, (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13,
              (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17 };
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_BINARY_20", createRowId(long_arr));

        failed("Didn't throw SQLException"+added);
      } catch (DataTruncation dt) {
        try {
          cs.close();
          assertCondition((dt.getIndex() == 13) && (dt.getParameter() == true)
              && (dt.getRead() == false) && (dt.getDataSize() == 24)
              && (dt.getTransferSize() == 20), added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }

  /**
   SetRowId() - Set a SMALLINT parameter.
   - Using a named parameter
   **/
  public void Var031() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_SMALLINT",
            createRowId(test));
        cs.execute();
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a INTEGER parameter.
   - Using a named parameter
   **/
  public void Var032() {
    if (checkJdbc40()) {
      try {

        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_INTEGER",
            createRowId(test));
        cs.execute();
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a REAL parameter.
   - Using a named parameter
   **/
  public void Var033() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_REAL",
            createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a FLOAT parameter.
   - Using a named parameter
   **/
  public void Var034() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_FLOAT",
            createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a DOUBLE parameter.
   - Using a named parameter
   **/
  public void Var035() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_DOUBLE",
            createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a DECIMAL parameter.
   - Using a named parameter
   **/
  public void Var036() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_DECIMAL_50",
            createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a NUMERIC parameter
   - Using a named parameter
   **/
  public void Var037() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_NUMERIC_50",
            createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a CHAR(1) parameter.
   - Using a named parameter
   **/
  public void Var038() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_CHAR_1",
            createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a CHAR(50) parameter.
   - Using a named parameter
   **/
  public void Var039() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_CHAR_50",
            createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a VARCHAR(50) parameter.
   - Using a named parameter
   **/
  public void Var040() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_VARCHAR_50",
            createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a CLOB parameter.
   - Using a named parameter
   **/
  public void Var041() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_CLOB",
            createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a BLOB parameter.
   - Using a named parameter
   **/
  public void Var042() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_BLOB",
            createRowId(test));
        cs.execute();
        byte[] v = cs.getBytes(19);
        assertCondition(areEqual(v, test), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }

  /**
   SetRowId() - Set a DATE parameter to a value that is not a valid date.  This should throw an exception. 
   - Using a named parameter
   **/
  public void Var043() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_DATE",
            createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a TIME parameter to a value that is not a valid date.  This should throw an exception. 
   - Using a named parameter
   **/
  public void Var044() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_TIME",
            createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a TIMESTAMP parameter to a value that is not valid.  This should throw an exception.
   - Using a named parameter
   **/
  public void Var045() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_TIMESTAMP",
            createRowId(test));
        failed("Didn't throw SQLException"+added);
      } catch (Exception e) {
        try {
          cs.close();
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        } catch (SQLException s) {
          failed(s, "Unexpected Exception"+added);
        }
      }
    }
  }

  /**
   SetRowId() - Set a DATALINK parameter.
   - Using a named parameter
   **/
  public void Var046() {
    if (checkJdbc40()) {
      if (checkLobSupport()) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());
          JDReflectionUtil.callMethod_V(cs, "setRowId", "P_DATALINK",
              createRowId(test));
          failed("Didn't throw SQLException"+added);
        } catch (Exception e) {
          try {
            cs.close();
            assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
          } catch (SQLException s) {
            failed(s, "Unexpected Exception"+added);
          }
        }
      }
    }
  }

  /**
   SetRowId() - Set a BIGINT parameter.
   - Using a named parameter
   **/
  public void Var047() {
    if (checkJdbc40()) {
      if (checkBigintSupport()) {
        try {
          cs = connection_.prepareCall(sql);
          JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_);
          JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
              supportedFeatures_, getDriver());
          JDReflectionUtil.callMethod_V(cs, "setRowId", "P_BIGINT",
              createRowId(test));
          failed("Didn't throw SQLException"+added);
        } catch (Exception e) {
          try {
            cs.close();
            assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
          } catch (SQLException s) {
            failed(s, "Unexpected Exception"+added);
          }
        }
      }
    }
  }

  /**
   SetRowId() - Set a BINARY parameter.
   - Using a named parameter
   **/
  public void Var048() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_BINARY_20",
            createRowId(test));
        cs.execute();
        byte[] v = cs.getBytes(13);
        assertCondition(areEqual(v, test), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }

  /**
   SetRowId() - Set a VARBINARY parameter.
   - Using a named parameter
   **/
  public void Var049() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_VARBINARY_20",
            createRowId(test));
        cs.execute();
        byte[] v = cs.getBytes(14);
        assertCondition(areEqual(v, test), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }

  /**
   SetRowId() - Should set to SQL NULL when the value is null.
   - Using a named parameter
   **/
  public void Var050() {
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());
        
        
        Class[] argTypes = new Class[2];
        argTypes[0] = Class.forName("java.lang.String"); 
        try {
          argTypes[1] = Class.forName("java.sql.RowId"); 
        }  catch (Exception e) {
          argTypes[1] = Class.forName("com.ibm.db2.jdbc.app.RowId"); 
        }
        Object[] args = new Object[2];
        args[0] = "P_VARBINARY_20";
        args[1] = null;
        
        JDReflectionUtil.callMethod_V(cs, "setRowId", argTypes, args);
        
        cs.execute();
        byte[] check = cs.getBytes(14);
        boolean wn = cs.wasNull();
        cs.close();
        assertCondition((check == null) && (wn == true), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }

  /**
   SetRowId() - Attempt to set a parameter twice.
   - Using an ordinal parameter
   **/
  public void Var051() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());

        JDReflectionUtil.callMethod_V(cs, "setRowId", 13, createRowId(test));
        byte[] test2 = new byte[]
          { (byte) 0xFF, (byte) 0x45, (byte) 0x50, (byte) 0xFF, (byte) 0x67,
              (byte) 0x78, (byte) 0xFF, (byte) 0x9A, (byte) 0xAB, (byte) 0xFF };
        byte[] test2exp = new byte[]
          { (byte) 0xFF, (byte) 0x45, (byte) 0x50, (byte) 0xFF, (byte) 0x67,
              (byte) 0x78, (byte) 0xFF, (byte) 0x9A, (byte) 0xAB, (byte) 0xFF,
              (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
              (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, };
        JDReflectionUtil.callMethod_V(cs, "setRowId", 13, createRowId(test2));
        cs.execute();
        byte[] v = cs.getBytes(13);
        cs.close();
        assertCondition(areEqual(v, test2exp), "v = " + dumpBytes(v)
            + " and test2 = " + dumpBytes(test2exp)+added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }

  /**
   SetRowId() - Attempt to set a parameter twice.
   - Using a named parameter
   **/
  public void Var052() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());

        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_BINARY_20",
            createRowId(test));
        byte[] test2 = new byte[]
          { (byte) 0xFF, (byte) 0x45, (byte) 0x50, (byte) 0xFF, (byte) 0x67,
              (byte) 0x78, (byte) 0xFF, (byte) 0x9A, (byte) 0xAB, (byte) 0xFF };
        byte[] test2exp = new byte[]
          { (byte) 0xFF, (byte) 0x45, (byte) 0x50, (byte) 0xFF, (byte) 0x67,
              (byte) 0x78, (byte) 0xFF, (byte) 0x9A, (byte) 0xAB, (byte) 0xFF,
              (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
              (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, };
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_BINARY_20", createRowId(test2));
        cs.execute();
        byte[] v = cs.getBytes(13);
        cs.close();
        assertCondition(areEqual(v, test2exp), "v = " + dumpBytes(v)
            + " and test2 = " + dumpBytes(test2exp)+added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }

  /**
   SetRowId() - Attempt to set a parameter twice.
   - Using an ordinal parameter
   **/
  public void Var053() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());

        JDReflectionUtil.callMethod_V(cs, "setRowId", 14, createRowId(test));
        byte[] test2 = new byte[]
          { (byte) 0xFF, (byte) 0x45, (byte) 0x50, (byte) 0xFF, (byte) 0x67,
              (byte) 0x78, (byte) 0xFF, (byte) 0x9A, (byte) 0xAB, (byte) 0xFF };
        JDReflectionUtil.callMethod_V(cs, "setRowId", 14, createRowId(test2));
        cs.execute();
        byte[] v = cs.getBytes(14);
        cs.close();
        assertCondition(areEqual(v, test2), "v = " + dumpBytes(v)
            + " and test2 = " + dumpBytes(test2)+added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }

  /**
   SetRowId() - Attempt to set a parameter twice.
   - Using a named parameter
   **/
  public void Var054() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        cs = connection_.prepareCall(sql);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT, supportedFeatures_
            , getDriver());

        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_VARBINARY_20",
            createRowId(test));
        byte[] test2 = new byte[]
          { (byte) 0xFF, (byte) 0x45, (byte) 0x50, (byte) 0xFF, (byte) 0x67,
              (byte) 0x78, (byte) 0xFF, (byte) 0x9A, (byte) 0xAB, (byte) 0xFF };
        JDReflectionUtil.callMethod_V(cs, "setRowId", "P_VARBINARY_20", createRowId(test2));
        cs.execute();
        byte[] v = cs.getBytes(14);
        cs.close();
        assertCondition(areEqual(v, test2), "v = " + dumpBytes(v)
            + " and test2 = " + dumpBytes(test2));
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+ added);
      }
    }
  }
  
  
  /**
  setRowId() - Set a ROWID parameter and make sure it works. 
  **/
      public void Var055()
      {
          String sql = ""; 
          if (checkJdbc40 ()) {
              try {
                  Statement statement_ = connection_.createStatement(); 
                  String tableName = JDCSTest.COLLECTION+".JDCSSROWID"; 
                  try { 
                     statement_.executeUpdate ("DROP TABLE "+tableName); 
                  } catch (Exception e) {} 

                  sql ="CREATE TABLE " +tableName+" (R ROWID, STUFF INT)"; 
                  statement_.executeUpdate (sql); 


                  String procedure = JDCSTest.COLLECTION+".JDCSSRIDP";
                  
                  try { 
                    statement_.executeUpdate ("DROP PROCEDURE  "+procedure); 
                 } catch (Exception e) {} 

                  sql = "create procedure "+procedure+" (X ROWID) dynamic result set 1 "+
                        "LANGUAGE SQL BEGIN DECLARE C1 CURSOR FOR SELECT R FROM "+tableName+
                        " WHERE R=X; open c1; set result sets cursor c1; end"; 
                  statement_.executeUpdate (sql); 
                  
                  sql = "INSERT INTO " + tableName+ " VALUES(DEFAULT, 1)";
                  statement_.executeUpdate (sql);
                  sql = "SELECT * FROM "+tableName; 
                  ResultSet rs = statement_.executeQuery (sql);  
                  rs.next(); 
                  Object rowId = JDReflectionUtil.callMethod_O(rs, "getRowId", 1); 
                  rs.close(); 
                  byte[] b = (byte[]) JDReflectionUtil.callMethod_O(rowId, "getBytes"); 
                  
                  sql = "CALL "+procedure+"(?)";  
                  CallableStatement cs = connection_.prepareCall( sql); 
                  
                  JDReflectionUtil.callMethod_V(cs, "setRowId", 1, rowId);
                  rs = cs.executeQuery(); 
                  rs.next ();
                  byte[] check = rs.getBytes(1); 

                  rs.close ();
                  sql = "DROP TABLE "+tableName; 
                  statement_.executeUpdate (sql); 

                  assertCondition (areEqual (b, check), added);
              }
              catch (Exception e) {
                  failed (e, "Unexpected Exception last SQL was "+sql+added);
              }
          }
      }

      /**
       * setRowId() - Set a BOOLEAN parameter. - Using an ordinal parameter
       */
      public void Var056() {

        if (checkBooleanSupport()) {

          setTestFailure("setRowId", 27, sampleRowId,
              "Added 2021/01/07 to test boolean support");
        }

      }

      /**
       * setRowId() - Set a BOOLEAN parameter. - Using a named parameter
       */
      public void Var057() {

        if (checkBooleanSupport()) {

          setTestFailure("setRowId", "P_BOOLEAN", sampleRowId,
              "Added 2021/01/07 to test boolean support");
        }

      }


}
