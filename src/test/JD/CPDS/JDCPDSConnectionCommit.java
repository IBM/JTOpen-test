///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCPDSConnectionCommit.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCPDSConnectionCommit.java
//
// Classes:      JDCPDSConnectionCommit
//
////////////////////////////////////////////////////////////////////////

package test.JD.CPDS;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Hashtable;

import javax.sql.DataSource;

import com.ibm.as400.access.AS400;

import test.JDCPDSTest;
import test.JDReflectionUtil;
import test.JDTestcase;
import test.PasswordVault;



/**
Testcase JDCPDSConnectionCommit.  This tests the following methods
of the JDBC Connection class:
The connection is obtained by using DB2ConnectionPool class.
<ul>
<li>commit()
<li>rollback()
<li>setAutoCommit()
<li>getAutoCommit()
</ul>
**/
public class JDCPDSConnectionCommit
extends JDTestcase {



    // Private data.
    private static String         table_      = JDCPDSTest.COLLECTION + ".JDCCOMMIT";
    private DataSource db2ConnectionPoolDataSource;
    private Connection     connection_;



/**
Constructor.
**/
    public JDCPDSConnectionCommit (AS400 systemObject,
                                   Hashtable<?,?> namesAndVars,
                                   int runMode,
                                   FileOutputStream fileOutputStream,
                                   
                                   String password)
    {
        super (systemObject, "JDCPDSConnectionCommit",
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
        table_      = JDCPDSTest.COLLECTION + ".JDCCOMMIT";
        if (isJdbc20StdExt()) {
            // Initialize the connection.
            db2ConnectionPoolDataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPool");
            JDReflectionUtil.callMethod_V(db2ConnectionPoolDataSource,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(db2ConnectionPoolDataSource,"setUser",userId_);
            try { 
	    char[] password = PasswordVault.decryptPassword(encryptedPassword_);
            JDReflectionUtil.callMethod_V(db2ConnectionPoolDataSource,"setPassword",password);
	    Arrays.fill(password,'\0');
            } catch (Exception e) { 
              String password = PasswordVault.decryptPasswordLeak(encryptedPassword_);
              JDReflectionUtil.callMethod_V(db2ConnectionPoolDataSource,"setPassword",password);
            }

            connection_ = db2ConnectionPoolDataSource.getConnection ();

            connection_.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
            connection_.setAutoCommit (false);

            // Create the table.
            Statement s = connection_.createStatement ();
            s.executeUpdate ("CREATE TABLE " + table_ + " (NAME VARCHAR(10))");
            s.close ();
            connection_.commit ();
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
            connection_.commit ();

            // Close the connection.
            connection_.close ();
        }
    }



/**
Checks that the result set contains a row.

@param name     The name.
@return         true if the result set contains the row, false otherwise.
**/
    private boolean checkForRow (String name)
    throws SQLException
    {
        Statement s = connection_.createStatement ();
        ResultSet rs = s.executeQuery ("SELECT * FROM " + table_);
        boolean found = false;
        while (rs.next ()) {
            if (rs.getString ("NAME").equals (name))
                found = true;
        }
        rs.close ();
        s.close ();
        return found;
    }



/**
setAutoCommit()/getAutoCommit() - Verify that autocommit is turned on by default.
**/
    public void Var001()
    {
        if (checkJdbc20StdExt()) {
            try {
                // Create a connection with default autocommit.
                Connection c2 = db2ConnectionPoolDataSource.getConnection ();
                boolean autoCommit = c2.getAutoCommit ();
                Statement s2 = c2.createStatement ();
                s2.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('default')");

                // Verify changes were committed.
                boolean found = checkForRow ("default");

                // Close the connection.
                c2.close ();

                assertCondition (found && autoCommit);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
setAutoCommit()/getAutoCommit() - Turn on autocommit.  Insert a value and then
verify it was committed.
**/
    public void Var002()
    {
        if (checkJdbc20StdExt()) {
            try {
                // Create a connection with autocommit on.
                Connection c2 = db2ConnectionPoolDataSource.getConnection ();
                c2.setAutoCommit (true);
                boolean autoCommit = c2.getAutoCommit ();
                Statement s2 = c2.createStatement ();
                s2.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('enabled')");

                // Verify changes were committed.
                boolean found = checkForRow ("enabled");

                // Close the connection.
                c2.close ();

                assertCondition (found && autoCommit);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
setAutoCommit()/getAutoCommit() - Turn off autocommit.  Insert a value and
then verify it was not committed.
**/
    public void Var003()
    {
        if (checkJdbc20StdExt()) {
            try {
                // Create a connection with autocommit off.
                Connection c2 = db2ConnectionPoolDataSource.getConnection ();
                c2.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
                c2.setAutoCommit (false);
                boolean autoCommit = c2.getAutoCommit ();
                Statement s2 = c2.createStatement ();
                s2.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('disabled')");
                c2.rollback ();

                // Verify changes were committed.
                boolean found = checkForRow ("disabled");

                // Close the connection.
                c2.close ();

                assertCondition (!found  && !autoCommit);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
setAutoCommit() - Verify that setting autocommit on a closed
connection throws an exception.
**/
    public void Var004()
    {
        if (checkJdbc20StdExt()) {
            try {
                Connection c2 = db2ConnectionPoolDataSource.getConnection ();
                c2.close ();
                c2.setAutoCommit (false);
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getAutoCommit() - Verify that getting autocommit on a closed
connection throws an exception.
**/
    public void Var005()
    {
        if (checkJdbc20StdExt()) {
            try {
                Connection c2 = db2ConnectionPoolDataSource.getConnection ();
                c2.close ();
                c2.getAutoCommit ();
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
rollback() - Insert twice into a table and then call rollback().
Verify that table exists but previous inserts were rolled back.
**/
    public void Var006()
    {
        if (checkJdbc20StdExt()) {
            try {
                // Perform 2 inserts that we can rollback.
                Statement s = connection_.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('ibm')");
                s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('bigblue')");

                // Verify changes occured before rolling back.
                boolean found1Before = checkForRow ("ibm");
                boolean found2Before = checkForRow ("bigblue");

                // Perform a rollback and verify that insertions have
                // been erased.
                connection_.rollback ();
                boolean found1After = checkForRow ("ibm");
                boolean found2After = checkForRow ("bigblue");

                assertCondition (found1Before && found2Before && !found1After && !found2After);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
commit(), rollback() - Insert into table, call commit().
Insert into table again, call rollback().  Verify that only committed
changes are reflected.
**/
    public void Var007()
    {
        if (checkJdbc20StdExt()) {
            try {
                // Insert into table and commit.
                Statement s = connection_.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('commit')");
                connection_.commit ();

                // Insert again and but don't commit.
                s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('rollback')");

                // Verify changes occured before rolling back.
                boolean found1Before = checkForRow ("commit");
                boolean found2Before = checkForRow ("rollback");

                // Perform a rollback and verify that only one insertion
                // remains.
                connection_.rollback ();
                boolean found1After = checkForRow ("commit");
                boolean found2After = checkForRow ("rollback");

                assertCondition (found1Before && found2Before && found1After && !found2After);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
commit(), rollback() - Verify that when a connection is closed
uncommited transactions are rolled back.
**/
    public void Var008()
    {
        if (checkJdbc20StdExt()) {
            try {
                // need to have a Connection Event Listener
                // Refer to JAVA PTR 9900535
                notApplicable();
                /*
                // Turn off auto-commit.
                Connection c2 = dataSource.getConnection ();
                c2.setAutoCommit (false);
                c2.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

                // Insert a row, but don't commit.
                Statement s = c2.createStatement ();
                s.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('josh')");

                // Close the connection.
                c2.close ();

                // Verify changes are rolled back when the connection
                // was closed.
                boolean foundAfter = checkForRow ("josh");

                assertCondition (!foundAfter);
                */
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
commit()/setAutocommit() - Verify that commit has no effect when autocommit is on.
**/
    public void Var009()
    {
        if (checkJdbc20StdExt()) {
            try {
                // Turn on auto-commit.
                Connection c2 = db2ConnectionPoolDataSource.getConnection ();
                c2.setAutoCommit (true);

                // Insert a row and commit.
                Statement s2 = connection_.createStatement();
                s2.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('Hola')");
                c2.commit();

                // Verify that the row is there.
                boolean foundAfter = checkForRow ("Hola");

                assertCondition (foundAfter);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }




/**
rollback()/setAutocommit( - Verify that rollback has no effect when autocommit is on.
**/
    public void Var010()
    {
        if (checkJdbc20StdExt()) {
            try {
                // Turn on auto-commit.
                Connection c2 = db2ConnectionPoolDataSource.getConnection ();
                c2.setAutoCommit (true);

                // Insert a row and commit.
                Statement s2 = connection_.createStatement();
                s2.executeUpdate ("INSERT INTO " + table_ + " (NAME) VALUES('Adios')");
                c2.rollback();

                // Verify that the row is there.
                boolean foundAfter = checkForRow ("Adios");

                assertCondition (foundAfter);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
commit() - Verify that committing a closed connection throws an exception.
**/
    public void Var011()
    {
        if (checkJdbc20StdExt()) {
            try {
                Connection c2 = db2ConnectionPoolDataSource.getConnection ();
                c2.close ();
                c2.commit ();
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
rollback() - Verify that rolling back a closed connection throws an exception.
**/
    public void Var012()
    {
        if (checkJdbc20StdExt()) {
            try {
                Connection c2 = db2ConnectionPoolDataSource.getConnection ();
                c2.close ();
                c2.rollback ();
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
rollback() - Insert twice into a table using a batch update and then 
call rollback().  Verify that table exists but previous inserts 
were rolled back.
**/
    public void Var013()
    {
        if (checkJdbc20StdExt()) {
            try {
                // Perform 2 inserts that we can rollback.
                Statement s =  connection_.createStatement ();
                s.addBatch ("INSERT INTO " + table_ + " (NAME) VALUES('Helium')");
                s.addBatch ("INSERT INTO " + table_ + " (NAME) VALUES('Oxygen')");
                s.executeBatch ();

                // Verify changes occured before rolling back.
                boolean found1Before = checkForRow ("Helium");
                boolean found2Before = checkForRow ("Oxygen");

                // Perform a rollback and verify that insertions have
                // been erased.
                connection_.rollback ();
                boolean found1After = checkForRow ("Helium");
                boolean found2After = checkForRow ("Oxygen");

                assertCondition (found1Before && found2Before && !found1After && !found2After);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
rollback() - Insert twice into a table using a result set insert and then 
call rollback().  Verify that table exists but previous inserts 
were rolled back.

SQL400 - Until we figure out anything different, the native driver expects you
         will close your statements to ensure that locks in the database will
         get released correctly.
**/
    public void Var014()
    {
        notApplicable();
        /* Not applicable because with the DB2ConnectionHandle we have there
        is no createStatement with the following signature 
        if(checkJdbc20StdExt()) { try {
            // Perform 2 inserts that we can rollback.
            Statement s = connection_.createStatement(DB2ResultSet.TYPE_SCROLL_SENSITIVE,
                                                       DB2ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + table_ + " FOR UPDATE");
            rs.moveToInsertRow ();
            rs.updateString ("NAME", "Hydrogen");
            rs.insertRow ();
            rs.updateString ("NAME", "Nitrogen");
            rs.insertRow ();
            rs.close ();

            // Verify changes occured before rolling back.
            boolean found1Before = checkForRow ("Hydrogen");
            boolean found2Before = checkForRow ("Nitrogen");

            // Perform a rollback and verify that insertions have
            // been erased.
            connection_.rollback ();
            boolean found1After = checkForRow ("Hydrogen");
            boolean found2After = checkForRow ("Nitrogen");

            // SQL400 - this line added to remove locks being held.
            s.close();

            assertCondition (found1Before && found2Before && !found1After && !found2After);
        }
        catch(Exception e) {
            failed(e, "Unexpected Exception");
        }
        */
    }



}



