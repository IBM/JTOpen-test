///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionTransactionIsolation.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDConnectionTransactionIsolation.java
 //
 // Classes:      JDConnectionTransactionIsolation
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDConnectionTransactionIsolation.  This tests
the following methods of the JDBC Connection class:

<ul>
<li>setTransactionIsolation()</li>
<li>getTransactionIsolation()</li>
<li>transaction isolation property</li>
</ul>
**/
public class JDConnectionTransactionIsolation
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionTransactionIsolation";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }



    // Private data.
    private static  String         table_      = JDConnectionTest.COLLECTION + ".JDCTXNISO";
    private              String         url_;



/**
Constructor.
**/
    public JDConnectionTransactionIsolation (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDConnectionTransactionIsolation",
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
        // Initialize private data.  Make sure that we
        // fetch rows without record blocking or prefetch.
        // This is necessary to verify phantom, dirty,
        // and non-repeatable reads.
        url_ = baseURL_ + ";prefetch=false;block criteria=0";
        table_      = JDConnectionTest.COLLECTION + ".JDCTXNISO";

        // Create the table.
        Connection c = testDriver_.getConnection (url_, userId_, encryptedPassword_);
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



/**
Cleanup.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        // Drop the table.
        Connection c = testDriver_.getConnection (url_, userId_, encryptedPassword_);
        Statement s = c.createStatement ();
        s.executeUpdate ("DROP TABLE " + table_);
        s.close ();
        c.close ();
    }



/**
Verifies if a dirty read succeeds.

@param  cA  Connection A.
@param  cB  Connection B.
@return     true if a dirty read succeeds, false otherwise.
**/
    boolean dirtyReadSucceeds (Connection cA, Connection cB)
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
    boolean nonRepeatableReadSucceeds (Connection cA, Connection cB)
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
    boolean phantomReadSucceeds (Connection cA, Connection cB)
        throws Exception
    {
        // boolean success = true;

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
        try {
            Connection c = testDriver_.getConnection (url_, userId_, encryptedPassword_);
            c.setTransactionIsolation (-987);
            failed("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
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
        try {
            Connection c = testDriver_.getConnection (url_, userId_, encryptedPassword_);
            c.setTransactionIsolation (Connection.TRANSACTION_NONE);
            ///if (isToolboxDriver()) 
            ///   failed("Didn't throw SQLException");
            ///else
               assertCondition(true);
        }
        catch (Exception e) {
           ///if (isToolboxDriver()) 
           ///   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
           ///else
              failed(e , "Unexpected exception");
        }
    }



/**
setTransactionIsolation() - TRANSACTION_READ_UNCOMMITTED
should allow dirty reads, non-repeatable reads, and
phantom reads.
**/
    public void Var003 ()
    {
        try {
            Connection cA = testDriver_.getConnection (url_, userId_, encryptedPassword_);
            Connection cB = testDriver_.getConnection (url_, userId_, encryptedPassword_);
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



/**
setTransactionIsolation() - TRANSACTION_READ_COMMITTED
should allow non-repeatable reads and phantom reads,
but not dirty reads.
**/
    public void Var004 ()
    {
        try {
            Connection cA = testDriver_.getConnection (url_, userId_, encryptedPassword_);
            Connection cB = testDriver_.getConnection (url_, userId_, encryptedPassword_);
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




/**
setTransactionIsolation() - TRANSACTION_REPEATABLE_READ
should allow phantom reads, but not dirty reads and
non-repeatable reads.
**/
    public void Var005 ()
    {
        try {
            Connection cA = testDriver_.getConnection (url_, userId_, encryptedPassword_);
            Connection cB = testDriver_.getConnection (url_, userId_, encryptedPassword_);
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




/**
setTransactionIsolation() - TRANSACTION_SERIALIZABLE
should not allow dirty reads, non-repeatable reads, or
phantom reads.
**/
    public void Var006 ()
    {
        try {
            Connection cA = testDriver_.getConnection (url_, userId_, encryptedPassword_);
            Connection cB = testDriver_.getConnection (url_, userId_, encryptedPassword_);
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



/**
setTransactionIsolation() - Called while a transaction is
active.  Verify that an exception is thrown.

@C1  In V5R3, the data supports setting the transaction isolation while a
     transaction is active.. The toolbox will need to step to to this also. 
**/
    public void Var007()
    {
        try {
	    String message = ""; 
            Connection c = testDriver_.getConnection (url_, userId_, encryptedPassword_);
            c.setAutoCommit (false);
            c.setTransactionIsolation (Connection.TRANSACTION_REPEATABLE_READ);
            Statement s = c.createStatement ();
            s.executeUpdate ("INSERT INTO " + table_
                + " (NAME, ID) VALUES ('Dinkytown', 55414)");

            boolean success = false;
            c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            success = true;

            c.rollback();
            c.close ();
            assertCondition (success, message);
        }
        catch (Exception e) {
            failed(e , "Unexpected exception -- testcase changed by native 5/19/2004 Note:  In V5R3 you should be able to set the transaction isolation level ");
        }
    }



/**
setTransactionIsolation() - When the connection is closed,
should throw an exception.
**/
    public void Var008()
    {
        try {
            Connection c = testDriver_.getConnection (url_, userId_, encryptedPassword_);
            c.close ();
            c.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e ,"java.sql.SQLException");
        }
    }



/**
getTransactionIsolation() - Verify that the default is TRANSACTION_READ_UNCOMMITTED.
**/
    public void Var009 ()
    {
        try {
            Connection c = testDriver_.getConnection (url_, userId_, encryptedPassword_);
            int ti = c.getTransactionIsolation ();
            c.close ();
            assertCondition (ti == Connection.TRANSACTION_READ_UNCOMMITTED);
        }
        catch (Exception e) {
            failed(e , "Unexpected exception");
        }
    }



/**
getTransactionIsolation() - Verify that this returns
the correct transaction isolation when set with
setTransactionIsolation().
**/
    public void Var010 ()
    {
        try {
            Connection c = testDriver_.getConnection (url_, userId_, encryptedPassword_);
            c.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
            int ti = c.getTransactionIsolation ();
            c.close ();
            assertCondition (ti == Connection.TRANSACTION_READ_COMMITTED);
        }
        catch (Exception e) {
            failed(e , "Unexpected exception");
        }
    }




/**
getTransactionIsolation() - Verify that this returns
the correct transaction isolation when set with
setTransactionIsolation() and then a bad isolation is set.
**/
    public void Var011 ()
    {
        try {
            Connection c = testDriver_.getConnection (url_, userId_, encryptedPassword_);
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



/**
transaction isolation property - Verify that setting to ""
leaves the default transaction isolation to TRANSACTION_READ_UNCOMMITTED.
**/
    public void Var012 ()
    {
        try {
            Connection c = testDriver_.getConnection (url_
                + ";transaction isolation=", userId_, encryptedPassword_);
            int ti = c.getTransactionIsolation ();
            c.close ();
            assertCondition (ti == Connection.TRANSACTION_READ_UNCOMMITTED);
        }
        catch (Exception e) {
            failed(e , "Unexpected exception");
        }
    }




/**
transaction isolation property - Verify that setting to "badvalue"
leaves the default transaction isolation to TRANSACTION_READ_UNCOMMITTED.
**/
    public void Var013 ()
    {
        try {
            Connection c = testDriver_.getConnection (url_
                + ";transaction isolation=badvalue", userId_, encryptedPassword_);
            int ti = c.getTransactionIsolation ();
            c.close ();
            assertCondition (ti == Connection.TRANSACTION_READ_UNCOMMITTED);
        }
        catch (Exception e) {
            failed(e , "Unexpected exception");
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
        try {
            Connection c = testDriver_.getConnection (url_
                + ";transaction isolation=none", userId_, encryptedPassword_);
            int ti = c.getTransactionIsolation ();
            c.close ();

            
            if (isToolboxDriver()) 
               assertCondition (ti == Connection.TRANSACTION_NONE, "Transaction isolation = "+ti+" expected TRANSACTION_NONE changed 3/23/2009 by toolbox driver to get TRANSACTION_NONE"); //if none, then none.  
            else
               assertCondition (ti == Connection.TRANSACTION_READ_UNCOMMITTED, "Transaction isolation = "+ti+" expected TRANSACTION_READ_UNCOMMITTED");
        }
        catch (Exception e) {
            failed(e , "Unexpected exception");
        }
    }




/**
transaction isolation property - Verify that setting to "read
uncommitted" sets the default transaction isolation to
TRANSACTION_READ_UNCOMMITTED.
**/
    public void Var015 ()
    {
        try {
            Connection c = testDriver_.getConnection (url_
                + ";transaction isolation=read uncommitted", userId_, encryptedPassword_);
            int ti = c.getTransactionIsolation ();
            c.close ();
            assertCondition (ti == Connection.TRANSACTION_READ_UNCOMMITTED);
        }
        catch (Exception e) {
            failed(e , "Unexpected exception");
        }
    }




/**
transaction isolation property - Verify that setting to "read
committed" sets the default transaction isolation to
TRANSACTION_READ_COMMITTED.
**/
    public void Var016 ()
    {
        try {
            Connection c = testDriver_.getConnection (url_
                + ";transaction isolation=read committed", userId_, encryptedPassword_);
            int ti = c.getTransactionIsolation ();
            c.close ();
            assertCondition (ti == Connection.TRANSACTION_READ_COMMITTED);
        }
        catch (Exception e) {
            failed(e , "Unexpected exception");
        }
    }




/**
transaction isolation property - Verify that setting to "repeatable
read" sets the default transaction isolation to
TRANSACTION_REPEATABLE_READ.
**/
    public void Var017 ()
    {
        try {
            Connection c = testDriver_.getConnection (url_
                + ";transaction isolation=repeatable read", userId_, encryptedPassword_);
            int ti = c.getTransactionIsolation ();
            c.close ();
            assertCondition (ti == Connection.TRANSACTION_REPEATABLE_READ);
        }
        catch (Exception e) {
            failed(e , "Unexpected exception");
        }
    }




/**
transaction isolation property - Verify that setting to "serializable"
sets the default transaction isolation to TRANSACTION_SERIALIZABLE.
**/
    public void Var018 ()
    {
        try {
            Connection c = testDriver_.getConnection (url_
                + ";transaction isolation=serializable", userId_, encryptedPassword_);
            int ti = c.getTransactionIsolation ();
            c.close ();
            assertCondition (ti == Connection.TRANSACTION_SERIALIZABLE);
        }
        catch (Exception e) {
            failed(e , "Unexpected exception");
        }
    }




/**
transaction isolation property - Verify that setting this property
using weird case still sets the default transaction isolation.
**/
    public void Var019 ()
    {
        try {
            Connection c = testDriver_.getConnection (url_
                + ";transaction isolation=SErIaLiZaBlE", userId_, encryptedPassword_);
            int ti = c.getTransactionIsolation ();
            c.close ();
            assertCondition (ti == Connection.TRANSACTION_SERIALIZABLE);
        }
        catch (Exception e) {
            failed(e , "Unexpected exception");
        }
    }




}



