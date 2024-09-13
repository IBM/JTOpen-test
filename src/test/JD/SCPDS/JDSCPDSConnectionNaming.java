///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSCPDSConnectionNaming.java
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
// File Name:    JDSCPDSConnectionNaming.java
//
// Classes:      JDSCPDSConnectionNaming
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.SCPDS;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestcase;
import test.PasswordVault;

import javax.sql.DataSource;

/**
Testcase JDSCPDSConnectionNaming.  This tests the following
property with respect to the JDBC Connection class:

<ul>
<li>naming
</ul>
**/
public class JDSCPDSConnectionNaming
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDSCPDSConnectionNaming";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDSCPDSTest.main(newArgs); 
   }



/**
Constructor.
**/
    public JDSCPDSConnectionNaming (AS400 systemObject,
                                   Hashtable namesAndVars,
                                   int runMode,
                                   FileOutputStream fileOutputStream,
                                   
                                   String password)
    {
        super (systemObject, "JDSCPDSConnectionNaming",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
naming - Does not specify naming but uses system naming.
An exception should be thrown.
**/
    public void Var001()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
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
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
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
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
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
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
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
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
                JDReflectionUtil.callMethod_V(dataSource,"setNamingOption","system");
                Connection c = dataSource.getConnection ();
                Statement s = c.createStatement ();
                s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
                failed ("SQL naming accepted with system naming set.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
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
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
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
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdConnectionPool");
               JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
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
                DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdConnectionPool");
               JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
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













