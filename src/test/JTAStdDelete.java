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
// File Name:    JTAStdDelete.java
//
// Description:  Same as JTADelete.java but test standard interfaces for
//               JTA & JDBC Std Ext
//
// Classes:      JTAStdDelete
//
////////////////////////////////////////////////////////////////////////
package test;

import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

// this testcase is similar to JTAInsert.java

public class JTAStdDelete extends JTATestcase {

   private String basTbl = JTATest.COLLECTION + ".CHARTABDEL";
   private Connection c;

/**
Constructor.
**/
   public JTAStdDelete (AS400 systemObject,
                        Hashtable namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password) {
      super (systemObject, "JTAStdDelete",
             namesAndVars, runMode, fileOutputStream,
             password);
   }


   public JTAStdDelete (AS400 systemObject,
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
      basTbl = JTATest.COLLECTION + ".CHARTABDEL";
      JTATest.verboseOut(baseURL_);
      c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
      isIasp = JDTestUtilities.isIasp(c); 
      Statement s = c.createStatement();
      s.execute("CREATE TABLE " + basTbl + " (COL1 VARCHAR (16) NOT NULL WITH DEFAULT)");
      s.close();
   }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
   protected void cleanup ()
   throws Exception
   {
      Statement s = c.createStatement();
      s.execute("DROP TABLE " + basTbl);
      s.close();
      c.close();
      unlockSystem(); 
   }

