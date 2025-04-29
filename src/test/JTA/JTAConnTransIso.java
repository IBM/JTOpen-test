///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
// Based on JDConnectionTransactionIsolation.java

package test.JTA;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import javax.sql.XADataSource;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestcase;
import test.JTATest;
import test.JD.JDTestUtilities;


public class JTAConnTransIso
      extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAConnTransIso";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTATest.main(newArgs); 
   }
   // Private data.
   private String         table_      = JTATest.COLLECTION + ".CONTXISO";
   private              String         url_;

   boolean isIasp = false; 
   boolean isNTS = false; 
/**
Constructor.
**/
   public JTAConnTransIso (AS400 systemObject,
                           Hashtable<String,Vector<String>> namesAndVars,
                           int runMode,
                           FileOutputStream fileOutputStream,
                           
                           String password) {
      super (systemObject, "JTAConnTransIso",
             namesAndVars, runMode, fileOutputStream,
             password);
   }

/**
setupForVar.

@exception Exception If an exception occurs.
**/
   protected void setupForVar ()
   throws Exception
   {
      if (isJdbc20StdExt ()) {
         // Initialize private data.  Make sure that we
         // fetch rows without record blocking or prefetch.
         // This is necessary to verify phantom, dirty,
         // and non-repeatable reads.
         url_ = baseURL_ + ";prefetch=false;block criteria=0;blocking enabled=false";

         // Create the table.
         Connection c = testDriver_.getConnection (url_, userId_, encryptedPassword_);
         Statement s = c.createStatement ();
         try {
            s.executeUpdate ("DROP TABLE " + table_);
         }
         catch (Exception e) {
         }
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


   protected void setup ()
   throws Exception
   {

       Connection c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
       isIasp = JDTestUtilities.isIasp(c); 
       c.commit();
       lockSystem("JTATEST",600);

       
   }


/**
Cleanup.

@exception Exception If an exception occurs.
**/
   protected void cleanup ()
   throws Exception
   {
      if (isJdbc20StdExt ()) {
         // Drop the table.
         Connection c = testDriver_.getConnection (url_, userId_, encryptedPassword_);
         Statement s = c.createStatement ();
         try {
            s.executeUpdate ("DROP TABLE " + table_);
         }
         catch (Exception e) {
         }
         s.close ();
         c.close ();
      }
      unlockSystem(); 
   }



/**
Verifies if a dirty read succeeds.

@param  cA  Connection A.
@param  cB  Connection B.
@return     true if a dirty read succeeds, false otherwise.
**/
   private boolean dirtyReadSucceeds (Connection cA, Connection cB)
   throws Exception
   {
      JTATest.verboseOut("Checking for dirty read");
      boolean success = true;

      String s = JTATest.getStr();

      // Connection A needs to be able to rollback.
      cA.setAutoCommit (false);

      JTATest.verboseOut("Connection A updates a row but does not commit");
      // Connection A changes the row, but does not commit.
      Statement sA = cA.createStatement ();
      sA.executeUpdate ("UPDATE " + table_
                        + " SET NAME='" + s + "' WHERE ID=55109");

      JTATest.verboseOut("Connection B reads the row");
      // Connection B reads the changed row.  (If an exception
      // is thrown here, it means that the server is
      // preventing dirty reads.)
      Statement sB = cB.createStatement ();
      ResultSet rsB;
      try {
         rsB = sB.executeQuery ("SELECT * FROM "
                                + table_ + " WHERE ID=55109");
         rsB.next();
         String s1 = rsB.getString(1);
         success = s1.equals(s);
         rsB.close();
      }
      catch (SQLException e) {
         success = false;
      }

      JTATest.verboseOut("Connection A rollbacks its change");
      // Connection A rollbacks its change.
      cA.rollback ();

      JTATest.verboseOut("Close s");
      // Clean up.
      sB.close ();
      sA.close ();

      JTATest.verboseOut("dirty read=" + success);
      return success;
   }



/**
Verifies if a non-repeatable read succeeds.

@param  cA  Connection A.
@param  cB  Connection B.
@return     true if a non-repeatable read succeeds, false otherwise.
**/
   private boolean nonRepeatableReadSucceeds (Connection cA, Connection cB)
   throws Exception
   {
      JTATest.verboseOut("Checking for non-repeatable read");
      boolean success = true;

      // Connection A needs to be able to rollback.
      cA.setAutoCommit (false);
      String s = JTATest.getStr();

      JTATest.verboseOut("Connection B reads a row");
      // Connection B reads a row.
      Statement sB = cB.createStatement ();
      ResultSet rsB = sB.executeQuery ("SELECT * FROM "
                                       + table_ + " WHERE ID=55110");

      JTATest.verboseOut("Connection A updates the row");
      // Connection A updates the row.  (If an exception
      // is thrown here, it means that the server is
      // preventing non-repeatable reads).
      Statement sA = cA.createStatement ();
      try {
         sA.executeUpdate ("UPDATE " + table_
                           + " SET NAME='" + s + "' WHERE ID=55110");
         cA.commit ();
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
         success = s1.equals(s);
      }
      catch (SQLException e) {
         success = false;
      }

      JTATest.verboseOut("Close rs,s");
      // Clean up.
      rsB.close ();
      sB.close ();
      sA.close ();

      JTATest.verboseOut("non-repeatable read=" + success);
      return success;
   }



/**
Verifies is a phantom read succeeds.

@param  cA  Connection A.
@param  cB  Connection B.
@return     true if a phantom read succeeds, false otherwise.
**/
   private boolean phantomReadSucceeds (Connection cA, Connection cB)
   throws Exception
   {
      JTATest.verboseOut("Checking for phantom read");
      // boolean success = true;

      // Connection A needs to be able to rollback.
      cA.setAutoCommit (false);


      JTATest.verboseOut("Connection B does a query");
      // Connection B executes a query.
      Statement sB1 = cB.createStatement ();
      ResultSet rsB1 = sB1.executeQuery ("SELECT * FROM "
                                         + table_ + " WHERE ID=55901");

      JTATest.verboseOut("Connection A inserts a row that matches B's query");
      // Connection A insert a row that matches
      // connection B's query.
      Statement sA = cA.createStatement ();
      sA.executeUpdate ("INSERT INTO " + table_
                        + " (NAME, ID) VALUES ('Olmsted', 55901)");

      JTATest.verboseOut("Connection B does the query again");
      // Connection B executes the query again.
      Statement sB2 = cB.createStatement ();
      ResultSet rsB2 = sB2.executeQuery ("SELECT * FROM "
                                         + table_ + " WHERE ID=55901");


      JTATest.verboseOut("Comparing the two result sets");
      // Compare the 2 result sets.  If they are
      // different then we had a phantom read.
      boolean same = true;
      try {
         while (rsB1.next ()) {
            if (rsB2.next () == false)
               same = false;
            else if (! rsB1.getString ("NAME").equals (rsB2.getString ("NAME")))
               same = false;
         }
      }
      catch (Exception e) {
         // Likely a timeout exception, we will cause
         // false to be returned, the phantom read didn't happen.
         same = true;
      }

      JTATest.verboseOut("Close rs, s");
      // Clean up.
      rsB1.close ();
      rsB2.close ();
      sB1.close ();
      sB2.close ();
      sA.close ();

      JTATest.verboseOut("phantom read=" + !same);
      return !same;
   }


   protected Connection crtConn() throws Exception {
	   
      XADataSource          ds    = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
      JDReflectionUtil.callMethod_V(ds,"setDatabaseName", system_); 
      JDReflectionUtil.callMethod_V(ds,"setUseBlocking", false); 
      Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
      // Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
      Connection              c   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");
      return c;
   }






/* In Vars 1,2,3,4 we are not starting a global transaction */


/**
setTransactionIsolation() - TRANSACTION_READ_UNCOMMITTED
should allow dirty reads, non-repeatable reads, and
phantom reads.
**/
   public void Var001 () {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }

      if (checkJdbc20StdExt()) {
         try {

            JTATest.verboseOut("TEST: TRANSACTION_READ_UNCOMMITTED should allow");
            JTATest.verboseOut("TEST: dirty , non-repeatable and phantom reads");

            // setupForVar
            table_      = JTATest.COLLECTION + ".CONTXISO01";
            setupForVar();

            JTATest.verboseOut("Get the XA connections");
            Connection cA1 = crtConn();
            Connection cB1 = crtConn();

            JTATest.verboseOut("Set the TX ISO Levels to READ_UNCOMMITTED");
            cA1.setTransactionIsolation (Connection.TRANSACTION_READ_UNCOMMITTED);
            cB1.setTransactionIsolation (Connection.TRANSACTION_READ_UNCOMMITTED);

            boolean success = true;
            if (dirtyReadSucceeds (cA1, cB1)) {
               JTATest.verboseOut("Dirty Read allowed, as expected");
            }
            else {
               JTATest.verboseOut("(Minor)Failure. Dirty Read not allowed");
               success = false;
            }
            cA1.close ();
            cB1.close ();

            JTATest.verboseOut("Get the XA connections");
            Connection cA2 = crtConn();
            Connection cB2 = crtConn();

            JTATest.verboseOut("Set the TX ISO Levels to READ_UNCOMMITTED");
            cA2.setTransactionIsolation (Connection.TRANSACTION_READ_UNCOMMITTED);
            cB2.setTransactionIsolation (Connection.TRANSACTION_READ_UNCOMMITTED);

            if (nonRepeatableReadSucceeds (cA2, cB2)) {
               JTATest.verboseOut("Nonrepeatable Read allowed, as expected");
            }
            else {
               JTATest.verboseOut("(Minor)Failure. Nonrepeatable Read not allowed");
               success = false;
            }
            cA2.close ();
            cB2.close ();

            JTATest.verboseOut("Get the XA connections");
            Connection cA3 = crtConn();
            Connection cB3 = crtConn();

            JTATest.verboseOut("Set the TX ISO Levels to READ_UNCOMMITTED");
            cA3.setTransactionIsolation (Connection.TRANSACTION_READ_UNCOMMITTED);
            cB3.setTransactionIsolation (Connection.TRANSACTION_READ_UNCOMMITTED);

            if (phantomReadSucceeds (cA3, cB3)) {
               JTATest.verboseOut("Phantom Read allowed, as expected");
            }
            else {
               JTATest.verboseOut("(Minor)Failure. Phantom Read not allowed");
               success = false;
            }
            cA3.rollback();
            cB3.rollback();
            cA3.close();
            cB3.close();

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
   public void Var002 () {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
             notApplicable();
             /*
            JTATest.verboseOut("TEST: TRANSACTION_READ_COMMITTED should allow");
            JTATest.verboseOut("TEST: non-repeatable and phantom reads but not dirty read");

            // setupForVar
            table_      = JTATest.COLLECTION + ".CONTXISO02";
            setupForVar();

            JTATest.verboseOut("Get the XA connections");
            Connection cA1 = crtConn();
            Connection cB1 = crtConn();

            JTATest.verboseOut("Set the TX ISO Levels to READ_COMMITTED");
            cA1.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
            cB1.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);

            boolean success = true;
            if (!dirtyReadSucceeds (cA1, cB1)) {
               JTATest.verboseOut("Dirty Read not allowed, as expected");
            }
            else {
               JTATest.verboseOut("Failure. Dirty Read allowed");
               success = false;
            }
            cA1.close ();
            cB1.close ();

            JTATest.verboseOut("Get the XA connections");
            Connection cA2 = crtConn();
            Connection cB2 = crtConn();

            JTATest.verboseOut("Set the TX ISO Levels to READ_COMMITTED");
            cA2.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
            cB2.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);

            if (nonRepeatableReadSucceeds (cA2, cB2)) {
               JTATest.verboseOut("Nonrepeatable Read allowed, as expected");
            }
            else {
               JTATest.verboseOut("(Minor)Failure. Nonrepeatable Read not allowed");
               success = false;
            }
            cA1.rollback();
            cB2.rollback();
            cA2.close ();
            cB2.close ();

            JTATest.verboseOut("Get the XA connections");
            Connection cA3 = crtConn();
            Connection cB3 = crtConn();

            JTATest.verboseOut("Set the TX ISO Levels to READ_COMMITTED");
            cA3.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
            cB3.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);

            if (phantomReadSucceeds (cA3, cB3)) {
               JTATest.verboseOut("Phantom Read allowed, as expected");
            }
            else {
               JTATest.verboseOut("(Minor)Failure. Phantom Read not allowed");
               success = false;
            }
            cA3.rollback();
            cB3.rollback();
            cA3.close ();
            cB3.close ();

            assertCondition (success);
              */
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
   public void Var003 () {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
             notApplicable();
             /*
            JTATest.verboseOut("TEST: TRANSACTION_REPEATABLE_READ should allow");
            JTATest.verboseOut("TEST: phantom reads but not non-repeatable and dirty read");

            // setupForVar
            table_      = JTATest.COLLECTION + ".CONTXISO03";
            setupForVar();

            JTATest.verboseOut("Get the XA connections");
            Connection cA1 = crtConn();
            Connection cB1 = crtConn();

            JTATest.verboseOut("Set the TX ISO Levels to READ_COMMITTED");
            cA1.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
            cB1.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);

            boolean success = true;
            if (!dirtyReadSucceeds (cA1, cB1)) {
               JTATest.verboseOut("Dirty Read not allowed, as expected");
            }
            else {
               JTATest.verboseOut("Failure. Dirty Read allowed");
               success = false;
            }
            cA1.close ();
            cB1.close ();

            JTATest.verboseOut("Get the XA connections");
            Connection cA2 = crtConn();
            Connection cB2 = crtConn();

            JTATest.verboseOut("Set the TX ISO Levels to READ_COMMITTED");
            cA2.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
            cB2.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);

            if (!nonRepeatableReadSucceeds (cA2, cB2)) {
               JTATest.verboseOut("Nonrepeatable Read not allowed, as expected");
            }
            else {
               JTATest.verboseOut("Failure. Nonrepeatable Read allowed");
               success = false;
            }
            cA2.rollback();
            cB2.rollback();
            cA2.close ();
            cB2.close ();

            JTATest.verboseOut("Get the XA connections");
            Connection cA3 = crtConn();
            Connection cB3 = crtConn();

            JTATest.verboseOut("Set the TX ISO Levels to READ_COMMITTED");
            cA3.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
            cB3.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);

            if (phantomReadSucceeds (cA3, cB3)) {
               JTATest.verboseOut("Phantom Read allowed, as expected");
            }
            else {
               JTATest.verboseOut("(Minor)Failure. Phantom Read not allowed");
               success = false;
            }
            cA3.rollback();
            cB3.rollback();
            cA3.close ();
            cB3.close ();
            assertCondition (success);
         */
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
   public void Var004 () {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
             notApplicable();
             /*
            JTATest.verboseOut("TEST: TRANSACTION_SERIALIZABLE should NOT allow");
            JTATest.verboseOut("TEST: phantom , non-repeatable or dirty read");

            // setupForVar
            table_      = JTATest.COLLECTION + ".CONTXISO04";
            setupForVar();

            JTATest.verboseOut("Get the XA connections");
            Connection cA1 = crtConn();
            Connection cB1 = crtConn();

            JTATest.verboseOut("Set the TX ISO Levels to READ_COMMITTED");
            cA1.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
            cB1.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);

            boolean success = true;
            if (!dirtyReadSucceeds (cA1, cB1)) {
               JTATest.verboseOut("Dirty Read not allowed, as expected");
            }
            else {
               JTATest.verboseOut("Failure. Dirty Read allowed");
               success = false;
            }
            cA1.close ();
            cB1.close ();

            JTATest.verboseOut("Get the XA connections");
            Connection cA2 = crtConn();
            Connection cB2 = crtConn();

            JTATest.verboseOut("Set the TX ISO Levels to READ_COMMITTED");
            cA2.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
            cB2.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);

            if (!nonRepeatableReadSucceeds (cA2, cB2)) {
               JTATest.verboseOut("Nonrepeatable Read not allowed, as expected");
            }
            else {
               JTATest.verboseOut("Failure. Nonrepeatable Read allowed");
               success = false;
            }
            cA2.rollback();
            cB2.rollback();
            cA2.close ();
            cB2.close ();

            JTATest.verboseOut("Get the XA connections");
            Connection cA3 = crtConn();
            Connection cB3 = crtConn();

            JTATest.verboseOut("Set the TX ISO Levels to READ_COMMITTED");
            cA3.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);
            cB3.setTransactionIsolation (Connection.TRANSACTION_READ_COMMITTED);

            if (!phantomReadSucceeds (cA3, cB3)) {
               JTATest.verboseOut("Phantom Read not allowed, as expected");
            }
            else {
               JTATest.verboseOut("Failure. Phantom Read allowed");
               success = false;
            }
            cA3.rollback();
            cB3.rollback();
            cA3.close ();
            cB3.close ();
            assertCondition (success);
      */   
         }
         catch (Exception e) {
            failed(e , "Unexpected exception");
         }
      }
      
   }

}



