///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RSoftwareResourceGenericAttributeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.RSoftware;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.ResourceException;
import com.ibm.as400.resource.ResourceEvent;
import com.ibm.as400.resource.ResourceMetaData;

import test.RSoftwareTest;
import test.Testcase;
import test.UserTest;
import test.UserTest.ResourceListener_;

import com.ibm.as400.resource.RSoftwareResource;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase RSoftwareResourceGenericAttributeTestcase.  This tests the following methods
of the RSoftwareResource class, inherited from BufferedResource:

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
public class RSoftwareResourceGenericAttributeTestcase
extends Testcase {



    // Constants.



    // Private data.
    private String          JC1ProductID_ = RSoftwareTest.JC1ProductID_;
    private AS400           pwrSys_;



/**
Constructor.
**/
    public RSoftwareResourceGenericAttributeTestcase (AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RSoftwareResourceGenericAttributeTestcase",
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
Checks a particular attribute meta data.
**/
    static boolean verifyAttributeMetaData(ResourceMetaData amd, 
                                            Object attributeID, 
                                            Class attributeType,
                                            boolean readOnly, 
                                            int possibleValueCount, 
                                            Object defaultValue, 
                                            boolean valueLimited,
                                            boolean multipleAllowed)
    {
        return((amd.areMultipleAllowed() == multipleAllowed)
               && (amd.getDefaultValue() == (defaultValue))
               && (amd.getPossibleValues().length == possibleValueCount)
               && (amd.getPresentation() != null)
               && (amd.getType() == attributeType)
               && (amd.isReadOnly() == readOnly)
               && (amd.isValueLimited() == valueLimited)
               && (amd.toString().equals(attributeID)));
    }



/**
Checks a particular attribute meta data.
**/
    static boolean verifyAttributeMetaData(ResourceMetaData[] amd, 
                                            Object attributeID, 
                                            Class attributeType,
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
no system or path set.
**/
    public void Var001()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
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
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            u.cancelAttributeChanges();
            String loadType2 = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition(loadType2.equals(loadType));
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
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, RJavaTest.jarFilePath_);
            Integer optimization = (Integer)u.getAttributeValue(RSoftwareResource.OPTIMIZATION);
            String epc = (String)u.getAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            u.setAttributeValue(RSoftwareResource.OPTIMIZATION, RSoftwareResource.OPTIMIZATION_INTERPRET);
            u.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION , RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            u.cancelAttributeChanges();
            Integer optimization2 = (Integer)u.getAttributeValue(RSoftwareResource.OPTIMIZATION);
            String epc2 = (String)u.getAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition((optimization2.equals(optimization))
                   && (epc2.equals(epc)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }


/**
cancelAttributeChanges() - When 2 changes have been made.
Verify that the changes are canceled, even when committed.
**/
    public void Var004()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, RJavaTest.jarFilePath_);
            Integer optimization = (Integer)u.getAttributeValue(RSoftwareResource.OPTIMIZATION);
            String epc = (String)u.getAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            u.setAttributeValue(RSoftwareResource.OPTIMIZATION, RSoftwareResource.OPTIMIZATION_10);
            u.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u.cancelAttributeChanges();
            u.commitAttributeChanges();
            Integer optimization2 = (Integer)u.getAttributeValue(RSoftwareResource.OPTIMIZATION);
            String epc2 = (String)u.getAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition((optimization2.equals(optimization))
                   && (epc2.equals(epc)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }


/**
cancelAttributeChanges() - Verify that a ResourceEvent is fired.
**/
    public void Var005()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
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
commitAttributeChanges() - No system or path set.
**/
    public void Var006()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            u.commitAttributeChanges();
            succeeded();
        }
        catch(Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
commitAttributeChanges() - When the connection is bogus.
**/
    public void Var007()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            AS400 system = new AS400("Toolbox", "is", "cool");
            system.setGuiAvailable(false);
            RSoftwareResource u = new RSoftwareResource(system, "ClifNock");
            u.setAttributeValue(RSoftwareResource.LICENSED_INTERNAL_CODE_OPTIONS, "AllowInlining");
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
        */
    }



/**
commitAttributeChanges() - When the path does not exist.
**/
    public void Var008()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, "NOTEXIST");
            u.setAttributeValue(RSoftwareResource.LICENSED_INTERNAL_CODE_OPTIONS, "AllowInlining");
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
        */
    }



