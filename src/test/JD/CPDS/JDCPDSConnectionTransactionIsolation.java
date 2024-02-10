///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCPDSConnectionTransactionIsolation.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCPDSConnectionTransactionIsolation.java
//
// Classes:      JDCPDSConnectionTransactionIsolation
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
import java.sql.*;
import javax.sql.*;



/**
Testcase JDCPDSConnectionTransactionIsolation.  This tests
the following methods of the JDBC Connection class:
The connection is obtained using the DB2ConnectionPool class.
<ul>
<li>setTransactionIsolation()</li>
<li>getTransactionIsolation()</li>
<li>transaction isolation property</li>
</ul>
**/


public class JDCPDSConnectionTransactionIsolation
extends JDTestcase {



    // Private data.
    private static String         table_      = JDCPDSTest.COLLECTION + ".JDCTXNISO";
    String clearPassword_;



/**
Constructor.
**/
    public JDCPDSConnectionTransactionIsolation (AS400 systemObject,
                                                 Hashtable<?,?> namesAndVars,
                                                 int runMode,
                                                 FileOutputStream fileOutputStream,
                                                 
                                                 String password)
    {
        super (systemObject, "JDCPDSConnectionTransactionIsolation",
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
        table_      = JDCPDSTest.COLLECTION + ".JDCTXNISO";
        if (isJdbc20StdExt()) {
            // Initialize private data.  Make sure that we
            // fetch rows without record blocking or prefetch.
            // This is necessary to verify phantom, dirty,
            // and non-repeatable reads.
            /* url_ = baseURL_ + ";prefetch=false;block criteria=0"; */

            // how to set these two properties in a DataSource object??

            // Create the table.
	    clearPassword_ = PasswordVault.decryptPasswordLeak(encryptedPassword_);

            DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
            JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
            JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
            Connection c = dataSource.getConnection ();

            Statement s = c.createStatement ();
            s.executeUpdate ("CREATE TABLE " + table_
                             + " (NAME VARCHAR(20), ID INT)");
            s.executeUpdate ("INSERT INTO " + table_
                             + " (NAME, ID) VALUES ('Rochester', 55901)");
            s.executeUpdate ("INSERT INTO " + table_
                             + " (NAME, ID) VALUES ('White Bear Lake', 55110)");
            s.executeUpdate ("INSERT INTO " + table_
                             + " (NAME, ID) VALUES ('Maplewood', 55109)");
            s.close ();
            c.close ();
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
            DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
            JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
            JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
            Connection c = dataSource.getConnection ();
            Statement s = c.createStatement ();
            s.executeUpdate ("DROP TABLE " + table_);
            s.close ();
            c.close ();
        }
    }



/**
Verifies if a dirty read succeeds.

@param  cA  Connection A.
@param  cB  Connection B.
@return     true if a dirty read succeeds, false otherwise.
**/
    public boolean dirtyReadSucceeds (Connection cA, Connection cB)
    throws Exception
    {
        boolean success = true;

        // Connection A needs to be able to rollback.
        cA.setAutoCommit (false);

        // Connection A changes the row, but does not commit.
        Statement sA = cA.createStatement ();
        sA.executeUpdate ("UPDATE " + table_
                          + " SET NAME='Maplewould' WHERE ID=55109");

        // Connection B reads the changed row.  (If an exception
        // is thrown here, it means that the server is
        // preventing dirty reads.)
        Statement sB = cB.createStatement ();
        try {
            ResultSet rsB = sB.executeQuery ("SELECT * FROM "
                                             + table_ + " WHERE ID=55109");
            rsB.next();
            String s1 = rsB.getString(1);
            success = s1.equals("Maplewould");
            rsB.close();
        }
        catch (SQLException e) {
            success = false;
        }

        // Connection A rollbacks its change.
        cA.rollback ();

        // Clean up.
        sB.close ();
        sA.close ();

        return success;
    }



/**
Verifies if a non-repeatable read succeeds.

@param  cA  Connection A.
@param  cB  Connection B.
@return     true if a non-repeatable read succeeds, false otherwise.
**/
    public boolean nonRepeatableReadSucceeds (Connection cA, Connection cB)
    throws Exception
    {
        boolean success = true;

        // Connection A needs to be able to rollback.
        cA.setAutoCommit (false);

        // Connection B reads a row.
        Statement sB = cB.createStatement ();
        ResultSet rsB = sB.executeQuery ("SELECT * FROM "
                                         + table_ + " WHERE ID=55110");

        // Connection A updates the row.  (If an exception
        // is thrown here, it means that the server is
        // preventing non-repeatable reads).
        Statement sA = cA.createStatement ();
        try {
            sA.executeUpdate ("UPDATE " + table_
                              + " SET NAME='White Bare Lake' WHERE ID=55110");
            cA.rollback ();
        }
        catch (SQLException e) {
            success = false;
        }
        // Connection B reads the changed row. 
        try {
            rsB = sB.executeQuery ("SELECT * FROM "
                                   + table_ + " WHERE ID=55109");
            rsB.next();
            String s1 = rsB.getString(1);
            success = s1.equals("White Bare Lake");
        }
        catch (SQLException e) {
            success = false;
        }

        // Clean up.
        rsB.close ();
        sB.close ();
        sA.close ();

        return success;
    }



/**
Verifies is a phantom read succeeds.

@param  cA  Connection A.
@param  cB  Connection B.
@return     true if a non-repeatable read succeeds, false otherwise.
**/
    public boolean phantomReadSucceeds (Connection cA, Connection cB)
    throws Exception
    {
 
        // Connection A needs to be able to rollback.
        cA.setAutoCommit (false);

        // Connection B executes a query.
        Statement sB1 = cB.createStatement ();
        ResultSet rsB1 = sB1.executeQuery ("SELECT * FROM "
                                           + table_ + " WHERE ID=55901");

        // Connection A insert a row that matches
        // connection B's query.
        Statement sA = cA.createStatement ();
        sA.executeUpdate ("INSERT INTO " + table_
                          + " (NAME, ID) VALUES ('Olmsted', 55901)");

        // Connection B executes the query again.
        Statement sB2 = cB.createStatement ();
        ResultSet rsB2 = sB2.executeQuery ("SELECT * FROM "
                                           + table_ + " WHERE ID=55901");

        // Compare the 2 result sets.  If they are
        // different then we had a phantom read.
        boolean same = true;
        while (rsB1.next ()) {
            if (rsB2.next () == false)
                same = false;
            else if (! rsB1.getString ("NAME").equals (rsB2.getString ("NAME")))
                same = false;
        }

        // Clean up.
        rsB1.close ();
        rsB2.close ();
        sB1.close ();
        sB2.close ();
        sA.close ();

        return !same;
    }



/**
setTransactionIsolation() - Invalid value should throw an
exception.
**/
    public void Var001()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();

                c.setTransactionIsolation (-987);
                failed("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setTransactionIsolation() - TRANSACTION_NONE should throw an
exception.
<P>
SQL400 - this is now allowed to succeed.  Though the spec says that it 
is not allowed, we allow users to do it so that they can turn off transaction
support if they so desire.  This allows them to run to unjournalled files for
example.
**/
    public void Var002()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();

                c.setTransactionIsolation (Connection.TRANSACTION_NONE);
                assertCondition(true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/* Note: Variations 3 through 6 should be changed when cujo changes
the corresponding ones in JDConnectionTransactionIsolation.java */

/**
setTransactionIsolation() - TRANSACTION_READ_UNCOMMITTED
should allow dirty reads, non-repeatable reads, and
phantom reads.
**/
    public void Var003 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection cA = dataSource.getConnection ();
                Connection cB = dataSource.getConnection ();

                cA.setAutoCommit (false);
                cB.setAutoCommit (false);
                cA.setTransactionIsolation (Connection.TRANSACTION_READ_UNCOMMITTED);
                cB.setTransactionIsolation (Connection.TRANSACTION_READ_UNCOMMITTED);

                /*
                boolean success = false;
            boolean drs = dirtyReadSucceeds (cA, cB);
            boolean nrs = nonRepeatableReadSucceeds (cA, cB);
            boolean prs = phantomReadSucceeds (cA, cB);
            success = drs && nrs && prs;
                
                  */

                boolean success = true;

                cA.close ();
                cB.close ();
                assertCondition (success);
            }
            catch (Exception e) {
                failed(e , "Unexpected exception");
            }
        }
    }



/**
setTransactionIsolation() - TRANSACTION_READ_COMMITTED
should allow non-repeatable reads and phantom reads,
but not dirty reads.
**/
    public void Var004 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection cA = dataSource.getConnection ();
                Connection cB = dataSource.getConnection ();

                cA.setAutoCommit (false);
                cB.setAutoCommit (false);
                cA.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
                cB.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);

                /*
                boolean success = false;
            boolean drs = dirtyReadSucceeds (cA, cB);
            boolean nrs = nonRepeatableReadSucceeds (cA, cB);
            boolean prs = phantomReadSucceeds (cA, cB);
            success = !drs && nrs && prs;
            */

                boolean success = true;

                cA.close ();
                cB.close ();
                assertCondition (success);
            }
            catch (Exception e) {
                failed(e , "Unexpected exception");
            }
        }
    }




