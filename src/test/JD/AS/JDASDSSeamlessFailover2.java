///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASDSSeamlessFailover2.java
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
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDASSeamlessFailover;

/**
 * Testcase JDASSeamlessFailover
 **/
public class JDASDSSeamlessFailover2 extends JDASSeamlessFailover {

  /**
   * Constructor.
   **/
  public JDASDSSeamlessFailover2(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, "JDASDSSeamlessFailover2", namesAndVars, runMode,
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
    testDSPSTypeParametersSwitch(psGraphicTransactions, psGraphicParms,
        JAVA_STRING);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Varchar parameters.
   **/
  public void Var019() {
    testDSPSTypeParametersSwitch(psVargraphicTransactions, psVargraphicParms,
        JAVA_STRING);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Clob parameters.
   **/
  public void Var020() {
    testDSPSTypeParametersSwitch(psClobTransactions, psClobParms, JAVA_STRING);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with DBClob parameters.
   **/
  public void Var021() {
    testDSPSTypeParametersSwitch(psDBClobTransactions, psDBClobParms, JAVA_STRING);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Binary parameters.
   **/
  public void Var022() {
    testDSPSTypeParametersSwitch(psBinaryTransactions, psBinaryParms,
        JAVA_BYTEARRAY);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Varbinary parameters.
   **/
  public void Var023() {
    testDSPSTypeParametersSwitch(psVarbinaryTransactions, psVarbinaryParms,
        JAVA_BYTEARRAY);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Blob parameters.
   **/
  public void Var024() {
    testDSPSTypeParametersSwitch(psBlobTransactions, psBlobParms, JAVA_BYTEARRAY);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Date parameters.
   **/
  public void Var025() {
    testDSPSTypeParametersSwitch(psDateTransactions, psDateParms, JAVA_DATE);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Date parameters.
   **/
  public void Var026() {
    testDSPSTypeParametersSwitch(psTimeTransactions, psTimeParms, JAVA_TIME);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Date parameters.
   **/
  public void Var027() {
    testDSPSTypeParametersSwitch(psTimestampTransactions, psTimestampParms,
        JAVA_TIMESTAMP);
  }
  
  
  
  
  
}
