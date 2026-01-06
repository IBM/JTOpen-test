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

package test.JD.Lob;


import java.io.FileOutputStream;
import java.util.Hashtable; import java.util.Vector;

import com.ibm.as400.access.AS400;



/**
Testcase JDLobClob8 is nonlocator version of JDLobClobLocator8.
See JDLobClobLocation8 for details of test.
**/
public class JDLobClob8 extends JDLobClobLocator8
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDLobClob8";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDLobTest.main(newArgs); 
   }


    /**
     Constructor.
    **/
    public JDLobClob8 (AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             String password)
    {
        super (systemObject, "JDLobClob8",
               namesAndVars, runMode, fileOutputStream,
               password);
	lobThreshold="1000000000"; 
	output_.println("JDLobClob8 set lob threshold to "+lobThreshold); 
    }


    public void Var240() {	notApplicable("JDLobClob8 Locator test");     }
    public void Var241() {	notApplicable("JDLobClob8 Locator test");     }
    public void Var242() {	notApplicable("JDLobClob8 Locator test");     }
    public void Var243() {	notApplicable("JDLobClob8 Locator test");     }
    public void Var257() {	notApplicable("JDLobClob8 Locator test");     }


}
