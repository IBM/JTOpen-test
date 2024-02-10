///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSRegisterOutParameterSQLType.java
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
// File Name:    JDCSRegisterOutParameterSQLType.java
//
// Classes:      JDCSRegisterOutParameterSQLType
//
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDReflectionUtil;
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
Testcase JDCSRegisterOutParameterSQLType.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>registerOutParameter()
</ul>
**/
public class JDCSRegisterOutParameterSQLType
extends JDTestcase
{


    private static final String package_            = "CSREGISTER";

    private static String PACKAGE_CACHE_NO    = "extended dynamic=false";
    private static String PACKAGE_CACHE_YES   = "extended dynamic=true;package="
      + package_ + ";package library="
      + JDCSTest.COLLECTION + ";package cache=true";

    private static String call1_  = "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)";
    private static String call3_  = "CALL " + JDSetupProcedure.STP_CS0;
    private  String call4_;
    private static String callRv_ = "?=CALL " + JDSetupProcedure.STP_CSPARMSRV + " (?, ?, ?)";    // @C1A
    private static String callRv0_ = "?=CALL " + JDSetupProcedure.STP_CSRV;                       // @C1A


    private Connection          connection_     = null;
    private String              properties_     = "";
    private CallableStatement   cs1_;
    private CallableStatement   cs3_;
    private CallableStatement   cs4_;
    private CallableStatement   csRv_;          // @C1A
    private CallableStatement   csRv0_;         // @C1A



/**
Constructor.
**/
    public JDCSRegisterOutParameterSQLType (AS400 systemObject,
					    Hashtable namesAndVars,
					    int runMode,
					    FileOutputStream fileOutputStream,
					    
					    String password)
    {
	super (systemObject, "JDCSRegisterOutParameterSQLType",
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

	    String url = baseURL_
	      
	      ;
	    connection_ = testDriver_.getConnection (url + ";" + properties_,systemObject_.getUserId(), encryptedPassword_);

	    // Prepare some calls ahead of time.
	    cs1_ = connection_.prepareCall (call1_);
	    cs3_ = connection_.prepareCall (call3_);
	    cs4_ = connection_.prepareCall (call4_);
	    if (areReturnValuesSupported()) {                       // @C1A
		csRv_ = connection_.prepareCall (callRv_);          // @C1A
		csRv0_ = connection_.prepareCall (callRv0_);        // @C1A
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
	callRv0_ = "?=CALL " + JDSetupProcedure.STP_CSRV;                       // @C1A



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
		JDSetupPackage.prime (systemObject_,             // @C1A
				      package_, JDCSTest.COLLECTION, callRv0_);               // @C1A
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
	    csRv0_.close ();                        // @C1A
	}                                           // @C1A
	connection_.close ();
    }



  /*
   * Create the SQLType corresponding to Type.
   */
    private Object getSQLType(int typesNumber) throws Exception {
	return JDReflectionUtil.callStaticMethod_O("java.sql.JDBCType", "valueOf",
						   typesNumber);
    }


/**
registerOutParameter() - Register a valid parameter index and type.
**/
    public void Var001()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		JDReflectionUtil.callMethod_V(cs4_,"registerOutParameter",2, getSQLType(Types.INTEGER));
		assertCondition (true);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
registerOutParameter() - Register parameter -1.
**/
    public void Var002()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		JDReflectionUtil.callMethod_V(cs4_,"registerOutParameter",-1, getSQLType(Types.INTEGER));
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

/**
registerOutParameter() - Register parameter 0.
**/
    public void Var003()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		JDReflectionUtil.callMethod_V(cs4_,"registerOutParameter",0, getSQLType(Types.INTEGER));
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

/**
registerOutParameter() - Register a parameter whose index
is too big.
**/
    public void Var004()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		JDReflectionUtil.callMethod_V(cs4_,"registerOutParameter",35, getSQLType(Types.INTEGER));
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

/**
registerOutParameter() - Register a parameter when there are no parameters.
**/
    public void Var005()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		JDReflectionUtil.callMethod_V(cs3_,"registerOutParameter",1, getSQLType(Types.INTEGER));
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
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
     //    JDReflectionUtil.callMethod_V(cs4_,"registerOutParameter",2, 457);
     //    failed ("Didn't throw SQLException");
     // }
     // catch(Exception e) {
     //     assertExceptionIsInstanceOf (e, "java.sql.SQLException");
     // }
	notApplicable("Not possible to set invalid type"); 
    /* 
     try
     {
	reconnect (PACKAGE_CACHE_NO);
	JDReflectionUtil.callMethod_V(cs1_,"registerOutParameter", 2, 457);
	assertCondition (true);
     }
     catch(Exception e)
     {
	failed (e, "Unexpected Exception");
     }
*/ 
    }



/**
registerOutParameter() - Register a parameter that is an IN parameter.
**/
    public void Var007()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		JDReflectionUtil.callMethod_V(cs1_,"registerOutParameter",1, getSQLType(Types.INTEGER));
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

/**
registerOutParameter() - Register a parameter that is an OUT parameter.
**/
    public void Var008()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		JDReflectionUtil.callMethod_V(cs1_,"registerOutParameter", 2, getSQLType(Types.INTEGER));
		assertCondition (true);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
registerOutParameter() - Register a parameter that is an INOUT parameter, that was
previously set.  Use the same type that was set.
**/
    public void Var009()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		cs1_.setInt (3, 123);
		JDReflectionUtil.callMethod_V(cs1_,"registerOutParameter",3, getSQLType(Types.INTEGER));
		assertCondition (true);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
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
     //   JDReflectionUtil.callMethod_V(cs1_,"registerOutParameter",3, Types.VARBINARY);
     //   failed ("Didn't throw SQLException");
     // }
     // catch(Exception e) {
     //    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
     // }
	if (checkJdbc42()) {
	    try
	    {
		reconnect (PACKAGE_CACHE_NO);
		cs1_.setInt (3, 123);
		JDReflectionUtil.callMethod_V(cs1_,"registerOutParameter",3, getSQLType(Types.VARBINARY));
		assertCondition (true);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
registerOutParameter() - Register a parameter that is an INOUT parameter, that was
not previously set.
**/
    public void Var011()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		JDReflectionUtil.callMethod_V(cs1_,"registerOutParameter",3, getSQLType(Types.INTEGER));
		assertCondition (true);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
registerOutParameter() - Register a parameter that is an INOUT parameter, that was
not previously set, then set the parameter with the same
type.
**/
    public void Var012()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		JDReflectionUtil.callMethod_V(cs1_,"registerOutParameter",3, getSQLType(Types.INTEGER));
		cs1_.setInt (3, 123);
		assertCondition (true);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
registerOutParameter() - Register a parameter that is an INOUT parameter, that was
not previously set, then set the parameter with a different
type.
**/
    public void Var013()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		JDReflectionUtil.callMethod_V(cs1_,"registerOutParameter",3, getSQLType(Types.INTEGER));
		cs1_.setString (3, "Hello");
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

/**
registerOutParameter() - Register a valid type with a scale.
**/
    public void Var014()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		JDReflectionUtil.callMethod_V(cs4_,"registerOutParameter",7, getSQLType(Types.DECIMAL), 5);
		assertCondition (true);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
registerOutParameter() - Register a scale of -1.
**/
    public void Var015()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		JDReflectionUtil.callMethod_V(cs4_,"registerOutParameter",6, getSQLType(Types.DECIMAL), -1);
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

/**
registerOutParameter() - Register a valid type more than once.
**/
    public void Var016()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		JDReflectionUtil.callMethod_V(cs4_,"registerOutParameter",2, getSQLType(Types.INTEGER));
		JDReflectionUtil.callMethod_V(cs4_,"registerOutParameter",2, getSQLType(Types.INTEGER));
		assertCondition (true);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
registerOutParameter() - Register a parameter when the statement is closed.
**/
    public void Var017()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		CallableStatement cs = connection_.prepareCall (
								"CALL STPTEST4 (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		cs.close ();
		JDReflectionUtil.callMethod_V(cs,"registerOutParameter",2, getSQLType(Types.INTEGER));
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

/**
registerOutParameter() - Register a valid parameter index and type,
when the statement comes from the package cache.
**/
    public void Var018()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_YES);
		JDReflectionUtil.callMethod_V(cs4_,"registerOutParameter",2, getSQLType(Types.INTEGER));
		assertCondition (true);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
registerOutParameter() - Register a parameter that is an IN parameter,
when the statement comes from the package cache.
**/
    public void Var019()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_YES);
		JDReflectionUtil.callMethod_V(cs1_,"registerOutParameter",1, getSQLType(Types.INTEGER));
		failed ("Didn't throw SQLException");
	    }
	    catch(Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

/**
registerOutParameter() - Register a parameter that is an INOUT parameter, that was
not previously set, when the statement comes from
the package cache.
**/
    public void Var020()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_YES);
		JDReflectionUtil.callMethod_V(cs1_,"registerOutParameter",3, getSQLType(Types.INTEGER));
		assertCondition (true);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}

    }

