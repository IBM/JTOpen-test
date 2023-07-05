package test;

//import java.lang.*;
import java.sql.*;
import java.util.*;

import javax.sql.DataSource;
import javax.sql.XADataSource;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;


public class JTAThread2 extends JDTestcase {

   public final int JTA_LOCAL_DROP    = 1;
   public final int JTA_GLOBAL_CREATE = 2;
   public final int JDBC_INSERT       = 3;
   public final int JTA_LOCAL_INSERT  = 4;
   public final int JTA_GLOBAL_INSERT = 5;
   public String TABLE = JTATest.COLLECTION + ".THREAD2TBL";

   boolean isIasp = false; 
   boolean isNTS = false; 

   /***************** Inner Class begin ***************/
   class TestThread1Worker implements Runnable {

      int                     testOption;
      Exception               ex;
      boolean                 passed = true;
      XADataSource            xaDs;
      DataSource            ds;
      Object               xaConn;
      Object                 xaRes;
      Connection                 conn;
      TestXid                 newXid = null;
      Statement setupStatement;

      // This processing occurs in another thread
      public TestThread1Worker(int testOption) throws Exception {
         this.testOption       = testOption;
         ds     = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2DataSource");

         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
         xaDs = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(xaDs,"setDatabaseName",system_);

      }

      public void JtaLocalDrop() throws Exception {
         // int      rc;
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         // Transactional connection for JTA work
         //
         // Do some JTA LOCAL TRANSACTIONAL WORK
         // (We didn't start a global transaction)
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         xaConn = xaDs.getXAConnection();
         xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         setupStatement = conn.createStatement();
         JTATest.verboseOut("JTA Local Tx Drop table " + JTAThread2.this.TABLE);
         setupStatement.execute("DROP TABLE " + JTAThread2.this.TABLE);

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
         xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         newXid = new TestXid();
         JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         setupStatement = conn.createStatement();
         JTATest.verboseOut("JTA Global Tx Create table " + JTAThread2.this.TABLE);
         setupStatement.execute("CREATE TABLE " + JTAThread2.this.TABLE + " (ID INTEGER PRIMARY KEY, " +
                                "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
         setupStatement.close(); setupStatement=null;
         // JTA 2 phase commit() required for JTA global transactional work
         JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
         rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
         if (rc != javax.transaction.xa.XAResource.XA_OK) {
            JTATest.verboseOut("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            passed = false;
         }

         JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);
         // Close the JTAConnection
         conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;
      }

      public void JdbcInsert() throws Exception {
         // int      rc;
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         // Normal JDBC auto-commit Connection
         //
         // DO some JDBC normal autocommit work
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         conn   = (Connection)JDReflectionUtil.callMethod_O(ds,"getConnection");
         setupStatement = conn.createStatement();
         JTATest.verboseOut("JDBC Local Insert into table " + JTAThread2.this.TABLE);
         setupStatement.execute("INSERT INTO " + JTAThread2.this.TABLE + " VALUES(0, 'Simple Test')");
         setupStatement.close(); setupStatement=null;
         // No commit required for JDBC autocommit
         // Close the Connection
         conn.close(); conn=null;
      }

      public void JtaLocalInsert() throws Exception {
         // int      rc;
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         // Transactional connection for JTA work
         //
         // Do some JTA LOCAL TRANSACTIONAL WORK
         // (We didn't start a global transaction)
         ////////////////////////////////////////////////////////////////////////
         ////////////////////////////////////////////////////////////////////////
         xaConn = xaDs.getXAConnection();
         xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         setupStatement = conn.createStatement();
         JTATest.verboseOut("JTA Local Insert into table " + JTAThread2.this.TABLE);
         setupStatement.execute("INSERT INTO " + JTAThread2.this.TABLE + " VALUES(1, " +
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
         xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         newXid = new TestXid();
         JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         setupStatement = conn.createStatement();
         JTATest.verboseOut("JTA Global Insert into table " + JTAThread2.this.TABLE);
         setupStatement.execute("INSERT INTO " + JTAThread2.this.TABLE + " VALUES(2, " +
                                "'Simple JTA Global Test')");
         setupStatement.close(); setupStatement=null;
         // JTA 2 phase commit() required for JTA global transactional work
         JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
         rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
         if (rc != javax.transaction.xa.XAResource.XA_OK) {
            JTATest.verboseOut("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            passed = false;
         }
         JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);
         // Close the JTAConnection
         conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;
      }

      /**
       * Run a portion of the test
       */
      public void run() {
         // int      rc;
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
   public JTAThread2 (AS400 systemObject,
                      Hashtable namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password) {
      super (systemObject, "JTAThread2",
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
      TABLE = JTATest.COLLECTION + ".THREAD2TBL";
      if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
         JTATest.verboseOut(baseURL_);
         c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
	 isIasp = JDTestUtilities.isIasp(c); 
         Statement s = c.createStatement();
         s.execute("CREATE TABLE " + TABLE + " (ID INTEGER PRIMARY KEY, " +
                   "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
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
      if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
         Statement s = c.createStatement();
         s.execute("DROP TABLE " + TABLE);
         s.close();
         c.commit();
         c.close();
         JTATest.verboseOut("cleanup: " + TABLE + " dropped");
      }
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
      if (checkNative ()) {
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
               // int               rc;
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
   }


   public void Var002() {
      if (checkNative ()) {
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
               // int               rc;
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





}




