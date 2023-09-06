///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASDSClientReroute7.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////
//
//  File Name: AS400JDBCEnableCALTestcase.java
//  Classes:   AS400JDBCEnableCALTestcase
//
////////////////////////////////////////////////////////////////////////////
// 
////////////////////////////////////////////////////////////////////////////
package test;


import java.io.FileOutputStream;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

/**
  Testcase JDASClientReroute
  
 **/
public class JDASDSClientReroute7 extends JDASClientReroute
{

    
    
		/**
      Constructor.  This is called from the AS400JDBCEnableCALTest constructor.
     **/
    public JDASDSClientReroute7(AS400 systemObject,
        Hashtable namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password, String pwrSysUserID, String pwrSysPassword)
 {
		super(systemObject, "JDASDSClientReroute7", namesAndVars, runMode,
		    fileOutputStream, password, pwrSysUserID, pwrSysPassword);
		// originalPrintWriter_ = Trace.getPrintWriter();
	}


	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Date parameters. 
	 **/
	public void Var050() {
		testDSCSTypeParametersSwitch(csDateTransactions, csDateParms, JAVA_DATE ,30); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Date parameters. 
	 **/
	public void Var051() {
		testDSCSTypeParametersSwitch(csTimeTransactions, csTimeParms, JAVA_TIME ,30); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Date parameters. 
	 **/
	public void Var052() {
		testCSTypeParametersSwitch(csTimestampTransactions, csTimestampParms, JAVA_TIMESTAMP ,30); 
	}
	
	
	


	
	
}
