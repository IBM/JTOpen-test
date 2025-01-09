///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSUpdateDBDefault.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;


import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;



/**
Testcase JDRSUpdateDBDefault.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>updateDBDefault()
</ul>
**/
public class JDRSUpdateDBDefault
extends JDRSUpdateDB2Default
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSUpdateDBDefault";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



/**
Constructor.
**/
    public JDRSUpdateDBDefault (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSUpdateDBDefault",
            namesAndVars, runMode, fileOutputStream,
            password);
	methodName = "updateDBDefault"; 
    }


/*
 * All variations inherited
 */


}


