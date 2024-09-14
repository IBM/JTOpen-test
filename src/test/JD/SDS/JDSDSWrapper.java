///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSDSWrapper.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDSDSWrapper.java
//
// Classes:      JDSDSWrapper
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.SDS;

import javax.sql.*; 
import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.util.Hashtable;



/**
Testcase JDSDSWrapper.  This tests the following methods
of the JDBC ResultSet class:

<ul>
<li>isWrapperFor()
<li>unwrap()
</ul>
**/
public class JDSDSWrapper
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDSDSWrapper";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDSDSTest.main(newArgs); 
   }



    // Private data.
    private              DataSource dataSource_;



/**
Constructor.
**/
    public JDSDSWrapper (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password) {
        super (systemObject, "JDSDSWrapper",
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
	dataSource_ = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdDataSource");
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {

    }





/**
isWrapperFor() -- Call with null, should return false.
**/
    public void Var001() {
	if (checkJdbc40()) {

	    try {
		boolean answer = JDReflectionUtil.callMethod_B(dataSource_, "isWrapperFor",  null);
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
		boolean answer = JDReflectionUtil.callMethod_B(dataSource_, "isWrapperFor",  Class.forName("java.lang.String"));
		assertCondition(!answer, "isWrapperFor(java.lang.String) returned true sb false"); 
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }




/**
isWrapperFor() -- Call with java.sql.ResultSetMetaData, should return true.
**/
    public void Var003() {
	if (checkJdbc40()) {

	    try {
		boolean answer = JDReflectionUtil.callMethod_B(dataSource_, "isWrapperFor",  Class.forName("javax.sql.DataSource"));
		assertCondition(answer, "isWrapperFor(javax.sql.DataSource) returned false sb true"); 
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
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		driverClass="com.ibm.db2.jdbc.app.DB2StdDataSource"; 
	    } else if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		driverClass="com.ibm.as400.access.AS400JDBCStatement"; 
	    } else {
		driverClass="UNKNOWNDRIVER"; 
	    } 
	    try {
		boolean answer = JDReflectionUtil.callMethod_B(dataSource_, "isWrapperFor",  Class.forName(driverClass));
		assertCondition(answer, "isWrapperFor("+driverClass+") returned false sb true"); 
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }



/**
unwrap() -- Call with null, should throw exception
**/
    public void Var005() {
	if (checkJdbc40()) {

	    try {
		Object answer = JDReflectionUtil.callMethod_O(dataSource_, "unwrap",  (Class)null);
		assertCondition(false, "Exception should have been thrown unwrapping null "+answer); 
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


/**
unwrap() -- Call with java.lang.String, should throw exception 
**/
    public void Var006() {
	if (checkJdbc40()) {

	    try {
		Object answer = JDReflectionUtil.callMethod_O(dataSource_, "unwrap",  Class.forName("java.lang.String"));

		assertCondition(false, "Exception should have been thrown unwrapping null "+answer); 
	    } catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }




/**
unwrap() -- Call with java.sql.DatabaseMetaData, should return object.
**/
    public void Var007() {
	if (checkJdbc40()) {

	    try {
		Object answer = JDReflectionUtil.callMethod_O(dataSource_, "unwrap",  Class.forName("javax.sql.DataSource"));
		assertCondition(answer==dataSource_, "unwrap(javax.sql.DataSource) returned "+answer+" sb "+dataSource_); 
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }


/**
unwrap() -- Call with driver specific class, should return object.
**/
    public void Var008() {
	if (checkJdbc40()) {
	    String driverClass;
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		driverClass="com.ibm.db2.jdbc.app.DB2StdDataSource"; 
	    } else if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		driverClass="com.ibm.as400.access.AS400JDBCStatement"; 
	    } else {
		driverClass="UNKNOWNDRIVER"; 
	    } 
	    try {
		Object answer = JDReflectionUtil.callMethod_O(dataSource_, "unwrap",  Class.forName(driverClass));
		assertCondition(answer==dataSource_, "unwrap("+driverClass+") returned "+answer+" sb "+dataSource_); 
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }








}



