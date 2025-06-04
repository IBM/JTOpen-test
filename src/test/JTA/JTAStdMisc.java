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
// File Name:    JTAStdMisc.java
//
// Description:  Same as JTAMisc.java but test standard interfaces for
//               JTA & JDBC Std Ext
//
// Classes:      JTAStdMisc
//
////////////////////////////////////////////////////////////////////////
package test.JTA;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;

import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import com.ibm.as400.access.AS400;

import test.JDTestDriver;
import test.JTATest;
import test.JVMInfo;
import test.JD.JDTestUtilities;

public class JTAStdMisc extends JTATestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTAStdMisc";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTAStdTest.main(newArgs); 
   }

   JTATestXid[] xids = new JTATestXid[10];
   //protected boolean isNTS = false;  @PDD move to JTATestcase
   long gtridPrefix = 0x1DBCBAD100000000L;   
   
   
   
   boolean xidFromThisTest(Xid xid) {
     long longGtrid = 0; 
     byte[] gtrid=xid.getGlobalTransactionId();
     for (int i = 0; i < gtrid.length && i < 8; i++ ) {
       longGtrid = longGtrid << 8; 
       longGtrid = longGtrid | ( 0xFF & ((long)gtrid[i])); 
     }
     longGtrid = longGtrid & 0xFFFFFFFF00000000L; 
     return (longGtrid == gtridPrefix); 
   }
