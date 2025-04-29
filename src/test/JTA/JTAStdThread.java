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
////////////////////////////////////////////////////////////////////////
//
// File Name:    JTAStdThread.java
//
// Description:  Same as JTAThread.java but test standard interfaces for
//               JTA & JDBC Std Ext
//
// Classes:      JTAStdThread
//
////////////////////////////////////////////////////////////////////////
package test.JTA;

import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JTATest;
import test.JD.JDTestUtilities;

import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;



public class JTAStdThread extends JTATestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAStdThread";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTAStdTest.main(newArgs); 
   }
   public final int INSERT         = 1;
   public final int QUERY          = 2;
   public final int TX_STARTNEW    = 1;
   public final int TX_END         = 2;
   public final int TX_INSERT      = 3;
   public final int TX_QUERY       = 4;
   public final int TX_PREPARE     = 5;
   public final int TX_PREPARE_RO  = 6;
   public final int TX_COMMIT      = 7;
   public final int TX_COMMIT_1PC  = 8;
   public final int TX_ROLLBACK    = 9;
   public static final boolean defaultTrace = true;
   // Some individual variations turn off tracing always (Var009 and Var010)
   public static boolean trace = defaultTrace;

   public static void printline(String s) {
      if (trace) {
         JTATest.verboseOut(s);
      }
   }

   /**
    * Base class that runs the test
    */
   class Thread1 implements Runnable {
      XADataSource            xaDs;
      XAConnection               xaConn;
      XAResource                 xaRes;
      Connection                 conn;
      int                        value;
      JTATestXid                    newXid = null;
      PreparedStatement          insertStmt;
      PreparedStatement          queryStmt;
      String                     rdb;
      int                        repetitions;
      boolean                    threadit;
      boolean                    deferInit;
      boolean                    passed;
      Exception                  ex;
      boolean                    initialized;
      String                     unique = "";

      public  String TABLE = JTATest.COLLECTION + ".THR1T";

      Thread1(String rdb, int repetitions, boolean threadit,
              boolean deferInit, String unique)
      throws Exception {
         this.rdb          = rdb;
         this.repetitions  = repetitions;
         this.threadit     = threadit;
         this.deferInit    = deferInit;
         this.unique       = unique;
         TABLE = JTATest.COLLECTION + ".THR1T";

         if (!deferInit) {
            init();
         }
      }

      void init() throws Exception {
         synchronized (this) {
            if (initialized) {
               return;
            }

            printline(Thread.currentThread().getName() + ": Get a DataSource");
            xaDs   = newXADataSource();

            printline(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.
            xaConn = xaDs.getXAConnection();

            printline(Thread.currentThread().getName() + ": Get the XAResource");
            // Get the real connection object
            xaRes  = xaConn.getXAResource();

            printline(Thread.currentThread().getName() + ": Get the Connection");
            conn   = xaConn.getConnection();

            Statement setupStatement = conn.createStatement();
            try {
               printline(Thread.currentThread().getName() + ": " + "Drop table " + TABLE + unique);
               setupStatement.execute("DROP TABLE " + TABLE + unique);
            }
            catch (Exception e) {
               printline(Thread.currentThread().getName() + ": " + "Drop failed: " + e);
            }
            try {
               printline(Thread.currentThread().getName() + ": " + "Create table " + TABLE + unique);
               setupStatement.execute("CREATE TABLE " + TABLE + unique + " (ID INTEGER NOT NULL WITH " +
                                      "DEFAULT, DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
            }
            catch (Exception e) {
               printline(Thread.currentThread().getName() + ": " + "Create failed: " + e);
            }
            setupStatement.close();
            conn.commit();
            initialized = true;
         }
      }

      public void run() {
         try {
            init();

            String sqlInsertString = "INSERT INTO " + TABLE + unique + " VALUES(?, ?)";
            printline(Thread.currentThread().getName() + ": " + "Prepare: " + sqlInsertString);
            insertStmt = conn.prepareStatement(sqlInsertString);

            String sqlQueryString  = "SELECT * FROM " + TABLE + unique + " WHERE ID = ?";
            printline(Thread.currentThread().getName() + ": " + "Prepare: " + sqlQueryString);
            queryStmt = conn.prepareStatement(sqlQueryString);

            printline(Thread.currentThread().getName() + ": " + "Try " + repetitions + " repetitions of insert/query");
            /* int               rc; */ 

            for (value=0; value < repetitions; ++value) {
               // Do each part of the test (the insert and the query) in seperate
               // transactions.
               TestThread1Worker  worker;
               Thread            t;

               ////////////// INSERT ////////////////
               printline(Thread.currentThread().getName() + ": " + "Create the INSERT worker thread");
               worker = new TestThread1Worker(this, INSERT);

               if (threadit) {
                  t = new Thread(worker);

                  t.start();
                  t.join();
               }
               else {
                  worker.run();
               }
               if (!worker.passed) {
                  printline(Thread.currentThread().getName() + ": " + "The worker INSERT thread FAILED!");
                  throw worker.ex;
               }

               ////////////// QUERY ////////////////
               printline(Thread.currentThread().getName() + ": " + "Create the QUERY worker thread");
               worker = new TestThread1Worker(this, QUERY);
               if (threadit) {
                  t = new Thread(worker);

                  t.start();
                  t.join();
               }
               else {
                  worker.run();
               }
               if (!worker.passed) {
                  printline(Thread.currentThread().getName() + ": " + "The worker QUERY thread FAILED!");
                  throw worker.ex;
               }
            } /* for i repetitions */
         }
         catch (Exception e) {
            ex = e;
            passed = false;
            e.printStackTrace();
            printline(Thread.currentThread().getName() + ": " + "Error occurred ");
            return;
         }
         passed = true;
         return;
      }

   }

   /**
    * Worker class to allow Thread1 to multi-thread the test
    */
   class TestThread1Worker implements Runnable {

      Thread1                 testObject;
      int                     testOption;
      Exception               ex;
      boolean                 passed;

      // This processing occurs in another thread
      public TestThread1Worker(Thread1 testObject,
                               int testOption) {
         this.testObject       = testObject;
         this.testOption       = testOption;
      }
      /**
       * Run a portion of the test
       */
      public void run() {
         int      rc;
         try {
            switch (testOption) {
            case INSERT:
               ////////////// INSERT ////////////////
               // Generate a new transaction
               testObject.newXid = new JTATestXid();
               printline(Thread.currentThread().getName() + ": " + "XAResource.start");
               testObject.xaRes.start(testObject.newXid, XAResource.TMNOFLAGS);

               testObject.insertStmt.setInt(1, testObject.value);
               testObject.insertStmt.setString(2, "This is big bad bean #" + testObject.value);


               printline(Thread.currentThread().getName() + ": " + "Insert #" + testObject.value);
               rc = testObject.insertStmt.executeUpdate();
               if (rc != 1) {
                  throw new IllegalStateException("Expected 1 row inserted, got " + rc);
               }

               printline(Thread.currentThread().getName() + ": " + "XAResource.end");
               testObject.xaRes.end(testObject.newXid, XAResource.TMSUCCESS);

               printline(Thread.currentThread().getName() + ": " + "XAResource.prepare");
               rc = testObject.xaRes.prepare(testObject.newXid);
               if (rc != XAResource.XA_OK) {
                  throw new java.lang.IllegalStateException("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
               }
               printline(Thread.currentThread().getName() + ": " + "XAResource.commit");
               testObject.xaRes.commit(testObject.newXid, false);
               break;
            case QUERY:
               ////////////// QUERY ////////////////
               // Generate a new transaction
               testObject.newXid = new JTATestXid();
               printline(Thread.currentThread().getName() + ": " + "XAResource.start");
               testObject.xaRes.start(testObject.newXid, XAResource.TMNOFLAGS);

               testObject.queryStmt.setInt(1, testObject.value);

               printline(Thread.currentThread().getName() + ": " + "Query #" + testObject.value);
               ResultSet rs = testObject.queryStmt.executeQuery();
               int      rows = 0;
               if (rs != null) {
                  while (rs.next()) {
                     ++rows;
                     rc = rs.getInt(1);
                     if (rc != testObject.value) {
                        throw new IllegalStateException("Expected column 1 value of " + testObject.value + " got, " + rc);
                     }
                     if (rows != 1) {
                        throw new IllegalStateException("Expected one row. Got more!");
                     }
                  }
                  rs.close();
               }
               else {
                  throw new IllegalStateException(Thread.currentThread().getName() + ": " + "Got no rows, expected 1!\n");
               }

               printline(Thread.currentThread().getName() + ": " + "XAResource.end");
               testObject.xaRes.end(testObject.newXid, XAResource.TMSUCCESS);

               printline(Thread.currentThread().getName() + ": " + "XAResource.prepare (read only)");
               rc = testObject.xaRes.prepare(testObject.newXid);
               if (rc != XAResource.XA_RDONLY) {
                  throw new IllegalStateException("Expected XA_RDONLY (" + XAResource.XA_RDONLY + "), got " + rc);
                  // No commit required for a read only TX.
               }
               break;
            }
         }
         catch (Exception e) {
            ex = e;
            passed = false;
            e.printStackTrace();
            printline(Thread.currentThread().getName() + ": " +
                    "Thread failed to process request ");
            return;
         }
         printline(Thread.currentThread().getName() + ": " + "Thread completed request");
         passed = true;
         return;
      }
   }

   /**
    * Base class that runs the test
    */
   class Thread2 implements Runnable {
      XADataSource            xaDs;
      XAConnection               xaConn;
      XAResource                 xaRes;
      Connection                 conn;
      int                        value;
      JTATestXid                    newXid = null;
      PreparedStatement          insertStmt;
      PreparedStatement          queryStmt;
      String                     rdb;
      int                        repetitions;
      boolean                    threadit;
      boolean                    deferInit;
      Exception                  ex;
      boolean                    passed;
      boolean                    initialized;
      String                     unique = "";

      public  String TABLE = JTATest.COLLECTION + ".THR2T";

      Thread2(String rdb, int repetitions, boolean threadit,
              boolean deferInit, String unique)
      throws Exception {
         this.rdb      = rdb;
         this.threadit = threadit;
         this.repetitions = repetitions;
         this.deferInit = deferInit;
         this.unique    = unique;
         TABLE = JTATest.COLLECTION + ".THR2T";

         if (!deferInit) {
            init();
         }
      }

      void init() throws Exception {
         synchronized (this) {
            if (initialized) {
               return;
            }

            printline(Thread.currentThread().getName() + ": Get a DataSource");
            xaDs   = newXADataSource();

            printline(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.
            xaConn = xaDs.getXAConnection();

            printline(Thread.currentThread().getName() + ": Get the XAResource");
            // Get the real connection object
            xaRes  = xaConn.getXAResource();

            printline(Thread.currentThread().getName() + ": Get the Connection");
            conn   = xaConn.getConnection();

            Statement setupStatement = conn.createStatement();
            try {
               printline(Thread.currentThread().getName() + ": " + "Drop table " + TABLE + unique);
               setupStatement.execute("DROP TABLE " + TABLE + unique);
            }
            catch (Exception e) {
               printline(Thread.currentThread().getName() + ": " + "Drop failed: " + e);
            }
            try {
               printline(Thread.currentThread().getName() + ": " + "Create table " + TABLE + unique);
               setupStatement.execute("CREATE TABLE " + TABLE + unique + " (ID INTEGER NOT NULL WITH " +
                                      "DEFAULT, DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
            }
            catch (Exception e) {
               printline(Thread.currentThread().getName() + ": " + "Create failed: " + e);
            }
            setupStatement.close();
            conn.commit();
         }
      }

      public void run() {
         try {
            init();

            String sqlInsertString = "INSERT INTO " + TABLE + unique + " VALUES(?, ?)";
            printline(Thread.currentThread().getName() + ": " + "Prepare: " + sqlInsertString);
            insertStmt = conn.prepareStatement(sqlInsertString);

            String sqlQueryString  = "SELECT * FROM " + TABLE + unique + " WHERE ID = ?";
            printline(Thread.currentThread().getName() + ": " + "Prepare: " + sqlQueryString);
            queryStmt = conn.prepareStatement(sqlQueryString);

            printline(Thread.currentThread().getName() + ": " + "Try " + repetitions + " repetitions of insert/query");
            /* int               rc; */ 

            for (value=0; value < repetitions; ++value) {
               // Do each part of the test (the insert and the query) in seperate
               // transactions.
               // Do each part of each transaction in a seperate thread.
               //////////// Insert /////////////
               processTxPart(TX_STARTNEW);
               processTxPart(TX_INSERT);
               processTxPart(TX_END);
               processTxPart(TX_PREPARE);
               processTxPart(TX_COMMIT);

               //////////// Query //////////////
               processTxPart(TX_STARTNEW);
               processTxPart(TX_QUERY);
               processTxPart(TX_END);
               processTxPart(TX_PREPARE_RO);
            } /* for i repetitions */
         }
         catch (Exception e) {
            printline(Thread.currentThread().getName() + ": " + "Error occurred: " + e);
            ex = e;
            passed = false;
            e.printStackTrace();
            return;
         }
         passed = true;
         return;
      }

      /**
       * Perform a very small portion of a transactional request.
       * Optionally do the small piece of work in an individual thread,
       * depending on how the JTAStdThread2 object is setup.
       */
      public void processTxPart(int testOption)
      throws Exception {
         TestThread2Worker  worker;
         Thread            t;

         worker = new TestThread2Worker(this, testOption);

         if (threadit) {
            t = new Thread(worker);

            t.start();
            t.join();
         }
         else {
            worker.run();
         }
         if (!worker.passed) {
            printline(Thread.currentThread().getName() + ": " + "The request FAILED!");
            throw worker.ex;
         }
      }
   }

   /**
    * Worker class that allows Thread2 to multi-thread the test.
    */
   class TestThread2Worker implements Runnable {
      Thread2                 testObject;
      int                     testOption;
      Exception               ex;
      boolean                 passed;

      // This processing occurs in another thread
      public TestThread2Worker(Thread2 testObject,
                               int testOption) {
         this.testObject       = testObject;
         this.testOption       = testOption;
      }
      /**
       * Run a portion of the test
       */
      public void run() {
         int      rc;
         try {
            switch (testOption) {

            case TX_STARTNEW:
               // Generate a new transaction
               testObject.newXid = new JTATestXid();
               printline(Thread.currentThread().getName() + ": " + "TX_STARTNEW: XAResource.start");
               testObject.xaRes.start(testObject.newXid, XAResource.TMNOFLAGS);
               break;
               //////////////////////////////////////////////
            case TX_END:
               printline(Thread.currentThread().getName() + ": " + "TX_END: XAResource.end");
               testObject.xaRes.end(testObject.newXid, XAResource.TMSUCCESS);
               break;
               //////////////////////////////////////////////
            case TX_INSERT:
               printline(Thread.currentThread().getName() + ": " + "TX_INSERT: Insert #" +
                       testObject.value);

               testObject.insertStmt.setInt(1, testObject.value);
               testObject.insertStmt.setString(2, "This is big bad bean #" + testObject.value);

               rc = testObject.insertStmt.executeUpdate();
               if (rc != 1) {
                  throw new IllegalStateException("Expected 1 row inserted, got " + rc);
               }
               break;
               //////////////////////////////////////////////
            case TX_QUERY:
               printline(Thread.currentThread().getName() + ": " + "TX_QUERY: Query #" +
                       testObject.value);
               testObject.queryStmt.setInt(1, testObject.value);

               ResultSet rs = testObject.queryStmt.executeQuery();
               int      rows = 0;
               if (rs != null) {
                  while (rs.next()) {
                     ++rows;
                     rc = rs.getInt(1);
                     if (rc != testObject.value) {
                        throw new IllegalStateException("Expected column 1 value of " + testObject.value +
                                                        " got, " + rc);
                     }
                     if (rows != 1) {
                        throw new IllegalStateException("Expected one row. Got more!");
                     }
                  }
                  rs.close();
               }
               else {
                  throw new IllegalStateException(Thread.currentThread().getName() + ": " + "Got no rows, expected 1!\n");
               }
               break;
               //////////////////////////////////////////////
            case TX_PREPARE:
               printline(Thread.currentThread().getName() + ": " + "TX_PREPARE: XAResource.prepare");
               rc = testObject.xaRes.prepare(testObject.newXid);
               if (rc != XAResource.XA_OK) {
                  throw new IllegalStateException("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
               }
               break;
               //////////////////////////////////////////////
            case TX_PREPARE_RO:
               printline(Thread.currentThread().getName() + ": " + "TX_PREPARE_RO: XAResource.prepare(read only)");
               rc = testObject.xaRes.prepare(testObject.newXid);
               if (rc != XAResource.XA_RDONLY) {
                  throw new IllegalStateException("Expected XA_RDONLY (" + XAResource.XA_RDONLY + "), got " + rc);
                  // No commit required for a read only TX.
               }
               break;
               //////////////////////////////////////////////
            case TX_COMMIT:
               printline(Thread.currentThread().getName() + ": " + "TX_COMMIT: XAResource.commit");
               testObject.xaRes.commit(testObject.newXid, false);
               break;
            case TX_COMMIT_1PC:
               printline(Thread.currentThread().getName() + ": " + "TX_COMMIT_1PCL: XAResource.commit(onePhase)");
               testObject.xaRes.commit(testObject.newXid, true);
               break;
               //////////////////////////////////////////////
            case TX_ROLLBACK:
               printline(Thread.currentThread().getName() + ": " + "TX_ROLLBACK: XAResource.rollback");
               testObject.xaRes.rollback(testObject.newXid);
               break;
               //////////////////////////////////////////////
            default : {
                  throw new IllegalStateException("Didn't get a valid operation: " + testOption);
               }
            }
         }
         catch (Exception e) {
            e.printStackTrace();
            passed = false;
            ex = e;
            printline(Thread.currentThread().getName() + ": " +
                    "Thread failed to process request ");
            return;
         }
         printline(Thread.currentThread().getName() + ": " + "Thread completed request");
         passed = true;
         return;
      }

   }

   /* private String insStr = "JTAThread"; */ 
   /* private Connection c; */ 

/**
Constructor.
**/
   public JTAStdThread (AS400 systemObject,
                        Hashtable<String,Vector<String>> namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password) {
      super (systemObject, "JTAStdThread",
             namesAndVars, runMode, fileOutputStream,
             password);
   }


   public JTAStdThread (AS400 systemObject,
                        String testname, 
                        Hashtable<String,Vector<String>> namesAndVars,
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
      if (isJdbc20StdExt ()) {
         printline(baseURL_);

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
      if (isJdbc20StdExt ()) {
      }
      unlockSystem(); 
   }


   public void Var001() {             // from ~kulack/JTA/jtatest/JTAThread1.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            printline(Thread.currentThread().getName() + ": " +
                    "TEST: Thread 1: setup using JTA local TX, then SINGLE-THREAD JTA Global TX, Insert then Query. Validate, repeat");
            boolean  threadit        = false;
            int      repetitions     = 10;
            Thread1 test = new Thread1(system_, repetitions, threadit, false, "");
            test.run();
            printline(Thread.currentThread().getName() + ": " +
                    "Test complete");

            printline(Thread.currentThread().getName() + ": " +"Done");
            assertCondition(test.passed);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
            return;
         }
      }
   }

   public void Var002() { // from ~kulack/JTA/jtatest/JTAThread2.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            printline(Thread.currentThread().getName() + ": " +
                    "TEST: Thread 2: setup using JTA local TX, then SINGLE-THREAD JTA Global TX, Start, Insert, Prepare, Commit, Start, Query, Commit all from seperate threads. Validate, repeat");
            boolean  threadit        = false;
            int      repetitions     = 10;
            Thread2 test = new Thread2(system_, repetitions, threadit, false, "");         // The run method uses other threads as appropriate.
            test.run();
            printline(Thread.currentThread().getName() + ": " + "Test complete");


            printline("Done");
            assertCondition(test.passed);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
            return;
         }
      }
   }

   public void Var003() {             // from ~kulack/JTA/jtatest/JTAThread1.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            printline(Thread.currentThread().getName() + ": " +
                    "TEST: Thread 1: setup using JTA local TX, then MULTI-THREAD JTA Global TX, Insert then Query. Validate, repeat");
            boolean  threadit        = true;
            int      repetitions     = 10;
            Thread1 test = new Thread1(system_, repetitions, threadit, false, "");
            test.run();
            printline(Thread.currentThread().getName() + ": " +
                    "Test complete");

            printline(Thread.currentThread().getName() + ": " +"Done");
            assertCondition(test.passed);

         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
            return;
         }
      }
   }

   public void Var004() { // from ~kulack/JTA/jtatest/JTAThread2.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            printline(Thread.currentThread().getName() + ": " +
                    "TEST: Thread 2: setup using JTA local TX, then MULTI-THREAD JTA Global TX, Start, Insert, Prepare, Commit, Start, Query, Commit all from seperate threads. Validate, repeat");
            boolean  threadit        = true;
            int      repetitions     = 10;
            Thread2 test = new Thread2(system_, repetitions, threadit, false, "");         // The run method uses other threads as appropriate.
            test.run();
            printline(Thread.currentThread().getName() + ": " + "Test complete");


            printline("Done");
            assertCondition(test.passed);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
            return;
         }
      }
   }

   public void Var005() {             // from ~kulack/JTA/jtatest/JTAThread1.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            printline(Thread.currentThread().getName() + ": " +
                    "TEST: Thread 1: setup using JTA local TX, then stress MULTI-THREAD JTA Global TX, Insert then Query. Validate, repeat");
            boolean  threadit        = true;
            int      repetitions     = 500;
            int      basereps            = 5;
            int      i;
            Thread1  tests[]         = new Thread1[basereps];
            Thread   t[]             = new Thread[basereps];
            // Start all of the threads
            for (i=basereps; i<basereps; ++i) {
               tests[i] = new Thread1(system_, repetitions, threadit, false, Integer.toString(i));
               t[i]     = new Thread(tests[i]);
               t[i].start();
            }

            // Wait for all of the threads to complete.
            for (i=basereps; i<basereps; ++i) {
               try {
                  t[i].join();
               }
               catch (InterruptedException e) {
                  /* ignore */
                  continue;
               }
            }

            // Check all of the status values.
            printline(Thread.currentThread().getName() + ": " +
                    "Test complete");
            for (i=basereps; i<basereps; ++i) {
               if (tests[i].passed == false) {
                  failed(tests[i].ex, "Base thread " + i + " failed");
                  return;
               }
            }

            printline(Thread.currentThread().getName() + ": " +"Done");
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
            return;
         }
      }
   }

   public void Var006() { // from ~kulack/JTA/jtatest/JTAThread2.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            printline(Thread.currentThread().getName() + ": " +
                    "TEST: Thread 2: setup using JTA local TX, then stress MULTI-THREAD JTA Global TX, Start, Insert, Prepare, Commit, Start, Query, Commit all from seperate threads. Validate, repeat");
            boolean  threadit        = true;
            int      repetitions     = 500;
            int      basereps            = 5;
            int      i;
            Thread2  tests[]         = new Thread2[basereps];
            Thread   t[]             = new Thread[basereps];
            // Start all of the threads
            for (i=basereps; i<basereps; ++i) {
               tests[i] = new Thread2(system_, repetitions, threadit, false, Integer.toString(i));
               t[i]     = new Thread(tests[i]);
               t[i].start();
            }

            // Wait for all of the threads to complete.
            for (i=basereps; i<basereps; ++i) {
               try {
                  t[i].join();
               }
               catch (InterruptedException e) {
                  /* ignore */
                  continue;
               }
            }

            // Check all of the status values.
            printline(Thread.currentThread().getName() + ": " +
                    "Test complete");
            for (i=basereps; i<basereps; ++i) {
               if (tests[i].passed == false) {
                  failed(tests[i].ex, "Base thread " + i + " failed");
                  return;
               }
            }

            printline(Thread.currentThread().getName() + ": " +"Done");
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
            return;
         }
      }
   }

   public void Var007() {             // from ~kulack/JTA/jtatest/JTAThread1.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            printline(Thread.currentThread().getName() + ": " +
                    "TEST: Thread 1: setup using JTA local TX, then MULTI-THREAD Create Conn objects, JTA Global TX, Insert then Query. Validate, repeat");
            boolean  threadit        = true;
            int      repetitions     = 50;
            int      basereps        = 5;
            int      i;
            Thread1  tests[]         = new Thread1[basereps];
            Thread   t[]             = new Thread[basereps];
            // Start all of the threads
            for (i=basereps; i<basereps; ++i) {
               tests[i] = new Thread1(system_, repetitions, threadit, true, Integer.toString(i));
               t[i]     = new Thread(tests[i]);
               t[i].start();
            }

            // Wait for all of the threads to complete.
            for (i=basereps; i<basereps; ++i) {
               try {
                  t[i].join();
               }
               catch (InterruptedException e) {
                  /* ignore */
                  continue;
               }
            }

            // Check all of the status values.
            printline(Thread.currentThread().getName() + ": " +
                    "Test complete");
            for (i=basereps; i<basereps; ++i) {
               if (tests[i].passed == false) {
                  failed(tests[i].ex, "Base thread " + i + " failed");
                  return;
               }
            }

            printline(Thread.currentThread().getName() + ": " +"Done");
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
            return;
         }
      }
   }

   public void Var008() { // from ~kulack/JTA/jtatest/JTAThread2.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            printline(Thread.currentThread().getName() + ": " +
                    "TEST: Thread 2: setup using JTA local TX, then MULTI-THREAD Create Conn objects, JTA Global TX, Start, Insert, Prepare, Commit, Start, Query, Commit all from seperate threads. Validate, repeat");
            boolean  threadit        = true;
            int      repetitions     = 50;
            int      basereps        = 5;
            int      i;
            Thread2  tests[]         = new Thread2[basereps];
            Thread   t[]             = new Thread[basereps];
            // Start all of the threads
            for (i=basereps; i<basereps; ++i) {
               tests[i] = new Thread2(system_, repetitions, threadit, true, Integer.toString(i));
               t[i]     = new Thread(tests[i]);
               t[i].start();
            }

            // Wait for all of the threads to complete.
            for (i=basereps; i<basereps; ++i) {
               try {
                  t[i].join();
               }
               catch (InterruptedException e) {
                  /* ignore */
                  continue;
               }
            }

            // Check all of the status values.
            printline(Thread.currentThread().getName() + ": " +
                    "Test complete");
            for (i=basereps; i<basereps; ++i) {
               if (tests[i].passed == false) {
                  failed(tests[i].ex, "Base thread " + i + " failed");
                  return;
               }
            }

            printline(Thread.currentThread().getName() + ": " +"Done");
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
            return;
         }
      }
   }

