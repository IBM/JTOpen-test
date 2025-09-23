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
// File Name:    JTATransaction.java
//
// Description:  XA loosely coupled transaction testing
//
// Classes:      JTAStdBasic
//
////////////////////////////////////////////////////////////////////////
package test.JTA;

import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;
import com.ibm.as400.access.AS400JDBCXADataSource;

import test.JDReflectionUtil;
import test.JTATest;
import test.LCTXid;
import test.PasswordVault;
import test.JD.JDTestUtilities;

import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

public class JTATransaction extends JTATestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JTATransaction";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JTAStdTest.main(newArgs); 
   }

    private String insStr = "JTATransaction";

    private String basTbl = JTATest.COLLECTION + ".TRANS00";

    private Connection c;
    


    //protected boolean isNTS = false; @PDD move to JTATestcase

    /**
     Constructor.
     **/
    public JTATransaction(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,  String password) {
        super(systemObject, "JTATransaction", namesAndVars, runMode, fileOutputStream, password);
    }

    public JTATransaction(AS400 systemObject, String testname, Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,  String password) {
        super(systemObject, testname, namesAndVars, runMode, fileOutputStream, password);
    }

    /**
     Performs setup needed before running variations.
     
     @exception Exception If an exception occurs.
     **/
    protected void setup() throws Exception {
	lockSystem("JTATEST",600);
        //basTbl = JTATest.COLLECTION + ".TRANS002"; //ped chage back to TRANSTAB
	// Reset basTbl since JTATest.COLLECTION may have changed. 
        basTbl = JTATest.COLLECTION + ".TRANS00";
        if (isJdbc20StdExt()) {
            JTATest.verboseOut(baseURL_);
            c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
	    isIasp = JDTestUtilities.isIasp(c); 
            Statement s = c.createStatement();
            s.execute("CREATE TABLE " + basTbl + " (COL1 CHAR (20 ), COL2 CHAR (20 ) )");
            s.close();
        }
    }

    /**
     Performs cleanup needed after running variations.
     
     @exception Exception If an exception occurs.
     **/
    protected void cleanup() throws Exception {
        if (isJdbc20StdExt()) {
            Statement s = c.createStatement();
            s.execute("DROP TABLE " + basTbl);
            s.close();
            c.close();
        }
	unlockSystem(); 
    }
 
    /* This variation test Loosely Coupled XA transactions with option on.
     * 1.  insert test row into table and commit
     * 2.  turn setXALooselyCoupledSupport ON
     * 3.  open conn; xaRes.start; select for update; xaRes.end 
     * 4.  open conn2; xaRes2.start; UPDATE row; xaRes2.end 
     * This should work since Loolely Coupled option is ON
     */
  
    public void Var001() {
        if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
        if (checkJdbc20StdExt()) {
            try {
                JTATest.verboseOut("TEST: Simple update using a XA loosely coupled transaction with Loosely Coupled option on");

                //put data in table and commit
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                AS400JDBCDataSource d = new AS400JDBCDataSource(system_, userId_, charPassword);
                Connection connX = d.getConnection(userId_, charPassword);
                Statement stmt = connX.createStatement();
                int rc = stmt.executeUpdate("DELETE FROM " + basTbl);
                rc = stmt.executeUpdate("INSERT INTO " + basTbl + " VALUES('" + insStr + "1', 'test1')");
                if (rc != 1) {
                    failed("Expected 1 row inserted, got " + rc);
                    return;
                }
                stmt.close();
                connX.commit();
                connX.close();

                XADataSource ds = newXADataSource();

                // ((AS400JDBCXADataSource)ds).setXALooselyCoupledSupport(1);
                JDReflectionUtil.callMethod_V(ds, "setXALooselyCoupledSupport", 1);
                ((AS400JDBCXADataSource) ds).setTransactionIsolation("repeatable read");

                XAConnection xaConn = ((AS400JDBCXADataSource) ds).getXAConnection(userId_, charPassword);
                XAConnection xaConn2 = ((AS400JDBCXADataSource) ds).getXAConnection(userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
                XAResource xaRes = xaConn.getXAResource();
                XAResource xaRes2 = xaConn2.getXAResource();
                Connection conn = xaConn.getConnection();
                Connection conn2 = xaConn2.getConnection();
                
                // Generate a new transaction
                Xid newXid = new LCTXid();
                Xid newXid2 = ((LCTXid)newXid).getMatchingLooselyCoupledXid();
                          
                //start first transaction
                xaRes.start(newXid, XAResource.TMNOFLAGS);
                String sqlString = "SELECT * FROM " + basTbl + " FOR UPDATE";
                JTATest.verboseOut("prepare \"" + sqlString + "\"");
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlString);
                if (rs.next() == false) {
                    failed("Expected 1 row returned, got none");
                    return;
                }
                stmt.close();
                //with or wo is ok xaRes.end(newXid, XAResource.TMSUSPEND); 

                //start loosely coupled transaction to first transaction
                xaRes2.start(newXid2, XAResource.TMNOFLAGS);
                Statement stmt2 = conn2.createStatement();
                int rc2;
                rc2 = stmt2.executeUpdate("UPDATE " + basTbl + " SET col1 = col2 ");
                if (rc2 != 1) {
                    failed("Expected 1 row updated, got " + rc2);
                    return;
                }
                stmt2.close();
                xaRes2.end(newXid2, XAResource.TMSUCCESS);
                xaRes.end(newXid, XAResource.TMSUCCESS);  
                
                //check if ok to global commit
                rc = xaRes.prepare(newXid);
                rc2 = xaRes2.prepare(newXid2);

                if (rc != XAResource.XA_RDONLY) {
                    failed("Expected XA_RDONLY (" + XAResource.XA_RDONLY + "), got " + rc);
                    return;
                }
                if (rc2 != XAResource.XA_OK) {
                    failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc2);
                    return;
                }

                //RDONLY, xaRes.commit(newXid, false);
                conn.close();

                xaRes2.commit(newXid2, false);
                conn2.close();

                xaConn.close();
                xaConn2.close();

                JTATest.verboseOut("Done");
                assertCondition(true);
            } catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

    /* This variation test Loosely Coupled XA transactions with option off.
     * 1.  insert test row into table and commit
     * 2.  turn setXALooselyCoupledSupport OFF
     * 3.  open conn; xaRes.start; select for update; xaRes.end 
     * 4.  open conn2; xaRes2.start; UPDATE row; xaRes2.end 
     * Since Loosely Coupled option is off, this variation should get exception
     */
    public void Var002() {
        if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
        if (checkJdbc20StdExt()) {
            try {
                /*This should get exception since Loosely Coupled option is off and doing an update while table is locked */
                JTATest.verboseOut("TEST: Simple update using a XA loosely coupled transaction with Loosely Coupled option off");

                //put data in table and commit
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                AS400JDBCDataSource d = new AS400JDBCDataSource(system_, userId_, charPassword);
                Connection connX = d.getConnection(userId_, charPassword);
                Statement stmt = connX.createStatement();
                int rc = stmt.executeUpdate("DELETE FROM " + basTbl);
                rc = stmt.executeUpdate("INSERT INTO " + basTbl + " VALUES('" + insStr + "1', 'test1')");
                if (rc != 1) {
                    failed("Expected 1 row inserted, got " + rc);
                    return;
                }
                stmt.close();
                connX.commit();
                connX.close();

                XADataSource ds = newXADataSource();

                // ((AS400JDBCXADataSource)ds).setXALooselyCoupledSupport(0);
                JDReflectionUtil.callMethod_V(ds, "setXALooselyCoupledSupport", 0);

                ((AS400JDBCXADataSource) ds).setTransactionIsolation("repeatable read");

                XAConnection xaConn = ((AS400JDBCXADataSource) ds).getXAConnection(userId_, charPassword);
                XAConnection xaConn2 = ((AS400JDBCXADataSource) ds).getXAConnection(userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
                XAResource xaRes = xaConn.getXAResource();
                XAResource xaRes2 = xaConn2.getXAResource();
                Connection conn = xaConn.getConnection();
                Connection conn2 = xaConn2.getConnection();
          
                
                // Generate a new transaction
                Xid newXid = new LCTXid();
                Xid newXid2 = ((LCTXid)newXid).getMatchingLooselyCoupledXid();

                //start first transaction
                xaRes.start(newXid, XAResource.TMNOFLAGS);
                String sqlString = "SELECT * FROM " + basTbl + " FOR UPDATE";
                JTATest.verboseOut("prepare \"" + sqlString + "\"");
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlString);
                if (rs.next() == false) {
                    failed("Expected 1 row returned, got none");
                    return;
                }
                stmt.close();
                

                //start loosely coupled transaction to first transaction
                xaRes2.start(newXid2, XAResource.TMNOFLAGS);
                Statement stmt2 = conn2.createStatement();
                int rc2;
                try {
                    rc2 = stmt2.executeUpdate("UPDATE " + basTbl + " SET col1 = col2 ");
                    failed("Expected Exception due to lock on table, got rc " + rc2);
                    return;
                } catch (SQLException e) {
                    //got exception as expected due to table lock
                    //assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }

                stmt2.close();
                try{
                    xaRes2.end(newXid2, XAResource.TMFAIL); //fail, so only rollback is valid
                } catch (XAException e) {
                    
                }
                xaRes.end(newXid, XAResource.TMSUCCESS);
                //check if ok to global commit

                //RDONLY, xaRes.commit(newXid, false);
                conn.close();
                xaRes.rollback(newXid);
                
                xaRes2.rollback(newXid2);
                conn2.close();

                xaConn.close();
                xaConn2.close();

                JTATest.verboseOut("Done");
                assertCondition(true);
            } catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

    /* This variation test Loosely Coupled XA transactions with option on.
     * 1.  insert test row into table and commit
     * 2.  turn setXALooselyCoupledSupport ON
     * 3.  open conn; xaRes.start; select for update; xaRes.end 
     * 4.  open conn2; xaRes2.start; DELETE row; xaRes2.end 
     * This should work since Loolely Coupled option is ON
     */
    
    public void Var003() {
        if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
        if (checkJdbc20StdExt()) {
            try {
                JTATest.verboseOut("TEST: Simple delete using a XA loosely coupled transaction with Loosely Coupled option on");

                //put data in table and commit
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                AS400JDBCDataSource d = new AS400JDBCDataSource(system_, userId_, charPassword);
                Connection connX = d.getConnection(userId_, charPassword);
                Statement stmt = connX.createStatement();
                int rc = stmt.executeUpdate("DELETE FROM " + basTbl);
                rc = stmt.executeUpdate("INSERT INTO " + basTbl + " VALUES('" + insStr + "1', 'test1')");
                if (rc != 1) {
                    failed("Expected 1 row inserted, got " + rc);
                    return;
                }
                stmt.close();
                connX.commit();
                connX.close();

                XADataSource ds = newXADataSource();

                // ((AS400JDBCXADataSource)ds).setXALooselyCoupledSupport(1);
                JDReflectionUtil.callMethod_V(ds, "setXALooselyCoupledSupport", 1);

                ((AS400JDBCXADataSource) ds).setTransactionIsolation("repeatable read");

                XAConnection xaConn = ((AS400JDBCXADataSource) ds).getXAConnection(userId_, charPassword);
                XAConnection xaConn2 = ((AS400JDBCXADataSource) ds).getXAConnection(userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
                XAResource xaRes = xaConn.getXAResource();
                XAResource xaRes2 = xaConn2.getXAResource();
                Connection conn = xaConn.getConnection();
                Connection conn2 = xaConn2.getConnection();
 

                // Generate a new transaction
                Xid newXid = new LCTXid();
                Xid newXid2 = ((LCTXid)newXid).getMatchingLooselyCoupledXid();

                //start first transaction
                xaRes.start(newXid, XAResource.TMNOFLAGS);
                String sqlString = "SELECT * FROM " + basTbl + " FOR UPDATE";
                JTATest.verboseOut("prepare \"" + sqlString + "\"");
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlString);
                if (rs.next() == false) {
                    failed("Expected 1 row returned, got none");
                    return;
                }
                stmt.close();
               
                //start loosely coupled transaction to first transaction
                xaRes2.start(newXid2, XAResource.TMNOFLAGS);
                Statement stmt2 = conn2.createStatement();
                int rc2;
                rc2 = stmt2.executeUpdate("DELETE from " + basTbl);
                if (rc2 != 1) {
                    failed("Expected 1 row deleted, got " + rc2);
                    return;
                }
                stmt2.close();
                xaRes.end(newXid, XAResource.TMSUCCESS);
                xaRes2.end(newXid2, XAResource.TMSUCCESS);

                //check if ok to global commit
                rc = xaRes.prepare(newXid);
                rc2 = xaRes2.prepare(newXid2);

                if (rc != XAResource.XA_RDONLY) {
                    failed("Expected XA_RDONLY (" + XAResource.XA_RDONLY + "), got " + rc);
                    return;
                }
                if (rc2 != XAResource.XA_OK) {
                    failed("Expected XA_OK (" + XAResource.XA_OK + "), got " + rc2);
                    return;
                }

                //RDONLY, xaRes.commit(newXid, false);
                conn.close();

                xaRes2.commit(newXid2, false);
                conn2.close();

                xaConn.close();
                xaConn2.close();

                JTATest.verboseOut("Done");
                assertCondition(true);
            } catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

    /* This variation test Loosely Coupled XA transactions with option off.
     * 1.  insert test row into table and commit
     * 2.  turn setXALooselyCoupledSupport OFF
     * 3.  open conn; xaRes.start; select for update; xaRes.end 
     * 4.  open conn2; xaRes2.start; DELETE row; xaRes2.end 
     * Since Loosely Coupled option is off, this variation should get exception
     */
    public void Var004() {
        if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
        if (checkJdbc20StdExt()) {
            try {
                /*This should get exception since Loosely Coupled option is off and doing a delete while table is locked */
                JTATest.verboseOut("TEST: Simple delete using a XA loosely coupled transaction with Loosely Coupled option off");

                //put data in table and commit
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                AS400JDBCDataSource d = new AS400JDBCDataSource(system_, userId_, charPassword);
                Connection connX = d.getConnection(userId_, charPassword);
                Statement stmt = connX.createStatement();
                int rc = stmt.executeUpdate("DELETE FROM " + basTbl);
                rc = stmt.executeUpdate("INSERT INTO " + basTbl + " VALUES('" + insStr + "1', 'test1')");
                if (rc != 1) {
                    failed("Expected 1 row inserted, got " + rc);
                    return;
                }
                stmt.close();
                connX.commit();
                connX.close();

                XADataSource ds = newXADataSource();

                // ((AS400JDBCXADataSource)ds).setXALooselyCoupledSupport(0);
                JDReflectionUtil.callMethod_V(ds, "setXALooselyCoupledSupport", 0);

                ((AS400JDBCXADataSource) ds).setTransactionIsolation("repeatable read");

                XAConnection xaConn = ((AS400JDBCXADataSource) ds).getXAConnection(userId_, charPassword);
                XAConnection xaConn2 = ((AS400JDBCXADataSource) ds).getXAConnection(userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
                XAResource xaRes = xaConn.getXAResource();
                XAResource xaRes2 = xaConn2.getXAResource();
                Connection conn = xaConn.getConnection();
                Connection conn2 = xaConn2.getConnection();
 
                
                // Generate a new transaction
                Xid newXid = new LCTXid();
                Xid newXid2 = ((LCTXid)newXid).getMatchingLooselyCoupledXid();

                //start first transaction
                xaRes.start(newXid, XAResource.TMNOFLAGS);
                String sqlString = "SELECT * FROM " + basTbl + " FOR UPDATE";
                JTATest.verboseOut("prepare \"" + sqlString + "\"");
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlString);
                if (rs.next() == false) {
                    failed("Expected 1 row returned, got none");
                    return;
                }
                stmt.close();
                

                //start loosely coupled transaction to first transaction
                xaRes2.start(newXid2, XAResource.TMNOFLAGS);
                Statement stmt2 = conn2.createStatement();
                int rc2;
                try {
                    rc2 = stmt2.executeUpdate("DELETE from " + basTbl);
                    failed("Expected Exception due to lock on table, got rc " + rc2);
                    return;
                } catch (SQLException e) {
                    //got exception as expected due to table lock
                }

                stmt2.close();
                try{
                    xaRes2.end(newXid2, XAResource.TMFAIL); //fail, so only rollback is valid
                } catch (XAException e) {
                    
                }
                xaRes.end(newXid, XAResource.TMSUCCESS);
                
                xaRes.rollback(newXid);
                conn.close();

                xaRes2.rollback(newXid2);
                conn2.close();

                xaConn.close();
                xaConn2.close();

                JTATest.verboseOut("Done");
                assertCondition(true);
            } catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

    /* This variation test Loosely Coupled XA transactions with option on.
     * 1.  insert test row into table and commit
     * 2.  turn setXALooselyCoupledSupport ON
     * 3.  open conn; xaRes.start; select for update; xaRes.end 
     * 4.  open conn2; xaRes2.start; SELECT for update; xaRes2.end 
     */
  

    public void Var005() {
        if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
        if (checkJdbc20StdExt()) {
            try {

                JTATest.verboseOut("TEST: Simple select using a XA loosely coupled transaction with Loosely Coupled option on");

                //put data in table and commit
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                AS400JDBCDataSource d = new AS400JDBCDataSource(system_, userId_, charPassword);
                Connection connX = d.getConnection(userId_, charPassword);
                Statement stmt = connX.createStatement();
                int rc = stmt.executeUpdate("DELETE FROM " + basTbl);
                rc = stmt.executeUpdate("INSERT INTO " + basTbl + " VALUES('" + insStr + "1', 'test1')");
                if (rc != 1) {
                    failed("Expected 1 row inserted, got " + rc);
                    return;
                }
                stmt.close();
                connX.commit();
                connX.close();

                XADataSource ds = newXADataSource();

                // ((AS400JDBCXADataSource)ds).setXALooselyCoupledSupport(1);
                JDReflectionUtil.callMethod_V(ds, "setXALooselyCoupledSupport", 1);

                ((AS400JDBCXADataSource) ds).setTransactionIsolation("repeatable read");

                XAConnection xaConn = ((AS400JDBCXADataSource) ds).getXAConnection(userId_, charPassword);
                XAConnection xaConn2 = ((AS400JDBCXADataSource) ds).getXAConnection(userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
                XAResource xaRes = xaConn.getXAResource();
                XAResource xaRes2 = xaConn2.getXAResource();
                Connection conn = xaConn.getConnection();
                Connection conn2 = xaConn2.getConnection();
 
                // Generate a new transaction
                Xid newXid = new LCTXid();
                Xid newXid2 = ((LCTXid)newXid).getMatchingLooselyCoupledXid();

                //start first transaction
                xaRes.start(newXid, XAResource.TMNOFLAGS);
                String sqlString = "SELECT * FROM " + basTbl + " FOR UPDATE";//update
                JTATest.verboseOut("prepare \"" + sqlString + "\"");
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlString);
                if (rs.next() == false) {
                    failed("Expected 1 row returned, got none");
                    return;
                }
                stmt.close();
               

                //start loosely coupled transaction to first transaction
                xaRes2.start(newXid2, XAResource.TMNOFLAGS);
                Statement stmt2 = conn2.createStatement();
                ResultSet rs2 = stmt2.executeQuery("SELECT * FROM " + basTbl + " FOR UPDATE");
                if (rs2.next() == false) {
                    failed("Expected 1 row returned, got none");
                    return;
                }

                stmt2.close();
                xaRes2.end(newXid2, XAResource.TMSUCCESS);
                xaRes.end(newXid, XAResource.TMSUCCESS);
                
                //check if ok to global commit
                rc = xaRes.prepare(newXid);
                int rc2 = xaRes2.prepare(newXid2);

                if (rc != XAResource.XA_RDONLY) {
                    failed("Expected XA_RDONLY (" + XAResource.XA_RDONLY + "), got " + rc);
                    return;
                }
                if (rc2 != XAResource.XA_RDONLY) {
                    failed("Expected XA_RDONLY (" + XAResource.XA_RDONLY + "), got " + rc2);
                    return;
                }

                //RDONLY, xaRes.commit(newXid, false);
                conn.close();
                conn2.close();

                xaConn.close();
                xaConn2.close();

                JTATest.verboseOut("Done");
                assertCondition(true);
            } catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

    /* This variation test Loosely Coupled XA transactions with option off.
     * 1.  insert test row into table and commit
     * 2.  turn setXALooselyCoupledSupport OFF
     * 3.  open conn; xaRes.start; select for update; xaRes.end 
     * 4.  open conn2; xaRes2.start; SELECT for update; xaRes2.end 
     * Since Loosely Coupled option is off, this variation should get exception
     */
    public void Var006() {
        if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
        if (checkJdbc20StdExt()) {
            try {
                /*This should get exception since Loosely Coupled option is off and doing a select while table is exclusively locked */
                JTATest.verboseOut("TEST: Simple select using a XA loosely coupled transaction with Loosely Coupled option off");

                //put data in table and commit
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                AS400JDBCDataSource d = new AS400JDBCDataSource(system_, userId_, charPassword);
                Connection connX = d.getConnection(userId_, charPassword);
                Statement stmt = connX.createStatement();
                int rc = stmt.executeUpdate("DELETE FROM " + basTbl);
                rc = stmt.executeUpdate("INSERT INTO " + basTbl + " VALUES('" + insStr + "1', 'test1')");
                if (rc != 1) {
                    failed("Expected 1 row inserted, got " + rc);
                    return;
                }
                stmt.close();
                connX.commit();
                connX.close();

                XADataSource ds = newXADataSource();

                // ((AS400JDBCXADataSource)ds).setXALooselyCoupledSupport(0);
                JDReflectionUtil.callMethod_V(ds, "setXALooselyCoupledSupport", 0);
                ((AS400JDBCXADataSource) ds).setTransactionIsolation("repeatable read");

                XAConnection xaConn = ((AS400JDBCXADataSource) ds).getXAConnection(userId_, charPassword);
                XAConnection xaConn2 = ((AS400JDBCXADataSource) ds).getXAConnection(userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
                XAResource xaRes = xaConn.getXAResource();
                XAResource xaRes2 = xaConn2.getXAResource();
                Connection conn = xaConn.getConnection();
                Connection conn2 = xaConn2.getConnection();
 
                
                // Generate a new transaction
                Xid newXid = new LCTXid();
                Xid newXid2 = ((LCTXid)newXid).getMatchingLooselyCoupledXid();
                
                //start first transaction
                xaRes.start(newXid, XAResource.TMNOFLAGS);
                String sqlString = "SELECT * FROM " + basTbl + " FOR UPDATE";
                JTATest.verboseOut("prepare \"" + sqlString + "\"");
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlString);
                if (rs.next() == false) {
                    failed("Expected 1 row returned, got none");
                    return;
                }
                stmt.close();
                

                //start loosely coupled transaction to first transaction
                xaRes2.start(newXid2, XAResource.TMNOFLAGS);
                Statement stmt2 = conn2.createStatement();
                ResultSet rs2 = null;
                try {
                    rs2 = stmt2.executeQuery("SELECT * FROM " + basTbl + " FOR UPDATE");
                    failed("Expected Exception due to lock on table "+rs2);
                    return;
                } catch (SQLException e) {
                    //got exception as expected due to table lock
                    //e.printStackTrace();
                }

                stmt2.close();
                try{
                    xaRes2.end(newXid2, XAResource.TMFAIL); //fail, so only rollback is valid
                } catch (XAException e) {
                
                }
                xaRes.end(newXid, XAResource.TMSUCCESS);
                //check if ok to global commit

                xaRes.rollback(newXid);
                xaRes2.rollback(newXid2);
                
                conn.close();
                conn2.close();

                xaConn.close();
                xaConn2.close();

                JTATest.verboseOut("Done");
                assertCondition(true);
            } catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

  
 
    void printXid( Xid xid)
    {
        byte[] g = xid.getGlobalTransactionId();
        byte[] b = xid.getBranchQualifier();
        System.out.print("----->globalXid=");
        for(int x = 0; x< g.length; x++)
        {
            System.out.print(g[x] + ",");   
        }
        System.out.print("\n----->branchXid=");
        for(int x = 0; x< b.length; x++)
        {
            System.out.print(b[x] + ",");   
        }
        System.out.println("");
    
    }
}
