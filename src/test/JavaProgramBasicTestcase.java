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
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
constructor() with 2 parms - Pass null for system.
**/
    public void Var002()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
constructor() with 2 parms - Pass null for name.
**/
    public void Var003()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
constructor() with 2 parms - Pass invalid values.  This should work,
because the constructor does not check the validity.
**/
    public void Var004()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
constructor() with 2 parms - Pass valid values.  Verify that the system and name are used.
**/
    public void Var005()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
serialization - Verify that the properties are set and verify that its usable.
**/
    public void Var006()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
getPath() - Should return null when the name has not been set.
**/
    public void Var007()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
getPath() - Should return the name when the name has been set.
**/
    public void Var008()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
getSystem() - Should return null when the system has not been set.
**/
    public void Var009()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
getSystem() - Should return the system when the system has been set.
**/
    public void Var010()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
setPath() - Should throw an exception if null is passed.
**/
    public void Var011()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
setPath() - Set to an invalid name.  Should be reflected by getPath(),
since the validity is not checked here.
**/
    public void Var012()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
setPath() - Set to a valid name.  Should be reflected by getPath() and
verify that it is used.
**/
    public void Var013()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
setPath() - Set to a valid name after the JavaProgram has made a connection.
**/
    public void Var014()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
setSystem() - Should throw an exception if null is passed.
**/
    public void Var015()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
setSystem() - Set to an invalid system.  Should be reflected by getSystem(),
since the validity is not checked here.
**/
    public void Var016()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
setSystem() - Set to a valid name.  Should be reflected by getSystem() and
verify that it is used.
**/
    public void Var017()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }



/**
setSystem() - Set to a valid name after the JavaProgram object has made a connection.
**/
    public void Var018()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        

    }

/**
getOptimizationLevel() 
**/
    public void Var019()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }

/**
getAdoptedAuthorityProfile()
**/
    public void Var020()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }

/**
getFileChangeDate()
**/
    public void Var021()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }
    
/**
getFileOwner()
**/
    public void Var022()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }

/**
getJavaProgramCreationDate()
**/
    public void Var023()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }

/**
getJavaProgramVersion()
**/
    public void Var024()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }

/**
getLICOptions()
**/
    public void Var025()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }

/**
getNumberOfAttachedPrograms()
**/
    public void Var026()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }

/**
getNumberOfClasses()
**/
    public void Var027()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }

/**
getNumberOfClassesWithCurrentJavaPrograms()
**/
    public void Var028()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }

/**
getNumberOfClassesWithErrors()
**/
    public void Var029()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }

/**
getNumberOfClassesWithoutCurrentJavaPrograms()
**/
    public void Var030()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }

/**
getPerformanceCollectionEnabledFlag()
**/
    public void Var031()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }

/**
getPerformanceCollectionType()
**/
    public void Var032()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }

/**
getProfilingDataStatus()
**/
    public void Var033()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }

/**
getSizeOfAttachedJavaPrograms()
**/
    public void Var034()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }

/**
isUseAdoptedAuthority()
**/
    public void Var035()
    {
	notApplicable("Java program objects not supported after V7R1");
  return;
    }
}