   protected boolean checkDel(String value)
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
      return !result;
   }

   public void Var001() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {

            String insStr = JTATest.getStr();
            JTATest.verboseOut("TEST: DELETE under a TX, then 2 phase prepare/commit");

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

            XADataSource          ds     = newXADataSource();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

            // Get the real connection object
            XAResource              xaRes  = xaConn.getXAResource();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            Connection              conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "')";

            JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            TestXid              newXid = new TestXid();
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

            // commit the insert
            xaRes.end(newXid, XAResource.TMSUCCESS);
            xaRes.commit(newXid, true);

            // Generate a new transaction for the delete
            newXid = new TestXid();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Start a new transaction \"" + newXid.toString() + "\"");
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            String delSql = "DELETE FROM " + basTbl + " WHERE COL1='" + insStr + "'";

            JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + delSql + "\"");
            stmt = conn.prepareStatement(delSql);

            JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the delete");
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row deleted, got " + rc);
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
            // now check to make sure the value was deleted
            assertCondition(checkDel(insStr));

         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
            return;
         }
      }
   }

   public void Var002() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            String insStr = JTATest.getStr();
            JTATest.verboseOut("TEST: DELETE under a TX, then one phase commit");

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

            XADataSource          ds     = newXADataSource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

            // Get the real connection object
            XAResource              xaRes  = xaConn.getXAResource();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            Connection              conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "')";

            JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            TestXid              newXid = new TestXid();

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
            else {
               // commit the insert
               xaRes.end(newXid, XAResource.TMSUCCESS);
               xaRes.commit(newXid, true);

               // Generate a new transaction for the delete
               newXid = new TestXid();
               JTATest.verboseOut(Thread.currentThread().getName() + ": Start a new transaction \"" + newXid.toString() + "\"");
               xaRes.start(newXid, XAResource.TMNOFLAGS);

               String delSql = "DELETE FROM " + basTbl + " WHERE COL1='" + insStr + "'";

               JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + delSql + "\"");
               stmt = conn.prepareStatement(delSql);

               JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the delete");
               rc = stmt.executeUpdate();
               if (rc != 1) {
                  failed("Expected 1 row deleted, got " + rc);
                  return;
               }
               else {
                  JTATest.verboseOut(Thread.currentThread().getName() + ": Using One-Phase, Commit the transaction");
                  xaRes.end(newXid, XAResource.TMSUCCESS);
                  xaRes.commit(newXid, true);
                  JTATest.verboseOut(Thread.currentThread().getName() + ": The transaction is complete");
                  // now check to make sure the value was deleted
                  assertCondition(checkDel(insStr));
               }

            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
            return;
         }
      }
   }

   public void Var003() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            String insStr = JTATest.getStr();
            JTATest.verboseOut("TEST: DELETE under a TX, then 2 phase rollback (after prepare)");

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

            XADataSource          ds     = newXADataSource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

            // Get the real connection object
            XAResource              xaRes  = xaConn.getXAResource();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            Connection              conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "')";

            JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            TestXid              newXid = new TestXid();
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
            // commit the insert
            xaRes.end(newXid, XAResource.TMSUCCESS);
            xaRes.commit(newXid, true);

            // Generate a new transaction for the delete
            newXid = new TestXid();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Start a new transaction \"" + newXid.toString() + "\"");
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            String delSql = "DELETE FROM " + basTbl + " WHERE COL1='" + insStr + "'";

            JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + delSql + "\"");
            stmt = conn.prepareStatement(delSql);
            JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the delete");
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row deleted, got " + rc);
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
            // now check to make sure the value was not deleted
            assertCondition(!checkDel(insStr));

         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
            return;
         }
      }
   }

   public void Var004() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            String insStr = JTATest.getStr();
            JTATest.verboseOut("TEST: DELETE under a TX, then 1 phase rollback (no prepare)");

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

            XADataSource          ds     = newXADataSource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

            // Get the real connection object
            XAResource              xaRes  = xaConn.getXAResource();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            Connection              conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "')";

            JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            TestXid              newXid = new TestXid();
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
            // commit the insert
            xaRes.end(newXid, XAResource.TMSUCCESS);
            xaRes.commit(newXid, true);

            // Generate a new transaction for the delete
            newXid = new TestXid();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Start a new transaction \"" + newXid.toString() + "\"");
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            String delSql = "DELETE FROM " + basTbl + " WHERE COL1='" + insStr + "'";

            JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + delSql + "\"");
            stmt = conn.prepareStatement(delSql);

            JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the delete");
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row deleted, got " + rc);
               return;
            }
            xaRes.end(newXid, XAResource.TMSUCCESS);

            JTATest.verboseOut(Thread.currentThread().getName() + ": rollback the transaction without prepare");
            xaRes.rollback(newXid);
            JTATest.verboseOut(Thread.currentThread().getName() + ": The transaction is complete");
            // now check to make sure the value was not deleted
            assertCondition(!checkDel(insStr));
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
            return;
         }
      }
   }

   public void Var005() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            String insStr = JTATest.getStr();
            JTATest.verboseOut("TEST: Repeatedly delete a row, select the row using 2PC "+insStr);
            int      repetitions  = 10;
            String TABLE = JTATest.COLLECTION + ".DEL5TBL";

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

            XADataSource          ds     = newXADataSource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

            // Get the real connection object
            XAResource              xaRes  = xaConn.getXAResource();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            Connection              conn   = xaConn.getConnection();

            Statement setupStatement = conn.createStatement();
            try {
               setupStatement.execute("DROP TABLE " + TABLE);
            }
            catch (Exception e) {
            }
            try {
               setupStatement.execute("CREATE TABLE " + TABLE + " (ID INTEGER NOT NULL WITH " +
                                      "DEFAULT, DESCRIPTION VARCHAR (256 ) NOT NULL WITH DEFAULT)");
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

            JTATest.verboseOut("Try " + repetitions + " repetitions of delete/query");
            int               value;
            int               rc;
            int               rows;
            TestXid        newXid = null;

            for (value=0; value < repetitions; ++value) {
               // Do each part of the test (the insert and the query) in seperate
               // transactions.
               JTATest.verboseOut("Delete/Select Transaction pair #" + value);

               ////////////// INSERT ////////////////
               insertStmt.setInt(1, value);
               insertStmt.setString(2, "This is big bad bean #" + value);

               // Generate a new transaction
               newXid = new TestXid();
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

               ////////////// DELETE ////////////////
               String sqlDeleteString = "DELETE FROM " + TABLE + " WHERE ID=" + value;
               JTATest.verboseOut("Prepare: " + sqlDeleteString);
               PreparedStatement deleteStmt = conn.prepareStatement(sqlDeleteString);
               // Generate a new transaction
               newXid = new TestXid();
               xaRes.start(newXid, XAResource.TMNOFLAGS);

               JTATest.verboseOut("Delete #" + value);
               rc = deleteStmt.executeUpdate();
               if (rc != 1) {
                  failed("Expected 1 row deleted, got " + rc);
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
               newXid = new TestXid();
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
                  if (rows > 0) {
                     failed("Expected 0 row, got a row!");
                     return;
                  }
               }
               rs.close();

               xaRes.end(newXid, XAResource.TMSUCCESS);

               rc = xaRes.prepare(newXid);
               if (rc != XAResource.XA_RDONLY) {
                  failed("Expected XA_RDONLY (" + XAResource.XA_RDONLY + "), got " + rc);
                  return;
                  // No commit required for a read only TX.
               }

            } /* for i repetitions */


            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
            return;
         }
      }
   }

   public void Var006() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            String insStr = JTATest.getStr();
            JTATest.verboseOut("TEST: JTA Global TX, Delete rows, prepare and rollback. Validate rollback with a query, repeat "+insStr);
            int      repetitions  = 10;
            String TABLE = JTATest.COLLECTION + ".DEL6TBL";

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

            XADataSource          ds     = newXADataSource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

            // Get the real connection object
            XAResource              xaRes  = xaConn.getXAResource();
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            Connection              conn   = xaConn.getConnection();

            Statement setupStatement = conn.createStatement();
            try {
               setupStatement.execute("DROP TABLE " + TABLE);
            }
            catch (Exception e) {
            }
            try {
               setupStatement.execute("CREATE TABLE " + TABLE + " (ID INTEGER NOT NULL WITH " +
                                      "DEFAULT, DESCRIPTION VARCHAR (256 ) NOT NULL WITH DEFAULT)");
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

            JTATest.verboseOut("Try " + repetitions + " repetitions of delete/query");
            int               value;
            int               rc;
            TestXid        newXid = null;

            for (value=0; value < repetitions; ++value) {
               // Do each part of the test (the insert and the query) in seperate
               // transactions.
               JTATest.verboseOut("Delete/Select Transaction pair #" + value);

               ////////////// INSERT ////////////////
               insertStmt.setInt(1, value);
               insertStmt.setString(2, "This is big bad bean #" + value);

               // Generate a new transaction
               newXid = new TestXid();
               xaRes.start(newXid, XAResource.TMNOFLAGS);

               JTATest.verboseOut("Insert #" + value);
               rc = insertStmt.executeUpdate();
               if (rc != 1) {
                  failed("Expected 1 row inserted, got " + rc);
                  return;
               }
               xaRes.end(newXid, XAResource.TMSUCCESS);
               xaRes.commit(newXid, true);

               ////////////// DELETE ////////////////
               // Generate a new transaction
               newXid = new TestXid();
               xaRes.start(newXid, XAResource.TMNOFLAGS);
               String sqlDeleteString = "DELETE FROM " + TABLE + " WHERE ID=" + value;
               JTATest.verboseOut("Prepare: " + sqlDeleteString);
               PreparedStatement deleteStmt = conn.prepareStatement(sqlDeleteString);

               JTATest.verboseOut("Delete #" + value);
               rc = deleteStmt.executeUpdate();
               if (rc != 1) {
                  failed("Expected 1 row deleted, got " + rc);
                  return;
               }

               xaRes.end(newXid, XAResource.TMSUCCESS);
               rc = xaRes.prepare(newXid);
               if (rc != XAResource.XA_OK) {
                  failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
                  return;
               }

               xaRes.rollback(newXid);

               ////////////// QUERY ////////////////
               queryStmt.setInt(1, value);

               // Generate a new transaction
               newXid = new TestXid();
               xaRes.start(newXid, XAResource.TMNOFLAGS);

               JTATest.verboseOut("Query #" + value);
               ResultSet rs = queryStmt.executeQuery();
               if (rs != null) {
                  while (rs.next()) {
                     rc = rs.getInt(1);
                     if (rc != value) {
                        failed("Expected column 1 value of " + value + " got, " + rc);
                        return;
                     }
                     JTATest.verboseOut("Got a value! " + rc);
                  }
                  rs.close();
               }
               else {
                  failed("Got no rows");
                  return;
               }

               xaRes.end(newXid, XAResource.TMSUCCESS);

               rc = xaRes.prepare(newXid);
               if (rc != XAResource.XA_RDONLY) {
                  failed("Expected XA_RDONLY (" + XAResource.XA_RDONLY + "), got " + rc);
                  return;
                  // No commit required for a read only TX.
               }

            } /* for i repetitions */


            setupStatement.execute("DROP TABLE " + TABLE);
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
            return;
         }
      }
   }
}
