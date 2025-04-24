///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASDSClientReroute6.java
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
public class JDASDSClientReroute6 extends JDASClientReroute
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDASDSClientReroute6";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDASTest.main(newArgs); 
   }

    
    
		/**
      Constructor.  This is called from the AS400JDBCEnableCALTest constructor.
     **/
    public JDASDSClientReroute6(AS400 systemObject,
        Hashtable<String,Vector<String>> namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password, String pwrSysUserID, String pwrSysPassword)
 {
		super(systemObject, "JDASDSClientReroute6", namesAndVars, runMode,
		    fileOutputStream, password, pwrSysUserID, pwrSysPassword);
		// originalPrintWriter_ = Trace.getPrintWriter();
	}


	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Clob parameters. 
	 **/
	public void Var045() {
		testDSCSTypeParametersSwitch(csClobTransactions, csClobParms, JAVA_STRING ,30); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with DBClob parameters. 
	 **/
	public void Var046() {
		testDSCSTypeParametersSwitch(csDBClobTransactions, csDBClobParms, JAVA_STRING ,30); 
	}
	

	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Binary parameters. 
	 **/
	public void Var047() {
		testDSCSTypeParametersSwitch(csBinaryTransactions, csBinaryParms, JAVA_BYTEARRAY ,30); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Varbinary parameters. 
	 **/
	public void Var048() {
		testDSCSTypeParametersSwitch(csVarbinaryTransactions, csVarbinaryParms, JAVA_BYTEARRAY ,30); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly switched. 
	 * Tests preparedStatements organized as transactions with Blob parameters. 
	 **/
	public void Var049() {
		testDSCSTypeParametersSwitch(csBlobTransactions, csBlobParms, JAVA_BYTEARRAY ,30); 
	}
	
	
	


	
	
}
