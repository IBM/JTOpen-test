///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RSoftwareResourceBufferedResourceTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RSoftware;

import java.awt.Image;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.Presentation;
import com.ibm.as400.resource.RSoftwareResource;
import com.ibm.as400.resource.ResourceEvent;

import test.RSoftwareTest;
import test.Testcase;
import test.UserTest;



/**
Testcase RSoftwareResourceBufferedResourceTestcase.  This tests the following methods
of the RSoftwareResource class, inherited from ChangeableResource:

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
public class RSoftwareResourceBufferedResourceTestcase
extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "RSoftwareResourceBufferedResourceTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.RSoftwareTest.main(newArgs); 
   }



    // Constants.



    // Private data.
    private AS400           pwrSys_;



/**
Constructor.
**/
    public RSoftwareResourceBufferedResourceTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RSoftwareResourceBufferedResourceTestcase",
               namesAndVars, runMode, fileOutputStream,
               password);
        pwrSys_ = pwrSys;

        if (pwrSys == null)
            throw new IllegalStateException("ERROR: Please specify a power system.");
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
    }



/**
addPropertyChangeListener() - Pass null.
**/
    public void Var001()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
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
            RSoftwareResource u = new RSoftwareResource(temp1, "washington");
            RSoftwareTest.PropertyChangeListener_ pcl = new RSoftwareTest.PropertyChangeListener_();
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
            RSoftwareResource u = new RSoftwareResource();
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
            RSoftwareResource u = new RSoftwareResource(temp1, "oregon");
            RSoftwareTest.ResourceListener_ rl = new RSoftwareTest.ResourceListener_();
            u.addResourceListener(rl);
            u.refreshAttributeValues();
            assertCondition ((rl.eventCount_ == 1)
                    && (rl.event_.getID() == ResourceEvent.ATTRIBUTE_VALUES_REFRESHED)
                    && (rl.event_.getAttributeID() == null)
                    && (rl.event_.getValue() == null));
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
            RSoftwareResource u = new RSoftwareResource();
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
            RSoftwareResource u = new RSoftwareResource(temp1, "oregon");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
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
equals() - Verify that a RSoftwareResource is not equal to null.
**/
    public void Var007()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(null) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RSoftwareResource is not equal to something that is not a RSoftwareResource.
**/
    @SuppressWarnings("unlikely-arg-type")
    public void Var008()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(new Date()) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RSoftwareResource is not equal to another RSoftwareResource that it should not be.
