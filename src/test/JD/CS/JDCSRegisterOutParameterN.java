///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSRegisterOutParameterN.java
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
// File Name:    JDCSRegisterOutParameter.java
//
// Classes:      JDCSRegisterOutParameter
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;
import test.JDTestcase;
import test.JD.JDSetupPackage;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDCSRegisterOutParameter.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>registerOutParameter()
</ul>
**/
public class JDCSRegisterOutParameterN
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSRegisterOutParameterN";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }


    private static final String package_            = "CSREGISTER";

    private static String PACKAGE_CACHE_NO    = "extended dynamic=false";
    private static String PACKAGE_CACHE_YES   = "extended dynamic=true;package="
                                                    + package_ + ";package library="
                                                    + JDCSTest.COLLECTION + ";package cache=true";

    private static String call1_  = "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)";
    private static String call3_  = "CALL " + JDSetupProcedure.STP_CS0;
    private  String call4_;
    private static String callRv_ = "?=CALL " + JDSetupProcedure.STP_CSPARMSRV + " (?, ?, ?)";    // @C1A


    private Connection          connection_     = null;
    private String              properties_     = "";
    private CallableStatement   cs1_;
    private CallableStatement   cs3_;
    private CallableStatement   cs4_;
    private CallableStatement   csRv_;          // @C1A



/**
Constructor.
**/
    public JDCSRegisterOutParameterN (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDCSRegisterOutParameterN",
            namesAndVars, runMode, fileOutputStream,
            password);
    }



/**
Reconnects with different properties, if needed.
**/
    private void reconnect (String properties)
        throws Exception
    {
        if (! properties_.equals (properties)) {
            properties_ = properties;
            if (connection_ != null)
                cleanup ();

            String url = baseURL_;
            connection_ = testDriver_.getConnection (url + ";" + properties_, userId_, encryptedPassword_);

            // Prepare some calls ahead of time.
            cs1_ = connection_.prepareCall (call1_);
            cs3_ = connection_.prepareCall (call3_);
            cs4_ = connection_.prepareCall (call4_);
            if (areReturnValuesSupported()) {                       // @C1A
                csRv_ = connection_.prepareCall (callRv_);          // @C1A
            }                                                       // @C1A
        }
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.

SQL400 - Removed the package priming code - we won't be using it. B1A
**/
    protected void setup ()
        throws Exception
    {

        call1_  = "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)";
        call3_  = "CALL " + JDSetupProcedure.STP_CS0;
        call4_ = JDSetupProcedure.getSQLString (JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);



	callRv_ = "?=CALL " + JDSetupProcedure.STP_CSPARMSRV + " (?, ?, ?)";    // @C1A



        if(isToolboxDriver()) {
        // Prime a package for package cache testing.
        JDSetupPackage.prime (systemObject_,
            package_, JDCSTest.COLLECTION, call1_);
        JDSetupPackage.prime (systemObject_,
            package_, JDCSTest.COLLECTION, call3_);
        JDSetupPackage.prime (systemObject_,
            package_, JDCSTest.COLLECTION, call4_);
        if (areReturnValuesSupported()) {                               // @C1A
            JDSetupPackage.prime (systemObject_,             // @C1A
                package_, JDCSTest.COLLECTION, callRv_);                // @C1A
        }                                                               // @C1A
        }
        else {
            /* SQL400 - don't do this package priming right now. */
            }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        cs1_.close ();
        cs3_.close ();
        cs4_.close ();
        if (areReturnValuesSupported()) {           // @C1A
            csRv_.close ();                         // @C1A
        }                                           // @C1A
        connection_.close ();
    }



/**
registerOutParameter() - Register a valid parameter index and type.
**/
  public void Var001()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        cs4_.registerOutParameter ("P_INTEGER", Types.INTEGER);
        assertCondition (true);
    }
    catch(Exception e)
    {
        failed (e, "Unexpected Exception");
    }
  }



/**
registerOutParameter() - Register parameter -1.
**/
  public void Var002()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        cs4_.registerOutParameter ("NONAME", Types.INTEGER);
        failed ("Didn't throw SQLException");
    }
    catch(Exception e) {
        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
    }
  }



/**
registerOutParameter() - Register parameter 0.
**/
  public void Var003()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        cs4_.registerOutParameter ("0", Types.INTEGER);
        failed ("Didn't throw SQLException");
    }
    catch(Exception e) {
        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
    }
  }



