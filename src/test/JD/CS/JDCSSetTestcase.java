///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSSetTestcase.java
//
// Classes:      JDCSSetTestcase.java
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
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestcase;

/**
 * Testcase JDCSSetTestcase. This tests the following method of the JDBC
 * CallableStatement class:
 * 
 * SetTestcase()
 * 
 **/
public class JDCSSetTestcase extends JDTestcase {

  // Private data.
  Connection connection_;
  CallableStatement cs;
  boolean lobSupport;
  boolean bigintSupport;;
  boolean decfloatSupport;
  boolean booleanSupport;
  String sql;
  String callSql; 
  
  Hashtable<String, String> createdProceduresHashtable = new Hashtable<String, String>();
  
  /**
   * Constructor.
   **/
  public JDCSSetTestcase(AS400 systemObject, String testname,
      Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password) {
    super(systemObject, testname, namesAndVars, runMode, fileOutputStream,
 password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    connection_ = testDriver_.getConnection(baseURL_ + ";errors=full", userId_, encryptedPassword_);
    lobSupport = supportedFeatures_.lobSupport;
    bigintSupport = supportedFeatures_.bigintSupport;
    decfloatSupport = supportedFeatures_.decfloatSupport;
    booleanSupport = supportedFeatures_.booleanSupport;
    StringBuffer buffer = new StringBuffer();
    buffer.append("CALL " + JDSetupProcedure.STP_CSINOUT
        + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
    if (lobSupport)
      buffer.append(", ?, ?, ?, ?");
    if (bigintSupport)
      buffer.append(",?");
    if (decfloatSupport)
      buffer.append(",?,?");
    if (booleanSupport)
      buffer.append(",?");

    buffer.append(")");
    sql = buffer.toString();
    callSql = sql; 
    cs = connection_.prepareCall(callSql);
    JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
        supportedFeatures_);

    JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
        supportedFeatures_, getDriver());

  }
  
  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    if (cs != null)  {
      cs.close();
      cs=null; 
    }
    connection_.close();
  }

  public void setTestSuccessful(String setMethodName, int parameterNumber,
      String setString, String expectedValue, String info) {
    StringBuffer sb = new StringBuffer(info + "  ");
    try {
      
      cs = connection_.prepareCall(callSql);
      JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
          supportedFeatures_);
      JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
          supportedFeatures_, getDriver());
      if (setMethodName.equals("setBigDecimal")) {
        cs.setBigDecimal(parameterNumber, new BigDecimal(setString));
      } else if (setMethodName.equals("setBoolean")) {
        cs.setBoolean(parameterNumber, Boolean.parseBoolean(setString));
      } else if (setMethodName.equals("setByte")) {
        cs.setByte(parameterNumber, Byte.parseByte(setString));
      } else if (setMethodName.equals("setDouble")) {
        cs.setDouble(parameterNumber, Double.parseDouble(setString));
      } else if (setMethodName.equals("setFloat")) {
        cs.setFloat(parameterNumber, Float.parseFloat(setString));
      } else if (setMethodName.equals("setInt")) {
        cs.setInt(parameterNumber, Integer.parseInt(setString));
      } else if (setMethodName.equals("setLong")) {
        cs.setLong(parameterNumber, Long.parseLong(setString));
      } else if (setMethodName.equals("setNString")) {
        JDReflectionUtil.callMethod_V(cs,setMethodName, parameterNumber, setString);
      } else if (setMethodName.equals("setShort")) {
        cs.setShort(parameterNumber, Short.parseShort(setString));
      } else if (setMethodName.equals("setString")) {
        cs.setString(parameterNumber, setString);
      } else {
        throw new Exception("Set method " + setMethodName + " not supported");
      }

      cs.execute();
      String answer = cs.getString(parameterNumber);
      assertCondition(answer.equals(expectedValue),
          sb.toString() + "Got " + answer + " sb " + expectedValue);
    } catch (Exception e) {
      failed(e, "Unexpected Exception calling "+setMethodName+"("+parameterNumber+","+ setString+")" + sb.toString() );
    }
  }

  public void setTestSuccessful(String setMethodName, String parameterName,
      String setString, String expectedValue, String info) {
    StringBuffer sb = new StringBuffer(info + "  ");
       try {
      cs = connection_.prepareCall(callSql);
      JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
          supportedFeatures_);
      JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
          supportedFeatures_, getDriver());
      if (setMethodName.equals("setBigDecimal")) {
        cs.setBigDecimal(parameterName, new BigDecimal(setString));
      } else if (setMethodName.equals("setBoolean")) {
          cs.setBoolean(parameterName, Boolean.parseBoolean(setString));
      } else if (setMethodName.equals("setByte")) {
        cs.setByte(parameterName, Byte.parseByte(setString));
      } else if (setMethodName.equals("setDouble")) {
        cs.setDouble(parameterName, Double.parseDouble(setString));
      } else if (setMethodName.equals("setFloat")) {
        cs.setFloat(parameterName, Float.parseFloat(setString));
      } else if (setMethodName.equals("setInt")) {
        cs.setInt(parameterName, Integer.parseInt(setString));
      } else if (setMethodName.equals("setLong")) {
        cs.setLong(parameterName, Long.parseLong(setString));
      } else if (setMethodName.equals("setNString")) {
        JDReflectionUtil.callMethod_V(cs,setMethodName, parameterName, setString);
      } else if (setMethodName.equals("setShort")) {
        cs.setShort(parameterName, Short.parseShort(setString));
      } else if (setMethodName.equals("setString")) {
        cs.setString(parameterName, setString);
      } else {
        throw new Exception("Set method " + setMethodName + " not supported");
      }

      cs.execute();
      String answer = cs.getString(parameterName);
      assertCondition(answer.equals(expectedValue),
          sb.toString() + "Got " + answer + " sb " + expectedValue);
    } catch (Exception e) {
      failed(e, "Unexpected Exception calling "+setMethodName+"("+parameterName+","+ setString+")" + sb.toString());
    }
  }

  
  
  public void setTestFailure(String setMethodName, int parameterNumber,
      Object setObject, String info) {
    setTestFailure(setMethodName, parameterNumber,
        setObject, Integer.MIN_VALUE, 0,  info);
  }
  
   public void setTestFailure(String setMethodName, int parameterNumber,
      Object setObject, int arg2,  String info) {
     setTestFailure(setMethodName, parameterNumber, setObject, arg2, 0, info); 
     
   }
  public void setTestFailure(String setMethodName, int parameterNumber,
      Object setObject, int arg2, int arg3, String info) {
    try {
      assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
      cs = connection_.prepareCall(callSql);
      JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
          supportedFeatures_);
      JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
          supportedFeatures_, getDriver());
      if (setMethodName.equals("setObject3")) { 
         JDReflectionUtil.callMethod_V(cs, "setObject", parameterNumber, setObject, arg2);
      } else 
      if (setMethodName.equals("setObject3S")) { 
         JDReflectionUtil.callMethod_V(cs, "setObject", parameterNumber, setObject, getSQLType(arg2));
      } else 
      if (setMethodName.equals("setObject4")) { 
         JDReflectionUtil.callMethod_V(cs, "setObject", parameterNumber, setObject, arg2, arg3);
      } else 
      if (setMethodName.equals("setObject4S")) { 
         JDReflectionUtil.callMethod_V(cs, "setObject", parameterNumber, setObject, getSQLType(arg2), arg3);
      } else 
      if (setMethodName.equals("setArray") ||
          setMethodName.equals("setBlob") || 
          setMethodName.equals("setBytes")||
          setMethodName.equals("setClob")||
          setMethodName.equals("setDate")||
         
          setMethodName.equals("setNClob")||
	  setMethodName.equals("setObject")||
          setMethodName.equals("setRowId")||
          setMethodName.equals("setTime")||
          setMethodName.equals("setTimestamp")||
          setMethodName.equals("setURL")) {
      
        JDReflectionUtil.callMethod_V(cs, setMethodName, parameterNumber, setObject);
      } else if ( setMethodName.equals("setNCharacterStream")) {
        
        if (arg2 == Integer.MIN_VALUE) { 
          JDReflectionUtil.callMethod_V(cs, setMethodName, parameterNumber, setObject);
        } else {
          JDReflectionUtil.callMethod_V(cs, setMethodName, parameterNumber, setObject,  (long) arg2);
        }
      } else {
        throw new Exception("setMethod " + setMethodName + " not supported");
      }
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      try {
        assertExceptionContains(e, dataTypeMismatchExceptions, null);
        cs.close();
      } catch (SQLException s) {
        failed(s, "Unexpected Exception");
      }
    }
  }

  
  public void setTestFailure(String setMethodName, String parameterName,
      Object setObject, String info) {
    setTestFailure(setMethodName, parameterName,
        setObject, Integer.MIN_VALUE, 0, info);
  }
   public void setTestFailure(String setMethodName, String parameterName,
      Object setObject, int arg2, String info) {
    setTestFailure(setMethodName, parameterName,
        setObject, Integer.MIN_VALUE, arg2, info);
  }
  public void setTestFailure(String setMethodName, String parameterName,
      Object setObject, int arg2, int arg3, String info) {
    try {
      assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
      cs = connection_.prepareCall(callSql);
      JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
          supportedFeatures_);
      JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
          supportedFeatures_, getDriver());
      if (setMethodName.equals("setObject3")) { 
         JDReflectionUtil.callMethod_V(cs, "setObject", parameterName, setObject, arg2);
      }
      else if (setMethodName.equals("setObject3S")) { 
         JDReflectionUtil.callMethod_V(cs, "setObject", parameterName, setObject, getSQLType(arg3));
      }

      else if (setMethodName.equals("setObject4")) { 
         JDReflectionUtil.callMethod_V(cs, "setObject", parameterName, setObject, arg2, arg3);
      }
      else if (setMethodName.equals("setObject4S")) { 
         JDReflectionUtil.callMethod_V(cs, "setObject", parameterName, setObject, getSQLType(arg2), arg3);
      }

      else if (setMethodName.equals("setBlob") ||
          setMethodName.equals("setBytes") ||
          setMethodName.equals("setClob")||
          setMethodName.equals("setDate")||
          setMethodName.equals("setNClob")||
          setMethodName.equals("setObject")||
          setMethodName.equals("setRowId")||
          setMethodName.equals("setTime")||
          setMethodName.equals("setTimestamp")||
          setMethodName.equals("setURL")) {
        JDReflectionUtil.callMethod_V(cs, setMethodName, parameterName, setObject);
      } else if ( setMethodName.equals("setNCharacterStream")) {
        if (arg2 == Integer.MIN_VALUE) { 
          JDReflectionUtil.callMethod_V(cs, setMethodName, parameterName, setObject);
        } else {
          JDReflectionUtil.callMethod_V(cs, setMethodName, parameterName, setObject, (long) arg2);
        }
       
      } else {
        throw new Exception("setMethod " + setMethodName + " not supported");
      }
      failed("Didn't throw SQLException");
    } catch (Exception e) {
      try {
        assertExceptionContains(e, dataTypeMismatchExceptions, null);
        
      } catch (Exception s) {
        failed(s, "Unexpected Exception");
      }
    }
  }

  
  public void assureProcedureExists(String procedure) throws Exception  {
 if (createdProceduresHashtable.get(procedure) == null) {
   
     JDSetupProcedure.create (systemObject_, connection_, 
        procedure, supportedFeatures_);
     createdProceduresHashtable.put(procedure, procedure);
 }
   }

  /*
   * Create the SQLType corresponding to Type.
   */
  public Object getSQLType(int typesNumber) throws Exception {
    return JDReflectionUtil.callStaticMethod_O("java.sql.JDBCType", "valueOf",
        typesNumber);
  }



}
