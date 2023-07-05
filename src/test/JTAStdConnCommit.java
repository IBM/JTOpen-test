////////////////////////////////////////////////////////////////////////
//
// File Name:    JTAStdConnCommit.java
//
// Description:  Same as JTAConnCommit.java but test standard interfaces for
//               JTA & JDBC Std Ext
//
// Classes:      JTAStdConnCommit
//
////////////////////////////////////////////////////////////////////////
//------------------- Maintenance-Change Activity ------------------
//
//  Flag  Reason     Rel Lvl   Date    PGMR     Comments
//  ---- --------    ------- -------- -------  ---------------------------
//                   V5R1M0F 05/16/00 stimmer  Created
//  $A1              v5r1m0  10/12/00 cnock    Toolbox can only throw SQLExceptions
//                                             from the Connection class.
//  $B1              v5r1m0  11/01/00 v2kea447 Cleaned up transaction in var006 as
//                                             it wasn't cleaned up previously.
//  $C1              v5r2m0  11/04/02 jeber    Fixed VAR006 and added comments 
//  $C2              v5r2m0  12/27/02 jeber    Added test.. VAR007
//                   v5r4m0t 01/18/06 pauldev  Move isNTS to JTATestcase
//
////////////////////////////////////////////////////////////////////////
package test;


import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;


import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.XAException;

public class JTAStdConnCommit extends JTATestcase {

   protected Connection c;
   //protected boolean isNTS = false;  @PDD move to JTATestcase


/**
Constructor.
**/
   public JTAStdConnCommit (AS400 systemObject,
                            Hashtable namesAndVars,
                            int runMode,
                            FileOutputStream fileOutputStream,
                            
                            String password) {
      super (systemObject, "JTAStdConnCommit",
             namesAndVars, runMode, fileOutputStream,
             password);
   }

