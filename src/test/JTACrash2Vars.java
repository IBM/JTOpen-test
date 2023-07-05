package test;

// import java.lang.*;
import java.sql.*;
import java.io.*;
import java.net.*;

import javax.sql.XADataSource; 

// This is not a testcase. This file is called from JTACrash2 to run that testcase
// Do not add this as a testcase to JTATest.java
// Var1 here is called from Var001 in JTACrash2 etc..
// This program will be called from JTACrash2.java, and will
// be toasted from that program to cause abend conditions.

public class JTACrash2Vars {

   private static Connection conn_;
   private static String system_ = null; // Note that the DB name comes from the socket connection
   private static Socket crash2Socket = null;
   private static PrintWriter out = null;
   private static BufferedReader in = null;
   private static String xidStr;
   private static TestXid oldXid;
   private static String Collection = null;
   //String table = JTATest.COLLECTION + ".CRASH2" + "XXX";


   static String cshMsg = "Ready to Crash"; // this should be the same as in JTACrash2.java


   protected static boolean checkIns(String table, String value)
   throws Exception
   {
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

   private static void Var1() {
      try {
         out.println("TEST: Start transaction, do work, 'crash'");
         String table = Collection + ".CRASH2" + "001";
         out.println(table);
         String insStr = "VAL1";
         try {
            JTATest.crtTmpTbl(table, conn_);
         }
         catch (Exception e) {
            String msg = e.toString();
            out.println("Caught exception: " + msg);
            if (msg.indexOf("*FILE already exists.") != -1) {
               out.println("Continuing");
            }
            else {
               out.println("Exception: " + e.getMessage());
               out.println("leaving var");
               return;
            }
         }

         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
         out.println(sqlString);
         PreparedStatement stmt = conn.prepareStatement(sqlString);

         // Generate a new transaction
         TestXid newXid = new TestXid();
         newXid.setColl(Collection);
         JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         int      rc;
         rc = stmt.executeUpdate();
         if (rc != 1) {
            out.println("Expected 1 row inserted, got " + rc);
         }

         // Notify the server that we have done up to the point of crash
         out.println(cshMsg);

         // Wait to be toasted
	 boolean notDone = true;
	 long startTime = System.currentTimeMillis(); 
         while (notDone) {
	    
	    // try {
	    //	 synchronized(c) { 
	    //	      conn_.wait(1000);
            //		 }
	    //  } catch (Exception ex) {
	    //	 notDone = false; 
	    //  }

	     // 10/13/2005
	     // Add some wait here .. Also exit if this doesn't crash within 10 minutes

	     try {
		 synchronized(conn_) {
		     conn_.wait(10);
		 }
	     } catch (Exception e) {
		 out.println("Exception: " + e.getMessage());
		 notDone = false; 
	     } 
	     long currentTime = System.currentTimeMillis();
	     if (currentTime - startTime > 600000) {
		 notDone = false;
		 out.println("Exiting because 10 minutes passed"); 
	     } 

         }

      }
      catch (Exception e) {
         out.println("Exception: " + e.getMessage());
      }
   }


   private static void Var1Vfy() {
      try {
         out.println("TEST: Check that Var1 was successful");
         if (checkIns(Collection + ".CRASH2" + "001", "VAL1"))
            out.println("FAILURE");
         else
            out.println("SUCCESS");
      }
      catch (Exception e) {
         out.println("Exception: " + e.getMessage());
      }
   }


   private static void Var2() {
      try {
         out.println("TEST: Start transaction, do work, end, 'crash'");
         String table = Collection + ".CRASH2" + "002";
         try {
            JTATest.crtTmpTbl(table, conn_);
         }
         catch (Exception e) {
            String msg = e.toString();
            out.println("Caught exception: " + msg);
            if (msg.indexOf("*FILE already exists.") != -1) {
               out.println("Continuing");
            }
            else {
               out.println("Exception: " + e.getMessage());
               out.println("leaving var");
               return;
            }
         }

         String insStr = "VAL2";
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
         out.println(sqlString);
         PreparedStatement stmt = conn.prepareStatement(sqlString);

         // Generate a new transaction
         TestXid newXid = new TestXid();
	 newXid.setColl(Collection);
         JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         int      rc;
         rc = stmt.executeUpdate();
         if (rc != 1) {
            out.println("Expected 1 row inserted, got " + rc);
            return;
         }

         JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);

         // Notify the server that we have done up to the point of crash
         out.println(newXid.toString());
         out.println(cshMsg);

         // Wait to be toasted
	 boolean notDone = true; 
         while (notDone) {
	     try {
		 Thread.sleep(1);
	     } catch (Exception ex) {
		 notDone = false; 
	     } 
         }

      }
      catch (Exception e) {
         out.println("Exception: " + e.getMessage());
      }
   }


