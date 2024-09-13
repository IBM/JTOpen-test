///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCPDSConnectionWarnings.java
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
// File Name:    JDCPDSConnectionWarnings.java
//
// Classes:      JDCPDSConnectionWarnings
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.CPDS;

import com.ibm.as400.access.AS400;

import test.JDCPDSTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;
import java.sql.*;
import javax.sql.*; 


/**
Testcase JDCPDSConnectionWarnings.  This tests the following methods
of the JDBC Connection class for a connection obtained through a
ConnectionPool DataSource

<ul>
<li>clearWarnings()
<li>getWarning()
</ul>
**/
public class JDCPDSConnectionWarnings
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCPDSConnectionWarnings";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCPDSTest.main(newArgs); 
   }



    // Private data.
    private static String         table_ = JDCPDSTest.COLLECTION + ".JDCWARN";

    private              Connection     connection_;
    private DataSource dataSource;


/**
Constructor.
**/
    public JDCPDSConnectionWarnings (AS400 systemObject,
                                     Hashtable<String,Vector<String>> namesAndVars,
                                     int runMode,
                                     FileOutputStream fileOutputStream,
                                     
                                     String password)
    {
        super (systemObject, "JDCPDSConnectionWarnings",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
Setup.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
        table_ = JDCPDSTest.COLLECTION + ".JDCWARN";
        if (isJdbc20StdExt()) {

	    String clearPassword = PasswordVault.decryptPasswordLeak(encryptedPassword_);

            // Create the table.
            dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");

            JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
            JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword);
            connection_ = dataSource.getConnection ();
            Statement s = connection_.createStatement ();
            s.executeUpdate ("CREATE TABLE " + table_ + " (NAME VARCHAR(10))");
            s.close ();
        }
    }



/**
Cleanup.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        if (isJdbc20StdExt()) {
            // Drop the table.
            Statement s = connection_.createStatement ();
            s.executeUpdate ("DROP TABLE " + table_);
            s.close ();
            connection_.close ();
        }
    }



/**
Forces a single warning to be posted to the connection.

@param c The connection.

@exception Exception If an exception occurs.
**/
    public void forceWarning (Connection c)
    throws Exception
    {
        if (isJdbc20StdExt()) {
	    if (  getRelease()  >= JDTestDriver.RELEASE_V7R1M0) {
		// In V5R5, we not longer cause a warning when switching autocommit
		// For another warning another way.
		JDReflectionUtil.callMethod_V(c, "setClientInfo", "Bogus", "x"); 

	    } else {

		c.setAutoCommit (false);
		c.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);

		Statement s = c.createStatement ();
		s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES ('MURCH')");
		s.close ();

	        // This forces a warning that the transaction was
	        // committed.
		c.setAutoCommit (true);
	    }
        }
    }



/**
clearWarnings() - Has no effect when there are no
warnings.
**/
    public void Var001()
    {
        if (checkJdbc20StdExt()) {
            try {
                connection_.clearWarnings ();
                connection_.clearWarnings ();
                SQLWarning w = connection_.getWarnings ();
                assertCondition (w == null);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
clearWarnings() - Clears warnings after 1 has been posted.
**/
    public void Var002()
    {
        if (checkJdbc20StdExt()) {
            try {
                forceWarning (connection_);
                SQLWarning w1 = connection_.getWarnings ();
                connection_.clearWarnings ();
                SQLWarning w2 = connection_.getWarnings ();
                assertCondition ((w1 != null) && (w2 == null), "w1="+w1+" sb != null\nw2="+w2+"sb null");
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
clearWarnings() - Clears warnings when a connection is
closed.
**/
    public void Var003()
    {
        if (checkJdbc20StdExt()) {
            try {
                Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
                forceWarning (c);
                c.close ();
                SQLWarning w1 = c.getWarnings ();
                c.clearWarnings ();
                SQLWarning w2 = c.getWarnings ();
                assertCondition ((w1 != null) && (w2 == null));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
getWarning() - Returns null if no warnings have been
reported.
**/
    public void Var004()
    {
        if (checkJdbc20StdExt()) {
            try {
                connection_.clearWarnings ();
                SQLWarning w = connection_.getWarnings();
                assertCondition (w == null);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
getWarning() - Returns the first warning when 1 warning has been
reported.
**/
    public void Var005()
    {
        if (checkJdbc20StdExt()) {
            try {
                connection_.clearWarnings ();
                forceWarning (connection_);
                SQLWarning w1 = connection_.getWarnings ();
                SQLWarning w2 = w1.getNextWarning ();
                assertCondition ((w1 != null) && (w2 == null));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
getWarning() - Returns the first warning when 2 warnings have been
reported.
**/
    public void Var006()
    {
        if (checkJdbc20StdExt()) {
            try {
                connection_.clearWarnings ();
                forceWarning (connection_);
                forceWarning (connection_);
                SQLWarning w1 = connection_.getWarnings ();
                SQLWarning w2 = w1.getNextWarning ();
                SQLWarning w3 = w2.getNextWarning ();
                assertCondition ((w1 != null) && (w2 != null) && (w3 == null));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }




/**
getWarning() - Returns the first warning even after the connection
is closed.

SQL400 - The DB2ConnectionHandle object (which is the connection
object when using pooled connections) is invalidated when
the connection is closed. So getWarnings throws exception
**/
    public void Var007()
    {
        if (checkJdbc20StdExt()) {
            try {
                Connection c = dataSource.getConnection ();
                forceWarning (c);
                c.close ();
                SQLWarning w = c.getWarnings ();
                if (getDriver() != JDTestDriver.DRIVER_NATIVE)
                    assertCondition (w != null);
                else
                    failed("Did not throw exception");
            }
            catch (Exception e) {
                if (getDriver() != JDTestDriver.DRIVER_NATIVE)
                    failed(e, "Unexpected Exception");
                else
                    assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }



}



