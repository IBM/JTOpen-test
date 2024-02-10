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
// File Name:    JTAStdBasic2.java
//
// Description:  Same as JTABasic2.java but test standard interfaces for
//               JTA & JDBC Std Ext
//
// Classes:      JTAStdBasic
//
////////////////////////////////////////////////////////////////////////

package test.JTA;

import java.sql.*;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JTATest;
import test.PasswordVault;
import test.JD.JDTestUtilities;

import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

public class JTAStdBasic2 extends JTATestcase {

   // private String insStr = "JTABasic2";
   //private String basTbl = JTATest.COLLECTION + ".CHARTAB";
   // private String endTbl = JTATest.COLLECTION + ".END1TBL";
   private Connection c;
   //private Connection gc;


/**
Constructor.
**/
   public JTAStdBasic2 (AS400 systemObject,
                     Hashtable namesAndVars,
                     int runMode,
                     FileOutputStream fileOutputStream,
                     
                     String password) {
      super (systemObject, "JTAStdBasic2",
             namesAndVars, runMode, fileOutputStream,
             password);
   }

   public JTAStdBasic2 (AS400 systemObject,
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
       // basTbl = JTATest.COLLECTION + ".CHARTAB";
       // endTbl = JTATest.COLLECTION + ".END1TBL";

      if (isJdbc20StdExt ()) {
         c = testDriver_.getConnection(baseURL_, userId_, encryptedPassword_);
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
      if (isJdbc20StdExt ()) {
      }
      unlockSystem(); 
   }


   // TO Test:
   // calls to methods which are not used (usually) in the other tests
   // these are essentially the getter and setter method types


   public void Var001() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Get the default timeout for the resource manager");

            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();

            int to = xaRes.getTransactionTimeout();
            JTATest.verboseOut("Default timeout=" + to + " seconds");
            assertCondition(to == 0);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var002() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            // currently cannot set a non-default value as timeout
            // notApplicable();
            JTATest.verboseOut("TEST: Set the timeout for the resource manager");

            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();

            boolean b = xaRes.setTransactionTimeout(5);
            int to = xaRes.getTransactionTimeout();
            JTATest.verboseOut("b=" + b + ", to=" + to);
	    if (isNTS) {
		assertCondition((b) && (to == 5));
	    } else {
		assertCondition(!(b) && (to == 0));
	    } 
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var003() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Setting the timeout value to 0 should set it to default value");

            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();

            int def = xaRes.getTransactionTimeout();
            boolean b = xaRes.setTransactionTimeout(0);
            int to = xaRes.getTransactionTimeout();
            JTATest.verboseOut("b=" + b + ", def=" + def + ", to=" + to);
	    if (isNTS) {
		assertCondition((b) && (to == 0) && (def == 0));
	    } else {
		assertCondition(!(b) && (to == 0) && (def == 0));
	    } 
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var004() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Compare RMs (same RM)");

            XADataSource          ds     = newXADataSource();

            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();

            assertCondition(xaRes.isSameRM(xaRes));
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var005() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Compare RMs (two XARes from same XaConn)");

            XADataSource          ds     = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes1  = xaConn.getXAResource();
            XAResource              xaRes2  = xaConn.getXAResource();

            assertCondition(xaRes1.isSameRM(xaRes2));
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }


   public void Var006() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Compare RMs (XARes from diff XaConn)");

            XADataSource          ds     = newXADataSource();
            XAConnection            xaConn1 = ds.getXAConnection();
            XAConnection            xaConn2 = ds.getXAConnection();
            XAResource              xaRes1  = xaConn1.getXAResource();
            XAResource              xaRes2  = xaConn2.getXAResource();

            assertCondition(xaRes1.isSameRM(xaRes2));
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
   }



   public void Var007() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
     Connection            conn = null; 
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Get FormatID");

            XADataSource          ds     = newXADataSource();
            XAConnection            xaConn = ds.getXAConnection();
            XAResource              xaRes  = xaConn.getXAResource();
                        conn = xaConn.getConnection();

            JTATestXid newXid = new JTATestXid();
            xaRes.start(newXid, XAResource.TMNOFLAGS);
            int fmtId = newXid.getFormatId();
            assertCondition(fmtId == 42);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception for "+conn);
         }
      }
   }

   public void Var008() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         String otherSystem = System.getProperty("jta.secondary.system");
         if (otherSystem == null ||
             otherSystem.equalsIgnoreCase(system_)) {
            failed("This variation requires the System property 'jta.secondary.system' " +
                   "be set to a system (DB) other than the one named for the main test");
            return;
         }
         try {
            JTATest.verboseOut("TEST: Compare RMs (diff DBs): RDB1=" + system_ +
                               ", RDB2=" + otherSystem);

           // String defaultSystem = testDriver_.systemObject_.getSystemName();
            JTATest.verboseOut("   Connecting to "+otherSystem); 
            XADataSource          ds2     = newXADataSource(otherSystem, userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAConnection            xaConn2 = ds2.getXAConnection();
            XAResource              xaRes2  = xaConn2.getXAResource();

            JTATest.verboseOut("   Connecting to "+system_); 
            XADataSource          ds1     = newXADataSource(system_, userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAConnection            xaConn1 = ds1.getXAConnection();
            XAResource              xaRes1  = xaConn1.getXAResource();


            // in our case the DB is the RM, so since each connections is to a
            // diff DB, we should get false
            assertCondition(!xaRes1.isSameRM(xaRes2),"otherSystem="+otherSystem+" system_="+system_+ " xaRes1="+xaRes1+" xaRes2="+xaRes2);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception.  Probably can't reach secondary system!");
         }
      }
   }

   public void Var009() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         String otherSystem = System.getProperty("jta.secondary.system");
         if (otherSystem == null ||
             otherSystem.equalsIgnoreCase(system_)) {
            failed("This variation requires the System property 'jta.secondary.system' " +
                   "be set to a system (DB) other than the one named for the main test");
            return;
         }
         try {
            JTATest.verboseOut("TEST: Compare RMs (same DBs): RDB1=" + otherSystem +
                               ", RDB2=" + otherSystem);

            // String defaultSystem = testDriver_.systemObject_.getSystemName();
            XADataSource          ds1     = newXADataSource(otherSystem, userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAConnection            xaConn1 = ds1.getXAConnection();
            XAResource              xaRes1  = xaConn1.getXAResource();

            XADataSource          ds2     = newXADataSource(otherSystem, userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));
            XAConnection            xaConn2 = ds2.getXAConnection();
            XAResource              xaRes2  = xaConn2.getXAResource();

            assertCondition(xaRes1.isSameRM(xaRes2));
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception.  Probably can't reach secondary system!");
         }
      }
   }

}
