///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSStressPSDirectMap.java
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
public class JDRSStressPSDirectMap
extends JDRSStress
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSStressPSDirectMap";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSStressTest.main(newArgs); 
   }

/**
Constructor.
**/
    public JDRSStressPSDirectMap (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSStressPSDirectMap",
            namesAndVars, runMode, fileOutputStream,
            password);
	connectionProperties = ";direct map=true;date format=iso;time format=jis";
        usePreparedStatement=true;
        directMap=true; 
    }

}

