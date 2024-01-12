///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RPrinterSpecificAttributeAtoDTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RPrint;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.ResourceEvent;
import com.ibm.as400.resource.ResourceMetaData;

import test.Testcase;

import com.ibm.as400.resource.RPrinter;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;



/**
Testcase RPrinterSpecificAttributeAtoDTestcase.  This tests the following attributes
of the RPrinter class:

<ul>
<li>ADVANCED_FUNCTION_PRINTING
<li>ALIGN_FORMS
<li>ALLOW_DIRECT_PRINTING
<li>AUTOMATICALLY_END_WRITER
<li>BETWEEN_COPIES_STATUS
<li>BETWEEN_FILES_STATUS
<li>CHANGES_TAKE_EFFECT
<li>COPIES_LEFT_TO_PRODUCE
<li>DEVICE_NAME
<li>DEVICE_STATUS
<li>DEVICE_TYPE
</ul>
**/
public class RPrinterSpecificAttributeAtoDTestcase
extends Testcase {



    // Constants.



    // Private data.
    private String printerName_;



/**
Constructor.
**/
    public RPrinterSpecificAttributeAtoDTestcase (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys,
                              String misc)
    {
        super (systemObject, "RPrinterSpecificAttributeAtoDTestcase",
               namesAndVars, runMode, fileOutputStream,
               password);
        systemObject_ = pwrSys;
        printerName_ = misc;

        if (pwrSys == null)
            throw new IllegalStateException("ERROR: Please specify a power system via -pwrsys.");
        if (misc == null)
            throw new IllegalStateException("ERROR: Please specify a printer via -misc.");
    }




/**
ADVANCED_FUNCTION_PRINTING - Check the attribute meta data in the entire list.
**/
    public void Var001()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.ADVANCED_FUNCTION_PRINTING, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ADVANCED_FUNCTION_PRINTING - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var002()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.ADVANCED_FUNCTION_PRINTING);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.ADVANCED_FUNCTION_PRINTING, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ADVANCED_FUNCTION_PRINTING - Get the attribute value.
**/
    public void Var003()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Object value = u.getAttributeValue(RPrinter.ADVANCED_FUNCTION_PRINTING);
            assertCondition(value instanceof Boolean); // We don't check the results...
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
ALIGN_FORMS - Check the attribute meta data in the entire list.
**/
    public void Var004()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.ALIGN_FORMS, String.class, true, 3, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ALIGN_FORMS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var005()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.ALIGN_FORMS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.ALIGN_FORMS, String.class, true, 3, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ALIGN_FORMS - Get the attribute value.
**/
    public void Var006()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.ALIGN_FORMS);
            assertCondition((value.equals(RPrinter.ALIGN_FORMS_FILE)) || (value.equals(RPrinter.ALIGN_FORMS_WRITER)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
ALLOW_DIRECT_PRINTING - Check the attribute meta data in the entire list.
**/
    public void Var007()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.ALLOW_DIRECT_PRINTING, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ALLOW_DIRECT_PRINTING - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var008()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.ALLOW_DIRECT_PRINTING);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.ALLOW_DIRECT_PRINTING, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
ALLOW_DIRECT_PRINTING - Get the attribute value.
**/
    public void Var009()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Object value = u.getAttributeValue(RPrinter.ALLOW_DIRECT_PRINTING);
            assertCondition(value instanceof Boolean); 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
AUTOMATICALLY_END_WRITER - Check the attribute meta data in the entire list.
**/
    public void Var010()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.AUTOMATICALLY_END_WRITER, String.class, true, 4, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
AUTOMATICALLY_END_WRITER - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var011()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.AUTOMATICALLY_END_WRITER);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.AUTOMATICALLY_END_WRITER, String.class, true, 4, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
AUTOMATICALLY_END_WRITER - Get the attribute value.
**/
    public void Var012()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.AUTOMATICALLY_END_WRITER);
            assertCondition((value.equals(RPrinter.OPERATION_NO_FILES_READY) 
                    || (value.equals(RPrinter.OPERATION_FILE_END))
                    || (value.equals(RPrinter.OPERATION_NONE))));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
BETWEEN_COPIES_STATUS - Check the attribute meta data in the entire list.
**/
    public void Var013()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.BETWEEN_COPIES_STATUS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
BETWEEN_COPIES_STATUS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var014()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.BETWEEN_COPIES_STATUS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.BETWEEN_COPIES_STATUS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
BETWEEN_COPIES_STATUS - Get the attribute value.
**/
    public void Var015()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Object value = u.getAttributeValue(RPrinter.BETWEEN_COPIES_STATUS);
            assertCondition(value instanceof Boolean);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