/**
registerOutParameter() - When there is only a return value, register the return value.
**/
    public void Var021()
    {
	if (checkReturnValueSupport()) {
	    if (checkJdbc42()) {
		try {
		    reconnect (PACKAGE_CACHE_NO);
		    JDReflectionUtil.callMethod_V(csRv0_,"registerOutParameter",1, getSQLType(Types.INTEGER));
		    assertCondition (true);
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }

/**
registerOutParameter() - When there is only a return value, register parameter 2.
**/
    public void Var022()
    {
	if (checkReturnValueSupport()) {
	    if (checkJdbc42()) {
		try {
		    reconnect (PACKAGE_CACHE_NO);
		    JDReflectionUtil.callMethod_V(csRv0_,"registerOutParameter", 1, getSQLType(Types.INTEGER));
		    JDReflectionUtil.callMethod_V(csRv0_,"registerOutParameter", 2, getSQLType(Types.INTEGER));
		    failed ("Didn't throw SQLException");
		}
		catch(Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
    }

/**
registerOutParameter() - When there is a return value, register the return value.
**/
    public void Var023()
    {
	if (checkReturnValueSupport()) {
	    if (checkJdbc42()) {
		try {
		    reconnect (PACKAGE_CACHE_NO);
		    JDReflectionUtil.callMethod_V(csRv_,"registerOutParameter",1, getSQLType(Types.INTEGER));
		    assertCondition (true);
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }

/**
registerOutParameter() - When there is a return value, register the last parameter value.
**/
    public void Var024()
    {
	if (checkReturnValueSupport()) {
	    if (checkJdbc42()) {
		try {
		    reconnect (PACKAGE_CACHE_NO);
		    JDReflectionUtil.callMethod_V(csRv_,"registerOutParameter",4, getSQLType(Types.INTEGER));
		    assertCondition (true);
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }

/**
registerOutParameter() - When there is a return value, register 1 more than the last parameter value.
**/
    public void Var025()
    {
	if (checkReturnValueSupport()) {
	    if (checkJdbc42()) {
		try {
		    reconnect (PACKAGE_CACHE_NO);
		    JDReflectionUtil.callMethod_V(csRv_,"registerOutParameter",5, getSQLType(Types.INTEGER));
		    failed ("Didn't throw SQLException");
		}
		catch(Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
    }

/**
registerOutParameter() - When there is a return value, register the return value as something
other than an integer.
**/
    public void Var026()
    {
	if (checkReturnValueSupport()) {
	    if (checkJdbc42()) {
		try {
		    reconnect (PACKAGE_CACHE_NO);
		    JDReflectionUtil.callMethod_V(csRv_,"registerOutParameter",1, getSQLType(Types.SMALLINT));
		    failed ("Didn't throw SQLException");
		}
		catch(Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
    }

/**
 * register an int parameter as Types.JAVA_OBJECT
 * see getObject for more tests 
 */
    public void Var027()
    {
	if (checkJdbc42()) {
	    try {
		reconnect (PACKAGE_CACHE_NO);
		JDReflectionUtil.callMethod_V(cs4_,"registerOutParameter",2, getSQLType(Types.JAVA_OBJECT));
		assertCondition (true);
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception -- added by native driver 2009/09/02 to test Types.OBJECT");
	    }
	}
    }

}