**/
    public void Var009()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, systemObject_.getUserId());
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, "FRANK");
            assertCondition(u.equals(u2) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RSoftwareResource is equal to itself.
**/
    public void Var010()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(u) == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RSoftwareResource is equal to another RSoftwareResource representing the same user,
before either has been used.  The resource keys have not been set, so this should
be false.
**/
    public void Var011()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, systemObject_.getUserId());
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(u2) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
equals() - Verify that a RSoftwareResource is equal to another RSoftwareResource representing the same user,
after both have been used.  The resource keys should be set, and this should be
true.
**/
    public void Var012()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);            
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            u2.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition(u.equals(u2) == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPresentation() - Verify that the presentation has default information
for a RSoftwareResource whose properties have not been set.
**/
    public void Var013()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();            
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
                   && (iconColor16 == null)
                   && (iconColor32 == null)
                   && (asString.equals("")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPresentation() - Verify that the presentation has default information
for a RSoftwareResource whose properties have been set and used.
**/
    public void Var014()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(pwrSys_, RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            @SuppressWarnings("unused")
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            Presentation p = u.getPresentation();
            String name = p.getName();
            String fullName = p.getFullName();
            String descriptionText = (String)p.getValue(Presentation.DESCRIPTION_TEXT);
            String helpText = (String)p.getValue(Presentation.HELP_TEXT);
            Image iconColor16 = (Image)p.getValue(Presentation.ICON_COLOR_16x16);
            Image iconColor32 = (Image)p.getValue(Presentation.ICON_COLOR_32x32);
            String asString = u.toString();
            String compareString = RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM + "-" +
                                       RSoftwareResource.RELEASE_LEVEL_CURRENT + "-" +
                                       RSoftwareResource.PRODUCT_OPTION_BASE + "-" +
                                       RSoftwareResource.LOAD_ID_CODE;
            assertCondition((name.equals(RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM))
                   && (fullName.equals(compareString))
                   && (descriptionText != null) // Can't compare directly, this is MRI.
                   && (helpText == null)
                   && (iconColor16 == null)
                   && (iconColor32 == null)
                   && (asString.equals(compareString)));
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
            RSoftwareResource u = new RSoftwareResource();            
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
            RSoftwareResource u = new RSoftwareResource();            
            Presentation p = u.getPresentation();
            assertCondition(p.getValue("THIS IS NOT RIGHT") == null);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResourceKey() - For a RSoftwareResource whose properties have not been set,
the resource key should be null.
**/
    public void Var017()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();  
            assertCondition(u.getResourceKey() == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResourceKey() - For a RSoftwareResource whose properties have been set and used,
the resource key should be set.
**/
    public void Var018()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(pwrSys_, RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            u.getAttributeValue(RSoftwareResource.LOAD_TYPE);

            StringBuffer buffer = new StringBuffer();
            buffer.append(RSoftwareResource.class);
            buffer.append(':');
            buffer.append(pwrSys_.getSystemName());
            buffer.append(':');
            buffer.append(pwrSys_.getUserId());
            buffer.append(':');
            buffer.append(RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            buffer.append(':');
            buffer.append(RSoftwareResource.RELEASE_LEVEL_CURRENT);
            buffer.append(':');
            buffer.append(RSoftwareResource.PRODUCT_OPTION_BASE);
            buffer.append(':');
            buffer.append(RSoftwareResource.LOAD_ID_CODE);
            String expected = buffer.toString();

            assertCondition(u.getResourceKey().equals(expected));
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
            RSoftwareResource u = new RSoftwareResource();
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
            RSoftwareResource u = new RSoftwareResource(temp1, "california");
            RSoftwareTest.PropertyChangeListener_ pcl = new RSoftwareTest.PropertyChangeListener_();
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
            RSoftwareResource u = new RSoftwareResource(temp1, "nevada");
            RSoftwareTest.PropertyChangeListener_ pcl = new RSoftwareTest.PropertyChangeListener_();
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
            RSoftwareResource u = new RSoftwareResource();
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
            RSoftwareResource u = new RSoftwareResource(temp1, "california");
            RSoftwareTest.ResourceListener_ rl = new RSoftwareTest.ResourceListener_();
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
            RSoftwareResource u = new RSoftwareResource(temp1, "idaho");
            RSoftwareTest.ResourceListener_ rl = new RSoftwareTest.ResourceListener_();
            u.addResourceListener(rl);
            u.removeResourceListener(rl);
            u.refreshAttributeValues();
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
            RSoftwareResource u = new RSoftwareResource();
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
            RSoftwareResource u = new RSoftwareResource(temp1, "california");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
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
            RSoftwareResource u = new RSoftwareResource(temp1, "nevada");
            UserTest.VetoableChangeListener_ vcl = new UserTest.VetoableChangeListener_();
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
toString() - For a RSoftwareResource whose properties have not been set,
this should return the default toString.
**/
    public void Var028()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();  
            assertCondition(u.toString() != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
toString() - For a RSoftwareResource whose properties have been set and used,
this should return the user name.
**/
    public void Var029()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(pwrSys_, RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition(u.toString().equals(RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM + "-" + 
                                       RSoftwareResource.RELEASE_LEVEL_CURRENT + "-" +
                                       RSoftwareResource.PRODUCT_OPTION_BASE + "-" +
                                       RSoftwareResource.LOAD_ID_CODE));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




