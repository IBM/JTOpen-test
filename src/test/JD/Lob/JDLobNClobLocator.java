///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobNClobLocator.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.Lob;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDLobNClobLocator.  This tests the following method
of the JDBC NClob class:

<ul>
<li>getAsciiStream()
<li>getCharacterStream()
<li>getSubString()
<li>length()
<li>position()
<li>setString() //@C2A
<li>setAsciiStream() //@C2A
<li>setCharacterStream() //@C2A
<li>truncate() //@C2A
<li>free() //@pda
</ul>
**/
public class JDLobNClobLocator
extends JDLobClobLocator
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDLobNClobLocator";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDLobTest.main(newArgs); 
   }

    boolean runningJ9 = false;


/**
Constructor.
**/
    public JDLobNClobLocator (AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             String password)
    {
        super (systemObject, "JDLobNClobLocator",
               namesAndVars, runMode, fileOutputStream,
               password);
	getMethod    = "getNClob"; 
	updateMethod = "updateNClob"; 
	setMethod    = "setNClob"; 
	requireJdbc40 = true; 
	suffix="X";
    }


    public JDLobNClobLocator (AS400 systemObject,
			     String testname, 	 
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             String password)
    {
        super (systemObject, testname,
               namesAndVars, runMode, fileOutputStream,
               password);
	lobThreshold = "1"; 
	getMethod    = "getNClob"; 
	updateMethod = "updateNClob"; 
	setMethod    = "setNClob"; 
	requireJdbc40 = true; 
    }

/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
        if (suffix.equals("")) suffix="X";
	super.setup();
	getMethod    = "getNClob"; 
	updateMethod = "updateNClob"; 
	setMethod    = "setNClob"; 

    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
	super.cleanup(); 
    }










/**
 **
 ** All TESTCASES inherited from superclass JDlobClobLocator
 **/





}
