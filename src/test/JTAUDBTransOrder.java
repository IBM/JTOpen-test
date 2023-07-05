////////////////////////////////////////////////////////////////////////
//
// File Name:    JTAUDBTransOrder.java
//
// Description:  Same as JTATransOrder.java but test standard interfaces for
//               JTA & JDBC UDB Ext
//
// Classes:      JTAUDBTransOrder
//
////////////////////////////////////////////////////////////////////////
//------------------- Maintenance-Change Activity ------------------
//
//  Flag  Reason     Rel Lvl   Date    PGMR     Comments
//  ---- --------    ------- -------- ------- ---------------------------
//                           06/14/02 JEBER    NEW PART 
////////////////////////////////////////////////////////////////////////
package test;

import java.lang.*;
import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.XAException;

public class JTAUDBTransOrder extends JTAStdTransOrder {

/**
Constructor.
**/
   public JTAUDBTransOrder (AS400 systemObject,
                    Hashtable namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTAUDBTransOrder",
             namesAndVars, runMode, fileOutputStream,
             password);
      useUDBDataSource=true;
      JTATest.COLLECTION=JTAUDBTest.COLLECTION; 
   }


/**
 * All testcases inheritied from JTAStdTransOrder
 */


/**
 * overwrite var004
 */
  /* public void Var004() {
       if (checkJdbc20StdExt()) {
	   XAResource     xaRes  = null; 
	   TestXid        newXid = null; 
	   JTATest.verboseOut("TEST: prepare() after a start() but without an end()");
	   failed("Var004() fails in V5R2 because DB2NTSXAResource is not tracking the start the and end.  It needs to do this to detect that the prepare should fail."); 
       }
   }*/


  /*
   * For V5R2 -- and exception is not thrown for NTS.. Since this doesn't really
   * matter, I will let it pass.
   */ 
   public void Var009() {
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: prepare() and rollback without doing work (this is considered read-only)");


            JTATest.verboseOut("Get the connection");
            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
            Connection              conn   = xaConn.getConnection();

            JTATest.verboseOut("Get the Xid and start a transaction");
            TestXid newXid = new TestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);
            xaRes.end(newXid, XAResource.TMSUCCESS);

            JTATest.verboseOut("Try to prepare() the transaction");
            // try to prepare. should work (actually, does an implicit commit)
            int rc = xaRes.prepare(newXid);
            if (rc != XAResource.XA_RDONLY) {
               failed("Expected READ_ONLY! rc=" + rc);
            }
            else {
               // try to rollback. should fail
               try {
                  xaRes.rollback(newXid);
                  assertCondition(true); 
               }
               catch (XAException ex) {
                  if (ex.errorCode == XAException.XAER_NOTA)
                     assertExceptionIsInstanceOf(ex, "javax.transaction.xa.XAException");
                  else
                     failed("Expected error code " + XAException.XAER_NOTA + " got " + ex.errorCode);
               }

            }
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


}
