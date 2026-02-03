///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDPSBatchNative.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDPSBatchNative.java
//
// Classes:      JDPSBatchNatvie 
//
////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.PS;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDPSBatchNative.  This is the same test as JDPSBatch.java, but runs with the
native property "use block insert" set, if the native driver is used.
**/
public class JDPSBatchNative
extends JDPSBatch {

/**
Constructor.
**/
    public JDPSBatchNative (AS400 systemObject,
                      Hashtable<String,Vector<String>> namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password)
    {
        super (systemObject, "JDPSBatchNative",
               namesAndVars, runMode, fileOutputStream,
               password);
	useBlockInsert = true;						// @A1
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {

	baseURL_ += ";use block insert=true";	
	super.setup("N");
    }


//
// All remaining testcases are inherited --- Yea...
// 

}



