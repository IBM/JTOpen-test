///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetNCharacterStream.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.util.Hashtable;



/**
Testcase JDRSGetNCharacterStream.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getNCharacterStream()
</ul>
**/
public class JDRSGetNCharacterStream
extends JDRSGetCharacterStream
{






/**
Constructor.
**/
    public JDRSGetNCharacterStream (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSGetNCharacterStream",
            namesAndVars, runMode, fileOutputStream,
            password);

	 methodName = "getNCharacterStream"; 

    }



    boolean checkLevel() {

	if (getDriver() == JDTestDriver.DRIVER_JCC) {
	    notApplicable("JCC does not support getNCharacterStream ");
	    return false ; 
	}
	return checkJdbc40 (); 
    } 

    boolean isLevel() {
	return isJdbc40(); 
    } 


    /* Everything else inherited */



}




