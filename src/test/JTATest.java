///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JTATest.java
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
import test.JTA.JTABasic;
import test.JTA.JTABasic2;
import test.JTA.JTAConn;
import test.JTA.JTAConnCommit;
import test.JTA.JTAConnProp;
import test.JTA.JTACrash1;
import test.JTA.JTACrash2;
import test.JTA.JTADMDGetXxx;
import test.JTA.JTADelete;
import test.JTA.JTAInsert;
import test.JTA.JTALocal;
import test.JTA.JTAMisc;
import test.JTA.JTATestXid;
import test.JTA.JTAThread;
import test.JTA.JTAThread2;
import test.JTA.JTATransInfo;
import test.JTA.JTATransOrder;
import test.JTA.JTAUpdate;

import java.io.*;


/**
Test driver for the JDBC Driver class.
**/
public class JTATest
      extends JDTestDriver {

   /**
   * 
   */
  public static  String DEFAULT_COLLECTION1 = "JTATESTCO";
   public static  String COLLECTION     = DEFAULT_COLLECTION1; 
   public static boolean verbose = false; // turn off verbose output for JTA tests
   public static StringBuffer verboseOutput = new StringBuffer();

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
       if (args.length >= 1 && args[0].equals("getTransInfo")) {
	   System.out.println("running getTransInfo");
	   JTATransInfo[] transInfo = getTransInfo();
	   if (transInfo == null) { 
	       System.out.println("transInfo returned null ");
	   } else {
	       System.out.println("transInfo returned "+transInfo.length+" entries");
	       for (int i = 0; i < transInfo.length; i++) {
		   System.out.println(i+":"+transInfo[i]); 
	       } 
	   } 
       } else { 
	   runApplication (new JTATest (args));
       }
   }



/**
Constructs an object for applets.
@exception  Exception  If an exception occurs.
**/
   public JTATest ()
   throws Exception
   {
      super();
   }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.
@exception  Exception  If an exception occurs.
**/
   public JTATest (String[] args)
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
      }
      System.out.println("JTATest collection is "+COLLECTION); 
      JDSetupCollection.create (systemObject_, 
                                c, COLLECTION,
				false, out_);  /* Do not drop collection */ 

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



   public static void verboseOut(String printThis) {
       verboseOutput.append(printThis);
       verboseOutput.append("\n");

      if (verbose)
         System.out.println(printThis);
   }

   public static String getVerboseOut() {
       String returnString =  verboseOutput.toString();

       verboseOutput.setLength(0);

       return returnString; 
   }

   public static void resetVerboseOut() {
       verboseOutput.setLength(0);
   }