/**
Constructor.
**/
   public void setupGtridPrefix() {
     //
     // We use a different grid prefix for each jdk level
     //
     int jdk = JVMInfo.getJDK(); 
     if (jdk == JVMInfo.JDK_16) {
       gtridPrefix = 0x1DBCCC1600000000L;    

     } else if (jdk == JVMInfo.JDK_17) {
       gtridPrefix = 0x1DBCCC1700000000L;    
     } else if (jdk == JVMInfo.JDK_18) {
       gtridPrefix = 0x1DBCCC1800000000L;    

     } else if (jdk == JVMInfo.JDK_19) {
       gtridPrefix = 0x1DBCCC1900000000L;    

     } else if (jdk == JVMInfo.JDK_V11) {
       gtridPrefix = 0x1DBCCC1B00000000L;    
     } else if (jdk == JVMInfo.JDK_V17) {
       gtridPrefix = 0x1DBCCC1C00000000L;    
     } else if (jdk == JVMInfo.JDK_V21) {
       gtridPrefix = 0x1DBCCC1D00000000L;    
     } else {
       System.out.println("Warning.. did not recognize jdk version "+jdk); 
     }
     JTATestXid.setGtridPrefix(gtridPrefix);
     
   }
   public JTAStdMisc (AS400 systemObject,
                      Hashtable<String,Vector<String>> namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password) {
      super (systemObject, "JTAStdMisc",
             namesAndVars, runMode, fileOutputStream,
             password);
      setupGtridPrefix(); 
  
   }

   public JTAStdMisc (AS400 systemObject,
		      String testname, 
                      Hashtable<String,Vector<String>> namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password) {
      super (systemObject, testname,
             namesAndVars, runMode, fileOutputStream,
             password);
      setupGtridPrefix(); 
   }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
   protected void setup ()
   throws Exception
   {
     
       lockSystem("JTATEST",600);
      if (isJdbc20StdExt()) {
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
      if (isJdbc20StdExt()) {
      }
      unlockSystem(); 
   }


   private class Recover {
      XADataSource            xaDs;
      XAConnection               xaConn;
      XAResource                 xaRes;
      Connection                 conn;
      JTATestXid                 newXid = null;

      String TABLE = JTATest.COLLECTION + ".RECOV1TBL";

      Recover(String rdb, int tableNumber) throws Exception {
         this(rdb, tableNumber, -1);
      }

      Recover(String rdb, int tableNumber, int formatId) throws Exception {
         TABLE = JTATest.COLLECTION + ".RECOV1TBL";
         JTATest.verboseOut(Thread.currentThread().getName() +
                            ": New transaction.");
         JTATest.verboseOut(Thread.currentThread().getName() +
                            ": Get a DataSource");

         xaDs   = newXADataSource();

         JTATest.verboseOut(Thread.currentThread().getName() +
                            ": Get an XAConnection");

         // Get the XAConnection.
         xaConn = xaDs.getXAConnection();
         JTATest.verboseOut(Thread.currentThread().getName() +
                            ": Get the XAResource");
         xaRes  = xaConn.getXAResource();
         // Get the real connection object
         JTATest.verboseOut(Thread.currentThread().getName() +
                            ": Get the Connection");
         conn   = xaConn.getConnection();

         // Generate a new transaction
         newXid = new JTATestXid(formatId);
         JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                            "XAResource.start");
         xaRes.start(newXid, XAResource.TMNOFLAGS);

         Statement setupStatement = conn.createStatement();
	 String sql = "CREATE TABLE " + TABLE + tableNumber +
                                " (ID INTEGER NOT NULL, " +
                                "  DESCRIPTION CHAR (254 ) NOT NULL )";
         JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                            "Create table using " +sql);
         setupStatement.execute(sql);

         JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                            "XAResource.end");
         xaRes.end(newXid, XAResource.TMSUCCESS);

         JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                            "XAResource.prepare");
         xaRes.prepare(newXid);
         JTATest.verboseOut("Cleanup");
         setupStatement.close();
         conn.close();

      }
   }



   private class Timeout {
      XADataSource            xaDs;
      XAConnection               xaConn;
      XAResource                 xaRes;
      Connection                 conn;
      JTATestXid                 newXid = null;

      String TABLE = JTATest.COLLECTION + ".TIM1TBL";

      Timeout(String rdb, int tableNumber, boolean endTx)
      throws Exception {

         TABLE = JTATest.COLLECTION + ".TIM1TBL";
         JTATest.verboseOut(Thread.currentThread().getName() +
                            ": New transaction. Will be ended == " + endTx);
         JTATest.verboseOut(Thread.currentThread().getName() +
                            ": Get a DataSource");
         xaDs   = newXADataSource();

         JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
         // Get the XAConnection.
         xaConn = xaDs.getXAConnection();

         JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");
         // Get the real connection object
         xaRes  = xaConn.getXAResource();

         JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
         conn   = xaConn.getConnection();

         // Generate a new transaction
         newXid = new JTATestXid();
         xids[tableNumber] = newXid;
         JTATest.verboseOut(Thread.currentThread().getName() + ": " + "XAResource.start");
         xaRes.start(newXid, XAResource.TMNOFLAGS);

         Statement setupStatement = conn.createStatement();
         JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Create table " + TABLE + tableNumber);
         setupStatement.execute("CREATE TABLE " + TABLE + tableNumber +
                                " (ID INTEGER NOT NULL WITH " +
                                "DEFAULT, DESCRIPTION CHAR (256 ) NOT NULL WITH DEFAULT)");

         if (endTx) {
            JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                               "XAResource.end(XAResource.TMTIMEOUT)");
            try {
		if (isNTS) {
		    xaRes.end(newXid, XAResource.TMFAIL);
		} else { 
		    xaRes.end(newXid, 0x00000100 /* com.ibm.db2.jdbc.app.DB2StdXAResource.TMTIMEOUT */ );
		}
            }
            catch (XAException e) {
               if (e.errorCode != XAException.XA_RBROLLBACK) {
                  throw e;
               }
            }
            // Since ending the transaction with TMTIMEOUT should never
            // roll back the transaction (only mark it rollback only),
            // we'll try to commit the transaction (it should fail),
            // then we'll rollback the transaction.
            boolean failTest = false;  // @A1
            try {
               xaRes.commit(newXid, true);
               failTest = true;
            }
            catch (Exception e) {
               // OK.
            }

            //
            // Try to clean up
            // 
            xaRes.rollback(newXid);

            if (failTest) {
               throw new IllegalArgumentException("Committed a transaction that was marked rollback only with TMTIMEOUT");
            }


         }
         // Don't do anything more we want the connection, statement and
         // all that jazz to still be present, but the Tx rolled back.
      }
   }




   public void Var001() { // from ~kulack/JTA/jtatest/JTARecover.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
    if (checkJdbc20StdExt()) {
	StringBuffer sb = new StringBuffer(); 
      try {
        JTATest
            .verboseOut(Thread.currentThread().getName()
                + ": "
                + "TEST: Start a bunch of transactions, prepare them, then do a recovery scan. All will be rolled back");
        int repetitions = 10;
        boolean doOnlyRecovery = false;

        if (!doOnlyRecovery) {
          Recover txList[] = new Recover[repetitions];
          JTATest.verboseOut(Thread.currentThread().getName() + ": "
              + "Creating a bunch of prepare transactions");
          for (int i = 0; i < repetitions; ++i) {
            txList[i] = new Recover(system_, i);
          }
        }

        JTATest.verboseOut(Thread.currentThread().getName() + ": "
            + "Now, using XAResource.recover() to find all "
            + "transactions we just put into the in-doubt phase");

        XADataSource ds = newXADataSource();

        XAConnection xaConn = ds.getXAConnection();
        XAResource xaRes = xaConn.getXAResource();
        Connection conn = xaConn.getConnection();

        Xid xids[];
        JTATest.verboseOut(Thread.currentThread().getName() + ": "
            + "Calling XAResource.recover(XAResource.TMSTARTRSCAN) with conn "
            + conn);
        xids = xaRes.recover(XAResource.TMSTARTRSCAN);
        if (xids == null || xids.length == 0) {
          failed("Expected some transactions!");
          return;
        }

        boolean done = false;
        int count = 0;
        // It may be that the XIDs were heuristically rolled back or committed
        // (If this testcase is being called with a 'doOnlyRecovery' value of
        // true in order
        // to forget about existing transactions.
        boolean heuRollback = false;
        boolean heuCommit = false;
        boolean heuHaz = false;

        while (!done) {
          int processedThisTime = 0;
          JTATest.verboseOut(Thread.currentThread().getName() + ": "
              + "Scan found " + xids.length + " transactions");
          for (int i = 0; i < xids.length; ++i) {
            // Check to see if xid[i] belongs to this test

            if (xidFromThisTest(xids[i])) {
		sb.append("Count="+count+" Found xid "+xids[i]+"\n"); 
              count++;
              processedThisTime++;
              JTATest.verboseOut(Thread.currentThread().getName() + ": "
                  + "Rollback " + xids[i]);
              try {
                xaRes.rollback(xids[i]);
                JTATest.verboseOut("Rollback completed");
                heuRollback = false;
                heuCommit = false;
                heuHaz = false;
              } catch (XAException e) {
                JTATest.verboseOut("Rollback rc=" + e.errorCode);
                if (e.errorCode == XAException.XA_HEURCOM) {
                  JTATest.verboseOut("The exception was already committed");
                  heuCommit = true;

                } else if (e.errorCode == XAException.XA_HEURRB) {
                  JTATest.verboseOut("The exception was already rolled back");
                  heuRollback = true;
                }
                // 
                // In V5R4 for NTS, we are now seeing this..
                // This should be ok since we want to forgot it anyway
                // 
                else if (e.errorCode == XAException.XA_HEURHAZ) {
                  JTATest.verboseOut("The exception was already rolled back");
                  heuHaz = true;
                } else {
                  throw e;
                }
              }

              if (heuRollback || heuCommit || heuHaz) {
                JTATest.verboseOut(Thread.currentThread().getName() + ": "
                    + "Forget " + xids[i]);
                xaRes.forget(xids[i]);
              } else {
                JTATest.verboseOut("Skipped Forget");
              }
            } else {
              System.out.println("Warning:  Found extra transaction for XID = "+xids[i]);
            }
          }

          if (processedThisTime == 0) {
            done = true;
          } else {
            JTATest.verboseOut(Thread.currentThread().getName() + ": "
                + "Calling XAResource.recover(XAResource.TMNOFLAGS)");
            xids = xaRes.recover(XAResource.TMNOFLAGS);
            if (xids == null || xids.length == 0) {
              JTATest.verboseOut("End of scans xids = "+xids);
	      if (xids != null) JTATest.verboseOut("End of scans xids.length = "+xids.length);
              done = true;
            }
          }
        }

        // TODO: Other transactional work on the system using XA may cause
        // TODO: us to see those transactions.
        if (!doOnlyRecovery) {
          if (count != repetitions) {
            failed("Expected " + repetitions + " in doubt transactions, found "
                + count+"\n"+sb.toString() );
            return;
          }
        }

        JTATest.verboseOut(Thread.currentThread().getName() + ": " + "Done");
        assertCondition(true);
      } catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }
  }


   public void Var002() { // from ~kulack/JTA/jtatest/JTASelect.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: SELECT under a TX, then prepare (read only) commit");

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XADataSource");
            XADataSource          ds     = newXADataSource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");
            // Get the XAConnection.
            XAConnection            xaConn = ds.getXAConnection();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");
            // Get the real connection object
            XAResource              xaRes  = xaConn.getXAResource();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
            Connection              conn   = xaConn.getConnection();

            String sqlString = "SELECT * FROM QSYS2.SYSTABLES";
            JTATest.verboseOut(Thread.currentThread().getName() + ": prepare \"" + sqlString + "\"");
            PreparedStatement stmt = conn.prepareStatement(sqlString);


            // Generate a new transaction
            JTATestXid              newXid = new JTATestXid();

            JTATest.verboseOut(Thread.currentThread().getName() + ": Start a new transaction \"" + newXid.toString() + "\"");
            // Start a transaction for the stuff we're about to do
            xaRes.start(newXid, XAResource.TMNOFLAGS);

            try {
               JTATest.verboseOut(Thread.currentThread().getName() + ": Execute the query");
               ResultSet rs = stmt.executeQuery();

               int         i=0;
               while (rs.next()) {
                  JTATest.verboseOut(Thread.currentThread().getName() + ": Col1 = " + rs.getString(1));
                  ++i;
                  if (i >= 5) {
                     JTATest.verboseOut(Thread.currentThread().getName() + ": Finished Reading 5 rows");
                     break;
                  }
               }
               if (rs != null) {
                  JTATest.verboseOut(Thread.currentThread().getName() + ": close results");
                  rs.close();
               }

               int      rc;
               xaRes.end(newXid, XAResource.TMSUCCESS);
               JTATest.verboseOut(Thread.currentThread().getName() + ": Prepare the transaction");
               rc = xaRes.prepare(newXid);
               if (rc != XAResource.XA_RDONLY) {
                  JTATest.verboseOut(Thread.currentThread().getName() + ": Expected a read only result! rc=" + rc);
                  failed("Expected XA_RDONLY (" + XAResource.XA_RDONLY + "), got " + rc);
                  return;
               }
               JTATest.verboseOut(Thread.currentThread().getName() + ": The read only transaction is completed");
            }
            catch (Exception e) {
               failed(e, "Unexpected exception");
               return;
            }

            JTATest.verboseOut("Done");
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected exception");
         }
      }
   }

   public void Var003() {    // from ~kulack/JTA/jtatest/JTATimeout.java
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
         notApplicable("Tests XAResource.TMTIMEOUT");
         return;
      }
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                               "TEST: Start a bunch of transactions, then use XAResource.end(TMTIMEOUT). All should be rolled back");

            int      repetitions     = 10;
            boolean  alternate = false; // All should timeout.
            boolean  shouldEnd = true;


            Timeout     txList[] = new Timeout[repetitions];
            for (int i=0; i<repetitions; ++i) {
               txList[i] = new Timeout(system_, i, shouldEnd);
               if (alternate) {
                  shouldEnd = !shouldEnd;
               }
            }

            // the table(s) should not exist. Should have been rolled back
            boolean result = false;

            Connection c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
            Statement s = c.createStatement();
            for (int i=0; i<repetitions; ++i) {
               try {
                  s.executeQuery("SELECT * FROM " + txList[i].TABLE + i);
                  failed("Did not throw exception");
                  return;
               }
               catch (SQLException e) {
               }
            }
            result = true;
            s.close();
            c.close();


            if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
               JTATransInfo[] match = JTATest.getTransInfo();
               boolean isTransPresent = false;
               if (match == null) {
                  isTransPresent = false;
               }
               else {
                  for (int i = 0; (i < match.length) && (match[i] != null); i++) {
                     for (int j = 0; j < repetitions; j++)
                        isTransPresent = isTransPresent || xids[j].match(match[i]);
                  }
               }
               result = result && !isTransPresent;
            }
            assertCondition(result);
            JTATest.verboseOut(Thread.currentThread().getName() + ": " +"Done");
         }
         catch (Exception e) {
            failed(e, "Unexpected exception");
         }
      }
   }

   // Duplicate of Var001, but we try various formatIds here because of V4R5 bug
   // described by JAVA PTR 9923576.
   public void Var004() {             // from ~kulack/JTA/jtatest/JTARecover.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                               "TEST: Start a bunch of transactions with various formatIds, prepare them, then do a recovery scan. All will be rolled back");
            int      repetitions     = 10;
            boolean  doOnlyRecovery = false;
            int      formatId;
            int      fmtIdList[] = new int[] {
               // Everything other than -1 should be valid.
               // Be sure to get some combinations of negative/posetive numbers,
               // and number that contain individual bytes that are 'negative' i.e. 0x8y
               0x00818283, 1, 0x88000000, 0xFFFFFFF0, 0,
                     -2, -987653, 0x00008000, 0x00000081, 113577 /* 113577 used by websphere */
            };

            int      ntsFmtIdList[] = new int[] {
               // 
               // NTS requires all format ids > 0
               // 
               0x00818283, 1, 0x78000000, 0x7FFFFFF0, 0,
                     2, 987653, 0x00008000, 0x00000081, 113577 /* 113577 used by websphere */
            };

	    if (isNTS) {
		fmtIdList = ntsFmtIdList; 
	    } 

            if (!doOnlyRecovery) {
               Recover     txList[] = new Recover[repetitions];
               JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                  "Creating a bunch of prepare transactions");
               for (int i=0; i<repetitions; ++i) {
                  formatId = fmtIdList[i];
		  JTATest.verboseOut("Running with format "+formatId); 
                  txList[i] = new Recover(system_, i, formatId);
               }
            }


            JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                               "Now, using XAResource.recover() to find all " +
                               "transactions we just put into the in-doubt phase");


            XADataSource            ds     = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            Xid xids[];
            JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                               "Calling XAResource.recover(XAResource.TMSTARTRSCAN) for conn "+conn);
            xids = xaRes.recover(XAResource.TMSTARTRSCAN);
            
            if (xids == null || xids.length == 0) {
               failed("Expected some transactions!");
               return;
            }

            boolean  done  = false;
            int      count = 0;
            // It may be that the XIDs were heuristically rolled back or committed
            // (If this testcase is being called with a 'doOnlyRecovery' value of true in order
            //  to forget about existing transactions.
            boolean  heuRollback = false;
            boolean  heuCommit   = false;
            boolean  heuHaz      = false;

            while (!done) {
               int processedThisTime = 0; 
               JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                  "Scan found " + xids.length + " transactions");
               for (int i=0; i<xids.length; ++i) {
                 if (xidFromThisTest(xids[i])) {
                   count++;
                   processedThisTime++; 
                  JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                     "Rollback " + xids[i]);
                  try {
                     xaRes.rollback(xids[i]);
                     JTATest.verboseOut("Rollback completed");
                     heuRollback = false;
                     heuCommit   = false;
		     heuHaz      = false; 
                  }
                  catch (XAException e) {
                     JTATest.verboseOut("Rollback rc=" + e.errorCode);
                     if (e.errorCode == XAException.XA_HEURCOM) {
                        JTATest.verboseOut("The exception was already committed");
                        heuCommit = true;

                     }
                     else if (e.errorCode == XAException.XA_HEURRB) {
                        JTATest.verboseOut("The exception was already rolled back");
                        heuRollback = true;
                     }
		     // 
		     // In V5R4 for NTS, we are now seeing this..
		     // This should be ok since we want to forgot it anyway
		     // 
                     else if (e.errorCode == XAException.XA_HEURHAZ) {
                        JTATest.verboseOut("The exception was already rolled back");
                        heuHaz = true;
                     }

                     else {
                        throw e;
                     }
                  }

                  if (heuRollback || heuCommit || heuHaz) {
                     JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                        "Forget " + xids[i]);
                     xaRes.forget(xids[i]);
                  }
                  else {
                     JTATest.verboseOut("Skipped Forget");
                  }
               }
               }
               if (processedThisTime == 0) {
                 done = true;
               } else {

            JTATest.verboseOut(Thread.currentThread().getName() + ": "
                + "Calling XAResource.recover(XAResource.TMNOFLAGS)");
            xids = xaRes.recover(XAResource.TMNOFLAGS);
            if (xids == null || xids.length == 0) {
              JTATest.verboseOut("End of scans");
              done = true;
            }
          }
            }

            // TODO: Other transactional work on the system using XA may cause
            // TODO: us to see those transactions.
            if (!doOnlyRecovery) {
               if (count != repetitions) {
                  failed("Expected " + repetitions + " in doubt transactions, found " + count);
                  return;
               }
            }

            JTATest.verboseOut(Thread.currentThread().getName() + ": " +"Done");
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }

  //
  // Duplicate of Var001 to find problems with recover(XAResource.TMSTARTRSCAN+XAResource.TMENDRSCAN)
  // 
  public void Var005() {             // from ~kulack/JTA/jtatest/JTARecover.java
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                               "TEST: Start a bunch of transactions, prepare them, then do a recovery scan. All will be rolled back");
            int      repetitions     = 10;
            boolean  doOnlyRecovery = false;

            if (!doOnlyRecovery) {
               Recover     txList[] = new Recover[repetitions];
               JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                  "Creating a bunch of prepare transactions");
               for (int i=0; i<repetitions; ++i) {
                  txList[i] = new Recover(system_, i);
               }
            }


            JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                               "Now, using XAResource.recover() to find all " +
                               "transactions we just put into the in-doubt phase");


            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            Xid xids[];
            JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                               "Calling XAResource.recover(XAResource.TMSTARTRSCAN+XAResource.TMENDRSCAN) for conn "+conn);
            xids = xaRes.recover(XAResource.TMSTARTRSCAN+XAResource.TMENDRSCAN);
            if (xids == null || xids.length == 0) {
               failed("Expected some transactions!");
               return;
            }

            boolean  done  = false;
            int      count = 0;
            // It may be that the XIDs were heuristically rolled back or committed
            // (If this testcase is being called with a 'doOnlyRecovery' value of true in order
            //  to forget about existing transactions.
            boolean  heuRollback = false;
            boolean  heuCommit   = false;
            boolean  heuHaz      = false;

            while (!done) {
              
               int processedThisTime = 0; 
               JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                  "Scan found " + xids.length + " transactions");
               for (int i=0; i<xids.length; ++i) {
                 if (xidFromThisTest(xids[i])) {
                   count++;
                   processedThisTime++; 

                  JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                     "Rollback " + xids[i]);
                  try {
                     xaRes.rollback(xids[i]);
                     JTATest.verboseOut("Rollback completed");
                     heuRollback = false;
                     heuCommit   = false;
		     heuHaz      = false; 
                  }
                  catch (XAException e) {
                     JTATest.verboseOut("Rollback rc=" + e.errorCode);
                     if (e.errorCode == XAException.XA_HEURCOM) {
                        JTATest.verboseOut("The exception was already committed");
                        heuCommit = true;

                     }
                     else if (e.errorCode == XAException.XA_HEURRB) {
                        JTATest.verboseOut("The exception was already rolled back");
                        heuRollback = true;
                     }
                     else if (e.errorCode == XAException.XA_HEURHAZ) {
                        JTATest.verboseOut("The exception was already rolled back");
                        heuHaz = true;
                     }
                     else {
                        throw e;
                     }
                  }

                  if (heuRollback || heuCommit || heuHaz) {
                     JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                        "Forget " + xids[i]);
                     xaRes.forget(xids[i]);
                  }
                  else {
                     JTATest.verboseOut("Skipped Forget");
                  }
               }

               JTATest.verboseOut(Thread.currentThread().getName() + ": " +
                                  "Calling XAResource.recover(XAResource.TMNOFLAGS) processedThisTime="+processedThisTime);

	       //
	       // End of scan...
	       //
               done = true;
             }
            }
            // TODO: Other transactional work on the system using XA may cause
            // TODO: us to see those transactions.
            if (!doOnlyRecovery) {
               if (count != repetitions) {
                  failed("Expected " + repetitions + " in doubt transactions, found " + count);
                  return;
               }
            }

            JTATest.verboseOut(Thread.currentThread().getName() + ": " +"Done");
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



}

