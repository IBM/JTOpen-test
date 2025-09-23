////////////////////////////////////////////////////////////////////////
//
// File Name:    JTAStdBasic.java
//
// Description:  Same as JTABasic.java but test standard interfaces for
//               JTA & JDBC Std Ext
//
// Classes:      JTAStdBasic
//
////////////////////////////////////////////////////////////////////////
package test.JTA;

import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JDTestDriver;
import test.JTATest;
import test.PasswordVault;
import test.JD.JDTestUtilities;

import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.XAException;

public class JTAStdBasic extends JTATestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAStdBasic";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTAStdTest.main(newArgs); 
   }

   private String insStr = "JTABasic";
   private String basTbl = JTATest.COLLECTION + ".CHARTAB";
   private String endTbl1 = JTATest.COLLECTION + ".END1TBL";
   private String endTbl2 = JTATest.COLLECTION + ".END2TBL";
   private Connection c;
   //protected boolean isNTS = false; @PDD move to JTATestcase

/**
Constructor.
**/
   public JTAStdBasic (AS400 systemObject,
                    Hashtable<String,Vector<String>> namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTAStdBasic",
             namesAndVars, runMode, fileOutputStream,
             password);
   }

   public JTAStdBasic (AS400 systemObject,
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
      basTbl = JTATest.COLLECTION + ".CHARTAB";
      endTbl1 = JTATest.COLLECTION + ".END1TBL";
      endTbl2 = JTATest.COLLECTION + ".END2TBL";

      if (isJdbc20StdExt ()) {
         JTATest.verboseOut(baseURL_);
         c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
	 isIasp = JDTestUtilities.isIasp(c); 
         Statement s = c.createStatement();
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
      if (isJdbc20StdExt ()) {
         Statement s = c.createStatement();
         s.execute("DROP TABLE " + basTbl);
         s.close();
         c.close();
      }
      unlockSystem(); 
   }


   // TO Test:
   // Basic Transactional (JTA) tests

   public void Var001() {             // from ~kulack/JTA/jtatest/JTAEasy.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt ()) {
         try {
            JTATest.verboseOut("TEST: Simple insert/commit using a global transaction");

            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "1')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
            }

            xaRes.end(newXid, XAResource.TMSUCCESS);

            rc = xaRes.prepare(newXid);

            if (rc != XAResource.XA_OK) {
               failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
            }

            xaRes.commit(newXid, false);
            conn.close();
            JTATest.verboseOut("Done");
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var002() { // from ~kulack/JTA/jtatest/JTAEnd.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {

            boolean endTx = true;

            JTATest.verboseOut(Thread.currentThread().getName() + ": New transaction. Will be ended == " + endTx);
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get a DataSource");
            XADataSource xaDs   = newXADataSource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.
            XAConnection xaConn = xaDs.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");
            // Get the real connection object
            XAResource xaRes  = xaConn.getXAResource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            Connection conn   = xaConn.getConnection();

            // Generate a new transaction
            JTATestXid newXid = new JTATestXid();
            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "XAResource.start");
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            Statement s = conn.createStatement();
            s.execute("CREATE TABLE " + endTbl1 + " (ID INTEGER NOT NULL WITH " +
                      "DEFAULT, DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
            JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                               "XAResource.end(XAResource.TMFAIL)");
            boolean  exceptionTaken = false;
            try {
               xaRes.end(newXid, XAResource.TMFAIL);
            }
            catch (XAException xaEx) {
               if (xaEx.errorCode != XAException.XA_RBROLLBACK) {
                  failed(xaEx, "Wrong exception taken");
                  return;
               }
               exceptionTaken = true;
            }
            if (!exceptionTaken) {
               JTATest.verboseOut("No exception was detected");
            }

            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "XAResource.rollback()");
            xaRes.rollback(newXid);
            boolean result = false;

            // the table should not exist. Should have been rolled back
            try {
               s.executeQuery("SELECT * FROM " + endTbl1);
               failed("Did not throw exception");
            }
            catch (SQLException e) {
               result = true;
            }

            conn.close();
            if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
                JTATransInfo[] match = JTATest.getTransInfo();
                boolean isTransPresent = false;
                if (match == null) {
                    isTransPresent = false;
                }
                else {
                    for (int i = 0; (i < match.length) && (match[i] != null); i++) {
                        isTransPresent = isTransPresent || newXid.match(match[i]);
                }
                }
                result = result && !isTransPresent;
            }
            assertCondition(result);

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

            boolean endTx = true;

            JTATest.verboseOut(Thread.currentThread().getName() + ": New transaction. Will be ended == " + endTx);
            JTATest.verboseOut(Thread.currentThread().getName() + ": Get a DataSource");
            XADataSource xaDs   = newXADataSource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.
            XAConnection xaConn = xaDs.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");
            // Get the real connection object
            XAResource xaRes  = xaConn.getXAResource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            Connection conn   = xaConn.getConnection();

            // Generate a new transaction
            JTATestXid newXid = new JTATestXid();
            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "XAResource.start");
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            // Do some work
            Statement s = conn.createStatement();
            s.execute("CREATE TABLE " + endTbl2 + " (ID INTEGER NOT NULL WITH " +
                      "DEFAULT, DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");


            JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                               "XAResource.end(XAResource.TMFAIL)");
            boolean exceptionTaken = false;
            try {
               xaRes.end(newXid, XAResource.TMFAIL);
            }
            catch (XAException xaEx) {
               if (xaEx.errorCode != XAException.XA_RBROLLBACK) {
                  failed(xaEx, "Wrong exception taken");
                  return;
               }
               exceptionTaken = true;
            }
            if (!exceptionTaken) {
               JTATest.verboseOut("No exception was detected");
	       //
               // For NTS we don't take an exception, we just mark that it must be rolled back
               //
	       if (!isNTS)
   	          failed("No exception taken on xaRes.end");
            }

            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "XAResource.commit()");
            try {
               xaRes.commit(newXid, true);
               failed("Did not throw exception");
               return;
            }
            catch (Exception ex) {
               if (!(ex instanceof XAException)) {
                  failed(ex, "Unexpected exception type");
                  return;
               }
               XAException xaEx = (XAException)ex;
	       if (!isNTS) {
		   if (xaEx.errorCode != XAException.XAER_PROTO) {
		       failed(ex, "Unexpected exception error code -- expected XAER_PROTO");
		       return;
		   }
	       } else {
		   //
                   // For NTS we return an invalid XID since the XID must be rolled back
                   //
               if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
                   if (xaEx.errorCode != XAException.XAER_PROTO) {
                       failed(ex, "Unexpected exception error code -- expected XAER_PROTO");
                       return;
                   }
                   
               }else{
		           if (xaEx.errorCode != XAException.XAER_NOTA) {
		               failed(ex, "Unexpected exception error code -- expected XAER_NOTA");
		               return;
                   }
	   
               }
	       }
               // assertExceptionIsInstanceOf(ex, "com.ibm.XAException");
            }

            JTATest.verboseOut(Thread.currentThread().getName() + ": " + "XAResource.rollback()");
            // Now rollback to complete the disassociation for the AS/400
            // on any other platform, the XAResource.end(TMFAIL) done above would
            // have been enough.
            xaRes.rollback(newXid);

            boolean result = false;
            // the table should not exist. Should have been rolled back
            try {
               s.executeQuery("SELECT * FROM " + endTbl2);
               failed("Did not throw exception");
               return;
            }
            catch (SQLException e) {
               JTATest.verboseOut("Table rolled back as expected");
               result = true;
            }

            conn.close();

            if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
                JTATransInfo[] match = JTATest.getTransInfo();
                boolean isTransPresent = false;
                if (match == null) {
                   isTransPresent = false;
                }
                else {
                   for (int i = 0; (i < match.length) && (match[i] != null); i++) {
                      isTransPresent = isTransPresent || newXid.match(match[i]);
                   }
                }
                result = result && !isTransPresent;
            }
            assertCondition(result);
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
            // TMSUSPEND and TMRESUME not currently supported
	     JTATest.verboseOut("TEST: start/suspend/restart a transaction for simple insert/commit" );


            if ( ! isNTS) {

               notApplicable("TMSUSPEND and TMRESUME not currently supported");
	    } else {


		XADataSource          ds     = newXADataSource();

		XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
		XAResource              xaRes  = xaConn.getXAResource();
		Connection              conn   = xaConn.getConnection();

		String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr + "1')";
		JTATest.verboseOut("prepare \"" + sqlString + "\"");
		PreparedStatement stmt = conn.prepareStatement(sqlString);

	        // Generate a new transaction
		JTATestXid              newXid = new JTATestXid();
		xaRes.start(newXid, XAResource.TMNOFLAGS);

		int	rc;
		rc = stmt.executeUpdate();
		if (rc != 1) {
		    failed("Expected 1 row inserted, got " + rc);
		}

	        // suspend the transaction
		xaRes.end(newXid, XAResource.TMSUSPEND);

	        // restart the transaction
		xaRes.start(newXid, XAResource.TMRESUME);

        xaRes.end(newXid, XAResource.TMSUCCESS);  //@A1

		rc = xaRes.prepare(newXid);

		if (rc != XAResource.XA_OK) {
		    failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
		}

		xaRes.commit(newXid, false);
		conn.close();
		JTATest.verboseOut("Done");
		assertCondition(true);
	    }
	 }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
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
            // start and end a transaction without doing any work
            JTATest.verboseOut("TEST: start and end a transaction without doing work");

            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            // end the transaction
            xaRes.end(newXid, XAResource.TMSUCCESS);
	    xaRes.rollback(newXid);  //@A1

            JTATest.verboseOut("Done for "+conn);
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



}
