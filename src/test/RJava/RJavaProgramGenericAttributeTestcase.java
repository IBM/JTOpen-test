///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RJavaProgramGenericAttributeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RJava;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.RJavaProgram;
import com.ibm.as400.resource.ResourceEvent;
import com.ibm.as400.resource.ResourceException;
import com.ibm.as400.resource.ResourceMetaData;

import test.JDTestDriver;
import test.RJavaTest;
import test.Testcase;
import test.UserTest;



/**
Testcase RJavaProgramGenericAttributeTestcase.  This tests the following methods
of the RJavaProgram class, inherited from BufferedResource:

<ul>
<li>cancelAttributeChanges() 
<li>commitAttributeChanges() 
<li>createResoure()
<li>delete()
<li>getAttributeMetaData() 
<li>getAttributeMetaData() 
<li>getAttributeUnchangedValue() 
<li>getAttributeValue() 
<li>hasUncommittedAttributeChanges() 
<li>refreshAttributeValues() 
<li>setAttributeValue() 
</ul>
**/
@SuppressWarnings("deprecation")
public class RJavaProgramGenericAttributeTestcase
extends Testcase {








/**
Constructor.
**/
    public RJavaProgramGenericAttributeTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RJavaProgramGenericAttributeTestcase",
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
	super.setup(); 
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
	super.setup(); 
    }



    private boolean exists(String path)
    {
        RJavaProgram u = new RJavaProgram(systemObject_, path);
        try {
            u.getAttributeValue(RJavaProgram.OPTIMIZATION);
        }
        catch(ResourceException e) {
            return false;
        }
        return true;
    }



/**
Checks a particular attribute meta data.
**/
    static boolean verifyAttributeMetaData(ResourceMetaData amd, 
                                            Object attributeID, 
                                            Class<?> attributeType,
                                            boolean readOnly, 
                                            int possibleValueCount, 
                                            Object defaultValue, 
                                            boolean valueLimited,
                                            boolean multipleAllowed)
    {
        return((amd.areMultipleAllowed() == multipleAllowed)
               && (amd.getDefaultValue() == (defaultValue))
               && (amd.getPossibleValues().length == possibleValueCount)
               && (amd.getPresentation() != null)
               && (amd.getType() == attributeType)
               && (amd.isReadOnly() == readOnly)
               && (amd.isValueLimited() == valueLimited)
               && (amd.toString().equals(attributeID)));
    }



/**
Checks a particular attribute meta data.
**/
    static boolean verifyAttributeMetaData(ResourceMetaData[] amd, 
                                            Object attributeID, 
                                            Class<?> attributeType,
                                            boolean readOnly, 
                                            int possibleValueCount, 
                                            Object defaultValue, 
                                            boolean valueLimited,
                                            boolean multipleAllowed)
    {
        int found = -1;
        for (int i = 0; (i < amd.length) && (found < 0); ++i) {
            if (amd[i].getID() == attributeID)
                found = i;
        }

        if (found < 0) {
            System.out.println("Attribute ID " + attributeID + " not found.");
            return false;
        }

        return verifyAttributeMetaData(amd[found], 
                                       attributeID, 
                                       attributeType, 
                                       readOnly, 
                                       possibleValueCount, 
                                       defaultValue, 
                                       valueLimited, 
                                       multipleAllowed);
    }



