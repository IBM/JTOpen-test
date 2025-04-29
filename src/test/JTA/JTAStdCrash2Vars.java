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
// File Name:    JTAStdCrash2Vars.java
//
// Description:  Same as JTACrash2Vars.java but test standard interfaces for
//               JTA & JDBC Std Ext
//
// Classes:      JTAStdCrash2Vars
//
////////////////////////////////////////////////////////////////////////
package test.JTA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;

import com.ibm.as400.access.AS400JDBCXADataSource;

import test.JDReflectionUtil;
import test.JTATest;

// This is not a testcase. This file is called from JTACrash2 to run that testcase
// Do not add this as a testcase to JTATest.java
// Var1 here is called from Var001 in JTACrash2 etc..
// This program will be called from JTACrash2.java, and will
// be toasted from that program to cause abend conditions.

public class JTAStdCrash2Vars {
  private static Connection conn_;
  private static Connection noneConnection;
  private static String driverName = null; // From the socket
  private static String system_ = null; // Note that the DB name comes from the
                                        // socket connection
  private static String userId_ = null; // From the socket
  private static String password_ = null;// From the socket
  private static Socket crash2Socket = null;

  private static PrintWriter out = null;
  private static BufferedReader inputReader = null;
  private static String xidStr;
  private static JTATestXid oldXid;
  private static String Collection = null;
  // String table = JTATest.COLLECTION + ".CRASH2" + "XXX";

  protected static boolean useUDBDataSource = false;

  static String cshMsg = "Ready to Crash"; // this should be the same as in
                                           // JTACrash2.java

  protected static boolean checkIns(String table, String value)
      throws Exception {
    Statement s = conn_.createStatement();
    ResultSet rs = s.executeQuery("SELECT * FROM " + table);
    boolean result = false;
    while (rs.next()) {
      String retVal = rs.getString(1);
      if (retVal.equals(value))
        result = true;
    }
    s.close();
    return result;
  }

  protected static boolean checkInsNoneConnection(String table, String value)
      throws Exception {
    Statement s = noneConnection.createStatement();
    ResultSet rs = s.executeQuery("SELECT * FROM " + table);
    boolean result = false;
    while (rs.next()) {
      String retVal = rs.getString(1);
      if (retVal.equals(value))
        result = true;
    }
    s.close();
    return result;
  }

  private static XADataSource newXADataSource() throws Exception {
    XADataSource xads;
    if (driverName.equalsIgnoreCase("toolbox")) {
      xads = new AS400JDBCXADataSource(system_, userId_, password_);
    } else {
      if (useUDBDataSource) {
        xads     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.UDBXADataSource");
        JDReflectionUtil.callMethod_V(xads,"setDatabaseName",system_); 
        try {
            JDReflectionUtil.callMethod_V(xads,"setUser",userId_); 
            JDReflectionUtil.callMethod_V(xads,"setPassword",password_); 

        } catch (Exception e) {
          e.printStackTrace(out);
        }

      } else {
        xads     = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdXADataSource");

        JDReflectionUtil.callMethod_V(xads,"setDatabaseName",system_); 
        try {
            JDReflectionUtil.callMethod_V(xads,"setUser",userId_); 
            JDReflectionUtil.callMethod_V(xads,"setPassword",password_); 
        } catch (Exception e) {
          e.printStackTrace(out);
        }
      }
    }
    return xads;
  }

  private static void Var1() {
    try {
      out.println("TEST: Start transaction, do work, 'crash'");
      String table = Collection + ".CRASH2" + "001";
      out.println(table);
      String insStr = "VAL1";
      try {
        JTATest.crtTmpTbl(table, conn_);
        conn_.commit();
      } catch (Exception e) {
        String msg = e.toString();
        out.println("Caught exception: " + msg);
        if (msg.indexOf("*FILE already exists.") != -1) {
          out.println("Continuing");
        } else {
          out.println("Exception: " + e.getMessage());
          out.println("leaving var");
          return;
        }
      }

      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      Connection conn = xaConn.getConnection();

      String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
      out.println(sqlString);
      PreparedStatement stmt = conn.prepareStatement(sqlString);

      // Generate a new transaction
      JTATestXid newXid = new JTATestXid();
      newXid.setColl(Collection);
      xaRes.start(newXid, XAResource.TMNOFLAGS);

      int rc;
      rc = stmt.executeUpdate();
      if (rc != 1) {
        out.println("Expected 1 row inserted, got " + rc);
      }

      // Notify the server that we have done up to the point of crash
      out.println(cshMsg);
      long startTime = System.currentTimeMillis();
      // Wait to be toasted
      while (true) {
        try {
          Thread.sleep(5000);
          out.println("Waiting to be toasted after "
              + (System.currentTimeMillis() - startTime) + "millis");
        } catch (Exception ex) {
          conn.close(); 
        }
      }
    } catch (Exception e) {
      out.println("Exception: " + e.getMessage());
      e.printStackTrace(out);
    }
  }

