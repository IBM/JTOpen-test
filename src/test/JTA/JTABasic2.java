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

import java.sql.Connection;
import java.util.*;

import java.io.FileOutputStream;
import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.JTATest;
import test.PasswordVault;
import test.JD.JDTestUtilities;

import javax.sql.XADataSource; 

public class JTABasic2 extends JDTestcase {

  boolean isIasp = false; 
  boolean isNTS = false; 
/**
Constructor.
**/
   public JTABasic2 (AS400 systemObject,
                     Hashtable namesAndVars,
                     int runMode,
                     FileOutputStream fileOutputStream,
                     
                     String password) {
      super (systemObject, "JTABasic2",
             namesAndVars, runMode, fileOutputStream,
             password);
   }


   public JTABasic2 (AS400 systemObject,
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

       if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {

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
       if ((isJdbc20StdExt ()) && (getDriver() != JDTestDriver.DRIVER_TOOLBOX)) {
      }
       unlockSystem(); 
   }


   // TO Test:
   // calls to methods which are not used (usually) in the other tests
   // these are essentially the getter and setter method types


   public void Var001() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Get the default timeout for the resource manager");

            javax.sql.XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);

	    if (getDriver () == JDTestDriver.DRIVER_NATIVE)
	    {
		JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
		JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);
	    }

            Object           xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");

            int to = JDReflectionUtil.callMethod_I(xaRes,"getTransactionTimeout");
            JTATest.verboseOut("Default timeout=" + to + " seconds");
            assertCondition(to == 0);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var002() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            // currently cannot set a non-default value as timeout
            // notApplicable();
            JTATest.verboseOut("TEST: Set the timeout for the resource manager");

            javax.sql.XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object           xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");

            boolean b = JDReflectionUtil.callMethod_B(xaRes,"setTransactionTimeout",5);
            int to = JDReflectionUtil.callMethod_I(xaRes,"getTransactionTimeout");
            JTATest.verboseOut("b=" + b + ", to=" + to);
            assertCondition(!(b) && (to == 0));
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var003() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Setting the timeout value to 0 should set it to default value");

            javax.sql.XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);


            Object           xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");

            int def = JDReflectionUtil.callMethod_I(xaRes,"getTransactionTimeout");
            boolean b = JDReflectionUtil.callMethod_B(xaRes,"setTransactionTimeout",0);
            int to = JDReflectionUtil.callMethod_I(xaRes,"getTransactionTimeout");
            JTATest.verboseOut("b=" + b + ", def=" + def + ", to=" + to);
            assertCondition(!(b) && (to == 0) && (def == 0));
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var004() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Compare RMs (same RM)");

            javax.sql.XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);


            Object           xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");

            assertCondition(JDReflectionUtil.callMethod_B(xaRes,"isSameRM", xaRes));
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var005() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Compare RMs (two XARes from same XaConn)");

            javax.sql.XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object           xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes1 = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");
            Object xaRes2 = JDReflectionUtil.callMethod_O(xaConn,"getXAResource");

            assertCondition(JDReflectionUtil.callMethod_B(xaRes1,"isSameRM", xaRes2));
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }


   public void Var006() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Compare RMs (XARes from diff XaConn)");

            javax.sql.XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object        xaConn1 = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object        xaConn2 = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes1 =  JDReflectionUtil.callMethod_O(xaConn1,"getXAResource");
            Object xaRes2 =  JDReflectionUtil.callMethod_O(xaConn2,"getXAResource");

            // in our case the DB is the RM, so since both connections are to the
            // same DB, we should get true
            assertCondition(JDReflectionUtil.callMethod_B(xaRes1,"isSameRM", xaRes2));
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }



   public void Var007() {
       if (checkNative ()) {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
      if (checkJdbc20StdExt()) {
         try {
            JTATest.verboseOut("TEST: Get FormatID");

            javax.sql.XADataSource ds = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds,"setDatabaseName",system_);
	    JDReflectionUtil.callMethod_V(ds,"setUser",userId_);
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	    JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword);
   PasswordVault.clearPassword(charPassword);

            Object           xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
            Object xaRes =  JDReflectionUtil.callMethod_O(xaConn,"getXAResource");

            JTATestXid newXid = new JTATestXid();
            JDReflectionUtil.callMethod_V(xaRes,"start",newXid, javax.transaction.xa.XAResource.TMNOFLAGS);
            int fmtId = newXid.getFormatId();
            assertCondition(fmtId == 42);
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }

   public void Var008() {
       if (checkNative ()) {
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
            javax.sql.XADataSource ds1      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds1,"setDatabaseName",system_);
	    //JDReflectionUtil.callMethod_V(ds1,"setUser",userId_);
	    //JDReflectionUtil.callMethod_V(ds1,"setPassword",password_);

            Object            xaConn1 = ds1.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes1 =  JDReflectionUtil.callMethod_O(xaConn1,"getXAResource");

            javax.sql.XADataSource ds2      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds2,"setDatabaseName",otherSystem);
            //JDReflectionUtil.callMethod_V(ds2,"setUser",userId_);       // @A1A
            //JDReflectionUtil.callMethod_V(ds2,"setPassword",password_); // @A1A
            Object            xaConn2 = ds2.getXAConnection(userId_,PasswordVault.decryptPasswordLeak(encryptedPassword_));
            Object xaRes2 =  JDReflectionUtil.callMethod_O(xaConn2,"getXAResource");

            // in our case the DB is the RM, so since each connections is to a
            // diff DB, we should get false
            assertCondition(!JDReflectionUtil.callMethod_B(xaRes1,"isSameRM", xaRes2));
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }

   public void Var009() {
       if (checkNative ()) {
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
            javax.sql.XADataSource ds1      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds1,"setDatabaseName",otherSystem);
            JDReflectionUtil.callMethod_V(ds1,"setUser",userId_);       // @A1A
            JDReflectionUtil.callMethod_V(ds1,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_)); // @A1A
            Object           xaConn1 = ds1.getXAConnection();
            Object xaRes1 =  JDReflectionUtil.callMethod_O(xaConn1,"getXAResource");

            javax.sql.XADataSource ds2      = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
            JDReflectionUtil.callMethod_V(ds2,"setDatabaseName",otherSystem);
            JDReflectionUtil.callMethod_V(ds2,"setUser",userId_);       // @A1A
            JDReflectionUtil.callMethod_V(ds2,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_)); // @A1A
            Object            xaConn2 = ds2.getXAConnection();
            Object xaRes2 =  JDReflectionUtil.callMethod_O(xaConn2,"getXAResource");

            // in our case the DB is the RM, so since each connections is to the
            // same DB, we should get true
            assertCondition(JDReflectionUtil.callMethod_B(xaRes1,"isSameRM", xaRes2));
         }
         catch (Exception e) {
            failed(e, "Unexpected Exception");
         }
      }
       }
   }

}
