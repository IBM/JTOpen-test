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

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JTATest;
import test.PasswordVault;
import test.JD.JDTestUtilities;

import javax.sql.XADataSource;

public class JTATransOrder extends JDTestcase {

   private String basTbl = JTATest.COLLECTION + ".XAORDER";
   private Connection c;
   boolean isIasp = false; 
   boolean isNTS = false; 

//   private Connection gc;

/**
Constructor.
**/
   public JTATransOrder (AS400 systemObject,
                         Hashtable namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password) {
      super (systemObject, "JTATransOrder",
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
       basTbl = JTATest.COLLECTION + ".XAORDER";
       if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
         JTATest.verboseOut(baseURL_);
         c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
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


   /* TO Test:
    Transaction order tests - i.e. change the order of
    doing transaction steps.
   */



   public void Var001() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: end() without a start() ");


            JTATest.verboseOut("Get the connection");
            javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            JTATest.verboseOut("Get the Xid but do not start a transaction");
            JTATestXid newXid = new JTATestXid();

            String table = basTbl + "001";
            Statement s = conn.createStatement();
            s.execute("CREATE TABLE " + table + " (COL1 INT)");


            JTATest.verboseOut("Try to end() the unstarted transaction");
            // try to end the unstarted transaction
            try {
               JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
               failed("Did not throw exception");
            }
            catch (Exception ex) {
		try { 
		    int errorCode =JDReflectionUtil.getField_I(ex,"errorCode"); 
		    if (errorCode == javax.transaction.xa.XAException.XAER_PROTO)
			if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
			    assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
			} else { 
			    assertExceptionIsInstanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException");
			}
		    else
			failed("Expected error code " + javax.transaction.xa.XAException.XAER_PROTO + " got " + errorCode);
		} catch (NoSuchFieldException ex2)  {
		    System.out.println("exception of class "+ex.getClass().getName()+" did not have errorCode"); 
		    ex.printStackTrace(); 
		}
            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var002() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: end() without a start() (no work done)");


            JTATest.verboseOut("Get the connection");
            javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            // Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            JTATest.verboseOut("Get the Xid but do not start a transaction");
            JTATestXid newXid = new JTATestXid();

            JTATest.verboseOut("Try to end() the unstarted transaction");
            // try to end the unstarted transaction
            try {
               JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
               failed("Did not throw exception");
            }
            catch (Exception ex) {
              int errorCode =JDReflectionUtil.getField_I(ex,"errorCode"); 

               if (errorCode == javax.transaction.xa.XAException.XAER_PROTO)
			if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
			    assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
			} else { 

			    assertExceptionIsInstanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException");
			}
               else
                  failed("Expected error code " + javax.transaction.xa.XAException.XAER_PROTO + " got " + errorCode);
            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var003() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: prepare() without a start() or an end() ");


            JTATest.verboseOut("Get the connection");
            javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            JTATest.verboseOut("Get the Xid but do not start a transaction");
            JTATestXid newXid = new JTATestXid();

            String table = basTbl + "003";
            Statement s = conn.createStatement();
            s.execute("CREATE TABLE " + table + " (COL1 INT)");


            JTATest.verboseOut("Try to prepare() the transaction");
            // try to prepare
            try {
               int rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
               failed("Did not throw exception  but got"+rc);
            }
            catch (Exception ex) {
              int errorCode =JDReflectionUtil.getField_I(ex,"errorCode"); 

               if (errorCode == javax.transaction.xa.XAException.XAER_NOTA)
			if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
			    assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
			} else { 
			    assertExceptionIsInstanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException");
			}
               else
                  failed("Expected error code " + javax.transaction.xa.XAException.XAER_NOTA + " got " + errorCode);
            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var004() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: prepare() after a start() but without an end()");


            JTATest.verboseOut("Get the connection");
            javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            JTATest.verboseOut("Get the Xid and start a transaction");
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            String table = basTbl + "004";
            Statement s = conn.createStatement();
            s.execute("CREATE TABLE " + table + " (COL1 INT)");


            JTATest.verboseOut("Try to prepare() the transaction");
            // try to prepare
            try {
               int rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
               failed("Did not throw exception "+rc);
            }
            catch (Exception ex) {
              int errorCode =JDReflectionUtil.getField_I(ex,"errorCode"); 

               if (errorCode == javax.transaction.xa.XAException.XAER_PROTO)
			if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
			    assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
			} else { 
			    assertExceptionIsInstanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException");
			}
               else
                  failed("Expected error code " + javax.transaction.xa.XAException.XAER_PROTO + " got " + errorCode);
            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var005() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: 2 phase commit() without a prepare()");


            JTATest.verboseOut("Get the connection");
            javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            JTATest.verboseOut("Get the Xid and start a transaction");
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            String table = basTbl + "005";
            Statement s = conn.createStatement();
            JTATest.verboseOut("CREATE TABLE " + table + " (COL1 INT)");
            s.execute("CREATE TABLE " + table + " (COL1 INT)");

            JTATest.verboseOut("End the transaction");
            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);

            JTATest.verboseOut("Try to commit() the transaction");
            // try to prepare
            try {
               JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false); // 1 phase set to false
               failed("Did not throw exception");
            }
            catch (Exception ex) {
              int errorCode =JDReflectionUtil.getField_I(ex,"errorCode"); 

               if (errorCode == javax.transaction.xa.XAException.XAER_PROTO) {
			if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
			    assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
			} else { 
			    assertExceptionIsInstanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException");
			}
                  JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);
               }
               else
                  failed("Expected error code " + javax.transaction.xa.XAException.XAER_PROTO + " got " + errorCode);
            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }



   public void Var006() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: 1 phase commit() after a prepare()");

            JTATest.verboseOut("Get the connection");
            javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            JTATest.verboseOut("Get the Xid and start a transaction");
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            String table = basTbl + "006";
            Statement s = conn.createStatement();
            JTATest.verboseOut("CREATE TABLE " + table + " (COL1 INT)");
            s.execute("CREATE TABLE " + table + " (COL1 INT)");

            JTATest.verboseOut("End the transaction");
            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);

            // prepare the transaction

            int rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_OK) {
               failed("Expected an OK! rc=" + rc);
            }

            JTATest.verboseOut("Try to 1 phase commit() the transaction");
            try {
               JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, true); // 1 phase set to true
               failed("Did not throw exception");
            }
            catch (Exception ex) {
              int errorCode =JDReflectionUtil.getField_I(ex,"errorCode"); 

               if (errorCode == javax.transaction.xa.XAException.XAER_PROTO) {
			if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
			    assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
			} else { 
			    assertExceptionIsInstanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException");
			}
                  JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);
               }
               else
                  failed("Expected error code " + javax.transaction.xa.XAException.XAER_PROTO + " got " + errorCode);
            }

         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var007() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: prepare() without doing work (this is considered read-only)");


            JTATest.verboseOut("Get the connection");
            javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            // Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            JTATest.verboseOut("Get the Xid and start a transaction");
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            JTATest.verboseOut("Try to prepare() the transaction");
            // try to prepare. should work (actually, does an implicit commit)
            int rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_RDONLY) {
               failed("Expected READ_ONLY! rc=" + rc);
            }
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var008() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: prepare() and commit without doing work (this is considered read-only)");


            JTATest.verboseOut("Get the connection");
            javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            // Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            JTATest.verboseOut("Get the Xid and start a transaction");
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            JTATest.verboseOut("Try to prepare() the transaction");
            // try to prepare. should work (actually, does an implicit commit)
            int rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_RDONLY) {
               failed("Expected READ_ONLY! rc=" + rc);
            }
            else {
               // try to commit. should fail
               try {
                  JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);
                  failed("Did not throw exception");
               }
               catch (Exception ex) {
                 int errorCode =JDReflectionUtil.getField_I(ex,"errorCode"); 

                  if (errorCode == javax.transaction.xa.XAException.XAER_NOTA)
			if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
			    assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
			} else { 
			    assertExceptionIsInstanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException");
			}
                  else
                     failed("Expected error code " + javax.transaction.xa.XAException.XAER_NOTA + " got " + errorCode);
               }

            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var009() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: prepare() and rollback without doing work (this is considered read-only)");


            JTATest.verboseOut("Get the connection");
            javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            // Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            JTATest.verboseOut("Get the Xid and start a transaction");
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            JTATest.verboseOut("Try to prepare() the transaction");
            // try to prepare. should work (actually, does an implicit commit)
            int rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_RDONLY) {
               failed("Expected READ_ONLY! rc=" + rc);
            }
            else {
               // try to rollback. should fail
               try {
                  JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);
                  failed("Did not throw exception");
               }
               catch (Exception ex) {
                 int errorCode =JDReflectionUtil.getField_I(ex,"errorCode"); 

                  if (errorCode == javax.transaction.xa.XAException.XAER_NOTA)
			if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
			    assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
			} else { 
			    assertExceptionIsInstanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException");
			}
                  else
                     failed("Expected error code " + javax.transaction.xa.XAException.XAER_NOTA + " got " + errorCode);
               }

            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var010() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: 1 phase commit without doing work (this is considered read-only)");


            JTATest.verboseOut("Get the connection");
            javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            //Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            JTATest.verboseOut("Get the Xid and start a transaction");
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, true);
	    assertCondition(true); 
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var011() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: 1 phase rollback without doing work (this is considered read-only)");


            JTATest.verboseOut("Get the connection");
            javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            //Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            JTATest.verboseOut("Get the Xid and start a transaction");
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);
	    assertCondition(true); 
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }
}
