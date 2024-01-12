///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RPrinterSpecificAttributeTtoZTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RPrint;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.QSYSObjectPathName;
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
Testcase RPrinterSpecificAttributeTtoZTestcase.  This tests the following attributes
of the RPrinter class:

<ul>
<li>TEXT_DESCRIPTION
<li>TOTAL_COPIES
<li>TOTAL_PAGES
<li>USER_NAME
<li>WAITING_FOR_DATA_STATUS
<li>WAITING_FOR_DEVICE_STATUS
<li>WAITING_FOR_MESSAGE_STATUS
<li>WRITER_JOB_NAME
<li>WRITER_JOB_NUMBER
<li>WRITER_JOB_USER_NAME
<li>WRITER_STARTED
<li>WRITER_STATUS
<li>WRITING_STATUS
</ul>
**/
public class RPrinterSpecificAttributeTtoZTestcase
extends Testcase {



    // Constants.



    // Private data.
    private String printerName_;



/**
Constructor.
**/
    public RPrinterSpecificAttributeTtoZTestcase (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys,
                              String misc)
    {
        super (systemObject, "RPrinterSpecificAttributeTtoZTestcase",
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
TEXT_DESCRIPTION - Check the attribute meta data in the entire list.
**/
    public void Var001()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.TEXT_DESCRIPTION, String.class, false, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
TEXT_DESCRIPTION - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var002()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.TEXT_DESCRIPTION);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.TEXT_DESCRIPTION, String.class, false, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
TEXT_DESCRIPTION - Get the attribute value without reading it first.
**/
    public void Var003()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            assertCondition(value.length() > 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
TEXT_DESCRIPTION - Set and Get the attribute value to "".
**/
    public void Var004()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.TEXT_DESCRIPTION, "");
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            String value = (String)u2.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            assertCondition(value.equals(""));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
TEXT_DESCRIPTION - Set and Get the attribute value to a valid description.
**/
    public void Var005()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.TEXT_DESCRIPTION, "This is a valid description");
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            String value = (String)u2.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            assertCondition(value.equals("This is a valid description"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
TOTAL_COPIES - Check the attribute meta data in the entire list.
**/
    public void Var006()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.TOTAL_COPIES, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
TOTAL_COPIES - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var007()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.TOTAL_COPIES);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.TOTAL_COPIES, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
TOTAL_COPIES - Get the attribute value.
**/
    public void Var008()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u.getAttributeValue(RPrinter.TOTAL_COPIES);
            assertCondition(value.intValue() >= 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
TOTAL_PAGES - Check the attribute meta data in the entire list.
**/
    public void Var009()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.TOTAL_PAGES, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
TOTAL_PAGES - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var010()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.TOTAL_PAGES);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.TOTAL_PAGES, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
TOTAL_PAGES - Get the attribute value.
**/
    public void Var011()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u.getAttributeValue(RPrinter.TOTAL_PAGES);
            assertCondition(value.intValue() >= 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
USER_NAME - Check the attribute meta data in the entire list.
**/
    public void Var012()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.USER_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
USER_NAME - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var013()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.USER_NAME);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.USER_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
USER_NAME - Get the attribute value.
**/
    public void Var014()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.USER_NAME);
            assertCondition(true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
WAITING_FOR_DATA_STATUS - Check the attribute meta data in the entire list.
**/
    public void Var015()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WAITING_FOR_DATA_STATUS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WAITING_FOR_DATA_STATUS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var016()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.WAITING_FOR_DATA_STATUS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WAITING_FOR_DATA_STATUS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WAITING_FOR_DATA_STATUS - Get the attribute value.
**/
    public void Var017()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Boolean value = (Boolean)u.getAttributeValue(RPrinter.WAITING_FOR_DATA_STATUS);
            assertCondition(true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
WAITING_FOR_DEVICE_STATUS - Check the attribute meta data in the entire list.
**/
    public void Var018()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WAITING_FOR_DEVICE_STATUS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WAITING_FOR_DEVICE_STATUS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var019()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.WAITING_FOR_DEVICE_STATUS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WAITING_FOR_DEVICE_STATUS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WAITING_FOR_DEVICE_STATUS - Get the attribute value.
**/
    public void Var020()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Boolean value = (Boolean)u.getAttributeValue(RPrinter.WAITING_FOR_DEVICE_STATUS);
            assertCondition(true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
WAITING_FOR_MESSAGE_STATUS - Check the attribute meta data in the entire list.
**/
    public void Var021()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WAITING_FOR_MESSAGE_STATUS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WAITING_FOR_MESSAGE_STATUS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var022()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.WAITING_FOR_MESSAGE_STATUS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WAITING_FOR_MESSAGE_STATUS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WAITING_FOR_MESSAGE_STATUS - Get the attribute value.
**/
    public void Var023()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Boolean value = (Boolean)u.getAttributeValue(RPrinter.WAITING_FOR_MESSAGE_STATUS);
            assertCondition(true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITER_JOB_NAME - Check the attribute meta data in the entire list.
**/
    public void Var024()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WRITER_JOB_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITER_JOB_NAME - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var025()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.WRITER_JOB_NAME);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WRITER_JOB_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITER_JOB_NAME - Get the attribute value.
**/
    public void Var026()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.WRITER_JOB_NAME);
            assertCondition(value.equalsIgnoreCase(printerName_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITER_JOB_NUMBER - Check the attribute meta data in the entire list.
**/
    public void Var027()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WRITER_JOB_NUMBER, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITER_JOB_NUMBER - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var028()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.WRITER_JOB_NUMBER);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WRITER_JOB_NUMBER, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITER_JOB_NUMBER - Get the attribute value.
**/
    public void Var029()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.WRITER_JOB_NUMBER);
            assertCondition(value.length() == 6);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
WRITER_JOB_USER_NAME - Check the attribute meta data in the entire list.
**/
    public void Var030()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WRITER_JOB_USER_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITER_JOB_USER_NAME - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var031()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.WRITER_JOB_USER_NAME);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WRITER_JOB_USER_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITER_JOB_USER_NAME - Get the attribute value.
**/
    public void Var032()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.WRITER_JOB_USER_NAME);
            assertCondition(value.equals("QSPLJOB"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITER_STARTED - Check the attribute meta data in the entire list.
**/
    public void Var033()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WRITER_STARTED, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITER_STARTED - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var034()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.WRITER_STARTED);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WRITER_STARTED, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITER_STARTED - Get the attribute value.
**/
    public void Var035()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Boolean value = (Boolean)u.getAttributeValue(RPrinter.WRITER_STARTED);
            assertCondition(true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
WRITER_STATUS - Check the attribute meta data in the entire list.
**/
    public void Var036()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WRITER_STATUS, byte[].class, true, 5, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITER_STATUS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var037()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.WRITER_STATUS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WRITER_STATUS, byte[].class, true, 5, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITER_STATUS - Get the attribute value.
**/
    public void Var038()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            byte[] value = (byte[])u.getAttributeValue(RPrinter.WRITER_STATUS);
            byte b0 = value[0];
            assertCondition((value.length == 1)
                   && ((b0 == RPrinter.WRITER_STATUS_STARTED[0])
                   || (b0 == RPrinter.WRITER_STATUS_ENDED[0])
                   || (b0 == RPrinter.WRITER_STATUS_JOB_QUEUE[0])
                   || (b0 == RPrinter.WRITER_STATUS_HELD[0])
                   || (b0 == RPrinter.WRITER_STATUS_MESSAGE_WAITING[0])));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITING_STATUS - Check the attribute meta data in the entire list.
**/
    public void Var039()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WRITING_STATUS, String.class, true, 4, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITING_STATUS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var040()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.WRITING_STATUS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.WRITING_STATUS, String.class, true, 4, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
WRITING_STATUS - Get the attribute value.
**/
    public void Var041()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.WRITING_STATUS);
            assertCondition((value.equals(RPrinter.WRITING_STATUS_YES))
                   || (value.equals(RPrinter.WRITING_STATUS_NO))
                   || (value.equals(RPrinter.WRITING_STATUS_SEPARATORS)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}