/**
@exception Exception If an exception occurs.
**/
   public static void crtTmpTbl(String table, Connection c) throws Exception {
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
   public  static void dltTmpTbl(String table, Connection c) throws Exception {
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
      String [] cmdA = new String[2];
      cmdA[0] = "system";
      cmdA[1] = "QSYS/WRKCMTDFN JOB(*ALL) STATUS(*XOPEN) OUTPUT(*PRINT)";

      verboseOut(cmdA[0]+" "+cmdA[1]);
      boolean end = false;
      /* */       
      /* The Runtime not longer uses the shell to parse this -- Use the flavor of exec */
      /* That passes the arguments */
      /* */

      Process p = Runtime.getRuntime().exec(cmdA);

      //	
      //  This won't end if the buffer fills up!!!!!!!
      //  No need to wait.. Just read for the buffer.  When the buffers empty,
      //  the process will be done. 
      //  p.waitFor();
      //


      InputStream is = p.getInputStream();
      InputStreamReader ir;
      //
      // For J9, an ASCII runtime is used?
      // Starting on 04/12/2007 for JTAUDBConn 10 
      // this is now returning EBCDIC
      //
      String vmName = System.getProperty("java.vm.name");
      // System.out.println("vmName is "+vmName);
      if (vmName.indexOf("J9") > 0) {
          // This always starts with a space, so use that to determine if EBCDIC or ASCII 
          int firstChar = is.read();
          if (firstChar == 0x40) { 
            ir = new InputStreamReader(is, "Cp037");
          } else { 
	    ir = new InputStreamReader(is);
          }

      } else { 
	  ir = new InputStreamReader(is, "Cp037");
      }
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
                     System.out.println("WARNING JTATest.getTransInfo() WRKCMTDFN: Unexpected line for state: " + s);
                  }

                  s = buf.readLine();
 
		  if ( s.indexOf("Format ID") > 0) {
                     // Skip this extra line -- Needed for V5R2 (maybe earlier) 
                     s = buf.readLine();                       
		  } 
                  if ((i = s.indexOf("Global transaction identifier")) != -1) {
                     // this line has gt id in this format.
                     int startGtid = s.indexOf("X", i)+2;
                     int endGtid = startGtid+s.substring(startGtid).indexOf("'")-1;
                     if (endGtid < startGtid) {
			 if (s.indexOf("X''") > 0) {
			     // don't bother with the warning message
			 } else {
			     System.out.println("WARNING getTransInfo() WRKCMTDFN: gtrid too " +
						"long to handle simply in line: " + s);
			 }
                        match.setGlobalTransactionId("0");
                     }
                     else {
                        // For Testcase XIDs, the GTRID is an 8 byte int.
                        // We'll just take all of the data regardless of length.
                        String gtid = s.substring(startGtid, endGtid+1);
                        match.setGlobalTransactionId(gtid);
                     }
                  }
                  else {
                     System.out.println("WARNING getTransInfo() WRKCMTDFN: Unexpected line for gtrid: " + s);
                  }

                  s = buf.readLine();
                  if ((i = s.indexOf("Branch qualifier")) != -1) {
                     // this line has bqual
                     int startBqual = s.indexOf("X", i)+2;
                     int endBqual   = startBqual+s.substring(startBqual).indexOf("'")-1;
                     if (endBqual < startBqual) {
			 if (s.indexOf("X''") > 0) {
			     // don't bother with the warning message
			 } else {
			     System.out.println("WARNING getTransInfo() WRKCMTDFN: bqual too " +
                                           "long to handle simply in line: " + s);
			 }
                        match.setBranchQualifier("0");
                        match.setCollection("0");
                     }
                     else {
                        // For testcase XIDs, the BQUAL is an 8 byte int,
                        // followed by the rest being the collection name.
                        // If longer than 8 bytes (16 hex digits), we'll use
                        // the rest as collection name.
                        String fullbqual = s.substring(startBqual, endBqual+1);
                        if (fullbqual.length() > 16) {
                           match.setBranchQualifier(fullbqual.substring(0,16));
                           match.setCollection(fullbqual.substring(16));
                        }
                        else {
                           match.setBranchQualifier(fullbqual);
                           match.setCollection("");
                        }
                     }
                  }
                  else {
                     System.out.println("WARNING getTransInfo() WRKCMTDFN: Unexpected line for bqual: " + s);
                  }

                  // Add this TransInfo object to the vector
                  v.addElement(match);

	       } /* Q_UDB_JTA != 1 */ 
	    } else { /* X/Open global transaction */
		// System.out.println("Ignoring line: "+s);
	    }
	 } /* not null */ 
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

       addTestcase (new JTADMDGetXxx (systemObject_,
				      namesAndVars_, runMode_, fileOutputStream_,
				      password_));

       addTestcase (new JTABasic (systemObject_,
				  namesAndVars_, runMode_, fileOutputStream_,
				  password_));

      addTestcase (new JTABasic2 (systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_,
                                  password_));

      addTestcase (new JTAConn (systemObject_,
                                namesAndVars_, runMode_, fileOutputStream_,
                                password_));

      addTestcase (new JTAConnCommit (systemObject_,
                                      namesAndVars_, runMode_, fileOutputStream_,
                                      password_));

      addTestcase (new JTAConnProp (systemObject_,
                                    namesAndVars_, runMode_, fileOutputStream_,
                                    password_));

      addTestcase (new JTALocal (systemObject_,
                                 namesAndVars_, runMode_, fileOutputStream_,
                                 password_));

      addTestcase (new JTAInsert (systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_,
                                  password_));

      addTestcase (new JTAUpdate (systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_,
                                  password_));

      addTestcase (new JTADelete (systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_,
                                  password_));

      addTestcase (new JTAMisc (systemObject_,
                                namesAndVars_, runMode_, fileOutputStream_,
                                password_));

      addTestcase (new JTAThread (systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_,
                                  password_));

      addTestcase (new JTAThread2 (systemObject_,
                                   namesAndVars_, runMode_, fileOutputStream_,
                                   password_));

      addTestcase (new JTATransOrder (systemObject_,
                                      namesAndVars_, runMode_, fileOutputStream_,
                                      password_));

      addTestcase (new JTACrash1 (systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_,
                                  password_));

      addTestcase (new JTACrash2 (systemObject_,
                                  namesAndVars_, runMode_, fileOutputStream_,
                                  password_));
//      addTestcase (new JTAConnTransIso (systemObject_,
//                                        namesAndVars_, runMode_, fileOutputStream_,
//                                        password_));
      // @A1A

// Kulack 5/1/2001 - Even though these tests are named as if they
//                   belong here, we put them in the JTAStdTest because
//                   they extend JTATestcase, and thus use the standard
//                   JTA package names (and are appropriate for the toolbox
//                   driver).
//      addTestcase (new JTAResource(systemObject_,
//                                  namesAndVars_, runMode_, fileOutputStream_,
//                                  password_));

//      addTestcase (new JTAResource2(systemObject_,
//                                  namesAndVars_, runMode_, fileOutputStream_,
//                                  password_));

      //addTestcase (new JTAResourceRecover(systemObject_,
      //                            namesAndVars_, runMode_, fileOutputStream_,
      //                            password_));
   }

}



