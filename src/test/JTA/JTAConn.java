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


public class JTAConn extends JDTestcase {

   private String insStr = "JTAConn";
   private String basTbl = JTATest.COLLECTION + ".CHARTAB";
   private Connection c;
   boolean isIasp = false; 
   boolean isNTS = false; 
/**
Constructor.
**/
   public JTAConn (AS400 systemObject,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   
                   String password) {
      super (systemObject, "JTAConn",
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

      if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
         JTATest.verboseOut(baseURL_);
         c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
         isIasp = JDTestUtilities.isIasp(c); 
         Statement s = c.createStatement();
	 try { 
	     s.executeUpdate("DROP TABLE "+basTbl);
	 } catch (Exception e) {
	 } 
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


   public void Var001() { // from ~kulack/JTA/jtatest/JTAConn.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: starting a transaction prior to using XAConnection.getConnection");

            XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");

            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);


            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "2')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

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


   public void Var002() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: starting and ending transaction prior to using XAConnection.getConnection");

            XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");

            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);

            Connection conn = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            try {
               // Under OS/400 JTA, we only allow a work under a new transaction
               // until the first one was committed/rolled back. We expect
               // the work to fail with an XAER_PROTO failure.
               String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "2')";
               JTATest.verboseOut("prepare \"" + sqlString + "\"");
               PreparedStatement stmt = conn.prepareStatement(sqlString);

               int      rc;
               rc = stmt.executeUpdate();
               if (rc != 1) {
                  failed("Expected 1 row inserted, got " + rc);
               }
            }
            catch (Exception ex) {
               int rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
               if (rc != javax.transaction.xa.XAException.XA_RDONLY) {
                  conn.rollback();
               }
               conn.close();
	       if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		   assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");

	       } else { 
		   assertExceptionIsInstanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException");
	       }


               Object  xaEx = ex;
               if (JDReflectionUtil.getField_I(xaEx,"errorCode") != javax.transaction.xa.XAException.XAER_PROTO) {
                  failed(ex, "Expected XAER_PROTO failure");
               }
            }

         }
         catch (Exception e) {
            failed(e, "Unexpected exception");
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
            JTATest.verboseOut("TEST: Try to associate two transactions with a connection. Should fail");

            XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            // Generate two new transactions
            JTATestXid              Xid1 = new JTATestXid();
            JTATestXid              Xid2 = new JTATestXid();

            // now associate both with the XA connection
            JDReflectionUtil.callMethod_V(xaRes,"start",Xid1, javax.transaction.xa.XAResource.TMNOFLAGS);
            try {
               JDReflectionUtil.callMethod_V(xaRes,"start",Xid2, javax.transaction.xa.XAResource.TMNOFLAGS);
               failed("Did not throw exception");
            }
            catch (Exception ex) {
	       if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		   assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");

	       } else { 
		   assertExceptionIsInstanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException");
	       }

            }
            conn.close();
            JTATest.verboseOut("Done");
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
            JTATest.verboseOut("TEST: Try to have 2 Connections for one XAConnection. Should not be able to");

            XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Connection              conn1  = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");
            // the next step would basically close conn1
            Connection              conn2  = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            // any operation on conn1 should now throw an exception
            try {
               conn1.getAutoCommit();
            }
            catch (Exception ex) {
               assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
            }
            conn2.close();
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
            JTATest.verboseOut("TEST: Try to have 2 Connections for one transaction. Should not be able to");

            XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object        xaConn1 = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes1 =  JDReflectionUtil.callMethod_O(xaConn1,"getXAResource");
            Connection              conn1   = (Connection)JDReflectionUtil.callMethod_O(xaConn1,"getConnection");

            Object        xaConn2 = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes2 =  JDReflectionUtil.callMethod_O(xaConn2,"getXAResource");
            Connection              conn2   = (Connection)JDReflectionUtil.callMethod_O(xaConn2,"getConnection");


            // Generate a new transaction
            JTATestXid              Xid1 = new JTATestXid();

            // now associate both connections with the transaction
            // after talking to Fred, it is a minor bug if we do not
            // get a exception here in this var. A major one is if we
            // do not get an exception while actually doing work with
            // the connections (vars which are done later in this testcase)
            JDReflectionUtil.callMethod_V(xaRes1,"start",Xid1, javax.transaction.xa.XAResource.TMNOFLAGS);
            try {
               JDReflectionUtil.callMethod_V(xaRes2,"start",Xid1, javax.transaction.xa.XAResource.TMNOFLAGS);
               // As a performance improvement, (to prevent us
               // from having to lookup the XID at start time,
               // this no longer fails until run time (var11 and 12)

               assertCondition(true);
               // failed("Did not throw exception");
            }
            catch (Exception ex) {
	       if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		   assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");

	       } else { 
		   assertExceptionIsInstanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException");
	       }

            }
            conn1.close();
            conn2.close();
            JTATest.verboseOut("Done");
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
            JTATest.verboseOut("TEST: Closing the connection handle before committing the transaction. should commit");

            String table = "CONNVAR6";

            XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            try {
               JTATest.dltTmpTbl(JTATest.COLLECTION + '.' + table, c);
            }
            catch (Exception e) {
            }


            String sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table + " (COL1 INT)";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            stmt.execute();

            conn.close();

            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
            JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, true); // 1 pc

            // the table should be there
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM " + JTATest.COLLECTION + ".SYSTABLES WHERE TABLE_NAME='" + table + "'");
            rs.next();
            assertCondition(rs.getString(1).equals(table));
            s.close();
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
            JTATest.verboseOut("TEST: Closing the connection then committing the transaction. should commit");
            String table = "CONNVAR7";

            XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            try {
               JTATest.dltTmpTbl(JTATest.COLLECTION + '.' + table, c);
            }
            catch (Exception e) {
               JTATest.verboseOut(table + " could not be DROPPED: " + e);
            }


            String sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table + " (COL1 INT)";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            stmt.execute();
            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            JDReflectionUtil.callMethod_V(xaConn,"close");

            JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, true); // 1 pc
            // the table should be there
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM " + JTATest.COLLECTION + ".SYSTABLES WHERE TABLE_NAME='" + table + "'");
            rs.next();
            assertCondition(rs.getString(1).equals(table));
            s.close();
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
            JTATest.verboseOut("TEST: Prepare transaction, close connection, commit transaction. commit should work");

            String table = "CONNVAR8";

            XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            try {
               JTATest.dltTmpTbl(JTATest.COLLECTION + '.' + table, c);
            }
            catch (Exception e) {
               JTATest.verboseOut(table + " could not be DROPPED: " + e);
            }


            String sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table + " (COL1 INT)";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            int      rc;
            stmt.execute();

            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
            rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_OK) {
               failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            }

            JDReflectionUtil.callMethod_V(xaConn,"close");

            JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);
            // the table should be there
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM " + JTATest.COLLECTION + ".SYSTABLES WHERE TABLE_NAME='" + table + "'");
            rs.next();
            assertCondition(rs.getString(1).equals(table));
            s.close();
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
            JTATest.verboseOut("TEST: Prepare transaction, close connection, rollback transaction. rollback should work");

            String table = "CONNVAR9";

            XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            try {
               JTATest.dltTmpTbl(JTATest.COLLECTION + '.' + table, c);
            }
            catch (Exception e) {
               JTATest.verboseOut(table + " could not be DROPPED: " + e);
            }


            String sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table + " (COL1 INT)";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            int      rc;
            stmt.execute();

            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
            rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_OK) {
               failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            }

            JDReflectionUtil.callMethod_V(xaConn,"close");

            JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);
            // the table should not be there
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM " + JTATest.COLLECTION + ".SYSTABLES WHERE TABLE_NAME='" + table + "'");
            rs.next();
            rs.getString(1);
            failed("Did not throw exception");
         }
         catch (Exception e) {
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
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
            // need to complete this testcase
            // (check for transactions, how to cleanup)
            // notApplicable();

            JTATest.verboseOut("TEST: Prepare transaction, close connection, leave without doing rollback or commit");

            String table = "CONNVAR10";

            XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            try {
               JTATest.dltTmpTbl(JTATest.COLLECTION + '.' + table, c);

            }
            catch (Exception e) {
               JTATest.verboseOut(table + " could not be DROPPED: " + e);
               String msg = e.toString();
               if (msg.indexOf("in use") != -1) {
                  JTATest.verboseOut("You likely got a failure on this variation last time");
                  JTATest.verboseOut("Locks still exist on the file due to pending transactions");
                  failed(e, "Locks still exist on the file");
                  return;
               }
            }

            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();
            JTATest.verboseOut(newXid.toString());
            JTATest.verboseOut("TestXid : fmt=?? gtrid=0x" +
                               Long.toHexString(newXid.myGtrid) +
                               " bqual=0x" + Long.toHexString(newXid.myBqual));
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);


            ////////////////////////////////// original transaction ///////////////////////
            String sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table + " (COL1 INT)";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            int      rc;
            stmt.execute();

            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
            rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_OK) {
               failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
               return;
            }

            JDReflectionUtil.callMethod_V(xaConn,"close");

            // Start over, do an operation on the table, and make sure that
            // the first operation times out, we can rollback the transaction,
            // then the second operation works.
            // In any case, try to rollback the transaction.
            boolean timeout = false;
            boolean rolledbackOk = false;
            boolean retryOk = false;
            try {
               xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
               xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               // Generate a new transaction
               JTATestXid              newXid2 = new JTATestXid();
               JTATest.verboseOut(newXid2.toString());
               JTATest.verboseOut("TestXid : fmt=?? gtrid=0x" +
                                  Long.toHexString(newXid2.myGtrid) +
                                  " bqual=0x" + Long.toHexString(newXid2.myBqual));
               JDReflectionUtil.callMethod_V(xaRes,"start",newXid2, javax.transaction.xa.XAResource.TMNOFLAGS);

               /////////////////////// Get timeout ////////////////////////
               // sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table + " (COL1 INT)";
               sqlString = "INSERT INTO " + JTATest.COLLECTION + ".CONNVAR10 VALUES(5)";
               JTATest.verboseOut("prepare \"" + sqlString + "\"");

               try {
                  // Expect this operation to timeout
                  stmt = conn.prepareStatement(sqlString);
                  stmt.execute();
               }
               catch (SQLException e) {
                  JTATest.verboseOut("Expected timeout exception: " + e);
                  String msg = e.toString();
                  if (msg.indexOf("Row or object " + table +
                                  " in " + JTATest.COLLECTION + " type *FILE in use") != -1) {
                     JTATest.verboseOut("Got the timeout");
                     timeout = true;
                  }
                  else {
                     JDReflectionUtil.callMethod_V(xaRes,"end",newXid2, javax.transaction.xa.XAResource.TMNOFLAGS);
                     JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid2);
                     timeout = false;
                     throw e;
                  }
               }
               JDReflectionUtil.callMethod_V(xaRes,"end",newXid2, javax.transaction.xa.XAResource.TMNOFLAGS);
               JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid2);

               /////////////////////// Rollback original transaction.
               JTATransInfo[] match = JTATest.getTransInfo();
               if (match == null) {
                  failed("Didn't find any transactions");
                  return;
               }
               boolean  isTransPresent = false;
               JTATest.verboseOut("Matching XID: " + newXid);

               for (int i = 0; (i < match.length) && (match[i] != null); i++) {
                  if (match[i].getState().equalsIgnoreCase("prepared")) {
                     if (newXid.match(match[i])) {
                        isTransPresent = true;
                        JTATest.verboseOut("Found the expected transaction in the correct (" + match[i]
                                           + ") state");
                        break;
                     }
                  }
                  else {
                     JTATest.verboseOut("Skipping " + match[i].toString());
                  }
               }
               if (!isTransPresent) {
                  failed("Couldn't find the expected transaction");
                  return;
               }

               // Rollback the original transaction
               JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);
               // Set the newXid to null so that the exception handler
               // and finally block below won't try to end or roll it back.
               JTATestXid  newXidCopy = newXid;
               newXid = null;

               /////////////////////// Try again
               isTransPresent = false;
               match = JTATest.getTransInfo();
               if (match != null) {
                  JTATest.verboseOut("Matching XID: " + newXidCopy);
                  for (int i = 0; (i < match.length) && (match[i] != null); i++) {
                     if (newXidCopy.match(match[i])) {
                        isTransPresent = true;
                        JTATest.verboseOut("Failed! Found the transaction, it was unexpected");
                        break;
                     }
                  }
               }
               // We should have rolled back the original transaction.
               if (!isTransPresent) {
                  rolledbackOk = true;
               }
               else {
                  rolledbackOk = false;
               }

               // Now, retry the create table so that we can ensure that it completes
               // successfully because the first transaction was rolled back.
               // Generate a new transaction
               newXid2 = new JTATestXid();
               JTATest.verboseOut(newXid2.toString());
               JDReflectionUtil.callMethod_V(xaRes,"start",newXid2, javax.transaction.xa.XAResource.TMNOFLAGS);

               sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table + " (COL1 INT)";
               JTATest.verboseOut("prepare \"" + sqlString + "\"");
               stmt = conn.prepareStatement(sqlString);

               try {
                  // Expect this operation to timeout
                  stmt.execute();
                  retryOk = true;
               }
               catch (Exception e) {
                  retryOk = false;
               }
               finally {
                  JDReflectionUtil.callMethod_V(xaRes,"end",newXid2, javax.transaction.xa.XAResource.TMNOFLAGS);
                  JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid2);
               }
            }
            finally {
               try {
                  if (newXid != null) {
                     JTATest.verboseOut("Cleanup by rolling back: " + newXid);
                     JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);
                  }
               }
               catch (Exception e) {
                  JTATest.verboseOut("You should try to manually rollback: " + newXid);
               }
            }
            JTATest.verboseOut("Timeout=" + timeout +
                               ", rolledbackOk=" + rolledbackOk +
                               ", retryOk=" + retryOk);
            assertCondition((timeout == true) && (rolledbackOk) && (retryOk == true));
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
            JTATest.verboseOut("TEST: Try to have 2 Connections for one transaction. Do work on this conn. Should not be able to");

            XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object        xaConn1 = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes1 =  JDReflectionUtil.callMethod_O(xaConn1,"getXAResource");
            // Connection              conn1   = (Connection)JDReflectionUtil.callMethod_O(xaConn1,"getConnection");

            Object        xaConn2 = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes2 =  JDReflectionUtil.callMethod_O(xaConn2,"getXAResource");
            // Connection              conn2   = (Connection)JDReflectionUtil.callMethod_O(xaConn2,"getConnection");


            // Generate a new transaction
            JTATestXid              Xid1 = new JTATestXid();

            // now associate both connections with the transaction
            // (xaRes1 is the one which would be valid)
            JDReflectionUtil.callMethod_V(xaRes1,"start",Xid1, javax.transaction.xa.XAResource.TMNOFLAGS);
            try {
               JDReflectionUtil.callMethod_V(xaRes2,"start",Xid1, javax.transaction.xa.XAResource.TMNOFLAGS);
            }
            catch (Exception ex) {
               if (JDReflectionUtil.instanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException") ||
                   ex instanceof javax.transaction.xa.XAException  ) {
                  Object xaEx = (Object)ex;
                  if (JDReflectionUtil.getField_I(xaEx,"errorCode") != javax.transaction.xa.XAException.XAER_DUPID) {
                     failed(ex, "Second start of transaction failed");
                     return;
                  }
               }
               else {
                  failed(ex, "Incorrect exception type");
               }
            }

