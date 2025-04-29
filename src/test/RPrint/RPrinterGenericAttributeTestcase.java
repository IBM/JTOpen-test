///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RPrinterGenericAttributeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RPrint;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Hashtable; import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.RPrinter;
import com.ibm.as400.resource.ResourceEvent;
import com.ibm.as400.resource.ResourceMetaData;

import test.Testcase;
import test.UserTest;



/**
Testcase RPrinterGenericAttributeTestcase.  This tests the following methods
of the RPrinter class, inherited from BufferedResource:

<ul>
<li>cancelAttributeChanges() 
<li>commitAttributeChanges() 
<li>getAttributeMetaData() 
<li>getAttributeMetaData() 
<li>getAttributeUnchangedValue() 
<li>getAttributeValue() 
<li>hasUncommittedAttributeChanges() 
<li>refreshAttributeValues() 
<li>setAttributeValue() 
</ul>
**/
@SuppressWarnings("deprecation")
public class RPrinterGenericAttributeTestcase
extends Testcase {



    // Constants.



    // Private data.
    private String printerName_;



/**
Constructor.
**/
    public RPrinterGenericAttributeTestcase (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys,
                              String misc)
    {
        super (systemObject, "RPrinterGenericAttributeTestcase",
               namesAndVars, runMode, fileOutputStream,
               password);
        pwrSys_ = pwrSys;
        printerName_ = misc;

        if (pwrSys == null)
            throw new IllegalStateException("ERROR: Please specify a power system via -pwrsys.");
        if (misc == null)
            throw new IllegalStateException("ERROR: Please specify a printer via -misc.");
    }



/**
Checks a particular attribute meta data.
**/
    static boolean verifyAttributeMetaData(ResourceMetaData amd, 
                                            Object attributeID, 
                                            Class<?> attributeType,
                                            boolean readOnly, 
                                            int possibleValueCount, 
                                            Object defaultValue, 
                                            boolean valueLimited,
                                            boolean multipleAllowed)
    {
        boolean match = ((amd.areMultipleAllowed() == multipleAllowed)
               && (amd.getDefaultValue() == (defaultValue))
               && (amd.getPossibleValues().length == possibleValueCount)
               && (amd.getPresentation() != null)
               && (amd.getType() == attributeType)
               && (amd.isReadOnly() == readOnly)
               && (amd.isValueLimited() == valueLimited)
               && (amd.toString().equals(attributeID)));
        return match;
    }



/**
Checks a particular attribute meta data.
**/
    static boolean verifyAttributeMetaData(ResourceMetaData[] amd, 
                                            Object attributeID, 
                                            Class<?> attributeType,
                                            boolean readOnly, 
                                            int possibleValueCount, 
                                            Object defaultValue, 
                                            boolean valueLimited,
                                            boolean multipleAllowed)
    {
        int found = -1;
        for (int i = 0; (i < amd.length) && (found < 0); ++i) {
            if (amd[i].getID() == attributeID)
                found = i;
        }

        if (found < 0) {
            System.out.println("Attribute ID " + attributeID + " not found.");
            return false;
        }

        return verifyAttributeMetaData(amd[found], 
                                       attributeID, 
                                       attributeType, 
                                       readOnly, 
                                       possibleValueCount, 
                                       defaultValue, 
                                       valueLimited, 
                                       multipleAllowed);
    }



/**
cancelAttributeChanges() - Should do nothing when there is
no system or user set.
**/
    public void Var001()
    {
        try {
            RPrinter u = new RPrinter();
            u.cancelAttributeChanges();
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
cancelAttributeChanges() - Should do nothing when no change has been made.
**/
    public void Var002()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            u.cancelAttributeChanges();
            String textDescription2 = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            assertCondition(textDescription2.equals(textDescription));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
cancelAttributeChanges() - When 2 changes have been made.
Verify that the changes are canceled.
**/
    public void Var003()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            String changes = (String)u.getAttributeValue(RPrinter.CHANGES_TAKE_EFFECT);
            u.setAttributeValue(RPrinter.TEXT_DESCRIPTION, "This is only a test.");
            u.setAttributeValue(RPrinter.CHANGES_TAKE_EFFECT, RPrinter.OPERATION_NO_FILES_READY);
            u.cancelAttributeChanges();
            String textDescription2 = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            // String changes2 = (String)u.getAttributeValue(RPrinter.CHANGES_TAKE_EFFECT);
            assertCondition((textDescription2.equals(textDescription))
                   && (changes.equals(changes)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
cancelAttributeChanges() - When 2 changes have been made.
Verify that the changes are canceled, even when committed.
**/
    public void Var004()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            String changes = (String)u.getAttributeValue(RPrinter.CHANGES_TAKE_EFFECT);
            u.setAttributeValue(RPrinter.TEXT_DESCRIPTION, "This is still a test.");
            u.setAttributeValue(RPrinter.CHANGES_TAKE_EFFECT, RPrinter.OPERATION_FILE_END);
            u.cancelAttributeChanges();
            u.commitAttributeChanges();
            String textDescription2 = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            String changes2 = (String)u.getAttributeValue(RPrinter.CHANGES_TAKE_EFFECT);
            assertCondition((textDescription2.equals(textDescription))
                   && (changes2.equals(changes)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
cancelAttributeChanges() - Verify that a ResourceEvent is fired.
**/
    public void Var005()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.cancelAttributeChanges();
            assertCondition ((rl.eventCount_ == 1)
                    && (rl.event_.getID() == ResourceEvent.ATTRIBUTE_CHANGES_CANCELED)
                    && (rl.event_.getAttributeID() == null)
                    && (rl.event_.getValue() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
commitAttributeChanges() - Should fail when there is
no system or user set.
**/
    public void Var006()
    {
        try {
            RPrinter u = new RPrinter();
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.Exception");
        }
    }


/**
commitAttributeChanges() - When the connection is bogus.
**/
    public void Var007()
    {
        try {
            AS400 system = new AS400("Toolbox", "is", "cool");
            system.setGuiAvailable(false);
            RPrinter u = new RPrinter(system, "ClifRock");
            u.setAttributeValue(RPrinter.SEPARATOR_DRAWER, RPrinter.SEPARATOR_PAGE_DEVICE);
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
commitAttributeChanges() - When the user does not exist.
**/
    public void Var008()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, "NOTEXIST");
            u.setAttributeValue(RPrinter.CHANGES_TAKE_EFFECT, RPrinter.OPERATION_FILE_END);
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
commitAttributeChanges() - Should do nothing when no change has been made.
**/
    public void Var009()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            u.commitAttributeChanges();
            String textDescription2 = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            assertCondition(textDescription2.equals(textDescription));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
commitAttributeChanges() - Should commit the change when 2 changes have been made.
**/
    public void Var010()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            // String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            //String changes = (String)u.getAttributeValue(RPrinter.CHANGES_TAKE_EFFECT);
            u.setAttributeValue(RPrinter.TEXT_DESCRIPTION, "This is only a test.");
            u.setAttributeValue(RPrinter.CHANGES_TAKE_EFFECT, RPrinter.OPERATION_FILE_END);
            u.commitAttributeChanges();
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            String textDescription2 = (String)u2.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            String changes2 = (String)u2.getAttributeValue(RPrinter.CHANGES_TAKE_EFFECT);
            assertCondition((textDescription2.equals("This is only a test."))
                   && (changes2.equalsIgnoreCase(RPrinter.OPERATION_FILE_END)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
commitAttributeChanges() - Verify that a ResourceEvent is fired.
**/
    public void Var011()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.commitAttributeChanges();
            assertCondition ((rl.eventCount_ == 1)
                    && (rl.event_.getID() == ResourceEvent.ATTRIBUTE_CHANGES_COMMITTED)
                    && (rl.event_.getAttributeID() == null)
                    && (rl.event_.getValue() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeMetaData() with 0 parameters - Verify that the array contains a few
selected attributes.  I did not check everyone...we will do that when verifying
each attribute.
**/
    public void Var012()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            // boolean found = false;
            // I did not hardcode an exact length...otherwise, we
            // have to change this every time we add a property.
            assertCondition((amd.length > 20) 
                   && (verifyAttributeMetaData(amd, RPrinter.FORM_TYPE, String.class, false, 3, null, false, false))
                   && (verifyAttributeMetaData(amd, RPrinter.HELD_STATUS, Boolean.class, true, 0, null, false, false))
                   && (verifyAttributeMetaData(amd, RPrinter.WRITING_STATUS, String.class, true, 4, null, true, false)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeMetaData() with 1 parameter - Pass null.
**/
    public void Var013()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(null);
            failed ("Didn't throw exception"+amd);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getAttributeMetaData() with 1 parameter - Pass an invalid attribute ID.
**/
    public void Var014()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(new Date());
            failed ("Didn't throw exception"+amd);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getAttributeMetaData() with 1 parameter - Pass the first attribute ID.
**/
    public void Var015()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.ADVANCED_FUNCTION_PRINTING);
            assertCondition(verifyAttributeMetaData(amd, RPrinter.ADVANCED_FUNCTION_PRINTING, Boolean.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
getAttributeMetaData() with 1 parameter - Pass a middle attribute ID.
**/
    public void Var016()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.OUTPUT_QUEUE);
            assertCondition(verifyAttributeMetaData(amd, RPrinter.OUTPUT_QUEUE, String.class, false, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
getAttributeMetaData() with 1 parameter - Pass the last attribute ID.
**/
    public void Var017()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData amd = u.getAttributeMetaData(RPrinter.WRITING_STATUS);
            assertCondition(verifyAttributeMetaData(amd, RPrinter.WRITING_STATUS, String.class, true, 4, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeMetaData() with 1 parameter - Try each of them.
**/
    public void Var018()
    {
        try {
            RPrinter u = new RPrinter();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            boolean success = true;
            for(int i = 0; i < amd.length; ++i) {
                boolean thisOne = verifyAttributeMetaData(u.getAttributeMetaData(amd[i].getID()), 
                                                             amd[i].getID(), 
                                                             amd[i].getType(), 
                                                             amd[i].isReadOnly(), 
                                                             amd[i].getPossibleValues().length, 
                                                             amd[i].getDefaultValue(), 
                                                             amd[i].isValueLimited(),
                                                             amd[i].areMultipleAllowed());
                if (!thisOne) {
                    System.out.println("Comparison failed for: " + amd[i] + ".");
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
getAttributeUnchangedValue() - When there is no connection.
**/
    public void Var019()
    {
        try {
            RPrinter u = new RPrinter();
            Object value = u.getAttributeUnchangedValue(RPrinter.USER_NAME);
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
getAttributeUnchangedValue() - Pass null.
**/
    public void Var020()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            Object value = u.getAttributeUnchangedValue(null);
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getAttributeUnchangedValue() - Pass an invalid attribute ID.
**/
    public void Var021()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            Object value = u.getAttributeUnchangedValue(new AS400());
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getAttributeUnchangedValue() - Pass an attribute ID, whose value has not been referenced.
**/
    public void Var022()
    {
        try {
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_FORMS);
            u2.commitAttributeChanges();

            RPrinter u = new RPrinter(pwrSys_, printerName_);
            Object value = u.getAttributeUnchangedValue(RPrinter.FORM_TYPE);
            assertCondition(value.equals(RPrinter.FORM_TYPE_FORMS));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeUnchangedValue() - Pass an attribute ID, whose value has been referenced,
but not changed.
**/
    public void Var023()
    {
        try {
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_ALL);
            u2.commitAttributeChanges();

            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.getAttributeValue(RPrinter.FORM_TYPE);
            Object value = u.getAttributeUnchangedValue(RPrinter.FORM_TYPE);
            assertCondition(value.equals(RPrinter.FORM_TYPE_ALL));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed,
but not committed.
**/
    public void Var024()
    {
        try {
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_STANDARD);
            u2.commitAttributeChanges();

            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_FORMS);
            Object value = u.getAttributeUnchangedValue(RPrinter.FORM_TYPE);
            assertCondition(value.equals(RPrinter.FORM_TYPE_STANDARD));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed,
not committed, but refreshed.
**/
    public void Var025()
    {
        try {
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_STANDARD);
            u2.commitAttributeChanges();

            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_ALL);
            u.refreshAttributeValues();
            Object value = u.getAttributeUnchangedValue(RPrinter.FORM_TYPE);
            assertCondition(value.equals(RPrinter.FORM_TYPE_STANDARD));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed
and committed.
**/
    public void Var026()
    {
        try {
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_ALL);
            u2.commitAttributeChanges();

            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_FORMS);
            u.commitAttributeChanges();
            Object value = u.getAttributeUnchangedValue(RPrinter.FORM_TYPE);
            assertCondition(value.equals(RPrinter.FORM_TYPE_FORMS));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeValue() - When there is no connection.
**/
    public void Var027()
    {
        try {
            RPrinter u = new RPrinter();
            Object value = u.getAttributeValue(RPrinter.FORM_TYPE);
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
getAttributeValue() - When the connection is bogus.
**/
    public void Var028()
    {
        try {
            AS400 system = new AS400("This", "is", "bad");
            system.setGuiAvailable(false);
            RPrinter u = new RPrinter(system, "Friend");
            Object value = u.getAttributeValue(RPrinter.FORM_TYPE);
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            // Browsers don't like even attempting to connect to "This".
            if (isApplet_)
                assertExceptionIsInstanceOf(e, "java.lang.Throwable");
            else
                assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
getAttributeValue() - When the user does not exist.
**/
    public void Var029()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, "NOTEXIST");
            Object value = u.getAttributeValue(RPrinter.FORM_TYPE);
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
getAttributeValue() - Pass null.
**/
    public void Var030()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            Object value = u.getAttributeValue(null);
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getAttributeValue() - Pass an invalid attribute ID.
**/
    public void Var031()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            Object value = u.getAttributeValue("Yo");
            failed ("Didn't throw exception"+value);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getAttributeValue() - Pass an attribute ID, whose value has not been referenced.
**/
    public void Var032()
    {
        try {
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_STANDARD);
            u2.commitAttributeChanges();

            RPrinter u = new RPrinter(pwrSys_, printerName_);
            Object value = u.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition(value.equals(RPrinter.FORM_TYPE_STANDARD));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeValue() - Pass an attribute ID, whose value has been referenced,
but not changed.
**/
    public void Var033()
    {
        try {
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_ALL);
            u2.commitAttributeChanges();

            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.getAttributeValue(RPrinter.FORM_TYPE);
            Object value = u.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition(value.equals(RPrinter.FORM_TYPE_ALL));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeValue() - Pass an attribute ID, whose value has been changed,
but not committed.
**/
    public void Var034()
    {
        try {
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_FORMS);
            u2.commitAttributeChanges();

            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_STANDARD);
            Object value = u.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition(value.equals(RPrinter.FORM_TYPE_STANDARD));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeValue() - Pass an attribute ID, whose value has been changed,
not committed, but refreshed.
**/
    public void Var035()
    {
        try {
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_ALL);
            u2.commitAttributeChanges();

            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_FORMS);
            u.refreshAttributeValues();
            Object value = u.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition(value.equals(RPrinter.FORM_TYPE_FORMS));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeValue() - Pass an attribute ID, whose value has been changed
and committed.
**/
    public void Var036()
    {
        try {
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_ALL);
            u2.commitAttributeChanges();

            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_FORMS);
            u.commitAttributeChanges();
            Object value = u.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition(value.equals(RPrinter.FORM_TYPE_FORMS));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeValue() - Pass every attribute ID.
**/
    public void Var037()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            ResourceMetaData[] amd = u.getAttributeMetaData();
            boolean success = true;
            for(int i = 0; i < amd.length; ++i) {
                // System.out.println("Getting attribute " + amd[i] + ".");
                Object value = u.getAttributeValue(amd[i].getID());
                Class<?> valueClass = value.getClass();
                Class<?> type = amd[i].getType();

                // Validate the type.
                if (amd[i].areMultipleAllowed()) {
                    if (!valueClass.isArray()) {
                        System.out.println("Error getting attribute " + amd[i] + ".");
                        System.out.println("Type array mismatch: " + valueClass + " is not an array, "
                                           + "but multiple values are allowed.");
                        success = false;
                    }
                    else {
                        Class<?> componentType = valueClass.getComponentType();
                        if (!componentType.equals(type)) {
                            System.out.println("Error getting attribute " + amd[i] + ".");
                            System.out.println("Type mismatch: " + componentType + " != " + type + ".");
                            success = false;
                        }
                    }
                }
                else if (!valueClass.equals(type)) {
                    System.out.println("Error getting attribute " + amd[i] + ".");
                    System.out.println("Type mismatch: " + valueClass + " != " + type + ".");
                    success = false;
                }

                // Validate the value.
                if (amd[i].isValueLimited()) {
                    Object[] possibleValues = amd[i].getPossibleValues();
                    boolean found = false;
                    if (amd[i].areMultipleAllowed()) {
                        Object[] asArray = (Object[])value;
                        for (int k = 0; k < asArray.length; ++k) {
                            for(int j = 0; (j < possibleValues.length) && (found == false); ++j)
                            if (possibleValues[j].equals(asArray[k]))
                                found = true;                           

                            if (! found) {
                                System.out.println("Error getting attribute " + amd[i] + ".");
                                System.out.println("Value: " + asArray[k] + " is not a valid possible value.");
                                success = false;
                            }
                        }
                    }
                    else {
                        for(int j = 0; (j < possibleValues.length) && (found == false); ++j) {
                            if (value instanceof byte[]) {
                                byte[] valueAsBytes = (byte[])value;
                                byte[] pvAsBytes = (byte[])possibleValues[j];
                                if (valueAsBytes.length == pvAsBytes.length) {
                                    boolean compare = true;
                                    for(int k = 0; k < valueAsBytes.length; ++k) {
                                        if(valueAsBytes[k] != pvAsBytes[k])
                                            compare = false;
                                    }
                                    if (compare)
                                        found = true;
                                }
                            }
                            else if (possibleValues[j].equals(value))
                                found = true;
                        }

                        if (! found) {
                            System.out.println("Error getting attribute " + amd[i] + ".");
                            System.out.println("Value: " + value + " is not a valid possible value.");
                            success = false;
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
hasUncommittedAttributeChanges() - When there is no connection.
**/
    public void Var038()
    {
        try {
            RPrinter u = new RPrinter();
            boolean pending = u.hasUncommittedAttributeChanges(RPrinter.FORM_TYPE);
            assertCondition(pending == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass null.
**/
    public void Var039()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            boolean pending = u.hasUncommittedAttributeChanges(null);
            failed ("Didn't throw exception"+pending);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass an invalid attribute ID.
**/
    public void Var040()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            boolean pending = u.hasUncommittedAttributeChanges("Go Toolbox");
            failed ("Didn't throw exception"+pending);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has not been referenced.
**/
    public void Var041()
    {
        try {
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_STANDARD);
            u2.commitAttributeChanges();

            RPrinter u = new RPrinter(pwrSys_, printerName_);
            boolean pending = u.hasUncommittedAttributeChanges(RPrinter.FORM_TYPE);
            assertCondition(pending == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has been referenced,
but not changed.
**/
    public void Var042()
    {
        try {
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_ALL);
            u2.commitAttributeChanges();

            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.getAttributeValue(RPrinter.FORM_TYPE);
            boolean pending = u.hasUncommittedAttributeChanges(RPrinter.FORM_TYPE);
            assertCondition(pending == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has been changed,
but not committed.
**/
    public void Var043()
    {
        try {
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_FORMS);
            u2.commitAttributeChanges();

            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_ALL);
            boolean pending = u.hasUncommittedAttributeChanges(RPrinter.FORM_TYPE);
            assertCondition(pending == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has been changed,
not committed, but refreshed.
**/
    public void Var044()
    {
        try {
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_STANDARD);
            u2.commitAttributeChanges();

            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_FORMS);
            u.refreshAttributeValues();
            boolean pending = u.hasUncommittedAttributeChanges(RPrinter.FORM_TYPE);
            assertCondition(pending == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has been changed
and committed.
**/
    public void Var045()
    {
        try {
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_STANDARD);
            u2.commitAttributeChanges();

            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_ALL);
            u.commitAttributeChanges();
            boolean pending = u.hasUncommittedAttributeChanges(RPrinter.FORM_TYPE);
            assertCondition(pending == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
refreshAttributeValues() - When no system or user set.
**/
    public void Var046()
    {
        try {
            RPrinter u = new RPrinter();
            u.refreshAttributeValues();
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
refreshAttributeValues() - When no change has been made.
**/
    public void Var047()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            u.refreshAttributeValues();
            String textDescription2 = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            assertCondition(textDescription2.equals(textDescription));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
refreshAttributeValues() - When 2 changes have been made for this
object, but not committed.  Verify that the changes are not cancelled.
**/
    public void Var048()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            String changes = (String)u.getAttributeValue(RPrinter.CHANGES_TAKE_EFFECT);
            u.setAttributeValue(RPrinter.TEXT_DESCRIPTION, "This is only another test.");
            u.setAttributeValue(RPrinter.CHANGES_TAKE_EFFECT, RPrinter.OPERATION_FILE_END);
            u.refreshAttributeValues();
            String textDescription2 = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            String changes2 = (String)u.getAttributeValue(RPrinter.CHANGES_TAKE_EFFECT);
            String textDescription2a = (String)u.getAttributeUnchangedValue(RPrinter.TEXT_DESCRIPTION);
            String changes2a = (String)u.getAttributeUnchangedValue(RPrinter.CHANGES_TAKE_EFFECT);
            assertCondition((textDescription2.equals("This is only another test."))
                   && (changes2.equals(RPrinter.OPERATION_FILE_END))
                   && (textDescription2a.equals(textDescription))
                   && (changes2a.equals(changes)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
refreshAttributeValues() - When 2 changes have been made for another
RPrinter object.  Verify that the changes are reflected.
**/
    public void Var049()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            // String textDescription = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            // String formType = (String)u.getAttributeValue(RPrinter.FORM_TYPE);

            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            u2.setAttributeValue(RPrinter.TEXT_DESCRIPTION, "This is another in a long series of tests.");
            u2.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_ALL);
            u2.commitAttributeChanges();

            u.refreshAttributeValues();
            String textDescription2 = (String)u.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            String formType2 = (String)u.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition((textDescription2.equals("This is another in a long series of tests."))
                   && (formType2.equalsIgnoreCase(RPrinter.FORM_TYPE_ALL)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
refreshAttributeValues() - Verify that a ResourceEvent is fired.
**/
    public void Var050()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.refreshAttributeValues();
            assertCondition ((rl.eventCount_ == 1)
                    && (rl.event_.getID() == ResourceEvent.ATTRIBUTE_VALUES_REFRESHED)
                    && (rl.event_.getAttributeID() == null)
                    && (rl.event_.getValue() == null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setAttributeValue() - When there is no connection.
**/
    public void Var051()
    {
        try {
            RPrinter u = new RPrinter();
            u.setAttributeValue(RPrinter.CHANGES_TAKE_EFFECT, RPrinter.OPERATION_FILE_END);
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setAttributeValue() - Pass null for the attribute ID.
**/
    public void Var052()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(null, new Integer(2));
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setAttributeValue() - Pass null for the value.
**/
    public void Var053()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.CHANGES_TAKE_EFFECT, null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setAttributeValue() - Pass an invalid attribute ID.
**/
    public void Var054()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(u, new Integer(3));
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setAttributeValue() - Set a read only attribute.
**/
    public void Var055()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.USER_NAME, "egledm");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
setAttributeValue() - Pass a value which is the wrong type.
**/
    public void Var056()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.CHANGES_TAKE_EFFECT, new Integer(4));
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setAttributeValue() - Pass a value which is not one of the possible values.
**/
    public void Var057()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.CHANGES_TAKE_EFFECT, "Bogus");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
setAttributeValue() - Pass an valid attribute ID, commit and verify.
**/
    public void Var058()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_FORMS);
            u.commitAttributeChanges();
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            Object value = u2.getAttributeValue(RPrinter.FORM_TYPE);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_STANDARD);
            u.commitAttributeChanges();
            u2.refreshAttributeValues();
            Object value2 = u2.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition((value.equals(RPrinter.FORM_TYPE_FORMS))
                   && (value2.equals(RPrinter.FORM_TYPE_STANDARD)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setAttributeValue() - Pass an valid attribute ID, set twice.  Commit and verify
that the second takes effect.
**/
    public void Var059()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_FORMS);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_STANDARD);
            u.commitAttributeChanges();
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            Object value = u2.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition(value.equals(RPrinter.FORM_TYPE_STANDARD));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setAttributeValue() - Pass an valid attribute ID, set and committed twice.  Verify
that the second takes effect.
**/
    public void Var060()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_STANDARD);
            u.commitAttributeChanges();
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_FORMS);
            u.commitAttributeChanges();
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            Object value = u2.getAttributeValue(RPrinter.FORM_TYPE);
            assertCondition(value.equals(RPrinter.FORM_TYPE_FORMS));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setAttributeValue() - Set 2 attributes the are set using the same CL
command.  Commit and verify that the both take effect.
**/
    public void Var061()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_STANDARD);
            u.setAttributeValue(RPrinter.NUMBER_OF_SEPARATORS, new Integer(3));
            u.commitAttributeChanges();
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            Object value1 = u2.getAttributeValue(RPrinter.FORM_TYPE);
            Object value2 = u2.getAttributeValue(RPrinter.NUMBER_OF_SEPARATORS);
            assertCondition((value1.equals(RPrinter.FORM_TYPE_STANDARD))
                   && (value2.equals(new Integer(3))));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
setAttributeValue() - Set 2 attributes the are set using different CL
commands.  Commit and verify that the both take effect.
**/
    public void Var062()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_STANDARD);
            u.setAttributeValue(RPrinter.TEXT_DESCRIPTION, "DEPTB");
            u.commitAttributeChanges();
            RPrinter u2 = new RPrinter(pwrSys_, printerName_);
            Object value1 = u2.getAttributeValue(RPrinter.FORM_TYPE);
            Object value2 = u2.getAttributeValue(RPrinter.TEXT_DESCRIPTION);
            assertCondition((value1.equals(RPrinter.FORM_TYPE_STANDARD))
                   && (value2.equals("DEPTB")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
setAttributeValue() - Verify that a ResourceEvent is fired.
**/
    public void Var063()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.setAttributeValue(RPrinter.FORM_TYPE, RPrinter.FORM_TYPE_FORMS);
            assertCondition ((rl.eventCount_ == 1)
                    && (rl.event_.getID() == ResourceEvent.ATTRIBUTE_VALUE_CHANGED)
                    && (rl.event_.getAttributeID() == RPrinter.FORM_TYPE)
                    && (rl.event_.getValue() == RPrinter.FORM_TYPE_FORMS));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setAttributeValue() - Pass every attribute ID.
**/
    public void Var064()
    {
        try {
            RPrinter u = new RPrinter(pwrSys_, printerName_);
            ResourceMetaData[] amd = u.getAttributeMetaData();
            boolean success = true;
            for(int i = 0; i < amd.length; ++i) {
                if (! amd[i].isReadOnly()) {
                    //System.out.println("Setting attribute " + amd[i] + ".");
                    Object originalValue = u.getAttributeValue(amd[i].getID());

                    // First, just try setting the value to what it is already equal.
                    u.setAttributeValue(amd[i].getID(), originalValue);
                    u.commitAttributeChanges();

                    u.setAttributeValue(amd[i].getID(), originalValue);
                }
            }

            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