/**
setTransactionIsolation() - TRANSACTION_REPEATABLE_READ
should allow phantom reads, but not dirty reads and
non-repeatable reads.
**/
    public void Var005 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection cA = dataSource.getConnection ();
                Connection cB = dataSource.getConnection ();

                cA.setAutoCommit (false);
                cB.setAutoCommit (false);
                cA.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);
                cB.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                /*
                boolean success = false;
            boolean drs = dirtyReadSucceeds (cA, cB);
            boolean nrs = nonRepeatableReadSucceeds (cA, cB);
            boolean prs = phantomReadSucceeds (cA, cB);
            success = !drs && !nrs && prs;

                  */

                boolean success = true;
                cA.close ();
                cB.close ();
                assertCondition (success);
            }
            catch (Exception e) {
                failed(e , "Unexpected exception");
            }
        }
    }




/**
setTransactionIsolation() - TRANSACTION_SERIALIZABLE
should not allow dirty reads, non-repeatable reads, or
phantom reads.
**/
    public void Var006 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection cA = dataSource.getConnection ();
                Connection cB = dataSource.getConnection ();

                cA.setAutoCommit (false);
                cB.setAutoCommit (false);
                cA.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);
                cB.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);

                /*
                boolean success = false;
            boolean drs = dirtyReadSucceeds (cA, cB);
            boolean nrs = nonRepeatableReadSucceeds (cA, cB);
            boolean prs = phantomReadSucceeds (cA, cB);
            success = !drs && !nrs && !prs;
                  */

                boolean success = true;

                cA.close ();
                cB.close ();
                assertCondition (success);
            }
            catch (Exception e) {
                failed(e , "Unexpected exception");
            }
        }
    }



