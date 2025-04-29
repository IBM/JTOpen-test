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
// File Name:    JTAStdLocal.java
//
// Description:  Same as JTALocal.java but test standard interfaces for
//               JTA & JDBC Std Ext
//
// Classes:      JTAStdLocal
//
////////////////////////////////////////////////////////////////////////
package test.JTA;

import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JDTestDriver;
import test.JTATest;
import test.JD.JDTestUtilities;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

public class JTAStdLocal extends JTATestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAStdLocal";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTAStdTest.main(newArgs); 
   }

   private String TABLE1 = JTATest.COLLECTION + ".LOC1TBL";
   private String TABLE2 = JTATest.COLLECTION + ".LOC2TBL";
   private String TABLE3 = JTATest.COLLECTION + ".LOC3TBL";
   private Connection c;
   boolean skipDrop = false; 
/**
Constructor.
**/
   public JTAStdLocal (AS400 systemObject,
                    Hashtable<String,Vector<String>> namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTAStdLocal",
             namesAndVars, runMode, fileOutputStream,
             password);
   }

   public JTAStdLocal (AS400 systemObject,
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
       TABLE1 = JTATest.COLLECTION + ".LOC1TBL";
       TABLE2 = JTATest.COLLECTION + ".LOC2TBL";
       TABLE3 = JTATest.COLLECTION + ".LOC3TBL";

      if (isJdbc20StdExt ()) {
         JTATest.verboseOut(baseURL_);
         c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
	 isIasp = JDTestUtilities.isIasp(c); 
         Statement s = c.createStatement();
	 try { 
	 s.execute("DROP TABLE " + TABLE1);
	 } catch (Exception e) {} 
	 try {
	 s.execute("DROP TABLE " + TABLE2);
	 } catch (Exception e) {} 
	 try {
	 s.execute("DROP TABLE " + TABLE3);
	 } catch (Exception e) {} 


         s.execute("CREATE TABLE " + TABLE1 + " (ID INTEGER PRIMARY KEY, " +
                   "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
         s.execute("CREATE TABLE " + TABLE2 + " (ID INTEGER PRIMARY KEY, " +
                   "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
         s.execute("CREATE TABLE " + TABLE3 + " (ID INTEGER PRIMARY KEY, " +
                   "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
         s.execute("GRANT ALL ON " + TABLE1 + " TO PUBLIC WITH GRANT OPTION"); 
         s.execute("GRANT ALL ON " + TABLE2 + " TO PUBLIC WITH GRANT OPTION"); 
         s.execute("GRANT ALL ON " + TABLE3 + " TO PUBLIC WITH GRANT OPTION");
	 s.execute("call QSYS.QCMDEXC('GRTOBJAUT OBJ("+TABLE1.replace('.','/')+") OBJTYPE(*FILE) USER(*PUBLIC) AUT(*ALL)                                                          ',000000080.00000)");
	 s.execute("call QSYS.QCMDEXC('GRTOBJAUT OBJ("+TABLE2.replace('.','/')+") OBJTYPE(*FILE) USER(*PUBLIC) AUT(*ALL)                                                          ',000000080.00000)");    
	 s.execute("call QSYS.QCMDEXC('GRTOBJAUT OBJ("+TABLE3.replace('.','/')+") OBJTYPE(*FILE) USER(*PUBLIC) AUT(*ALL)                                                          ',000000080.00000)");    

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
      if (isJdbc20StdExt ()) {
         Statement s = c.createStatement();
	 if (!skipDrop) { 
	     s.execute("DROP TABLE " + TABLE1);
	     s.execute("DROP TABLE " + TABLE2);
	     s.execute("DROP TABLE " + TABLE3);
	 }
         s.close();
         c.commit();
         c.close();
      }
      unlockSystem(); 
   }


   public void Var001() {             // from ~kulack/JTA/jtatest/JTALocal1.java
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
            XAConnection               xaConn            = null;
            XAResource                 xaRes             = null;

            // Either a transactional or normal JDBC connection depending
            // on which object gets used for getConnection()
            Connection                 conn              = null;
            Statement                  setupStatement    = null;
            JTATestXid                 newXid            = null;

            ds     = newDataSource();
            xaDs   = newXADataSource();


            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            // Transactional connection for JTA work
            //
            // Do some JTA LOCAL TRANSACTIONAL WORK
            // (We didn't start a global transaction)
            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            xaConn = xaDs.getXAConnection();
            xaRes  = xaConn.getXAResource();
            conn   = xaConn.getConnection();

            setupStatement = conn.createStatement();
            try {
               JTATest.verboseOut("JTA Local Tx Drop table " + TABLE);
               setupStatement.execute("DROP TABLE " + TABLE);
            }
            catch (Exception e) {
		System.out.println(e.toString());
		skipDrop = true; 
               JTATest.verboseOut("JTA Local Tx Drop failed: " + e);
               if (JTATest.verbose) {
                  e.printStackTrace();
               }
            }
            setupStatement.close(); setupStatement=null;
            // Explicit Connection.commit() required for JTA local transactional work
            conn.commit();
            // Close the JTAConnection
            conn.close(); conn=null; xaConn=null; xaRes=null; 
            // newXid=null; /* can only be null at this point */ 


            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            // Transactional connection for JTA work
            //
            // Do some JTA GLOCAL TRANSACTIONAL WORK
            // Need to start the new transaction.
            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            xaConn = xaDs.getXAConnection();
            xaRes  = xaConn.getXAResource();
            conn   = xaConn.getConnection();

            newXid = new JTATestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            setupStatement = conn.createStatement();
            try {
               JTATest.verboseOut("JTA Global Tx Create table " + TABLE);
               setupStatement.execute("CREATE TABLE " + TABLE + " (ID INTEGER PRIMARY KEY, " +
                                      "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
               setupStatement.execute("GRANT ALL ON " + TABLE + " TO PUBLIC WITH GRANT OPTION"); 
            }
            catch (Exception e) {
               JTATest.verboseOut("JTA Global Tx Create failed: " + e);
               failed(e, "Unexpected exception");
            }
            setupStatement.close(); setupStatement=null;
            // JTA 2 phase commit() required for JTA global transactional work
            xaRes.end(newXid, XAResource.TMSUCCESS);
            rc = xaRes.prepare(newXid);
            if (rc != XAResource.XA_OK) {
               failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
            }

            xaRes.commit(newXid, false);
            // Close the JTAConnection
            conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;



            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            // Normal JDBC auto-commit Connection
            //
            // DO some JDBC normal autocommit work
            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            conn   = ds.getConnection();
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
            xaConn = xaDs.getXAConnection();
            xaRes  = xaConn.getXAResource();
            conn   = xaConn.getConnection();

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
            conn.close(); conn=null; xaConn=null; xaRes=null; 
            // newXid=null; /* Can only be null at this point */ 


            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            // Transactional connection for JTA work
            //
            // Do some JTA GLOBAL TRANSACTIONAL WORK
            // Need to start the new transaction.
            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////
            xaConn = xaDs.getXAConnection();
            xaRes  = xaConn.getXAResource();
            conn   = xaConn.getConnection();

            newXid = new JTATestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);

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
            xaRes.end(newXid, XAResource.TMSUCCESS);
            rc = xaRes.prepare(newXid);
            if (rc != XAResource.XA_OK) {
               failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
            }
            xaRes.commit(newXid, false);
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

   public void Var002() { // from ~kulack/JTA/jtatest/JTALocal2.java
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
            XAConnection               xaConn            = null;
            XAResource                 xaRes             = null;

            // Either a transactional or normal JDBC connection depending
            // on which object gets used for getConnection()
            Connection                 conn              = null;
            Statement                  setupStatement    = null;
            JTATestXid                 newXid            = null;
            int repetitions = 3;
            int count = repetitions;


            ds     = newDataSource();
            xaDs   = newXADataSource();


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
               xaConn = xaDs.getXAConnection();
               xaRes  = xaConn.getXAResource();
               conn   = xaConn.getConnection();

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
               xaRes.start(newXid, XAResource.TMNOFLAGS);

               setupStatement = conn.createStatement();
               try {
                  JTATest.verboseOut(count + " :JTA Global Tx Create table " + TABLE);
                  setupStatement.execute("CREATE TABLE " + TABLE + " (ID INTEGER PRIMARY KEY, " +
                                         "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
		  setupStatement.execute("GRANT ALL ON " + TABLE + " TO PUBLIC WITH GRANT OPTION"); 

               }
               catch (Exception e) {
                  JTATest.verboseOut("JTA Global Tx Create failed: " + e);
                  failed(e, "Unexpected exception");
               }
               setupStatement.close(); setupStatement=null;
               // JTA 2 phase commit() required for JTA global transactional work
               xaRes.end(newXid, XAResource.TMSUCCESS);
               rc = xaRes.prepare(newXid);
               if (rc != XAResource.XA_OK) {
                  failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
               }
               xaRes.commit(newXid, false);
               // Close the JTAConnection
               conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;



               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               // Normal JDBC auto-commit Connection
               //
               // DO some JDBC normal autocommit work
               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               conn   = ds.getConnection();
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
               xaConn = xaDs.getXAConnection();
               xaRes  = xaConn.getXAResource();
               conn   = xaConn.getConnection();

               newXid = new JTATestXid();
               xaRes.start(newXid, XAResource.TMNOFLAGS);

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
               xaRes.end(newXid, XAResource.TMSUCCESS);
               rc = xaRes.prepare(newXid);
               if (rc != XAResource.XA_OK) {
                  failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
               }
               xaRes.commit(newXid, false);
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


   public void Var003() { // similar to var2 but all JTA is under same connection

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
            XAConnection               xaConn            = null;
            XAResource                 xaRes             = null;

            // Either a transactional or normal JDBC connection depending
            // on which object gets used for getConnection()
            Connection                 conn              = null;
            Statement                  setupStatement    = null;
            JTATestXid                 newXid            = null;
            int repetitions = 3;
            int count = repetitions;


            ds     = newDataSource();
            xaDs   = newXADataSource();


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
               xaConn = xaDs.getXAConnection();
               xaRes  = xaConn.getXAResource();
               conn   = xaConn.getConnection();

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
               xaRes.start(newXid, XAResource.TMNOFLAGS);

               setupStatement = conn.createStatement();
               try {
                  JTATest.verboseOut(count + " :JTA Global Tx Create table " + TABLE);
                  setupStatement.execute("CREATE TABLE " + TABLE + " (ID INTEGER PRIMARY KEY, " +
                                         "DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");
		  setupStatement.execute("GRANT ALL ON " + TABLE + " TO PUBLIC WITH GRANT OPTION"); 

               }
               catch (Exception e) {
                  JTATest.verboseOut("JTA Global Tx Create failed: " + e);
                  failed(e, "Unexpected exception");
               }
               setupStatement.close(); setupStatement=null;
               // JTA 2 phase commit() required for JTA global transactional work
               xaRes.end(newXid, XAResource.TMSUCCESS);
               rc = xaRes.prepare(newXid);
               if (rc != XAResource.XA_OK) {
                  failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
               }
               xaRes.commit(newXid, false);
               // Dont Close the JTAConnection
               //conn.close(); conn=null; xaConn=null; xaRes=null; newXid=null;



               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               // Normal JDBC auto-commit Connection
               //
               // DO some JDBC normal autocommit work
               ////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////
               Connection connCopy = null;
               if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
                   connCopy = conn;  //@pda for toolbox, need reference
               }
               conn   = ds.getConnection();
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
               if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
                   //toolbox just throws exception if connection is already open
                   conn = connCopy; 
                }else{
                   conn = xaConn.getConnection();
               }

               newXid = new JTATestXid();
               xaRes.start(newXid, XAResource.TMNOFLAGS);

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
               xaRes.end(newXid, XAResource.TMSUCCESS);
               rc = xaRes.prepare(newXid);
               if (rc != XAResource.XA_OK) {
                  failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc);
               }
               xaRes.commit(newXid, false);
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
