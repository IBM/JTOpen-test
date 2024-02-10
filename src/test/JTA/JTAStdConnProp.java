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
// File Name:    JTAStdConnProp.java
//
// Description:  Same as JTAConnProp.java but test standard interfaces for
//               JTA & JDBC Std Ext
//
// Classes:      JTAStdConnProp
//
////////////////////////////////////////////////////////////////////////
package test.JTA;

import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JTATest;
import test.JD.JDTestUtilities;

import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;

public class JTAStdConnProp extends JTATestcase {
   //protected boolean isNTS = false;   @PDD move to JTATestcase

/**
Constructor.
**/
   public JTAStdConnProp (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password) {
      super (systemObject, "JTAStdConnProp",
             namesAndVars, runMode, fileOutputStream,
             password);
   }


   public JTAStdConnProp (AS400 systemObject,
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
      if (checkJdbc20StdExt()) {
         JTATest.verboseOut(baseURL_);
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
      if (checkJdbc20StdExt()) {
      }
      unlockSystem(); 
   }


   // Test changing the properties of a connection during
   // various stages of a transaction

   public void Var001() {
      assertCondition(true); // moved to JTAConnCommit.java
      // since there have been already been PTRs opened which include var numbers
      // I just decided to make this successful instead of changing the numbers
   }


   public void Var002() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Change a connection's trans iso level in the middle of a transaction");


            String table = JTATest.COLLECTION + ".CPROPVAR02";

            XADataSource          ds    = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);

            Statement s = conn.createStatement();

            try {
               s.execute("DROP TABLE " + table);
            }
            catch (Exception e) {
            }
            s.execute("CREATE TABLE " + table + " (COLA INT)");

            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

