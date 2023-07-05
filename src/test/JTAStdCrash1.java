////////////////////////////////////////////////////////////////////////
//
// File Name:    JTAStdCrash1.java
//
// Description:  Same as JTACrash1.java but test standard interfaces for
//               JTA & JDBC Std Ext
//
// Classes:      JTAStdCrash1
//
////////////////////////////////////////////////////////////////////////
//------------------- Maintenance-Change Activity ------------------
//
//  Flag  Reason     Rel Lvl   Date    PGMR     Comments
//  ---- --------    ------- -------- ------- ---------------------------
//                   V5R1M0F 05/16/00 stimmer Created
//                   v5r5m0t 07/26/07 pauldev toolbox fix for ur
////////////////////////////////////////////////////////////////////////
package test;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;

import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;

import com.ibm.as400.access.AS400;

public class JTAStdCrash1 extends JTATestcase {

   private String basTbl = JTATest.COLLECTION + ".CRASH1";
   private Connection conn_;

/**
Constructor.
**/
   public JTAStdCrash1 (AS400 systemObject,
                        Hashtable namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password) {
      super (systemObject, "JTAStdCrash1",
             namesAndVars, runMode, fileOutputStream,
             password);
   }


   public JTAStdCrash1 (AS400 systemObject,
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
      basTbl = JTATest.COLLECTION + ".CRASH1";
      if (isJdbc20StdExt() /* && (getDriver() != JDTestDriver.DRIVER_TOOLBOX) */ ) {
         JTATest.verboseOut(baseURL_);
         conn_ = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
      	 isIasp = JDTestUtilities.isIasp(conn_);
         conn_.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

      }
   }

/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
   protected void cleanup ()
   throws Exception
   {
      if (isJdbc20StdExt() /* && (getDriver() != JDTestDriver.DRIVER_TOOLBOX) */ ) {
      }
      unlockSystem(); 
       if (conn_ != null) try { conn_.close(); } catch (Throwable t) {}
   }


   protected boolean checkIns(String table, String value)
   throws Exception
   {
      Statement s = conn_.createStatement();
      //toolbox getting uncommited read, so set true autocommit
      Connection c2 = null;
      if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
      {
          c2 = testDriver_.getConnection(baseURL_ + ";true autocommit=true", userId_, encryptedPassword_);
          s = c2.createStatement();
      }
      JTATest.verboseOut("CHECKINS:  Running quey on "+table+" looking for "+value);
      ResultSet rs = s.executeQuery("SELECT * FROM " + table);
      boolean result = false;
      while (rs.next()) {
         String retVal = rs.getString(1);
	 if (retVal.equals(value)) {
	     JTATest.verboseOut("CHECKINS:  Found "+value);
            result = true;
	 }
      }
      s.close();
      if(c2 != null)
          c2.close();
      return result;
   }


   // TO Test:
   // There are 4 points (marked '*') during a transaction where we try to
   // simulate a crash and see the results.

   // | Start (do work)  * End  *  Prepare  *  Commit/rollback  *  |
   // In this testcase "Crash" scenario is created by closing the xa connection
  public void Var001() {
    JTATest.resetVerboseOut();
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
      notApplicable();
      return;
    }
    JTATest.verboseOut("");

    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        JTATest.verboseOut("TEST001: Start transaction, do work, 'crash'");
        String table = basTbl + "001";
        try {
          JTATest.verboseOut("TEST001: creating table " + table);
          JTATest.crtTmpTbl(table, conn_);
        } catch (Exception e) {
          String msg = e.toString();
          JTATest.verboseOut("TEST001: Caught exception: " + msg);
          if (msg.indexOf("*FILE already exists.") != -1) {
            JTATest.verboseOut("TEST001: Continuing");
          } else {
            failed(e, "Unexpected Exception");
            return;
          }
        }

        String insStr = JTATest.getStr();
        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xaRes = xaConn.getXAResource();
        Connection conn = xaConn.getConnection();

        String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
        JTATest.verboseOut("TEST001: prepare \"" + sqlString + "\"");
        PreparedStatement stmt = conn.prepareStatement(sqlString);

        // Generate a new transaction
        JTATest.verboseOut("TEST001: Generate a new transaction");
        TestXid newXid = new TestXid();
        xaRes.start(newXid, XAResource.TMNOFLAGS);

        int rc;
        JTATest.verboseOut("TEST001: Execute in transaction");
        rc = stmt.executeUpdate();
        if (rc != 1) {
          failed("Expected 1 row inserted, got " + rc);
          return;
        }

