///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobClob8.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.io.FileOutputStream;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;



/**
Testcase JDLobClob8 is nonlocator version of JDLobClobLocator8.
See JDLobClobLocation8 for details of test.
**/
public class JDLobClob8 extends JDLobClobLocator8
{


    /**
     Constructor.
    **/
    public JDLobClob8 (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             String password)
    {
        super (systemObject, "JDLobClob8",
               namesAndVars, runMode, fileOutputStream,
               password);
	lobThreshold="1000000000"; 
	System.out.println("JDLobClob8 set lob threshold to "+lobThreshold); 
    }


    public void Var240() {	notApplicable("JDLobClob8 Locator test");     }
    public void Var241() {	notApplicable("JDLobClob8 Locator test");     }
    public void Var242() {	notApplicable("JDLobClob8 Locator test");     }
    public void Var243() {	notApplicable("JDLobClob8 Locator test");     }
    public void Var257() {	notApplicable("JDLobClob8 Locator test");     }


}
