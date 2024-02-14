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
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

import test.JDTestDriver;



/**
Testcase JDLobClob5035 is nonlocator version of JDLobClobLocator5035.
See JDLobClobLocation5035 for details of test.
**/
public class JDLobClob5035 extends JDLobClobLocator5035
{


    /**
     Constructor.
    **/
    public JDLobClob5035 (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             String password)
    {
        super (systemObject, "JDLobClob5035",
               namesAndVars, runMode, fileOutputStream,
               password);
	lobThreshold="1000000000"; 
    }



    public void Var005() {
	if (getDriver()== JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in V5R3 added by native driver 10/26/2006"); 
	} else {
	    super.Var005(); 
	} 
    }

    public void Var006() {
	if (getDriver()== JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in V5R3 added by native driver 10/26/2006"); 
	} else {
	    super.Var006(); 
	} 
    }


    public void Var019() {
	if (getDriver()== JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in V5R3 added by native driver 10/26/2006"); 
	} else {
	    super.Var019(); 
	} 
    }

    public void Var020() {
	if (getDriver()== JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in V5R3 added by native driver 10/26/2006"); 
	} else {
	    super.Var020(); 
	} 
    }

    public void Var024() {
	if (getDriver()== JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in V5R3 added by native driver 10/26/2006"); 
	} else {
	    super.Var024(); 
	} 
    }

    public void Var039() {
	if (getDriver()== JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in V5R3 added by native driver 10/26/2006"); 
	} else {
	    super.Var039(); 
	} 
    }

    public void Var061() {
	if (getDriver()== JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in V5R3 added by native driver 10/26/2006"); 
	} else {
	    super.Var061(); 
	} 
    }

    public void Var063() {
	if (getDriver()== JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in V5R3 added by native driver 10/26/2006"); 
	} else {
	    super.Var063(); 
	} 
    }


    public void Var110() {
	if (getDriver()== JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in V5R3 added by native driver 10/26/2006"); 
	} else {
	    super.Var110(); 
	} 
    }

    public void Var111() {
	if (getDriver()== JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in V5R3 added by native driver 10/26/2006"); 
	} else {
	    super.Var111(); 
	} 
    }

    public void Var112() {
	if (getDriver()== JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in V5R3 added by native driver 10/26/2006"); 
	} else {
	    super.Var112(); 
	} 
    }

    public void Var113() {
	if (getDriver()== JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in V5R3 added by native driver 10/26/2006"); 
	} else {
	    super.Var113(); 
	} 
    }


    public void Var115() {
	if (getDriver()== JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in V5R3 added by native driver 10/26/2006"); 
	} else {
	    super.Var115(); 
	} 
    }

    public void Var116() {
	if (getDriver()== JDTestDriver.DRIVER_NATIVE && getRelease() == JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Not working in V5R3 added by native driver 10/26/2006"); 
	} else {
	    super.Var116(); 
	} 
    }

}
