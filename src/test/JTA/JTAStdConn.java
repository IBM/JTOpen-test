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
// File Name:    JTAStdConn.java
//
// Description:  Same as JTAConn.java but test standard interfaces for
//               JTA & JDBC Std Ext
//
// Classes:      JTAStdConn
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
import test.PasswordVault;
import test.JD.JDTestUtilities;

import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;

public class JTAStdConn extends JTATestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAStdConn";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTAStdTest.main(newArgs); 
   }

  private String insStr = "JTAConn";
  private String basTbl = JTATest.COLLECTION + ".CHARTAB";
  // private String endTbl = JTATest.COLLECTION + ".END1TBL";
  private Connection pwrConn_;

  // protected boolean isNTS = false; @PDD move to JTATestcase

  /**
   * Constructor.
   **/
  public JTAStdConn(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream,  String password,
      String pwrSysUid, String pwrSysPwd) {
    super(systemObject, "JTAStdConn", namesAndVars, runMode, fileOutputStream,
         password, pwrSysUid, pwrSysPwd);
  }

  public JTAStdConn(AS400 systemObject, String testname,
      Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password) {
    super(systemObject, testname, namesAndVars, runMode, fileOutputStream,
         password);
  }

  public JTAStdConn(AS400 systemObject, String testname,
      Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password, String pwrSysUid, String pwrSysPwd) {
    super(systemObject, testname, namesAndVars, runMode, fileOutputStream,
         password, pwrSysUid, pwrSysPwd);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  
  protected void setup() throws Exception {
    lockSystem("JTATEST",600);
    basTbl = JTATest.COLLECTION + ".CHARTAB";
    // endTbl = JTATest.COLLECTION + ".END1TBL";
    pwrConn_ = testDriver_.getConnection(baseURL_, pwrSysUserID_,
        pwrSysEncryptedPassword_);

    if (isJdbc20StdExt()) {
      Connection conn = null;
      try {
        JTATest.verboseOut(baseURL_);
        Statement s = pwrConn_.createStatement();
        try {
          s.execute("DROP TABLE " + basTbl);
        } catch (Throwable t) {
          String tInfo = t.toString();
          if (tInfo.indexOf("not found") < 0) {
            t.printStackTrace(System.out);
            System.out.println("pwrConn_" + pwrConn_ + " using "
                + pwrSysUserID_);
          }
        }
        s.close();

        conn = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
        isIasp = JDTestUtilities.isIasp(conn);
        s = conn.createStatement();
        s.execute("CREATE TABLE " + basTbl
            + " (COL1 CHAR (10 ) NOT NULL WITH DEFAULT)");
        s.execute("GRANT ALL ON " + basTbl + " TO PUBLIC");
        s.close();

      } finally {
        if (conn != null)
          try {
            conn.close();
          } catch (Throwable t) {
            t.printStackTrace();
          }
      }
    }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {

    if (isJdbc20StdExt()) {
      try {
        Statement s = pwrConn_.createStatement();
        s.execute("DROP TABLE " + basTbl);
        s.close();
      } finally {
        if (pwrConn_ != null)
          pwrConn_.close();
      }
    }
    unlockSystem(); 
  }

  public void Var001() { // from ~kulack/JTA/jtatest/JTAConn.java
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        JTATest
            .verboseOut("TEST: starting a transaction prior to using XAConnection.getConnection");

        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection();
        XAResource xaRes = xaConn.getXAResource();

        // Generate a new transaction
        JTATestXid newXid = new JTATestXid();
        xaRes.start(newXid, XAResource.TMNOFLAGS);

        Connection conn = xaConn.getConnection();

        String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr
            + "2')";
        JTATest.verboseOut("prepare \"" + sqlString + "\"");
        PreparedStatement stmt = conn.prepareStatement(sqlString);

        int rc;
        rc = stmt.executeUpdate();
        if (rc != 1) {
          failed("Expected 1 row inserted, got " + rc);
          conn.close();
          return;
        }

        xaRes.end(newXid, XAResource.TMSUCCESS);
        rc = xaRes.prepare(newXid);
        if (rc != XAResource.XA_OK) {
          failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
          conn.close();
          return;
        }

        xaRes.commit(newXid, false);
        conn.close();
        JTATest.verboseOut("Done");
        assertCondition(true);
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
    Connection conn = null;
    if (checkJdbc20StdExt()) {
      XAResource xaRes = null;
      JTATestXid newXid = null;
      try {
        JTATest
            .verboseOut("TEST: starting and ending transaction prior to using XAConnection.getConnection");

        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection();
        xaRes = xaConn.getXAResource();

        // Generate a new transaction
        newXid = new JTATestXid();
        xaRes.start(newXid, XAResource.TMNOFLAGS);
        xaRes.end(newXid, XAResource.TMSUCCESS);

        conn = xaConn.getConnection();

        try {
          // Under OS/400 JTA, we only allow a work under a new transaction
          // until the first one was committed/rolled back. We expect
          // the work to fail with an XAER_PROTO failure.
          // -- This no longer applies to NTS
          String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr
              + "2')";
          JTATest.verboseOut("prepare \"" + sqlString + "\"");
          PreparedStatement stmt = conn.prepareStatement(sqlString);

          int rc;
          rc = stmt.executeUpdate();
          if (rc != 1) {

            failed("Expected 1 row inserted, got " + rc);
            conn.commit();
            conn.close();
            conn = null;
            /* return; -- this will return anyway */
          } else {
            JTATest.verboseOut("Execute successful");
            succeeded();
            conn.commit();
            conn.close();
            conn = null;
          }
        } catch (Exception ex) {
          JTATest.verboseOut("Exception caught");
          int rc = xaRes.prepare(newXid);
          if (rc != XAException.XA_RDONLY) {
            if (conn != null)
              conn.rollback();
          }
          if (conn != null)
            conn.close();
          conn = null;
          if (ex instanceof javax.transaction.xa.XAException) {

            XAException xaEx = (XAException) ex;
            if (xaEx.errorCode != XAException.XAER_PROTO) {
              failed(ex, "Expected XAER_PROTO failure");
            } else {
              assertCondition(true);
            }
          } else {
            failed(ex,
                "Exception not instance of javax.transaction.xa.XAException");
          }
        }

      } catch (Exception e) {
        failed(e, "Unexpected exception");
        if (conn != null) {
          try {
            conn.commit();
            conn.close();
          } catch (Exception e2) {
            e2.printStackTrace();
          }
        }
      }
      try {
        if (xaRes != null)
          xaRes.rollback(newXid);
      } catch (Exception e) {
        // Just ignore */
      }
      /* succeeded(); // This is a duplicate succeeded */
    }
  }

  public void Var003() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        JTATest
            .verboseOut("TEST: Try to associate two transactions with a connection. Should fail");

        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection();
        XAResource xaRes = xaConn.getXAResource();
        Connection conn = xaConn.getConnection();

        // Generate two new transactions
        JTATestXid Xid1 = new JTATestXid();
        JTATestXid Xid2 = new JTATestXid();

        // now associate both with the XA connection
        xaRes.start(Xid1, XAResource.TMNOFLAGS);
        try {
          xaRes.start(Xid2, XAResource.TMNOFLAGS);
          failed("Did not throw exception");
        } catch (Exception ex) {
          assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
        }
        conn.close();
        JTATest.verboseOut("Done");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var004() {
    //  na on toolbox driver for now
    if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
      notApplicable("Non-toolbox test only.");
      return;
    }
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        JTATest
            .verboseOut("TEST: Try to have 2 Connections for one XAConnection. Should not be able to");

        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection();
        // XAResource xaRes = xaConn.getXAResource();
        Connection conn1 = xaConn.getConnection();
        // the next step would basically close conn1
        Connection conn2 = xaConn.getConnection();

        // any operation on conn1 should now throw an exception
        try {
          boolean ac = conn1.getAutoCommit();
          failed("Exception not thrown " + ac);
        } catch (Exception ex) {
          assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
        }
        conn2.close();
        JTATest.verboseOut("Done");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var005() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        JTATest
            .verboseOut("TEST: Try to have 2 Connections for one transaction. Should not be able to");

        XADataSource ds = newXADataSource();

        XAConnection xaConn1 = ds.getXAConnection();
        XAResource xaRes1 = xaConn1.getXAResource();
        Connection conn1 = xaConn1.getConnection();

        XAConnection xaConn2 = ds.getXAConnection();
        XAResource xaRes2 = xaConn2.getXAResource();
        Connection conn2 = xaConn2.getConnection();

        // Generate a new transaction
        JTATestXid Xid1 = new JTATestXid();

        // now associate both connections with the transaction
        // after talking to Fred, it is a minor bug if we do not
        // get a exception here in this var. A major one is if we
        // do not get an exception while actually doing work with
        // the connections (vars which are done later in this testcase)
        xaRes1.start(Xid1, XAResource.TMNOFLAGS);
        try {
          xaRes2.start(Xid1, XAResource.TMNOFLAGS);
          // As a performance improvement, (to prevent us
          // from having to lookup the XID at start time,
          // this no longer fails until run time (var11 and 12)

          assertCondition(true);
          // failed("Did not throw exception");
        } catch (Exception ex) {
          assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException"); // @A1
        }
        conn1.close();
        conn2.close();
        JTATest.verboseOut("Done");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var006() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      Connection conn = null;
      boolean passed = true;
      StringBuffer sb = new StringBuffer();
      try {
        JTATest
            .verboseOut("TEST: Closing the connection handle before committing the transaction. should commit");

        String table = "CONNVAR6";

        try {
          JTATest.dltTmpTbl(JTATest.COLLECTION + '.' + table, pwrConn_);
        } catch (Exception e) {
        }

        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection();
        XAResource xaRes = xaConn.getXAResource();
        conn = xaConn.getConnection();

        // Generate a new transaction
        JTATestXid newXid = new JTATestXid();
        xaRes.start(newXid, XAResource.TMNOFLAGS);

        String sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table
            + " (COL1 INT)";
        JTATest.verboseOut("prepare \"" + sqlString + "\"");
        PreparedStatement stmt = conn.prepareStatement(sqlString);

        // int rc;
        stmt.execute();

        conn.close();

        xaRes.end(newXid, XAResource.TMSUCCESS);
        xaRes.commit(newXid, true); // 1 pc

        // the table should be there
        Statement s = pwrConn_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JTATest.COLLECTION
            + ".SYSTABLES WHERE TABLE_NAME='" + table + "'");
        rs.next();
        passed = rs.getString(1).equals(table);
        if (!passed) {
          sb.append("Got " + rs.getString(1) + " sb " + table + "\n");
        }
        s.close();
        JTATest.dltTmpTbl(JTATest.COLLECTION + '.' + table, pwrConn_);
        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      } finally {
        if (conn != null)
          try {
            conn.close();
          } catch (Throwable t) {
          }
      }
    }
  }

  public void Var007() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        JTATest
            .verboseOut("TEST: Closing the connection then committing the transaction. should commit");
        String table = "CONNVAR7";

        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection();
        XAResource xaRes = xaConn.getXAResource();
        Connection conn = xaConn.getConnection();

        // Generate a new transaction
        JTATestXid newXid = new JTATestXid();
        xaRes.start(newXid, XAResource.TMNOFLAGS);

        try {
          JTATest.dltTmpTbl(JTATest.COLLECTION + '.' + table, pwrConn_);
        } catch (Exception e) {
          JTATest.verboseOut(table + " could not be DROPPED: " + e);
        }

        String sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table
            + " (COL1 INT)";
        JTATest.verboseOut("prepare \"" + sqlString + "\"");
        PreparedStatement stmt = conn.prepareStatement(sqlString);

        // int rc;
        stmt.execute();
        xaRes.end(newXid, XAResource.TMSUCCESS);

        xaConn.close();

        //
        // Commit transaction.. Retry if connection is closed
        //
        try {
          xaRes.commit(newXid, true); // 1 pc
        } catch (Exception ex) {
          XAException xaEx = (XAException) ex;
          // @PDA toolbox returns rmfail
          if (xaEx.errorCode == XAException.XAER_PROTO
              || xaEx.errorCode == XAException.XAER_RMFAIL) {
            xaConn = ds.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
            xaRes = xaConn.getXAResource();
            xaRes.commit(newXid, true); // 1 pc
          } else {
            throw ex;
          }
        }

        // the table should be there
        Statement s = pwrConn_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JTATest.COLLECTION
            + ".SYSTABLES WHERE TABLE_NAME='" + table + "'");
        rs.next();
        assertCondition(rs.getString(1).equals(table));
        JTATest.dltTmpTbl(JTATest.COLLECTION + '.' + table, pwrConn_);
        s.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var008() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        JTATest
            .verboseOut("TEST: Prepare transaction, close connection, commit transaction. commit should work");

        String table = "CONNVAR8";

        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection();
        XAResource xaRes = xaConn.getXAResource();
        Connection conn = xaConn.getConnection();

        // Generate a new transaction
        JTATestXid newXid = new JTATestXid();
        xaRes.start(newXid, XAResource.TMNOFLAGS);

        try {
          JTATest.dltTmpTbl(JTATest.COLLECTION + '.' + table, pwrConn_);
        } catch (Exception e) {
          JTATest.verboseOut(table + " could not be DROPPED: " + e);
        }

        String sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table
            + " (COL1 INT)";
        JTATest.verboseOut("prepare \"" + sqlString + "\"");
        PreparedStatement stmt = conn.prepareStatement(sqlString);

        int rc;
        stmt.execute();

        xaRes.end(newXid, XAResource.TMSUCCESS);

        stmt.close(); // Should be able to close here...

        rc = xaRes.prepare(newXid);
        if (rc != XAResource.XA_OK) {
          failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
          xaConn.close();
          return;
        }

        xaConn.close();
        // Commit.. If needed, reset the connection
        try {
          xaRes.commit(newXid, false);
        } catch (Exception ex) {
          XAException xaEx = (XAException) ex;
          // @pda toolbox returns rmfail
          if (xaEx.errorCode == XAException.XAER_PROTO
              || xaEx.errorCode == XAException.XAER_RMFAIL) {
            xaConn = ds.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
            xaRes = xaConn.getXAResource();
            xaRes.commit(newXid, false);
          } else {
            throw ex;
          }
        }

        // the table should be there
        Statement s = pwrConn_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JTATest.COLLECTION
            + ".SYSTABLES WHERE TABLE_NAME='" + table + "'");
        rs.next();
        assertCondition(rs.getString(1).equals(table));
        JTATest.dltTmpTbl(JTATest.COLLECTION + '.' + table, pwrConn_);
        s.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var009() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      String table = "CONNVAR9";
      try {
        JTATest
            .verboseOut("TEST: Prepare transaction, close connection, rollback transaction. rollback should work");

        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection();
        XAResource xaRes = xaConn.getXAResource();
        Connection conn = xaConn.getConnection();

        // Generate a new transaction
        JTATestXid newXid = new JTATestXid();
        xaRes.start(newXid, XAResource.TMNOFLAGS);

        try {
          JTATest.dltTmpTbl(JTATest.COLLECTION + '.' + table, pwrConn_);
        } catch (Exception e) {
          JTATest.verboseOut(table + " could not be DROPPED: " + e);
        }

        String sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table
            + " (COL1 INT)";
        JTATest.verboseOut("prepare \"" + sqlString + "\"");
        PreparedStatement stmt = conn.prepareStatement(sqlString);

        int rc;
        stmt.execute();

        xaRes.end(newXid, XAResource.TMSUCCESS);
        rc = xaRes.prepare(newXid);
        if (rc != XAResource.XA_OK) {
          failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
          xaConn.close();
          return;
        }

        // @pdc toolbox driver needs open connection to do rollback
        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
          xaConn.close();

          xaRes.rollback(newXid);
        } else {
          xaRes.rollback(newXid);
          xaConn.close();
        }
        // the table should not be there
        Statement s = pwrConn_.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + JTATest.COLLECTION
            + ".SYSTABLES WHERE TABLE_NAME='" + table + "'");
        rs.next();
        rs.getString(1);
        failed("Did not throw exception");
      } catch (XAException e) {
        assertExceptionIsInstanceOf(e, "javax.transaction.xa.XAException"); // @PDA
                                                                            // toolbox
                                                                            // throws
                                                                            // xa
                                                                            // exception
      } catch (Exception e) {
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      } finally {
        try {
          JTATest.dltTmpTbl(JTATest.COLLECTION + '.' + table, pwrConn_);
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }

  public void Var010() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        // need to complete this testcase
        // (check for transactions, how to cleanup)
        // notApplicable();

        JTATest
            .verboseOut("TEST: Prepare transaction, close connection, leave without doing rollback or commit");

        String table = "CONNVAR10";

        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection();
        XAResource xaRes = xaConn.getXAResource();
        Connection conn = xaConn.getConnection();

        try {
          JTATest.dltTmpTbl(JTATest.COLLECTION + '.' + table, pwrConn_);

        } catch (Exception e) {
          JTATest.verboseOut(table + " could not be DROPPED: " + e);
          String msg = e.toString();
          if (msg.indexOf("Row or object " + table + " in "
              + JTATest.COLLECTION + " type *FILE in use") != -1) {
            JTATest
                .verboseOut("You likely got a failure on this variation last time");
            JTATest
                .verboseOut("Locks still exist on the file due to pending transactions");
            failed(e, "Locks still exist on the file");
            return;
          }
        }

        // Generate a new transaction
        JTATestXid newXid = new JTATestXid();
        JTATest.verboseOut(newXid.toString());
        JTATest.verboseOut("TestXid : fmt=?? gtrid=0x"
            + Long.toHexString(newXid.myGtrid) + " bqual=0x"
            + Long.toHexString(newXid.myBqual));
        xaRes.start(newXid, XAResource.TMNOFLAGS);

        // //////////////////////////////// original transaction
        // ///////////////////////
        String sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table
            + " (COL1 INT)";
        JTATest.verboseOut("prepare \"" + sqlString + "\"");
        PreparedStatement stmt = conn.prepareStatement(sqlString);

        int rc;
        stmt.execute();

        xaRes.end(newXid, XAResource.TMSUCCESS);
        rc = xaRes.prepare(newXid);
        if (rc != XAResource.XA_OK) {
          failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
          return;
        }

        xaConn.close();

        // Start over, do an operation on the table, and make sure that
        // the first operation times out, we can rollback the transaction,
        // then the second operation works.
        // In any case, try to rollback the transaction.
        boolean timeout = false;
        boolean rolledbackOk = false;
        boolean retryOk = false;
        try {
          xaConn = ds.getXAConnection();
          xaRes = xaConn.getXAResource();
          conn = xaConn.getConnection();
          if (isNTS) {
            xaRes.setTransactionTimeout(5);
          }

          // Generate a new transaction
          JTATestXid newXid2 = new JTATestXid();
          JTATest.verboseOut(newXid2.toString());
          JTATest.verboseOut("TestXid : fmt=?? gtrid=0x"
              + Long.toHexString(newXid2.myGtrid) + " bqual=0x"
              + Long.toHexString(newXid2.myBqual));
          xaRes.start(newXid2, XAResource.TMNOFLAGS);

          // ///////////////////// Get timeout ////////////////////////
          // sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table +
          // " (COL1 INT)";
          sqlString = "INSERT INTO " + JTATest.COLLECTION
              + ".CONNVAR10 VALUES(5)";
          JTATest.verboseOut("prepare \"" + sqlString + "\"");

          try {
            // Expect this operation to timeout
            stmt = conn.prepareStatement(sqlString);
            stmt.execute();
          } catch (SQLException e) {
            JTATest.verboseOut("Expected timeout exception: " + e);
            String msg = e.toString();
            if (msg.indexOf("Row or object " + table + " in "
                + JTATest.COLLECTION + " type *FILE in use") != -1) {
              JTATest.verboseOut("Got the timeout");
              timeout = true;
            } else {
              try {
                xaRes.end(newXid2, XAResource.TMNOFLAGS);
              } catch (XAException e1) {

              }
              xaRes.rollback(newXid2);
              timeout = false;
              throw e;
            }
          }
          try { // @PDC toolbox now thows RCs that are > 0
            xaRes.end(newXid2, XAResource.TMSUCCESS);
          } catch (XAException e) {

          }
          xaRes.rollback(newXid2);

          boolean isTransPresent = false;
          JTATransInfo[] match;
          if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
            // ///////////////////// Rollback original transaction.
            match = JTATest.getTransInfo();
            if (match == null) {
              failed("Didn't find any transactions -- Verify that user has *JOBCTL permission:  CHGUSRPRF USRPRF(JAVA) SPCAUT(*JOBCTL)");
              return;
            }
            JTATest.verboseOut("Matching XID: " + newXid);

            for (int i = 0; (i < match.length) && (match[i] != null); i++) {
              if (match[i].getState().equalsIgnoreCase("prepared")) {
                if (newXid.match(match[i])) {
                  isTransPresent = true;
                  JTATest
                      .verboseOut("Found the expected transaction in the correct ("
                          + match[i] + ") state");
                  break;
                }
              } else {
                JTATest.verboseOut("Skipping " + match[i].toString());
              }
            }
            if (!isTransPresent) {
              failed("Couldn't find the expected transaction");
              return;
            }
          }

          // Rollback the original transaction
          xaRes.rollback(newXid);
          // Set the newXid to null so that the exception handler
          // and finally block below won't try to end or roll it back.
          JTATestXid newXidCopy = newXid;
          newXid = null;

          // ///////////////////// Try again
          if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
            isTransPresent = false;
            match = JTATest.getTransInfo();
            if (match != null) {
              JTATest.verboseOut("Matching XID: " + newXidCopy);
              for (int i = 0; (i < match.length) && (match[i] != null); i++) {
                if (newXidCopy.match(match[i])) {
                  isTransPresent = true;
                  JTATest
                      .verboseOut("Failed! Found the transaction, it was unexpected");
                  break;
                }
              }
            }
            // We should have rolled back the original transaction.
            if (!isTransPresent) {
              rolledbackOk = true;
            } else {
              rolledbackOk = false;
            }
          } else
            rolledbackOk = true;

          // Now, retry the create table so that we can ensure that it completes
          // successfully because the first transaction was rolled back.
          // Generate a new transaction
          newXid2 = new JTATestXid();
          JTATest.verboseOut(newXid2.toString());
          xaRes.start(newXid2, XAResource.TMNOFLAGS);

          sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table
              + " (COL1 INT)";
          JTATest.verboseOut("prepare \"" + sqlString + "\"");
          stmt = conn.prepareStatement(sqlString);

          try {
            // Expect this operation to timeout
            stmt.execute();
            retryOk = true;
          } catch (Exception e) {
            retryOk = false;
          } finally {
            xaRes.end(newXid2, XAResource.TMSUCCESS);
            xaRes.rollback(newXid2);
          }
        } finally {
          try {
            if (newXid != null) {
              JTATest.verboseOut("Cleanup by rolling back: " + newXid);
              xaRes.rollback(newXid);
            }
          } catch (Exception e) {
            JTATest
                .verboseOut("You should try to manually rollback: " + newXid);
          }
        }
        JTATest.verboseOut("Timeout=" + timeout + ", rolledbackOk="
            + rolledbackOk + ", retryOk=" + retryOk);
        assertCondition((timeout == true) && (rolledbackOk)
            && (retryOk == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var011() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        JTATest
            .verboseOut("TEST: Try to have 2 Connections for one transaction. Do work on this conn. Should not be able to");

        XADataSource ds = newXADataSource();

        XAConnection xaConn1 = ds.getXAConnection();
        XAResource xaRes1 = xaConn1.getXAResource();
        // Connection conn1 = xaConn1.getConnection();

        XAConnection xaConn2 = ds.getXAConnection();
        XAResource xaRes2 = xaConn2.getXAResource();
        // Connection conn2 = xaConn2.getConnection();

        // Generate a new transaction
        JTATestXid Xid1 = new JTATestXid();

        // now associate both connections with the transaction
        // (xaRes1 is the one which would be valid)
        xaRes1.start(Xid1, XAResource.TMNOFLAGS);
        try {
          xaRes2.start(Xid1, XAResource.TMNOFLAGS);
        } catch (Exception ex) {
          if (isNTS) {
            if (JTATest.verbose) {
              ex.printStackTrace();
            }
            if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
              assertExceptionIsInstanceOf(ex,
                  "javax.transaction.xa.XAException");
              return; // @B1A
            } else {

              if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
                assertExceptionIsInstanceOf(ex,
                    "com.ibm.db2.jdbc.app.DB2XAException");
                return;
              } else {

                if (ex instanceof javax.transaction.xa.XAException) {
                  XAException xaEx = (XAException) ex;
                  if (xaEx.errorCode != XAException.XAER_DUPID) {
                    failed(ex, "Second start of transaction failed");
                    return;
                  } else {
                    assertCondition(true);
                    return;
                  }
                } else {
                  failed(ex, "Incorrect exception type");
                  return;
                }
              }

            }
          } else {

            if (ex instanceof javax.transaction.xa.XAException) {
              XAException xaEx = (XAException) ex;
              if (xaEx.errorCode != XAException.XAER_DUPID) {
                failed(ex, "Second start of transaction failed");
                return;
              }
            } else {
              failed(ex, "Incorrect exception type");
              return;
            }
          }
        }

        // This code is no longer valid.
        // Statement s1 = conn1.createStatement();
        // Statement s2 = conn2.createStatement();
        // // using s1 should work
        // s1.executeQuery("SELECT * FROM " + JTATest.COLLECTION +
        // ".SYSTABLES");
        // // using s2 should throw an exception
        // try {
        // s2.executeQuery("SELECT * FROM " + JTATest.COLLECTION +
        // ".SYSTABLES");
        // failed("Did not throw exception");
        // }
        // catch (Exception ex) {
        // assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
        // }

        JTATest.verboseOut("Done");
        if (isNTS) {
          failed("Exception should have been thrown");
        } else {
          assertCondition(true);
        }

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var012() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        JTATest
            .verboseOut("TEST: Try to have 2 Connections for one transaction. Do work on this conn. Should not be able to");
        // same as the var above. except the order is reversed

        XADataSource ds = newXADataSource();

        XAConnection xaConn1 = ds.getXAConnection();
        XAResource xaRes1 = xaConn1.getXAResource();
        // Connection conn1 = xaConn1.getConnection();

        XAConnection xaConn2 = ds.getXAConnection();
        XAResource xaRes2 = xaConn2.getXAResource();
        // Connection conn2 = xaConn2.getConnection();

        // Generate a new transaction
        JTATestXid Xid1 = new JTATestXid();

        // now associate both connections with the transaction
        // (xaRes1 is the one which would be valid)
        xaRes1.start(Xid1, XAResource.TMNOFLAGS);
        try {
          xaRes2.start(Xid1, XAResource.TMNOFLAGS);
        } catch (Exception ex) {
          if (isNTS) {
            if (JTATest.verbose) {
              ex.printStackTrace();
            }
            if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
              assertExceptionIsInstanceOf(ex,
                  "javax.transaction.xa.XAException");
              return; // @B1A
            } else {
              if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
                assertExceptionIsInstanceOf(ex,
                    "com.ibm.db2.jdbc.app.DB2XAException");

                return;

              } else {
                if (ex instanceof javax.transaction.xa.XAException) {
                  XAException xaEx = (XAException) ex;
                  if (xaEx.errorCode != XAException.XAER_DUPID) {
                    failed(ex, "Second start of transaction failed");
                    return;
                  } else {
                    assertCondition(true);
                    return;
                  }
                } else {
                  failed(ex, "Incorrect exception type");
                  return;
                }
              }
            }

          } else {

            if (ex instanceof javax.transaction.xa.XAException) {
              XAException xaEx = (XAException) ex;
              if (xaEx.errorCode != XAException.XAER_DUPID) {
                failed(ex, "Second start of transaction failed");
                return;
              }
              // Fall through
            } else {
              failed(ex, "Unexpected exception type");
              return;
            }
          }
        }

        JTATest.verboseOut("Done");
        if (isNTS) {
          failed("Exception should have been thrown");
        } else {
          assertCondition(true);
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var013() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        JTATest
            .verboseOut("TEST: 2 Connections/transactions. Try to create a table with both");

        String table = JTATest.COLLECTION + ".CONNVAR13";

        XADataSource ds1 = newXADataSource();
        XAConnection xaConn1 = ds1.getXAConnection();
        XAResource xaRes1 = xaConn1.getXAResource();
        Connection conn1 = xaConn1.getConnection();

        XADataSource ds2 = newXADataSource();
        XAConnection xaConn2 = ds2.getXAConnection();
        XAResource xaRes2 = xaConn2.getXAResource();
        Connection conn2 = xaConn2.getConnection();

        // Generate a new transaction
        JTATestXid Xid1 = new JTATestXid();
        JTATestXid Xid2 = new JTATestXid();

        // now associate both connections with their transactions
        xaRes1.start(Xid1, XAResource.TMNOFLAGS);
        xaRes2.start(Xid2, XAResource.TMNOFLAGS);

        Statement s1 = conn1.createStatement();
        Statement s2 = conn2.createStatement();

        s1.execute("CREATE TABLE " + table + " (COLA INT)");
        // next one should throw an exception
        try {
          s2.execute("CREATE TABLE " + table + " (COLB INT)");
        } catch (Exception ex) {
          assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
        }

        xaRes1.end(Xid1, XAResource.TMSUCCESS);
        xaRes1.rollback(Xid1);
        xaRes2.end(Xid2, XAResource.TMSUCCESS);
        xaRes2.rollback(Xid2);
        JTATest.verboseOut("Done");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var014() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        JTATest
            .verboseOut("TEST: 2 Connections/transactions. create a table with first one");
        JTATest
            .verboseOut("TEST: Insert a row. (do not commit). Do a query with the second one");
        JTATest.verboseOut("TEST: Check to see if the value was inserted.");

        String table = JTATest.COLLECTION + ".CONNVAR14";

        XADataSource ds1 = newXADataSource();
        XAConnection xaConn1 = ds1.getXAConnection();
        XAResource xaRes1 = xaConn1.getXAResource();
        Connection conn1 = xaConn1.getConnection();

        XADataSource ds2 = newXADataSource();
        XAConnection xaConn2 = ds2.getXAConnection();
        XAResource xaRes2 = xaConn2.getXAResource();
        Connection conn2 = xaConn2.getConnection();

        // get the default transaction isolation level
        if (conn1.getTransactionIsolation() == Connection.TRANSACTION_READ_UNCOMMITTED)
          JTATest
              .verboseOut("Default TRANS ISO LEVEL = TRANSACTION_READ_UNCOMMITTED");

        // Generate a new transaction
        JTATestXid Xid1 = new JTATestXid();
        JTATestXid Xid2 = new JTATestXid();

        // now associate both connections with their transactions
        xaRes1.start(Xid1, XAResource.TMNOFLAGS);
        xaRes2.start(Xid2, XAResource.TMNOFLAGS);

        Statement s1 = conn1.createStatement();
        Statement s2 = conn2.createStatement();

        s1.execute("CREATE TABLE " + table + " (COLA INT)");
        s1.executeUpdate("INSERT INTO " + table + " VALUES (101)");
        // the table should still be locked, since the CREATE hasn't been
        // committed
        try {
          ResultSet rs = s2.executeQuery("SELECT * FROM " + table);
          failed("Exception not thrown on locked file " + rs);
        } catch (Exception ex) {
          assertExceptionIsInstanceOf(ex, "java.sql.SQLException");
        }
        xaRes1.end(Xid1, XAResource.TMSUCCESS);
        xaRes1.rollback(Xid1);
        JTATest.verboseOut("Done");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var015() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        JTATest
            .verboseOut("TEST: 2 Connections/transactions. create a table with first one. commit");
        JTATest
            .verboseOut("TEST: Insert a row. (do not commit). Do a query with the second one");
        JTATest.verboseOut("TEST: Check to see if the value was inserted.");

        String table = JTATest.COLLECTION + ".CONNVAR15";

        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection();
        XAResource xaRes = xaConn.getXAResource();
        Connection conn = xaConn.getConnection();

        XADataSource ds1 = newXADataSource();
        XAConnection xaConn1 = ds1.getXAConnection();
        XAResource xaRes1 = xaConn1.getXAResource();
        Connection conn1 = xaConn1.getConnection();

        XADataSource ds2 = newXADataSource();
        XAConnection xaConn2 = ds2.getXAConnection();
        XAResource xaRes2 = xaConn2.getXAResource();
        Connection conn2 = xaConn2.getConnection();

        // Generate a new transaction
        JTATestXid Xid = new JTATestXid();
        JTATestXid Xid1 = new JTATestXid();
        JTATestXid Xid2 = new JTATestXid();

        // now associate both connections with their transactions
        xaRes.start(Xid, XAResource.TMNOFLAGS);
        xaRes1.start(Xid1, XAResource.TMNOFLAGS);
        xaRes2.start(Xid2, XAResource.TMNOFLAGS);

        Statement s2 = conn2.createStatement();
        Statement s1 = conn1.createStatement();
        Statement s = conn.createStatement();

        try {
          s.execute("DROP TABLE " + table);
        } catch (Exception e) {
        }
        s.execute("CREATE TABLE " + table + " (COLA INT)");
        s.close();
        xaRes.end(Xid, XAResource.TMSUCCESS);
        xaRes.commit(Xid, true);

        s1.executeUpdate("INSERT INTO " + table + " VALUES (101)");

        // get the default transaction isolation level
        if (conn1.getTransactionIsolation() == Connection.TRANSACTION_READ_UNCOMMITTED) {
          JTATest
              .verboseOut("Default TRANS ISO LEVEL = TRANSACTION_READ_UNCOMMITTED");
          // should be able to read
          ResultSet rs = s2.executeQuery("SELECT * FROM " + table);
          rs.next();
          assertCondition(rs.getInt(1) == 101);
        } else
          assertCondition(true);
        xaRes1.end(Xid1, XAResource.TMSUCCESS);
        xaRes1.rollback(Xid1);
        xaRes2.end(Xid2, XAResource.TMSUCCESS);
        xaRes2.rollback(Xid2);
        JTATest.verboseOut("Done");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  public void Var016() {

    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      Connection conn = null;
      try {
        JTATest.verboseOut("TEST: Do work on a connection after xaRes.end()");

        String table = JTATest.COLLECTION + ".CONNVAR16";
        String table2 = JTATest.COLLECTION + ".CONNVAR16A";
        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection();
        XAResource xaRes = xaConn.getXAResource();
        conn = xaConn.getConnection();

        // Generate a new transaction
        JTATestXid Xid = new JTATestXid();

        // associate the transaction
        xaRes.start(Xid, XAResource.TMNOFLAGS);

        Statement s = conn.createStatement();

        try {
          s.execute("DROP TABLE " + table);
        } catch (Exception e) {
        }
        s.execute("CREATE TABLE " + table + " (COLA INT)");
        // disassociate the transaction
        xaRes.end(Xid, XAResource.TMSUCCESS);

        // now, try to do work on the connection
        try {
          s.execute("DROP TABLE " + table2);
          //
          // For NTS, more work is allowed on the local connection
          // NTS will still throw exception
          //
          failed("Did not throw exception");
          conn.close();
          return;
        } catch (Exception ex) {
          if (JTATest.verbose) {
            ex.printStackTrace();
          }
          if (isNTS) {
            //
            // For NTS we should get the message that the table couldn't be
            // found
            //
            String text = ex.getMessage().toUpperCase();
            if (text.indexOf("NOT FOUND") >= 0)
              succeeded();
            else
              failed(ex, "wrong exception, expected file not found");

          } else {
            assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
            XAException xaEx = (XAException) ex;
            if (xaEx.errorCode != XAException.XAER_PROTO) {
              failed(ex, "Incorrect exception error code");
              xaRes.rollback(Xid);
              conn.close();
              return;
            }
          }
        }

        xaRes.rollback(Xid);
        conn.close();
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
        if (conn != null) {
          try {
            conn.close();
          } catch (Exception e2) {
          }
        }
      }
    }
  }

  public void Var017() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        JTATest.verboseOut("TEST: Simple test of JTATest.getTransInfo()");

        if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
          // ///////////////////// Rollback original transaction.
          JTATransInfo[] match = JTATest.getTransInfo();
          if (match == null) {
            JTATest.verboseOut("No transactions active or in-doubt");
            assertCondition(true);
            return;
          }
          // boolean isTransPresent = false;
          JTATestXid newXid = new JTATestXid();
          JTATest.verboseOut("Matching XID: " + newXid);
          for (int i = 0; (i < match.length) && (match[i] != null); i++) {
            JTATest.verboseOut(match[0].toString());
          }
        }
        assertCondition(true);
      } catch (Exception e) {
        failed(e, "Unexpected Transaction");
      }
    }
  }

  public void Var018() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        JTATest
            .verboseOut("TEST: use getCatalog prior to starting a transaction XAConnection.getConnection");

        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection();
        XAResource xaRes = xaConn.getXAResource();

        Connection conn = xaConn.getConnection();

        conn.getCatalog();

        //
        // There won't be a problem if you commit
        // conn.commit();

        // Generate a new transaction
        JTATestXid newXid = new JTATestXid();
        xaRes.start(newXid, XAResource.TMNOFLAGS);

        String sqlString = "INSERT INTO " + basTbl + " VALUES('" + insStr
            + "2')";
        JTATest.verboseOut("prepare \"" + sqlString + "\"");
        PreparedStatement stmt = conn.prepareStatement(sqlString);

        int rc;
        rc = stmt.executeUpdate();
        if (rc != 1) {
          failed("Expected 1 row inserted, got " + rc);
          conn.close();
          return;
        }

        xaRes.end(newXid, XAResource.TMSUCCESS);
        rc = xaRes.prepare(newXid);
        if (rc != XAResource.XA_OK) {
          failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
          conn.close();
          return;
        }

        xaRes.commit(newXid, false);
        conn.close();
        JTATest.verboseOut("Done");
        assertCondition(true);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * Test getServerJobName
   **/
  public void Var019() {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      if (isIasp && (!isNTS)) {
        notApplicable("Not applicable for IASP and not NTS");
        return;
      }
      if (checkJdbc20StdExt()) {
        try {
          JTATest.verboseOut("TEST: use getServerJobName");

          XADataSource ds = newXADataSource();

          XAConnection xaConn = ds.getXAConnection();
          // XAResource xaRes = xaConn.getXAResource();

          Connection conn = xaConn.getConnection();
          String serverJobName;
          if (JDReflectionUtil.instanceOf(conn,"com.ibm.db2.jdbc.app.DB2ConnectionHandle")) {
            serverJobName = JDReflectionUtil.callMethod_S( conn, "getServerJobName");
          } else if (JDReflectionUtil.instanceOf(conn,"com.ibm.db2.jdbc.app.UDBConnectionHandle")) {
              serverJobName = JDReflectionUtil.callMethod_S( conn, "getServerJobName");
          } else {
            serverJobName = "class of connection is "
                + conn.getClass().getName();
          }

          assertCondition(serverJobName.indexOf("QSQSRVR") >= 0
              && serverJobName.indexOf("QUSER") >= 0, "Server name "
              + serverJobName + " not valid");

        } catch (Exception e) {
          failed(e, "Unexpected Exception");
        }
      }
    } else {
      notApplicable("Native driver variation");
    }
  }

  // TODO: Copy Var008, but close result set, statements, etc.. after the
  // xaRes.end()

  // Like Var010, but let transaction timeout before
  // attempting to acquire lock.
  public void Var020() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        // need to complete this testcase
        // (check for transactions, how to cleanup)
        // notApplicable();

        JTATest
            .verboseOut("TEST: Prepare transaction, close connection, leave without doing rollback or commit");

        String table = "CONNVAR10";

        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection();
        XAResource xaRes = xaConn.getXAResource();
        Connection conn = xaConn.getConnection();

        try {
          JTATest.dltTmpTbl(JTATest.COLLECTION + '.' + table, pwrConn_);

        } catch (Exception e) {
          JTATest.verboseOut(table + " could not be DROPPED: " + e);
          String msg = e.toString();
          if (msg.indexOf("Row or object " + table + " in "
              + JTATest.COLLECTION + " type *FILE in use") != -1) {
            JTATest
                .verboseOut("You likely got a failure on this variation last time");
            JTATest
                .verboseOut("Locks still exist on the file due to pending transactions");
            failed(e, "Locks still exist on the file");
            return;
          }
        }

        // Generate a new transaction
        JTATestXid newXid = new JTATestXid();
        JTATest.verboseOut(newXid.toString());
        JTATest.verboseOut("TestXid : fmt=?? gtrid=0x"
            + Long.toHexString(newXid.myGtrid) + " bqual=0x"
            + Long.toHexString(newXid.myBqual));
        System.out.println("Setting of locker to 10");
        xaRes.setTransactionTimeout(10);

        xaRes.start(newXid, XAResource.TMNOFLAGS);

        // //////////////////////////////// original transaction
        // ///////////////////////
        String sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table
            + " (COL1 INT)";
        JTATest.verboseOut("prepare \"" + sqlString + "\"");
        PreparedStatement stmt = conn.prepareStatement(sqlString);

        int rc;
        stmt.execute();

        xaRes.end(newXid, XAResource.TMSUCCESS);
        rc = xaRes.prepare(newXid);
        if (rc != XAResource.XA_OK) {
          failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
          return;
        }

        xaConn.close();

        // Start over, do an operation on the table, and make sure that
        // the first operation times out, we can rollback the transaction,
        // then the second operation works.
        // In any case, try to rollback the transaction.
        boolean timeout = false;
        boolean rolledbackOk = false;
        boolean retryOk = false;
        try {
          xaConn = ds.getXAConnection();
          xaRes = xaConn.getXAResource();
          conn = xaConn.getConnection();
          if (isNTS) {
            System.out.println("Setting transaction timeout to 5");
            xaRes.setTransactionTimeout(5);
          }

          // Generate a new transaction
          JTATestXid newXid2 = new JTATestXid();
          JTATest.verboseOut(newXid2.toString());
          JTATest.verboseOut("TestXid : fmt=?? gtrid=0x"
              + Long.toHexString(newXid2.myGtrid) + " bqual=0x"
              + Long.toHexString(newXid2.myBqual));
          xaRes.start(newXid2, XAResource.TMNOFLAGS);

          // ///////////////////// Get timeout ////////////////////////
          // sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table +
          // " (COL1 INT)";
          sqlString = "INSERT INTO " + JTATest.COLLECTION
              + ".CONNVAR10 VALUES(5)";
          JTATest.verboseOut("prepare \"" + sqlString + "\"");

          try {
            // Expect this operation to timeout
            System.out.println("Thread sleeping for 6 seconds");
            Thread.sleep(6000);
            System.out.println("Back from sleep");
            stmt = conn.prepareStatement(sqlString);
            System.out.println("Back from prepare");

            stmt.execute();
            System.out.println("Back from execute");
          } catch (SQLException e) {
            JTATest.verboseOut("Expected timeout exception: " + e);
            String msg = e.toString();
            System.out.println("Got exception " + msg);
            /* Toolbox error message */ 
            if (msg.indexOf("Row or object " + table + " in "
                + JTATest.COLLECTION + " type *FILE in use") != -1) {
              JTATest.verboseOut("Got the timeout");
              timeout = true;
            /* Native error message */ 
            } else if (msg.indexOf("ROLLBACK required") != -1) {
              JTATest.verboseOut("Got the timeout");
              timeout = true;
            } else {
              try {
                xaRes.end(newXid2, XAResource.TMNOFLAGS);
              } catch (XAException e1) {

              }
              try { 
                 xaRes.rollback(newXid2);
              } catch (XAException e1) {

              }
              timeout = false;
              throw e;
            }
          }
          try { // @PDC toolbox now thows RCs that are > 0
            xaRes.end(newXid2, XAResource.TMSUCCESS);
          } catch (XAException e) {

          }
          xaRes.rollback(newXid2);

          boolean isTransPresent = false;
          JTATransInfo[] match;
          if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
            // ///////////////////// Rollback original transaction.
            match = JTATest.getTransInfo();
            if (match == null) {
              failed("Didn't find any transactions -- Verify that user has *JOBCTL permission:  CHGUSRPRF USRPRF(JAVA) SPCAUT(*JOBCTL)");
              return;
            }
            JTATest.verboseOut("Matching XID: " + newXid);

            for (int i = 0; (i < match.length) && (match[i] != null); i++) {
              if (match[i].getState().equalsIgnoreCase("prepared")) {
                if (newXid.match(match[i])) {
                  isTransPresent = true;
                  JTATest
                      .verboseOut("Found the expected transaction in the correct ("
                          + match[i] + ") state");
                  break;
                }
              } else {
                JTATest.verboseOut("Skipping " + match[i].toString());
              }
            }
            if (!isTransPresent) {
              failed("Couldn't find the expected transaction");
              return;
            }
          }

          // Rollback the original transaction
          xaRes.rollback(newXid);
          // Set the newXid to null so that the exception handler
          // and finally block below won't try to end or roll it back.
          JTATestXid newXidCopy = newXid;
          newXid = null;

          
          // ///////////////////// Try again
          if (getDriver() != JDTestDriver.DRIVER_TOOLBOX) {
            isTransPresent = false;
            match = JTATest.getTransInfo();
            if (match != null) {
              JTATest.verboseOut("Matching XID: " + newXidCopy);
              for (int i = 0; (i < match.length) && (match[i] != null); i++) {
                if (newXidCopy.match(match[i])) {
                  isTransPresent = true;
                  JTATest
                      .verboseOut("Failed! Found the transaction, it was unexpected");
                  break;
                }
              }
            }
            // We should have rolled back the original transaction.
            if (!isTransPresent) {
              rolledbackOk = true;
            } else {
              rolledbackOk = false;
            }
          } else
            rolledbackOk = true;

          // Now, retry the create table so that we can ensure that it completes
          // successfully because the first transaction was rolled back.
          // Generate a new transaction
          newXid2 = new JTATestXid();
          JTATest.verboseOut(newXid2.toString());
          xaRes.start(newXid2, XAResource.TMNOFLAGS);

          sqlString = "CREATE TABLE " + JTATest.COLLECTION + "." + table
              + " (COL1 INT)";
          JTATest.verboseOut("prepare \"" + sqlString + "\"");
          stmt = conn.prepareStatement(sqlString);

          try {
            // Expect this operation to timeout
            stmt.execute();
            retryOk = true;
          } catch (Exception e) {
            retryOk = false;
          } finally {
            xaRes.end(newXid2, XAResource.TMSUCCESS);
            xaRes.rollback(newXid2);
          }
        } finally {
          try {
            if (newXid != null) {
              JTATest.verboseOut("Cleanup by rolling back: " + newXid);
              xaRes.rollback(newXid);
            }
          } catch (Exception e) {
            JTATest
                .verboseOut("You should try to manually rollback: " + newXid);
          }
        }
        JTATest.verboseOut("Timeout=" + timeout + ", rolledbackOk="
            + rolledbackOk + ", retryOk=" + retryOk);
        assertCondition((timeout == true) && (rolledbackOk)
            && (retryOk == true));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

}
