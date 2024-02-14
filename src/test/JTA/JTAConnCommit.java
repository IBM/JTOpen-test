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
import java.util.*;

import javax.sql.XADataSource;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JTATest;
import test.PasswordVault;
import test.JD.JDTestUtilities;

public class JTAConnCommit extends JDTestcase {

  boolean isIasp = false; 
  boolean isNTS = false; 
/**
Constructor.
**/
   public JTAConnCommit (AS400 systemObject,
                         Hashtable namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password) {
      super (systemObject, "JTAConnCommit",
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
         JTATest.verboseOut(baseURL_);
         Connection c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
	 isIasp = JDTestUtilities.isIasp(c);
	 c.close(); 
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

   // Test changing the commit property of a connection during
   // various stages of a transaction

   public void Var001() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            Object         xaConn = null;
            try {
               JTATest.verboseOut("TEST: Change a connection's auto-commit property in the middle of a transaction");


               String table = JTATest.COLLECTION + ".CCOMMVAR01";

               XADataSource          ds    = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
               xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
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
               try {
                  conn.setAutoCommit(true); // default is false
                  failed("Did not throw exception");
               }
               catch (Exception e) {
                  JTATest.verboseOut(e.getMessage());
                  assertExceptionIsInstanceOf(e, "java.sql.SQLException");
               }

            }
            catch (Exception e) {
               failed(e, "Unexpected exception");
            }
            finally {
               if (xaConn != null) {
                  try {
                     JDReflectionUtil.callMethod_V(xaConn,"close");
                  }
                  catch (Exception e) {
                  }

               }
            }
         }
      }
   }

   public void Var002() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            Object         xaConn = null;
            try {
               JTATest.verboseOut("TEST: Change a connection's auto-commit property in a local transaction");

               String table = JTATest.COLLECTION + ".CCOMMVAR02";
               XADataSource          ds    = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
               xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               // Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               Statement s = conn.createStatement();

               try {
                  s.execute("DROP TABLE " + table);
               }
               catch (Exception e) {
               }
               s.execute("CREATE TABLE " + table + " (COLA INT)");

               // since this is a local transaction, it should behave like normal JDBC.
               // [though this is not entirely true since this connection has
               // auto commit set to false (by default) because that is the default
               // in which the connection from a XADataSource is created]
               // normal JDBC means that we should be able to set the auto-commit value.

               conn.setAutoCommit(true); // default is false
               conn.setAutoCommit(false);
               assertCondition(true);
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
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
            Object         xaConn = null;
            try {
               JTATest.verboseOut("TEST: Set a connection's auto-commit property in the middle of a transaction");


               String table = JTATest.COLLECTION + ".CCOMMVAR03";

               XADataSource          ds    = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
               xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
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
               JTATest.verboseOut("CREATE TABLE " + table + " (COLA INT)");
               s.execute("CREATE TABLE " + table + " (COLA INT)");

               conn.setAutoCommit(false); // default is false
               JDReflectionUtil.callMethod_V(xaRes,"end",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
               JDReflectionUtil.callMethod_V(xaRes,"rollback",Xid);
               assertCondition(true);
            }
            catch (Exception e) {
               failed(e, "Unexpected exception");
            }
            finally {
               if (xaConn != null) {
                  try {
                     JDReflectionUtil.callMethod_V(xaConn,"close");
                  }
                  catch (Exception e) {
                  }

               }
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
            Object         xaConn = null;
            try {
               JTATest.verboseOut("TEST: Set a xa connection's auto-commit property to false and then start a transaction");

               String table = JTATest.COLLECTION + ".CCOMMVAR04";

               XADataSource          ds    = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
               xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");
               conn.setAutoCommit(false); // default is false

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
               JTATest.verboseOut("CREATE TABLE " + table + " (COLA INT)");
               s.execute("CREATE TABLE " + table + " (COLA INT)");

               JDReflectionUtil.callMethod_V(xaRes,"end",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
               JDReflectionUtil.callMethod_V(xaRes,"rollback",Xid);
               assertCondition(true);
            }
            catch (Exception e) {
               failed(e, "Unexpected exception");
            }
            finally {
               if (xaConn != null) {
                  try {
                     JDReflectionUtil.callMethod_V(xaConn,"close");
                  }
                  catch (Exception e) {
                  }

               }
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
            Object         xaConn = null;
            try {
               JTATest.verboseOut("TEST: Set a xa connection's auto-commit property to true and then start a transaction");
               JTATest.verboseOut("TEST: Do work. Do xaRes rollback. Make sure it took effect");

               String table = JTATest.COLLECTION + ".CCOMMVAR05";

               XADataSource          ds    = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");

               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
               xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");
               conn.setAutoCommit(true); // default is false

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
               JTATest.verboseOut("auto commit=" + conn.getAutoCommit());
               JTATest.verboseOut("CREATE TABLE " + table + " (COLA INT)");
               s.execute("CREATE TABLE " + table + " (COLA INT)");

               JDReflectionUtil.callMethod_V(xaRes,"end",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
               JDReflectionUtil.callMethod_V(xaRes,"rollback",Xid);
               JTATest.verboseOut("auto commit=" + conn.getAutoCommit());

               // now check to make sure that the rollback worked
               try {
                  s.execute("DROP TABLE " + table);
                  failed("Did not throw exception");
               }
               catch (Exception e) {
                  String msg = e.getMessage();
                  JTATest.verboseOut(msg);

		  //
		  // Execute an explicit commit on the connection to prevent
                  // problem documented in issue 20318
		  //
		  conn.commit(); 

                  assertExceptionIsInstanceOf(e, "java.sql.SQLException");

               }

            }
            catch (Exception e) {
               failed(e, "Unexpected exception");
            }
            finally {
               if (xaConn != null) {
                  try {
                     JDReflectionUtil.callMethod_V(xaConn,"close");
                  }
                  catch (Exception e) {
                  }

               }
            }
         }
      }
   }


   public void Var006() {
      String description = "TEST: Set a xa connection's auto-commit property to true and then start a transaction\n"+
	"TEST: Do work. Do conn.rollback(). it should fail";
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            Object         xaConn = null;
	    JTATestXid              Xid = null;
	    Object xaRes =  null;
	    boolean xaResStarted = false; 
            try {
               JTATest.verboseOut(description); 

               String table = JTATest.COLLECTION + ".CCOMMVAR06";

               XADataSource          ds    = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
               xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");
               conn.setAutoCommit(true); // default is false

               // Generate a new transaction
               Xid = new JTATestXid();

               // start the transaction
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
	       xaResStarted=true; 

               Statement s = conn.createStatement();

               try {
                  s.execute("DROP TABLE " + table);
               }
               catch (Exception e) {
               }
               JTATest.verboseOut("auto commit=" + conn.getAutoCommit());
               JTATest.verboseOut("CREATE TABLE " + table + " (COLA INT)");
               s.execute("CREATE TABLE " + table + " (COLA INT)");

               JDReflectionUtil.callMethod_V(xaRes,"end",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
               JTATest.verboseOut("auto commit=" + conn.getAutoCommit());

               try {
                  conn.rollback(); // this is like doing work on a ended tx.
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
                     failed(e, "Incorrect Exception Message (NO XAER_PROTO) "+description );
               }

            }
            catch (Exception e) {
               failed(e, "Unexpected exception");
            }
            finally {
		if (xaRes != null) {
		    try {
			if (xaResStarted) { 
			    JDReflectionUtil.callMethod_V(xaRes,"rollback",Xid);
			}
		    } catch (Exception e) {
			System.out.println("Exception caught during cleanup ");
			e.printStackTrace(); 
		    } 
		}	
               if (xaConn != null) {
                  try {

                     JDReflectionUtil.callMethod_V(xaConn,"close");
                  }
                  catch (Throwable e) {
		      System.out.println("Throwable caught during cleanup ");
		      e.printStackTrace(); 
                  }

               }

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
            Object         xaConn = null;
            try {
               JTATest.verboseOut("TEST: Change a connection's auto-commit property after ending a transaction");

               String table = JTATest.COLLECTION + ".CCOMMVAR07";

               XADataSource          ds    = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
               xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
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

               try {
                  conn.setAutoCommit(true); // default is false
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
                     failed(e, "Incorrect Exception Message");
               }
               JTATest.verboseOut("rollback");
               JDReflectionUtil.callMethod_V(xaRes,"rollback",Xid);

            }
            catch (Exception e) {
               failed(e, "Unexpected exception");
            }
            finally {
               if (xaConn != null) {
                  try {
                     JDReflectionUtil.callMethod_V(xaConn,"close");
                  }
                  catch (Exception e) {
                  }

               }
            }
         }
      }
   }




}
