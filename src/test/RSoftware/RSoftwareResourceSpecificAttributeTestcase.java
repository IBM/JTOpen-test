///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RSoftwareResourceSpecificAttributeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RSoftware;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.ResourceEvent;
import com.ibm.as400.resource.ResourceMetaData;

import test.RSoftwareTest;
import test.Testcase;

import com.ibm.as400.resource.RSoftwareResource;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase RSoftwareResourceSpecificAttributeTestcase.  This tests the following attributes
of the RSoftwareResource class:

<ul>
<li>LEVEL 
<li>LOAD_ERROR_INDICATOR 
<li>LOAD_ID 
<li>LOAD_STATE 
<li>LOAD_TYPE 
<li>MINIMUM_BASE_VRM 
<li>MINIMUM_TARGET_RELEASE 
<li>PRIMARY_LANGUAGE_LOAD_ID 
<li>PRODUCT_ID 
<li>PRODUCT_OPTION 
<li>REGISTRATION_TYPE 
<li>REGISTRATION_VALUE 
<li>RELEASE_LEVEL 
<li>REQUIREMENTS_MET 
<li>SUPPORTED_FLAG 
<li>SYMBOLIC_LOAD_STATE 
</ul>
**/
public class RSoftwareResourceSpecificAttributeTestcase
extends Testcase 
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "RSoftwareResourceSpecificAttributeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.RSoftwareTest.main(newArgs); 
   }



    // Constants.



    // Private data.
    private AS400           pwrSys_;
    private String          JC1ProductID_ = RSoftwareTest.JC1ProductID_;



/**
Constructor.
**/
    public RSoftwareResourceSpecificAttributeTestcase (AS400 systemObject,
                                                  Hashtable<String,Vector<String>> namesAndVars,
                                                  int runMode,
                                                  FileOutputStream fileOutputStream,
                                                  
                                                  String password,
                                                  AS400 pwrSys)
    {
        super (systemObject, "RSoftwareResourceSpecificAttributeTestcase",
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
      JC1ProductID_ = RSoftwareTest.JC1ProductID_;
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
LEVEL - Check the attribute meta data in the entire list.
**/
    public void Var001()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.LEVEL, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LEVEL - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var002()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.LEVEL);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.LEVEL, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LEVEL - Get the attribute value, for the operating system.
**/
    public void Var003()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            String value = (String)u.getAttributeValue(RSoftwareResource.LEVEL);
            assertCondition((value.length() == 3) && (value.charAt(0) == 'L'));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LEVEL - Get the attribute value, for an LPP.
**/
    public void Var004()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String value = (String)u.getAttributeValue(RSoftwareResource.LEVEL);

            if (getSystemVRM() > VRM_V6R1M0)
            	assertCondition((value.length() == 3) && (value.charAt(0) == 'L'));
            
            else
            	assertCondition((value.length() == 1) && (value.charAt(0) == 'L'));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LOAD_ERROR_INDICATOR - Check the attribute meta data in the entire list.
**/
    public void Var005()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.LOAD_ERROR_INDICATOR, String.class, true, 2, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LOAD_ERROR_INDICATOR - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var006()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.LOAD_ERROR_INDICATOR);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.LOAD_ERROR_INDICATOR, String.class, true, 2, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LOAD_ERROR_INDICATOR - Get the attribute value.
