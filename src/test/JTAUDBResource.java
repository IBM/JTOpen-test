////////////////////////////////////////////////////////////////////////
//
// File Name:    JTAUDBResource.java
//
// Description:  Same as JTAResource.java but test standard interfaces for
//               JTA & JDBC UDB Ext
//
// Classes:      JTAUDBResource
//
////////////////////////////////////////////////////////////////////////
//------------------- Maintenance-Change Activity ------------------
//
//  Flag  Reason     Rel Lvl   Date    PGMR     Comments
//  ---- --------    ------- -------- ------- ---------------------------
//                           06/14/02 JEBER    NEW PART
//                          2005/19/19 JEBER   Added VAR 053 (like var 17)
//                                             for PTR 9B12364
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
import javax.transaction.xa.Xid;

public class JTAUDBResource extends JTAResource {

/**
Constructor.
**/
   public JTAUDBResource (AS400 systemObject,
                    Hashtable namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password) {
      super (systemObject, "JTAUDBResource",
             namesAndVars, runMode, fileOutputStream,
             password);
      useUDBDataSource=true;
      JTATest.COLLECTION=JTAUDBTest.COLLECTION;
      isNTS = true;             /* Fixed 06/27/2008 */ 
   }


/**
 * All testcases inheritied from JTAStdResource
 */


   public void Var011() {
       if (getDriver()  == JDTestDriver.DRIVER_NATIVE &&
	   getRelease() == JDTestDriver.RELEASE_V5R4M0) {
	   notApplicable("Not working in V5R4 native code");
	   return; 
       } 
       super.Var011(); 
   } 

/**
end() - Pass TMSUSPEND.
**/
    public void Var017()
    {
        if (checkJdbc20StdExt()) {
            try {
                XADataSource xads = newXADataSource();
                XAConnection xac = xads.getXAConnection();
                XAResource xar = xac.getXAResource();
                Connection c = xac.getConnection();

                Xid xid = new TestXid();
		        globalIds[17]=xid.getGlobalTransactionId();
                xar.start(xid, XAResource.TMNOFLAGS);
                Statement s = c.createStatement();
                s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-234077)");
                int errorCode = 0;
                try {
                    xar.end(xid, XAResource.TMSUSPEND);
		    xar.start(xid, XAResource.TMRESUME);
                }
                catch (XAException e) {
                    errorCode = e.errorCode;
                }
                xar.end(xid, XAResource.TMSUCCESS);
                xar.rollback(xid);
                xac.close();

                assertCondition((errorCode == 0) && (countRows(-234077) == 0));

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
end() - Pass TMSUSPEND.
**/
    public void Var053()
    {
	// Don't run in V5R3 or earlier.  When the
        // XAResource.end(SUSPEND) is followed by
        // XAResource.end(END), the native JDBC driver has
        // a bug that prevents that xa_open(END) from occurring
        // The transaction is then in a weird state and does
        // not get cleaned up when the transaction ends.
	//
	if (getRelease() >=  JDTestDriver.RELEASE_V5R4M0) { 
	    if (checkJdbc20StdExt()) {
		try {
		    XADataSource xads = newXADataSource();
		    XAConnection xac = xads.getXAConnection();
		    XAResource xar = xac.getXAResource();
		    Connection c = xac.getConnection();

		    Xid xid = new TestXid();
		    globalIds[17]=xid.getGlobalTransactionId();
		    xar.start(xid, XAResource.TMNOFLAGS);
		    Statement s = c.createStatement();
		    s.executeUpdate("INSERT INTO " + table_ + " (COL1) VALUES(-234077)");
		    int errorCode = 0;
		    try {
			xar.end(xid, XAResource.TMSUSPEND);
		    // Shouldn't have to call resume
		    // xar.start(xid, XAResource.TMRESUME);
		    }
		    catch (XAException e) {
			errorCode = e.errorCode;
		    }
		    xar.end(xid, XAResource.TMSUCCESS);
		    xar.rollback(xid);
		    xac.close();

		    assertCondition((errorCode == 0) && (countRows(-234077) == 0));

		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	} else {
	    notApplicable("V5R4 or later variation"); 
	} 
    }



}
