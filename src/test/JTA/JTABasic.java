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


public class JTABasic extends JDTestcase {

   private String insStr = "JTABasic";
   private String basTbl = JTATest.COLLECTION + ".CHARTAB";
   private String endTbl1 = JTATest.COLLECTION + ".END1TBL";
   private String endTbl2 = JTATest.COLLECTION + ".END2TBL";
   private Connection c;

/**
Constructor.
**/
   public JTABasic (AS400 systemObject,
                    Hashtable namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTABasic",
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

      basTbl = JTATest.COLLECTION + ".CHARTAB";
      endTbl1 = JTATest.COLLECTION + ".END1TBL";
      endTbl2 = JTATest.COLLECTION + ".END2TBL";

      if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
         JTATest.verboseOut(baseURL_);
         c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
         Statement s = c.createStatement();
         s.execute("CREATE TABLE " + basTbl + " (COL1 CHAR (10 ) NOT NULL WITH DEFAULT)");
         s.close();
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
         Statement s = c.createStatement();
         s.execute("DROP TABLE " + basTbl);
         s.close();
         c.close();
      }
       unlockSystem(); 
   }


   // TO Test:
   // Basic Transactional (JTA) tests

   public void Var001() {             // from ~kulack/JTA/jtatest/JTAEasy.java
      if ( getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
         notApplicable("OLD JTA TEST"); 
         return; 
      }
      if (checkNative ()) {
      if (checkJdbc20StdExt ()) {
         try {
            JTATest.verboseOut("TEST: Simple insert/commit using a global transaction");

            javax.sql.XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
	    {
		JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
		JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);
	    }
            
            Object            xaConn = ds.getXAConnection();
            Object            xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection        conn   = (Connection) JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "1')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();
                 
            JDReflectionUtil.callMethod_V(xaRes, "start", newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
            }

            JDReflectionUtil.callMethod_V(xaRes,"end", newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);

            if (rc != javax.transaction.xa.XAResource.XA_OK) {
               failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            }

            JDReflectionUtil.callMethod_V(xaRes,"commit", newXid, false);
            conn.close();
            JTATest.verboseOut("Done");
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
      }
   }


   public void Var002() { // from ~kulack/JTA/jtatest/JTAEnd.java
     if ( getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
       notApplicable("OLD JTA TEST"); 
       return; 
    }
       if (checkNative ()) {
      if (checkJdbc20StdExt()) {
         try {

            boolean endTx = true;

            JTATest.verboseOut(Thread.currentThread().getName() + ": New transaction. Will be ended == " + endTx);
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get a DataSource");
            javax.sql.XADataSource xaDs = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(xaDs,"setDatabaseName",system_);

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.
	    Object  xaConn;
	    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
	    {
		xaConn = xaDs.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
	    }
	    else
	    {
		xaConn = xaDs.getXAConnection();
	    }
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");
            // Get the real connection object
            Object xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            Connection conn   = (Connection) JDReflectionUtil.callMethod_O(xaConn, "getConnection");

            // Generate a new transaction
            JTATestXid newXid = new JTATestXid();
            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "XAResource.start");
            JDReflectionUtil.callMethod_V(xaRes, "start", newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            Statement s = conn.createStatement();
            s.execute("CREATE TABLE " + endTbl1 + " (ID INTEGER NOT NULL WITH " +
                      "DEFAULT, DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
            JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                               "XAResource.end(DB2XAResource.TMFAIL)");
            boolean  exceptionTaken = false;
            try {
              JDReflectionUtil.callMethod_V(xaRes, "end",newXid, javax.transaction.xa.XAResource.TMFAIL);
            }
            catch (Exception xaEx) {
               int errorCode = JDReflectionUtil.getField_I(xaEx, "errorCode"); 
               if (errorCode != javax.transaction.xa.XAException.XA_RBROLLBACK) {
                  failed(xaEx, "Wrong exception taken");
                  return;
               }
               exceptionTaken = true;
            }
            if (!exceptionTaken) {
               JTATest.verboseOut("No exception was detected");
            }

            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "XAResource.rollback()");
            JDReflectionUtil.callMethod_V(xaRes, "rollback", newXid);
            boolean result = false;

            // the table should not exist. Should have been rolled back
            try {
               s.executeQuery("SELECT * FROM " + endTbl1);
               failed("Did not throw exception");
            }
            catch (SQLException e) {
               result = true;
            }

            conn.close();
            JTATransInfo[] match = JTATest.getTransInfo();
            boolean isTransPresent = false;
            if (match == null) {
               isTransPresent = false;
            }
            else {
               for (int i = 0; (i < match.length) && (match[i] != null); i++) {
                  isTransPresent = isTransPresent || newXid.match(match[i]);
               }
            }
            result = result && !isTransPresent;
            assertCondition(result, "isTransPresent="+isTransPresent);

         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }

   public void Var003() {
     if ( getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
       notApplicable("OLD JTA TEST"); 
       return; 
    }
      if (checkNative ()) {
      if (checkJdbc20StdExt()) {
         try {

            boolean endTx = true;

            JTATest.verboseOut(Thread.currentThread().getName() + ": New transaction. Will be ended == " + endTx);
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get a DataSource");
            javax.sql.XADataSource xaDs = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(xaDs,"setDatabaseName",system_);

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");

            // Get the XAConnection.
	    Object xaConn;
	    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
	    {
		xaConn = xaDs.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
	    }
	    else
	    {
		xaConn = xaDs.getXAConnection();
	    }

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");
            // Get the real connection object
            Object xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            Connection conn   = (Connection) JDReflectionUtil.callMethod_O(xaConn, "getConnection");

            // Generate a new transaction
            JTATestXid newXid = new JTATestXid();
            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "XAResource.start");
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            Statement s = conn.createStatement();
            s.execute("CREATE TABLE " + endTbl2 + " (ID INTEGER NOT NULL WITH " +
                      "DEFAULT, DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
            JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                               "XAResource.end(DB2XAResource.TMFAIL)");
            boolean exceptionTaken = false;
            try {
               JDReflectionUtil.callMethod_V(xaRes,"end", newXid, javax.transaction.xa.XAResource.TMFAIL);
            }
            catch (Exception xaEx) {
              int errorCode = JDReflectionUtil.getField_I(xaEx, "errorCode"); 

               if (errorCode != javax.transaction.xa.XAException.XA_RBROLLBACK) {
                  failed(xaEx, "Wrong exception taken");
                  return;
               }
               exceptionTaken = true;
            }
            if (!exceptionTaken) {
               JTATest.verboseOut("No exception was detected");
            }

            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "XAResource.commit()");
            try {
               JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, true);
               failed("Did not throw exception");
               return;
            }
            catch (Exception ex) {
            	
               if (!(JDReflectionUtil.instanceOf(ex, "com.ibm.db2.jdbc.app.DB2XAException") )) {
                  failed(ex, "Unexpected exception type");
                  return;
               }
               int errorCode = JDReflectionUtil.getField_I(ex, "errorCode"); 
               if (errorCode != javax.transaction.xa.XAException.XAER_PROTO) {
                  failed(ex, "Unexpected exception error code");
                  return;
               }
               // assertExceptionIsInstanceOf(ex, "com.ibm.db2.jdbc.app.DB2XAException");
            }

            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "XAResource.rollback()");
            // Now rollback to complete the disassociation for the AS/400
            // on any other platform, the XAResource.end(TMFAIL) done above would
            // have been enough.
            JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);

            boolean result = false;
            // the table should not exist. Should have been rolled back
            try {
               s.executeQuery("SELECT * FROM " + endTbl2);
               failed("Did not throw exception");
               return;
            }
            catch (SQLException e) {
               JTATest.verboseOut("Table rolled back as expected");
               result = true;
            }

            conn.close();
            JTATransInfo[] match = JTATest.getTransInfo();
            boolean isTransPresent = false;
            if (match == null) {
               isTransPresent = false;
            }
            else {
               for (int i = 0; (i < match.length) && (match[i] != null); i++) {
                  isTransPresent = isTransPresent || newXid.match(match[i]);
               }
            }
            result = result && !isTransPresent;
            assertCondition(result, "isTransPresent="+isTransPresent);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }



   public void Var004() {
     if ( getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
       notApplicable("OLD JTA TEST"); 
       return; 
    }
       if (checkNative ()) {
      if (checkJdbc20StdExt()) {
         try {
            // TMSUSPEND and TMRESUME not currently supported
            notApplicable("TMSUSPEND and TMRESUME not currently supported");
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var005() {
     if ( getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
       notApplicable("OLD JTA TEST"); 
       return; 
    }
       if (checkNative ()) {
      if (checkJdbc20StdExt()) {
         try {
            // start and end a transaction without doing any work
            JTATest.verboseOut("TEST: start and end a transaction without doing work");

            javax.sql.XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

	    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
	    {
		JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
		JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);
	    }

            Object            xaConn = ds.getXAConnection();
            Object              xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");

            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            // end the transaction
            JDReflectionUtil.callMethod_V(xaRes,"end", newXid, javax.transaction.xa.XAResource.TMSUCCESS);

            JTATest.verboseOut("Done");
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }



}
