///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSBatchCompressPerformance.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////



package test.JD.PS;


import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnection;

import test.JDJobName;
import test.JTOpenTestEnvironment;



/**
Testcase JDPSBatchCompressPerformance.  This test the performance of the batch compress.  Put in separate testcases so that it can be easily rerun in the case where the system is busy and the performance numbers are bad.
**/
public class JDPSBatchCompressPerformance
extends JDPSBatchCompress {

private Connection killConnection = null ;
/**
Constructor.
**/
    public JDPSBatchCompressPerformance (AS400 systemObject,
                      Hashtable<String,Vector<String>> namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password,
                            String powerUserID,
                            String powerPassword)
    {
        super (systemObject, "JDPSBatchCompressPerformance",
               namesAndVars, runMode, fileOutputStream,
               password,powerUserID, powerPassword);
    }


    public JDPSBatchCompressPerformance (AS400 systemObject,
		      String testname, 
                      Hashtable<String,Vector<String>> namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password,
                            String powerUserID,
                            String powerPassword)
    {
        super (systemObject, testname,
               namesAndVars, runMode, fileOutputStream,
               password, powerUserID, powerPassword);
    }


    /**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
      throws Exception
    {

    super.setup();

    /* If running on IBM i, change the job priority */

    if (JTOpenTestEnvironment.isOS400 && !JTOpenTestEnvironment.isOS400open) {
      String jobName = JDJobName.getJobName();
      if (jobName.indexOf("not Available") < 0) {
        String sql = "";
        try {
          
          Connection c = testDriver_.getConnection("jdbc:db2:*LOCAL",   pwrSysUserID_, pwrSysEncryptedPassword_);
          sql = "CALL QSYS.QCMDEXC ('CHGJOB JOB(" + jobName
              + ")  RUNPTY(1)            ', 0000000051.00000)";
          System.out.println("Setup:  "+sql); 
          Statement s = c.createStatement();

          s.execute(sql);
          c.close();
        } catch (Exception e) {
          System.out.println("Warning:  error in setup sql=" + sql);
          e.printStackTrace();
        }
      }
    }

  }

      /* Don't run variations from the superclass */

      public void Var001() { notApplicable("non-performance variations"); }
      public void Var002() { notApplicable("non-performance variations"); }
      public void Var003() { notApplicable("non-performance variations"); }
      public void Var004() { notApplicable("non-performance variations"); }
      public void Var005() { notApplicable("non-performance variations"); }
      public void Var006() { notApplicable("non-performance variations"); }
      public void Var007() { notApplicable("non-performance variations"); }
      public void Var008() { notApplicable("non-performance variations"); }
      public void Var009() { notApplicable("non-performance variations"); }
      public void Var010() { notApplicable("non-performance variations"); }
      public void Var011() { notApplicable("non-performance variations"); }
      public void Var012() { notApplicable("non-performance variations"); }
      public void Var013() { notApplicable("non-performance variations"); }
      public void Var014() { notApplicable("non-performance variations"); }
      public void Var015() { notApplicable("non-performance variations"); }
      public void Var016() { notApplicable("non-performance variations"); }
      public void Var017() { notApplicable("non-performance variations"); }
      public void Var018() { notApplicable("non-performance variations"); }
      public void Var019() { notApplicable("non-performance variations"); }
      public void Var020() { notApplicable("non-performance variations"); }
      public void Var021() { notApplicable("non-performance variations"); }
      public void Var022() { notApplicable("non-performance variations"); }
      public void Var023() { notApplicable("non-performance variations"); }
      public void Var024() { notApplicable("non-performance variations"); }
      public void Var025() { notApplicable("non-performance variations"); }
      public void Var026() { notApplicable("non-performance variations"); }
      public void Var027() { notApplicable("non-performance variations"); }
      public void Var028() { notApplicable("non-performance variations"); }
      public void Var029() { notApplicable("non-performance variations"); }
      public void Var030() { notApplicable("non-performance variations"); }
      public void Var031() { notApplicable("non-performance variations"); }
      public void Var032() { notApplicable("non-performance variations"); }
  /**
   * Testing only VARCHAR1000 types  
  **/
      public void Var033() {
	  int[] dataTypes = { TYPE_VARCHAR1000, TYPE_VARCHAR1000, TYPE_VARCHAR1000,
	  TYPE_VARCHAR1000, TYPE_VARCHAR1000, };
  
	  double expectedRatio = 0.75;
	  // If running on Windows or linux the performance difference will not be so greate
	  if (JTOpenTestEnvironment.isLinux || JTOpenTestEnvironment.isWindows) {
	      expectedRatio  =  0.85; 
	  }
	  String hostname = "";
    try {
      hostname = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
    }
    if (hostname.equals("SQ740")) {
      // Performance is worse on test system
      expectedRatio = 0.91; 
    }
        
	  testInsertCompressionTimePerformance("500 rows of 5 VARCHAR(1000)",
	      "JDPSBC033", 
	      dataTypes, 
	      500, /* rows */ 
	      expectedRatio, /*ratio*/  
	      2 /* minutes*/   );
      }



  /**
   * Testing only VARCHAR30 types  
  **/
      public void Var034() {
	  int[] dataTypes = { TYPE_VARCHAR60, TYPE_VARCHAR60, TYPE_VARCHAR60,
	  TYPE_VARCHAR60, TYPE_VARCHAR60, };
          //
	  // With the removal of RLL this changed from 0.96 to .83
	  // 

	  testInsertCompressionTimePerformance("500 rows of 5 VARCHAR(60)","JDPSBC034", 
	      dataTypes, 
	      500, /* rows */ 
	      0.96, /*ratio*/  
	      2 /* minutes*/   );
      }


    /**
     * Testing only LONGVARCHAR types  
    **/
      public void Var035() {
	  int[] dataTypes = { TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, TYPE_LONGVARCHAR,
	  TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, };



	  double expectedRatio = 0.39;
	  // If running on Windows or linux the performance difference will not be so greate
	  if (JTOpenTestEnvironment.isLinux || JTOpenTestEnvironment.isWindows) { 
	      expectedRatio  =  0.40; 
	  }
	  String hostname = ""; 
    try {
      hostname = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
    }

    if (hostname.equals("SQ740")) {
      // Performance is worse on test system
      expectedRatio = 0.92; 
    }


	  testInsertCompressionTimePerformance("500 rows of 5 LONGVARCHAR", "JDPSBC035", dataTypes, 500, expectedRatio, 2 /*minutes */ );
      }

      public void Var036() { notApplicable("non-performance variations"); }

      public void Var037() {
	  int[] dataTypes = { TYPE_VARCHAR60, TYPE_VARCHAR60, TYPE_VARCHAR60,
	  TYPE_VARCHAR60, TYPE_VARCHAR60, };
          //
	  // With the removal of RLL this changed from 0.8 to 0.9
	  // 
	  testInsertCompressionBytePerformance("500 rows of 5 VARCHAR(60)","JDPSBC037", dataTypes, 500, 0.91);
      }

      public void Var038() {
	  int[] dataTypes = { TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, TYPE_LONGVARCHAR,
	  TYPE_LONGVARCHAR, TYPE_LONGVARCHAR, };

	  // Note after disabling RLL compression,
          // This changed from .87 to .94 
	  testInsertCompressionBytePerformance("500 rows of 5 LONGVARCHAR","JDPSBC038", dataTypes, 500, 0.94);
      }


      public void Var039() {
	  int[] dataTypes = { TYPE_VARCHAR1000, TYPE_VARCHAR1000, TYPE_VARCHAR1000,
	  TYPE_VARCHAR1000, TYPE_VARCHAR1000, };
          //
	  // With the removal of RLL this changed from 0.85 to 0.91
	  // 

	  testInsertCompressionBytePerformance("500 rows of 5 VARCHAR(1000)", "JDPSBC039", dataTypes, 500, 0.91);
      }


      public void Var040() { notApplicable("non-performance variations"); }
      public void Var041() { notApplicable("non-performance variations"); }
      public void Var042() { notApplicable("non-performance variations"); }
      public void Var043() { notApplicable("non-performance variations"); }
      public void Var044() { notApplicable("non-performance variations"); }
      public void Var045() { notApplicable("non-performance variations"); }
      public void Var046() { notApplicable("non-performance variations"); }
      public void Var047() { notApplicable("non-performance variations"); }
      public void Var048() { notApplicable("non-performance variations"); }
      public void Var049() { notApplicable("non-performance variations"); }
      public void Var050() { notApplicable("non-performance variations"); }
      public void Var051() { notApplicable("non-performance variations"); }
      public void Var052() { notApplicable("non-performance variations"); }
      public void Var053() { notApplicable("non-performance variations"); }
      public void Var054() { notApplicable("non-performance variations"); }
      public void Var055() { notApplicable("non-performance variations"); }
      public void Var056() { notApplicable("non-performance variations"); }
      public void Var057() { notApplicable("non-performance variations"); }
      public void Var058() { notApplicable("non-performance variations"); }
      public void Var059() { notApplicable("non-performance variations"); }
      public void Var060() { notApplicable("non-performance variations"); }
      public void Var061() { notApplicable("non-performance variations"); }
      public void Var062() { notApplicable("non-performance variations"); }
      public void Var063() {

	  /* Run a variation where we get a fresh QZDASONIT job */
	  /* this is to assure that the client level is used to */
          /* set the server level.  A previous bug in QZDACMDP  */
          /* only set the server level based on a previous      */
          /* client level.                                      */

	  try { 
	      refreshServerJobs();

	      int[] dataTypes = { TYPE_VARCHAR60, TYPE_VARCHAR60, TYPE_VARCHAR60,
	      TYPE_VARCHAR60, TYPE_VARCHAR60, };
          //
	  // With the removal of RLL this changed from 0.8 to 0.9
	  // 
	      testInsertCompressionBytePerformance("500 rows of 5 VARCHAR(60)","JDPSBC063", dataTypes, 500, 0.91);
	  } catch (Exception e) {
	      failed(e, "Testing new server jobs"); 
	  } 

      }
      
      
      /* Refresh the current server jobs so that we are likely to get a new server job 
       * that has not been used before
       */
      
      private void refreshServerJobs() throws Exception {
        cleanup(); 
        for (int j = 0; j < 2; j++) { 
          Connection connections[] = new Connection[50]; 
          String     jobNames[]    = new String[50]; 
          
          if (killConnection != null) { 
            try { 
              killConnection.close(); 
            } catch (Exception e) { 
              
            }
            killConnection = null; 
          }
          for (int i = 0; i < 50; i++) {
	      try { 
		  connections[i] = testDriver_.getConnection(baseURL_, pwrSysUserID_, pwrSysEncryptedPassword_);
		  
		  if (connections[i] instanceof AS400JDBCConnection) {
		      String unformatttedJobdName =((AS400JDBCConnection)connections[i]).getServerJobIdentifier();
		      jobNames[i] = unformatttedJobdName.substring(20, 26).trim() +"/"+
			unformatttedJobdName.substring(10, 20).trim() +"/"+
			unformatttedJobdName.substring(0, 10).trim();
		  }
	      } catch (Exception e) {
		  System.out.println("Warning:  Exception on getConnection("+
				     baseURL_+","+pwrSysUserID_+")");
		  e.printStackTrace(System.out); 
				     
	      } 
          }

          
          killConnection  = testDriver_.getConnection(baseURL_, pwrSysUserID_, pwrSysEncryptedPassword_);
          Statement stmt = killConnection.createStatement(); 
          for (int i = 0; i < 50; i++) { 
            if (jobNames[i] != null) { 
              String sql = "call QSYS.QCMDEXC('ENDJOB JOB("+jobNames[i]+") OPTION(*IMMED)        ' , 0000000056.00000)";
              // System.out.println("Running "+sql); 
              stmt.executeUpdate(sql);
              
            }
          }
          for (int i = 0; i < 50; i++ ) { 
            try {
              Statement s = connections[i].createStatement(); 
              s.executeQuery("Select * from sysibm.sysdummy1"); 
              connections[i].close(); 
              // System.out.println("Connection "+i+" closed"); 
            } catch (Exception e) {
              //System.out.println("Connection "+i+" exception while closing"); 
              String message = e.toString(); 
              if (message.indexOf("Communication link failure") < 0 ) {
                System.out.println("Connection "+i+" : UNEXPECTED exception while closing"); 
                e.printStackTrace(System.out);
              }
            }
          }

          // Keep the killConnection open so it is not reused. 
          // killConnection.close();
        }
        setup(); 
      }


      public void Var064()  {

	  /* Run a variation where we get a fresh QZDASONIT job */
	  /* this is to assure that the client level is used to */
          /* set the server level.  A previous bug in QZDACMDP  */
          /* only set the server level based on a previous      */
          /* client level.                                      */

	  try { 
	      refreshServerJobs();

	      int[] dataTypes = { TYPE_VARCHAR60, TYPE_VARCHAR60, TYPE_VARCHAR60,
	      TYPE_VARCHAR60, TYPE_VARCHAR60, };
	      testInsertCompressionBytePerformance("500 rows of 5 VARCHAR(60)","JDPSBC064", dataTypes, 500, 0.91);
	  } catch (Exception e) {
	      failed(e, "Testing new server jobs"); 
	  } 

      }
      public void Var065() {

	  /* Run a variation where we get a fresh QZDASONIT job */
	  /* this is to assure that the client level is used to */
          /* set the server level.  A previous bug in QZDACMDP  */
          /* only set the server level based on a previous      */
          /* client level.                                      */

	  try { 
	      refreshServerJobs();

	      int[] dataTypes = { TYPE_VARCHAR60, TYPE_VARCHAR60, TYPE_VARCHAR60,
	      TYPE_VARCHAR60, TYPE_VARCHAR60, };
	      testInsertCompressionBytePerformance("500 rows of 5 VARCHAR(60)","JDPSBC065", dataTypes, 500, 0.91);
	  } catch (Exception e) {
	      failed(e, "Testing new server jobs"); 
	  } 

      }

      public void Var066() { notApplicable("non-performance variations"); }
      public void Var067() { notApplicable("non-performance variations"); }
      public void Var068() { notApplicable("non-performance variations"); }
      public void Var069() { notApplicable("non-performance variations"); }

      public void Var070() { notApplicable("non-performance variations"); }
      public void Var071() { notApplicable("non-performance variations"); }
      public void Var072() { notApplicable("non-performance variations"); }
      public void Var073() { notApplicable("non-performance variations"); }
      public void Var074() { notApplicable("non-performance variations"); }
      public void Var075() { notApplicable("non-performance variations"); }
      public void Var076() { notApplicable("non-performance variations"); }
      public void Var077() { notApplicable("non-performance variations"); }
      public void Var078() { notApplicable("non-performance variations"); }
      public void Var079() { notApplicable("non-performance variations"); }

      public void Var080() { notApplicable("non-performance variations"); }
      public void Var081() { notApplicable("non-performance variations"); }
      public void Var082() { notApplicable("non-performance variations"); }
      public void Var083() { notApplicable("non-performance variations"); }

  public void Var084() {
    if (checkBooleanSupport()) {

      try {
        refreshServerJobs();
        int[] dataTypes = { TYPE_VARCHAR60, TYPE_VARCHAR60, TYPE_VARCHAR60,
            TYPE_VARCHAR60, TYPE_BOOLEAN, };
        testInsertCompressionBytePerformance("500 rows of 4 VARCHAR(60) + BOOLEAN ",
            "JDPSBC065", dataTypes, 500, 0.91);
      } catch (Exception e) {
        failed(e, "Testing new server jobs");
      }
    }
  }

      public void Var085() { notApplicable("non-performance variations"); }
      public void Var086() { notApplicable("non-performance variations"); }
      public void Var087() { notApplicable("non-performance variations"); }
      public void Var088() { notApplicable("non-performance variations"); }
      public void Var089() { notApplicable("non-performance variations"); }

        
}

