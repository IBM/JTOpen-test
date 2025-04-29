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


public class JTACrash1 extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTACrash1";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTATest.main(newArgs); 
   }

   private String basTbl = JTATest.COLLECTION + ".CRASH1";
   private Connection conn_;
   boolean isIasp = false; 
   boolean isNTS = false; 
//   private Connection gc;

/**
Constructor.
**/
   public JTACrash1 (AS400 systemObject,
                     Hashtable<String,Vector<String>> namesAndVars,
                     int runMode,
                     FileOutputStream fileOutputStream,
                     
                     String password) {
      super (systemObject, "JTACrash1",
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
       basTbl = JTATest.COLLECTION + ".CRASH1";
       if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
           JTATest.verboseOut(baseURL_);
           conn_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
	   isIasp = JDTestUtilities.isIasp(conn_); 
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
       if (conn_ != null) try { conn_.close(); } catch (Throwable t) {}
       unlockSystem(); 
   }


   protected boolean checkIns(String table, String value)
   throws Exception
   {
      Statement s = conn_.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + table);
      boolean result = false;
      while (rs.next()) {
         String retVal = rs.getString(1);
         if (retVal.equals(value))
            result = true;
      }
      s.close();
      return result;
   }


   // TO Test:
   // There are 4 points (*) during a transaction where we try to
   // simulate a crash and see the results.
   // | Start (do work)  *    End  * Prepare * Commit/rollback *  |
   // In this testcase "Crash" scenario is created by closing the xa connection


   public void Var001() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Start transaction, do work, 'crash'");
            String table = basTbl + "001";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSouce");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
            }


            // now close the xa connection
            JDReflectionUtil.callMethod_V(xaConn,"close");

            // make sure that the value was not inserted
            assertCondition(!checkIns(table, insStr));
            JTATest.verboseOut("Done");
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
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
         try {
            JTATest.verboseOut("TEST: Start transaction, do work, end, 'crash'");
            String table = basTbl + "002";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSouce");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
            }

            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);

            // now close the xa connection (i.e. 'crash')
            JDReflectionUtil.callMethod_V(xaConn,"close");

            // make sure that the value was inserted
            assertCondition(checkIns(table, insStr));

            // Now rollback so we can run this testcase next time.
            JTATest.verboseOut("Done");
            JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);
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
         try {
            JTATest.verboseOut("TEST: Start transaction, do work, end, prepare, 'crash'");
            String table = basTbl + "003";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSouce");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
            }

            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
            rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_OK) {
               failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            }

            // now close the xa connection
            JDReflectionUtil.callMethod_V(xaConn,"close");

            // make sure that the value was inserted
            assertCondition(checkIns(table, insStr));
            JTATest.verboseOut("Done");
            JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);
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
            JTATest.verboseOut("TEST: Start transaction, do work, prepare, commit, 'crash'");
            String table = basTbl + "004";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSouce");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
            }

            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
            rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_OK) {
               failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            }

            JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);
            // now close the xa connection
            JDReflectionUtil.callMethod_V(xaConn,"close");

            // make sure that the value was inserted
            assertCondition(checkIns(table, insStr));
            JTATest.verboseOut("Done");
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
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
            JTATest.verboseOut("TEST: Start transaction, do work, prepare, rollback, 'crash'");
            String table = basTbl + "005";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSouce");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
            }

            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
            rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_OK) {
               failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            }

            JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);
            // now close the xa connection
            JDReflectionUtil.callMethod_V(xaConn,"close");

            // make sure that the value was not inserted
            assertCondition(!checkIns(table, insStr));
            JTATest.verboseOut("Done");
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var006() {
       String description = "TEST: Start transaction, do work, 'crash', (data should rollback) end fails"; 
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut(description);
            String table = basTbl + "006";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unexpected Exception "+description);
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSouce");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc + " "+description );
               return;
            }


            // now close the xa connection
            JDReflectionUtil.callMethod_V(xaConn,"close");
            try {
               JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
            }
            catch (Exception ex) {
		if ((!(JDReflectionUtil.instanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException"))) &&
		    (!(ex instanceof javax.transaction.xa.XAException)
		     )) {
                  failed(ex, "Unexpected exception "+description);
                  return;
               }
               if (JDReflectionUtil.getField_I(ex,"errorCode") != javax.transaction.xa.XAException.XAER_PROTO) {
                  failed(ex, "Expected XAER_PROTO failure "+description);
                  return;
               }
            }
//          rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
//          if (rc != javax.transaction.xa.XAResource.XA_OK) {
//             failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
//          }
//          JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);
//
            // make sure that the value was not inserted
            assertCondition(!checkIns(table, insStr));
            JTATest.verboseOut("Done");
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception "+description);
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
            JTATest.verboseOut("TEST: Start transaction, do work, end, 'crash', prepare, commit");
            String table = basTbl + "007";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
            }

            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);

            // now close the xa connection
            JDReflectionUtil.callMethod_V(xaConn,"close");

            rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_OK) {
               failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            }
            JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);

            // make sure that the value was inserted
            assertCondition(checkIns(table, insStr));
            JTATest.verboseOut("Done");
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
            JTATest.verboseOut("TEST: Start transaction, do work, prepare, 'crash', commit");
            String table = basTbl + "008";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSouce");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
            }

            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
            rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_OK) {
               failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            }

            // now close the xa connection
            JDReflectionUtil.callMethod_V(xaConn,"close");

            JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);

            // make sure that the value was inserted
            assertCondition(checkIns(table, insStr));
            JTATest.verboseOut("Done");
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
            JTATest.verboseOut("TEST: Start transaction, do work, 'crash', (data should rollback) end fails");
            String table = basTbl + "009";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSouce");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
               return;
            }


            // now close the xa connection
            JDReflectionUtil.callMethod_V(xaConn,"close");
            try {
               JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
            }
            catch (Exception ex) {
               if ((!(JDReflectionUtil.instanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException"))) && (!(ex instanceof javax.transaction.xa.XAException))) {
                  failed(ex, "Unexpected exception");
                  return;
               }
               if (JDReflectionUtil.getField_I(ex,"errorCode") != javax.transaction.xa.XAException.XAER_PROTO) {
                  failed(ex, "Expected XAER_PROTO failure");
                  return;
               }
            }
//          rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
//          if (rc != javax.transaction.xa.XAResource.XA_OK) {
//             failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
//          }
//          JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);
//
            // make sure that the value was not inserted
            assertCondition(!checkIns(table, insStr));
            JTATest.verboseOut("Done");
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
            JTATest.verboseOut("TEST: Start transaction, do work, end, 'crash', prepare, rollback");
            String table = basTbl + "010";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSouce");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
            }

            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);

            // now close the xa connection
            JDReflectionUtil.callMethod_V(xaConn,"close");

            rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_OK) {
               failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            }
            JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);

            // make sure that the value was not inserted
            assertCondition(!checkIns(table, insStr));
            JTATest.verboseOut("Done");
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
            JTATest.verboseOut("TEST: Start transaction, do work, prepare, 'crash', rollback");
            String table = basTbl + "011";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSouce");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

            Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
            }

            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
            rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_OK) {
               failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            }

            // now close the xa connection
            JDReflectionUtil.callMethod_V(xaConn,"close");

            JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);

            // make sure that the value was not inserted
            assertCondition(!checkIns(table, insStr));
            JTATest.verboseOut("Done");
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }

}