/**
commitAttributeChanges() - When the software reosurce does not exist.
**/
    public void Var009()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, RJavaTest.classFilePathNoJvapgm_);
            u.setAttributeValue(RSoftwareResource.LICENSED_INTERNAL_CODE_OPTIONS, "AllowInlining");
            u.commitAttributeChanges();
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
        */
    }



/**
commitAttributeChanges() - Should do nothing when no change has been made.
**/
    public void Var010()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            u.commitAttributeChanges();
            String loadType2 = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition(loadType2.equals(loadType));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
commitAttributeChanges() - Should commit the change when 2 changes have been made.
**/
    public void Var011()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            Integer optimization = (Integer)u.getAttributeValue(RSoftwareResource.OPTIMIZATION);
            String epc = (String)u.getAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            u.setAttributeValue(RSoftwareResource.OPTIMIZATION, RSoftwareResource.OPTIMIZATION_20);
            u.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            u.commitAttributeChanges();
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            Integer optimization2 = (Integer)u2.getAttributeValue(RSoftwareResource.OPTIMIZATION);
            String epc2 = (String)u2.getAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition((optimization2.equals(RSoftwareResource.OPTIMIZATION_20))
                   && (epc2.equalsIgnoreCase(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }


/**
commitAttributeChanges() - Verify that a ResourceEvent is fired.
**/
    public void Var012()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
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
    public void Var013()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            boolean found = false;
            // I did not hardcode an exact length...otherwise, we
            // have to change this every time we add a property.
            assertCondition((amd.length > 5) 
                   && (verifyAttributeMetaData(amd, RSoftwareResource.LEVEL, String.class, true, 0, null, false, false))
                   && (verifyAttributeMetaData(amd, RSoftwareResource.LOAD_TYPE, String.class, true, 2, null, true, false))
                   && (verifyAttributeMetaData(amd, RSoftwareResource.SYMBOLIC_LOAD_STATE, String.class, true, 6, null, true, false)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeMetaData() with 1 parameter - Pass null.
**/
    public void Var014()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getAttributeMetaData() with 1 parameter - Pass an invalid attribute ID.
**/
    public void Var015()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(new Date());
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getAttributeMetaData() with 1 parameter - Pass the first attribute ID.
**/
    public void Var016()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.LEVEL );
            assertCondition(verifyAttributeMetaData(amd, RSoftwareResource.LEVEL , String.class, true, 0, null, false, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
getAttributeMetaData() with 1 parameter - Pass a middle attribute ID.
**/
    public void Var017()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.LOAD_TYPE );
            assertCondition(verifyAttributeMetaData(amd, RSoftwareResource.LOAD_TYPE, String.class, true, 2, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
getAttributeMetaData() with 1 parameter - Pass the last attribute ID.
**/
    public void Var018()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            ResourceMetaData amd = u.getAttributeMetaData(RSoftwareResource.SYMBOLIC_LOAD_STATE);
            assertCondition(verifyAttributeMetaData(amd, RSoftwareResource.SYMBOLIC_LOAD_STATE, String.class, true, 6, null, true, false));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeMetaData() with 1 parameter - Try each of them.
**/
    public void Var019()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
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
    public void Var020()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            Object value = u.getAttributeUnchangedValue(RSoftwareResource.LOAD_TYPE);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
getAttributeUnchangedValue() - Pass null.
**/
    public void Var021()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            Object value = u.getAttributeUnchangedValue(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getAttributeUnchangedValue() - Pass an invalid attribute ID.
**/
    public void Var022()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            Object value = u.getAttributeUnchangedValue(new AS400());
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getAttributeUnchangedValue() - Pass an attribute ID, whose value has not been referenced.
**/
    public void Var023()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            Object value = u.getAttributeUnchangedValue(RSoftwareResource.LOAD_TYPE);
            assertCondition(((String)value).equals(RSoftwareResource.LOAD_TYPE_CODE));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeUnchangedValue() - Pass an attribute ID, whose value has been referenced,
but not changed.
**/
    public void Var024()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            Object value = u.getAttributeUnchangedValue(RSoftwareResource.LOAD_TYPE);
            assertCondition(value.equals(RSoftwareResource.LOAD_TYPE_CODE));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed,
but not committed.
**/
    public void Var025()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            u2.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u2.commitAttributeChanges();

            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            Object value = u.getAttributeUnchangedValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_NONE));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }



/**
getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed,
not committed, but refreshed.
**/
    public void Var026()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            u2.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            u2.commitAttributeChanges();

            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u.refreshAttributeValues();
            Object value = u.getAttributeUnchangedValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }



/**
getAttributeUnchangedValue() - Pass an attribute ID, whose value has been changed
and committed.
**/
    public void Var027()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            u2.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u2.commitAttributeChanges();

            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u.commitAttributeChanges();
            Object value = u.getAttributeUnchangedValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_FULL));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }



/**
getAttributeValue() - When there is no connection.
**/
    public void Var028()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            Object value = u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
getAttributeValue() - When the connection is bogus.
**/
    public void Var029()
    {
        try {
            AS400 system = new AS400("This", "is", "bad");
            system.setGuiAvailable(false);
            RSoftwareResource u = new RSoftwareResource(system, "Friend");
            Object value = u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
getAttributeValue() - When the path does not exist.
**/
    public void Var030()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, "NOTEXIST");
            Object value = u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
getAttributeValue() - When the software resource does not exist.
**/
    public void Var031()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, "BOGUS");
            Object value = u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
getAttributeValue() - Pass null.
**/
    public void Var032()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            Object value = u.getAttributeValue(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
getAttributeValue() - Pass an invalid attribute ID.
**/
    public void Var033()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            Object value = u.getAttributeValue("Yo");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
getAttributeValue() - Pass an attribute ID, whose value has not been referenced.
**/
    public void Var034()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            Object value = u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition(value.equals(RSoftwareResource.LOAD_TYPE_CODE));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeValue() - Pass an attribute ID, whose value has been referenced,
but not changed.
**/
    public void Var035()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            Object value = u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition(value.equals(RSoftwareResource.LOAD_TYPE_CODE));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getAttributeValue() - Pass an attribute ID, whose value has been changed,
but not committed.
**/
    public void Var036()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            u2.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u2.commitAttributeChanges();

            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            Object value = u.getAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }



/**
getAttributeValue() - Pass an attribute ID, whose value has been changed,
not committed, but refreshed.
**/
    public void Var037()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            u2.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u2.commitAttributeChanges();

            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            u.refreshAttributeValues();
            Object value = u.getAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }



/**
getAttributeValue() - Pass an attribute ID, whose value has been changed
and committed.
**/
    public void Var038()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            u2.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u2.commitAttributeChanges();

            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u.commitAttributeChanges();
            Object value = u.getAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(value.equals(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_FULL));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }



/**
getAttributeValue() - Pass every attribute ID.
**/
    public void Var039()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            ResourceMetaData[] amd = u.getAttributeMetaData();
            boolean success = true;
            for(int i = 0; i < amd.length; ++i) {
                // System.out.println("Getting attribute " + amd[i] + ".");
                Object value = u.getAttributeValue(amd[i].getID());
                Class valueClass = value.getClass();
                Class type = amd[i].getType();

                // Validate the type.
                if (amd[i].areMultipleAllowed()) {
                    if (!valueClass.isArray()) {
                        System.out.println("Error getting attribute " + amd[i] + ".");
                        System.out.println("Type array mismatch: " + valueClass + " is not an array, "
                                           + "but multiple values are allowed.");
                        success = false;
                    }
                    else {
                        Class componentType = valueClass.getComponentType();
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
                        for(int j = 0; (j < possibleValues.length) && (found == false); ++j)
                            if (possibleValues[j].equals(value))
                                found = true;

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
    public void Var040()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            boolean pending = u.hasUncommittedAttributeChanges(RSoftwareResource.LOAD_TYPE);
            assertCondition(pending == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass null.
**/
    public void Var041()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            boolean pending = u.hasUncommittedAttributeChanges(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass an invalid attribute ID.
**/
    public void Var042()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            boolean pending = u.hasUncommittedAttributeChanges("Go Toolbox");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



/**
hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has not been referenced.
**/
    public void Var043()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            boolean pending = u.hasUncommittedAttributeChanges(RSoftwareResource.LOAD_TYPE);
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
    public void Var044()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            boolean pending = u.hasUncommittedAttributeChanges(RSoftwareResource.LOAD_TYPE);
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
    public void Var045()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            u2.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u2.commitAttributeChanges();

            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            boolean pending = u.hasUncommittedAttributeChanges(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(pending == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }



/**
hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has been changed,
not committed, but refreshed.
**/
    public void Var046()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            u2.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u2.commitAttributeChanges();

            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u.refreshAttributeValues();
            boolean pending = u.hasUncommittedAttributeChanges(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(pending == true);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }



/**
hasUncommittedAttributeChanges() - Pass an attribute ID, whose value has been changed
and committed.
**/
    public void Var047()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            u2.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_ENTRY_EXIT);
            u2.commitAttributeChanges();

            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u.commitAttributeChanges();
            boolean pending = u.hasUncommittedAttributeChanges(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition(pending == false);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }


/**
refreshAttributeValues() - When no system or path set.
**/
    public void Var048()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
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
    public void Var049()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            u.refreshAttributeValues();
            String loadType2 = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition(loadType2.equals(loadType));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
refreshAttributeValues() - When 2 changes have been made for this
object, but not committed.  Verify that the changes are not cancelled.
**/
    public void Var050()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            Integer optimization = (Integer)u.getAttributeValue(RSoftwareResource.OPTIMIZATION);
            String epc = (String)u.getAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            u.setAttributeValue(RSoftwareResource.OPTIMIZATION, RSoftwareResource.OPTIMIZATION_30);
            u.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_NONE);
            u.refreshAttributeValues();
            Integer optimization2 = (Integer)u.getAttributeValue(RSoftwareResource.OPTIMIZATION);
            String epc2 = (String)u.getAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            Integer optimization2a = (Integer)u.getAttributeUnchangedValue(RSoftwareResource.OPTIMIZATION);
            String epc2a = (String)u.getAttributeUnchangedValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition((optimization2.equals(RSoftwareResource.OPTIMIZATION_30))
                   && (epc2.equals(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_NONE))
                   && (optimization2a.equals(optimization))
                   && (epc2a.equals(epc)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }


/**
refreshAttributeValues() - When 2 changes have been made for another
RSoftwareResource object.  Verify that the changes are reflected.
**/
    public void Var051()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            Integer optimization = (Integer)u.getAttributeValue(RSoftwareResource.OPTIMIZATION);
            String epc = (String)u.getAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);

            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            u2.setAttributeValue(RSoftwareResource.OPTIMIZATION, RSoftwareResource.OPTIMIZATION_40);
            u2.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u2.commitAttributeChanges();

            u.refreshAttributeValues();
            Integer optimization2 = (Integer)u.getAttributeValue(RSoftwareResource.OPTIMIZATION);
            String epc2 = (String)u.getAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition((optimization2.equals(RSoftwareResource.OPTIMIZATION_40))
                   && (epc2.equalsIgnoreCase(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_FULL)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }


/**
refreshAttributeValues() - Verify that a ResourceEvent is fired.
**/
    public void Var052()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
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
    public void Var053()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource();
            u.setAttributeValue(RSoftwareResource.LICENSED_INTERNAL_CODE_OPTIONS, "AllowInlining");
            succeeded();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }



/**
setAttributeValue() - Pass null for the attribute ID.
**/
    public void Var054()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
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
    public void Var055()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.LOAD_TYPE, null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
        */
    }



/**
setAttributeValue() - Pass an invalid attribute ID.
**/
    public void Var056()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
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
    public void Var057()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.LOAD_TYPE, RSoftwareResource.LOAD_TYPE_CODE);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "com.ibm.as400.resource.ResourceException");
        }
    }



/**
setAttributeValue() - Pass a value which is the wrong type.
**/
    public void Var058()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.LICENSED_INTERNAL_CODE_OPTIONS, new Integer(4));
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
        */
    }



/**
setAttributeValue() - Pass a value which is not one of the possible values.
**/
    public void Var059()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.OPTIMIZATION, new Integer(33));
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
        */
    }



