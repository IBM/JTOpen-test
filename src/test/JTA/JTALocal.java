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
package test.JTA;


import java.sql.*;
import java.util.*;

import javax.sql.DataSource;
import javax.sql.XADataSource;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JTATest;
import test.PasswordVault;
import test.JD.JDTestUtilities;

public class JTALocal extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTALocal";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTATest.main(newArgs); 
   }

   // private String insStr = "JTALocal";
   private String TABLE1 = JTATest.COLLECTION + ".LOC1TBL";
   private String TABLE2 = JTATest.COLLECTION + ".LOC2TBL";
   private String TABLE3 = JTATest.COLLECTION + ".LOC3TBL";
   private Connection c;
   boolean isIasp = false; 
   boolean isNTS = false; 

   
/**
Constructor.
**/
   public JTALocal (AS400 systemObject,
                    Hashtable<String,Vector<String>> namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTALocal",
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
       TABLE1 = JTATest.COLLECTION + ".LOC1TBL";
       TABLE2 = JTATest.COLLECTION + ".LOC2TBL";
       TABLE3 = JTATest.COLLECTION + ".LOC3TBL";

       if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
         JTATest.verboseOut(baseURL_);
         c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
	 isIasp = JDTestUtilities.isIasp(c); 
         Statement s = c.createStatement();
         s.execute("CREATE TABLE " + TABLE1 + " (ID INTEGER PRIMARY KEY, " +
                   "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
	 s.execute("GRANT ALL ON "+TABLE1+" TO PUBLIC "); 
         s.execute("CREATE TABLE " + TABLE2 + " (ID INTEGER PRIMARY KEY, " +
                   "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
	 s.execute("GRANT ALL ON "+TABLE2+" TO PUBLIC "); 
         s.execute("CREATE TABLE " + TABLE3 + " (ID INTEGER PRIMARY KEY, " +
                   "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
	 s.execute("GRANT ALL ON "+TABLE3+" TO PUBLIC "); 
         s.close();
         c.commit();
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
         s.execute("DROP TABLE " + TABLE1);
         s.execute("DROP TABLE " + TABLE2);
         s.execute("DROP TABLE " + TABLE3);
         s.close();
         c.commit();
         c.close();
      }
       unlockSystem(); 
   }


   public void Var001() {             // from ~kulack/JTA/jtatest/JTALocal1.java
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Same thread, JTA local Tx work, JTA global Tx work," +
                               "JDBC auto-commit work, repeat JTA work");

            String TABLE = TABLE1;
            int                        rc;
            XADataSource            xaDs              = null;
            DataSource              ds                = null;
            Object               xaConn            = null;
            Object xaRes =  null;

            // Either a transactional or normal JDBC connection depending
            // on which object gets used for getConnection()
            Connection                 conn              = null;
            Statement                  setupStatement    = null;
            JTATestXid                 newXid            = null;

            ds    = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2DataSource");

            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds, "setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

   xaDs    = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");

            JDReflectionUtil.callMethod_V(xaDs,"setDatabaseName",system_);


            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            // Transactional connection for JTA work
            //
            // Do some JTA LOCAL TRANSACTIONAL WORK
            // (We didn't start a global transaction)
            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            xaConn = xaDs.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            setupStatement = conn.createStatement();
            try {
               JTATest.verboseOut("JTA Local Tx Drop table " + TABLE);
               setupStatement.execute("DROP TABLE " + TABLE);
            }
            catch (Exception e) {
               JTATest.verboseOut("JTA Local Tx Drop failed: " + e);
               if (JTATest.verbose) {
                  e.printStackTrace();
               }
            }
            setupStatement.close(); setupStatement=null;
            // Explicit Connection.commit() required for JTA local transactional work
            conn.commit();
            // Close the JTAConnection
            conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;


            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            // Transactional connection for JTA work
            //
            // Do some JTA GLOCAL TRANSACTIONAL WORK
            // Need to start the new transaction.
            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            xaConn = xaDs.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            setupStatement = conn.createStatement();
            try {
               JTATest.verboseOut("JTA Global Tx Create table " + TABLE);
               setupStatement.execute("CREATE TABLE " + TABLE + " (ID INTEGER PRIMARY KEY, " +
                                      "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");

	       setupStatement.execute("GRANT ALL ON " + TABLE + " TO PUBLIC");

            }
            catch (Exception e) {
               JTATest.verboseOut("JTA Global Tx Create failed: " + e);
               failed(e, "Unexpected exception");
            }
            setupStatement.close(); setupStatement=null;
            // JTA 2 phase commit() required for JTA global transactional work
            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
            rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_OK) {
               failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            }

            JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);
            // Close the JTAConnection
            conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;



            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            // Normal JDBC auto-commit Connection
            //
            // DO some JDBC normal autocommit work
            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            conn   = (Connection)JDReflectionUtil.callMethod_O(ds,"getConnection");
            setupStatement = conn.createStatement();
            try {
               JTATest.verboseOut("JDBC Local Insert into table " + TABLE);
               setupStatement.execute("INSERT INTO " + TABLE + " VALUES(0, 'Simple Test')");
            }
            catch (Exception e) {
               JTATest.verboseOut("JDBC Local Insert failed: " + e);
               failed(e, "Unexpected exception");
            }
            setupStatement.close(); setupStatement=null;
            // No commit required for JDBC autocommit
            // Close the Connection
            conn.close(); conn=null;

            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            // Transactional connection for JTA work
            //
            // Do some JTA LOCAL TRANSACTIONAL WORK
            // (We didn't start a global transaction)
            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            xaConn = xaDs.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            setupStatement = conn.createStatement();
            try {
               JTATest.verboseOut("JTA Local Insert into table " + TABLE);
               setupStatement.execute("INSERT INTO " + TABLE + " VALUES(1, " +
                                      "'Simple JTA Local Test')");
            }
            catch (Exception e) {
               JTATest.verboseOut("JTA Local Insert failed: " + e);
               failed(e, "Unexpected exception");
            }
            setupStatement.close(); setupStatement=null;
            // Explicit Connection.commit() required for JTA local transactional work
            conn.commit();
            // Close the JTAConnection
            conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;


            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            // Transactional connection for JTA work
            //
            // Do some JTA GLOBAL TRANSACTIONAL WORK
            // Need to start the new transaction.
            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            xaConn = xaDs.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

            newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

            setupStatement = conn.createStatement();
            try {
               JTATest.verboseOut("JTA Global Insert into table " + TABLE);
               setupStatement.execute("INSERT INTO " + TABLE + " VALUES(2, " +
                                      "'Simple JTA Global Test')");
            }
            catch (Exception e) {
               JTATest.verboseOut("JTA Global Insert failed: " + e);
               failed(e, "Unexpected exception");
            }
            setupStatement.close(); setupStatement=null;
            // JTA 2 phase commit() required for JTA global transactional work
            JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
            rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
            if (rc != javax.transaction.xa.XAResource.XA_OK) {
               failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
            }
            JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);
            // Close the JTAConnection
            conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;

            JTATest.verboseOut("Done");
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }

   public void Var002() { // from ~kulack/JTA/jtatest/JTALocal2.java
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Same thread, REPEAT: JTA 1 connection: local Tx work/global Tx work, JDBC 1 connection auto-commit work, JTA work again but use global THEN local. ");

            String TABLE = TABLE2;
            int                        rc;
            XADataSource            xaDs              = null;
            DataSource              ds                = null;
            Object               xaConn            = null;
            Object xaRes =  null;

            // Either a transactional or normal JDBC connection depending
            // on which object gets used for getConnection()
            Connection                 conn              = null;
            Statement                  setupStatement    = null;
            JTATestXid                 newXid            = null;
            int repetitions = 3;
            int count = repetitions;


            ds     = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2DataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(    ds,"setUser", userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            xaDs    = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");

            JDReflectionUtil.callMethod_V(xaDs,"setDatabaseName",system_);


            while (count > 0) {
               --count;

               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               // Transactional connection for JTA work
               //
               // Do some JTA LOCAL TRANSACTIONAL WORK
               // (We didn't start a global transaction)
               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               xaConn = xaDs.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               setupStatement = conn.createStatement();
               try {
                  JTATest.verboseOut(count + " :JTA Local Tx Drop table " + TABLE);
                  setupStatement.execute("DROP TABLE " + TABLE);
               }
               catch (Exception e) {
                  JTATest.verboseOut("JTA Local Tx Drop failed: " + e);
                  failed(e, "Unexpected exception");
               }
               setupStatement.close(); setupStatement=null;
               // Explicit Connection.commit() required for JTA local transactional work
               conn.commit();
               // DON'T close the JTAConnection. Use it again for global work
               // conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;


               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               // Transactional connection for JTA work
               //
               // Do some JTA GLOCAL TRANSACTIONAL WORK using the same connection
               // Need to start the new transaction.
               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               newXid = new JTATestXid();
               JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

               setupStatement = conn.createStatement();
               try {
                  JTATest.verboseOut(count + " :JTA Global Tx Create table " + TABLE);
                  setupStatement.execute("CREATE TABLE " + TABLE + " (ID INTEGER PRIMARY KEY, " +
                                         "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
                  setupStatement.execute("GRANT ALL ON " + TABLE + " TO PUBLIC"); 
               }
               catch (Exception e) {
                  JTATest.verboseOut("JTA Global Tx Create failed: " + e);
                  failed(e, "Unexpected exception");
               }
               setupStatement.close(); setupStatement=null;
               // JTA 2 phase commit() required for JTA global transactional work
               JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
               rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
               if (rc != javax.transaction.xa.XAResource.XA_OK) {
                  failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
               }
               JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);
               // Close the JTAConnection
               conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;



               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               // Normal JDBC auto-commit Connection
               //
               // DO some JDBC normal autocommit work
               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               conn   = (Connection)JDReflectionUtil.callMethod_O(ds,"getConnection");
               setupStatement = conn.createStatement();
               try {
                  JTATest.verboseOut(count + " :JDBC Local Insert into table " + TABLE);
                  setupStatement.execute("INSERT INTO " + TABLE + " VALUES(0, 'Simple Test')");
               }
               catch (Exception e) {
                  JTATest.verboseOut("JDBC Local Insert failed: " + e);
                  failed(e, "Unexpected exception");
               }
               setupStatement.close(); setupStatement=null;
               // No commit required for JDBC autocommit
               // Close the Connection
               conn.close(); conn=null;

               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               // Transactional connection for JTA work
               //
               // Do some JTA GLOBAL TRANSACTIONAL WORK
               // Need to start the new transaction.
               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               xaConn = xaDs.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               newXid = new JTATestXid();
               JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

               setupStatement = conn.createStatement();
               try {
                  JTATest.verboseOut(count + " :JTA Global Insert into table " + TABLE);
                  setupStatement.execute("INSERT INTO " + TABLE + " VALUES(2, " +
                                         "'Simple JTA Global Test')");
               }
               catch (Exception e) {
                  JTATest.verboseOut("JTA Global Insert failed: " + e);
                  failed(e, "Unexpected exception");
               }
               setupStatement.close(); setupStatement=null;
               // JTA 2 phase commit() required for JTA global transactional work
               JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
               rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
               if (rc != javax.transaction.xa.XAResource.XA_OK) {
                  failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
               }
               JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);
               // DONT Close the JTAConnection, reuse for local connection work
               // conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;

               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               // Transactional connection for JTA work
               //
               // Do some JTA LOCAL TRANSACTIONAL WORK
               // (We didn't start a global transaction)
               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               setupStatement = conn.createStatement();
               try {
                  JTATest.verboseOut(count + " :JTA Local Insert into table " + TABLE);
                  setupStatement.execute("INSERT INTO " + TABLE + " VALUES(1, " +
                                         "'Simple JTA Local Test')");
               }
               catch (Exception e) {
                  JTATest.verboseOut("JTA Local Insert failed: " + e);
                  failed(e, "Unexpected exception");
               }
               setupStatement.close(); setupStatement=null;
               // Explicit Connection.commit() required for JTA local transactional work
               conn.commit();
               // Close the JTAConnection
               conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;

            }

            JTATest.verboseOut("Done");
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var003() { // similar to var2 but all JTA is under same connection
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Same thread, REPEAT: JTA 1 connection: local Tx work/global Tx work, JDBC 1 connection auto-commit work, JTA work again but use global THEN local.(same conn as first JTA) ");

            String TABLE = TABLE3;
            int                        rc;
            XADataSource            xaDs              = null;
            DataSource              ds                = null;
            Object               xaConn            = null;
            Object xaRes =  null;

            // Either a transactional or normal JDBC connection depending
            // on which object gets used for getConnection()
            Connection                 conn              = null;
            Statement                  setupStatement    = null;
            JTATestXid                 newXid            = null;
            int repetitions = 3;
            int count = repetitions;


            ds     = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2DataSource");

            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            xaDs    = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");

            JDReflectionUtil.callMethod_V(xaDs,"setDatabaseName",system_);


            while (count > 0) {
               --count;

               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               // Transactional connection for JTA work
               //
               // Do some JTA LOCAL TRANSACTIONAL WORK
               // (We didn't start a global transaction)
               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               xaConn = xaDs.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
               xaRes  = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
               conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               setupStatement = conn.createStatement();
               try {
                  JTATest.verboseOut(count + " :JTA Local Tx Drop table " + TABLE);
                  setupStatement.execute("DROP TABLE " + TABLE);
               }
               catch (Exception e) {
                  JTATest.verboseOut("JTA Local Tx Drop failed: " + e);
                  failed(e, "Unexpected exception");
               }
               setupStatement.close(); setupStatement=null;
               // Explicit Connection.commit() required for JTA local transactional work
               conn.commit();
               // DON'T close the JTAConnection. Use it again for global work
               // conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;


               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               // Transactional connection for JTA work
               //
               // Do some JTA GLOCAL TRANSACTIONAL WORK using the same connection
               // Need to start the new transaction.
               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               newXid = new JTATestXid();
               JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

               setupStatement = conn.createStatement();
               try {
                  JTATest.verboseOut(count + " :JTA Global Tx Create table " + TABLE);
                  setupStatement.execute("CREATE TABLE " + TABLE + " (ID INTEGER PRIMARY KEY, " +
                                         "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
                  setupStatement.execute("GRANT ALL ON " + TABLE + "  TO PUBLIC"); 
               }
               catch (Exception e) {
                  JTATest.verboseOut("JTA Global Tx Create failed: " + e);
                  failed(e, "Unexpected exception");
               }
               setupStatement.close(); setupStatement=null;
               // JTA 2 phase commit() required for JTA global transactional work
               JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
               rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
               if (rc != javax.transaction.xa.XAResource.XA_OK) {
                  failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
               }
               JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);
               // Dont Close the JTAConnection
               //conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;



               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               // Normal JDBC auto-commit Connection
               //
               // DO some JDBC normal autocommit work
               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               conn   = (Connection)JDReflectionUtil.callMethod_O(ds,"getConnection");
               setupStatement = conn.createStatement();
               try {
                  JTATest.verboseOut(count + " :JDBC Local Insert into table " + TABLE);
                  setupStatement.execute("INSERT INTO " + TABLE + " VALUES(0, 'Simple Test')");
               }
               catch (Exception e) {
                  JTATest.verboseOut("JDBC Local Insert failed: " + e);
                  failed(e, "Unexpected exception");
               }
               setupStatement.close(); setupStatement=null;
               // No commit required for JDBC autocommit
               // Close the Connection
               conn.close(); conn=null;

               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               // Transactional connection for JTA work
               //
               // Do some JTA GLOBAL TRANSACTIONAL WORK
               // Need to start the new transaction.
               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               // use the connection used in JTA before
               conn   = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

               newXid = new JTATestXid();
               JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);

               setupStatement = conn.createStatement();
               try {
                  JTATest.verboseOut(count + " :JTA Global Insert into table " + TABLE);
                  setupStatement.execute("INSERT INTO " + TABLE + " VALUES(2, " +
                                         "'Simple JTA Global Test')");
               }
               catch (Exception e) {
                  JTATest.verboseOut("JTA Global Insert failed: " + e);
                  failed(e, "Unexpected exception");
               }
               setupStatement.close(); setupStatement=null;
               // JTA 2 phase commit() required for JTA global transactional work
               JDReflectionUtil.callMethod_V(xaRes,"end",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
               rc = JDReflectionUtil.callMethod_I(xaRes,"prepare",newXid);
               if (rc != javax.transaction.xa.XAResource.XA_OK) {
                  failed("Expected XA_OK (" + javax.transaction.xa.XAResource.XA_OK + "), got " + rc);
               }
               JDReflectionUtil.callMethod_V(xaRes,"commit",newXid, false);
               // DONT Close the JTAConnection, reuse for local connection work
               // conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;

               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               // Transactional connection for JTA work
               //
               // Do some JTA LOCAL TRANSACTIONAL WORK
               // (We didn't start a global transaction)
               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               setupStatement = conn.createStatement();
               try {
                  JTATest.verboseOut(count + " :JTA Local Insert into table " + TABLE);
                  setupStatement.execute("INSERT INTO " + TABLE + " VALUES(1, " +
                                         "'Simple JTA Local Test')");
               }
               catch (Exception e) {
                  JTATest.verboseOut("JTA Local Insert failed: " + e);
                  failed(e, "Unexpected exception");
               }
               setupStatement.close(); setupStatement=null;
               // Explicit Connection.commit() required for JTA local transactional work
               conn.commit();
               // Close the JTAConnection
               conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;

            }

            JTATest.verboseOut("Done");
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }
}