// This code is no longer valid.
//          Statement s1 = conn1.createStatement();
//          Statement s2 = conn2.createStatement();
//          // using s1 should work
//          s1.executeQuery("SELECT * FROM " + JTATest.COLLECTION + ".SYSTABLES");
//          // using s2 should throw an exception
//          try {
//             s2.executeQuery("SELECT * FROM " + JTATest.COLLECTION + ".SYSTABLES");
//             failed("Did not throw exception");
//          }
//          catch (Exception ex) {
//             assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
//          }

            JTATest.verboseOut("Done");
            assertCondition(true);
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
            JTATest.verboseOut("TEST: Try to have 2 Connections for one transaction. Do work on this conn. Should not be able to");
            // same as the var above. except the order is reversed

            XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object        xaConn1 = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes1 =  JDReflectionUtil.callMethod_O(xaConn1,"getXAResource");
            // Connection              conn1   = (Connection)JDReflectionUtil.callMethod_O(xaConn1,"getConnection");

            Object        xaConn2 = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes2 =  JDReflectionUtil.callMethod_O(xaConn2,"getXAResource");
            // Connection              conn2   = (Connection)JDReflectionUtil.callMethod_O(xaConn2,"getConnection");


            // Generate a new transaction
            JTATestXid              Xid1 = new JTATestXid();

            // now associate both connections with the transaction
            // (xaRes1 is the one which would be valid)
            JDReflectionUtil.callMethod_V(xaRes1,"start",Xid1, javax.transaction.xa.XAResource.TMNOFLAGS);
            try {
               JDReflectionUtil.callMethod_V(xaRes2,"start",Xid1, javax.transaction.xa.XAResource.TMNOFLAGS);
            }
            catch (Exception ex) {
               if (JDReflectionUtil.instanceOf(ex,"com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException") ||
		   ex instanceof javax.transaction.xa.XAException) {
                  if (JDReflectionUtil.getField_I(ex,"errorCode") != javax.transaction.xa.XAException.XAER_DUPID) {
                     failed(ex, "Second start of transaction failed");
                     return;
                  }
                  // Fall through
               }
               else {
                  failed(ex, "Unexpected exception type");
                  return;
               }
            }

            JTATest.verboseOut("Done");
            assertCondition(true);
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
            JTATest.verboseOut("TEST: 2 Connections/transactions. Try to create a table with both");

            String table = JTATest.COLLECTION + ".CONNVAR13";

            XADataSource ds1 = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds1,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(ds1,"setUser",userId_);
            JDReflectionUtil.callMethod_V(ds1,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object        xaConn1 = JDReflectionUtil.callMethod_O(ds1,"getXAConnection");
            Object xaRes1 =  JDReflectionUtil.callMethod_O(xaConn1,"getXAResource");
            Connection              conn1   = (Connection)JDReflectionUtil.callMethod_O(xaConn1,"getConnection");

            XADataSource ds2 = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds2,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(ds2,"setUser",userId_);
            JDReflectionUtil.callMethod_V(ds2,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object        xaConn2 = JDReflectionUtil.callMethod_O(ds2,"getXAConnection");
            Object xaRes2 =  JDReflectionUtil.callMethod_O(xaConn2,"getXAResource");
            Connection              conn2   = (Connection)JDReflectionUtil.callMethod_O(xaConn2,"getConnection");


            // Generate a new transaction
            JTATestXid              Xid1 = new JTATestXid();
            JTATestXid              Xid2 = new JTATestXid();

            // now associate both connections with their transactions
            JDReflectionUtil.callMethod_V(xaRes1,"start",Xid1, javax.transaction.xa.XAResource.TMNOFLAGS);
            JDReflectionUtil.callMethod_V(xaRes2,"start",Xid2, javax.transaction.xa.XAResource.TMNOFLAGS);

            Statement s1 = conn1.createStatement();
            Statement s2 = conn2.createStatement();

            s1.execute("CREATE TABLE " + table + " (COLA INT)");
            // next one should throw an exception
            try {
               s2.execute("CREATE TABLE " + table + " (COLB INT)");
            }
            catch (Exception ex) {
               assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
            }

            JDReflectionUtil.callMethod_V(xaRes1,"end",Xid1, javax.transaction.xa.XAResource.TMSUCCESS);
            JDReflectionUtil.callMethod_V(xaRes1,"rollback",Xid1);
            JDReflectionUtil.callMethod_V(xaRes2,"end",Xid2, javax.transaction.xa.XAResource.TMSUCCESS);
            JDReflectionUtil.callMethod_V(xaRes2,"rollback",Xid2);
            JTATest.verboseOut("Done");
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
            JTATest.verboseOut("TEST: 2 Connections/transactions. create a table with first one");
            JTATest.verboseOut("TEST: Insert a row. (do not commit). Do a query with the second one");
            JTATest.verboseOut("TEST: Check to see if the value was inserted.");


            String table = JTATest.COLLECTION + ".CONNVAR14";

            XADataSource ds1 = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds1,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(ds1,"setUser",userId_);
            JDReflectionUtil.callMethod_V(ds1,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object        xaConn1 = JDReflectionUtil.callMethod_O(ds1,"getXAConnection");
            Object xaRes1 =  JDReflectionUtil.callMethod_O(xaConn1,"getXAResource");
            Connection              conn1   = (Connection)JDReflectionUtil.callMethod_O(xaConn1,"getConnection");

            XADataSource ds2 = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds2,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(ds2,"setUser",userId_);
            JDReflectionUtil.callMethod_V(ds2,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object        xaConn2 = JDReflectionUtil.callMethod_O(ds2,"getXAConnection");
            Object xaRes2 =  JDReflectionUtil.callMethod_O(xaConn2,"getXAResource");
            Connection              conn2   = (Connection)JDReflectionUtil.callMethod_O(xaConn2,"getConnection");

            // get the default transaction isolation level
            if (conn1.getTransactionIsolation() == Connection.TRANSACTION_READ_UNCOMMITTED)
               JTATest.verboseOut("Default TRANS ISO LEVEL = TRANSACTION_READ_UNCOMMITTED");

            // Generate a new transaction
            JTATestXid              Xid1 = new JTATestXid();
            JTATestXid              Xid2 = new JTATestXid();

            // now associate both connections with their transactions
            JDReflectionUtil.callMethod_V(xaRes1,"start",Xid1, javax.transaction.xa.XAResource.TMNOFLAGS);
            JDReflectionUtil.callMethod_V(xaRes2,"start",Xid2, javax.transaction.xa.XAResource.TMNOFLAGS);

            Statement s1 = conn1.createStatement();
            Statement s2 = conn2.createStatement();

            s1.execute("CREATE TABLE " + table + " (COLA INT)");
            s1.executeUpdate("INSERT INTO " + table + " VALUES (101)");
            // the table should still be locked, since the CREATE hasn't been committed
            try {
               /* ResultSet rs = */  s2.executeQuery("SELECT * FROM " + table);
            }
            catch (Exception ex) {
               assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
            }
            JDReflectionUtil.callMethod_V(xaRes1,"end",Xid1, javax.transaction.xa.XAResource.TMSUCCESS);
            JDReflectionUtil.callMethod_V(xaRes1,"rollback",Xid1);
            JTATest.verboseOut("Done");
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
            JTATest.verboseOut("TEST: 2 Connections/transactions. create a table with first one. commit");
            JTATest.verboseOut("TEST: Insert a row. (do not commit). Do a query with the second one");
            JTATest.verboseOut("TEST: Check to see if the value was inserted.");


            String table = JTATest.COLLECTION + ".CONNVAR15";

            XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);
            Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            XADataSource ds1 = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
    	    JDReflectionUtil.callMethod_V(ds1,"setDatabaseName",system_);
    	    
    	    JDReflectionUtil.callMethod_V(ds1,"setUser",userId_);
	    JDReflectionUtil.callMethod_V(ds1,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object        xaConn1 = JDReflectionUtil.callMethod_O(ds1,"getXAConnection");
            Object xaRes1 =  JDReflectionUtil.callMethod_O(xaConn1,"getXAResource");
            Connection              conn1   = (Connection)JDReflectionUtil.callMethod_O(xaConn1,"getConnection");

            XADataSource ds2 = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
    	    JDReflectionUtil.callMethod_V(ds2,"setDatabaseName",system_);
    	    JDReflectionUtil.callMethod_V(ds2,"setUser",userId_);
    	    JDReflectionUtil.callMethod_V(ds2,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object        xaConn2 = JDReflectionUtil.callMethod_O(ds2,"getXAConnection");
            Object xaRes2 =  JDReflectionUtil.callMethod_O(xaConn2,"getXAResource");
            Connection              conn2   = (Connection)JDReflectionUtil.callMethod_O(xaConn2,"getConnection");


            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();
            JTATestXid              Xid1 = new JTATestXid();
            JTATestXid              Xid2 = new JTATestXid();

            // now associate both connections with their transactions
            JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);
            JDReflectionUtil.callMethod_V(xaRes1,"start",Xid1, javax.transaction.xa.XAResource.TMNOFLAGS);
            JDReflectionUtil.callMethod_V(xaRes2,"start",Xid2, javax.transaction.xa.XAResource.TMNOFLAGS);

            Statement s2 = conn2.createStatement();
            Statement s1 = conn1.createStatement();
            Statement s = conn.createStatement();

            try {
               s.execute("DROP TABLE " + table);
            }
            catch (Exception e) {
            }
            s.execute("CREATE TABLE " + table + " (COLA INT)");
            s.close();
            JDReflectionUtil.callMethod_V(xaRes,"end",Xid, javax.transaction.xa.XAResource.TMSUCCESS);
            JDReflectionUtil.callMethod_V(xaRes,"commit",Xid, true);

            s1.executeUpdate("INSERT INTO " + table + " VALUES (101)");

            // get the default transaction isolation level
            if (conn1.getTransactionIsolation() == Connection.TRANSACTION_READ_UNCOMMITTED) {
               JTATest.verboseOut("Default TRANS ISO LEVEL = TRANSACTION_READ_UNCOMMITTED");
               // should be able to read
               ResultSet rs = s2.executeQuery("SELECT * FROM " + table);
               rs.next();
               assertCondition(rs.getInt(1) == 101);
            }
            else
               assertCondition(true);
            JDReflectionUtil.callMethod_V(xaRes1,"end",Xid1, javax.transaction.xa.XAResource.TMSUCCESS);
            JDReflectionUtil.callMethod_V(xaRes1,"rollback",Xid1);
            JTATest.verboseOut("Done");
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
            JTATest.verboseOut("TEST: Do work on a connection after xaRes end");


            String table = JTATest.COLLECTION + ".CONNVAR16";

            XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource"); 
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);
            Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // associate the transaction
            JDReflectionUtil.callMethod_V(xaRes,"start",Xid, javax.transaction.xa.XAResource.TMNOFLAGS);

            Statement s = conn.createStatement();

            try {
               s.execute("DROP TABLE " + table);
            }
            catch (Exception e) {
            }
            s.execute("CREATE TABLE " + table + " (COLA INT)");
            // disassociate the transaction
            JDReflectionUtil.callMethod_V(xaRes,"end",Xid, javax.transaction.xa.XAResource.TMSUCCESS);

            // now, try to do work on the connection
            try {
               s.execute("DROP TABLE " + table);
               failed("Did not throw exception");
            }
            catch (Exception ex) {
	       if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		   assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");

	       } else { 
		   assertExceptionIsInstanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException");
	       }
               if (JDReflectionUtil.getField_I(ex,"errorCode") != javax.transaction.xa.XAException.XAER_PROTO) {
                  failed(ex, "Incorrect exception error code");
               }
            }

            JDReflectionUtil.callMethod_V(xaRes,"rollback",Xid);
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
            JTATest.verboseOut("TEST: Simple test of JTATest.getTransInfo()");

            /////////////////////// Rollback original transaction.
            JTATransInfo[] match = JTATest.getTransInfo();
            if (match == null) {
               JTATest.verboseOut("No transactions active or in-doubt");
               assertCondition(true);
               return;
            }
            JTATestXid newXid = new JTATestXid();
            JTATest.verboseOut("Matching XID: " + newXid);
            for (int i = 0; (i < match.length) && (match[i] != null); i++) {
               JTATest.verboseOut(match[0].toString());
            }
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Transaction");
         }
      }
       }
   }

   // TODO: Copy Var008, but close result set, statements, etc.. after the JDReflectionUtil.callMethod_V(xaRes,"end",)

}

