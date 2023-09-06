///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASClientReroute4.java
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
public class JDASClientReroute4 extends JDASClientReroute
{

    
    
		/**
      Constructor.  This is called from the AS400JDBCEnableCALTest constructor.
     **/
    public JDASClientReroute4(AS400 systemObject,
        Hashtable namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password, String pwrSysUserID, String pwrSysPassword)
 {
		super(systemObject, "JDASClientReroute4", namesAndVars, runMode,
		    fileOutputStream, password, pwrSysUserID, pwrSysPassword);
		// originalPrintWriter_ = Trace.getPrintWriter();
	}


	public void Var031() { notApplicable(""); } 
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with int parameters. 
	 **/
	public void Var032() {
		testCSTypeParametersSwitch (csIntTransactions, csIntParms, JAVA_INT,30); 
	}

	
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with small parameters. 
	 **/
	public void Var033() {
		testCSTypeParametersSwitch (csSmallintTransactions, csSmallintParms, JAVA_SHORT,30); 
	}


	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with bigint parameters. 
	 **/
	public void Var034() {
		testCSTypeParametersSwitch(csBigintTransactions, csBigintParms, JAVA_LONG,30); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with real parameters. 
	 **/
	public void Var035() {
		testCSTypeParametersSwitch(csRealTransactions, csRealParms, JAVA_FLOAT,30); 
	}

	
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with float parameters. 
	 **/
	public void Var036() {
		testCSTypeParametersSwitch(csFloatTransactions, csFloatParms, JAVA_DOUBLE,30); 
	}


	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Double parameters. 
	 **/
	public void Var037() {
		testCSTypeParametersSwitch(csDoubleTransactions, csDoubleParms, JAVA_DOUBLE,30); 
	}
	
	
	


	
	
}
