///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RPrinterSpecificAttributeNtoSTestcase.java
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
Testcase RPrinterSpecificAttributeNtoSTestcase.  This tests the following attributes
of the RPrinter class:

<ul>
<li>NEXT_FILE_SEPARATORS
<li>NEXT_FORM_TYPE
<li>NEXT_MESSAGE_OPTION
<li>NEXT_OUTPUT_QUEUE
<li>NEXT_SEPARATOR_DRAWER
<li>NUMBER_OF_SEPARATORS
<li>OUTPUT_QUEUE
<li>OUTPUT_QUEUE_STATUS
<li>OVERALL_STATUS
<li>PAGE_BEING_WRITTEN
<li>PUBLISHED_STATUS
<li>SEPARATOR_DRAWER
<li>SPOOLED_FILE_NAME
<li>SPOOLED_FILE_NUMBER
<li>STARTED_BY_USER
</ul>
**/
@SuppressWarnings("deprecation")
public class RPrinterSpecificAttributeNtoSTestcase
extends Testcase {



    // Constants.



    // Private data.
    private String printerName_;



/**
Constructor.
**/
    public RPrinterSpecificAttributeNtoSTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys,
                              String misc)
    {
        super (systemObject, "RPrinterSpecificAttributeNtoSTestcase",
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
NEXT_FILE_SEPARATORS - Check the attribute meta data in the entire list.
**/
    public void Var001()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.NEXT_FILE_SEPARATORS, Integer.class, true, 2, null, false, false,output_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NEXT_FILE_SEPARATORS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var002()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.NEXT_FILE_SEPARATORS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.NEXT_FILE_SEPARATORS, Integer.class, true, 2, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NEXT_FILE_SEPARATORS - Get the attribute value.
**/
    public void Var003()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u.getAttributeValue(RPrinter.NEXT_FILE_SEPARATORS);
            assertCondition((value.equals(RPrinter.SEPARATOR_PAGE_FILE)) 
                   || (value.equals(RPrinter.SEPARATOR_PAGE_NONE))
                   || (value.intValue() >= 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
NEXT_FORM_TYPE - Check the attribute meta data in the entire list.
**/
    public void Var004()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.NEXT_FORM_TYPE, String.class, true, 3, null, false, false, output_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NEXT_FORM_TYPE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var005()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.NEXT_FORM_TYPE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.NEXT_FORM_TYPE, String.class, true, 3, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NEXT_FORM_TYPE - Get the attribute value.
**/
    public void Var006()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.NEXT_FORM_TYPE);
            assertCondition(true,"value="+value); 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NEXT_MESSAGE_OPTION - Check the attribute meta data in the entire list.
**/
    public void Var007()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.NEXT_MESSAGE_OPTION, String.class, true, 5, null, true, false,output_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NEXT_MESSAGE_OPTION - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var008()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.NEXT_MESSAGE_OPTION);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.NEXT_MESSAGE_OPTION, String.class, true, 5, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NEXT_MESSAGE_OPTION - Get the attribute value.
**/
    public void Var009()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.MESSAGE_OPTION);
            assertCondition((value.equals(RPrinter.MESSAGE_OPTION_MESSAGE) 
                    || (value.equals(RPrinter.MESSAGE_OPTION_NO_MESSAGE))
                    || (value.equals(RPrinter.MESSAGE_OPTION_INFORMATIONAL_MESSAGE))
                    || (value.equals(RPrinter.MESSAGE_OPTION_INQUIRY_MESSAGE))
                    || (value.equals(""))));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NEXT_OUTPUT_QUEUE - Check the attribute meta data in the entire list.
**/
    public void Var010()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.NEXT_OUTPUT_QUEUE, String.class, true, 0, null, false, false,output_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NEXT_OUTPUT_QUEUE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var011()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.NEXT_OUTPUT_QUEUE);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.NEXT_OUTPUT_QUEUE, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NEXT_OUTPUT_QUEUE - Get the attribute value.