**/
    public void Var007()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String value = (String)u.getAttributeValue(RSoftwareResource.LOAD_ERROR_INDICATOR);
            assertCondition((value.equals(RSoftwareResource.LOAD_ERROR_INDICATOR_NONE)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
LOAD_ID - Check the attribute meta data in the entire list.
**/
    public void Var008()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.LOAD_ID, String.class, true, 1, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LOAD_ID - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var009()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.LOAD_ID);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.LOAD_ID, String.class, true, 1, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LOAD_ID - Get the attribute value, for a code load.
**/
    public void Var010()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String value = (String)u.getAttributeValue(RSoftwareResource.LOAD_ID);
            assertCondition(value.equals("5050"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LOAD_ID - Get the attribute value, for a feature.
**/
    public void Var011()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setLoadID("2924");
            String value = (String)u.getAttributeValue(RSoftwareResource.LOAD_ID);
            assertCondition(value.equals("2924"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
LOAD_STATE - Check the attribute meta data in the entire list.
**/
    public void Var012()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.LOAD_STATE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LOAD_STATE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var013()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.LOAD_STATE);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.LOAD_STATE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LOAD_STATE - Get the attribute value.
**/
    public void Var014()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String value = (String)u.getAttributeValue(RSoftwareResource.LOAD_STATE);
            assertCondition((value.equals("90")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LOAD_TYPE - Check the attribute meta data in the entire list.
**/
    public void Var015()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.LOAD_TYPE, String.class, true, 2, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LOAD_TYPE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var016()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.LOAD_TYPE);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.LOAD_TYPE, String.class, true, 2, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LOAD_TYPE - Get the attribute value, for a code load.
**/
    public void Var017()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String value = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition(value.equals(RSoftwareResource.LOAD_TYPE_CODE));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
LOAD_TYPE - Get the attribute value, for a feature.
**/
    public void Var018()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setLoadID("2924");
            String value = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition(value.equals(RSoftwareResource.LOAD_TYPE_LANGUAGE));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MINIMUM_BASE_VRM - Check the attribute meta data in the entire list.
**/
    public void Var019()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.MINIMUM_BASE_VRM, String.class, true, 1, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MINIMUM_BASE_VRM - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var020()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.MINIMUM_BASE_VRM);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.MINIMUM_BASE_VRM, String.class, true, 1, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MINIMUM_BASE_VRM - Get the attribute value, for the operating system.
**/
    public void Var021()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            String value = (String)u.getAttributeValue(RSoftwareResource.MINIMUM_BASE_VRM);
            assertCondition((value.equals(RSoftwareResource.MINIMUM_BASE_VRM_MATCH)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MINIMUM_BASE_VRM - Get the attribute value, for an LPP.
**/
    public void Var022()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String value = (String)u.getAttributeValue(RSoftwareResource.MINIMUM_BASE_VRM);
            assertCondition((value.equals(RSoftwareResource.MINIMUM_BASE_VRM_MATCH)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MINIMUM_TARGET_RELEASE - Check the attribute meta data in the entire list.
**/
    public void Var023()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.MINIMUM_TARGET_RELEASE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MINIMUM_TARGET_RELEASE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var024()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.MINIMUM_TARGET_RELEASE);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.MINIMUM_TARGET_RELEASE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MINIMUM_TARGET_RELEASE - Get the attribute value, for the operating system.
**/
    public void Var025()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            String value = (String)u.getAttributeValue(RSoftwareResource.MINIMUM_TARGET_RELEASE);
            assertCondition((value.equals("V" + systemObject_.getVersion() + "R" + systemObject_.getRelease() + "M" + systemObject_.getModification())));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PRIMARY_LANGUAGE_LOAD_ID - Check the attribute meta data in the entire list.
**/
    public void Var026()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.PRIMARY_LANGUAGE_LOAD_ID, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PRIMARY_LANGUAGE_LOAD_ID - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var027()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.PRIMARY_LANGUAGE_LOAD_ID);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.PRIMARY_LANGUAGE_LOAD_ID, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PRIMARY_LANGUAGE_LOAD_ID - Get the attribute value, for a code load.
**/
    public void Var028()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String value = (String)u.getAttributeValue(RSoftwareResource.PRIMARY_LANGUAGE_LOAD_ID);
            assertCondition(value.equals("2924"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PRIMARY_LANGUAGE_LOAD_ID - Get the attribute value, for a feature.
**/
    public void Var029()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setLoadID("2924");
            String value = (String)u.getAttributeValue(RSoftwareResource.PRIMARY_LANGUAGE_LOAD_ID);
            assertCondition(value.equals(""));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PRODUCT_ID - Check the attribute meta data in the entire list.
**/
    public void Var030()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.PRODUCT_ID, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PRODUCT_ID - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var031()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.PRODUCT_ID);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.PRODUCT_ID, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PRODUCT_ID - Get the attribute value, for the operating system.
**/
    public void Var032()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            String value = (String)u.getAttributeValue(RSoftwareResource.PRODUCT_ID);
            assertCondition((value.length() == 7) && (value.endsWith("SS1")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PRODUCT_ID - Get the attribute value, for an LPP.
**/
    public void Var033()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String value = (String)u.getAttributeValue(RSoftwareResource.PRODUCT_ID);
            assertCondition((value.equals(JC1ProductID_)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PRODUCT_OPTION - Check the attribute meta data in the entire list.
**/
    public void Var034()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.PRODUCT_OPTION, String.class, true, 1, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PRODUCT_OPTION - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var035()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.PRODUCT_OPTION);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.PRODUCT_OPTION, String.class, true, 1, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PRODUCT_OPTION - Get the attribute value, for the operating system, base option.
**/
    public void Var036()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            String value = (String)u.getAttributeValue(RSoftwareResource.PRODUCT_OPTION);
            assertCondition((value.equals(RSoftwareResource.PRODUCT_OPTION_BASE)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PRODUCT_OPTION - Get the attribute value, for the operating system, base option.
**/
    public void Var037()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            u.setProductOption("0001");
            String value = (String)u.getAttributeValue(RSoftwareResource.PRODUCT_OPTION);
            assertCondition((value.equals("0001")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
REGISTRATION_TYPE - Check the attribute meta data in the entire list.
**/
    public void Var038()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.REGISTRATION_TYPE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
REGISTRATION_TYPE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var039()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.REGISTRATION_TYPE);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.REGISTRATION_TYPE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
REGISTRATION_TYPE - Get the attribute value.
**/
    public void Var040()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String value = (String)u.getAttributeValue(RSoftwareResource.REGISTRATION_TYPE);
            assertCondition((value.equals("04")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
REGISTRATION_VALUE - Check the attribute meta data in the entire list.
**/
    public void Var041()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.REGISTRATION_VALUE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
REGISTRATION_VALUE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var042()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.REGISTRATION_VALUE);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.REGISTRATION_VALUE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
REGISTRATION_VALUE - Get the attribute value.
**/
    public void Var043()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String value = (String)u.getAttributeValue(RSoftwareResource.REGISTRATION_VALUE);
            assertCondition((value.equals("1-800-IBM-SERV")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
RELEASE_LEVEL - Check the attribute meta data in the entire list.
**/
    public void Var044()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.RELEASE_LEVEL, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
RELEASE_LEVEL - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var045()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.RELEASE_LEVEL);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.RELEASE_LEVEL, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
RELEASE_LEVEL - Get the attribute value, for the operating system.
**/
    public void Var046()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            String value = (String)u.getAttributeValue(RSoftwareResource.RELEASE_LEVEL);
            assertCondition((value.equals("V" + systemObject_.getVersion() + "R" + systemObject_.getRelease() + "M" + systemObject_.getModification())));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
RELEASE_LEVEL - Get the attribute value, for an LPP.
**/
    public void Var047()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String value = (String)u.getAttributeValue(RSoftwareResource.RELEASE_LEVEL);
            assertCondition((value.equals("V" + systemObject_.getVersion() + "R" + systemObject_.getRelease() + "M" + systemObject_.getModification())));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
REQUIREMENTS_MET - Check the attribute meta data in the entire list.
**/
    public void Var048()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.REQUIREMENTS_MET, String.class, true, 4, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
REQUIREMENTS_MET - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var049()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.REQUIREMENTS_MET);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.REQUIREMENTS_MET, String.class, true, 4, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
REQUIREMENTS_MET - Get the attribute value, for the operating system.
**/
    public void Var050()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            String value = (String)u.getAttributeValue(RSoftwareResource.REQUIREMENTS_MET);
            assertCondition((value.equals(RSoftwareResource.REQUIREMENTS_MET_ALL)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
REQUIREMENTS_MET - Get the attribute value, for an LPP.
**/
    public void Var051()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String value = (String)u.getAttributeValue(RSoftwareResource.REQUIREMENTS_MET);
            assertCondition((value.equals(RSoftwareResource.REQUIREMENTS_MET_ALL)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
SUPPORTED_FLAG - Check the attribute meta data in the entire list.
**/
    public void Var052()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.SUPPORTED_FLAG, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SUPPORTED_FLAG - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var053()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.SUPPORTED_FLAG);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.SUPPORTED_FLAG, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SUPPORTED_FLAG - Get the attribute value, for a code load.
**/
    public void Var054()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            Boolean value = (Boolean)u.getAttributeValue(RSoftwareResource.SUPPORTED_FLAG);
            assertCondition(value.booleanValue() == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SUPPORTED_FLAG - Get the attribute value, for an unsupported load.
**/
    public void Var055()
    {
      try {
        String prodID = "";
        // Pick a product that's generally marked "not supported".
        if (getSystemVRM() >=  VRM_V6R1M0) 
        {
	    if (getSystemVRM() ==  VRM_V6R1M0) {
        		prodID = "5761WDS";  // good for v6r1
	    } else { 
        		prodID = "5770WDS";  // good for v7r1
	    }
        	
        }
        else 
            prodID = "5722WDS";  // good for v5r2, v5r3, v5r4

        RSoftwareResource u = new RSoftwareResource(systemObject_, prodID);
        Boolean value = (Boolean)u.getAttributeValue(RSoftwareResource.SUPPORTED_FLAG);
        assertCondition(value.booleanValue() == false);
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }
    // Note to tester: If the above variation fails with "Assertion failed",
    // that probably means that the specified product has been marked as "supported".
    // You can then identify unsupported products by using the following code:
    //     AS400 system = new AS400(...);
    //     ProductList list = new ProductList(system);
    //     Product[] prods = list.getProducts();
    //     for (int i=0; i<prods.length; i++) {
    //       Product prod = prods[i];
    //       if (!prod.isSupported()) System.out.println(prod.toString());
    //     }




/**
SYMBOLIC_LOAD_STATE - Check the attribute meta data in the entire list.
**/
    public void Var056()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.SYMBOLIC_LOAD_STATE, String.class, true, 6, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SYMBOLIC_LOAD_STATE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var057()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.SYMBOLIC_LOAD_STATE);
            assertCondition(RSoftwareResourceGenericAttributeTestcase.verifyAttributeMetaData(amd, RSoftwareResource.SYMBOLIC_LOAD_STATE, String.class, true, 6, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SYMBOLIC_LOAD_STATE - Get the attribute value.
**/
    public void Var058()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            String value = (String)u.getAttributeValue(RSoftwareResource.SYMBOLIC_LOAD_STATE);
            assertCondition((value.equals(RSoftwareResource.SYMBOLIC_LOAD_STATE_DEFINED))
                   || (value.equals(RSoftwareResource.SYMBOLIC_LOAD_STATE_CREATED))
                   || (value.equals(RSoftwareResource.SYMBOLIC_LOAD_STATE_PACKAGED))
                   || (value.equals(RSoftwareResource.SYMBOLIC_LOAD_STATE_DAMAGED))
                   || (value.equals(RSoftwareResource.SYMBOLIC_LOAD_STATE_LOADED))
                   || (value.equals(RSoftwareResource.SYMBOLIC_LOAD_STATE_INSTALLED)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


        
}