        // now close the xa connection (i.e. 'crash')
        JTATest.verboseOut("TEST001:  closing the xa connection (without commit)");
        xaConn.close();

        JTATest.verboseOut("TEST001:  checking that the value was not inserted");
        // make sure that the value was not inserted
        boolean check = checkIns(table, insStr);
        assertCondition(!check, "Error Row was inserted: "
            + JTATest.getVerboseOut());
        JTATest.verboseOut("TEST001: Done");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }


   public void Var002() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    JTATest.resetVerboseOut();
    JTATest.verboseOut("");
    if (checkJdbc20StdExt()) {
      try {
        JTATest.verboseOut("TEST002: Start transaction, do work, end, 'crash'");
        String table = basTbl + "002";
        try {
          JTATest.verboseOut("TEST002:  Creating table "+table);
          JTATest.crtTmpTbl(table, conn_);
        } catch (Exception e) {
          String msg = e.toString();
          JTATest.verboseOut("TEST002:Caught exception: " + msg);
          if (msg.indexOf("*FILE already exists.") != -1) {
            JTATest.verboseOut("Continuing");
          } else {
            failed(e, "Unexpected Exception");
            return;
          }
        }
        conn_.commit();

        String insStr = JTATest.getStr();
        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xaRes = xaConn.getXAResource();
        Connection conn = xaConn.getConnection();

        String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
        JTATest.verboseOut("TEST002:  prepare XAConn \"" + sqlString + "\"");
        PreparedStatement stmt = conn.prepareStatement(sqlString);

        // Generate a new transaction
        JTATest.verboseOut("TEST002:  start a new transaction");
        TestXid newXid = new TestXid();
        xaRes.start(newXid, XAResource.TMNOFLAGS);

        int rc;
        boolean passed = true;
        JTATest.verboseOut("TEST002:  update table in transaction ");
        rc = stmt.executeUpdate();
        if (rc != 1) {
          passed = false;
          failed("Expected 1 row inserted, got " + rc);
        }
        JTATest.verboseOut("TEST002:  XAEND ");
        xaRes.end(newXid, XAResource.TMSUCCESS);

        // now close the xa connection (i.e. 'crash')
        JTATest.verboseOut("TEST002:  close XA without commit");
        xaConn.close();

        JTATest.verboseOut("TEST002:  using READ_UNCOMMITTED to check  "+table+" for "+insStr);

        conn_.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        if (!checkIns(table, insStr)) {
          passed = false;
        }
        // make sure that the value was inserted

        // Now rollback so we can run this testcase next time.

        try {
          JTATest.verboseOut("TEST002:  xaRes.prepare");
          xaRes.prepare(newXid);
        } catch (Exception ex) {
          XAException xaEx = (XAException) ex;
          // @pda toolbox gets rmfail
          if (xaEx.errorCode == XAException.XAER_PROTO
              || xaEx.errorCode == XAException.XAER_RMFAIL) {
            xaConn = ds.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
            xaRes = xaConn.getXAResource();
            JTATest.verboseOut("TEST002:  xaRes.prepare after first exception ");
            xaRes.prepare(newXid);
          } else {
            throw ex;
          }
        }
        JTATest.verboseOut("TEST002:  Rolling back");
        xaRes.rollback(newXid);

        assertCondition(passed);

      } catch (Exception e) {
        failed(e, "Unexpected Exception " + JTATest.getVerboseOut());
      }
    }
  }


   public void Var003() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return;
     }
     JTATest.resetVerboseOut();
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Start transaction, do work, end, prepare, 'crash'");
            String table = basTbl + "003";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            TestXid newXid = new TestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
            }

	    boolean passed = true;
	    String message="" ;
            xaRes.end(newXid, XAResource.TMSUCCESS);
            rc = xaRes.prepare(newXid);
            if (rc != XAResource.XA_OK) {
	       passed = false;
	       message += "Expected XA_OK (" + XAResource.XA_OK + "), got " + rc;
            }

            // now close the xa connection (i.e. 'crash')
            xaConn.close();

            // make sure that the value was inserted
	    if (passed) {
        conn_.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        passed = checkIns(table, insStr);
		if (!passed) {
		    message +=" checkIns failed";
		}
	    }
            JTATest.verboseOut("Done");

	    try {
		xaRes.rollback(newXid);
	    } catch (Exception ex) {
               XAException  xaEx = (XAException)ex;
               //@pda toolbox gets rmfail
               if (xaEx.errorCode == XAException.XAER_PROTO || xaEx.errorCode == XAException.XAER_RMFAIL) {
		   xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
		   xaRes  = xaConn.getXAResource();
		   xaRes.rollback(newXid);
	       } else {
		   throw ex;
	       }
	    }


            assertCondition(passed, message );

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
     JTATest.resetVerboseOut();
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Start transaction, do work, prepare, commit, 'crash'");
            String table = basTbl + "004";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            TestXid newXid = new TestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);

	    boolean passed = true;
	    String message="";
            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
		passed = false;
		message +=  "Expected 1 row inserted, got " + rc;
            }

            xaRes.end(newXid, XAResource.TMSUCCESS);
            rc = xaRes.prepare(newXid);
            if (rc != XAResource.XA_OK) {
		passed = false;
               message += "Expected XA_OK (" + XAResource.XA_OK + "), got " + rc;
            }

            xaRes.commit(newXid, false);
            // now close the xa connection (i.e. 'crash')
            xaConn.close();

            // make sure that the value was inserted
            conn_.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	    if ( ! checkIns(table, insStr)) {
		passed = false;
		message += "checkIns(table, insStr)";
	    }
            assertCondition(passed);
            JTATest.verboseOut("Done");
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
     JTATest.resetVerboseOut();
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Start transaction, do work, prepare, rollback, 'crash'");
            String table = basTbl + "005";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            TestXid newXid = new TestXid();
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

            xaRes.rollback(newXid);
            // now close the xa connection (i.e. 'crash')
            xaConn.close();

            // make sure that the value was not inserted
            conn_.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            assertCondition(!checkIns(table, insStr));
            JTATest.verboseOut("Done");
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var006() {
       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
       {
           notApplicable();
           return;
       }
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return;
     }
     JTATest.resetVerboseOut();
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Start transaction, do work, 'crash', (data should rollback) end fails");
            String table = basTbl + "006";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unexpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            TestXid newXid = new TestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
               return;
            }


            // now close the xa connection (i.e. 'crash')
            xaConn.close();
            try {
               xaRes.end(newXid, XAResource.TMSUCCESS);
            }
            catch (Exception ex) {
               if (!(ex instanceof XAException)) {
                  failed(ex, "Unexpected exception");
                  return;
               }
               XAException  xaEx = (XAException)ex;
               //@pda toolbox gets rmfail
               if (xaEx.errorCode != XAException.XAER_PROTO && xaEx.errorCode != XAException.XAER_RMFAIL) {
                  failed(ex, "Expected XAER_PROTO failure");
                  return;
               }
            }