**/
    public void Var012()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.NEXT_OUTPUT_QUEUE);
            String type = "";
            if (value.length() > 0) {
                QSYSObjectPathName path = new QSYSObjectPathName(value);
                type = path.getObjectType();
            }
            assertCondition((type.equals("OUTQ")) || (value.equals("")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NEXT_SEPARATOR_DRAWER - Check the attribute meta data in the entire list.
**/
    public void Var013()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.NEXT_SEPARATOR_DRAWER, Integer.class, true, 3, null, false, false,output_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NEXT_SEPARATOR_DRAWER - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var014()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.NEXT_SEPARATOR_DRAWER);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.NEXT_SEPARATOR_DRAWER, Integer.class, true, 3, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NEXT_SEPARATOR_DRAWER - Get the attribute value.
**/
    public void Var015()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u.getAttributeValue(RPrinter.NEXT_SEPARATOR_DRAWER);
            assertCondition((value.equals(RPrinter.SEPARATOR_PAGE_FILE)) 
                   || (value.equals(RPrinter.SEPARATOR_PAGE_NONE))
                   || (value.equals(RPrinter.SEPARATOR_PAGE_DEVICE))
                   || (value.intValue() >= 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NUMBER_OF_SEPARATORS - Check the attribute meta data in the entire list.
**/
    public void Var016()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.NUMBER_OF_SEPARATORS, Integer.class, false, 1, null, false, false,output_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NUMBER_OF_SEPARATORS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var017()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.NUMBER_OF_SEPARATORS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.NUMBER_OF_SEPARATORS, Integer.class, false, 1, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NUMBER_OF_SEPARATORS - Get the attribute value without reading it first.
**/
    public void Var018()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u.getAttributeValue(RPrinter.NUMBER_OF_SEPARATORS);
            assertCondition((value.equals(RPrinter.SEPARATOR_PAGE_FILE)) 
                   || (value.intValue() >= 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
NUMBER_OF_SEPARATORS - Set and Get the attribute value to file.
**/
    public void Var019()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.NUMBER_OF_SEPARATORS, RPrinter.SEPARATOR_PAGE_FILE);
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u2.getAttributeValue(RPrinter.NUMBER_OF_SEPARATORS);
            assertCondition(value.equals(RPrinter.SEPARATOR_PAGE_FILE));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
NUMBER_OF_SEPARATORS - Set and Get the attribute value to -99.
**/
    public void Var020()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.NUMBER_OF_SEPARATORS, Integer.valueOf(99));
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
NUMBER_OF_SEPARATORS - Set and Get the attribute value to 0.
**/
    public void Var021()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.NUMBER_OF_SEPARATORS, Integer.valueOf(0));
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u2.getAttributeValue(RPrinter.NUMBER_OF_SEPARATORS);
            assertCondition(value.intValue() == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NUMBER_OF_SEPARATORS - Set and Get the attribute value to 1.
**/
    public void Var022()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.NUMBER_OF_SEPARATORS, Integer.valueOf(1));
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u2.getAttributeValue(RPrinter.NUMBER_OF_SEPARATORS);
            assertCondition(value.intValue() == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NUMBER_OF_SEPARATORS - Set and Get the attribute value to 9.
**/
    public void Var023()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.NUMBER_OF_SEPARATORS, Integer.valueOf(9));
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u2.getAttributeValue(RPrinter.NUMBER_OF_SEPARATORS);
            assertCondition(value.intValue() == 9);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
NUMBER_OF_SEPARATORS - Set and Get the attribute value to 10.
**/
    public void Var024()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.NUMBER_OF_SEPARATORS, Integer.valueOf(10));
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }
    


/**
OUTPUT_QUEUE - Check the attribute meta data in the entire list.
**/
    public void Var025()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.OUTPUT_QUEUE, String.class, false, 0, null, false, false,output_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OUTPUT_QUEUE - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var026()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.OUTPUT_QUEUE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.OUTPUT_QUEUE, String.class, false, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OUTPUT_QUEUE - Get the attribute value without setting it first.
