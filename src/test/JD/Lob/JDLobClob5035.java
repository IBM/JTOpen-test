///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobClob5035.java
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
Testcase JDLobClob5035 is nonlocator version of JDLobClobLocator5035.
See JDLobClobLocation5035 for details of test.
**/
public class JDLobClob5035 extends JDLobClobLocator5035
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDLobClob5035";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDLobTest.main(newArgs); 
   }


    /**
     Constructor.
    **/
    public JDLobClob5035 (AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             String password)
    {
        super (systemObject, "JDLobClob5035",
               namesAndVars, runMode, fileOutputStream,
               password);
	lobThreshold="1000000000"; 
	suffix="K";
    }



    public void Var005() {
	super.Var005(); 
    }

    public void Var006() {
	super.Var006(); 
    }


    public void Var019() {
	super.Var019(); 
    }

    public void Var020() {
	super.Var020(); 
    }

    public void Var024() {
	super.Var024(); 
    }

    public void Var039() {
	super.Var039(); 
    }

    public void Var061() {
	super.Var061(); 
    }

    public void Var063() {
	super.Var063(); 
    }


    public void Var110() {
	super.Var110(); 
    }

    public void Var111() {
	super.Var111(); 
    }

    public void Var112() {
	super.Var112(); 
    }

    public void Var113() {
	super.Var113(); 
    }


    public void Var115() {
	super.Var115(); 
    }

    public void Var116() {
	super.Var116(); 
    }

}
