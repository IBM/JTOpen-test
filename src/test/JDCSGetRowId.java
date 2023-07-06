///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetRowId.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetRowId.java
//
// Classes:      JDCSGetRowId
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
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.math.BigInteger;

/**
 Testcase JDCSGetRowId.  This tests the following
 method of the JDBC CallableStatement class:

 <ul>
 <li>getRowId()
 </ul>
 **/
public class JDCSGetRowId extends JDCSGetTestcase {
  String added = " -- added by native driver 02/07/2007 "; 

  /**
   Constructor.
   **/
  public JDCSGetRowId(AS400 systemObject, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream,  String password) {
    super(systemObject, "JDCSGetRowId", namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   Performs setup needed before running variations.
   
   @exception Exception If an exception occurs.
   **/
  protected void setup() throws Exception {
    super.setup(); 
   
  }

  /**
   Performs cleanup needed after running variations.
   
   @exception Exception If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    super.cleanup(); 
   
  }

  /**
   getRowId() - Get parameter -1.
   **/
  public void Var001() {
    if (checkJdbc40()) {
      try {
        csTypes_.execute();
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", -1);
        failed("Didn't throw SQLException " + p);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      }
    }
  }

  /**
   getRowId() - Get parameter 0.
   **/
  public void Var002() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 0);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Use a parameter that is too big.
   **/
  public void Var003() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 35);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a parameter when there are no parameters.
   **/
  public void Var004() {
    if (checkJdbc40()) { // I created a whole new Connection object here to work
      // around a server bug.
      Connection c = null;
      try {
        c = testDriver_.getConnection(baseURL_ + ";errors=full", userId_, encryptedPassword_);
        CallableStatement cs = c
            .prepareCall("CALL " + JDSetupProcedure.STP_CS0);
        cs.execute();
        Object p = JDReflectionUtil.callMethod_O(cs, "getRowId", 1);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }

      if (c != null)
        try {
          c.close();
        } catch (Exception e) {
        }
    }
  }

