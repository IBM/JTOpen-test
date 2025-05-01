///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RJavaProgramBufferedResourceTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RJava;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.Presentation;
import com.ibm.as400.resource.ResourceEvent;

import test.RJavaTest;
import test.Testcase;
import test.UserTest;
import test.UserTest.PropertyChangeListener_;
import test.UserTest.ResourceListener_;
import test.UserTest.VetoableChangeListener_;

import com.ibm.as400.resource.RJavaProgram;
import java.awt.Image;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase RJavaProgramBufferedResourceTestcase.  This tests the following methods
of the RJavaProgram class, inherited from ChangeableResource:

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

@SuppressWarnings({ "unused", "deprecation" })

public class RJavaProgramBufferedResourceTestcase
extends Testcase {



    // Constants.



    // Private data.
    private AS400           pwrSys_;



/**
Constructor.
**/
    public RJavaProgramBufferedResourceTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RJavaProgramBufferedResourceTestcase",
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RJavaProgram u = new RJavaProgram(temp1, "washington");
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            RJavaProgram u = new RJavaProgram(temp1, "oregon");
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_20);
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_30);
            assertCondition ((rl.eventCount_ == 1)
                    && (rl.event_.getID() == ResourceEvent.ATTRIBUTE_VALUE_CHANGED)
                    && (rl.event_.getAttributeID().equals(RJavaProgram.OPTIMIZATION))
                    && (rl.event_.getValue().equals(RJavaProgram.OPTIMIZATION_30)));
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RJavaProgram u = new RJavaProgram(temp1, "oregon");
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
equals() - Verify that a RJavaProgram is not equal to null.
**/
    public void Var007()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(null) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RJavaProgram is not equal to something that is not a RJavaProgram.
**/
    public void Var008()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(new Date()) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RJavaProgram is not equal to another RJavaProgram that it should not be.
**/
    public void Var009()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, systemObject_.getUserId());
            RJavaProgram u2 = new RJavaProgram(systemObject_, "FRANK");
            assertCondition(u.equals(u2) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RJavaProgram is equal to itself.
**/
    public void Var010()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(u) == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
equals() - Verify that a RJavaProgram is equal to another RJavaProgram representing the same user,
before either has been used.  The resource keys have not been set, so this should
be false.
**/
    public void Var011()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, systemObject_.getUserId());
            RJavaProgram u2 = new RJavaProgram(systemObject_, systemObject_.getUserId());
            assertCondition(u.equals(u2) == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
equals() - Verify that a RJavaProgram is equal to another RJavaProgram representing the same user,
after both have been used.  The resource keys should be set, and this should be
true.
**/
    public void Var012()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);            
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            u2.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition(u.equals(u2) == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPresentation() - Verify that the presentation has default information
for a RJavaProgram whose properties have not been set.
**/
    public void Var013()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();            
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
                   && (iconColor16 != null)
                   && (iconColor32 != null)
                   && (asString.equals("")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPresentation() - Verify that the presentation has default information
for a RJavaProgram whose properties have been set and used.
**/
    public void Var014()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(pwrSys_, RJavaTest.classFilePath_);
            Integer optimization = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            Presentation p = u.getPresentation();
            String name = p.getName();
            String fullName = p.getFullName();
            String descriptionText = (String)p.getValue(Presentation.DESCRIPTION_TEXT);
            String helpText = (String)p.getValue(Presentation.HELP_TEXT);
            Image iconColor16 = (Image)p.getValue(Presentation.ICON_COLOR_16x16);
            Image iconColor32 = (Image)p.getValue(Presentation.ICON_COLOR_32x32);
            String asString = u.toString();
            assertCondition((name.equals(RJavaTest.classFilePath_.substring(RJavaTest.classFilePath_.lastIndexOf('/') + 1)))
                   && (fullName.equals(RJavaTest.classFilePath_))
                   && (descriptionText != null) // Can't compare directly, this is MRI.
                   && (helpText == null)
                   && (iconColor16 != null)
                   && (iconColor32 != null)
                   && (asString.equals(RJavaTest.classFilePath_)));
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();            
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();            
            Presentation p = u.getPresentation();
            assertCondition(p.getValue("THIS IS NOT RIGHT") == null);
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResourceKey() - For a RJavaProgram whose properties have not been set,
the resource key should be null.
**/
    public void Var017()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();  
            assertCondition(u.getResourceKey() == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getResourceKey() - For a RJavaProgram whose properties have been set and used,
the resource key should be set.
**/
    public void Var018()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(pwrSys_, RJavaTest.jarFilePath_);
            u.getAttributeValue(RJavaProgram.OPTIMIZATION);

            StringBuffer buffer = new StringBuffer();
            buffer.append(RJavaProgram.class);
            buffer.append(':');
            buffer.append(pwrSys_.getSystemName());
            buffer.append(':');
            buffer.append(pwrSys_.getUserId());
            buffer.append(':');
            buffer.append(RJavaTest.jarFilePath_);
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RJavaProgram u = new RJavaProgram(temp1, "california");
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RJavaProgram u = new RJavaProgram(temp1, "nevada");
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RJavaProgram u = new RJavaProgram(temp1, "california");
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            RJavaProgram u = new RJavaProgram(temp1, "idaho");
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_30);
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.removeResourceListener(rl);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_20);
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RJavaProgram u = new RJavaProgram(temp1, "california");
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
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RJavaProgram u = new RJavaProgram(temp1, "nevada");
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
toString() - For a RJavaProgram whose properties have not been set,
this should return the default toString.
**/
    public void Var028()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();  
            assertCondition(u.toString() != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
toString() - For a RJavaProgram whose properties have been set and used,
this should return the user name.
**/
    public void Var029()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(pwrSys_, RJavaTest.classFilePath_);
            u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition(u.toString().equals(RJavaTest.classFilePath_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




