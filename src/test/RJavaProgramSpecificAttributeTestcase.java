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
package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.ResourceEvent;
import com.ibm.as400.resource.ResourceMetaData;
import com.ibm.as400.resource.RJavaProgram;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;



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
public class RJavaProgramSpecificAttributeTestcase
extends Testcase 
{



    // Constants.



    // Private data.
    private AS400           pwrSys_;



/**
Constructor.
**/
    public RJavaProgramSpecificAttributeTestcase (AS400 systemObject,
                                                  Hashtable namesAndVars,
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



/**
CLASSES_WITHOUT_CURRENT_JAVA_PROGRAMS - Check the attribute meta data in the entire list.
**/
    public void Var001()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.CLASSES_WITHOUT_CURRENT_JAVA_PROGRAMS, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CLASSES_WITHOUT_CURRENT_JAVA_PROGRAMS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var002()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.CLASSES_WITHOUT_CURRENT_JAVA_PROGRAMS);
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.CLASSES_WITHOUT_CURRENT_JAVA_PROGRAMS, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CLASSES_WITHOUT_CURRENT_JAVA_PROGRAMS - Get the attribute value.
**/
    public void Var003()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            Object value = u.getAttributeValue(RJavaProgram.CLASSES_WITHOUT_CURRENT_JAVA_PROGRAMS);
            assertCondition(((Integer)value).intValue() == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CLASSES_WITH_CURRENT_JAVA_PROGRAMS - Check the attribute meta data in the entire list.
**/
    public void Var004()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.CLASSES_WITH_CURRENT_JAVA_PROGRAMS, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CLASSES_WITH_CURRENT_JAVA_PROGRAMS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var005()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.CLASSES_WITH_CURRENT_JAVA_PROGRAMS);
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.CLASSES_WITH_CURRENT_JAVA_PROGRAMS, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CLASSES_WITH_CURRENT_JAVA_PROGRAMS - Get the attribute value.
**/
    public void Var006()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            Object value = u.getAttributeValue(RJavaProgram.CLASSES_WITH_CURRENT_JAVA_PROGRAMS);
            assertCondition(((Integer)value).intValue() > 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ENABLE_PERFORMANCE_COLLECTION - Check the attribute meta data in the entire list.
**/
    public void Var007()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, String.class, false, 3, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ENABLE_PERFORMANCE_COLLECTION - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var008()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, String.class, false, 3, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ENABLE_PERFORMANCE_COLLECTION - Get the attribute value without setting it first.
**/
    public void Var009()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            Object value = u.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(((String)value != null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ENABLE_PERFORMANCE_COLLECTION - Set and get the attribute value to none..
**/
    public void Var010()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u.commitAttributeChanges();

            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            Object value = u2.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
ENABLE_PERFORMANCE_COLLECTION - Set and get the attribute value to entry exit..
**/
    public void Var011()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            u.commitAttributeChanges();

            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            Object value = u2.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(systemObject_.getVRM() >= 0x00050500 ? RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE : RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
ENABLE_PERFORMANCE_COLLECTION - Set and get the attribute value to full..
**/
    public void Var012()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            u.setAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION, RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u.commitAttributeChanges();

            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            Object value = u2.getAttributeValue(RJavaProgram.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(systemObject_.getVRM() >= 0x00050500 ? RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_NONE : RJavaProgram.ENABLE_PERFORMANCE_COLLECTION_FULL));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
FILE_CHANGE - Check the attribute meta data in the entire list.
**/
    public void Var013()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.FILE_CHANGE, Date.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
FILE_CHANGE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var014()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.FILE_CHANGE);
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.FILE_CHANGE, Date.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
FILE_CHANGE - Get the attribute value.
**/
    public void Var015()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            Object value = u.getAttributeValue(RJavaProgram.FILE_CHANGE);
            assertCondition(((Date)value != null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JAVA_PROGRAMS - Check the attribute meta data in the entire list.
**/
    public void Var016()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.JAVA_PROGRAMS, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JAVA_PROGRAMS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var017()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.JAVA_PROGRAMS);
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.JAVA_PROGRAMS, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JAVA_PROGRAMS - Get the attribute value.
**/
    public void Var018()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            Object value = u.getAttributeValue(RJavaProgram.JAVA_PROGRAMS);
            assertCondition(((Integer)value).intValue() > 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
JAVA_PROGRAM_CREATION - Check the attribute meta data in the entire list.
**/
    public void Var019()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.JAVA_PROGRAM_CREATION, Date.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JAVA_PROGRAM_CREATION - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var020()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.JAVA_PROGRAM_CREATION);
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.JAVA_PROGRAM_CREATION, Date.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JAVA_PROGRAM_CREATION - Get the attribute value.
**/
    public void Var021()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            Object value = u.getAttributeValue(RJavaProgram.JAVA_PROGRAM_CREATION);
            assertCondition(((Date)value != null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JAVA_PROGRAM_SIZE - Check the attribute meta data in the entire list.
**/
    public void Var022()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.JAVA_PROGRAM_SIZE, Long.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JAVA_PROGRAM_SIZE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var023()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.JAVA_PROGRAM_SIZE);
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.JAVA_PROGRAM_SIZE, Long.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JAVA_PROGRAM_SIZE - Get the attribute value.
**/
    public void Var024()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.classFilePath_);
            Object value = u.getAttributeValue(RJavaProgram.JAVA_PROGRAM_SIZE);
            assertCondition(((Long)value).longValue() > 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LICENSED_INTERNAL_CODE_OPTIONS - Check the attribute meta data in the entire list.
**/
    public void Var025()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS, String.class, false, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LICENSED_INTERNAL_CODE_OPTIONS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var026()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS);
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS, String.class, false, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LICENSED_INTERNAL_CODE_OPTIONS - Get the attribute value without setting it first.
**/
    public void Var027()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u.getAttributeValue(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS);
            assertCondition(((String)value != null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LICENSED_INTERNAL_CODE_OPTIONS - Set and get the attribute value to "".
**/
    public void Var028()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS, "");
            u.commitAttributeChanges();

            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u2.getAttributeValue(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS);

            // We can't test the exact value since the lic options semantics are
            // a little strange.
            assertCondition(value instanceof String);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
LICENSED_INTERNAL_CODE_OPTIONS - Set and get the attribute value to a valid value with one licopt.
**/
    public void Var029()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS, "AllFieldsVolatile");
            u.commitAttributeChanges();

            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u2.getAttributeValue(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS);
            // We can't test the exact value since the lic options semantics are
            // a little strange.
            assertCondition(value instanceof String);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
LICENSED_INTERNAL_CODE_OPTIONS - Set and get the attribute value to a valid value with two licopts.
**/
    public void Var030()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS, "AllFieldsVolatile, AllowInlining");
            u.commitAttributeChanges();

            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u2.getAttributeValue(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS);
            // We can't test the exact value since the lic options semantics are
            // a little strange.
            assertCondition(value instanceof String);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
LICENSED_INTERNAL_CODE_OPTIONS - Set and get the attribute value to a bogus value.
**/
    public void Var031()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS, "bogus licopts");
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }


/**
LICENSED_INTERNAL_CODE_OPTIONS - Set and get the attribute value something too long.
**/
    public void Var032()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            StringBuffer buffer = new StringBuffer(5000);
            for (int i = 0; i < 5000; ++i)
                buffer.append('Q');
            u.setAttributeValue(RJavaProgram.LICENSED_INTERNAL_CODE_OPTIONS, buffer.toString());
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
OPTIMIZATION - Check the attribute meta data in the entire list.
**/
    public void Var033()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.OPTIMIZATION, Integer.class, false, 5, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OPTIMIZATION - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var034()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.OPTIMIZATION);
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.OPTIMIZATION, Integer.class, false, 5, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OPTIMIZATION - Get the attribute value without setting it first.
**/
    public void Var035()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition(((Integer)value != null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OPTIMIZATION - Set and get the attribute value to *INTERPRET.
**/
    public void Var036()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_INTERPRET);
            u.commitAttributeChanges();

            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u2.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition(value.equals(RJavaProgram.OPTIMIZATION_INTERPRET));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
OPTIMIZATION - Set and get the attribute value to 10.
**/
    public void Var037()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
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
OPTIMIZATION - Set and get the attribute value to 20.
**/
    public void Var038()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_20);
            u.commitAttributeChanges();

            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u2.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition(value.equals(systemObject_.getVRM() >= 0x00050500 ? RJavaProgram.OPTIMIZATION_INTERPRET : RJavaProgram.OPTIMIZATION_20));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
OPTIMIZATION - Set and get the attribute value to 30.
**/
    public void Var039()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
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
OPTIMIZATION - Set and get the attribute value to 40.
**/
    public void Var040()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            u.setAttributeValue(RJavaProgram.OPTIMIZATION, RJavaProgram.OPTIMIZATION_40);
            u.commitAttributeChanges();

            RJavaProgram u2 = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u2.getAttributeValue(RJavaProgram.OPTIMIZATION);
            assertCondition(value.equals(systemObject_.getVRM() >= 0x00050500 ? RJavaProgram.OPTIMIZATION_INTERPRET : RJavaProgram.OPTIMIZATION_40));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OWNER - Check the attribute meta data in the entire list.
**/
    public void Var041()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.OWNER, String.class, true, 0, null,false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OWNER - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var042()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.OWNER);
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.OWNER, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OWNER - Get the attribute value.
**/
    public void Var043()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u.getAttributeValue(RJavaProgram.OWNER);
	    String systemObjectUserId=systemObject_.getUserId();
	    String pwrSysUserId =     pwrSys_.getUserId(); 
            assertCondition(((String)value).equalsIgnoreCase(systemObjectUserId) ||
			    ((String)value).equalsIgnoreCase(pwrSysUserId), 
			    "object owner attribute ="+value+" expected systemObjectUserId="+systemObjectUserId+
			    " or pwrSysUserId="+pwrSysUserId);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
        
        
        
