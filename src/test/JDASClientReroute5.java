///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASClientReroute5.java
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
public class JDASClientReroute5 extends JDASClientReroute
{

    
    
		/**
      Constructor.  This is called from the AS400JDBCEnableCALTest constructor.
     **/
    public JDASClientReroute5(AS400 systemObject,
        Hashtable namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password, String pwrSysUserID, String pwrSysPassword)
 {
		super(systemObject, "JDASClientReroute5", namesAndVars, runMode,
		    fileOutputStream, password, pwrSysUserID, pwrSysPassword);
		// originalPrintWriter_ = Trace.getPrintWriter();
	}


	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Decimal parameters. 
	 **/
	public void Var038() {
		testCSTypeParametersSwitch(csDecimalTransactions, csDecimalParms, JAVA_BIGDECIMAL,30); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Numeric parameters. 
	 **/
	public void Var039() {
		testCSTypeParametersSwitch(csNumericTransactions, csNumericParms, JAVA_BIGDECIMAL,30); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Decfloat parameters. 
	 **/
	public void Var040() {
		testCSTypeParametersSwitch(csDecfloatTransactions, csDecfloatParms, JAVA_BIGDECIMAL,30); 
	}

	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Char parameters. 
	 **/
	public void Var041() {
		testCSTypeParametersSwitch(csCharTransactions, csCharParms, JAVA_STRING ,30); 
	}
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Varchar parameters. 
	 **/
	public void Var042() {
		testCSTypeParametersSwitch(csVarcharTransactions, csVarcharParms, JAVA_STRING ,30); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Graphic parameters. 
	 **/
	public void Var043() {
		testCSTypeParametersSwitch(csGraphicTransactions, csGraphicParms, JAVA_STRING ,30); 
	}
	
	
	


	
	
}
