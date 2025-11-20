///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDWrapper.java
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
// File Name:    JDDMDWrapper.java
//
// Classes:      JDDMDWrapper
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.DMD;

import java.io.FileOutputStream;
import java.sql.DatabaseMetaData;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;


/**
Testcase JDDMDWrapper.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>isWrapperFor()
<li>unwrap()
</ul>
**/
public class JDDMDWrapper
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDWrapper";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }



    // Private data.
    private              DatabaseMetaData dmd_; 



/**
Constructor.
**/
    public JDDMDWrapper (AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password) {
        super (systemObject, "JDDMDWrapper",
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
	dmd_ = connection_.getMetaData(); 
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        connection_.close ();
        connection_ = null; 

    }





/**
isWrapperFor() -- Call with null, should return false.
**/
    public void Var001() {
	if (checkJdbc40()) {

	    try {
		boolean answer = JDReflectionUtil.callMethod_B(dmd_, "isWrapperFor",  null);
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
		boolean answer = JDReflectionUtil.callMethod_B(dmd_, "isWrapperFor",  Class.forName("java.lang.String"));
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
		boolean answer = JDReflectionUtil.callMethod_B(dmd_, "isWrapperFor",  Class.forName("java.sql.DatabaseMetaData"));
		assertCondition(answer, "isWrapperFor(java.sql.DatabaseMetaData) returned false sb true"); 
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
		driverClass="com.ibm.db2.jdbc.app.DB2DatabaseMetaData"; 
	    } else if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		driverClass="com.ibm.as400.access.AS400JDBCDatabaseMetaData"; 
	    } else {
		driverClass="UNKNOWNDRIVER"; 
	    } 
	    try {
		boolean answer = JDReflectionUtil.callMethod_B(dmd_, "isWrapperFor",  Class.forName(driverClass));
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
		Object answer = JDReflectionUtil.callMethod_O(dmd_, "unwrap",  (Class<?>) null);
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
		Object answer = JDReflectionUtil.callMethod_O(dmd_, "unwrap",  Class.forName("java.lang.String"));

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
		Object answer = JDReflectionUtil.callMethod_O(dmd_, "unwrap",  Class.forName("java.sql.DatabaseMetaData"));
		assertCondition(answer==dmd_, "unwrap(java.sql.DatabaseMetaData) returned "+answer+" sb "+dmd_); 
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
		driverClass="com.ibm.db2.jdbc.app.DB2DatabaseMetaData"; 
	    } else if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
		driverClass="com.ibm.as400.access.AS400JDBCDatabaseMetaData"; 
	    } else {
		driverClass="UNKNOWNDRIVER"; 
	    } 
	    try {
		Object answer = JDReflectionUtil.callMethod_O(dmd_, "unwrap",  Class.forName(driverClass));
		assertCondition(answer==dmd_, "unwrap("+driverClass+") returned "+answer+" sb "+dmd_); 
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }








}