  private static void Var1Vfy() {
    try {
      out.println("TEST: Check that Var1 was successful");
      String result = "FAILURE";

      if (checkIns(Collection + ".CRASH2" + "001", "VAL1"))
        result = "FAILURE";
      else
        result = "SUCCESS";

      out.println("TEST: Var1 table check was " + result);
      out.println(result);
    } catch (Exception e) {
      out.println("Exception: " + e.getMessage());
      e.printStackTrace(out);
      out.println("FAILURE");
    }
  }

  private static void Var2() {
    try {
      out.println("TEST: Start transaction, do work, end, 'crash'");
      String table = Collection + ".CRASH2" + "002";
      try {
        JTATest.crtTmpTbl(table, conn_);
        conn_.commit();
      } catch (Exception e) {
        String msg = e.toString();
        out.println("Caught exception: " + msg);
        if (msg.indexOf("*FILE already exists.") != -1) {
          out.println("Continuing");
        } else {
          out.println("Exception: " + e.getMessage());
          out.println("leaving var");
          return;
        }
      }

      String insStr = "VAL2";
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      Connection conn = xaConn.getConnection();

      String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
      out.println(sqlString);
      PreparedStatement stmt = conn.prepareStatement(sqlString);

      // Generate a new transaction
      JTATestXid newXid = new JTATestXid();
      newXid.setColl(Collection);
      xaRes.start(newXid, XAResource.TMNOFLAGS);

      int rc;
      rc = stmt.executeUpdate();
      if (rc != 1) {
        out.println("Expected 1 row inserted, got " + rc);
        return;
      }

      xaRes.end(newXid, XAResource.TMSUCCESS);

      // Notify the server that we have done up to the point of crash
      out.println(newXid.toString());
      out.println(cshMsg);

      // Wait to be toasted
      while (true) {
        try {
          Thread.sleep(1);
        } catch (Exception ex) {
          conn.close(); 
        }
      }

    } catch (Exception e) {
      out.println("Exception: " + e.getMessage());
      e.printStackTrace(out);
    }
  }

  private static void Var2Vfy() {
    try {
      //
      // In this case, an end of the XA was done, but
      // has not been committed. The table will be
      // left as locked, but the update will be visible
      // using a *NONE connection
      //
      out.println("TEST: Check that Var2 was successful");
      String result = "FAILURE";

      if (!checkInsNoneConnection(Collection + ".CRASH2" + "002", "VAL2"))
        result = "FAILURE";
      else
        result = "SUCCESS";

      out.println("TEST: Var2 table check was " + result);

      // Now rollback so we can run this testcase next time.
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      xaRes.prepare(oldXid);
      xaRes.rollback(oldXid);
      out.println(result);
    } catch (Exception e) {
      String msg = e.getMessage();
      out.println(msg);
      e.printStackTrace(out);
      out.println("FAILURE");
    }
  }