   private static void Var2Vfy() {
      try {
         // TODO: A rollback of the transaction.
         out.println("TEST: Check that Var2 was successful");
         String result;
         if (!checkIns(Collection + ".CRASH2" + "002", "VAL2"))
            result = "FAILURE";
         else
            result = "SUCCESS";

         // Now rollback so we can run this testcase next time.
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         JDReflectionUtil.callMethod_I(xaRes,"prepare",oldXid);
         JDReflectionUtil.callMethod_V(xaRes,"rollback",oldXid);
         out.println(result);
      }
      catch (Exception e) {
         String msg = e.getMessage();
         out.println(msg);
         out.println("FAILURE");
      }
   }


   private static void Var3() {
      try {
         out.println("TEST: Start transaction, do work, end, prepare, 'crash'");
         String table = Collection + ".CRASH2" + "003";
         try {
            JTATest.crtTmpTbl(table, conn_);
         }
         catch (Exception e) {
            String msg = e.toString();
            out.println("Caught exception: " + msg);
            if (msg.indexOf("*FILE already exists.") != -1) {
               out.println("Continuing");
            }
            else {
               out.println("Exception: " + e.getMessage());
               out.println("Unexpected Exception. Leaving Var");
               return;
            }
         }

         String insStr = "VAL3";
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
         out.println(sqlString);
         PreparedStatement stmt = conn.prepareStatement(sqlString);

         // Generate a new transaction
         TestXid newXid = new TestXid();
	 newXid.setColl(Collection);
         JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         int      rc;
         rc = stmt.executeUpdate();
         if (rc != 1) {
            out.println("Expected 1 row inserted, got " + rc);
            return;
         }

         JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
         rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
         if (rc != javax.transaction.xa.XAResource.XA_OK) {
            out.println("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            return;
         }

         // Notify the server that we have done up to the point of crash
         out.println(newXid.toString());
         out.println(cshMsg);


         // Wait to be toasted
	 boolean notDone = true; 
         while (notDone) {
	     try {
		 Thread.sleep(1);
	     } catch (Exception ex) {
		 notDone = false; 
	     } 
         }


      }
      catch (Exception e) {
         out.println("Exception" + e.getMessage());
      }
   }


   private static void Var3Vfy() {
      try {
         // TODO: A rollback of the transaction.
         out.println("TEST: Check that Var3 was successful");
         String result;
         if (!checkIns(Collection + ".CRASH2" + "003", "VAL3"))
            result = "FAILURE";
         else
            result = "SUCCESS";
         out.println("TEST: Var3 table check was " + result);

         // Now rollback so we can run this testcase next time.
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         out.println("will rollback " + oldXid.toString());
         JDReflectionUtil.callMethod_V(xaRes,"rollback",oldXid);
         out.println(result);
      }
      catch (Exception e) {
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
         }
         catch (Exception e) {
            String msg = e.toString();
            out.println("Caught exception: " + msg);
            if (msg.indexOf("*FILE already exists.") != -1) {
               out.println("Continuing");
            }
            else {
               out.println("Unexpected Exception. Leaving Var");
               return;
            }
         }

         String insStr = "VAL4";
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
         out.println(sqlString);
         PreparedStatement stmt = conn.prepareStatement(sqlString);

         // Generate a new transaction
         TestXid newXid = new TestXid();
	 newXid.setColl(Collection);
         JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         int      rc;
         rc = stmt.executeUpdate();
         if (rc != 1) {
            out.println("Expected 1 row inserted, got " + rc);
            return;
         }

         JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
         rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
         if (rc != javax.transaction.xa.XAResource.XA_OK) {
            out.println("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            return;
         }

         JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);
         // Notify the server that we have done up to the point of crash
         out.println(newXid.toString());
         out.println(cshMsg);


         // Wait to be toasted
	 boolean notDone = true; 
         while (notDone) {
	     try {
		 Thread.sleep(1);
	     } catch (Exception ex) {
		 notDone = false; 
	     } 
         }


      }
      catch (Exception e) {
         out.println("Unexpected Exception");
      }
   }


