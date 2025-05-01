///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RPrinterSpecificAttributeEtoMTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RPrint;

import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.resource.RPrinter;
import com.ibm.as400.resource.ResourceMetaData;

import test.Testcase;
import test.User.UserGenericAttributeTestcase;



/**
Testcase RPrinterSpecificAttributeEtoMTestcase.  This tests the following attributes
of the RPrinter class:

<ul>
<li>END_PENDING_STATUS
<li>FORM_TYPE
<li>HELD_STATUS
<li>HOLD_PENDING_STATUS
<li>JOB_NAME
<li>JOB_NUMBER
<li>JOB_QUEUE_STATUS
<li>MESSAGE_KEY
<li>MESSAGE_OPTION
<li>MESSAGE_QUEUE
</ul>
**/
@SuppressWarnings("deprecation")
public class RPrinterSpecificAttributeEtoMTestcase
extends Testcase {



    // Constants.



    // Private data.
    private String printerName_;



/**
Constructor.
**/
    public RPrinterSpecificAttributeEtoMTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys,
                              String misc)
    {
        super (systemObject, "RPrinterSpecificAttributeEtoMTestcase",
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
END_PENDING_STATUS - Check the attribute meta data in the entire list.
**/
    public void Var001()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.END_PENDING_STATUS, String.class, true, 5, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
END_PENDING_STATUS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var002()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.END_PENDING_STATUS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.END_PENDING_STATUS, String.class, true, 5, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
END_PENDING_STATUS - Get the attribute value.
**/
    public void Var003()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.END_PENDING_STATUS);
            assertCondition((value.equals(RPrinter.PENDING_STATUS_NONE)) 
                   || (value.equals(RPrinter.PENDING_STATUS_CONTROLLED))
                   || (value.equals(RPrinter.PENDING_STATUS_IMMEDIATE))
                   || (value.equals(RPrinter.PENDING_STATUS_PAGE_END)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
FORM_TYPE - Check the attribute meta data in the entire list.
**/
    public void Var004()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.FORM_TYPE, String.class, false, 3, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
FORM_TYPE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var005()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.FORM_TYPE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.FORM_TYPE, String.class, false, 3, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
FORM_TYPE - Get the attribute value without setting it first.
**/
    public void Var006()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition(true, "value="+value); 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
FORM_TYPE - Set and get the attribute value to all.
**/
    public void Var007()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_ALL);
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Object value = u2.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition(((String)value).equals(RPrinter.FORM_TYPE_ALL));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
FORM_TYPE - Set and get the attribute value to forms.
**/
    public void Var008()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_FORMS);
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Object value = u2.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition(((String)value).equals(RPrinter.FORM_TYPE_FORMS));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
FORM_TYPE - Set and get the attribute value to standard.
**/
    public void Var009()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_STANDARD);
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Object value = u2.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition(((String)value).equals(RPrinter.FORM_TYPE_STANDARD));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
FORM_TYPE - Set and get the attribute value to a valid form type.
**/
    public void Var010()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, "Joe Bob");
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Object value = u2.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition(((String)value).equals("Joe Bob"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
FORM_TYPE - Set the attribute value to be a valid form type that does not exist.
**/
    public void Var011()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, "NotEXIST");
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Object value = u2.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition(((String)value).equals("NotEXIST"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
FORM_TYPE - Set the attribute value to be a invalid form type name.
**/
    public void Var012()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, "1234567890a");
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
FORM_TYPE - Set the attribute value to be the empty string.
**/
    public void Var013()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, "");
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Object value = u2.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition(value instanceof String);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
HELD_STATUS - Check the attribute meta data in the entire list.
**/
    public void Var014()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.HELD_STATUS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
HELD_STATUS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var015()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.HELD_STATUS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.HELD_STATUS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
HELD_STATUS - Get the attribute value.
**/
    public void Var016()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Object value = u.getAttributeValue(RPrinter.HELD_STATUS);
            assertCondition(value instanceof Boolean); 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
HOLD_PENDING_STATUS - Check the attribute meta data in the entire list.
**/
    public void Var017()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.HOLD_PENDING_STATUS, String.class, true, 5, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
HOLD_PENDING_STATUS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var018()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.HOLD_PENDING_STATUS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.HOLD_PENDING_STATUS, String.class, true, 5, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
HOLD_PENDING_STATUS - Get the attribute value.
**/
    public void Var019()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.HOLD_PENDING_STATUS);
            assertCondition((value.equals(RPrinter.PENDING_STATUS_NONE)) 
                   || (value.equals(RPrinter.PENDING_STATUS_CONTROLLED))
                   || (value.equals(RPrinter.PENDING_STATUS_IMMEDIATE))
                   || (value.equals(RPrinter.PENDING_STATUS_PAGE_END)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JOB_NAME - Check the attribute meta data in the entire list.
**/
    public void Var020()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.JOB_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JOB_NAME - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var021()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.JOB_NAME);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.JOB_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JOB_NAME - Get the attribute value.
**/
    public void Var022()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Object value = u.getAttributeValue(RPrinter.JOB_NAME);
            assertCondition(value instanceof String);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JOB_NUMBER - Check the attribute meta data in the entire list.
**/
    public void Var023()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.JOB_NUMBER, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JOB_NUMBER - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var024()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.JOB_NUMBER);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.JOB_NUMBER, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JOB_NUMBER - Get the attribute value.