/**
registerOutParameter() - Register a parameter whose index
is too big.
**/
  public void Var004()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        cs4_.registerOutParameter ("35", Types.INTEGER);
        failed ("Didn't throw SQLException");
    }
    catch(Exception e) {
        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
    }
  }



/**
registerOutParameter() - Register a parameter when there are no parameters.
**/
  public void Var005()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        cs3_.registerOutParameter ("1", Types.INTEGER);
        failed ("Didn't throw SQLException");
    }
    catch(Exception e) {
        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
    }
  }



/**
registerOutParameter() - Register a type that is invalid.
**/
  public void Var006()
  {
     // @D1 test used to throw an exception.  It will now work
     // because the driver ignores type.
     //
     // try
     // {
     //    reconnect (PACKAGE_CACHE_NO);
     //    cs4_.registerOutParameter (2, 457);
     //    failed ("Didn't throw SQLException");
     // }
     // catch(Exception e) {
     //     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
     // }
     try
     {
        reconnect (PACKAGE_CACHE_NO);
        cs1_.registerOutParameter ("P2", 457);
        assertCondition (true);
     }
     catch(Exception e)
     {
        failed (e, "Unexpected Exception");
     }

  }



/**
registerOutParameter() - Register a parameter that is an IN parameter.
**/
  public void Var007()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        cs1_.registerOutParameter ("P1", Types.INTEGER);
        failed ("Didn't throw SQLException");
    }
    catch(Exception e) {
        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
    }
  }



/**
registerOutParameter() - Register a parameter that is an OUT parameter.
**/
  public void Var008()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        cs1_.registerOutParameter ("P2", Types.INTEGER);
        assertCondition (true);
    }
    catch(Exception e)
    {
        failed (e, "Unexpected Exception");
    }
  }



/**
registerOutParameter() - Register a parameter that is an INOUT parameter, that was
previously set.  Use the same type that was set.
**/
  public void Var009()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        cs1_.setInt ("P3", 123);
        cs1_.registerOutParameter ("P3", Types.INTEGER);
        assertCondition (true);
    }
    catch(Exception e)
    {
        failed (e, "Unexpected Exception");
    }
  }



/**
registerOutParameter() - Register a parameter that is an INOUT parameter, that was
previously set.  Use a different type that was set.
**/
  public void Var010()
  {
     // @D1 test used to throw an exception.  It will now work
     // because the driver ignores type.
     //
     // try
     // {
     //   reconnect (PACKAGE_CACHE_NO);
     //   cs1_.setInt (3, 123);
     //   cs1_.registerOutParameter (3, Types.VARBINARY);
     //   failed ("Didn't throw SQLException");
     // }
     // catch(Exception e) {
     //    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
     // }
     try
     {
        reconnect (PACKAGE_CACHE_NO);
        cs1_.setInt ("P3", 123);
        cs1_.registerOutParameter ("P3", Types.VARBINARY);
        assertCondition (true);
     }
     catch(Exception e)
     {
        failed (e, "Unexpected Exception");
     }
  }



/**
registerOutParameter() - Register a parameter that is an INOUT parameter, that was
not previously set.
**/
  public void Var011()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        cs1_.registerOutParameter ("P3", Types.INTEGER);
        assertCondition (true);
    }
    catch(Exception e)
    {
        failed (e, "Unexpected Exception");
    }
  }



/**
registerOutParameter() - Register a parameter that is an INOUT parameter, that was
not previously set, then set the parameter with the same
type.
**/
  public void Var012()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        cs1_.registerOutParameter ("P3", Types.INTEGER);
        cs1_.setInt ("P3", 123);
        assertCondition (true);
    }
    catch(Exception e)
    {
        failed (e, "Unexpected Exception");
    }
  }



/**
registerOutParameter() - Register a parameter that is an INOUT parameter, that was
not previously set, then set the parameter with a different
type.
**/
  public void Var013()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        cs1_.registerOutParameter ("P3", Types.INTEGER);
        cs1_.setString ("P3", "Hello");
        failed ("Didn't throw SQLException");
    }
    catch(Exception e) {
        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
    }
  }



/**
registerOutParameter() - Register a valid type with a scale.
**/
  public void Var014()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        cs4_.registerOutParameter ("P_DECIMAL_105", Types.DECIMAL, 5);
        assertCondition (true);
    }
    catch(Exception e)
    {
        failed (e, "Unexpected Exception");
    }
  }