   public JTAStdConnCommit (AS400 systemObject,
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
      if (checkJdbc20StdExt()) {
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
       /* Cleanup closed connections */ 
       System.gc(); 
      if (checkJdbc20StdExt()) {
      }
      unlockSystem(); 
   }

   // Test changing the commit property of a connection during
   // various stages of a transaction

   public void Var001() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         XAConnection            xaConn = null;
         try {
            JTATest.verboseOut("TEST: Change a connection's auto-commit property in the middle of a transaction");


            String table = JTATest.COLLECTION + ".CCOMMVAR01";

            XADataSource          ds    = newXADataSource();
            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            // Generate a new transaction
            TestXid              Xid = new TestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);

            Statement s = conn.createStatement();

            try {
               s.execute("DROP TABLE " + table);
            }
            catch (Exception e) {
            }
            s.execute("CREATE TABLE " + table + " (COLA INT)");
            try {
               conn.setAutoCommit(true); // default is false
               failed("Did not throw exception");
            }
            catch (Exception e) {
               JTATest.verboseOut(e.getMessage());
               assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }

         }
         catch (Exception e) {
            failed(e, "Unexpected exception");
         }
         finally {
            if (xaConn != null) {
               try {
                  xaConn.close();
               }
               catch (Exception e) {
               }
            }
         }
      }
   }

   public void Var002() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         XAConnection            xaConn = null;
         try {
            JTATest.verboseOut("TEST: Change a connection's auto-commit property in a local transaction");

            String table = JTATest.COLLECTION + ".CCOMMVAR02";
            XADataSource          ds    = newXADataSource();
            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            // XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            Statement s = conn.createStatement();

            try {
               s.execute("DROP TABLE " + table);
            }
            catch (Exception e) {
            }
            s.execute("CREATE TABLE " + table + " (COLA INT)");

            // since this is a local transaction, it should behave like normal JDBC.
            // [though this is not entirely true since this connection has
            // auto commit set to false (by default) because that is the default
            // in which the connection from a XADataSource is created]
            // normal JDBC means that we should be able to set the auto-commit value.

            conn.setAutoCommit(true); // default is false
            conn.setAutoCommit(false);
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
         finally {
            if (xaConn != null) {
               try {
                  xaConn.close();
               }
               catch (Exception e) {
               }

            }
         }
      }
   }


   public void Var003() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         XAConnection            xaConn = null;
         try {
            JTATest.verboseOut("TEST: Set a connection's auto-commit property in the middle of a transaction");


            String table = JTATest.COLLECTION + ".CCOMMVAR03";

            XADataSource          ds    = newXADataSource();
            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            // Generate a new transaction
            TestXid              Xid = new TestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);

            Statement s = conn.createStatement();

            try {
               s.execute("DROP TABLE " + table);
            }
            catch (Exception e) {
            }
            JTATest.verboseOut("CREATE TABLE " + table + " (COLA INT)");
            s.execute("CREATE TABLE " + table + " (COLA INT)");

            conn.setAutoCommit(false); // default is false
            xaRes.end(Xid, XAResource.TMSUCCESS);
            xaRes.rollback(Xid);
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected exception");
         }
         finally {
            if (xaConn != null) {
               try {
                  xaConn.close();
               }
               catch (Exception e) {
               }

            }
         }
      }
   }


   public void Var004() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         XAConnection            xaConn = null;
         try {
            JTATest.verboseOut("TEST: Set a xa connection's auto-commit property to false and then start a transaction");

            String table = JTATest.COLLECTION + ".CCOMMVAR04";

            XADataSource          ds    = newXADataSource();
            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();
            conn.setAutoCommit(false); // default is false

            // Generate a new transaction
            TestXid              Xid = new TestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);

            Statement s = conn.createStatement();

            try {
               s.execute("DROP TABLE " + table);
            }
            catch (Exception e) {
            }
            JTATest.verboseOut("CREATE TABLE " + table + " (COLA INT)");
            s.execute("CREATE TABLE " + table + " (COLA INT)");

            xaRes.end(Xid, XAResource.TMSUCCESS);
            xaRes.rollback(Xid);
            assertCondition(true);
         }
         catch (Exception e) {
            failed(e, "Unexpected exception");
         }
         finally {
            if (xaConn != null) {
               try {
                  xaConn.close();
               }
               catch (Exception e) {
               }

            }
         }
      }
   }


   public void Var005() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         XAConnection            xaConn = null;
         try {
            JTATest.verboseOut("TEST: Set a xa connection's auto-commit property to true and then start a transaction");
            JTATest.verboseOut("TEST: Do work. Do xaRes.rollback(). Make sure it took effect");

            String table = JTATest.COLLECTION + ".CCOMMVAR05";

            XADataSource          ds    = newXADataSource();
            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();
            conn.setAutoCommit(true); // default is false

            // Generate a new transaction
            TestXid              Xid = new TestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);

            Statement s = conn.createStatement();

            try {
               s.execute("DROP TABLE " + table);
            }
            catch (Exception e) {
            }
            JTATest.verboseOut("auto commit=" + conn.getAutoCommit());
            JTATest.verboseOut("CREATE TABLE " + table + " (COLA INT)");
            s.execute("CREATE TABLE " + table + " (COLA INT)");

            xaRes.end(Xid, XAResource.TMSUCCESS);
            xaRes.rollback(Xid);
            JTATest.verboseOut("auto commit=" + conn.getAutoCommit());

            // now check to make sure that the rollback worked
            try {
               s.execute("DROP TABLE " + table);
               failed("Did not throw exception");
            }
            catch (Exception e) {
               String msg = e.getMessage();
               JTATest.verboseOut(msg);
               assertExceptionIsInstanceOf(e, "java.sql.SQLException");

	       //
               // Execute an explicit commit on the connection to prevent
               // problem documented in issue 20318
               //
	       conn.commit(); 

            }

         }
         catch (Exception e) {
            failed(e, "Unexpected exception");
         }
         finally {
            if (xaConn != null) {
               try {
                  xaConn.close();
               }
               catch (Exception e) {
               }

            }
         }
      }
   }


   public void Var006() {
      // This one reflects the native drivers problem where a connection is associated
      // with a transaction from the time start() is called until the time commit() is done.
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         XAConnection            xaConn = null;
	 XAResource              xaRes = null;
	 TestXid              Xid = null; 
         try {
            JTATest.verboseOut("TEST: Set a xa connection's auto-commit property to true and then start a transaction");
            JTATest.verboseOut("TEST: Do work. Do conn.rollback(). it should fail");

            String table = JTATest.COLLECTION + ".CCOMMV6A";
            String table2 = JTATest.COLLECTION + ".CCOMMV6B";

            XADataSource          ds    = newXADataSource();
            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            xaRes  = xaConn.getXAResource();


            Connection              conn   = xaConn.getConnection();



	    //
            // BEGIN @C1M 
            // Make sure the tables are gone before using AUTOCOMMIT.
            // Note... If we don't this after we turn on autocommit,
            // it doesn't work right for the old XA model.
            //
            // The problem is that JDBC expect a commit to be done after
            // every statement (i.e. and so it doesn't do the explict
            // xa_end).  However, if the statement fails, the commit
            // never happens, i.e. the xa_end never happens and
            // tn becomes very confused. 
            // 


            Statement s = conn.createStatement();
	    try {
		s.execute("DROP TABLE " + table);
	    } catch (Exception e) {}
	    try {
		s.execute("DROP TABLE " + table2);
	    } catch (Exception e) {}

            //
            // END @C1M
            // 

            conn.setAutoCommit(true); // default is false

            // Generate a new transaction
            Xid = new TestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);

            JTATest.verboseOut("auto commit=" + conn.getAutoCommit());
            JTATest.verboseOut("CREATE TABLE " + table + " (COLA INT)");
            s.execute("CREATE TABLE " + table + " (COLA INT)");

            xaRes.end(Xid, XAResource.TMSUCCESS);
            JTATest.verboseOut("auto commit=" + conn.getAutoCommit());

            try {
               // Do work on ended transaction. This would work
               // in a model that fully supported JTA.
               // s.execute("INSERT INTO " + table + " VALUES(1)");
	       s.execute("CREATE TABLE " + table2 + " (COLA INT)");
	       if (isNTS) {
		   succeeded(); 
	       } else { 
		   failed("Did not throw exception");
	       }
            }
            catch (Exception ex) {
		if (isNTS) {
		    failed(ex, "Unexpected exception");
		} else { 
		    if (!(ex instanceof XAException)) {
			failed(ex, "Unexpected exception");
		    } else { 
			XAException xaEx = (XAException)ex;
			if (xaEx.errorCode != XAException.XAER_PROTO) {
			    failed(ex, "Expected XAER_PROTO failure");
			}  else { 
			    succeeded();
			}
		    }
		}
            }
         }
         catch (Exception e) {
            failed(e, "Unexpected exception");
         }
         finally {
            if (xaRes != null) {
               try {
		   xaRes.rollback(Xid);
               }
               catch (Exception e) {
		   System.out.println("Cleanup Rollback Error");
		   e.printStackTrace(); 
               }

	    } 
            if (xaConn != null) {
               try {
                  xaConn.close();
               }
               catch (Exception e) {
		   System.out.println("Cleanup Connection Error");
		   e.printStackTrace(); 
               }
            }
         }
      }
   }


   public void Var007() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         XAConnection            xaConn = null;
	 XAResource              xaRes = null;
	 TestXid              Xid  = null; 
         try {

            JTATest.verboseOut("TEST: Change a connection's auto-commit property after ending a transaction");

            String table = JTATest.COLLECTION + ".CCOMMVAR07";

            XADataSource          ds    = newXADataSource();
            xaConn = ds.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
                     xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            // Generate a new transaction
            Xid = new TestXid();

            // start the transaction
            xaRes.start(Xid, XAResource.TMNOFLAGS);

            Statement s = conn.createStatement();

            try {
               s.execute("DROP TABLE " + table);
            }
            catch (Exception e) {
            }
            s.execute("CREATE TABLE " + table + " (COLA INT)");
            xaRes.end(Xid, XAResource.TMSUCCESS);

            try {
               conn.setAutoCommit(true); // default is false
	       if (isNTS) {
		   succeeded();
	       } else { 
		   failed("Did not throw exception");
	       }
            }
            catch (Exception ex) {
               if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {                  // @A1A
		   if (isNTS) {
		       failed(ex, "Unexpected exception");                      
		   } else { 
		       if (!(ex instanceof SQLException)) {                           // @A1A
			   failed(ex, "Unexpected exception");                        // @A1A
		       } else {                                                      // @A1A
			   succeeded();                                              // @A1A
		       }
		   }
               } else {                                                             // @A1A
                  if (!(ex instanceof XAException)) {
                     failed(ex, "Unexpected exception");
                     return;
		  } else { 
		      XAException xaEx = (XAException)ex;
		      if (xaEx.errorCode != XAException.XAER_PROTO) {
			  failed(ex, "Expected XAER_PROTO failure");
		      } else { 
			  succeeded();
		      }
		  }
               }                                                                   // @A1A
            }
            JTATest.verboseOut("rollback");

         }
         catch (Exception e) {
            failed(e, "Unexpected exception");
         }
         finally {
            if (xaRes != null) {
               try {
		   xaRes.rollback(Xid);
               }
               catch (Exception e) {
               }
            }

            if (xaConn != null) {
               try {
                  xaConn.close();
               }
               catch (Exception e) {
               }
            }
         }
      }
   }


   public void Var008() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
	 String xaModel = "original"; 
         XAConnection            xaConn = null;
	 XAResource              xaRes = null;
	 TestXid              Xid  = null; 
         try {

            JTATest.verboseOut("TEST: Using a connection containing auto commit and original model");

            String table1 = JTATest.COLLECTION + ".CCOMMVAR8A";
            String table2 = JTATest.COLLECTION + ".CCOMMVAR8B";
            String table3 = JTATest.COLLECTION + ".CCOMMVAR8C";

            XADataSource          ds    = newXADataSource();
	    if (JDReflectionUtil.instanceOf(ds,"com.ibm.db2.jdbc.app.DB2StdXADataSource")) {
	    	JDReflectionUtil.callMethod_V(ds, "setAutoCommit", true); 
	    	JDReflectionUtil.callMethod_V(ds, "setTransactionIsolationLevel", "serializable");
	    	JDReflectionUtil.callMethod_V(ds, "setXaModel", xaModel); 
		xaConn =  ds.getXAConnection();
		xaRes  = xaConn.getXAResource();

		Connection conn = xaConn.getConnection();

		JTATest.verboseOut("Start, work, end, commit"); 
		TestXid xid = new TestXid();
		JTATest.verboseOut("....start"); 
		xaRes.start(xid, XAResource.TMNOFLAGS); 
		Statement   stmt = conn.createStatement();
		try {
		    JTATest.verboseOut("....work"); 
		    stmt.executeUpdate("drop table "+table1); 
		} catch (Exception e) {}
		try {
		    JTATest.verboseOut("....work"); 
		    stmt.executeUpdate("drop table "+table2); 
		} catch (Exception e) {}

		try {
		    JTATest.verboseOut("....work"); 
		    stmt.executeUpdate("drop table "+table3); 
		} catch (Exception e) {}

		JTATest.verboseOut("....end"); 
		xaRes.end(xid, XAResource.TMSUCCESS);
		JTATest.verboseOut("....commit"); 
		xaRes.commit(xid, true);

		JTATest.verboseOut("Start, work, end, rollback"); 
		xid = new TestXid();  
		xaRes.start(xid, XAResource.TMNOFLAGS); 
		stmt.executeUpdate("create table "+table1+"(x int);");
		xaRes.end(xid, XAResource.TMSUCCESS);
		xaRes.rollback(xid);


		JTATest.verboseOut("Start, work, end, commit"); 
		xid = new TestXid();  
		xaRes.start(xid, XAResource.TMNOFLAGS); 
		stmt.executeUpdate("create table "+table1+"(x int);");
		xaRes.end(xid, XAResource.TMSUCCESS);
		xaRes.commit(xid, true);


		JTATest.verboseOut("Do some local work, that should be auto commited");
		stmt.executeUpdate("create table "+table2+"(x int);");


		JTATest.verboseOut("Start, work, end, commit"); 
		xid = new TestXid();  
		xaRes.start(xid, XAResource.TMNOFLAGS); 
		stmt.executeUpdate("create table "+table3+"(x int);");
		xaRes.end(xid, XAResource.TMSUCCESS);
		xaRes.commit(xid, true);

		xaRes = null;

		JTATest.verboseOut("Verify Success by dropping tables ");
		stmt.executeUpdate("drop table "+table1);
		stmt.executeUpdate("drop table "+table2);
		stmt.executeUpdate("drop table "+table3); 

		JTATest.verboseOut("DONE");
                assertCondition(true); 
		
	    } else {  /* ds instance of DB2StdXADataSource */
		notApplicable("V5R2 Native variation with autocommit datasource"); 
	    } /* ds instance of DB2StdXADataSource */
         } catch (Exception e) {
            failed(e, "Unexpected exception -> new native V5R2 variation (12/26/02) ");
         } finally {
            if (xaRes != null) {
               try {
		   JTATest.verboseOut("rollback");
		   xaRes.rollback(Xid);
               }
               catch (Exception e) {
               }
            }

            if (xaConn != null) {
               try {
		  JTATest.verboseOut("close");
                  xaConn.close();
               }
               catch (Exception e) {
               }
            }
	 } /* try */ 
      } /* checkJDBC20StdExt() */ 
   }


   public void Var009() {
       String description = "TEST:  Using a connection containing auto commit and standard model.  The jdbc spec states the following:  'If a connection has auto-commit mode already enabled at the time it joins a global transaction, the attribute will be ignored. The auto-commit behavior will resume when the connection returns to local transaction mode.'";
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }

       if (getDriver()  == JDTestDriver.DRIVER_NATIVE &&
	   (getRelease() >= JDTestDriver.RELEASE_V5R4M0  &&
	    getRelease() <= JDTestDriver.RELEASE_V6R1M0)) {
	   notApplicable("Not working in V5R4/V6R1 native code");
	   return; 
       } 

      if (checkJdbc20StdExt()) {
	 String xaModel = "standard"; 
         XAConnection            xaConn = null;
	 XAResource              xaRes = null;
	 TestXid              Xid  = null; 
         try {

            JTATest.verboseOut("TEST: Using a connection containing auto commit and standard model");

            String table1 = JTATest.COLLECTION + ".CCOMMVAR8A";
            String table2 = JTATest.COLLECTION + ".CCOMMVAR8B";
            String table3 = JTATest.COLLECTION + ".CCOMMVAR8C";

            XADataSource          ds    = newXADataSource();
	    if (JDReflectionUtil.instanceOf(ds,"com.ibm.db2.jdbc.app.DB2StdXADataSource")) {
    	JDReflectionUtil.callMethod_V(ds, "setAutoCommit", true); 
    	JDReflectionUtil.callMethod_V(ds, "setTransactionIsolationLevel", "serializable");
    	JDReflectionUtil.callMethod_V(ds, "setXaModel", xaModel); 

		xaConn =  ds.getXAConnection();
		xaRes  = xaConn.getXAResource();

		Connection conn = xaConn.getConnection();

		JTATest.verboseOut("Start, work, end, commit"); 
		TestXid xid = new TestXid();
		JTATest.verboseOut("....start"); 
		xaRes.start(xid, XAResource.TMNOFLAGS); 
		Statement   stmt = conn.createStatement();
		try {
		    JTATest.verboseOut("....work"); 
		    stmt.executeUpdate("drop table "+table1); 
		} catch (Exception e) {}
		try {
		    JTATest.verboseOut("....work"); 
		    stmt.executeUpdate("drop table "+table2); 
		} catch (Exception e) {}

		try {
		    JTATest.verboseOut("....work"); 
		    stmt.executeUpdate("drop table "+table3); 
		} catch (Exception e) {}

		JTATest.verboseOut("....end"); 
		xaRes.end(xid, XAResource.TMSUCCESS);
		JTATest.verboseOut("....commit"); 
		xaRes.commit(xid, true);

		JTATest.verboseOut("Start, work, end, rollback"); 
		xid = new TestXid();  
		xaRes.start(xid, XAResource.TMNOFLAGS); 
		stmt.executeUpdate("create table "+table1+"(x int);");
		xaRes.end(xid, XAResource.TMSUCCESS);
		xaRes.rollback(xid);


		JTATest.verboseOut("Start, work, end, commit"); 
		xid = new TestXid();  
		xaRes.start(xid, XAResource.TMNOFLAGS); 
		stmt.executeUpdate("create table "+table1+"(x int);");
		xaRes.end(xid, XAResource.TMSUCCESS);
		xaRes.commit(xid, true);


		JTATest.verboseOut("Do some local work, that should be auto commited");
		stmt.executeUpdate("create table "+table2+"(x int);");


		JTATest.verboseOut("Start, work, end, commit"); 
		xid = new TestXid();  
		xaRes.start(xid, XAResource.TMNOFLAGS); 
		stmt.executeUpdate("create table "+table3+"(x int);");
		xaRes.end(xid, XAResource.TMSUCCESS);
		xaRes.commit(xid, true);

		JTATest.verboseOut("Verify Success by dropping tables ");
		stmt.executeUpdate("drop table "+table1);
		stmt.executeUpdate("drop table "+table2);
		stmt.executeUpdate("drop table "+table3); 

		JTATest.verboseOut("DONE");
                assertCondition(true, description); 
		
	    } else {  /* ds instance of DB2StdXADataSource */
		notApplicable("V5R2 Native variation with autocommit datasource "); 
	    } /* ds instance of DB2StdXADataSource */
         } catch (Exception e) {
            failed(e, "Unexpected exception -> new native V5R2 variation (12/27/02) "+description );
         } finally {
            if (xaRes != null) {
               try {
		   JTATest.verboseOut("rollback");
		   xaRes.rollback(Xid);
               }
               catch (Exception e) {
               }
            }

            if (xaConn != null) {
               try {
		  JTATest.verboseOut("close");
                  xaConn.close();
               }
               catch (Exception e) {
               }
            }
	 } /* try */ 
      } /* checkJDBC20StdExt() */ 
   }



}
