///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementWrapper.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementWrapper.java
//
// Classes:      JDStatementWrapper
//
////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Hashtable;


/**
Testcase JDStatementWrapper.  This tests the following methods
of the JDBC ResultSet class:

<ul>
<li>isWrapperFor()
<li>unwrap()
</ul>
**/
public class JDStatementWrapper
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDStatementWrapper";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDStatementTest.main(newArgs); 
   }



    // Private data.
    private              Connection     connection_;
    private              Statement      stmt_; 



/**
Constructor.
**/
    public JDStatementWrapper (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password) {
        super (systemObject, "JDStatementWrapper",
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
	stmt_ = connection_.createStatement();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
	stmt_.close(); 
        connection_.close ();
    }





/**
isWrapperFor() -- Call with null, should return false.
**/
    public void Var001() {
	if (checkJdbc40()) {

	    try {
		boolean answer = JDReflectionUtil.callMethod_B(stmt_, "isWrapperFor",  null);
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
		boolean answer = JDReflectionUtil.callMethod_B(stmt_, "isWrapperFor",  Class.forName("java.lang.String"));
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
		boolean answer = JDReflectionUtil.callMethod_B(stmt_, "isWrapperFor",  Class.forName("java.sql.Statement"));
		assertCondition(answer, "isWrapperFor(java.sql.Statement) returned false sb true"); 
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
		driverClass="com.ibm.db2.jdbc.app.DB2Statement"; 
	    } else if (isToolboxDriver()) {
            if(proxy==null)
                driverClass="com.ibm.as400.access.AS400JDBCStatement"; 
            else
                driverClass="java.sql.Statement"; //jdStatementProxy cannot cast to AS400jdbcstatement (purpose of wrapper)
 
	    } else {
		driverClass="UNKNOWNDRIVER"; 
	    } 
	    try {
		boolean answer = JDReflectionUtil.callMethod_B(stmt_, "isWrapperFor",  Class.forName(driverClass));
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
		Object answer = JDReflectionUtil.callMethod_O(stmt_, "unwrap",  (Class) null);
		assertCondition(false, "Exception should have been thrown unwrapping null "+answer ); 
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
		Object answer = JDReflectionUtil.callMethod_O(stmt_, "unwrap",  Class.forName("java.lang.String"));

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
		Object answer = JDReflectionUtil.callMethod_O(stmt_, "unwrap",  Class.forName("java.sql.Statement"));
		assertCondition(answer==stmt_, "unwrap(java.sql.Statement) returned "+answer+" sb "+stmt_); 
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
        String proxy = System.getProperty("com.ibm.as400.access.AS400.proxyServer");
	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
		driverClass="com.ibm.db2.jdbc.app.DB2Statement"; 
	    } else if (isToolboxDriver()) {
            if(proxy==null)
                driverClass="com.ibm.as400.access.AS400JDBCStatement"; 
            else
                driverClass="java.sql.Statement"; //jdStatementProxy cannot cast to AS400jdbcstatement (purpose of wrapper)
 
	    } else {
		driverClass="UNKNOWNDRIVER"; 
	    } 
	    try {
		Object answer = JDReflectionUtil.callMethod_O(stmt_, "unwrap",  Class.forName(driverClass));
		assertCondition(answer==stmt_, "unwrap("+driverClass+") returned "+answer+" sb "+stmt_); 
	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }








}



