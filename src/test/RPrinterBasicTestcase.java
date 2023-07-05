///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RPrinterBasicTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.RPrinter;

import java.io.FileOutputStream;
import java.util.Hashtable;



/**
Testcase RPrinterBasicTestcase.  This tests the following methods
of the RPrinter class:

<ul>
<li>constructors
<li>serialization
<li>getName()
<li>setName()
</ul>
**/
public class RPrinterBasicTestcase
extends Testcase {



    // Constants.



    // Private data.
    private String printerName_;



/**
Constructor.
**/
    public RPrinterBasicTestcase (AS400 systemObject,
                                  Hashtable namesAndVars,
                                   int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys,
                              String misc)
    {
        super (systemObject, "RPrinterBasicTestcase",
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
            RPrinter u = new RPrinter();
            assertCondition ((u.getSystem() == null) && (u.getName() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
constructor() with 2 parms - Pass null for system.
**/
    public void Var002()
    {
        try {
            RPrinter u = new RPrinter(null, "TESTPrt");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
constructor() with 2 parms - Pass null for name.
**/
    public void Var003()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
constructor() with 2 parms - Pass invalid values.  This should work,
because the constructor does not check the validity.
**/
    public void Var004()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RPrinter u = new RPrinter(bogus, "BadRPRinter");
            assertCondition ((u.getSystem() == bogus) 
                    && (u.getName().equals("BadRPRinter")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
constructor() with 2 parms - Pass valid values.  Verify that the system and name are used.
**/
    public void Var005()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            assertCondition ((u.getSystem() == systemObject_) 
                    && (u.getName().equals(printerName_))
                    && (textDescription != null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
constructor() with 2 parms - Pass a lowercase printer name.  Verify that it is used.
**/
    public void Var006()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_.toLowerCase());
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            assertCondition ((u.getSystem() == systemObject_) 
                    && (u.getName().equalsIgnoreCase(printerName_)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
serialization - Verify that the properties are set and verify that its usable.
**/
    public void Var007()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            RPrinter u2 = (RPrinter)RPrintTest.serialize(u);
            String textDescription2 = (String)u2.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            assertCondition ((u2.getSystem().getSystemName().equals(systemObject_.getSystemName()))
                    && (u2.getSystem().getUserId().equals(systemObject_.getUserId()))
                    && (u2.getName().equals(printerName_))
                    && (textDescription.equals(textDescription2)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getName() - Should return null when the name has not been set.
**/
    public void Var008()
    {
        try {
            RPrinter u = new RPrinter();
            assertCondition (u.getName() == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getName() - Should return the name when the name has been set.
**/
    public void Var009()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, "lexmaRk");
            assertCondition (u.getName().equals("lexmaRk"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSystem() - Should return null when the system has not been set.
**/
    public void Var010()
    {
        try {
            RPrinter u = new RPrinter();
            assertCondition (u.getSystem() == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSystem() - Should return the system when the system has been set.
**/
    public void Var011()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, "hpdeskjet");
            assertCondition (u.getSystem().equals(systemObject_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setName() - Should throw an exception if null is passed.
**/
    public void Var012()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, "massachusetts");
            u.setName(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setName() - Set to an invalid name.  Should be reflected by getName(),
since the validity is not checked here.
**/
    public void Var013()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, "canon");
            u.setName("apple");
            assertCondition (u.getName().equals("apple"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setName() - Set to a valid name.  Should be reflected by getName() and
verify that it is used.
**/
    public void Var014()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, "hplaserjet");
            u.setName(printerName_);
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            assertCondition ((u.getName().equals(printerName_))
                    && (textDescription != null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setName() - Set to a valid lowercase name.  Should be reflected by getName() and
verify that it is used.
**/
    public void Var015()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, "xerox");
            u.setName(printerName_.toLowerCase());
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            assertCondition (u.getName().equalsIgnoreCase(printerName_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setName() - Set to a valid name after the RPrinter has made a connection.
**/
    public void Var016()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            u.setName("gateway");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
setName() - Should fire a property change event.
**/
    public void Var017()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, "kodak");
            RPrintTest.PropertyChangeListener_ pcl = new RPrintTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setName("laserPrinter");
            assertCondition ((pcl.eventCount_ == 1)
                    && (pcl.event_.getPropertyName().equals("name"))
                    && (pcl.event_.getOldValue().equals("kodak"))
                    && (pcl.event_.getNewValue().equals("laserPrinter")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setName() - Should fire a vetoable change event.
**/
    public void Var018()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, "lexmark");
            RPrintTest.VetoableChangeListener_ vcl = new RPrintTest.VetoableChangeListener_();
            u.addVetoableChangeListener(vcl);
            u.setName("hp");
            assertCondition ((vcl.eventCount_ == 1)
                    && (vcl.event_.getPropertyName().equals("name"))
                    && (vcl.event_.getOldValue().equals("lexmark"))
                    && (vcl.event_.getNewValue().equals("hp")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setName() - Should throw a PropertyVetoException if the change is vetoed.
**/
    public void Var019()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, "hp");
            RPrintTest.VetoableChangeListener_ vcl = new RPrintTest.VetoableChangeListener_(true);
            u.addVetoableChangeListener(vcl);
            u.setName("canon");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.beans.PropertyVetoException");
        }
    }



/**
setSystem() - Should throw an exception if null is passed.
**/
    public void Var020()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, "hp");
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
    public void Var021()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RPrinter u = new RPrinter(systemObject_, "xerox");
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
    public void Var022()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RPrinter u = new RPrinter(bogus, printerName_);
            u.setSystem(systemObject_);
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            assertCondition ((u.getSystem().getSystemName().equals(systemObject_.getSystemName()))
                    && (u.getSystem().getUserId().equals(systemObject_.getUserId()))
                    && (textDescription != null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Set to a valid name after the RPrinter object has made a connection.
**/
    public void Var023()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
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
    public void Var024()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RPrinter u = new RPrinter(temp1, "lexmark");
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
    public void Var025()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RPrinter u = new RPrinter(temp1, "hp");
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
    public void Var026()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RPrinter u = new RPrinter(temp1, "kodak");
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




