///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionWrapper.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionWrapper.java
//
// Classes:      JDConnectionWrapper
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Hashtable;


/**
Testcase JDConnectionWrapper.  This tests the following methods
of the JDBC Connection class:

<ul>
<li>isWrapperFor()
<li>unwrap()
</ul>
**/
public class JDConnectionWrapper
extends JDTestcase {



    // Private data.
    private              Connection     connection_;



/**
Constructor.
**/
    public JDConnectionWrapper (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password) {
        super (systemObject, "JDConnectionWrapper",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        connection_.close ();
    }





/**
isWrapperFor() -- Call with null, should return false.
**/
    public void Var001() {
	if (checkJdbc40()) {

	    try {
		boolean answer = JDReflectionUtil.callMethod_B(connection_, "isWrapperFor",  null);
		assertCondition(!answer, "isWrapperFor(null) returned true sb false"); 
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }


/**
isWrapperFor() -- Call with java.lang.String, should return false.
**/
    public void Var002() {
	if (checkJdbc40()) {

	    try {
		boolean answer = JDReflectionUtil.callMethod_B(connection_, "isWrapperFor",  Class.forName("java.lang.String"));
		assertCondition(!answer, "isWrapperFor(java.lang.String) returned true sb false"); 
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }




/**
isWrapperFor() -- Call with java.sql.Connection, should return true.
**/
    public void Var003() {
	if (checkJdbc40()) {

	    try {
		boolean answer = JDReflectionUtil.callMethod_B(connection_, "isWrapperFor",  Class.forName("java.sql.Connection"));
		assertCondition(answer, "isWrapperFor(java.sql.Connection) returned false sb true"); 
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }


/**
isWrapperFor() -- Call with driver specific class, should return true.
**/
    public void Var004() {
	if (checkJdbc40()) {
	    String driverClass;
        String proxy = System.getProperty("com.ibm.as400.access.AS400.proxyServer");
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		driverClass="com.ibm.db2.jdbc.app.DB2Connection"; 
	    } else if (isToolboxDriver()) {
            if(proxy==null)
		        driverClass="com.ibm.as400.access.AS400JDBCConnection"; 
            else
                driverClass="java.sql.Connection";  //jdconnectionProxy cannot cast to AS400jdbcconnection (purpose of wrapper)
	    } else {
		driverClass="UNKNOWNDRIVER"; 
	    } 
	    try {
		boolean answer = JDReflectionUtil.callMethod_B(connection_, "isWrapperFor",  Class.forName(driverClass));
		assertCondition(answer, "isWrapperFor("+driverClass+") returned false sb true"); 
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }


/**
isWrapperFor() -- Call with driver specific ewlm class, should return true.
**/
    public void Var005() {
	if (checkJdbc40()) {
	    String driverClass;
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		driverClass="com.ibm.db2.jdbc.app.DB2eWLMConnection"; 
		try {
		    boolean answer = JDReflectionUtil.callMethod_B(connection_, "isWrapperFor",  Class.forName(driverClass));
		    assertCondition(answer, "isWrapperFor("+driverClass+") returned false sb true"); 
		} catch (Exception e) {
		    failed(e, "Unexpected Exception");
		}
	    } else {
		notApplicable("native ewlm test"); 
	    }
	}
    }



/**
unwrap() -- Call with null, should throw exception
**/
    public void Var006() {
	if (checkJdbc40()) {

	    try {
		Object answer = JDReflectionUtil.callMethod_O(connection_, "unwrap",  (Class) null);
		assertCondition(false, "Exception should have been thrown unwrapping null "+answer); 
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
unwrap() -- Call with java.lang.String, should throw exception 
**/
    public void Var007() {
	if (checkJdbc40()) {

	    try {
		Object answer = JDReflectionUtil.callMethod_O(connection_, "unwrap",  Class.forName("java.lang.String"));

		assertCondition(false, "Exception should have been thrown unwrapping null "+answer); 
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }




/**
unwrap() -- Call with java.sql.Connection, should return object.
**/
    public void Var008() {
	if (checkJdbc40()) {

	    try {
		Object answer = JDReflectionUtil.callMethod_O(connection_, "unwrap",  Class.forName("java.sql.Connection"));
		assertCondition(answer==connection_, "unwrap(java.sql.Connection) returned "+answer+" sb "+connection_); 
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }


/**
unwrap() -- Call with driver specific class, should return object.
**/
    public void Var009() {
	if (checkJdbc40()) {
	    String driverClass;
        String proxy = System.getProperty("com.ibm.as400.access.AS400.proxyServer");
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		driverClass="com.ibm.db2.jdbc.app.DB2Connection"; 
	    } else if (isToolboxDriver()) {
            if(proxy==null)
                driverClass="com.ibm.as400.access.AS400JDBCConnection"; 
            else
                driverClass="java.sql.Connection";  //jdconnectionProxy cannot cast to AS400jdbcconnection (purpose of wrapper)
		 
	    } else {
		driverClass="UNKNOWNDRIVER"; 
	    } 
	    try {
		Object answer = JDReflectionUtil.callMethod_O(connection_, "unwrap",  Class.forName(driverClass));
		assertCondition(answer==connection_, "unwrap(java.sql.Connection) returned "+answer+" sb "+connection_); 
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }


/**
unwrap() -- Call with driver specific ewlm class, should return object 
**/
    public void Var010() {
	if (checkJdbc40()) {
	    String driverClass;
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		driverClass="com.ibm.db2.jdbc.app.DB2eWLMConnection"; 
		try {
		    Object answer = JDReflectionUtil.callMethod_O(connection_, "unwrap",  Class.forName(driverClass));
		    assertCondition(answer==connection_, "unwrap(java.sql.Connection) returned "+answer+" sb "+connection_); 

		} catch (Exception e) {
		    failed(e, "Unexpected Exception");
		}
	    } else {
		notApplicable("native ewlm test"); 
	    }
	}
    }






}