BETWEEN_FILES_STATUS - Check the attribute meta data in the entire list.
**/
    public void Var016()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.BETWEEN_FILES_STATUS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
BETWEEN_FILES_STATUS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var017()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.BETWEEN_FILES_STATUS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.BETWEEN_FILES_STATUS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
BETWEEN_FILES_STATUS - Get the attribute value.
**/
    public void Var018()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Object value = u.getAttributeValue(RPrinter.BETWEEN_FILES_STATUS);
            assertCondition(value instanceof Boolean);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CHANGES_TAKE_EFFECT - Check the attribute meta data in the entire list.
**/
    public void Var019()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.CHANGES_TAKE_EFFECT, String.class, false, 3, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CHANGES_TAKE_EFFECT - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var020()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.CHANGES_TAKE_EFFECT);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.CHANGES_TAKE_EFFECT, String.class, false, 3, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CHANGES_TAKE_EFFECT - Get the attribute value without setting it first.
**/
    public void Var021()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.CHANGES_TAKE_EFFECT);
            assertCondition((value.equals(RPrinter.OPERATION_NO_FILES_READY))
                   || (value.equals(RPrinter.OPERATION_FILE_END))
                   || (value.equals("")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CHANGES_TAKE_EFFECT - Set and get the attribute value to no files ready.
**/
    public void Var022()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.CHANGES_TAKE_EFFECT, RPrinter.OPERATION_NO_FILES_READY);
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            String value = (String)u2.getAttributeValue(RPrinter.CHANGES_TAKE_EFFECT);
            assertCondition((value.equals(RPrinter.OPERATION_NO_FILES_READY)) || (value.length() == 0)); 
            // Allow the case where no changes are pending (this test would involve
            // setting up print jobs and actually printing stuff!).
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CHANGES_TAKE_EFFECT - Set and get the attribute value to 
file end.
**/
    public void Var023()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.CHANGES_TAKE_EFFECT, RPrinter.OPERATION_FILE_END);
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            String value = (String)u2.getAttributeValue(RPrinter.CHANGES_TAKE_EFFECT);
            assertCondition((value.equals(RPrinter.OPERATION_FILE_END)) || (value.length() == 0)); 
            // Allow the case where no changes are pending (this test would involve
            // setting up print jobs and actually printing stuff!).
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CHANGES_TAKE_EFFECT - Set and get the attribute value to "".
**/
    public void Var024()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.CHANGES_TAKE_EFFECT, "");
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Object value = u2.getAttributeValue(RPrinter.CHANGES_TAKE_EFFECT);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
CHANGES_TAKE_EFFECT - Set the attribute value to be a bogus string.
**/
    public void Var025()
    {
        try {
            String bogusString = "bogus";

            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.CHANGES_TAKE_EFFECT, bogusString);
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
COPIES_LEFT_TO_PRODUCE - Check the attribute meta data in the entire list.
**/
    public void Var026()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.COPIES_LEFT_TO_PRODUCE, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
COPIES_LEFT_TO_PRODUCE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var027()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.COPIES_LEFT_TO_PRODUCE);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.COPIES_LEFT_TO_PRODUCE, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
COPIES_LEFT_TO_PRODUCE - Get the attribute value.
**/
    public void Var028()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u.getAttributeValue(RPrinter.COPIES_LEFT_TO_PRODUCE);
            assertCondition(value.intValue() >= 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
DEVICE_NAME - Check the attribute meta data in the entire list.
**/
    public void Var029()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.DEVICE_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
DEVICE_NAME - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var030()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.DEVICE_NAME);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.DEVICE_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
DEVICE_NAME - Get the attribute value.
**/
    public void Var031()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Object value = u.getAttributeValue(RPrinter.DEVICE_NAME);
            assertCondition(((String)value).equals(printerName_.toUpperCase()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
DEVICE_STATUS - Check the attribute meta data in the entire list.
**/
    public void Var032()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.DEVICE_STATUS, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
DEVICE_STATUS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var033()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.DEVICE_STATUS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.DEVICE_STATUS, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
DEVICE_STATUS - Get the attribute value.
**/
    public void Var034()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u.getAttributeValue(RPrinter.DEVICE_STATUS);
            assertCondition(value.intValue() >= 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
DEVICE_TYPE - Check the attribute meta data in the entire list.
**/
    public void Var035()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.DEVICE_TYPE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
DEVICE_TYPE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var036()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.DEVICE_TYPE);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.DEVICE_TYPE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
DEVICE_TYPE - Get the attribute value.
**/
    public void Var037()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.DEVICE_TYPE);
            assertCondition(value.length() > 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