**/
    public void Var027()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.OUTPUT_QUEUE);
            QSYSObjectPathName path = new QSYSObjectPathName(value);
            assertCondition(path.getObjectType().equals("OUTQ"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OUTPUT_QUEUE - Set and get the attribute value to a valid output queue .
**/
    public void Var028()
    {
        try {
            String outq = "/QSYS.LIB/QUSRSYS.LIB/" + printerName_ + ".OUTQ";

            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.OUTPUT_QUEUE, outq);
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Object value = u2.getAttributeValue(RPrinter.OUTPUT_QUEUE);
            assertCondition(((String)value).equalsIgnoreCase(outq));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OUTPUT_QUEUE - Set the attribute value to be a valid output queue name that does not exist.
**/
    public void Var029()
    {
        try {
            String outq = "/QSYS.LIB/NOTEXIST.OUTQ";

            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.OUTPUT_QUEUE, outq);
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }




/**
OUTPUT_QUEUE - Set the attribute value to be a invalid output queue name.
**/
    public void Var030()
    {
        try {
            String outq = "//////";

            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.OUTPUT_QUEUE, outq);
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
OUTPUT_QUEUE - Set the attribute value to be the empty string.
**/
    public void Var031()
    {
        try {
            String outq = "";

            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.OUTPUT_QUEUE, outq);
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
OUTPUT_QUEUE_STATUS - Check the attribute meta data in the entire list.
**/
    public void Var032()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.OUTPUT_QUEUE_STATUS, String.class, true, 3, null, true, false,output_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OUTPUT_QUEUE_STATUS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var033()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.OUTPUT_QUEUE_STATUS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.OUTPUT_QUEUE_STATUS, String.class, true, 3, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OUTPUT_QUEUE_STATUS - Get the attribute value.
**/
    public void Var034()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.OUTPUT_QUEUE_STATUS);
            assertCondition((value.equals(RPrinter.STATUS_HELD)) 
                   || (value.equals(RPrinter.STATUS_RELEASED)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
OVERALL_STATUS - Check the attribute meta data in the entire list.
**/
    public void Var035()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.OVERALL_STATUS, Integer.class, true, 0, null, false, false,output_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OVERALL_STATUS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var036()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.OVERALL_STATUS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.OVERALL_STATUS, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
OVERALL_STATUS - Get the attribute value.
**/
    public void Var037()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u.getAttributeValue(RPrinter.OVERALL_STATUS);
            assertCondition(value.intValue() >= 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
PAGE_BEING_WRITTEN - Check the attribute meta data in the entire list.
**/
    public void Var038()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.PAGE_BEING_WRITTEN, Integer.class, true, 0, null, false, false,output_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PAGE_BEING_WRITTEN - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var039()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.PAGE_BEING_WRITTEN);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.PAGE_BEING_WRITTEN, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PAGE_BEING_WRITTEN - Get the attribute value.
**/
    public void Var040()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u.getAttributeValue(RPrinter.PAGE_BEING_WRITTEN);
            assertCondition(value.intValue() >= 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
PUBLISHED_STATUS - Check the attribute meta data in the entire list.
**/
    public void Var041()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.PUBLISHED_STATUS, Boolean.class, true, 0, null, false, false,output_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PUBLISHED_STATUS - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var042()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.PUBLISHED_STATUS);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.PUBLISHED_STATUS, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
PUBLISHED_STATUS - Get the attribute value.
**/
    public void Var043()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Boolean value = (Boolean)u.getAttributeValue(RPrinter.PUBLISHED_STATUS);
            assertCondition(true,"value="+value); 
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
SEPARATOR_DRAWER - Check the attribute meta data in the entire list.
**/
    public void Var044()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.SEPARATOR_DRAWER, Integer.class, false, 2, null, false, false,output_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SEPARATOR_DRAWER - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var045()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.SEPARATOR_DRAWER);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.SEPARATOR_DRAWER, Integer.class, false, 2, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SEPARATOR_DRAWER - Get the attribute value when it has not been set.
**/
    public void Var046()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u.getAttributeValue(RPrinter.SEPARATOR_DRAWER);
            assertCondition((value.equals(RPrinter.SEPARATOR_PAGE_FILE)) 
                   || (value.equals(RPrinter.SEPARATOR_PAGE_DEVICE))
                   || (value.intValue() >= 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SEPARATOR_DRAWER - Set and get the attribute value to file.
**/
    public void Var047()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.SEPARATOR_DRAWER, RPrinter.SEPARATOR_PAGE_FILE);
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u2.getAttributeValue(RPrinter.SEPARATOR_DRAWER);
            assertCondition(value.equals(RPrinter.SEPARATOR_PAGE_FILE));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SEPARATOR_DRAWER - Set and get the attribute value to device.
**/
    public void Var048()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.SEPARATOR_DRAWER, RPrinter.SEPARATOR_PAGE_DEVICE);
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u2.getAttributeValue(RPrinter.SEPARATOR_DRAWER);
            assertCondition((value.equals(RPrinter.SEPARATOR_PAGE_DEVICE))
                   || (value.equals(RPrinter.SEPARATOR_PAGE_FILE))); // Allow File because thats what the system does.
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SEPARATOR_DRAWER - Set and get the attribute value to -10.
**/
    public void Var049()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.SEPARATOR_DRAWER, Integer.valueOf(-10));
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
SEPARATOR_DRAWER - Set and get the attribute value to 0.
**/
    public void Var050()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.SEPARATOR_DRAWER, Integer.valueOf(0));
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
SEPARATOR_DRAWER - Set and get the attribute value to 1.
**/
    public void Var051()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.SEPARATOR_DRAWER, Integer.valueOf(1));
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u2.getAttributeValue(RPrinter.SEPARATOR_DRAWER);
            assertCondition(value.intValue() == 1);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SEPARATOR_DRAWER - Set and get the attribute value to 255.
**/
    public void Var052()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.SEPARATOR_DRAWER, Integer.valueOf(255));
            u.commitAttributeChanges();

            RPrinter u2 = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u2.getAttributeValue(RPrinter.SEPARATOR_DRAWER);
            assertCondition(value.intValue() == 255);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SEPARATOR_DRAWER - Set and get the attribute value to 256.
**/
    public void Var053()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            u.setAttributeValue(RPrinter.SEPARATOR_DRAWER, Integer.valueOf(256));
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
SPOOLED_FILE_NAME - Check the attribute meta data in the entire list.
**/
    public void Var054()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.SPOOLED_FILE_NAME, String.class, true, 0, null, false, false,output_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SPOOLED_FILE_NAME - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var055()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.SPOOLED_FILE_NAME);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.SPOOLED_FILE_NAME, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SPOOLED_FILE_NAME - Get the attribute value.