// No need to do this. It was already rolled back above.
//          rc = xaRes.prepare(newXid);
//          if (rc != XAResource.XA_OK) {
//             failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
//          }
//          xaRes.commit(newXid, false);
//
            // make sure that the value was not inserted

	    // Note checkIns uses a *NONE connection.. Give the system
	    // a second to clean up before checking
	    // try { Thread.sleep(1000);  } catch (Exception e) {}
      // Remove sleep from test and use the right transaction isolation level
      conn_.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

	    boolean notInserted = !checkIns(table, insStr);
            assertCondition(notInserted, "Value should not have been inserted");
            JTATest.verboseOut("Done");
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
     JTATest.resetVerboseOut();
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Start transaction, do work, end, 'crash', prepare, commit");
            String table = basTbl + "007";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            TestXid newXid = new TestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
            }

            xaRes.end(newXid, XAResource.TMSUCCESS);

            // now close the xa connection (i.e. 'crash')
            xaConn.close();
	    try {
		rc = xaRes.prepare(newXid);
	    } catch (Exception ex) {
		XAException  xaEx = (XAException)ex;
         //@pda toolbox gets rmfail
		if (xaEx.errorCode == XAException.XAER_PROTO || xaEx.errorCode == XAException.XAER_RMFAIL) {
		    xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
		    xaRes  = xaConn.getXAResource();
		    rc = xaRes.prepare(newXid);
		} else {
		    throw ex;
		}
	    }



            if (rc != XAResource.XA_OK) {
               failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
            }
            xaRes.commit(newXid, false);

            // make sure that the value was inserted
            conn_.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            assertCondition(checkIns(table, insStr));
            JTATest.verboseOut("Done");
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
     JTATest.resetVerboseOut();
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Start transaction, do work, prepare, 'crash', commit");
            String table = basTbl + "008";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            TestXid newXid = new TestXid();
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

            // now close the xa connection (i.e. 'crash')

            xaConn.close();

	    // Call commit and re-establish the connection if needed
	    try {
		xaRes.commit(newXid, false);
	    } catch (Exception ex) {

		XAException  xaEx = (XAException)ex;
        //@pda toolbox gets rmfail
		if (xaEx.errorCode == XAException.XAER_PROTO || xaEx.errorCode == XAException.XAER_RMFAIL) {
		    xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
		    xaRes  = xaConn.getXAResource();
		    xaRes.commit(newXid, false);
		} else {
		    throw ex;
		}
	    }


            // make sure that the value was inserted
            conn_.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            assertCondition(checkIns(table, insStr));
            JTATest.verboseOut("Done");
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var009() {
	   if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
	   {
		   notApplicable();
		   return;
	   }
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return;
     }
     JTATest.resetVerboseOut();
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Start transaction, do work, 'crash', (data should rollback) end fails");
            String table = basTbl + "009";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unexpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            TestXid newXid = new TestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
               return;
            }


            // now close the xa connection (i.e. 'crash')
            xaConn.close();

            try {
               xaRes.end(newXid, XAResource.TMSUCCESS);
            }
            catch (Exception ex) {
               if (!(ex instanceof XAException)) {
                  failed(ex, "Unexpected exception");
                  return;
               }
               XAException  xaEx = (XAException)ex;
               //@pda toolbox gets rmfail
               if (xaEx.errorCode != XAException.XAER_PROTO  && xaEx.errorCode != XAException.XAER_RMFAIL) {
                  failed(ex, "Expected XAER_PROTO failure");
                  return;
               }
            }