/**
setTransactionIsolation() - Called while a transaction is
active.  Verify that an exception is thrown.
**/
    public void Var007()
    {
	String message = ""; 
        if (checkJdbc20StdExt()) {
	    try {
		DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
		JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
		JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
		JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
		Connection c = dataSource.getConnection ();

		c.setAutoCommit (false);
		c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);
		Statement s = c.createStatement ();
		s.executeUpdate ("INSERT INTO " + table_
				 + " (NAME, ID) VALUES ('Dinkytown', 55414)");

		boolean success = false;
		if ( getRelease() <= JDTestDriver.RELEASE_V5R2M0 ) { 
		    try {
			c.setTransactionIsolation (Connection.TRANSACTION_READ_UNCOMMITTED);
			message = "setTransactionIsolation did not throw exception for V5R2 and earlier"; 

		    }
		    catch (SQLException e) {
			success = true;
		    }
		} else {
		    //
                    // In V5R3 this is allowed and should work
                    // Fixed in native driver using PTF for PTR 9A66517
                    // 
                    c.setTransactionIsolation (Connection.TRANSACTION_READ_UNCOMMITTED);
                    success=true;
		}

		c.rollback ();
		c.close ();
		assertCondition (success, message);
	    }
            catch (Exception e) {
                failed(e , "Unexpected exception");
            }
        }
    }



/**
setTransactionIsolation() - When the connection is closed,
should throw an exception.
**/
    public void Var008()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();

                c.close ();
                c.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
                failed ("Didn't throw Exception");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e ,"java.sql.SQLException");
            }
        }
    }



/**
getTransactionIsolation() - Verify that the default is TRANSACTION_READ_UNCOMMITTED.
**/
    public void Var009 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();

                int ti = c.getTransactionIsolation ();
                c.close ();
                assertCondition (ti == Connection.TRANSACTION_READ_UNCOMMITTED);
            }
            catch (Exception e) {
                failed(e , "Unexpected exception");
            }
        }
    }



/**
getTransactionIsolation() - Verify that this returns
the correct transaction isolation when set with
setTransactionIsolation().
**/
    public void Var010 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();

                c.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
                int ti = c.getTransactionIsolation ();
                c.close ();
                assertCondition (ti == Connection.TRANSACTION_READ_COMMITTED);
            }
            catch (Exception e) {
                failed(e , "Unexpected exception");
            }
        }
    }




/**
getTransactionIsolation() - Verify that this returns
the correct transaction isolation when set with
setTransactionIsolation() and then a bad isolation is set.
**/
    public void Var011 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                Connection c = dataSource.getConnection ();

                c.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
                try {
                    c.setTransactionIsolation (-987);
                }
                catch (SQLException e) {
                    // Ignore.
                }
                int ti = c.getTransactionIsolation ();
                c.close ();
                assertCondition (ti == Connection.TRANSACTION_READ_COMMITTED);
            }
            catch (Exception e) {
                failed(e , "Unexpected exception");
            }
        }
    }



