///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASSeamlessFailover2.java
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
public class JDASSeamlessFailover2 extends JDASSeamlessFailover {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDASSeamlessFailover2";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDASTest.main(newArgs); 
   }

  /**
   * Constructor.
   **/
  public JDASSeamlessFailover2(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, "JDASSeamlessFailover2", namesAndVars, runMode,
        fileOutputStream, password, pwrSysUserID, pwrSysPassword);
    // originalPrintWriter_ = Trace.getPrintWriter();
  }


  

  /* These are the same test as in JDASClientReroute */
  /* We run them again to make sure they work with seamless */

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Graphic parameters.
   **/
  public void Var018() {
    testPSTypeParametersSwitch(psGraphicTransactions, psGraphicParms,
        JAVA_STRING);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Varchar parameters.
   **/
  public void Var019() {
    testPSTypeParametersSwitch(psVargraphicTransactions, psVargraphicParms,
        JAVA_STRING);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Clob parameters.
   **/
  public void Var020() {
    testPSTypeParametersSwitch(psClobTransactions, psClobParms, JAVA_STRING);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with DBClob parameters.
   **/
  public void Var021() {
    testPSTypeParametersSwitch(psDBClobTransactions, psDBClobParms, JAVA_STRING);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Binary parameters.
   **/
  public void Var022() {
    testPSTypeParametersSwitch(psBinaryTransactions, psBinaryParms,
        JAVA_BYTEARRAY);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Varbinary parameters.
   **/
  public void Var023() {
    testPSTypeParametersSwitch(psVarbinaryTransactions, psVarbinaryParms,
        JAVA_BYTEARRAY);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Blob parameters.
   **/
  public void Var024() {
    testPSTypeParametersSwitch(psBlobTransactions, psBlobParms, JAVA_BYTEARRAY);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Date parameters.
   **/
  public void Var025() {
    testPSTypeParametersSwitch(psDateTransactions, psDateParms, JAVA_DATE);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Date parameters.
   **/
  public void Var026() {
    testPSTypeParametersSwitch(psTimeTransactions, psTimeParms, JAVA_TIME);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Date parameters.
   **/
  public void Var027() {
    testPSTypeParametersSwitch(psTimestampTransactions, psTimestampParms,
        JAVA_TIMESTAMP);
  }
  
  
  
  
  
}
