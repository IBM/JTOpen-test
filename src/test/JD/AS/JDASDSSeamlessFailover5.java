///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASDSSeamlessFailover5.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////
//
//  File Name: AS400JDBCSeamlessFailover.java
//  Classes:   AS400JDBCSeamlessFailover
//
////////////////////////////////////////////////////////////////////////////
// 
////////////////////////////////////////////////////////////////////////////
package test.JD.AS;


import java.io.FileOutputStream;
import java.util.Hashtable; import java.util.Vector;


import com.ibm.as400.access.AS400;



/**
 * Testcase JDASSeamlessFailover
 **/
public class JDASDSSeamlessFailover5 extends JDASSeamlessFailover {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDASDSSeamlessFailover5";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDASTest.main(newArgs); 
   }

  /**
   * Constructor.
   **/
  public JDASDSSeamlessFailover5(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, "JDASDSSeamlessFailover5", namesAndVars, runMode,
        fileOutputStream, password, pwrSysUserID, pwrSysPassword);
    // originalPrintWriter_ = Trace.getPrintWriter();
  }


  

  /* These are the same test as in JDASClientReroute */
  /* We run them again to make sure they work with seamless */

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Blob parameters.
   **/
  public void Var049() {
    testDSCSTypeParametersSwitch(csBlobTransactions, csBlobParms, JAVA_BYTEARRAY);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Date parameters.
   **/
  public void Var050() {
    testDSCSTypeParametersSwitch(csDateTransactions, csDateParms, JAVA_DATE);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Date parameters.
   **/
  public void Var051() {
    testDSCSTypeParametersSwitch(csTimeTransactions, csTimeParms, JAVA_TIME);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Date parameters.
   **/
  public void Var052() {
    testDSCSTypeParametersSwitch(csTimestampTransactions, csTimestampParms,
        JAVA_TIMESTAMP);
  }

  public void Var053() {
    notApplicable();
  }

  public void Var054() {
    notApplicable();
  }

  public void Var055() {
    notApplicable();
  }

  public void Var056() {
    notApplicable();
  }

  public void Var057() {
    notApplicable();
  }

  public void Var058() {
    notApplicable();
  }

  public void Var059() {
    notApplicable();
  }

  public void Var060() {
    notApplicable();
  }

  public void Var061() {
    notApplicable();
  }
    
  
  
}
