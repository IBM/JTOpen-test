package test;

// import java.lang.*;
import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

//import com.ibm.db2.jdbc.app.stdext.javax.sql.DataSource;
//import com.ibm.db2.jdbc.app.stdext.javax.sql.XADataSource;
//import com.ibm.db2.jdbc.app.stdext.javax.sql.XAConnection;
//import com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAResource;
//import com.ibm.db2.jdbc.app.jta.javax.transaction.xa.Xid;
//import com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException;
import javax.sql.XADataSource;

public class JTAMisc extends JDTestcase {

   TestXid[] xids = new TestXid[10];

   boolean isIasp = false; 
   boolean isNTS = false; 
   Connection c; 
/**
Constructor.
**/
   public JTAMisc (AS400 systemObject,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   
                   String password) {
      super (systemObject, "JTAMisc",
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


   private class Recover {
      XADataSource            xaDs;
      Object               xaConn;
      Object                 xaRes;
      Connection                 conn;
      TestXid                 newXid = null;

      String TABLE = JTATest.COLLECTION + ".RECOV1TBL";

      Recover(String rdb, int tableNumber) throws Exception {
         this(rdb, tableNumber, -1);
      }

      Recover(String rdb, int tableNumber, int formatId) throws Exception {
         TABLE = JTATest.COLLECTION + ".RECOV1TBL";
         JTATest.verboseOut(Thread.currentThread().getName() +
                            ": New transaction.");
         JTATest.verboseOut(Thread.currentThread().getName() +
                            ": Get a DataSource");

         xaDs = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(xaDs,"setDatabaseName",rdb);

         JTATest.verboseOut(Thread.currentThread().getName() +
                            ": Get an XAConnection");

         // Get the XAConnection.
         xaConn = xaDs.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
         JTATest.verboseOut(Thread.currentThread().getName() +
                            ": Get the XAResource");
         xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         // Get the real connection object
         JTATest.verboseOut(Thread.currentThread().getName() +
                            ": Get the Connection");
         conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         // Generate a new transaction
         newXid = new TestXid(formatId);
         JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                            "XAResource.start");
         JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         Statement setupStatement = conn.createStatement();
         JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                            "Create table " + TABLE + tableNumber);
         setupStatement.execute("CREATE TABLE " + TABLE + tableNumber +
                                " (ID INTEGER NOT NULL WITH " +
                                "DEFAULT, DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");

         JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                            "XAResource.end");
         JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                            "XAResource.prepare");
         JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
         JTATest.verboseOut("Cleanup");
         setupStatement.close();
         conn.close();

      }
   }



   private class Timeout {
      XADataSource            xaDs;
      Object               xaConn;
      Object                 xaRes;
      Connection                 conn;
      TestXid                 newXid = null;

      String TABLE = JTATest.COLLECTION + ".TIM1TBL";

      Timeout(String rdb, int tableNumber, boolean endTx)
      throws Exception {

         TABLE = JTATest.COLLECTION + ".TIM1TBL";
         JTATest.verboseOut(Thread.currentThread().getName() +
                            ": New transaction. Will be ended == " + endTx);
         JTATest.verboseOut(Thread.currentThread().getName() +
                            ": Get a DataSource");
         xaDs = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(xaDs,"setDatabaseName",rdb);

         JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
         // Get the XAConnection.
         xaConn = xaDs.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

         JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");
         // Get the real connection object
         xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");

         JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
         conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         // Generate a new transaction
         newXid = new TestXid();
         xids[tableNumber] = newXid;
         JTATest.verboseOut(Thread.currentThread().getName() + ": " + "XAResource.start");
         JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         Statement setupStatement = conn.createStatement();
         JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Create table " + TABLE + tableNumber);
         setupStatement.execute("CREATE TABLE " + TABLE + tableNumber +
                                " (ID INTEGER NOT NULL WITH " +
                                "DEFAULT, DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");

         if (endTx) {
            JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                               "XAResource.end(DB2XAResource.TMTIMEOUT)");
            try {
               JDReflectionUtil.callMethod_V(xaRes,"end",newXid, 0x100 /*DB2XAResource.TMTIMEOUT */);

            }
            catch (Exception e) {
               if (JDReflectionUtil.getField_I(e,"errorCode") != javax.transaction.xa.XAException.XA_RBROLLBACK) {
                  throw e;
               }
            }
            // Since ending the transaction with TMTIMEOUT should never
            // roll back the transaction (only mark it rollback only),
            // we'll try to commit the transaction (it should fail),
            // then we'll rollback the transaction.
            boolean failTest = false;
            try {
               JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, true);
               failTest = true;
               //failed("Committed a transaction that was marked rollback only with TMTIMEOUT");
            }
            catch (Exception e) {
               // OK.
            }
            if (failTest) {
               throw new IllegalArgumentException("Committed a transaction that was marked rollback only with TMTIMEOUT");
            }
            JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);
         }
         // Don't do anything more we want the connection, statement and
         // all that jazz to still be present, but the Tx rolled back.
      }
   }




   public void Var001() {             // from ~kulack/JTA/jtatest/JTARecover.java
      if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                  "TEST: Start a bunch of transactions, prepare them, then do a recovery scan. All will be rolled back");
               int      repetitions     = 10;
               boolean  doOnlyRecovery = false;

               if (!doOnlyRecovery) {
                  Recover     txList[] = new Recover[repetitions];
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                     "Creating a bunch of prepare transactions");
                  for (int i=0; i<repetitions; ++i) {
                     txList[i] = new Recover(system_, i);
                  }
               }


               JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                  "Now, using XAResource.recover() to find all " +
                                  "transactions we just put into the in-doubt phase");


               javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               // Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               Object xids[];
               JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                  "Calling XAResource.recover(javax.transaction.xa.XAResource.TMSTARTRSCAN)");
               xids = (Object[]) JDReflectionUtil.callMethod_O(xaRes,"recover",javax.transaction.xa.XAResource.TMSTARTRSCAN);
               if (xids == null || xids.length == 0) {
                  failed("Expected some transactions!");
                  return;
               }

               boolean  done  = false;
               int      count = 0;
               // It may be that the XIDs were heuristically rolled back or committed
               // (If this testcase is being called with a 'doOnlyRecovery' value of true in order
               //  to forget about existing transactions.
               boolean  heuRollback = false;
               boolean  heuCommit   = false;

               while (!done) {
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                     "Scan found " + xids.length + " transactions");
                  count += xids.length;
                  for (int i=0; i< xids.length; ++i) {
                     JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                        "Rollback " + xids[i]);
                     try {
                        JDReflectionUtil.callMethod_V(xaRes,"rollback",xids[i]);
                        JTATest.verboseOut("Rollback completed");
                        heuRollback = false;
                        heuCommit   = false;
                     }
                     catch (Exception e) {
                        JTATest.verboseOut("Rollback rc=" + JDReflectionUtil.getField_I(e,"errorCode"));
                        if (JDReflectionUtil.getField_I(e,"errorCode") == javax.transaction.xa.XAException.XA_HEURCOM) {
                           JTATest.verboseOut("The exception was already committed");
                           heuCommit = true;

                        }
                        else if (JDReflectionUtil.getField_I(e,"errorCode") == javax.transaction.xa.XAException.XA_HEURRB) {
                           JTATest.verboseOut("The exception was already rolled back");
                           heuRollback = true;
                        }
                        else {
                           throw e;
                        }
                     }

                     if (heuRollback || heuCommit) {
                        JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                           "Forget " + xids[i]);
                        JDReflectionUtil.callMethod_V(xaRes, "forget", xids[i]);
                     }
                     else {
                        JTATest.verboseOut("Skipped Forget");
                     }
                  }

                  JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                     "Calling XAResource.recover(javax.transaction.xa.XAResource.TMNOFLAGS)");
                  xids = (Object[]) JDReflectionUtil.callMethod_O(xaRes,"recover",javax.transaction.xa.XAResource.TMNOFLAGS);
                  if (xids == null || xids.length == 0) {
                     JTATest.verboseOut("End of scans");
                     done = true;
                  }
               }

               // TODO: Other transactional work on the system using XA may cause
               // TODO: us to see those transactions.
               if (!doOnlyRecovery) {
                  if (count != repetitions) {
                     failed("Expected " + repetitions + " in doubt transactions, found " + count);
                     return;
                  }
               }

               JTATest.verboseOut(Thread.currentThread().getName() + ": " +"Done");
               assertCondition(true);
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }


   public void Var002() { // from ~kulack/JTA/jtatest/JTASelect.java
      if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut("TEST: SELECT under a TX, then prepare (read only) commit");

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");
               javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");

               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
               // Get the XAConnection.
               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");
               // Get the real connection object
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");

               JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
               Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               String sqlString = "SELECT * FROM QSYS2.SYSTABLES";
               JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + sqlString + "\"");
               PreparedStatement stmt = conn.prepareStatement(sqlString);


               // Generate a new transaction
               TestXid              newXid = new TestXid();

               JTATest.verboseOut(Thread.currentThread().getName() + ": Start a new transaction \"" + newXid.toString() + "\"");
               // Start a transaction for the stuff we're about to do
               JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

               try {
                  JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the query");
                  ResultSet rs = stmt.executeQuery();

                  int         i=0;
                  while (rs.next()) {
                     JTATest.verboseOut(Thread.currentThread().getName() + ": Col1 = " + rs.getString(1));
                     ++i;
                     if (i >= 5) {
                        JTATest.verboseOut(Thread.currentThread().getName() + ": Finished Reading 5 rows");
                        break;
                     }
                  }
                  if (rs != null) {
                     JTATest.verboseOut(Thread.currentThread().getName() + ": close results");
                     rs.close();
                  }

                  int      rc;
                  JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
                  JTATest.verboseOut(Thread.currentThread().getName() + ": Prepare the transaction");
                  rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
                  if (rc != javax.transaction.xa.XAResource.XA_RDONLY) {
                     JTATest.verboseOut(Thread.currentThread().getName() + ": Expected a read only result! rc=" + rc);
                     failed("Expected XA_RDONLY (" + javax.transaction.xa.XAResource.XA_RDONLY + "), got " + rc);
                  }
                  JTATest.verboseOut(Thread.currentThread().getName() + ": The read only transaction is completed");
               }
               catch (Exception e) {
                  failed(e, "Unexpected exception");
               }

               JTATest.verboseOut("Done");
               assertCondition(true);
            }
            catch (Exception e) {
               failed(e, "Unexpected exception");
            }
         }
      }
   }

   public void Var003() {    // from ~kulack/JTA/jtatest/JTATimeout.java
      if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                  "TEST: Start a bunch of transactions, then use XAResource.end(TMTIMEOUT). All should be rolled back");

               int      repetitions     = 10;
               boolean  alternate = false; // All should timeout.
               boolean  shouldEnd = true;


               Timeout     txList[] = new Timeout[repetitions];
               for (int i=0; i<repetitions; ++i) {
                  txList[i] = new Timeout(system_, i, shouldEnd);
                  if (alternate) {
                     shouldEnd = !shouldEnd;
                  }
               }

               // the table(s) should not exist. Should have been rolled back
               boolean result = false;

               Connection c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
               Statement s = c.createStatement();
               for (int i=0; i<repetitions; ++i) {
                  try {
                     s.executeQuery("SELECT * FROM " + txList[i].TABLE + i);
                     failed("Did not throw exception");
                  }
                  catch (SQLException e) {
                     result = true;
                  }
               }
               s.close();
               c.close();

               TransInfo[] match = JTATest.getTransInfo();
               boolean isTransPresent = false;
               if (match == null) {
                  isTransPresent = false;
               }
               else {
                  for (int i = 0; (i < match.length) && (match[i] != null); i++) {
                     for (int j = 0; j < repetitions; j++)
                        isTransPresent = isTransPresent || xids[j].match(match[i]);
                  }
               }
               result = result && !isTransPresent;
               assertCondition(result);
               JTATest.verboseOut(Thread.currentThread().getName() + ": " +"Done");
            }
            catch (Exception e) {
               failed(e, "Unexpected exception");
            }
         }
      }
   }

   // Duplicate of Var001, but we try various formatIds here because of V4R5 bug
   // described by JAVA PTR 9923576.
   public void Var004() {             // from ~kulack/JTA/jtatest/JTARecover.java
      if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                  "TEST: Start a bunch of transactions with various formatIds, prepare them, then do a recovery scan. All will be rolled back");
               int      repetitions     = 10;
               boolean  doOnlyRecovery = false;
               int      formatId;
               int      fmtIdList[] = new int[] {
                  // Everything other than -1 should be valid.
                  // Be sure to get some combinations of negative/posetive numbers,
                  // and number that contain individual bytes that are 'negative' i.e. 0x8y
                  0x00818283, 1, 0x88000000, 0xFFFFFFF0, 0,
                        -2, -987653, 0x00008000, 0x00000081, 113577 /* 113577 used by websphere */
               };

               if (!doOnlyRecovery) {
                  Recover     txList[] = new Recover[repetitions];
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                     "Creating a bunch of prepare transactions");
                  for (int i=0; i<repetitions; ++i) {
                     formatId = fmtIdList[i];
                     txList[i] = new Recover(system_, i, formatId);
                  }
               }


               JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                  "Now, using XAResource.recover() to find all " +
                                  "transactions we just put into the in-doubt phase");


               javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               // Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               Object xids[];
               JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                  "Calling XAResource.recover(javax.transaction.xa.XAResource.TMSTARTRSCAN)");
               xids = (Object[]) JDReflectionUtil.callMethod_O(xaRes,"recover",javax.transaction.xa.XAResource.TMSTARTRSCAN);
               if (xids == null || xids.length == 0) {
                  failed("Expected some transactions!");
                  return;
               }

               boolean  done  = false;
               int      count = 0;
               // It may be that the XIDs were heuristically rolled back or committed
               // (If this testcase is being called with a 'doOnlyRecovery' value of true in order
               //  to forget about existing transactions.
               boolean  heuRollback = false;
               boolean  heuCommit   = false;

               while (!done) {
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                     "Scan found " + xids.length + " transactions");
                  count += xids.length;
                  for (int i=0; i<xids.length; ++i) {
                     JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                        "Rollback " + xids[i]);
                     try {
                        JDReflectionUtil.callMethod_V(xaRes,"rollback",xids[i]);
                        JTATest.verboseOut("Rollback completed");
                        heuRollback = false;
                        heuCommit   = false;
                     }
                     catch (Exception e) {
                        JTATest.verboseOut("Rollback rc=" + JDReflectionUtil.getField_I(e,"errorCode"));
                        if (JDReflectionUtil.getField_I(e,"errorCode") == javax.transaction.xa.XAException.XA_HEURCOM) {
                           JTATest.verboseOut("The exception was already committed");
                           heuCommit = true;

                        }
                        else if (JDReflectionUtil.getField_I(e,"errorCode") == javax.transaction.xa.XAException.XA_HEURRB) {
                           JTATest.verboseOut("The exception was already rolled back");
                           heuRollback = true;
                        }
                        else {
                           throw e;
                        }
                     }

                     if (heuRollback || heuCommit) {
                        JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                           "Forget " + xids[i]);
                        JDReflectionUtil.callMethod_V(xaRes, "forget", xids[i]);
                     }
                     else {
                        JTATest.verboseOut("Skipped Forget");
                     }
                  }

                  JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                     "Calling XAResource.recover(javax.transaction.xa.XAResource.TMNOFLAGS)");
                  xids = (Object[]) JDReflectionUtil.callMethod_O(xaRes,"recover",javax.transaction.xa.XAResource.TMNOFLAGS);
                  if (xids == null || xids.length == 0) {
                     JTATest.verboseOut("End of scans");
                     done = true;
                  }
               }

               // TODO: Other transactional work on the system using XA may cause
               // TODO: us to see those transactions.
               if (!doOnlyRecovery) {
                  if (count != repetitions) {
                     failed("Expected " + repetitions + " in doubt transactions, found " + count);
                     return;
                  }
               }

               JTATest.verboseOut(Thread.currentThread().getName() + ": " +"Done");
               assertCondition(true);
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }


   // Use both START and end bits (like var 1) 
   public void Var005() {             // from ~kulack/JTA/jtatest/JTARecover.java
      if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
         if (checkJdbc20StdExt()) {
            try {
               JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                  "TEST: Start a bunch of transactions, prepare them, then do a recovery scan. All will be rolled back");
               int      repetitions     = 10;
               boolean  doOnlyRecovery = false;

               if (!doOnlyRecovery) {
                  Recover     txList[] = new Recover[repetitions];
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                     "Creating a bunch of prepare transactions");
                  for (int i=0; i<repetitions; ++i) {
                     txList[i] = new Recover(system_, i);
                  }
               }


               JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                  "Now, using XAResource.recover() to find all " +
                                  "transactions we just put into the in-doubt phase");


               javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
               JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

               Object            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               // Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               Object xids[];
               JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                  "Calling XAResource.recover(javax.transaction.xa.XAResource.TMSTARTRSCAN+XAResource.TMENDRSCAN)");
               xids = (Object[]) JDReflectionUtil.callMethod_O(xaRes,"recover",javax.transaction.xa.XAResource.TMSTARTRSCAN+javax.transaction.xa.XAResource.TMENDRSCAN);
               if (xids == null || xids.length == 0) {
                  failed("Expected some transactions!");
                  return;
               }

               boolean  done  = false;
               int      count = 0;
               // It may be that the XIDs were heuristically rolled back or committed
               // (If this testcase is being called with a 'doOnlyRecovery' value of true in order
               //  to forget about existing transactions.
               boolean  heuRollback = false;
               boolean  heuCommit   = false;

               while (!done) {
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                     "Scan found " + xids.length + " transactions");
                  count += xids.length;
                  for (int i=0; i<xids.length; ++i) {
                     JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                        "Rollback " + xids[i]);
                     try {
                        JDReflectionUtil.callMethod_V(xaRes,"rollback",xids[i]);
                        JTATest.verboseOut("Rollback completed");
                        heuRollback = false;
                        heuCommit   = false;
                     }
                     catch (Exception e) {
                        JTATest.verboseOut("Rollback rc=" + JDReflectionUtil.getField_I(e,"errorCode"));
                        if (JDReflectionUtil.getField_I(e,"errorCode") == javax.transaction.xa.XAException.XA_HEURCOM) {
                           JTATest.verboseOut("The exception was already committed");
                           heuCommit = true;

                        }
                        else if (JDReflectionUtil.getField_I(e,"errorCode") == javax.transaction.xa.XAException.XA_HEURRB) {
                           JTATest.verboseOut("The exception was already rolled back");
                           heuRollback = true;
                        }
                        else {
                           throw e;
                        }
                     }

                     if (heuRollback || heuCommit) {
                        JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                           "Forget " + xids[i]);
                        JDReflectionUtil.callMethod_V(xaRes,"forget",xids[i]);
                     }
                     else {
                        JTATest.verboseOut("Skipped Forget");
                     }
                  }

		  // Don't call recover since we already said we were done
		  done = true;

               }

               // TODO: Other transactional work on the system using XA may cause
               // TODO: us to see those transactions.
               if (!doOnlyRecovery) {
                  if (count != repetitions) {
                     failed("Expected " + repetitions + " in doubt transactions, found " + count);
                     return;
                  }
               }

               JTATest.verboseOut(Thread.currentThread().getName() + ": " +"Done");
               assertCondition(true);
            }
            catch (Exception e) {
               failed(e, "Unexpected Exception");
            }
         }
      }
   }


}

