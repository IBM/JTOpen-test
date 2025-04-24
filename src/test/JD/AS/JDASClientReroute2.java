///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASClientReroute2.java
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
package test.JD.AS;


import java.io.FileOutputStream;
import java.util.Hashtable; import java.util.Vector;

import com.ibm.as400.access.AS400;

/**
  Testcase JDASClientReroute
  
 **/
public class JDASClientReroute2 extends JDASClientReroute
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDASClientReroute2";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDASTest.main(newArgs); 
   }

    
    
		/**
      Constructor.  This is called from the AS400JDBCEnableCALTest constructor.
     **/
    public JDASClientReroute2(AS400 systemObject,
        Hashtable<String,Vector<String>> namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password, String pwrSysUserID, String pwrSysPassword)
 {
		super(systemObject, "JDASClientReroute2", namesAndVars, runMode,
		    fileOutputStream, password, pwrSysUserID, pwrSysPassword);
		// originalPrintWriter_ = Trace.getPrintWriter();
	}


	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Decfloat parameters. 
	 **/
	public void Var015() {
		testPSTypeParametersSwitch(psDecfloatTransactions, psDecfloatParms, JAVA_BIGDECIMAL,30); 
	}

	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Char parameters. 
	 **/
	public void Var016() {
		testPSTypeParametersSwitch(psCharTransactions, psCharParms, JAVA_STRING ,30); 
	}
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Varchar parameters. 
	 **/
	public void Var017() {
		testPSTypeParametersSwitch(psVarcharTransactions, psVarcharParms, JAVA_STRING ,30); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Graphic parameters. 
	 **/
	public void Var018() {
		testPSTypeParametersSwitch(psGraphicTransactions, psGraphicParms, JAVA_STRING ,30); 
	}
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Varchar parameters. 
	 **/
	public void Var019() {
		testPSTypeParametersSwitch(psVargraphicTransactions, psVargraphicParms, JAVA_STRING ,30); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Clob parameters. 
	 **/
	public void Var020() {
		testPSTypeParametersSwitch(psClobTransactions, psClobParms, JAVA_STRING ,30); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with DBClob parameters. 
	 **/
	public void Var021() {
		testPSTypeParametersSwitch(psDBClobTransactions, psDBClobParms, JAVA_STRING ,30); 
	}
	
	
	


	
	
}
