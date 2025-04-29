///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JavaProgramTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
//
// File Name:    JavaProgramBasicTestcase.java
//
// Classes:      JavaProgramBasicTestcase
//
////////////////////////////////////////////////////////////////////////
package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.JavaProgram;

import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JavaProgramBasicTestcase.  This tests the following methods
of the JavaProgram class:

<ul>
<li>constructors
<li>serialization
<li>getPath()
<li>getSystem()
<li>setPath()
<li>setSystem()
</ul>
**/
public class JavaProgramBasicTestcase
extends Testcase {








/**
Constructor.
**/
    public JavaProgramBasicTestcase(AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "JavaProgramBasicTestcase",
               namesAndVars, runMode, fileOutputStream,
               password);

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
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram();
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
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(null, JavaProgramTest.classFilePath_);
            failed ("Didn't throw exception for "+u);
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
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, null);
            failed ("Didn't throw exception for "+u);
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
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus".toCharArray());
            JavaProgram u = new JavaProgram(bogus, "BadJavaProgram");
            assertCondition ((u.getSystem() == bogus) 
                    && (u.getPath().equals("BadJavaProgram")));
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
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.classFilePath_);
            int optimization = 20;
            if(systemObject_.getVRM() >= 0x00050500)
                optimization = 0;   // In V5R5 and later, DE support was removed so *INTERPRED or JIT for optizmiation level

            assertCondition ((u.getSystem() == systemObject_) 
                    && (u.getPath().equals(JavaProgramTest.classFilePath_))
                    && (u.getOptimizationLevel() == optimization));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
serialization - Verify that the properties are set and verify that its usable.
**/
    public void Var006()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.jarFilePath_);
            int optimization = u.getOptimizationLevel();
            JavaProgram u2 = (JavaProgram)JavaProgramTest.serialize(u);
            int optimization2 = u.getOptimizationLevel();
            assertCondition ((u2.getSystem().getSystemName().equals(systemObject_.getSystemName()))
                    && (u2.getSystem().getUserId().equals(systemObject_.getUserId()))
                    && (u2.getPath().equals(JavaProgramTest.jarFilePath_))
                    && (optimization == optimization2));
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
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram();
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
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, "maine");
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
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram();
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
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, "newhampshire");
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
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, "massachusetts");
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
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, "connecticut");
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
            JavaProgram u = new JavaProgram(systemObject_, "vermont");
            u.setPath(JavaProgramTest.classFilePath_);
            int optimization = u.getOptimizationLevel();
            int optimizationLevel = 20;
            if(systemObject_.getVRM() >= 0x00050500)
                optimizationLevel = 0;   // In V5R5 and later, DE support was removed so *INTERPRED or JIT for optizmiation level

            assertCondition ((u.getPath().equals(JavaProgramTest.classFilePath_))
                    && (optimization == optimizationLevel));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setPath() - Set to a valid name after the JavaProgram has made a connection.
**/
    public void Var014()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.classFilePath_);
            int optimization = u.getOptimizationLevel();
            u.setPath("alabama");
            failed ("Didn't throw exception for "+optimization);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
setSystem() - Should throw an exception if null is passed.
**/
    public void Var015()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, "virginia");
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
    public void Var016()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus".toCharArray());
            JavaProgram u = new JavaProgram(systemObject_, "scarolina");
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
    public void Var017()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus".toCharArray());
            JavaProgram u = new JavaProgram(bogus, JavaProgramTest.jarFilePath_);
            u.setSystem(systemObject_);
            int optimization = u.getOptimizationLevel();
            int optimizationLevel = 20;
            if(systemObject_.getVRM() >= 0x00050500)
                optimizationLevel = 0;   // In V5R5 and later, DE support was removed so *INTERPRED or JIT for optizmiation level

            assertCondition ((u.getSystem().getSystemName().equals(systemObject_.getSystemName()))
                    && (u.getSystem().getUserId().equals(systemObject_.getUserId()))
                    && (optimization == optimizationLevel));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Set to a valid name after the JavaProgram object has made a connection.
**/
    public void Var018()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.jarFilePath_);
            int optimization = u.getOptimizationLevel();
            u.setSystem(systemObject_);
            failed ("Didn't throw exception "+optimization);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }

/**
getOptimizationLevel() 
**/
    public void Var019()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.jarFilePath_);
            int optimization = u.getOptimizationLevel();
            int optimizationLevel = 20;
            if(systemObject_.getVRM() >= 0x00050500)
                optimizationLevel = 0;   // In V5R5 and later, DE support was removed so *INTERPRED or JIT for optizmiation level

            assertCondition(optimization == optimizationLevel);
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

/**
getAdoptedAuthorityProfile()
**/
    public void Var020()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.classFilePath_);
            String ap = u.getAdoptedAuthorityProfile();
            assertCondition(ap.equals("*USER"));
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

/**
getFileChangeDate()
**/
    public void Var021()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.classFilePath_);
            u.getFileChangeDate();
            succeeded();
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
    
/**
getFileOwner()
**/
    public void Var022()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.classFilePath_);
            String fo = u.getFileOwner();
            assertCondition((fo.trim()).equals(systemObject_.getUserId()), "assertion failed File Owner is " + fo + "! but should be " + systemObject_.getUserId() + "!");
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

/**
getJavaProgramCreationDate()
**/
    public void Var023()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.classFilePath_);
            u.getJavaProgramCreationDate();
            succeeded();
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

/**
getJavaProgramVersion()
**/
    public void Var024()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.classFilePath_);
            String jpv = u.getJavaProgramVersion();
            String expected = "V" + systemObject_.getVersion() + "R" + systemObject_.getRelease() + "M" + systemObject_.getModification();
            assertCondition(jpv.equals(expected));
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

/**
getLICOptions()
**/
    public void Var025()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.classFilePath_);
            String lic = u.getLICOptions();
            assertCondition(lic.equals(""));
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

/**
getNumberOfAttachedPrograms()
**/
    public void Var026()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.classFilePath_);
            int nap = u.getNumberOfAttachedPrograms();
            assertCondition(nap == 1);
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

/**
getNumberOfClasses()
**/
    public void Var027()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.jarFilePath_);
            int noc = u.getNumberOfClasses();
            assertCondition(noc == 1);
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

/**
getNumberOfClassesWithCurrentJavaPrograms()
**/
    public void Var028()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.jarFilePath_);
            int noccjv = u.getNumberOfClassesWithCurrentJavaPrograms();
            assertCondition(noccjv == 1);
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

/**
getNumberOfClassesWithErrors()
**/
    public void Var029()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.jarFilePath_);
            int noc = u.getNumberOfClassesWithErrors();
            assertCondition(noc == 1);
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

/**
getNumberOfClassesWithoutCurrentJavaPrograms()
**/
    public void Var030()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.jarFilePath_);
            int noccjv = u.getNumberOfClassesWithoutCurrentJavaPrograms();
            assertCondition(noccjv == 0);
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

/**
getPerformanceCollectionEnabledFlag()
**/
    public void Var031()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.jarFilePath_);
            String pcef = u.getPerformanceCollectionEnabledFlag();
            assertCondition(pcef.equals("0"));
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

/**
getPerformanceCollectionType()
**/
    public void Var032()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.jarFilePath_);
            String pcef = u.getPerformanceCollectionType();
            assertCondition(pcef.equals(JavaProgram.PERFORMANCE_COLLECTION_TYPE_ENTRYEXIT));
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

/**
getProfilingDataStatus()
**/
    public void Var033()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.jarFilePath_);
            String pcef = u.getProfilingDataStatus();
            assertCondition(pcef.equals(JavaProgram.PROFILING_DATA_STATUS_NOCOL), "Profiling Data Status Should be " + JavaProgram.PROFILING_DATA_STATUS_NOCOL + " but is " + pcef);
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

/**
getSizeOfAttachedJavaPrograms()
**/
    public void Var034()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.jarFilePath_);
            int size = u.getSizeOfAttachedJavaPrograms();
            
            // Randy Esch (i.e. i5 Java) indicated that the sizeOfAttchedPrograms is     @A1A
            // subject to change from one release to the next.  Therefore, the following @A1A
            // check will verify a range rather than an explicit value.                  @A1A
            // assertCondition(size == 20);                                              @A1D
            assertCondition((size >= 20) && (size <= 120));                            //@A1C
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

/**
isUseAdoptedAuthority()
**/
    public void Var035()
    {
	if (getRelease() > JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            JavaProgram u = new JavaProgram(systemObject_, JavaProgramTest.jarFilePath_);
            assertCondition(!u.isUseAdoptedAuthority());
        }
        catch(Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}




