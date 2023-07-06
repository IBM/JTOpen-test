///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetObject41N.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetObject41N.java
//
// Classes:      JDCSGetObject41N.java
//
////////////////////////////////////////////////////////////////////////



package test;

import com.ibm.as400.access.AS400;
import java.io.FileOutputStream;
import java.util.Hashtable;


/**
Testcase JDCSGetObject41.  This tests the following
method of the JDBC CallableStatement class:

     getObject()

This testcases tests when the values are registered using Types.JAVA_OBJECT.
This tests the new method for JDBC 4.1

**/
public class JDCSGetObject41N
extends JDCSGetObject41
{

/**
Constructor.
**/
    public JDCSGetObject41N (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetObject41N",
               namesAndVars, runMode, fileOutputStream,
               password);
	// Cause the named tests to be run 
	parameterName = "B"; 
    }



/* Everything else is inherited */

}
