///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JTAUDBTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.sql.*;
import java.util.*;

import test.JD.JDSetupCollection;
import test.JTA.JTATestXid;
import test.JTA.JTATransInfo;
import test.JTA.JTAUDBBasic;
import test.JTA.JTAUDBBasic2;
import test.JTA.JTAUDBConn;
import test.JTA.JTAUDBConnCommit;
import test.JTA.JTAUDBConnProp;
import test.JTA.JTAUDBCrash1;
import test.JTA.JTAUDBCrash2;
import test.JTA.JTAUDBDelete;
import test.JTA.JTAUDBIasp;
import test.JTA.JTAUDBInsert;
import test.JTA.JTAUDBLocal;
import test.JTA.JTAUDBMisc;
import test.JTA.JTAUDBResource;
import test.JTA.JTAUDBResource2;
import test.JTA.JTAUDBThread;
import test.JTA.JTAUDBThread2;
import test.JTA.JTAUDBTransOrder;
import test.JTA.JTAUDBUpdate;

import java.io.*;


/**
Test driver for the JDBC Driver class.
**/
public class JTAUDBTest
      extends JDTestDriver {

   /**
   * 
   */
  public static  String COLLECTION     = "JTAUDBTST";
   static boolean verbose = false; // turn off verbose output for JTA tests

   static {
      try {
         String setting;
         setting = System.getProperty("jta.verbose");
         if (setting != null) {
            // Allow us to turn the trace on or off. Its both annoying,
            // and required at different times. Since its more annoying
            // than required, the default is off.
            if (setting.equalsIgnoreCase("on") ||
                setting.equalsIgnoreCase("true")) {
               verbose = true;
            }
            if (setting.equalsIgnoreCase("off") ||
                setting.equalsIgnoreCase("false")) {
               verbose = false;
            }
         }
	 setting = System.getProperty("jtaudb.library");
	 if (setting != null) {
	     COLLECTION = setting; 
	 } 
      }
      catch (Throwable e) { // System.getProperty() not allowed in applets.
         // Ignore.
      }
   }



/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.
@exception  Exception  If an exception occurs.
**/
   public static void main (String args[])
   throws Exception
   {
      runApplication (new JTAUDBTest (args));
   }



/**
Constructs an object for applets.
@exception  Exception  If an exception occurs.
**/
   public JTAUDBTest ()
   throws Exception
   {
      super();

   }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.
@exception  Exception  If an exception occurs.
**/
   public JTAUDBTest (String[] args)
   throws Exception
   {
      super (args);

   }

/**
Performs setup needed before running testcases.

@exception Exception If an exception occurs.
@exception  Exception  If an exception occurs.
**/
   public void setup ()
   throws Exception
   {
      super.setup();
      Connection c = getConnection (getBaseURL (),
                                    systemObject_.getUserId (), encryptedPassword_);
      if (testLib_ != null) {
         COLLECTION = "JDX"+testLib_.substring(3);
	 JTATest.COLLECTION = COLLECTION; 
      }
      System.out.println("JTAUDBTest collection is "+COLLECTION); 

      JDSetupCollection.create (systemObject_, 
                                c, COLLECTION);

      c.close ();
   }



/**
Performs cleanup needed after running testcases.

@exception Exception If an exception occurs.
**/
   public void cleanup ()
   throws Exception
   {
   }

/**
dropCollections - - this does not run automatically - - it is called by JDCleanup.
**/
   public static void dropCollections(Connection c)
   {
      dropCollection(c, COLLECTION);
   }



   protected static void verboseOut(String printThis) {
      if (verbose)
         System.out.println(printThis);
   }

/**
@exception Exception If an exception occurs.
**/
   protected static void crtTmpTbl(String table, Connection c) throws Exception {
      Statement s = c.createStatement();

      try {
         s.execute("DROP TABLE " + table);
      }
      catch (Exception e) {
      }
      s.execute("CREATE TABLE " + table + " (COL1 VARCHAR (16) NOT NULL WITH DEFAULT)");
      s.close();
   }

/**
@exception Exception If an exception occurs.
**/
   protected static void dltTmpTbl(String table, Connection c) throws Exception {
      Statement s = c.createStatement();
      s.execute("DROP TABLE " + table);
      s.close();
   }

/**
@exception Exception If an exception occurs.
**/
   public static JTATransInfo[] getTransInfo() throws Exception {
      int count = -1;
      Vector<JTATransInfo> v = new Vector<JTATransInfo>();

      String cmd = "system \"QSYS/WRKCMTDFN JOB(*ALL) STATUS(*XOPEN) OUTPUT(*PRINT)\"";

      String[] cmdA = new String[2];
      cmdA[0] = "system";
      cmdA[1] = "QSYS/WRKCMTDFN JOB(*ALL) STATUS(*XOPEN) OUTPUT(*PRINT)";

      verboseOut(cmd);
      boolean end = false;
      Process p = Runtime.getRuntime().exec(cmdA);
      //	
      //  This won't end if the buffer fills up!!!!!!!
      //  No need to wait.. Just read for the buffer.  When the buffers empty,
      //  the process will be done. 
      //  p.waitFor();
      // 

      InputStream is = p.getInputStream();
      InputStreamReader ir = new InputStreamReader(is, "Cp037");
      BufferedReader buf = new BufferedReader(ir);
      while (!end) {
         String s = buf.readLine();
         if (s == null)
            end = true;
         else {
            if (s.indexOf("X/Open global transaction") != -1) {
               s = buf.readLine();

               if ((s.indexOf("Transaction manager name") != -1) &&
                   (s.indexOf("Q_UDB_JTA") != -1)) {
                  int i;

                  JTATransInfo match = new JTATransInfo();
                  s = buf.readLine();

                  count++;
                  JTATest.verboseOut("count is " + count);

		  // In V7R1, a line for relational database was added.
		  // Just skip it if present

                  if ((i = s.indexOf("Relational database")) != -1) {
		      s = buf.readLine();
		  }		  

                  if ((i = s.indexOf("Transaction branch state")) != -1) {
                     // this line has state
                     int j = s.indexOf(":  ", i);
                     String state = s.substring(j+3);
                     state = state.trim();
                     match.setState(state);
                  }
                  else {
                     System.out.println("WARNING JTAUDBTest.getTransInfo() WRKCMTDFN: Unexpected line for state: " + s);
                  }

                  s = buf.readLine();
                  if ((i = s.indexOf("Global transaction identifier")) != -1) {
                     // this line has gt id
                     int j = s.indexOf("X", i);
                     String gtid = s.substring(j+2, j+2+16);
                     match.setGlobalTransactionId(gtid);
                  }
                  else {
                     System.out.println("WARNING getTransInfo() WRKCMTDFN: Unexpected line for gtrid: " + s);
                  }

                  s = buf.readLine();
                  if ((i = s.indexOf("Branch qualifier")) != -1) {
                     // this line has bqual
                     int j = s.indexOf("X", i);
                     String bqual = s.substring(j+2, j+2+16);
                     match.setBranchQualifier(bqual);
                     String coll = s.substring(j+2+16);
                     match.setCollection(coll);
                  }
                  else {
                     System.out.println("WARNING getTransInfo() WRKCMTDFN: Unexpected line for bqual: " + s);
                  }

                  // Add this TransInfo object to the vector
                  v.addElement(match);

               }
            }
         }
      }
      if (v.size() == 0) {
         return null;
      }
      // convert the vector into a TransInfo array and return it
      JTATransInfo[] tiArr = new JTATransInfo[v.size()];
      v.copyInto(tiArr);
      return tiArr;
   }


   public static String getStr() {
      // get unique string (to use in table operations)
      java.util.Date d = new java.util.Date();
      long l = d.getTime();
      String s = Long.toString(l);
      return s;

   }

   public static JTATestXid Crash2Xid; // Used in Crash2 test

   public static void storeXid(JTATestXid xid) {
      Crash2Xid = xid;
   }

   public static JTATestXid getXid() {
      return Crash2Xid;
   }


  

/**
Creates the testcases.
**/
   public void createTestcases2 () {


      addTestcase (new JTAUDBMisc (systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_,
                                   password_));

      addTestcase (new JTAUDBBasic (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_,
                                    password_));


      addTestcase (new JTAUDBBasic2 (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_,
                                     password_));

 
      addTestcase (new JTAUDBConn (systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_,
                                   password_, pwrSysUserID_, pwrSysPassword_));

      addTestcase (new JTAUDBConnCommit (systemObject_,
                                         namesAndVars_, runMode_, fileOutputStream_,
                                         password_));

      addTestcase (new JTAUDBConnProp (systemObject_,
                                       namesAndVars_, runMode_, fileOutputStream_,
                                       password_));

      addTestcase (new JTAUDBLocal (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_,
                                    password_));

      addTestcase (new JTAUDBInsert (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_,
                                     password_));

      addTestcase (new JTAUDBUpdate (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_,
                                     password_));

      addTestcase (new JTAUDBDelete (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_,
                                     password_));


      addTestcase (new JTAUDBThread (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_,
                                     password_));

      addTestcase (new JTAUDBThread2 (systemObject_,
                                      namesAndVars_, runMode_, fileOutputStream_,
                                      password_));

      addTestcase (new JTAUDBTransOrder (systemObject_,
                                         namesAndVars_, runMode_, fileOutputStream_,
                                         password_));

      addTestcase (new JTAUDBCrash1 (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_,                                  password_));

      addTestcase (new JTAUDBCrash2 (systemObject_,
                                     namesAndVars_, runMode_, fileOutputStream_,
                                     password_));

//      addTestcase (new JTAStdConnTransIso (systemObject_,
//                                        namesAndVars_, runMode_, fileOutputStream_,
//                                        password_));

// Kulack 5/1/2001 - Even though these tests are named as if they
//                   don't belong here, we put them here because
//                   they extend JTATestcase, and thus use the standard
//                   JTA package names (and are appropriate for the toolbox
//                   driver).
      addTestcase (new JTAUDBResource(systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_,
                                  password_));

      addTestcase (new JTAUDBResource2(systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_,
                                  password_));



      addTestcase (new JTAUDBIasp(systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_,
                                  password_));




   }

}