/**
cancelAttributeChanges() - Should do nothing when there is
no system or path set.
**/
    public void Var001()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
	System.out.println("Var001: release is "+getRelease()); 

        try {
            RJavaProgram u = new RJavaProgram();
            u.cancelAttributeChanges();
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
cancelAttributeChanges() - Should do nothing when no change has been made.
**/
    public void Var002()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 

        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            Integer optimization = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            u.cancelAttributeChanges();
            Integer optimization2 = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition(optimization2.equals(optimization));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
cancelAttributeChanges() - When 2 changes have been made.
Verify that the changes are canceled.
**/
    public void Var003()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 

        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Integer optimization = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            String epc = (String)u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_INTERPRET);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION , RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            u.cancelAttributeChanges();
            Integer optimization2 = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            String epc2 = (String)u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition((optimization2.equals(optimization))
                   && (epc2.equals(epc)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
cancelAttributeChanges() - When 2 changes have been made.
Verify that the changes are canceled, even when committed.
**/
    public void Var004()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Integer optimization = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            String epc = (String)u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_10);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u.cancelAttributeChanges();
            u.commitAttributeChanges();
            Integer optimization2 = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            String epc2 = (String)u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition((optimization2.equals(optimization))
                   && (epc2.equals(epc)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
cancelAttributeChanges() - Verify that a ResourceEvent is fired.
**/
    public void Var005()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.cancelAttributeChanges();
            assertCondition ((rl.eventCount_ == 1)
                    && (rl.event_.getID() == ResourceEvent.ATTRIBUTE_CHANGES_CANCELED)
                    && (rl.event_.getAttributeID() == null)
                    && (rl.event_.getValue() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
commitAttributeChanges() - No system or path set.
**/
    public void Var006()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }


/**
commitAttributeChanges() - When the connection is bogus.
**/
    public void Var007()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            AS400 system = new AS400("Toolbox", "is", "cool");
            system.setGuiAvailable(false);
            RJavaProgram u = new RJavaProgram(system, "ClifNock");
            u.setAttributeValue(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS, "AllowInlining");
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
commitAttributeChanges() - When the path does not exist.
**/
    public void Var008()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, "NOTEXIST");
            u.setAttributeValue(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS, "AllowInlining");
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
commitAttributeChanges() - When the Java program does not exist.
**/
    public void Var009()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePathNoJvapgm_);
            u.setAttributeValue(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS, "AllowInlining");
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
commitAttributeChanges() - Should do nothing when no change has been made.
**/
    public void Var010()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Integer optimization = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            u.commitAttributeChanges();
            Integer optimization2 = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition(optimization2.equals(optimization));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
commitAttributeChanges() - Should commit the change when 2 changes have been made.
**/
    public void Var011()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            //Integer optimization = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            //String epc = (String)u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_20);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            u.commitAttributeChanges();
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Integer optimization2 = (Integer)u2.getAttributeValue(RJavaProgram.OPTIMIZATION);
            String epc2 = (String)u2.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            boolean v5r5 = systemObject_.getVRM() >= 0x00050500;
            assertCondition((optimization2.equals(v5r5 ? RJavaProgram.OPTIMIZATION_INTERPRET : RJavaProgram.OPTIMIZATION_20))
                   && (epc2.equalsIgnoreCase(v5r5 ? RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE : RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
commitAttributeChanges() - Verify that a ResourceEvent is fired.
**/
    public void Var012()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.commitAttributeChanges();
            assertCondition ((rl.eventCount_ == 1)
                    && (rl.event_.getID() == ResourceEvent.ATTRIBUTE_CHANGES_COMMITTED)
                    && (rl.event_.getAttributeID() == null)
                    && (rl.event_.getValue() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
delete() - When the system is not set.  
**/
    public void Var013()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            u.setPath(RJavaTest.jarFilePath_);
            u.delete();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
delete() - When the name is not set.  
**/
    public void Var014()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            u.setSystem(systemObject_);
            u.delete();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
delete() - A java program.  
**/
    public void Var015()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.delete();
            assertCondition(!exists(RJavaTest.jarFilePath_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeMetaData() with 0 parameters - Verify that the array contains a few
selected attributes.  I did not check everyone...we will do that when verifying
each attribute.
**/
    public void Var016()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            // boolean found = false;
            // I did not hardcode an exact length...otherwise, we
            // have to change this every time we add a property.
            assertCondition((amd.length > 5) 
                   && (verifyAttributeMetaData(amd, RJavaProgram.CLASSES_WITHOUT_CURRENT_JAVA_PROGRAMS , Integer.class, true, 0, null, false, false))
                   && (verifyAttributeMetaData(amd, RJavaProgram.OPTIMIZATION, Integer.class, false, 5, null, true, false))
                   && (verifyAttributeMetaData(amd, RJavaProgram.USER_PROFILE , String.class, true, 2, null, true, false)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeMetaData() with 1 parameter - Pass null.
**/
    public void Var017()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(null);
            failed ("Didn't throw exception"+amd);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getAttributeMetaData() with 1 parameter - Pass an invalid attribute ID.
**/
    public void Var018()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(new Date());
            failed ("Didn't throw exception"+amd);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getAttributeMetaData() with 1 parameter - Pass the first attribute ID.
**/
    public void Var019()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.CLASSES_WITHOUT_CURRENT_JAVA_PROGRAMS );
            assertCondition(verifyAttributeMetaData(amd, RJavaProgram.CLASSES_WITHOUT_CURRENT_JAVA_PROGRAMS , Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
getAttributeMetaData() with 1 parameter - Pass a middle attribute ID.
**/
    public void Var020()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION );
            assertCondition(verifyAttributeMetaData(amd, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, String.class, false, 3, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
getAttributeMetaData() with 1 parameter - Pass the last attribute ID.
**/
    public void Var021()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.USER_PROFILE);
            assertCondition(verifyAttributeMetaData(amd, RJavaProgram.USER_PROFILE, String.class, true, 2, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeMetaData() with 1 parameter - Try each of them.
**/
    public void Var022()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            boolean success = true;
            for(int i = 0; i < amd.length; ++i) {
                boolean thisOne = verifyAttributeMetaData(u.getAttributeMetaData(amd[i].getID()), 
                                                             amd[i].getID(), 
                                                             amd[i].getType(), 
                                                             amd[i].isReadOnly(), 
                                                             amd[i].getPossibleValues().length, 
                                                             amd[i].getDefaultValue(), 
                                                             amd[i].isValueLimited(),
                                                             amd[i].areMultipleAllowed());
                if (!thisOne) {
                    System.out.println("Comparison failed for: " + amd[i] + ".");
                    success = false;
                }
            }
            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeUnchangedValue() - When there is no connection.
**/
    public void Var023()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            Object value = u.getAttributeUnchangedValue(RJavaProgram.JAVA_PROGRAM_CREATION);
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
getAttributeUnchangedValue() - Pass null.
**/
    public void Var024()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u.getAttributeUnchangedValue(null);
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getAttributeUnchangedValue() - Pass an invalid attribute ID.
**/
    public void Var025()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u.getAttributeUnchangedValue(new AS400());
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getAttributeUnchangedValue() - Pass an attribute ID, whose value has not been referenced.
**/
    public void Var026()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_20);
            u2.commitAttributeChanges();

            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u.getAttributeUnchangedValue(RJavaProgram.OPTIMIZATION);
            assertCondition(((Integer)value).intValue() == (systemObject_.getVRM() >= 0x00050500 ? -1 : 20));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeUnchangedValue() - Pass an attribute ID, whose value has been referenced,
but not changed.
**/
    public void Var027()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_INTERPRET);
            u2.commitAttributeChanges();

            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            Object value = u.getAttributeUnchangedValue(RJavaProgram.OPTIMIZATION);
            assertCondition(value.equals(RJavaProgram.OPTIMIZATION_INTERPRET));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed,
but not committed.
**/
    public void Var028()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u2.commitAttributeChanges();

            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            Object value = u.getAttributeUnchangedValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed,
not committed, but refreshed.
**/
    public void Var029()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            u2.commitAttributeChanges();

            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u.refreshAttributeValues();
            Object value = u.getAttributeUnchangedValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(systemObject_.getVRM() >= 0x00050500 ? RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE : RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed
and committed.
**/
    public void Var030()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u2.commitAttributeChanges();

            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u.commitAttributeChanges();
            Object value = u.getAttributeUnchangedValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeValue() - When there is no connection.
**/
    public void Var031()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            Object value = u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
getAttributeValue() - When the connection is bogus.
**/
    public void Var032()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            AS400 system = new AS400("This", "is", "bad");
            system.setGuiAvailable(false);
            RJavaProgram u = new RJavaProgram(system, "Friend");
            Object value = u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
getAttributeValue() - When the path does not exist.
**/
    public void Var033()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, "NOTEXIST");
            Object value = u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
getAttributeValue() - When the java program does not exist.
**/
    public void Var034()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePathNoJvapgm_);
            Object value = u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
getAttributeValue() - Pass null.
**/
    public void Var035()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u.getAttributeValue(null);
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getAttributeValue() - Pass an invalid attribute ID.
**/
    public void Var036()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u.getAttributeValue("Yo");
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getAttributeValue() - Pass an attribute ID, whose value has not been referenced.
**/
    public void Var037()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            u2.commitAttributeChanges();

            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(systemObject_.getVRM() >= 0x00050500 ? RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE : RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeValue() - Pass an attribute ID, whose value has been referenced,
but not changed.
**/
    public void Var038()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u2.commitAttributeChanges();

            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            Object value = u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(systemObject_.getVRM() >= 0x00050500 ? RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE : RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeValue() - Pass an attribute ID, whose value has been changed,
but not committed.
**/
    public void Var039()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u2.commitAttributeChanges();

            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            Object value = u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeValue() - Pass an attribute ID, whose value has been changed,
not committed, but refreshed.
**/
    public void Var040()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u2.commitAttributeChanges();

            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            u.refreshAttributeValues();
            Object value = u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeValue() - Pass an attribute ID, whose value has been changed
and committed.
**/
    public void Var041()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u2.commitAttributeChanges();

            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u.commitAttributeChanges();
            Object value = u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeValue() - Pass every attribute ID.
**/
    public void Var042()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            ResourceMetaData[] amd = u.getAttributeMetaData();
            boolean success = true;
            for(int i = 0; i < amd.length; ++i) {
                // System.out.println("Getting attribute " + amd[i] + ".");
                Object value = u.getAttributeValue(amd[i].getID());
                Class<?> valueClass = value.getClass();
                Class<?> type = amd[i].getType();

                // Validate the type.
                if (amd[i].areMultipleAllowed()) {
                    if (!valueClass.isArray()) {
                        System.out.println("Error getting attribute " + amd[i] + ".");
                        System.out.println("Type array mismatch: " + valueClass + " is not an array, "
                                           + "but multiple values are allowed.");
                        success = false;
                    }
                    else {
                        Class<?> componentType = valueClass.getComponentType();
                        if (!componentType.equals(type)) {
                            System.out.println("Error getting attribute " + amd[i] + ".");
                            System.out.println("Type mismatch: " + componentType + " != " + type + ".");
                            success = false;
                        }
                    }
                }
                else if (!valueClass.equals(type)) {
                    System.out.println("Error getting attribute " + amd[i] + ".");
                    System.out.println("Type mismatch: " + valueClass + " != " + type + ".");
                    success = false;
                }

                // Validate the value.
                if (amd[i].isValueLimited()) {
                    Object[] possibleValues = amd[i].getPossibleValues();
                    boolean found = false;
                    if (amd[i].areMultipleAllowed()) {
                        Object[] asArray = (Object[])value;
                        for (int k = 0; k < asArray.length; ++k) {
                            for(int j = 0; (j < possibleValues.length) && (found == false); ++j)
                            if (possibleValues[j].equals(asArray[k]))
                                found = true;                           

                            if (! found) {
                                System.out.println("Error getting attribute " + amd[i] + ".");
                                System.out.println("Value: " + asArray[k] + " is not a valid possible value.");
                                success = false;
                            }
                        }
                    }
                    else {
                        for(int j = 0; (j < possibleValues.length) && (found == false); ++j)
                            if (possibleValues[j].equals(value))
                                found = true;

                        if (! found) {
                            System.out.println("Error getting attribute " + amd[i] + ".");
                            System.out.println("Value: " + value + " is not a valid possible value.");
                            success = false;
                        }
                    }
                }
            }
            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
hasUncommittedAttributeChanges() - When there is no connection.
**/
    public void Var043()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            boolean pending = u.hasUncommittedAttributeChanges(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(pending == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass null.
**/
    public void Var044()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            boolean pending = u.hasUncommittedAttributeChanges(null);
            failed ("Didn't throw exception"+pending);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass an invalid attribute ID.
**/
    public void Var045()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            boolean pending = u.hasUncommittedAttributeChanges("Go Toolbox");
            failed ("Didn't throw exception"+pending);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has not been referenced.
**/
    public void Var046()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            u2.commitAttributeChanges();

            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            boolean pending = u.hasUncommittedAttributeChanges(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(pending == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has been referenced,
but not changed.
**/
    public void Var047()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u2.commitAttributeChanges();

            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            boolean pending = u.hasUncommittedAttributeChanges(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(pending == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has been changed,
but not committed.
**/
    public void Var048()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u2.commitAttributeChanges();

            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            boolean pending = u.hasUncommittedAttributeChanges(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(pending == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has been changed,
not committed, but refreshed.
**/
    public void Var049()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u2.commitAttributeChanges();

            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u.refreshAttributeValues();
            boolean pending = u.hasUncommittedAttributeChanges(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(pending == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has been changed
and committed.
**/
    public void Var050()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            u2.commitAttributeChanges();

            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u.commitAttributeChanges();
            boolean pending = u.hasUncommittedAttributeChanges(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(pending == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
refreshAttributeValues() - When no system or path set.
**/
    public void Var051()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            u.refreshAttributeValues();
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
refreshAttributeValues() - When no change has been made.
**/
    public void Var052()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Integer optimization = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            u.refreshAttributeValues();
            Integer optimization2 = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition(optimization2.equals(optimization));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
refreshAttributeValues() - When 2 changes have been made for this
object, but not committed.  Verify that the changes are not cancelled.
**/
    public void Var053()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Integer optimization = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            String epc = (String)u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_30);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u.refreshAttributeValues();
            Integer optimization2 = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            String epc2 = (String)u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            Integer optimization2a = (Integer)u.getAttributeUnchangedValue(RJavaProgram.OPTIMIZATION);
            String epc2a = (String)u.getAttributeUnchangedValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition((optimization2.equals(RJavaProgram.OPTIMIZATION_30))
                   && (epc2.equals(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE))
                   && (optimization2a.equals(optimization))
                   && (epc2a.equals(epc)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
refreshAttributeValues() - When 2 changes have been made for another
RJavaProgram object.  Verify that the changes are reflected.
**/
    public void Var054()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            // Integer optimization = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            // String epc = (String)u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);

            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u2.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_40);
            u2.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u2.commitAttributeChanges();

            u.refreshAttributeValues();
            boolean v5r5 = systemObject_.getVRM() >= 0x00050500;
            Integer optimization2 = (Integer)u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            String epc2 = (String)u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition((optimization2.equals(v5r5 ? RJavaProgram.OPTIMIZATION_INTERPRET : RJavaProgram.OPTIMIZATION_40))
                   && (epc2.equalsIgnoreCase(v5r5 ? RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE : RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
refreshAttributeValues() - Verify that a ResourceEvent is fired.
**/
    public void Var055()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
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
setAttributeValue() - When there is no connection.
**/
    public void Var056()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            u.setAttributeValue(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS, "AllowInlining");
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setAttributeValue() - Pass null for the attribute ID.
**/
    public void Var057()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(null, Integer.valueOf(2));
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setAttributeValue() - Pass null for the value.
**/
    public void Var058()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS, null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setAttributeValue() - Pass an invalid attribute ID.
**/
    public void Var059()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(u, Integer.valueOf(3));
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setAttributeValue() - Set a read only attribute.
**/
    public void Var060()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.USER_PROFILE, RJavaProgram.USER_PROFILE_OWNER);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
setAttributeValue() - Pass a value which is the wrong type.
**/
    public void Var061()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS, Integer.valueOf(4));
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setAttributeValue() - Pass a value which is not one of the possible values.
**/
    public void Var062()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, Integer.valueOf(33));
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setAttributeValue() - Pass an valid attribute ID, commit and verify.
**/
    public void Var063()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_30);
            u.commitAttributeChanges();
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u2.getAttributeValue(RJavaProgram.OPTIMIZATION);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION,RJavaProgram.OPTIMIZATION_40);
            u.commitAttributeChanges();
            u2.refreshAttributeValues();
            boolean v5r5 = systemObject_.getVRM() >= 0x00050500;
            Object value2 = u2.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition((value.equals(v5r5 ? RJavaProgram.OPTIMIZATION_INTERPRET : RJavaProgram.OPTIMIZATION_30))
                   && (value2.equals(v5r5 ? RJavaProgram.OPTIMIZATION_INTERPRET : RJavaProgram.OPTIMIZATION_40)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setAttributeValue() - Pass an valid attribute ID, set twice.  Commit and verify
that the second takes effect.
**/
    public void Var064()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_INTERPRET);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_10);
            u.commitAttributeChanges();
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u2.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition(value.equals(systemObject_.getVRM() >= 0x00050500 ? RJavaProgram.OPTIMIZATION_INTERPRET : RJavaProgram.OPTIMIZATION_10));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setAttributeValue() - Pass an valid attribute ID, set and committed twice.  Verify
that the second takes effect.
**/
    public void Var065()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_20);
            u.commitAttributeChanges();
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_30);
            u.commitAttributeChanges();
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u2.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition(value.equals(systemObject_.getVRM() >= 0x00050500 ? RJavaProgram.OPTIMIZATION_INTERPRET : RJavaProgram.OPTIMIZATION_30));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setAttributeValue() - Set 2 attributes the are set using the same CL
command.  Commit and verify that the both take effect.
**/
    public void Var066()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_40);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u.commitAttributeChanges();
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value1 = u2.getAttributeValue(RJavaProgram.OPTIMIZATION);
            Object value2 = u2.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            boolean v5r5 = systemObject_.getVRM() >= 0x00050500;
            assertCondition((value1.equals(v5r5 ? RJavaProgram.OPTIMIZATION_INTERPRET : RJavaProgram.OPTIMIZATION_40))
                   && (value2.equals(v5r5 ? RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE : RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setAttributeValue() - Verify that a ResourceEvent is fired.
**/
    public void Var067()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_INTERPRET);
            assertCondition ((rl.eventCount_ == 1)
                    && (rl.event_.getID() == ResourceEvent.ATTRIBUTE_VALUE_CHANGED)
                    && (rl.event_.getAttributeID() == RJavaProgram.OPTIMIZATION)
                    && (rl.event_.getValue().equals(RJavaProgram.OPTIMIZATION_INTERPRET)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setAttributeValue() - Pass every attribute ID.
**/
    public void Var068()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            ResourceMetaData[] amd = u.getAttributeMetaData();
            boolean success = true;
            for(int i = 0; i < amd.length; ++i) {
                // Don't bother with the lic options, since the semantics are a bit
                // unpredictable.
                if ((! amd[i].isReadOnly()) && (amd[i].getID() != RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS)) {
                    // System.out.println("Setting attribute " + amd[i] + ".");
                    Object originalValue = u.getAttributeValue(amd[i].getID());

                    // First, just try setting the value to what it is already equal.
                    u.setAttributeValue(amd[i].getID(), originalValue);
                    u.commitAttributeChanges();

                    u.setAttributeValue(amd[i].getID(), originalValue);
                }
            }
            u2.commitAttributeChanges();

            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