/**
RELEASE_PROGRAM_CREATED_FOR - Check the attribute meta data in the entire list.
**/
    public void Var044()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.RELEASE_PROGRAM_CREATED_FOR, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
RELEASE_PROGRAM_CREATED_FOR - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var045()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.RELEASE_PROGRAM_CREATED_FOR);
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.RELEASE_PROGRAM_CREATED_FOR, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
RELEASE_PROGRAM_CREATED_FOR - Get the attribute value.
**/
    public void Var046()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u.getAttributeValue(RJavaProgram.RELEASE_PROGRAM_CREATED_FOR);
            StringBuffer buffer = new StringBuffer();
            buffer.append('V');
            buffer.append(systemObject_.getVersion());
            buffer.append('R');
            buffer.append(systemObject_.getRelease());
            buffer.append('M');
            buffer.append(systemObject_.getModification());
            assertCondition(((String)value).equals(buffer.toString()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
USE_ADOPTED_AUTHORITY - Check the attribute meta data in the entire list.
**/
    public void Var047()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.USE_ADOPTED_AUTHORITY, String.class, true, 2, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
USE_ADOPTED_AUTHORITY - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var048()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.USE_ADOPTED_AUTHORITY);
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.USE_ADOPTED_AUTHORITY, String.class, true, 2, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
USE_ADOPTED_AUTHORITY - Get the attribute value.
**/
    public void Var049()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u.getAttributeValue(RJavaProgram.USE_ADOPTED_AUTHORITY);
            assertCondition((((String)value).equals(RJavaProgram.YES))
                   || ((String)value).equals(RJavaProgram.NO));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
USER_PROFILE - Check the attribute meta data in the entire list.
**/
    public void Var050()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.USER_PROFILE, String.class, true, 2, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
USER_PROFILE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var051()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram();
            ResourceMetaData amd = u.getAttributeMetaData(RJavaProgram.USER_PROFILE);
            assertCondition(RJavaProgramGenericAttributeTestcase.verifyAttributeMetaData(amd, RJavaProgram.USER_PROFILE, String.class, true, 2, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
USER_PROFILE - Get the attribute value.
**/
    public void Var052()
    {
	if (true) {
	    notApplicable("Java program objects not supported after V7R1");
	    return; 
	} 
        try {
            RJavaProgram u = new RJavaProgram(systemObject_, RJavaTest.jarFilePath_);
            Object value = u.getAttributeValue(RJavaProgram.USER_PROFILE);
            assertCondition((((String)value).equals(RJavaProgram.USER_PROFILE_USER))
                   || ((String)value).equals(RJavaProgram.USER_PROFILE_OWNER));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }
        
        
        
        }