// Mo need to do tihs, TX sould have already rolled back.
//          rc = xaRes.prepare(newXid);
//          if (rc != XAResource.XA_OK) {
//             failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
//          }
//          xaRes.rollback(newXid);
//

	    // Note checkIns uses a *NONE connection.. Give the system
	    // a second to clean up before checking
	    try { Thread.sleep(1000);  } catch (Exception e) {}


            // make sure that the value was not inserted
      conn_.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	    boolean notInserted =  !checkIns(table, insStr);
            assertCondition(notInserted, "Error.. values was inserted, should not have been");
            JTATest.verboseOut("Done");
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
            JTATest.verboseOut("TEST: Start transaction, do work, end, 'crash', prepare, rollback");
            String table = basTbl + "010";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            TestXid newXid = new TestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            int      rc;
            rc = stmt.executeUpdate();
            if (rc != 1) {
               failed("Expected 1 row inserted, got " + rc);
            }

            xaRes.end(newXid, XAResource.TMSUCCESS);

            // now close the xa connection (i.e. 'crash')
            xaConn.close();


	    //
	    // Re-establish the connection if necessary
	    //

	    try {
		rc = xaRes.prepare(newXid);
	    } catch (Exception ex) {
		XAException  xaEx = (XAException)ex;
        //@pda toolbox gets rmfail
		if (xaEx.errorCode == XAException.XAER_PROTO || xaEx.errorCode == XAException.XAER_RMFAIL ) {
		    xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
		    xaRes  = xaConn.getXAResource();
		    rc = xaRes.prepare(newXid);
		} else {
		    throw ex;
		}
	    }




            if (rc != XAResource.XA_OK) {
               failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
            }
            xaRes.rollback(newXid);

            // make sure that the value was not inserted
            conn_.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            assertCondition(!checkIns(table, insStr));
            JTATest.verboseOut("Done");
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
            JTATest.verboseOut("TEST: Start transaction, do work, prepare, 'crash', rollback");
            String table = basTbl + "011";
            try {
               JTATest.crtTmpTbl(table, conn_);
            }
            catch (Exception e) {
               String msg = e.toString();
               JTATest.verboseOut("Caught exception: " + msg);
               if (msg.indexOf("*FILE already exists.") != -1) {
                  JTATest.verboseOut("Continuing");
               }
               else {
                  failed(e, "Unecpected Exception");
                  return;
               }
            }

            String insStr = JTATest.getStr();
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
            JTATest.verboseOut("prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            // Generate a new transaction
            TestXid newXid = new TestXid();
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

            // now close the xa connection (i.e. 'crash')
            xaConn.close();

	    //
	    // Rollback.. If needed re-establish the connection
	    //
	    try {
		xaRes.rollback(newXid);
	    } catch (Exception ex) {
		XAException  xaEx = (XAException)ex;
        //@pda toolbox gets rmfail
		if (xaEx.errorCode == XAException.XAER_PROTO || xaEx.errorCode == XAException.XAER_RMFAIL) {
		    xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
		    xaRes  = xaConn.getXAResource();
		    xaRes.rollback(newXid);
		} else {
		    throw ex;
		}
	    }


            // make sure that the value was not inserted
      conn_.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            assertCondition(!checkIns(table, insStr));
            JTATest.verboseOut("Done");
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }
}