	    if ( getRelease() <= JDTestDriver.RELEASE_V5R2M0 ) { 
		failed("Did not throw exception");
	    } else {
		succeeded(); 
	    } 

         }
         catch (SQLException e) {
	     if ( getRelease() <= JDTestDriver.RELEASE_V5R2M0 ) { 
		 succeeded();
	     } else {
		 failed(e, "Unexpected  Exception Message Should not throw exception in V5R3 ");
	     } 
         }
         catch (Exception e) {
            failed(e, "Incorrect Exception Message");
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
            JTATest.verboseOut("TEST: Change a connection's trans iso level for a transaction which had no work done");



            XADataSource          ds    = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);

            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
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
            JTATest.verboseOut("TEST: Change a connection's trans iso level after committing (but not ending) a transaction");


            String table = JTATest.COLLECTION + ".CPROPVAR04";

            XADataSource          ds    = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);

            Statement s = conn.createStatement();

            try {
               s.execute("DROP TABLE " + table);
            }
            catch (Exception e) {
            }
            s.execute("CREATE TABLE " + table + " (COLA INT)");
            xaRes.commit(Xid, true);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            failed("Did not throw exception");
         }
         catch (SQLException e) {
            succeeded();
         }
         catch (XAException e) {                                            // @A2A
            succeeded();                                                   // @A2A
         }                                                                  // @A2A
         catch (Exception e) {
            failed(e, "Incorrect Exception Message");
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
            JTATest.verboseOut("TEST: Change a connection's trans iso level after rolling back (and ending) a transaction");


            String table = JTATest.COLLECTION + ".CPROPVAR05";

            XADataSource          ds    = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);

            Statement s = conn.createStatement();

            try {
               s.execute("DROP TABLE " + table);
            }
            catch (Exception e) {
            }
            s.execute("CREATE TABLE " + table + " (COLA INT)");
            xaRes.end(Xid, XAResource.TMSUCCESS);
            xaRes.rollback(Xid);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
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
            JTATest.verboseOut("TEST: Change a connection's trans iso level after committing (and ending) a transaction");


            String table = JTATest.COLLECTION + ".CPROPVAR06";

            XADataSource          ds    = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);

            Statement s = conn.createStatement();

            try {
               s.execute("DROP TABLE " + table);
            }
            catch (Exception e) {
            }
            s.execute("CREATE TABLE " + table + " (COLA INT)");
            xaRes.end(Xid, XAResource.TMSUCCESS);
            xaRes.commit(Xid, true);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var007() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Change a connection's trans iso level after rolling back (and ending) a transaction");


            String table = JTATest.COLLECTION + ".CPROPVAR07";

            XADataSource          ds    = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);

            Statement s = conn.createStatement();

            try {
               s.execute("DROP TABLE " + table);
            }
            catch (Exception e) {
            }
            s.execute("CREATE TABLE " + table + " (COLA INT)");
            xaRes.end(Xid, XAResource.TMSUCCESS);
            xaRes.rollback(Xid);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var008() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Get a connection's default read-only property after starting a transaction");

            XADataSource          ds    = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            JTATest.verboseOut("Before starting a transaction, the connection's read-only prop=" + conn.isReadOnly());

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);
            JTATest.verboseOut("After starting a transaction, the connection's read-only prop=" + conn.isReadOnly());
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var009() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Change a connection to read-only after starting a transaction");

            XADataSource          ds    = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);

            conn.setReadOnly(true);
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var010() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Change a connection to read-only after starting and doing work in a transaction");

            String table = JTATest.COLLECTION + ".CPROPVAR10";
            XADataSource          ds    = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);
            // do work
            Statement s = conn.createStatement();
            try {
               s.execute("DROP TABLE " + table);
            }
            catch (Exception e) {
            }
            s.execute("CREATE TABLE " + table + " (COLA INT)");

            try {
               conn.setReadOnly(true);
               failed("Did not throw exception");
            }
            catch (SQLException e) {
               succeeded();
            }
            catch (Exception e) {
               failed(e, "Incorrect Exception Message");
            }
	    // Cleanup				/* @B1 */
	    s.execute("DROP TABLE "+table);	/* @B1 */
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var011() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Change a connection to read-only after starting a transaction");
            JTATest.verboseOut("TEST: Then try to CREATE using the connection");

            String table = JTATest.COLLECTION + ".CPROPVAR11";

            XADataSource          ds    = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);
            conn.setReadOnly(true);
            Statement s = conn.createStatement();
            try {
               s.execute("CREATE TABLE " + table + " (COLA INT)");
               failed("Did not throw exception");
               s.execute("DROP TABLE " + table); // cleanup
            }
            catch (Exception ex) {
               if (JTATest.verbose) {
                  ex.printStackTrace();
               }
               assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var012() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Change a connection to read-only before starting a transaction");
            JTATest.verboseOut("TEST: Then try to CREATE using the connection");

            String table = JTATest.COLLECTION + ".CPROPVAR12";

            XADataSource          ds    = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();
            conn.setReadOnly(true);

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);
            Statement s = conn.createStatement();
            try {
               s.execute("CREATE TABLE " + table + " (COLA INT)");
               failed("Did not throw exception");
               s.execute("DROP TABLE " + table); // cleanup
            }
            catch (Exception ex) {
               if (JTATest.verbose) {
                  ex.printStackTrace();
               }
               assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }

   public void Var013() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Change a connection to read-only after starting a transaction");
            JTATest.verboseOut("TEST: Then try to write using the connection, and commit");

            String table = JTATest.COLLECTION + ".CPROPVAR13";

            XADataSource          ds    = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);
            conn.setReadOnly(true);
            Statement s = conn.createStatement();
            try {
               s.execute("CREATE TABLE " + table + " (COLA INT)");
               xaRes.end(Xid, XAResource.TMSUCCESS);
               xaRes.commit(Xid, true);
               failed("Did not throw exception");
               s.execute("DROP TABLE " + table); // cleanup
               conn.commit();
            }
            catch (Exception ex) {
               if (JTATest.verbose) {
                  ex.printStackTrace();
               }
               assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var014() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Change a connection to read-only before starting a transaction");
            JTATest.verboseOut("TEST: Then try to write using the connection and commit");

            String table = JTATest.COLLECTION + ".CPROPVAR14";

            XADataSource          ds    = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();
            conn.setReadOnly(true);

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);
            Statement s = conn.createStatement();
            try {
               s.execute("CREATE TABLE " + table + " (COLA INT)");
               xaRes.end(Xid, XAResource.TMSUCCESS);
               xaRes.commit(Xid, true);
               failed("Did not throw exception");
               s.execute("DROP TABLE " + table); // cleanup
               conn.commit();
            }
            catch (Exception ex) {
               if (JTATest.verbose) {
                  ex.printStackTrace();
               }
               assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }

   public void Var015() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Change a connection to read-only in the data source itself");
            JTATest.verboseOut("TEST: and try to do writes under this connection");

            String table = JTATest.COLLECTION + ".CPROPVAR15";
            XADataSource          ds    = newXADataSource();
            if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
               ((com.ibm.as400.access.AS400JDBCDataSource)ds).setAccess("read only");
	    else {
	    	JDReflectionUtil.callMethod_V(ds, "setAccess","read only");; 
	    }
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);
            // do work
            Statement s = conn.createStatement();
            try {
               s.execute("CREATE TABLE " + table + " (COLA INT)");
               failed("Did not throw exception");
               s.execute("DROP TABLE " + table); // cleanup
               conn.commit();
            }
            catch (Exception e) {
               if ((exceptionMsgHas(e, "authorization failure")) ||
                   (getDriver() == JDTestDriver.DRIVER_TOOLBOX))
                  assertExceptionIsInstanceOf(e, "java.sql.SQLException");
               else
                  failed(e, "Incorrect Exception Message");
            }

         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var016() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
            notApplicable();
         else {
            try {
               JTATest.verboseOut("TEST: Change a connection to read-only in the data source itself");
               JTATest.verboseOut("TEST: and try to do setReadOnly later");
	       JTATest.verboseOut("TEST: This will not throw an exception in V5R3"); 

               XADataSource          ds    = newXADataSource();
               if (getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                  ((com.ibm.as400.access.AS400JDBCDataSource)ds).setAccess("read only");
	       else {
			   JDReflectionUtil.callMethod_V(ds, "setAccess", "read only"); 
	       }
               XAConnection            xaConn = ds.getXAConnection();
               XAResource              xaRes  = xaConn.getXAResource();
               Connection              conn   = xaConn.getConnection();

               // Generate a new transaction
               JTATestXid              Xid = new JTATestXid();

               // start the transaction
               xaRes.start(Xid, XAResource.TMNOFLAGS);

               try {
                  conn.setReadOnly(true);
		  if (getRelease() >= JDTestDriver.RELEASE_V5R3M0) {
		      assertCondition(true); 
		  } else { 
		      failed("Did not throw exception");
		  }
               }
               catch (Exception e) {
		   if (getRelease() >= JDTestDriver.RELEASE_V5R3M0) {
			   failed(e, "Exception thrown calling setReadonly on readonly connection");
		   } else { 
		       if (exceptionMsgHas(e, "authorization failure"))
			   assertExceptionIsInstanceOf(e, "java.sql.SQLException");
		       else
			   failed(e, "Incorrect Exception Message");
		   }
               }

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
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Change a connection to read-only after starting a transaction");
            JTATest.verboseOut("TEST: Then try to INSERT using the connection");

            String table = JTATest.COLLECTION + ".CPROPVAR17";

            XADataSource          ds    = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();
            Statement s1 = conn.createStatement();
            try {
               s1.execute("DROP TABLE " + table);
               s1.execute("CREATE TABLE " + table + " (COLA INT)");
            }
            catch (Exception e) {
            }
            conn.commit();

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);
            conn.setReadOnly(true);
            Statement s = conn.createStatement();
            try {
               s.execute("INSERT INTO " + table + " VALUES (17)");
               failed("Did not throw exception");
            }
            catch (SQLException e) {
               succeeded();
            }
            catch (Exception e) {
               failed(e, "Incorrect Exception Message");
            }
            finally {
               try {
                  conn.commit();
               }
               catch (Exception e) {
               }
            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var018() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         String failure = null;
         try {
            JTATest.verboseOut("TEST: Change a connection to read-only before starting a transaction");
            JTATest.verboseOut("TEST: Then try to INSERT using the connection");

            String table = JTATest.COLLECTION + ".CPROPVAR18";

            XADataSource          ds    = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();
            Statement s1 = conn.createStatement();
            try {
               s1.execute("DROP TABLE " + table);
               s1.execute("CREATE TABLE " + table + " (COLA INT)");
            }
            catch (Exception e) {
            }
            conn.commit();
            conn.setReadOnly(true);

            // Generate a new transaction
            JTATestXid              Xid = new JTATestXid();


            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);
            Statement s = conn.createStatement();
            try {
               s.execute("INSERT INTO " + table + " VALUES (17)");
               failure = "Did not throw exception";
               s.execute("DROP TABLE " + table); // cleanup
            }
            catch (SQLException e) {

            }
            catch (Exception e) {
               failure = "Incorrect Exception Message";
            }
            // conn.commit();
         }
         catch (Exception e) {
            failure = "Unexpected Exception";
         }
         if (failure != null)
            failed(failure);
         else
            succeeded();
      }
   }

}



