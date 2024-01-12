///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RPrinterListBasicTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RPrint;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.ResourceException;

import test.RPrintTest;
import test.Testcase;
import test.RPrintTest.PropertyChangeListener_;
import test.RPrintTest.VetoableChangeListener_;

import com.ibm.as400.resource.RPrinter;
import com.ibm.as400.resource.RPrinterList;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase RPrinterListBasicTestcase.  This tests the following methods
of the RPrinter class:

<ul>
<li>constructors
<li>serialization
<li>getSystem()
<li>setSystem()
</ul>
**/
public class RPrinterListBasicTestcase
extends Testcase {



    // Constants.



    // Private data.
    private String printerName_;



/**
Constructor.
**/
    public RPrinterListBasicTestcase (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys,
                              String misc)
    {
        super (systemObject, "RPrinterListBasicTestcase",
               namesAndVars, runMode, fileOutputStream,
               password);
        pwrSys_ = pwrSys;
        printerName_ = misc;

        if (pwrSys == null)
            throw new IllegalStateException("ERROR: Please specify a power system via -pwrsys.");
        if (misc == null)
            throw new IllegalStateException("ERROR: Please specify a printer via -misc.");
    }



/**
constructor() with 0 parms - Should work.
**/
    public void Var001()
    {
        try {
            RPrinterList ul = new RPrinterList();
            assertCondition(ul.getSystem() == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
constructor() with 1 parm - Pass null for system.
**/
    public void Var002()
    {
        try {
            RPrinterList ul = new RPrinterList(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
constructor() with 1 parm - Should work.
**/
    public void Var003()
    {
        try {            
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.open();
            ul.waitForComplete();
            assertCondition((ul.getSystem() == pwrSys_) && (ul.getListLength() > 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
serialization - Verify that the properties are set and verify that its usable.
**/
    public void Var004()
    {
        try {
            RPrinterList ul1 = new RPrinterList(pwrSys_);
            ul1.setSelectionValue(RPrinterList.PRINTER_NAMES, printerName_);
            RPrinterList ul2 = (RPrinterList)RPrintTest.serialize(ul1);
            assertCondition ((ul2.getSystem().getSystemName().equals(pwrSys_.getSystemName()))
                    && (((String[])ul2.getSelectionValue(RPrinterList.PRINTER_NAMES))[0].equals(printerName_)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSystem() - Should return null when the system has not been set.
**/
    public void Var005()
    {
        try {
            RPrinterList ul = new RPrinterList();
            assertCondition (ul.getSystem() == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSystem() - Should return the system when the system has been set.
**/
    public void Var006()
    {
        try {
            RPrinterList ul = new RPrinterList(systemObject_);
            assertCondition (ul.getSystem() == systemObject_);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Should throw an exception if null is passed.
**/
    public void Var007()
    {
        try {
            RPrinterList u = new RPrinterList(systemObject_);
            u.setSystem(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setSystem() - Set to an invalid system.  Should be reflected by getSystem(),
since the validity is not checked here.
**/
    public void Var008()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RPrinterList u = new RPrinterList(systemObject_);
            u.setSystem(bogus);
            assertCondition ((u.getSystem().getSystemName().equals("bogus"))
                    && (u.getSystem().getUserId().equalsIgnoreCase("bogus")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Set to a valid name.  Should be reflected by getSystem() and
verify that it is used.
**/
    public void Var009()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RPrinterList u = new RPrinterList(bogus);
            u.setSystem(pwrSys_);
            u.open();
            RPrinter user = (RPrinter)u.resourceAt(0);
            u.close();
            assertCondition ((u.getSystem().getSystemName().equals(pwrSys_.getSystemName()))
                    && (u.getSystem().getUserId().equals(pwrSys_.getUserId()))
                    && (user != null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Set to a valid name after the RPrinter object has made a connection.
**/
    public void Var010()
    {
        try {
            RPrinterList u = new RPrinterList(pwrSys_);
            u.open();
            u.setSystem(systemObject_);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
setSystem() - Should fire a property change event.
**/
    public void Var011()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RPrinterList u = new RPrinterList(temp1);
            RPrintTest.PropertyChangeListener_ pcl = new RPrintTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setSystem(temp2);
            assertCondition ((pcl.eventCount_ == 1)
                    && (pcl.event_.getPropertyName().equals("system"))
                    && (pcl.event_.getOldValue().equals(temp1))
                    && (pcl.event_.getNewValue().equals(temp2)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Should fire a vetoable change event.
**/
    public void Var012()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RPrinterList u = new RPrinterList(temp1);
            RPrintTest.VetoableChangeListener_ vcl = new RPrintTest.VetoableChangeListener_();
            u.addVetoableChangeListener(vcl);
            u.setSystem(temp2);
            assertCondition ((vcl.eventCount_ == 1)
                    && (vcl.event_.getPropertyName().equals("system"))
                    && (vcl.event_.getOldValue().equals(temp1))
                    && (vcl.event_.getNewValue().equals(temp2)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Should throw a PropertyVetoException if the change is vetoed.
**/
    public void Var013()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RPrinterList u = new RPrinterList(temp1);
            RPrintTest.VetoableChangeListener_ vcl = new RPrintTest.VetoableChangeListener_(true);
            u.addVetoableChangeListener(vcl);
            u.setSystem(temp2);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.beans.PropertyVetoException");
        }
    }



}




