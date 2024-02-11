///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASDSSeamlessFailover4.java
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
public class JDASDSSeamlessFailover4 extends JDASSeamlessFailover {

  /**
   * Constructor.
   **/
  public JDASDSSeamlessFailover4(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String pwrSysUserID, String pwrSysPassword) {
    super(systemObject, "JDASDSSeamlessFailover4", namesAndVars, runMode,
        fileOutputStream, password, pwrSysUserID, pwrSysPassword);
    // originalPrintWriter_ = Trace.getPrintWriter();
  }


  

  /* These are the same test as in JDASClientReroute */
  /* We run them again to make sure they work with seamless */

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Decfloat parameters.
   **/
  public void Var040() {
    testDSCSTypeParametersSwitch(csDecfloatTransactions, csDecfloatParms,
        JAVA_BIGDECIMAL);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Char parameters.
   **/
  public void Var041() {
    testDSCSTypeParametersSwitch(csCharTransactions, csCharParms, JAVA_STRING);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Varchar parameters.
   **/
  public void Var042() {
    testDSCSTypeParametersSwitch(csVarcharTransactions, csVarcharParms,
        JAVA_STRING);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Graphic parameters.
   **/
  public void Var043() {
    testDSCSTypeParametersSwitch(csGraphicTransactions, csGraphicParms,
        JAVA_STRING);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Varchar parameters.
   **/
  public void Var044() {
    testDSCSTypeParametersSwitch(csVargraphicTransactions, csVargraphicParms,
        JAVA_STRING);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Clob parameters.
   **/
  public void Var045() {
    testDSCSTypeParametersSwitch(csClobTransactions, csClobParms, JAVA_STRING);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with DBClob parameters.
   **/
  public void Var046() {
    testDSCSTypeParametersSwitch(csDBClobTransactions, csDBClobParms, JAVA_STRING);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Binary parameters.
   **/
  public void Var047() {
    testDSCSTypeParametersSwitch(csBinaryTransactions, csBinaryParms,
        JAVA_BYTEARRAY);
  }

  /**
   * Run a test for 60 seconds where the connection is randomly switched. Tests
   * preparedStatements organized as transactions with Varbinary parameters.
   **/
  public void Var048() {
    testDSCSTypeParametersSwitch(csVarbinaryTransactions, csVarbinaryParms,
        JAVA_BYTEARRAY);
  }
  
  
  
  
  
}