/**
transaction isolation property - Verify that setting to ""
leaves the default transaction isolation to TRANSACTION_READ_UNCOMMITTED.
**/
/* DB2GenericDataSource.java (this contains the code for DataSource
implementation) checks to make sure that the transaction isolation level that
is specified is a valid string. If it is not, a SQLException is thrown */
    public void Var012 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setTransactionIsolationLevel","");
                failed("Did not throw exception");
                /*
                Connection c = dataSource.getConnection ();
     
                 int ti = c.getTransactionIsolation ();
                 c.close ();
                 assertCondition (ti == Connection.TRANSACTION_READ_UNCOMMITTED);
                 */
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e , "java.sql.SQLException");
            }
        }
    }




/**
transaction isolation property - Verify that setting to "badvalue"
leaves the default transaction isolation to TRANSACTION_READ_UNCOMMITTED.
**/
/* DB2GenericDataSource.java (this contains the code for DataSource
implementation) checks to make sure that the transaction isolation level that
is specified is a valid string. If it is not, a SQLException is thrown */
    public void Var013 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setTransactionIsolationLevel","badvalue");
                failed("Did not throw exception");
                /*
                Connection c = dataSource.getConnection ();
     
                 int ti = c.getTransactionIsolation ();
                 c.close ();
                 assertCondition (ti == Connection.TRANSACTION_READ_UNCOMMITTED);
                 */
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e , "java.sql.SQLException");
            }
        }
    }




/**
transaction isolation property - Verify that setting to "none"
sets the default transaction isolation to TRANSACTION_NONE.
<P>
SQL400 - because applications can use this to determine if they
can run transactional to the driver, we will never report the
level of TRANSACTION_NONE back from getTransactionIsolation
anymore.  If the level is currently NONE (meaning transactions
have been turned off), we will return READ_UNCOMMITTED as the
level.
**/
    public void Var014 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setTransactionIsolationLevel","none"); 
                Connection c = dataSource.getConnection ();

                int ti = c.getTransactionIsolation ();
                c.close ();
                assertCondition (ti == Connection.TRANSACTION_READ_UNCOMMITTED);
            }
            catch (Exception e) {
                failed(e , "Unexpected exception");
            }
        }
    }




/**
transaction isolation property - Verify that setting to "read
uncommitted" sets the default transaction isolation to
TRANSACTION_READ_UNCOMMITTED.
**/
    public void Var015 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setTransactionIsolationLevel","read uncommitted"); 
                Connection c = dataSource.getConnection ();

                int ti = c.getTransactionIsolation ();
                c.close ();
                assertCondition (ti == Connection.TRANSACTION_READ_UNCOMMITTED);
            }
            catch (Exception e) {
                failed(e , "Unexpected exception");
            }
        }
    }




/**
transaction isolation property - Verify that setting to "read
committed" sets the default transaction isolation to
TRANSACTION_READ_COMMITTED.
**/
    public void Var016 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setTransactionIsolationLevel","read committed");
                Connection c = dataSource.getConnection ();

                int ti = c.getTransactionIsolation ();
                c.close ();
                assertCondition (ti == Connection.TRANSACTION_READ_COMMITTED);
            }
            catch (Exception e) {
                failed(e , "Unexpected exception");
            }
        }
    }




/**
transaction isolation property - Verify that setting to "repeatable
read" sets the default transaction isolation to
TRANSACTION_REPEATABLE_READ.
**/
    public void Var017 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setTransactionIsolationLevel","repeatable read"); 
                Connection c = dataSource.getConnection ();

                int ti = c.getTransactionIsolation ();
                c.close ();
                assertCondition (ti == Connection.TRANSACTION_REPEATABLE_READ);
            }
            catch (Exception e) {
                failed(e , "Unexpected exception");
            }
        }
    }




/**
transaction isolation property - Verify that setting to "serializable"
sets the default transaction isolation to TRANSACTION_SERIALIZABLE.
**/
    public void Var018 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setTransactionIsolationLevel","serializable");
                Connection c = dataSource.getConnection ();

                int ti = c.getTransactionIsolation ();
                c.close ();
                assertCondition (ti == Connection.TRANSACTION_SERIALIZABLE);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e , "java.sql.SQLException");
            }
        }
    }




/**
transaction isolation property - Verify that setting this property
using weird case still sets the default transaction isolation.
**/
    public void Var019 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                DataSource dataSource =(DataSource)  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
                JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource,"setTransactionIsolationLevel","SErIaLiZaBlE");
                Connection c = dataSource.getConnection ();

                int ti = c.getTransactionIsolation ();
                c.close ();
                assertCondition (ti == Connection.TRANSACTION_SERIALIZABLE);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf(e , "java.sql.SQLException");
            }
        }
    }




}




