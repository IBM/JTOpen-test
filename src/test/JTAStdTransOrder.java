////////////////////////////////////////////////////////////////////////
//
// File Name:    JTAStdTransOrder.java
//
// Description:  Same as JTATransOrder.java but test standard interfaces for
//               JTA & JDBC Std Ext
//
// Classes:      JTAStdTransOrder
//
////////////////////////////////////////////////////////////////////////
//------------------- Maintenance-Change Activity ------------------
//
//  Flag  Reason     Rel Lvl   Date    PGMR     Comments
//  ---- --------    ------- -------- ------- ---------------------------
//                           05/16/00 stimmer Created.
////////////////////////////////////////////////////////////////////////
package test;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;

import com.ibm.as400.access.AS400;

public class JTAStdTransOrder extends JTATestcase {

   private String basTbl = JTATest.COLLECTION + ".XAORDER";
   public  Connection c;
   public  Connection gc;

/**
Constructor.
**/
   public JTAStdTransOrder (AS400 systemObject,
                         Hashtable namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password) {
      super (systemObject, "JTAStdTransOrder",
             namesAndVars, runMode, fileOutputStream,
             password);
   }

   public JTAStdTransOrder (AS400 systemObject,
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
       basTbl = JTATest.COLLECTION + ".XAORDER";
      if (isJdbc20StdExt ()) {
         JTATest.verboseOut(baseURL_);
         c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
	 isIasp = JDTestUtilities.isIasp(c); 

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


   /* TO Test:
    Transaction order tests - i.e. change the order of
    doing transaction steps.
   */



   public void Var001() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
	 int expectedErrorCode =  XAException.XAER_PROTO;
	 // If V5R4 and UDBdatasource then error should be XAER_NOTA
	 if ((useUDBDataSource) && (getRelease() >= JDTestDriver.RELEASE_V5R4M0)) {
	     expectedErrorCode = XAException.XAER_NOTA; 
	 }

         try {
            JTATest.verboseOut("TEST: end() without a start() ");


            JTATest.verboseOut("Get the connection");
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            JTATest.verboseOut("Get the Xid but do not start a transaction");
            TestXid newXid = new TestXid();

            String table = basTbl + "001";
            Statement s = conn.createStatement();
            try {
                s.executeUpdate("DROP TABLE " + table); 
            }
            catch(SQLException e) { }
            s.execute("CREATE TABLE " + table + " (COL1 INT)");


            JTATest.verboseOut("Try to end() the unstarted transaction");
            // try to end the unstarted transaction
                try {
                   xaRes.end(newXid, XAResource.TMSUCCESS);
                   failed("Did not throw exception");
                }
                catch (XAException ex) {
                   if (ex.errorCode == expectedErrorCode )
                      assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
                   else
                      failed("Expected error code " + expectedErrorCode + " got " + ex.errorCode);
                }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var002() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
	 int expectedErrorCode =  XAException.XAER_PROTO;
	 // If V5R4 and UDBdatasource then error should be XAER_NOTA
	 if ((useUDBDataSource) && (getRelease() >= JDTestDriver.RELEASE_V5R4M0)) {
	     expectedErrorCode = XAException.XAER_NOTA; 
	 }

         try {
            JTATest.verboseOut("TEST: end() without a start() (no work done)");


            JTATest.verboseOut("Get the connection");
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();

            JTATest.verboseOut("Get the Xid but do not start a transaction");
            TestXid newXid = new TestXid();

            JTATest.verboseOut("Try to end() the unstarted transaction");
            // try to end the unstarted transaction
            try {
               xaRes.end(newXid, XAResource.TMSUCCESS);
               failed("Did not throw exception");
            }
            catch (XAException ex) {
               if (ex.errorCode == expectedErrorCode)
                  assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
               else
                  failed("Expected error code " + expectedErrorCode + " got " + ex.errorCode);
            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
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
            JTATest.verboseOut("TEST: prepare() without a start() or an end() ");


            JTATest.verboseOut("Get the connection");
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            JTATest.verboseOut("Get the Xid but do not start a transaction");
            TestXid newXid = new TestXid();

            String table = basTbl + "003";
            Statement s = conn.createStatement();
            try {
                s.execute("DROP TABLE " + table);
            }
            catch(SQLException e) { }
            s.execute("CREATE TABLE " + table + " (COL1 INT)");


            JTATest.verboseOut("Try to prepare() the transaction");
            // try to prepare
            try {
               int rc = xaRes.prepare(newXid);
               failed("Did not throw exception but returned "+rc);
            }
            catch (XAException ex) {
               if (ex.errorCode == XAException.XAER_NOTA)
                  assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
               else
                  failed("Expected error code " + XAException.XAER_NOTA + " got " + ex.errorCode);
            }
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
	 XAResource     xaRes  = null; 
         TestXid        newXid = null; 
         try {
            JTATest.verboseOut("TEST: prepare() after a start() but without an end()");


            JTATest.verboseOut("Get the connection");
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
                                    xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            JTATest.verboseOut("Get the Xid and start a transaction");
            newXid = new TestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            String table = basTbl + "004";
            Statement s = conn.createStatement();
            s.execute("CREATE TABLE " + table + " (COL1 INT)");


            JTATest.verboseOut("Try to prepare() the transaction");
            // try to prepare
            try {
               int rc = xaRes.prepare(newXid);
               failed("Did not throw exception but returned "+rc);
            }
            catch (XAException ex) {
               if (ex.errorCode == XAException.XAER_PROTO)
                  assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
               else
                  failed("Expected error code " + XAException.XAER_PROTO + " got " + ex.errorCode);
            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
	 try {
	     xaRes.rollback(newXid); 
	 } catch (Exception e) {
           // Ignore
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
            JTATest.verboseOut("TEST: 2 phase commit() without a prepare()");


            JTATest.verboseOut("Get the connection");
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            JTATest.verboseOut("Get the Xid and start a transaction");
            TestXid newXid = new TestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            String table = basTbl + "005";
            Statement s = conn.createStatement();
            JTATest.verboseOut("CREATE TABLE " + table + " (COL1 INT)");
            s.execute("CREATE TABLE " + table + " (COL1 INT)");

            JTATest.verboseOut("End the transaction");
            xaRes.end(newXid, XAResource.TMSUCCESS);

            JTATest.verboseOut("Try to commit() the transaction");
            // try to prepare
            try {
               xaRes.commit(newXid, false); // 1 phase set to false
               failed("Did not throw exception");
            }
            catch (XAException ex) {
               if (ex.errorCode == XAException.XAER_PROTO) {
                  assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
                  xaRes.rollback(newXid);
               }
               else
                  failed("Expected error code " + XAException.XAER_PROTO + " got " + ex.errorCode);
            }
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
            JTATest.verboseOut("TEST: 1 phase commit() after a prepare()");

            JTATest.verboseOut("Get the connection");
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            JTATest.verboseOut("Get the Xid and start a transaction");
            TestXid newXid = new TestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            String table = basTbl + "006";
            Statement s = conn.createStatement();
            JTATest.verboseOut("CREATE TABLE " + table + " (COL1 INT)");
            s.execute("CREATE TABLE " + table + " (COL1 INT)");

            JTATest.verboseOut("End the transaction");
            xaRes.end(newXid, XAResource.TMSUCCESS);

            // prepare the transaction

            int rc = xaRes.prepare(newXid);
            if (rc != XAResource.XA_OK) {
               failed("Expected an OK! rc=" + rc);
            }

            JTATest.verboseOut("Try to 1 phase commit() the transaction");
            try {
               xaRes.commit(newXid, true); // 1 phase set to true
               failed("Did not throw exception");
            }
            catch (XAException ex) {
               if (ex.errorCode == XAException.XAER_PROTO) {
                  assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
                  xaRes.rollback(newXid);
               }
               else
                  failed("Expected error code " + XAException.XAER_PROTO + " got " + ex.errorCode);
            }

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
            JTATest.verboseOut("TEST: prepare() without doing work (this is considered read-only)");


            JTATest.verboseOut("Get the connection");
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();

            JTATest.verboseOut("Get the Xid and start a transaction");
            TestXid newXid = new TestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);
            xaRes.end(newXid, XAResource.TMSUCCESS);

            JTATest.verboseOut("Try to prepare() the transaction");
            // try to prepare. should work (actually, does an implicit commit)
            int rc = xaRes.prepare(newXid);
            if (rc != XAResource.XA_RDONLY) {
               failed("Expected READ_ONLY! rc=" + rc);
            }
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
            JTATest.verboseOut("TEST: prepare() and commit without doing work (this is considered read-only)");


            JTATest.verboseOut("Get the connection");
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();

            JTATest.verboseOut("Get the Xid and start a transaction");
            TestXid newXid = new TestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);
            xaRes.end(newXid, XAResource.TMSUCCESS);

            JTATest.verboseOut("Try to prepare() the transaction");
            // try to prepare. should work (actually, does an implicit commit)
            int rc = xaRes.prepare(newXid);
            if (rc != XAResource.XA_RDONLY) {
               failed("Expected READ_ONLY! rc=" + rc);
            }
            else {
               // try to commit. should fail
               try {
                  xaRes.commit(newXid, false);
                  failed("Did not throw exception");
               }
               catch (XAException ex) {
                  if (ex.errorCode == XAException.XAER_NOTA)
                     assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
                  else
                     failed("Expected error code " + XAException.XAER_NOTA + " got " + ex.errorCode);
               }

            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var009() {
       //@PDC na on toolbox driver for now
       if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
           notApplicable("toolbox driver behavior since v5r3");
           return;
       }
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: prepare() and rollback without doing work (this is considered read-only)");


            JTATest.verboseOut("Get the connection");
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();

            JTATest.verboseOut("Get the Xid and start a transaction");
            TestXid newXid = new TestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);
            xaRes.end(newXid, XAResource.TMSUCCESS);

            JTATest.verboseOut("Try to prepare() the transaction");
            // try to prepare. should work (actually, does an implicit commit)
            int rc = xaRes.prepare(newXid);
            if (rc != XAResource.XA_RDONLY) {
               failed("Expected READ_ONLY! rc=" + rc);
            }
            else {
               // try to rollback. should fail
               try {
                  xaRes.rollback(newXid);
                  failed("Did not throw exception");
               }
               catch (XAException ex) {
                  if (ex.errorCode == XAException.XAER_NOTA)
                     assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
                  else
                     failed("Expected error code " + XAException.XAER_NOTA + " got " + ex.errorCode);
               }

            }
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
            JTATest.verboseOut("TEST: 1 phase commit without doing work (this is considered read-only)");


            JTATest.verboseOut("Get the connection");
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();

            JTATest.verboseOut("Get the Xid and start a transaction");
            TestXid newXid = new TestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);
            xaRes.end(newXid, XAResource.TMSUCCESS);

            xaRes.commit(newXid, true);
	    assertCondition(true); 
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
            JTATest.verboseOut("TEST: 1 phase rollback without doing work (this is considered read-only)");


            JTATest.verboseOut("Get the connection");
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();

            JTATest.verboseOut("Get the Xid and start a transaction");
            TestXid newXid = new TestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);
            xaRes.end(newXid, XAResource.TMSUCCESS);

            xaRes.rollback(newXid);
	    assertCondition(true); 
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }
}
