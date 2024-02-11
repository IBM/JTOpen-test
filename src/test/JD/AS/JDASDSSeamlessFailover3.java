///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASDSSeamlessFailover3.java
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

/**
 * Testcase JDASSeamlessFailover
 **/
public class JDASDSSeamlessFailover3 extends JDASSeamlessFailover {

  /**
   * Constructor.
   **/
  public JDASDSSeamlessFailover3(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, "JDASDSSeamlessFailover3", namesAndVars, runMode,
        fileOutputStream, password, pwrSysUserID, pwrSysPassword);
    // originalPrintWriter_ = Trace.getPrintWriter();
  }


  

  /* These are the same test as in JDASClientReroute */
  /* We run them again to make sure they work with seamless */

  public void Var028() {
    notApplicable("");
  }

  public void Var029() {
    notApplicable("");
  }

  public void Var030() {
    notApplicable("");
  }

  public void Var031() {
    notApplicable("");
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with int parameters.
   **/
  public void Var032() {
    testDSCSTypeParametersSwitch(csIntTransactions, csIntParms, JAVA_INT);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with small parameters.
   **/
  public void Var033() {
    testDSCSTypeParametersSwitch(csSmallintTransactions, csSmallintParms,
        JAVA_SHORT);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with bigint parameters.
   **/
  public void Var034() {
    testDSCSTypeParametersSwitch(csBigintTransactions, csBigintParms, JAVA_LONG);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with real parameters.
   **/
  public void Var035() {
    testDSCSTypeParametersSwitch(csRealTransactions, csRealParms, JAVA_FLOAT);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with float parameters.
   **/
  public void Var036() {
    testDSCSTypeParametersSwitch(csFloatTransactions, csFloatParms, JAVA_DOUBLE);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Double parameters.
   **/
  public void Var037() {
    testDSCSTypeParametersSwitch(csDoubleTransactions, csDoubleParms, JAVA_DOUBLE);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Decimal parameters.
   **/
  public void Var038() {
    testDSCSTypeParametersSwitch(csDecimalTransactions, csDecimalParms,
        JAVA_BIGDECIMAL);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Numeric parameters.
   **/
  public void Var039() {
    testDSCSTypeParametersSwitch(csNumericTransactions, csNumericParms,
        JAVA_BIGDECIMAL);
  }
  
  
  
  
  
}