  /**
   getRowId() - Get a parameter that was not registered.
   **/
  public void Var005() {
    if (checkJdbc40()) {
      try {
        CallableStatement cs = JDSetupProcedure.prepare(connection_,
            JDSetupProcedure.STP_CSTYPESOUT,
            supportedFeatures_);
        cs.execute();
        Object p = JDReflectionUtil.callMethod_O(cs, "getRowId", 12);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a parameter when the statement has not been
   executed.
   **/
  public void Var006() {
    if (checkJdbc40()) {
      try {
        CallableStatement cs = JDSetupProcedure.prepare(connection_,
            JDSetupProcedure.STP_CSTYPESOUT, 
            supportedFeatures_);
        cs.registerOutParameter(14, Types.VARBINARY);
        Object p = JDReflectionUtil.callMethod_O(cs, "getRowId", 14);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a parameter when the statement is closed.
   **/
  public void Var007() {
    if (checkJdbc40()) {
      try {
        CallableStatement cs = JDSetupProcedure.prepare(connection_,
            JDSetupProcedure.STP_CSTYPESOUT,
            supportedFeatures_);
        cs.registerOutParameter(14, Types.VARBINARY);
        cs.execute();
        cs.close();
        Object p = JDReflectionUtil.callMethod_O(cs, "getRowId", 14);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get an IN parameter that was correctly registered.
   **/
  public void Var008() {
    if (checkJdbc40()) {
      try {
        CallableStatement cs = JDSetupProcedure.prepare(connection_,
            JDSetupProcedure.STP_CSTYPESIN, 
            supportedFeatures_);
        JDSetupProcedure.setTypesParameters(cs, JDSetupProcedure.STP_CSTYPESIN,
            supportedFeatures_);
        cs.registerOutParameter(14, Types.VARBINARY);
        cs.execute();
        Object p = JDReflectionUtil.callMethod_O(cs, "getRowId", 14);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get an INOUT parameter, where the OUT parameter is
   longer than the IN parameter.
   **/
  public void Var009() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        CallableStatement cs = JDSetupProcedure.prepare(connection_,
            JDSetupProcedure.STP_CSTYPESINOUT, 
            supportedFeatures_);
        JDSetupProcedure.setTypesParameters(cs,
            JDSetupProcedure.STP_CSTYPESINOUT, 
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSTYPESINOUT,
            supportedFeatures_, getDriver());
        cs.registerOutParameter(14, Types.VARBINARY);
        cs.execute();
        Object p = JDReflectionUtil.callMethod_O(cs, "getRowId", 14);
        byte[] check = new byte[]
          { (byte) 'D', (byte) 'a', (byte) 'v', (byte) 'e', (byte) ' ',
              (byte) 'W', (byte) 'a', (byte) 'l', (byte) 'l', (byte) 'B',
              (byte) 'o', (byte) 'n', (byte) 'j', (byte) 'o', (byte) 'u',
              (byte) 'r' };
        // for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
        // for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");
	boolean passed = areEqual(ba, check);
	if (!passed) {
	   for (int i = 0; i < ba.length && i < check.length; ++i){
	       System.out.println ("ba[" + i + "]=" + Integer.toHexString((int)(0xFF & ba[i]))+" check[" + i + "]=" + Integer.toHexString((int)(0xFF & check[i])));
	   }

	}
        assertCondition(passed, added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a SMALLINT.
   **/
  public void Var010() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 1);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an INTEGER.
   **/
  public void Var011() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 2);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an REAL.
   **/
  public void Var012() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 3);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an FLOAT.
   **/
  public void Var013() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 4);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an DOUBLE.
   **/
  public void Var014() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 5);
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an DECIMAL.
   **/
  public void Var015() {
    if (checkJdbc40()) {

      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 6);
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an NUMERIC.
   **/
  public void Var016() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 8);
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a CHAR.
   **/
  public void Var017() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 11);
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a VARCHAR.
   **/
  public void Var018() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 12);
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a BINARY.
   **/
  public void Var019() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 13);
        byte[] check = new byte[]
          { (byte) 'M', (byte) 'u', (byte) 'r', (byte) 'c', (byte) 'h',
              (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
              (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
              (byte) ' ', (byte) ' ', (byte) ' ' };
        // for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
        // for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

        assertCondition(areEqual(ba, check), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a BINARY, with maxFieldSize
   set to 0.  Check that no DataTruncation warning is posted
   and the string is not trucated.
   **/
  public void Var020() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        csTypes_.setMaxFieldSize(0);
        csTypes_.clearWarnings();
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 13);
        byte[] check = new byte[]
          { (byte) 'M', (byte) 'u', (byte) 'r', (byte) 'c', (byte) 'h',
              (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
              (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
              (byte) ' ', (byte) ' ', (byte) ' ' };
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

        assertCondition(areEqual(ba, check) && (csTypes_.getWarnings() == null), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }

  }

  /**
   getRowId() - Get a type that was registered as a BINARY, with maxFieldSize
   set to 5.  Check that no DataTruncation warning is posted
   and the string is truncated.
   **/
  public void Var021() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        csTypes_.setMaxFieldSize(3);
        csTypes_.clearWarnings();
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 13);
        byte[] check = new byte[]
          { (byte) 'M', (byte) 'u', (byte) 'r' };
        DataTruncation dt = (DataTruncation) csTypes_.getWarnings();
        csTypes_.setMaxFieldSize(0);
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

        assertCondition((areEqual(ba, check))
            && (csTypes_.getWarnings() == null) && dt == null, added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a VARBINARY.
   **/
  public void Var022() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 14);
        byte[] check = new byte[]
          { (byte) 'D', (byte) 'a', (byte) 'v', (byte) 'e', (byte) ' ',
              (byte) 'W', (byte) 'a', (byte) 'l', (byte) 'l' };
        // for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
        // for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

        assertCondition(areEqual(ba, check), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a VARBINARY, with maxFieldSize
   set to 0.  Check that no DataTruncation warning is posted
   and the string is not trucated.
   **/
  public void Var023() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        csTypes_.setMaxFieldSize(0);
        csTypes_.clearWarnings();
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 14);
        byte[] check = new byte[]
          { (byte) 'D', (byte) 'a', (byte) 'v', (byte) 'e', (byte) ' ',
              (byte) 'W', (byte) 'a', (byte) 'l', (byte) 'l' };
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

        assertCondition((areEqual(ba, check))
            && (csTypes_.getWarnings() == null), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a VARBINARY, with maxFieldSize
   set to 5.  Check that no DataTruncation warning is posted
   and the string is truncated.
   **/
  public void Var024() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        csTypes_.setMaxFieldSize(6);
        csTypes_.clearWarnings();
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 14);
        byte[] check = new byte[]
          { (byte) 'D', (byte) 'a', (byte) 'v', (byte) 'e', (byte) ' ',
              (byte) 'W' };
        csTypes_.setMaxFieldSize(0);
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

        assertCondition((areEqual(ba, check)
            && (csTypes_.getWarnings() == null)), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }

  }

  /**
   getRowId() - Get a type that was registered as a DATE.
   **/
  public void Var025() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 15);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }

  }

  /**
   getRowId() - Get a type that was registered as a TIME.
   **/
  public void Var026() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 16);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a TIMESTAMP.
   **/
  public void Var027() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 17);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an OTHER.
   **/
  public void Var028() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 18);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a BLOB.
   **/
  public void Var029() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 19);

        byte[] check = new byte[]
          { (byte) 'D', (byte) 'a', (byte) 'v', (byte) 'e', (byte) ' ',
              (byte) 'E', (byte) 'g', (byte) 'l', (byte) 'e' }; // Should be 'Dave Egle'

        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");
        assertCondition(areEqual(ba, check), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a CLOB.
   **/
  public void Var030() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 20);
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a BIGINT.
   **/
  public void Var031() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_O(csTypes_, "getRowId", 22);
        if (getDriver() == JDTestDriver.DRIVER_NATIVE)  /* rowid from bigint */ 
          failed("Didn't throw SQLException");
        else {
          // should be 987662234567 in bytes
          BigInteger bigint = new BigInteger("987662234567");
          byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

          BigInteger parameter = new BigInteger(ba);
          if (bigint.compareTo(parameter) == 0)
            succeeded();
          else
            failed(bigint.toString() + " != " + parameter.toString()+added );
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) /* rowid from bigint */ 
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        else
          failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getRowId() - Get an INOUT parameter, where the OUT parameter is
   longer than the IN parameter, when the output parameter is registered first.
   
   SQL400 - We added this testcase because of a customer bug.
   **/
  public void Var032() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        CallableStatement cs = JDSetupProcedure.prepare(connection_,
            JDSetupProcedure.STP_CSTYPESINOUT, 
            supportedFeatures_);
        JDSetupProcedure.register(cs, JDSetupProcedure.STP_CSTYPESINOUT,
            supportedFeatures_, getDriver());
        cs.registerOutParameter(14, Types.VARBINARY);
        JDSetupProcedure.setTypesParameters(cs,
            JDSetupProcedure.STP_CSTYPESINOUT, 
            supportedFeatures_);
        cs.execute();
        Object p = JDReflectionUtil.callMethod_O(cs, "getRowId", 14);
        byte[] check = new byte[]
          { (byte) 'D', (byte) 'a', (byte) 'v', (byte) 'e', (byte) ' ',
              (byte) 'W', (byte) 'a', (byte) 'l', (byte) 'l', (byte) 'B',
              (byte) 'o', (byte) 'n', (byte) 'j', (byte) 'o', (byte) 'u',
              (byte) 'r' };
        //for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
        //for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

	boolean passed = areEqual(ba, check);
	if (!passed) {
	   for (int i = 0; i < ba.length && i < check.length; ++i){
	       System.out.println ("ba[" + i + "]=" + Integer.toHexString((int)(0xFF & ba[i]))+" check[" + i + "]=" + Integer.toHexString((int)(0xFF & check[i])));
	   }

	}
 
        assertCondition(passed, added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }
  }

  // - NAMED PARAMETERS
  /**
   getRowId() - Get a type that was registered as a SMALLINT.
   **/
  public void Var033() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_SMALLINT");
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an INTEGER.
   **/
  public void Var034() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_INTEGER");
        failed("Didn't throw SQLException " + p + added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an REAL.
   **/
  public void Var035() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_REAL");
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an FLOAT.
   **/
  public void Var036() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_FLOAT");
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an DOUBLE.
   **/
  public void Var037() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_DOUBLE");
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an DECIMAL.
   **/
  public void Var038() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_DECIMAL_50");
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an DECIMAL.
   **/
  public void Var039() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_DECIMAL_105");
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an NUMERIC.
   **/
  public void Var040() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_NUMERIC_50");
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an NUMERIC.
   **/
  public void Var041() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_NUMERIC_105");
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a CHAR.
   **/
  public void Var042() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_CHAR_1");
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) /* rowid from char */ 
          failed("Didn't throw SQLException" + p+added);
        else {
          byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

          if (ba[0] == 12)
            succeeded();
          else
            failed(ba[0] + " != 12"+added);
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) /* rowid from char */ 
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        else
          failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a CHAR.
   **/
  public void Var043() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_CHAR_50");
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a VARCHAR.
   **/
  public void Var044() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_VARCHAR");
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a BINARY.
   **/
  public void Var045() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_BINARY_20");
        byte[] check = new byte[]
          { (byte) 'M', (byte) 'u', (byte) 'r', (byte) 'c', (byte) 'h',
              (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
              (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
              (byte) ' ', (byte) ' ', (byte) ' ' };
        // for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
        // for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

        assertCondition(areEqual(ba, check), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a BINARY, with maxFieldSize
   set to 0.  Check that no DataTruncation warning is posted
   and the string is not trucated.
   **/
  public void Var046() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        csTypes_.setMaxFieldSize(0);
        csTypes_.clearWarnings();
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_BINARY_20");
        byte[] check = new byte[]
          { (byte) 'M', (byte) 'u', (byte) 'r', (byte) 'c', (byte) 'h',
              (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
              (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ', (byte) ' ',
              (byte) ' ', (byte) ' ', (byte) ' ' };
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

        assertCondition(areEqual(ba, check) && (csTypes_.getWarnings() == null), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a BINARY, with maxFieldSize
   set to 5.  Check that no DataTruncation warning is posted
   and the string is truncated.
   **/
  public void Var047() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        csTypes_.setMaxFieldSize(3);
        csTypes_.clearWarnings();
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_BINARY_20");
        byte[] check = new byte[]
          { (byte) 'M', (byte) 'u', (byte) 'r' };
        DataTruncation dt = (DataTruncation) csTypes_.getWarnings();
        csTypes_.setMaxFieldSize(0);
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

        assertCondition((areEqual(ba, check))
            && (csTypes_.getWarnings() == null) && (dt == null), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a VARBINARY.
   **/
  public void Var048() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_VARBINARY_20");
        byte[] check = new byte[]
          { (byte) 'D', (byte) 'a', (byte) 'v', (byte) 'e', (byte) ' ',
              (byte) 'W', (byte) 'a', (byte) 'l', (byte) 'l' };
        // for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
        // for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

        assertCondition(areEqual(ba, check), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a VARBINARY, with maxFieldSize
   set to 0.  Check that no DataTruncation warning is posted
   and the string is not trucated.
   **/
  public void Var049() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        csTypes_.setMaxFieldSize(0);
        csTypes_.clearWarnings();
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_VARBINARY_20");
        byte[] check = new byte[]
          { (byte) 'D', (byte) 'a', (byte) 'v', (byte) 'e', (byte) ' ',
              (byte) 'W', (byte) 'a', (byte) 'l', (byte) 'l' };
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

        assertCondition((areEqual(ba, check))
            && (csTypes_.getWarnings() == null), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a VARBINARY, with maxFieldSize
   set to 5.  Check that no DataTruncation warning is posted
   and the string is truncated.
   **/
  public void Var050() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        csTypes_.setMaxFieldSize(6);
        csTypes_.clearWarnings();
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_VARBINARY_20");
        byte[] check = new byte[]
          { (byte) 'D', (byte) 'a', (byte) 'v', (byte) 'e', (byte) ' ',
              (byte) 'W' };
        csTypes_.setMaxFieldSize(0);
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

        assertCondition((areEqual(ba, check))
            && (csTypes_.getWarnings() == null), added);
      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a DATE.
   **/
  public void Var051() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_DATE");
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a TIME.
   **/
  public void Var052() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_TIME");
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }

  }

  /**
   getRowId() - Get a type that was registered as a TIMESTAMP.
   **/
  public void Var053() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_TIMESTAMP");
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as an OTHER.
   **/
  public void Var054() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_DATALINK");
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a BLOB.
   **/
  public void Var055() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_BLOB");

        byte[] check = new byte[]
          { (byte) 'D', (byte) 'a', (byte) 'v', (byte) 'e', (byte) ' ',
              (byte) 'E', (byte) 'g', (byte) 'l', (byte) 'e' }; // Should be 'Dave Egle'
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

        assertCondition(areEqual(ba, check), added);

      } catch (Exception e) {
        failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a CLOB.
   **/
  public void Var056() {
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_CLOB");
        failed("Didn't throw SQLException" + p+added);
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
      }
    }
  }

  /**
   getRowId() - Get a type that was registered as a BIGINT.
   **/
  public void Var057() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {
        Object p = JDReflectionUtil.callMethod_OS(csTypes_, "getRowId",
            "P_BIGINT");
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) /* rowid from bigint */ 
          failed("Didn't throw SQLException");
        else {
          // should be 987662234567 in bytes
          BigInteger bigint = new BigInteger("987662234567");
          byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

          BigInteger parameter = new BigInteger(ba);
          if (bigint.compareTo(parameter) == 0)
            succeeded();
          else
            failed(bigint.toString() + " != " + parameter.toString()+added);
        }
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_NATIVE)  /* rowid from bigint */ 
          assertExceptionIsInstanceOf(e, "java.sql.SQLException", added);
        else
          failed(e, "Unexpected Exception"+added);
      }
    }
  }

  /**
   getByte() -- Get a type registered as VARCHAR for BIT data, but containing all spaces.
   Added to expose native bug..
   **/
  public void Var058() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {

        String sql = "CREATE PROCEDURE "
            + JDCSTest.COLLECTION
            + ".ALLSPVB (OUT B VARCHAR(80) FOR BIT DATA) LANGUAGE SQL SPECIFIC ALLSPVB JDCSALLSPVB: BEGIN SET B=X'0000000000000000'; END JDCSALLSPVB";

        Statement stmt = connection_.createStatement();
        try {
          stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION
              + ".ALLSPVB");
        } catch (Exception e) {
        }
        stmt.executeUpdate(sql);
        CallableStatement cstmt = connection_.prepareCall("{call "
            + JDCSTest.COLLECTION + ".ALLSPVB (?)}");
        cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
        cstmt.execute();

        byte[] check =
          { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
              (byte) 0, (byte) 0 };
        Object p = JDReflectionUtil.callMethod_O(cstmt, "getRowId", 1);
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

        assertCondition(areEqual(ba, check),
            "Values are not equal "+added + "***\ngot\n****\n"+
	      bytesToString(ba) +"\n****\nexpected\n****\n"+
              bytesToString(check)); 
      
      } catch  (Exception e) {
        failed(e, "Unexpected Exception "+added); 
      }
    }
  }

  /**
   getByte() -- Get a type registered as VARCHAR for BIT data, but containing all spaces.
   Added to expose native bug..
   **/
  public void Var059() {
      if (isToolboxDriver())
      {
          notApplicable("Toolbox does not allow this");
          return;
      }
    if (checkJdbc40()) {
      try {

        String sql = "CREATE PROCEDURE "
            + JDCSTest.COLLECTION
            + ".ALLSPVB (OUT B VARCHAR(80) FOR BIT DATA) LANGUAGE SQL SPECIFIC ALLSPVB JDCSALLSPVB: BEGIN SET B=X'4040404040404040'; END JDCSALLSPVB";

        Statement stmt = connection_.createStatement();
        try {
          stmt.executeUpdate("drop procedure " + JDCSTest.COLLECTION
              + ".ALLSPVB");
        } catch (Exception e) {
        }
        stmt.executeUpdate(sql);
        CallableStatement cstmt = connection_.prepareCall("{call "
            + JDCSTest.COLLECTION + ".ALLSPVB (?)}");
        cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
        cstmt.execute();

        byte[] check =
          { (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40,
              (byte) 0x40, (byte) 0x40, (byte) 0x40 };
        Object p = JDReflectionUtil.callMethod_O(cstmt, "getRowId", 1);
        byte[] ba = (byte[]) JDReflectionUtil.callMethod_O(p, "getBytes");

        assertCondition(areEqual(ba, check),
            "Values are not equal -- "+added); 
      } catch (Exception e) {
        failed(e, "Unexpected Exception -- "+added); 
      }
    }
  }

  /**
   * getRowId() - Get a type that was registered as a BOOLEAN.
   **/

  public void Var060() {
    if (checkJdbc40()) {

      if (checkBooleanSupport()) {
        getTestFailed(csTypes_, "getRowId", 25, "Data type mismatch", "");

      }
    }
  }

  /**
   * getRowId() - Get a type that was registered as a BOOLEAN.
   **/

  public void Var061() {
    if (checkJdbc40()) {

      if (checkBooleanSupport()) {
        getTestFailed(csTypes_, "getRowId", "P_BOOLEAN", "Data type mismatch",
            "");

      }
    }
  }

}