/**
registerOutParameter() - Register a scale of -1.
**/
  public void Var015()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        cs4_.registerOutParameter ("P_DECIMAL_50", Types.DECIMAL, -1);
        failed ("Didn't throw SQLException");
    }
    catch(Exception e) {
        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
    }
  }



/**
registerOutParameter() - Register a valid type more than once.
**/
  public void Var016()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        cs4_.registerOutParameter ("P_INTEGER", Types.INTEGER);
        cs4_.registerOutParameter ("P_INTEGER", Types.INTEGER);
        assertCondition (true);
    }
    catch(Exception e)
    {
        failed (e, "Unexpected Exception");
    }
  }


/**
registerOutParameter() - Register a parameter when the statement is closed.
**/
  public void Var017()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        CallableStatement cs = connection_.prepareCall (
            "CALL STPTEST4 (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        cs.close ();
	cs.registerOutParameter ("P2", Types.INTEGER);
        failed ("Didn't throw SQLException");
    }
    catch(Exception e) {
        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
    }
  }



/**
registerOutParameter() - Register a valid parameter index and type,
when the statement comes from the package cache.
**/
  public void Var018()
  {
    try {
        reconnect (PACKAGE_CACHE_YES);
        cs4_.registerOutParameter ("P_INTEGER", Types.INTEGER);
        assertCondition (true);
    }
    catch(Exception e)
    {
        failed (e, "Unexpected Exception");
    }
  }



/**
registerOutParameter() - Register a parameter that is an IN parameter,
when the statement comes from the package cache.
**/
  public void Var019()
  {
    try {
        reconnect (PACKAGE_CACHE_YES);
        cs1_.registerOutParameter ("P1", Types.INTEGER);
        failed ("Didn't throw SQLException");
    }
    catch(Exception e) {
        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
    }
  }



/**
registerOutParameter() - Register a parameter that is an INOUT parameter, that was
not previously set, when the statement comes from
the package cache.
**/
  public void Var020()
  {
    try {
        reconnect (PACKAGE_CACHE_YES);
        cs1_.registerOutParameter ("P3", Types.INTEGER);
        assertCondition (true);
    }
    catch(Exception e)
    {
        failed (e, "Unexpected Exception");
    }
  }



/**
registerOutParameter() - When there is only a return value, register the return value.
**/
  public void Var021()
  {
    notApplicable("Return value test"); 
  }


/**
registerOutParameter() - When there is only a return value, register parameter 2.
**/
  public void Var022()
  {
    notApplicable("Return value test"); 
  }


/**
registerOutParameter() - When there is a return value, register the return value.
**/
  public void Var023()
  {
      notApplicable("Return parameter");
  }


/**
registerOutParameter() - When there is a return value, register the last parameter value.
**/
  public void Var024()
  {
      if (checkReturnValueSupport()) {
    try {
        reconnect (PACKAGE_CACHE_NO);
        csRv_.registerOutParameter ("P3", Types.INTEGER);
        assertCondition (true);
    }
    catch(Exception e)
    {
        failed (e, "Unexpected Exception");
    }
      }
  }


/**
registerOutParameter() - When there is a return value, register 1 more than the last parameter value.
**/
  public void Var025()
  {
      if (checkReturnValueSupport()) {
    try {
        reconnect (PACKAGE_CACHE_NO);
        csRv_.registerOutParameter ("5", Types.INTEGER);
        failed ("Didn't throw SQLException");
    }
    catch(Exception e) {
        assertExceptionIsInstanceOf (e, "java.sql.SQLException");
    }
      }
  }


/**
registerOutParameter() - When there is a return value, register the return value as something
other than an integer.
**/
  public void Var026()
  {
      notApplicable("Return value parameter");
  }


/**
 * register an int parameter as Types.JAVA_OBJECT
 * see getObject for more tests 
 */
  public void Var027()
  {
    try {
        reconnect (PACKAGE_CACHE_NO);
        cs4_.registerOutParameter ("P_INTEGER", Types.JAVA_OBJECT);
        assertCondition (true);
    }
    catch(Exception e)
    {
        failed (e, "Unexpected Exception -- added by native driver 2009/09/02 to test Types.OBJECT");
    }
  }


}



