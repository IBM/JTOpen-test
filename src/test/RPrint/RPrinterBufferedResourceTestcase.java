///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RPrinterBufferedResourceTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RPrint;

import java.awt.Image;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.Presentation;
import com.ibm.as400.resource.RPrinter;
import com.ibm.as400.resource.ResourceEvent;

import test.RPrintTest;
import test.Testcase;



/**
Testcase RPrinterBufferedResourceTestcase.  This tests the following methods
of the RPrinter class, inherited from BufferedResource:

<ul>
<li>addPropertyChangeListener() 
<li>addResourceListener() 
<li>addVetoableChangeListener() 
<li>equals() 
<li>getPresentation() 
<li>getResourceKey() 
<li>removePropertyChangeListener() 
<li>removeResourceListener() 
<li>removeVetoableChangeListener() 
<li>toString() 
</ul>
**/
@SuppressWarnings("deprecation")
public class RPrinterBufferedResourceTestcase
extends Testcase {



    // Constants.



    // Private data.
    private String printerName_;



/**
Constructor.
**/
    public RPrinterBufferedResourceTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys,
                              String misc)
    {
        super (systemObject, "RPrinterBufferedResourceTestcase",
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
addPropertyChangeListener() - Pass null.
**/
    public void Var001()
    {
        try {
            RPrinter u = new RPrinter();
            u.addPropertyChangeListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
addPropertyChangeListener() - Pass a listener.
**/
    public void Var002()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RPrinter u = new RPrinter(temp1, "washington");
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
addResourceListener() - Pass null.
**/
    public void Var003()
    {
        try {
            RPrinter u = new RPrinter();
            u.addResourceListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
addResourceListener() - Pass a listener.
**/
    public void Var004()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            RPrinter u = new RPrinter(temp1, "oregon");
            u.setAttributeValue(RPrinter.TEXT_DESCRIPTION, "Microsoft");
            RPrintTest.ResourceListener_ rl = new RPrintTest.ResourceListener_();
            u.addResourceListener(rl);
            u.setAttributeValue(RPrinter.TEXT_DESCRIPTION, "Sun");
            assertCondition ((rl.eventCount_ == 1)
                    && (rl.event_.getID() == ResourceEvent.ATTRIBUTE_VALUE_CHANGED)
                    && (rl.event_.getAttributeID().equals(RPrinter.TEXT_DESCRIPTION))
                    && (rl.event_.getValue().equals("Sun")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
addVetoableChangeListener() - Pass null.
**/
    public void Var005()
    {
        try {
            RPrinter u = new RPrinter();
            u.addVetoableChangeListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
addVetoableChangeListener() - Pass a listener.
**/
    public void Var006()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RPrinter u = new RPrinter(temp1, "oregon");
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
equals() - Verify that a RPrinter is not equal to null.
**/
    public void Var007()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(null) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RPrinter is not equal to something that is not a RPrinter.
**/
    @SuppressWarnings("unlikely-arg-type")
    public void Var008()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(new Date()) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RPrinter is not equal to another RPrinter that it should not be.
**/
    public void Var009()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, systemObject_.getUserId());
            RPrinter u2 = new RPrinter(systemObject_, "FRANK");
            assertCondition(u.equals(u2) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RPrinter is equal to itself.
**/
    public void Var010()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(u) == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RPrinter is equal to another RPrinter representing the same user,
before either has been used.  The resource keys have not been set, so this should
be false.
**/
    public void Var011()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, systemObject_.getUserId());
            RPrinter u2 = new RPrinter(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(u2) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
equals() - Verify that a RPrinter is equal to another RPrinter representing the same user,
after both have been used.  The resource keys should be set, and this should be
true.
**/
    public void Var012()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);            
            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            u2.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            assertCondition(u.equals(u2) == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPresentation() - Verify that the presentation has default information
for a RPrinter whose properties have not been set.
**/
    public void Var013()
    {
        try {
            RPrinter u = new RPrinter();            
            Presentation p = u.getPresentation();
            String name = p.getName();
            String fullName = p.getFullName();
            String descriptionText = (String)p.getValue(Presentation.DESCRIPTION_TEXT);
            String helpText = (String)p.getValue(Presentation.HELP_TEXT);
            Image iconColor16 = (Image)p.getValue(Presentation.ICON_COLOR_16x16);
            Image iconColor32 = (Image)p.getValue(Presentation.ICON_COLOR_32x32);
            String asString = p.toString();
            assertCondition((name.equals(""))
                   && (fullName.equals(""))
                   && (descriptionText != null) // Can't compare directly, this is MRI.
                   && (helpText == null)
                   && ((isApplet_) || (iconColor16 != null))    // Applets don't always load gifs.
                   && ((isApplet_) || (iconColor32 != null))    // Applets don't always load gifs.
                   && (asString.equals("")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPresentation() - Verify that the presentation has default information
for a RPrinter whose properties have been set and used.
**/
    public void Var014()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            Presentation p = u.getPresentation();
            String name = p.getName();
            String fullName = p.getFullName();
            String descriptionText = (String)p.getValue(Presentation.DESCRIPTION_TEXT);
            String helpText = (String)p.getValue(Presentation.HELP_TEXT);
            Image iconColor16 = (Image)p.getValue(Presentation.ICON_COLOR_16x16);
            Image iconColor32 = (Image)p.getValue(Presentation.ICON_COLOR_32x32);
            String asString = p.toString();
            assertCondition((name.equals(printerName_))
                   && (fullName.equals(printerName_))
                   && (descriptionText != null) // Can't compare directly, this is MRI.
                   && (helpText == null)
                   && ((isApplet_) || (iconColor16 != null))    // Applets don't always load gifs.
                   && ((isApplet_) || (iconColor32 != null))    // Applets don't always load gifs.
                   && (asString.equals(printerName_)), "textDescription="+textDescription);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPresentation() - Verify that passing null to the Presentation
throws an exception.
**/
    public void Var015()
    {
        try {
            RPrinter u = new RPrinter();            
            Presentation p = u.getPresentation();
            p.getValue(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getPresentation() - Verify that asking the Presentation for
bogus information throws an exception.
**/
    public void Var016()
    {
        try {
            RPrinter u = new RPrinter();            
            Presentation p = u.getPresentation();
            assertCondition(p.getValue("THIS IS NOT RIGHT") == null);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResourceKey() - For a RPrinter whose properties have not been set,
the resource key should be null.
**/
    public void Var017()
    {
        try {
            RPrinter u = new RPrinter();  
            assertCondition(u.getResourceKey() == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResourceKey() - For a RPrinter whose properties have been set and used,
the resource key should be set.
**/
    public void Var018()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);

            StringBuffer buffer = new StringBuffer();
            buffer.append(RPrinter.class);
            buffer.append(':');
            buffer.append(systemObject_.getSystemName());
            buffer.append(':');
            buffer.append(systemObject_.getUserId());
            buffer.append(':');
            buffer.append(printerName_);
            String expected = buffer.toString();

            assertCondition(u.getResourceKey().equals(expected), "textDescription="+textDescription);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
removePropertyChangeListener() - Pass null.
**/
    public void Var019()
    {
        try {
            RPrinter u = new RPrinter();
            u.removePropertyChangeListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removePropertyChangeListener() - Pass a listener that had never been added.
**/
    public void Var020()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RPrinter u = new RPrinter(temp1, "california");
            RPrintTest.PropertyChangeListener_ pcl = new RPrintTest.PropertyChangeListener_();
            u.removePropertyChangeListener(pcl);
            u.setSystem(temp2);
            assertCondition (pcl.eventCount_ == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
removePropertyChangeListener() - Pass a listener ihat had been added previously.
**/
    public void Var021()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RPrinter u = new RPrinter(temp1, "nevada");
            RPrintTest.PropertyChangeListener_ pcl = new RPrintTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.removePropertyChangeListener(pcl);
            u.setSystem(temp2);
            assertCondition (pcl.eventCount_ == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
removeResourceListener() - Pass null.
**/
    public void Var022()
    {
        try {
            RPrinter u = new RPrinter();
            u.removeResourceListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removeResourceListener() - Pass a listener that had never been added.
**/
    public void Var023()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RPrinter u = new RPrinter(temp1, "california");
            RPrintTest.ResourceListener_ rl = new RPrintTest.ResourceListener_();
            u.removeResourceListener(rl);
            u.setSystem(temp2);
            assertCondition (rl.eventCount_ == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
removeResourceListener() - Pass a listener that had been added previously.
**/
    public void Var024()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            RPrinter u = new RPrinter(temp1, "idaho");
            u.setAttributeValue(RPrinter.TEXT_DESCRIPTION, "Microsoft");
            RPrintTest.ResourceListener_ rl = new RPrintTest.ResourceListener_();
            u.addResourceListener(rl);
            u.removeResourceListener(rl);
            u.setAttributeValue(RPrinter.TEXT_DESCRIPTION, "Sun");
            assertCondition (rl.eventCount_ == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
removeVetoableChangeListener() - Pass null.
**/
    public void Var025()
    {
        try {
            RPrinter u = new RPrinter();
            u.removeVetoableChangeListener(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removeVetoableChangeListener() - Pass a listener that had never been added.
**/
    public void Var026()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RPrinter u = new RPrinter(temp1, "california");
            RPrintTest.VetoableChangeListener_ vcl = new RPrintTest.VetoableChangeListener_();
            u.removeVetoableChangeListener(vcl);
            u.setSystem(temp2);
            assertCondition (vcl.eventCount_ == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
removePropertyChangeListener() - Pass a listener ihat had been added previously.
**/
    public void Var027()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RPrinter u = new RPrinter(temp1, "nevada");
            RPrintTest.VetoableChangeListener_ vcl = new RPrintTest.VetoableChangeListener_();
            u.addVetoableChangeListener(vcl);
            u.removeVetoableChangeListener(vcl);
            u.setSystem(temp2);
            assertCondition (vcl.eventCount_ == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
toString() - For a RPrinter whose properties have not been set,
this should return the default toString.
**/
    public void Var028()
    {
        try {
            RPrinter u = new RPrinter();  
            assertCondition(u.toString() != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
toString() - For a RPrinter whose properties have been set and used,
this should return the user name.
**/
    public void Var029()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            assertCondition(u.toString().equals(printerName_), "textDescription="+textDescription);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




