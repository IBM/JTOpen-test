///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSStressScrollPS.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;

import java.util.Hashtable;



/**
Testcase JDRSStress.  This tests various combinations of datatypes in a
table.  There is one generic testcase that is run.  Specific testcases
are created for error condition that needed to be fixed.  Otherwise,
the test will run for a specific amount of time to try to detect errors.
**/
public class JDRSStressScrollPS
extends JDRSStress
{

/**
Constructor.
**/
    public JDRSStressScrollPS (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSStressScrollPS",
            namesAndVars, runMode, fileOutputStream,
            password);
	usePreparedStatement=true;
	useScrollableCursor=true; 
    }

}

