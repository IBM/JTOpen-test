///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASClientReroute3.java
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
public class JDASClientReroute3 extends JDASClientReroute
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDASClientReroute3";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDASTest.main(newArgs); 
   }

    
    
		/**
      Constructor.  This is called from the AS400JDBCEnableCALTest constructor.
     **/
    public JDASClientReroute3(AS400 systemObject,
        Hashtable<String,Vector<String>> namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password, String pwrSysUserID, String pwrSysPassword)
 {
		super(systemObject, "JDASClientReroute3", namesAndVars, runMode,
		    fileOutputStream, password, pwrSysUserID, pwrSysPassword);
		// originalPrintWriter_ = Trace.getPrintWriter();
	}


	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Binary parameters. 
	 **/
	public void Var022() {
		testPSTypeParametersSwitch(psBinaryTransactions, psBinaryParms, JAVA_BYTEARRAY ,30); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Varbinary parameters. 
	 **/
	public void Var023() {
		testPSTypeParametersSwitch(psVarbinaryTransactions, psVarbinaryParms, JAVA_BYTEARRAY ,30); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Blob parameters. 
	 **/
	public void Var024() {
		testPSTypeParametersSwitch(psBlobTransactions, psBlobParms, JAVA_BYTEARRAY ,30); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Date parameters. 
	 **/
	public void Var025() {
		testPSTypeParametersSwitch(psDateTransactions, psDateParms, JAVA_DATE ,30); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Date parameters. 
	 **/
	public void Var026() {
		testPSTypeParametersSwitch(psTimeTransactions, psTimeParms, JAVA_TIME,30); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Date parameters. 
	 **/
	public void Var027() {
		testPSTypeParametersSwitch(psTimestampTransactions, psTimestampParms, JAVA_TIMESTAMP,30); 
	}

	public void Var028() { notApplicable(""); } 
	public void Var029() { notApplicable(""); } 
	public void Var030() { notApplicable(""); }
	
	
	


	
	
}
