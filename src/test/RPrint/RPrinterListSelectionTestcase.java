///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RPrinterListSelectionTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RPrint;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.ResourceMetaData;

import test.Testcase;

import com.ibm.as400.resource.RPrinter;
import com.ibm.as400.resource.RPrinterList;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;



/**
Testcase RPrinterListSelectionTestcase.  This tests the following methods
of the RPrinterList class, some inherited from BufferedResourceList:

<ul>
<li>getSelectionMetaData() 
<li>getSelectionMetaData() 
<li>getSelectionValue() 
<li>setSelectionValue() 
</ul>
**/
public class RPrinterListSelectionTestcase
extends Testcase {



    // Constants.



    // Private data.
    private String printerName_;
    private String outputQueueName_;



/**
Constructor.
**/
    public RPrinterListSelectionTestcase (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys,
                              String misc)
    {
        super (systemObject, "RPrinterListSelectionTestcase",
               namesAndVars, runMode, fileOutputStream,
               password);
        
        pwrSys_ = pwrSys;
        printerName_ = misc;
        outputQueueName_ = "/QSYS.LIB/QUSRSYS.LIB/" + printerName_ + ".OUTQ";

        if (pwrSys == null)
            throw new IllegalStateException("ERROR: Please specify a power system via -pwrsys.");
        if (misc == null)
            throw new IllegalStateException("ERROR: Please specify a printer via -misc.");

    }



/**
Checks a particular selection meta data.
**/
    static boolean verifySelectionMetaData(ResourceMetaData smd, 
                                            Object attributeID, 
                                            Class attributeType,
                                            boolean readOnly, 
                                            int possibleValueCount, 
                                            Object defaultValue, 
                                            boolean valueLimited,
                                            boolean multipleAllowed)
    {
        return((smd.areMultipleAllowed() == multipleAllowed)
               && (smd.getDefaultValue() == (defaultValue))
               && (smd.getPossibleValues().length == possibleValueCount)
               && (smd.getPresentation() != null)
               && (smd.getType() == attributeType)
               && (smd.isReadOnly() == readOnly)
               && (smd.isValueLimited() == valueLimited)
               && (smd.toString().equals(attributeID)));
    }



/**
Checks a particular selection meta data.
**/
    static boolean verifySelectionMetaData(ResourceMetaData[] smd, 
                                            Object attributeID, 
                                            Class attributeType,
                                            boolean readOnly, 
                                            int possibleValueCount, 
                                            Object defaultValue, 
                                            boolean valueLimited,
                                            boolean multipleAllowed)
    {
        int found = -1;
        for (int i = 0; (i < smd.length) && (found < 0); ++i) {
            if (smd[i].getID() == attributeID)
                found = i;
        }

        if (found < 0) {
            System.out.println("Attribute ID " + attributeID + " not found.");
            return false;
        }

        return verifySelectionMetaData(smd[found], 
                                       attributeID, 
                                       attributeType, 
                                       readOnly, 
                                       possibleValueCount, 
                                       defaultValue, 
                                       valueLimited, 
                                       multipleAllowed);
    }




/**
getSelectionMetaData() with 0 parameters - Verify that the array contains all
selections.
**/
    public void Var001()
    {
        try {
            RPrinterList u = new RPrinterList();
            ResourceMetaData[] smd = u.getSelectionMetaData();
            assertCondition((smd.length == 2) 
                   && (verifySelectionMetaData(smd, RPrinterList.PRINTER_NAMES, String.class, false, 0, null, false, true))
                   && (verifySelectionMetaData(smd, RPrinterList.OUTPUT_QUEUES, String.class, false, 0, null, false, true)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSelectionMetaData() with 1 parameter - Pass null.
**/
    public void Var002()
    {
        try {
            RPrinterList u = new RPrinterList();
            ResourceMetaData smd = u.getSelectionMetaData(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getSelectionMetaData() with 1 parameter - Pass an invalid attribute ID.
**/
    public void Var003()
    {
        try {
            RPrinterList u = new RPrinterList();
            ResourceMetaData smd = u.getSelectionMetaData(new Date());
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getSelectionMetaData() with 1 parameter - Pass PRINTER_NAMES.
**/
    public void Var004()
    {
        try {
            RPrinterList u = new RPrinterList();
            ResourceMetaData smd = u.getSelectionMetaData(RPrinterList.PRINTER_NAMES);
            assertCondition(verifySelectionMetaData(smd, RPrinterList.PRINTER_NAMES, String.class, false, 0, null, false, true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
getSelectionMetaData() with 1 parameter - Pass OUTPUT_QUEUES.
**/
    public void Var005()
    {
        try {
            RPrinterList u = new RPrinterList();
            ResourceMetaData smd = u.getSelectionMetaData(RPrinterList.OUTPUT_QUEUES);
            assertCondition(verifySelectionMetaData(smd, RPrinterList.OUTPUT_QUEUES, String.class, false, 0, null, false, true));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
getSelectionMetaData() with 1 parameter - Try each of them.
**/
    public void Var006()
    {
        try {
            RPrinterList u = new RPrinterList();
            ResourceMetaData[] smd = u.getSelectionMetaData();
            boolean success = true;
            for(int i = 0; i < smd.length; ++i) {
                boolean thisOne = verifySelectionMetaData(u.getSelectionMetaData(smd[i].getID()), 
                                                             smd[i].getID(), 
                                                             smd[i].getType(), 
                                                             smd[i].isReadOnly(), 
                                                             smd[i].getPossibleValues().length, 
                                                             smd[i].getDefaultValue(), 
                                                             smd[i].isValueLimited(),
                                                             smd[i].areMultipleAllowed());
                if (!thisOne) {
                    System.out.println("Comparison failed for: " + smd[i] + ".");
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
getSelectionValue() - When there is no connection.
**/
    public void Var007()
    {
        try {
            RPrinterList u = new RPrinterList();
            String[] printerNames = (String[])u.getSelectionValue(RPrinterList.PRINTER_NAMES);
            String[] outputQueues = (String[])u.getSelectionValue(RPrinterList.OUTPUT_QUEUES);
            assertCondition((printerNames == null) && (outputQueues == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSelectionValue() - When the connection is bogus.
**/
    public void Var008()
    {
        try {
            AS400 system = new AS400("This", "is", "bad");
            system.setGuiAvailable(false);
            RPrinterList u = new RPrinterList(system);
            String[] printerNames = (String[])u.getSelectionValue(RPrinterList.PRINTER_NAMES);
            String[] outputQueues = (String[])u.getSelectionValue(RPrinterList.OUTPUT_QUEUES);
            assertCondition((printerNames == null) && (outputQueues == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSelectionValue() - Pass null.
**/
    public void Var009()
    {
        try {
            RPrinterList u = new RPrinterList(pwrSys_);
            Object value = u.getSelectionValue(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getSelectionValue() - Pass an invalid selection ID.
**/
    public void Var010()
    {
        try {
            RPrinterList u = new RPrinterList(pwrSys_);
            Object value = u.getSelectionValue("Yo");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getSelectionValue() - Pass an selection ID, whose value has not been referenced.
**/
    public void Var011()
    {
        try {
            RPrinterList u = new RPrinterList(pwrSys_);
            String[] printerNames = (String[])u.getSelectionValue(RPrinterList.PRINTER_NAMES);
            String[] outputQueues = (String[])u.getSelectionValue(RPrinterList.OUTPUT_QUEUES);
            assertCondition((printerNames == null) && (outputQueues == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSelectionValue() - Pass an selection ID, whose value has been changed.
**/
    public void Var012()
    {
        try {
            RPrinterList u = new RPrinterList(pwrSys_);
            u.setSelectionValue(RPrinterList.PRINTER_NAMES, new String[] { printerName_ });
            u.setSelectionValue(RPrinterList.OUTPUT_QUEUES, new String[] { } );

            String[] printerNames = (String[])u.getSelectionValue(RPrinterList.PRINTER_NAMES);
            String[] outputQueues = (String[])u.getSelectionValue(RPrinterList.OUTPUT_QUEUES);
            assertCondition((printerNames.length == 1) && (printerNames[0].equals(printerName_)) && (outputQueues.length == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSelectionValue() - Pass every selection ID.
**/
    public void Var013()
    {
        try {
            RPrinterList u = new RPrinterList(pwrSys_);
            ResourceMetaData[] smd = u.getSelectionMetaData();
            boolean success = true;
            for(int i = 0; i < smd.length; ++i) {
                // System.out.println("Getting selection " + smd[i] + ".");
                Object value = u.getSelectionValue(smd[i].getID());
                if (value != null) {
                    Class valueClass = value.getClass();
                    Class type = smd[i].getType();
    
                    // Validate the type.
                    if (smd[i].areMultipleAllowed()) {
                        if (!valueClass.isArray()) {
                            System.out.println("Error getting selection " + smd[i] + ".");
                            System.out.println("Type array mismatch: " + valueClass + " is not an array, "
                                               + "but multiple values are allowed.");
                            success = false;
                        }
                        else {
                            Class componentType = valueClass.getComponentType();
                            if (!componentType.equals(type)) {
                                System.out.println("Error getting selection " + smd[i] + ".");
                                System.out.println("Type mismatch: " + componentType + " != " + type + ".");
                                success = false;
                            }
                        }
                    }
                    else if (!valueClass.equals(type)) {
                        System.out.println("Error getting selection " + smd[i] + ".");
                        System.out.println("Type mismatch: " + valueClass + " != " + type + ".");
                        success = false;
                    }

                    // Validate the value.
                    if (smd[i].isValueLimited()) {
                        Object[] possibleValues = smd[i].getPossibleValues();
                        boolean found = false;
                        if (smd[i].areMultipleAllowed()) {
                            Object[] asArray = (Object[])value;
                            for (int k = 0; k < asArray.length; ++k) {
                                for(int j = 0; (j < possibleValues.length) && (found == false); ++j)
                                if (possibleValues[j].equals(asArray[k]))
                                    found = true;                           
    
                                if (! found) {
                                    System.out.println("Error getting selection " + smd[i] + ".");
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
                                System.out.println("Error getting selection " + smd[i] + ".");
                                System.out.println("Value: " + value + " is not a valid possible value.");
                                success = false;
                            }
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
setSelectionValue() - When there is no connection.
**/
    public void Var014()
    {
        try {
            RPrinterList u = new RPrinterList();
            u.setSelectionValue(RPrinterList.PRINTER_NAMES, new String[] { });
            u.setSelectionValue(RPrinterList.OUTPUT_QUEUES, new String[] { outputQueueName_ });
            String[] printerNames = (String[])u.getSelectionValue(RPrinterList.PRINTER_NAMES);
            String[] outputQueues = (String[])u.getSelectionValue(RPrinterList.OUTPUT_QUEUES);
            assertCondition((printerNames.length == 0) && (outputQueues.length == 1) && (outputQueues[0].equals(outputQueueName_)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Pass null for the selection ID.
**/
    public void Var015()
    {
        try {
            RPrinterList u = new RPrinterList(pwrSys_);
            u.setSelectionValue(null, new Integer(2));
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setSelectionValue() - Pass null for the value.
**/
    public void Var016()
    {
        try {
            RPrinterList u = new RPrinterList(pwrSys_);
            // Set to something else first.
            u.setSelectionValue(RPrinterList.PRINTER_NAMES, new String[] { printerName_ });
            u.setSelectionValue(RPrinterList.OUTPUT_QUEUES, new String[] { outputQueueName_ });

            u.setSelectionValue(RPrinterList.PRINTER_NAMES, null);
            u.setSelectionValue(RPrinterList.OUTPUT_QUEUES, null);
            assertCondition((u.getSelectionValue(RPrinterList.PRINTER_NAMES) == null) 
                   && (u.getSelectionValue(RPrinterList.OUTPUT_QUEUES) == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Pass an invalid selection ID.
**/
    public void Var017()
    {
        try {
            RPrinterList u = new RPrinterList(pwrSys_);
            u.setSelectionValue(u, "This is a test");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setSelectionValue() - Pass a value which is the wrong type.
**/
    public void Var018()
    {
        try {
            RPrinterList u = new RPrinterList(pwrSys_);
            u.setSelectionValue(RPrinterList.PRINTER_NAMES, new Integer(4));
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setSelectionValue() - Set the printer names selection to null.
**/
    public void Var019()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.PRINTER_NAMES, null);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean found = false;
            for(long i = 0; i < length; ++i) {
                RPrinter p = (RPrinter)ul.resourceAt(i);
                if (p.getName().equalsIgnoreCase(printerName_))
                    found = true;
            }
            ul.close();
            String[] sv = (String[])ul.getSelectionValue(RPrinterList.PRINTER_NAMES);
            assertCondition((length >= 1) && (found == true) && (sv == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Set the printer names selection to an empty array.
**/
    public void Var020()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.PRINTER_NAMES, new String[0]);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            ul.close();
            String[] sv = (String[])ul.getSelectionValue(RPrinterList.PRINTER_NAMES);
            assertCondition((length > 0) && (sv.length == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Set the printer names selection to a single string which identifies
an existing printer.
**/
    public void Var021()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.PRINTER_NAMES, printerName_);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean found = false;
            for(long i = 0; i < length; ++i) {
                RPrinter p = (RPrinter)ul.resourceAt(i);
                if (p.getName().equalsIgnoreCase(printerName_))
                    found = true;
            }
            ul.close();
            String[] sv = (String[])ul.getSelectionValue(RPrinterList.PRINTER_NAMES);
            assertCondition((length == 1) && (found == true) && (sv[0].equals(printerName_)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Set the printer names selection to a single string which identifies
an existing printer, but using a lowercase string.
**/
    public void Var022()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.PRINTER_NAMES, printerName_.toLowerCase());
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean found = false;
            for(long i = 0; i < length; ++i) {
                RPrinter p = (RPrinter)ul.resourceAt(i);
                if (p.getName().equalsIgnoreCase(printerName_))
                    found = true;
            }
            ul.close();
            String[] sv = (String[])ul.getSelectionValue(RPrinterList.PRINTER_NAMES);
            assertCondition((length == 1) && (found == true) && (sv[0].equals(printerName_.toLowerCase())));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Set the printer names selection to a single string which identifies
a non-existing printer.
**/
    public void Var023()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.PRINTER_NAMES, "NOTEXIST");
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            ul.close();
            String[] sv = (String[])ul.getSelectionValue(RPrinterList.PRINTER_NAMES);
            assertCondition((length == 0) && (sv[0].equals("NOTEXIST")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Set the printer names selection to a single element array which identifies
an existing printer.
**/
    public void Var024()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.PRINTER_NAMES, new String[] {printerName_});
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean found = false;
            for(long i = 0; i < length; ++i) {
                RPrinter p = (RPrinter)ul.resourceAt(i);
                if (p.getName().equalsIgnoreCase(printerName_))
                    found = true;
            }
            ul.close();
            String[] sv = (String[])ul.getSelectionValue(RPrinterList.PRINTER_NAMES);
            assertCondition((length == 1) && (found == true) && (sv.length == 1) && (sv[0].equals(printerName_)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Set the printer names selection to a single string which identifies
a non-existing printer.
**/
    public void Var025()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.PRINTER_NAMES, new String[] {"NOTEXIST"});
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            ul.close();
            String[] sv = (String[])ul.getSelectionValue(RPrinterList.PRINTER_NAMES);
            assertCondition((length == 0) && (sv.length == 1) && (sv[0].equals("NOTEXIST")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Set the printer names selection to a multiple element array which identifies
at least one existing printer.
**/
    public void Var026()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.PRINTER_NAMES, new String[] {printerName_, "JAVAPRT"});
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean found = false;
            for(long i = 0; i < length; ++i) {
                RPrinter p = (RPrinter)ul.resourceAt(i);
                if (p.getName().equalsIgnoreCase(printerName_))
                    found = true;
            }
            ul.close();
            String[] sv = (String[])ul.getSelectionValue(RPrinterList.PRINTER_NAMES);
            assertCondition((length >= 1) && (found == true) 
                   && (sv.length == 2) && (sv[0].equals(printerName_)) && (sv[1].equals("JAVAPRT")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Set the printer names selection to a single string which identifies
only non-existing printers.
**/
    public void Var027()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.PRINTER_NAMES, new String[] {"NOTEXIST", "BADPRT"});
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            ul.close();
            String[] sv = (String[])ul.getSelectionValue(RPrinterList.PRINTER_NAMES);
            assertCondition((length == 0) && (sv.length == 2) && (sv[0].equals("NOTEXIST")) && (sv[1].equals("BADPRT")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Set the output queues selection to null.
**/
    public void Var028()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.OUTPUT_QUEUES, null);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean found = false;
            for(long i = 0; i < length; ++i) {
                RPrinter p = (RPrinter)ul.resourceAt(i);
                if (p.getName().equalsIgnoreCase(printerName_))
                    found = true;
            }
            ul.close();
            String[] sv = (String[])ul.getSelectionValue(RPrinterList.OUTPUT_QUEUES);
            assertCondition((length >= 1) && (found == true) && (sv == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Set the output queues selection to an empty array.
**/
    public void Var029()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.OUTPUT_QUEUES, new String[0]);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            ul.close();
            String[] sv = (String[])ul.getSelectionValue(RPrinterList.OUTPUT_QUEUES);
            assertCondition((length > 0) && (sv.length == 0));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Set the output queues selection to a single string which identifies
an existing printer.
**/
    public void Var030()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.OUTPUT_QUEUES, outputQueueName_);
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean found = false;
            for(long i = 0; i < length; ++i) {
                RPrinter p = (RPrinter)ul.resourceAt(i);
                if (p.getName().equalsIgnoreCase(printerName_))
                    found = true;
            }
            ul.close();
            String[] sv = (String[])ul.getSelectionValue(RPrinterList.OUTPUT_QUEUES);
            assertCondition((length == 1) && (found == true) && (sv[0].equals(outputQueueName_)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Set the output queues selection to a single string which identifies
an existing printer, but using a lowercase string.
**/
    public void Var031()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.OUTPUT_QUEUES, outputQueueName_.toLowerCase());
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean found = false;
            for(long i = 0; i < length; ++i) {
                RPrinter p = (RPrinter)ul.resourceAt(i);
                if (p.getName().equalsIgnoreCase(printerName_))
                    found = true;
            }
            ul.close();
            String[] sv = (String[])ul.getSelectionValue(RPrinterList.OUTPUT_QUEUES);
            assertCondition((length == 1) && (found == true) && (sv[0].equals(outputQueueName_.toLowerCase())));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Set the output queues selection to a single string which identifies
a non-existing printer.
**/
    public void Var032()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.OUTPUT_QUEUES, "NOTEXIST");
            ul.open();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
setSelectionValue() - Set the output queues selection to a single element array which identifies
an existing printer.
**/
    public void Var033()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.OUTPUT_QUEUES, new String[] {outputQueueName_});
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean found = false;
            for(long i = 0; i < length; ++i) {
                RPrinter p = (RPrinter)ul.resourceAt(i);
                if (p.getName().equalsIgnoreCase(printerName_))
                    found = true;
            }
            ul.close();
            String[] sv = (String[])ul.getSelectionValue(RPrinterList.OUTPUT_QUEUES);
            assertCondition((length == 1) && (found == true) && (sv.length == 1) && (sv[0].equals(outputQueueName_)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Set the output queues selection to a single string which identifies
a non-existing printer.
**/
    public void Var034()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.OUTPUT_QUEUES, new String[] {"NOTEXIST"});
            ul.open();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
setSelectionValue() - Set the output queues selection to a multiple element array which identifies
at least one existing printer.
**/
    public void Var035()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.OUTPUT_QUEUES, new String[] {outputQueueName_, "/QSYS.LIB/QUSRSYS.LIB/JAVAPRT.OUTQ"});
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean found = false;
            for(long i = 0; i < length; ++i) {
                RPrinter p = (RPrinter)ul.resourceAt(i);
                if (p.getName().equalsIgnoreCase(printerName_))
                    found = true;
            }
            ul.close();
            String[] sv = (String[])ul.getSelectionValue(RPrinterList.OUTPUT_QUEUES);
            assertCondition((length >= 1) && (found == true) 
                   && (sv.length == 2) && (sv[0].equals(outputQueueName_)) && (sv[1].equals("/QSYS.LIB/QUSRSYS.LIB/JAVAPRT.OUTQ")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSelectionValue() - Set the output queues selection to a single string which identifies
only non-existing printers.
**/
    public void Var036()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.OUTPUT_QUEUES, new String[] {"NOTEXIST", "BADPRT"});
            ul.open();
            failed ("Didn't throw exception");
       }
       catch(Exception e) {
           assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
       }
    }


/**
setSelectionValue() - Set both selections.
**/
    public void Var037()
    {
        try {
            RPrinterList ul = new RPrinterList(pwrSys_);
            ul.setSelectionValue(RPrinterList.PRINTER_NAMES, printerName_);
            ul.setSelectionValue(RPrinterList.OUTPUT_QUEUES, new String[] {outputQueueName_ });
            ul.open();
            ul.waitForComplete();
            long length = ul.getListLength();
            boolean found = false;
            for(long i = 0; i < length; ++i) {
                RPrinter p = (RPrinter)ul.resourceAt(i);
                if (p.getName().equalsIgnoreCase(printerName_))
                    found = true;
            }
            ul.close();
            String[] sv1 = (String[])ul.getSelectionValue(RPrinterList.PRINTER_NAMES);
            String[] sv2 = (String[])ul.getSelectionValue(RPrinterList.OUTPUT_QUEUES);
            assertCondition((length == 1) && (found == true) && (sv1.length == 1)
                   && (sv1[0].equals(printerName_)) && (sv2.length == 1) && (sv2[0].equals(outputQueueName_)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




}