**/
    public void Var025()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Object value = u.getAttributeValue(RPrinter.JOB_NUMBER);
            assertCondition(value instanceof String);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JOB_QUEUE_STATUS - Check the attribute meta data in the entire list.
**/
    public void Var026()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.JOB_QUEUE_STATUS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JOB_QUEUE_STATUS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var027()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.JOB_QUEUE_STATUS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.JOB_QUEUE_STATUS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
JOB_QUEUE_STATUS - Get the attribute value.
**/
    public void Var028()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Object value = u.getAttributeValue(RPrinter.JOB_QUEUE_STATUS);
            assertCondition(value instanceof Boolean); 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MESSAGE_KEY - Check the attribute meta data in the entire list.
**/
    public void Var029()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.MESSAGE_KEY, byte[].class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MESSAGE_KEY - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var030()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.MESSAGE_KEY);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.MESSAGE_KEY, byte[].class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MESSAGE_KEY - Get the attribute value.
**/
    public void Var031()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            byte[] value = (byte[])u.getAttributeValue(RPrinter.MESSAGE_KEY);
            assertCondition(value.length == 4);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MESSAGE_OPTION - Check the attribute meta data in the entire list.
**/
    public void Var032()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.MESSAGE_OPTION, String.class, true, 5, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MESSAGE_OPTION - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var033()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.MESSAGE_OPTION);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.MESSAGE_OPTION, String.class, true, 5, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MESSAGE_OPTION - Get the attribute value.
**/
    public void Var034()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.MESSAGE_OPTION);
            assertCondition((value.equals(RPrinter.MESSAGE_OPTION_MESSAGE) 
                    || (value.equals(RPrinter.MESSAGE_OPTION_NO_MESSAGE))
                    || (value.equals(RPrinter.MESSAGE_OPTION_INFORMATIONAL_MESSAGE))
                    || (value.equals(RPrinter.MESSAGE_OPTION_INQUIRY_MESSAGE))));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MESSAGE_QUEUE - Check the attribute meta data in the entire list.
**/
    public void Var035()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.MESSAGE_QUEUE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MESSAGE_QUEUE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var036()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.MESSAGE_QUEUE);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.MESSAGE_QUEUE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
MESSAGE_QUEUE - Get the attribute value.
**/
    public void Var037()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.MESSAGE_QUEUE);
            QSYSObjectPathName path = new QSYSObjectPathName(value);
            assertCondition(path.getObjectType().equals("MSGQ"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




