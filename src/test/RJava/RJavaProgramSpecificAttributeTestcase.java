///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RJavaProgramSpecificAttributeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RJava;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.ResourceEvent;
import com.ibm.as400.resource.ResourceMetaData;

import test.RJavaTest;
import test.Testcase;

import com.ibm.as400.resource.RJavaProgram;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase RJavaProgramSpecificAttributeTestcase.  This tests the following attributes
of the RJavaProgram class:

<ul>
<li>CLASSES_WITHOUT_CURRENT_JAVA_PROGRAMS 
<li>CLASSES_WITH_CURRENT_JAVA_PROGRAMS 
<li>ENABLE_PERFORMANCE_COLLECTION 
<li>FILE_CHANGE 
<li>JAVA_PROGRAMS 
<li>JAVA_PROGRAM_CREATION 
<li>JAVA_PROGRAM_SIZE 
<li>LICENSED_INTERNAL_CODE_OPTIONS 
<li>OPTIMIZATION 
<li>OWNER 
<li>RELEASE_PROGRAM_CREATED_FOR 
<li>USE_ADOPTED_AUTHORITY 
<li>USER_PROFILE 
</ul>
**/
@SuppressWarnings({ "unused", "deprecation" })
public class RJavaProgramSpecificAttributeTestcase
extends Testcase 
{



    // Constants.



    // Private data.



/**
Constructor.
**/
    public RJavaProgramSpecificAttributeTestcase (AS400 systemObject,
                                                  Hashtable<String,Vector<String>> namesAndVars,
                                                  int runMode,
                                                  FileOutputStream fileOutputStream,
                                                  
                                                  String password,
                                                  AS400 pwrSys)
    {
        super (systemObject, "RJavaProgramSpecificAttributeTestcase",
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



    public void Var001()  {   notApplicable("Java program objects not supported after V7R1");   }
    
    
    
        
        
        }


