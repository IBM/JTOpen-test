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

//import java.lang.*;
import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JTATest;
import test.JD.JDTestUtilities;

//import com.ibm.db2.jdbc.app.stdext.javax.sql.DataSource;
//import com.ibm.db2.jdbc.app.stdext.javax.sql.XADataSource;
//import com.ibm.db2.jdbc.app.stdext.javax.sql.XAConnection;
//import com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAResource;
import javax.sql.XADataSource;

// this testcase is similar to JTAInsert.java

public class JTAUpdate extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAUpdate";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTATest.main(newArgs); 
   }

   private String basTbl = JTATest.COLLECTION + ".CHARTABJTU";
   private Connection c;
   boolean isIasp = false; ;
   boolean isNTS = false; 

/**
Constructor.
**/
   public JTAUpdate (AS400 systemObject,
                     Hashtable namesAndVars,
                     int runMode,
                     FileOutputStream fileOutputStream,
                     
                     String password) {
      super (systemObject, "JTAUpdate",
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

      basTbl = JTATest.COLLECTION + ".CHARTABJTU";
      if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
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
      if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
         Statement s = c.createStatement();
         s.execute("DROP TABLE " + basTbl);
         s.close();
         c.close();
      }
      unlockSystem(); 
   }

   protected boolean checkUpd(String value)
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

   public void Var001() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {

               String insStr = JTATest.getStr();
               JTATest.verboseOut("TEST: UPDATE under a TX, then 2 phase prepare/commit");

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

               javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");

               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
               JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
               // Get the XAConnection.

               Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

               // Get the real connection object
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "')";

               JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + sqlString + "\"");
               PreparedStatement stmt = conn.prepareStatement(sqlString);

               // Generate a new transaction
               JTATestXid              newXid = new JTATestXid();
               JTATest.verboseOut(Thread.currentThread().getName() + ": Start a new transaction \"" + newXid.toString() + "\"");

               // Start a transaction for the stuff we're about to do

               JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

               JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the insert");
               int      rc;
               rc = stmt.executeUpdate();
               if (rc != 1) {
                  failed("Expected 1 row inserted, got " + rc);
                  return;
               }


               String updStr = JTATest.getStr();
               String updSql = "UPDATE " + basTbl + " SET COL1='" + updStr + "' WHERE COL1='" + insStr + "'";

               JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + updSql + "\"");
               stmt = conn.prepareStatement(updSql);

               JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the update");
               rc = stmt.executeUpdate();
               if (rc != 1) {
                  failed("Expected 1 row updated, got " + rc);
                  return;
               }

               JTATest.verboseOut(Thread.currentThread().getName() + ": End transaction \"" + newXid.toString() + "\"");
               JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
               JTATest.verboseOut(Thread.currentThread().getName() + ": Prepare the transaction");
               rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
               if (rc != javax.transaction.xa.XAResource.XA_OK) {
                  failed("Expected an OK! rc=" + rc);
                  return;
               }

               JTATest.verboseOut(Thread.currentThread().getName() + ": Commit the transaction");
               JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);
               JTATest.verboseOut(Thread.currentThread().getName() + ": The transaction is complete");
               // now check to make sure the value was updated
               assertCondition(checkUpd(updStr));

            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
               return;
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
               String insStr = JTATest.getStr();
               JTATest.verboseOut("TEST: UPDATE under a TX, then one phase commit");

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

               javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");

               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
               JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
               // Get the XAConnection.

               Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

               // Get the real connection object
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "')";

               JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + sqlString + "\"");
               PreparedStatement stmt = conn.prepareStatement(sqlString);

               // Generate a new transaction
               JTATestXid              newXid = new JTATestXid();

               JTATest.verboseOut(Thread.currentThread().getName() + ": Start a new transaction \"" + newXid.toString() + "\"");
               // Start a transaction for the stuff we're about to do
               JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

               JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the insert");
               int      rc;
               rc = stmt.executeUpdate();
               if (rc != 1) {
                  failed("Expected 1 row inserted, got " + rc);
                  return;
               }

               String updStr = JTATest.getStr();
               String updSql = "UPDATE " + basTbl + " SET COL1='" + updStr + "' WHERE COL1='" + insStr + "'";

               JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + updSql + "\"");
               stmt = conn.prepareStatement(updSql);

               JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the update");
               rc = stmt.executeUpdate();
               if (rc != 1) {
                  failed("Expected 1 row updated, got " + rc);
                  return;
               }

               JTATest.verboseOut(Thread.currentThread().getName() + ": End transaction \"" + newXid.toString() + "\"");
               JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
               JTATest.verboseOut(Thread.currentThread().getName() + ": Using One-Phase, Commit the transaction");
               JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, true);
               JTATest.verboseOut(Thread.currentThread().getName() + ": The transaction is complete");
               // now check to make sure the value was updated
               assertCondition(checkUpd(updStr));
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
               return;
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
               String insStr = JTATest.getStr();
               JTATest.verboseOut("TEST: UPDATE under a TX, then 2 phase rollback (after prepare)");

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

               javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");

               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
               JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
               // Get the XAConnection.

               Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

               // Get the real connection object
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "')";

               JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + sqlString + "\"");
               PreparedStatement stmt = conn.prepareStatement(sqlString);

               // Generate a new transaction
               JTATestXid              newXid = new JTATestXid();
               JTATest.verboseOut(Thread.currentThread().getName() + ": Start a new transaction \"" + newXid.toString() + "\"");

               // Start a transaction for the stuff we're about to do

               JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

               JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the insert");
               int      rc;
               rc = stmt.executeUpdate();
               if (rc != 1) {
                  failed("Expected 1 row inserted, got " + rc);
                  return;
               }

               String updStr = JTATest.getStr();
               String updSql = "UPDATE " + basTbl + " SET COL1='" + updStr + "' WHERE COL1='" + insStr + "'";

               JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + updSql + "\"");
               stmt = conn.prepareStatement(updSql);

               JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the update");
               rc = stmt.executeUpdate();
               if (rc != 1) {
                  failed("Expected 1 row updated, got " + rc);
                  return;
               }

               JTATest.verboseOut(Thread.currentThread().getName() + ": End transaction \"" + newXid.toString() + "\"");
               JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
               JTATest.verboseOut(Thread.currentThread().getName() + ": Prepare the transaction");
               rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
               if (rc != javax.transaction.xa.XAResource.XA_OK) {
                  failed("Expected an OK! rc=" + rc);
                  return;
               }

               JTATest.verboseOut(Thread.currentThread().getName() + ": rollbackt the transaction");
               JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);
               JTATest.verboseOut(Thread.currentThread().getName() + ": The transaction is complete");
               // now check to make sure the value was not updated
               assertCondition(!checkUpd(updStr));

            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
               return;
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
               String insStr = JTATest.getStr();
               JTATest.verboseOut("TEST: UPDATE under a TX, then 1 phase rollback (no prepare)");

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

               javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");

               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
               JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
               // Get the XAConnection.

               Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

               // Get the real connection object
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "')";

               JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + sqlString + "\"");
               PreparedStatement stmt = conn.prepareStatement(sqlString);

               // Generate a new transaction
               JTATestXid              newXid = new JTATestXid();
               JTATest.verboseOut(Thread.currentThread().getName() + ": Start a new transaction \"" + newXid.toString() + "\"");

               // Start a transaction for the stuff we're about to do

               JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

               JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the insert");
               int      rc;
               rc = stmt.executeUpdate();
               if (rc != 1) {
                  failed("Expected 1 row inserted, got " + rc);
                  return;
               }

               String updStr = JTATest.getStr();
               String updSql = "UPDATE " + basTbl + " SET COL1='" + updStr + "' WHERE COL1='" + insStr + "'";

               JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + updSql + "\"");
               stmt = conn.prepareStatement(updSql);

               JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the update");
               rc = stmt.executeUpdate();
               if (rc != 1) {
                  failed("Expected 1 row updated, got " + rc);
                  return;
               }

               JTATest.verboseOut(Thread.currentThread().getName() + ": End transaction \"" + newXid.toString() + "\"");
               JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
               JTATest.verboseOut(Thread.currentThread().getName() + ": rollback the transaction without prepare");
               JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);
               JTATest.verboseOut(Thread.currentThread().getName() + ": The transaction is complete");
               // now check to make sure the value was not updated
               assertCondition(!checkUpd(updStr));
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
               return;
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
               // String insStr = JTATest.getStr();
               JTATest.verboseOut("TEST: Repeatedly update a row, select the row using 2PC");
               int      repetitions  = 10;
               String TABLE = JTATest.COLLECTION + ".UPD5TBL";

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

               javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");

               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
               JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
               // Get the XAConnection.

               Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

               // Get the real connection object
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

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

               JTATest.verboseOut("Try " + repetitions + " repetitions of update/query");
               int               value;
               int               rc;
               int               rows;
               JTATestXid        newXid = null;

               for (value=0; value < repetitions; ++value) {
                  // Do each part of the test (the insert and the query) in seperate
                  // transactions.
                  JTATest.verboseOut("Update/Select Transaction pair #" + value);

                  ////////////// INSERT ////////////////
                  insertStmt.setInt(1, value);
                  insertStmt.setString(2, "This is big bad bean #" + value);

                  // Generate a new transaction
                  newXid = new JTATestXid();
                  JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

                  JTATest.verboseOut("Insert #" + value);
                  rc = insertStmt.executeUpdate();
                  if (rc != 1) {
                     failed("Expected 1 row inserted, got " + rc);
                     return;
                  }

                  JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
                  rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
                  if (rc != javax.transaction.xa.XAResource.XA_OK) {
                     failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
                     return;
                  }

                  JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);

                  ////////////// UPDATE ////////////////
                  String updDesc = JTATest.getStr();
                  String sqlUpdateString = "UPDATE " + TABLE + " SET DESCRIPTION='" + updDesc + "' WHERE ID=" + value;
                  JTATest.verboseOut("Prepare: " + sqlUpdateString);
                  PreparedStatement updateStmt = conn.prepareStatement(sqlUpdateString);
                  // Generate a new transaction
                  newXid = new JTATestXid();
                  JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

                  JTATest.verboseOut("Update #" + value);
                  rc = updateStmt.executeUpdate();
                  if (rc != 1) {
                     failed("Expected 1 row updated, got " + rc);
                     return;
                  }

                  JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
                  rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
                  if (rc != javax.transaction.xa.XAResource.XA_OK) {
                     failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
                     return;
                  }

                  JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);

                  ////////////// QUERY ////////////////
                  queryStmt.setInt(1, value);

                  // Generate a new transaction
                  newXid = new JTATestXid();
                  JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

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
                     String s2 = rs.getString(2);
                     if (!s2.equals(updDesc)) {
                        failed("Expected column 2 value of " + updDesc + " got, " + s2);
                        return;
                     }
                  }
                  rs.close();

                  JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

                  rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
                  if (rc != javax.transaction.xa.XAResource.XA_RDONLY) {
                     failed("Expected XA_RDONLY (" + javax.transaction.xa.XAResource.XA_RDONLY + "), got " + rc);
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
   }

   public void Var006() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkNative ()) {
         if (checkJdbc20StdExt()) {
            try {
               // String insStr = JTATest.getStr();
               JTATest.verboseOut("TEST: JTA Global TX, Update rows, prepare and rollback. Validate rollback with a query, repeat");
               int      repetitions  = 10;
               String TABLE = JTATest.COLLECTION + ".UPD6TBL";

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");

               javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");

               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
               JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
               // Get the XAConnection.

               Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

               // Get the real connection object
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

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

               JTATest.verboseOut("Try " + repetitions + " repetitions of update/query");
               int               value;
               int               rc;
               // int               rows;
               JTATestXid        newXid = null;

               for (value=0; value < repetitions; ++value) {
                  // Do each part of the test (the insert and the query) in seperate
                  // transactions.
                  JTATest.verboseOut("Update/Select Transaction pair #" + value);

                  ////////////// INSERT ////////////////
                  insertStmt.setInt(1, value);
                  insertStmt.setString(2, "This is big bad bean #" + value);

                  // Generate a new transaction
                  newXid = new JTATestXid();
                  JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

                  JTATest.verboseOut("Insert #" + value);
                  rc = insertStmt.executeUpdate();
                  if (rc != 1) {
                     failed("Expected 1 row inserted, got " + rc);
                     return;
                  }


                  ////////////// UPDATE ////////////////
                  String updDesc = JTATest.getStr();
                  String sqlUpdateString = "UPDATE " + TABLE + " SET DESCRIPTION='" + updDesc + "' WHERE ID=" + value;
                  JTATest.verboseOut("Prepare: " + sqlUpdateString);
                  PreparedStatement updateStmt = conn.prepareStatement(sqlUpdateString);

                  JTATest.verboseOut("Update #" + value);
                  rc = updateStmt.executeUpdate();
                  if (rc != 1) {
                     failed("Expected 1 row updated, got " + rc);
                     return;
                  }

                  JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
                  rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
                  if (rc != javax.transaction.xa.XAResource.XA_OK) {
                     failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
                     return;
                  }

                  JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);

                  ////////////// QUERY ////////////////
                  queryStmt.setInt(1, value);

                  // Generate a new transaction
                  newXid = new JTATestXid();
                  JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

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
                        failed("Expected NO result set! Got one");
                        return;
                     }
                     rs.close();
                     JTATest.verboseOut("Got a result set but no data");
                  }
                  else {
                     JTATest.verboseOut("Got no rows, as expected\n");
                  }

                  JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

                  rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
                  if (rc != javax.transaction.xa.XAResource.XA_RDONLY) {
                     failed("Expected XA_RDONLY (" + javax.transaction.xa.XAResource.XA_RDONLY + "), got " + rc);
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






}