   private static void Var4Vfy() {
      try {
         out.println("TEST: Check that Var4 was successful");
         if (!checkIns(Collection + ".CRASH2" + "004", "VAL4"))
            out.println("FAILURE");
         else
            out.println("SUCCESS");
      }
      catch (Exception e) {
         out.println("Exception: " + e.getMessage());
      }
   }

   private static void Var5() {
      try {
         out.println("TEST: Start transaction, do work, prepare, rollback, 'crash'");
         String table = Collection + ".CRASH2" + "005";
         try {
            JTATest.crtTmpTbl(table, conn_);
         }
         catch (Exception e) {
            String msg = e.toString();
            out.println("Caught exception: " + msg);
            if (msg.indexOf("*FILE already exists.") != -1) {
               out.println("Continuing");
            }
            else {
               out.println("Unexpected Exception. Leaving Var");
               return;
            }
         }

         String insStr = "VAL5";
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
         out.println(sqlString);
         PreparedStatement stmt = conn.prepareStatement(sqlString);

         // Generate a new transaction
         TestXid newXid = new TestXid();
	 newXid.setColl(Collection);
         JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         int      rc;
         rc = stmt.executeUpdate();
         if (rc != 1) {
            out.println("Expected 1 row inserted, got " + rc);
            return;
         }

         JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
         rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
         if (rc != javax.transaction.xa.XAResource.XA_OK) {
            out.println("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            return;
         }

         JDReflectionUtil.callMethod_V(xaRes,"rollback",newXid);
         // Notify the server that we have done up to the point of crash
         out.println(newXid.toString());
         out.println(cshMsg);

         // Wait to be toasted
	 boolean notDone = true; 
         while (notDone) {
	     try {
		 Thread.sleep(1);
	     } catch (Exception ex) {
		 notDone = false; 
	     } 
         }


      }
      catch (Exception e) {
         out.println("Unexpected Exception");
      }
   }

   private static void Var5Vfy() {
      try {
         out.println("TEST: Check that Var5 was successful");
         if (checkIns(Collection + ".CRASH2" + "005", "VAL5"))
            out.println("FAILURE");
         else
            out.println("SUCCESS");
      }
      catch (Exception e) {
         out.println("Exception: " + e.getMessage());
      }
   }


   private static void Var6() {
      try {
         out.println("TEST: Start transaction, do work, 'crash', (data should rollback) end fails");
         String table = Collection + ".CRASH2" + "006";
         try {
            JTATest.crtTmpTbl(table, conn_);
         }
         catch (Exception e) {
            String msg = e.toString();
            out.println("Caught exception: " + msg);
            if (msg.indexOf("*FILE already exists.") != -1) {
               out.println("Continuing");
            }
            else {
               out.println("Unexpected Exception");
               return;
            }
         }

         String insStr = JTATest.getStr();
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
         out.println("prepare \"" + sqlString + "\"");
         PreparedStatement stmt = conn.prepareStatement(sqlString);

         // Generate a new transaction
         TestXid newXid = new TestXid();
	 newXid.setColl(Collection);
         JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         int      rc;
         rc = stmt.executeUpdate();
         if (rc != 1) {
            out.println("Expected 1 row inserted, got " + rc);
            return;
         }


         // Notify the server that we have done up to the point of crash
         out.println(newXid.toString());
         out.println(cshMsg);

         // Wait to be toasted
	 boolean notDone = true; 
         while (notDone) {
	     try {
		 Thread.sleep(1);
	     } catch (Exception ex) {
		 notDone = false; 
	     } 
         }

      }
      catch (Exception e) {
         out.println("Unexpected Exception");
      }
   }

   private static void Var6Vfy() {
      try {
         out.println("TEST: end should fail");
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         out.println(oldXid.toString());
         try {
            JDReflectionUtil.callMethod_V(xaRes,"end",oldXid, javax.transaction.xa.XAResource.TMSUCCESS);
            out.println("FAILURE");
         }
         catch (Exception ex) {
            if ((!(JDReflectionUtil.instanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException"))) &&
		(!(ex instanceof javax.transaction.xa.XAException))) {
               out.println("Unexpected exception" + ex.getMessage());
               out.println("FAILURE");
               return;
            }
            if (JDReflectionUtil.getField_I(ex,"errorCode") != javax.transaction.xa.XAException.XAER_PROTO) {
               out.println("Expected " + javax.transaction.xa.XAException.XAER_PROTO + " failure. Got " + JDReflectionUtil.getField_I(ex,"errorCode"));
               out.println("FAILURE");
               return;
            }
            out.println("SUCCESS");
         }

      }
      catch (Exception e) {
         out.println("Exception: " + e.getMessage());
      }
   }


   private static void Var7() {
      try {
         out.println("TEST: Start transaction, do work, end, 'crash', prepare, commit");
         String table = Collection + ".CRASH2" + "007";
         try {
            JTATest.crtTmpTbl(table, conn_);
         }
         catch (Exception e) {
            String msg = e.toString();
            out.println("Caught exception: " + msg);
            if (msg.indexOf("*FILE already exists.") != -1) {
               out.println("Continuing");
            }
            else {
               out.println("Unexpected Exception. Leaving Var");
               return;
            }
         }

         String insStr = "VAL7";
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
         out.println(sqlString);
         PreparedStatement stmt = conn.prepareStatement(sqlString);

         // Generate a new transaction
         TestXid newXid = new TestXid();
	 newXid.setColl(Collection);
         JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         int      rc;
         rc = stmt.executeUpdate();
         if (rc != 1) {
            out.println("Expected 1 row inserted, got " + rc);
            return;
         }

         JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);

         // Notify the server that we have done up to the point of crash
         out.println(newXid.toString());
         out.println(cshMsg);

         // Wait to be toasted
	 boolean notDone = true; 
         while (notDone) {
	     try {
		 Thread.sleep(1);
	     } catch (Exception ex) {
		 notDone = false; 
	     } 
         }

      }
      catch (Exception e) {
         out.println("Unexpected Exception");
      }
   }


   private static void Var7Vfy() {
      try {
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         out.println(oldXid.toString());
         int rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",oldXid);
         if (rc != javax.transaction.xa.XAResource.XA_OK) {
            out.println("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            return;
         }
         JDReflectionUtil.callMethod_V(xaRes,"commit",oldXid, false);

         out.println("TEST: Check that Var7 was successful");
         if (!checkIns(Collection + ".CRASH2" + "007", "VAL7"))
            out.println("FAILURE");
         else
            out.println("SUCCESS");
      }
      catch (Exception e) {
         out.println("Exception: " + e.getMessage());
      }
   }

   private static void Var8() {
      try {
         out.println("TEST: Start transaction, do work, prepare, 'crash', commit");
         String table = Collection + ".CRASH2" + "008";
         try {
            JTATest.crtTmpTbl(table, conn_);
         }
         catch (Exception e) {
            String msg = e.toString();
            out.println("Caught exception: " + msg);
            if (msg.indexOf("*FILE already exists.") != -1) {
               out.println("Continuing");
            }
            else {
               out.println("Unexpected Exception. Leaving Var");
               return;
            }
         }

         String insStr = "VAL8";
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
         out.println(sqlString);
         PreparedStatement stmt = conn.prepareStatement(sqlString);

         // Generate a new transaction
         TestXid newXid = new TestXid();
	 newXid.setColl(Collection);
         JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         int      rc;
         rc = stmt.executeUpdate();
         if (rc != 1) {
            out.println("Expected 1 row inserted, got " + rc);
         }

         JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
         rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
         if (rc != javax.transaction.xa.XAResource.XA_OK) {
            out.println("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
         }

         // Notify the server that we have done up to the point of crash
         out.println(newXid.toString());
         out.println(cshMsg);

         // Wait to be toasted
	 boolean notDone = true; 
         while (notDone) {
	     try {
		 Thread.sleep(1);
	     } catch (Exception ex) {
		 notDone = false; 
	     } 
         }


      }
      catch (Exception e) {
         out.println("Unexpected Exception");
      }
   }

   private static void Var8Vfy() {
      try {
         out.println("TEST: Check that Var8 was successful");
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         out.println(oldXid.toString());
         JDReflectionUtil.callMethod_V(xaRes,"commit",oldXid, false);

         if (!checkIns(Collection + ".CRASH2" + "008", "VAL8"))
            out.println("FAILURE");
         else
            out.println("SUCCESS");
      }
      catch (Exception e) {
         out.println("Exception: " + e.getMessage());
      }
   }


   private static void Var9() {
      try {
         out.println("TEST: Start transaction, do work, 'crash', (data should rollback) end fails");
         String table = Collection + ".CRASH2" + "009";
         try {
            JTATest.crtTmpTbl(table, conn_);
         }
         catch (Exception e) {
            String msg = e.toString();
            out.println("Caught exception: " + msg);
            if (msg.indexOf("*FILE already exists.") != -1) {
               out.println("Continuing");
            }
            else {
               out.println("Unexpected Exception. Leaving Var");
               return;
            }
         }

         String insStr = JTATest.getStr();
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
         out.println(sqlString);
         PreparedStatement stmt = conn.prepareStatement(sqlString);

         // Generate a new transaction
         TestXid newXid = new TestXid();
	 newXid.setColl(Collection);
         JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         int      rc;
         rc = stmt.executeUpdate();
         if (rc != 1) {
            out.println("Expected 1 row inserted, got " + rc);
            return;
         }


         // Notify the server that we have done up to the point of crash
         out.println(newXid.toString());
         out.println(cshMsg);

         // Wait to be toasted
	 boolean notDone = true; 
         while (notDone) {
	     try {
		 Thread.sleep(1);
	     } catch (Exception ex) {
		 notDone = false; 
	     } 
         }

      }
      catch (Exception e) {
         out.println("Unexpected Exception");
      }
   }

   private static void Var9Vfy() {
      try {
         out.println("TEST: Check that Var9 was successful");
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         out.println(oldXid.toString());

         try {
            JDReflectionUtil.callMethod_V(xaRes,"end",oldXid, javax.transaction.xa.XAResource.TMSUCCESS);
            out.println("FAILURE");
         }
         catch (Exception ex) {
            if ((!(JDReflectionUtil.instanceOf(ex, "com.ibm.db2.jdbc.app.jta.javax.transaction.xa.XAException"))) &&
		(!(ex instanceof javax.transaction.xa.XAException))) {
               out.println("Unexpected exception" + ex.getMessage());
               return;
            }
            Object  xaEx = ex;
            if (JDReflectionUtil.getField_I(xaEx,"errorCode") != javax.transaction.xa.XAException.XAER_PROTO) {
               out.println("Expected " + javax.transaction.xa.XAException.XAER_PROTO + " failure. Got " + JDReflectionUtil.getField_I(xaEx,"errorCode"));
               return;
            }
            out.println("SUCCESS");
         }
      }
      catch (Exception e) {
         out.println("Exception: " + e.getMessage());
      }
   }


   private static void Var10() {
      try {
         out.println("TEST: Start transaction, do work, end, 'crash', prepare, rollback");
         String table = Collection + ".CRASH2" + "010";
         try {
            JTATest.crtTmpTbl(table, conn_);
         }
         catch (Exception e) {
            String msg = e.toString();
            out.println("Caught exception: " + msg);
            if (msg.indexOf("*FILE already exists.") != -1) {
               out.println("Continuing");
            }
            else {
               out.println("Unexpected Exception");
               return;
            }
         }

         String insStr = "VAL10";
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
         out.println(sqlString);
         PreparedStatement stmt = conn.prepareStatement(sqlString);

         // Generate a new transaction
         TestXid newXid = new TestXid();
	 newXid.setColl(Collection);
         JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         int      rc;
         rc = stmt.executeUpdate();
         if (rc != 1) {
            out.println("Expected 1 row inserted, got " + rc);
            return;
         }

         JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);

         // Notify the server that we have done up to the point of crash
         out.println(newXid.toString());
         out.println(cshMsg);

         // Wait to be toasted
	 boolean notDone = true; 
         while (notDone) {
	     try {
		 Thread.sleep(1);
	     } catch (Exception ex) {
		 notDone = false; 
	     } 
         }


      }
      catch (Exception e) {
         out.println("Unexpected Exception");
      }
   }

   private static void Var10Vfy() {
      try {
         out.println("TEST: Check that Var10 was successful");
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         out.println(oldXid.toString());

         int rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",oldXid);
         if (rc != javax.transaction.xa.XAResource.XA_OK) {
            out.println("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            return;
         }
         JDReflectionUtil.callMethod_V(xaRes,"rollback",oldXid);

         if (checkIns(Collection + ".CRASH2" + "010", "VAL10"))
            out.println("FAILURE");
         else
            out.println("SUCCESS");
      }
      catch (Exception e) {
         out.println("Exception: " + e.getMessage());
      }
   }


   private static void Var11() {
      try {
         out.println("TEST: Start transaction, do work, prepare, 'crash', rollback");
         String table = Collection + ".CRASH2" + "011";
         try {
            JTATest.crtTmpTbl(table, conn_);
         }
         catch (Exception e) {
            String msg = e.toString();
            out.println("Caught exception: " + msg);
            if (msg.indexOf("*FILE already exists.") != -1) {
               out.println("Continuing");
            }
            else {
               out.println("Unexpected Exception");
               return;
            }
         }

         String insStr = "VAL11";
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         Connection              conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

         String sqlString = "INSERT INTO " + table + " VALUES('" + insStr + "')";
         out.println(sqlString);
         PreparedStatement stmt = conn.prepareStatement(sqlString);

         // Generate a new transaction
         TestXid newXid = new TestXid();
	 newXid.setColl(Collection);
         JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

         int      rc;
         rc = stmt.executeUpdate();
         if (rc != 1) {
            out.println("Expected 1 row inserted, got " + rc);
            return;
         }

         JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMSUCCESS);
         rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
         if (rc != javax.transaction.xa.XAResource.XA_OK) {
            out.println("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            return;
         }

         // Notify the server that we have done up to the point of crash
         out.println(newXid.toString());
         out.println(cshMsg);

         // Wait to be toasted
	 boolean notDone = true; 
         while (notDone) {
	     try {
		 Thread.sleep(1);
	     } catch (Exception ex) {
		 notDone = false; 
	     } 
         }

      }
      catch (Exception e) {
         out.println("Unexpected Exception");
      }
   }

   private static void Var11Vfy() {
      try {
         out.println("TEST: Check that Var11 was successful");
         javax.sql.XADataSource ds      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
         JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
         Object        xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
         Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
         out.println(oldXid.toString());
         JDReflectionUtil.callMethod_V(xaRes,"rollback",oldXid);

         if (checkIns(Collection + ".CRASH2" + "011", "VAL11"))
            out.println("FAILURE");
         else
            out.println("SUCCESS");
      }
      catch (Exception e) {
         out.println("Exception: " + e.getMessage());
      }
   }


   public static void main(String[] args) throws Exception {
      int      varNum = 0;
      int      serverPort = 0;
      String sysName = "localhost";
      try {
         serverPort = Integer.parseInt(System.getProperty("jta.server.socket"));
      }
      catch (Exception e) {
         System.err.println("Illegal server socket number: " +
                            System.getProperty("jta.server.socket"));
         System.exit(1);
      }
      try {
         crash2Socket = new Socket(sysName, serverPort);
         out = new PrintWriter(crash2Socket.getOutputStream(), true);
         in = new BufferedReader(new InputStreamReader(crash2Socket.getInputStream()));
      }
      catch (UnknownHostException e) {
         System.err.println("Don't know about host: " + sysName);
         System.exit(1);
      }
      catch (IOException e) {
         System.err.println("Couldn't get I/O for the connection to: " + sysName);
         System.exit(1);
      }

      try {
        out.println("In JTACrash2Vars: main()");
        String fromCrash2;
        while ((fromCrash2 = in.readLine()) != null) {
          out.println("fromCrash2 " + fromCrash2);

          JTATest.verboseOut("Crash2: " + fromCrash2);
          if (fromCrash2.startsWith("DBName:")) {
            system_ = fromCrash2.substring(7);
          }
          if (fromCrash2.startsWith("Collection:")) {
            Collection = fromCrash2.substring(11);
          }
          if (fromCrash2.startsWith("TestXid : L")) {
            xidStr = fromCrash2;
          }
          if (fromCrash2.startsWith("Var")) {
            break;
          }
        }

        if (xidStr != null) {
          try {
            oldXid = new TestXid(xidStr);
          }
          catch (Throwable e) {
            out.println("Caught Exception creating TestXid");
            out.println(e.getMessage());
            System.exit(1);
          }
        }

        varNum = new Integer(fromCrash2.substring(3,6)).intValue();
        Class.forName("com.ibm.db2.jdbc.app.DB2Driver");
        try {
          conn_ = DriverManager.getConnection("jdbc:db2:" + system_);
        }
        catch (Exception e) {
          out.println("DriverManager.getConnection(jdbc:db2:" + system_ + ") out.println");
          out.println(e.toString());
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
      }
      finally {
        if (conn_ != null) try { conn_.close(); } catch (Throwable t) {}
      }



   }

}