**/
    public void Var056()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.SPOOLED_FILE_NAME);
            assertCondition(value != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SPOOLED_FILE_NUMBER - Check the attribute meta data in the entire list.
**/
    public void Var057()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.SPOOLED_FILE_NUMBER, Integer.class, true, 0, null, false, false,output_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SPOOLED_FILE_NUMBER - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var058()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.SPOOLED_FILE_NUMBER);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.SPOOLED_FILE_NUMBER, Integer.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
SPOOLED_FILE_NUMBER - Get the attribute value.
**/
    public void Var059()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            Integer value = (Integer)u.getAttributeValue(RPrinter.SPOOLED_FILE_NUMBER);
            assertCondition(value.intValue() >= 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
STARTED_BY_USER - Check the attribute meta data in the entire list.
**/
    public void Var060()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.STARTED_BY_USER, String.class, true, 0, null, false, false,output_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
STARTED_BY_USER - Check the attribute meta data when retrieved for only this attribute.
**/
    public void Var061()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.STARTED_BY_USER);
            assertCondition(RPrinterGenericAttributeTestcase.verifyAttributeMetaData(amd, RPrinter.STARTED_BY_USER, String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
STARTED_BY_USER - Get the attribute value.
**/
    public void Var062()
    {
        try {
            RPrinter u = new RPrinter(systemObject_, printerName_);
            String value = (String)u.getAttributeValue(RPrinter.STARTED_BY_USER);
            assertCondition(value != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}

