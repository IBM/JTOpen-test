///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASDSClientReroute5.java
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
public class JDASDSClientReroute5 extends JDASClientReroute
{

    
    
		/**
      Constructor.  This is called from the AS400JDBCEnableCALTest constructor.
     **/
    public JDASDSClientReroute5(AS400 systemObject,
        Hashtable namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password, String pwrSysUserID, String pwrSysPassword)
 {
		super(systemObject, "JDASDSClientReroute5", namesAndVars, runMode,
		    fileOutputStream, password, pwrSysUserID, pwrSysPassword);
		// originalPrintWriter_ = Trace.getPrintWriter();
	}


	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Decimal parameters. 
	 **/
	public void Var038() {
		testDSCSTypeParametersSwitch(csDecimalTransactions, csDecimalParms, JAVA_BIGDECIMAL,30); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Numeric parameters. 
	 **/
	public void Var039() {
		testDSCSTypeParametersSwitch(csNumericTransactions, csNumericParms, JAVA_BIGDECIMAL,30); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Decfloat parameters. 
	 **/
	public void Var040() {
		testDSCSTypeParametersSwitch(csDecfloatTransactions, csDecfloatParms, JAVA_BIGDECIMAL,30); 
	}

	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Char parameters. 
	 **/
	public void Var041() {
		testDSCSTypeParametersSwitch(csCharTransactions, csCharParms, JAVA_STRING ,30); 
	}
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Varchar parameters. 
	 **/
	public void Var042() {
		testDSCSTypeParametersSwitch(csVarcharTransactions, csVarcharParms, JAVA_STRING ,30); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Graphic parameters. 
	 **/
	public void Var043() {
		testDSCSTypeParametersSwitch(csGraphicTransactions, csGraphicParms, JAVA_STRING ,30); 
	}
	
	
	


	
	
}
