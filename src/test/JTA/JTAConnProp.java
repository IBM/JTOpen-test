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
package test.JTA;

import java.sql.*;
import javax.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JTATest;
import test.PasswordVault;
import test.JD.JDTestUtilities;


public class JTAConnProp extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAConnProp";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTATest.main(newArgs); 
   }

  boolean isIasp = false; 
  boolean isNTS = false; 

/**
Constructor.
**/
   public JTAConnProp (AS400 systemObject,
                       Hashtable namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password) {
      super (systemObject, "JTAConnProp",
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
       lockSystem("JTATEST",600);

      if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
         Connection c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
	 isIasp = JDTestUtilities.isIasp(c); 
	 c.close(); 
         JTATest.verboseOut(baseURL_);	
      }
   }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
   protected void cleanup ()
   throws Exception
   {
      if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
      }
      unlockSystem(); 
   }

   // Test changing the properties of a connection during
   // various stages of a transaction

   public void Var001() {
      assertCondition(true); // moved to JTAConnCommit.java
      // since there have been already been PTRs opened which include var numbers
      // I just decided to make this successful instead of changing the numbers
   }


   public void Var002() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection's trans iso level in the middle of a transaction");


               String table = JTATest.COLLECTION + ".CPROPVAR02";

               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);

               Statement s = conn.createStatement();

               try {
                  s.execute("DROP TABLE " + table);
               }
               catch (Exception e) {
               }
               s.execute("CREATE TABLE " + table + " (COLA INT)");
               conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	       if ( getRelease() <= JDTestDriver.RELEASE_V7R1M0 ) { 
		   failed("Did not throw exception");
	       } else {
		   succeeded(); 
	       } 
            }
            catch (SQLException e) {
	       if ( getRelease() <= JDTestDriver.RELEASE_V7R1M0 ) { 
		   succeeded();
	       } else {
		   failed(e, "Unexpected  Exception Message Should not throw exception in V5R3 ");
	       } 
            }
            catch (Exception e) {
               failed(e, "Incorrect Exception Message");
            }
         }
      }
   }


   public void Var003() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection's trans iso level for a transaction which had no work done");


               // String table = JTATest.COLLECTION + ".CPROPVAR03";

               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);

               conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
               assertCondition(true);
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }


   public void Var004() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection's trans iso level after committing (but not ending) a transaction");


               String table = JTATest.COLLECTION + ".CPROPVAR04";

               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);

               Statement s = conn.createStatement();

               try {
                  s.execute("DROP TABLE " + table);
               }
               catch (Exception e) {
               }
               s.execute("CREATE TABLE " + table + " (COLA INT)");
               JDReflectionUtil.callMethod_V(xaRes,"commit",Xid, true);
               conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
               failed("Did not throw exception");
            }
            catch (Exception e) {
               if (exceptionMsgHas(e, "XAER_PROTO"))
		   if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		       assertExceptionIsInstanceOf(e, "javax.transaction.xa.XAException");
		   } else { 
		       assertExceptionIsInstanceOf(e, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException");
		   }
               else
                  failed(e, "Incorrect Exception Message should have XAER_PROTO");
            }
         }
      }
   }


   public void Var005() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection's trans iso level after rolling back (and ending) a transaction");


               String table = JTATest.COLLECTION + ".CPROPVAR05";

               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);

               Statement s = conn.createStatement();

               try {
                  s.execute("DROP TABLE " + table);
               }
               catch (Exception e) {
               }
               s.execute("CREATE TABLE " + table + " (COLA INT)");
               JDReflectionUtil.callMethod_V(xaRes,"end",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
               JDReflectionUtil.callMethod_V(xaRes,"rollback",Xid);
               conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
               assertCondition(true);
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }


   public void Var006() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection's trans iso level after committing (and ending) a transaction");


               String table = JTATest.COLLECTION + ".CPROPVAR06";

               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);

               Statement s = conn.createStatement();

               try {
                  s.execute("DROP TABLE " + table);
               }
               catch (Exception e) {
               }
               s.execute("CREATE TABLE " + table + " (COLA INT)");
               JDReflectionUtil.callMethod_V(xaRes,"end",Xid, javax.transaction.xa.XAResource.TMSUCCESS);
               JDReflectionUtil.callMethod_V(xaRes,"commit",Xid, true);
               conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
               assertCondition(true);
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }


   public void Var007() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection's trans iso level after rolling back (and ending) a transaction");


               String table = JTATest.COLLECTION + ".CPROPVAR07";

               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);

               Statement s = conn.createStatement();

               try {
                  s.execute("DROP TABLE " + table);
               }
               catch (Exception e) {
               }
               s.execute("CREATE TABLE " + table + " (COLA INT)");
               JDReflectionUtil.callMethod_V(xaRes,"end",Xid, javax.transaction.xa.XAResource.TMSUCCESS);
               JDReflectionUtil.callMethod_V(xaRes,"rollback",Xid);
               conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
               assertCondition(true);
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }


   public void Var008() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Get a connection's default read-only property after starting a transaction");

               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               JTATest.verboseOut("Before starting a transaction, the connection's read-only prop=" + conn.isReadOnly());

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
               JTATest.verboseOut("After starting a transaction, the connection's read-only prop=" + conn.isReadOnly());
               assertCondition(true);
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }


   public void Var009() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection to read-only after starting a transaction");

               // String table = JTATest.COLLECTION + ".CPROPVAR09";
               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);

               conn.setReadOnly(true);
               assertCondition(true);
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }


   public void Var010() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection to read-only after starting and doing work in a transaction");

               String table = JTATest.COLLECTION + ".CPROPVAR10";
               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
               // do work
               Statement s = conn.createStatement();
               try {
                  s.execute("DROP TABLE " + table);
               }
               catch (Exception e) {
               }
               s.execute("CREATE TABLE " + table + " (COLA INT)");

               try {
                  conn.setReadOnly(true);
                  failed("Did not throw exception");
               }
               catch (Exception e) {
                  if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
                     if (exceptionMsgHas(e, "transaction was currently in progress"))
                        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                     else
                        failed(e, "Incorrect Exception Message");
                  }
                  else {
                     if (exceptionMsgHas(e, "Transaction state not valid"))
                        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                     else
                        failed(e, "Incorrect Exception Message");
                  }
               }
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }


   public void Var011() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection to read-only after starting a transaction");
               JTATest.verboseOut("TEST: Then try to CREATE using the connection");

               String table = JTATest.COLLECTION + ".CPROPVAR11";

               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
               conn.setReadOnly(true);
               Statement s = conn.createStatement();
               try {
                  s.execute("CREATE TABLE " + table + " (COLA INT)");
                  failed("Did not throw exception");
                  s.execute("DROP TABLE " + table); // cleanup
               }
               catch (Exception ex) {
                  if (JTATest.verbose) {
                     ex.printStackTrace();
                  }
                  assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
               }
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }


   public void Var012() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection to read-only before starting a transaction");
               JTATest.verboseOut("TEST: Then try to CREATE using the connection");

               String table = JTATest.COLLECTION + ".CPROPVAR12";

               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");
               conn.setReadOnly(true);

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
               Statement s = conn.createStatement();
               try {
                  s.execute("CREATE TABLE " + table + " (COLA INT)");
                  failed("Did not throw exception");
                  s.execute("DROP TABLE " + table); // cleanup
               }
               catch (Exception ex) {
                  if (JTATest.verbose) {
                     ex.printStackTrace();
                  }
                  assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
               }
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }

   public void Var013() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection to read-only after starting a transaction");
               JTATest.verboseOut("TEST: Then try to write using the connection, and commit");

               String table = JTATest.COLLECTION + ".CPROPVAR13";

               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
               conn.setReadOnly(true);
               Statement s = conn.createStatement();
               try {
                  s.execute("CREATE TABLE " + table + " (COLA INT)");
                  JDReflectionUtil.callMethod_V(xaRes,"end",Xid, javax.transaction.xa.XAResource.TMSUCCESS);
                  JDReflectionUtil.callMethod_V(xaRes,"commit",Xid, true);
                  failed("Did not throw exception");
                  s.execute("DROP TABLE " + table); // cleanup
                  conn.commit();
               }
               catch (Exception ex) {
                  if (JTATest.verbose) {
                     ex.printStackTrace();
                  }
                  assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
               }
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }


   public void Var014() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection to read-only before starting a transaction");
               JTATest.verboseOut("TEST: Then try to write using the connection and commit");

               String table = JTATest.COLLECTION + ".CPROPVAR14";

               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");
               conn.setReadOnly(true);

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
               Statement s = conn.createStatement();
               try {
                  s.execute("CREATE TABLE " + table + " (COLA INT)");
                  JDReflectionUtil.callMethod_V(xaRes,"end",Xid, javax.transaction.xa.XAResource.TMSUCCESS);
                  JDReflectionUtil.callMethod_V(xaRes,"commit",Xid, true);
                  failed("Did not throw exception");
                  s.execute("DROP TABLE " + table); // cleanup
                  conn.commit();
               }
               catch (Exception ex) {
                  if (JTATest.verbose) {
                     ex.printStackTrace();
                  }
                  assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
               }
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }

   public void Var015() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection to read-only in the data source itself");
               JTATest.verboseOut("TEST: and try to do writes under this connection");

               String table = JTATest.COLLECTION + ".CPROPVAR15";
               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               JDReflectionUtil.callMethod_V(ds,  "setAccess","read only");
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
               // do work
               Statement s = conn.createStatement();
               try {
                  s.execute("CREATE TABLE " + table + " (COLA INT)");
                  failed("Did not throw exception");
                  s.execute("DROP TABLE " + table); // cleanup
                  conn.commit();
               }
               catch (Exception e) {
                  if (exceptionMsgHas(e, "authorization failure"))
                     assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                  else
                     failed(e, "Incorrect Exception Message");
               }

            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }


   public void Var016() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection to read-only in the data source itself");
               JTATest.verboseOut("TEST: and try to do setReadOnly later");
	       JTATest.verboseOut("TEST: This will not throw an exception in V5R3"); 

               // String table = JTATest.COLLECTION + ".CPROPVAR16";
           XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               JDReflectionUtil.callMethod_V(ds,  "setAccess","read only");
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);

	       try {
		   conn.setReadOnly(true);
		   if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		       assertCondition(true); 
		   } else { 
		       failed("Did not throw exception");
		   }
	       }
               catch (Exception e) {
		   if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
			   failed(e, "Exception thrown calling setReadonly on readonly connection");
		   } else { 
		       if (exceptionMsgHas(e, "authorization failure"))
			   assertExceptionIsInstanceOf(e, "java.sql.SQLException");
		       else
			   failed(e, "Incorrect Exception Message");
		   }
               }

            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }


   public void Var017() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection to read-only after starting a transaction");
               JTATest.verboseOut("TEST: Then try to INSERT using the connection");

               String table = JTATest.COLLECTION + ".CPROPVAR17";

               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");
               Statement s1 = conn.createStatement();
               try {
                  s1.execute("DROP TABLE " + table);
                  s1.execute("CREATE TABLE " + table + " (COLA INT)");
               }
               catch (Exception e) {
               }
               conn.commit();

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
               conn.setReadOnly(true);
               Statement s = conn.createStatement();
               try {
                  s.execute("INSERT INTO " + table + " VALUES (17)");
                  failed("Did not throw exception");
                  s.execute("DROP TABLE " + table); // cleanup
               }
               catch (Exception e) {
                  if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
                     if (exceptionMsgHas(e, "read only connection"))
                        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                     else
                        failed(e, "Incorrect Exception Message");
                  }
                  else {
                     if (exceptionMsgHas(e, "Connection authorization failure"))
                        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                     else
                        failed(e, "Incorrect Exception Message");
                  }

               }
	       try {
		   conn.commit();
	       } catch (Exception e) {
		   System.out.println("Exception after test completion");
		   e.printStackTrace(); 
	       } 
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }


   public void Var018() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: Change a connection to read-only before starting a transaction");
               JTATest.verboseOut("TEST: Then try to INSERT using the connection");

               String table = JTATest.COLLECTION + ".CPROPVAR18";

               XADataSource          ds    =  (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,  "setDatabaseName",system_);
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_) );
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");
               Statement s1 = conn.createStatement();
               try {
                  s1.execute("DROP TABLE " + table);
                  s1.execute("CREATE TABLE " + table + " (COLA INT)");
               }
               catch (Exception e) {
               }
               conn.commit();
               conn.setReadOnly(true);

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
               Statement s = conn.createStatement();
               try {
                  s.execute("INSERT INTO " + table + " VALUES (17)");
                  failed("Did not throw exception");
                  s.execute("DROP TABLE " + table); // cleanup
               }
               catch (Exception e) {
                  if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
                     if (exceptionMsgHas(e, "read only connection"))
                        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                     else
                        failed(e, "Incorrect Exception Message");
                  }
                  else {
                     if (exceptionMsgHas(e, "Connection authorization failure"))
                        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                     else
                        failed(e, "Incorrect Exception Message");
                  }
               }
	       try { 
		   conn.commit();
	       } catch (Exception e) {
		   System.out.println("Exception after test completion");
		   e.printStackTrace(); 
	       } 
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }

}



