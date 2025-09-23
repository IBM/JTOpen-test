///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JTAResource.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.JTA;

import com.ibm.as400.access.AS400;

import test.JDTestDriver;
import test.JTATest;
import test.PasswordVault;
import test.JD.JDTestUtilities;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.io.*;

/**
 * Testcase JTAResource. This tests the following methods of the JDBC XAResource
 * class:
 * 
 * <ul>
 * <li>commit()</li>
 * <li>end()</li>
 * <li>forget()</li>
 * <li>getTransactionTimeout()</li>
 * <li>isSameRM()</li>
 * <li>prepare()</li>
 * <li>rollback()</li>
 * <li>setTransactionTimeout()</li>
 * <li>start()</li>
 * <li>toString()</li>
 * </ul>
 **/
public class JTAResource extends JTATestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAResource";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTAStdTest.main(newArgs); 
   }
  //
  // Print the global ids so we can figure out which testcase has a leak.
  // This is needed if we can't drop a table at the end.
  //
  byte[] globalIds[];
  // Private data.
  protected static String table_ = JTATest.COLLECTION + ".JTARESOURCE";
  BufferedReader is = null;

  /**
   * Constructor.
   **/
  public JTAResource(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream,  String password) {
    super(systemObject, "JTAResource", namesAndVars, runMode, fileOutputStream,
         password);
  }

  public JTAResource(AS400 systemObject, String testname,
      Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password) {
    super(systemObject, testname, namesAndVars, runMode, fileOutputStream,
         password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    lockSystem("JTATEST", 600);
    globalIds = new byte[53][];

    for (int i = 0; i < globalIds.length; i++) {
      globalIds[i] = null;
    }
    Connection c = testDriver_.getConnection(baseURL_ + ";errors=full",
        userId_, encryptedPassword_);

    isIasp = JDTestUtilities.isIasp(c);
    Statement s = c.createStatement();

    table_ = JTATest.COLLECTION + ".JTARESOURCE";
    /* */
    /* Make sure the table is gone */
    /* */
    try {
      s.executeUpdate("DROP TABLE " + table_);
    } catch (SQLException e) {
      if (e.getMessage().indexOf("not found") > 0) {
        // Ignore the file not found exception
      } else {
        e.printStackTrace();
      }
    }
    s.executeUpdate("CREATE TABLE " + table_ + " (COL1 INTEGER)");
    s.executeUpdate("GRANT ALL ON " + table_ + " TO PUBLIC");
    s.close();
    c.close();

    if (interactive_) {
      is = new BufferedReader(new InputStreamReader(System.in));
    }

  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    //
    // Run the garbage collector to clean up any stray references.
    // This should prevent the DROP table from failing
    //
    System.gc();
    Connection c = testDriver_.getConnection(baseURL_ + ";errors=full",
        userId_, encryptedPassword_);
    Statement s = c.createStatement();
    try {
      s.executeUpdate("DROP TABLE " + table_);
    } catch (SQLException e) {
      if (e.getMessage().indexOf("in use") > 0) {
        e.printStackTrace();
        System.out.println("Dumping global IDS");
        for (int i = 0; i < globalIds.length; i++) {
          if (globalIds[i] != null) {
            System.out.print(i + ": X'");
            for (int j = 0; j < globalIds[i].length; j++) {
              int value = globalIds[i][j];
              if (value < 16)
                System.out.print("0");
              System.out.print(Integer.toHexString(value));
            }
            System.out.println("'");
          }
        }
      }
    }
    s.close();
    unlockSystem();
    c.close();
  }

  protected int countRows(int value) throws Exception {
    Connection c = testDriver_.getConnection(baseURL_ + ";errors=full",
        userId_, encryptedPassword_);
    Statement s = c.createStatement();
    ResultSet rs = s.executeQuery("SELECT * FROM " + table_ + " WHERE COL1 = "
        + value);
    int count = 0;
    while (rs.next())
      ++count;
    rs.close();
    s.close();
    c.close();
    // System.out.println("Row count = " + count);
    return count;
  }

  /**
   * commit() - Pass a null xid.
   **/
  public void Var001() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        Exception saveException = null;
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[1] = xid.getGlobalTransactionId();

        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(4)");
        xar.end(xid, XAResource.TMSUCCESS);
        int errorCode = 0;
        try {
          xar.commit(null, true);
        } catch (XAException e) {
          errorCode = e.errorCode;
          saveException = e;
        }
        xar.rollback(xid);
        xac.close();
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && !useUDBDataSource) {
          /* The more appropriate error code is that the XID is not valid */
          boolean condition = (errorCode == XAException.XAER_NOTA)
              && (countRows(4) == 0);
          if (!condition) {
            System.out.println("errorCode -<" + errorCode
                + ">- should be XAER_NOTA" + XAException.XAER_NOTA);
            System.out.println("countRows(4) -< " + countRows(4)
                + " >- should be 0 ");
            if (saveException != null)
              saveException.printStackTrace();
          }
          assertCondition(condition);
        } else {
          assertCondition((errorCode == XAException.XAER_INVAL)
              && (countRows(4) == 0));

        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * commit() - Pass an invalid xid.
   **/
  public void Var002() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[2] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(5)");
        xar.end(xid, XAResource.TMSUCCESS);
        int errorCode = 0;
        try {
          xar.commit(new JTATestXid(), true);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xar.rollback(xid);
        xac.close();

        assertCondition((errorCode == XAException.XAER_NOTA)
            && (countRows(5) == 0));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * commit() - Pass an valid xid, with true, and the transaction has not been
   * prepared.
   **/
  public void Var003() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[3] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(654)");
        xar.end(xid, XAResource.TMSUCCESS);
        xar.commit(xid, true);
        xac.close();

        assertCondition(countRows(654) == 1);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * commit() - Pass an valid xid, with true, and the transaction has been
   * prepared.
   **/
  public void Var004() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[4] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(4654)");
        xar.end(xid, XAResource.TMSUCCESS);
        xar.prepare(xid);
        int errorCode = 0;
        try {
          xar.commit(xid, true);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xar.rollback(xid);
        xac.close();

        assertCondition((errorCode == XAException.XAER_PROTO)
            && (countRows(4654) == 0));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * commit() - Pass an valid xid, with false, and the transaction has not been
   * prepared.
   **/
  public void Var005() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[4] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-1654)");
        xar.end(xid, XAResource.TMSUCCESS);
        int errorCode = 0;
        try {
          xar.commit(xid, false);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xar.rollback(xid);
        xac.close();

        assertCondition((errorCode == XAException.XAER_PROTO)
            && (countRows(-1654) == 0));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * commit() - Pass an valid xid, with false, and the transaction has been
   * prepared.
   **/
  public void Var006() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[6] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-654)");
        xar.end(xid, XAResource.TMSUCCESS);
        xar.prepare(xid);
        xar.commit(xid, false);
        xac.close();

        assertCondition(countRows(-654) == 1);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * commit() - Before end has been called.
   **/
  public void Var007() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[7] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(432)");
        int errorCode = 0;
        try {
          xar.commit(xid, true);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xar.end(xid, XAResource.TMSUCCESS);
        xar.rollback(xid);
        xac.close();

        assertCondition((errorCode == XAException.XAER_PROTO)
            && (countRows(432) == 0));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * commit() - Before start has been called.
   **/
  public void Var008() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[8] = xid.getGlobalTransactionId();
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(3234)");
        int errorCode = 0;
        try {
          xar.commit(xid, true);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        c.commit(); // Normal JDBC Commit to get the row in.
        xac.close();

        assertCondition((errorCode == XAException.XAER_NOTA)
            && (countRows(3234) == 1));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * commit() - After a transaction has been marked as failed.
   **/
  public void Var009() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[9] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(5868)");
        int errorCode1 = 0;
        try {
          xar.end(xid, XAResource.TMFAIL);
        } catch (XAException e) {
          // DB2/400 resource manager gives the rollback
          // exception in this senario.
          errorCode1 = e.errorCode;
        }
        int errorCode2 = 0;
        try {
          xar.commit(xid, true);
        } catch (XAException e) {
          errorCode2 = e.errorCode;
        }
        xar.rollback(xid);
        xac.close();

        int rowCount = countRows(5);
        int expectedErrorCode2 = XAException.XAER_PROTO;
        if (useUDBDataSource) {
          expectedErrorCode2 = XAException.XAER_NOTA;
        }

        boolean condition = false;
        //  toolbox driver behavoir
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          condition = (errorCode2 == expectedErrorCode2) && (rowCount == 0);
        } else {
          condition = (errorCode1 == XAException.XA_RBROLLBACK)
              && (errorCode2 == expectedErrorCode2) && (rowCount == 0);
        }
        if (!condition) {
          System.out
              .println("errorCode1 should be XAException.XA_RBROLLBACK but is "
                  + errorCode1);
          System.out.println("errorCode2 should be " + expectedErrorCode2
              + " but is " + errorCode2);
          System.out.println("countRows(5) should be 0, but is " + rowCount);
        }

        assertCondition(condition);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * end() - Pass a null xid.
   **/
  public void Var010() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[10] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-8432)");
        int errorCode = 0;
        try {
          xar.end(null, XAResource.TMSUCCESS);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xar.end(xid, XAResource.TMSUCCESS);
        xar.rollback(xid);
        xac.close();
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && !useUDBDataSource) {
          /* JWE changed to XAER_NOTA -- XID is not valid */
          boolean condition = (errorCode == XAException.XAER_NOTA)
              && (countRows(-8432) == 0);
          if (!condition) {
            System.out.println("errorCode -<" + errorCode
                + ">- should be XAER_NOTA" + XAException.XAER_NOTA);
            System.out.println("countRows(-8432) -< " + countRows(-8432)
                + " >- should be 0 ");
          }

          assertCondition(condition);
        } else {

          assertCondition((errorCode == XAException.XAER_INVAL)
              && (countRows(-8432) == 0));
        }

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * end() - Pass a bad xid.
   **/
  public void Var011() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      int errorCode = 0;
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[11] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-4432)");
        try {
          xar.end(new JTATestXid(), XAResource.TMSUCCESS);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        try {
          xar.end(xid, XAResource.TMSUCCESS);
        } catch (Exception e) {
          System.out
              .println("Exception caught on real XA end -- could be native driver problem in issue 29649 ");
        }
        xar.rollback(xid);
        xac.close();
        int rowCount = countRows(-4432);
        //  toolbox driver
        boolean condition = false;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          condition = (errorCode == XAException.XAER_PROTO) && (rowCount == 0);
        } else {
          condition = (errorCode == XAException.XAER_NOTA) && (rowCount == 0);
        }
        if (!condition) {
          System.out
              .println("errorCode should be XAException.XAER_NOTA, but is "
                  + errorCode
                  + " note.. in V5R4 this will fail -- see issue 29649");
          System.out.println("countRows(-4432) should be 0 but is " + rowCount);
        }
        assertCondition(condition);
      } catch (Exception e) {
        failed(e, "Unexpected Exception errorcode from bad xaend was "
            + errorCode);
      }
    }
  }

  /**
   * end() - Pass a bad flag.
   **/
  public void Var012() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();
        Xid xid = new JTATestXid();
        globalIds[12] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-12)");
        int errorCode = 0;
        try {
          xar.end(xid, 444444);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xar.end(xid, XAResource.TMSUCCESS);
        xar.rollback(xid);
        xac.close();

        assertCondition((errorCode == XAException.XAER_INVAL)
            && (countRows(-12) == 0));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
        System.out.println("For Toolbox driver, opened issue 29717 12-22-05");
      }
    }
  }

  /**
   * end() - Pass a flag that is not allowed.
   **/
  public void Var013() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[13] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(234)");
        int errorCode = 0;
        try {
          if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            /* JWE -- HMM.. our code lets TMNOFLAGS through */
            /* Try a different one */
            xar.end(xid, XAResource.TMONEPHASE);
          } else {
            xar.end(xid, XAResource.TMONEPHASE);
          }
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          /** Since we did something bad, this transaction should just fail **/
          /** Cleanup as best we can */
          try {
            xar.end(xid, XAResource.TMFAIL);
          } catch (Exception e) {
            // e.printStackTrace();
          }
        } else {
          xar.end(xid, XAResource.TMSUCCESS);
        }
        xar.rollback(xid);
        xac.close();
        if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
          boolean condition = (errorCode == XAException.XAER_INVAL)
              && (countRows(234) == 0);
          if (!condition) {
            System.out.println("errorCode = " + errorCode
                + " should be  XAER_INVAL" + XAException.XAER_INVAL);
            System.out.println("countRows(234) = " + countRows(234)
                + " should be 0");
          }
          assertCondition(condition);
        } else {

          assertCondition((errorCode == XAException.XAER_INVAL)
              && (countRows(234) == 0));

        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
        System.out.println("For Toolbox driver, opened issue 29717 12-22-05");
      }
    }
  }

  /**
   * end() - Without a corresponding start().
   **/
  public void Var014() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      int expectedErrorCode = XAException.XAER_PROTO;
      // If V5R4 and UDBdatasource then error should be XAER_NOTA
      if ((useUDBDataSource) && (true)) {
        expectedErrorCode = XAException.XAER_NOTA;
      }
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[14] = xid.getGlobalTransactionId();
        int errorCode = 0;
        try {
          xar.end(xid, XAResource.TMSUCCESS);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xac.close();

        assertCondition(errorCode == expectedErrorCode,
            "Expected error code of " + expectedErrorCode + " but got "
                + errorCode + " connection was " + c);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * end() - Pass TMSUCCESS.
   **/
  public void Var015() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[15] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(23407)");
        xar.end(xid, XAResource.TMSUCCESS);
        xar.commit(xid, true);
        xac.close();

        assertCondition(countRows(23407) == 1);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * end() - Pass TMFAIL.
   **/
  public void Var016() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[16] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-23400)");
        int errorCode1 = 0;
        try {
          xar.end(xid, XAResource.TMFAIL);
        } catch (XAException e) {
          // DB2/400 resource manager gives the rollback
          // exception in this senario.
          errorCode1 = e.errorCode;
        }
        xar.rollback(xid);
        xac.close();

        int rowCount = countRows(-23400);
        //  toolbox driver behavoir
        boolean condition = false;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          condition = (rowCount == 0);
        } else {
          condition = (errorCode1 == XAException.XA_RBROLLBACK)
              && (rowCount == 0);
        }
        if (!condition) {
          System.out
              .println("errorCode1 should be XAException.XA_RBROLLBACK but is "
                  + errorCode1);
          System.out.println("countRows(-23400) should be  0 but is "
              + rowCount);
        }
        assertCondition(condition);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * end() - Pass TMSUSPEND.
   **/
  public void Var017() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[17] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-234077)");
        int errorCode = 0;
        try {
          xar.end(xid, XAResource.TMSUSPEND);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xar.end(xid, XAResource.TMSUCCESS);
        xar.rollback(xid);
        xac.close();

        // Normally, this would be a perfectly reasonable thing
        // to do. But due to As/400 architecture problems, we
        // cannot support it. Because of that, we return something
        // other than a normal XA return code.
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && !useUDBDataSource) {
          assertCondition((errorCode == -90001) && (countRows(-234077) == 0));
        } else if (getDriver() == JDTestDriver.DRIVER_NATIVE
            && useUDBDataSource) {
          //
          // This is now supported
          //
          assertCondition((errorCode == 0) && (countRows(-234077) == 0));
        } else if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          // @PDA fixed in toolbox implicit start
          assertCondition((errorCode == 0) && (countRows(-234077) == 0));
        } else {
          int rowCount = countRows(-234077);
          boolean condition = (errorCode == XAException.XAER_INVAL)
              && (rowCount == 0);
          if (!condition) {
            System.out
                .println("errorCode should be XAException.XAER_INVAL, but is "
                    + errorCode);
            System.out.println("countRows(-234077) should be 0 but is "
                + rowCount);
          }
          assertCondition(condition);
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * forget() - Pass a null xid.
   **/
  public void Var018() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[18] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(68432)");
        int errorCode = 0;
        try {
          xar.forget(null);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xar.end(xid, XAResource.TMSUCCESS);
        xar.rollback(xid);
        xac.close();
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && !useUDBDataSource) {
          /* JWE -- Changed to XAER_NOTA -- XID is not valid */
          boolean condition = (errorCode == XAException.XAER_NOTA)
              && (countRows(68432) == 0);
          if (!condition) {
            System.out.println("errorCode -<" + errorCode
                + ">- should be XAER_NOTA" + XAException.XAER_NOTA);
            System.out.println("countRows(68432) -< " + countRows(68432)
                + " >- should be 0 ");
          }
          assertCondition(condition);

        } else {

          boolean condition = (errorCode == XAException.XAER_INVAL)
              && (countRows(68432) == 0);
          if (!condition) {
            System.out.println("errorCode -<" + errorCode
                + ">- should be XAER_INVAL" + XAException.XAER_INVAL);
            System.out.println("countRows(68432) -< " + countRows(68432)
                + " >- should be 0 ");
          }
          assertCondition(condition);
        }

      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * forget() - Pass a bad xid.
   **/
  public void Var019() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[19] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(68433)");
        int errorCode = 0;
        try {
          xar.forget(new JTATestXid());
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xar.end(xid, XAResource.TMSUCCESS);
        xar.rollback(xid);
        xac.close();

        assertCondition((errorCode == XAException.XAER_NOTA)
            && (countRows(68433) == 0));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * forget() - Call without first calling start.
   **/
  public void Var020() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[20] = xid.getGlobalTransactionId();
        int errorCode = 0;
        try {
          // Calling forget without start is an ok thing to do,
          // but the transaction is not active nor in doubt so
          // forget will still fail.
          xar.forget(xid);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xac.close();

        assertCondition(errorCode == XAException.XAER_NOTA, "Expected "
            + XAException.XAER_NOTA + " but got " + errorCode
            + " for Connection " + c);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * forget() - Call before end().
   **/
  public void Var021() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[21] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-88432)");
        int errorCode = 0;
        try {
          xar.forget(xid);
        } catch (XAException e) {
          // Illegal to forget a transaction in flight
          errorCode = e.errorCode;
        }
        xar.end(xid, XAResource.TMSUCCESS);
        xar.commit(xid, true);
        xac.close();

        assertCondition((errorCode == XAException.XAER_PROTO)
            && (countRows(-88432) == 1));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * forget() - Call after end() for a transaction that has not been prepared.
   **/
  public void Var022() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[21] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(188432)");
        xar.end(xid, XAResource.TMSUCCESS);
        int errorCode = 0;
        try {
          xar.forget(xid);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xar.commit(xid, true);
        xac.close();

        assertCondition((errorCode == XAException.XAER_PROTO)
            && (countRows(188432) == 1));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * forget() - Call after end() for a prepared transaction.
   **/
  public void Var023() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[23] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-884328)");
        xar.end(xid, XAResource.TMSUCCESS);
        xar.prepare(xid);
        int errorCode = 0;
        try {
          xar.forget(xid);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xar.commit(xid, false);
        xac.close();

        assertCondition((errorCode == XAException.XAER_PROTO)
            && (countRows(-884328) == 1));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * getTransactionTimeout() - Always returns 0.
   **/
  public void Var024() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();

        int transactionTimeout = xar.getTransactionTimeout();

        assertCondition(transactionTimeout == 0);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * isSameRM() - Pass null.
   **/
  public void Var025() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();

        boolean isSame = xar.isSameRM(null);

        assertCondition(isSame == false);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * isSameRM() - Pass a completely different resource.
   **/
  public void Var026() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();

        XADataSource xads2 = newXADataSource();
        XAConnection xac2 = xads2.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar2 = xac2.getXAResource();

        boolean isSame = xar.isSameRM(xar2);
        // Both resources come from the same database
        assertCondition(isSame == true);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * isSameRM() - Pass a resource from the same data source.
   **/
  public void Var027() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();

        XAConnection xac2 = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar2 = xac2.getXAResource();

        boolean isSame = xar.isSameRM(xar2);
        // Both resources come from the same database
        assertCondition(isSame == true);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * isSameRM() - Pass a resource from the same connection.
   **/
  public void Var028() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();

        XAResource xar2 = xac.getXAResource();

        boolean isSame = xar.isSameRM(xar2);

        // XAResource is on a 1 to 1 relationship
        // with XAConnection, its not only the same RM,
        // but its ALSO the same physical XAResource object
        assertCondition(xar == xar2 && isSame == true);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * isSameRM() - Pass the same resource.
   **/
  public void Var029() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();

        boolean isSame = xar.isSameRM(xar);

        assertCondition(isSame == true);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepare() - Pass a null xid.
   **/
  public void Var030() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[30] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(23513)");
        xar.end(xid, XAResource.TMSUCCESS);
        int errorCode = 0;
        try {
          xar.prepare(null);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xar.rollback(xid);
        xac.close();
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && !useUDBDataSource) {
          /* XAER_NOTA -- xid is not valid */
          assertCondition((errorCode == XAException.XAER_NOTA)
              && (countRows(23513) == 0));
        } else {

          assertCondition((errorCode == XAException.XAER_INVAL)
              && (countRows(23513) == 0));

        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepare() - Pass a bad xid.
   **/
  public void Var031() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[31] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(23514)");
        xar.end(xid, XAResource.TMSUCCESS);
        int errorCode = 0;
        try {
          xar.prepare(new JTATestXid());
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xar.rollback(xid);
        xac.close();

        assertCondition((errorCode == XAException.XAER_NOTA)
            && (countRows(23514) == 0));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepare() - Call without a start().
   **/
  public void Var032() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[32] = xid.getGlobalTransactionId();
        int errorCode = 0;
        try {
          // Doing prepare without start is perfectly valid,
          // its just that we don't have a transaction to prepare here.
          xar.prepare(xid);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xac.close();

        assertCondition(errorCode == XAException.XAER_NOTA, " expected "
            + XAException.XAER_NOTA + " but got " + errorCode
            + " for connection " + c);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepare() - Call before end().
   **/
  public void Var033() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[33] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(23512)");
        int errorCode = 0;
        try {
          xar.prepare(xid);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xar.end(xid, XAResource.TMSUCCESS);
        xar.rollback(xid);
        xac.close();

        assertCondition((errorCode == XAException.XAER_PROTO)
            && (countRows(23512) == 0));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepare() - Call after end(), before commit() or rollback().
   **/
  public void Var034() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[34] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(23612)");
        xar.end(xid, XAResource.TMSUCCESS);
        int rc = xar.prepare(xid);
        xar.commit(xid, false);
        xac.close();

        assertCondition((rc == XAResource.XA_OK) && (countRows(23612) == 1));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepare() - Call after end(), after commit().
   **/
  public void Var035() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[35] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(23712)");
        xar.end(xid, XAResource.TMSUCCESS);
        xar.commit(xid, true);
        int errorCode = 0;
        try {
          xar.prepare(xid);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xac.close();

        assertCondition((errorCode == XAException.XAER_NOTA)
            && (countRows(23712) == 1));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepare() - Call after end(), after rollback().
   **/
  public void Var036() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[36] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(23812)");
        xar.end(xid, XAResource.TMSUCCESS);
        xar.rollback(xid);
        int errorCode = 0;
        try {
          xar.prepare(xid);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xac.close();
        int countRows23812 = countRows(23812);
        assertCondition(
            (errorCode == XAException.XAER_NOTA || errorCode == XAException.XAER_PROTO)
                && (countRows23812 == 0), "\nerrorCode = " + errorCode
                + " sb XAException.XAER_NOTA=" + XAException.XAER_NOTA
                + " or XAException.XAER_PROTO=" + XAException.XAER_PROTO
                + " countRows(23812)=" + countRows23812 + " sb 0\n");
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * prepare() - Call after only read operations.
   **/
  public void Var037() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[37] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeQuery("SELECT * FROM " + table_);
        s.close();
        xar.end(xid, XAResource.TMSUCCESS);
        int rc = xar.prepare(xid);
        xac.close();

        assertCondition(rc == XAResource.XA_RDONLY);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * rollback() - Pass a null xid.
   **/
  public void Var038() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[38] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(476)");
        int errorCode1 = 0;
        try {
          xar.end(xid, XAResource.TMFAIL);
        } catch (XAException e) {
          // DB2/400 resource manager gives the rollback
          // exception in this senario.
          errorCode1 = e.errorCode;
        }
        int errorCode2 = 0;
        try {
          xar.rollback(null);
        } catch (XAException e) {
          errorCode2 = e.errorCode;
        }
        xar.rollback(xid);
        xac.close();
        /* XAER_NOTA -- xid is not valid */
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && !useUDBDataSource) {
          int rowCount = countRows(476);
          boolean condition = (errorCode1 == XAException.XA_RBROLLBACK)
              && (errorCode2 == XAException.XAER_NOTA) && (rowCount == 0);
          if (!condition) {
            System.out
                .println("errorCode1 should be XAException.XA_RBROLLBACK, but is "
                    + errorCode1);
            System.out
                .println("errorCode2 should be XAException.XAER_NOTA, but is "
                    + errorCode2);
            System.out
                .println("countRows(476) should be 0, but is " + rowCount);
          }
          assertCondition(condition);
        } else if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          // 
          assertCondition((errorCode2 == XAException.XAER_INVAL)
              && (countRows(476) == 0));
        } else {
          assertCondition((errorCode1 == XAException.XA_RBROLLBACK)
              && (errorCode2 == XAException.XAER_INVAL)
              && (countRows(476) == 0));
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * rollback() - Pass an invalid xid.
   **/
  public void Var039() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[39] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(576)");
        int errorCode1 = 0;
        try {
          xar.end(xid, XAResource.TMFAIL);
        } catch (XAException e) {
          // DB2/400 resource manager gives the rollback
          // exception in this senario.
          errorCode1 = e.errorCode;
        }
        int errorCode2 = 0;
        try {
          xar.rollback(new JTATestXid());
        } catch (XAException e) {
          errorCode2 = e.errorCode;
        }
        xar.rollback(xid);
        xac.close();

        int rowCount = countRows(576);
        boolean condition;
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && useUDBDataSource) {
          // For NTS, we do not fail with an error message.
          condition = (errorCode1 == XAException.XA_RBROLLBACK)
              && (errorCode2 == 0) && (rowCount) == 0;
          if (!condition) {
            System.out
                .println("errorCode1 should be XAException.XA_RBROLLBACK, but is "
                    + errorCode1);
            System.out.println("errorCode2 should be 0, but is " + errorCode2);
            System.out
                .println("countRows(576) should be 0, but is " + rowCount);
          }

        } else if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          // 
          condition = (errorCode1 == XAException.XA_RBROLLBACK)
              && (errorCode2 == 0) && (rowCount) == 0;
          if (!condition) {
            System.out
                .println("errorCode2 should be XAException.XAER_RBROLLBACK, but is "
                    + errorCode2);
            System.out
                .println("countRows(576) should be 0, but is " + rowCount);

          }
        } else {
          condition = (errorCode1 == XAException.XA_RBROLLBACK)
              && (errorCode2 == XAException.XAER_NOTA) && (rowCount) == 0;
          if (!condition) {
            System.out
                .println("errorCode1 should be XAException.XA_RBROLLBACK, but is "
                    + errorCode1);
            System.out
                .println("errorCode2 should be XAException.XAER_NOTA, but is "
                    + errorCode2);
            System.out
                .println("countRows(576) should be 0, but is " + rowCount);
          }
        }
        assertCondition(condition);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * rollback() - Pass an valid xid.
   **/
  public void Var040() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[40] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-165476)");
        int errorCode1 = 0;
        try {
          xar.end(xid, XAResource.TMFAIL);
        } catch (XAException e) {
          // DB2/400 resource manager gives the rollback
          // exception in this senario.
          errorCode1 = e.errorCode;
        }
        xar.rollback(xid);
        xac.close();

        int rowCount = countRows(-165476);
        // 
        boolean condition = false;
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          condition = (rowCount == 0);
          if (!condition) {
            System.out.println("countRows(-165476) should be 0, but is "
                + rowCount);
          }
        } else {
          condition = (errorCode1 == XAException.XA_RBROLLBACK)
              && (rowCount == 0);
          if (!condition) {
            System.out
                .println("errorCode1 should be XAException.XA_RBROLLBACK, but is "
                    + errorCode1);
            System.out.println("countRows(-165476) should be 0, but is "
                + rowCount);
          }
        }

        assertCondition(condition);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * rollback() - Before end has been called.
   **/
  public void Var041() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[41] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(43276)");
        int errorCode = 0;
        try {
          xar.rollback(xid);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        xar.end(xid, XAResource.TMSUCCESS);
        xar.rollback(xid);
        xac.close();

        assertCondition((errorCode == XAException.XAER_PROTO)
            && (countRows(43276) == 0));
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * rollback() - Before start has been called.
   **/
  public void Var042() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[42] = xid.getGlobalTransactionId();
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(323476)");
        int errorCode = 0;
        try {
          xar.rollback(xid);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        c.commit(); // Commit the non-JTA changes
        xac.close();
        int rowCount = countRows(323476);
        boolean condition;
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && useUDBDataSource) {
          condition = (errorCode == 0) && (rowCount == 1);
          if (!condition) {
            System.out.println("errorCode should be  0, but is " + errorCode);
            System.out.println("countRows(323476) should be 1, but is "
                + rowCount);
          }
        } else if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          //  toolbox
          condition = (errorCode == 0) && (rowCount == 1);
          if (!condition) {
            System.out.println("errorCode should be 0 , but is " + errorCode);
            System.out.println("countRows(323476) should be 1, but is "
                + rowCount);

          }
        } else {
          condition = (errorCode == XAException.XAER_NOTA) && (rowCount == 1);
          if (!condition) {
            System.out
                .println("errorCode should be  XAException.XAER_NOTA, but is "
                    + errorCode);
            System.out.println("countRows(323476) should be 1, but is "
                + rowCount);
          }
        }
        assertCondition(condition);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * rollback() - After a transaction has been marked as succeeded.
   **/
  public void Var043() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[43] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(586876)");
        xar.end(xid, XAResource.TMSUCCESS);
        xar.rollback(xid);
        xac.close();

        assertCondition(countRows(586876) == 0);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * setTransactionTimeout() - Always returns false.
   **/
  public void Var044() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();

        boolean success = xar.setTransactionTimeout(12345);
        if (!useUDBDataSource) {
          if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            assertCondition(success == true); 
          } else
            assertCondition(success == false);
        } else {
          assertCondition(success == true);
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * start() - Pass a null xid.
   **/
  public void Var045() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        XAException e2 = null;
        int errorCode = 0;
        try {
          xar.start(null, XAResource.TMNOFLAGS);
        } catch (XAException e) {
          errorCode = e.errorCode;
          e2 = e;
        }

        if (getDriver() == JDTestDriver.DRIVER_NATIVE && !useUDBDataSource) {
          /* XAER_NOTA -- xid is not valid */
          boolean condition = (errorCode == XAException.XAER_NOTA);
          if (!condition) {
            System.out.println("errorCode is " + errorCode
                + " but should be XAER_NOTA = " + XAException.XAER_NOTA);
            if (e2 != null)
              e2.printStackTrace();
          }
          assertCondition(condition);
        } else {
          assertCondition(errorCode == XAException.XAER_INVAL);
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * start() - Pass a bad flag.
   **/
  public void Var046() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();

        Xid xid = new JTATestXid();
        globalIds[46] = xid.getGlobalTransactionId();
        int errorCode = 0;
        try {
          xar.start(xid, 433545);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }

        assertCondition(errorCode == XAException.XAER_INVAL);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * start() - Pass a flag that is not allowed.
   **/
  public void Var047() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();

        Xid xid = new JTATestXid();
        globalIds[47] = xid.getGlobalTransactionId();
        int errorCode = 0;
        try {
          xar.start(xid, XAResource.TMSUCCESS);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }

        assertCondition(errorCode == XAException.XAER_INVAL);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * start() - When already started.
   **/
  public void Var048() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();
        XAException e2 = null;
        Xid xid = new JTATestXid();
        globalIds[48] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        int errorCode = 0;
        int expectedErrorCode = 0;
        try {
          xar.start(xid, XAResource.TMNOFLAGS);
        } catch (XAException e) {
          errorCode = e.errorCode;
          e2 = e;
        }
        xar.end(xid, XAResource.TMSUCCESS);
        xar.rollback(xid);
        xac.close();
        boolean condition;
        if (!useUDBDataSource) {
          condition = (errorCode == XAException.XAER_PROTO);
          expectedErrorCode = XAException.XAER_PROTO;
        } else {
          condition = (errorCode == XAException.XAER_PROTO /*
                                                            * XAException.
                                                            * XAER_INVAL
                                                            */);
          expectedErrorCode = XAException.XAER_PROTO;
        }
        if (!condition) {
          System.out.println("errorCode should be " + expectedErrorCode
              + ", but is " + errorCode + " for connection " + c);
          if (e2 != null)
            e2.printStackTrace();
        }
        assertCondition(condition);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * start() - When already started and ended, using the same xid.
   **/
  public void Var049() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();
        XAException e2 = null;
        Xid xid = new JTATestXid();
        globalIds[49] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-96065)");
        xar.end(xid, XAResource.TMSUCCESS);
        int errorCode = 0;
        try {
          xar.start(xid, XAResource.TMNOFLAGS);
        } catch (XAException e) {
          errorCode = e.errorCode;
          e2 = e;
        }
        xar.commit(xid, true);
        xac.close();
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && !useUDBDataSource) {
          //
          // Note -- The AS400 does not support multiple transactions on the
          // same connection
          // (see var 50), so we get the same error as var 50 instead of DUPID.
          //
          boolean condition = (errorCode == XAException.XAER_PROTO)
              && (countRows(-96065) == 1);
          if (!condition) {
            System.out.println("errorCode = " + errorCode
                + " should be XAException.XAER_PROTO = "
                + XAException.XAER_PROTO);
            System.out.println("countRows(-96065) = " + countRows(-96065)
                + " should be 1");
            if (e2 != null) {
              e2.printStackTrace();
            }
          }
          assertCondition(condition);

        } else {

          boolean condition = (errorCode == XAException.XAER_DUPID)
              && (countRows(-96065) == 1);
          if (!condition) {
            System.out.println("errorCode = " + errorCode
                + " should be XAException.XAER_DUPID = "
                + XAException.XAER_DUPID);
            System.out.println("countRows(-96065) = " + countRows(-96065)
                + " should be 1");
            if (e2 != null) {
              e2.printStackTrace();
            }
          }
          assertCondition(condition);
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * start() - When already started and ended, using a different xid.
   **/
  public void Var050() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();
        Connection c = xac.getConnection();

        Xid xid = new JTATestXid();
        globalIds[50] = xid.getGlobalTransactionId();
        xar.start(xid, XAResource.TMNOFLAGS);
        Statement s = c.createStatement();
        s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-96965)");
        xar.end(xid, XAResource.TMSUCCESS);
        Xid xid2 = new JTATestXid();
        globalIds[25] = xid2.getGlobalTransactionId();

        int errorCode1 = 0;
        try {
          xar.start(xid2, XAResource.TMNOFLAGS);
        } catch (XAException e) {
          System.out.println("xar.start(xid2=" + xid2 + ") failed with " + e);
          errorCode1 = e.errorCode;
          if (is != null) {
            System.out.println("Interactive:  press enter to continue");
            is.readLine();

          }
        }
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          // JTA now should support this.

          s = c.createStatement();
          s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-96965)");
          xar.end(xid2, XAResource.TMSUCCESS);
          xar.commit(xid, true);
          xar.commit(xid2, true);
          xac.close();
          int rowCount = countRows(-96965);
          boolean condition = (errorCode1 == 0) && (rowCount == 2);
          if (!condition) {
            System.out.println("errorCode1 should be 0, but is " + errorCode1);
            System.out.println("rowCount should be 2, but is " + rowCount);
          }
          assertCondition(condition);
        } else if (!useUDBDataSource) {
          // This would have been an invalid test.
          // DB2/400 does not yet support multiplexing transactions
          // over a single connection.
          // s = c.createStatement();
          // s.executeUpdate("INSERT INTO " + table_ +
          // " (COL1) VALUES(-96965)");
          // xar.end(xid, XAResource.TMSUCCESS);
          xar.commit(xid, true);
          xac.close();

          assertCondition((errorCode1 == XAException.XAER_PROTO)
              && (countRows(-96965) == 1));
        } else {
          // JTA now should support this.

          s = c.createStatement();
          s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-96965)");
          xar.end(xid2, XAResource.TMSUCCESS);
          xar.commit(xid, true);
          xar.commit(xid2, true);
          xac.close();
          int rowCount = countRows(-96965);
          boolean condition = (errorCode1 == 0) && (rowCount == 2);
          if (!condition) {
            System.out.println("errorCode1 should be 0, but is " + errorCode1);
            System.out.println("rowCount should be 2, but is " + rowCount);
          }
          assertCondition(condition);

        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * start() - Pass TMRESUME.
   **/
  public void Var051() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();

        Xid xid = new JTATestXid();
        globalIds[51] = xid.getGlobalTransactionId();
        int errorCode = 0;
        try {
          // Even if RMRESUME is supported, we would need to
          // create/access the transaction first.
          xar.start(xid, XAResource.TMRESUME);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }
        // Normally, this would be a perfectly reasonable thing
        // to do. But due to As/400 architecture problems, we
        // cannot support it. Because of that, we return something
        // other than a normal XA return code.
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && !useUDBDataSource) {
          boolean condition = (errorCode == -90001);
          if (!condition) {
            System.out.println("Error code should be -90001, but is "
                + errorCode);
          }
          assertCondition(condition);
        } else {
          // in V5R4 we return XAER_NOTA
          assertCondition(errorCode == XAException.XAER_NOTA,
              "Error code should be XAER_NOTA=" + XAException.XAER_NOTA
                  + " but is " + errorCode);
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  /**
   * start() - Pass TMJOIN.
   **/
  public void Var052() {
    if (isIasp && (!isNTS)) {
      notApplicable("Not applicable for IASP and not NTS");
      return;
    }
    if (checkJdbc20StdExt()) {
      try {
        XADataSource xads = newXADataSource();
        XAConnection xac = xads.getXAConnection(userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
        XAResource xar = xac.getXAResource();

        Xid xid = new JTATestXid();
        globalIds[52] = xid.getGlobalTransactionId();
        int errorCode = 0;
        try {
          xar.start(xid, XAResource.TMJOIN);
        } catch (XAException e) {
          errorCode = e.errorCode;
        }

        // Normally, this would be a perfectly reasonable thing
        // to do. But due to As/400 architecture problems, we
        // cannot support it. Because of that, we return something
        // other than a normal XA return code.
        if (getDriver() == JDTestDriver.DRIVER_NATIVE && !useUDBDataSource) {
          boolean condition = (errorCode == -90001);
          if (!condition) {
            System.out.println("Error code should be -90001, but is "
                + errorCode);
          }
          assertCondition(condition);

        } else {
          // in V5R4 we return XAER_NOTA
          assertCondition(errorCode == XAException.XAER_NOTA,
              "Error code should be XAER_NOTA=" + XAException.XAER_NOTA
                  + " but is " + errorCode);
        }
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }

  // This testcase should work if we fully supported the JTA
  // specification. Obviously, we don't.
  //
  // /**
  // * start() start second transaction on a connection
  // */
  // public void Var053()
  // {
  // if (checkJdbc20StdExt()) {
  // try {
  // XADataSource xads = newXADataSource();
  // XAConnection xac = xads.getXAConnection(userId_,password_);
  // XAResource xar = xac.getXAResource();
  // Connection c = xac.getConnection();
  //
  // Xid xid = new TestXid();
  // Xid xid2 = new TestXid();
  //
  // Statement s = c.createStatement();
  //
  // xar.start(xid, XAResource.TMNOFLAGS);
  // s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-96965)");
  // xar.end(xid, XAResource.TMSUCCESS);
  //
  // xar.start(xid2, XAResource.TMNOFLAGS);
  // s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-96965)");
  // xar.end(xid2, XAResource.TMSUCCESS);
  //
  // xar.commit(xid2, true);
  // xar.rollback(xid);
  // xac.close();
  // // One transaction rolled back, the other committed.
  // assertCondition(countRows(-96965) == 1);
  // }
  // catch (Exception e) {
  // failed (e, "Unexpected Exception");
  // }
  // }
  // }

}