//    public void Var009() {             // from ~kulack/JTA/jtatest/JTAThread1.java
//       try {
//          printline(Thread.currentThread().getName() + ": " +
//                  "TEST: Thread 1: setup using JTA local TX, then stress MULTI-THREAD Create Conn objects, JTA Global TX, Insert then Query. Validate, repeat");
//          JTAStdThread.trace = false;
//          boolean  threadit        = true;
//          int      repetitions     = 1000;
//          int      basereps        = 100;
//          int      i;
//          Thread1  tests[]         = new Thread1[basereps];
//          Thread   t[]             = new Thread[basereps];
//          // Start all of the threads
//          for (i=0; i<basereps; ++i) {
//             tests[i] = new Thread1(system_, repetitions, threadit, true, Integer.toString(i));
//             t[i]     = new Thread(tests[i]);
//             t[i].start();
//          }
//
//          // Wait for all of the threads to complete.
//          for (i=0; i<basereps; ++i) {
//             try {
//                t[i].join();
//             }
//             catch (InterruptedException e) {
//                /* ignore */
//                continue;
//             }
//          }
//
//          // Check all of the status values.
//          printline(Thread.currentThread().getName() + ": " +
//                  "Test complete");
//          for (i=0; i<basereps; ++i) {
//             if (tests[i].passed == false) {
//                failed(tests[i].ex, "Base thread " + i + " failed");
//                return;
//             }
//          }
//
//          printline(Thread.currentThread().getName() + ": " +"Done");
//          assertCondition(true);
//       }
//       catch (Exception e) {
//          failed(e, "Unexpected Exception");
//       }
//       finally {
//          JTAStdThread.trace = JTAStdThread.defaultTrace;
//       }
//    }
//
//    public void Var010() { // from ~kulack/JTA/jtatest/JTAThread2.java
//       try {
//          printline(Thread.currentThread().getName() + ": " +
//                  "TEST: Thread 2: setup using JTA local TX, then stress MULTI-THREAD Create Conn objects, JTA Global TX, Start, Insert, Prepare, Commit, Start, Query, Commit all from seperate threads. Validate, repeat");
//          JTAStdThread.trace = false;
//          boolean  threadit        = true;
//          int      repetitions     = 1000;
//          int      basereps        = 100;
//          int      i;
//          Thread2  tests[]         = new Thread2[basereps];
//          Thread   t[]             = new Thread[basereps];
//          // Start all of the threads
//          for (i=0; i<basereps; ++i) {
//             tests[i] = new Thread2(system_, repetitions, threadit, true, Integer.toString(i));
//             t[i]     = new Thread(tests[i]);
//             t[i].start();
//          }
//
//          // Wait for all of the threads to complete.
//          for (i=0; i<basereps; ++i) {
//             try {
//                t[i].join();
//             }
//             catch (InterruptedException e) {
//                /* ignore */
//                continue;
//             }
//          }
//
//          // Check all of the status values.
//          printline(Thread.currentThread().getName() + ": " +
//                  "Test complete");
//          for (i=0; i<basereps; ++i) {
//             if (tests[i].passed == false) {
//                failed(tests[i].ex, "Base thread " + i + " failed");
//                return;
//             }
//          }
//
//          printline(Thread.currentThread().getName() + ": " +"Done");
//          assertCondition(true);
//       }
//       catch (Exception e) {
//          failed(e, "Unexpected Exception");
//       }
//       finally {
//          JTAStdThread.trace = JTAStdThread.defaultTrace;
//       }
//    }
//
}




