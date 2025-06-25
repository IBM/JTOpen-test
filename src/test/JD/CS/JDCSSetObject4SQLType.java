///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetObject4SQLType.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.CS;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.Date;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDLobTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;

/**
 * Testcase JDCSSetObject4SQLType. This tests the following method of the JDBC
 * Callable class (4 parameters):
 * 
 * <ul>
 * <li>setObject(int,Object,int,int)
 * </ul>
 **/
public class JDCSSetObject4SQLType extends JDCSSetObjectBase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSSetObject4SQLType";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }
  Connection connectionDateTime_; 

  /**
   * Constructor.
   **/
  public JDCSSetObject4SQLType(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDCSSetObject4SQLType", namesAndVars, runMode,
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
 String url = baseURL_
    
    
    + ";data truncation=true";
  connectionDateTime_ = testDriver_.getConnection (url+";date format=jis;time format=jis",systemObject_.getUserId(), encryptedPassword_);


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
   * setObject() - Should throw exception when the prepared statement is closed.
   **/
  public void Var001() {
    if (checkJdbc42()) {
      try {

        CallableStatement cs = prepareCall(Types.NUMERIC);
        cs.close();
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal("2.3"),
            getSQLType(Types.NUMERIC), 1);
        cs.registerOutParameter(2, Types.NUMERIC);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Should throw exception when an invalid index is specified.
   **/
  public void Var002() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.INTEGER);
        JDReflectionUtil.callMethod_V(cs, "setObject", 100, Integer.valueOf(3),
            getSQLType(Types.INTEGER), 0);
        cs.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Should throw exception when index is 0.
   **/
  public void Var003() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.VARCHAR);
        JDReflectionUtil.callMethod_V(cs, "setObject", 0, "Hi Mom",
            getSQLType(Types.VARCHAR), 0);
        cs.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Should throw exception when index is -1.
   **/
  public void Var004() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.VARCHAR);
        JDReflectionUtil.callMethod_V(cs, "setObject", -1, "Yo Dad",
            getSQLType(Types.VARCHAR), 0);
        cs.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Should work with a valid parameter index greater than 1.
   **/
  public void Var005() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.NUMERIC);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1,
            new BigDecimal("4.3"), getSQLType(Types.NUMERIC), 1);
        cs.registerOutParameter(2, Types.NUMERIC);
        cs.executeUpdate();
        BigDecimal check = cs.getBigDecimal(2);
        cs.close();

        assertCondition(check.doubleValue() == 4.3);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Should set to SQL NULL when the object is null.
   **/
  public void Var006() {
    if (checkJdbc42()) {
      try {

        CallableStatement cs = prepareCall(Types.NUMERIC);
        Class<?>[] argClasses = new Class[4];
        argClasses[0] = Integer.TYPE;
        argClasses[1] = Class.forName("java.lang.Object");
        argClasses[2] = Class.forName("java.sql.SQLType");
        argClasses[3] = Integer.TYPE;
        Object[] args = new Object[4];
        args[0] = Integer.valueOf(1);
        args[1] = null;
        args[2] = getSQLType(Types.NUMERIC);
        args[3] = Integer.valueOf(1);
        JDReflectionUtil.callMethod_V(cs, "setObject", argClasses, args);

        cs.registerOutParameter(2, Types.NUMERIC);
        cs.executeUpdate();
        BigDecimal check = cs.getBigDecimal(2);
        boolean wn = cs.wasNull();
        cs.close();

        assertCondition((check == null) && (wn == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Should throw exception when the type is invalid.
   **/
  public void Var007() {
    notApplicable("cannot pass invalid type");
    /*
     * if (checkJdbc42()) { try { CallableStatement cs =
     * prepareCall(Types.VARCHAR);
     * JDReflectionUtil.callMethod_V(cs,"setObject",0, "Hi Mom", 73639823,0);
     * cs.close(); failed("Didn't throw SQLException"); } catch (Exception e) {
     * assertExceptionIsInstanceOf(e, "java.sql.SQLException"); } }
     */
  }

  /**
   * setObject() - Should throw exception when the parameter is not an input
   * parameter.
   **/
  public void Var008() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.INTEGER);
        JDReflectionUtil.callMethod_V(cs, "setObject", 2, Integer.valueOf(3),
            getSQLType(Types.INTEGER), 0);
        cs.close();
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Should throw exception when the parameter is not anything
   * close to being a JDBC-style type.
   **/
  @SuppressWarnings("rawtypes")
  public void Var009() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.SMALLINT);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new Hashtable(),
            getSQLType(Types.SMALLINT), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a SMALLINT parameter.
   **/
  public void Var010() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.SMALLINT);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1,
            Short.valueOf((short) -33), getSQLType(Types.SMALLINT), 0);
        cs.registerOutParameter(2, Types.SMALLINT);
        cs.execute();
        short check = cs.getShort(2);
        cs.close();

        assertCondition(check == -33, "returned " + check + " sb -33");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a SMALLINT parameter, when the data gets truncated.
   **/
  public void Var011() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.SMALLINT);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            -24433323.0), getSQLType(Types.SMALLINT), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
          assertSqlException(e, -99999, "07006", "Data type mismatch",
              "Mismatch instead of truncation in V7R1");

      }
    }
  }

  /**
   * setObject() - Set a SMALLINT parameter, when the data contains a fraction.
   * This is ok.
   **/
  public void Var012() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.SMALLINT);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Float.valueOf(-33.3f),
            getSQLType(Types.SMALLINT), 0);
        cs.registerOutParameter(2, Types.SMALLINT);
        cs.execute();
        short check = cs.getShort(2);
        cs.close();

        assertCondition(check == -33, "returned " + check + " sb -33");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a SMALLINT parameter, when the object is the wrong type.
   **/
  public void Var013() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.SMALLINT);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new Time(7450),
            getSQLType(Types.SMALLINT), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a SMALLINT parameter, when the type is invalid.
   **/
  public void Var014() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.SMALLINT);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1,
            Short.valueOf((short) -33), getSQLType(Types.NULL), 0);
        cs.registerOutParameter(2, Types.SMALLINT);
        cs.execute();
        short check = cs.getShort(2);
        cs.close();

        assertCondition(check == -33, "returned " + check + " sb -33");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an INTEGER parameter.
   **/
  public void Var015() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.INTEGER);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Integer.valueOf(9595),
            getSQLType(Types.INTEGER), 0);
        cs.registerOutParameter(2, Types.INTEGER, 0);
        cs.execute();
        int check = cs.getInt(2);
        cs.close();

        assertCondition(check == 9595, "returned " + check + " sb 9595");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a INTEGER parameter, when the data gets truncated.
   **/
  public void Var016() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.SMALLINT);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            -24475577533323.0), getSQLType(Types.INTEGER), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
          assertSqlException(e, -99999, "07006", "Data type mismatch",
              "Mismatch instead of truncation in V7R1");

      }
    }
  }

  /**
   * setObject() - Set a INTEGER parameter, when the data contains a fraction.
   * This is ok.
   **/
  public void Var017() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.INTEGER);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Float.valueOf(-33.3f),
            getSQLType(Types.INTEGER), 0);
        cs.registerOutParameter(2, Types.INTEGER, 0);
        cs.execute();
        int check = cs.getInt(2);
        cs.close();

        assertCondition(check == -33, "returned " + check + " sb -33");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an INTEGER parameter, when the object is the wrong type.
   **/
  public void Var018() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.INTEGER);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new Date(1234),
            getSQLType(Types.INTEGER), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set an INTEGER parameter, when the type is invalid.
   **/
  public void Var019() {
    if (checkJdbc42()) {
      try {

        CallableStatement cs = prepareCall(Types.INTEGER);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Integer.valueOf(9595),
            getSQLType(Types.DATE), 0);
        cs.registerOutParameter(2, Types.INTEGER, 0);
        cs.execute();
        int check = cs.getInt(2);
        cs.close();

        assertCondition(check == 9595, "returned " + check + " sb 9595");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an REAL parameter.
   **/
  public void Var020() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.REAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Float.valueOf(-4.385f),
            getSQLType(Types.REAL), 0);
        cs.registerOutParameter(2, Types.REAL);
        cs.execute();
        float check = cs.getFloat(2);
        cs.close();

        assertCondition(check == -4.385f);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a REAL parameter, when the object is the wrong type.
   **/
  public void Var021() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.REAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "Pierre",
            getSQLType(Types.REAL), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a REAL parameter, when the type is invalid.
   **/
  public void Var022() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.REAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Float.valueOf(4.325f),
            getSQLType(Types.VARCHAR), 0);
        cs.registerOutParameter(2, Types.REAL);
        cs.execute();
        float check = cs.getFloat(2);
        cs.close();

        assertCondition(check == 4.325f);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an FLOAT parameter.
   **/
  public void Var023() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.REAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Float.valueOf(-34.2f),
            getSQLType(Types.DOUBLE), 0);
        cs.registerOutParameter(2, Types.REAL);
        cs.execute();
        float check = cs.getFloat(2);
        cs.close();

        assertCondition(check == -34.2f);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a FLOAT parameter, when the object is the wrong type.
   **/
  public void Var024() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.REAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "Sioux Falls",
            getSQLType(Types.DOUBLE), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a FLOAT parameter, when the object is the wrong type.
   **/
  public void Var025() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.REAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Float.valueOf(-34.2f),
            getSQLType(Types.DATE), 0);
        cs.registerOutParameter(2, Types.REAL);
        cs.execute();
        float check = cs.getFloat(2);
        cs.close();

        assertCondition(check == -34.2f);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an DOUBLE parameter.
   **/
  public void Var026() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DOUBLE);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Double.valueOf(3.14159),
            getSQLType(Types.DOUBLE), 0);
        cs.registerOutParameter(2, Types.DOUBLE);
        cs.execute();
        double check = cs.getDouble(2);
        cs.close();

        assertCondition(check == 3.14159);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a DOUBLE parameter, when the object is the wrong type.
   **/
  public void Var027() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DOUBLE);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new Timestamp(
            34422343), getSQLType(Types.DOUBLE), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a DOUBLE parameter, when the type is invalid.
   **/
  public void Var028() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DOUBLE);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Double.valueOf(3.14159),
            getSQLType(Types.BINARY), 0);
        cs.registerOutParameter(2, Types.DOUBLE);
        cs.execute();
        double check = cs.getDouble(2);
        cs.close();

        assertCondition(check == 3.14159);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an DECIMAL parameter with scale equal to the number.
   **/
  public void Var029() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DECIMAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "322.344"), getSQLType(Types.DECIMAL), 2);
        cs.registerOutParameter(2, Types.DECIMAL);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(2);
        cs.close();
        double outputValue = check.doubleValue();
        assertCondition(outputValue == 322.34, "got outputValue=" + outputValue
            + " sb 322.34");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an DECIMAL parameter, when the data gets truncated.
   **/
  public void Var030() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DECIMAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            -1234567891.123), getSQLType(Types.DECIMAL), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        if (e instanceof DataTruncation) {
          DataTruncation dt = (DataTruncation) e;
          assertCondition((dt.getIndex() == 1) && (dt.getParameter() == true)
              && (dt.getRead() == false) && (dt.getTransferSize() == 10));
        } else {
	  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in latest toolbox ");


        }
      }
    }
  }

  /**
   * setObject() - Set an DECIMAL parameter, when the fraction gets truncated.
   * This does not cause a data truncation exception.
   **/
  public void Var031() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DECIMAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "322.344"), getSQLType(Types.DECIMAL), 2);
        cs.registerOutParameter(2, Types.DECIMAL);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(2);
        cs.close();

        assertCondition(check.doubleValue() == 322.34,
            "returned " + check.doubleValue() + " sb 322.34");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a DECIMAL parameter, when the object is the wrong type.
   **/
  public void Var032() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DECIMAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "Friends",
            getSQLType(Types.DECIMAL), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a DECIMAL parameter, when the type is invalid.
   **/
  public void Var033() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DECIMAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "322.34"), getSQLType(Types.BLOB), 2);
        cs.registerOutParameter(2, Types.DECIMAL);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(2);
        cs.close();

        assertCondition(check.doubleValue() == 322.34, "check=" + check
            + " sb 322.34");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an NUMERIC parameter with scale equal to the number.
   **/
  public void Var034() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.NUMERIC);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "-9999.12"), getSQLType(Types.NUMERIC), 2);
        cs.registerOutParameter(2, Types.NUMERIC);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(2);
        cs.close();

        assertCondition(check.doubleValue() == -9999.12);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a NUMERIC parameter, when the data gets truncated.
   **/
  public void Var035() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.NUMERIC);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            -2433442332.2344), getSQLType(Types.NUMERIC), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {

        if (e instanceof DataTruncation) {
          DataTruncation dt = (DataTruncation) e;
          assertCondition((dt.getIndex() == 1) && (dt.getParameter() == true)
              && (dt.getRead() == false) && (dt.getTransferSize() == 10),
              "Got dt.getIndex()=" + dt.getIndex() + " sb 2\n"
                  + "Got dt.getParameter()=" + dt.getParameter() + " sb true\n"
                  + "Got dt.getRead() =" + dt.getRead() + " sb false\n"
                  + "Got dt.getTransferSize() =" + dt.getTransferSize()
                  + " sb 10");
        } else {
	  assertSqlException(e, -99999, "07006", "Data type mismatch", "Mismatch instead of truncation in latest toolbox ");
         
        }

      }
    }
  }

  /**
   * setObject() - Set an NUMERIC parameter, when the fraction gets truncated.
   * This does not cause a data truncation exception.
   **/
  public void Var036() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.NUMERIC);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "322.3443846"), getSQLType(Types.NUMERIC), 2);
        cs.registerOutParameter(2, Types.NUMERIC);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(2);
        cs.close();

        assertCondition(check.doubleValue() == 322.34);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a NUMERIC parameter, when the object is the wrong type.
   **/
  public void Var037() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.NUMERIC);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new Time(32354),
            getSQLType(Types.NUMERIC), 1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a NUMERIC parameter, when the type is invalid.
   **/
  public void Var038() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.NUMERIC);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "-9999.12"), getSQLType(Types.CHAR), 2);
        cs.registerOutParameter(2, Types.NUMERIC);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(2);
        cs.close();

        assertCondition(check.doubleValue() == -9999.12, "got " + check
            + " sb -9999.12");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a CHAR(50) parameter.
   **/
  public void Var039() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.CHAR);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "Dell Rapids",
            getSQLType(Types.CHAR), 0);
        cs.registerOutParameter(2, Types.CHAR);
        cs.execute();
        String check = cs.getString(2);
        cs.close();

        assertCondition(check.equals("Dell Rapids                   "));

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a CHAR parameter, when the data gets truncated.
   **/
  public void Var040() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.CHAR);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1,
            "Sky45678911234567892123456789312345", getSQLType(Types.CHAR), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
      }
    }
  }

  /**
   * setObject() - Set a CHAR parameter, when the object is the wrong type.
   **/
  public void Var041() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.CHAR);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1,
            new byte[] { (byte) -12 }, getSQLType(Types.CHAR), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a CHAR parameter, when the object is the wrong type.
   **/
  public void Var042() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.CHAR);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "Nature",
            getSQLType(Types.DOUBLE), 0);
        cs.registerOutParameter(2, Types.CHAR);
        cs.execute();
        String check = cs.getString(2);
        cs.close();

        assertCondition(check.equals("Nature                        "));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an VARCHAR parameter.
   **/
  public void Var043() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.VARCHAR);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "Aberdeen",
            getSQLType(Types.VARCHAR), 0);
        cs.registerOutParameter(2, Types.VARCHAR, 0);
        cs.execute();
        String check = cs.getString(2);
        cs.close();

        assertCondition(check.equals("Aberdeen"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a VARCHAR parameter, when the data gets truncated.
   **/
  public void Var044() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.VARCHAR);
        JDReflectionUtil
            .callMethod_V(
                cs,
                "setObject",
                1,
                "JDBC testing is sure fun, especially when you get to test method after method after method, ...",
                getSQLType(Types.VARCHAR), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
      }
    }
  }

  /**
   * setObject() - Set a VARCHAR parameter, when the object is the wrong type.
   **/
  public void Var045() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.VARCHAR);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new int[0],
            getSQLType(Types.VARCHAR), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a VARCHAR parameter, when the type is invalid.
   **/
  public void Var046() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.VARCHAR);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "Aberdeen",
            getSQLType(Types.SMALLINT), 0);
        cs.registerOutParameter(2, Types.VARCHAR, 0);
        cs.execute();
        String check = cs.getString(2);
        cs.close();

        assertCondition(check.equals("Aberdeen"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a CLOB parameter.
   **/
  public void Var047() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        if (checkJdbc42()) {
          try {
            CallableStatement cs = prepareCall(Types.CLOB);
            JDReflectionUtil.callMethod_V(cs, "setObject", 1,
                new JDLobTest.JDTestClob("Milbank"), getSQLType(Types.CLOB), 0);
            cs.registerOutParameter(2, Types.CLOB);
            cs.execute();
            Clob check = cs.getClob(2);
            cs.close();

            assertCondition(check.getSubString(1, (int) check.length()).equals(
                "Milbank")); // @D1C
          } catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
  }

  /**
   * setObject() - Set a CLOB parameter, when the object is the wrong type.
   **/
  public void Var048() {
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.CLOB);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, Short.valueOf(
              (short) 3342), getSQLType(Types.CLOB), 0);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setObject() - Set a CLOB parameter, when the object is the wrong type.
   **/
  public void Var049() {
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.CLOB);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, Integer.valueOf(5),
              getSQLType(Types.NUMERIC), 1);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setObject() - Set a DBCLOB parameter.
   **/
  public void Var050() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        if (checkJdbc42()) {
          try {
            CallableStatement cs = prepareCall(TYPES_DBCLOB);
            JDReflectionUtil.callMethod_V(cs, "setObject", 1,
                new JDLobTest.JDTestClob("Brookings"), getSQLType(Types.CLOB),
                0);
            cs.registerOutParameter(2, Types.CLOB);
            cs.execute();
            Clob check = cs.getClob(2);
	    String checkString = check.getSubString(1, (int) check.length()); 
            cs.close();

            assertCondition(
                checkString.equals("Brookings"),
                "DBCLOB getSubstring"); // @D1C
          } catch (Exception e) {
            failed(e, "Unexpected Exception -- DBCLOB getSubstring");
          }
        }
      }
    }
  }

  /**
   * setObject() - Set a DBCLOB parameter, when the object is the wrong type.
   **/
  public void Var051() {
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(TYPES_DBCLOB);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, Long.valueOf(34242),
              getSQLType(Types.CLOB), 0);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setObject() - Set a DBCLOB parameter, when the type is invalid.
   **/
  public void Var052() {
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(TYPES_DBCLOB);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, Float.valueOf(4.33f),
              getSQLType(Types.VARCHAR), 0);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setObject() - Set a BINARY parameter.
   **/
  public void Var053() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.BINARY);
        byte[] b = { (byte) 32, (byte) 0, (byte) -1, (byte) -11 };
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, b,
            getSQLType(Types.BINARY), 0);
        cs.registerOutParameter(2, Types.BINARY);
        cs.execute();
        byte[] check = cs.getBytes(2);
        cs.close();

        byte[] b2 = new byte[30];
        System.arraycopy(b, 0, b2, 0, b.length);
        StringBuffer sb = new StringBuffer();
        boolean passed = areEqual(check, b2, sb);
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a BINARY parameter, when data gets truncated.
   **/
  public void Var054() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.BINARY);
        byte[] b = { (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5,
            (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11,
            (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17,
            (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23,
            (byte) 24, (byte) 25, (byte) 26, (byte) 27, (byte) 28, (byte) 29,
            (byte) 30, (byte) 32, (byte) 12, (byte) 123, (byte) 0, (byte) 32,
            (byte) 12, (byte) 123, (byte) 0, (byte) 32, (byte) 12, (byte) 123,
            (byte) 0, (byte) 32, (byte) 12, (byte) 123, (byte) 0 };
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, b,
            getSQLType(Types.BINARY), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
      }
    }
  }

  /**
   * setObject() - Set a BINARY parameter, when the object is the wrong type.
   **/
  public void Var055() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.BINARY);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1,
            new BigDecimal("34.23"), getSQLType(Types.BINARY), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a BINARY parameter, when the type is invalid.
   **/
  public void Var056() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.BINARY);
        byte[] b = { (byte) 32, (byte) 0, (byte) -1, (byte) -11 };
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, b,
            getSQLType(Types.BINARY), 0);
        cs.registerOutParameter(2, Types.BINARY);
        cs.execute();
        byte[] check = cs.getBytes(2);
        cs.close();

        byte[] b2 = new byte[30];
        System.arraycopy(b, 0, b2, 0, b.length);
        assertCondition(areEqual(check, b2));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a VARBINARY parameter.
   **/
  public void Var057() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.VARBINARY);
        byte[] b = { (byte) -13, (byte) 32, (byte) 0, (byte) -1, (byte) -11 };
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, b,
            getSQLType(Types.VARBINARY), 0);
        cs.registerOutParameter(2, Types.VARBINARY);
        cs.execute();
        byte[] check = cs.getBytes(2);
        cs.close();

        assertCondition(areEqual(check, b));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a VARBINARY parameter, when data gets truncated.
   **/
  public void Var058() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.VARBINARY);
        byte[] b = { (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5,
            (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11,
            (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17,
            (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23,
            (byte) 24, (byte) 25, (byte) 26, (byte) 27, (byte) 28, (byte) 29,
            (byte) 30, (byte) 32, (byte) 12, (byte) 123, (byte) 0, (byte) 32,
            (byte) 12, (byte) 123, (byte) 0, (byte) 32, (byte) 12, (byte) 123,
            (byte) 0, (byte) 32, (byte) 12, (byte) 123, (byte) 0 };
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, b,
            getSQLType(Types.VARBINARY), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.DataTruncation");
      }
    }
  }

  /**
   * setObject() - Set a VARBINARY parameter, when the object is the wrong type.
   **/
  public void Var059() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.VARBINARY);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Double.valueOf(34.23),
            getSQLType(Types.VARBINARY), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a VARBINARY parameter, when the type is invalid.
   **/
  public void Var060() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.VARBINARY);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new byte[43],
            getSQLType(Types.FLOAT), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a BLOB parameter.
   **/
  public void Var061() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        if (checkJdbc42()) {
          try {
            CallableStatement cs = prepareCall(Types.BLOB);
            byte[] b = new byte[] { (byte) 12, (byte) 94 };
            JDReflectionUtil.callMethod_V(cs, "setObject", 1,
                new JDLobTest.JDTestBlob(b), getSQLType(Types.BLOB), 0);
            cs.registerOutParameter(2, Types.BLOB);
            cs.execute();
            Blob check = cs.getBlob(2);
            cs.close();

            assertCondition(areEqual(check.getBytes(1, (int) check.length()), b)); // @D1C
          } catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
  }

  /**
   * setObject() - Set a BLOB parameter, when the object is the wrong type.
   **/
  public void Var062() {
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.BLOB);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, new Date(3424),
              getSQLType(Types.BLOB), 0);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setObject() - Set a BLOB parameter, when the type is invalid.
   **/
  public void Var063() {
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.BLOB);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1,
              new Time(14), getSQLType(Types.DATE), 0);
          failed("Didn't throw SQLException ");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setObject() - Set a DATE parameter.
   **/
  public void Var064() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DATE);
        Date d = new Date(342349439);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, d,
            getSQLType(Types.DATE), 0);
        cs.registerOutParameter(2, Types.DATE);
        cs.execute();
        Date check = cs.getDate(2);
        cs.close();

        assertCondition(d.toString().equals(check.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a DATE parameter, when the object is the wrong type.
   **/
  public void Var065() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DATE);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1,
            "This is only a test.", getSQLType(Types.DATE), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a DATE parameter, when the type is invalid.
   **/
  public void Var066() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DATE);
        Date d = new Date(342349439);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, d,
            getSQLType(Types.CHAR), 0);
        cs.registerOutParameter(2, Types.DATE);
        cs.execute();
        Date check = cs.getDate(2);
        cs.close();

        assertCondition(d.toString().equals(check.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a TIME parameter.
   **/
  public void Var067() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.TIME);
        Time t = new Time(33333);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, t,
            getSQLType(Types.TIME), 0);
        cs.registerOutParameter(2, Types.TIME);
        cs.execute();
        Time check = cs.getTime(2);
        cs.close();

        assertCondition(check.toString().equals(t.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a TIME parameter, when the object is the wrong type.
   **/
  public void Var068() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.TIME);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "Water",
            getSQLType(Types.TIME), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a TIME parameter, when the object is the wrong type.
   **/
  public void Var069() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.TIME);
        Time t = new Time(33333);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, t,
            getSQLType(Types.NULL), 0);
        cs.registerOutParameter(2, Types.TIME);
        cs.execute();
        Time check = cs.getTime(2);
        cs.close();

        assertCondition(check.toString().equals(t.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a TIMESTAMP parameter.
   **/
  public void Var070() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.TIMESTAMP);
        Timestamp ts = new Timestamp(343452230);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, ts,
            getSQLType(Types.TIMESTAMP), 0);
        cs.registerOutParameter(2, Types.TIMESTAMP);
        cs.execute();
        Timestamp check = cs.getTimestamp(2);
        cs.close();

        assertCondition(check.toString().equals(ts.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a TIMESTAMP parameter, when the object is the wrong type.
   **/
  public void Var071() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.TIMESTAMP);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal("34.2"),
            getSQLType(Types.TIMESTAMP), 0);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a TIMESTAMP parameter, when the type is invalid.
   **/
  public void Var072() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.TIMESTAMP);
        Timestamp ts = new Timestamp(343452230);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, ts,
            getSQLType(Types.INTEGER), 0);
        cs.registerOutParameter(2, Types.TIMESTAMP);
        cs.execute();
        Timestamp check = cs.getTimestamp(2);
        cs.close();

        assertCondition(check.toString().equals(ts.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a DATALINK parameter.
   **/
  public void Var073() {
      if (getRelease() <= JDTestDriver.RELEASE_V7R2M0 ) {
	  notApplicable("Data links not working before V7R3");
	  return; 
      } 
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.DATALINK);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1,
              "http://www.yahoo.com/news.html", getSQLType(Types.DATALINK), 0);
          cs.registerOutParameter(2, Types.DATALINK);
          cs.execute();
          Ref check = cs.getRef(2);
          cs.close();

          assertCondition(check.toString().equalsIgnoreCase(
              "http://www.yahoo.com/news.html"));
        } catch (Exception e) {
          failed(e, "Unexpected Exception -- Datalink type");
        }
      }
    }
  }

  /**
   * setObject() - Set a DATALINK parameter, when the object is the wrong type.
   **/
  public void Var074() {
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.DATALINK);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, Integer.valueOf(8),
              getSQLType(Types.INTEGER), 0);
          assertCondition(false);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");

        }
      }
    }
  }

  /**
   * setObject() - Set a DATALINK parameter, when the type is invalid.
   **/
  public void Var075() {
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.DATALINK);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1,
              "http://www.ibm.com/", getSQLType(Types.INTEGER), 0);
          assertCondition(true);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setObject() - Set a DISTINCT parameter.
   **/
  public void Var076() {
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.DISTINCT);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, Integer.valueOf(-412),
              getSQLType(Types.DISTINCT), 0);
          cs.registerOutParameter(2, Types.DISTINCT);
          cs.execute();
          Object check = cs.getObject(2);
          cs.close();

          assertCondition(check.toString().equals("-412"), "returned " + check
              + " sb -412");
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setObject() - Set a DISTINCT parameter, when the object is the wrong type.
   **/
  public void Var077() {
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.DISTINCT);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1,
              "We are done, soon.", getSQLType(Types.VARCHAR), 0);
          failed("Didn't throw SQLException");

        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");

        }
      }
    }
  }

  /**
   * setObject() - Set a DISTINCT parameter, when the type is invalid.
   **/
  public void Var078() {
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.DISTINCT);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
              "3.321"), getSQLType(Types.TIMESTAMP), 0);
          cs.registerOutParameter(2, Types.DISTINCT);
          cs.execute();
          Object check = cs.getObject(2);
          cs.close();

          assertCondition(check.toString().equals("3"), "returned " + check
              + " sb 3");

        } catch (Exception e) {
          failed(e, "Unexpected exception");
        }
      }
    }
  }

  /**
   * setObject() - Set a BIGINT parameter.
   **/
  public void Var079() {
    if (checkBigintSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.BIGINT);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, Long.valueOf(
              959543224556l), getSQLType(Types.BIGINT), 0);
          cs.registerOutParameter(2, Types.BIGINT);
          cs.execute();
          long check = cs.getLong(2);
          cs.close();

          assertCondition(check == 959543224556l, "returned " + check
              + " sb 959543224556");
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setFloat() - Set an BIGINT parameter, when the value is too big. This will
   * cause a data truncation exception.
   **/
  public void Var080() {
    if (checkBigintSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.BIGINT);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, Float.valueOf(
              -92233720368547758099.0f), getSQLType(Types.BIGINT), 0);
          failed("Didn't throw SQLException");
        } catch (Exception e) {

            assertSqlException(e, -99999, "07006", "Data type mismatch",
                "Mismatch instead of truncation in V7R1");

        }
      }
    }
  }

  /**
   * setObject() - Set a BIGINT parameter, when the data contains a fraction.
   * This is ok.
   **/
  public void Var081() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.BIGINT);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Float.valueOf(-33.3f),
            getSQLType(Types.BIGINT), 0);
        cs.registerOutParameter(2, Types.BIGINT);
        cs.execute();
        long check = cs.getLong(2);
        cs.close();

        assertCondition(check == -33, "returned " + check + " sb -33");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a BIGINT parameter, when the object is the wrong type.
   **/
  public void Var082() {
    if (checkBigintSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.BIGINT);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, "This is a test",
              getSQLType(Types.BIGINT), 0);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setObject() - Set a BIGINT parameter, when the type is invalid.
   **/
  public void Var083() {
    if (checkBigintSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.BIGINT);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, Long.valueOf(
              959543224556l), getSQLType(Types.VARCHAR), 0);
          cs.registerOutParameter(2, Types.BIGINT);
          cs.execute();
          long check = cs.getLong(2);
          cs.close();

          assertCondition(check == 959543224556l, "returned " + check
              + " sb 959543224556l");
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setObject() - Set an VARCHAR parameter using Types.LONGVARCHAR. This is by
   * customer request.
   * 
   * SQL400 - Not run through the Toolbox as I don't think they make this work.
   **/
  public void Var084() {

    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.VARCHAR);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "Aberdeen",
            getSQLType(Types.LONGVARCHAR), 0);
        cs.registerOutParameter(2, Types.LONGVARCHAR);
        cs.execute();
        String check = cs.getString(2);
        cs.close();

        assertCondition(check.equals("Aberdeen"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }

    }
  }

  /**
   * setObject() - Set a VARBINARY parameter using Types.LONGVARBINARY. This is
   * by customer request.
   * 
   * SQL400 - Not run through the Toolbox as I don't think they make this work.
   **/
  public void Var085() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.VARBINARY);
        byte[] b = { (byte) -13, (byte) 32, (byte) 0, (byte) -1, (byte) -11 };
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, b,
            getSQLType(Types.LONGVARBINARY), 0);
        cs.registerOutParameter(2, Types.LONGVARBINARY);
        cs.execute();
        byte[] check = cs.getBytes(2);
        cs.close();

        assertCondition(areEqual(check, b));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var086() {
    notApplicable("No testcase");
  }

  /**
   * setObject() - Set an DECFLOAT(16) parameter.
   **/
  public void Var087() {

    String added = " -- DECFLOAT(16) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {

      if (checkJdbc42()) {
        try {

          CallableStatement cs = prepareCall(TYPES_DECFLOAT16);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
              "322.344"), getSQLType(Types.OTHER), 0);
          cs.registerOutParameter(2, Types.OTHER);
          cs.execute();
          BigDecimal check = cs.getBigDecimal(2);
          cs.close();

          assertCondition(check.doubleValue() == 322.344,
              "Got " + check.doubleValue() + " expected 322.34 " + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception " + added);
        }
      }
    }
  }

  /**
   * setObject() - Set an DECFLOAT(16) parameter, when the data gets truncated.
   **/
  public void Var088() {
    String added = " -- DECFLOAT(16) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {

      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(TYPES_DECFLOAT16);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
              "9.999999999999999E800"), getSQLType(Types.OTHER), 0);
          cs.registerOutParameter(2, Types.OTHER);
          cs.execute();
          BigDecimal check = cs.getBigDecimal(2);
          cs.close();

          failed("Didn't throw SQLException but got " + check + added);
        } catch (DataTruncation dt) {
          assertCondition((dt.getIndex() == 1) && (dt.getParameter() == true)
              && (dt.getRead() == false) && (dt.getTransferSize() == 5), added);
        } catch (SQLException e) {
          // The native driver will throw a Data Type mismatch since this cannot
          // be expressed
          boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
          if (success) {
            assertCondition(true);
          } else {
            failed(e, "Expected data type mismatch (for native driver)" + added);
          }

        } catch (Exception e) {
          failed(e, "Unexpected Exception " + added);

        }
      }

    }
  }

  /**
   * setObject() - Set an DECFLOAT(16) parameter, when the fraction gets
   * truncated. This does not cause a data truncation exception.
   **/
  public void Var089() {
    String added = " -- DECFLOAT(16) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(TYPES_DECFLOAT16);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
              "322.34412345678901234567890"), getSQLType(Types.OTHER), 0);
          cs.registerOutParameter(2, Types.OTHER);
          cs.execute();
          BigDecimal check = cs.getBigDecimal(2);
          cs.close();

          assertCondition(check.toString().equals("322.3441234567890")
              || check.toString().equals("322.344123456789"), "Got " + check
              + " sb 322.3441234567890 or 322.344123456789 " + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception" + added);
        }

      }
    }
  }

  /**
   * setObject() - Set a DECFLOAT(16) parameter, when the object is the wrong
   * type.
   **/
  public void Var090() {
    String added = " -- DECFLOAT(16) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(TYPES_DECFLOAT16);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, "Friends",
              getSQLType(Types.OTHER), 0);
          failed("Didn't throw SQLException" + added);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setObject() - Set an DECFLOAT(34) parameter.
   **/
  public void Var091() {

    String added = " -- DECFLOAT(34) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {

      if (checkJdbc42()) {
        try {

          CallableStatement cs = prepareCall(TYPES_DECFLOAT34);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
              "322.344"), getSQLType(Types.OTHER), 0);

          cs.registerOutParameter(2, Types.OTHER);
          cs.execute();
          BigDecimal check = cs.getBigDecimal(2);
          cs.close();

          assertCondition(check.doubleValue() == 322.344,
              "Got " + check.doubleValue() + " expected 322.34 " + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception " + added);
        }
      }
    }
  }

  /**
   * setObject() - Set an DECFLOAT(34) parameter, when the data gets truncated.
   **/
  public void Var092() {
    String added = " -- DECFLOAT(34) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {

      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(TYPES_DECFLOAT34);

          JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
              "9.999999999999999E56642"), getSQLType(Types.OTHER), 0);
          cs.registerOutParameter(2, Types.OTHER);
          cs.execute();
          BigDecimal check = cs.getBigDecimal(2);
          cs.close();

          failed("Didn't throw SQLException got " + check + added);
        } catch (DataTruncation dt) {
          assertCondition((dt.getIndex() == 1) && (dt.getParameter() == true)
              && (dt.getRead() == false) && (dt.getTransferSize() == 5), added);

        } catch (SQLException e) {
          // The native driver will throw a Data Type mismatch since this cannot
          // be expressed
          boolean success = e.getMessage().indexOf("Data type mismatch") != -1;
          if (success) {
            assertCondition(true);
          } else {
            failed(e, "Expected data type mismatch (for native driver)" + added);
          }

        } catch (Exception e) {
          failed(e, "Unexpected Exception " + added);

        }
      }

    }
  }

  /**
   * setObject() - Set an DECFLOAT(34) parameter, when the fraction gets
   * rounded. This does not cause a data truncation exception.
   **/
  public void Var093() {
    String added = " -- DECFLOAT(34) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(TYPES_DECFLOAT34);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
              "322.344123456789012345678901234567890123456789088323"),
              getSQLType(Types.OTHER), 0);
          cs.registerOutParameter(2, Types.OTHER);
          cs.execute();
          BigDecimal check = cs.getBigDecimal(2);
          cs.close();

          String expected = "322.3441234567890123456789012345679";
          assertCondition(check.toString().equals(expected), "Got " + check
              + " sb " + expected + added);
        } catch (Exception e) {
          failed(e, "Unexpected Exception" + added);
        }

      }
    }
  }

  /**
   * setObject() - Set a DECFLOAT(34) parameter, when the object is the wrong
   * type.
   **/
  public void Var094() {
    String added = " -- DECFLOAT(34) test added by native driver 05/09/2007";
    if (checkDecFloatSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(TYPES_DECFLOAT34);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, "Friends",
              getSQLType(Types.OTHER), 0);
          failed("Didn't throw SQLException" + added);
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }

  }

  /**
   * setObject() - Set an DECIMAL parameter with scale 0.
   **/
  public void Var095() {
    String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";

    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DECIMAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "32.234"), getSQLType(Types.DECIMAL), 0);
        cs.registerOutParameter(2, Types.DECIMAL);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(2);
        cs.close();

        assertCondition(check.doubleValue() == 32, added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + added);
      }
    }
  }

  /**
   * setObject() - Set an DECIMAL parameter with scale greater than the number.
   **/
  public void Var096() {
    String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";

    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DECIMAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "-12.87"), getSQLType(Types.DECIMAL), 4);
        cs.registerOutParameter(2, Types.DECIMAL);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(2);
        cs.close();

        assertCondition(check.doubleValue() == -12.87, added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + added);
      }
    }
  }

  /**
   * setObject() - Set an NUMERIC parameter with scale 0.
   **/
  public void Var097() {
    String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";

    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.NUMERIC);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "-432.34"), getSQLType(Types.NUMERIC), 0);
        cs.registerOutParameter(2, Types.NUMERIC);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(2);
        cs.close();

        assertCondition(check.doubleValue() == -432, added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + added);
      }
    }
  }

  /**
   * setObject() - Set an NUMERIC parameter with scale less than the number.
   **/
  public void Var098() {
    String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";

    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.NUMERIC);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "52.219"), getSQLType(Types.NUMERIC), 1);
        cs.registerOutParameter(2, Types.NUMERIC);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(2);
        cs.close();

        assertCondition(check.doubleValue() == 52.2, added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + added);
      }
    }
  }

  /**
   * setObject() - Set an DECIMAL parameter using exponential notation
   **/
  @SuppressWarnings("deprecation")
  public void Var099() {
    String added = " -- added 12/17/2009 to test native driver for CPS 7YSU2X";

    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DECIMAL);

        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "8.223187E+02",
            getSQLType(Types.NUMERIC), 2);
        cs.registerOutParameter(2, Types.NUMERIC);
        cs.execute();
        BigDecimal check = cs.getBigDecimal(2, 4);
        cs.close();

        assertCondition(check.doubleValue() == 822.31, added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception" + added);
      }
    }

  }

  /**
   * setObject() - Should throw exception when the scale is invalid.
   **/
  public void Var100() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.INTEGER);

        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Integer.valueOf(4),
            getSQLType(Types.INTEGER), -1);
        failed("Didn't throw SQLException");
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   * setObject() - Set a SMALLINT parameter with a nonzero scale specified.
   **/
  public void Var101() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.SMALLINT);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1,
            Short.valueOf((short) 76), getSQLType(Types.SMALLINT), 2);
        cs.registerOutParameter(2, Types.SMALLINT);
        cs.execute();
        short check = cs.getShort(2);
        cs.close();
        assertCondition(check == 76);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an INTEGER parameter with a nonzero scale.
   **/
  public void Var102() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.SMALLINT);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Integer.valueOf(-595),
            getSQLType(Types.INTEGER), 4);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.execute();
        int check = cs.getInt(2);
        cs.close();
        assertCondition(check == -595);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an REAL parameter with nonzero scale.
   **/
  public void Var103() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.REAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Float.valueOf(-4.3235f),
            getSQLType(Types.REAL), 3);
        cs.registerOutParameter(2, Types.REAL);
        cs.execute();
        float check = cs.getFloat(2);
        cs.close();

        assertCondition(check == -4.3235f);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * setObject() - Set an FLOAT parameter with a nonzero scale.
   **/
  public void Var104() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.FLOAT);

        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Float.valueOf(3.43212f),
            getSQLType(Types.DOUBLE), 3);
        cs.registerOutParameter(2, Types.DOUBLE);
        cs.execute();

        float check = cs.getFloat(2);
        cs.close();

        assertCondition(check == 3.43212f);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an DOUBLE parameter with a nonzero scale.
   **/
  public void Var105() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DOUBLE);

        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Double.valueOf(3159.343),
            getSQLType(Types.DOUBLE), 2);
        cs.registerOutParameter(2, Types.DOUBLE);
        cs.execute();

        double check = cs.getDouble(2);
        cs.close();

        assertCondition(check == 3159.343);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a CHAR(50) parameter with a nonzero scale.
   **/
  public void Var106() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.CHAR);

        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "Harrisburg",
            getSQLType(Types.CHAR), 4);
        cs.registerOutParameter(2, Types.CHAR);
        cs.execute();

        String check = cs.getString(2);
        cs.close();

        assertCondition(check.equals("Harrisburg                    "), "got '"
            + check + "'");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an VARCHAR parameter with a nonzero scale.
   **/
  public void Var107() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.VARCHAR);

        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "Desmet",
            getSQLType(Types.VARCHAR), 4);
        cs.registerOutParameter(2, Types.VARCHAR);
        cs.execute();

        String check = cs.getString(2);
        cs.close();

        assertCondition(check.equals("Desmet"));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a CLOB parameter with a nonzero scale.
   **/
  public void Var108() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        if (checkJdbc42()) {
          try {
            CallableStatement cs = prepareCall(Types.CLOB);

            JDReflectionUtil.callMethod_V(cs, "setObject", 1,
                new JDLobTest.JDTestClob("Garretson"), getSQLType(Types.CLOB),
                4);
            cs.registerOutParameter(2, Types.CLOB);
            cs.execute();

            Clob check = cs.getClob(2);
            cs.close();

            String string = check.getSubString(1, (int) check.length());
            if (string.equals("Garretson"))
              succeeded();
            else
              failed("Strings did not match. Garretson != " + string);
          } catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
  }

  /**
   * setObject() - Set an DECIMAL parameter with scale 0.
   **/
  @SuppressWarnings("deprecation")
  public void Var109() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DECIMAL);

        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "32.234"), getSQLType(Types.DECIMAL), 0);
        cs.registerOutParameter(2, Types.DECIMAL);
        cs.execute();

        BigDecimal check = cs.getBigDecimal(2, 3);
        cs.close();

        assertCondition(check.doubleValue() == 32);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an DECIMAL parameter with scale less than the number.
   **/
  @SuppressWarnings("deprecation")
  public void Var110() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DECIMAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "-32.2319"), getSQLType(Types.DECIMAL), 2);
        cs.registerOutParameter(2, Types.DECIMAL);
        cs.execute();

        BigDecimal check = cs.getBigDecimal(2, 2);
        cs.close();

        assertCondition(check.doubleValue() == -32.23);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an DECIMAL parameter with scale greater than the number.
   **/
  @SuppressWarnings("deprecation")
  public void Var111() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DECIMAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "-12.87"), getSQLType(Types.DECIMAL), 4);
        cs.registerOutParameter(2, Types.DECIMAL);
        cs.execute();

        BigDecimal check = cs.getBigDecimal(2, 2);
        cs.close();

        assertCondition(check.doubleValue() == -12.87);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an NUMERIC parameter with scale 0.
   **/
  @SuppressWarnings("deprecation")
  public void Var112() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.NUMERIC);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "-432.34"), getSQLType(Types.NUMERIC), 0);
        cs.registerOutParameter(2, Types.NUMERIC);
        cs.execute();

        BigDecimal check = cs.getBigDecimal(2, 0);
        cs.close();

        assertCondition(check.doubleValue() == -432);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an NUMERIC parameter with scale less than the number.
   **/
  @SuppressWarnings("deprecation")
  public void Var113() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.NUMERIC);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "52.219"), getSQLType(Types.NUMERIC), 1);
        cs.registerOutParameter(2, Types.NUMERIC);
        cs.execute();

        BigDecimal check = cs.getBigDecimal(2, 1);
        cs.close();

        assertCondition(check.doubleValue() == 52.2);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * setObject() - Set an NUMERIC parameter with scale greater than the number.
   **/
  @SuppressWarnings("deprecation")
  public void Var114() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.NUMERIC);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, new BigDecimal(
            "32.87"), getSQLType(Types.NUMERIC), 4);
        cs.registerOutParameter(2, Types.NUMERIC);
        cs.execute();

        BigDecimal check = cs.getBigDecimal(2, 2);
        cs.close();

        assertCondition(check.doubleValue() == 32.87);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a DBCLOB parameter with a nonzero scale.
   **/
  public void Var115() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {

        if (checkJdbc42()) {
          try {
            CallableStatement cs = prepareCall(TYPES_DBCLOB);
            JDReflectionUtil.callMethod_V(cs, "setObject", 1,
                new JDLobTest.JDTestClob("Lennox"), getSQLType(Types.CLOB), 20);
            cs.registerOutParameter(2, Types.CLOB);
            cs.execute();

            Clob check = cs.getClob(2);
            String string = check.getSubString(1, (int) check.length());

            cs.close();

            if (string.equals("Lennox"))
              succeeded();
            else
              failed("Strings did not match. Lennox != " + string);

          } catch (Exception e) {
            failed(e, "Unexpected Exception -- DBCLOB testcase");
          }
        }
      }
    }

  }

  /**
   * setObject() - Set a BINARY parameter with a nonzero scale.
   **/
  public void Var116() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.BINARY);

        byte[] b = { (byte) 9, (byte) -1, (byte) -11, (byte) 12 };
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, b,
            getSQLType(Types.BINARY), 7);
        cs.registerOutParameter(2, Types.BINARY);
        cs.execute();

        byte[] check = cs.getBytes(2);
        cs.close();

        byte[] b2 = new byte[30];
        System.arraycopy(b, 0, b2, 0, b.length);
        StringBuffer sb = new StringBuffer();
        assertCondition(areEqual(check, b2, sb), sb);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * setObject() - Set a VARBINARY parameter with a nonzero scale.
   **/
  public void Var117() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.VARBINARY);

        byte[] b = { (byte) -13, (byte) 32, (byte) 100, (byte) -1, (byte) -11,
            (byte) 99 };
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, b,
            getSQLType(Types.VARBINARY), 3);
        cs.registerOutParameter(2, Types.VARBINARY);
        cs.execute();

        byte[] check = cs.getBytes(2);
        cs.close();

        assertCondition(areEqual(check, b));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a BLOB parameter with a nonzero scale.
   **/
  public void Var118() {
    if (checkJdbc20()) {
      if (checkLobSupport()) {
        if (checkJdbc42()) {
          try {
            CallableStatement cs = prepareCall(Types.BLOB);

            byte[] b = new byte[] { (byte) 94, (byte) -1, (byte) 0 };
            JDReflectionUtil.callMethod_V(cs, "setObject", 1,
                new JDLobTest.JDTestBlob(b), getSQLType(Types.BLOB), 4);
            cs.registerOutParameter(2, Types.BLOB);
            cs.execute();

            Blob check = cs.getBlob(2);
            cs.close();
            // rs.close (); // @F1D

            assertCondition(areEqual(check.getBytes(1, (int) check.length()), b)); // @D1C
          } catch (Exception e) {
            failed(e, "Unexpected Exception");
          }
        }
      }
    }
  }

  /**
   * setObject() - Set a DATE parameter with nonzero scale.
   **/
  public void Var119() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DATE);

        Date d = new Date(142333499);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, d,
            getSQLType(Types.DATE), 4);
        cs.registerOutParameter(2, Types.DATE);
        cs.execute();

        Date check = cs.getDate(2);
        cs.close();

        assertCondition(d.toString().equals(check.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * setObject() - Set a TIME parameter with a nonzero scale.
   **/
  public void Var120() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.TIME);

        Time t = new Time(344311133);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, t,
            getSQLType(Types.TIME), 8);
        cs.registerOutParameter(2, Types.TIME);
        cs.execute();

        Time check = cs.getTime(2);
        cs.close();

        assertCondition(check.toString().equals(t.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a TIMESTAMP parameter with a nonzero scale.
   **/
  public void Var121() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.TIMESTAMP);

        Timestamp ts = new Timestamp(934230);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, ts,
            getSQLType(Types.TIMESTAMP), 3);
        cs.registerOutParameter(2, Types.TIMESTAMP);
        cs.execute();

        Timestamp check = cs.getTimestamp(2);
        cs.close();

        assertCondition(check.toString().equals(ts.toString()));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

  }

  /**
   * setObject() - Set a DATALINK parameter with a nonzero scale.
   **/
  public void Var122() {
      if (getRelease() <= JDTestDriver.RELEASE_V7R2M0 ) {
	  notApplicable("Data links not working before V7R3");
	  return; 
      } 

    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.DATALINK);

          JDReflectionUtil.callMethod_V(cs, "setObject", 1,
              "http://java.sun.com/jdbc.html", getSQLType(Types.VARCHAR), 7);
          cs.registerOutParameter(2, Types.VARCHAR);
          cs.execute();

          String check = cs.getString(2);
          cs.close();

          assertCondition(check
              .equalsIgnoreCase("http://java.sun.com/jdbc.html"));
        } catch (Exception e) {
          failed(e, "Unexpected Exception -- datalink testcase");
        }
      }
    }

  }

  /**
   * setObject() - Set a DISTINCT parameter with a nonzero scale.
   **/
  public void Var123() {
    if (checkLobSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.DISTINCT);

          JDReflectionUtil.callMethod_V(cs, "setObject", 1, Integer.valueOf(12),
              getSQLType(Types.INTEGER), 2);
          cs.registerOutParameter(2, Types.INTEGER);
          cs.execute();

          int check = cs.getInt(2);
          cs.close();

          assertCondition(check == 12);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }

  }

  /**
   * setObject() - Set a BIGINT parameter with a nonzero scale.
   **/
  public void Var124() {
    if (checkBigintSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.BIGINT);

          JDReflectionUtil.callMethod_V(cs, "setObject", 1, Long.valueOf(
              -59322445665l), getSQLType(Types.BIGINT), 4);
          cs.registerOutParameter(2, Types.BIGINT);
          cs.execute();

          long check = cs.getLong(2);
          cs.close();

          assertCondition(check == -59322445665l);
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setObject() - Set a VARBINARY parameter with a nonzero scale using
   * Types.LONGVARBINARY. This is by user request.
   * 
   * SQL400 - Not run through the Toolbox as I don't think they make this work.
   **/
  public void Var125() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.VARBINARY);

          byte[] b = { (byte) -13, (byte) 32, (byte) 100, (byte) -1,
              (byte) -11, (byte) 99 };
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, b,
              getSQLType(Types.LONGVARBINARY), 3);
          cs.registerOutParameter(2, Types.LONGVARBINARY);
          cs.execute();

          byte[] check = cs.getBytes(2);
          cs.close();

          assertCondition(areEqual(check, b));
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    } else {
      notApplicable();
    }
  }

  /**
   * setObject() - Set an DECIMAL parameter using exponential notation
   **/
  @SuppressWarnings("deprecation")
  public void Var126() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DECIMAL);

        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "8.223187E+02",
            getSQLType(Types.DECIMAL), 2);

        cs.registerOutParameter(2, Types.DECIMAL);
        cs.execute();

        BigDecimal check = cs.getBigDecimal(2, 4);
        cs.close();

        assertCondition(check.doubleValue() == 822.31);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an DECIMAL parameter using exponential notation with 0 in
   * the exponent
   **/
  @SuppressWarnings("deprecation")
  public void Var127() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DECIMAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "8.22E+00",
            getSQLType(Types.DECIMAL), 2);
        cs.registerOutParameter(2, Types.DECIMAL);
        cs.execute();

        BigDecimal check = cs.getBigDecimal(2, 4);
        cs.close();

        assertCondition(check.doubleValue() == 8.22);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an DECIMAL parameter using exponential notation with 0 as
   * the value
   **/
  @SuppressWarnings("deprecation")
  public void Var128() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DECIMAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "0E+01",
            getSQLType(Types.DECIMAL), 2);
        cs.registerOutParameter(2, Types.DECIMAL);
        cs.execute();

        BigDecimal check = cs.getBigDecimal(2, 4);
        cs.close();

        assertCondition(check.doubleValue() == 0.0);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set an DECIMAL parameter using exponential notation with 0 as
   * the value and 0 as the exponent
   **/
  @SuppressWarnings("deprecation")
  public void Var129() {
    if (checkJdbc42()) {
      try {
        CallableStatement cs = prepareCall(Types.DECIMAL);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, "0E+00",
            getSQLType(Types.DECIMAL), 2);
        cs.registerOutParameter(2, Types.DECIMAL);
        cs.execute();

        BigDecimal check = cs.getBigDecimal(2, 4);
        cs.close();

        assertCondition(check.doubleValue() == 0.0);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

 
  
  /**
   * setObject() - Set a BOOLEAN parameter.
   **/
  public void Var130() {
    if (checkBooleanSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.BOOLEAN);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, Boolean.valueOf(true), getSQLType(Types.BOOLEAN), 0);
          cs.registerOutParameter(2, Types.BOOLEAN);
          cs.execute();
          boolean check = cs.getBoolean(2);
          cs.close();

          assertCondition(check == true, "returned " + check
              + " sb true");
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setFloat() - Set an BOOLEAN parameter, when the value is too big. This will
   * cause a data truncation exception.
   **/
  public void Var131() {
    if (checkBooleanSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.BOOLEAN);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, Float.valueOf(
              -92233720368547758099.0f), getSQLType(Types.BOOLEAN), 0);
          cs.registerOutParameter(2, Types.BOOLEAN);
          cs.execute();
          boolean check = cs.getBoolean(2);
          cs.close();

          assertCondition(check == true, "returned " + check
              + " sb true");
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }

  /**
   * setObject() - Set a BOOLEAN parameter, when the data contains a fraction.
   * This is ok.
   **/
  public void Var132() {
    if (checkJdbc42() && checkBooleanSupport()) {
      try {
        CallableStatement cs = prepareCall(Types.BOOLEAN);
        JDReflectionUtil.callMethod_V(cs, "setObject", 1, Float.valueOf(-33.3f),
            getSQLType(Types.BOOLEAN), 0);
        cs.registerOutParameter(2, Types.BOOLEAN);
        cs.execute();
        boolean check = cs.getBoolean(2);
        cs.close();

        assertCondition(check == true, "returned " + check + " sb true");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setObject() - Set a BOOLEAN parameter, when the object is the wrong type.
   **/
  public void Var133() {
    if (checkBooleanSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.BOOLEAN);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, "This is a test",
              getSQLType(Types.BOOLEAN), 0);
          failed("Didn't throw SQLException");
        } catch (Exception e) {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
      }
    }
  }

  /**
   * setObject() - Set a BOOLEAN parameter, when the type is invalid.
   **/
  public void Var134() {
    if (checkBooleanSupport()) {
      if (checkJdbc42()) {
        try {
          CallableStatement cs = prepareCall(Types.BOOLEAN);
          JDReflectionUtil.callMethod_V(cs, "setObject", 1, Long.valueOf(
              959543224556l), getSQLType(Types.VARCHAR), 0);
          cs.registerOutParameter(2, Types.BOOLEAN);
          cs.execute();
          boolean check = cs.getBoolean(2);
          cs.close();

          assertCondition(check == true, "returned " + check
              + " sb true");
        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }




  public void Var135() { notApplicable(); }
 public void Var136() { notApplicable(); }
 public void Var137() { notApplicable(); }
 public void Var138() { notApplicable(); }
 public void Var139() { notApplicable(); }
 public void Var140() { notApplicable(); }


           
            
        
	 /* Test a parameter */
	    public void testParameter(String parameterName, Object o, int type, String expectedResult)
	    {
		try {
		  
		        assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
      CallableStatement cs = connectionDateTime_.prepareCall(callSql);
      JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSINOUT,
          supportedFeatures_);
      JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSINOUT,
          supportedFeatures_, getDriver());

                  JDReflectionUtil.callMethod_V(cs, "setObject", parameterName, o, getSQLType(type), 0);
		    cs.execute();
		    String s = cs.getString(parameterName); 
		    cs.close(); 
		    assertCondition (expectedResult.equals(s), "got '"+s+"' expected '"+expectedResult+"'");
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }


	 

	    public void testLocalTimeParameter(String parameterName, int type, int hour, int minute, int second, String expectedResult) {
		if (checkJdbc42()) {
		    try {
			Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
								       "of", hour, minute, second);
			testParameter(parameterName,  o, type, expectedResult); 
		    } catch (Exception e) {
			failed (e, "Unexpected Exception");
		    }
		}
	    }

	    public void testLocalTimeFailure(String parameterName, int type, String info) {
		if (checkJdbc42()) {
		    try {
			Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalTime",
								       "of", 1,2,3);
			int scale = 2; 
			setTestFailure("setObject4S", parameterName,   o, type, scale, info);
		    } catch (Exception e) {
			failed (e, "Unexpected Exception");
		    }
		}
	    }

	    
  public void testLocalDateParameter(String parameterName, int type, int year, int month,
      int day, String expectedResult) {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", year, month, day);
        testParameter(parameterName, o,  type, expectedResult);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }
  
  public void testLocalDateFailure(String parameterName, int type, String info) {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDate",
            "of", 2001, 2, 3);
        setTestFailure("setObject4S",  parameterName, o, type, 0, info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }


	    
  public void testLocalDateTimeParameter(String parameterName, int type, int year, int month,
      int day, int hour, int minute, int second, int nano, String expectedResult) {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", year, month, day, hour, minute, second, nano);
        testParameter(parameterName,  o, type, expectedResult);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }
  
  public void testLocalDateTimeFailure(String parameterName, int type, String info) {
    if (checkJdbc42()) {
      try {
        Object o = JDReflectionUtil.callStaticMethod_O("java.time.LocalDateTime",
            "of", 2001, 2, 3, 4, 5, 6, 123456789);
        setTestFailure("setObject4S", parameterName, o, type, 0,  info);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }



	    /* Test the types using LocalTime */ 

	    public void Var141() { 		testLocalTimeFailure("P_SMALLINT", Types.SMALLINT, "LocalTime"); 	    }

 
      public void Var142() {    testLocalTimeFailure("P_INTEGER", Types.INTEGER,  "LocalTime");       }
      public void Var143() {    testLocalTimeFailure("P_REAL", Types.REAL, "LocalTime");      }
      public void Var144() {    testLocalTimeFailure("P_FLOAT", Types.FLOAT, "LocalTime");       }
      public void Var145() {    testLocalTimeFailure("P_DOUBLE",  Types.DOUBLE, "LocalTime");      }
      public void Var146() {    testLocalTimeFailure("P_DECIMAL_50", Types.DECIMAL,"LocalTime");      }
      public void Var147() {    testLocalTimeFailure("P_DECIMAL_105", Types.DECIMAL, "LocalTime");       }
      public void Var148() {    testLocalTimeFailure("P_NUMERIC_50", Types.NUMERIC,  "LocalTime");      }
      public void Var149() {    testLocalTimeFailure("P_NUMERIC_105", Types.NUMERIC, "LocalTime");       }
      public void Var150() {    testLocalTimeFailure("P_CHAR_1", Types.CHAR, "LocalTime");       }
      
      public void Var151() {  testLocalTimeParameter("P_CHAR_50", Types.CHAR, 0,0,0,"00:00:00                                          " ); }
      public void Var152() {  testLocalTimeParameter("P_CHAR_50", Types.CHAR, 2,3,4,"02:03:04                                          " ); }
      public void Var153() {  testLocalTimeParameter("P_CHAR_50", Types.CHAR, 23,59,59,"23:59:59                                          " ); }
      public void Var154() {  testLocalTimeParameter("P_VARCHAR_50",Types.VARCHAR, 0,0,0,"00:00:00" ); }
      public void Var155() {  testLocalTimeParameter("P_VARCHAR_50",Types.VARCHAR, 2,3,4,"02:03:04" ); }
      public void Var156() {  testLocalTimeParameter("P_VARCHAR_50",Types.VARCHAR,23,59,59,"23:59:59" ); }
      public void Var157() {    testLocalTimeFailure("P_BINARY_20",Types.BINARY, "LocalTime");       }
      public void Var158() {    testLocalTimeFailure("P_VARBINARY_20",Types.VARBINARY, "LocalTime");      }
      public void Var159() {    testLocalTimeFailure("P_DATE",Types.DATE, "LocalTime");      }
      public void Var160() {  testLocalTimeParameter("P_TIME",Types.TIME,0,0,0,"00:00:00" ); }
      public void Var161() {  testLocalTimeParameter("P_TIME",Types.TIME,2,3,4,"02:03:04" ); }
      public void Var162() {  testLocalTimeParameter("P_TIME",Types.TIME,23,59,59,"23:59:59" ); }
      public void Var163() {    testLocalTimeFailure("P_TIMESTAMP",Types.TIMESTAMP, "LocalTime");       }
      public void Var164() {   notApplicable("Skipping 'P_DATALINK' as is VARCHAR"); } 
      public void Var165() {    testLocalTimeFailure("P_BLOB",Types.BLOB, "LocalTime");      }
      public void Var166() {    testLocalTimeFailure("P_CLOB",Types.CLOB, "LocalTime");      }
      public void Var167() {    testLocalTimeFailure("P_DBCLOB",Types.CLOB, "LocalTime");      }
      public void Var168() {    testLocalTimeFailure("P_BIGINT",Types.BIGINT, "LocalTime");      }
      public void Var169() {    testLocalTimeFailure("P_DECFLOAT16",Types.OTHER, "LocalTime");      }
      public void Var170() {    testLocalTimeFailure("P_DECFLOAT34",Types.OTHER, "LocalTime");      }
      public void Var171() {  if (checkBooleanSupport())  testLocalTimeFailure("P_BOOLEAN",Types.BOOLEAN, "LocalTime");       }
      public void Var172() { notApplicable(); } 
      public void Var173() { notApplicable(); } 
      public void Var174() { notApplicable(); } 
      public void Var175() { notApplicable(); } 
      public void Var176() { notApplicable(); } 
      public void Var177() { notApplicable(); } 
      public void Var178() { notApplicable(); } 
      public void Var179() { notApplicable(); } 
      public void Var180() { notApplicable(); } 
           
         


/* Test using Local Date */
           
         

      public void Var181() {    testLocalDateFailure("P_SMALLINT", Types.SMALLINT, "LocalDate");      }

 
      public void Var182() {    testLocalDateFailure("P_INTEGER", Types.INTEGER, "LocalDate");       }
      public void Var183() {    testLocalDateFailure("P_REAL", Types.REAL, "LocalDate");      }
      public void Var184() {    testLocalDateFailure("P_FLOAT", Types.FLOAT, "LocalDate");       }
      public void Var185() {    testLocalDateFailure("P_DOUBLE", Types.DOUBLE, "LocalDate");      }
      public void Var186() {    testLocalDateFailure("P_DECIMAL_50", Types.DECIMAL, "LocalDate");      }
      public void Var187() {    testLocalDateFailure("P_DECIMAL_105", Types.DECIMAL, "LocalDate");       }
      public void Var188() {    testLocalDateFailure("P_NUMERIC_50", Types.NUMERIC, "LocalDate");      }
      public void Var189() {    testLocalDateFailure("P_NUMERIC_105", Types.NUMERIC,  "LocalDate");       }
      public void Var190() {    testLocalDateFailure("P_CHAR_1",Types.CHAR, "LocalDate");       }
      
      public void Var191() {  testLocalDateParameter("P_CHAR_50",Types.CHAR,1900,1,1,"1900-01-01                                        " ); }
      public void Var192() {  testLocalDateParameter("P_CHAR_50",Types.CHAR,2022,3,4,"2022-03-04                                        " ); }
      public void Var193() {  testLocalDateParameter("P_CHAR_50",Types.CHAR,2099,12,31,"2099-12-31                                        " ); }
      public void Var194() {  testLocalDateParameter("P_VARCHAR_50",Types.VARCHAR,1900,1,1,"1900-01-01" ); }
      public void Var195() {  testLocalDateParameter("P_VARCHAR_50",Types.VARCHAR,2022,3,4,"2022-03-04" ); }
      public void Var196() {  testLocalDateParameter("P_VARCHAR_50",Types.VARCHAR,2099,12,31,"2099-12-31" ); }
      public void Var197() {    testLocalDateFailure("P_BINARY_20",Types.BINARY, "LocalDate");       }
      public void Var198() {    testLocalDateFailure("P_VARBINARY_20",Types.VARBINARY, "LocalDate");      }
      public void Var199() {    testLocalDateFailure("P_TIME",Types.TIME, "LocalDate");      }
      public void Var200() {  testLocalDateParameter("P_DATE",Types.DATE,1970,1,1,"1970-01-01" ); }
      public void Var201() {  testLocalDateParameter("P_DATE",Types.DATE,2022,3,4,"2022-03-04" ); }
      public void Var202() {  testLocalDateParameter("P_DATE",Types.DATE,2099,12,31,"2099-12-31" ); }
      public void Var203() {    testLocalDateFailure("P_TIMESTAMP",Types.TIMESTAMP, "LocalDate");       }
      public void Var204() {   notApplicable("Skipping 'P_DATALINK' as is VARCHAR"); } 
      public void Var205() {    testLocalDateFailure("P_BLOB",Types.BLOB, "LocalDate");      }
      public void Var206() {    testLocalDateFailure("P_CLOB",Types.CLOB, "LocalDate");      }
      public void Var207() {    testLocalDateFailure("P_DBCLOB",Types.CLOB, "LocalDate");      }
      public void Var208() {    testLocalDateFailure("P_BIGINT",Types.BIGINT, "LocalDate");      }
      public void Var209() {    testLocalDateFailure("P_DECFLOAT16", Types.OTHER, "LocalDate");      }
      public void Var210() {    testLocalDateFailure("P_DECFLOAT34", Types.OTHER,"LocalDate");      }
      public void Var211() {  if (checkBooleanSupport())  testLocalDateFailure("P_BOOLEAN", Types.BOOLEAN, "LocalDate");       }
      public void Var212() { notApplicable(); } 
      public void Var213() { notApplicable(); } 
      public void Var214() { notApplicable(); } 
      public void Var215() { notApplicable(); } 
      public void Var216() { notApplicable(); } 
      public void Var217() { notApplicable(); } 
      public void Var218() { notApplicable(); } 
      public void Var219() { notApplicable(); } 
      public void Var220() { notApplicable(); } 
           
  
      /* Test using LocalDateTime */

      public void Var221() {  testLocalDateTimeParameter("P_CHAR_50",Types.CHAR,1900,1,1,0,0,0,0,             "1900-01-01 00:00:00.0                             " ); }
      public void Var222() {  testLocalDateTimeParameter("P_CHAR_50",Types.CHAR,2022,3,4,4,5,6,123456789,     "2022-03-04 04:05:06.123456789                     " ); }
      public void Var223() {  testLocalDateTimeParameter("P_CHAR_50",Types.CHAR,2099,12,31,23,59,59,999999999,"2099-12-31 23:59:59.999999999                     " ); }
      public void Var224() {  testLocalDateTimeParameter("P_VARCHAR_50",Types.VARCHAR,1900,1,1,0,0,0,0,             "1900-01-01 00:00:00.0" ); }
      public void Var225() {  testLocalDateTimeParameter("P_VARCHAR_50",Types.VARCHAR,2022,3,4,4,5,6,123456789,     "2022-03-04 04:05:06.123456789" ); }
      public void Var226() {  testLocalDateTimeParameter("P_VARCHAR_50",Types.VARCHAR,2099,12,31,23,59,59,999999999,"2099-12-31 23:59:59.999999999" ); }
      public void Var227() {    testLocalDateTimeFailure("P_BINARY_20", Types.BINARY,  "LocalDateTime");       }
      public void Var228() {    testLocalDateTimeFailure("P_VARBINARY_20", Types.VARBINARY, "LocalDateTime");      }
      public void Var229() {  testLocalDateTimeParameter("P_TIME",Types.TIME,1900,1,1,0,0,0,0,             "00:00:00" ); }
      public void Var230() {  testLocalDateTimeParameter("P_TIME",Types.TIME,2022,3,4,4,5,6,123456789,     "04:05:06" ); }
      public void Var231() {  testLocalDateTimeParameter("P_TIME",Types.TIME,2099,12,31,23,59,59,999999999,"23:59:59" ); }
      public void Var232() {  testLocalDateTimeParameter("P_DATE",Types.DATE,1900,1,1,0,0,0,0,             "1900-01-01" ); }
      public void Var233() {  testLocalDateTimeParameter("P_DATE",Types.DATE,2022,3,4,4,5,6,123456789,     "2022-03-04" ); }
      public void Var234() {  testLocalDateTimeParameter("P_DATE",Types.DATE,2099,12,31,23,59,59,999999999,"2099-12-31" ); }

      public void Var235() {
	  if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	      testLocalDateTimeParameter("P_TIMESTAMP",Types.TIMESTAMP,1900,1,1,0,0,0,0,             "1900-01-01-00.00.00.000000" );
	  } else {
	      testLocalDateTimeParameter("P_TIMESTAMP",Types.TIMESTAMP,1900,1,1,0,0,0,0,             "1900-01-01 00:00:00.000000" );
	  }
      }
    public void Var236() {
	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	    testLocalDateTimeParameter("P_TIMESTAMP",Types.TIMESTAMP,2022,3,4,4,5,6,123456789,     "2022-03-04-04.05.06.123456" );
	} else {
	    testLocalDateTimeParameter("P_TIMESTAMP",Types.TIMESTAMP,2022,3,4,4,5,6,123456789,     "2022-03-04 04:05:06.123456" );
	}
    }
    public void Var237() {
	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	    testLocalDateTimeParameter("P_TIMESTAMP",Types.TIMESTAMP,2099,12,31,23,59,59,999999999,"2099-12-31-23.59.59.999999" );
	} else {
	    testLocalDateTimeParameter("P_TIMESTAMP",Types.TIMESTAMP,2099,12,31,23,59,59,999999999,"2099-12-31 23:59:59.999999" );
	}
    }

      public void Var238() {   notApplicable("Skipping 'P_DATALINK' as is VARCHAR"); } 
      public void Var239() {    testLocalDateTimeFailure("P_BLOB",Types.BLOB, "LocalDateTime");      }
      public void Var240() {    testLocalDateTimeFailure("P_CLOB",Types.CLOB, "LocalDateTime");      }
      public void Var241() {    testLocalDateTimeFailure("P_DBCLOB",Types.CLOB, "LocalDateTime");      }
      public void Var242() {    testLocalDateTimeFailure("P_BIGINT",Types.BIGINT, "LocalDateTime");      }
      public void Var243() {    testLocalDateTimeFailure("P_DECFLOAT16",Types.OTHER, "LocalDateTime");      }
      public void Var244() {    testLocalDateTimeFailure("P_DECFLOAT34",Types.OTHER, "LocalDateTime");      }
      public void Var245() {  if (checkBooleanSupport())   testLocalDateTimeFailure("P_BOOLEAN",Types.BOOLEAN, "LocalDateTime");       }
      public void Var246() { notApplicable(); } 
      public void Var247() { notApplicable(); } 
      public void Var248() { notApplicable(); } 
      public void Var249() { notApplicable(); } 
      public void Var250() { notApplicable(); } 



  
  



}
