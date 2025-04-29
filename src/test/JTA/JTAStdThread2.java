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
// File Name:    JTAStdThread2.java
//
// Description:  Same as JTAThread2.java but test standard interfaces for
//               JTA & JDBC Std Ext
//
// Classes:      JTAStdThread2
//
////////////////////////////////////////////////////////////////////////
package test.JTA;

import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JTATest;
import test.JD.JDTestUtilities;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;


public class JTAStdThread2 extends JTATestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAStdThread2";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTAStdTest.main(newArgs); 
   }

   public final int JTA_LOCAL_DROP    = 1;
   public final int JTA_GLOBAL_CREATE = 2;
   public final int JDBC_INSERT       = 3;
   public final int JTA_LOCAL_INSERT  = 4;
   public final int JTA_GLOBAL_INSERT = 5;
   public String TABLE = JTATest.COLLECTION + ".THREAD2TBL";


   /***************** Inner Class begin ***************/
   class TestThread1Worker implements Runnable {

      int                     testOption;
      Exception               ex;
      boolean                 passed = true;
      XADataSource            xaDs;
      DataSource            ds;
      XAConnection               xaConn;
      XAResource                 xaRes;
      Connection                 conn;
      JTATestXid                 newXid = null;
      Statement setupStatement;

      // This processing occurs in another thread
      public TestThread1Worker(int testOption) throws Exception {
         this.testOption       = testOption;
         ds     = newDataSource();
         xaDs   = newXADataSource();

      }

      public void JtaLocalDrop() throws Exception {
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         // Transactional connection for JTA work
         //
         // Do some JTA LOCAL TRANSACTIONAL WORK
         // (We didn't start a global transaction)
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         xaConn = xaDs.getXAConnection();
         xaRes  = xaConn.getXAResource();
         conn   = xaConn.getConnection();

         setupStatement = conn.createStatement();
         JTATest.verboseOut("JTA Local Tx Drop table " + JTAStdThread2.this.TABLE);
         setupStatement.execute("DROP TABLE " + JTAStdThread2.this.TABLE);

         setupStatement.close(); setupStatement=null;
         // Explicit Connection.commit() required for JTA local transactional work
         conn.commit();
         // Close the JTAConnection
         conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;
      }

      public void JtaGlobalCreate() throws Exception {
         int      rc;
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         // Transactional connection for JTA work
         //
         // Do some JTA GLOCAL TRANSACTIONAL WORK
         // Need to start the new transaction.
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         xaConn = xaDs.getXAConnection();
         xaRes  = xaConn.getXAResource();
         conn   = xaConn.getConnection();

         newXid = new JTATestXid();
         xaRes.start(newXid, XAResource.TMNOFLAGS);

         setupStatement = conn.createStatement();
         JTATest.verboseOut("JTA Global Tx Create table " + JTAStdThread2.this.TABLE);
         setupStatement.execute("CREATE TABLE " + JTAStdThread2.this.TABLE + " (ID INTEGER PRIMARY KEY, " + 
                                "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");

	 setupStatement.execute("GRANT ALL ON " + JTAStdThread2.this.TABLE + " TO PUBLIC");

         setupStatement.close(); setupStatement=null;
         // JTA 2 phase commit() required for JTA global transactional work
         xaRes.end(newXid, XAResource.TMSUCCESS);
         rc = xaRes.prepare(newXid);
         if (rc != XAResource.XA_OK) {
            JTATest.verboseOut("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
            passed = false;
         }

         xaRes.commit(newXid, false);

	 // 
	 // Run this outside of XA transaction
	 // 
	 setupStatement = conn.createStatement();
	 String command = "call QSYS.QCMDEXC('GRTOBJAUT OBJ("+JTAStdThread2.this.TABLE.replace('.','/')+") OBJTYPE(*FILE) USER(*PUBLIC) AUT(*ALL)                                                                            ',000000080.00000)";
	 try {
	     setupStatement.execute(command);
	 } catch (SQLException e) {
	     System.out.println("Exception on -- "+command);
	     Thread.sleep(60000); 
	     throw e; 
	 } 

	 setupStatement.close();
	 conn.commit();

         // Close the JTAConnection
         conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;
      }

      public void JdbcInsert() throws Exception {
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         // Normal JDBC auto-commit Connection
         //
         // DO some JDBC normal autocommit work
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         conn   = ds.getConnection();
         setupStatement = conn.createStatement();
         JTATest.verboseOut("JDBC Local Insert into table " + JTAStdThread2.this.TABLE);
         setupStatement.execute("INSERT INTO " + JTAStdThread2.this.TABLE + " VALUES(0, 'Simple Test')");
         setupStatement.close(); setupStatement=null;
         // No commit required for JDBC autocommit
         // Close the Connection
         conn.close(); conn=null;
      }

      public void JtaLocalInsert() throws Exception {
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         // Transactional connection for JTA work
         //
         // Do some JTA LOCAL TRANSACTIONAL WORK
         // (We didn't start a global transaction)
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         xaConn = xaDs.getXAConnection();
         xaRes  = xaConn.getXAResource();
         conn   = xaConn.getConnection();

         setupStatement = conn.createStatement();
         JTATest.verboseOut("JTA Local Insert into table " + JTAStdThread2.this.TABLE);
         setupStatement.execute("INSERT INTO " + JTAStdThread2.this.TABLE + " VALUES(1, " +
                                "'Simple JTA Local Test')");
         setupStatement.close(); setupStatement=null;
         // Explicit Connection.commit() required for JTA local transactional work
         conn.commit();
         // Close the JTAConnection
         conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;
      }

      public void JtaGlobalInsert() throws Exception {
         int      rc;
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         // Transactional connection for JTA work
         //
         // Do some JTA GLOBAL TRANSACTIONAL WORK
         // Need to start the new transaction.
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         xaConn = xaDs.getXAConnection();
         xaRes  = xaConn.getXAResource();
         conn   = xaConn.getConnection();

         newXid = new JTATestXid();
         xaRes.start(newXid, XAResource.TMNOFLAGS);

         setupStatement = conn.createStatement();
         JTATest.verboseOut("JTA Global Insert into table " + JTAStdThread2.this.TABLE);
         setupStatement.execute("INSERT INTO " + JTAStdThread2.this.TABLE + " VALUES(2, " +
                                "'Simple JTA Global Test')");
         setupStatement.close(); setupStatement=null;
         // JTA 2 phase commit() required for JTA global transactional work
         xaRes.end(newXid, XAResource.TMSUCCESS);
         rc = xaRes.prepare(newXid);
         if (rc != XAResource.XA_OK) {
            JTATest.verboseOut("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
            passed = false;
         }
         xaRes.commit(newXid, false);
         // Close the JTAConnection
         conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;
      }

      /**
       * Run a portion of the test
       */
      public void run() {
         try {
            switch (testOption) {
            case JTA_LOCAL_DROP:
               JtaLocalDrop();
               break;

            case JTA_GLOBAL_CREATE:
               JtaGlobalCreate();
               break;

            case JDBC_INSERT:
               JdbcInsert();
               break;

            case JTA_LOCAL_INSERT:
               JtaLocalInsert();
               break;

            case JTA_GLOBAL_INSERT:
               JtaGlobalInsert();
               break;
            }
         }
         catch (Exception e) {
            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Thread failed to process request ");
            ex = e;
            passed = false;
            return;
         }
         JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Thread completed request");
         return;
      }
   }

   /***************** Inner Class end ***************/

/**
Constructor.
**/
   public JTAStdThread2 (AS400 systemObject,
                         Hashtable<String,Vector<String>> namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password) {
      super (systemObject, "JTAStdThread2",
             namesAndVars, runMode, fileOutputStream,
             password);
   }


   public JTAStdThread2 (AS400 systemObject,
			 String testname, 
                         Hashtable<String,Vector<String>> namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password) {
      super (systemObject, testname, 
             namesAndVars, runMode, fileOutputStream,
             password);
   }



   private Connection c;
   TestThread1Worker  worker;
   Thread            t;


/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
   protected void setup ()
   throws Exception
   {
       lockSystem("JTATEST",600);
      TABLE = JTATest.COLLECTION + ".THREAD2TBL";
      if (isJdbc20StdExt ()) {
         JTATest.verboseOut(baseURL_);
         c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
	 isIasp = JDTestUtilities.isIasp(c); 

         Statement s = c.createStatement();
         s.execute("CREATE TABLE " + TABLE + " (ID INTEGER PRIMARY KEY, " +
                   "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");

	 s.execute("call QSYS.QCMDEXC('GRTOBJAUT OBJ("+TABLE.replace('.','/')+") OBJTYPE(*FILE) USER(*PUBLIC) AUT(*ALL)                                                          ',000000080.00000)");

         s.close();
         c.commit();
         JTATest.verboseOut("setup: " + TABLE + " created");
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
         s.execute("DROP TABLE " + TABLE);
         s.close();
         c.commit();
         c.close();
         JTATest.verboseOut("cleanup: " + TABLE + " dropped");
      }
      unlockSystem(); 
   }


   protected void callThreadWorker(int testOption, boolean threadit) throws Exception {
      worker = new TestThread1Worker(testOption);

      if (threadit) {
         t = new Thread(worker);

         t.start();
         t.join();
      }
      else {
         worker.run();
      }
      if (!worker.passed) {
         JTATest.verboseOut(Thread.currentThread().getName() + ": " + "The worker thread FAILED!");
         throw worker.ex;
      }
   }


   public void Var001() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                               "TEST: Jta Local Drop, JTA GLobal Create, JDBC insert, " +
                               "JTA Local Insert, JTA Global Insert (each in diff thread)");

            boolean  threadit        = true;
            int      repetitions     = 1;
            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Try " + repetitions + " repetitions of insert/query");
            int value;

            for (value=0; value < repetitions; ++value) {
               // Do each part of the test
               try {

                  ////////////// JTA LOCAL DROP ////////////////
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Create the JTA LOCAL DROP worker thread");
                  callThreadWorker(JTA_LOCAL_DROP, threadit);

                  ////////////// JTA GLOBAL CREATE ////////////////
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Create the JTA GLOBAL CREATE worker thread");
                  callThreadWorker(JTA_GLOBAL_CREATE, threadit);

                  ////////////// JDBC INSERT ////////////////
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Create the JDBC INSERT worker thread");
                  callThreadWorker(JDBC_INSERT, threadit);

                  ////////////// JTA LOCAL INSERT ////////////////
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Create the JTA LOCAL INSERT worker thread");
                  callThreadWorker(JTA_LOCAL_INSERT, threadit);

                  ////////////// JTA GLOBAL INSERT ////////////////
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Create the JTA GLOBAL INSERT worker thread");
                  callThreadWorker(JTA_GLOBAL_INSERT, threadit);

               }
               catch (Exception e) {
                  failed(e, Thread.currentThread().getName() + ": " + "Error occurred ");
                  return;
               }
            } /* for i repetitions */
            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Test complete");
            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Done");
            assertCondition(true);
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
            JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                               "TEST: Jta Local Drop, JTA GLobal Create, JDBC insert, " +
                               "JTA Global Insert, JTA Local Insert (each in diff thread)");

            boolean  threadit        = true;
            int      repetitions     = 1;
            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Try " + repetitions + " repetitions of insert/query");
            int value;

            for (value=0; value < repetitions; ++value) {
               // Do each part of the test
               try {

                  ////////////// JTA LOCAL DROP ////////////////
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Create the JTA LOCAL DROP worker thread");
                  callThreadWorker(JTA_LOCAL_DROP, threadit);

                  ////////////// JTA GLOBAL CREATE ////////////////
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Create the JTA GLOBAL CREATE worker thread");
                  callThreadWorker(JTA_GLOBAL_CREATE, threadit);

                  ////////////// JDBC INSERT ////////////////
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Create the JDBC INSERT worker thread");
                  callThreadWorker(JDBC_INSERT, threadit);

                  ////////////// JTA GLOBAL INSERT ////////////////
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Create the JTA GLOBAL INSERT worker thread");
                  callThreadWorker(JTA_GLOBAL_INSERT, threadit);

                  ////////////// JTA LOCAL INSERT ////////////////
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Create the JTA LOCAL INSERT worker thread");
                  callThreadWorker(JTA_LOCAL_INSERT, threadit);

               }
               catch (Exception e) {
                  failed(e, Thread.currentThread().getName() + ": " + "Error occurred ");
                  return;
               }
            } /* for i repetitions */
            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Test complete");
            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Done");
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
            return;
         }
      }
   }


}