/**
setAttributeValue() - Pass an valid attribute ID, commit and verify.
**/
    public void Var060()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.OPTIMIZATION, RSoftwareResource.OPTIMIZATION_30);
            u.commitAttributeChanges();
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            Object value = u2.getAttributeValue(RSoftwareResource.OPTIMIZATION);
            u.setAttributeValue(RSoftwareResource.OPTIMIZATION,RSoftwareResource.OPTIMIZATION_40);
            u.commitAttributeChanges();
            u2.refreshAttributeValues();
            Object value2 = u2.getAttributeValue(RSoftwareResource.OPTIMIZATION);
            assertCondition((value.equals(RSoftwareResource.OPTIMIZATION_30))
                   && (value2.equals(RSoftwareResource.OPTIMIZATION_40)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }



/**
setAttributeValue() - Pass an valid attribute ID, set twice.  Commit and verify
that the second takes effect.
**/
    public void Var061()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.OPTIMIZATION, RSoftwareResource.OPTIMIZATION_INTERPRET);
            u.setAttributeValue(RSoftwareResource.OPTIMIZATION, RSoftwareResource.OPTIMIZATION_10);
            u.commitAttributeChanges();
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            Object value = u2.getAttributeValue(RSoftwareResource.OPTIMIZATION);
            assertCondition(value.equals(RSoftwareResource.OPTIMIZATION_10));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }



/**
setAttributeValue() - Pass an valid attribute ID, set and committed twice.  Verify
that the second takes effect.
**/
    public void Var062()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.OPTIMIZATION, RSoftwareResource.OPTIMIZATION_20);
            u.commitAttributeChanges();
            u.setAttributeValue(RSoftwareResource.OPTIMIZATION, RSoftwareResource.OPTIMIZATION_30);
            u.commitAttributeChanges();
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            Object value = u2.getAttributeValue(RSoftwareResource.OPTIMIZATION);
            assertCondition(value.equals(RSoftwareResource.OPTIMIZATION_30));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }



/**
setAttributeValue() - Set 2 attributes the are set using the same CL
command.  Commit and verify that the both take effect.
**/
    public void Var063()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setAttributeValue(RSoftwareResource.OPTIMIZATION, RSoftwareResource.OPTIMIZATION_40);
            u.setAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION, RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_FULL);
            u.commitAttributeChanges();
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            Object value1 = u2.getAttributeValue(RSoftwareResource.OPTIMIZATION);
            Object value2 = u2.getAttributeValue(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION);
            assertCondition((value1.equals(RSoftwareResource.OPTIMIZATION_40))
                   && (value2.equals(RSoftwareResource.ENABLE_PERFORMANCE_COLLECTION_FULL)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }



/**
setAttributeValue() - Verify that a ResourceEvent is fired.
**/
    public void Var064()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            UserTest.ResourceListener_ rl = new UserTest.ResourceListener_();
            u.addResourceListener(rl);
            u.setAttributeValue(RSoftwareResource.OPTIMIZATION, RSoftwareResource.OPTIMIZATION_INTERPRET);
            assertCondition ((rl.eventCount_ == 1)
                    && (rl.event_.getID() == ResourceEvent.ATTRIBUTE_VALUE_CHANGED)
                    && (rl.event_.getAttributeID() == RSoftwareResource.OPTIMIZATION)
                    && (rl.event_.getValue().equals(RSoftwareResource.OPTIMIZATION_INTERPRET)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }



/**
setAttributeValue() - Pass every attribute ID.
**/
    public void Var065()
    {
        // There are no changeable attributes in this resource!
        notApplicable();
        /*
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            RSoftwareResource u2 = new RSoftwareResource(systemObject_, JC1ProductID_);
            ResourceMetaData[] amd = u.getAttributeMetaData();
            boolean success = true;
            for(int i = 0; i < amd.length; ++i) {
                // Don't bother with the lic options, since the semantics are a bit
                // unpredictable.
                if ((! amd[i].isReadOnly()) && (amd[i].getID() != RSoftwareResource.LICENSED_INTERNAL_CODE_OPTIONS)) {
                    // System.out.println("Setting attribute " + amd[i] + ".");
                    Object originalValue = u.getAttributeValue(amd[i].getID());

                    // First, just try setting the value to what it is already equal.
                    u.setAttributeValue(amd[i].getID(), originalValue);
                    u.commitAttributeChanges();

                    u.setAttributeValue(amd[i].getID(), originalValue);
                }
            }
            u2.commitAttributeChanges();

            assertCondition(success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
        */
    }



}




