////////////////////////////////////////////////////////////////////////
//
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
// File Name:    JTAStdInsert.java
//
// Description:  Same as JTAInsert.java but test standard interfaces for
//               JTA & JDBC Std Ext
//
// Classes:      JTAStdInsert
//
////////////////////////////////////////////////////////////////////////
package test.JTA;

import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JTATest;
import test.PasswordVault;
import test.JD.JDTestUtilities;

import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

public class JTAStdInsert extends JTATestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAStdInsert";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTAStdTest.main(newArgs); 
   }

   private String basTbl = JTATest.COLLECTION + ".CHARTABINS";
   private Connection c;
   //protected boolean isNTS = false; @PDD move to JTATestcase

/**
Constructor.
**/
   public JTAStdInsert (AS400 systemObject,
                     Hashtable namesAndVars,
                     int runMode,
                     FileOutputStream fileOutputStream,
                     
                     String password) {
      super (systemObject, "JTAStdInsert",
             namesAndVars, runMode, fileOutputStream,
             password);
   }

   public JTAStdInsert (AS400 systemObject,
			String testname, 
                     Hashtable namesAndVars,
                     int runMode,
                     FileOutputStream fileOutputStream,
                     
                     String password) {
      super (systemObject, testname, 
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
       basTbl = JTATest.COLLECTION + ".CHARTABINS";
      if (isJdbc20StdExt ()) {
         JTATest.verboseOut(baseURL_);
         c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
	 isIasp = JDTestUtilities.isIasp(c); 
         Statement s = c.createStatement();
         s.execute("CREATE TABLE " + basTbl + " (COL1 VARCHAR (16) NOT NULL WITH DEFAULT)");
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
      if (isJdbc20StdExt ()) {
         Statement s = c.createStatement();
         s.execute("DROP TABLE " + basTbl);
         s.close();
         c.close();
      }
      unlockSystem(); 
   }

   protected boolean checkIns(String value)
   throws Exception
   {
      Statement s = c.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM " + basTbl);
      boolean result = false;
      while (rs.next()) {
         String retVal = rs.getString(1);
         if (retVal.equals(value))
            result = true;
      }
      s.close();
      return result;
   }

   public void Var001() {       // from ~kulack/JTA/jtatest/JTAInsert1.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         Connection conn   = null;
         try {

            String insStr = JTATest.getStr();
            JTATest.verboseOut("TEST: INSERT under a TX, then 2 phase prepare/commit");

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

            XADataSource          ds     = newXADataSource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

            // Get the real connection object
            XAResource              xaRes  = xaConn.getXAResource();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "')";

            JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Start a new transaction \"" + newXid.toString() + "\"");

            // Start a transaction for the stuff we're about to do

            xaRes.start(newXid, XAResource.TMNOFLAGS);

            JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the insert");
            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
               return;
            }
            xaRes.end(newXid, XAResource.TMSUCCESS);

            JTATest.verboseOut(Thread.currentThread().getName() + ": Prepare the transaction");
            rc = xaRes.prepare(newXid);
            if (rc != XAResource.XA_OK) {
               failed("Expected an OK! rc=" + rc);
               return;
            }

            JTATest.verboseOut(Thread.currentThread().getName() + ": Commit the transaction");
            xaRes.commit(newXid, false);
            JTATest.verboseOut(Thread.currentThread().getName() + ": The transaction is complete");
            // now check to make sure the value was inserted
            assertCondition(checkIns(insStr));

         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
         finally {
           if (conn != null) try { conn.close(); } catch (Exception e) {}
         }
      }
   }

   public void Var002() { // from ~kulack/JTA/jtatest/JTAInsert2.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         Connection conn   = null;
         try {
            String insStr = JTATest.getStr();
            JTATest.verboseOut("TEST: INSERT under a TX, then one phase commit");

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

            XADataSource          ds     = newXADataSource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

            // Get the real connection object
            XAResource              xaRes  = xaConn.getXAResource();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "')";

            JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Start a new transaction \"" + newXid.toString() + "\"");
            // Start a transaction for the stuff we're about to do
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the insert");
            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
               return;
            }

            xaRes.end(newXid, XAResource.TMSUCCESS);
            JTATest.verboseOut(Thread.currentThread().getName() + ": Using One-Phase, Commit the transaction");
            xaRes.commit(newXid, true);
            JTATest.verboseOut(Thread.currentThread().getName() + ": The transaction is complete");
            // now check to make sure the value was inserted
            assertCondition(checkIns(insStr));
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
         finally {
           if (conn != null) try { conn.close(); } catch (Exception e) {}
         }
      }
   }

   public void Var003() { // from ~kulack/JTA/jtatest/JTAInsert3.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         Connection conn   = null;
         try {
            String insStr = JTATest.getStr();
            JTATest.verboseOut("TEST: INSERT under a TX, then 2 phase rollback (after prepare)");

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

            XADataSource          ds     = newXADataSource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

            // Get the real connection object
            XAResource              xaRes  = xaConn.getXAResource();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "')";

            JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Start a new transaction \"" + newXid.toString() + "\"");

            // Start a transaction for the stuff we're about to do

            xaRes.start(newXid, XAResource.TMNOFLAGS);

            JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the insert");
            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
               return;
            }

            xaRes.end(newXid, XAResource.TMSUCCESS);
            JTATest.verboseOut(Thread.currentThread().getName() + ": Prepare the transaction");
            rc = xaRes.prepare(newXid);
            if (rc != XAResource.XA_OK) {
               failed("Expected an OK! rc=" + rc);
               return;
            }

            JTATest.verboseOut(Thread.currentThread().getName() + ": rollbackt the transaction");
            xaRes.rollback(newXid);
            JTATest.verboseOut(Thread.currentThread().getName() + ": The transaction is complete");
            // now check to make sure the value was not inserted
            assertCondition(!checkIns(insStr));

         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
         finally {
           if (conn != null) try { conn.close(); } catch (Exception e) {}
         }
      }
   }

   public void Var004() { // from ~kulack/JTA/jtatest/JTAInsert4.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         Connection conn   = null;
         try {
            String insStr = JTATest.getStr();
            JTATest.verboseOut("TEST: INSERT under a TX, then 1 phase rollback (no prepare)");

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

            XADataSource          ds     = newXADataSource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

            // Get the real connection object
            XAResource              xaRes  = xaConn.getXAResource();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "')";

            JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Start a new transaction \"" + newXid.toString() + "\"");

            // Start a transaction for the stuff we're about to do

            xaRes.start(newXid, XAResource.TMNOFLAGS);

            JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the insert");
            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
               return;
            }

            xaRes.end(newXid, XAResource.TMSUCCESS);

            JTATest.verboseOut(Thread.currentThread().getName() + ": rollback the transaction without prepare");
            xaRes.rollback(newXid);
            JTATest.verboseOut(Thread.currentThread().getName() + ": The transaction is complete");
            // now check to make sure the value was not inserted
            assertCondition(!checkIns(insStr));
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
         finally {
           if (conn != null) try { conn.close(); } catch (Exception e) {}
         }
      }
   }

   public void Var005() { // from ~kulack/JTA/jtatest/JTAInsert5.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         Connection conn   = null;
         try {
            String insStr = JTATest.getStr();
            JTATest.verboseOut("TEST: Repeatedly insert a row, select the row using 2PC "+insStr);
            int      repetitions  = 10;
            String TABLE = JTATest.COLLECTION + ".INS5TBL";

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

            XADataSource          ds     = newXADataSource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

            // Get the real connection object
            XAResource              xaRes  = xaConn.getXAResource();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            conn   = xaConn.getConnection();

            Statement setupStatement = conn.createStatement();
            try {
               setupStatement.execute("DROP TABLE " + TABLE);
            }
            catch (Exception e) {
            }
            try {
               setupStatement.execute("CREATE TABLE " + TABLE + " (ID INTEGER NOT NULL WITH " +
                                      "DEFAULT, DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
            }
            catch (Exception e) {
               if (JTATest.verbose) {
                  e.printStackTrace();
               }
            }
            conn.commit();

            String sqlInsertString = "INSERT INTO " + TABLE + " VALUES(?, ?)";
            JTATest.verboseOut("Prepare: " + sqlInsertString);
            PreparedStatement insertStmt = conn.prepareStatement(sqlInsertString);

            String sqlQueryString  = "SELECT * FROM " + TABLE + " WHERE ID = ?";
            JTATest.verboseOut("Prepare: " + sqlQueryString);
            PreparedStatement queryStmt = conn.prepareStatement(sqlQueryString);

            JTATest.verboseOut("Try " + repetitions + " repetitions of insert/query");
            int               value;
            int               rc;
            int               rows;
            JTATestXid        newXid = null;

            for (value=0; value < repetitions; ++value) {
               // Do each part of the test (the insert and the query) in seperate
               // transactions.
               JTATest.verboseOut("Insert/Select Transaction pair #" + value);

               ////////////// INSERT ////////////////
               insertStmt.setInt(1, value);
               insertStmt.setString(2, "This is big bad bean #" + value);

               // Generate a new transaction
               newXid = new JTATestXid();
               xaRes.start(newXid, XAResource.TMNOFLAGS);

               JTATest.verboseOut("Insert #" + value);
               rc = insertStmt.executeUpdate();
               if (rc != 1) {
                  failed("Expected 1 row inserted, got " + rc);
                  return;
               }

               xaRes.end(newXid, XAResource.TMSUCCESS);
               rc = xaRes.prepare(newXid);
               if (rc != XAResource.XA_OK) {
                  failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
                  return;
               }

               xaRes.commit(newXid, false);

               ////////////// QUERY ////////////////
               queryStmt.setInt(1, value);

               // Generate a new transaction
               newXid = new JTATestXid();
               xaRes.start(newXid, XAResource.TMNOFLAGS);

               JTATest.verboseOut("Query #" + value);
               ResultSet rs = queryStmt.executeQuery();
               if (rs == null) {
                  failed("Expected a result set! Got none");
                  return;
               }

               rows = 0;
               while (rs.next()) {
                  ++rows;
                  if (rows > 1) {
                     failed("Expected 1 row, got a second row!");
                     return;
                  }
                  rc = rs.getInt(1);
                  if (rc != value) {
                     failed("Expected column 1 value of " + value + " got, " + rc);
                     return;
                  }
               }
               rs.close();

               xaRes.end(newXid, XAResource.TMSUCCESS);

               rc = xaRes.prepare(newXid);
               if (rc != XAResource.XA_RDONLY) {
                  failed("Expected XA_RDONLY (" + XAResource.XA_RDONLY + "), got " + rc);
                  // No commit required for a read only TX.
                  return;
               }

            } /* for i repetitions */


            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
         finally {
           if (conn != null) try { conn.close(); } catch (Exception e) {}
         }
      }
   }

   public void Var006() { // from ~kulack/JTA/jtatest/JTAInsert6.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         JTATestXid        newXid = null;
	 XAResource              xaRes  = null; 
         Connection conn   = null;
         try {
            JTATest.getStr();
            JTATest.verboseOut("TEST: JTA Global TX, Insert rows, prepare and rollback. Validate rollback with a query, repeat ");
            int      repetitions  = 10;
            String TABLE = JTATest.COLLECTION + ".INS6TBL";

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

            XADataSource          ds     = newXADataSource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

            // Get the real connection object
            xaRes  = xaConn.getXAResource();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            conn   = xaConn.getConnection();

            Statement setupStatement = conn.createStatement();
            try {
               setupStatement.execute("DROP TABLE " + TABLE);
            }
            catch (Exception e) {
            }
            try {
               setupStatement.execute("CREATE TABLE " + TABLE + " (ID INTEGER NOT NULL WITH " +
                                      "DEFAULT, DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
            }
            catch (Exception e) {
               if (JTATest.verbose) {
                  e.printStackTrace();
               }
            }
            conn.commit();

            String sqlInsertString = "INSERT INTO " + TABLE + " VALUES(?, ?)";
            JTATest.verboseOut("Prepare: " + sqlInsertString);
            PreparedStatement insertStmt = conn.prepareStatement(sqlInsertString);

            String sqlQueryString  = "SELECT * FROM " + TABLE + " WHERE ID = ?";
            JTATest.verboseOut("Prepare: " + sqlQueryString);
            PreparedStatement queryStmt = conn.prepareStatement(sqlQueryString);

            JTATest.verboseOut("Try " + repetitions + " repetitions of insert/query");
            int               value;
            int               rc;

            for (value=0; value < repetitions; ++value) {
               // Do each part of the test (the insert and the query) in seperate
               // transactions.
               JTATest.verboseOut("Insert/Select Transaction pair #" + value);

               ////////////// INSERT ////////////////
               insertStmt.setInt(1, value);
               insertStmt.setString(2, "This is big bad bean #" + value);

               // Generate a new transaction
               newXid = new JTATestXid();
               xaRes.start(newXid, XAResource.TMNOFLAGS);

               JTATest.verboseOut("Insert #" + value);
               rc = insertStmt.executeUpdate();
               if (rc != 1) {
                  failed("Expected 1 row inserted, got " + rc);
		  xaRes.rollback(newXid);
                  return;
               }

               xaRes.end(newXid, XAResource.TMSUCCESS);
               rc = xaRes.prepare(newXid);
               if (rc != XAResource.XA_OK) {
                  failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
		  xaRes.rollback(newXid);
                  return;
               }

               xaRes.rollback(newXid);

               ////////////// QUERY ////////////////
               queryStmt.setInt(1, value);

               // Generate a new transaction
               newXid = new JTATestXid();
               xaRes.start(newXid, XAResource.TMNOFLAGS);

               JTATest.verboseOut("Query #" + value);
               ResultSet rs = queryStmt.executeQuery();
               if (rs != null) {
                  while (rs.next()) {
                     rc = rs.getInt(1);
                     if (rc != value) {
                        failed("Expected column 1 value of " + value + " got, " + rc);
			xaRes.rollback(newXid);
                        return;
                     }
                     JTATest.verboseOut("Got a value! " + rc);
                     failed("Expected NO result set! Got one");
		     xaRes.rollback(newXid);
                     return;
                  }
                  rs.close();
                  JTATest.verboseOut("Got a result set but no data");
               }
               else {
                  JTATest.verboseOut("Got no rows, as expected\n");
               }

               xaRes.end(newXid, XAResource.TMSUCCESS);

               rc = xaRes.prepare(newXid);
               if (rc != XAResource.XA_RDONLY) {
                  failed("Expected XA_RDONLY (" + XAResource.XA_RDONLY + "), got " + rc);
                  // No commit required for a read only TX.
                  return;
               }

            } /* for i repetitions */


            setupStatement.execute("DROP TABLE " + TABLE);
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
	    if (xaRes != null) {
		try { 
		    xaRes.rollback(newXid);
		} catch (Exception e2) {} 
	    }
         }
         finally {
           if (conn != null) try { conn.close(); } catch (Exception e) {}
         }
      }
   }






}
