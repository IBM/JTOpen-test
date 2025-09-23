///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCPDSConnectionNaming.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCPDSConnectionNaming.java
//
// Classes:      JDCPDSConnectionNaming
//
////////////////////////////////////////////////////////////////////////

package test.JD.CPDS;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import javax.sql.*; 
import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestcase;
import test.PasswordVault;



/**
Testcase JDCPDSConnectionNaming.  This tests the following
property with respect to the JDBC Connection class:

<ul>
<li>naming
</ul>
**/
public class JDCPDSConnectionNaming
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCPDSConnectionNaming";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCPDSTest.main(newArgs); 
   }

String clearPassword_; 

/**
Constructor.
**/
    public JDCPDSConnectionNaming (AS400 systemObject,
        Hashtable<String,Vector<String>> namesAndVars,
                                   int runMode,
                                   FileOutputStream fileOutputStream,
                                   
                                   String password)
    {
        super (systemObject, "JDCPDSConnectionNaming",
               namesAndVars, runMode, fileOutputStream,
               password);
	    clearPassword_ = PasswordVault.decryptPasswordLeak(encryptedPassword_);

    }



/**
naming - Does not specify naming but uses system naming.
An exception should be thrown.
**/
    public void Var001()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource)  (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();

                Statement s = c.createStatement ();
                s.executeQuery ("SELECT * FROM QIWS/QCUSTCDT");
                failed ("System naming accepted with sql naming set.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
naming - Does not specify naming and uses sql naming.
This should work.
**/
    public void Var002()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
naming - Specifies sql naming but uses system naming.
An exception should be thrown.
**/
    public void Var003()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setNamingOption","sql");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeQuery ("SELECT * FROM QIWS/QCUSTCDT");
                failed ("System naming accepted with sql naming set.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
naming - Specifies sql naming and uses sql naming.
This should work.
**/
    public void Var004()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setNamingOption","sql");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
naming - Specifies system naming but uses sql naming.
An exception should be thrown.
**/
    public void Var005()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setNamingOption","system");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                /* 09/09/2012 -- system now accepts system naming */
		
		    assertCondition(true); 
		
            }
            catch (Exception e) {
		/* 09/09/2012 -- System now accepts system naming */
		    failed(e, "Should not throw exception for naming mismatch in V7R2");  
		
            }
        }
    }



/**
naming - Specifies system naming and uses system naming.
This should work.
**/
    public void Var006()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setNamingOption","system");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeQuery ("SELECT * FROM QIWS/QCUSTCDT");
                succeeded ();
            }
            catch (Exception e) {
                failed(e,"Unexpected Exception");
            }
        }
    }



/**
naming - Specifies bogus naming but uses system naming.
An exception should be thrown.
**/
    public void Var007()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setNamingOption","bogus");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeQuery ("SELECT * FROM QIWS/QCUSTCDT");
                failed ("System naming accepted with bogus naming set.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
naming - Specifies bogus naming and uses sql naming.
This should work.
**/
/* DB2GenericDataSource.java (this contains the code for DataSource
implementation) checks to make sure that the naming option that
is specified is a valid one. If it is not, a SQLException is thrown */
    public void Var008()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setNamingOption","bogus");
                failed("Did not throw exception");
                /*
                Connection c = dataSource.getConnection ();
                 Statement s = c.createStatement ();
                 s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                 succeeded ();
                 */
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



}













