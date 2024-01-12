///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RJavaProgramBasicTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RJava;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.RJavaProgram;

import test.JDTestDriver;
import test.RJavaTest;
import test.Testcase;
import test.UserTest;
import test.UserTest.PropertyChangeListener_;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Hashtable;



/**
Testcase RJavaProgramBasicTestcase.  This tests the following methods
of the RJavaProgram class:

<ul>
<li>constructors
<li>serialization
<li>getPath()
<li>getSystem()
<li>setPath()
<li>setSystem()
</ul>
**/
public class RJavaProgramBasicTestcase
extends Testcase {



    // Constants.



    // Private data.
    private AS400           pwrSys_;



/**
Constructor.
**/
    public RJavaProgramBasicTestcase(AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RJavaProgramBasicTestcase",
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
constructor() with 0 parms - Should work.
**/
    public void Var001()
    {
        try {
            RJavaProgram u = new RJavaProgram();
            assertCondition ((u.getSystem() == null) && (u.getPath() == null));
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
            RJavaProgram u = new RJavaProgram(null, RJavaTest.classFilePath_);
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
            RJavaProgram u = new RJavaProgram(systemObject_, null);
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
            RJavaProgram u = new RJavaProgram(bogus, "BadRJavaProgram");
            assertCondition ((u.getSystem() == bogus) 
                    && (u.getPath().equals("BadRJavaProgram")));
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
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            Integer optimization = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition ((u.getSystem() == systemObject_) 
                    && (u.getPath().equals(RJavaTest.classFilePath_))
                    && (optimization.equals(systemObject_.getVRM() >= 0x00050500 ? RJavaProgram.OPTIMIZATION_INTERPRET : RJavaProgram.OPTIMIZATION_10)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
serialization - Verify that the properties are set and verify that its usable.
**/
    public void Var006(int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) {
        // This variation pops up a signon dialog.
        notApplicable("Skipping attended variation.");
        return;
      }
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Integer optimization = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            RJavaProgram u2 = (RJavaProgram)RJavaTest.serialize(u);
            Integer optimization2 = (Integer)u2.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition ((u2.getSystem().getSystemName().equals(systemObject_.getSystemName()))
                    && (u2.getSystem().getUserId().equals(systemObject_.getUserId()))
                    && (u2.getPath().equals(RJavaTest.jarFilePath_))
                    && (optimization.equals(optimization2)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPath() - Should return null when the name has not been set.
**/
    public void Var007()
    {
        try {
            RJavaProgram u = new RJavaProgram();
            assertCondition (u.getPath() == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getPath() - Should return the name when the name has been set.
**/
    public void Var008()
    {
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, "maine");
            assertCondition (u.getPath().equals("maine"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSystem() - Should return null when the system has not been set.
**/
    public void Var009()
    {
        try {
            RJavaProgram u = new RJavaProgram();
            assertCondition (u.getSystem() == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSystem() - Should return the system when the system has been set.
**/
    public void Var010()
    {
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, "newhampshire");
            assertCondition (u.getSystem().equals(systemObject_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setPath() - Should throw an exception if null is passed.
**/
    public void Var011()
    {
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, "massachusetts");
            u.setPath(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setPath() - Set to an invalid name.  Should be reflected by getPath(),
since the validity is not checked here.
**/
    public void Var012()
    {
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, "connecticut");
            u.setPath("rhodeisland");
            assertCondition (u.getPath().equals("rhodeisland"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setPath() - Set to a valid name.  Should be reflected by getPath() and
verify that it is used.
**/
    public void Var013()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 

        try {
            RJavaProgram u = new RJavaProgram(systemObject_, "vermont");
            u.setPath(RJavaTest.classFilePath_);
            Integer optimization = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition ((u.getPath().equals(RJavaTest.classFilePath_))
                    && (optimization.equals(systemObject_.getVRM() >= 0x00050500 ? RJavaProgram.OPTIMIZATION_INTERPRET : RJavaProgram.OPTIMIZATION_10)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setPath() - Set to a valid name after the RJavaProgram has made a connection.
**/
    public void Var014()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 

        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            Integer optimization = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            u.setPath("alabama");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
setPath() - Should fire a property change event.
**/
    public void Var015()
    {
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, "massachusetts");
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setPath("newyork");
            assertCondition ((pcl.eventCount_ == 1)
                    && (pcl.event_.getPropertyName().equals("path"))
                    && (pcl.event_.getOldValue().equals("massachusetts"))
                    && (pcl.event_.getNewValue().equals("newyork")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Should throw an exception if null is passed.
**/
    public void Var016()
    {
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, "virginia");
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
    public void Var017()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RJavaProgram u = new RJavaProgram(systemObject_, "scarolina");
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
    public void Var018()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 

        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RJavaProgram u = new RJavaProgram(bogus, RJavaTest.jarFilePath_);
            u.setSystem(systemObject_);
            Integer optimization = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition ((u.getSystem().getSystemName().equals(systemObject_.getSystemName()))
                    && (u.getSystem().getUserId().equals(systemObject_.getUserId()))
                    && (optimization.equals(systemObject_.getVRM() >= 0x00050500 ? RJavaProgram.OPTIMIZATION_INTERPRET : RJavaProgram.OPTIMIZATION_10)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Set to a valid name after the RJavaProgram object has made a connection.
**/
    public void Var019()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 

        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Integer optimization = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
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
    public void Var020()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RJavaProgram u = new RJavaProgram(temp1, "ncarolina");
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



}