  private static void Var3() {
    try {
      out.println("TEST: Start transaction, do work, end, prepare, 'crash'");
      String table = Collection + ".CRASH2" + "003";
      try {
        JTATest.crtTmpTbl(table, conn_);
        conn_.commit();
      } catch (Exception e) {
        String msg = e.toString();
        out.println("Caught exception: " + msg);
        if (msg.indexOf("*FILE already exists.") != -1) {
          out.println("Continuing");
        } else {
          out.println("Exception: " + e.getMessage());
          out.println("Unexpected Exception. Leaving Var");
          return;
        }
      }

      String insStr = "VAL3";
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      Connection conn = xaConn.getConnection();

      String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
      out.println(sqlString);
      PreparedStatement stmt = conn.prepareStatement(sqlString);

      // Generate a new transaction
      JTATestXid newXid = new JTATestXid();
      newXid.setColl(Collection);
      xaRes.start(newXid, XAResource.TMNOFLAGS);

      int rc;
      rc = stmt.executeUpdate();
      if (rc != 1) {
        out.println("Expected 1 row inserted, got " + rc);
        return;
      }

      xaRes.end(newXid, XAResource.TMSUCCESS);
      rc = xaRes.prepare(newXid);
      if (rc != XAResource.XA_OK) {
        out.println("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
        return;
      }

      // Notify the server that we have done up to the point of crash
      out.println(newXid.toString());
      out.println(cshMsg);

      // Wait to be toasted
      while (true) {
        try {
          Thread.sleep(1);
        } catch (Exception ex) {
          conn.close(); 
        }
      }

    } catch (Exception e) {
      out.println("Exception" + e.getMessage());
      e.printStackTrace(out);
    }
  }

  private static void Var3Vfy() {
    try {
      // TODO: A rollback of the transaction.
      out.println("TEST: Check that Var3 was successful");
      String result;
      if (!checkInsNoneConnection(Collection + ".CRASH2" + "003", "VAL3"))
        result = "FAILURE";
      else
        result = "SUCCESS";
      out.println("TEST: Var3 table check was " + result);

      // Now rollback so we can run this testcase next time.
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      out.println("will rollback " + oldXid.toString());
      xaRes.rollback(oldXid);
      out.println(result);
    } catch (Exception e) {
      String msg = e.getMessage();
      out.println(msg);
      e.printStackTrace(out);
      out.println("FAILURE");
    }
  }

  private static void Var4() {
    try {
      out.println("TEST: Start transaction, do work, prepare, commit, 'crash'");
      String table = Collection + ".CRASH2" + "004";
      try {
        JTATest.crtTmpTbl(table, conn_);
        conn_.commit();
      } catch (Exception e) {
        String msg = e.toString();
        out.println("Caught exception: " + msg);
        if (msg.indexOf("*FILE already exists.") != -1) {
          out.println("Continuing");
        } else {
          out.println("Unexpected Exception. Leaving Var");
          return;
        }
      }

      String insStr = "VAL4";
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      Connection conn = xaConn.getConnection();

      out.println("Connection class is " + conn.getClass().getName());
      out.println("Connection       is " + conn);
      String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
      out.println(sqlString);
      PreparedStatement stmt = conn.prepareStatement(sqlString);

      // Generate a new transaction
      JTATestXid newXid = new JTATestXid();
      newXid.setColl(Collection);
      xaRes.start(newXid, XAResource.TMNOFLAGS);

      int rc;
      System.gc();
      rc = stmt.executeUpdate();
      if (rc != 1) {
        out.println("Expected 1 row inserted, got " + rc);
        return;
      }

      xaRes.end(newXid, XAResource.TMSUCCESS);
      rc = xaRes.prepare(newXid);
      if (rc != XAResource.XA_OK) {
        out.println("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
        return;
      }

      xaRes.commit(newXid, false);
      // Notify the server that we have done up to the point of crash
      out.println(newXid.toString());
      out.println(cshMsg);

      // Wait to be toasted
      boolean waiting = true;
      while (waiting) {
        try {
          Thread.sleep(1);
        } catch (Exception ex) {
          waiting = false;
        }
      }
      conn.close();
    } catch (Exception e) {
      out.println("Unexpected Exception");
      e.printStackTrace(out);
    }
  }

  private static void Var4Vfy() {
    try {
      out.println("TEST: Check that Var4 was successful");
      if (!checkIns(Collection + ".CRASH2" + "004", "VAL4"))
        out.println("FAILURE");
      else
        out.println("SUCCESS");
    } catch (Exception e) {
      out.println("Exception: " + e.getMessage());
      e.printStackTrace(out);
    }
  }

  private static void Var5() {
    try {
      out.println("TEST: Start transaction, do work, prepare, rollback, 'crash'");
      String table = Collection + ".CRASH2" + "005";
      try {
        JTATest.crtTmpTbl(table, conn_);
        conn_.commit();
      } catch (Exception e) {
        String msg = e.toString();
        out.println("Caught exception: " + msg);
        if (msg.indexOf("*FILE already exists.") != -1) {
          out.println("Continuing");
        } else {
          out.println("Unexpected Exception. Leaving Var");
          return;
        }
      }

      String insStr = "VAL5";
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      Connection conn = xaConn.getConnection();

      String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
      out.println(sqlString);
      PreparedStatement stmt = conn.prepareStatement(sqlString);

      // Generate a new transaction
      JTATestXid newXid = new JTATestXid();
      newXid.setColl(Collection);
      xaRes.start(newXid, XAResource.TMNOFLAGS);

      int rc;
      rc = stmt.executeUpdate();
      if (rc != 1) {
        out.println("Expected 1 row inserted, got " + rc);
        return;
      }

      xaRes.end(newXid, XAResource.TMSUCCESS);
      rc = xaRes.prepare(newXid);
      if (rc != XAResource.XA_OK) {
        out.println("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
        return;
      }

      xaRes.rollback(newXid);
      // Notify the server that we have done up to the point of crash
      out.println(newXid.toString());
      out.println(cshMsg);

      // Wait to be toasted
      while (true) {
        try {
          Thread.sleep(1);
        } catch (Exception ex) {
          conn.close(); 
        }
      }

    } catch (Exception e) {
      out.println("Unexpected Exception");
      e.printStackTrace(out);
    }
  }

  private static void Var5Vfy() {
    try {
      out.println("TEST: Check that Var5 was successful");
      if (checkIns(Collection + ".CRASH2" + "005", "VAL5"))
        out.println("FAILURE");
      else
        out.println("SUCCESS");
    } catch (Exception e) {
      out.println("Exception: " + e.getMessage());
      e.printStackTrace(out);
    }
  }

  private static void Var6() {
    try {
      out.println("TEST: Start transaction, do work, 'crash', (data should rollback) end fails");
      String table = Collection + ".CRASH2" + "006";
      try {
        JTATest.crtTmpTbl(table, conn_);
        conn_.commit();
      } catch (Exception e) {
        String msg = e.toString();
        out.println("Caught exception: " + msg);
        if (msg.indexOf("*FILE already exists.") != -1) {
          out.println("Continuing");
        } else {
          out.println("Unexpected Exception");
          return;
        }
      }

      String insStr = JTATest.getStr();
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      Connection conn = xaConn.getConnection();

      String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
      out.println("prepare \"" + sqlString + "\"");
      PreparedStatement stmt = conn.prepareStatement(sqlString);

      // Generate a new transaction
      JTATestXid newXid = new JTATestXid();
      newXid.setColl(Collection);
      xaRes.start(newXid, XAResource.TMNOFLAGS);

      int rc;
      rc = stmt.executeUpdate();
      if (rc != 1) {
        out.println("Expected 1 row inserted, got " + rc);
        return;
      }

      // Notify the server that we have done up to the point of crash
      out.println(newXid.toString());
      out.println(cshMsg);

      // Wait to be toasted
      while (true) {
        try {
          Thread.sleep(1);
        } catch (Exception ex) {
          conn.close(); 
        }
      }

    } catch (Exception e) {
      out.println("Unexpected Exception");
      e.printStackTrace(out);
    }
  }

  private static void Var6Vfy() {
    try {
      out.println("TEST: end should fail");
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      out.println(oldXid.toString());
      try {
        xaRes.end(oldXid, XAResource.TMSUCCESS);
        out.println("FAILURE");
      } catch (Exception ex) {
        if (!(ex instanceof XAException)) {
          out.println("Unexpected exception" + ex.getMessage());
          out.println("FAILURE");
          return;
        }
        XAException xaEx = (XAException) ex;
        if ((xaEx.errorCode != XAException.XAER_PROTO)
            && (xaEx.errorCode != XAException.XAER_NOTA)) {
          out.println("Expected " + XAException.XAER_PROTO + " or "
              + XAException.XAER_PROTO + " failure. Got " + xaEx.errorCode);
          out.println("FAILURE");
          return;
        }
        out.println("SUCCESS");
      }

    } catch (Exception e) {
      out.println("Exception: " + e.getMessage());
      e.printStackTrace(out);
    }
  }

  private static void Var7() {
    try {
      out.println("TEST: Start transaction, do work, end, 'crash', prepare, commit");
      String table = Collection + ".CRASH2" + "007";
      try {
        JTATest.crtTmpTbl(table, conn_);
        conn_.commit();
      } catch (Exception e) {
        String msg = e.toString();
        out.println("Caught exception: " + msg);
        if (msg.indexOf("*FILE already exists.") != -1) {
          out.println("Continuing");
        } else {
          out.println("Unexpected Exception. Leaving Var");
          return;
        }
      }

      String insStr = "VAL7";
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      Connection conn = xaConn.getConnection();

      String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
      out.println(sqlString);
      PreparedStatement stmt = conn.prepareStatement(sqlString);

      // Generate a new transaction
      JTATestXid newXid = new JTATestXid();
      newXid.setColl(Collection);
      xaRes.start(newXid, XAResource.TMNOFLAGS);

      int rc;
      rc = stmt.executeUpdate();
      if (rc != 1) {
        out.println("Expected 1 row inserted, got " + rc);
        return;
      }

      xaRes.end(newXid, XAResource.TMSUCCESS);

      // Notify the server that we have done up to the point of crash
      out.println(newXid.toString());
      out.println(cshMsg);

      // Wait to be toasted
      while (true) {
        try {
          Thread.sleep(1);
        } catch (Exception ex) {
          conn.close(); 
        }
      }

    } catch (Exception e) {
      out.println("Unexpected Exception");
      e.printStackTrace(out);
    }
  }

  private static void Var7Vfy() {
    try {
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      out.println(oldXid.toString());
      int rc = xaRes.prepare(oldXid);
      if (rc != XAResource.XA_OK) {
        out.println("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
        return;
      }
      xaRes.commit(oldXid, false);

      out.println("TEST: Check that Var7 was successful");
      if (!checkIns(Collection + ".CRASH2" + "007", "VAL7"))
        out.println("FAILURE");
      else
        out.println("SUCCESS");
    } catch (Exception e) {
      out.println("Exception: " + e.getMessage());
      e.printStackTrace(out);
    }
  }

  private static void Var8() {
    try {
      out.println("TEST: Start transaction, do work, prepare, 'crash', commit");
      String table = Collection + ".CRASH2" + "008";
      try {
        JTATest.crtTmpTbl(table, conn_);
        conn_.commit();
      } catch (Exception e) {
        String msg = e.toString();
        out.println("Caught exception: " + msg);
        if (msg.indexOf("*FILE already exists.") != -1) {
          out.println("Continuing");
        } else {
          out.println("Unexpected Exception. Leaving Var");
          return;
        }
      }

      String insStr = "VAL8";
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      Connection conn = xaConn.getConnection();

      String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
      out.println(sqlString);
      PreparedStatement stmt = conn.prepareStatement(sqlString);

      // Generate a new transaction
      JTATestXid newXid = new JTATestXid();
      newXid.setColl(Collection);
      xaRes.start(newXid, XAResource.TMNOFLAGS);

      int rc;
      rc = stmt.executeUpdate();
      if (rc != 1) {
        out.println("Expected 1 row inserted, got " + rc);
      }

      xaRes.end(newXid, XAResource.TMSUCCESS);
      rc = xaRes.prepare(newXid);
      if (rc != XAResource.XA_OK) {
        out.println("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
      }

      // Notify the server that we have done up to the point of crash
      out.println(newXid.toString());
      out.println(cshMsg);

      // Wait to be toasted
      while (true) {
        try {
          Thread.sleep(1);
        } catch (Exception ex) {
          conn.close(); 
        }
      }

    } catch (Exception e) {
      out.println("Unexpected Exception");
      e.printStackTrace(out);
    }
  }

  private static void Var8Vfy() {
    try {
      out.println("TEST: Check that Var8 was successful");
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      out.println(oldXid.toString());
      xaRes.commit(oldXid, false);

      if (!checkIns(Collection + ".CRASH2" + "008", "VAL8"))
        out.println("FAILURE");
      else
        out.println("SUCCESS");
    } catch (Exception e) {
      out.println("Exception: " + e.getMessage());
      e.printStackTrace(out);
    }
  }

  private static void Var9() {
    try {
      out.println("TEST: Start transaction, do work, 'crash', (data should rollback) end fails");
      String table = Collection + ".CRASH2" + "009";
      try {
        JTATest.crtTmpTbl(table, conn_);
        conn_.commit();
      } catch (Exception e) {
        String msg = e.toString();
        out.println("Caught exception: " + msg);
        if (msg.indexOf("*FILE already exists.") != -1) {
          out.println("Continuing");
        } else {
          out.println("Unexpected Exception. Leaving Var");
          return;
        }
      }

      String insStr = JTATest.getStr();
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      Connection conn = xaConn.getConnection();

      String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
      out.println(sqlString);
      PreparedStatement stmt = conn.prepareStatement(sqlString);

      // Generate a new transaction
      JTATestXid newXid = new JTATestXid();
      newXid.setColl(Collection);
      xaRes.start(newXid, XAResource.TMNOFLAGS);

      int rc;
      rc = stmt.executeUpdate();
      if (rc != 1) {
        out.println("Expected 1 row inserted, got " + rc);
        return;
      }

      // Notify the server that we have done up to the point of crash
      out.println(newXid.toString());
      out.println(cshMsg);

      // Wait to be toasted
      while (true) {
        try {
          Thread.sleep(1);
        } catch (Exception ex) {
          conn.close(); 
        }
      }

    } catch (Exception e) {
      out.println("Unexpected Exception");
      e.printStackTrace(out);
    }
  }

  private static void Var9Vfy() {
    try {
      out.println("TEST: Check that Var9 was successful");
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      out.println(oldXid.toString());

      try {
        xaRes.end(oldXid, XAResource.TMSUCCESS);
        out.println("FAILURE");
      } catch (Exception ex) {
        if (!(ex instanceof XAException)) {
          out.println("Unexpected exception" + ex.getMessage());
          return;
        }
        XAException xaEx = (XAException) ex;
        if ((xaEx.errorCode != XAException.XAER_PROTO)
            && (xaEx.errorCode != XAException.XAER_NOTA)) {
          out.println("Expected " + XAException.XAER_PROTO + " or "
              + XAException.XAER_NOTA + " failure. Got " + xaEx.errorCode);
          return;
        }
        out.println("SUCCESS");
      }
    } catch (Exception e) {
      out.println("Exception: " + e.getMessage());
      e.printStackTrace(out);
    }
  }

  private static void Var10() {
    try {
      out.println("TEST: Start transaction, do work, end, 'crash', prepare, rollback");
      String table = Collection + ".CRASH2" + "010";
      try {
        JTATest.crtTmpTbl(table, conn_);
        conn_.commit();
      } catch (Exception e) {
        String msg = e.toString();
        out.println("Caught exception: " + msg);
        if (msg.indexOf("*FILE already exists.") != -1) {
          out.println("Continuing");
        } else {
          out.println("Unexpected Exception");
          return;
        }
      }

      String insStr = "VAL10";
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      Connection conn = xaConn.getConnection();

      String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
      out.println(sqlString);
      PreparedStatement stmt = conn.prepareStatement(sqlString);

      // Generate a new transaction
      JTATestXid newXid = new JTATestXid();
      newXid.setColl(Collection);
      xaRes.start(newXid, XAResource.TMNOFLAGS);

      int rc;
      rc = stmt.executeUpdate();
      if (rc != 1) {
        out.println("Expected 1 row inserted, got " + rc);
        return;
      }

      xaRes.end(newXid, XAResource.TMSUCCESS);

      // Notify the server that we have done up to the point of crash
      out.println(newXid.toString());
      out.println(cshMsg);

      // Wait to be toasted
      while (true) {
        try {
          Thread.sleep(1);
        } catch (Exception ex) {
          conn.close(); 
        }
      }

    } catch (Exception e) {
      out.println("Unexpected Exception");
      e.printStackTrace(out);
    }
  }

  private static void Var10Vfy() {
    try {
      out.println("TEST: Check that Var10 was successful");
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      out.println(oldXid.toString());

      int rc = xaRes.prepare(oldXid);
      if (rc != XAResource.XA_OK) {
        out.println("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
        return;
      }
      xaRes.rollback(oldXid);

      if (checkIns(Collection + ".CRASH2" + "010", "VAL10"))
        out.println("FAILURE");
      else
        out.println("SUCCESS");
    } catch (Exception e) {
      out.println("Exception: " + e.getMessage());
      e.printStackTrace(out);
    }
  }

  private static void Var11() {
    try {
      out.println("TEST: Start transaction, do work, prepare, 'crash', rollback");
      String table = Collection + ".CRASH2" + "011";
      try {
        JTATest.crtTmpTbl(table, conn_);
        conn_.commit();
      } catch (Exception e) {
        String msg = e.toString();
        out.println("Caught exception: " + msg);
        if (msg.indexOf("*FILE already exists.") != -1) {
          out.println("Continuing");
        } else {
          out.println("Unexpected Exception");
          return;
        }
      }

      String insStr = "VAL11";
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      Connection conn = xaConn.getConnection();

      String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
      out.println(sqlString);
      PreparedStatement stmt = conn.prepareStatement(sqlString);

      // Generate a new transaction
      JTATestXid newXid = new JTATestXid();
      newXid.setColl(Collection);
      xaRes.start(newXid, XAResource.TMNOFLAGS);

      int rc;
      rc = stmt.executeUpdate();
      if (rc != 1) {
        out.println("Expected 1 row inserted, got " + rc);
        return;
      }

      xaRes.end(newXid, XAResource.TMSUCCESS);
      rc = xaRes.prepare(newXid);
      if (rc != XAResource.XA_OK) {
        out.println("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
        return;
      }

      // Notify the server that we have done up to the point of crash
      out.println(newXid.toString());
      out.println(cshMsg);

      // Wait to be toasted
      while (true) {
        try {
          Thread.sleep(1);
        } catch (Exception ex) {
          conn.close(); 
        }
      }

    } catch (Exception e) {
      out.println("Unexpected Exception");
      e.printStackTrace(out);
    }
  }

  private static void Var11Vfy() {
    try {
      out.println("TEST: Check that Var11 was successful");
      XADataSource ds = newXADataSource();
      XAConnection xaConn = ds.getXAConnection();
      XAResource xaRes = xaConn.getXAResource();
      out.println(oldXid.toString());
      xaRes.rollback(oldXid);

      if (checkIns(Collection + ".CRASH2" + "011", "VAL11"))
        out.println("FAILURE");
      else
        out.println("SUCCESS");
    } catch (Exception e) {
      out.println("Exception: " + e.getMessage());
      e.printStackTrace(out);
    }
  }

  public static void main(String[] args) throws Exception {
    String sysName = "localhost";
    int varNum = 0;
    int serverPort = 0;
    System.out.println("Starting JTAStdCrash2Vars: main()");
    try {
      serverPort = Integer.parseInt(System.getProperty("jta.server.socket"));
    } catch (Exception e) {
      System.err.println("Illegal server socket number: "
          + System.getProperty("jta.server.socket"));
      System.err.println("  if null, set -Djta.server.socket");
      System.exit(1);
    }

    if (serverPort == 0) {
      out = new PrintWriter(System.out, true);
      inputReader = new BufferedReader(new InputStreamReader(System.in));

    } else {
      try {
        crash2Socket = new Socket(sysName, serverPort);
        out = new PrintWriter(crash2Socket.getOutputStream(), true);
        inputReader = new BufferedReader(new InputStreamReader(
            crash2Socket.getInputStream()));

      } catch (UnknownHostException e) {
        System.err.println("Unknown host exception: " + sysName);
        e.printStackTrace(System.err);
        System.exit(1);
      } catch (IOException e) {
        System.err
            .println("Couldn't get I/O for the connection to: " + sysName);
        e.printStackTrace(System.err);
        System.exit(1);
      }
    }
    try {
      System.out.println("In JTAStdCrash2Vars: main()");

      out.println("In JTAStdCrash2Vars: main()");
      String fromCrash2;
      boolean running = true;
      while (running && (fromCrash2 = inputReader.readLine()) != null) {
        out.println("received command <" + fromCrash2 + ">");

        JTATest.verboseOut("Crash2: " + fromCrash2);
        if (fromCrash2.startsWith("exit") || fromCrash2.startsWith("quit")) {
          running = false;
        }

        if (fromCrash2.startsWith("DBName:")) {
          system_ = fromCrash2.substring(7);
          out.println("db set to " + system_);
        }
        if (fromCrash2.startsWith("Collection:")) {
          Collection = fromCrash2.substring(11);
          out.println("collection set to " + Collection);
        }
        if (fromCrash2.startsWith("TestXid : L")) {
          xidStr = fromCrash2;
          out.println("xid set to " + xidStr);
        }
        if (fromCrash2.startsWith("User:")) {
          userId_ = fromCrash2.substring(5);
          out.println("user set to " + userId_);
        }
        if (fromCrash2.startsWith("Password:")) {
          password_ = fromCrash2.substring(9);
          out.println("password set to " + password_);
        }
        if (fromCrash2.startsWith("Driver:")) {
          driverName = fromCrash2.substring(7);
          out.println("driver set to " + driverName);
        }
        // Expect the variation gets sent LAST.
        if (fromCrash2.startsWith("Var")) {
          varNum = Integer.parseInt(fromCrash2.substring(3, 6));
          out.println("variation set to " + varNum);
          break;
        }
      }

      if (!(driverName.equalsIgnoreCase("native") || driverName
          .equalsIgnoreCase("toolbox"))) {
        out.println("Bad Driver name: " + driverName);
        System.exit(1);
      }

      if (xidStr != null) {
        try {
          oldXid = new JTATestXid(xidStr);
        } catch (Throwable e) {
          out.println("Caught Exception creating TestXid");
          out.println(e.getMessage());
          e.printStackTrace(out);
          System.exit(1);
        }
      }

      if (driverName.equalsIgnoreCase("native")) {
        Class.forName("com.ibm.db2.jdbc.app.DB2Driver");
        try {
          conn_ = DriverManager.getConnection("jdbc:db2:" + system_
              + ";transaction isolation=serializable", userId_, password_);
        } catch (Exception e) {
          out.println("Native DriverManager.getConnection(jdbc:db2:" + system_
              + ") fails");
          out.println(e.toString());
          e.printStackTrace(out);
          System.exit(1);
        }
        try {
          noneConnection = DriverManager.getConnection("jdbc:db2:" + system_,
              userId_, password_);
        } catch (Exception e) {
          out.println("Native DriverManager.getConnection(jdbc:db2:" + system_
              + ") fails");
          out.println(e.toString());
          e.printStackTrace(out);
          System.exit(1);
        }
      }
      if (driverName.equalsIgnoreCase("toolbox")) {
        Class.forName("com.ibm.as400.access.AS400JDBCDriver");
        try {
          conn_ = DriverManager.getConnection("jdbc:as400:" + system_, userId_,
              password_);
        } catch (Exception e) {
          out.println("Toolbox DriverManager.getConnection(jdbc:as400:"
              + system_ + ","+userId_+","+password_+") fails");
          out.println(e.toString());
          e.printStackTrace(out);
          System.exit(1);
        }
        try {
          noneConnection = DriverManager.getConnection("jdbc:as400:" + system_,
              userId_, password_);
        } catch (Exception e) {
          out.println("Toolbox DriverManager.getConnection(jdbc:as400:"
              + system_ + ","+userId_+","+password_+ ") fails");
          out.println(e.toString());
          e.printStackTrace(out);
          System.exit(1);
        }
      }

      switch (varNum) {
      case 1:
        Var1();
        break;
      case 101:
        Var1Vfy();
        break;
      case 2:
        Var2();
        break;
      case 102:
        Var2Vfy();
        break;
      case 3:
        Var3();
        break;
      case 103:
        Var3Vfy();
        break;
      case 4:
        Var4();
        break;
      case 104:
        Var4Vfy();
        break;
      case 5:
        Var5();
        break;
      case 105:
        Var5Vfy();
        break;
      case 6:
        Var6();
        break;
      case 106:
        Var6Vfy();
        break;
      case 7:
        Var7();
        break;
      case 107:
        Var7Vfy();
        break;
      case 8:
        Var8();
        break;
      case 108:
        Var8Vfy();
        break;
      case 9:
        Var9();
        break;
      case 109:
        Var9Vfy();
        break;
      case 10:
        Var10();
        break;
      case 110:
        Var10Vfy();
        break;
      case 11:
        Var11();
        break;
      case 111:
        Var11Vfy();
        break;
      default:
        break;
      }
    } catch (Exception e) {
      e.printStackTrace();
      e.printStackTrace(out);
    } finally {
      if (conn_ != null)
        try {
          conn_.close();
        } catch (Throwable t) {
        }
    }
  }

}
